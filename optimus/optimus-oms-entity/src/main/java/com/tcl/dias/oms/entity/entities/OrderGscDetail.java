package com.tcl.dias.oms.entity.entities;

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
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 
 * This file contains the OrderGscDetail.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_gsc_details")
@NamedQuery(name = "OrderGscDetail.findAll", query = "SELECT o FROM OrderGscDetail o")
public class OrderGscDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	private Double arc;

	@Column(name = "created_by", length = 45)
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(length = 100)
	private String dest;

	@Column(name = "dest_type", length = 45)
	private String destType;

	private Double mrc;

	private Double nrc;

	@Column(length = 100)
	private String src;

	@Column(name = "src_type", length = 45)
	private String srcType;

	@Column(name = "type")
	private String type;

	// bi-directional many-to-one association to OrderGsc
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_gsc_id")
	private OrderGsc orderGsc;

	// bi-directional many-to-one association to OrderGscTfn
	@OneToMany(mappedBy = "orderGscDetail")
	private Set<OrderGscTfn> orderGscTfns;

	// bi-directional many-to-one association to MstOrderSiteStage
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_site_stage_id")
	private MstOrderSiteStage mstOrderSiteStage;

	// bi-directional many-to-one association to MstOrderSiteStatus
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_order_site_status_id")
	private MstOrderSiteStatus mstOrderSiteStatus;

	public OrderGscDetail() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
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

	public String getDest() {
		return this.dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getDestType() {
		return this.destType;
	}

	public void setDestType(String destType) {
		this.destType = destType;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public String getSrc() {
		return this.src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrcType() {
		return this.srcType;
	}

	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}

	public OrderGsc getOrderGsc() {
		return this.orderGsc;
	}

	public void setOrderGsc(OrderGsc orderGsc) {
		this.orderGsc = orderGsc;
	}

	public Set<OrderGscTfn> getOrderGscTfns() {
		return this.orderGscTfns;
	}

	public void setOrderGscTfns(Set<OrderGscTfn> orderGscTfns) {
		this.orderGscTfns = orderGscTfns;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public OrderGscTfn addOrderGscTfn(OrderGscTfn orderGscTfn) {
		getOrderGscTfns().add(orderGscTfn);
		orderGscTfn.setOrderGscDetail(this);

		return orderGscTfn;
	}

	public OrderGscTfn removeOrderGscTfn(OrderGscTfn orderGscTfn) {
		getOrderGscTfns().remove(orderGscTfn);
		orderGscTfn.setOrderGscDetail(null);

		return orderGscTfn;
	}

	/**
	 * @return the mstOrderSiteStage
	 */
	public MstOrderSiteStage getMstOrderSiteStage() {
		return mstOrderSiteStage;
	}

	/**
	 * @param mstOrderSiteStage
	 *            the mstOrderSiteStage to set
	 */
	public void setMstOrderSiteStage(MstOrderSiteStage mstOrderSiteStage) {
		this.mstOrderSiteStage = mstOrderSiteStage;
	}

	/**
	 * @return the mstOrderSiteStatus
	 */
	public MstOrderSiteStatus getMstOrderSiteStatus() {
		return mstOrderSiteStatus;
	}

	/**
	 * @param mstOrderSiteStatus
	 *            the mstOrderSiteStatus to set
	 */
	public void setMstOrderSiteStatus(MstOrderSiteStatus mstOrderSiteStatus) {
		this.mstOrderSiteStatus = mstOrderSiteStatus;
	}

}