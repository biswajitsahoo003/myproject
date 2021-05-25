package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class BillingProfileCmsIdBean extends TaskDetailsBaseBean {
	
	private List<BillingProfileBean> billingProfilebean;
	private String  message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<BillingProfileBean> getBillingProfilebean() {
		return billingProfilebean;
	}

	public void setBillingProfilebean(List<BillingProfileBean> billingProfilebean) {
		this.billingProfilebean = billingProfilebean;
	}

}
