package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
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

/**
 * 
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_site_status_audit")
@NamedQuery(name = "OrderSiteStatusAudit.findAll", query = "SELECT o FROM OrderSiteStatusAudit o")
public class OrderSiteStatusAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "end_time")
	private Timestamp endTime;

	@Column(name = "is_active")
	private Byte isActive;

	@Column(name = "parent_id")
	private Integer parentId;

	@Column(name = "start_time")
	private Timestamp startTime;

	// bi-directional many-to-one association to OrderSiteStageAudit
	@OneToMany(mappedBy = "orderSiteStatusAudit")
	private Set<OrderSiteStageAudit> orderSiteStageAudits;

	// bi-directional many-to-one association to MstOrderSiteStatus
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_site_status_id")
	private MstOrderSiteStatus mstOrderSiteStatus;

	// bi-directional many-to-one association to QuoteIllSite
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_ill_sites_id")
	private OrderIllSite orderIllSite;

	public OrderSiteStatusAudit() {
		// DO NOTHING
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

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Timestamp getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Set<OrderSiteStageAudit> getOrderSiteStageAudits() {
		return this.orderSiteStageAudits;
	}

	public void setOrderSiteStageAudits(Set<OrderSiteStageAudit> orderSiteStageAudits) {
		this.orderSiteStageAudits = orderSiteStageAudits;
	}

	public OrderSiteStageAudit addOrderSiteStageAudit(OrderSiteStageAudit orderSiteStageAudit) {
		getOrderSiteStageAudits().add(orderSiteStageAudit);
		orderSiteStageAudit.setOrderSiteStatusAudit(this);

		return orderSiteStageAudit;
	}

	public OrderSiteStageAudit removeOrderSiteStageAudit(OrderSiteStageAudit orderSiteStageAudit) {
		getOrderSiteStageAudits().remove(orderSiteStageAudit);
		orderSiteStageAudit.setOrderSiteStatusAudit(null);

		return orderSiteStageAudit;
	}

	public MstOrderSiteStatus getMstOrderSiteStatus() {
		return this.mstOrderSiteStatus;
	}

	public void setMstOrderSiteStatus(MstOrderSiteStatus mstOrderSiteStatus) {
		this.mstOrderSiteStatus = mstOrderSiteStatus;
	}

	public OrderIllSite getOrderIllSite() {
		return orderIllSite;
	}

	public void setOrderIllSite(OrderIllSite orderIllSite) {
		this.orderIllSite = orderIllSite;
	}

	

}