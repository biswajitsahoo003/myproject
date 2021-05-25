package com.tcl.common.keycloack.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "access_token",
    "expires_in",
    "refresh_expires_in",
    "refresh_token",
    "token_type",
    "not-before-policy",
    "session_state",
    "scope"
})
public class keycloakAuthBean {
	
	@JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("expires_in")
    private String expiresIn;
    @JsonProperty("refresh_expires_in")
    private String refreshExpiresIn;
    @JsonProperty("not-before-policy")
    private String notBeforePolicy;
    @JsonProperty("session_state")
    private String sessionState;
    @JsonProperty("scope")
    private String scope;
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getRefreshExpiresIn() {
		return refreshExpiresIn;
	}
	public void setRefreshExpiresIn(String refreshExpiresIn) {
		this.refreshExpiresIn = refreshExpiresIn;
	}
	public String getNotBeforePolicy() {
		return notBeforePolicy;
	}
	public void setNotBeforePolicy(String notBeforePolicy) {
		this.notBeforePolicy = notBeforePolicy;
	}
	public String getSessionState() {
		return sessionState;
	}
	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
    

}
