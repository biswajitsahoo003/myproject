package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the order_dr_mediagateways database table.
 * 
 * @author Srinivasa Raghavan
 * 
 */
@Entity
@Table(name = "order_dr_mediagateways")
@NamedQuery(name = "OrderDirectRoutingMediaGateways.findAll", query = "SELECT o FROM OrderDirectRoutingMediaGateways o")
public class OrderDirectRoutingMediaGateways implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Double arc;

	@Column(name = "created_time")
	private Date createdTime;

	private Double mrc;

	private String name;

	private Double nrc;

	@Column(name = "order_version")
	private Integer orderVersion;

	private Integer quantity;

	private Double tcv;

	// bi-directional many-to-one association to OrderDirectRoutingCity
	@ManyToOne
	@JoinColumn(name = "order_dr_city_id")
	private OrderDirectRoutingCity orderDirectRoutingCity;

	public OrderDirectRoutingMediaGateways() {
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

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Integer getOrderVersion() {
		return this.orderVersion;
	}

	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTcv() {
		return this.tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public OrderDirectRoutingCity getOrderDirectRoutingCity() {
		return this.orderDirectRoutingCity;
	}

	public void setOrderDirectRoutingCity(OrderDirectRoutingCity orderDirectRoutingCity) {
		this.orderDirectRoutingCity = orderDirectRoutingCity;
	}

}