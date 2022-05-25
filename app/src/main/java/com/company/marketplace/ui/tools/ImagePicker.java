package com.company.marketplace.ui.tools;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.company.marketplace.R;
import com.company.marketplace.models.ImageOutput;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ImagePicker {

	private final ActivityResultLauncher<String> contentPickerLauncher, permissionLauncher;
	private final Context context;
	private final Integer maxCount;
	private final Consumer<List<ImageOutput>> imageConsumer;

	public ImagePicker(Fragment fragment, Integer maxCount, Consumer<List<ImageOutput>> imageConsumer) {
		context = fragment.getContext();
		this.maxCount = maxCount;
		this.imageConsumer = imageConsumer;

		contentPickerLauncher = maxCount != null && maxCount == 1
			? fragment.registerForActivityResult(new ActivityResultContracts.GetContent(), this::handleUri)
			: fragment.registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), this::handleUris);

		permissionLauncher = fragment.registerForActivityResult(
			new ActivityResultContracts.RequestPermission(),
			this::launchPicker);
	}

	public void pickImages() {
		permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
	}

	private void launchPicker(boolean isPermissionGranted) {
		if (isPermissionGranted)
			contentPickerLauncher.launch("image/*");
		else
			Toast.makeText(context, R.string.permission_error, Toast.LENGTH_SHORT).show();
	}

	private void handleUri(Uri uri) {
		ImageOutput image = readImage(uri);
		if (image != null)
			imageConsumer.accept(Collections.singletonList(image));
	}

	private void handleUris(List<Uri> uris) {
		if (maxCount != null && uris.size() > maxCount) {
			uris.subList(maxCount, uris.size()).clear();
			Toast.makeText(context,
				Objects.requireNonNull(context).getString(R.string.max_images_error, maxCount),
				Toast.LENGTH_SHORT
			).show();
		}

		List<ImageOutput> images = new ArrayList<>(uris.size());
		for (Uri uri : uris) {
			ImageOutput image = readImage(uri);
			if (image != null)
				images.add(image);
		}

		if (images.size() != 0)
			imageConsumer.accept(images);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private ImageOutput readImage(Uri uri) {
		try (InputStream stream = Objects.requireNonNull(context).getContentResolver().openInputStream(uri)) {
			byte[] bytes = new byte[stream.available()];
			stream.read(bytes);
			return new ImageOutput("image." + getExtension(context, uri), bytes);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getExtension(Context context, Uri uri) {
		return uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)
			? MimeTypeMap.getSingleton()
				.getExtensionFromMimeType(
					Objects.requireNonNull(context)
						.getContentResolver()
						.getType(uri))
			: MimeTypeMap.getFileExtensionFromUrl(
				Uri.fromFile(new File(uri.getPath())).toString());
	}
}
