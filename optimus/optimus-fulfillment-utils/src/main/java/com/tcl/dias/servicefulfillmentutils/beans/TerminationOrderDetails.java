package com.tcl.dias.servicefulfillmentutils.beans;

public class TerminationOrderDetails {

	private String orderId;
	private String terminationOrderId;
	private String erfCustCustomerName;
	private String erfCustLeName;
	private String tpsSfdcCuid;
	private String erfCustSpLeName;
	private String sfdcAccountId;
	private String tpsSfdcOptyId;
	private String orderType;
	private String orderCategory;
	private String orderSource;
	private String createdBy;
	
	private TerminationServiceDetails terminationServiceDetails;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTerminationOrderId() {
		return terminationOrderId;
	}

	public void setTerminationOrderId(String terminationOrderId) {
		this.terminationOrderId = terminationOrderId;
	}

	public String getErfCustCustomerName() {
		return erfCustCustomerName;
	}

	public void setErfCustCustomerName(String erfCustCustomerName) {
		this.erfCustCustomerName = erfCustCustomerName;
	}

	public String getErfCustLeName() {
		return erfCustLeName;
	}

	public void setErfCustLeName(String erfCustLeName) {
		this.erfCustLeName = erfCustLeName;
	}

	public String getTpsSfdcCuid() {
		return tpsSfdcCuid;
	}

	public void setTpsSfdcCuid(String tpsSfdcCuid) {
		this.tpsSfdcCuid = tpsSfdcCuid;
	}

	public String getErfCustSpLeName() {
		return erfCustSpLeName;
	}

	public void setErfCustSpLeName(String erfCustSpLeName) {
		this.erfCustSpLeName = erfCustSpLeName;
	}

	public String getSfdcAccountId() {
		return sfdcAccountId;
	}

	public void setSfdcAccountId(String sfdcAccountId) {
		this.sfdcAccountId = sfdcAccountId;
	}

	public String getTpsSfdcOptyId() {
		return tpsSfdcOptyId;
	}

	public void setTpsSfdcOptyId(String tpsSfdcOptyId) {
		this.tpsSfdcOptyId = tpsSfdcOptyId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public TerminationServiceDetails getTerminationServiceDetails() {
		return terminationServiceDetails;
	}

	public void setTerminationServiceDetails(TerminationServiceDetails terminationServiceDetails) {
		this.terminationServiceDetails = terminationServiceDetails;
	}

}
