package com.tcl.dias.oms.ipc.beans;

public class SecurityGroupCatalystBean {

	private String location;
	
	private String customer;
	
	private String service;
	
	private String hypervisor;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}
	
}
