package com.tcl.dias.oms.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is used for dashboard screen for quote
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class DashboardQuoteBean {

	private Date createdDate;
	
	private Integer quoteId;
	
	private String quoteCode;
	
	private Integer orderId;
	
	private String orderCode;
	
	private String quoteStage;
	

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
	 * @return the orderId
	 */
	public Integer getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode the orderCode to set
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	

	
}
