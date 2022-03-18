package com.company.marketplace.models;

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
		return languageTag == null ? null : Locale.forLanguageTag(languageTag);
	}

	public String getSymbol() {
		Locale locale = getLocale();
		return locale == null ? null : java.util.Currency.getInstance(locale).getSymbol();
	}
}
