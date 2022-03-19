package com.company.marketplace.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentAddItemBinding;
import com.company.marketplace.models.Category;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.ImageOutput;
import com.company.marketplace.models.Item;
import com.company.marketplace.network.repositories.CategoryRepository;
import com.company.marketplace.network.repositories.CurrencyRepository;
import com.company.marketplace.network.repositories.ImageRepository;
import com.company.marketplace.network.repositories.ItemRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.ui.tools.ImagePicker;
import com.company.marketplace.ui.tools.ObjectSelector;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class AddItemFragment extends Fragment implements View.OnClickListener {

	private FragmentAddItemBinding binding;
	private ImagePicker imagePicker;
	private CategoryRepository categoryRepository;
	private CurrencyRepository currencyRepository;
	private ImageRepository imageRepository;
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
					binding.addItemImages.addView(imageView);
				}
				binding.addItemAddPhoto.setIconResource(R.drawable.ic_delete);
			}
		});
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentAddItemBinding.inflate(inflater, container, false);
		binding.addItem.setOnClickListener(this);

		binding.addItemAddPhoto.setOnClickListener(v -> {
			if (this.images == null)
				imagePicker.pickImages();
			else {
				this.images = null;
				binding.addItemImages.removeAllViews();
				binding.addItemAddPhoto.setIconResource(R.drawable.ic_add_photo);
			}
		});

		categoryRepository = new MarketplaceRepositoryFactory(getContext()).createCategoryRepository();
		currencyRepository = new MarketplaceRepositoryFactory(getContext()).createCurrencyRepository();
		imageRepository = new MarketplaceRepositoryFactory(getContext()).createImageRepository();
		itemRepository = new MarketplaceRepositoryFactory(getContext()).createItemRepository();

		currencyRepository.getCurrencies(currencies ->
			currencySelector = new ObjectSelector<>(
				binding.addItemCurrency,
				null,
				currencies,
				Currency::getSymbol));

		categoryRepository.getCategories(categories ->
			categorySelector = new ObjectSelector<>(
				binding.addItemCategory,
				R.string.not_selected,
				categories,
				Category::getTitle));

		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	@Override
	public void onClick(View v) {
		String title = Objects.requireNonNull(binding.addItemTitle.getText()).toString();
		String description = Objects.requireNonNull(binding.addItemDescription.getText()).toString();
		String priceString = Objects.requireNonNull(binding.addItemPrice.getText()).toString();
		BigDecimal price = priceString.equals("") ? null : new BigDecimal(priceString);

		if (title.trim().isEmpty()) {
			Toast.makeText(getContext(), R.string.title_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (price != null && (price.compareTo(BigDecimal.ZERO) < 0 || price.compareTo(new BigDecimal("999999999.99")) > 0)) {
			Toast.makeText(getContext(), R.string.price_range, Toast.LENGTH_SHORT).show();
			return;
		}

		itemRepository.addItem(
			new Item(
				title,
				description.equals("") ? null : description,
				price,
				currencySelector.getSelectedObject(),
				categorySelector.getSelectedObject()
			),
			itemId -> {
				if (images == null)
					Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
						.navigate(R.id.nav_my_items);
				else {
					imageRepository.addItemImages(itemId, images, ignored ->
						Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
							.navigate(R.id.nav_my_items));
				}
			},
			() -> Toast.makeText(getContext(), R.string.add_item_error, Toast.LENGTH_SHORT).show());
	}
}
