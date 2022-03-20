package com.company.marketplace.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

public class RegisterViewModel extends AndroidViewModel {

	private final MutableLiveData<Void> success;
	private final MutableLiveData<Void> error;

	public RegisterViewModel(@NonNull Application application) {
		super(application);
		success = new MutableLiveData<>();
		error = new MutableLiveData<>();
	}

	public void register(User user) {
		new MarketplaceRepositoryFactory(getApplication())
			.createUserRepository()
			.addUser(user,
				success::setValue,
				() -> error.setValue(null));
	}

	public LiveData<Void> getSuccess() {
		return success;
	}

	public LiveData<Void> getError() {
		return error;
	}
}
