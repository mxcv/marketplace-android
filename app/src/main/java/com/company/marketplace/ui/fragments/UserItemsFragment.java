package com.company.marketplace.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentUserItemsBinding;
import com.company.marketplace.models.User;
import com.company.marketplace.ui.adapters.ItemAdapter;
import com.company.marketplace.ui.viewmodels.SelectedItemViewModel;
import com.company.marketplace.ui.viewmodels.SelectedUserViewModel;
import com.company.marketplace.ui.viewmodels.UserItemsViewModel;
import com.squareup.picasso.Picasso;

public class UserItemsFragment extends Fragment {

	private FragmentUserItemsBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentUserItemsBinding.inflate(inflater, container, false);

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
				.into(binding.userItemsUser.userImage);

		binding.userItemsUser.userName.setText(user.getName());
		binding.userItemsUser.userPhone.setText(user.getPhoneNumber());
		binding.userItemsUser.userLocation.setText(user.getLocationFormat());
		binding.userItemsUser.userItems.setEnabled(false);

		new ViewModelProvider(this)
			.get(UserItemsViewModel.class)
			.getItems(user)
			.observe(getViewLifecycleOwner(), items -> {
				binding.userItemsRecyclerView.setAdapter(new ItemAdapter(requireContext(), items, item -> {
					new ViewModelProvider(requireActivity())
						.get(SelectedItemViewModel.class)
						.select(item);
					Navigation.findNavController(binding.getRoot())
						.navigate(R.id.action_user_items_to_item);
				}));
			});
	}
}
