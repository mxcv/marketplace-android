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
import com.company.marketplace.repositories.response.BadRequestErrorListener;
import com.company.marketplace.repositories.response.ProviderErrorListener;
import com.company.marketplace.repositories.response.ResponseListener;
import com.company.marketplace.repositories.response.UnauthorizedErrorListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class MarketplaceRepository implements UserRepository {

	private static final String ACCESS_TOKEN_PATH = "access_token.txt";

	private final Context context;
	private final RequestQueue requestQueue;
	private final String apiUrl;
	private final Gson gson;
	private final UnauthorizedErrorListener unauthorizedErrorListener;
	private final ProviderErrorListener providerErrorListener;

	public MarketplaceRepository(Context context,
								 UnauthorizedErrorListener unauthorizedErrorListener,
								 ProviderErrorListener providerErrorListener) {

		this.context = context;
		this.unauthorizedErrorListener = unauthorizedErrorListener;
		this.providerErrorListener = providerErrorListener;
		requestQueue = Volley.newRequestQueue(context);
		gson = new Gson();
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
					setAccessToken(String.valueOf(root.get("accessToken")));
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
		setAccessToken(null);
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


	}

	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + getAccessToken());
		return headers;
	}

	private String getAccessToken() {
		try (FileInputStream stream = context.openFileInput(ACCESS_TOKEN_PATH)) {
			byte[] b = new byte[stream.available()];
			stream.read(b);
			return new String(b);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void setAccessToken(String accessToken) {
		if (accessToken == null) {
			new File(ACCESS_TOKEN_PATH).delete();
		}
		else {
			try (FileOutputStream stream = context.openFileOutput(ACCESS_TOKEN_PATH, Context.MODE_PRIVATE)) {
				stream.write(accessToken.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
