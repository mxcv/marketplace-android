package com.company.marketplace.repositories.jwt;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileJwtRepository implements JwtRepository {

	private static final String ACCESS_TOKEN_PATH = "access_token.txt";
	private static final String REFRESH_TOKEN_PATH = "refresh_token.txt";
	private final Context context;

	public FileJwtRepository(Context context) {
		this.context = context;
	}

	@Override
	public String getToken(JwtType type) {
		try (FileInputStream stream = context.openFileInput(getPath(type))) {
			byte[] b = new byte[stream.available()];
			stream.read(b);
			return new String(b);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setToken(JwtType type, String token) {
		if (token == null)
			new File(getPath(type)).delete();
		else {
			try (FileOutputStream stream = context.openFileOutput(getPath(type), Context.MODE_PRIVATE)) {
				stream.write(token.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getPath(JwtType type) {
		return type == JwtType.ACCESS ? ACCESS_TOKEN_PATH : REFRESH_TOKEN_PATH;
	}
}
