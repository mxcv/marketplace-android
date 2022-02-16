package com.company.marketplace.network.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.NetworkErrorListener;
import com.company.marketplace.network.responses.ResponseListener;
import com.company.marketplace.network.responses.UnauthorizedErrorListener;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleCallback<T> implements Callback<T> {

	private final ResponseListener<T> responseListener;
	private final BadRequestErrorListener badRequestErrorListener;
	private final UnauthorizedErrorListener unauthorizedErrorListener;
	private final NetworkErrorListener networkErrorListener;

	public SimpleCallback(ResponseListener<T> responseListener,
						  BadRequestErrorListener badRequestErrorListener,
						  UnauthorizedErrorListener unauthorizedErrorListener,
						  NetworkErrorListener networkErrorListener) {
		this.responseListener = responseListener;
		this.badRequestErrorListener = badRequestErrorListener;
		this.unauthorizedErrorListener = unauthorizedErrorListener;
		this.networkErrorListener = networkErrorListener;
	}

	@Override
	public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
		if (response.isSuccessful()) {
			if (responseListener != null)
				responseListener.onResponse(response.body());
		}
		else {
			Log.w("network", String.valueOf(response.code()));
			switch (response.code()) {
				case HttpURLConnection.HTTP_BAD_REQUEST:
					if (badRequestErrorListener != null)
						badRequestErrorListener.onBadRequestError();
					break;
				case HttpURLConnection.HTTP_UNAUTHORIZED:
					if (unauthorizedErrorListener != null)
						unauthorizedErrorListener.onUnauthorizedError();
					break;
				default:
					if (networkErrorListener != null)
						networkErrorListener.onProviderError();
					break;
			}
		}
	}

	@Override
	public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
		t.printStackTrace();
		if (networkErrorListener != null)
			networkErrorListener.onProviderError();
	}
}
