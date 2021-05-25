package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * PrefixlistConfig Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class PrefixlistConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Integer prefixlistId;
	private String action;
	private Timestamp endDate;
	private String geValue;
	private Timestamp lastModifiedDate;
	private String leValue;
	private String modifiedBy;
	private String networkPrefix;
	private Timestamp startDate;
	private boolean isEdited;

	public Integer getPrefixlistId() {
		return prefixlistId;
	}
	public void setPrefixlistId(Integer prefixlistId) {
		this.prefixlistId = prefixlistId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getGeValue() {
		return geValue;
	}
	public void setGeValue(String geValue) {
		this.geValue = geValue;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLeValue() {
		return leValue;
	}
	public void setLeValue(String leValue) {
		this.leValue = leValue;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getNetworkPrefix() {
		return networkPrefix;
	}
	public void setNetworkPrefix(String networkPrefix) {
		this.networkPrefix = networkPrefix;
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