package com.company.marketplace.provider;

import com.company.marketplace.models.User;
import com.company.marketplace.provider.response.BadRequestErrorListener;
import com.company.marketplace.provider.response.ResponseListener;

public interface UserProvider {

	void login(String email,
			   String password,
			   ResponseListener<Void> responseListener,
			   BadRequestErrorListener badRequestErrorListener);

	void logout();

	void getUser(ResponseListener<User> responseListener);

	void createUser(User user,
					ResponseListener<Void> responseListener,
					BadRequestErrorListener badRequestErrorListener);
}
