package com.company.marketplace.network.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import com.company.marketplace.models.AccessRefreshJwt;
import com.company.marketplace.network.jwt.FileJwtRepository;
import com.company.marketplace.network.jwt.JwtRepository;
import com.company.marketplace.network.jwt.JwtType;

import java.util.Objects;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

	private static NetworkService instance;
	private final Retrofit retrofit;

	private NetworkService(Context context) {
		String baseUrl = "";
		try {
			baseUrl = context
				.getPackageManager()
				.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
				.metaData
				.get("api_url")
				.toString();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		JwtRepository jwtRepository = new FileJwtRepository(context.getApplicationContext());
		OkHttpClient client = getUnsafeOkHttpClient()
			.addInterceptor(chain -> {
				String token = jwtRepository.getToken(JwtType.ACCESS);
				return chain.proceed(token == null ? chain.request() : chain.request()
					.newBuilder()
					.addHeader("Authorization", getAuthHeader(token))
					.build()
				);
			}).authenticator((route, response) -> {
				Response<AccessRefreshJwt> refreshResponse = NetworkService.getInstance()
					.getTokenService()
					.refresh(jwtRepository.getTokens())
					.execute();
				if (refreshResponse.isSuccessful()) {
					AccessRefreshJwt jwt = refreshResponse.body();
					jwtRepository.setTokens(jwt);
					return response.request()
						.newBuilder()
						.header("Authorization", getAuthHeader(Objects.requireNonNull(jwt).getAccessToken()))
						.build();
				}
				return null;
			}).build();

		retrofit = new Retrofit.Builder()
			.baseUrl(baseUrl)
			.addConverterFactory(GsonConverterFactory.create())
			.client(client)
			.build();
	}

	public static synchronized void initialize(Context context) {
		if (instance == null)
			instance = new NetworkService(context);
	}
	public static synchronized NetworkService getInstance() {
		return instance;
	}

	public UserService getUserService() {
		return retrofit.create(UserService.class);
	}
	public TokenService getTokenService() {
		return retrofit.create(TokenService.class);
	}
	public CurrencyService getCurrencyService() {
		return retrofit.create(CurrencyService.class);
	}
	public ItemService getItemService() {
		return retrofit.create(ItemService.class);
	}

	private String getAuthHeader(String token) {
		return token == null ? null : "Bearer " + token;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({"CustomX509TrustManager", "TrustAllX509TrustManager"})
	private OkHttpClient.Builder getUnsafeOkHttpClient() {
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
					}

					@Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
					}

					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return new java.security.cert.X509Certificate[]{};
					}
				}
			};

			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.sslSocketFactory(sslSocketFactory);
			builder.hostnameVerifier((hostname, session) -> true);

			return builder;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
