package com.company.marketplace.models;

public class PageOutput {

	private int skipCount, takeCount;

	public PageOutput() {
	}
	public PageOutput(int skipCount, int takeCount) {
		this.skipCount = skipCount;
		this.takeCount = takeCount;
	}

	public int getSkipCount() {
		return skipCount;
	}
	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}

	public int getTakeCount() {
		return takeCount;
	}
	public void setTakeCount(int takeCount) {
		this.takeCount = takeCount;
	}
}
