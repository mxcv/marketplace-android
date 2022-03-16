package com.company.marketplace.models;

import java.math.BigDecimal;

public class ItemRequest {

	private String query;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Currency currency;
	private Category category;
	private Country country;
	private Region region;
	private City city;
	private User user;
	private SortType sortType;
	private Integer skipCount;
	private Integer takeCount;

	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
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

	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}

	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}

	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public SortType getSortType() {
		return sortType;
	}
	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}

	public Integer getSkipCount() {
		return skipCount;
	}
	public void setSkipCount(Integer skipCount) {
		this.skipCount = skipCount;
	}

	public Integer getTakeCount() {
		return takeCount;
	}
	public void setTakeCount(Integer takeCount) {
		this.takeCount = takeCount;
	}
}
