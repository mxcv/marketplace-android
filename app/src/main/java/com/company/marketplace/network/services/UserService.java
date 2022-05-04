package com.company.marketplace.network.services;

import com.company.marketplace.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

	@GET("users/{id}")
	Call<User> getUser(int id);

	@GET("users")
	Call<User> getCurrentUser();

	@POST("users")
	Call<Integer> postUser(@Body User user);
}
