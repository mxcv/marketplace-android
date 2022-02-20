package com.company.marketplace.models;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Currency {

	private int id;
	private String countryCode;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Locale getLocale() {
		String[] codes = countryCode.split("-");
		return new Locale(codes[0], codes[1]);
	}

	@NonNull
	@Override
	public String toString() {
		return java.util.Currency.getInstance(getLocale()).getSymbol();
	}
}
