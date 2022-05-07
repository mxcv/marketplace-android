package com.company.marketplace.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.marketplace.R;
import com.company.marketplace.models.Feedback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

	private final LayoutInflater inflater;
	private final List<Feedback> feedback;

	public FeedbackAdapter(Context context, List<Feedback> feedback) {
		this.inflater = LayoutInflater.from(context);
		this.feedback = feedback;
	}

	@NonNull
	@Override
	public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View view = inflater.inflate(R.layout.list_feedback, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {
		Feedback feedback = this.feedback.get(position);

		holder.userNameTextView.setText(feedback.getReviewer().getName());
		holder.createdTextView.setText(feedback.getCreatedDateFormat());
		holder.feedbackTextView.setText(feedback.getText());

		for (int i = 0; i < feedback.getRate(); ++i)
			((ImageView)holder.rateLinearLayout.getChildAt(i)).setImageResource(R.drawable.ic_star);

		if (feedback.getReviewer().getImage() != null)
			Picasso.get()
				.load(feedback.getReviewer().getImage().getFullPath())
				.placeholder(R.drawable.ic_hourglass_empty)
				.into(holder.userImageView);
	}

	@Override
	public int getItemCount() {
		return feedback.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		private final TextView userNameTextView, createdTextView, feedbackTextView;
		private final ImageView userImageView;
		private final LinearLayout rateLinearLayout;

		ViewHolder(View view) {
			super(view);
			userNameTextView = view.findViewById(R.id.listFeedbackUserName);
			createdTextView = view.findViewById(R.id.listFeedbackCreated);
			feedbackTextView = view.findViewById(R.id.listFeedbackText);
			userImageView = view.findViewById(R.id.listFeedbackUserImage);
			rateLinearLayout = view.findViewById(R.id.listFeedbackRate);
		}
	}
}
