package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class RenewalAttachmentDetails {

    private Integer serviceId;
    private List<AttachmentBean> attachmentDetails;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
    
	public List<AttachmentBean> getAttachmentDetails() {
		return attachmentDetails;
	}

	public void setAttachmentDetails(List<AttachmentBean> attachmentDetails) {
		this.attachmentDetails = attachmentDetails;
	}
}
