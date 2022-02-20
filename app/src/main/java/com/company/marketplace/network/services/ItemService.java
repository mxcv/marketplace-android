package com.company.marketplace.network.services;

import com.company.marketplace.models.Item;
import com.company.marketplace.models.PageInput;
import com.company.marketplace.models.PageOutput;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ItemService {

	@POST("items/my")
	Call<PageInput> getMyItems(@Body PageOutput pageOutput);

	@PUT("items")
	Call<Void> putItem(@Body Item item);

	@DELETE("items/{id}")
	Call<Void> deleteItem(@Path("id") int id);
}
