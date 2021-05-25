package com.tcl.dias.oms.beans;


/**
 * This file contains the CreditCheckStatusResponse.java class.
 * 
 *
 * @author AnneF
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CreditCheckStatusResponse {
	
	private String creditCheckStatus;
	
	private Boolean preapprovedOpportunityFlag;

	public String getCreditCheckStatus() {
		return creditCheckStatus;
	}

	public void setCreditCheckStatus(String creditCheckStatus) {
		this.creditCheckStatus = creditCheckStatus;
	}

	public Boolean getPreapprovedOpportunityFlag() {
		return preapprovedOpportunityFlag;
	}

	public void setPreapprovedOpportunityFlag(Boolean preapprovedOpportunityFlag) {
		this.preapprovedOpportunityFlag = preapprovedOpportunityFlag;
	}

	@Override
	public String toString() {
		return "CreditCheckStatusResponse [creditCheckStatus=" + creditCheckStatus + ", preapprovedOpportunityFlag="
				+ preapprovedOpportunityFlag + "]";
	}
	
	
	
	
	
	

}
