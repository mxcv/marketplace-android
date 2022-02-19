package com.company.marketplace.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.marketplace.R;
import com.company.marketplace.account.Account;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

public class LogoutFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new MarketplaceRepositoryFactory().create(getActivity()).logout();
		Account.getInstance().setUser(null, getActivity());
	}
}
