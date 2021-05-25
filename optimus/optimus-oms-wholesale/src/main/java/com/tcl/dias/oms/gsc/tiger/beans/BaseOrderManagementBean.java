package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.List;

public abstract class BaseOrderManagementBean {
	private String corrID;
	private String optimusId;
	private String state;
	private String description;
	private BusinessEntity technicalEntity;
	private BillingEntity billingEntity;
	private String requestedCompletionDate;
	private String expectedCompletionDate;
	private List<Party> relatedParties;
	private String vatNumber;
	private String crn;
	private String custPONumber;
	private ContractingEntity contractingEntity;
	private String productIdentifier;
	private String solutionId;
	private String solutionName;
	private String orderName;
	private String notes;

	public String getCorrID() {
		return corrID;
	}

	public void setCorrID(String corrID) {
		this.corrID = corrID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProductIdentifier() {
		return productIdentifier;
	}

	public void setProductIdentifier(String productIdentifier) {
		this.productIdentifier = productIdentifier;
	}

	public String getRequestedCompletionDate() {
		return requestedCompletionDate;
	}

	public void setRequestedCompletionDate(String requestedCompletionDate) {
		this.requestedCompletionDate = requestedCompletionDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getOptimusId() {
		return optimusId;
	}

	public void setOptimusId(String optimusID) {
		this.optimusId = optimusID;
	}

	public String getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public BusinessEntity getTechnicalEntity() {
		return technicalEntity;
	}

	public void setTechnicalEntity(BusinessEntity technicalEntity) {
		this.technicalEntity = technicalEntity;
	}

	public BillingEntity getBillingEntity() {
		return billingEntity;
	}

	public void setBillingEntity(BillingEntity billingEntity) {
		this.billingEntity = billingEntity;
	}

	public ContractingEntity getContractingEntity() {
		return contractingEntity;
	}

	public void setContractingEntity(ContractingEntity contractingEntity) {
		this.contractingEntity = contractingEntity;
	}

	public List<Party> getRelatedParties() {
		return relatedParties;
	}

	public void setRelatedParties(List<Party> relatedParties) {
		this.relatedParties = relatedParties;
	}

	public String getExpectedCompletionDate() {
		return expectedCompletionDate;
	}

	public void setExpectedCompletionDate(String expectedCompletionDate) {
		this.expectedCompletionDate = expectedCompletionDate;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public String getCrn() {
		return crn;
	}

	public void setCrn(String crn) {
		this.crn = crn;
	}

	public String getCustPONumber() {
		return custPONumber;
	}

	public void setCustPONumber(String custPONumber) {
		this.custPONumber = custPONumber;
	}

}
