package com.tcl.dias.oms.ipc.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecurityGroupCatalystResponse {

	@JsonProperty("environments")
	private List<Environment> environments;
	
	@JsonProperty("departments")
	private List<Department> departments;
	
	@JsonProperty("zones")
	private List<Zone> zones;

	public List<Environment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<Environment> environments) {
		this.environments = environments;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}
	
	
}
