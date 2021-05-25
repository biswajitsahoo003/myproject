package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "product_service_property_value")
@NamedQuery(name = "ProductServicePropertyValue.findAll", query = "SELECT p FROM ProductServicePropertyValue p")
public class ProductServicePropertyValue extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "attr_val")
	private String attrVal;

	private String comparator;

	@Column(name = "dim_or_msr_cd")
	private String dimOrMsrCd;

	// bi-directional many-to-one association to ProductServiceCategory
	@ManyToOne
	@JoinColumn(name = "product_service_category_id")
	private ProductServiceCategory productServiceCategory;

	// bi-directional many-to-one association to AttributeMaster
	@ManyToOne
	@JoinColumn(name = "attr_id")
	private AttributeMaster attributeMaster;

	public ProductServicePropertyValue() {
		// default constructor
	}

	public String getAttrVal() {
		return this.attrVal;
	}

	public void setAttrVal(String attrVal) {
		this.attrVal = attrVal;
	}

	public String getComparator() {
		return this.comparator;
	}

	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	public String getDimOrMsrCd() {
		return this.dimOrMsrCd;
	}

	public void setDimOrMsrCd(String dimOrMsrCd) {
		this.dimOrMsrCd = dimOrMsrCd;
	}

	public ProductServiceCategory getProductServiceCategory() {
		return this.productServiceCategory;
	}

	public void setProductServiceCategory(ProductServiceCategory productServiceCategory) {
		this.productServiceCategory = productServiceCategory;
	}

	public AttributeMaster getAttributeMaster() {
		return this.attributeMaster;
	}

	public void setAttributeMaster(AttributeMaster attributeMaster) {
		this.attributeMaster = attributeMaster;
	}

}