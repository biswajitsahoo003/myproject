package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class NumberMappingBean extends TaskDetailsBaseBean {
	
	private String isNumberMappingCompleted;
	private String numberMappingcompletionDate;

	public String getIsNumberMappingCompleted() {
		return isNumberMappingCompleted;
	}

	public void setIsNumberMappingCompleted(String isNumberMappingCompleted) {
		this.isNumberMappingCompleted = isNumberMappingCompleted;
	}

	public String getNumberMappingcompletionDate() {
		return numberMappingcompletionDate;
	}

	public void setNumberMappingcompletionDate(String numberMappingcompletionDate) {
		this.numberMappingcompletionDate = numberMappingcompletionDate;
	}
	
	

}
