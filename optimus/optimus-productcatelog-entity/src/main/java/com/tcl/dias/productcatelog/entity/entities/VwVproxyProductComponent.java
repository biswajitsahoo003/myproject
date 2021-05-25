package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author vpachava
 *
 */

@Entity
@Table(name = "vw_product_offr_comp_attr_dtl_vproxy")
@NamedQuery(name = "VwVproxyProductComponent.findAll", query = "SELECT c FROM VwVproxyProductComponent c")
@IdClass(VwVproxyProductComponentId.class)
public class VwVproxyProductComponent {

	
	@Column(name = "product_name")
	private String productName;

	
	@Column(name = "license_name")
	private String licenseName;

	
	@Column(name = "addon_nm")
	private String addonName;

	@Id
	@Column(name = "attribute_cd")
	private String attributeCode;

	@Id
	@Column(name = "attribute_nm")
	private String attributeName;

	@Id
	@Column(name = "attribute_desc")
	private String attributeDesc;

	@Id
	@Column(name = "attribute_value")
	private String attributeValue;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public String getAddonName() {
		return addonName;
	}

	public void setAddonName(String addonName) {
		this.addonName = addonName;
	}

	public String getAttributeCode() {
		return attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeDesc() {
		return attributeDesc;
	}

	public void setAttributeDesc(String attributeDesc) {
		this.attributeDesc = attributeDesc;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

}
