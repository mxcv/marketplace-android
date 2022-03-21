package com.company.marketplace.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.Page;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

public class ItemsViewModel extends AndroidViewModel {

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
		this.page.setValue(null);
	}

	public synchronized void loadMoreItems(ItemRequest itemRequest) {
		if (!isLoading && (page.getValue() == null || page.getValue().getLeftCount() != 0)) {
			isLoading = true;
			if (page.getValue() != null)
				itemRequest.setSkipCount(page.getValue().getItems().size());

			new MarketplaceRepositoryFactory(getApplication())
				.createItemRepository()
				.getItems(itemRequest, page -> {
					if (this.page.getValue() != null)
						page.getItems().addAll(0, this.page.getValue().getItems());
					this.page.setValue(page);
					isLoading = false;
				});
		}
	}
}
