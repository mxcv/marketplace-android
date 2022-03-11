package com.company.marketplace.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.marketplace.R;
import com.company.marketplace.account.Account;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.ui.adapters.MyItemAdapter;

public class MyItemsFragment extends Fragment {

	private RecyclerView myItemsRecyclerView;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_my_items, container, false);
		myItemsRecyclerView = view.findViewById(R.id.myItemsRecyclerView);

		new MarketplaceRepositoryFactory(getActivity()).createItemRepository().getMyItems(null, null,
			page -> {
				User user = Account.getInstance().getUser();
				page.getItems().forEach(item -> item.setUser(user));
				myItemsRecyclerView.setAdapter(new MyItemAdapter(getContext(), page.getItems()));
			});

		return view;
	}
}
