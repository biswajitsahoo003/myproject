package com.tcl.dias.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * used to get the ticket details in service request management
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketsBean {

	private String ticketId;
	private String catalogName;
	@JsonProperty("commonVariables")
	private CommonVariablesBean commonVariables;
	@JsonProperty("additionalVariables")

	private AdditionalVariablesBean additionalVariables;
	private String approvalFlag;
	private String approvalStatus;

	private String customerActionRequired;

	private Rfoinformation rfoinformation;

	/**
	 * @return customerActionRequired
	 */
	public String getCustomerActionRequired() {
		return customerActionRequired;
	}

	/**
	 * @param customerActionRequired
	 *            the customerActionRequired to set
	 */
	public void setCustomerActionRequired(String customerActionRequired) {
		this.customerActionRequired = customerActionRequired;
	}

	@JsonProperty("requestMetaData")
	private RequestMetadataBean requestMetaData;
	private List<NotesBean> notes;

	/**
	 * @return the ticketId
	 */
	public String getTicketId() {
		return ticketId;
	}

	/**
	 * @param ticketId
	 *            the ticketId to set
	 */
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	/**
	 * @return the catalogName
	 */
	public String getCatalogName() {
		return catalogName;
	}

	/**
	 * @param catalogName
	 *            the catalogName to set
	 */
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	/**
	 * @return the commonVariables
	 */
	@JsonProperty("commonVariables")

	public CommonVariablesBean getCommonVariables() {
		return commonVariables;
	}

	/**
	 * @param commonVariables
	 *            the commonVariables to set
	 */
	@JsonProperty("commonVariables")

	public void setCommonVariables(CommonVariablesBean commonVariables) {
		this.commonVariables = commonVariables;
	}

	/**
	 * @return the approvalFlag
	 */
	public String getApprovalFlag() {
		return approvalFlag;
	}

	/**
	 * @param approvalFlag
	 *            the approvalFlag to set
	 */
	public void setApprovalFlag(String approvalFlag) {
		this.approvalFlag = approvalFlag;
	}

	/**
	 * @return the notes
	 */
	public List<NotesBean> getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(List<NotesBean> notes) {
		this.notes = notes;
	}

	/**
	 * @return the requestMetaData
	 */
	@JsonProperty("requestMetaData")
	public RequestMetadataBean getRequestMetaData() {
		return requestMetaData;
	}

	/**
	 * @param requestMetaData
	 *            the requestMetaData to set
	 */
	@JsonProperty("requestMetaData")
	public void setRequestMetaData(RequestMetadataBean requestMetaData) {
		this.requestMetaData = requestMetaData;
	}

	/**
	 * @return the approvalStatus
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}

	/**
	 * @param approvalStatus
	 *            the approvalStatus to set
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	@JsonProperty("additionalVariables")

	public AdditionalVariablesBean getAdditionalVariables() {
		return additionalVariables;
	}

	@JsonProperty("additionalVariables")

	public void setAdditionalVariables(AdditionalVariablesBean additionalVariables) {
		this.additionalVariables = additionalVariables;
	}

	/**
	 * @return the rfo info of a ticket
	 */
	public Rfoinformation getRfoinformation() {
		return rfoinformation;
	}

	/**
	 * @param rfoinformation
	 *            the rfo to set
	 */
	public void setRfoinformation(Rfoinformation rfoinformation) {
		this.rfoinformation = rfoinformation;
	}

}
