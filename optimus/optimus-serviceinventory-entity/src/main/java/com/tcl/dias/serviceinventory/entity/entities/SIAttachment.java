package com.tcl.dias.serviceinventory.entity.entities;

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
import javax.persistence.Table;

/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * The persistent class for the si_attachment database table.
 * 
 */
@Entity
@Table(name="si_attachment")
public class SIAttachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="is_active")
	private String isActive;

	@Column(name="offering_name")
	private String offeringName;

	@Column(name="order_id")
	private Integer orderId;

	@Column(name="product_name")
	private String productName;

	@Column(name="site_id")
	private Integer siteId;

	//bi-directional many-to-one association to Attachment
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Attachment attachment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id")
	private SIServiceDetail siServiceDetail;

    public SIAttachment() {
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

	public SIServiceDetail getSiServiceDetail() {
		return siServiceDetail;
	}

	public void setSiServiceDetail(SIServiceDetail siServiceDetail) {
		this.siServiceDetail = siServiceDetail;
	}
}