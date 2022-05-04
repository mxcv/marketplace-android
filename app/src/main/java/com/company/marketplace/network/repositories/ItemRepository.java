package com.company.marketplace.network.repositories;

import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.Page;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.ResponseListener;

public interface ItemRepository {

	void getItems(ItemRequest itemRequest,
				  ResponseListener<Page<Item>> responseListener);

	void addItem(Item item,
				 ResponseListener<Integer> responseListener,
				 BadRequestErrorListener badRequestErrorListener);

	void removeItem(int id,
					ResponseListener<Void> responseListener,
					BadRequestErrorListener badRequestErrorListener);
}
