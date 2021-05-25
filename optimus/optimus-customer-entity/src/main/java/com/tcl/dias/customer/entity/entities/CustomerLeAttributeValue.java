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
@Table(name = "customer_le_attribute_values")
@NamedQuery(name = "CustomerLeAttributeValue.findAll", query = "SELECT c FROM CustomerLeAttributeValue c")
public class CustomerLeAttributeValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attribute_values")
	private String attributeValues;

	@Column(name = "product_name")
	private String productName;

	// bi-directional many-to-one association to CustomerLegalEntity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_le_id")
	private CustomerLegalEntity customerLegalEntity;

	// bi-directional many-to-one association to MstLeAttribute
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_le_attributes_id")
	private MstLeAttribute mstLeAttribute;

	public CustomerLeAttributeValue() {
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

	public CustomerLegalEntity getCustomerLegalEntity() {
		return this.customerLegalEntity;
	}

	public void setCustomerLegalEntity(CustomerLegalEntity customerLegalEntity) {
		this.customerLegalEntity = customerLegalEntity;
	}

	public MstLeAttribute getMstLeAttribute() {
		return this.mstLeAttribute;
	}

	public void setMstLeAttribute(MstLeAttribute mstLeAttribute) {
		this.mstLeAttribute = mstLeAttribute;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}