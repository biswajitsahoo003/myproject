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
 * The persistent class for the order_dr_city database table.
 * 
 * @author Srinivasa Raghavan
 */
@Entity
@Table(name = "order_dr_city")
@NamedQuery(name = "OrderDirectRoutingCity.findAll", query = "SELECT o FROM OrderDirectRoutingCity o")
public class OrderDirectRoutingCity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private BigDecimal arc;

	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "media_gateway_type")
	private String mediaGatewayType;

	private BigDecimal mrc;

	private String name;

	private BigDecimal nrc;

	@Column(name = "order_version")
	private Integer orderVersion;

	private BigDecimal tcv;

	// bi-directional many-to-one association to OrderDirectRouting
	@ManyToOne
	@JoinColumn(name = "order_dr_id")
	private OrderDirectRouting orderDirectRouting;

	// bi-directional many-to-one association to OrderDirectRoutingMediaGateways
	@OneToMany(mappedBy = "orderDirectRoutingCity")
	private List<OrderDirectRoutingMediaGateways> orderDirectRoutingMediagateways;

	public OrderDirectRoutingCity() {
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

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getMediaGatewayType() {
		return this.mediaGatewayType;
	}

	public void setMediaGatewayType(String mediaGatewayType) {
		this.mediaGatewayType = mediaGatewayType;
	}

	public BigDecimal getMrc() {
		return this.mrc;
	}

	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public OrderDirectRouting getOrderDirectRouting() {
		return this.orderDirectRouting;
	}

	public void setOrderDirectRouting(OrderDirectRouting orderDirectRouting) {
		this.orderDirectRouting = orderDirectRouting;
	}

	public List<OrderDirectRoutingMediaGateways> getOrderDirectRoutingMediagateways() {
		return this.orderDirectRoutingMediagateways;
	}

	public void setOrderDirectRoutingMediagateways(
			List<OrderDirectRoutingMediaGateways> orderDirectRoutingMediagateways) {
		this.orderDirectRoutingMediagateways = orderDirectRoutingMediagateways;
	}

	public OrderDirectRoutingMediaGateways addOrderDirectRoutingMg(
			OrderDirectRoutingMediaGateways orderDirectRoutingMediagateways) {
		getOrderDirectRoutingMediagateways().add(orderDirectRoutingMediagateways);
		orderDirectRoutingMediagateways.setOrderDirectRoutingCity(this);

		return orderDirectRoutingMediagateways;
	}

	public OrderDirectRoutingMediaGateways removeOrderDirectRoutingMg(
			OrderDirectRoutingMediaGateways orderDirectRoutingMediagateways) {
		getOrderDirectRoutingMediagateways().remove(orderDirectRoutingMediagateways);
		orderDirectRoutingMediagateways.setOrderDirectRoutingCity(null);

		return orderDirectRoutingMediagateways;
	}

}