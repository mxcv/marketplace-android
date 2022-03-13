package com.company.marketplace.network.repositories;

import com.company.marketplace.models.Category;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.ImageOutput;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.Page;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.ResponseListener;

import java.util.List;

public interface ItemRepository {

	void getItems(Integer userId,
				  Integer skipCount,
				  Integer takeCount,
				  ResponseListener<Page> responseListener);

	void addItem(Item item,
				 ResponseListener<Integer> responseListener,
				 BadRequestErrorListener badRequestErrorListener);

	void removeItem(int id,
					ResponseListener<Void> responseListener,
					BadRequestErrorListener badRequestErrorListener);

	void getCurrencies(ResponseListener<List<Currency>> responseListener);

	void getCategories(ResponseListener<List<Category>> responseListener);

	void addItemImages(int itemId, List<ImageOutput> images,
					   ResponseListener<Void> responseListener);
}
