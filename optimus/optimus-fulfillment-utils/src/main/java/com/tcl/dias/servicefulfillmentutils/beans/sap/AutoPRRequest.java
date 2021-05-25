
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.List;

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
@JsonPropertyOrder({ "PR_Header","PR_Lines" })
public class AutoPRRequest {

	@JsonProperty("PR_Header")
	private PRHeader pRHeader;
	
	@JsonProperty("PR_Lines")
	private List<PRLine> prLines;

	@JsonProperty("PR_Header")
	public PRHeader getPRHeader() {
		return pRHeader;
	}

	@JsonProperty("PR_Header")
	public void setPRHeader(PRHeader pRHeader) {
		this.pRHeader = pRHeader;
	}

	@JsonProperty("PR_Lines")
	public List<PRLine> getPrLines() {
		return prLines;
	}

	@JsonProperty("PR_Lines")
	public void setPrLines(List<PRLine> prLines) {
		this.prLines = prLines;
	}
	
	

}
