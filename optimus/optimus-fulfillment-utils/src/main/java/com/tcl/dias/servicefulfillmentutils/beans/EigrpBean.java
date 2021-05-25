package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * Eigrp Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class EigrpBean implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Integer eigrpProtocolId;
	private String eigrpBwKbps;
	private boolean isEdited;
	private Timestamp endDate;
	private String interfaceDelay;
	private Boolean isredistributeConnectedEnabled;
	private Boolean isredistributeStaticEnabled;
	private Boolean isroutemapEnabled;
	private Boolean isroutemapPreprovisioned;
	private Timestamp lastModifiedDate;
	private String load;
	private String localAsnumber;
	private String modifiedBy;
	private String mtu;
	private String redistributeRoutemapName;
	private String redistributionDelay;
	private String reliability;
	private String remoteAsnumber;
	private String remoteCeAsnumber;
	private Boolean sooRequired;
	private Timestamp startDate;

	private InterfaceDetailBean interfaceDetailBean;

	public InterfaceDetailBean getInterfaceDetailBean() {
		return interfaceDetailBean;
	}

	public void setInterfaceDetailBean(InterfaceDetailBean interfaceDetailBean) {
		this.interfaceDetailBean = interfaceDetailBean;
	}

	public Integer getEigrpProtocolId() {
		return eigrpProtocolId;
	}

	public void setEigrpProtocolId(Integer eigrpProtocolId) {
		this.eigrpProtocolId = eigrpProtocolId;
	}

	public String getEigrpBwKbps() {
		return eigrpBwKbps;
	}

	public void setEigrpBwKbps(String eigrpBwKbps) {
		this.eigrpBwKbps = eigrpBwKbps;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getInterfaceDelay() {
		return interfaceDelay;
	}

	public void setInterfaceDelay(String interfaceDelay) {
		this.interfaceDelay = interfaceDelay;
	}

	public Boolean getIsredistributeConnectedEnabled() {
		return isredistributeConnectedEnabled;
	}

	public void setIsredistributeConnectedEnabled(Boolean isredistributeConnectedEnabled) {
		this.isredistributeConnectedEnabled = isredistributeConnectedEnabled;
	}

	public Boolean getIsredistributeStaticEnabled() {
		return isredistributeStaticEnabled;
	}

	public void setIsredistributeStaticEnabled(Boolean isredistributeStaticEnabled) {
		this.isredistributeStaticEnabled = isredistributeStaticEnabled;
	}

	public Boolean getIsroutemapEnabled() {
		return isroutemapEnabled;
	}

	public void setIsroutemapEnabled(Boolean isroutemapEnabled) {
		this.isroutemapEnabled = isroutemapEnabled;
	}

	public Boolean getIsroutemapPreprovisioned() {
		return isroutemapPreprovisioned;
	}

	public void setIsroutemapPreprovisioned(Boolean isroutemapPreprovisioned) {
		this.isroutemapPreprovisioned = isroutemapPreprovisioned;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	public String getLocalAsnumber() {
		return localAsnumber;
	}

	public void setLocalAsnumber(String localAsnumber) {
		this.localAsnumber = localAsnumber;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getMtu() {
		return mtu;
	}

	public void setMtu(String mtu) {
		this.mtu = mtu;
	}

	public String getRedistributeRoutemapName() {
		return redistributeRoutemapName;
	}

	public void setRedistributeRoutemapName(String redistributeRoutemapName) {
		this.redistributeRoutemapName = redistributeRoutemapName;
	}

	public String getRedistributionDelay() {
		return redistributionDelay;
	}

	public void setRedistributionDelay(String redistributionDelay) {
		this.redistributionDelay = redistributionDelay;
	}

	public String getReliability() {
		return reliability;
	}

	public void setReliability(String reliability) {
		this.reliability = reliability;
	}

	public String getRemoteAsnumber() {
		return remoteAsnumber;
	}

	public void setRemoteAsnumber(String remoteAsnumber) {
		this.remoteAsnumber = remoteAsnumber;
	}

	public String getRemoteCeAsnumber() {
		return remoteCeAsnumber;
	}

	public void setRemoteCeAsnumber(String remoteCeAsnumber) {
		this.remoteCeAsnumber = remoteCeAsnumber;
	}

	public Boolean getSooRequired() {
		return sooRequired;
	}

	public void setSooRequired(Boolean sooRequired) {
		this.sooRequired = sooRequired;
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