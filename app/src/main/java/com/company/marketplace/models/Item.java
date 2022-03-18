package com.company.marketplace.models;

import android.content.Context;

import com.company.marketplace.R;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Item {

	private int id;
	private String title, description;
	private BigDecimal price;
	private Date created;
	private Currency currency;
	private Category category;
	private User user;
	private List<ImageInput> images;

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

	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
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

	public List<ImageInput> getImages() {
		return images;
	}
	public void setImages(List<ImageInput> images) {
		this.images = images;
	}

	public String getPriceFormat(Context context) {
		if (price == null)
			return "";
		if (price.compareTo(BigDecimal.ZERO) == 0)
			return context.getString(R.string.free);
		Locale locale;
		if (currency == null || (locale = currency.getLocale()) == null)
			return price.toString();

		return NumberFormat.getCurrencyInstance(locale).format(price);
	}
	public String getCreatedDateFormat(Context context) {
		if (created == null)
			return null;

		ZonedDateTime itemCreatedZonedDateTime = created.toInstant().atZone(ZoneId.systemDefault());
		LocalDate itemCreatedDate = itemCreatedZonedDateTime.toLocalDate();
		LocalTime itemCreatedTime = itemCreatedZonedDateTime.toLocalTime();
		LocalDate currentDate = LocalDate.now();

		String itemCreatedDateSting;
		if (currentDate.isEqual(itemCreatedDate))
			itemCreatedDateSting = context.getString(R.string.today);
		else if (currentDate.minusDays(1).isEqual(itemCreatedDate))
			itemCreatedDateSting = context.getString(R.string.yesterday);
		else
			itemCreatedDateSting = itemCreatedDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

		return String.format("%s %s",
			itemCreatedDateSting,
			itemCreatedTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
	}
}
