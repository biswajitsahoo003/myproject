package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

import javax.persistence.GenerationType;

/**
 * The persistent class for the gde_schedule_details database table.
 * @author archchan
 *
 */
@Entity
@Table(name="gde_schedule_details")
@NamedQuery(name="GdeScheduleDetails.findAll", query="SELECT g FROM GdeScheduleDetails g")
public class GdeScheduleDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4270383151444065152L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="activation_status")
	private String activationStatus;

	@Column(name="created_time")
	private Timestamp createdTime;

	@Column(name="feasibility_status")
	private String feasibilityStatus;

	@Column(name="feasibility_validity")
	private Timestamp feasibilityValidity;

	@Column(name="link_id")
	private int linkId;

	@Column(name="mdso_feasibility_uuid")
	private String mdsoFeasibilityUuid;

	@Column(name="mdso_resource_id")
	private String mdsoResourceId;

	@Column(name="quote_code")
	private String quoteCode;

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
	
	@Column(name="base_circuit_bw")
	private Integer baseCircuitBw;
	
	@Column(name="bw_on_demand")
	private Integer bwOnDemand;
	
	@Column(name="payment_currency")
	private String paymentCurrency;
	
	@Column (name="chargeable_nrc")
	private Double chargeableNrc;
	
	@Column (name="bandwidth_unit")
	private String bandwidthUnit;
	
	@Column (name="is_active")
	private Byte isActive;

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

	public int getLinkId() {
		return this.linkId;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
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

	public String getQuoteCode() {
		return this.quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
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

	public Byte getIsActive() {
		return isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}
	
	
}


