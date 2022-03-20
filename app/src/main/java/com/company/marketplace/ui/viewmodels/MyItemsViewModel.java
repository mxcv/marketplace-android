package com.company.marketplace.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.Account;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

import java.util.List;

public class MyItemsViewModel extends AndroidViewModel {

	private MutableLiveData<List<Item>> myItems;

	public MyItemsViewModel(@NonNull Application application) {
		super(application);
	}

	public LiveData<List<Item>> getMyItems() {
		if (myItems == null) {
			myItems = new MutableLiveData<>();
			ItemRequest itemRequest = new ItemRequest();
			itemRequest.setUser(Account.get().getUser().getValue());
			new MarketplaceRepositoryFactory(getApplication())
				.createItemRepository()
				.getItems(itemRequest, items -> myItems.setValue(items.getItems()));
		}
		return myItems;
	}

	public void removeItem(int itemId) {
		new MarketplaceRepositoryFactory(getApplication())
			.createItemRepository()
			.removeItem(itemId, null, null);
	}
}
