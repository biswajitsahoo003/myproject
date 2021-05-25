package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the ScAttachment.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "sc_attachment")
@NamedQuery(name = "ScAttachment.findAll", query = "SELECT s FROM ScAttachment s")
public class ScAttachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "offering_name")
	private String offeringName;

	@Column(name = "order_id")
	private Integer orderId;

	@Column(name = "product_name")
	private String productName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	private ScServiceDetail scServiceDetail;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "site_id")
	private Integer siteId;

	@Column(name = "attachment_type")
	private String attachmentType;
	
	@Column(name = "site_type")
	private String siteType="A";
	
	
	
	
	

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	// bi-directional many-to-one association to Attachment
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Attachment attachment;

	public ScAttachment() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getOfferingName() {
		return this.offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ScServiceDetail getScServiceDetail() {
		return scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Attachment getAttachment() {
		return this.attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

}