package com.company.marketplace.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.Feedback;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.FeedbackRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

import java.util.List;

public class UserFeedbackViewModel extends AndroidViewModel {

	private MutableLiveData<List<Feedback>> items;

	public UserFeedbackViewModel(@NonNull Application application) {
		super(application);
	}

	public LiveData<List<Feedback>> getItems(User user) {
		if (items == null) {
			items = new MutableLiveData<>();
			loadItems(user);
		}
		return items;
	}

	private void loadItems(User user) {
		FeedbackRepository feedbackRepository = new MarketplaceRepositoryFactory(getApplication())
			.createFeedbackRepository();
		feedbackRepository.getFeedback(user.getId(), 1, 20, page -> {
			items.setValue(page.getItems());
		});
	}
}
