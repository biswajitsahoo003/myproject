package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * This file contains the DocusignAuditBean bean.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DocusignAuditBean implements Serializable{
	
	private Integer docusignAuditId;
	
	private String orderRefId;
	
	private String customerEnvelopeId;
	
	private String supplierEnvelopeId;
	
	private String stage;
	
	private String status;
	
	private Date customerSignedDate;
	
	private Date supplierSignedDate;

	private String approverOneEnvelopeId;

	private String approverTwoEnvelopeId;

	private String customerOneEnvelopeId;

	private String CustomerTwoEnvelopeId;

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

	public Integer getDocusignAuditId() {
		return docusignAuditId;
	}

	public void setDocusignAuditId(Integer docusignAuditId) {
		this.docusignAuditId = docusignAuditId;
	}

	public String getOrderRefId() {
		return orderRefId;
	}

	public void setOrderRefId(String orderRefId) {
		this.orderRefId = orderRefId;
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

	public String getCustomerOneEnvelopeId() {
		return customerOneEnvelopeId;
	}

	public void setCustomerOneEnvelopeId(String customerOneEnvelopeId) {
		this.customerOneEnvelopeId = customerOneEnvelopeId;
	}

	public String getCustomerTwoEnvelopeId() {
		return CustomerTwoEnvelopeId;
	}

	public void setCustomerTwoEnvelopeId(String customerTwoEnvelopeId) {
		CustomerTwoEnvelopeId = customerTwoEnvelopeId;
	}
}
