package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to track and complete Lm Delivery
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class LmDelivery extends TaskDetailsBaseBean {

	private List<LmDeliveryAttributesBean> attributes;
	private String status;
	private String action;
	private HandoffDetailsBean handoffDetails;
	private List<AttachmentIdBean> documentIds;
	private String mastRequired;
	private String reasonForMast;
	private Integer mastHeight;
	private String mastInstallationDate;
	private String typeOfMastAntennaErection;
	private String fotRequired;
	private String raiseSiteRedinessIssue;
	private String siteRedinessIssueDetails;
	private boolean raiseLMJeopardy;
	private String lmJeoPardyIssueDetails;
	private String mastOwner;
	private String poleHeight;
	private String typeOfPoleErection ;
	private String supplierBillStartDate ;
	private String cableLengthCustomerServerRoom  ;
	private String bsoCircuitId ;
	private String addtionalHardwareAtCustomer ;
	private String neededHardware ;
	private String neededHardwareOwner;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String providerOrderLogDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String lMTestCompletionDate;
	private String otherId;
	private String provideOrderreferenceId;
	private String testIp;
	private String supplierCategory;
	
	
	
	public String getTestIp() {
		return testIp;
	}
	public void setTestIp(String testIp) {
		this.testIp = testIp;
	}
	
	public String getNeededHardwareOwner() {
		return neededHardwareOwner;
	}
	public void setNeededHardwareOwner(String neededHardwareOwner) {
		this.neededHardwareOwner = neededHardwareOwner;
	}
	public String getlMTestCompletionDate() {
		return lMTestCompletionDate;
	}
	public void setlMTestCompletionDate(String lMTestCompletionDate) {
		this.lMTestCompletionDate = lMTestCompletionDate;
	}
	public String getProviderOrderLogDate() {
		return providerOrderLogDate;
	}
	public void setProviderOrderLogDate(String providerOrderLogDate) {
		this.providerOrderLogDate = providerOrderLogDate;
	}
	public String getOtherId() {
		return otherId;
	}
	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}
	public String getProvideOrderreferenceId() {
		return provideOrderreferenceId;
	}
	public void setProvideOrderreferenceId(String provideOrderreferenceId) {
		this.provideOrderreferenceId = provideOrderreferenceId;
	}

	
	public String getMastOwner() {
		return mastOwner;
	}
	public void setMastOwner(String mastOwner) {
		this.mastOwner = mastOwner;
	}
	public String getPoleHeight() {
		return poleHeight;
	}
	public void setPoleHeight(String poleHeight) {
		this.poleHeight = poleHeight;
	}
	public String getTypeOfPoleErection() {
		return typeOfPoleErection;
	}
	public void setTypeOfPoleErection(String typeOfPoleErection) {
		this.typeOfPoleErection = typeOfPoleErection;
	}
	public List<LmDeliveryAttributesBean> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<LmDeliveryAttributesBean> attributes) {
		this.attributes = attributes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public HandoffDetailsBean getHandoffDetails() {
		return handoffDetails;
	}
	public void setHandoffDetails(HandoffDetailsBean handoffDetails) {
		this.handoffDetails = handoffDetails;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getMastRequired() {
		return mastRequired;
	}

	public void setMastRequired(String mastRequired) {
		this.mastRequired = mastRequired;
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

	public String getFotRequired() {
		return fotRequired;
	}

	public void setFotRequired(String fotRequired) {
		this.fotRequired = fotRequired;
	}

	public String getRaiseSiteRedinessIssue() {
		return raiseSiteRedinessIssue;
	}

	public void setRaiseSiteRedinessIssue(String raiseSiteRedinessIssue) {
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

	public String getSupplierBillStartDate() {
		return supplierBillStartDate;
	}

	public void setSupplierBillStartDate(String supplierBillStartDate) {
		this.supplierBillStartDate = supplierBillStartDate;
	}

	public String getBsoCircuitId() {
		return bsoCircuitId;
	}

	public void setBsoCircuitId(String bsoCircuitId) {
		this.bsoCircuitId = bsoCircuitId;
	}

	public String getCableLengthCustomerServerRoom() {
		return cableLengthCustomerServerRoom;
	}

	public void setCableLengthCustomerServerRoom(String cableLengthCustomerServerRoom) {
		this.cableLengthCustomerServerRoom = cableLengthCustomerServerRoom;
	}

	public String getAddtionalHardwareAtCustomer() {
		return addtionalHardwareAtCustomer;
	}

	public void setAddtionalHardwareAtCustomer(String addtionalHardwareAtCustomer) {
		this.addtionalHardwareAtCustomer = addtionalHardwareAtCustomer;
	}

	public String getNeededHardware() {
		return neededHardware;
	}

	public void setNeededHardware(String neededHardware) {
		this.neededHardware = neededHardware;
	}
	public String getSupplierCategory() {
		return supplierCategory;
	}

	public void setSupplierCategory(String supplierCategory) {
		this.supplierCategory = supplierCategory;
	}
}
