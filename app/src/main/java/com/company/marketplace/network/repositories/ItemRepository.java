package com.company.marketplace.network.repositories;

import com.company.marketplace.models.Item;
import com.company.marketplace.models.PageInput;
import com.company.marketplace.models.PageOutput;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.ResponseListener;

import java.util.List;

public interface ItemRepository {

	void getMyItems(PageOutput pageOutput,
					ResponseListener<PageInput> responseListener);

	void addItem(Item item,
				 ResponseListener<Void> responseListener,
				 BadRequestErrorListener badRequestErrorListener);

	void removeItem(int id,
					ResponseListener<Void> responseListener,
					BadRequestErrorListener badRequestErrorListener);
}
