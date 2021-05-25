package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the gde_schedule_details database table.
 * @author archchan
 *
 */
@Entity
@Table(name="odr_schedule_details_audit")
@NamedQuery(name="OdrScheduleDetailsAudit.findAll", query="SELECT o FROM OdrScheduleDetailsAudit o")
public class OdrScheduleDetailsAudit implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="odr_schedule_detail_id")
	private Integer odrScheduleDetailId;

	@Column(name="activation_status")
	private String activationStatus;

	@Column(name="created_time")
	private Timestamp createdTime;

	@Column(name="feasibility_status")
	private String feasibilityStatus;

	@Column(name="feasibility_validity")
	private Timestamp feasibilityValidity;

	@Column(name="mdso_feasibility_uuid")
	private String mdsoFeasibilityUuid;

	@Column(name="mdso_resource_id")
	private String mdsoResourceId;

	@Column(name="order_code")
	private String orderCode;

	@Column(name="schedule_end_date")
	private OffsetDateTime scheduleEndDate;

	@Column(name="schedule_start_date")
	private OffsetDateTime scheduleStartDate;

	@Column(name="service_id")
	private String serviceId;

	private int slots;

	@Column(name="updated_time")
	private Timestamp updatedTime;
	
	@Column(name="upgraded_bw")
	private Integer upgradedBw;
	
	@Column(name="order_link_id")
	private Integer orderLinkId;
	
	@Column(name="base_circuit_bw")
	private Integer baseCircuitBw;
	
	@Column(name="bw_on_demand")
	private Integer bwOnDemand;
	
	@Column(name="payment_currency")
	private String paymentCurrency;
	
	@Column (name="schedule_operation_id")
	private String scheduleOperationId;
	
	@Column (name="chargeable_nrc")
	private Double chargeableNrc;
	
	@Column (name="bandwidth_unit")
	private String bandwidthUnit;
	
	@Column(name="ticket_id")
	private String ticketId;
	
	@Column(name="schedule_id")
	private String scheduleId;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActivationStatus() {
		return this.activationStatus;
	}

	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getFeasibilityStatus() {
		return this.feasibilityStatus;
	}

	public void setFeasibilityStatus(String feasibilityStatus) {
		this.feasibilityStatus = feasibilityStatus;
	}

	public Timestamp getFeasibilityValidity() {
		return this.feasibilityValidity;
	}

	public void setFeasibilityValidity(Timestamp feasibilityValidity) {
		this.feasibilityValidity = feasibilityValidity;
	}

	public String getMdsoFeasibilityUuid() {
		return this.mdsoFeasibilityUuid;
	}

	public void setMdsoFeasibilityUuid(String mdsoFeasibilityUuid) {
		this.mdsoFeasibilityUuid = mdsoFeasibilityUuid;
	}

	public String getMdsoResourceId() {
		return this.mdsoResourceId;
	}

	public void setMdsoResourceId(String mdsoResourceId) {
		this.mdsoResourceId = mdsoResourceId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public OffsetDateTime getScheduleEndDate() {
		return this.scheduleEndDate;
	}

	public void setScheduleEndDate(OffsetDateTime scheduleEndDate) {
		this.scheduleEndDate = scheduleEndDate;
	}

	public OffsetDateTime getScheduleStartDate() {
		return this.scheduleStartDate;
	}

	public void setScheduleStartDate(OffsetDateTime scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getSlots() {
		return this.slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getUpgradedBw() {
		return upgradedBw;
	}

	public void setUpgradedBw(Integer upgradedBw) {
		this.upgradedBw = upgradedBw;
	}

	public Integer getOrderLinkId() {
		return orderLinkId;
	}

	public void setOrderLinkId(Integer orderLinkId) {
		this.orderLinkId = orderLinkId;
	}

	public Integer getBaseCircuitBw() {
		return baseCircuitBw;
	}

	public void setBaseCircuitBw(Integer baseCircuitBw) {
		this.baseCircuitBw = baseCircuitBw;
	}

	public Integer getBwOnDemand() {
		return bwOnDemand;
	}

	public void setBwOnDemand(Integer bwOnDemand) {
		this.bwOnDemand = bwOnDemand;
	}

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public String getScheduleOperationId() {
		return scheduleOperationId;
	}

	public void setScheduleOperationId(String scheduleOperationId) {
		this.scheduleOperationId = scheduleOperationId;
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

	public String getBandwidthUnit() {
		return bandwidthUnit;
	}

	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}

	public Integer getOdrScheduleDetailId() {
		return odrScheduleDetailId;
	}

	public void setOdrScheduleDetailId(Integer odrScheduleDetailId) {
		this.odrScheduleDetailId = odrScheduleDetailId;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}	
	
	
	
}


