package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * This file contains the ComponentDetail.java class. Bean class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentDetail implements Serializable {
	private static final long serialVersionUID = 7085974668204699696L;
	private Integer componentId;

	private Integer componentMasterId;
	private String name;
	private String isActive;
	private String type;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Integer quotePriceId;
	private List<AttributeDetail> attributes = new ArrayList<>();
	private String vrfPortType;
	private String serviceId;
	
	

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getVrfPortType() {
		return vrfPortType;
	}

	public void setVrfPortType(String vrfPortType) {
		this.vrfPortType = vrfPortType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public List<AttributeDetail> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeDetail> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the componentId
	 */
	public Integer getComponentId() {
		return componentId;
	}

	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	/**
	 * @return the componentMasterId
	 */
	public Integer getComponentMasterId() {
		return componentMasterId;
	}

	/**
	 * @param componentMasterId the componentMasterId to set
	 */
	public void setComponentMasterId(Integer componentMasterId) {
		this.componentMasterId = componentMasterId;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public Integer getQuotePriceId() {
		return quotePriceId;
	}

	public void setQuotePriceId(Integer quotePriceId) {
		this.quotePriceId = quotePriceId;
	}

	@Override
	public String toString() {
		return "ComponentDetail [name=" + name + ", isActive=" + isActive + ", attributes=" + attributes + "]";
	}

}
