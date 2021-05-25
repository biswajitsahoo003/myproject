/**
 * 
 */
package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * this contains location it contact detais.
 * 
 * @author KusumaK
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class LocationItContact {

	private Integer locationId;
	private String name;
	private String email;
	private String contactNo;
	private Integer erfCustomerLeId;
	private Integer localItContactId;
	private byte isActive;

	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId
	 *            the locationId to set
	 */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the contactNo
	 */
	public String getContactNo() {
		return contactNo;
	}

	/**
	 * @param contactNo
	 *            the contactNo to set
	 */
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	/**
	 * the erfCustomerLeId to get
	 * getErfCustomerLeId
	 * @return
	 */

	public Integer getErfCustomerLeId() {
		return erfCustomerLeId;
	}
	
	/**
	 * the erfCustomerLeId to set
	 * setErfCustomerLeId
	 * @param erfCustomerLeId
	 */

	public void setErfCustomerLeId(Integer erfCustomerLeId) {
		this.erfCustomerLeId = erfCustomerLeId;
	}
	
	

	/**
	 * @return the localItContactId
	 */
	public Integer getLocalItContactId() {
		return localItContactId;
	}

	/**
	 * @param localItContactId the localItContactId to set
	 */
	public void setLocalItContactId(Integer localItContactId) {
		this.localItContactId = localItContactId;
	}

	public byte getIsActive() {
		return isActive;
	}

	public void setIsActive(byte isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "LocationItContact{" +
				"locationId=" + locationId +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", contactNo='" + contactNo + '\'' +
				", erfCustomerLeId=" + erfCustomerLeId +
				", localItContactId=" + localItContactId +
				", isActive=" + isActive +
				'}';
	}
}
