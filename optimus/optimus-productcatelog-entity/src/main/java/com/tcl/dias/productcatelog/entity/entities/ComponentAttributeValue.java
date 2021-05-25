package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "component_attribute_value_assoc")
//@Table(name="component_cfs_specification")
@NamedQuery(name = "ComponentAttributeValue.findAll", query = "SELECT c FROM ComponentAttributeValue c")
public class ComponentAttributeValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ComponentAttributeValuePK id;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "is_active_ind")
	private String isActiveInd;

	@Column(name = "is_default_ind")
	private String isDefaultInd;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_dt")
	private Date updatedDt;

	// bi-directional many-to-one association to ComponentCategoryMaster
	@ManyToOne
	@JoinColumn(name = "category_id", insertable = false, updatable = false)
	private ComponentCategoryMaster componentCategoryMaster;

	// bi-directional many-to-one association to Component
	@ManyToOne
	@JoinColumn(insertable = false, updatable = false)
	private Component component;

	// bi-directional many-to-one association to AttributeMaster
	@ManyToOne
	@JoinColumn(name = "attr_id", insertable = false, updatable = false)
	private AttributeMaster attributeMaster;

	public ComponentAttributeValuePK getId() {
		return this.id;
	}

	public void setId(ComponentAttributeValuePK id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getIsActiveInd() {
		return this.isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public String getIsDefaultInd() {
		return this.isDefaultInd;
	}

	public void setIsDefaultInd(String isDefaultInd) {
		this.isDefaultInd = isDefaultInd;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public ComponentCategoryMaster getComponentCategoryMaster() {
		return this.componentCategoryMaster;
	}

	public void setComponentCategoryMaster(ComponentCategoryMaster componentCategoryMaster) {
		this.componentCategoryMaster = componentCategoryMaster;
	}

	public Component getComponent() {
		return this.component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public AttributeMaster getAttributeMaster() {
		return this.attributeMaster;
	}

	public void setAttributeMaster(AttributeMaster attributeMaster) {
		this.attributeMaster = attributeMaster;
	}

}