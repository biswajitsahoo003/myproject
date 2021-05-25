package com.tcl.dias.sfdc.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.sfdc.response.bean.SfdcSalesFunnelResponseBean;

/**
 * This class is used as a response bean in credit check
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"status","message","data"})
public class SfdcCreditCheckQueryResponseBean {
	
		@JsonProperty("status")
	    private String status;

	    @JsonProperty("message")
	    private String message;

	    @JsonProperty("data")
	    private List<SfdcCreditCheckQueryResponse> sfdcCreditCheckQueryBean;

		/**
		 * @return the status
		 */
	    @JsonProperty("status")
		public String getStatus() {
			return status;
		}

		/**
		 * @param status the status to set
		 */
	    @JsonProperty("status")
		public void setStatus(String status) {
			this.status = status;
		}

		/**
		 * @return the message
		 */
	    @JsonProperty("message")
		public String getMessage() {
			return message;
		}

		/**
		 * @param message the message to set
		 */
	    @JsonProperty("message")
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the sfdcCreditCheckQueryBean
		 */
	    @JsonProperty("data")
		public List<SfdcCreditCheckQueryResponse> getSfdcCreditCheckQueryBean() {
			return sfdcCreditCheckQueryBean;
		}

		/**
		 * @param sfdcCreditCheckQueryBean the sfdcCreditCheckQueryBean to set
		 */
	    @JsonProperty("data")
		public void setSfdcCreditCheckQueryBean(List<SfdcCreditCheckQueryResponse> sfdcCreditCheckQueryBean) {
			this.sfdcCreditCheckQueryBean = sfdcCreditCheckQueryBean;
		}
	    
	    

}
