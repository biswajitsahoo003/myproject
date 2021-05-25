package com.tcl.dias.servicefulfillmentutils.beans;


import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * IpaddrWanv4Address Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class IpaddrWanv4AddressBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer wanv4AddrId;
	private Timestamp endDate;
	private Boolean iscustomerprovided;
	private Boolean issecondary;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private Timestamp startDate;
	private String wanv4Address;
	private boolean isEdited;
	public Integer getWanv4AddrId() {
		return wanv4AddrId;
	}
	public void setWanv4AddrId(Integer wanv4AddrId) {
		this.wanv4AddrId = wanv4AddrId;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public Boolean getIscustomerprovided() {
		return iscustomerprovided;
	}
	public void setIscustomerprovided(Boolean iscustomerprovided) {
		this.iscustomerprovided = iscustomerprovided;
	}
	public Boolean getIssecondary() {
		return issecondary;
	}
	public void setIssecondary(Boolean issecondary) {
		this.issecondary = issecondary;
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
	public String getWanv4Address() {
		return wanv4Address;
	}
	public void setWanv4Address(String wanv4Address) {
		this.wanv4Address = wanv4Address;
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