package com.company.marketplace.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.company.marketplace.ui.tools.ImagePicker;
import com.company.marketplace.ui.tools.ObjectSelector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.util.List;

public class AddItemFragment extends Fragment implements View.OnClickListener {

	private EditText titleView, priceView, descriptionView;
	private LinearLayout imageView;
	private AutoCompleteTextView currencyView, categoryView;
	private MaterialButton addPhotoView;
	private ImagePicker imagePicker;
	private ItemRepository itemRepository;
	private List<ImageOutput> images;
	private ObjectSelector<Currency> currencySelector;
	private ObjectSelector<Category> categorySelector;

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
					this.imageView.addView(imageView);
				}
				addPhotoView.setIconResource(R.drawable.ic_delete);
			}
		});
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_item, container, false);
		view.findViewById(R.id.addItem).setOnClickListener(this);
		addPhotoView = view.findViewById(R.id.addItemAddPhoto);
		titleView = ((TextInputLayout)view.findViewById(R.id.addItemTitle)).getEditText();
		priceView = ((TextInputLayout)view.findViewById(R.id.addItemPrice)).getEditText();
		descriptionView = ((TextInputLayout)view.findViewById(R.id.addItemDescription)).getEditText();
		imageView = view.findViewById(R.id.addItemImages);
		currencyView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.addItemCurrency)).getEditText();
		categoryView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.addItemCategory)).getEditText();

		addPhotoView.setOnClickListener(v -> {
			if (this.images == null)
				imagePicker.pickImages();
			else {
				this.images = null;
				imageView.removeAllViews();
				addPhotoView.setIconResource(R.drawable.ic_add_photo);
			}
		});

		itemRepository = new MarketplaceRepositoryFactory(getActivity()).createItemRepository();
		itemRepository.getCurrencies(currencies ->
			currencySelector = new ObjectSelector<>(currencyView, null, currencies, Currency::toString));
		itemRepository.getCategories(categories ->
			categorySelector = new ObjectSelector<>(categoryView, R.string.not_selected, categories, Category::getTitle));

		return view;
	}

	@Override
	public void onClick(View v) {
		BigDecimal price = new BigDecimal(priceView.getText().toString());
		if (titleView.getText().toString().trim().isEmpty()) {
			Toast.makeText(getContext(), R.string.title_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (price.compareTo(new BigDecimal(0)) < 0 || price.compareTo(new BigDecimal("999999999.99")) > 0) {
			Toast.makeText(getContext(), R.string.price_range, Toast.LENGTH_SHORT).show();
			return;
		}

		itemRepository.addItem(new Item(
				titleView.getText().toString(),
				descriptionView.getText().toString(),
				price,
				currencySelector.getSelectedObject(),
				categorySelector.getSelectedObject()
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
