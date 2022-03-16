package com.company.marketplace.models;

public class SortType {

	private int id;
	private String name;

	public SortType() {
	}
	public SortType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
