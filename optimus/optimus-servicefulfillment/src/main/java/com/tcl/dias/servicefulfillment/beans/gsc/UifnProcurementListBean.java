package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class UifnProcurementListBean extends TaskDetailsBaseBean {

	private List<UifnProcurementBean> uifnNumbers;

	public List<UifnProcurementBean> getUifnNumbers() {
		return uifnNumbers;
	}

	public void setUifnNumbers(List<UifnProcurementBean> uifnNumbers) {
		this.uifnNumbers = uifnNumbers;
	}
}
