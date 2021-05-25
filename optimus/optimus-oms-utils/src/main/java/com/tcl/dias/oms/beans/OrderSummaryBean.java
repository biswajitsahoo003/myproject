package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This file contains the OrderSummaryBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderSummaryBean {

	private Integer orderId;

	private String orderCode;

	private String createdBy;

	private Date createdTime;

	private String orderStage;

	private String quoteCreatedBy;

	private Boolean isO2cProcessed;

	private Boolean disableLr;

	private Boolean wholesaleOrder;

	private List<OrderLeSVBean> legalEntity = new ArrayList<>();

	private List<OrderFamilySVBean> orderFamily = new ArrayList<>();

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getOrderStage() {
		return orderStage;
	}

	public void setOrderStage(String orderStage) {
		this.orderStage = orderStage;
	}

	public String getQuoteCreatedBy() {
		return quoteCreatedBy;
	}

	public void setQuoteCreatedBy(String quoteCreatedBy) {
		this.quoteCreatedBy = quoteCreatedBy;
	}

	public Boolean getIsO2cProcessed() {
		return isO2cProcessed;
	}

	public void setIsO2cProcessed(Boolean isO2cProcessed) {
		this.isO2cProcessed = isO2cProcessed;
	}

	public List<OrderLeSVBean> getLegalEntity() {
		return legalEntity;
	}

	public void setLegalEntity(List<OrderLeSVBean> legalEntity) {
		this.legalEntity = legalEntity;
	}

	public List<OrderFamilySVBean> getOrderFamily() {
		return orderFamily;
	}

	public void setOrderFamily(List<OrderFamilySVBean> orderFamily) {
		this.orderFamily = orderFamily;
	}

	public Boolean getDisableLr() {
		return disableLr;
	}

	public void setDisableLr(Boolean disableLr) {
		this.disableLr = disableLr;
	}

	public Boolean getWholesaleOrder() {
		return wholesaleOrder;
	}

	public void setWholesaleOrder(Boolean wholesaleOrder) {
		this.wholesaleOrder = wholesaleOrder;
	}

	@Override
	public String toString() {
		return "OrderSummaryBean [orderId=" + orderId + ", orderCode=" + orderCode + ", createdBy=" + createdBy
				+ ", createdTime=" + createdTime + ", orderStage=" + orderStage + ", quoteCreatedBy=" + quoteCreatedBy
				+ ", isO2cProcessed=" + isO2cProcessed + ", disableLr=" + disableLr + ", legalEntity=" + legalEntity
				+ ", orderFamily=" + orderFamily + "]";
	}

}
