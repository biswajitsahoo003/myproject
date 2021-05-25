package com.tcl.dias.servicefulfillment.beans;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * This class is used to  Define Scope of Work & Project Plan
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class DefineScopeWorkProjectPlanBean {

	private String deliveryMilestone;	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String milestoneCompletionDate;
	
	public String getDeliveryMilestone() {
		return deliveryMilestone;
	}
	public void setDeliveryMilestone(String deliveryMilestone) {
		this.deliveryMilestone = deliveryMilestone;
	}
	public String getMilestoneCompletionDate() {
		return milestoneCompletionDate;
	}
	public void setMilestoneCompletionDate(String milestoneCompletionDate) {
		this.milestoneCompletionDate = milestoneCompletionDate;
	}
	
}
