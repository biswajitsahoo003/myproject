package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author archchan
 * 
 *  This bean class is to capture the RFO details of a ticket passed to
 *  Service Now.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "rforesponsible", "rfocause", "rfospecification", "closureNotes" })
public class Rfoinformation {

	@JsonProperty("rforesponsible")
	private String rforesponsible;

	@JsonProperty("rfocause")
	private String rfocause;

	@JsonProperty("rfospecification")
	private String rfospecification;

	@JsonProperty("closureNotes")
	private String closureNotes;

	@JsonProperty("rforesponsible")
	public String getRforesponsible() {
		return rforesponsible;
	}

	/**
	 * @param rforesponsible
	 * 
	 *            the RFO responsible to set
	 */
	@JsonProperty("rforesponsible")
	public void setRforesponsible(String rforesponsible) {
		this.rforesponsible = rforesponsible;
	}

	/**
	 * @return the rfocause
	 */
	@JsonProperty("rfocause")
	public String getRfocause() {
		return rfocause;
	}

	/**
	 * @param rfocause
	 * 
	 *            the rfocause to set
	 */
	@JsonProperty("rfocause")
	public void setRfocause(String rfocause) {
		this.rfocause = rfocause;
	}

	/**
	 * @return rfospecification
	 */
	@JsonProperty("rfospecification")
	public String getRfospecification() {
		return rfospecification;
	}

	/**
	 * @param rfospecification
	 *            the rfospecification to set
	 */
	@JsonProperty("rfospecification")
	public void setRfospecification(String rfospecification) {
		this.rfospecification = rfospecification;
	}

	/**
	 * @return closureNotes
	 */
	@JsonProperty("closureNotes")
	public String getClosureNotes() {
		return closureNotes;
	}

	/**
	 * @param closureNotes
	 *            closureNotes to set
	 */
	@JsonProperty("closureNotes")
	public void setClosureNotes(String closureNotes) {
		this.closureNotes = closureNotes;
	}

}
