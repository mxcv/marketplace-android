package com.company.marketplace.repositories.jwt;

public interface JwtRepository {

	String getToken(JwtType type);
	void setToken(JwtType type, String token);
}
