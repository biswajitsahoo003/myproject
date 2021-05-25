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
@JsonPropertyOrder({ "whereClause", "objectName", "fields", "sourceSystem", "transactionId" })
public class SfdcCreditCheckQueryRequestBean extends BaseBean {

	@JsonProperty("whereClause")
	private String whereClause;

	@JsonProperty("objectName")
	private String objectName;

	@JsonProperty("fields")
	private String fields;

	@JsonProperty("sourceSystem")
	private String sourceSystem;

	@JsonProperty("transactionId")
	private String transactionId;

	/**
	 * @return the whereClause
	 */
	@JsonProperty("whereClause")
	public String getWhereClause() {
		return whereClause;
	}

	/**
	 * @param whereClause the whereClause to set
	 */
	@JsonProperty("whereClause")
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	/**
	 * @return the objectName
	 */
	@JsonProperty("objectName")
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	@JsonProperty("objectName")
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * @return the fields
	 */
	 @JsonProperty("fields")
	public String getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	 @JsonProperty("fields")
	public void setFields(String fields) {
		this.fields = fields;
	}

	/**
	 * @return the sourceSystem
	 */
	 @JsonProperty("sourceSystem")
	public String getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * @param sourceSystem the sourceSystem to set
	 */
	 @JsonProperty("sourceSystem")
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * @return the transactionId
	 */
	 @JsonProperty("transactionId")
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	 @JsonProperty("transactionId")
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	

}
