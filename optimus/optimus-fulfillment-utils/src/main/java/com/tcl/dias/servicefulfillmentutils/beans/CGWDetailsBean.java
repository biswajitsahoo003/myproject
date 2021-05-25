package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * This file contains the CGWDetailsBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CGWDetailsBean implements Serializable {

	private Integer serviceId;
	private String serviceCode;
	private String orderCode;
	private Integer orderId;
	private String priSecLinkedServiceId;
	private String priSec;

	private Timestamp rrfsDate;

	private String priority;
	
	private Timestamp commitedDeliveryDate;
	
	private String siteAddress;

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}
	

	public Timestamp getCommitedDeliveryDate() {
		return commitedDeliveryDate;
	}

	public void setCommitedDeliveryDate(Timestamp commitedDeliveryDate) {
		this.commitedDeliveryDate = commitedDeliveryDate;
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getPriSecLinkedServiceId() {
		return priSecLinkedServiceId;
	}

	public void setPriSecLinkedServiceId(String priSecLinkedServiceId) {
		this.priSecLinkedServiceId = priSecLinkedServiceId;
	}

	public String getPriSec() {
		return priSec;
	}

	public void setPriSec(String priSec) {
		this.priSec = priSec;
	}

}
