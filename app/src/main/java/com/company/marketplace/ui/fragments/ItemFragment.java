package com.company.marketplace.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.company.marketplace.databinding.FragmentItemBinding;
import com.company.marketplace.models.ImageInput;
import com.company.marketplace.ui.adapters.SliderAdapter;
import com.company.marketplace.ui.viewmodels.SelectedItemViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;

import java.util.Collections;
import java.util.List;

public class ItemFragment extends Fragment {

	private FragmentItemBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentItemBinding.inflate(inflater, container, false);
		new ViewModelProvider(requireActivity())
			.get(SelectedItemViewModel.class)
			.getItem()
			.observe(getViewLifecycleOwner(), item -> {
				List<ImageInput> images;
				if (item.getImages().size() == 0)
					images = Collections.singletonList(null);
				else images = item.getImages();
				binding.itemImageSlider.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
				binding.itemImageSlider.setSliderAdapter(new SliderAdapter(images));
			});
		return binding.getRoot();
	}
}
