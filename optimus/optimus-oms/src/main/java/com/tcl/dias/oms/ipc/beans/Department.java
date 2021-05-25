package com.tcl.dias.oms.ipc.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Department {

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("id")
	private String id;

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
