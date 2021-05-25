package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * This is the entity class for UsergroupToPartnerLe table
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "usergroup_to_partner_le")
@NamedQuery(name = "UsergroupToPartnerLe.findAll", query = "SELECT u FROM UsergroupToPartnerLe u")
public class UsergroupToPartnerLe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "erf_cus_partner_le_id")
	private Integer erfCusPartnerLeId;

	@Column(name = "erf_cus_partner_le_name")
	private String erfCusPartnerLeName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "partner_id")
	private Partner partner;

	// bi-directional many-to-one association to MstUserGroup
	@ManyToOne
	@JoinColumn(name = "user_group_id")
	private MstUserGroups mstUserGroup;

	public UsergroupToPartnerLe() {
		//DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErfCusPartnerLeId() {
		return this.erfCusPartnerLeId;
	}

	public void setErfCusPartnerLeId(Integer erfCusPartnerLeId) {
		this.erfCusPartnerLeId = erfCusPartnerLeId;
	}

	public MstUserGroups getMstUserGroup() {
		return this.mstUserGroup;
	}

	public void setMstUserGroup(MstUserGroups mstUserGroup) {
		this.mstUserGroup = mstUserGroup;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public String getErfCusPartnerLeName() {
		return erfCusPartnerLeName;
	}

	public void setErfCusPartnerLeName(String erfCusPartnerLeName) {
		this.erfCusPartnerLeName = erfCusPartnerLeName;
	}

}