package com.tcl.dias.l2oworkflowutils.beans;

import java.io.Serializable;

/**
 * Class to hold mftaskdata
 * 
 * @author archchan
 *
 */
public class MfTaskData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customerLat;
	private String customerLong;

	// Added for NPL
	private String customerLatA;
	private String customerLongA;
	private String customerLatB;
	private String customerLongB;

	private String taskRelatedTo;

	public String getTaskRelatedTo() {
		return taskRelatedTo;
	}

	public void setTaskRelatedTo(String taskRelatedTo) {
		this.taskRelatedTo = taskRelatedTo;
	}

	public String getCustomerLatA() {
		return customerLatA;
	}

	public void setCustomerLatA(String customerLatA) {
		this.customerLatA = customerLatA;
	}

	public String getCustomerLongA() {
		return customerLongA;
	}

	public void setCustomerLongA(String customerLongA) {
		this.customerLongA = customerLongA;
	}

	public String getCustomerLatB() {
		return customerLatB;
	}

	public void setCustomerLatB(String customerLatB) {
		this.customerLatB = customerLatB;
	}

	public String getCustomerLongB() {
		return customerLongB;
	}

	public void setCustomerLongB(String customerLongB) {
		this.customerLongB = customerLongB;
	}

	public String getCustomerLat() {
		return customerLat;
	}

	public void setCustomerLat(String customerLat) {
		this.customerLat = customerLat;
	}

	public String getCustomerLong() {
		return customerLong;
	}

	public void setCustomerLong(String customerLong) {
		this.customerLong = customerLong;
	}

}
