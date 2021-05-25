package com.tcl.dias.oms.entity.entities;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This file contains the OrderLinkStatusAudit.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "order_link_status_audit")
public class OrderLinkStatusAudit {

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

	// bi-directional many-to-one association to orderLinkStageAudits
	@OneToMany(mappedBy = "orderLinkStatusAudit")
	private Set<OrderLinkStageAudit> orderLinkStageAudits;

	// bi-directional many-to-one association to mstOrderLinkStatus
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_link_status_id")
	private MstOrderLinkStatus mstOrderLinkStatus;

	// bi-directional many-to-one association to QuoteIllSite
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_link_id")
	private OrderNplLink orderNplLink;

	public OrderLinkStatusAudit() {
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

	public Set<OrderLinkStageAudit> getOrderLinkStageAudits() {
		return orderLinkStageAudits;
	}

	public void setOrderLinkStageAudits(Set<OrderLinkStageAudit> orderLinkStageAudits) {
		this.orderLinkStageAudits = orderLinkStageAudits;
	}

	public MstOrderLinkStatus getMstOrderLinkStatus() {
		return mstOrderLinkStatus;
	}

	public void setMstOrderLinkStatus(MstOrderLinkStatus mstOrderLinkStatus) {
		this.mstOrderLinkStatus = mstOrderLinkStatus;
	}

	public OrderNplLink getOrderNplLink() {
		return orderNplLink;
	}

	public void setOrderNplLink(OrderNplLink orderNplLink) {
		this.orderNplLink = orderNplLink;
	}


	



}
