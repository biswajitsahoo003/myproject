package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * This is the entity class for SDWAN product offerings
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="vw_sdwan_product_offerings")
public class VwSdwanProductOffering implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="addon_cd")
	private String addonCd;
	
	@Column(name="addon_nm")
	private String addonName;

	@Lob
	@Column(name="addon_description")
	private String addonDescription;
	
	@Id
	private Integer id;

	private String mrc;

	private String nrc;

	@Column(name="profile_cd")
	private String profileCd;
	
	@Column(name="profile_nm")
	private String profileName;

	public String getAddonName() {
		return addonName;
	}

	public void setAddonName(String addonName) {
		this.addonName = addonName;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	@Column(name="profile_description")
	private String profileDescription;

	@Column(name="vendor_cd")
	private String vendorCd;

	public VwSdwanProductOffering() {
	}

	public String getAddonCd() {
		return this.addonCd;
	}

	public void setAddonCd(String addonCd) {
		this.addonCd = addonCd;
	}

	public String getAddonDescription() {
		return this.addonDescription;
	}

	public void setAddonDescription(String addonDescription) {
		this.addonDescription = addonDescription;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMrc() {
		return this.mrc;
	}

	public void setMrc(String mrc) {
		this.mrc = mrc;
	}

	public String getNrc() {
		return this.nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public String getProfileCd() {
		return this.profileCd;
	}

	public void setProfileCd(String profileCd) {
		this.profileCd = profileCd;
	}

	public String getProfileDescription() {
		return this.profileDescription;
	}

	public void setProfileDescription(String profileDescription) {
		this.profileDescription = profileDescription;
	}

	public String getVendorCd() {
		return this.vendorCd;
	}

	public void setVendorCd(String vendorCd) {
		this.vendorCd = vendorCd;
	}

}