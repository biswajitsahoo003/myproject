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
@Table(name = "product_bundle_assoc")
@NamedQuery(name = "ProductBundleAssoc.findAll", query = "SELECT p FROM ProductBundleAssoc p")
public class ProductBundleAssoc extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to ProductBundleMaster
	@ManyToOne
	@JoinColumn(name = "product_bundle_id")
	private ProductBundleMaster productBundleMaster;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	public ProductBundleMaster getProductBundleMaster() {
		return this.productBundleMaster;
	}

	public void setProductBundleMaster(ProductBundleMaster productBundleMaster) {
		this.productBundleMaster = productBundleMaster;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}