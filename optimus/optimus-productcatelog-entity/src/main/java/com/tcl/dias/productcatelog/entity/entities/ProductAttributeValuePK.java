package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * @author Manojkumar R
 *
 */
@Embeddable
public class ProductAttributeValuePK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "product_id", insertable = false, updatable = false)
	private int productId;

	@Column(name = "attr_id", insertable = false, updatable = false)
	private int attrId;

	@Column(name = "attr_value")
	private String attrValue;

	public int getProductId() {
		return this.productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
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
		if (!(other instanceof ProductAttributeValuePK)) {
			return false;
		}
		ProductAttributeValuePK castOther = (ProductAttributeValuePK) other;
		return (this.productId == castOther.productId) && (this.attrId == castOther.attrId)
				&& this.attrValue.equals(castOther.attrValue);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productId;
		hash = hash * prime + this.attrId;
		hash = hash * prime + this.attrValue.hashCode();

		return hash;
	}
}