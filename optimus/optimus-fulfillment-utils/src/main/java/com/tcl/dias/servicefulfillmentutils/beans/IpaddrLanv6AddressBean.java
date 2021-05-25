package com.tcl.dias.servicefulfillmentutils.beans;


import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * IpaddrLanv6Address Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class IpaddrLanv6AddressBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer lanv6AddrId;
	private Timestamp endDate;
	private Boolean iscustomerprovided;
	private Boolean issecondary;
	private String lanv6Address;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private Timestamp startDate;
	private boolean isEdited;
	public Integer getLanv6AddrId() {
		return lanv6AddrId;
	}
	public void setLanv6AddrId(Integer lanv6AddrId) {
		this.lanv6AddrId = lanv6AddrId;
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
	public String getLanv6Address() {
		return lanv6Address;
	}
	public void setLanv6Address(String lanv6Address) {
		this.lanv6Address = lanv6Address;
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