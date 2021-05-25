package com.tcl.dias.oms.npl.pdf.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.npl.beans.NplSite;
import com.tcl.dias.oms.beans.SolutionDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the QuoteRequest.java class. Bean class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class NplMcQuoteDetailBean {

	private Integer quoteId;
	private Integer orderId;
	private Integer quoteleId;
	private Integer customerId;
	private String productName;
	List<NplLinkDetailBean> links;
	public Integer getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(Integer linkCount) {
		this.linkCount = linkCount;
	}

	private Integer linkCount;
	@JsonIgnore
	boolean isManualFeasible;
	
	private String orderType;
	private String orderCategory;
	private String serviceId;
	
	
	

	public List<NplLinkDetailBean> getLinks() {
		return links;
	}

	public void setLinks(List<NplLinkDetailBean> links) {
		this.links = links;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteleId() {
		return quoteleId;
	}

	public void setQuoteleId(Integer quoteleId) {
		this.quoteleId = quoteleId;
	}

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	

	/**
	 * @return the orderId
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	
	/**
	 * @param orderLeIds
	 *            the orderLeIds to set
	 */
	

	public boolean isManualFeasible() {
		return isManualFeasible;
	}

	public void setManualFeasible(boolean isManualFeasible) {
		this.isManualFeasible = isManualFeasible;
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

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "NplQuoteDetail [quoteId=" + quoteId + ", quoteleId=" + quoteleId + ", customerId=" + customerId
				+ ", productName=" + productName + ", solutions=" +
				", serviceId=" + serviceId +"]";
	}

}
