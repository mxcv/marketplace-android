package com.company.marketplace.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.Page;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

public class ItemsViewModel extends AndroidViewModel {

	private static final int PAGE_SIZE = 20;

	private final MutableLiveData<Page> page;
	private boolean isLoading;

	public ItemsViewModel(@NonNull Application application) {
		super(application);
		this.page = new MutableLiveData<>();
	}

	public LiveData<Page> getPage() {
		return page;
	}

	public void clearPage() {
		Log.d("items", "Loaded items were cleared.");
		this.page.setValue(null);
	}

	public synchronized void loadMoreItems(ItemRequest itemRequest) {
		if (!isLoading && (page.getValue() == null || page.getValue().getPageIndex() != page.getValue().getTotalPages())) {
			isLoading = true;
			if (page.getValue() != null)
				itemRequest.setPageIndex(page.getValue().getPageIndex() + 1);
			itemRequest.setPageSize(PAGE_SIZE);

			new MarketplaceRepositoryFactory(getApplication())
				.createItemRepository()
				.getItems(itemRequest, page -> {
					if (this.page.getValue() != null)
						page.getItems().addAll(0, this.page.getValue().getItems());
					Log.d("items", "Items loaded: " + page.getItems().size());
					Log.d("items", "Current page: " + page.getPageIndex());
					Log.d("items", "Total pages: " + page.getTotalPages());
					this.page.setValue(page);
					isLoading = false;
				});
		}
	}
}
