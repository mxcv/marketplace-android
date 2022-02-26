package com.company.marketplace.models;

import java.math.BigDecimal;

public class Item {

	private int id;
	private String title, description;
	private BigDecimal price;
	private Currency currency;
	private Category category;
	private User user;

	public Item() {

	}
	public Item(String title, String description, BigDecimal price, Currency currency, Category category) {
		this.title = title;
		this.description = description;
		this.price = price;
		this.currency = currency;
		this.category = category;
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

	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
