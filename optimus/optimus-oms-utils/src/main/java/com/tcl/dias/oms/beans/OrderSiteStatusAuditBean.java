package com.tcl.dias.oms.beans;

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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderSiteStatusAuditBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1666710268146747523L;

	private String orderStatusName;

	private String createdBy;

	private Timestamp createdTime;

	private List<OrderSiteStageAuditBean> stages;

	
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime
	 *            the createdTime to set
	 */
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the stages
	 */
	public List<OrderSiteStageAuditBean> getStages() {
		if(stages==null) {
			stages=new ArrayList<>();
		}
		return stages;
	}

	/**
	 * @param stages
	 *            the stages to set
	 */
	public void setStages(List<OrderSiteStageAuditBean> stages) {
		this.stages = stages;
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
	
	

}
