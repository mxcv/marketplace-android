package com.company.marketplace.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.company.marketplace.R;
import com.company.marketplace.models.ImageInput;
import com.company.marketplace.network.services.NetworkService;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

	private final List<ImageInput> images;

	public SliderAdapter(List<ImageInput> sliderDataArrayList) {
		this.images = sliderDataArrayList;
	}

	@Override
	public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_image, parent, false);
		return new SliderAdapterViewHolder(view);
	}

	@Override
	public void onBindViewHolder(SliderAdapterViewHolder holder, final int position) {
		ImageInput image = images.get(position);

		if (image == null) {
			holder.imageView.setImageResource(R.drawable.ic_image_not_supported);
			holder.imageView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		}
		else Picasso.get()
			.load(NetworkService.get().getBaseUrl() + image.getPath())
			.placeholder(R.drawable.ic_hourglass_empty)
			.into(holder.imageView);
	}

	@Override
	public int getCount() {
		return images.size();
	}

	static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {

		private final ImageView imageView;

		public SliderAdapterViewHolder(View itemView) {
			super(itemView);
			imageView = itemView.findViewById(R.id.sliderImageImage);
		}
	}
}
