package com.tcl.dias.servicefulfillmentutils.beans;


import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * IpaddrWanv6Address Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class IpaddrWanv6AddressBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer wanv6AddrId;
	private boolean isEdited;
	private Timestamp endDate;
	private Boolean iscustomerprovided;
	private Boolean issecondary;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private Timestamp startDate;
	private String wanv6Address;
	public Integer getWanv6AddrId() {
		return wanv6AddrId;
	}
	public void setWanv6AddrId(Integer wanv6AddrId) {
		this.wanv6AddrId = wanv6AddrId;
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
	public String getWanv6Address() {
		return wanv6Address;
	}
	public void setWanv6Address(String wanv6Address) {
		this.wanv6Address = wanv6Address;
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