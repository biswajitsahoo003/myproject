/**
 * 
 */
package com.tcl.dias.nso.beans;

import java.io.Serializable;
import java.util.Date;

import com.tcl.dias.nso.beans.SlaMasterBean;

/**
 * @author KarMani
 *
 */

public class QuoteSlaBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8994816654530400751L;

	private Integer id;

	private Date slaEndDate;

	private Date slaStartDate;


	private SlaMasterBean slaMaster;
	
	private String slaValue;

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
	 * @return the slaEndDate
	 */
	public Date getSlaEndDate() {
		return slaEndDate;
	}

	/**
	 * @param slaEndDate the slaEndDate to set
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
	 * @param slaStartDate the slaStartDate to set
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
	 * @param slaMaster the slaMaster to set
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
	 * @param slaValue the slaValue to set
	 */
	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}
	
	
	
	
	


}
