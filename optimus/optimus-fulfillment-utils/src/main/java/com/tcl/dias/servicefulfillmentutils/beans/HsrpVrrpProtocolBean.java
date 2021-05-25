package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * HsrpVrrpProtocol Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class HsrpVrrpProtocolBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer hsrpVrrpId;
	private Timestamp endDate;
	private String helloValue;
	private String holdValue;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private boolean isEdited;
	private String priority;
	private String role;
	private Timestamp startDate;
	private String timerUnit;
	private String virtualIpv4Address;
	private String virtualIpv6Address;
	public Integer getHsrpVrrpId() {
		return hsrpVrrpId;
	}
	public void setHsrpVrrpId(Integer hsrpVrrpId) {
		this.hsrpVrrpId = hsrpVrrpId;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public String getHelloValue() {
		return helloValue;
	}
	public void setHelloValue(String helloValue) {
		this.helloValue = helloValue;
	}
	public String getHoldValue() {
		return holdValue;
	}
	public void setHoldValue(String holdValue) {
		this.holdValue = holdValue;
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public String getTimerUnit() {
		return timerUnit;
	}
	public void setTimerUnit(String timerUnit) {
		this.timerUnit = timerUnit;
	}
	public String getVirtualIpv4Address() {
		return virtualIpv4Address;
	}
	public void setVirtualIpv4Address(String virtualIpv4Address) {
		this.virtualIpv4Address = virtualIpv4Address;
	}
	public String getVirtualIpv6Address() {
		return virtualIpv6Address;
	}
	public void setVirtualIpv6Address(String virtualIpv6Address) {
		this.virtualIpv6Address = virtualIpv6Address;
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