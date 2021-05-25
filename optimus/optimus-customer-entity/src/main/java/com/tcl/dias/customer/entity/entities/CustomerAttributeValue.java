package com.tcl.dias.customer.entity.entities;

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
@Table(name = "customer_attribute_values")
@NamedQuery(name = "CustomerAttributeValue.findAll", query = "SELECT c FROM CustomerAttributeValue c")
public class CustomerAttributeValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attribute_values")
	private String attributeValues;

	// bi-directional many-to-one association to Customer
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;

	// bi-directional many-to-one association to MstCustomerSpAttribute
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_customer_sp_attribute_id")
	private MstCustomerSpAttribute mstCustomerSpAttribute;

	public CustomerAttributeValue() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeValues() {
		return this.attributeValues;
	}

	public void setAttributeValues(String attributeValues) {
		this.attributeValues = attributeValues;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public MstCustomerSpAttribute getMstCustomerSpAttribute() {
		return this.mstCustomerSpAttribute;
	}

	public void setMstCustomerSpAttribute(MstCustomerSpAttribute mstCustomerSpAttribute) {
		this.mstCustomerSpAttribute = mstCustomerSpAttribute;
	}

}