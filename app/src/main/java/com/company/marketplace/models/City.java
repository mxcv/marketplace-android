package com.company.marketplace.models;

import androidx.annotation.NonNull;

public class City {

	private int id;
	private String name;
	private Region region;

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

	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}

	@NonNull
	@Override
	public String toString() {
		return name;
	}
}
