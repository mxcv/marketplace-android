package com.company.marketplace.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentUserFeedbackBinding;
import com.company.marketplace.models.Feedback;
import com.company.marketplace.models.User;
import com.company.marketplace.ui.adapters.FeedbackAdapter;
import com.company.marketplace.ui.tools.UserInfoSetter;
import com.company.marketplace.ui.viewmodels.SelectedUserViewModel;
import com.company.marketplace.ui.viewmodels.UserFeedbackViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserFeedbackFragment extends Fragment {

	private FragmentUserFeedbackBinding binding;
	private List<Feedback> feedback;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentUserFeedbackBinding.inflate(inflater, container, false);

		binding.userFeedbackUser.userFeedback.setEnabled(false);
		binding.userFeedbackUser.userItems.setOnClickListener(v ->
			Navigation.findNavController(binding.getRoot())
				.navigate(R.id.action_user_feedback_to_user_items));

		new ViewModelProvider(requireActivity())
			.get(SelectedUserViewModel.class)
			.getUser()
			.observe(getViewLifecycleOwner(), this::setUser);

		return binding.getRoot();
	}

	private void setUser(User user) {
		UserInfoSetter.setUser(requireContext(), binding.userFeedbackUser, user);

		feedback = new ArrayList<>();
		binding.userFeedbackRecyclerView.setAdapter(new FeedbackAdapter(requireContext(), feedback));
		UserFeedbackViewModel userFeedbackViewModel = new ViewModelProvider(this).get(UserFeedbackViewModel.class);

		if (userFeedbackViewModel.getFeedback().getValue() != null)
			addFeedback(userFeedbackViewModel.getFeedback().getValue());
		else
			userFeedbackViewModel.loadNextPage(user);

		userFeedbackViewModel.getLastPage().observe(getViewLifecycleOwner(), page -> addFeedback(page.getItems()));

		binding.userFeedbackScrollView.setOnScrollChangeListener(
			(NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
				if (scrollY > oldScrollY && scrollY >= (binding.userFeedbackRecyclerView.getMeasuredHeight() - v.getMeasuredHeight()))
					userFeedbackViewModel.loadNextPage(user);
			});
	}

	private void addFeedback(List<Feedback> newFeedback) {
		int start = feedback.size();
		feedback.addAll(newFeedback);
		Objects.requireNonNull(binding.userFeedbackRecyclerView.getAdapter()).notifyItemRangeInserted(start, newFeedback.size());
	}
}
