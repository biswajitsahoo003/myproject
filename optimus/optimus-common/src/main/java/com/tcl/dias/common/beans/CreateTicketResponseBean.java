package com.tcl.dias.common.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file contains the CreateTicketResponseBean.java class.
 * 
 * used to get the response of ticket details
 * 
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTicketResponseBean {

	private String status;

	private String message;

	private String correlationId;

	private String creationDate;

	private String ticketStatus;

	private String serviceAlias;

	private String shortDescription;

	private String serviceType;

	private String ticketId;

	private String description;

	private String category;

	private String serviceIdentifier;

	private String impact;

	private String issueOccurenceDate;

	private TicketContactBean contact;
	
	private TicketEscalationInfoBeanInfo escalationInfo;
	
	private String resolutionAcceptanceTime;
	
	private List<TicketNotesBean> notes;
	
	private String updatedDate;
	
	private String serviceDowntimeStart;

	private String serviceDowntimeEnd;

	private String totalOutageDuration;

	private String resolutionAccepted;

	private String rfoAccepted;

	private String statusChangeReason;

	private String customerLeName;

	private String cuid;

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}

	private Rfoinformation rfoinformation;
	
	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getServiceIdentifier() {
		return serviceIdentifier;
	}

	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}

	public String getImpact() {
		return impact;
	}

	public void setImpact(String impact) {
		this.impact = impact;
	}

	public String getIssueOccurenceDate() {
		return issueOccurenceDate;
	}

	public void setIssueOccurenceDate(String issueOccurenceDate) {
		this.issueOccurenceDate = issueOccurenceDate;
	}

	public TicketContactBean getContact() {
		return contact;
	}

	public void setContact(TicketContactBean contact) {
		this.contact = contact;
	}

	public List<TicketNotesBean> getNotes() {
		return notes;
	}

	public void setNotes(List<TicketNotesBean> notes) {
		this.notes = notes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getServiceAlias() {
		return serviceAlias;
	}

	public void setServiceAlias(String serviceAlias) {
		this.serviceAlias = serviceAlias;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public TicketEscalationInfoBeanInfo getEscalationInfo() {
		return escalationInfo;
	}

	public void setEscalationInfo(TicketEscalationInfoBeanInfo escalationInfo) {
		this.escalationInfo = escalationInfo;
	}

	public String getResolutionAcceptanceTime() {
		return resolutionAcceptanceTime;
	}

	public void setResolutionAcceptanceTime(String resolutionAcceptanceTime) {
		this.resolutionAcceptanceTime = resolutionAcceptanceTime;
	}

	/**
	 * @return the serviceDowntimeStart
	 */
	public String getServiceDowntimeStart() {
		return serviceDowntimeStart;
	}

	/**
	 * @param serviceDowntimeStart the serviceDowntimeStart to set
	 */
	public void setServiceDowntimeStart(String serviceDowntimeStart) {
		this.serviceDowntimeStart = serviceDowntimeStart;
	}

	/**
	 * @return the serviceDowntimeEnd
	 */
	public String getServiceDowntimeEnd() {
		return serviceDowntimeEnd;
	}

	/**
	 * @param serviceDowntimeEnd the serviceDowntimeEnd to set
	 */
	public void setServiceDowntimeEnd(String serviceDowntimeEnd) {
		this.serviceDowntimeEnd = serviceDowntimeEnd;
	}

	/**
	 * @return the totalOutageDuration
	 */
	public String getTotalOutageDuration() {
		return totalOutageDuration;
	}

	/**
	 * @param totalOutageDuration the totalOutageDuration to set
	 */
	public void setTotalOutageDuration(String totalOutageDuration) {
		this.totalOutageDuration = totalOutageDuration;
	}

	/**
	 * @return the resolutionAccepted
	 */
	public String getResolutionAccepted() {
		return resolutionAccepted;
	}

	/**
	 * @param resolutionAccepted the resolutionAccepted to set
	 */
	public void setResolutionAccepted(String resolutionAccepted) {
		this.resolutionAccepted = resolutionAccepted;
	}

	/**
	 * @return the rfoAccepted
	 */
	public String getRfoAccepted() {
		return rfoAccepted;
	}

	/**
	 * @param rfoAccepted the rfoAccepted to set
	 */
	public void setRfoAccepted(String rfoAccepted) {
		this.rfoAccepted = rfoAccepted;
	}

	/**
	 * @return the statusChangeReason
	 */
	public String getStatusChangeReason() {
		return statusChangeReason;
	}

	/**
	 * @param statusChangeReason the statusChangeReason to set
	 */
	public void setStatusChangeReason(String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}

	/**
	 * @return the rfoinformation
	 */
	public Rfoinformation getRfoinformation() {
		return rfoinformation;
	}

	/**
	 * @param rfoinformation the rfoinformation to set
	 */
	public void setRfoinformation(Rfoinformation rfoinformation) {
		this.rfoinformation = rfoinformation;
	}
	
}
