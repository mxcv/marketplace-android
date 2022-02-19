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

	@NonNull
	@Override
	public String toString() {
		String[] codes = countryCode.split("-");
		return java.util.Currency.getInstance(new Locale(codes[0], codes[1])).getSymbol();
	}
}
