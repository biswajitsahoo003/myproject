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
 * This file contains the QuoteAccessPermission.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_access_permission")
@NamedQuery(name = "QuoteAccessPermission.findAll", query = "SELECT s FROM QuoteAccessPermission s")
public class QuoteAccessPermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "type")
	private String type;

	@Column(name = "ref_id")
	private String refId;

	@Column(name = "product_family_id")
	private Integer productFamilyId;

	@Column(name = "is_customer_view")
	private Byte isCustomerView;

	@Column(name = "is_sales_view")
	private Byte isSalesView;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time")
	private Date updatedTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Integer getProductFamilyId() {
		return productFamilyId;
	}

	public void setProductFamilyId(Integer productFamilyId) {
		this.productFamilyId = productFamilyId;
	}

	public Byte getIsCustomerView() {
		return isCustomerView;
	}

	public void setIsCustomerView(Byte isCustomerView) {
		this.isCustomerView = isCustomerView;
	}

	public Byte getIsSalesView() {
		return isSalesView;
	}

	public void setIsSalesView(Byte isSalesView) {
		this.isSalesView = isSalesView;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}