package com.tcl.dias.auth.beans;

import com.tcl.dias.common.utils.Utils;

/**
 * 
 * @author Manojkumar R
 *
 */
public class LoginRequest {

	private String username;
	private String password;

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

	@Override
	public String toString() {
		return "LoginRequest [username=" + username + ", password=" + Utils.maskPassword(password) + "]";
	}

}
