package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
/**
 * Id class for CloudProviderAttribute entity
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CloudProviderAttributeViewId implements Serializable{

	private String cloudProviderName;	
	
	private String attributeName;
	
	private String attributeValue;
	
	private String uom;
	
	public CloudProviderAttributeViewId() {
		
	}

	public CloudProviderAttributeViewId(String cloudProviderName, String attributeName, String attributeValue,
			String uom) {
		super();
		this.cloudProviderName = cloudProviderName;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
		this.uom = uom;
	}

}
