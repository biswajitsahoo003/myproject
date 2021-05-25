package com.tcl.dias.ticketing.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.beans.NotesBean;

/**
 * used for the updation of ticket
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateSRRequest {

	private String approvalStatus;
	private String approvalFlag;
	private String customerActionRequired;

	private String rfoAcceptedRejected;
	private List<NotesBean> notes;

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
	 * @return customerActionRequired
	 */
	public String getCustomerActionRequired() {
		return customerActionRequired;
	}

	/**
	 * @param customerActionRequired
	 * 
	 *            the customerActionRequired to set
	 */
	public void setCustomerActionRequired(String customerActionRequired) {
		this.customerActionRequired = customerActionRequired;
	}

	/**
	 * @return rfoAcceptedRejected
	 */
	public String getRfoAcceptedRejected() {
		return rfoAcceptedRejected;
	}

	/**
	 * @param rfoAcceptedRejected
	 *            the rfoAcceptedRejected to set
	 */
	public void setRfoAcceptedRejected(String rfoAcceptedRejected) {
		this.rfoAcceptedRejected = rfoAcceptedRejected;
	}

}
