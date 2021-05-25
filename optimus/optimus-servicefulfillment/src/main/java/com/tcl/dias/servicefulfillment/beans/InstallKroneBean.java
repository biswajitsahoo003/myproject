package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.common.beans.TaskDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Bean to Capture Krone Installion details
 * 
 * @author yogesh
 */
public class InstallKroneBean extends TaskDetailsBaseBean {

	private String isKroneInstalled;

	private Integer noOfKroneInstalled;

	private Integer totalKroneSlotAvailable;

	private String delayReason;

	

	public String getIsKroneInstalled() {
		return isKroneInstalled;
	}

	public void setIsKroneInstalled(String isKroneInstalled) {
		this.isKroneInstalled = isKroneInstalled;
	}

	public Integer getNoOfKroneInstalled() {
		return noOfKroneInstalled;
	}

	public void setNoOfKroneInstalled(Integer noOfKroneInstalled) {
		this.noOfKroneInstalled = noOfKroneInstalled;
	}

	public Integer getTotalKroneSlotAvailable() {
		return totalKroneSlotAvailable;
	}

	public void setTotalKroneSlotAvailable(Integer totalKroneSlotAvailable) {
		this.totalKroneSlotAvailable = totalKroneSlotAvailable;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

}
