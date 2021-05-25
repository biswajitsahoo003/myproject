package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
/**
 * 
 * This file contains site wise commercial info.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzosdwanCommericalBean implements Serializable{
	private String serviceId;
	private String serviceType;
	private String product;
	private String orderType;
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	private String actionType;
	private String chareableLineItem;
	private String bandwidth;
	private String arc;
	private String nrc;
	private String mrc;

	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getChareableLineItem() {
		return chareableLineItem;
	}
	public void setChareableLineItem(String chareableLineItem) {
		this.chareableLineItem = chareableLineItem;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getArc() {
		return arc;
	}
	public void setArc(String arc) {
		this.arc = arc;
	}
	public String getNrc() {
		return nrc;
	}
	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public String getMrc() {
		return mrc;
	}

	public void setMrc(String mrc) {
		this.mrc = mrc;
	}
}
