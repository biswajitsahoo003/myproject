package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * @author Manojkumar R
 *
 */
@Embeddable
public class ComponentLocationAssocPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "comp_id", insertable = false, updatable = false)
	private int compId;

	@Column(name = "loc_id", insertable = false, updatable = false)
	private int locId;

	public int getCompId() {
		return this.compId;
	}

	public void setCompId(int compId) {
		this.compId = compId;
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
		if (!(other instanceof ComponentLocationAssocPK)) {
			return false;
		}
		ComponentLocationAssocPK castOther = (ComponentLocationAssocPK) other;
		return (this.compId == castOther.compId) && (this.locId == castOther.locId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.compId;
		hash = hash * prime + this.locId;

		return hash;
	}
}