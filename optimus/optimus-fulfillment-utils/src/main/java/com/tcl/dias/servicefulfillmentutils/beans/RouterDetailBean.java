package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * RouterDetail Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class RouterDetailBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer routerId;
	private Timestamp endDate;
	private boolean isEdited;
	private String ipv4MgmtAddress;
	private String ipv6MgmtAddress;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String routerHostname;
	private String routerMake;
	private String routerModel;
	private String routerRole;
	private String routerType;
	private Timestamp startDate;
	
	private InterfaceDetailBean interfaceDetailBean;

	public InterfaceDetailBean getInterfaceDetailBean() {
		return interfaceDetailBean;
	}

	public void setInterfaceDetailBean(InterfaceDetailBean interfaceDetailBean) {
		this.interfaceDetailBean = interfaceDetailBean;
	}

	public Integer getRouterId() {
		return routerId;
	}

	public void setRouterId(Integer routerId) {
		this.routerId = routerId;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getIpv4MgmtAddress() {
		return ipv4MgmtAddress;
	}

	public void setIpv4MgmtAddress(String ipv4MgmtAddress) {
		this.ipv4MgmtAddress = ipv4MgmtAddress;
	}

	public String getIpv6MgmtAddress() {
		return ipv6MgmtAddress;
	}

	public void setIpv6MgmtAddress(String ipv6MgmtAddress) {
		this.ipv6MgmtAddress = ipv6MgmtAddress;
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

	public String getRouterHostname() {
		return routerHostname;
	}

	public void setRouterHostname(String routerHostname) {
		this.routerHostname = routerHostname;
	}

	public String getRouterMake() {
		return routerMake;
	}

	public void setRouterMake(String routerMake) {
		this.routerMake = routerMake;
	}

	public String getRouterModel() {
		return routerModel;
	}

	public void setRouterModel(String routerModel) {
		this.routerModel = routerModel;
	}

	public String getRouterRole() {
		return routerRole;
	}

	public void setRouterRole(String routerRole) {
		this.routerRole = routerRole;
	}

	public String getRouterType() {
		return routerType;
	}

	public void setRouterType(String routerType) {
		this.routerType = routerType;
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