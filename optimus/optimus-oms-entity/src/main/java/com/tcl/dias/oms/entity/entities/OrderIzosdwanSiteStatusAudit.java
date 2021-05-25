package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * This file contains the OrderIzosdwanSiteStatusAudit.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_izosdwan_site_status_audit")
@NamedQuery(name = "OrderIzosdwanSiteStatusAudit.findAll", query = "SELECT o FROM OrderIzosdwanSiteStatusAudit o")
public class OrderIzosdwanSiteStatusAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "is_active")
	private Byte isActive;

	// bi-directional many-to-one association to OrderSiteStageAudit
	@OneToMany(mappedBy = "orderSiteStatusAudit")
	private Set<OrderSiteStageAudit> orderSiteStageAudits;

	// bi-directional many-to-one association to MstOrderSiteStatus
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_site_status_id")
	private MstOrderSiteStatus mstOrderSiteStatus;

	// bi-directional many-to-one association to QuoteIllSite
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_izosdwan_sites_id")
	private OrderIzosdwanSite orderIzosdwanSite;

	@Column(name = "parent_id")
	private Integer parentId;

	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	public OrderIzosdwanSiteStatusAudit() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public Set<OrderSiteStageAudit> getOrderSiteStageAudits() {
		return orderSiteStageAudits;
	}

	public void setOrderSiteStageAudits(Set<OrderSiteStageAudit> orderSiteStageAudits) {
		this.orderSiteStageAudits = orderSiteStageAudits;
	}

	public MstOrderSiteStatus getMstOrderSiteStatus() {
		return mstOrderSiteStatus;
	}

	public void setMstOrderSiteStatus(MstOrderSiteStatus mstOrderSiteStatus) {
		this.mstOrderSiteStatus = mstOrderSiteStatus;
	}

	

	public OrderIzosdwanSite getOrderIzosdwanSite() {
		return orderIzosdwanSite;
	}

	public void setOrderIzosdwanSite(OrderIzosdwanSite orderIzosdwanSite) {
		this.orderIzosdwanSite = orderIzosdwanSite;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}