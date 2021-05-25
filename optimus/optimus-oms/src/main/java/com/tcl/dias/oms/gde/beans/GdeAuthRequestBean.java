package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;

/**
 * class contain auth request bean fto invoke MDSO
 * @author archchan
 *
 */
public class GdeAuthRequestBean implements Serializable{
	
	private static final long serialVersionUID = -178049378459393487L;
	
	private String username;
	private String password;
	private String tenant;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTenant() {
		return tenant;
	}
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}	
	

}
