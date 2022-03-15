package com.company.marketplace.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.marketplace.R;
import com.company.marketplace.account.Account;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.ItemRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.ui.adapters.ItemAdapter;

import java.util.List;
import java.util.Objects;

public class MyItemsFragment extends Fragment {

	private RecyclerView myItemsView;
	private List<Item> items;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_my_items, container, false);
		myItemsView = view.findViewById(R.id.myItemsRecyclerView);

		ItemRepository itemRepository = new MarketplaceRepositoryFactory(getActivity()).createItemRepository();

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
			new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
				@Override
				public boolean onMove(@NonNull RecyclerView recyclerView,
									  @NonNull RecyclerView.ViewHolder viewHolder,
									  @NonNull RecyclerView.ViewHolder target) {

					return false;
				}

				@Override
				public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
					int position = viewHolder.getAdapterPosition();
					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireContext());
					alertBuilder.setTitle(R.string.remove_item_title);
					alertBuilder.setMessage(R.string.remove_item_message);
					alertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> {
						itemRepository.removeItem(items.get(position).getId(), null, null);
						items.remove(position);
						Objects.requireNonNull(myItemsView.getAdapter()).notifyItemRemoved(position);
						dialog.dismiss();
					});
					alertBuilder.setNegativeButton(R.string.no, (dialog, which) -> {
						Objects.requireNonNull(myItemsView.getAdapter()).notifyItemChanged(position);
						dialog.dismiss();
					});
					alertBuilder.create().show();
				}
			});

		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setUser(Account.get().getUser());
		itemRepository.getItems(itemRequest,
			page -> {
				items = page.getItems();
				User user = Account.get().getUser();
				for (Item item : items)
					item.setUser(user);
				myItemsView.setAdapter(new ItemAdapter(getContext(), items));
				itemTouchHelper.attachToRecyclerView(myItemsView);
			});

		return view;
	}
}
