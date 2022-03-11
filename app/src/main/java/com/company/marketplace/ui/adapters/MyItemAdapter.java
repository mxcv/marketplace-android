package com.company.marketplace.ui.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.marketplace.R;
import com.company.marketplace.models.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.ViewHolder>{

	private final Context context;
	private final LayoutInflater inflater;
	private final List<Item> items;

	public MyItemAdapter(Context context, List<Item> items) {
		this.context = context;
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
		holder.priceTextView.setText(item.getPriceFormat(context));

		if (item.getImages().size() > 0)
			Picasso.get()
				.load(getBaseUrl(context) + item.getImages().get(0).getPath())
				.placeholder(R.drawable.ic_hourglass_empty)
				.into(holder.imageView);
		else
			holder.imageView.setImageResource(R.drawable.ic_image_not_supported);

		StringBuilder sb = new StringBuilder();
		if (item.getUser().getCity() != null)
			sb.append(item.getUser().getCity().toString()).append(", ");
		sb.append(item.getCreatedDateFormat(context));
		holder.locationDateTextView.setText(sb.toString());
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	private String getBaseUrl(Context context) {
		try {
			return context
				.getPackageManager()
				.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
				.metaData
				.get("base_url")
				.toString();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		private final TextView titleTextView, priceTextView, locationDateTextView;
		private final ImageView imageView;

		ViewHolder(View view){
			super(view);
			titleTextView = view.findViewById(R.id.listItemTitle);
			priceTextView = view.findViewById(R.id.listItemPrice);
			locationDateTextView = view.findViewById(R.id.listItemLocationDate);
			imageView = view.findViewById(R.id.listItemImage);
		}
	}
}
