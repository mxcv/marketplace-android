package com.company.marketplace.repositories.response;

public interface ResponseListener<T> {
	void onResponse(T response);
}
