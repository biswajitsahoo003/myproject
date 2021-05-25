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
@Table(name = "component_category_master")
@NamedQuery(name = "ComponentCategoryMaster.findAll", query = "SELECT c FROM ComponentCategoryMaster c")
public class ComponentCategoryMaster extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ccm_type")
	private String ccmType;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	@Column(name = "product_family_id")
	private int productFamilyId;

	// bi-directional many-to-one association to ComponentAttributeValue
	@OneToMany(mappedBy = "componentCategoryMaster")
	private List<ComponentAttributeValue> componentAttributeValues;

	// bi-directional many-to-one association to ComponentCategoryMaster
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private ComponentCategoryMaster parentComponentCategory;

	// bi-directional many-to-one association to ComponentCategoryMaster
	@OneToMany(mappedBy = "parentComponentCategory")
	private List<ComponentCategoryMaster> componentCategoryMasters;

	public String getCcmType() {
		return this.ccmType;
	}

	public void setCcmType(String ccmType) {
		this.ccmType = ccmType;
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

	public List<ComponentAttributeValue> getComponentAttributeValues() {
		return this.componentAttributeValues;
	}

	public void setComponentAttributeValues(List<ComponentAttributeValue> componentAttributeValues) {
		this.componentAttributeValues = componentAttributeValues;
	}

	public ComponentAttributeValue addComponentAttributeValue(ComponentAttributeValue componentAttributeValue) {
		getComponentAttributeValues().add(componentAttributeValue);
		componentAttributeValue.setComponentCategoryMaster(this);

		return componentAttributeValue;
	}

	public ComponentAttributeValue removeComponentAttributeValue(ComponentAttributeValue componentAttributeValue) {
		getComponentAttributeValues().remove(componentAttributeValue);
		componentAttributeValue.setComponentCategoryMaster(null);

		return componentAttributeValue;
	}

	public ComponentCategoryMaster getParentComponentCategory() {
		return parentComponentCategory;
	}

	public void setParentComponentCategory(ComponentCategoryMaster parentComponentCategory) {
		this.parentComponentCategory = parentComponentCategory;
	}

	public List<ComponentCategoryMaster> getComponentCategoryMasters() {
		return this.componentCategoryMasters;
	}

	public void setComponentCategoryMasters(List<ComponentCategoryMaster> componentCategoryMasters) {
		this.componentCategoryMasters = componentCategoryMasters;
	}

	public ComponentCategoryMaster addComponentCategoryMaster(ComponentCategoryMaster componentCategoryMaster) {
		getComponentCategoryMasters().add(componentCategoryMaster);
		componentCategoryMaster.setParentComponentCategory(this);

		return componentCategoryMaster;
	}

	public ComponentCategoryMaster removeComponentCategoryMaster(ComponentCategoryMaster componentCategoryMaster) {
		getComponentCategoryMasters().remove(componentCategoryMaster);
		componentCategoryMaster.setParentComponentCategory(null);

		return componentCategoryMaster;
	}

}