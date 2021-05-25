package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This file contains the QuoteSumaryBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteSummaryBean {

	private Integer quoteId;

	private String quoteCode;

	private String createdBy;

	private Date createdTime;

	private String quoteStage;

	private String nsQuote;

	private List<QuoteLeSVBean> legalEntity;

	private List<QuoteFamilySVBean> quoteFamily;

	/**
	 * @return the quoteId
	 */
	public Integer getQuoteId() {
		return quoteId;
	}

	/**
	 * @param quoteId the quoteId to set
	 */
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	/**
	 * @return the quoteCode
	 */
	public String getQuoteCode() {
		return quoteCode;
	}

	/**
	 * @param quoteCode the quoteCode to set
	 */
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the quoteStage
	 */
	public String getQuoteStage() {
		return quoteStage;
	}

	/**
	 * @param quoteStage the quoteStage to set
	 */
	public void setQuoteStage(String quoteStage) {
		this.quoteStage = quoteStage;
	}

	/**
	 * @return the legalEntity
	 */
	public List<QuoteLeSVBean> getLegalEntity() {
		if (legalEntity == null) {
			legalEntity = new ArrayList<>();
		}
		return legalEntity;
	}

	/**
	 * @param legalEntity the legalEntity to set
	 */
	public void setLegalEntity(List<QuoteLeSVBean> legalEntity) {
		this.legalEntity = legalEntity;
	}

	/**
	 * @return the quoteFamily
	 */
	public List<QuoteFamilySVBean> getQuoteFamily() {

		if (quoteFamily == null) {
			quoteFamily = new ArrayList<>();
		}
		return quoteFamily;
	}

	/**
	 * @param quoteFamily the quoteFamily to set
	 */
	public void setQuoteFamily(List<QuoteFamilySVBean> quoteFamily) {
		this.quoteFamily = quoteFamily;
	}

	public String getNsQuote() {
		return nsQuote;
	}

	public void setNsQuote(String nsQuote) {
		this.nsQuote = nsQuote;
	}

}
