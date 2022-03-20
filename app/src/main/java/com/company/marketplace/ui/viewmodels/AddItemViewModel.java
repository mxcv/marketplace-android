package com.company.marketplace.ui.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.R;
import com.company.marketplace.models.ImageOutput;
import com.company.marketplace.models.Item;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

import java.util.List;

public class AddItemViewModel extends AndroidViewModel {

	private final MutableLiveData<Integer> itemId;
	private final MutableLiveData<List<ImageOutput>> images;

	public AddItemViewModel(@NonNull Application application) {
		super(application);
		itemId = new MutableLiveData<>();
		images = new MutableLiveData<>();
	}

	public LiveData<List<ImageOutput>> getImages() {
		return images;
	}

	public void setImages(List<ImageOutput> images) {
		this.images.setValue(images);
	}

	public void add(Item item) {
		new MarketplaceRepositoryFactory(getApplication())
			.createItemRepository()
			.addItem(item,
				itemId -> {
					if (images.getValue() == null)
						this.itemId.setValue(itemId);
					else new MarketplaceRepositoryFactory(getApplication())
						.createImageRepository()
						.addItemImages(itemId, images.getValue(), ignored -> this.itemId.setValue(itemId));
				},
				() -> Toast.makeText(getApplication(), R.string.add_item_error, Toast.LENGTH_SHORT).show());
	}

	public LiveData<Integer> getItemId() {
		return itemId;
	}
}
