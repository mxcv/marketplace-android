package com.company.marketplace.network.services;

import com.company.marketplace.models.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ItemService {

	@GET("items/my")
	Call<List<Item>> getMyItems();

	@PUT("items")
	Call<Void> putItem(@Body Item item);

	@DELETE("items/{id}")
	Call<Void> deleteItem(@Path("id") int id);
}
