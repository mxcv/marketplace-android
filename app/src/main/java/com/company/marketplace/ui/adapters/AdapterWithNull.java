package com.company.marketplace.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.company.marketplace.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterWithNull<T> extends ArrayAdapter<T> {

	private final Context context;

	public AdapterWithNull(@NonNull Context context, @NonNull List<T> list) {

		super(context, android.R.layout.simple_spinner_item, new ArrayList<>(list));
		setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.context = context;
		insert(null, 0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_item);
	}

	@Override
	public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
	}

	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {

		if (convertView == null) {
			LayoutInflater vi = LayoutInflater.from(context);
			convertView = vi.inflate(resource, parent, false);
		}

		T object = getItem(position);
		TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
		if (textView != null)
			textView.setText(object == null ? context.getString(R.string.not_selected) : object.toString());
		return convertView;
	}
}
