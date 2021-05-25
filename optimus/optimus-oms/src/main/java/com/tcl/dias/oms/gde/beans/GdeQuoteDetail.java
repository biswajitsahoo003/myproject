package com.tcl.dias.oms.gde.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.SolutionDetail;

/**
 * @author archchan
 *
 */
@JsonInclude(Include.NON_NULL)
public class GdeQuoteDetail {

	private Integer quoteId;
	private Integer orderId;
	private Integer quoteleId;
	private Integer customerId;
	private List<Integer> orderLeIds;
	private String productName;
	List<SolutionDetail> solutions;
	List<GdeSite> site;
	private Integer linkCount;
	@JsonIgnore
	boolean isManualFeasible;
	private String orderType;
	private String orderCategory;
	private String serviceId;
	private Boolean isOrderCreated;
	private String errorMessage;

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuoteleId() {
		return quoteleId;
	}

	public void setQuoteleId(Integer quoteleId) {
		this.quoteleId = quoteleId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public List<Integer> getOrderLeIds() {
		if (orderLeIds == null) {
			orderLeIds = new ArrayList<>();
		}
		return orderLeIds;
	}

	public void setOrderLeIds(List<Integer> orderLeIds) {
		this.orderLeIds = orderLeIds;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<SolutionDetail> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<SolutionDetail> solutions) {
		this.solutions = solutions;
	}

	public List<GdeSite> getSite() {
		return site;
	}

	public void setSite(List<GdeSite> site) {
		this.site = site;
	}

	public Integer getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(Integer linkCount) {
		this.linkCount = linkCount;
	}

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

	public Boolean getIsOrderCreated() {
		return isOrderCreated;
	}

	public void setIsOrderCreated(Boolean isOrderCreated) {
		this.isOrderCreated = isOrderCreated;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "GdeQuoteDetail [quoteId=" + quoteId + ", orderId=" + orderId + ", quoteleId=" + quoteleId
				+ ", customerId=" + customerId + ", orderLeIds=" + orderLeIds + ", productName=" + productName
				+ ", solutions=" + solutions + ", site=" + site + ", linkCount=" + linkCount + ", isManualFeasible="
				+ isManualFeasible + ", orderType=" + orderType + ", orderCategory=" + orderCategory + ", serviceId="
				+ serviceId + "]";
	}

}
