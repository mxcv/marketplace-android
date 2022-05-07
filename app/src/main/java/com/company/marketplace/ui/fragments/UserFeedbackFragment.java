package com.company.marketplace.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentUserFeedbackBinding;
import com.company.marketplace.models.User;
import com.company.marketplace.ui.adapters.FeedbackAdapter;
import com.company.marketplace.ui.viewmodels.SelectedUserViewModel;
import com.company.marketplace.ui.viewmodels.UserFeedbackViewModel;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class UserFeedbackFragment extends Fragment {

	private FragmentUserFeedbackBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentUserFeedbackBinding.inflate(inflater, container, false);

		new ViewModelProvider(requireActivity())
			.get(SelectedUserViewModel.class)
			.getUser()
			.observe(getViewLifecycleOwner(), this::setUser);

		return binding.getRoot();
	}

	private void setUser(User user) {
		if (user.getImage() != null)
			Picasso.get()
				.load(user.getImage().getFullPath())
				.into(binding.userFeedbackUser.userImage);

		binding.userFeedbackUser.userItems.setEnabled(false);
		binding.userFeedbackUser.userName.setText(user.getName());
		binding.userFeedbackUser.userPhone.setText(user.getPhoneNumber());
		binding.userFeedbackUser.userLocation.setText(user.getLocationFormat());
		binding.userFeedbackUser.userFeedbackCount.setText(getResources().getString(R.string.feedback_count, user.getFeedbackStatistics().getCount()));
		binding.userFeedbackUser.userFeedbackAverage.setText(String.format(Locale.getDefault(), "%.1f", user.getFeedbackStatistics().getAverage()));

		int roundedAverage = (int)Math.round(user.getFeedbackStatistics().getAverage() * 2);
		for (int i = 0; i < roundedAverage / 2; ++i)
			((ImageView)binding.userFeedbackUser.userFeedbackRate.getRoot().getChildAt(i)).setImageResource(R.drawable.ic_star);
		if (roundedAverage % 2 == 1)
			((ImageView)binding.userFeedbackUser.userFeedbackRate.getRoot().getChildAt(roundedAverage / 2)).setImageResource(R.drawable.ic_star_half);

		new ViewModelProvider(this)
			.get(UserFeedbackViewModel.class)
			.getItems(user)
			.observe(getViewLifecycleOwner(), feedback -> {
				binding.userFeedbackRecyclerView.setAdapter(new FeedbackAdapter(requireContext(), feedback));
			});
	}
}
