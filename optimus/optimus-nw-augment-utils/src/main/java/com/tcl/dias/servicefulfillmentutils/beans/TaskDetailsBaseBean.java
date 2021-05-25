package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * used to set and get task delays for flowable tasks
 * @author diksha garg
 *
 */
public class TaskDetailsBaseBean extends BaseRequest {

	private String delayReason;
	
	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}
	
}