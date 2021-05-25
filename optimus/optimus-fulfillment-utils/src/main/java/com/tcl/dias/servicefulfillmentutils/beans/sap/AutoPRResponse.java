
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * Bean Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "PR_Response" })
public class AutoPRResponse {

	@JsonProperty("PR_Response")
	private PRResponse pRResponse;

	@JsonProperty("PR_Response")
	public PRResponse getPRResponse() {
		return pRResponse;
	}

	@JsonProperty("PR_Response")
	public void setPRResponse(PRResponse pRResponse) {
		this.pRResponse = pRResponse;
	}

}
