package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

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
@Table(name = "product_service_category")
@NamedQuery(name = "ProductServiceCategory.findAll", query = "SELECT p FROM ProductServiceCategory p")
public class ProductServiceCategory extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to ServiceCategoryMaster
	@ManyToOne
	@JoinColumn(name = "category_id")
	private ServiceCategoryMaster serviceCategoryMaster;

	// bi-directional many-to-one association to Product
	@ManyToOne
	private Product product;

	// bi-directional many-to-one association to ProductServicePropertyValue
	@OneToMany(mappedBy = "productServiceCategory")
	private List<ProductServicePropertyValue> productServicePropertyValues;

	public ProductServiceCategory() {
		// default constructor
	}


	public ServiceCategoryMaster getServiceCategoryMaster() {
		return this.serviceCategoryMaster;
	}

	public void setServiceCategoryMaster(ServiceCategoryMaster serviceCategoryMaster) {
		this.serviceCategoryMaster = serviceCategoryMaster;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductServicePropertyValue> getProductServicePropertyValues() {
		return this.productServicePropertyValues;
	}

	public void setProductServicePropertyValues(List<ProductServicePropertyValue> productServicePropertyValues) {
		this.productServicePropertyValues = productServicePropertyValues;
	}

	public ProductServicePropertyValue addProductServicePropertyValue(
			ProductServicePropertyValue productServicePropertyValue) {
		getProductServicePropertyValues().add(productServicePropertyValue);
		productServicePropertyValue.setProductServiceCategory(this);

		return productServicePropertyValue;
	}

	public ProductServicePropertyValue removeProductServicePropertyValue(
			ProductServicePropertyValue productServicePropertyValue) {
		getProductServicePropertyValues().remove(productServicePropertyValue);
		productServicePropertyValue.setProductServiceCategory(null);

		return productServicePropertyValue;
	}

}