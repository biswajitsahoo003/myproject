package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "mst_customer_sp_attributes")
@NamedQuery(name = "MstCustomerSpAttribute.findAll", query = "SELECT m FROM MstCustomerSpAttribute m")
public class MstCustomerSpAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String description;

	private String name;

	private Byte status;

	private String type;

	// bi-directional many-to-one association to CustomerAttributeValue
	@OneToMany(mappedBy = "mstCustomerSpAttribute")
	private Set<CustomerAttributeValue> customerAttributeValues;

	// bi-directional many-to-one association to SpAttributeValue
	@OneToMany(mappedBy = "mstCustomerSpAttribute")
	private Set<SpAttributeValue> spAttributeValues;

	public MstCustomerSpAttribute() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<CustomerAttributeValue> getCustomerAttributeValues() {
		return this.customerAttributeValues;
	}

	public void setCustomerAttributeValues(Set<CustomerAttributeValue> customerAttributeValues) {
		this.customerAttributeValues = customerAttributeValues;
	}

	public CustomerAttributeValue addCustomerAttributeValue(CustomerAttributeValue customerAttributeValue) {
		getCustomerAttributeValues().add(customerAttributeValue);
		customerAttributeValue.setMstCustomerSpAttribute(this);

		return customerAttributeValue;
	}

	public CustomerAttributeValue removeCustomerAttributeValue(CustomerAttributeValue customerAttributeValue) {
		getCustomerAttributeValues().remove(customerAttributeValue);
		customerAttributeValue.setMstCustomerSpAttribute(null);

		return customerAttributeValue;
	}

	public Set<SpAttributeValue> getSpAttributeValues() {
		return this.spAttributeValues;
	}

	public void setSpAttributeValues(Set<SpAttributeValue> spAttributeValues) {
		this.spAttributeValues = spAttributeValues;
	}

	public SpAttributeValue addSpAttributeValue(SpAttributeValue spAttributeValue) {
		getSpAttributeValues().add(spAttributeValue);
		spAttributeValue.setMstCustomerSpAttribute(this);

		return spAttributeValue;
	}

	public SpAttributeValue removeSpAttributeValue(SpAttributeValue spAttributeValue) {
		getSpAttributeValues().remove(spAttributeValue);
		spAttributeValue.setMstCustomerSpAttribute(null);

		return spAttributeValue;
	}

}