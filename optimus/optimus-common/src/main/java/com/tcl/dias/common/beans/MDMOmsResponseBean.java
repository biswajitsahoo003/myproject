package com.tcl.dias.common.beans;

/**
 * This file Contains MDMOmsResponseBean  information
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


public class MDMOmsResponseBean {
	private Integer tpsId;
	private String status;
	private String message;
	private String corrId;
	private String partyId;
	private String firstName;
	private String lastName;
	private Integer customerLeBillinginfoId;
	private MDMOmsSrcKeys srcKey;  
	private boolean isAddAddress;
	private String contactId;
	
	
	
	
	
	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public boolean isAddAddress() {
		return isAddAddress;
	}

	public void setAddAddress(boolean isAddAddress) {
		this.isAddAddress = isAddAddress;
	}

	public Integer getCustomerLeBillinginfoId() {
		return customerLeBillinginfoId;
	}

	public void setCustomerLeBillinginfoId(Integer customerLeBillinginfoId) {
		this.customerLeBillinginfoId = customerLeBillinginfoId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCorrId() {
		return corrId;
	}

	public void setCorrId(String corrId) {
		this.corrId = corrId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public MDMOmsSrcKeys getSrcKey() {
		return srcKey;
	}

	public void setSrcKey(MDMOmsSrcKeys srcKey) {
		this.srcKey = srcKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTpsId() {
		return tpsId;
	}

	public void setTpsId(Integer tpsId) {
		this.tpsId = tpsId;
	}
	

}
