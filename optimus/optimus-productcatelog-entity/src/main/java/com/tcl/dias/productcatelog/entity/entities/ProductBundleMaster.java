package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "product_bundle_master")
@NamedQuery(name = "ProductBundleMaster.findAll", query = "SELECT p FROM ProductBundleMaster p")
public class ProductBundleMaster extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	// bi-directional many-to-one association to ProductBundleAssoc
	@OneToMany(mappedBy = "productBundleMaster")
	private List<ProductBundleAssoc> productBundleAssocs;

	public ProductBundleMaster() {
		//default constructor
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

	public List<ProductBundleAssoc> getProductBundleAssocs() {
		return this.productBundleAssocs;
	}

	public void setProductBundleAssocs(List<ProductBundleAssoc> productBundleAssocs) {
		this.productBundleAssocs = productBundleAssocs;
	}

	public ProductBundleAssoc addProductBundleAssoc(ProductBundleAssoc productBundleAssoc) {
		getProductBundleAssocs().add(productBundleAssoc);
		productBundleAssoc.setProductBundleMaster(this);

		return productBundleAssoc;
	}

	public ProductBundleAssoc removeProductBundleAssoc(ProductBundleAssoc productBundleAssoc) {
		getProductBundleAssocs().remove(productBundleAssoc);
		productBundleAssoc.setProductBundleMaster(null);

		return productBundleAssoc;
	}

}