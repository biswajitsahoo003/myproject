package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 * This class is used to Complete OSP Acceptance Testing
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class CompleteOspAcceptanceBean extends TaskDetailsBaseBean {
	private String ospAcceptanceObservationsRemarks;
	private String reviewerName;
	private String OSPRemarks;
	private String reviewerEmail;
	private String ospAcceptanceReport;
	private String IBDRemarks;
	private String ibdAcceptanceReport;
	private String atFailedReason;
	private String overallAtStatus;
	private String prowStatus;
	private String upsStatus;
	private String earthingStatus;
	private String rackReadiness;
	private String dualPowerStatus;
	private String ibdTestAcceptanceReport;
	private String ospAllCoreTestingStatus;
	private String gisUpdationStatus;

	private List<AttachmentIdBean> documentIds;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String ospAcceptanceDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String ibdAcceptanceDate;
	
	
	public String getOspAcceptanceObservationsRemarks() {
		return ospAcceptanceObservationsRemarks;
	}
	public void setOspAcceptanceObservationsRemarks(String ospAcceptanceObservationsRemarks) {
		this.ospAcceptanceObservationsRemarks = ospAcceptanceObservationsRemarks;
	}
	
	public String getOspAcceptanceDate() {
		return ospAcceptanceDate;
	}
	public void setOspAcceptanceDate(String ospAcceptanceDate) {
		this.ospAcceptanceDate = ospAcceptanceDate;
	}
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
	public String getOSPRemarks() {
		return OSPRemarks;
	}
	public void setOSPRemarks(String oSPRemarks) {
		OSPRemarks = oSPRemarks;
	}
	public String getReviewerEmail() {
		return reviewerEmail;
	}
	public void setReviewerEmail(String reviewerEmail) {
		this.reviewerEmail = reviewerEmail;
	}
	public String getOspAcceptanceReport() {
		return ospAcceptanceReport;
	}
	public void setOspAcceptanceReport(String ospAcceptanceReport) {
		this.ospAcceptanceReport = ospAcceptanceReport;
	}
	public String getIBDRemarks() {
		return IBDRemarks;
	}
	public void setIBDRemarks(String iBDRemarks) {
		IBDRemarks = iBDRemarks;
	}
	public String getIbdAcceptanceReport() {
		return ibdAcceptanceReport;
	}
	public void setIbdAcceptanceReport(String ibdAcceptanceReport) {
		this.ibdAcceptanceReport = ibdAcceptanceReport;
	}
	public String getAtFailedReason() {
		return atFailedReason;
	}
	public void setAtFailedReason(String atFailedReason) {
		this.atFailedReason = atFailedReason;
	}
	public String getOverallAtStatus() {
		return overallAtStatus;
	}
	public void setOverallAtStatus(String overallAtStatus) {
		this.overallAtStatus = overallAtStatus;
	}
	public String getProwStatus() {
		return prowStatus;
	}
	public void setProwStatus(String prowStatus) {
		this.prowStatus = prowStatus;
	}
	public String getUpsStatus() {
		return upsStatus;
	}
	public void setUpsStatus(String upsStatus) {
		this.upsStatus = upsStatus;
	}
	public String getEarthingStatus() {
		return earthingStatus;
	}
	public void setEarthingStatus(String earthingStatus) {
		this.earthingStatus = earthingStatus;
	}
	public String getRackReadiness() {
		return rackReadiness;
	}
	public void setRackReadiness(String rackReadiness) {
		this.rackReadiness = rackReadiness;
	}
	public String getDualPowerStatus() {
		return dualPowerStatus;
	}
	public void setDualPowerStatus(String dualPowerStatus) {
		this.dualPowerStatus = dualPowerStatus;
	}
	public String getIbdTestAcceptanceReport() {
		return ibdTestAcceptanceReport;
	}
	public void setIbdTestAcceptanceReport(String ibdTestAcceptanceReport) {
		this.ibdTestAcceptanceReport = ibdTestAcceptanceReport;
	}
	public String getOspAllCoreTestingStatus() {
		return ospAllCoreTestingStatus;
	}
	public void setOspAllCoreTestingStatus(String ospAllCoreTestingStatus) {
		this.ospAllCoreTestingStatus = ospAllCoreTestingStatus;
	}
	public String getGisUpdationStatus() {
		return gisUpdationStatus;
	}
	public void setGisUpdationStatus(String gisUpdationStatus) {
		this.gisUpdationStatus = gisUpdationStatus;
	}
	public String getIbdAcceptanceDate() {
		return ibdAcceptanceDate;
	}
	public void setIbdAcceptanceDate(String ibdAcceptanceDate) {
		this.ibdAcceptanceDate = ibdAcceptanceDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
