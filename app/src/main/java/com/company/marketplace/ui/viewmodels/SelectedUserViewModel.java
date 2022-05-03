package com.company.marketplace.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.company.marketplace.models.User;

public class SelectedUserViewModel extends ViewModel {

	private final MutableLiveData<User> user;

	public SelectedUserViewModel() {
		this.user = new MutableLiveData<>();
	}

	public LiveData<User> getUser() {
		return user;
	}

	public void select(User user) {
		this.user.setValue(user);
	}
}
