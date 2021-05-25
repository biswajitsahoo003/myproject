package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;

/**
 * Gde schedule Detail bean 
 * @author archchan
 *
 */
public class GdeScheduleDetailBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serviceId;
	private String activationStatus;
	private String feasibilityStatus;
	private String feasibilityValidity;
	private int linkId;
	private String mdsoFeasibilityUuid;
	private String mdsoResourceId;
	private String quoteCode;
	private String scheduleEndDate;
	private String scheduleStartDate;
	private int slots;
	private String updatedTime;
	private String upgradedBw; 
	private String createdTime;
	private String baseCircuitBw;
	private String bwOnDemand;
	private Integer orderLinkId;
	private String operationId;
	private String ticketId;
	private String scheduleId;
	private Double chargeableNrc;
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getActivationStatus() {
		return activationStatus;
	}
	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}
	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}
	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}
	public String getFeasibilityValidity() {
		return feasibilityValidity;
	}
	public void setFeasibilityValidity(String feasibilityValidity) {
		this.feasibilityValidity = feasibilityValidity;
	}
	public int getLinkId() {
		return linkId;
	}
	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}
	public String getMdsoFeasibilityUuid() {
		return mdsoFeasibilityUuid;
	}
	public void setMdsoFeasibilityUuid(String mdsoFeasibilityUuid) {
		this.mdsoFeasibilityUuid = mdsoFeasibilityUuid;
	}
	public String getMdsoResourceId() {
		return mdsoResourceId;
	}
	public void setMdsoResourceId(String mdsoResourceId) {
		this.mdsoResourceId = mdsoResourceId;
	}
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public String getScheduleEndDate() {
		return scheduleEndDate;
	}
	public void setScheduleEndDate(String scheduleEndDate) {
		this.scheduleEndDate = scheduleEndDate;
	}
	public String getScheduleStartDate() {
		return scheduleStartDate;
	}
	public void setScheduleStartDate(String scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}
	public int getSlots() {
		return slots;
	}
	public void setSlots(int slots) {
		this.slots = slots;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getUpgradedBw() {
		return upgradedBw;
	}
	public void setUpgradedBw(String upgradedBw) {
		this.upgradedBw = upgradedBw;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getBaseCircuitBw() {
		return baseCircuitBw;
	}
	public void setBaseCircuitBw(String baseCircuitBw) {
		this.baseCircuitBw = baseCircuitBw;
	}
	public String getBwOnDemand() {
		return bwOnDemand;
	}
	public void setBwOnDemand(String bwOnDemand) {
		this.bwOnDemand = bwOnDemand;
	}
	public Integer getOrderLinkId() {
		return orderLinkId;
	}
	public void setOrderLinkId(Integer orderLinkId) {
		this.orderLinkId = orderLinkId;
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Double getChargeableNrc() {
		return chargeableNrc;
	}
	public void setChargeableNrc(Double chargeableNrc) {
		this.chargeableNrc = chargeableNrc;
	}
	

}

