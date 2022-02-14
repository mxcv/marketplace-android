package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.models.User;
import com.company.marketplace.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.repositories.UserRepository;

public class RegisterFragment extends Fragment implements View.OnClickListener {

	private EditText nameEditText, phoneNumberEditText, emailEditText, passwordEditText, confirmPasswordEditText;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container, false);
		view.findViewById(R.id.register).setOnClickListener(this);
		nameEditText = view.findViewById(R.id.registerName);
		phoneNumberEditText = view.findViewById(R.id.registerPhoneNumber);
		emailEditText = view.findViewById(R.id.registerEmail);
		passwordEditText = view.findViewById(R.id.registerPassword);
		confirmPasswordEditText = view.findViewById(R.id.registerConfirmPassword);
		return view;
	}

	@Override
	public void onClick(View v) {
		if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()))
			Toast.makeText(getContext(), R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
		else {
			UserRepository userRepository = new MarketplaceRepositoryFactory().create(getActivity());
			userRepository.createUser(new User(
					emailEditText.getText().toString(),
					passwordEditText.getText().toString(),
					phoneNumberEditText.getText().toString(),
					nameEditText.getText().toString()
				),
				ignored -> Navigation.findNavController(v).navigate(R.id.nav_login),
				() -> Toast.makeText(getContext(), R.string.invalid_email_or_password, Toast.LENGTH_SHORT).show()
			);
		}
	}
}
