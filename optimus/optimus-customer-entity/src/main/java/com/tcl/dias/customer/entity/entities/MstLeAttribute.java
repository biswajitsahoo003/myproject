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
@Table(name = "mst_le_attributes")
@NamedQuery(name = "MstLeAttribute.findAll", query = "SELECT m FROM MstLeAttribute m")
public class MstLeAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String description;

	private String name;

	private Byte status;

	private String type;

	// bi-directional many-to-one association to CustomerLeAttributeValue
	@OneToMany(mappedBy = "mstLeAttribute")
	private Set<CustomerLeAttributeValue> customerLeAttributeValues;

	// bi-directional many-to-one association to SpLeAttributeValue
	@OneToMany(mappedBy = "mstLeAttribute")
	private Set<SpLeAttributeValue> spLeAttributeValues;

	public MstLeAttribute() {
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

	public Set<CustomerLeAttributeValue> getCustomerLeAttributeValues() {
		return this.customerLeAttributeValues;
	}

	public void setCustomerLeAttributeValues(Set<CustomerLeAttributeValue> customerLeAttributeValues) {
		this.customerLeAttributeValues = customerLeAttributeValues;
	}

	public CustomerLeAttributeValue addCustomerLeAttributeValue(CustomerLeAttributeValue customerLeAttributeValue) {
		getCustomerLeAttributeValues().add(customerLeAttributeValue);
		customerLeAttributeValue.setMstLeAttribute(this);

		return customerLeAttributeValue;
	}

	public CustomerLeAttributeValue removeCustomerLeAttributeValue(CustomerLeAttributeValue customerLeAttributeValue) {
		getCustomerLeAttributeValues().remove(customerLeAttributeValue);
		customerLeAttributeValue.setMstLeAttribute(null);

		return customerLeAttributeValue;
	}

	public Set<SpLeAttributeValue> getSpLeAttributeValues() {
		return this.spLeAttributeValues;
	}

	public void setSpLeAttributeValues(Set<SpLeAttributeValue> spLeAttributeValues) {
		this.spLeAttributeValues = spLeAttributeValues;
	}

	public SpLeAttributeValue addSpLeAttributeValue(SpLeAttributeValue spLeAttributeValue) {
		getSpLeAttributeValues().add(spLeAttributeValue);
		spLeAttributeValue.setMstLeAttribute(this);

		return spLeAttributeValue;
	}

	public SpLeAttributeValue removeSpLeAttributeValue(SpLeAttributeValue spLeAttributeValue) {
		getSpLeAttributeValues().remove(spLeAttributeValue);
		spLeAttributeValue.setMstLeAttribute(null);

		return spLeAttributeValue;
	}

}