package com.company.marketplace.models;

import androidx.annotation.NonNull;

import java.util.List;

public class Region {

	private int id;
	private String name;
	private Country country;
	private List<City> cities;

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

	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}

	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
}
