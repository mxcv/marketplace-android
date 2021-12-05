package com.company.marketplace.provider.response;

public interface ResponseListener<T> {
	void onResponse(T response);
}
