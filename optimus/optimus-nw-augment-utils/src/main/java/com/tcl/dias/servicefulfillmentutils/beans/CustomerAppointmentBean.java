package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * This class is used for Customer Appointment Details
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class CustomerAppointmentBean extends TaskDetailsBaseBean {

	private Integer appointmentSlot;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String appointmentDate;
	
	private String localContactName;
	
	private String localContactEMail;
	
	private String localContactNumber;
	
	private Integer attachmentPermissionId;

	private List<Integer> documentAttachments;
	
	private String otherDocument;

	public Integer getAppointmentSlot() {
		return appointmentSlot;
	}

	public void setAppointmentSlot(Integer appointmentSlot) {
		this.appointmentSlot = appointmentSlot;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getLocalContactName() {
		return localContactName;
	}

	public void setLocalContactName(String localContactName) {
		this.localContactName = localContactName;
	}

	public String getLocalContactEMail() {
		return localContactEMail;
	}

	public void setLocalContactEMail(String localContactEMail) {
		this.localContactEMail = localContactEMail;
	}

	public String getLocalContactNumber() {
		return localContactNumber;
	}

	public void setLocalContactNumber(String localContactNumber) {
		this.localContactNumber = localContactNumber;
	}

	public Integer getAttachmentPermissionId() {
		return attachmentPermissionId;
	}

	public void setAttachmentPermissionId(Integer attachmentPermissionId) {
		this.attachmentPermissionId = attachmentPermissionId;
	}

	public List<Integer> getDocumentAttachments() {
		return documentAttachments;
	}

	public void setDocumentAttachments(List<Integer> documentAttachments) {
		this.documentAttachments = documentAttachments;
	}

	public String getOtherDocument() {
		return otherDocument;
	}

	public void setOtherDocument(String otherDocument) {
		this.otherDocument = otherDocument;
	}
	
	
}
