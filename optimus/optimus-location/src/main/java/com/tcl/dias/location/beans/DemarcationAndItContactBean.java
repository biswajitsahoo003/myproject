package com.tcl.dias.location.beans;

import com.tcl.dias.common.beans.DemarcationBean;
import com.tcl.dias.common.beans.LocationItContact;

/**
 * This file contains the DemarcationAndItContactBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DemarcationAndItContactBean {

	private LocationItContact contact;

	private DemarcationBean demarcation;
	
	private Integer customerId;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the contact
	 */
	public LocationItContact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(LocationItContact contact) {
		this.contact = contact;
	}

	/**
	 * @return the demarcation
	 */
	public DemarcationBean getDemarcation() {
		return demarcation;
	}

	/**
	 * @param demarcation
	 *            the demarcation to set
	 */
	public void setDemarcation(DemarcationBean demarcation) {
		this.demarcation = demarcation;
	}

}
