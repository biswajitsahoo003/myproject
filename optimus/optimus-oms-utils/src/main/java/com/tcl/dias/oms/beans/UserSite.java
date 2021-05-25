package com.tcl.dias.oms.beans;

/**
 * This file contains the UserSite.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UserSite {

	private Integer siteId;
	private Integer locationId;
	private Integer localItContact;
	private String orderStatus;
	private String orderStage;

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getLocalItContact() {
		return localItContact;
	}

	public void setLocalItContact(Integer localItContact) {
		this.localItContact = localItContact;
	}
	

	/**
	 * @return the orderStage
	 */
	public String getOrderStage() {
		return orderStage;
	}

	/**
	 * @param orderStage the orderStage to set
	 */
	public void setOrderStage(String orderStage) {
		this.orderStage = orderStage;
	}
	
	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "UserSite [siteId=" + siteId + ", locationId=" + locationId + ", localItContact=" + localItContact + "]";
	}

}
