package com.company.marketplace.network.services;

import com.company.marketplace.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface UserService {

	@GET("users")
	Call<User> getUser();

	@PUT("users")
	Call<Void> createUser(@Body User user);
}
