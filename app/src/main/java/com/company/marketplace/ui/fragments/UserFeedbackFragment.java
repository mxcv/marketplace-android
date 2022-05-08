package com.company.marketplace.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentUserFeedbackBinding;
import com.company.marketplace.models.User;
import com.company.marketplace.ui.adapters.FeedbackAdapter;
import com.company.marketplace.ui.tools.UserInfoSetter;
import com.company.marketplace.ui.viewmodels.SelectedUserViewModel;
import com.company.marketplace.ui.viewmodels.UserFeedbackViewModel;

public class UserFeedbackFragment extends Fragment {

	private FragmentUserFeedbackBinding binding;

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

		new ViewModelProvider(this)
			.get(UserFeedbackViewModel.class)
			.getItems(user)
			.observe(getViewLifecycleOwner(), feedback -> {
				binding.userFeedbackRecyclerView.setAdapter(new FeedbackAdapter(requireContext(), feedback));
			});
	}
}
