package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class ProvideHandoverBean extends BaseRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date appointmentDate;
    private String appointmentTime;
    private String customerServiceAcceptance;
    private String customerTechicalContactEmail;
    private String customerTechicalContactName;
    private String customerTechicalContactNumber;
    private List<AttachmentIdBean> documentIds;
    private String serviceIssue;
    private String serviceIssueDescription;

    public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getCustomerServiceAcceptance() {
        return customerServiceAcceptance;
    }

    public void setCustomerServiceAcceptance(String customerServiceAcceptance) {
        this.customerServiceAcceptance = customerServiceAcceptance;
    }

    public String getCustomerTechicalContactEmail() {
        return customerTechicalContactEmail;
    }

    public void setCustomerTechicalContactEmail(String customerTechicalContactEmail) {
        this.customerTechicalContactEmail = customerTechicalContactEmail;
    }

    public String getCustomerTechicalContactName() {
        return customerTechicalContactName;
    }

    public void setCustomerTechicalContactName(String customerTechicalContactName) {
        this.customerTechicalContactName = customerTechicalContactName;
    }

    public String getCustomerTechicalContactNumber() {
        return customerTechicalContactNumber;
    }

    public void setCustomerTechicalContactNumber(String customerTechicalContactNumber) {
        this.customerTechicalContactNumber = customerTechicalContactNumber;
    }

    public String getServiceIssue() {
        return serviceIssue;
    }

    public void setServiceIssue(String serviceIssue) {
        this.serviceIssue = serviceIssue;
    }

    public String getServiceIssueDescription() {
        return serviceIssueDescription;
    }

    public void setServiceIssueDescription(String serviceIssueDescription) {
        this.serviceIssueDescription = serviceIssueDescription;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
