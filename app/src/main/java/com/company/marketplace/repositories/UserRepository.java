package com.company.marketplace.repositories;

import com.company.marketplace.models.User;
import com.company.marketplace.repositories.response.BadRequestErrorListener;
import com.company.marketplace.repositories.response.ResponseListener;

public interface UserRepository {

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
