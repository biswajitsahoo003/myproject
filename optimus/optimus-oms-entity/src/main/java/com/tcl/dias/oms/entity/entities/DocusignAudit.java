package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * This file contains the DocusignAudit.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "docusign_audit")
@NamedQuery(name = "DocusignAudit.findAll", query = "SELECT o FROM DocusignAudit o")
public class DocusignAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "order_ref_uuid")
	private String orderRefUuid;

	@Column(name = "customer_envelope_id")
	private String customerEnvelopeId;

	@Column(name = "supplier_envelope_id")
	private String supplierEnvelopeId;

	@Column(name = "approver_one_envelope_id")
	private String approverOneEnvelopeId;

	@Column(name = "approver_two_envelope_id")
	private String approverTwoEnvelopeId;

	@Column(name = "stage")
	private String stage;

	@Column(name = "status")
	private String status;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;
	
	@Column(name = "customer_email")
	private String customerEmail;

	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "supplier_email")
	private String supplierEmail;

	@Column(name = "customer_signed_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date customerSignedDate;
	
	@Column(name = "supplier_signed_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date supplierSignedDate;

	@Column(name = "approver_one_email")
	private String approverOneEmail;

	@Column(name = "approver_one_name")
	private String approverOneName;

	@Column(name = "approver_one_signed_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approverOneSignedDate;

	@Column(name = "approver_two_email")
	private String approverTwoEmail;

	@Column(name = "approver_two_name")
	private String approverTwoName;

	@Column(name = "approver_two_signed_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approverTwoSignedDate;

	@Column(name = "customer_one_email")
	private String customerOneEmail;

	@Column(name = "customer_one_name")
	private String customerOneName;

	@Column(name = "customer_one_signed_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date customerOneSignedDate;

	@Column(name = "customer_one_envelope_id")
	private String customerOneEnvelopeId;

	@Column(name = "customer_two_email")
	private String customerTwoEmail;

	@Column(name = "customer_two_name")
	private String customerTwoName;

	@Column(name = "customer_two_signed_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date customerTwoSignedDate;

	@Column(name = "customer_two_envelope_id")
	private String customerTwoEnvelopeId;
	
	@Column(name = "commercial_email")
	private String commercialEmail;

	@Column(name = "commercial_name")
	private String commercialName;

	@Column(name = "commercial_signed_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date commercialSignedDate;

	@Column(name = "commercial_envelope_id")
	private String commercialEnvelopeId;

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail;
	}

	public DocusignAudit() {
		// Do NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderRefUuid() {
		return orderRefUuid;
	}

	public void setOrderRefUuid(String orderRefUuid) {
		this.orderRefUuid = orderRefUuid;
	}

	public String getCustomerEnvelopeId() {
		return customerEnvelopeId;
	}

	public void setCustomerEnvelopeId(String customerEnvelopeId) {
		this.customerEnvelopeId = customerEnvelopeId;
	}

	public String getSupplierEnvelopeId() {
		return supplierEnvelopeId;
	}

	public void setSupplierEnvelopeId(String supplierEnvelopeId) {
		this.supplierEnvelopeId = supplierEnvelopeId;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getCustomerSignedDate() {
		return customerSignedDate;
	}

	public void setCustomerSignedDate(Date customerSignedDate) {
		this.customerSignedDate = customerSignedDate;
	}

	public Date getSupplierSignedDate() {
		return supplierSignedDate;
	}

	public void setSupplierSignedDate(Date supplierSignedDate) {
		this.supplierSignedDate = supplierSignedDate;
	}

	public String getApproverOneEmail() {
		return approverOneEmail;
	}

	public void setApproverOneEmail(String approverOneEmail) {
		this.approverOneEmail = approverOneEmail;
	}

	public Date getApproverOneSignedDate() {
		return approverOneSignedDate;
	}

	public void setApproverOneSignedDate(Date approverOneSignedDate) {
		this.approverOneSignedDate = approverOneSignedDate;
	}

	public String getApproverTwoEmail() {
		return approverTwoEmail;
	}

	public void setApproverTwoEmail(String approverTwoEmail) {
		this.approverTwoEmail = approverTwoEmail;
	}

	public Date getApproverTwoSignedDate() {
		return approverTwoSignedDate;
	}

	public void setApproverTwoSignedDate(Date approverTwoSignedDate) {
		this.approverTwoSignedDate = approverTwoSignedDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getApproverOneName() {
		return approverOneName;
	}

	public void setApproverOneName(String approverOneName) {
		this.approverOneName = approverOneName;
	}

	public String getApproverTwoName() {
		return approverTwoName;
	}

	public void setApproverTwoName(String approverTwoName) {
		this.approverTwoName = approverTwoName;
	}

	public String getApproverOneEnvelopeId() {
		return approverOneEnvelopeId;
	}

	public void setApproverOneEnvelopeId(String approverOneEnvelopeId) {
		this.approverOneEnvelopeId = approverOneEnvelopeId;
	}

	public String getApproverTwoEnvelopeId() {
		return approverTwoEnvelopeId;
	}

	public void setApproverTwoEnvelopeId(String approverTwoEnvelopeId) {
		this.approverTwoEnvelopeId = approverTwoEnvelopeId;
	}

	public String getCustomerOneEmail() {
		return customerOneEmail;
	}

	public void setCustomerOneEmail(String customerOneEmail) {
		this.customerOneEmail = customerOneEmail;
	}

	public String getCustomerOneName() {
		return customerOneName;
	}

	public void setCustomerOneName(String customerOneName) {
		this.customerOneName = customerOneName;
	}

	public Date getCustomerOneSignedDate() {
		return customerOneSignedDate;
	}

	public void setCustomerOneSignedDate(Date customerOneSignedDate) {
		this.customerOneSignedDate = customerOneSignedDate;
	}

	public String getCustomerTwoEmail() {
		return customerTwoEmail;
	}

	public void setCustomerTwoEmail(String customerTwoEmail) {
		this.customerTwoEmail = customerTwoEmail;
	}

	public String getCustomerTwoName() {
		return customerTwoName;
	}

	public void setCustomerTwoName(String customerTwoName) {
		this.customerTwoName = customerTwoName;
	}

	public Date getCustomerTwoSignedDate() {
		return customerTwoSignedDate;
	}

	public void setCustomerTwoSignedDate(Date customerTwoSignedDate) {
		this.customerTwoSignedDate = customerTwoSignedDate;
	}

	public String getCustomerOneEnvelopeId() {
		return customerOneEnvelopeId;
	}

	public void setCustomerOneEnvelopeId(String customerOneEnvelopeId) {
		this.customerOneEnvelopeId = customerOneEnvelopeId;
	}

	public String getCustomerTwoEnvelopeId() {
		return customerTwoEnvelopeId;
	}

	public void setCustomerTwoEnvelopeId(String customerTwoEnvelopeId) {
		this.customerTwoEnvelopeId = customerTwoEnvelopeId;
	}

	public String getCommercialEmail() {
		return commercialEmail;
	}

	public void setCommercialEmail(String commercialEmail) {
		this.commercialEmail = commercialEmail;
	}

	public String getCommercialName() {
		return commercialName;
	}

	public void setCommercialName(String commercialName) {
		this.commercialName = commercialName;
	}

	public Date getCommercialSignedDate() {
		return commercialSignedDate;
	}

	public void setCommercialSignedDate(Date commercialSignedDate) {
		this.commercialSignedDate = commercialSignedDate;
	}

	public String getCommercialEnvelopeId() {
		return commercialEnvelopeId;
	}

	public void setCommercialEnvelopeId(String commercialEnvelopeId) {
		this.commercialEnvelopeId = commercialEnvelopeId;
	}

	@Override
	public String toString() {
		return "DocusignAudit [id=" + id + ", orderRefUuid=" + orderRefUuid + ", customerEnvelopeId="
				+ customerEnvelopeId + ", supplierEnvelopeId=" + supplierEnvelopeId + ", approverOneEnvelopeId="
				+ approverOneEnvelopeId + ", approverTwoEnvelopeId=" + approverTwoEnvelopeId + ", stage=" + stage
				+ ", status=" + status + ", createdTime=" + createdTime + ", updatedTime=" + updatedTime
				+ ", customerEmail=" + customerEmail + ", customerName=" + customerName + ", supplierEmail="
				+ supplierEmail + ", customerSignedDate=" + customerSignedDate + ", supplierSignedDate="
				+ supplierSignedDate + ", approverOneEmail=" + approverOneEmail + ", approverOneName=" + approverOneName
				+ ", approverOneSignedDate=" + approverOneSignedDate + ", approverTwoEmail=" + approverTwoEmail
				+ ", approverTwoName=" + approverTwoName + ", approverTwoSignedDate=" + approverTwoSignedDate
				+ ", customerOneEmail=" + customerOneEmail + ", customerOneName=" + customerOneName
				+ ", customerOneSignedDate=" + customerOneSignedDate + ", customerOneEnvelopeId="
				+ customerOneEnvelopeId + ", customerTwoEmail=" + customerTwoEmail + ", customerTwoName="
				+ customerTwoName + ", customerTwoSignedDate=" + customerTwoSignedDate + ", customerTwoEnvelopeId="
				+ customerTwoEnvelopeId + ", commercialEmail=" + commercialEmail + ", commercialName=" + commercialName
				+ ", commercialSignedDate=" + commercialSignedDate + ", commercialEnvelopeId=" + commercialEnvelopeId
				+ "]";
	}
	
	
}
