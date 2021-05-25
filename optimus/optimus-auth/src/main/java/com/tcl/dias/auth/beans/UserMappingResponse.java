package com.tcl.dias.auth.beans;

public class UserMappingResponse {

	private Integer id;
	private String entityName;

	public UserMappingResponse() {
		// DO NOTHING
	}

	public UserMappingResponse(Integer id, String entityName) {
		this.id = id;
		this.entityName = entityName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@Override
	public String toString() {
		return "UserMappingResponse [id=" + id + ", entityName=" + entityName + "]";
	}

}
