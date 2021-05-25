package com.tcl.dias.common.servicefulfillment.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class CpeBomResource {

	private Integer id;

	private String bomName;

	private String uniCode;

	private List<BomResources> resources;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getUniCode() {
		return uniCode;
	}

	public void setUniCode(String uniCode) {
		this.uniCode = uniCode;
	}

	public List<BomResources> getResources() {

		if (resources == null) {
			resources = new ArrayList<BomResources>();
		}
		return resources;
	}

	public void setResources(List<BomResources> resources) {
		this.resources = resources;
	}

}
