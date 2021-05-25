package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * RegexAspathConfig Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class RegexAspathConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer regexAspathid;
	private String action;
	private String asPath;
	private Timestamp endDate;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String name;
	private Timestamp startDate;
	private boolean isEdited;
	public Integer getRegexAspathid() {
		return regexAspathid;
	}
	public void setRegexAspathid(Integer regexAspathid) {
		this.regexAspathid = regexAspathid;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAsPath() {
		return asPath;
	}
	public void setAsPath(String asPath) {
		this.asPath = asPath;
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
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
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