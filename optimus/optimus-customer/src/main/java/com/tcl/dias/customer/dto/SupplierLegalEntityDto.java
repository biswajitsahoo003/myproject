package com.tcl.dias.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
/**
 * This class contains the information  about SupplierLegalEntityDto
 * 
 * @author NITHYA V
 *
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limi
 */
@JsonInclude(Include.NON_NULL)
public class SupplierLegalEntityDto {
	
	private String entityName;
	
	
	private String country;
	
	private String currencyName;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	
	

}
