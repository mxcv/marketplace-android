package com.company.marketplace.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemsViewModel extends AndroidViewModel {

	private static final int TAKE_COUNT = 20;
	private final MutableLiveData<List<Item>> items;
	private Integer leftCount;

	public ItemsViewModel(@NonNull Application application) {
		super(application);
		items = new MutableLiveData<>(new ArrayList<>());
	}

	public LiveData<List<Item>> getItems() {
		return items;
	}

	public Integer getLeftItemsCount() {
		return leftCount;
	}

	public void clearItems() {
		items.setValue(new ArrayList<>());
	}

	public void loadMoreItems(ItemRequest itemRequest) {
		itemRequest.setSkipCount(Objects.requireNonNull(items.getValue()).size());
		itemRequest.setTakeCount(TAKE_COUNT);
		new MarketplaceRepositoryFactory(getApplication()).createItemRepository().getItems(itemRequest, page -> {
			leftCount = page.getLeftCount();
			items.getValue().addAll(page.getItems());
			items.setValue(items.getValue());
		});
	}
}
