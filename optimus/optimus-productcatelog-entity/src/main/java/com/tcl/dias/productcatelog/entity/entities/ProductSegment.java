package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;


/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "product_segment")
@NamedQuery(name = "ProductSegment.findAll", query = "SELECT p FROM ProductSegment p")
public class ProductSegment extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	// bi-directional many-to-one association to BusinessUnitProductSegmentAssoc
	@OneToMany(mappedBy = "productSegment")
	private List<BusinessUnitProductSegmentAssoc> businessUnitProductSegmentAssocs;

	// bi-directional many-to-one association to ProductFamily
	@OneToMany(mappedBy = "productSegment")
	private List<ProductCatalog> productFamilies;

	// bi-directional many-to-one association to ProductSegment
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private ProductSegment parentProductSegment;

	// bi-directional many-to-one association to ProductSegment
	@OneToMany(mappedBy = "parentProductSegment")
	private List<ProductSegment> productSegments;

	public ProductSegment() {
		// default constructor
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BusinessUnitProductSegmentAssoc> getBusinessUnitProductSegmentAssocs() {
		return this.businessUnitProductSegmentAssocs;
	}

	public void setBusinessUnitProductSegmentAssocs(
			List<BusinessUnitProductSegmentAssoc> businessUnitProductSegmentAssocs) {
		this.businessUnitProductSegmentAssocs = businessUnitProductSegmentAssocs;
	}

	public BusinessUnitProductSegmentAssoc addBusinessUnitProductSegmentAssoc(
			BusinessUnitProductSegmentAssoc businessUnitProductSegmentAssoc) {
		getBusinessUnitProductSegmentAssocs().add(businessUnitProductSegmentAssoc);
		businessUnitProductSegmentAssoc.setProductSegment(this);

		return businessUnitProductSegmentAssoc;
	}

	public BusinessUnitProductSegmentAssoc removeBusinessUnitProductSegmentAssoc(
			BusinessUnitProductSegmentAssoc businessUnitProductSegmentAssoc) {
		getBusinessUnitProductSegmentAssocs().remove(businessUnitProductSegmentAssoc);
		businessUnitProductSegmentAssoc.setProductSegment(null);

		return businessUnitProductSegmentAssoc;
	}

	public List<ProductCatalog> getProductFamilies() {
		return this.productFamilies;
	}

	public void setProductFamilies(List<ProductCatalog> productFamilies) {
		this.productFamilies = productFamilies;
	}

	public ProductCatalog addProductFamily(ProductCatalog productFamily) {
		getProductFamilies().add(productFamily);
		productFamily.setProductSegment(this);

		return productFamily;
	}

	public ProductCatalog removeProductFamily(ProductCatalog productFamily) {
		getProductFamilies().remove(productFamily);
		productFamily.setProductSegment(null);

		return productFamily;
	}

	public ProductSegment getParentProductSegment() {
		return parentProductSegment;
	}

	public void setParentProductSegment(ProductSegment parentProductSegment) {
		this.parentProductSegment = parentProductSegment;
	}

	public List<ProductSegment> getProductSegments() {
		return this.productSegments;
	}

	public void setProductSegments(List<ProductSegment> productSegments) {
		this.productSegments = productSegments;
	}

	public ProductSegment addProductSegment(ProductSegment productSegment) {
		getProductSegments().add(productSegment);
		productSegment.setParentProductSegment(this);

		return productSegment;
	}

	public ProductSegment removeProductSegment(ProductSegment productSegment) {
		getProductSegments().remove(productSegment);
		productSegment.setParentProductSegment(null);

		return productSegment;
	}


}