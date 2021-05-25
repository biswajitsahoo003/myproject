package com.tcl.dias.oms.beans;

/**
 * This file contains the OrderLeEnitityBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderLeSVBean {

	private Integer legalEntityId;

	private String stage;

	private String opportunityId;

	private String accountCuid;

	private String legalEntityName;

	private String poNumber;

	private String poDate;

	private String orderType;

	private String orderCategory;
	
	private String leGstNumber;

	public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	/**
	 * @return the legalEntityId
	 */
	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	/**
	 * @param legalEntityId
	 *            the legalEntityId to set
	 */
	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * @return the opportunityId
	 */
	public String getOpportunityId() {
		return opportunityId;
	}

	/**
	 * @param opportunityId
	 *            the opportunityId to set
	 */
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getAccountCuid() {
		return accountCuid;
	}

	public void setAccountCuid(String accountCuid) {
		this.accountCuid = accountCuid;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
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

	public String getLeGstNumber() {
		return leGstNumber;
	}

	public void setLeGstNumber(String leGstNumber) {
		this.leGstNumber = leGstNumber;
	}

	
}
