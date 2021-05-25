package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 * 
 *
 * @author AnneF
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_site_service_termination_details")
@NamedQuery(name = "OrderSiteServiceTerminationDetails.findAll", query = "SELECT q FROM OrderSiteServiceTerminationDetails q")
public class OrderSiteServiceTerminationDetails  implements Serializable {
	

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_site_to_service_id")
	private OrderIllSiteToService orderIllSiteToService;
	
	@Column(name="order_to_le_id")
	private Integer orderToLeId;
	
	@Column(name="effective_date_of_change")
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDateOfChange;
	
	@Column(name="customer_mail_received_date")
	@Temporal(TemporalType.TIMESTAMP)
	 private Date customerMailReceivedDate;
	
	@Column(name="requested_date_for_termination")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestedDateForTermination;
	
	@Column(name="term_in_months")
	private String termInMonths;
	
	@Column(name="sub_reason")
	private String subReason;
	 
	@Column(name="reason_for_termination")
	private String reasonForTermination;
	
	@Column(name="communication_recipient")
	private String communicationReceipient;
	 
	@Column(name="local_it_contact_name")
	private String localItContactName;
	
	@Column(name="local_it_contact_number")
	private String localItContactNumber;
	 
	@Column(name="local_it_contact_email_id")
	private String localItContactEmailId;
	
	@Column(name="internal_customer")
	private String internalCustomer;
	
	@Column(name="customer_email_confirmation_erf_cus_attachment_id")
	private Integer customerEmailConfirmationErfCusAttachmentId;
	
	@Column(name="etc_applicable")
	private Byte etcApplicable;
	
	@Column(name="handover_to")
	private String handoverTo;
	
	@Column(name="actual_etc")
	private Double actualEtc;
	
	@Column(name="proposed_etc")
	private Double proposedEtc;
	
	@Column(name="waiver_type")
	private String waiverType;
	
	@Column(name="waiver_policy")
	private String waiverPolicy;
	
	@Column(name="waiver_remarks")
	private String waiverRemarks;	
	
	@Column(name="final_etc")
	private Double finalEtc;
	
	@Column(name="proposed_by_sales")
	private String proposedBySales;
	
	@Column(name="csm_non_csm_name")
	private String csmNonCsmName;
	
	@Column(name="csm_non_csm_email_id")
	private String csmNonCsmEmail;
	
	@Column(name="csm_non_csm_contact_number")
	private String csmNonCsmContactNumber;
	
	@Column(name="termination_sub_type")
	private String terminationSubtype;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	@Column(name="terminated_parent_order_code")
	private String terminatedParentOrderCode;
	
	@Column(name="termination_send_to_td_date")
	private Date terminationSendToTdDate;
	

	@Column(name="sales_task_response")
	private String salesTaskResponse;
	
	@Column(name = "o2c_call_initiated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date o2cCallInitiatedDate;
	
	@Column(name="retention_reason")
	private String retentionReason;	
	
	@Column(name="termination_remarks")
	private String terminationRemarks;
	

	@Column(name="regretted_non_regretted_termination")
	private String regrettedNonRegrettedTermination;

	@Column(name="compensatory_details")
	private String compensatoryDetails;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OrderIllSiteToService getOrderIllSiteToService() {
		return orderIllSiteToService;
	}

	public void setOrderIllSiteToService(OrderIllSiteToService orderIllSiteToService) {
		this.orderIllSiteToService = orderIllSiteToService;
	}

	public Date getEffectiveDateOfChange() {
		return effectiveDateOfChange;
	}

	public void setEffectiveDateOfChange(Date effectiveDateOfChange) {
		this.effectiveDateOfChange = effectiveDateOfChange;
	}

	public Date getCustomerMailReceivedDate() {
		return customerMailReceivedDate;
	}

	public void setCustomerMailReceivedDate(Date customerMailReceivedDate) {
		this.customerMailReceivedDate = customerMailReceivedDate;
	}

	public Date getRequestedDateForTermination() {
		return requestedDateForTermination;
	}

	public void setRequestedDateForTermination(Date requestedDateForTermination) {
		this.requestedDateForTermination = requestedDateForTermination;
	}

	public String getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(String termInMonths) {
		this.termInMonths = termInMonths;
	}

	public Date getTerminationSendToTdDate() {
		return terminationSendToTdDate;
	}

	public void setTerminationSendToTdDate(Date terminationSendToTdDate) {
		this.terminationSendToTdDate = terminationSendToTdDate;
	}

	public String getSubReason() {
		return subReason;
	}

	public void setSubReason(String subReason) {
		this.subReason = subReason;
	}

	public String getReasonForTermination() {
		return reasonForTermination;
	}

	public void setReasonForTermination(String reasonForTermination) {
		this.reasonForTermination = reasonForTermination;
	}

	public String getCommunicationReceipient() {
		return communicationReceipient;
	}

	public void setCommunicationReceipient(String communicationReceipient) {
		this.communicationReceipient = communicationReceipient;
	}

	public String getLocalItContactName() {
		return localItContactName;
	}

	public void setLocalItContactName(String localItContactName) {
		this.localItContactName = localItContactName;
	}

	public String getLocalItContactNumber() {
		return localItContactNumber;
	}

	public void setLocalItContactNumber(String localItContactNumber) {
		this.localItContactNumber = localItContactNumber;
	}

	public String getLocalItContactEmailId() {
		return localItContactEmailId;
	}

	public void setLocalItContactEmailId(String localItContactEmailId) {
		this.localItContactEmailId = localItContactEmailId;
	}

	public String getInternalCustomer() {
		return internalCustomer;
	}

	public void setInternalCustomer(String internalCustomer) {
		this.internalCustomer = internalCustomer;
	}

	public Integer getCustomerEmailConfirmationErfCusAttachmentId() {
		return customerEmailConfirmationErfCusAttachmentId;
	}

	public void setCustomerEmailConfirmationErfCusAttachmentId(Integer customerEmailConfirmationErfCusAttachmentId) {
		this.customerEmailConfirmationErfCusAttachmentId = customerEmailConfirmationErfCusAttachmentId;
	}

	public Byte getEtcApplicable() {
		return etcApplicable;
	}

	public void setEtcApplicable(Byte etcApplicable) {
		this.etcApplicable = etcApplicable;
	}

	public String getHandoverTo() {
		return handoverTo;
	}

	public void setHandoverTo(String handoverTo) {
		this.handoverTo = handoverTo;
	}

	public String getCsmNonCsmName() {
		return csmNonCsmName;
	}

	public void setCsmNonCsmName(String csmNonCsmName) {
		this.csmNonCsmName = csmNonCsmName;
	}

	public String getCsmNonCsmEmail() {
		return csmNonCsmEmail;
	}

	public void setCsmNonCsmEmail(String csmNonCsmEmail) {
		this.csmNonCsmEmail = csmNonCsmEmail;
	}

	public String getCsmNonCsmContactNumber() {
		return csmNonCsmContactNumber;
	}

	public void setCsmNonCsmContactNumber(String csmNonCsmContactNumber) {
		this.csmNonCsmContactNumber = csmNonCsmContactNumber;
	}

	public String getTerminationSubtype() {
		return terminationSubtype;
	}

	public void setTerminationSubtype(String terminationSubtype) {
		this.terminationSubtype = terminationSubtype;
	}

	public Integer getOrderToLeId() {
		return orderToLeId;
	}

	public void setOrderToLeId(Integer orderToLeId) {
		this.orderToLeId = orderToLeId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getTerminatedParentOrderCode() {
		return terminatedParentOrderCode;
	}

	public void setTerminatedParentOrderCode(String terminatedParentOrderCode) {
		this.terminatedParentOrderCode = terminatedParentOrderCode;
	}

	public String getSalesTaskResponse() {
		return salesTaskResponse;
	}

	public void setSalesTaskResponse(String salesTaskResponse) {
		this.salesTaskResponse = salesTaskResponse;
	}

	public Date getO2cCallInitiatedDate() {
		return o2cCallInitiatedDate;
	}

	public void setO2cCallInitiatedDate(Date o2cCallInitiatedDate) {
		this.o2cCallInitiatedDate = o2cCallInitiatedDate;
	}

	public String getRetentionReason() {
		return retentionReason;
	}

	public void setRetentionReason(String retentionReason) {
		this.retentionReason = retentionReason;
	}

	public String getTerminationRemarks() {
		return terminationRemarks;
	}

	public void setTerminationRemarks(String terminationRemarks) {
		this.terminationRemarks = terminationRemarks;
	}

	public Double getActualEtc() {
		return actualEtc;
	}

	public void setActualEtc(Double actualEtc) {
		this.actualEtc = actualEtc;
	}

	public Double getProposedEtc() {
		return proposedEtc;
	}

	public void setProposedEtc(Double proposedEtc) {
		this.proposedEtc = proposedEtc;
	}

	public String getWaiverType() {
		return waiverType;
	}

	public void setWaiverType(String waiverType) {
		this.waiverType = waiverType;
	}

	public String getWaiverPolicy() {
		return waiverPolicy;
	}

	public void setWaiverPolicy(String waiverPolicy) {
		this.waiverPolicy = waiverPolicy;
	}

	public String getWaiverRemarks() {
		return waiverRemarks;
	}

	public void setWaiverRemarks(String waiverRemarks) {
		this.waiverRemarks = waiverRemarks;
	}

	public Double getFinalEtc() {
		return finalEtc;
	}

	public void setFinalEtc(Double finalEtc) {
		this.finalEtc = finalEtc;
	}

	public String getProposedBySales() {
		return proposedBySales;
	}

	public void setProposedBySales(String proposedBySales) {
		this.proposedBySales = proposedBySales;
	}

	public String getRegrettedNonRegrettedTermination() {
		return regrettedNonRegrettedTermination;
	}

	public void setRegrettedNonRegrettedTermination(String regrettedNonRegrettedTermination) {
		this.regrettedNonRegrettedTermination = regrettedNonRegrettedTermination;
	}

	public String getCompensatoryDetails() {
		return compensatoryDetails;
	}

	public void setCompensatoryDetails(String compensatoryDetails) {
		this.compensatoryDetails = compensatoryDetails;
	}
	
	
	

}
