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
import java.util.Objects;
import java.util.function.Consumer;

public class ImagePicker {

	private final ActivityResultLauncher<String> contentPickerLauncher;
	private final ActivityResultLauncher<String[]> permissionLauncher;

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public ImagePicker(Fragment fragment, Consumer<ImageOutput> imageConsumer) {
		Context context = fragment.getContext();
		contentPickerLauncher = fragment.registerForActivityResult(new ActivityResultContracts.GetContent(),
			uri -> {
				try (InputStream stream = Objects.requireNonNull(context).getContentResolver().openInputStream(uri)) {
					byte[] bytes = new byte[stream.available()];
					stream.read(bytes);
					imageConsumer.accept(new ImageOutput("image." + getExtension(context, uri), bytes));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		permissionLauncher = fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
			result -> {
				for (boolean isGranted : result.values())
					if (!isGranted) {
						Toast.makeText(context, R.string.permission_error, Toast.LENGTH_SHORT).show();
						return;
					}
				contentPickerLauncher.launch("image/*");
			});
	}

	public void pickImage() {
		permissionLauncher.launch(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE });
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
