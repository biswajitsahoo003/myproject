package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class TaxationBean extends TaskDetailsBaseBean {
	
	private String taxExempt;

	public String getTaxExempt() {
		return taxExempt;
	}

	public void setTaxExempt(String taxExempt) {
		this.taxExempt = taxExempt;
	}
}
