package com.tcl.dias.serviceinventory.izosdwan.beans.performance_parameters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteUtilizationRequest {
	
	private String api_type;
	
	private String cpename;
	
	private String customername;
	
	private String device_ip;
	
	private String requestid;
	
	private String vd_ip;

	private String vd_port;

	public String getApi_type() {
		return api_type;
	}

	public void setApi_type(String api_type) {
		this.api_type = api_type;
	}

	public String getCpename() {
		return cpename;
	}

	public void setCpename(String cpename) {
		this.cpename = cpename;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getDevice_ip() {
		return device_ip;
	}

	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}

	public String getRequestid() {
		return requestid;
	}

	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}

	public String getVd_ip() {
		return vd_ip;
	}

	public void setVd_ip(String vd_ip) {
		this.vd_ip = vd_ip;
	}

	public String getVd_port() {
		return vd_port;
	}

	public void setVd_port(String vd_port) {
		this.vd_port = vd_port;
	}

		

}
