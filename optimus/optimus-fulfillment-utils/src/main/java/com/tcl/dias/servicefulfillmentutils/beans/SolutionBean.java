package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class SolutionBean {
	
    public Integer serviceId;
	
	public String serviceCode;
	
	public String orderCode;
	
	public Integer orderId;
	
	public String orderType;
	
	public String orderCategory;
	
	public String orderSubCategory;
	
	public String productName;
	
	public String offeringName;
	
	public List<OverlayBean> overlayBeanList;
	 
	public List<CGWDetailsBean> cgwDetailList;
	
	public int overlaySize;
	
	public Integer solutionServiceId;
	
	private String solutionServiceCode;

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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public List<OverlayBean> getOverlayBeanList() {
		return overlayBeanList;
	}

	public void setOverlayBeanList(List<OverlayBean> overlayBeanList) {
		this.overlayBeanList = overlayBeanList;
	}

	public int getOverlaySize() {
		return overlaySize;
	}

	public void setOverlaySize(int overlaySize) {
		this.overlaySize = overlaySize;
	}

	public List<CGWDetailsBean> getCgwDetailList() {
		return cgwDetailList;
	}

	public void setCgwDetailList(List<CGWDetailsBean> cgwDetailList) {
		this.cgwDetailList = cgwDetailList;
	}

	public Integer getSolutionServiceId() {
		return solutionServiceId;
	}

	public void setSolutionServiceId(Integer solutionServiceId) {
		this.solutionServiceId = solutionServiceId;
	}

	public String getSolutionServiceCode() {
		return solutionServiceCode;
	}

	public void setSolutionServiceCode(String solutionServiceCode) {
		this.solutionServiceCode = solutionServiceCode;
	}

}
