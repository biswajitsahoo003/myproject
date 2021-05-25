package com.tcl.dias.common.beans;

import java.io.Serializable;

public class KeycloakIdentityProviderBean implements Serializable{
	
	private String identityProviderType;
	private String identityProviderUserId;
	private String identityProviderUserName;
	public String getIdentityProviderType() {
		return identityProviderType;
	}
	public void setIdentityProviderType(String identityProviderType) {
		this.identityProviderType = identityProviderType;
	}
	public String getIdentityProviderUserId() {
		return identityProviderUserId;
	}
	public void setIdentityProviderUserId(String identityProviderUserId) {
		this.identityProviderUserId = identityProviderUserId;
	}
	public String getIdentityProviderUserName() {
		return identityProviderUserName;
	}
	public void setIdentityProviderUserName(String identityProviderUserName) {
		this.identityProviderUserName = identityProviderUserName;
	}
	

}
