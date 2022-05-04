package com.company.marketplace.ui.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.R;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;

public class LoginViewModel extends AndroidViewModel {

	private final MutableLiveData<User> user;

	public LoginViewModel(@NonNull Application application) {
		super(application);
		user = new MutableLiveData<>();
	}

	public void login(User inputUser) {
		UserRepository userRepository = new MarketplaceRepositoryFactory(getApplication()).createUserRepository();
		userRepository.login(inputUser,
			ignored -> userRepository.getCurrentUser(user::setValue),
			() -> Toast.makeText(getApplication(), R.string.login_error, Toast.LENGTH_SHORT).show());
	}

	public LiveData<User> getUser() {
		return user;
	}
}
