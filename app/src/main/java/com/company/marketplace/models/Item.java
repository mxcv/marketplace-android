package com.company.marketplace.models;

import java.math.BigDecimal;

public class Item {

	private int id;
	private String title, description;
	private BigDecimal price;
	private Currency currency;
	private User user;

	public Item() {

	}
	public Item(String title, String description, BigDecimal price, Currency currency) {
		this.title = title;
		this.description = description;
		this.price = price;
		this.currency = currency;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
