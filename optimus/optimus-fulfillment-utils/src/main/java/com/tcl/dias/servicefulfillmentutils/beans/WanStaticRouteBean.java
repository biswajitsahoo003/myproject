package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * WanStaticRoute Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class WanStaticRouteBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer wanstaticrouteId;
	private String advalue;
	private String description;
	private Timestamp endDate;
	private Boolean global;
	private String ipsubnet;
	private Boolean isCewan;
	private Boolean isCpelanStaticroutes;
	private Boolean isCpewanStaticroutes;
	private boolean isEdited;
	private Boolean isPewan;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String nextHopid;
	private String popCommunity;
	private String regionalCommunity;
	private String serviceCommunity;
	private Timestamp startDate;
	public Integer getWanstaticrouteId() {
		return wanstaticrouteId;
	}
	public void setWanstaticrouteId(Integer wanstaticrouteId) {
		this.wanstaticrouteId = wanstaticrouteId;
	}
	public String getAdvalue() {
		return advalue;
	}
	public void setAdvalue(String advalue) {
		this.advalue = advalue;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public Boolean getGlobal() {
		return global;
	}
	public void setGlobal(Boolean global) {
		this.global = global;
	}
	public String getIpsubnet() {
		return ipsubnet;
	}
	public void setIpsubnet(String ipsubnet) {
		this.ipsubnet = ipsubnet;
	}
	public Boolean getIsCewan() {
		return isCewan;
	}
	public void setIsCewan(Boolean isCewan) {
		this.isCewan = isCewan;
	}
	public Boolean getIsCpelanStaticroutes() {
		return isCpelanStaticroutes;
	}
	public void setIsCpelanStaticroutes(Boolean isCpelanStaticroutes) {
		this.isCpelanStaticroutes = isCpelanStaticroutes;
	}
	public Boolean getIsCpewanStaticroutes() {
		return isCpewanStaticroutes;
	}
	public void setIsCpewanStaticroutes(Boolean isCpewanStaticroutes) {
		this.isCpewanStaticroutes = isCpewanStaticroutes;
	}
	public Boolean getIsPewan() {
		return isPewan;
	}
	public void setIsPewan(Boolean isPewan) {
		this.isPewan = isPewan;
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
	public String getNextHopid() {
		return nextHopid;
	}
	public void setNextHopid(String nextHopid) {
		this.nextHopid = nextHopid;
	}
	public String getPopCommunity() {
		return popCommunity;
	}
	public void setPopCommunity(String popCommunity) {
		this.popCommunity = popCommunity;
	}
	public String getRegionalCommunity() {
		return regionalCommunity;
	}
	public void setRegionalCommunity(String regionalCommunity) {
		this.regionalCommunity = regionalCommunity;
	}
	public String getServiceCommunity() {
		return serviceCommunity;
	}
	public void setServiceCommunity(String serviceCommunity) {
		this.serviceCommunity = serviceCommunity;
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