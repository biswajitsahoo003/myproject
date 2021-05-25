package com.tcl.dias.oms.gst.redis.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author archchan
 *
 */
public class GstToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2055443593273191709L;

	private String token;

	private Date createdTime;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		return "GstToken [token=" + token + ", createdTime=" + createdTime + "]";
	}

}
