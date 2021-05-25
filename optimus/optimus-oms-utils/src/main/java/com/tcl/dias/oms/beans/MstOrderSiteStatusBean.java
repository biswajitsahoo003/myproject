package com.tcl.dias.oms.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStatus;

/**
 *  
 * Bean class
 * 
 *
 * @author ANNE NISHA 
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class MstOrderSiteStatusBean implements Serializable{

	
	private Integer orderStatusId;

	private String orderStatusCode;

	private Byte isActive;

	private String orderStatusName;

	
	public MstOrderSiteStatusBean() {}
	
	
	public MstOrderSiteStatusBean(MstOrderSiteStatus mstOrderSiteStatus) {
		
		this.setOrderStatusCode(mstOrderSiteStatus.getCode());
		this.setOrderStatusId(mstOrderSiteStatus.getId());
		this.setIsActive(mstOrderSiteStatus.getIsActive());
		this.setOrderStatusName(mstOrderSiteStatus.getName());
		
	}
	
	

	/**
	 * @return the orderStatusId
	 */
	public Integer getOrderStatusId() {
		return orderStatusId;
	}


	/**
	 * @param orderStatusId the orderStatusId to set
	 */
	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}


	/**
	 * @return the orderStatusCode
	 */
	public String getOrderStatusCode() {
		return orderStatusCode;
	}


	/**
	 * @param orderStatusCode the orderStatusCode to set
	 */
	public void setOrderStatusCode(String orderStatusCode) {
		this.orderStatusCode = orderStatusCode;
	}


	/**
	 * @return the orderStatusName
	 */
	public String getOrderStatusName() {
		return orderStatusName;
	}


	/**
	 * @param orderStatusName the orderStatusName to set
	 */
	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}


	/**
	 * @return the isActive
	 */
	public Byte getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	
	
}
