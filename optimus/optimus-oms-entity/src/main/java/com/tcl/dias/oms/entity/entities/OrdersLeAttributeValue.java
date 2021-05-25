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
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "orders_le_attribute_values")
@NamedQuery(name = "OrdersLeAttributeValue.findAll", query = "SELECT o FROM OrdersLeAttributeValue o")
public class OrdersLeAttributeValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attribute_value")
	private String attributeValue;

	@Column(name = "display_value")
	private String displayValue;

	// bi-directional many-to-one association to MstOmsAttribute
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_oms_attributes_id")
	private MstOmsAttribute mstOmsAttribute;

	// bi-directional many-to-one association to OrderToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "orders_to_le_id")
	private OrderToLe orderToLe;

	public OrdersLeAttributeValue() {
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

	public MstOmsAttribute getMstOmsAttribute() {
		return this.mstOmsAttribute;
	}

	public void setMstOmsAttribute(MstOmsAttribute mstOmsAttribute) {
		this.mstOmsAttribute = mstOmsAttribute;
	}

	public OrderToLe getOrderToLe() {
		return this.orderToLe;
	}

	public void setOrderToLe(OrderToLe orderToLe) {
		this.orderToLe = orderToLe;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}


}