package com.tcl.dias.common.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * Bean for l2oReport related to Manual Feasibility response
 *
 * @author krutsrin
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MfL2OReportBean {

	private String feasibilityId;
	private Integer taskId;
	private Integer quoteId;
	private String siteId;
	private Integer quoteTole;
	private String feasibilityResponseId;
	private List<MfAttachmentBean> mfAttachments;
	private List<OmsAttachBean> omsAttachmentDetails;

	
	public String getFeasibilityResponseId() {
		return feasibilityResponseId;
	}

	public void setFeasibilityResponseId(String feasibilityResponseId) {
		this.feasibilityResponseId = feasibilityResponseId;
	}

	

	
    public List<OmsAttachBean> getOmsAttachmentDetails() {
		return omsAttachmentDetails;
	}

	public void setOmsAttachmentDetails(List<OmsAttachBean> omsAttachmentDetails) {
		this.omsAttachmentDetails = omsAttachmentDetails;
	}

	public String getFeasibilityId() {
		return feasibilityId;
	}

	public void setFeasibilityId(String feasibilityId) {
		this.feasibilityId = feasibilityId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public List<MfAttachmentBean> getMfAttachments() {
		return mfAttachments;
	}

	public void setMfAttachments(List<MfAttachmentBean> mfAttachments) {
		this.mfAttachments = mfAttachments;
	}
	
	public Integer getQuoteTole() {
		return quoteTole;
	}

	public void setQuoteTole(Integer quoteTole) {
		this.quoteTole = quoteTole;
	}



}
