package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author Manojkumar R
 *
 */
@Embeddable
public class ComponentAttributeValuePK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "component_attr_assoc_id", insertable = false, updatable = false)
	private int componentId;

	@Column(name = "category_id", insertable = false, updatable = false)
	private int categoryId;

	@Column(name = "attr_id", insertable = false, updatable = false)
	private int attrId;

	@Column(name = "attr_value")
	private String attrValue;

	public int getComponentId() {
		return this.componentId;
	}

	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getAttrId() {
		return this.attrId;
	}

	public void setAttrId(int attrId) {
		this.attrId = attrId;
	}

	public String getAttrValue() {
		return this.attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ComponentAttributeValuePK)) {
			return false;
		}
		ComponentAttributeValuePK castOther = (ComponentAttributeValuePK) other;
		return (this.componentId == castOther.componentId) && (this.categoryId == castOther.categoryId)
				&& (this.attrId == castOther.attrId) && this.attrValue.equals(castOther.attrValue);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.componentId;
		hash = hash * prime + this.categoryId;
		hash = hash * prime + this.attrId;
		hash = hash * prime + this.attrValue.hashCode();

		return hash;
	}
}