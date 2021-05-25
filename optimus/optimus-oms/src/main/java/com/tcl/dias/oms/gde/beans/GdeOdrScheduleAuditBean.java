package com.tcl.dias.oms.gde.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import com.tcl.dias.oms.entity.entities.OdrScheduleDetailsAudit;

public class GdeOdrScheduleAuditBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String orderCode;
	private Integer odrScheduleDetailId;
	private String activtionStatus;
	private Timestamp createdTime; 
	private String feasibilityStatus;
	private String mdsoFeasibilityId;
	private String mdsoResourceId;
	private String scheduleOperationId;
	private Timestamp updatedTime;
	private Timestamp feasibilityValidity;
	private String serviceId;
	private Integer orderLinkId;
	private String ticketId;
	private String scheduleId;
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getActivtionStatus() {
		return activtionStatus;
	}
	public void setActivtionStatus(String activtionStatus) {
		this.activtionStatus = activtionStatus;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public String getFeasibilityStatus() {
		return feasibilityStatus;
	}
	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}
	public String getMdsoFeasibilityId() {
		return mdsoFeasibilityId;
	}
	public void setMdsoFeasibilityId(String mdsoFeasibilityId) {
		this.mdsoFeasibilityId = mdsoFeasibilityId;
	}
	public String getMdsoResourceId() {
		return mdsoResourceId;
	}
	public void setMdsoResourceId(String mdsoResourceId) {
		this.mdsoResourceId = mdsoResourceId;
	}
	
	public String getScheduleOperationId() {
		return scheduleOperationId;
	}
	public void setScheduleOperationId(String scheduleOperationId) {
		this.scheduleOperationId = scheduleOperationId;
	}
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	public Timestamp getFeasibilityValidity() {
		return feasibilityValidity;
	}
	public void setFeasibilityValidity(Timestamp feasibilityValidity) {
		this.feasibilityValidity = feasibilityValidity;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}	
	public Integer getOrderLinkId() {
		return orderLinkId;
	}
	public void setOrderLinkId(Integer orderLinkId) {
		this.orderLinkId = orderLinkId;
	}
	
	public Integer getOdrScheduleDetailId() {
		return odrScheduleDetailId;
	}
	public void setOdrScheduleDetailId(Integer odrScheduleDetailId) {
		this.odrScheduleDetailId = odrScheduleDetailId;
	}
	public GdeOdrScheduleAuditBean() {
		
	}
	public String getTicketId() {
		return ticketId;
	}
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	public String getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	public GdeOdrScheduleAuditBean (OdrScheduleDetailsAudit orderScheduleDetails){
		this.setActivtionStatus(orderScheduleDetails.getActivationStatus());
		this.setCreatedTime(orderScheduleDetails.getCreatedTime());
		this.setFeasibilityStatus(orderScheduleDetails.getFeasibilityStatus());
		this.setFeasibilityValidity(orderScheduleDetails.getFeasibilityValidity());
		this.setMdsoFeasibilityId(orderScheduleDetails.getMdsoFeasibilityUuid());
		this.setMdsoResourceId(orderScheduleDetails.getMdsoResourceId());
		this.setScheduleOperationId(orderScheduleDetails.getScheduleOperationId());
		this.setOrderCode(orderScheduleDetails.getOrderCode());
		this.setServiceId(orderScheduleDetails.getServiceId());
		this.setUpdatedTime(orderScheduleDetails.getUpdatedTime());
		this.setOrderLinkId(orderScheduleDetails.getOrderLinkId());
		this.setOdrScheduleDetailId(orderScheduleDetails.getOdrScheduleDetailId());
		this.setTicketId(orderScheduleDetails.getTicketId());
		this.setScheduleId(orderScheduleDetails.getScheduleId());
	}
	
	

}
