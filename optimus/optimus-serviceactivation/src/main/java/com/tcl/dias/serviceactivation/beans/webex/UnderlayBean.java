package com.tcl.dias.serviceactivation.beans.webex;

import java.sql.Timestamp;
import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;

public class UnderlayBean {

	public Integer serviceId;

	public String serviceCode;
	
	private String commissioningDate;

	private Timestamp commitedDeliveryDate;
	
	private List<AttachmentIdBean> documentIds;

	public Integer getServiceId() {
		return serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public Timestamp getCommitedDeliveryDate() {
		return commitedDeliveryDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getCommissioningDate() {
		return commissioningDate;
	}

	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}

	public void setCommitedDeliveryDate(Timestamp commitedDeliveryDate) {
		this.commitedDeliveryDate = commitedDeliveryDate;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
