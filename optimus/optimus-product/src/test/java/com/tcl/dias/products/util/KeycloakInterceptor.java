package com.tcl.dias.products.util;

import java.io.IOException;

import org.keycloak.common.util.Time;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;


public class KeycloakInterceptor implements ClientHttpRequestInterceptor {

	private static final String AUTHORIZATION_HEADER = "Authorization";

	private String authServer;

	private String realm;

	private String resource;

	private String keycloakTestUser;

	private String keycloakTestPassword;
	
	private AccessTokenResponse token;
	
	private static final int firstRequestedTime = Time.currentTime();

	
	public KeycloakInterceptor(String authServer, String realm, String resource, String keycloakTestUser,
			String keycloakTestPassword) {
		super();
		this.authServer = authServer;
		this.realm = realm;
		this.resource = resource;
		this.keycloakTestUser = keycloakTestUser;
		this.keycloakTestPassword = keycloakTestPassword;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
			ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
		int currentTime = Time.currentTime();
		if(token == null || currentTime - firstRequestedTime >= token.getExpiresIn()) {
			token = getAccessTokenResponse();
		}
		httpRequest.getHeaders().set(AUTHORIZATION_HEADER, "Bearer " + token.getToken());
		return clientHttpRequestExecution.execute(httpRequest, bytes);
	}

	private AccessTokenResponse getAccessTokenResponse() {
		return null;//Keycloak.getInstance(authServer, realm, keycloakTestUser, keycloakTestPassword, resource).tokenManager()
				//.getAccessToken();
	}
}


