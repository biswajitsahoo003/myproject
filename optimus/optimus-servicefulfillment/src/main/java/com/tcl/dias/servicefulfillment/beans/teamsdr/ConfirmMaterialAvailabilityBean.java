package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillment.beans.teamsdr.SerialNumberBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * Bean for confirm material availability
 *
 * @author Srinivasa Raghavan
 */
public class ConfirmMaterialAvailabilityBean extends TaskDetailsBaseBean {

	private String action;

	private String grnNumber;

	private String materialReceived;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String expectedETADate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String materialReceivedDate;

	private List<SerialNumberBean> serialNumber;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getGrnNumber() {
		return grnNumber;
	}

	public void setGrnNumber(String grnNumber) {
		this.grnNumber = grnNumber;
	}

	public String getMaterialReceived() {
		return materialReceived;
	}

	public void setMaterialReceived(String materialReceived) {
		this.materialReceived = materialReceived;
	}

	public String getExpectedETADate() {
		return expectedETADate;
	}

	public void setExpectedETADate(String expectedETADate) {
		this.expectedETADate = expectedETADate;
	}

	public String getMaterialReceivedDate() {
		return materialReceivedDate;
	}

	public void setMaterialReceivedDate(String materialReceivedDate) {
		this.materialReceivedDate = materialReceivedDate;
	}

	public List<SerialNumberBean> getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(List<SerialNumberBean> serialNumber) {
		this.serialNumber = serialNumber;
	}
}
