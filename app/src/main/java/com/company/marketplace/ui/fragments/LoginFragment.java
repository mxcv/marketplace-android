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
import com.company.marketplace.models.Account;
import com.company.marketplace.databinding.FragmentLoginBinding;
import com.company.marketplace.models.User;
import com.company.marketplace.ui.viewmodels.LoginViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment implements View.OnClickListener {

	private FragmentLoginBinding binding;
	private LoginViewModel loginViewModel;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentLoginBinding.inflate(inflater, container, false);
		binding.loginLogin.setOnClickListener(this);

		loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
		loginViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
			Account.get().setUser(user);
			Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
				.navigate(R.id.nav_items);
		});

		return binding.getRoot();
	}

	@Override
	public void onClick(View v) {
		loginViewModel.login(new User(
			Objects.requireNonNull(binding.loginEmail.getText()).toString(),
			Objects.requireNonNull(binding.loginPassword.getText()).toString()));
	}
}
