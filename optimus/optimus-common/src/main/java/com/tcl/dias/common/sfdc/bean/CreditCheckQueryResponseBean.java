package com.tcl.dias.common.sfdc.bean;

import java.util.List;

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
public class CreditCheckQueryResponseBean {
	
	
    private String status;

    private String message;
    
    private List<CreditCheckQueryResponse> sfdcCreditCheckQueryResponse;
    
    private Integer tpsId;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the sfdcCreditCheckQueryResponse
	 */
	public List<CreditCheckQueryResponse> getSfdcCreditCheckQueryResponse() {
		return sfdcCreditCheckQueryResponse;
	}

	/**
	 * @param sfdcCreditCheckQueryResponse the sfdcCreditCheckQueryResponse to set
	 */
	public void setSfdcCreditCheckQueryResponse(List<CreditCheckQueryResponse> sfdcCreditCheckQueryResponse) {
		this.sfdcCreditCheckQueryResponse = sfdcCreditCheckQueryResponse;
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
    
	
    

}
