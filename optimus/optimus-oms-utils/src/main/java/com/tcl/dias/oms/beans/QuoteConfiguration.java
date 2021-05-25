package com.tcl.dias.oms.beans;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.common.utils.QuoteAccess;

/**
 * Bean file
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteConfiguration {

	private String quoteCode;
	private Integer quoteId;
	private String productName;
	private Date quoteCreatedDate;
	private String quoteStage;
	private BigInteger siteCount;
	private String quoteType;
	private String quoteCategory;
	private String quoteOptyId;
	private String nsQuote;
	private String orderCode;
	private Integer orderId;
	private Integer orderLeId;
	private Date orderCreatedDate;
	private String orderStage;
	private String orderType;
	private String orderCategory;
	private String orderOptyId;
	private Integer isAmended;
	private String isMulticircuit;
	private List<MilestoneStagesBean> milestoneStagesBean;
	private String quoteAccess = QuoteAccess.FULL.toString();
	private String quoteCreatedUserType;
	@JsonIgnore
	private Integer quoteCreatedBy;
	private String isGscMultiMacd = "No";

	public String getQuoteOptyId() {
		return quoteOptyId;
	}

	public void setQuoteOptyId(String quoteOptyId) {
		this.quoteOptyId = quoteOptyId;
	}

	public String getOrderOptyId() {
		return orderOptyId;
	}

	public void setOrderOptyId(String orderOptyId) {
		this.orderOptyId = orderOptyId;
	}

	private String customerName;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@JsonIgnore
	private Date createdDate;

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getQuoteCreatedDate() {
		return quoteCreatedDate;
	}

	public void setQuoteCreatedDate(Date quoteCreatedDate) {
		this.quoteCreatedDate = quoteCreatedDate;
	}

	public String getQuoteStage() {
		return quoteStage;
	}

	public void setQuoteStage(String quoteStage) {
		this.quoteStage = quoteStage;
	}

	public BigInteger getSiteCount() {
		return siteCount;
	}

	public void setSiteCount(BigInteger siteCount) {
		this.siteCount = siteCount;
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

	public Date getOrderCreatedDate() {
		return orderCreatedDate;
	}

	public void setOrderCreatedDate(Date orderCreatedDate) {
		this.orderCreatedDate = orderCreatedDate;
	}

	public String getOrderStage() {
		return orderStage;
	}

	public void setOrderStage(String orderStage) {
		this.orderStage = orderStage;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the quoteType
	 */
	public String getQuoteType() {
		return quoteType;
	}

	/**
	 * @param quoteType the quoteType to set
	 */
	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	/**
	 * @return the quoteCategory
	 */
	public String getQuoteCategory() {
		return quoteCategory;
	}

	/**
	 * @param quoteCategory the quoteCategory to set
	 */
	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the orderCategory
	 */
	public String getOrderCategory() {
		return orderCategory;
	}

	/**
	 * @param orderCategory the orderCategory to set
	 */
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getIsMulticircuit() {
		return isMulticircuit;
	}

	public void setIsMulticircuit(String isMulticircuit) {
		this.isMulticircuit = isMulticircuit;
	}

	public String getNsQuote() {
		return nsQuote;
	}

	public void setNsQuote(String nsQuote) {
		this.nsQuote = nsQuote;
	}

	public Integer getIsAmended() {
		return isAmended;
	}

	public void setIsAmended(Integer isAmended) {
		this.isAmended = isAmended;
	}

	public String getQuoteAccess() {
		return quoteAccess;
	}

	public void setQuoteAccess(String quoteAccess) {
		this.quoteAccess = quoteAccess;
	}

	public String getQuoteCreatedUserType() {
		return quoteCreatedUserType;
	}

	public void setQuoteCreatedUserType(String quoteCreatedUserType) {
		this.quoteCreatedUserType = quoteCreatedUserType;
	}

	public Integer getQuoteCreatedBy() {
		return quoteCreatedBy;
	}

	public void setQuoteCreatedBy(Integer quoteCreatedBy) {
		this.quoteCreatedBy = quoteCreatedBy;
	}

	public String getIsGscMultiMacd() {
		return isGscMultiMacd;
	}

	public void setIsGscMultiMacd(String isGscMultiMacd) {
		this.isGscMultiMacd = isGscMultiMacd;
	}

	public List<MilestoneStagesBean> getMilestoneStagesBean() {
		return milestoneStagesBean;
	}

	public void setMilestoneStagesBean(List<MilestoneStagesBean> milestoneStagesBean) {
		this.milestoneStagesBean = milestoneStagesBean;
	}

	public Integer getOrderLeId() {
		return orderLeId;
	}

	public void setOrderLeId(Integer orderLeId) {
		this.orderLeId = orderLeId;
	}

	@Override
	public String toString() {
		return "QuoteConfiguration{" +
				"quoteCode='" + quoteCode + '\'' +
				", quoteId=" + quoteId +
				", productName='" + productName + '\'' +
				", quoteCreatedDate=" + quoteCreatedDate +
				", quoteStage='" + quoteStage + '\'' +
				", siteCount=" + siteCount +
				", quoteType='" + quoteType + '\'' +
				", quoteCategory='" + quoteCategory + '\'' +
				", quoteOptyId='" + quoteOptyId + '\'' +
				", nsQuote='" + nsQuote + '\'' +
				", orderCode='" + orderCode + '\'' +
				", orderId=" + orderId +
				", orderLeId=" + orderLeId +
				", orderCreatedDate=" + orderCreatedDate +
				", orderStage='" + orderStage + '\'' +
				", orderType='" + orderType + '\'' +
				", orderCategory='" + orderCategory + '\'' +
				", orderOptyId='" + orderOptyId + '\'' +
				", isAmended=" + isAmended +
				", isMulticircuit='" + isMulticircuit + '\'' +
				", isGscMultiMacd='" + isGscMultiMacd + '\'' +
				", customerName='" + customerName + '\'' +
				", createdDate=" + createdDate +
				", mileStoneStages=" + milestoneStagesBean +
				'}';
	}
}
