package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This class is used as a response bean in credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "Id","Customer_Code__c"})
public class CustomerContractingEntity {
	
	@JsonProperty("Id")
	private String Id;
	
	@JsonProperty("Customer_Code__c")
	private String customerCodeC;

	@JsonProperty("Id")
	public String getId() {
		return Id;
	}

	@JsonProperty("Id")
	public void setId(String id) {
		Id = id;
	}

	@JsonProperty("Customer_Code__c")
	public String getCustomerCodeC() {
		return customerCodeC;
	}

	@JsonProperty("Customer_Code__c")
	public void setCustomerCodeC(String customerCodeC) {
		this.customerCodeC = customerCodeC;
		
	}
	
	

}
