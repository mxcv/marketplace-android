package com.company.marketplace.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.ItemRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

import java.util.List;

public class UserItemsViewModel extends AndroidViewModel {

	private MutableLiveData<List<Item>> items;

	public UserItemsViewModel(@NonNull Application application) {
		super(application);
	}

	public LiveData<List<Item>> getItems(User user) {
		if (items == null) {
			items = new MutableLiveData<>();
			loadItems(user);
		}
		return items;
	}

	private void loadItems(User user) {
		ItemRequest request = new ItemRequest();
		request.setUser(user);

		ItemRepository itemRepository = new MarketplaceRepositoryFactory(getApplication()).createItemRepository();
		itemRepository.getItems(request, page -> {
			for (Item item : page.getItems())
				item.setUser(user);
			items.setValue(page.getItems());
		});
	}
}
