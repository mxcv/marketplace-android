package com.company.marketplace.network.repositories;

import com.company.marketplace.models.Country;
import com.company.marketplace.models.ImageOutput;
import com.company.marketplace.models.User;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.ResponseListener;

import java.util.List;

public interface UserRepository {

	void login(String email, String password,
			   ResponseListener<Void> responseListener,
			   BadRequestErrorListener badRequestErrorListener);

	void logout();

	void getUser(ResponseListener<User> responseListener);

	void addUser(User user,
				 ResponseListener<Void> responseListener,
				 BadRequestErrorListener badRequestErrorListener);

	void getCountries(ResponseListener<List<Country>> responseListener);

	void setUserImage(ImageOutput image,
					  ResponseListener<Void> responseListener);
}
