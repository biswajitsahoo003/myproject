package com.tcl.common.keycloack.bean;

import java.io.Serializable;

public class UserRoleMappingBean implements Serializable{
	private String id;
	private String name;
	private Boolean composite;
	private Boolean clientRole;
	private String containerId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getComposite() {
		return composite;
	}
	public void setComposite(Boolean composite) {
		this.composite = composite;
	}
	
	public Boolean getClientRole() {
		return clientRole;
	}
	public void setClientRole(Boolean clientRole) {
		this.clientRole = clientRole;
	}
	public String getContainerId() {
		return containerId;
	}
	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}
	
}
