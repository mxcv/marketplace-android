package com.company.marketplace.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentUserFeedbackBinding;
import com.company.marketplace.databinding.ListFeedbackBinding;
import com.company.marketplace.models.Account;
import com.company.marketplace.models.Feedback;
import com.company.marketplace.models.User;
import com.company.marketplace.ui.adapters.FeedbackAdapter;
import com.company.marketplace.ui.tools.UserInfoSetter;
import com.company.marketplace.ui.viewmodels.SelectedUserViewModel;
import com.company.marketplace.ui.viewmodels.UserFeedbackViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFeedbackFragment extends Fragment {

	private FragmentUserFeedbackBinding binding;
	private SelectedUserViewModel selectedUserViewModel;
	private UserFeedbackViewModel userFeedbackViewModel;
	private List<Feedback> feedback;
	private int rate;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentUserFeedbackBinding.inflate(inflater, container, false);

		binding.userFeedbackUser.userFeedback.setEnabled(false);
		binding.userFeedbackUser.userItems.setOnClickListener(v ->
			Navigation.findNavController(binding.getRoot())
				.navigate(R.id.action_user_feedback_to_user_items));

		binding.userFeedbackAdd.getRoot().setVisibility(View.GONE);
		binding.userFeedbackRemove.getRoot().setVisibility(View.GONE);

		selectedUserViewModel = new ViewModelProvider(requireActivity()).get(SelectedUserViewModel.class);
		userFeedbackViewModel = new ViewModelProvider(this).get(UserFeedbackViewModel.class);
		selectedUserViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
			UserInfoSetter.setUser(requireContext(), binding.userFeedbackUser, user);
			setFeedbackList(user);
			setFeedbackHeader(user);
		});

		return binding.getRoot();
	}

	private void setFeedbackList(User seller) {
		feedback = new ArrayList<>();
		binding.userFeedbackRecyclerView.setAdapter(new FeedbackAdapter(requireContext(), feedback));

		if (userFeedbackViewModel.getFeedback().getValue() != null)
			addFeedback(userFeedbackViewModel.getFeedback().getValue());
		else
			userFeedbackViewModel.loadNextPage(seller);

		userFeedbackViewModel.getLastPage().observe(getViewLifecycleOwner(), page -> addFeedback(page.getItems()));

		binding.userFeedbackScrollView.setOnScrollChangeListener(
			(NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
				if (scrollY > oldScrollY && scrollY >= (binding.userFeedbackRecyclerView.getMeasuredHeight() - v.getMeasuredHeight()))
					userFeedbackViewModel.loadNextPage(seller);
			});
	}

	private void setFeedbackHeader(User seller) {
		User currentUser = Account.get().getUser().getValue();
		if (currentUser != null && currentUser.getId() != seller.getId())
			userFeedbackViewModel.getLeftFeedback(seller).observe(getViewLifecycleOwner(), feedback -> {
				userFeedbackViewModel.getAddedFeedbackId()
					.observe(getViewLifecycleOwner(), id -> reloadFragment());
				userFeedbackViewModel.getRemovedFeedback()
					.observe(getViewLifecycleOwner(), ignored -> reloadFragment());

				if (feedback == null) {
					binding.userFeedbackAdd.getRoot().setVisibility(View.VISIBLE);
					for (int i = 0; i < binding.userFeedbackAdd.feedbackAddStarLayout.getChildCount(); ++i)
						binding.userFeedbackAdd.feedbackAddStarLayout.getChildAt(i).setOnClickListener(v -> {
							rate = binding.userFeedbackAdd.feedbackAddStarLayout.indexOfChild(v) + 1;
							for (int j = 0; j < binding.userFeedbackAdd.feedbackAddStarLayout.getChildCount(); ++j)
								((ImageView)binding.userFeedbackAdd.feedbackAddStarLayout.getChildAt(j))
									.setImageResource(j < rate ? R.drawable.ic_star : R.drawable.ic_star_outline);
						});
					binding.userFeedbackAdd.feedbackAddAdd.setOnClickListener(v -> {
						Feedback newFeedback = new Feedback();
						newFeedback.setSeller(seller);
						newFeedback.setRate(rate);
						newFeedback.setText(Objects.requireNonNull(binding.userFeedbackAdd.feedbackAddText.getText()).toString());
						userFeedbackViewModel.addFeedback(newFeedback);
					});
				}
				else {
					binding.userFeedbackRemove.getRoot().setVisibility(View.VISIBLE);
					fillFeedbackLayout(binding.userFeedbackRemove.feedbackRemoveFeedback, feedback);
					binding.userFeedbackRemove.feedbackRemoveRemove.setOnClickListener(v ->
						userFeedbackViewModel.removeFeedback(seller));
				}
			});
	}

	private void addFeedback(List<Feedback> newFeedback) {
		int start = feedback.size();
		feedback.addAll(newFeedback);
		Objects.requireNonNull(binding.userFeedbackRecyclerView.getAdapter()).notifyItemRangeInserted(start, newFeedback.size());
	}

	private void reloadFragment() {
		selectedUserViewModel.select(Objects.requireNonNull(selectedUserViewModel.getUser().getValue()));
		Navigation.findNavController(binding.getRoot())
			.navigate(R.id.action_user_feedback_self);
	}

	private void fillFeedbackLayout(ListFeedbackBinding binding, Feedback feedback) {
		binding.listFeedbackUserName.setText(feedback.getReviewer().getName());
		binding.listFeedbackCreated.setText(feedback.getCreatedDateFormat());
		binding.listFeedbackText.setText(feedback.getText());

		for (int i = 0; i < feedback.getRate(); ++i)
			((ImageView)binding.listFeedbackRate.getRoot().getChildAt(i)).setImageResource(R.drawable.ic_star);

		if (feedback.getReviewer().getImage() != null)
			Picasso.get()
				.load(feedback.getReviewer().getImage().getFullPath())
				.into(binding.listFeedbackUserImage);
	}
}
