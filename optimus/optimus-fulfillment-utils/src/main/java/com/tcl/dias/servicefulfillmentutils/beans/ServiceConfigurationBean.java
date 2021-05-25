package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;
/**
 * 
 * This file contains the ServiceConfigurationBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ServiceConfigurationBean extends TaskDetailsBaseBean{
	
	private List<AttachmentIdBean> documentIds;
    private String status;
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
	
    

}
