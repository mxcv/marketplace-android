package com.company.marketplace.network.responses;

public interface ResponseListener<T> {
	void onResponse(T response);
}
