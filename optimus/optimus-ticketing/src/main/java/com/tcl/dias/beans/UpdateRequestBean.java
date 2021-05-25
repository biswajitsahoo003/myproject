package com.tcl.dias.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the UpdateRequestBean.java class is used for the update
 * request.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRequestBean {

	private String correlationId;

	private String creationDateFrom;

	private String creationDateTo;

	private String ticketId;

	private String impact;

	private String issueType;

	private String ticketCreatedFrom;

	private String ticketCreatedTo;

	private String updateDateTo;

	private String issueCode;

	private String sortBy;

	private String sortOrder;

	private String offset;
	private String updateDateFrom;

	private String ticketStatus;

	private String serviceIdentifier;

	private String serviceType;

	private String serviceAlias;

	private String orgId;

	private String limit;

	private String customerLeName;

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getCreationDateFrom() {
		return creationDateFrom;
	}

	public void setCreationDateFrom(String creationDateFrom) {
		this.creationDateFrom = creationDateFrom;
	}

	public String getCreationDateTo() {
		return creationDateTo;
	}

	public void setCreationDateTo(String creationDateTo) {
		this.creationDateTo = creationDateTo;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getTicketCreatedFrom() {
		return ticketCreatedFrom;
	}

	public void setTicketCreatedFrom(String ticketCreatedFrom) {
		this.ticketCreatedFrom = ticketCreatedFrom;
	}

	public String getTicketCreatedTo() {
		return ticketCreatedTo;
	}

	public void setTicketCreatedTo(String ticketCreatedTo) {
		this.ticketCreatedTo = ticketCreatedTo;
	}

	public String getUpdateDateTo() {
		return updateDateTo;
	}

	public void setUpdateDateTo(String updateDateTo) {
		this.updateDateTo = updateDateTo;
	}

	public String getIssueCode() {
		return issueCode;
	}

	public void setIssueCode(String issueCode) {
		this.issueCode = issueCode;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getUpdateDateFrom() {
		return updateDateFrom;
	}

	public void setUpdateDateFrom(String updateDateFrom) {
		this.updateDateFrom = updateDateFrom;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getServiceIdentifier() {
		return serviceIdentifier;
	}

	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceAlias() {
		return serviceAlias;
	}

	public void setServiceAlias(String serviceAlias) {
		this.serviceAlias = serviceAlias;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}
}
