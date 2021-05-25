package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * 
 * This file contains the OrderIzosdwanSiteStageAudit.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order__izosdwan_site_stage_audit")
@NamedQuery(name = "OrderIzosdwanSiteStageAudit.findAll", query = "SELECT o FROM OrderIzosdwanSiteStageAudit o")
public class OrderIzosdwanSiteStageAudit implements Serializable {
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

	// bi-directional many-to-one association to MstOrderSiteStage
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_site_stage_id")
	private MstOrderSiteStage mstOrderSiteStage;

	// bi-directional many-to-one association to OrderSiteStatusAudit
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_site_status_audit_id")
	private OrderIzosdwanSiteStatusAudit orderIzosdwanSiteStatusAudit;

	@Column(name = "parent_id")
	private Integer parentId;

	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	public OrderIzosdwanSiteStageAudit() {
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

	public MstOrderSiteStage getMstOrderSiteStage() {
		return mstOrderSiteStage;
	}

	public void setMstOrderSiteStage(MstOrderSiteStage mstOrderSiteStage) {
		this.mstOrderSiteStage = mstOrderSiteStage;
	}

	

	public OrderIzosdwanSiteStatusAudit getOrderIzosdwanSiteStatusAudit() {
		return orderIzosdwanSiteStatusAudit;
	}

	public void setOrderIzosdwanSiteStatusAudit(OrderIzosdwanSiteStatusAudit orderIzosdwanSiteStatusAudit) {
		this.orderIzosdwanSiteStatusAudit = orderIzosdwanSiteStatusAudit;
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