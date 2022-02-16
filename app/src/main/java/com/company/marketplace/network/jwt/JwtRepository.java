package com.company.marketplace.network.jwt;

import com.company.marketplace.models.AccessRefreshJwt;

public interface JwtRepository {

	String getToken(JwtType type);
	void setToken(JwtType type, String token);

	AccessRefreshJwt getTokens();
	void setTokens(AccessRefreshJwt jwt);
}
