package com.tcl.dias.products.izopc.beans;

import java.util.Objects;

import com.tcl.dias.productcatelog.entity.entities.CloudProviderAttribute;

/**
 * POJO class for  Cloud provider attribute details.
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CloudProviderAttributeBean {
	
	private String cloudProviderName;
	
	private String attributeName;
	
	private String attributeValue;
	
	private String uom;

	public CloudProviderAttributeBean(CloudProviderAttribute entity) {
		if (!Objects.isNull(entity)) {
			this.setAttributeName(entity.getAttributeName());
			this.setAttributeValue(entity.getAttributeValue());
			this.setCloudProviderName(entity.getCloudProviderName());
			this.setUom(entity.getUom());
		}
	}
	
	public CloudProviderAttributeBean() {
		
	}
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
		result = prime * result + ((attributeValue == null) ? 0 : attributeValue.hashCode());
		result = prime * result + ((cloudProviderName == null) ? 0 : cloudProviderName.hashCode());
		result = prime * result + ((uom == null) ? 0 : uom.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().equals(obj.getClass()))
			return false;
		CloudProviderAttributeBean other = (CloudProviderAttributeBean) obj;
		if (attributeName == null) {
			if (other.attributeName != null)
				return false;
		} else if (!attributeName.equals(other.attributeName))
			return false;
		if (attributeValue == null) {
			if (other.attributeValue != null)
				return false;
		} else if (!attributeValue.equals(other.attributeValue))
			return false;
		if (cloudProviderName == null) {
			if (other.cloudProviderName != null)
				return false;
		} else if (!cloudProviderName.equals(other.cloudProviderName))
			return false;
		if (uom == null) {
			if (other.uom != null)
				return false;
		} else if (!uom.equals(other.uom))
			return false;
		return true;
	}
	
	

}
