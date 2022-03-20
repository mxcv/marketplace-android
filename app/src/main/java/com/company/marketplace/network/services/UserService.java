package com.company.marketplace.network.services;

import com.company.marketplace.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService {

	@GET("users")
	Call<User> getUser();

	@POST("users")
	Call<Integer> postUser(@Body User user);
}
