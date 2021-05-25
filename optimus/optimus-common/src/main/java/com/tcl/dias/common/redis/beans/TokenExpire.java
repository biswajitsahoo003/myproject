package com.tcl.dias.common.redis.beans;

import java.io.Serializable;
import java.sql.Timestamp;

public class TokenExpire implements Serializable {

	private static final long serialVersionUID = -2639849622091010042L;
	private Timestamp actualTimeStamp;
	private String token;
	private boolean isChild;

	public Timestamp getActualTimeStamp() {
		return actualTimeStamp;
	}

	public void setActualTimeStamp(Timestamp actualTimeStamp) {
		this.actualTimeStamp = actualTimeStamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isChild() {
		return isChild;
	}

	public void setChild(boolean isChild) {
		this.isChild = isChild;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "TokenExpire [actualTimeStamp=" + actualTimeStamp + ", token=" + token + ", isChild=" + isChild + "]";
	}

}
