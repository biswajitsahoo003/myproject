package com.tcl.dias.servicefulfillment.beans;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class SiteReadinessDetailBean extends TaskDetailsBaseBean {

	private String isSiteReady;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String tentativeDate;

	public String getIsSiteReady() {
		return isSiteReady;
	}

	public void setIsSiteReady(String isSiteReady) {
		this.isSiteReady = isSiteReady;
	}

	public String getTentativeDate() {
		return tentativeDate;
	}

	public void setTentativeDate(String tentativeDate) {
		this.tentativeDate = tentativeDate;
	}

	@Override
	public String toString() {
		return "SiteReadinessDetailBean{" + "isSiteReady='" + isSiteReady + '\'' + ", tentativeDate=" + tentativeDate
				+ '}';
	}
}