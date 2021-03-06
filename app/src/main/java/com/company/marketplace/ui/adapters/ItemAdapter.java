package com.company.marketplace.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.marketplace.R;
import com.company.marketplace.models.Item;
import com.company.marketplace.network.services.NetworkService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

	private final Context context;
	private final LayoutInflater inflater;
	private final List<Item> items;
	private final OnItemClickListener onItemClickListener;

	public ItemAdapter(Context context, List<Item> items, OnItemClickListener onItemClickListener) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.items = items;
		this.onItemClickListener = onItemClickListener;
	}

	@NonNull
	@Override
	public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View view = inflater.inflate(R.layout.list_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
		Item item = items.get(position);
		holder.titleTextView.setText(item.getTitle());
		holder.priceTextView.setText(item.getPriceFormat(context));

		if (item.getImages().size() > 0)
			Picasso.get()
				.load(item.getImages().get(0).getFullPath())
				.placeholder(R.drawable.ic_hourglass_empty)
				.into(holder.imageView);
		else
			holder.imageView.setImageResource(R.drawable.ic_image_not_supported);

		StringBuilder sb = new StringBuilder();
		if (item.getUser().getCity() != null)
			sb.append(item.getUser().getCity().getName()).append(", ");
		sb.append(item.getCreatedDateFormat(context));
		holder.locationDateTextView.setText(sb.toString());

		if (onItemClickListener != null)
			holder.itemView.setOnClickListener(v ->
				onItemClickListener.onItemClick(item));
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		private final TextView titleTextView, priceTextView, locationDateTextView;
		private final ImageView imageView;

		ViewHolder(View view) {
			super(view);
			titleTextView = view.findViewById(R.id.listItemTitle);
			priceTextView = view.findViewById(R.id.listItemPrice);
			locationDateTextView = view.findViewById(R.id.listItemLocationDate);
			imageView = view.findViewById(R.id.listItemImage);
		}
	}

	public interface OnItemClickListener {

		void onItemClick(Item item);
	}
}
