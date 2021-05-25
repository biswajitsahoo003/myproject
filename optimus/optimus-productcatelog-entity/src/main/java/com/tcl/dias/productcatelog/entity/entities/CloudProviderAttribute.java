package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * Entity class for vw_cloud_provider_attr_values_IZO
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "vw_cloud_provider_attr_values_IZO")
@Immutable
@IdClass(CloudProviderAttributeViewId.class)
public class CloudProviderAttribute {
	
	@Id
	@Column(name = "IZO_cloud_provider_name")
	private String cloudProviderName;
	
	@Id
	@Column(name = "attribute_name")
	private String attributeName;
	
	@Id
	@Column(name = "attribute_value")
	private String attributeValue;
	
	@Id
	@Column(name = "attribute_unit_of_measure")
	private String uom;

	public String getCloudProviderName() {
		return cloudProviderName;
	}

	public void setCloudProviderName(String cloudProviderName) {
		this.cloudProviderName = cloudProviderName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}
	
	public CloudProviderAttribute() {
		
	}

	public CloudProviderAttribute(String cloudProviderName, String attributeName, String attributeValue, String uom) {
		super();
		this.cloudProviderName = cloudProviderName;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
		this.uom = uom;
	}

	

}
