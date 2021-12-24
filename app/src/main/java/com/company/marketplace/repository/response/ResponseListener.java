package com.company.marketplace.repository.response;

public interface ResponseListener<T> {
	void onResponse(T response);
}
