package com.company.marketplace.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.marketplace.R;
import com.company.marketplace.models.Item;

import java.text.NumberFormat;
import java.util.List;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.ViewHolder>{

	private final LayoutInflater inflater;
	private final List<Item> items;

	public MyItemAdapter(Context context, List<Item> items) {
		this.items = items;
		this.inflater = LayoutInflater.from(context);
	}

	@NonNull
	@Override
	public MyItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View view = inflater.inflate(R.layout.list_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MyItemAdapter.ViewHolder holder, int position) {
		Item item = items.get(position);
		holder.titleTextView.setText(item.getTitle());
		holder.priceTextView.setText(
			NumberFormat.getCurrencyInstance(item.getCurrency().getLocale())
				.format(item.getPrice()));
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		final TextView titleTextView, priceTextView;
		ViewHolder(View view){
			super(view);
			titleTextView = view.findViewById(R.id.myItemsTitle);
			priceTextView = view.findViewById(R.id.myItemsPrice);
		}
	}
}
