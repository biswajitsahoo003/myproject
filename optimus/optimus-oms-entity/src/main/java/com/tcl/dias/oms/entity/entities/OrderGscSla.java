package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * This file contains the OrderGscSla.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "order_gsc_sla")
@NamedQuery(name = "OrderGscSla.findAll", query = "SELECT o FROM OrderGscSla o")
public class OrderGscSla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "attribute_name", length = 200)
	private String attributeName;

	@Column(name = "attribute_value", length = 200)
	private String attributeValue;

	// bi-directional many-to-one association to OrderGsc
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_gsc_id")
	private OrderGsc orderGsc;

	// bi-directional many-to-one association to SlaMaster
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla_master_id")
	private SlaMaster slaMaster;

	public OrderGscSla() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public OrderGsc getOrderGsc() {
		return this.orderGsc;
	}

	public void setOrderGsc(OrderGsc orderGsc) {
		this.orderGsc = orderGsc;
	}

	public SlaMaster getSlaMaster() {
		return this.slaMaster;
	}

	public void setSlaMaster(SlaMaster slaMaster) {
		this.slaMaster = slaMaster;
	}

}