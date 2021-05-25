package com.tcl.dias.nso.dto;

import java.io.Serializable;

import com.tcl.dias.oms.entity.entities.MstProductFamily;

/**
 * 
 * This file contains the MstProductFamilyDto.java class. Dto Class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MstProductFamilyDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String name;

	private Byte status;

	private String termsCondition;

	public MstProductFamilyDto() {
	}
	public MstProductFamilyDto(MstProductFamily mstProductFamily) {
		if (mstProductFamily != null) {
			this.id = mstProductFamily.getId();
			this.name = mstProductFamily.getName();
			this.status = mstProductFamily.getStatus();
			this.id = mstProductFamily.getId();
			this.name = mstProductFamily.getName();
		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTermsCondition() {
		return this.termsCondition;
	}

	public void setTermsCondition(String termsCondition) {
		this.termsCondition = termsCondition;
	}

	/**
	 * toString
	 * @return
	 */
	@Override
	public String toString() {
		return "MstProductFamilyDto [id=" + id + ", name=" + name + ", status=" + status + ", termsCondition="
				+ termsCondition + "]";
	}
	
	

}