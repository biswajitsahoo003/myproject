package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class ByonReadinessBean extends TaskDetailsBaseBean {
	
	private String thirdPartyIPAddress;
    private String thirdPartyTicketingContact;
    private String thirdPartyEscalationMatrix;
    private List<AttachmentIdBean> documentIds;
    private String portBandwidth;
    private String thirdPartyServiceID;
    
	public String getThirdPartyIPAddress() {
		return thirdPartyIPAddress;
	}
	public void setThirdPartyIPAddress(String thirdPartyIPAddress) {
		this.thirdPartyIPAddress = thirdPartyIPAddress;
	}
	
	public String getThirdPartyTicketingContact() {
		return thirdPartyTicketingContact;
	}
	public void setThirdPartyTicketingContact(String thirdPartyTicketingContact) {
		this.thirdPartyTicketingContact = thirdPartyTicketingContact;
	}
	public String getThirdPartyEscalationMatrix() {
		return thirdPartyEscalationMatrix;
	}
	public void setThirdPartyEscalationMatrix(String thirdPartyEscalationMatrix) {
		this.thirdPartyEscalationMatrix = thirdPartyEscalationMatrix;
	}
	
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	
	public String getThirdPartyServiceID() {
		return thirdPartyServiceID;
	}
	public void setThirdPartyServiceID(String thirdPartyServiceID) {
		this.thirdPartyServiceID = thirdPartyServiceID;
	}
	public String getPortBandwidth() {
		return portBandwidth;
	}
	public void setPortBandwidth(String portBandwidth) {
		this.portBandwidth = portBandwidth;
	}
}
