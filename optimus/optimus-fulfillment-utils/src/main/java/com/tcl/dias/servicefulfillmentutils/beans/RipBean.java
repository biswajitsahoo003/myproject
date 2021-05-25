package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * Rip Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class RipBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer ripId;
	private Timestamp endDate;
	private String groupName;
	private boolean isEdited;
	private Timestamp lastModifiedDate;
	private String localPreference;
	private String localPreferenceV6;
	private String modifiedBy;
	private Timestamp startDate;
	private String version;

	private InterfaceDetailBean interfaceDetailBean;

	public InterfaceDetailBean getInterfaceDetailBean() {
		return interfaceDetailBean;
	}

	public void setInterfaceDetailBean(InterfaceDetailBean interfaceDetailBean) {
		this.interfaceDetailBean = interfaceDetailBean;
	}

	private Set<PolicyTypeBean> policyTypes;

	public Integer getRipId() {
		return ripId;
	}

	public void setRipId(Integer ripId) {
		this.ripId = ripId;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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