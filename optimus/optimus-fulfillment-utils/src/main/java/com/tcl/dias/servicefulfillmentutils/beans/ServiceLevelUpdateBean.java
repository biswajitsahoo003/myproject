package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * This file contains the ServiceLevelUpdateBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ServiceLevelUpdateBean implements Serializable{
	
	private Integer serviceId;
	
	private Timestamp rrfsDate;
	
	private String priority;

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Timestamp getRrfsDate() {
		return rrfsDate;
	}

	public void setRrfsDate(Timestamp rrfsDate) {
		this.rrfsDate = rrfsDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

}
