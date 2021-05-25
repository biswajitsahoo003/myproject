package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * used to set and get task delays for flowable tasks
 * @author diksha garg
 *
 */
public class TaskDetailsBaseBean extends BaseRequest {

	private String delayReason;

	private String delayReasonCategory;

	private String delayReasonSubCategory;
	
	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

	public String getDelayReasonCategory() {
		return delayReasonCategory;
	}

	public void setDelayReasonCategory(String delayReasonCategory) {
		this.delayReasonCategory = delayReasonCategory;
	}

	public String getDelayReasonSubCategory() {
		return delayReasonSubCategory;
	}

	public void setDelayReasonSubCategory(String delayReasonSubCategory) {
		this.delayReasonSubCategory = delayReasonSubCategory;
	}
}