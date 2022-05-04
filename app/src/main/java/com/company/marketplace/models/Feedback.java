package com.company.marketplace.models;

import java.util.Date;

public class Feedback {

	private int id;
	private int rate;
	private String text;
	private Date created;
	private User reviewer;
	private User seller;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}

	public User getReviewer() {
		return reviewer;
	}
	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public User getSeller() {
		return seller;
	}
	public void setSeller(User seller) {
		this.seller = seller;
	}
}
