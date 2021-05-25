package com.tcl.dias.servicefulfillmentutils.beans;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *used for task count
 */
public class TaskCountBean {

	private Integer reopenCount;

	private Integer assignetCount;

	private Integer closedCount;

	private Integer inProgressCount;

	private Integer onholdCount;

	public Integer getReopenCount() {
		return reopenCount;
	}

	public void setReopenCount(Integer reopenCount) {
		this.reopenCount = reopenCount;
	}

	public Integer getAssignetCount() {
		return assignetCount;
	}

	public void setAssignetCount(Integer assignetCount) {
		this.assignetCount = assignetCount;
	}

	public Integer getClosedCount() {
		return closedCount;
	}

	public void setClosedCount(Integer closedCount) {
		this.closedCount = closedCount;
	}

	public Integer getInProgressCount() {
		return inProgressCount;
	}

	public void setInProgressCount(Integer inProgressCount) {
		this.inProgressCount = inProgressCount;
	}

	public Integer getOnholdCount() {
		return onholdCount;
	}

	public void setOnholdCount(Integer onholdCount) {
		this.onholdCount = onholdCount;
	}

}
