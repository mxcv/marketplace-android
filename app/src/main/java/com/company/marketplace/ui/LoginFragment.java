package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.company.marketplace.R;
import com.company.marketplace.account.Account;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment implements View.OnClickListener {

	private EditText emailView, passwordView;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		view.findViewById(R.id.login).setOnClickListener(this);
		emailView = ((TextInputLayout)view.findViewById(R.id.loginEmail)).getEditText();
		passwordView = ((TextInputLayout)view.findViewById(R.id.loginPassword)).getEditText();
		return view;
	}

	@Override
	public void onClick(View v) {
		UserRepository userRepository = new MarketplaceRepositoryFactory(getActivity()).createUserRepository();
		userRepository.login(emailView.getText().toString(), passwordView.getText().toString(),
			ignored -> userRepository.getUser(user -> Account.get().setUser(user, getActivity())),
			() -> Toast.makeText(getContext(), R.string.login_error, Toast.LENGTH_SHORT).show()
		);
	}
}
