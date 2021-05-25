package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * Topology Bean class
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 * 
 */

public class TopologyBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer topologyId;
	private Timestamp endDate;
	private boolean isEdited;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private Integer serviceDetailsId;
	private Timestamp startDate;
	private String topologyName;
	
	private Set<RouterUplinkportBean> routerUplinkports;

	private Set<UniswitchDetailBean> uniswitchDetails;

	public Integer getTopologyId() {
		return topologyId;
	}

	public void setTopologyId(Integer topologyId) {
		this.topologyId = topologyId;
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

	public Integer getServiceDetailsId() {
		return serviceDetailsId;
	}

	public void setServiceDetailsId(Integer serviceDetailsId) {
		this.serviceDetailsId = serviceDetailsId;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getTopologyName() {
		return topologyName;
	}

	public void setTopologyName(String topologyName) {
		this.topologyName = topologyName;
	}

	public Set<RouterUplinkportBean> getRouterUplinkports() {
		if(routerUplinkports==null){
			routerUplinkports = new HashSet<>();
		}
		return routerUplinkports;
	}

	public void setRouterUplinkports(Set<RouterUplinkportBean> routerUplinkports) {
		this.routerUplinkports = routerUplinkports;
	}

	public Set<UniswitchDetailBean> getUniswitchDetails() {
		
		if (uniswitchDetails==null) {
			uniswitchDetails=new HashSet<>();
			
		}
		return uniswitchDetails;
	}

	public void setUniswitchDetails(Set<UniswitchDetailBean> uniswitchDetails) {
		this.uniswitchDetails = uniswitchDetails;
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