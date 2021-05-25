
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcProductServices.java class. used to connect with
 * sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "ProductServiceID" })
public class SfdcDeleteProductServices extends BaseBean {

	@JsonProperty("ProductServiceID")
	private String productServiceId;

	public String getProductServiceId() {
		return productServiceId;
	}

	public void setProductServiceId(String productServiceId) {
		this.productServiceId = productServiceId;
	}

}
