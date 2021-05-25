package com.tcl.dias.common.beans;

import java.util.Date;

/**
 * 
 * This file is used as a dto for serviceProviderLegal Entity class
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class ServiceProviderLegalBean {
	
	private Integer id;

	private String country;

	private Date createdTime;

	private String entityName;

	private Byte status;

	private String tpsSfdcCuid;
	
	private String contractingAddressId;
	
	public ServiceProviderLegalBean() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTpsSfdcCuid() {
		return tpsSfdcCuid;
	}

	public void setTpsSfdcCuid(String tpsSfdcCuid) {
		this.tpsSfdcCuid = tpsSfdcCuid;
	}

	public String getContractingAddressId() {
		return contractingAddressId;
	}

	public void setContractingAddressId(String contractingAddressId) {
		this.contractingAddressId = contractingAddressId;
	}
	
	

}
