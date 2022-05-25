package com.company.marketplace.ui.tools;

import android.content.Context;
import android.widget.ImageView;

import com.company.marketplace.R;
import com.company.marketplace.databinding.UserBinding;
import com.company.marketplace.models.User;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class UserInfoSetter {

	public static void setUser(Context context, UserBinding binding, User user) {
		if (user.getImage() == null)
			binding.userImage.setImageResource(R.drawable.ic_person);
		else
			Picasso.get()
				.load(user.getImage().getFullPath())
				.into(binding.userImage);

		binding.userName.setText(user.getName());
		binding.userPhone.setText(user.getPhoneNumber());
		binding.userLocation.setText(user.getLocationFormat());
		binding.userFeedbackCount.setText(context.getResources().getString(R.string.feedback_count, user.getFeedbackStatistics().getCount()));
		binding.userFeedbackAverage.setText(String.format(Locale.getDefault(), "%.1f", user.getFeedbackStatistics().getAverage()));

		int roundedAverage = (int)Math.round(user.getFeedbackStatistics().getAverage() * 2);
		for (int i = 0; i < roundedAverage / 2; ++i)
			((ImageView)binding.userFeedbackRate.getRoot().getChildAt(i)).setImageResource(R.drawable.ic_star);
		if (roundedAverage % 2 == 1)
			((ImageView)binding.userFeedbackRate.getRoot().getChildAt(roundedAverage / 2)).setImageResource(R.drawable.ic_star_half);
	}
}
