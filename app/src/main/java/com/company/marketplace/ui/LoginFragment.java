package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.models.Account;
import com.company.marketplace.databinding.FragmentLoginBinding;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;

import java.util.Objects;

public class LoginFragment extends Fragment implements View.OnClickListener {

	private FragmentLoginBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentLoginBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		binding.loginLogin.setOnClickListener(this);
		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	@Override
	public void onClick(View v) {
		UserRepository userRepository = new MarketplaceRepositoryFactory(getActivity()).createUserRepository();
		userRepository.login(
			Objects.requireNonNull(binding.loginEmail.getEditText()).getText().toString(),
			Objects.requireNonNull(binding.loginPassword.getEditText()).getText().toString(),
			ignored -> userRepository.getUser(user -> {
				Account.get().setUser(user);
				Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
					.navigate(R.id.nav_items);
			}),
			() -> Toast.makeText(getContext(), R.string.login_error, Toast.LENGTH_SHORT).show()
		);
	}
}
