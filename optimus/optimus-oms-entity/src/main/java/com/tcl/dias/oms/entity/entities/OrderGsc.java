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
 * This file contains the OrderGsc.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_gsc")
@NamedQuery(name = "OrderGsc.findAll", query = "SELECT o FROM OrderGsc o")
public class OrderGsc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "access_type", length = 100)
	private String accessType;

	private Double arc;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "created_time", nullable = false)
	private Timestamp createdTime;

	@Column(name = "image_url", length = 100)
	private String imageUrl;

	private Double mrc;

	@Column(length = 150)
	private String name;

	private Double nrc;

	@Column(name = "product_name", length = 150)
	private String productName;

	private Byte status;

	private Double tcv;

	// bi-directional many-to-one association to OrderToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_to_le_id")
	private OrderToLe orderToLe;

	// bi-directional many-to-one association to OrderProductSolution
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_product_solution_id")
	private OrderProductSolution orderProductSolution;

	// bi-directional many-to-one association to OrderGscDetail
	@OneToMany(mappedBy = "orderGsc")
	private Set<OrderGscDetail> orderGscDetails;

	// bi-directional many-to-one association to OrderGscSla
	@OneToMany(mappedBy = "orderGsc")
	private Set<OrderGscSla> orderGscSlas;

	public OrderGsc() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessType() {
		return this.accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
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

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getTcv() {
		return this.tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public OrderToLe getOrderToLe() {
		return this.orderToLe;
	}

	public void setOrderToLe(OrderToLe orderToLe) {
		this.orderToLe = orderToLe;
	}

	public OrderProductSolution getOrderProductSolution() {
		return this.orderProductSolution;
	}

	public void setOrderProductSolution(OrderProductSolution orderProductSolution) {
		this.orderProductSolution = orderProductSolution;
	}

	public Set<OrderGscDetail> getOrderGscDetails() {
		return this.orderGscDetails;
	}

	public void setOrderGscDetails(Set<OrderGscDetail> orderGscDetails) {
		this.orderGscDetails = orderGscDetails;
	}

	public OrderGscDetail addOrderGscDetail(OrderGscDetail orderGscDetail) {
		getOrderGscDetails().add(orderGscDetail);
		orderGscDetail.setOrderGsc(this);

		return orderGscDetail;
	}

	public OrderGscDetail removeOrderGscDetail(OrderGscDetail orderGscDetail) {
		getOrderGscDetails().remove(orderGscDetail);
		orderGscDetail.setOrderGsc(null);

		return orderGscDetail;
	}

	public Set<OrderGscSla> getOrderGscSlas() {
		return this.orderGscSlas;
	}

	public void setOrderGscSlas(Set<OrderGscSla> orderGscSlas) {
		this.orderGscSlas = orderGscSlas;
	}

	public OrderGscSla addOrderGscSla(OrderGscSla orderGscSla) {
		getOrderGscSlas().add(orderGscSla);
		orderGscSla.setOrderGsc(this);

		return orderGscSla;
	}

	public OrderGscSla removeOrderGscSla(OrderGscSla orderGscSla) {
		getOrderGscSlas().remove(orderGscSla);
		orderGscSla.setOrderGsc(null);

		return orderGscSla;
	}

}