package com.company.marketplace.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.company.marketplace.models.Item;

public class SelectedItemViewModel extends ViewModel {

	private final MutableLiveData<Item> item;

	public SelectedItemViewModel() {
		this.item = new MutableLiveData<>();
	}

	public LiveData<Item> getItem() {
		return item;
	}

	public void select(Item item) {
		this.item.setValue(item);
	}
}
