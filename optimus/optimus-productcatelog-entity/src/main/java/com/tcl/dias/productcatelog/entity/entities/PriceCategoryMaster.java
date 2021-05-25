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
@Table(name = "price_category_master")
@NamedQuery(name = "PriceCategoryMaster.findAll", query = "SELECT p FROM PriceCategoryMaster p")
public class PriceCategoryMaster extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	@Column(name = "product_family_id")
	private int productFamilyId;

	@Column(name = "svc_cat_type")
	private String svcCatType;

	// bi-directional many-to-one association to ComponentPricePropertyValue
	@OneToMany(mappedBy = "priceCategoryMaster")
	private List<ComponentPricePropertyValue> componentPricePropertyValues;

	// bi-directional many-to-one association to PriceCategoryMaster
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private PriceCategoryMaster parentPriceCategory;

	// bi-directional many-to-one association to PriceCategoryMaster
	@OneToMany(mappedBy = "parentPriceCategory")
	private List<PriceCategoryMaster> priceCategoryMasters;

	public int getProductFamilyId() {
		return this.productFamilyId;
	}

	public void setProductFamilyId(int productFamilyId) {
		this.productFamilyId = productFamilyId;
	}

	public String getSvcCatType() {
		return this.svcCatType;
	}

	public void setSvcCatType(String svcCatType) {
		this.svcCatType = svcCatType;
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

	public List<ComponentPricePropertyValue> getComponentPricePropertyValues() {
		return this.componentPricePropertyValues;
	}

	public void setComponentPricePropertyValues(List<ComponentPricePropertyValue> componentPricePropertyValues) {
		this.componentPricePropertyValues = componentPricePropertyValues;
	}

	public ComponentPricePropertyValue addComponentPricePropertyValue(
			ComponentPricePropertyValue componentPricePropertyValue) {
		getComponentPricePropertyValues().add(componentPricePropertyValue);
		componentPricePropertyValue.setPriceCategoryMaster(this);

		return componentPricePropertyValue;
	}

	public ComponentPricePropertyValue removeComponentPricePropertyValue(
			ComponentPricePropertyValue componentPricePropertyValue) {
		getComponentPricePropertyValues().remove(componentPricePropertyValue);
		componentPricePropertyValue.setPriceCategoryMaster(null);

		return componentPricePropertyValue;
	}

	public PriceCategoryMaster getParentPriceCategory() {
		return parentPriceCategory;
	}

	public void setParentPriceCategory(PriceCategoryMaster parentPriceCategory) {
		this.parentPriceCategory = parentPriceCategory;
	}

	public List<PriceCategoryMaster> getPriceCategoryMasters() {
		return this.priceCategoryMasters;
	}

	public void setPriceCategoryMasters(List<PriceCategoryMaster> priceCategoryMasters) {
		this.priceCategoryMasters = priceCategoryMasters;
	}

	public PriceCategoryMaster addPriceCategoryMaster(PriceCategoryMaster priceCategoryMaster) {
		getPriceCategoryMasters().add(priceCategoryMaster);
		priceCategoryMaster.setParentPriceCategory(this);

		return priceCategoryMaster;
	}

	public PriceCategoryMaster removePriceCategoryMaster(PriceCategoryMaster priceCategoryMaster) {
		getPriceCategoryMasters().remove(priceCategoryMaster);
		priceCategoryMaster.setParentPriceCategory(null);

		return priceCategoryMaster;
	}

}