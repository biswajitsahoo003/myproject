package com.tcl.dias.customer.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This Class contains the response information for contact info
 * 
 * 
 * @author SEKHAR ER
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class CustomerConatctInfoResponseDto 
{
	
	private String tpsSfdcAccountId;
	
	private String customerContractingEntity;
	
	private String supplierContractingEntity;
	
	private String agrrementId;
	
	private List<EntityAddressLocationID> entityAdress;
	
	
	
	private SupplierContractingInfo supplierContractingInfo;

	
	
	/**
	 * the tpsSfdcAccountId to get
	 * 
	 * @return tpsSfdcAccountId
	 */
	public String getTpsSfdcAccountId() {
		return tpsSfdcAccountId;
	}
	
	

	/**
	 * the tpsSfdcAccountId to set
	 * 
	 * @param tpsSfdcAccountId
	 */
	public void setTpsSfdcAccountId(String tpsSfdcAccountId) {
		this.tpsSfdcAccountId = tpsSfdcAccountId;
	}

	
	
	/** the agrrementId to get
	 * 
	 * @return agrrementId
	 */
	public String getAgrrementId() {
		return agrrementId;
	}

	/**
	 *the agrrementId to set
	 * 
	 * @param agrrementId
	 */
	public void setAgrrementId(String agrrementId) {
		this.agrrementId = agrrementId;
	}

	/**
	 * the entityAdress to get
	 * 
	 * @return entityAdress List
	 */
	public List<EntityAddressLocationID> getEntityAdress() {
		return entityAdress;
	}

	/**
	 * the entityAdress to set
	 * 
	 * @param entityAdress
	 */
	public void setEntityAdress(List<EntityAddressLocationID> entityAdress) {
		this.entityAdress = entityAdress;
	}

	
	/**
	 * the supplierContractingInfo to get
	 * 
	 * @return supplierContractingInfo
	 */
	public SupplierContractingInfo getSupplierContractingInfo() {
		return supplierContractingInfo;
	}

	
	
	/**
	 * the supplierContractingInfo to set
	 * 
	 * @param supplierContractingInfo
	 */
	public void setSupplierContractingInfo(SupplierContractingInfo supplierContractingInfo) {
		this.supplierContractingInfo = supplierContractingInfo;
	}



	/**
	 * @return the customerContractingEntity
	 */
	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}



	/**
	 * @param customerContractingEntity the customerContractingEntity to set
	 */
	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}



	/**
	 * @return the supplierContractingEntity
	 */
	public String getSupplierContractingEntity() {
		return supplierContractingEntity;
	}



	/**
	 * @param supplierContractingEntity the supplierContractingEntity to set
	 */
	public void setSupplierContractingEntity(String supplierContractingEntity) {
		this.supplierContractingEntity = supplierContractingEntity;
	}
	
	
	
	
	

}
