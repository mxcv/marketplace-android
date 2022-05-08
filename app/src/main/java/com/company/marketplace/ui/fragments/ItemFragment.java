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
import com.company.marketplace.databinding.FragmentItemBinding;
import com.company.marketplace.models.ImageInput;
import com.company.marketplace.models.Item;
import com.company.marketplace.ui.adapters.SliderAdapter;
import com.company.marketplace.ui.tools.UserInfoSetter;
import com.company.marketplace.ui.viewmodels.SelectedItemViewModel;
import com.company.marketplace.ui.viewmodels.SelectedUserViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;

import java.util.Collections;
import java.util.List;

public class ItemFragment extends Fragment {

	private FragmentItemBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentItemBinding.inflate(inflater, container, false);

		binding.itemUser.userFeedback.setOnClickListener(v ->
			Navigation.findNavController(binding.getRoot())
				.navigate(R.id.action_item_to_user_feedback));
		binding.itemUser.userItems.setOnClickListener(v ->
			Navigation.findNavController(binding.getRoot())
				.navigate(R.id.action_item_to_user_items));

		new ViewModelProvider(requireActivity())
			.get(SelectedItemViewModel.class)
			.getItem()
			.observe(getViewLifecycleOwner(), item -> {
				SelectedUserViewModel userModel = new ViewModelProvider(requireActivity())
					.get(SelectedUserViewModel.class);
				userModel.select(item.getUser());
				userModel.getUser().observe(getViewLifecycleOwner(), user ->
					UserInfoSetter.setUser(requireContext(), binding.itemUser, user));
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
}
