package com.tcl.dias.oms.gsc.tiger.beans;

import java.util.Date;
import java.util.List;

/**
 * Base bean for order management response
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public abstract class BaseOrderManagementResponse extends TigerServiceResponse {
	private String corrID;
	private String description;
	private String orderId;
	private String orderName;
	private String state;
	private String productIdentifier;
	private Date requestedCompletionDate;
	private String notes;
	private BusinessEntity technicalEntity;
	private BillingEntity billingEntity;
	private ContractingEntity contractingEntity;
	private List<Party> relatedParties;
	private SubOrderDetails subOrderDetails;

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

	public Date getRequestedCompletionDate() {
		return requestedCompletionDate;
	}

	public void setRequestedCompletionDate(Date requestedCompletionDate) {
		this.requestedCompletionDate = requestedCompletionDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public SubOrderDetails getSubOrderDetails() {
		return subOrderDetails;
	}

	public void setSubOrderDetails(SubOrderDetails subOrderDetails) {
		this.subOrderDetails = subOrderDetails;
	}
}
