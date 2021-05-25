package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 * Create PO for media gateway
 *
 * @author srraghav
 */
public class CreatePoForEndpointBean extends TaskDetailsBaseBean {

	private String endpointInstallationPoNumber;
	private String endpointInstallationPoDate;
	private String endpointSupportPoNumber;
	private String endpointSupportPoDate;
	private List<AttachmentIdBean> documentIds;

	public String getEndpointInstallationPoNumber() {
		return endpointInstallationPoNumber;
	}

	public void setEndpointInstallationPoNumber(String endpointInstallationPoNumber) {
		this.endpointInstallationPoNumber = endpointInstallationPoNumber;
	}

	public String getEndpointInstallationPoDate() {
		return endpointInstallationPoDate;
	}

	public void setEndpointInstallationPoDate(String endpointInstallationPoDate) {
		this.endpointInstallationPoDate = endpointInstallationPoDate;
	}

	public String getEndpointSupportPoNumber() {
		return endpointSupportPoNumber;
	}

	public void setEndpointSupportPoNumber(String endpointSupportPoNumber) {
		this.endpointSupportPoNumber = endpointSupportPoNumber;
	}

	public String getEndpointSupportPoDate() {
		return endpointSupportPoDate;
	}

	public void setEndpointSupportPoDate(String endpointSupportPoDate) {
		this.endpointSupportPoDate = endpointSupportPoDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
