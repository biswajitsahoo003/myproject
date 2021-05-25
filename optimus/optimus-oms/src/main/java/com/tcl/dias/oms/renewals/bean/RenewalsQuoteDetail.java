package com.tcl.dias.oms.renewals.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the QuoteRequest.java class. Bean class
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class RenewalsQuoteDetail {

	private Integer quoteId;
	private Integer orderId;
	private Integer quoteleId;
	private Integer customerId;
	private List<Integer> orderLeIds= new ArrayList<Integer>();
	private String productName;
	private List<RenewalsSolutionDetail> solutions = new ArrayList<RenewalsSolutionDetail>();

	private String orderType;
	private Integer orderIllSiteId;
	private String orderCategory;
	private String primaryOrSecondaryOrBoth;

	private Byte isMulticircuit;
	private String orderAmendmentParentOrderId;
	List<RenewalsSite> site = new ArrayList<RenewalsSite>();
	@JsonIgnore

	private String engagementOptyId;
	private String quoteCode;
	private String classification;
	private String serviceId;
	
	private String ismultiVrf;
	
	private Map<Integer, String> mappingDetails;

	List<RenewalsAttributeDetail> quoteAttributeList = new ArrayList<RenewalsAttributeDetail>();
	
    private Map<String,  RenewalsPriceBean>  renewalsPriceBean;
    
	private Integer linkCount;
    
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

	public List<RenewalsSolutionDetail> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<RenewalsSolutionDetail> solutions) {
		this.solutions = solutions;
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
	 * @return the orderLeIds
	 */
	public List<Integer> getOrderLeIds() {
		if (orderLeIds == null) {
			orderLeIds = new ArrayList<>();
		}
		return orderLeIds;
	}

	/**
	 * @param orderLeIds
	 *            the orderLeIds to set
	 */
	public void setOrderLeIds(List<Integer> orderLeIds) {
		this.orderLeIds = orderLeIds;
	}


	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderIllSiteId() {
		return orderIllSiteId;
	}

	public void setOrderIllSiteId(Integer orderIllSiteId) {
		this.orderIllSiteId = orderIllSiteId;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getPrimaryOrSecondaryOrBoth() {
		return primaryOrSecondaryOrBoth;
	}

	public void setPrimaryOrSecondaryOrBoth(String primaryOrSecondaryOrBoth) {
		this.primaryOrSecondaryOrBoth = primaryOrSecondaryOrBoth;
	}

	public Byte getIsMulticircuit() {
		return isMulticircuit;
	}

	public void setIsMulticircuit(Byte isMulticircuit) {
		this.isMulticircuit = isMulticircuit;
	}

	public String getOrderAmendmentParentOrderId() {
		return orderAmendmentParentOrderId;
	}

	public void setOrderAmendmentParentOrderId(String orderAmendmentParentOrderId) {
		this.orderAmendmentParentOrderId = orderAmendmentParentOrderId;
	}

	public List<RenewalsSite> getSite() {
		return site;
	}

	public void setSite(List<RenewalsSite> site) {
		this.site = site;
	}

	public String getEngagementOptyId() {
		return engagementOptyId;
	}

	public void setEngagementOptyId(String engagementOptyId) {
		this.engagementOptyId = engagementOptyId;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getIsmultiVrf() {
		return ismultiVrf;
	}

	public void setIsmultiVrf(String ismultiVrf) {
		this.ismultiVrf = ismultiVrf;
	}
	
	public List<RenewalsAttributeDetail> getQuoteAttributeList() {
		return quoteAttributeList;
	}

	public void setQuoteAttributeList(List<RenewalsAttributeDetail> quoteAttributeList) {
		this.quoteAttributeList = quoteAttributeList;
	}
		

	public Map<Integer, String> getMappingDetails() {
		return mappingDetails;
	}

	public void setMappingDetails(Map<Integer, String> mappingDetails) {
		this.mappingDetails = mappingDetails;
	}

	
	public Map<String, RenewalsPriceBean> getRenewalsPriceBean() {
		return renewalsPriceBean;
	}

	public void setRenewalsPriceBean(Map<String, RenewalsPriceBean> renewalsPriceBean) {
		this.renewalsPriceBean = renewalsPriceBean;
	}
	
	public Integer getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(Integer linkCount) {
		this.linkCount = linkCount;
	}

	@Override
	public String toString() {
		return "RenewalsQuoteDetail [quoteId=" + quoteId + ", orderId=" + orderId + ", quoteleId=" + quoteleId
				+ ", customerId=" + customerId + ", orderLeIds=" + orderLeIds + ", productName=" + productName
				+ ", solutions=" + solutions + ", orderType=" + orderType + ", orderIllSiteId=" + orderIllSiteId
				+ ", orderCategory=" + orderCategory + ", primaryOrSecondaryOrBoth=" + primaryOrSecondaryOrBoth
				+ ", isMulticircuit=" + isMulticircuit + ", orderAmendmentParentOrderId=" + orderAmendmentParentOrderId
				+ ", site=" + site + ", engagementOptyId=" + engagementOptyId + ", quoteCode=" + quoteCode
				+ ", classification=" + classification + ", serviceId=" + serviceId + ", ismultiVrf=" + ismultiVrf
				+ ", mappingDetails=" + mappingDetails + ", quoteAttributeList=" + quoteAttributeList
				+ ", renewalsPriceBean=" + renewalsPriceBean + ", linkCount=" + linkCount + "]";
	}




}
