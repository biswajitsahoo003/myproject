package com.tcl.dias.customer.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class contains the information  about SupplierContractingInfo
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class SupplierContractingInfo {
	
	private String accountManagerId;
	private String agreementId;
	private List<EntityAddressLocationID> contractingEntityId;
	private Map<String,Boolean> signingDetails;
	


	public Map<String, Boolean> getSigningDetails() {
		return signingDetails;
	}


	public void setSigningDetails(Map<String, Boolean> signingDetails) {
		this.signingDetails = signingDetails;
	}


	/**
	 * the accountManagerId to get
	 * 
	 * @return accountManagerId
	 */
	public String getAccountManagerId() {
		return accountManagerId;
	}
	
	
	/**
	 * the accountManagerId to set
	 * 
	 * @param accountManagerId
	 */
	public void setAccountManagerId(String accountManagerId) {
		this.accountManagerId = accountManagerId;
	}
	
	/**
	 * the agreementId to get
	 * 
	 * @return agreementId
	 */
	public String getAgreementId() {
		return agreementId;
	}
	/**
	 * the agreementId to set
	 * 
	 * @param agreementId
	 */
	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}
	/**
	 * the contractingEntityId to get
	 * 
	 * @return contractingEntityId
	 */
	public List<EntityAddressLocationID> getContractingEntityId() {
		return contractingEntityId;
	}
	/**
	 * the contractingEntityId to set
	 * 
	 * @param contractingEntityId
	 */
	public void setContractingEntityId(List<EntityAddressLocationID> contractingEntityId) {
		this.contractingEntityId = contractingEntityId;
	}
	

}
