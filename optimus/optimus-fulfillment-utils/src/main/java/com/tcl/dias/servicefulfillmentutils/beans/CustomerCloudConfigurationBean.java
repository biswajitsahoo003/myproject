package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

/**
 * 
 * This file contains the CustomerCloudConfigurationBean.java class.
 * 
 *
 * @author DimpleS
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class CustomerCloudConfigurationBean extends TaskDetailsBaseBean {
	
	private List<AttachmentIdBean> documentIds;
	private String status;
	private String remarks;

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
