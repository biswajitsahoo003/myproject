package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class CreationCallbackGroupsBean extends TaskDetailsBaseBean{
	
	private String creationOfCallbackGroupCompleted;
	private String callbackCreationCompletionDate;

	public String getCreationOfCallbackGroupCompleted() {
		return creationOfCallbackGroupCompleted;
	}

	public void setCreationOfCallbackGroupCompleted(String creationOfCallbackGroupCompleted) {
		this.creationOfCallbackGroupCompleted = creationOfCallbackGroupCompleted;
	}

	public String getCallbackCreationCompletionDate() {
		return callbackCreationCompletionDate;
	}

	public void setCallbackCreationCompletionDate(String callbackCreationCompletionDate) {
		this.callbackCreationCompletionDate = callbackCreationCompletionDate;
	}

}
