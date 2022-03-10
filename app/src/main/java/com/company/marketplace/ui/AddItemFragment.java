package com.company.marketplace.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.models.Category;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.ImageOutput;
import com.company.marketplace.models.Item;
import com.company.marketplace.network.repositories.ItemRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.ui.adapters.AdapterWithNull;
import com.google.android.material.button.MaterialButton;

import java.math.BigDecimal;
import java.util.List;

public class AddItemFragment extends Fragment implements View.OnClickListener {

	private EditText titleEditText, priceEditText, descriptionEditText;
	private LinearLayout imageLinearLayout;
	private Spinner currencySpinner, categorySpinner;
	private MaterialButton addPhotoButton;
	private ItemRepository itemRepository;
	private ImagePicker imagePicker;
	private List<ImageOutput> images;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imagePicker = new ImagePicker(this, 7, images -> {
			if (images.size() > 0) {
				this.images = images;
				for (ImageOutput image : images) {
					ImageView imageView = new ImageView(getContext());
					imageView.setLayoutParams(new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT));
					imageView.setPadding(5, 0, 5, 0);
					imageView.setImageBitmap(BitmapFactory.decodeByteArray(image.getBytes(), 0, image.getBytes().length));
					imageView.setAdjustViewBounds(true);
					imageLinearLayout.addView(imageView);
				}
				addPhotoButton.setIconResource(R.drawable.ic_delete);
			}
		});
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_item, container, false);
		view.findViewById(R.id.addItem).setOnClickListener(this);
		addPhotoButton = view.findViewById(R.id.addItemAddPhoto);
		titleEditText = view.findViewById(R.id.addItemTitle);
		priceEditText = view.findViewById(R.id.addItemPrice);
		imageLinearLayout = view.findViewById(R.id.addItemImages);
		descriptionEditText = view.findViewById(R.id.addItemDescription);
		currencySpinner = view.findViewById(R.id.addItemCurrency);
		categorySpinner = view.findViewById(R.id.addItemCategory);

		addPhotoButton.setOnClickListener(v -> {
			if (this.images == null)
				imagePicker.pickImages();
			else {
				this.images = null;
				imageLinearLayout.removeAllViews();
				addPhotoButton.setIconResource(R.drawable.ic_add_photo);
			}
		});

		itemRepository = new MarketplaceRepositoryFactory(getActivity()).createItemRepository();
		itemRepository.getCurrencies(currencies -> {
			ArrayAdapter<Currency> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, currencies);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			currencySpinner.setAdapter(adapter);
		});
		itemRepository.getCategories(categories -> categorySpinner.setAdapter(new AdapterWithNull<>(getContext(), categories)));
		return view;
	}

	@Override
	public void onClick(View v) {
		BigDecimal price = new BigDecimal(priceEditText.getText().toString());
		if (titleEditText.getText().toString().trim().isEmpty()) {
			Toast.makeText(getContext(), R.string.title_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (price.compareTo(new BigDecimal(0)) < 0 || price.compareTo(new BigDecimal("999999999.99")) > 0) {
			Toast.makeText(getContext(), R.string.price_range, Toast.LENGTH_SHORT).show();
			return;
		}

		itemRepository.addItem(new Item(
				titleEditText.getText().toString(),
				descriptionEditText.getText().toString(),
				price,
				(Currency) currencySpinner.getSelectedItem(),
				(Category) categorySpinner.getSelectedItem()
			),
			itemId -> {
				if (images == null)
					Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
						.navigate(R.id.nav_my_items);
				else {
					itemRepository.addItemImages(itemId, images, ignored ->
						Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
							.navigate(R.id.nav_my_items));
				}
			},
			() -> Toast.makeText(getContext(), R.string.add_item_error, Toast.LENGTH_SHORT).show());
	}
}
