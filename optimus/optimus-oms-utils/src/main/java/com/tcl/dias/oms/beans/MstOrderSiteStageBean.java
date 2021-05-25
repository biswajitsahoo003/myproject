package com.tcl.dias.oms.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.MstOrderSiteStage;

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
public class MstOrderSiteStageBean implements Serializable {
	
	private Integer orderStageId;

	private String orderStageCode;

	private String isActive;

	private String orderStageName;
	
	public MstOrderSiteStageBean() {}

	public MstOrderSiteStageBean(MstOrderSiteStage mstOrderSiteStage) {
		
		this.setOrderStageCode(mstOrderSiteStage.getCode());
		this.setOrderStageId(mstOrderSiteStage.getId());
		this.setIsActive(mstOrderSiteStage.getIsActive());
		this.setOrderStageName(mstOrderSiteStage.getName());
		
	}

	

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the orderStageId
	 */
	public Integer getOrderStageId() {
		return orderStageId;
	}

	/**
	 * @param orderStageId the orderStageId to set
	 */
	public void setOrderStageId(Integer orderStageId) {
		this.orderStageId = orderStageId;
	}

	/**
	 * @return the orderStageCode
	 */
	public String getOrderStageCode() {
		return orderStageCode;
	}

	/**
	 * @param orderStageCode the orderStageCode to set
	 */
	public void setOrderStageCode(String orderStageCode) {
		this.orderStageCode = orderStageCode;
	}

	/**
	 * @return the orderStageName
	 */
	public String getOrderStageName() {
		return orderStageName;
	}

	/**
	 * @param orderStageName the orderStageName to set
	 */
	public void setOrderStageName(String orderStageName) {
		this.orderStageName = orderStageName;
	}



	
	
}
