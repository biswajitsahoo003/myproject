package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class AuditBean implements Serializable {

	private static final long serialVersionUID = -3328114376259577452L;

	private Integer orderId;

	private Integer orderToLeId;

	private Integer orderIllSiteId;

	private Integer locationId;

	private String currentStatus;

	private String currentStage;

	private List<OrderSiteStatusAuditBean> audit;

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
	 * @return the orderToLeId
	 */
	public Integer getOrderToLeId() {
		return orderToLeId;
	}

	/**
	 * @param orderToLeId
	 *            the orderToLeId to set
	 */
	public void setOrderToLeId(Integer orderToLeId) {
		this.orderToLeId = orderToLeId;
	}

	/**
	 * @return the orderIllSiteId
	 */
	public Integer getOrderIllSiteId() {
		return orderIllSiteId;
	}

	/**
	 * @param orderIllSiteId
	 *            the orderIllSiteId to set
	 */
	public void setOrderIllSiteId(Integer orderIllSiteId) {
		this.orderIllSiteId = orderIllSiteId;
	}

	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId
	 *            the locationId to set
	 */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the audit
	 */
	public List<OrderSiteStatusAuditBean> getAudit() {
		if (audit == null) {
			audit = new ArrayList<>();
		}
		return audit;
	}

	/**
	 * @param audit
	 *            the audit to set
	 */
	public void setAudit(List<OrderSiteStatusAuditBean> audit) {
		this.audit = audit;
	}

	/**
	 * @return the currentStatus
	 */
	public String getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param currentStatus
	 *            the currentStatus to set
	 */
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	/**
	 * @return the currentStage
	 */
	public String getCurrentStage() {
		return currentStage;
	}

	/**
	 * @param currentStage
	 *            the currentStage to set
	 */
	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}

}
