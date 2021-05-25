package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.OrderSiteStageAudit;

/**
 * 
 * Bean class
 * 
 *
 * @author ANNE NISHA
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class OrderSiteStageAuditBean implements Serializable {

	private Integer orderStageAuditId;

	private String createdBy;

	private Timestamp createdTime;

	private Timestamp endTime;

	private Timestamp startTime;

	private String orderStageName;

	public OrderSiteStageAuditBean() {
	}

	/**
	 * @return the orderStageAuditId
	 */
	public Integer getOrderStageAuditId() {
		return orderStageAuditId;
	}

	/**
	 * @param orderStageAuditId
	 *            the orderStageAuditId to set
	 */
	public void setOrderStageAuditId(Integer orderStageAuditId) {
		this.orderStageAuditId = orderStageAuditId;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the startTime
	 */
	public Timestamp getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the orderStageName
	 */
	public String getOrderStageName() {
		return orderStageName;
	}

	/**
	 * @param orderStageName the orderStageName to set
	 */
	public void setOrderStageName(String orderStageName) {
		this.orderStageName = orderStageName;
	}

	
}
