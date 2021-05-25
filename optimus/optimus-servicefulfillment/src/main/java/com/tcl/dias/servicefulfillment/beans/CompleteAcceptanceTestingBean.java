package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to Complete IBD Acceptance Testing
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class CompleteAcceptanceTestingBean extends TaskDetailsBaseBean {
	
	private String ibdAcceptanceStatus;
	private String ibdAcceptanceObservationsRemarks;
	private String reviewersName;
	private String reviewersEmailId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String ibdAcceptanceDate;
	
	public String isIbdAcceptanceStatus() {
		return ibdAcceptanceStatus;
	}
	public void setIbdAcceptanceStatus(String ibdAcceptanceStatus) {
		this.ibdAcceptanceStatus = ibdAcceptanceStatus;
	}
	public String getIbdAcceptanceObservationsRemarks() {
		return ibdAcceptanceObservationsRemarks;
	}
	public void setIbdAcceptanceObservationsRemarks(String ibdAcceptanceObservationsRemarks) {
		this.ibdAcceptanceObservationsRemarks = ibdAcceptanceObservationsRemarks;
	}
	public String getReviewersName() {
		return reviewersName;
	}
	public void setReviewersName(String reviewersName) {
		this.reviewersName = reviewersName;
	}
	public String getReviewersEmailId() {
		return reviewersEmailId;
	}
	public void setReviewersEmailId(String reviewersEmailId) {
		this.reviewersEmailId = reviewersEmailId;
	}
	public String getIbdAcceptanceDate() {
		return ibdAcceptanceDate;
	}
	public void setIbdAcceptanceDate(String ibdAcceptanceDate) {
		this.ibdAcceptanceDate = ibdAcceptanceDate;
	}
	
}
