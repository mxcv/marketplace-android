package com.company.marketplace.network.repositories;

import com.company.marketplace.models.Category;
import com.company.marketplace.network.responses.ResponseListener;

import java.util.List;

public interface CategoryRepository {

	void getCategories(ResponseListener<List<Category>> responseListener);
}
