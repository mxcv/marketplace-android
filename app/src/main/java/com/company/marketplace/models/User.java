package com.company.marketplace.models;

public class User {

	private int id;
	private String email;
	private String password;
	private String phoneNumber;
	private String name;
	private City city;
	private ImageInput image;
	private FeedbackStatistics feedbackStatistics;

	public User() {
	}
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	public User(String email, String password, String phoneNumber, String name, City city) {
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.city = city;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}

	public ImageInput getImage() {
		return image;
	}
	public void setImage(ImageInput image) {
		this.image = image;
	}

	public FeedbackStatistics getFeedbackStatistics() {
		return feedbackStatistics;
	}
	public void setFeedbackStatistics(FeedbackStatistics feedbackStatistics) {
		this.feedbackStatistics = feedbackStatistics;
	}

	public String getLocationFormat() {
		if (city == null)
			return "";
		return String.format("%s, %s, %s",
			city.getRegion().getCountry().getName(),
			city.getRegion().getName(),
			city.getName());
	}
}
