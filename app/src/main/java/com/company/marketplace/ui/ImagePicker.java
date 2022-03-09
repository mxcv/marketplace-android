package com.company.marketplace.ui;

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
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ImagePicker {

	private final ActivityResultLauncher<String> contentPickerLauncher, permissionLauncher;

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public ImagePicker(Fragment fragment, Integer maxCount, Consumer<List<ImageOutput>> imageConsumer) {
		Context context = fragment.getContext();
		contentPickerLauncher = fragment.registerForActivityResult(new ActivityResultContracts.GetMultipleContents(),
			uris -> {
				if (maxCount != null && uris.size() > maxCount)
					uris.subList(maxCount, uris.size()).clear();
				List<ImageOutput> images = new ArrayList<>(uris.size());

				for (Uri uri : uris) {
					try (InputStream stream = Objects.requireNonNull(context).getContentResolver().openInputStream(uri)) {
						byte[] bytes = new byte[stream.available()];
						stream.read(bytes);
						images.add(new ImageOutput("image." + getExtension(context, uri), bytes));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				imageConsumer.accept(images);
			});
		permissionLauncher = fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(),
			isGranted -> {
				if (isGranted)
					contentPickerLauncher.launch("image/*");
				else
					Toast.makeText(context, R.string.permission_error, Toast.LENGTH_SHORT).show();
			});
	}

	public void pickImages() {
		permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
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
