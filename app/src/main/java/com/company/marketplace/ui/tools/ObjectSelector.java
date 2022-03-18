package com.company.marketplace.ui.tools;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectSelector<T> {

	private final AutoCompleteTextView textView;
	private final List<T> list;
	private final Function<T, String> mapper;

	public ObjectSelector(AutoCompleteTextView textView,
						  Integer defaultStringResource,
						  List<T> list,
						  Function<T, String> mapper) {

		this.textView = textView;
		this.list = list;
		this.mapper = mapper;

		List<String> strings = list.stream().map(mapper).collect(Collectors.toList());
		if (defaultStringResource != null)
			strings.add(0, textView.getContext().getString(defaultStringResource));

		textView.setAdapter(new ArrayAdapter<>(
			textView.getContext(),
			android.R.layout.simple_spinner_dropdown_item,
			new ArrayList<>(strings)));
		if (strings.size() != 0)
			textView.setText(strings.get(0), false);
	}

	public List<T> getObjects() {
		return list;
	}

	public T getSelectedObject() {
		return list.stream()
			.filter(x -> textView.getText().toString().equals(mapper.apply(x)))
			.findFirst()
			.orElse(null);
	}
}
