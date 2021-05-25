package com.tcl.dias.oms.npl.beans;

import com.tcl.dias.oms.beans.UserSite;

/**
 * This file contains the UserLink.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UserLink {
	private Integer linkId;
	private Integer locationId;
	private Integer localItContact;
	private String orderStatus;
	private String orderStage;
	private UserSite userSiteA;
	private UserSite userSiteB;
	
	public UserSite getUserSiteA() {
		return userSiteA;
	}
	public void setUserSiteA(UserSite userSiteA) {
		this.userSiteA = userSiteA;
	}
	public UserSite getUserSiteB() {
		return userSiteB;
	}
	public void setUserSiteB(UserSite userSiteB) {
		this.userSiteB = userSiteB;
	}
	
	
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
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
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderStage() {
		return orderStage;
	}
	public void setOrderStage(String orderStage) {
		this.orderStage = orderStage;
	}
	
}
