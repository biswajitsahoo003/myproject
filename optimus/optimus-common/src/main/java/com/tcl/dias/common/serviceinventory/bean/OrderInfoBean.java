package com.tcl.dias.common.serviceinventory.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;
/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderInfoBean   implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String optimusOrderCode;
	private Integer scOrderId;
	private Integer parentId;
	private String parentOptimusOrderCode;
	private String optyClassification;
	private String isMultipleLe;
	private String isBundleOrder;
	private String orderType;
	private String orderCategory;
	private String orderSource;
	private Integer orderTermsInMonth;
	private Timestamp contractStartDate;
	private Timestamp contractEndDate;
	private Timestamp lastMacdDate;
	
	private Timestamp orderCreatedDate;

	private Map<String, String> orderAttributes;

	public String getOptimusOrderCode() {
		return optimusOrderCode;
	}

	public void setOptimusOrderCode(String optimusOrderCode) {
		this.optimusOrderCode = optimusOrderCode;
	}

	public Integer getScOrderId() {
		return scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentOptimusOrderCode() {
		return parentOptimusOrderCode;
	}

	public void setParentOptimusOrderCode(String parentOptimusOrderCode) {
		this.parentOptimusOrderCode = parentOptimusOrderCode;
	}

	public String getOptyClassification() {
		return optyClassification;
	}

	public void setOptyClassification(String optyClassification) {
		this.optyClassification = optyClassification;
	}

	public String getIsMultipleLe() {
		return isMultipleLe;
	}

	public void setIsMultipleLe(String isMultipleLe) {
		this.isMultipleLe = isMultipleLe;
	}

	public String getIsBundleOrder() {
		return isBundleOrder;
	}

	public void setIsBundleOrder(String isBundleOrder) {
		this.isBundleOrder = isBundleOrder;
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

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public Integer getOrderTermsInMonth() {
		return orderTermsInMonth;
	}

	public void setOrderTermsInMonth(Integer orderTermsInMonth) {
		this.orderTermsInMonth = orderTermsInMonth;
	}

	public Timestamp getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Timestamp getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Timestamp getLastMacdDate() {
		return lastMacdDate;
	}

	public void setLastMacdDate(Timestamp lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
	}

	public Map<String, String> getOrderAttributes() {
		return orderAttributes;
	}

	public void setOrderAttributes(Map<String, String> orderAttributes) {
		this.orderAttributes = orderAttributes;
	}
	
	

	public Timestamp getOrderCreatedDate() {
		return orderCreatedDate;
	}

	public void setOrderCreatedDate(Timestamp orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
	}

	@Override
	public String toString() {
		return "OrderInfoBean [optimusOrderCode=" + optimusOrderCode + ", scOrderId=" + scOrderId + ", parentId="
				+ parentId + ", parentOptimusOrderCode=" + parentOptimusOrderCode + ", optyClassification="
				+ optyClassification + ", isMultipleLe=" + isMultipleLe + ", isBundleOrder=" + isBundleOrder
				+ ", orderType=" + orderType + ", orderCategory=" + orderCategory + ", orderSource=" + orderSource
				+ ", orderTermsInMonth=" + orderTermsInMonth + ", contractStartDate=" + contractStartDate
				+ ", contractEndDate=" + contractEndDate + ", lastMacdDate=" + lastMacdDate + ", orderAttributes="
				+ orderAttributes + "]";
	}

}
