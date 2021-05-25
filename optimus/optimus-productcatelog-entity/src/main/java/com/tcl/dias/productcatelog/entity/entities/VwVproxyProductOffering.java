package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author vpachava
 *
 */

@Entity
@Table(name = "vw_bundled_product_offr_vproxy")
@NamedQuery(name="VwVproxyProductOffering.findAll", query="SELECT c FROM VwVproxyProductOffering c")

public class VwVproxyProductOffering {
	
	@Id
	@Column(name="id")
	private Integer id;

	@Column(name="product_name")
	private String productName;
	
	@Column(name = "product_offering_nm")
	private String productOfferingName;

	
	
	@Column(name = "license_name")
	private String profileName;

	@Lob
	@Column(name = "license_description")
	private String profileDescription;

	
	@Column(name = "addon_name")
	private String addonName;

	public String getProductOfferingName() {
		return productOfferingName;
	}

	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getProfileDescription() {
		return profileDescription;
	}

	public void setProfileDescription(String profileDescription) {
		this.profileDescription = profileDescription;
	}

	public String getAddonName() {
		return addonName;
	}

	public void setAddonName(String addonName) {
		this.addonName = addonName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	
}
