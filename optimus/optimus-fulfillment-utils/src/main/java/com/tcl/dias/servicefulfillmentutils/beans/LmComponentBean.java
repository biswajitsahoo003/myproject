package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * LmComponent bean class
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class LmComponentBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer lmComponentId;
	private Timestamp endDate;
	private Timestamp lastModifiedDate;
	private String lmOnwlProvider;
	private String modifiedBy;
	private String remarks;
	private Timestamp startDate;
	private Integer version;
	private boolean isEdited;

	private Set<CambiumLastmileBean> cambiumLastmiles;

	
	private Set<RadwinLastmileBean> radwinLastmiles;

	
	private Set<WimaxLastmileBean> wimaxLastmiles;


	public Integer getLmComponentId() {
		return lmComponentId;
	}


	public void setLmComponentId(Integer lmComponentId) {
		this.lmComponentId = lmComponentId;
	}


	public Timestamp getEndDate() {
		return endDate;
	}


	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}


	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}


	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	public String getLmOnwlProvider() {
		return lmOnwlProvider;
	}


	public void setLmOnwlProvider(String lmOnwlProvider) {
		this.lmOnwlProvider = lmOnwlProvider;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public Timestamp getStartDate() {
		return startDate;
	}


	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}


	public Integer getVersion() {
		return version;
	}


	public void setVersion(Integer version) {
		this.version = version;
	}


	public Set<CambiumLastmileBean> getCambiumLastmiles() {
		
		if(cambiumLastmiles==null) {
			cambiumLastmiles=new HashSet<>();
		}
		return cambiumLastmiles;
	}


	public void setCambiumLastmiles(Set<CambiumLastmileBean> cambiumLastmiles) {
		this.cambiumLastmiles = cambiumLastmiles;
	}


	public Set<RadwinLastmileBean> getRadwinLastmiles() {
		
		if(radwinLastmiles==null) {
			radwinLastmiles=new HashSet<>();
		}
		return radwinLastmiles;
	}


	public void setRadwinLastmiles(Set<RadwinLastmileBean> radwinLastmiles) {
		this.radwinLastmiles = radwinLastmiles;
	}


	public Set<WimaxLastmileBean> getWimaxLastmiles() {
		
		if(wimaxLastmiles==null) {
			wimaxLastmiles=new HashSet<>();
		}
		return wimaxLastmiles;
	}


	public void setWimaxLastmiles(Set<WimaxLastmileBean> wimaxLastmiles) {
		this.wimaxLastmiles = wimaxLastmiles;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}