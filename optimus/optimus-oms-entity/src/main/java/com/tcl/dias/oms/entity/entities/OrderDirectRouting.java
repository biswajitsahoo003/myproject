package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the order_dr database table.
 * 
 * @author Srinivasa Raghavan
 */
@Entity
@Table(name = "order_dr")
@NamedQuery(name = "OrderDirectRouting.findAll", query = "SELECT o FROM OrderDirectRouting o")
public class OrderDirectRouting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private BigDecimal arc;

	private String country;

	@Column(name = "created_time")
	private Date createdTime;

	private BigDecimal mrc;

	private BigDecimal nrc;

	@Column(name = "order_version")
	private Integer orderVersion;

	private BigDecimal tcv;

	// bi-directional many-to-one association to OrderTeamsDR
	@ManyToOne
	@JoinColumn(name = "order_teamsdr_id")
	private OrderTeamsDR orderTeamsDR;

	// bi-directional many-to-one association to OrderDirectRoutingCity
	@OneToMany(mappedBy = "orderDirectRouting")
	private List<OrderDirectRoutingCity> orderDRCities;

	public OrderDirectRouting() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getArc() {
		return this.arc;
	}

	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public BigDecimal getMrc() {
		return this.mrc;
	}

	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	public BigDecimal getNrc() {
		return this.nrc;
	}

	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	public Integer getOrderVersion() {
		return this.orderVersion;
	}

	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

	public BigDecimal getTcv() {
		return this.tcv;
	}

	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}

	public OrderTeamsDR getOrderTeamsDR() {
		return this.orderTeamsDR;
	}

	public void setOrderTeamsDR(OrderTeamsDR orderTeamsDR) {
		this.orderTeamsDR = orderTeamsDR;
	}

	public List<OrderDirectRoutingCity> getOrderDRCities() {
		return this.orderDRCities;
	}

	public void setOrderDRCities(List<OrderDirectRoutingCity> orderDRCities) {
		this.orderDRCities = orderDRCities;
	}

	public OrderDirectRoutingCity addOrderDirectRoutingCity(OrderDirectRoutingCity orderDirectRoutingCity) {
		getOrderDRCities().add(orderDirectRoutingCity);
		orderDirectRoutingCity.setOrderDirectRouting(this);

		return orderDirectRoutingCity;
	}

	public OrderDirectRoutingCity removeOrderDirectRoutingCity(OrderDirectRoutingCity orderDirectRoutingCity) {
		getOrderDRCities().remove(orderDirectRoutingCity);
		orderDirectRoutingCity.setOrderDirectRouting(null);

		return orderDirectRoutingCity;
	}

}