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
@Table(name = "partner_type")
@NamedQuery(name = "PartnerType.findAll", query = "SELECT p FROM PartnerType p")
public class PartnerType extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	// bi-directional many-to-one association to Partner
	@OneToMany(mappedBy = "partnerType")
	private List<Partner> partners;

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

	public List<Partner> getPartners() {
		return this.partners;
	}

	public void setPartners(List<Partner> partners) {
		this.partners = partners;
	}

	public Partner addPartner(Partner partner) {
		getPartners().add(partner);
		partner.setPartnerType(this);

		return partner;
	}

	public Partner removePartner(Partner partner) {
		getPartners().remove(partner);
		partner.setPartnerType(null);

		return partner;
	}

}