package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "downtime_details")
@NamedQuery(name = "DownTimeDetails.findAll", query = "select d from DownTimeDetails d")
public class DownTimeDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "sc_service_detail_id")
	private Integer scServiceDetailId;
	
	@Column(name = "solution_id")
	private Integer solutionId;
	
	@Column(name = "order_code")
	private String orderCode;
	
	@Column(name = "is_byon_ready_for_downtime")
	private String isByonReadyForDownTime;
	
	@Column(name = "is_byon_ready_for_customer_appointment")
	private String isByonReadyForCustomerAppointment;
	
	@Column(name = "is_ip_downtime_required")
	private String isIpDownTimeRequired;
	
	@Column(name = "is_ip_ready_for_downtime")
	private String isIpReadyForDownTime;
	
	@Column(name = "is_tx_downtime_required")
	private String isTxDownTimeRequired;
	
	@Column(name = "is_tx_ready_for_downtime")
	private String isTxReadyForDownTime;
	
	@Column(name = "is_cpe_downtime_required")
	private String isCpeDowntimeRequired;
	
	@Column(name = "is_cpe_ready_for_downtime")
	private String isCpeReadyForDownTime;
	
	@Column(name = "is_cpe_customer_appointment_required")
	private String isCpeCustomerAppointmentRequired;
	
	@Column(name = "is_cpe_ready_for_customer_appointment")
	private String isCpeReadyForCustomerAppointment;
	
	@Column(name = "is_lm_test_required")
	private String isLMTestRequired;
	
	@Column(name = "is_lm_ready_for_customer_appointment")
	private String isLMReadyForCustomerAppointment;
	
	@Column(name = "is_config_completed")
	private String isConfigCompleted;
	
	@Column(name = "is_cpe_already")
	private String isCpeAlreadyManaged;
	
	@Column(name = "is_e2e_completed")
	private String isE2ECompleted;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "is_provision_ready_for_downtime")
	private String isProvisionReadyForDownTime;
	
	@Column(name = "is_provision_ready_for_customer_appointment")
	private String isProvisionReadyForCustomerAppointment;

	/*@Column(name = "is_cpe_required")
	private String isCpeRequired;

	@Column(name = "is_track_cpe_delivery_closed")
	private String isTrackCpeDeliveryClosed;
	
	@Column(name = "is_byon_readiness_closed")
	private String isByonReadinessClosed;
	
	@Column(name = "is_commercial_vetting_required")
	private String isCommercialVettingRequired;
	
	@Column(name = "is_commercial_vetting_closed")
	private String isCommercialVettingClosed;
	
	@Column(name = "is_ip_required")
	private String isIpRequired;
	
	@Column(name = "is_tx_required")
	private String isTxRequired;
	
	@Column(name = "is_ip_downtime_required")
	private String isIpDowntimeRequired;
	
	@Column(name = "is_tx_downtime_required")
	private String isTxDowntimeRequired;
	
	@Column(name = "is_tx_cable_extension_closed")
	private String isTxCableExtensionClosed;
	
	@Column(name = "is_activity_go_ahead_required")
	private String isActivityGoAheadRequired;
	
	@Column(name = "is_assign_dummy_required")
	private String isAssignDummyRequired;
	
	@Column(name = "is_assign_dummy_closed")
	private String isAssignDummyClosed;
	
	@Column(name = "is_p2p_required")
	private String isP2PRequired;
	
	@Column(name = "is_p2p_closed")
	private String isP2PClosed;*/
	
	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Timestamp updatedDate;
	
	/*@Column(name = "is_service_activation_closed")
	private String isServiceActivationClosed;*/

	public DownTimeDetails() {
		// do nothing
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScServiceDetailId() {
		return scServiceDetailId;
	}

	public void setScServiceDetailId(Integer scServiceDetailId) {
		this.scServiceDetailId = scServiceDetailId;
	}

	/*public String getIsTrackCpeDeliveryClosed() {
		return isTrackCpeDeliveryClosed;
	}

	public void setIsTrackCpeDeliveryClosed(String isTrackCpeDeliveryClosed) {
		this.isTrackCpeDeliveryClosed = isTrackCpeDeliveryClosed;
	}

	public String getIsByonReadinessClosed() {
		return isByonReadinessClosed;
	}

	public void setIsByonReadinessClosed(String isByonReadinessClosed) {
		this.isByonReadinessClosed = isByonReadinessClosed;
	}

	public String getIsCommercialVettingClosed() {
		return isCommercialVettingClosed;
	}

	public void setIsCommercialVettingClosed(String isCommercialVettingClosed) {
		this.isCommercialVettingClosed = isCommercialVettingClosed;
	}*/

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getIsByonReadyForDownTime() {
		return isByonReadyForDownTime;
	}

	public void setIsByonReadyForDownTime(String isByonReadyForDownTime) {
		this.isByonReadyForDownTime = isByonReadyForDownTime;
	}

	public String getIsIpDownTimeRequired() {
		return isIpDownTimeRequired;
	}

	public void setIsIpDownTimeRequired(String isIpDownTimeRequired) {
		this.isIpDownTimeRequired = isIpDownTimeRequired;
	}

	public String getIsIpReadyForDownTime() {
		return isIpReadyForDownTime;
	}

	public void setIsIpReadyForDownTime(String isIpReadyForDownTime) {
		this.isIpReadyForDownTime = isIpReadyForDownTime;
	}

	public String getIsTxDownTimeRequired() {
		return isTxDownTimeRequired;
	}

	public void setIsTxDownTimeRequired(String isTxDownTimeRequired) {
		this.isTxDownTimeRequired = isTxDownTimeRequired;
	}

	public String getIsTxReadyForDownTime() {
		return isTxReadyForDownTime;
	}

	public void setIsTxReadyForDownTime(String isTxReadyForDownTime) {
		this.isTxReadyForDownTime = isTxReadyForDownTime;
	}

	public String getIsCpeDowntimeRequired() {
		return isCpeDowntimeRequired;
	}

	public void setIsCpeDowntimeRequired(String isCpeDowntimeRequired) {
		this.isCpeDowntimeRequired = isCpeDowntimeRequired;
	}

	public String getIsCpeReadyForDownTime() {
		return isCpeReadyForDownTime;
	}

	public void setIsCpeReadyForDownTime(String isCpeReadyForDownTime) {
		this.isCpeReadyForDownTime = isCpeReadyForDownTime;
	}

	public String getIsConfigCompleted() {
		return isConfigCompleted;
	}

	public void setIsConfigCompleted(String isConfigCompleted) {
		this.isConfigCompleted = isConfigCompleted;
	}

	public Integer getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}

	public String getIsCpeAlreadyManaged() {
		return isCpeAlreadyManaged;
	}

	public void setIsCpeAlreadyManaged(String isCpeAlreadyManaged) {
		this.isCpeAlreadyManaged = isCpeAlreadyManaged;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getIsE2ECompleted() {
		return isE2ECompleted;
	}

	public void setIsE2ECompleted(String isE2ECompleted) {
		this.isE2ECompleted = isE2ECompleted;
	}

	public String getIsCpeCustomerAppointmentRequired() {
		return isCpeCustomerAppointmentRequired;
	}

	public void setIsCpeCustomerAppointmentRequired(String isCpeCustomerAppointmentRequired) {
		this.isCpeCustomerAppointmentRequired = isCpeCustomerAppointmentRequired;
	}

	public String getIsCpeReadyForCustomerAppointment() {
		return isCpeReadyForCustomerAppointment;
	}

	public void setIsCpeReadyForCustomerAppointment(String isCpeReadyForCustomerAppointment) {
		this.isCpeReadyForCustomerAppointment = isCpeReadyForCustomerAppointment;
	}

	public String getIsByonReadyForCustomerAppointment() {
		return isByonReadyForCustomerAppointment;
	}

	public void setIsByonReadyForCustomerAppointment(String isByonReadyForCustomerAppointment) {
		this.isByonReadyForCustomerAppointment = isByonReadyForCustomerAppointment;
	}

	public String getIsLMTestRequired() {
		return isLMTestRequired;
	}

	public void setIsLMTestRequired(String isLMTestRequired) {
		this.isLMTestRequired = isLMTestRequired;
	}

	public String getIsLMReadyForCustomerAppointment() {
		return isLMReadyForCustomerAppointment;
	}

	public void setIsLMReadyForCustomerAppointment(String isLMReadyForCustomerAppointment) {
		this.isLMReadyForCustomerAppointment = isLMReadyForCustomerAppointment;
	}

	public String getIsProvisionReadyForDownTime() {
		return isProvisionReadyForDownTime;
	}

	public void setIsProvisionReadyForDownTime(String isProvisionReadyForDownTime) {
		this.isProvisionReadyForDownTime = isProvisionReadyForDownTime;
	}

	public String getIsProvisionReadyForCustomerAppointment() {
		return isProvisionReadyForCustomerAppointment;
	}

	public void setIsProvisionReadyForCustomerAppointment(String isProvisionReadyForCustomerAppointment) {
		this.isProvisionReadyForCustomerAppointment = isProvisionReadyForCustomerAppointment;
	}
	
	
	/*public String getIsIpDowntimeRequired() {
		return isIpDowntimeRequired;
	}

	public void setIsIpDowntimeRequired(String isIpDowntimeRequired) {
		this.isIpDowntimeRequired = isIpDowntimeRequired;
	}

	public String getIsActivityGoAheadRequired() {
		return isActivityGoAheadRequired;
	}

	public void setIsActivityGoAheadRequired(String isActivityGoAheadRequired) {
		this.isActivityGoAheadRequired = isActivityGoAheadRequired;
	}

	public String getIsAssignDummyRequired() {
		return isAssignDummyRequired;
	}

	public void setIsAssignDummyRequired(String isAssignDummyRequired) {
		this.isAssignDummyRequired = isAssignDummyRequired;
	}

	public String getIsAssignDummyClosed() {
		return isAssignDummyClosed;
	}

	public void setIsAssignDummyClosed(String isAssignDummyClosed) {
		this.isAssignDummyClosed = isAssignDummyClosed;
	}

	public String getIsP2PRequired() {
		return isP2PRequired;
	}

	public void setIsP2PRequired(String isP2PRequired) {
		this.isP2PRequired = isP2PRequired;
	}

	public String getIsP2PClosed() {
		return isP2PClosed;
	}

	public void setIsP2PClosed(String isP2PClosed) {
		this.isP2PClosed = isP2PClosed;
	}

	public String getIsCommercialVettingRequired() {
		return isCommercialVettingRequired;
	}

	public void setIsCommercialVettingRequired(String isCommercialVettingRequired) {
		this.isCommercialVettingRequired = isCommercialVettingRequired;
	}

	public String getIsTxDowntimeRequired() {
		return isTxDowntimeRequired;
	}

	public void setIsTxDowntimeRequired(String isTxDowntimeRequired) {
		this.isTxDowntimeRequired = isTxDowntimeRequired;
	}

	public String getIsTxCableExtensionClosed() {
		return isTxCableExtensionClosed;
	}

	public void setIsTxCableExtensionClosed(String isTxCableExtensionClosed) {
		this.isTxCableExtensionClosed = isTxCableExtensionClosed;
	}

	public String getIsIpRequired() {
		return isIpRequired;
	}

	public void setIsIpRequired(String isIpRequired) {
		this.isIpRequired = isIpRequired;
	}

	public String getIsTxRequired() {
		return isTxRequired;
	}

	public void setIsTxRequired(String isTxRequired) {
		this.isTxRequired = isTxRequired;
	}

	public String getIsCpeRequired() {
		return isCpeRequired;
	}

	public void setIsCpeRequired(String isCpeRequired) {
		this.isCpeRequired = isCpeRequired;
	}

	public String getIsServiceActivationClosed() {
		return isServiceActivationClosed;
	}

	public void setIsServiceActivationClosed(String isServiceActivationClosed) {
		this.isServiceActivationClosed = isServiceActivationClosed;
	}*/
	
	
}
