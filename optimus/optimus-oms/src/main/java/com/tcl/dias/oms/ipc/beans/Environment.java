package com.tcl.dias.oms.ipc.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Environment {

	@JsonProperty("departmentId")
	private String departmentId;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("id")
	private String id;

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
