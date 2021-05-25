package com.tcl.dias.sfdc.bean;

import java.util.List;

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
@JsonPropertyOrder({ "totalSize","done","records"})
public class ProductServicesQuery {
	
	@JsonProperty("records")
	private List<ProductServicesRecord> record;
	
	@JsonProperty("totalSize")
	private String totalSize;
	
	@JsonProperty("done")
	private String done;

	/**
	 * @return the record
	 */
	@JsonProperty("records")
	public List<ProductServicesRecord> getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	@JsonProperty("records")
	public void setRecord(List<ProductServicesRecord> record) {
		this.record = record;
	}

	/**
	 * @return the totalSize
	 */
	@JsonProperty("totalSize")
	public String getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize to set
	 */
	@JsonProperty("totalSize")
	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the done
	 */
	@JsonProperty("done")
	public String getDone() {
		return done;
	}

	/**
	 * @param done the done to set
	 */
	@JsonProperty("done")
	public void setDone(String done) {
		this.done = done;
	}
	
	

}
