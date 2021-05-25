package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class AdvancedEnrichmentBean extends TaskDetailsBaseBean {

	private List<MicrositeDetailsBean> micrositeDetails;
	private String trainingAccessWebex;
    private String contactName;
    private String contactTitle;
    private String emailAddress;
    private String officePhone;
    private String mobilePhone;
    private String timeZone;
    private String businessStartTime;
    private String businessEndTime;
    private String numberOfUsers;
    private String numberOfTrainings;
    private String nameOfTrainings;
    private String numberOfAdmin;
    private String trainingTimeZone;
    private String orderSituation;
    private String ccaSpTelephonyFeatures;
    private String ssoConfiguration;
    private String addUserMethod;
    private String directoryConnector;
    private String adFileSyncNeeded;
    
    private List<AttachmentIdBean> documentIds;

	public List<MicrositeDetailsBean> getMicrositeDetails() {
		return micrositeDetails;
	}

	public String getTrainingAccessWebex() {
		return trainingAccessWebex;
	}

	public String getContactName() {
		return contactName;
	}

	public String getContactTitle() {
		return contactTitle;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getBusinessStartTime() {
		return businessStartTime;
	}

	public String getBusinessEndTime() {
		return businessEndTime;
	}

	public String getNumberOfUsers() {
		return numberOfUsers;
	}

	public String getNumberOfTrainings() {
		return numberOfTrainings;
	}

	public String getNameOfTrainings() {
		return nameOfTrainings;
	}

	public String getNumberOfAdmin() {
		return numberOfAdmin;
	}

	public String getTrainingTimeZone() {
		return trainingTimeZone;
	}

	public String getOrderSituation() {
		return orderSituation;
	}

	public String getCcaSpTelephonyFeatures() {
		return ccaSpTelephonyFeatures;
	}

	public String getSsoConfiguration() {
		return ssoConfiguration;
	}

	public String getAddUserMethod() {
		return addUserMethod;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setMicrositeDetails(List<MicrositeDetailsBean> micrositeDetails) {
		this.micrositeDetails = micrositeDetails;
	}

	public void setTrainingAccessWebex(String trainingAccessWebex) {
		this.trainingAccessWebex = trainingAccessWebex;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public void setBusinessStartTime(String businessStartTime) {
		this.businessStartTime = businessStartTime;
	}

	public void setBusinessEndTime(String businessEndTime) {
		this.businessEndTime = businessEndTime;
	}

	public void setNumberOfUsers(String numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	public void setNumberOfTrainings(String numberOfTrainings) {
		this.numberOfTrainings = numberOfTrainings;
	}

	public void setNameOfTrainings(String nameOfTrainings) {
		this.nameOfTrainings = nameOfTrainings;
	}

	public void setNumberOfAdmin(String numberOfAdmin) {
		this.numberOfAdmin = numberOfAdmin;
	}

	public void setTrainingTimeZone(String trainingTimeZone) {
		this.trainingTimeZone = trainingTimeZone;
	}

	public void setOrderSituation(String orderSituation) {
		this.orderSituation = orderSituation;
	}

	public void setCcaSpTelephonyFeatures(String ccaSpTelephonyFeatures) {
		this.ccaSpTelephonyFeatures = ccaSpTelephonyFeatures;
	}

	public void setSsoConfiguration(String ssoConfiguration) {
		this.ssoConfiguration = ssoConfiguration;
	}

	public void setAddUserMethod(String addUserMethod) {
		this.addUserMethod = addUserMethod;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getDirectoryConnector() {
		return directoryConnector;
	}

	public String getAdFileSyncNeeded() {
		return adFileSyncNeeded;
	}

	public void setDirectoryConnector(String directoryConnector) {
		this.directoryConnector = directoryConnector;
	}

	public void setAdFileSyncNeeded(String adFileSyncNeeded) {
		this.adFileSyncNeeded = adFileSyncNeeded;
	}
}
