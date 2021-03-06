package com.company.marketplace.network.services;

import android.content.Context;
import android.content.pm.PackageManager;

import com.company.marketplace.models.AccessRefreshJwt;
import com.company.marketplace.models.JwtType;
import com.company.marketplace.network.repositories.JwtRepository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

	private static NetworkService instance;
	private final Context context;
	private final Retrofit retrofit;
	private String baseUrl;

	private NetworkService(Context context) {
		this.context = context.getApplicationContext();
		OkHttpClient client = new OkHttpClient.Builder()
			.readTimeout(20, TimeUnit.SECONDS)
			.connectTimeout(20, TimeUnit.SECONDS)
			.addInterceptor(chain -> {
				String token = JwtRepository.get().getToken(JwtType.ACCESS);
				return chain.proceed(token == null ? chain.request() : chain.request()
					.newBuilder()
					.addHeader("Accept-Language", this.context.getResources().getConfiguration().getLocales().toLanguageTags())
					.addHeader("Authorization", getAuthHeader(token))
					.build()
				);
			}).authenticator((route, response) -> {
				Response<AccessRefreshJwt> refreshResponse = NetworkService.get()
					.getTokenService()
					.refresh(JwtRepository.get().getTokens())
					.execute();
				if (refreshResponse.isSuccessful()) {
					AccessRefreshJwt jwt = refreshResponse.body();
					JwtRepository.get().setTokens(jwt);
					return response.request()
						.newBuilder()
						.header("Authorization", getAuthHeader(Objects.requireNonNull(jwt).getAccessToken()))
						.build();
				}
				return null;
			}).build();

		retrofit = new Retrofit.Builder()
			.baseUrl(Objects.requireNonNull(getBaseUrl()) + "/api/")
			.addConverterFactory(GsonConverterFactory.create())
			.client(client)
			.build();
	}

	public static synchronized void initialize(Context context) {
		if (instance == null)
			instance = new NetworkService(context);
	}

	public static synchronized NetworkService get() {
		return instance;
	}

	public CategoryService getCategoryService() {
		return retrofit.create(CategoryService.class);
	}
	public CurrencyService getCurrencyService() {
		return retrofit.create(CurrencyService.class);
	}
	public FeedbackService getFeedbackService() {
		return retrofit.create(FeedbackService.class);
	}
	public ImageService getImageService() {
		return retrofit.create(ImageService.class);
	}
	public ItemService getItemService() {
		return retrofit.create(ItemService.class);
	}
	public LocationService getLocationService() {
		return retrofit.create(LocationService.class);
	}
	public TokenService getTokenService() {
		return retrofit.create(TokenService.class);
	}
	public UserService getUserService() {
		return retrofit.create(UserService.class);
	}

	public String getBaseUrl() {
		if (baseUrl == null) {
			try {
				baseUrl = context
					.getPackageManager()
					.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
					.metaData
					.get("base_url")
					.toString();
			}
			catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		return baseUrl;
	}

	private String getAuthHeader(String token) {
		return token == null ? null : "Bearer " + token;
	}
}
