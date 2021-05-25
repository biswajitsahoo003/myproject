package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ServiceAcceptanceBean extends TaskDetailsBaseBean {
	
	private AttachmentIdBean document;

	private List<Integer> acceptScAssetsId;
	
	private List<Integer> rejectScAssetsId;
	
	private List<ServiceAcceptanceBillingBean> serviceAcceptBilling;

	public List<ServiceAcceptanceBillingBean> getServiceAcceptBilling() {
		return serviceAcceptBilling;
	}

	public void setServiceAcceptBilling(List<ServiceAcceptanceBillingBean> serviceAcceptBilling) {
		this.serviceAcceptBilling = serviceAcceptBilling;
	}

	public AttachmentIdBean getDocument() {
		return document;
	}

	public void setDocument(AttachmentIdBean document) {
		this.document = document;
	}

	public List<Integer> getAcceptScAssetsId() {
		return acceptScAssetsId;
	}

	public List<Integer> getRejectScAssetsId() {
		return rejectScAssetsId;
	}

	public void setAcceptScAssetsId(List<Integer> acceptScAssetsId) {
		this.acceptScAssetsId = acceptScAssetsId;
	}

	public void setRejectScAssetsId(List<Integer> rejectScAssetsId) {
		this.rejectScAssetsId = rejectScAssetsId;
	}

}
