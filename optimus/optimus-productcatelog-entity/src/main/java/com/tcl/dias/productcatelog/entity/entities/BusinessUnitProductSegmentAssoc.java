package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

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
@Table(name = "business_unit_product_segment_assoc")
@NamedQuery(name = "BusinessUnitProductSegmentAssoc.findAll", query = "SELECT b FROM BusinessUnitProductSegmentAssoc b")
public class BusinessUnitProductSegmentAssoc extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to BusinessUnit
	@ManyToOne
	@JoinColumn(name = "bu_id")
	private BusinessUnit businessUnit;

	// bi-directional many-to-one association to ProductSegment
	@ManyToOne
	@JoinColumn(name = "product_segment_id")
	private ProductSegment productSegment;

	public BusinessUnit getBusinessUnit() {
		return this.businessUnit;
	}

	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}

	public ProductSegment getProductSegment() {
		return this.productSegment;
	}

	public void setProductSegment(ProductSegment productSegment) {
		this.productSegment = productSegment;
	}

}