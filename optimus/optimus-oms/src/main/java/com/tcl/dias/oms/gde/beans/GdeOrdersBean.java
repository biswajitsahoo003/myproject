package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Bean class to hold Gde order data
 * @author archchan
 *
 */
@JsonInclude(Include.NON_NULL)
public class GdeOrdersBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String orderCode;

	private Integer createdBy;

	private Date createdTime;

	private Date effectiveDate;

	private Date endDate;

	private String stage;

	private Date startDate;

	private Byte status;

	private Integer termInMonths;

	private Set<OrderToLeBean> orderToLeBeans;

	private String checkList;
	
	private Integer customerId;
	
	private String orderType;
	
	private String orderCategory;
	
	private Boolean isMacdInitiated = false;
	
	private String serviceId;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate
	 *            the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * @return the termInMonths
	 */
	public Integer getTermInMonths() {
		return termInMonths;
	}

	/**
	 * @param termInMonths
	 *            the termInMonths to set
	 */
	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}

	/**
	 * @return the orderToLeBeans
	 */
	public Set<OrderToLeBean> getOrderToLeBeans() {
		return orderToLeBeans;
	}

	/**
	 * @param orderToLeBeans
	 *            the orderToLeBeans to set
	 */
	public void setOrderToLeBeans(Set<OrderToLeBean> orderToLeBeans) {
		this.orderToLeBeans = orderToLeBeans;
	}

	/**
	 * @return the orderCode
	 */

	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode
	 *            the orderCode to set
	 */

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCheckList() {
		return checkList;
	}

	public void setCheckList(String checkList) {
		this.checkList = checkList;
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

	public Boolean getIsMacdInitiated() {
		return isMacdInitiated;
	}

	public void setIsMacdInitiated(Boolean isMacdInitiated) {
		this.isMacdInitiated = isMacdInitiated;
	}
	
	

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public String toString() {
		return "OrdersBean [id=" + id + ", orderCode=" + orderCode + ", createdBy=" + createdBy + ", createdTime="
				+ createdTime + ", effectiveDate=" + effectiveDate + ", endDate=" + endDate + ", stage=" + stage + ", startDate=" + startDate
				+ ", status=" + status + ", termInMonths=" + termInMonths + ", orderToLeBeans=" + orderToLeBeans
				+ ", checkList=" + checkList 
				+ ", orderType=" + orderType 
				+ ", orderCategory=" + orderCategory 
				+ ", isMacdInitiated=" + isMacdInitiated
				+ ", serviceId=" + serviceId + "]";
	}

}