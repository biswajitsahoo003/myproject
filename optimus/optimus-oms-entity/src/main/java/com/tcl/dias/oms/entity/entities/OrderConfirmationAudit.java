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
 * The persistent class for the order_confirmation_audit database table.
 * 
 * @author SEKHAR ER
 *
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_confirmation_audit")
@NamedQuery(name = "OrderConfirmationAudit.findAll", query = "SELECT o FROM OrderConfirmationAudit o")
public class OrderConfirmationAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	private String name;

	@Column(name = "order_ref_uuid")
	private String orderRefUuid;

	@Column(name = "public_ip")
	private String publicIp;
	
	@Column(name = "supplier_ip")
	private String supplierIp;

	@Column(name = "mode")
	private String mode;

	@Column(name = "uploaded_by")
	private String uploadedBy;

	@Column(name = "customer_docusign_response")
	private String customerDocuSignResponse;

	@Column(name = "supplier_docusign_response")
	private String supplierDocuSignResponse;
	
	@Column(name = "created_time_unix")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTimeUnix;

	public Date getCreatedTimeUnix() {
		return createdTimeUnix;
	}

	public void setCreatedTimeUnix(Date createdTimeUnix) {
		this.createdTimeUnix = createdTimeUnix;
	}

	public OrderConfirmationAudit() {
		// Do NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderRefUuid() {
		return this.orderRefUuid;
	}

	public void setOrderRefUuid(String orderRefUuid) {
		this.orderRefUuid = orderRefUuid;
	}

	public String getPublicIp() {
		return this.publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getSupplierIp() {
		return supplierIp;
	}

	public void setSupplierIp(String supplierIp) {
		this.supplierIp = supplierIp;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getCustomerDocuSignResponse() {
		return customerDocuSignResponse;
	}

	public void setCustomerDocuSignResponse(String customerDocuSignResponse) {
		this.customerDocuSignResponse = customerDocuSignResponse;
	}

	public String getSupplierDocuSignResponse() {
		return supplierDocuSignResponse;
	}

	public void setSupplierDocuSignResponse(String supplierDocuSignResponse) {
		this.supplierDocuSignResponse = supplierDocuSignResponse;
	}

}
