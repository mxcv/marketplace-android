package com.company.marketplace.network.jwt;

import android.content.Context;
import android.util.Log;

import com.company.marketplace.models.AccessRefreshJwt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileJwtRepository implements JwtRepository {

	private static final String ACCESS_TOKEN_FILENAME = "access_token.txt";
	private static final String REFRESH_TOKEN_FILENAME = "refresh_token.txt";
	private static final String FILES_DIRECTORY = "files";
	private final Context context;

	public FileJwtRepository(Context context) {
		this.context = context;
	}

	@Override
	public String getToken(JwtType type) {
		try (FileInputStream stream = context.openFileInput(
				type == JwtType.ACCESS ? ACCESS_TOKEN_FILENAME : REFRESH_TOKEN_FILENAME)) {
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
		String fileName = type == JwtType.ACCESS ? ACCESS_TOKEN_FILENAME : REFRESH_TOKEN_FILENAME;
		if (token == null) {
			Paths.get(context.getApplicationInfo().dataDir, FILES_DIRECTORY, fileName)
				.toFile()
				.delete();
			Log.i("JWT", (type == JwtType.ACCESS ? "Access" : "Refresh") + " token was removed.");
		}
		else {
			try (FileOutputStream stream = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
				stream.write(token.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("JWT", (type == JwtType.ACCESS ? "Access" : "Refresh") + " token was updated.");
		}
	}

	@Override
	public AccessRefreshJwt getTokens() {
		return new AccessRefreshJwt(getToken(JwtType.ACCESS), getToken(JwtType.REFRESH));
	}

	@Override
	public void setTokens(AccessRefreshJwt jwt) {
		if (jwt == null)
			jwt = new AccessRefreshJwt();
		setToken(JwtType.ACCESS, jwt.getAccessToken());
		setToken(JwtType.REFRESH, jwt.getRefreshToken());
	}
}
