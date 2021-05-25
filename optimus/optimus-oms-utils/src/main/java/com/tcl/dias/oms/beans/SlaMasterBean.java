package com.tcl.dias.oms.beans;

import java.io.Serializable;

import com.tcl.dias.oms.entity.entities.SlaMaster;

/**
 * This file contains the SlaMasterBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SlaMasterBean   implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7812699740101010572L;

	private Integer id;

	private Integer slaDurationInDays;

	private String slaName;

	public static SlaMasterBean fromSlaMaster(SlaMaster slaMaster) {
		SlaMasterBean slaMasterBean=new SlaMasterBean();
		slaMasterBean.setId(slaMaster.getId());
		slaMasterBean.setSlaDurationInDays(slaMaster.getSlaDurationInDays());
		slaMasterBean.setSlaName(slaMaster.getSlaName());
		return slaMasterBean;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the slaDurationInDays
	 */
	public Integer getSlaDurationInDays() {
		return slaDurationInDays;
	}

	/**
	 * @param slaDurationInDays the slaDurationInDays to set
	 */
	public void setSlaDurationInDays(Integer slaDurationInDays) {
		this.slaDurationInDays = slaDurationInDays;
	}

	/**
	 * @return the slaName
	 */
	public String getSlaName() {
		return slaName;
	}

	/**
	 * @param slaName the slaName to set
	 */
	public void setSlaName(String slaName) {
		this.slaName = slaName;
	}
	
	


}
