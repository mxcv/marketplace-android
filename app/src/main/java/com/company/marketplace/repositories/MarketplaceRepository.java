package com.company.marketplace.repositories;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.company.marketplace.HttpsTrustManager;
import com.company.marketplace.models.User;
import com.company.marketplace.repositories.jwt.FileJwtRepository;
import com.company.marketplace.repositories.jwt.JwtRepository;
import com.company.marketplace.repositories.jwt.JwtType;
import com.company.marketplace.repositories.response.BadRequestErrorListener;
import com.company.marketplace.repositories.response.ProviderErrorListener;
import com.company.marketplace.repositories.response.ResponseListener;
import com.company.marketplace.repositories.response.UnauthorizedErrorListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class MarketplaceRepository implements UserRepository {

	private final RequestQueue requestQueue;
	private final String apiUrl;
	private final Gson gson;
	private final UnauthorizedErrorListener unauthorizedErrorListener;
	private final ProviderErrorListener providerErrorListener;
	private final JwtRepository jwtRepository;

	public MarketplaceRepository(Context context,
								 UnauthorizedErrorListener unauthorizedErrorListener,
								 ProviderErrorListener providerErrorListener) {

		this.unauthorizedErrorListener = unauthorizedErrorListener;
		this.providerErrorListener = providerErrorListener;
		requestQueue = Volley.newRequestQueue(context);
		gson = new Gson();
		jwtRepository = new FileJwtRepository(context);
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			apiUrl = String.valueOf(ai.metaData.get("api_url"));
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			throw new Resources.NotFoundException();
		}
	}

	@Override
	public void login(String email, String password,
						ResponseListener<Void> responseListener,
						BadRequestErrorListener badRequestErrorListener) {

		HttpsTrustManager.allowAllSSL();
		try {
			JsonObjectRequest request = new JsonObjectRequest(
				Request.Method.POST,
				apiUrl + "/users",
				new JSONObject(gson.toJson(new User(email, password))),
				response -> {
					Map<?, ?> root = gson.fromJson(response.toString(), Map.class);
					jwtRepository.setToken(JwtType.ACCESS, String.valueOf(root.get("accessToken")));
					if (responseListener != null)
						responseListener.onResponse(null);
				},
				error -> {
					if (error.networkResponse != null && error.networkResponse.statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
						if (badRequestErrorListener != null)
							badRequestErrorListener.onBadRequestError();
					}
					else if (providerErrorListener != null)
						providerErrorListener.onProviderError();
				}
			);
			requestQueue.add(request);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void logout() {
		jwtRepository.setToken(JwtType.ACCESS, null);
	}

	@Override
	public void getUser(ResponseListener<User> responseListener) {
		HttpsTrustManager.allowAllSSL();
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl + "/users", null,
			response -> {
				if (responseListener != null)
					responseListener.onResponse(gson.fromJson(response.toString(), User.class));
			},
			error -> {
				if (error.networkResponse != null && error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
					if (unauthorizedErrorListener != null)
						unauthorizedErrorListener.onUnauthorizedError();
				}
				else if (providerErrorListener != null)
					providerErrorListener.onProviderError();
			}
		) {
			@Override
			public Map<String, String> getHeaders() {
				return MarketplaceRepository.this.getHeaders();
			}
		};
		requestQueue.add(request);
	}

	@Override
	public void createUser(User user,
						   ResponseListener<Void> responseListener,
						   BadRequestErrorListener badRequestErrorListener) {

		HttpsTrustManager.allowAllSSL();
		try {
			JsonObjectRequestWithNull request = new JsonObjectRequestWithNull(
				Request.Method.PUT,
				apiUrl + "/users",
				new JSONObject(gson.toJson(user)),
				response -> {
					if (responseListener != null)
						responseListener.onResponse(null);
				},
				error -> {
					if (error.networkResponse != null && error.networkResponse.statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
						if (badRequestErrorListener != null)
							badRequestErrorListener.onBadRequestError();
					}
					else if (providerErrorListener != null)
						providerErrorListener.onProviderError();
				}
			);
			requestQueue.add(request);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + jwtRepository.getToken(JwtType.ACCESS));
		return headers;
	}
}
