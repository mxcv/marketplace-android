package com.company.marketplace.network.repositories;

import android.content.Context;
import android.util.Log;

import com.company.marketplace.models.AccessRefreshJwt;
import com.company.marketplace.models.JwtType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@SuppressWarnings({"ResultOfMethodCallIgnored", "FieldCanBeLocal"})
public class JwtRepository {

	private static JwtRepository instance;
	private final String accessTokenFilename = "access_token.txt";
	private final String refreshTokenFilename = "refresh_token.txt";
	private final String filesDirectory = "files";
	private final Context context;

	private JwtRepository(Context context) {
		this.context = context.getApplicationContext();
	}

	public static synchronized void initialize(Context context) {
		if (instance == null)
			instance = new JwtRepository(context);
	}

	public static synchronized JwtRepository get() {
		return instance;
	}

	public synchronized String getToken(JwtType type) {
		try (FileInputStream stream = context.openFileInput(
			type == JwtType.ACCESS ? accessTokenFilename : refreshTokenFilename)) {
			byte[] b = new byte[stream.available()];
			stream.read(b);
			return new String(b);
		}
		catch (FileNotFoundException e) {
			return null;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public synchronized void setToken(JwtType type, String token) {
		String fileName = type == JwtType.ACCESS ? accessTokenFilename : refreshTokenFilename;
		if (token == null) {
			Paths.get(context.getApplicationInfo().dataDir, filesDirectory, fileName)
				.toFile()
				.delete();
			Log.i("JWT", (type == JwtType.ACCESS ? "Access" : "Refresh") + " token was removed.");
		}
		else {
			try (FileOutputStream stream = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
				stream.write(token.getBytes());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			Log.i("JWT", (type == JwtType.ACCESS ? "Access" : "Refresh") + " token was updated.");
		}
	}

	public AccessRefreshJwt getTokens() {
		return new AccessRefreshJwt(getToken(JwtType.ACCESS), getToken(JwtType.REFRESH));
	}

	public void setTokens(AccessRefreshJwt jwt) {
		if (jwt == null)
			jwt = new AccessRefreshJwt();
		setToken(JwtType.ACCESS, jwt.getAccessToken());
		setToken(JwtType.REFRESH, jwt.getRefreshToken());
	}
}
