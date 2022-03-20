package com.company.marketplace.network.repositories;

import com.company.marketplace.models.User;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.ResponseListener;

public interface UserRepository {

	void login(User user,
			   ResponseListener<Void> responseListener,
			   BadRequestErrorListener badRequestErrorListener);

	void logout(ResponseListener<Void> responseListener);

	void getUser(ResponseListener<User> responseListener);

	void addUser(User user,
				 ResponseListener<Integer> responseListener,
				 BadRequestErrorListener badRequestErrorListener);
}
