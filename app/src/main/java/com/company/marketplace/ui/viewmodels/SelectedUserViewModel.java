package com.company.marketplace.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

public class SelectedUserViewModel extends AndroidViewModel {

	private final MutableLiveData<User> user;

	public SelectedUserViewModel(@NonNull Application application) {
		super(application);
		this.user = new MutableLiveData<>();
	}

	public LiveData<User> getUser() {
		return user;
	}

	public void select(User user) {
		new MarketplaceRepositoryFactory(getApplication())
			.createUserRepository()
			.getUser(user.getId(), fullUser -> {
				user.setFeedbackStatistics(fullUser.getFeedbackStatistics());
				this.user.setValue(user);
			});
	}
}
