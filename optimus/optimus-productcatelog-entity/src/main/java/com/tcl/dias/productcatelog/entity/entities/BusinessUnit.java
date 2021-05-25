package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "business_unit")
@NamedQuery(name = "BusinessUnit.findAll", query = "SELECT b FROM BusinessUnit b")
public class BusinessUnit extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	// bi-directional many-to-one association to BusinessUnitProductSegmentAssoc
	@OneToMany(mappedBy = "businessUnit")
	private List<BusinessUnitProductSegmentAssoc> businessUnitProductSegmentAssocs;

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

	public List<BusinessUnitProductSegmentAssoc> getBusinessUnitProductSegmentAssocs() {
		return this.businessUnitProductSegmentAssocs;
	}

	public void setBusinessUnitProductSegmentAssocs(
			List<BusinessUnitProductSegmentAssoc> businessUnitProductSegmentAssocs) {
		this.businessUnitProductSegmentAssocs = businessUnitProductSegmentAssocs;
	}

	public BusinessUnitProductSegmentAssoc addBusinessUnitProductSegmentAssoc(
			BusinessUnitProductSegmentAssoc businessUnitProductSegmentAssoc) {
		getBusinessUnitProductSegmentAssocs().add(businessUnitProductSegmentAssoc);
		businessUnitProductSegmentAssoc.setBusinessUnit(this);

		return businessUnitProductSegmentAssoc;
	}

	public BusinessUnitProductSegmentAssoc removeBusinessUnitProductSegmentAssoc(
			BusinessUnitProductSegmentAssoc businessUnitProductSegmentAssoc) {
		getBusinessUnitProductSegmentAssocs().remove(businessUnitProductSegmentAssoc);
		businessUnitProductSegmentAssoc.setBusinessUnit(null);

		return businessUnitProductSegmentAssoc;
	}

}