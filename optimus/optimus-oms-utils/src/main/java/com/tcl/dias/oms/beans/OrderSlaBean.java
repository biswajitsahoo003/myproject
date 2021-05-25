package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.Date;

import com.tcl.dias.oms.entity.entities.OrderGscSla;

/**
 * This file contains the OrderSlaBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OrderSlaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8994816654530400751L;

	private Integer id;

	private Date slaEndDate;

	private Date slaStartDate;

	private SlaMasterBean slaMaster;

	private String slaValue;
	
    private String attributeName;
    
    private String attributeValue;

    /**
     * Convert OrderGscSla to OrderSlaBean
     * 
     * @param orderGscSla
     * @return {@link OrderSlaBean}
     */
	public static OrderSlaBean fromOrderGscSla(OrderGscSla orderGscSla) {
		OrderSlaBean orderSlaBean = new OrderSlaBean();
		orderSlaBean.setId(orderGscSla.getId());
		orderSlaBean.setSlaMaster(SlaMasterBean.fromSlaMaster(orderGscSla.getSlaMaster()));
		orderSlaBean.setAttributeName(orderGscSla.getAttributeName());
		orderSlaBean.setAttributeValue(orderGscSla.getAttributeValue());
	    return orderSlaBean;
    }
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the slaEndDate
	 */
	public Date getSlaEndDate() {
		return slaEndDate;
	}

	/**
	 * @param slaEndDate
	 *            the slaEndDate to set
	 */
	public void setSlaEndDate(Date slaEndDate) {
		this.slaEndDate = slaEndDate;
	}

	/**
	 * @return the slaStartDate
	 */
	public Date getSlaStartDate() {
		return slaStartDate;
	}

	/**
	 * @param slaStartDate
	 *            the slaStartDate to set
	 */
	public void setSlaStartDate(Date slaStartDate) {
		this.slaStartDate = slaStartDate;
	}

	/**
	 * @return the slaMaster
	 */
	public SlaMasterBean getSlaMaster() {
		return slaMaster;
	}

	/**
	 * @param slaMaster
	 *            the slaMaster to set
	 */
	public void setSlaMaster(SlaMasterBean slaMaster) {
		this.slaMaster = slaMaster;
	}

	/**
	 * @return the slaValue
	 */
	public String getSlaValue() {
		return slaValue;
	}

	/**
	 * @param slaValue
	 *            the slaValue to set
	 */
	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
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
}
