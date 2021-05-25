package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

public class SetCLRSyncBean implements Serializable {

	private static final long serialVersionUID = -1900703592960964669L;
	private CramerServiceHeader cramerServiceHeader;
	private String objectName;
	private String objectType;
	private String initialRelationship;
	private String finalRelationship;
	
	private Integer serviceId;
	private String orderCode;
	
	
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public CramerServiceHeader getCramerServiceHeader() {
		return cramerServiceHeader;
	}
	public void setCramerServiceHeader(CramerServiceHeader cramerServiceHeader) {
		this.cramerServiceHeader = cramerServiceHeader;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getInitialRelationship() {
		return initialRelationship;
	}
	public void setInitialRelationship(String initialRelationship) {
		this.initialRelationship = initialRelationship;
	}
	public String getFinalRelationship() {
		return finalRelationship;
	}
	public void setFinalRelationship(String finalRelationship) {
		this.finalRelationship = finalRelationship;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}	

}
