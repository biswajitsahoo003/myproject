package com.tcl.dias.servicefulfillment.entity.entities;

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
 * Entity Class for Servicehanover table
 * 
 *
 * @author Yogesh 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "service_handover_audit")
@NamedQuery(name = "ServicehandoverAudit.findAll", query = "SELECT s FROM ServicehandoverAudit s")
public class ServicehandoverAudit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "opportunity_id")
	private String opportunityId;

	@Column(name = "crn_id")
	private String crnId;

	@Column(name = "order_id")
	private String orderId;

	@Column(name = "geneva_group_id")
	private String genevaGrpId;

	@Column(name = "request_type")
	private String requestType;

	@Column(name = "customer_type")
	private String customerType;

	@Column(name = "account_number")
	private String accountNumber;

	@Column(name = "request")
	private String request;

	@Column(name = "response")
	private String response;

	@Column(name = "status")
	private String status;

	@Column(name = "error_msg")
	private String erroMsg;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	@Column(name = "invoice_no")
	private String invoiceNo;
	
	@Column(name = "service_code")
	private String serviceCode;
	
	@Column(name = "provider_segment")
	private String providerSegment;
	
	@Column(name = "legal_entity_name")
	private String legalEntity;
	
	@Column(name = "process_id")
	private String processInstanceId;
	
	@Column(name = "count")
	private Integer count; 
	
	@Column(name = "cloud_code")
	private String cloudCode;
	
	@Column(name = "parent_cloud_code")
	private String parentCloudCode;
	
	@Column(name = "macd_order_id")
	private String macdOrderId;
	
	@Column(name = "service_type")
	private String serviceType;
	
	@Column(name = "source_prod_seq")
	private String sourceProdSeq;
	
	@Column(name = "service_id")
	private String serviceId;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "site_type")
	private String siteType;
	
	@Column(name = "split_ratio")
	private String splitRatio;
	
	@Column(name = "action_type")
	private String actionType;
	
	
	
	public ServicehandoverAudit() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getCrnId() {
		return crnId;
	}

	public void setCrnId(String crnId) {
		this.crnId = crnId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGenevaGrpId() {
		return genevaGrpId;
	}

	public void setGenevaGrpId(String genevaGrpId) {
		this.genevaGrpId = genevaGrpId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErroMsg() {
		return erroMsg;
	}

	public void setErroMsg(String erroMsg) {
		this.erroMsg = erroMsg;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getProviderSegment() {
		return providerSegment;
	}

	public void setProviderSegment(String providerSegment) {
		this.providerSegment = providerSegment;
	}

	public String getLegalEntity() {
		return legalEntity;
	}

	public void setLegalEntity(String legalEntity) {
		this.legalEntity = legalEntity;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getParentCloudCode() {
		return parentCloudCode;
	}

	public void setParentCloudCode(String parentCloudCode) {
		this.parentCloudCode = parentCloudCode;
	}
	
	public String getMacdOrderId() {
		return macdOrderId;
	}

	public void setMacdOrderId(String macdOrderId) {
		this.macdOrderId = macdOrderId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public String getSourceProdSeq() {
		return sourceProdSeq;
	}

	public void setSourceProdSeq(String sourceProdSeq) {
		this.sourceProdSeq = sourceProdSeq;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getSplitRatio() {
		return splitRatio;
	}

	public void setSplitRatio(String splitRatio) {
		this.splitRatio = splitRatio;
	}
	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		return "ServicehandoverAudit [id=" + id + ", opportunityId=" + opportunityId + ", crnId=" + crnId + ", orderId="
				+ orderId + ", genevaGrpId=" + genevaGrpId + ", requestType=" + requestType + ", customerType="
				+ customerType + ", accountNumber=" + accountNumber + ", request=" + request + ", response=" + response
				+ ", status=" + status + ", erroMsg=" + erroMsg + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + ", invoiceNo=" + invoiceNo
				+ ", serviceCode=" + serviceCode + ", providerSegment=" + providerSegment + ", legalEntity="
				+ legalEntity + ", processInstanceId=" + processInstanceId + ", count=" + count + ", cloudCode="
				+ cloudCode + ", parentCloudCode=" + parentCloudCode + ", macdOrderId=" + macdOrderId + ", serviceType="
				+ serviceType + ", sourceProdSeq=" + sourceProdSeq + ", serviceId=" + serviceId + ", productName="
				+ productName + ", siteType=" + siteType + ", splitRatio=" + splitRatio + "]";
	}
}
