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
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "sp_attribute_values")
@NamedQuery(name = "SpAttributeValue.findAll", query = "SELECT s FROM SpAttributeValue s")
public class SpAttributeValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attribute_values")
	private String attributeValues;

	// bi-directional many-to-one association to MstCustomerSpAttribute
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_customer_sp_attribute_id")
	private MstCustomerSpAttribute mstCustomerSpAttribute;

	// bi-directional many-to-one association to ServiceProvider
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sp_id")
	private ServiceProvider serviceProvider;

	public SpAttributeValue() {
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

	public MstCustomerSpAttribute getMstCustomerSpAttribute() {
		return this.mstCustomerSpAttribute;
	}

	public void setMstCustomerSpAttribute(MstCustomerSpAttribute mstCustomerSpAttribute) {
		this.mstCustomerSpAttribute = mstCustomerSpAttribute;
	}

	public ServiceProvider getServiceProvider() {
		return this.serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

}