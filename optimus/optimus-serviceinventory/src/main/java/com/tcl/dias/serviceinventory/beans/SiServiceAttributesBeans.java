package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;

import java.util.Objects;

/**
 * Bean class to hold SI service attribute bean
 *
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SiServiceAttributesBeans {

	private Integer id;

	private Integer serviceDetailId;

	private String attributeName;

	private String attributeValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getServiceDetailId() {
		return serviceDetailId;
	}

	public SiServiceAttributesBeans(Integer id, Integer serviceDetailId, String attributeName, String attributeValue) {
		super();
		this.id = id;
		this.serviceDetailId = serviceDetailId;
		this.attributeName = attributeName;
		this.attributeValue = attributeValue;
	}

	public void setServiceDetailId(Integer serviceDetailId) {
		this.serviceDetailId = serviceDetailId;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public SiServiceAttributesBeans(SIServiceAttribute entity) {
		if (Objects.nonNull(entity)) {
			this.setId(entity.getId());
			this.setAttributeName(entity.getAttributeName());
			this.setAttributeValue(entity.getAttributeValue());
		}
	}

	public SiServiceAttributesBeans() {

	}

}
