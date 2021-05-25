package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * This file contains the OrderIzosdwanAttributeValue.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="order_izosdwan_attribute_values")
@NamedQuery(name="OrderIzosdwanAttributeValue.findAll", query="SELECT o FROM OrderIzosdwanAttributeValue o")
public class OrderIzosdwanAttributeValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="attribute_value")
	private String attributeValue;

	@Column(name="display_value")
	private String displayValue;

	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	@Column(name="order_version")
	private Integer orderVersion;

	public OrderIzosdwanAttributeValue() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getDisplayValue() {
		return this.displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Integer getOrderVersion() {
		return this.orderVersion;
	}

	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

}