package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * VlanQosProfile Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class VlanQosProfileBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer vlanQosProfileId;
	private String downstreamQosProfile;
	private Timestamp endDate;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private Timestamp startDate;
	private String upstreamQosProfile;
	private Integer vlanId;
	private boolean isEdited;
	public Integer getVlanQosProfileId() {
		return vlanQosProfileId;
	}
	public void setVlanQosProfileId(Integer vlanQosProfileId) {
		this.vlanQosProfileId = vlanQosProfileId;
	}
	public String getDownstreamQosProfile() {
		return downstreamQosProfile;
	}
	public void setDownstreamQosProfile(String downstreamQosProfile) {
		this.downstreamQosProfile = downstreamQosProfile;
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
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public String getUpstreamQosProfile() {
		return upstreamQosProfile;
	}
	public void setUpstreamQosProfile(String upstreamQosProfile) {
		this.upstreamQosProfile = upstreamQosProfile;
	}
	public Integer getVlanId() {
		return vlanId;
	}
	public void setVlanId(Integer vlanId) {
		this.vlanId = vlanId;
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