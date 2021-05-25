package com.tcl.dias.common.beans;

import java.io.Serializable;
/**
 * 
 * This file contains the SolutionComponentBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SolutionComponentBean implements Serializable{

	private Integer solutionComponentid;

	private String componentGroup;

	private Integer cpeComponentId;

	private String isActive;

	private String orderCode;

	private String parentServiceCode;

	private String solutionCode;

	private Integer odrOrderId;
	
	private String serviceCode;

	private Integer serviceDetailId;

	private Integer parentServiceDetailId;

	private Integer solutionServiceDetailId;

	public Integer getSolutionComponentid() {
		return solutionComponentid;
	}

	public void setSolutionComponentid(Integer solutionComponentid) {
		this.solutionComponentid = solutionComponentid;
	}

	public String getComponentGroup() {
		return componentGroup;
	}

	public void setComponentGroup(String componentGroup) {
		this.componentGroup = componentGroup;
	}

	public Integer getCpeComponentId() {
		return cpeComponentId;
	}

	public void setCpeComponentId(Integer cpeComponentId) {
		this.cpeComponentId = cpeComponentId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getParentServiceCode() {
		return parentServiceCode;
	}

	public void setParentServiceCode(String parentServiceCode) {
		this.parentServiceCode = parentServiceCode;
	}

	public String getSolutionCode() {
		return solutionCode;
	}

	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
	}

	public Integer getOdrOrderId() {
		return odrOrderId;
	}

	public void setOdrOrderId(Integer odrOrderId) {
		this.odrOrderId = odrOrderId;
	}

	public Integer getServiceDetailId() {
		return serviceDetailId;
	}

	public void setServiceDetailId(Integer serviceDetailId) {
		this.serviceDetailId = serviceDetailId;
	}

	public Integer getParentServiceDetailId() {
		return parentServiceDetailId;
	}

	public void setParentServiceDetailId(Integer parentServiceDetailId) {
		this.parentServiceDetailId = parentServiceDetailId;
	}

	public Integer getSolutionServiceDetailId() {
		return solutionServiceDetailId;
	}

	public void setSolutionServiceDetailId(Integer solutionServiceDetailId) {
		this.solutionServiceDetailId = solutionServiceDetailId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

}
