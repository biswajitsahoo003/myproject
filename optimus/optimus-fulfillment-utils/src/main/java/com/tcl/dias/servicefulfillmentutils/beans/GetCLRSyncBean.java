package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

public class GetCLRSyncBean implements Serializable{

	private static final long serialVersionUID = 1270471205754393558L;
	
	private CramerServiceHeader cramerServiceHeader;
	private String objectType;
	private String objectName;
	private String relationshipType;
	public CramerServiceHeader getCramerServiceHeader() {
		return cramerServiceHeader;
	}
	public void setCramerServiceHeader(CramerServiceHeader cramerServiceHeader) {
		this.cramerServiceHeader = cramerServiceHeader;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getRelationshipType() {
		return relationshipType;
	}
	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}
	
	

}
