package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * @author Manojkumar R
 *
 */
@Embeddable
public class ProductLocationAssocPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "prd_id", insertable = false, updatable = false)
	private int prdId;

	@Column(name = "loc_id", insertable = false, updatable = false)
	private int locId;

	public ProductLocationAssocPK() {
		//default constructor
	}

	public int getPrdId() {
		return this.prdId;
	}

	public void setPrdId(int prdId) {
		this.prdId = prdId;
	}

	public int getLocId() {
		return this.locId;
	}

	public void setLocId(int locId) {
		this.locId = locId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProductLocationAssocPK)) {
			return false;
		}
		ProductLocationAssocPK castOther = (ProductLocationAssocPK) other;
		return (this.prdId == castOther.prdId) && (this.locId == castOther.locId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.prdId;
		hash = hash * prime + this.locId;

		return hash;
	}
}