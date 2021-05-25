package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to save Offnet Site Survey Details
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class OffnetSiteSurvey extends TaskDetailsBaseBean {

	private List<AttachmentIdBean> documentIds;
	private String mastRequired;
	private String reasonForMast;
	private Integer mastHeight;
	private String mastInstallationDate;
	private String typeOfMastAntennaErection;
	private String fotRequired;
	private boolean raiseSiteRedinessIssue;
	private String siteRedinessIssueDetails;
	private boolean raiseLMJeopardy;
	private String lmJeoPardyIssueDetails;
	
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(List<AttachmentIdBean> documentId) {
		this.documentIds = documentId;
	}
	public String getMastRequired() {
		return mastRequired;
	}
	public void setMastRequired(String mastRequired) {
		this.mastRequired = mastRequired;
	}
	public String getFotRequired() {
		return fotRequired;
	}
	public void setFotRequired(String fotRequired) {
		this.fotRequired = fotRequired;
	}
	public boolean isRaiseSiteRedinessIssue() {
		return raiseSiteRedinessIssue;
	}
	public void setRaiseSiteRedinessIssue(boolean raiseSiteRedinessIssue) {
		this.raiseSiteRedinessIssue = raiseSiteRedinessIssue;
	}
	public String getSiteRedinessIssueDetails() {
		return siteRedinessIssueDetails;
	}
	public void setSiteRedinessIssueDetails(String siteRedinessIssueDetails) {
		this.siteRedinessIssueDetails = siteRedinessIssueDetails;
	}
	public boolean isRaiseLMJeopardy() {
		return raiseLMJeopardy;
	}
	public void setRaiseLMJeopardy(boolean raiseLMJeopardy) {
		this.raiseLMJeopardy = raiseLMJeopardy;
	}
	public String getLmJeoPardyIssueDetails() {
		return lmJeoPardyIssueDetails;
	}
	public void setLmJeoPardyIssueDetails(String lmJeoPardyIssueDetails) {
		this.lmJeoPardyIssueDetails = lmJeoPardyIssueDetails;
	}
	public String getReasonForMast() {
		return reasonForMast;
	}
	public void setReasonForMast(String reasonForMast) {
		this.reasonForMast = reasonForMast;
	}
	public Integer getMastHeight() {
		return mastHeight;
	}
	public void setMastHeight(Integer mastHeight) {
		this.mastHeight = mastHeight;
	}
	public String getMastInstallationDate() {
		return mastInstallationDate;
	}
	public void setMastInstallationDate(String mastInstallationDate) {
		this.mastInstallationDate = mastInstallationDate;
	}
	public String getTypeOfMastAntennaErection() {
		return typeOfMastAntennaErection;
	}
	public void setTypeOfMastAntennaErection(String typeOfMastAntennaErection) {
		this.typeOfMastAntennaErection = typeOfMastAntennaErection;
	}
	
	
}
