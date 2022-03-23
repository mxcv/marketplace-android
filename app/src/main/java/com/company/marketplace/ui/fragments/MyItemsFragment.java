package com.company.marketplace.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentMyItemsBinding;
import com.company.marketplace.models.Account;
import com.company.marketplace.models.Category;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.User;
import com.company.marketplace.ui.adapters.ItemAdapter;
import com.company.marketplace.ui.tools.ItemInfoFiller;
import com.company.marketplace.ui.viewmodels.MarketplaceViewModel;
import com.company.marketplace.ui.viewmodels.MyItemsViewModel;
import com.company.marketplace.ui.viewmodels.SelectedItemViewModel;

import java.util.List;
import java.util.Objects;

public class MyItemsFragment extends Fragment {

	private FragmentMyItemsBinding binding;
	private List<Category> categories;
	private List<Currency> currencies;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentMyItemsBinding.inflate(inflater, container, false);

		MarketplaceViewModel marketplaceViewModel = new ViewModelProvider(requireActivity())
			.get(MarketplaceViewModel.class);
		marketplaceViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
			synchronized (this) {
				this.categories = categories;
				notify();
			}
		});
		marketplaceViewModel.getCurrencies().observe(getViewLifecycleOwner(), currencies -> {
			synchronized (this) {
				this.currencies = currencies;
				notify();
			}
		});

		MyItemsViewModel myItemsViewModel = new ViewModelProvider(this).get(MyItemsViewModel.class);
		myItemsViewModel.getMyItems().observe(getViewLifecycleOwner(), myItems -> new Thread(() -> {
			try {
				synchronized (this) {
					while (categories == null || currencies == null)
						wait();
				}
				requireActivity().runOnUiThread(() -> {
					User user = Account.get().getUser().getValue();
					myItems.forEach(i -> i.setUser(user));
					new ItemInfoFiller(myItems)
						.fillCategories(categories)
						.fillCurrencies(currencies);
					binding.myItemsRecyclerView.setAdapter(new ItemAdapter(getContext(), myItems,
						item -> {
							new ViewModelProvider(this)
								.get(SelectedItemViewModel.class)
								.select(item);
						}));
				});
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start());

		new ItemTouchHelper(new RemoveItemHelper(myItemsViewModel, binding.myItemsRecyclerView))
			.attachToRecyclerView(binding.myItemsRecyclerView);

		return binding.getRoot();
	}

	private static class RemoveItemHelper extends ItemTouchHelper.SimpleCallback {

		private final MyItemsViewModel myItemsViewModel;
		private final RecyclerView recyclerView;

		public RemoveItemHelper(MyItemsViewModel myItemsViewModel, RecyclerView recyclerView) {
			super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

			this.myItemsViewModel = myItemsViewModel;
			this.recyclerView = recyclerView;
		}

		@Override
		public boolean onMove(@NonNull RecyclerView recyclerView,
							  @NonNull RecyclerView.ViewHolder viewHolder,
							  @NonNull RecyclerView.ViewHolder target) {

			return false;
		}

		@Override
		public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
			int position = viewHolder.getAdapterPosition();
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(recyclerView.getContext());
			alertBuilder.setTitle(R.string.remove_item_title);
			alertBuilder.setMessage(R.string.remove_item_message);
			alertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> {
				List<Item> items = Objects.requireNonNull(myItemsViewModel.getMyItems().getValue());
				myItemsViewModel.removeItem(items.get(position).getId());
				items.remove(position);
				Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRemoved(position);
				dialog.dismiss();
			});
			alertBuilder.setNegativeButton(R.string.no, (dialog, which) -> {
				Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(position);
				dialog.dismiss();
			});
			alertBuilder.create().show();
		}
	}
}
