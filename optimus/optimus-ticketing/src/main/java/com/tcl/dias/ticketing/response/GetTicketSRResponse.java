package com.tcl.dias.ticketing.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.beans.AdditionalVariablesBean;
import com.tcl.dias.beans.CommonVariablesBean;
import com.tcl.dias.beans.NotesBean;
import com.tcl.dias.beans.RequestMetadataBean;
import com.tcl.dias.beans.Rfoinformation;

/**

 * 
 * used to send the response of get ticket by ticket id
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTicketSRResponse {
	
	private String status;
	private String message;
	private String catalogName;
	private CommonVariablesBean commonVariables;
	private AdditionalVariablesBean additionalVariables;
	private String approvalFlag;
	private String approvalStatus;
	private List<NotesBean> notes;
	private RequestMetadataBean requestMetaData;
	private Rfoinformation rfoinformation;
	private String customerActionRequired;
  
	public Rfoinformation getRfoinformation() {
		return rfoinformation;
	}
	public void setRfoinformation(Rfoinformation rfoinformation) {
		this.rfoinformation = rfoinformation;
	}
	private String customerActionRquired;
	
	
	public String getCustomerActionRquired() {
		return customerActionRquired;
	}
	public void setCustomerActionRquired(String customerActionRquired) {
		this.customerActionRquired = customerActionRquired;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the catalogName
	 */
	public String getCatalogName() {
		return catalogName;
	}
	/**
	 * @param catalogName the catalogName to set
	 */
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	/**
	 * @return the commonVariables
	 */
	public CommonVariablesBean getCommonVariables() {
		return commonVariables;
	}
	/**
	 * @param commonVariables the commonVariables to set
	 */
	public void setCommonVariables(CommonVariablesBean commonVariables) {
		this.commonVariables = commonVariables;
	}
	/**
	 * @return the additionalVariables
	 */
	public AdditionalVariablesBean getAdditionalVariables() {
		return additionalVariables;
	}
	/**
	 * @param additionalVariables the additionalVariables to set
	 */
	public void setAdditionalVariables(AdditionalVariablesBean additionalVariables) {
		this.additionalVariables = additionalVariables;
	}
	/**
	 * @return the approvalFlag
	 */
	public String getApprovalFlag() {
		return approvalFlag;
	}
	/**
	 * @param approvalFlag the approvalFlag to set
	 */
	public void setApprovalFlag(String approvalFlag) {
		this.approvalFlag = approvalFlag;
	}
	/**
	 * @return the approvalStatus
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * @param approvalStatus the approvalStatus to set
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	/**
	 * @return the notes
	 */
	public List<NotesBean> getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(List<NotesBean> notes) {
		this.notes = notes;
	}
	/**
	 * @return the requestMetaData
	 */
	public RequestMetadataBean getRequestMetaData() {
		return requestMetaData;
	}
	/**
	 * @param requestMetaData the requestMetaData to set
	 */
	public void setRequestMetaData(RequestMetadataBean requestMetaData) {
		this.requestMetaData = requestMetaData;
	}
	/**
	 * @return the customerActionRequired
	 */
	public String getCustomerActionRequired() {
		return customerActionRequired;
	}
	/**
	 * @param customerActionRequired the customerActionRequired to set
	 */
	public void setCustomerActionRequired(String customerActionRequired) {
		this.customerActionRequired = customerActionRequired;
	}
	
}
