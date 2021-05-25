package com.tcl.dias.preparefulfillment.beans;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 
 * This file contains the ServiceDashBoardBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ServiceDashBoardBean {

	private Integer serviceId;
	private String serviceCode;
	private Date orderDate;
	private String orderCode;
	private String productName;
	private String offeringName;
	private String location;	
	private String locationB;
	private Integer locationId;	
	private Integer locationIB;
	private Timestamp estimatedDeliveryDate;
	private Timestamp committedDeliveryDate;
	
	private Timestamp rrfsDate;
	private Timestamp actualDeliveryDate;
	private Timestamp targetedDeliveryDate;
	private List<StagePlanBean> stages;
	private String primarySecondary;
	private String assignedPM;
	


	private String status;
	private Timestamp crfsDate;	
	private Byte isJeopardyTask;
	private String orderSubCategory;
	
	private String localLoopBandwidth;
	private String localLoopBandwidthUnit;
	private String billStartDate;
	private String terminationFlowTriggered;
	private String customerRequestorDate;
	private String terminationEffectiveDate;

	private Timestamp terminationTrigerredDate;

	private String cancellationFlowTriggered;
	private Timestamp cancellationInitiationDate;

	private String localItContactEmailId;
	private String localItContactMobile;
	private String localItContactName;
	
	private boolean isCustomerServiceDelayed;
	private String isOrderAmendent;
	
	private String requestForAmendment;
	
	private String orderType;
	private String orderCategory;
	private Timestamp serviceCommissionedDate;
	
	private String supplierBillStopDate;
	private String supplierBillStopDateB;
	
	
	private String customerMailReceiveDate;
	
	private String parentServiceCode;
	
	private String internationalServiceCode;
	
	private boolean isTerminationStatusChangeOptionRequired;
	
	
	
	public String getParentServiceCode() {
		return parentServiceCode;
	}

	public void setParentServiceCode(String parentServiceCode) {
		this.parentServiceCode = parentServiceCode;
	}

	/**
	 * @return the customerMailReceiveDate
	 */
	public String getCustomerMailReceiveDate() {
		return customerMailReceiveDate;
	}

	/**
	 * @param customerMailReceiveDate the customerMailReceiveDate to set
	 */
	public void setCustomerMailReceiveDate(String customerMailReceiveDate) {
		this.customerMailReceiveDate = customerMailReceiveDate;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the orderCategory
	 */
	public String getOrderCategory() {
		return orderCategory;
	}

	/**
	 * @param orderCategory the orderCategory to set
	 */
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	/**
	 * @return the requestForAmendment
	 */
	public String getRequestForAmendment() {
		return requestForAmendment;
	}

	/**
	 * @param requestForAmendment the requestForAmendment to set
	 */
	public void setRequestForAmendment(String requestForAmendment) {
		this.requestForAmendment = requestForAmendment;
	}

	public boolean isCustomerServiceDelayed() {
		return isCustomerServiceDelayed;
	}

	public void setCustomerServiceDelayed(boolean isCustomerServiceDelayed) {
		this.isCustomerServiceDelayed = isCustomerServiceDelayed;
	}

	public Timestamp getRrfsDate() {
		return rrfsDate;
	}

	public void setRrfsDate(Timestamp rrfsDate) {
		this.rrfsDate = rrfsDate;
	}

	public Timestamp getTerminationTrigerredDate() {
		return terminationTrigerredDate;
	}

	public void setTerminationTrigerredDate(Timestamp terminationTrigerredDate) {
		this.terminationTrigerredDate = terminationTrigerredDate;
	}

	public String getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}

	public String getLocalLoopBandwidth() {
		return localLoopBandwidth;
	}

	public void setLocalLoopBandwidth(String localLoopBandwidth) {
		this.localLoopBandwidth = localLoopBandwidth;
	}

	public String getLocalLoopBandwidthUnit() {
		return localLoopBandwidthUnit;
	}

	public void setLocalLoopBandwidthUnit(String localLoopBandwidthUnit) {
		this.localLoopBandwidthUnit = localLoopBandwidthUnit;
	}

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public Byte getIsJeopardyTask() {
		return isJeopardyTask;
	}

	public void setIsJeopardyTask(Byte isJeopardyTask) {
		this.isJeopardyTask = isJeopardyTask;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocationB() {
		return locationB;
	}

	public void setLocationB(String locationB) {
		this.locationB = locationB;
	}

	public Integer getLocationIB() {
		return locationIB;
	}

	public void setLocationIB(Integer locationIB) {
		this.locationIB = locationIB;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public List<StagePlanBean> getStages() {
		return stages;
	}

	public void setStages(List<StagePlanBean> stages) {
		this.stages = stages;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Timestamp getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(Timestamp estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	public Timestamp getCommittedDeliveryDate() {
		return committedDeliveryDate;
	}

	public void setCommittedDeliveryDate(Timestamp committedDeliveryDate) {
		this.committedDeliveryDate = committedDeliveryDate;
	}

	public Timestamp getActualDeliveryDate() {
		return actualDeliveryDate;
	}

	public void setActualDeliveryDate(Timestamp actualDeliveryDate) {
		this.actualDeliveryDate = actualDeliveryDate;
	}

	public Timestamp getTargetedDeliveryDate() {
		return targetedDeliveryDate;
	}

	public void setTargetedDeliveryDate(Timestamp targetedDeliveryDate) {
		this.targetedDeliveryDate = targetedDeliveryDate;
	}

	public String getPrimarySecondary() {
		return primarySecondary;
	}

	public void setPrimarySecondary(String primarySecondary) {
		this.primarySecondary = primarySecondary;
	}
	public Timestamp getCrfsDate() {
		return crfsDate;
	}

	public void setCrfsDate(Timestamp crfsDate) {
		this.crfsDate = crfsDate;
	}
	public String getTerminationFlowTriggered() {
		return terminationFlowTriggered;
	}

	public void setTerminationFlowTriggered(String terminationFlowTriggered) {
		this.terminationFlowTriggered = terminationFlowTriggered;
	}

	public String getCustomerRequestorDate() {
		return customerRequestorDate;
	}

	public void setCustomerRequestorDate(String customerRequestorDate) {
		this.customerRequestorDate = customerRequestorDate;
	}

	public String getTerminationEffectiveDate() {
		return terminationEffectiveDate;
	}

	public void setTerminationEffectiveDate(String terminationEffectiveDate) {
		this.terminationEffectiveDate = terminationEffectiveDate;
	}
	public String getCancellationFlowTriggered() {
		return cancellationFlowTriggered;
	}

	public void setCancellationFlowTriggered(String cancellationFlowTriggered) {
		this.cancellationFlowTriggered = cancellationFlowTriggered;
	}

	public Timestamp getCancellationInitiationDate() {
		return cancellationInitiationDate;
	}

	public void setCancellationInitiationDate(Timestamp cancellationInitiationDate) {
		this.cancellationInitiationDate = cancellationInitiationDate;
	}
	public String getLocalItContactEmailId() {
		return localItContactEmailId;
	}

	public void setLocalItContactEmailId(String localItContactEmailId) {
		this.localItContactEmailId = localItContactEmailId;
	}

	public String getLocalItContactMobile() {
		return localItContactMobile;
	}

	public void setLocalItContactMobile(String localItContactMobile) {
		this.localItContactMobile = localItContactMobile;
	}

	public String getLocalItContactName() {
		return localItContactName;
	}

	public void setLocalItContactName(String localItContactName) {
		this.localItContactName = localItContactName;
	}

	public String getIsOrderAmendent() {
		return isOrderAmendent;
	}

	public void setIsOrderAmendent(String isOrderAmendent) {
		this.isOrderAmendent = isOrderAmendent;
	}
	
	public String getAssignedPM() {
		return assignedPM;
	}

	public void setAssignedPM(String assignedPM) {
		this.assignedPM = assignedPM;
	}

	public Timestamp getServiceCommissionedDate() {
		return serviceCommissionedDate;
	}

	public void setServiceCommissionedDate(Timestamp serviceCommissionedDate) {
		this.serviceCommissionedDate = serviceCommissionedDate;
	}

	public String getSupplierBillStopDate() {
		return supplierBillStopDate;
	}

	public void setSupplierBillStopDate(String supplierBillStopDate) {
		this.supplierBillStopDate = supplierBillStopDate;
	}

	public String getSupplierBillStopDateB() {
		return supplierBillStopDateB;
	}

	public void setSupplierBillStopDateB(String supplierBillStopDateB) {
		this.supplierBillStopDateB = supplierBillStopDateB;
	}

	public boolean getIsTerminationStatusChangeOptionRequired() {
		return isTerminationStatusChangeOptionRequired;
	}

	public void setIsTerminationStatusChangeOptionRequired(boolean isTerminationStatusChangeOptionRequired) {
		this.isTerminationStatusChangeOptionRequired = isTerminationStatusChangeOptionRequired;
	}

	public String getInternationalServiceCode() {
		return internationalServiceCode;
	}

	public void setInternationalServiceCode(String internationalServiceCode) {
		this.internationalServiceCode = internationalServiceCode;
	}
	
}
