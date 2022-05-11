package com.company.marketplace.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.Page;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

import java.util.List;

public class ItemsViewModel extends AndroidViewModel {

	private static final int PAGE_SIZE = 20;
	private final MutableLiveData<List<Item>> items;
	private final MutableLiveData<Page<Item>> lastPage;
	private boolean isLoading;

	public ItemsViewModel(@NonNull Application application) {
		super(application);
		items = new MutableLiveData<>();
		lastPage = new MutableLiveData<>();
	}

	public LiveData<List<Item>> getItems() {
		return items;
	}

	public LiveData<Page<Item>> getLastPage() {
		return lastPage;
	}

	public void clearItems() {
		Log.d("items", "Loaded items were cleared.");
		items.setValue(null);
		lastPage.setValue(null);
	}

	public synchronized void loadNextPage(ItemRequest itemRequest) {
		if (!isLoading && hasMorePages()) {
			isLoading = true;
			if (lastPage.getValue() != null)
				itemRequest.setPageIndex(lastPage.getValue().getPageIndex() + 1);
			itemRequest.setPageSize(PAGE_SIZE);

			new MarketplaceRepositoryFactory(getApplication())
				.createItemRepository()
				.getItems(itemRequest, page -> {
					Log.d("items", "Items loaded: " + page.getItems().size());
					Log.d("items", String.format("Page: %d/%d", page.getPageIndex(), page.getTotalPages()));

					if (items.getValue() == null)
						items.setValue(page.getItems());
					else {
						items.getValue().addAll(page.getItems());
						items.setValue(items.getValue());
					}
					lastPage.setValue(page);
					isLoading = false;
				});
		}
	}

	private boolean hasMorePages() {
		Page<Item> lastPageValue = lastPage.getValue();
		return lastPageValue == null || lastPageValue.getPageIndex() != lastPageValue.getTotalPages();
	}
}
