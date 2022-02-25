package com.company.marketplace.models;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Currency {

	private int id;
	private String languageTag;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getLanguageTag() {
		return languageTag;
	}
	public void setLanguageTag(String languageTag) {
		this.languageTag = languageTag;
	}

	public Locale getLocale() {
		return Locale.forLanguageTag(languageTag);
	}

	@NonNull
	@Override
	public String toString() {
		return java.util.Currency.getInstance(getLocale()).getSymbol();
	}
}
