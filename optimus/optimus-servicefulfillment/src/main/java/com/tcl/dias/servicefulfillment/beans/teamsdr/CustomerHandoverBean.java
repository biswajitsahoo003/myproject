package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Customer handover bean
 *
 * @author srraghav
 */
public class CustomerHandoverBean extends TaskDetailsBaseBean {
	private String customerHandoverCompleted;
	private String customerHandoverCompletedDate;

	public String getCustomerHandoverCompleted() {
		return customerHandoverCompleted;
	}

	public void setCustomerHandoverCompleted(String customerHandoverCompleted) {
		this.customerHandoverCompleted = customerHandoverCompleted;
	}

	public String getCustomerHandoverCompletedDate() {
		return customerHandoverCompletedDate;
	}

	public void setCustomerHandoverCompletedDate(String customerHandoverCompletedDate) {
		this.customerHandoverCompletedDate = customerHandoverCompletedDate;
	}
}
