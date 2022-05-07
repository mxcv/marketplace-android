package com.company.marketplace.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentItemBinding;
import com.company.marketplace.models.ImageInput;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.User;
import com.company.marketplace.ui.adapters.SliderAdapter;
import com.company.marketplace.ui.viewmodels.SelectedItemViewModel;
import com.company.marketplace.ui.viewmodels.SelectedUserViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ItemFragment extends Fragment {

	private FragmentItemBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentItemBinding.inflate(inflater, container, false);

		new ViewModelProvider(requireActivity())
			.get(SelectedItemViewModel.class)
			.getItem()
			.observe(getViewLifecycleOwner(), item -> {
				SelectedUserViewModel userModel = new ViewModelProvider(requireActivity())
					.get(SelectedUserViewModel.class);
				userModel.select(item.getUser());
				userModel.getUser().observe(getViewLifecycleOwner(), this::setUser);
				setItem(item);
			});
		return binding.getRoot();
	}

	private void setItem(Item item) {
		List<ImageInput> images;
		if (item.getImages().size() == 0)
			images = Collections.singletonList(null);
		else images = item.getImages();
		binding.itemImageSlider.setIndicatorAnimation(IndicatorAnimationType.COLOR);
		binding.itemImageSlider.setSliderAdapter(new SliderAdapter(images));

		binding.itemTitle.setText(item.getTitle());
		binding.itemDate.setText(item.getCreatedDateFormat(getContext()));
		binding.itemPrice.setText(item.getPriceFormat(getContext()));
		binding.itemDescription.setText(item.getDescription());
	}

	private void setUser(User user) {
		binding.itemUser.userItems.setOnClickListener(v -> {
			Navigation.findNavController(binding.getRoot())
				.navigate(R.id.action_item_to_user_items);
		});
		binding.itemUser.userFeedback.setOnClickListener(v -> {
			Navigation.findNavController(binding.getRoot())
				.navigate(R.id.action_item_to_user_feedback);
		});

		if (user.getImage() != null)
			Picasso.get()
				.load(user.getImage().getFullPath())
				.into(binding.itemUser.userImage);

		binding.itemUser.userName.setText(user.getName());
		binding.itemUser.userPhone.setText(user.getPhoneNumber());
		binding.itemUser.userLocation.setText(user.getLocationFormat());
		binding.itemUser.userFeedbackCount.setText(getResources().getString(R.string.feedback_count, user.getFeedbackStatistics().getCount()));
		binding.itemUser.userFeedbackAverage.setText(String.format(Locale.getDefault(), "%.1f", user.getFeedbackStatistics().getAverage()));

		int roundedAverage = (int)Math.round(user.getFeedbackStatistics().getAverage() * 2);
		for (int i = 0; i < roundedAverage / 2; ++i)
			((ImageView)binding.itemUser.userFeedbackRate.getRoot().getChildAt(i)).setImageResource(R.drawable.ic_star);
		if (roundedAverage % 2 == 1)
			((ImageView)binding.itemUser.userFeedbackRate.getRoot().getChildAt(roundedAverage / 2)).setImageResource(R.drawable.ic_star_half);
	}
}
