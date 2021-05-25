package com.tcl.dias.oms.beans;

import java.util.Date;
import java.util.List;

/**
 * Bean file
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderConfiguration {

	private Integer orderId;
	private String orderCode;
	private Date orderCreatedDate;
	private String productName;
	private Integer orderToLeId;
	private String status;
	private String classification;
	private String orderType;
	private Boolean  isO2cEnabled;
	private Byte isAmended;
	private Byte isMilestoneParent;
	private List<String> serviceTypeList;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getOrderCreatedDate() {
		return orderCreatedDate;
	}

	public void setOrderCreatedDate(Date orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	

	public Boolean getIsO2cEnabled() {
		return isO2cEnabled;
	}

	public void setIsO2cEnabled(Boolean isO2cEnabled) {
		this.isO2cEnabled = isO2cEnabled;
	}

	@Override
	public String toString() {
		return "OrderConfiguration [orderId=" + orderId + ", orderCode=" + orderCode + ", orderCreatedDate="
				+ orderCreatedDate + "]";
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the orderToLeId
	 */
	public Integer getOrderToLeId() {
		return orderToLeId;
	}

	/**
	 * @param orderToLeId the orderToLeId to set
	 */
	public void setOrderToLeId(Integer orderToLeId) {
		this.orderToLeId = orderToLeId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getClassification() {
		return classification;
	}

	public Byte getIsAmended() {
		return isAmended;
	}

	public void setIsAmended(Byte isAmended) {
		this.isAmended = isAmended;
	}

	public Byte getIsMilestoneParent() {
		return isMilestoneParent;
	}

	public void setIsMilestoneParent(Byte isMilestoneParent) {
		this.isMilestoneParent = isMilestoneParent;
	}
	
	public List<String> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(List<String> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}
}
