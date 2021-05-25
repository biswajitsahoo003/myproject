package com.tcl.dias.products.izopc.beans;

import java.util.Objects;

import com.tcl.dias.productcatelog.entity.entities.Provider;

/**
 * POJO class for cloud provider details.
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CloudProviderBean {
	
	private String name;
	
	private String aliasName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
	public CloudProviderBean (Provider provider) {
		if (Objects.nonNull(provider)) {
			this.setName(provider.getName());
			//this.setAliasName(provider.getAliasName());
		}
	}
	
	

}
