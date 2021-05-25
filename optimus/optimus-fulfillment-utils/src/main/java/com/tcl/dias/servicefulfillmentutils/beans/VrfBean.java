package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * Vrf Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class VrfBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer vrfId;
	private Timestamp endDate;
	private Boolean ismultivrf;
	private Boolean isvrfLiteEnabled;
	private Timestamp lastModifiedDate;
	private String mastervrfServiceid;
	private String maxRoutesValue;
	private String modifiedBy;
	private Timestamp startDate;
	private String threshold;
	private String vrfName;
	private String warnOn;
	private String vrfProjectName;
	private String slaveVrfServiceId;

	private boolean isEdited;

	private Set<PolicyTypeBean> policyTypes;

	public Integer getVrfId() {
		return vrfId;
	}

	public void setVrfId(Integer vrfId) {
		this.vrfId = vrfId;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsmultivrf() {
		return ismultivrf;
	}

	public void setIsmultivrf(Boolean ismultivrf) {
		this.ismultivrf = ismultivrf;
	}

	public Boolean getIsvrfLiteEnabled() {
		return isvrfLiteEnabled;
	}

	public void setIsvrfLiteEnabled(Boolean isvrfLiteEnabled) {
		this.isvrfLiteEnabled = isvrfLiteEnabled;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getMastervrfServiceid() {
		return mastervrfServiceid;
	}

	public void setMastervrfServiceid(String mastervrfServiceid) {
		this.mastervrfServiceid = mastervrfServiceid;
	}

	public String getMaxRoutesValue() {
		return maxRoutesValue;
	}

	public void setMaxRoutesValue(String maxRoutesValue) {
		this.maxRoutesValue = maxRoutesValue;
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

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String getVrfName() {
		return vrfName;
	}

	public void setVrfName(String vrfName) {
		this.vrfName = vrfName;
	}

	public String getWarnOn() {
		return warnOn;
	}

	public void setWarnOn(String warnOn) {
		this.warnOn = warnOn;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}
	public String getVrfProjectName() {
		return vrfProjectName;
	}

	public void setVrfProjectName(String vrfProjectName) {
		this.vrfProjectName = vrfProjectName;
	}

	public String getSlaveVrfServiceId() {
		return slaveVrfServiceId;
	}

	public void setSlaveVrfServiceId(String slaveVrfServiceId) {
		this.slaveVrfServiceId = slaveVrfServiceId;
	}
}