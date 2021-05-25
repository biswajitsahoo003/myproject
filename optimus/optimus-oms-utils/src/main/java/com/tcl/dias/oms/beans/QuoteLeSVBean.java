
package com.tcl.dias.oms.beans;

/**
 * This file contains the OrderLeEnitityBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteLeSVBean {

	private Integer legalEntityId;

	private String stage;
	
	private String opportunityId;
	
	private String accountCuid;

	private String legalEntityName;
	
	private String quoteType;

	/**
	 * @return the legalEntityId
	 */
	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	/**
	 * @param legalEntityId the legalEntityId to set
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
	 * @param stage the stage to set
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
	 * @param opportunityId the opportunityId to set
	 */
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	/**
	 * @return the accountCuid
	 */
	public String getAccountCuid() {
		return accountCuid;
	}

	/**
	 * @param accountCuid the accountCuid to set
	 */
	public void setAccountCuid(String accountCuid) {
		this.accountCuid = accountCuid;
	}

	/**
	 * @return the legalEntityName
	 */
	public String getLegalEntityName() {
		return legalEntityName;
	}

	/**
	 * @param legalEntityName the legalEntityName to set
	 */
	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}
	
}
