package com.tcl.dias.common.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This class is used as a response bean in credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SfdcCreditCheckQueryRequest {
	
	 	private String whereClause;
	    private String objectName;
	    private String fields;
	    private String sourceSystem;
	    private String transactionId;
	    private Integer tpsId;
		/**
		 * @return the whereClause
		 */
		public String getWhereClause() {
			return whereClause;
		}
		/**
		 * @param whereClause the whereClause to set
		 */
		public void setWhereClause(String whereClause) {
			this.whereClause = whereClause;
		}
		/**
		 * @return the objectName
		 */
		public String getObjectName() {
			return objectName;
		}
		/**
		 * @param objectName the objectName to set
		 */
		public void setObjectName(String objectName) {
			this.objectName = objectName;
		}
		/**
		 * @return the fields
		 */
		public String getFields() {
			return fields;
		}
		/**
		 * @param fields the fields to set
		 */
		public void setFields(String fields) {
			this.fields = fields;
		}
		/**
		 * @return the sourceSystem
		 */
		public String getSourceSystem() {
			return sourceSystem;
		}
		/**
		 * @param sourceSystem the sourceSystem to set
		 */
		public void setSourceSystem(String sourceSystem) {
			this.sourceSystem = sourceSystem;
		}
		/**
		 * @return the transactionId
		 */
		public String getTransactionId() {
			return transactionId;
		}
		/**
		 * @param transactionId the transactionId to set
		 */
		public void setTransactionId(String transactionId) {
			this.transactionId = transactionId;
		}
		
		
		/**
		 * @return the tpsId
		 */
		public Integer getTpsId() {
			return tpsId;
		}
		/**
		 * @param tpsId the tpsId to set
		 */
		public void setTpsId(Integer tpsId) {
			this.tpsId = tpsId;
		}
		@Override
		public String toString() {
			return "SfdcCreditCheckQueryRequest [whereClause=" + whereClause + ", objectName=" + objectName
					+ ", fields=" + fields + ", sourceSystem=" + sourceSystem + ", transactionId=" + transactionId
					+ ", tpsId=" + tpsId
					+ "]";
		}
	    
	    

}
