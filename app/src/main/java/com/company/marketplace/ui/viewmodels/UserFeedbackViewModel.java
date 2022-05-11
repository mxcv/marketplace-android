package com.company.marketplace.ui.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.Feedback;
import com.company.marketplace.models.Page;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

import java.util.List;

public class UserFeedbackViewModel extends AndroidViewModel {

	private static final int PAGE_SIZE = 20;
	private final MutableLiveData<List<Feedback>> feedback;
	private final MutableLiveData<Page<Feedback>> lastPage;
	private boolean isLoading;

	public UserFeedbackViewModel(@NonNull Application application) {
		super(application);
		feedback = new MutableLiveData<>();
		lastPage = new MutableLiveData<>();
	}

	public LiveData<List<Feedback>> getFeedback() {
		return feedback;
	}

	public MutableLiveData<Page<Feedback>> getLastPage() {
		return lastPage;
	}

	public synchronized void loadNextPage(User user) {
		if (!isLoading && hasMorePages()) {
			isLoading = true;
			new MarketplaceRepositoryFactory(getApplication())
				.createFeedbackRepository()
				.getFeedback(
					user.getId(),
					lastPage.getValue() == null ? 1 : lastPage.getValue().getPageIndex() + 1,
					PAGE_SIZE,
					page -> {
						Log.d("feedback", "Feedback loaded: " + page.getItems().size());
						Log.d("feedback", String.format("Page: %d/%d", page.getPageIndex(), page.getTotalPages()));
						if (feedback.getValue() == null)
							feedback.setValue(page.getItems());
						else {
							feedback.getValue().addAll(page.getItems());
							feedback.setValue(feedback.getValue());
						}
						lastPage.setValue(page);
						isLoading = false;
					});
		}
	}

	private boolean hasMorePages() {
		Page<Feedback> lastPageValue = lastPage.getValue();
		return lastPageValue == null || lastPageValue.getPageIndex() != lastPageValue.getTotalPages();
	}
}
