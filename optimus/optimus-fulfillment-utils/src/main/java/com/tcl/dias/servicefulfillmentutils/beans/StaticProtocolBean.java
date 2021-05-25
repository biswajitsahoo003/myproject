package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * StaticProtocol Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class StaticProtocolBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer staticprotocolId;
	private Timestamp endDate;
	private boolean isEdited;
	private Boolean isroutemapEnabled;
	private Boolean isroutemapPreprovisioned;
	private Timestamp lastModifiedDate;
	private String localPreference;
	private String localPreferenceV6;
	private String modifiedBy;
	private String redistributeRoutemapIpv4;
	private Timestamp startDate;

	private InterfaceDetailBean interfaceDetailBean;

	public InterfaceDetailBean getInterfaceDetailBean() {
		return interfaceDetailBean;
	}

	public void setInterfaceDetailBean(InterfaceDetailBean interfaceDetailBean) {
		this.interfaceDetailBean = interfaceDetailBean;
	}

	private Set<PolicyTypeBean> policyTypes;

	private Set<WanStaticRouteBean> wanStaticRoutes;

	public Integer getStaticprotocolId() {
		return staticprotocolId;
	}

	public void setStaticprotocolId(Integer staticprotocolId) {
		this.staticprotocolId = staticprotocolId;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
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

	public String getLocalPreference() {
		return localPreference;
	}

	public void setLocalPreference(String localPreference) {
		this.localPreference = localPreference;
	}

	public String getLocalPreferenceV6() {
		return localPreferenceV6;
	}

	public void setLocalPreferenceV6(String localPreferenceV6) {
		this.localPreferenceV6 = localPreferenceV6;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getRedistributeRoutemapIpv4() {
		return redistributeRoutemapIpv4;
	}

	public void setRedistributeRoutemapIpv4(String redistributeRoutemapIpv4) {
		this.redistributeRoutemapIpv4 = redistributeRoutemapIpv4;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}



	public Set<PolicyTypeBean> getPolicyTypes() {
		
		if(policyTypes==null) {
			policyTypes=new HashSet<>();
		}
		return policyTypes;
	}

	public void setPolicyTypes(Set<PolicyTypeBean> policyTypes) {
		this.policyTypes = policyTypes;
	}

	public Set<WanStaticRouteBean> getWanStaticRoutes() {
		
		if(wanStaticRoutes==null) {
			wanStaticRoutes=new HashSet<>();
		}
		return wanStaticRoutes;
	}

	public void setWanStaticRoutes(Set<WanStaticRouteBean> wanStaticRoutes) {
		this.wanStaticRoutes = wanStaticRoutes;
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