package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Install MAST POJO for a specific task
 * 
 * @author arjayapa
 *
 */
public class TaskMastRequest extends TaskDetailsBaseBean {
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String mastInstallationDate;
	
	private String mastMaintainedBy;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String mastWarrantyStartDate;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String mastWarrantyEndDate;	
	
	private List<AttachmentIdBean> documentIds;

	public String getMastInstallationDate() {
		return mastInstallationDate;
	}

	public void setMastInstallationDate(String mastInstallationDate) {
		this.mastInstallationDate = mastInstallationDate;
	}

	public String getMastMaintainedBy() {
		return mastMaintainedBy;
	}

	public void setMastMaintainedBy(String mastMaintainedBy) {
		this.mastMaintainedBy = mastMaintainedBy;
	}

	public String getMastWarrantyStartDate() {
		return mastWarrantyStartDate;
	}

	public void setMastWarrantyStartDate(String mastWarrantyStartDate) {
		this.mastWarrantyStartDate = mastWarrantyStartDate;
	}

	public String getMastWarrantyEndDate() {
		return mastWarrantyEndDate;
	}

	public void setMastWarrantyEndDate(String mastWarrantyEndDate) {
		this.mastWarrantyEndDate = mastWarrantyEndDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}	

}
