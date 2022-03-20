package com.company.marketplace.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;

public class LoginViewModel extends AndroidViewModel {

	private final MutableLiveData<User> user;
	private final MutableLiveData<Void> error;

	public LoginViewModel(@NonNull Application application) {
		super(application);
		user = new MutableLiveData<>();
		error = new MutableLiveData<>();
	}

	public void login(User inputUser) {
		UserRepository userRepository = new MarketplaceRepositoryFactory(getApplication()).createUserRepository();
		userRepository.login(inputUser,
			ignored -> userRepository.getUser(user::setValue),
			() -> error.setValue(null));
	}

	public LiveData<User> getUser() {
		return user;
	}

	public LiveData<Void> getError() {
		return error;
	}
}
