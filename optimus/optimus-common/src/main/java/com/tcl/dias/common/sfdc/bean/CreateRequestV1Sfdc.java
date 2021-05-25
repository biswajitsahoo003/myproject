package com.tcl.dias.common.sfdc.bean;

/**
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 * @author vpachava
 *
 */
public class CreateRequestV1Sfdc {

	private Opportunity opportunity;
	
	private String customerContractEntity;

	private String accountCuid;

	private String ownerName;

	private String productBundleName;

	public Opportunity getOpportunity() {
		return opportunity;
	}

	public void setOpportunity(Opportunity opportunity) {
		this.opportunity = opportunity;
	}

	public String getAccountCuid() {
		return accountCuid;
	}

	public void setAccountCuid(String accountCuid) {
		this.accountCuid = accountCuid;
	}

	
	public String getCustomerContractEntity() {
		return customerContractEntity;
	}

	public void setCustomerContractEntity(String customerContractEntity) {
		this.customerContractEntity = customerContractEntity;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getProductBundleName() {
		return productBundleName;
	}

	public void setProductBundleName(String productBundleName) {
		this.productBundleName = productBundleName;
	}

	@Override
	public String toString() {
		return "CreateRequestV1Sfdc [opportunity=" + opportunity + ", customerContractEntity=" + customerContractEntity
				+ ", accountCuid=" + accountCuid + ", ownerName=" + ownerName + ", productBundleName="
				+ productBundleName + "]";
	}

}
