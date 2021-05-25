package com.tcl.dias.common.beans;

import java.io.Serializable;

/**
 * This file contains the SLaDetailsBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SLaDetailsBean implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8825545094281819654L;

	private String slaValue;
	
	private String slaTier;
	
	private String name;
	
	private String sltVariant;

	/**
	 * @return the slaValue
	 */
	public String getSlaValue() {
		return slaValue;
	}

	/**
	 * @param slaValue the slaValue to set
	 */
	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

	/**
	 * @return the slaTier
	 */
	public String getSlaTier() {
		return slaTier;
	}

	/**
	 * @param slaTier the slaTier to set
	 */
	public void setSlaTier(String slaTier) {
		this.slaTier = slaTier;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getSltVariant() {
		return sltVariant;
	}

	public void setSltVariant(String sltVariant) {
		this.sltVariant = sltVariant;
	}

	
	
	
}
