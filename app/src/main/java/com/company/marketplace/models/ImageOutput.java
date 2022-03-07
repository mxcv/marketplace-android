package com.company.marketplace.models;

public class ImageOutput {

	private String filename;
	private byte[] bytes;

	public ImageOutput() {
	}
	public ImageOutput(String filename, byte[] bytes) {
		this.filename = filename;
		this.bytes = bytes;
	}

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
