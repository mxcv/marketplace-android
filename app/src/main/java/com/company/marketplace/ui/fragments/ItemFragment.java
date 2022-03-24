package com.company.marketplace.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.company.marketplace.databinding.FragmentItemBinding;

public class ItemFragment extends Fragment {

	private FragmentItemBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentItemBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}
}
