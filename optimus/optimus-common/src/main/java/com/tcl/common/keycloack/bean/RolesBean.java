package com.tcl.common.keycloack.bean;

import java.io.Serializable;

public class RolesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
