package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This file contains the PricingIpcPartnerCommission.java class.
 * 
 * @author Savanya
 *
 */
@Entity
@Table(name = "pricing_ipc_partner_commission")
@NamedQuery(name = "PricingIpcPartnerCommission.findAll", query = "SELECT pc FROM PricingIpcPartnerCommission pc")
public class PricingIpcPartnerCommission  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "profile_id")
	private Integer profileId;
	
	@Column(name = "ind_base_commission")
	private Integer indBaseCommission;

	@Column(name = "ind_deal_reg_commission")
	private Integer indDealRegCommission;
	
	@Column(name = "ind_multi_year_commission")
	private Integer indMultiYearCommission;

	@Column(name = "intl_base_commission")
	private Integer intlBaseCommission;
	
	@Column(name = "intl_multi_year_commission")
	private Integer intlMultiYearCommission;

	public PricingIpcPartnerCommission() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public Integer getIndBaseCommission() {
		return indBaseCommission;
	}

	public void setIndBaseCommission(Integer indBaseCommission) {
		this.indBaseCommission = indBaseCommission;
	}

	public Integer getIndDealRegCommission() {
		return indDealRegCommission;
	}

	public void setIndDealRegCommission(Integer indDealRegCommission) {
		this.indDealRegCommission = indDealRegCommission;
	}

	public Integer getIndMultiYearCommission() {
		return indMultiYearCommission;
	}

	public void setIndMultiYearCommission(Integer indMultiYearCommission) {
		this.indMultiYearCommission = indMultiYearCommission;
	}

	public Integer getIntlBaseCommission() {
		return intlBaseCommission;
	}

	public void setIntlBaseCommission(Integer intlBaseCommission) {
		this.intlBaseCommission = intlBaseCommission;
	}

	public Integer getIntlMultiYearCommission() {
		return intlMultiYearCommission;
	}

	public void setIntlMultiYearCommission(Integer intlMultiYearCommission) {
		this.intlMultiYearCommission = intlMultiYearCommission;
	}

}
