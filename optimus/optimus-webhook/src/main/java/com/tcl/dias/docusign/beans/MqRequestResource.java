package com.tcl.dias.docusign.beans;

/**
 * Customer MQ Request Resource
 * 
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MqRequestResource<T> {

	private T request;
	private String mdcFilterToken;
	private String username;
	
	public MqRequestResource() {
		this.request = null;
		this.mdcFilterToken ="";
		this.username = "admin";
	}

	public MqRequestResource(T request, String mdcToken, String username) {
		this.request = request;
		this.mdcFilterToken = mdcToken;
		this.username = username;
	}

	public MqRequestResource(T request) {
		this.request = request;
		this.mdcFilterToken = "";
		this.username = "admin";
	}

	public T getRequest() {
		return request;
	}

	public void setRequest(T request) {
		this.request = request;
	}

	public String getMdcFilterToken() {
		return mdcFilterToken;
	}

	public void setMdcFilterToken(String mdcFilterToken) {
		this.mdcFilterToken = mdcFilterToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "MqRequestResource [request=" + request + ", mdcFilterToken=" + mdcFilterToken + ", username=" + username
				+ "]";
	}

}
