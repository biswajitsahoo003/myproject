package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * routerUplinkPort Bean class
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class RouterUplinkportBean implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Integer routerUplinkportId;
	private Timestamp endDate;
	private boolean isEdited;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String physicalPort1Name;
	private String physicalPort2Name;
	private Timestamp startDate;
	private Integer topologyServiceDetailsId;
	public Integer getRouterUplinkportId() {
		return routerUplinkportId;
	}
	public void setRouterUplinkportId(Integer routerUplinkportId) {
		this.routerUplinkportId = routerUplinkportId;
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
	public String getPhysicalPort1Name() {
		return physicalPort1Name;
	}
	public void setPhysicalPort1Name(String physicalPort1Name) {
		this.physicalPort1Name = physicalPort1Name;
	}
	public String getPhysicalPort2Name() {
		return physicalPort2Name;
	}
	public void setPhysicalPort2Name(String physicalPort2Name) {
		this.physicalPort2Name = physicalPort2Name;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public Integer getTopologyServiceDetailsId() {
		return topologyServiceDetailsId;
	}
	public void setTopologyServiceDetailsId(Integer topologyServiceDetailsId) {
		this.topologyServiceDetailsId = topologyServiceDetailsId;
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