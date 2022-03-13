package com.company.marketplace.network.services;

import com.company.marketplace.models.Item;
import com.company.marketplace.models.Page;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItemService {

	@GET("items")
	Call<Page> getItems(@Query("userId") Integer userId,
						@Query("skipCount") Integer skipCount,
						@Query("takeCount") Integer takeCount);

	@POST("items")
	Call<Integer> postItem(@Body Item item);

	@DELETE("items/{id}")
	Call<Void> deleteItem(@Path("id") int id);
}
