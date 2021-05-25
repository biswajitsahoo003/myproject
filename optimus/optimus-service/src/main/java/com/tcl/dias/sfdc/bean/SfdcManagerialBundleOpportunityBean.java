package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcManagerialBundleOpportunityBean.java class. used
 * to connect with sdfc
 * 
 *
 * @author vpachava
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)


@JsonPropertyOrder({ "opportunity", "AccountCUID", "CustomerContractingEntity", "OwnerName", "ProductBundleName" })
public class SfdcManagerialBundleOpportunityBean extends BaseBean{

	@JsonProperty("opportunity")
	private SfdcBundleOpportunityBean opportunity;

	@JsonProperty("AccountCUID")
	private String accountId;

	@JsonProperty("CustomerContractingEntity")
	private String CustomerContractingEntity;

	@JsonProperty("OwnerName")
	private String ownerName;

	@JsonProperty("ProductBundleName")
	private String ProductBundleName;

	@JsonProperty("opportunity")
	public SfdcBundleOpportunityBean getOpportunity() {
		return opportunity;
	}

	@JsonProperty("opportunity")
	public void setOpportunity(SfdcBundleOpportunityBean opportunity) {
		this.opportunity = opportunity;
	}

	@JsonProperty("AccountCUID")
	public String getAccountId() {
		return accountId;
	}

	@JsonProperty("AccountCUID")
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@JsonProperty("CustomerContractingEntity")
	public String getCustomerContractingEntity() {
		return CustomerContractingEntity;
	}

	@JsonProperty("CustomerContractingEntity")
	public void setCustomerContractingEntity(String customerContractingEntity) {
		CustomerContractingEntity = customerContractingEntity;
	}

	@JsonProperty("OwnerName")
	public String getOwnerName() {
		return ownerName;
	}

	@JsonProperty("OwnerName")
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@JsonProperty("ProductBundleName")
	public String getProductBundleName() {
		return ProductBundleName;
	}

	@JsonProperty("ProductBundleName")
	public void setProductBundleName(String productBundleName) {
		ProductBundleName = productBundleName;
	}

}
