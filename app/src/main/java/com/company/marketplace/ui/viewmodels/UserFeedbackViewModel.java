package com.company.marketplace.ui.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.R;
import com.company.marketplace.models.Feedback;
import com.company.marketplace.models.Page;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

import java.util.List;

public class UserFeedbackViewModel extends AndroidViewModel {

	private static final int PAGE_SIZE = 20;
	private final MutableLiveData<List<Feedback>> feedback;
	private final MutableLiveData<Page<Feedback>> lastPage;
	private final MutableLiveData<Integer> addedFeedbackId;
	private final MutableLiveData<Void> removedFeedback;
	private MutableLiveData<Feedback> leftFeedback;
	private boolean isLoading;

	public UserFeedbackViewModel(@NonNull Application application) {
		super(application);
		feedback = new MutableLiveData<>();
		lastPage = new MutableLiveData<>();
		addedFeedbackId = new MutableLiveData<>();
		removedFeedback = new MutableLiveData<>();
	}

	public LiveData<List<Feedback>> getFeedback() {
		return feedback;
	}

	public LiveData<Page<Feedback>> getLastPage() {
		return lastPage;
	}

	public LiveData<Integer> getAddedFeedbackId() {
		return addedFeedbackId;
	}

	public LiveData<Void> getRemovedFeedback() {
		return removedFeedback;
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

	public void addFeedback(Feedback feedback) {
		if (feedback.getRate() < 1 || feedback.getRate() > 5) {
			Toast.makeText(getApplication(), R.string.feedback_error_rate, Toast.LENGTH_SHORT).show();
			return;
		}

		new MarketplaceRepositoryFactory(getApplication())
			.createFeedbackRepository()
			.addFeedback(feedback, addedFeedbackId::setValue, () -> Toast.makeText(getApplication(), R.string.feedback_error_add, Toast.LENGTH_SHORT).show());
	}

	public void removeFeedback(User seller) {
		new MarketplaceRepositoryFactory(getApplication())
			.createFeedbackRepository()
			.removeFeedback(seller.getId(), removedFeedback::setValue, () -> {
				Toast.makeText(getApplication(), R.string.feedback_error_remove, Toast.LENGTH_SHORT).show();
				removedFeedback.setValue(null);
			});
	}

	public LiveData<Feedback> getLeftFeedback(User user) {
		if (leftFeedback == null) {
			leftFeedback = new MutableLiveData<>();
			new MarketplaceRepositoryFactory(getApplication())
				.createFeedbackRepository()
				.getLeftFeedback(user.getId(), feedback -> leftFeedback.setValue(feedback));
		}
		return leftFeedback;
	}

	private boolean hasMorePages() {
		Page<Feedback> lastPageValue = lastPage.getValue();
		return lastPageValue == null || lastPageValue.getPageIndex() != lastPageValue.getTotalPages();
	}
}
