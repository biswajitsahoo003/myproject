package com.tcl.dias.l2oworkflow.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 
 * This file contains the ServiceDashBoardBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ServiceDashBoardBean {

	private Integer serviceId;
	private String serviceCode;
	private Date orderDate;
	private String orderCode;
	private String productName;
	private String location;
	private Integer locationId;
	private Timestamp estimatedDeliveryDate;

	private Timestamp committedDeliveryDate;

	private Timestamp actualDeliveryDate;

	private Timestamp targetedDeliveryDate;
	private List<StagePlanBean> stages;
	private String primarySecondary;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public List<StagePlanBean> getStages() {
		return stages;
	}

	public void setStages(List<StagePlanBean> stages) {
		this.stages = stages;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Timestamp getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(Timestamp estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	public Timestamp getCommittedDeliveryDate() {
		return committedDeliveryDate;
	}

	public void setCommittedDeliveryDate(Timestamp committedDeliveryDate) {
		this.committedDeliveryDate = committedDeliveryDate;
	}

	public Timestamp getActualDeliveryDate() {
		return actualDeliveryDate;
	}

	public void setActualDeliveryDate(Timestamp actualDeliveryDate) {
		this.actualDeliveryDate = actualDeliveryDate;
	}

	public Timestamp getTargetedDeliveryDate() {
		return targetedDeliveryDate;
	}

	public void setTargetedDeliveryDate(Timestamp targetedDeliveryDate) {
		this.targetedDeliveryDate = targetedDeliveryDate;
	}

	public String getPrimarySecondary() {
		return primarySecondary;
	}

	public void setPrimarySecondary(String primarySecondary) {
		this.primarySecondary = primarySecondary;
	}

}
