package com.company.marketplace.network.services;

import com.company.marketplace.models.Item;
import com.company.marketplace.models.Page;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItemService {

	@GET("items")
	Call<Page<Item>> getItems(@Query("query") String query,
						@Query("minPrice") BigDecimal minPrice,
						@Query("maxPrice") BigDecimal maxPrice,
						@Query("currencyId") Integer currencyId,
						@Query("categoryId") Integer categoryId,
						@Query("countryId") Integer countryId,
						@Query("regionId") Integer regionId,
						@Query("cityId") Integer cityId,
						@Query("userId") Integer userId,
						@Query("sortTypeId") Integer sortTypeId,
						@Query("pageIndex") Integer pageIndex,
						@Query("pageSize") Integer pageSize);

	@POST("items")
	Call<Integer> postItem(@Body Item item);

	@DELETE("items/{id}")
	Call<Void> deleteItem(@Path("id") int id);
}
