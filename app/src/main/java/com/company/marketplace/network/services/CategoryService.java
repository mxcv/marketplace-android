package com.company.marketplace.network.services;

import com.company.marketplace.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {

	@GET("categories")
	Call<List<Category>> getCategories();
}
