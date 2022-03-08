package com.company.marketplace.models;

import java.util.List;

public class Page {

	private List<Item> items;
	private int leftCount;

	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}

	public int getLeftCount() {
		return leftCount;
	}
	public void setLeftCount(int leftCount) {
		this.leftCount = leftCount;
	}
}
