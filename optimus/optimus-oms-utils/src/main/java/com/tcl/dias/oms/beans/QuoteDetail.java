package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcl.dias.common.beans.ProductOfferingsBean;
import com.tcl.dias.oms.izosdwan.beans.QuestionnaireInputDetails;

/**
 * This file contains the QuoteRequest.java class. Bean class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class QuoteDetail {

	private Integer quoteId;
	private Integer orderId;
	private Integer quoteleId;
	private String orderType;
	private Integer orderIllSiteId;
	private String orderCategory;
	private String primaryOrSecondaryOrBoth;
	private Integer customerId;
	private Integer customerLeId;
	private String ns_quote;
	
	
	public String getNs_quote() {
		return ns_quote;
	}

	public void setNs_quote(String ns_quote) {
		this.ns_quote = ns_quote;
	}

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	private List<Integer> orderLeIds;
	private String productName;
	private Byte isMulticircuit;
	private String orderAmendmentParentOrderId;

	List<SolutionDetail> solutions;
	List<ProductOfferingsBean> izosdwanSolutions;
	private List<QuestionnaireInputDetails> quoteAttributes;
	private String vendorName;
	private String suggestedProfileName;
	private String izosdwanFlavour;
	
	

	public List<QuestionnaireInputDetails> getQuoteAttributes() {
		return quoteAttributes;
	}

	public void setQuoteAttributes(List<QuestionnaireInputDetails> quoteAttributes) {
		this.quoteAttributes = quoteAttributes;
	}

	public List<ProductOfferingsBean> getIzosdwanSolutions() {
		return izosdwanSolutions;
	}

	public void setIzosdwanSolutions(List<ProductOfferingsBean> izosdwanSolutions) {
		this.izosdwanSolutions = izosdwanSolutions;
	}

	List<Site> site;
	@JsonIgnore
	boolean isManualFeasible;
	private String engagementOptyId;
	private String quoteCode;
	private String classification;
	private String serviceId;
	
	private String ismultiVrf;

	private String erfServiceInventoryTpsServiceId;
	private Boolean siteChangeflag;
	
	List<AttributeDetail> quoteAttributeList = new ArrayList<AttributeDetail>();
	

	public String getIsmultiVrf() {
		return ismultiVrf;
	}

	public void setIsmultiVrf(String ismultiVrf) {
		this.ismultiVrf = ismultiVrf;
	}

	
		
	public String getSuggestedProfileName() {
		return suggestedProfileName;
	}

	public void setSuggestedProfileName(String profileName) {
		this.suggestedProfileName = profileName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
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

	public List<SolutionDetail> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<SolutionDetail> solutions) {
		this.solutions = solutions;
	}

	public List<Site> getSite() {
		return site;
	}

	public void setSite(List<Site> site) {
		this.site = site;
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

	public boolean isManualFeasible() {
		return isManualFeasible;
	}

	public void setManualFeasible(boolean isManualFeasible) {
		this.isManualFeasible = isManualFeasible;
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
	
	

	public Byte getIsMulticircuit() {
		return isMulticircuit;
	}

	public void setIsMulticircuit(Byte isMulticircuit) {
		this.isMulticircuit = isMulticircuit;
	}

	public String getServiceId() { return serviceId; }

	public void setServiceId(String serviceId) { this.serviceId = serviceId; }

	public String getOrderAmendmentParentOrderId() {
		return orderAmendmentParentOrderId;
	}

	public void setOrderAmendmentParentOrderId(String orderAmendmentParentOrderId) {
		this.orderAmendmentParentOrderId = orderAmendmentParentOrderId;
	}
	
	// For IPC
	@JsonProperty("isMigrationOrder")
	private boolean isMigrationOrder;
	
	@JsonProperty("dcOrderId")
	private String dcOrderId;

	public boolean isMigrationOrder() {
		return isMigrationOrder;
	}

	public void setMigrationOrder(boolean isMigrationOrder) {
		this.isMigrationOrder = isMigrationOrder;
	}
	



	public List<AttributeDetail> getQuoteAttributeList() {
		return quoteAttributeList;
	}

	public void setQuoteAttributeList(List<AttributeDetail> quoteAttributeList) {
		this.quoteAttributeList = quoteAttributeList;
	}

	public String getDcOrderId() {
		return dcOrderId;
	}

	public void setDcOrderId(String dcOrderId) {
		this.dcOrderId = dcOrderId;
	}

	@Override
	public String toString() {

		return "QuoteDetail{" +
				"quoteId=" + quoteId +
				", orderId=" + orderId +
				", quoteleId=" + quoteleId +
				", orderType='" + orderType + '\'' +
				", orderIllSiteId=" + orderIllSiteId +
				", orderCategory='" + orderCategory + '\'' +
				", primaryOrSecondaryOrBoth='" + primaryOrSecondaryOrBoth + '\'' +
				", customerId=" + customerId +
				", orderLeIds=" + orderLeIds +
				", productName='" + productName + '\'' +
				", isMulticircuit=" + isMulticircuit +
				", isMigrationOrder=" + isMigrationOrder +
				", orderAmendmentParentOrderId='" + orderAmendmentParentOrderId + '\'' +
				", solutions=" + solutions +
				", site=" + site +
				", isManualFeasible=" + isManualFeasible +
				", engagementOptyId='" + engagementOptyId + '\'' +
				", quoteCode='" + quoteCode + '\'' +
				", classification='" + classification + '\'' +
				", serviceId='" + serviceId + '\'' +
				'}';
	}

	public String getIzosdwanFlavour() {
		return izosdwanFlavour;
	}

	public void setIzosdwanFlavour(String izosdwanFlavour) {
		this.izosdwanFlavour = izosdwanFlavour;
	}

	public String getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public Boolean getSiteChangeflag() {
		return siteChangeflag;
	}

	public void setSiteChangeflag(Boolean siteChangeflag) {
		this.siteChangeflag = siteChangeflag;
	}

}
