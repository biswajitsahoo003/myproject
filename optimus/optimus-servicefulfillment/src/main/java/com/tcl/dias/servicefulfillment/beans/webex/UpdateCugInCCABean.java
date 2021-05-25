package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class UpdateCugInCCABean extends TaskDetailsBaseBean {

	private String updateCugNumbersInCcw;
	private String updateCugcompletionDate;

	public String getUpdateCugNumbersInCcw() {
		return updateCugNumbersInCcw;
	}

	public void setUpdateCugNumbersInCcw(String updateCugNumbersInCcw) {
		this.updateCugNumbersInCcw = updateCugNumbersInCcw;
	}

	public String getUpdateCugcompletionDate() {
		return updateCugcompletionDate;
	}

	public void setUpdateCugcompletionDate(String updateCugcompletionDate) {
		this.updateCugcompletionDate = updateCugcompletionDate;
	}
	
}
