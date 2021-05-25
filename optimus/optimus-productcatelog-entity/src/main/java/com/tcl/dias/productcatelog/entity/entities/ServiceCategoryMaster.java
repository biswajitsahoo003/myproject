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
@Table(name = "service_category_master")
@NamedQuery(name = "ServiceCategoryMaster.findAll", query = "SELECT s FROM ServiceCategoryMaster s")
public class ServiceCategoryMaster extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	@Column(name = "product_family_id")
	private int productFamilyId;

	@Column(name = "scm_type")
	private String scmType;

	// bi-directional many-to-one association to ProductServiceCategory
	@OneToMany(mappedBy = "serviceCategoryMaster")
	private List<ProductServiceCategory> productServiceCategories;

	// bi-directional many-to-one association to ServiceCategoryMaster
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private ServiceCategoryMaster parentServiceCategoryMaster;

	// bi-directional many-to-one association to ServiceCategoryMaster
	@OneToMany(mappedBy = "parentServiceCategoryMaster")
	private List<ServiceCategoryMaster> serviceCategoryMasters;

	public ServiceCategoryMaster() {
		// default constructor
	}

	public int getProductFamilyId() {
		return this.productFamilyId;
	}

	public void setProductFamilyId(int productFamilyId) {
		this.productFamilyId = productFamilyId;
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

	public String getScmType() {
		return this.scmType;
	}

	public void setScmType(String scmType) {
		this.scmType = scmType;
	}

	public List<ProductServiceCategory> getProductServiceCategories() {
		return this.productServiceCategories;
	}

	public void setProductServiceCategories(List<ProductServiceCategory> productServiceCategories) {
		this.productServiceCategories = productServiceCategories;
	}

	public ProductServiceCategory addProductServiceCategory(ProductServiceCategory productServiceCategory) {
		getProductServiceCategories().add(productServiceCategory);
		productServiceCategory.setServiceCategoryMaster(this);

		return productServiceCategory;
	}

	public ProductServiceCategory removeProductServiceCategory(ProductServiceCategory productServiceCategory) {
		getProductServiceCategories().remove(productServiceCategory);
		productServiceCategory.setServiceCategoryMaster(null);

		return productServiceCategory;
	}

	public ServiceCategoryMaster getParentServiceCategoryMaster() {
		return parentServiceCategoryMaster;
	}

	public void setParentServiceCategoryMaster(ServiceCategoryMaster parentServiceCategoryMaster) {
		this.parentServiceCategoryMaster = parentServiceCategoryMaster;
	}

	public List<ServiceCategoryMaster> getServiceCategoryMasters() {
		return this.serviceCategoryMasters;
	}

	public void setServiceCategoryMasters(List<ServiceCategoryMaster> serviceCategoryMasters) {
		this.serviceCategoryMasters = serviceCategoryMasters;
	}

	public ServiceCategoryMaster addServiceCategoryMaster(ServiceCategoryMaster serviceCategoryMaster) {
		getServiceCategoryMasters().add(serviceCategoryMaster);
		serviceCategoryMaster.setParentServiceCategoryMaster(this);

		return serviceCategoryMaster;
	}

	public ServiceCategoryMaster removeServiceCategoryMaster(ServiceCategoryMaster serviceCategoryMaster) {
		getServiceCategoryMasters().remove(serviceCategoryMaster);
		serviceCategoryMaster.setParentServiceCategoryMaster(null);

		return serviceCategoryMaster;
	}

}