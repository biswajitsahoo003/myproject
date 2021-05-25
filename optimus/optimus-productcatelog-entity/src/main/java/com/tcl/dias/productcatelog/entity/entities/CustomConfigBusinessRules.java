package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This file contains the CustomConfigBusinessRules.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@Entity
@Table(name = "custom_config_business_rules")
public class CustomConfigBusinessRules implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "attribute_name")
	private String attributeName;
	
	@Column(name = "attribute_value")
	private String attributeValue;
	
	@Column(name = "dependent_attributes")
	private String dependentAttibutes;
	
	
	// bi-directional many-to-one association to ProductFamily
		@ManyToOne
		@JoinColumn(name = "product_id")
		private Product product;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getDependentAttibutes() {
		return dependentAttibutes;
	}

	public void setDependentAttibutes(String dependentAttibutes) {
		this.dependentAttibutes = dependentAttibutes;
	}

	public Product getProduct() {
		return product;
	}

	public void setProductOffering(Product product) {
		this.product = product;
	}


}
