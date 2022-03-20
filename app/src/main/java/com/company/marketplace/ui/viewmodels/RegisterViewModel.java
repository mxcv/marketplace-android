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

public class RegisterViewModel extends AndroidViewModel {

	private final MutableLiveData<Integer> userId;

	public RegisterViewModel(@NonNull Application application) {
		super(application);
		userId = new MutableLiveData<>();
	}

	public void register(User user) {
		new MarketplaceRepositoryFactory(getApplication())
			.createUserRepository()
			.addUser(user,
				userId::setValue,
				() -> Toast.makeText(getApplication(), R.string.registration_error, Toast.LENGTH_LONG).show());
	}

	public LiveData<Integer> getUserId() {
		return userId;
	}
}
