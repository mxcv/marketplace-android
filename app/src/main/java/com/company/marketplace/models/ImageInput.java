package com.company.marketplace.models;

import com.company.marketplace.network.services.NetworkService;

public class ImageInput {

	private String path;

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public String getFullPath() {
		return path == null ? null : NetworkService.get().getBaseUrl() + path;
	}
}
