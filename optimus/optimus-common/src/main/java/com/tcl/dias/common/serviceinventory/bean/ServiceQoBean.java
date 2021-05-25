package com.tcl.dias.common.serviceinventory.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


/**
 * 
 * ServiceQo Bean - To push few attributes to Service Inventory
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class ServiceQoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String cosPackage;
	private String cosProfile;
	private String cosType;
	private Boolean cosUpdateAction;
	private Timestamp endDate;
	private String flexiCosIdentifier;
	private Boolean isbandwidthApplicable;
	private Boolean isdefaultFc;
	private Boolean isflexicos;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private Boolean ncTraffic;
	private String pirBw;
	private String pirBwUnit;
	private String qosTrafiicMode;
	private Timestamp startDate;
	private String summationOfBw;

	private List<ServiceCosCriteriaBean> serviceCosCriteriaBeans;

	public String getCosPackage() {
		return cosPackage;
	}

	public void setCosPackage(String cosPackage) {
		this.cosPackage = cosPackage;
	}

	public String getCosProfile() {
		return cosProfile;
	}

	public void setCosProfile(String cosProfile) {
		this.cosProfile = cosProfile;
	}

	public String getCosType() {
		return cosType;
	}

	public void setCosType(String cosType) {
		this.cosType = cosType;
	}

	public Boolean getCosUpdateAction() {
		return cosUpdateAction;
	}

	public void setCosUpdateAction(Boolean cosUpdateAction) {
		this.cosUpdateAction = cosUpdateAction;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getFlexiCosIdentifier() {
		return flexiCosIdentifier;
	}

	public void setFlexiCosIdentifier(String flexiCosIdentifier) {
		this.flexiCosIdentifier = flexiCosIdentifier;
	}

	public Boolean getIsbandwidthApplicable() {
		return isbandwidthApplicable;
	}

	public void setIsbandwidthApplicable(Boolean isbandwidthApplicable) {
		this.isbandwidthApplicable = isbandwidthApplicable;
	}

	public Boolean getIsdefaultFc() {
		return isdefaultFc;
	}

	public void setIsdefaultFc(Boolean isdefaultFc) {
		this.isdefaultFc = isdefaultFc;
	}

	public Boolean getIsflexicos() {
		return isflexicos;
	}

	public void setIsflexicos(Boolean isflexicos) {
		this.isflexicos = isflexicos;
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

	public Boolean getNcTraffic() {
		return ncTraffic;
	}

	public void setNcTraffic(Boolean ncTraffic) {
		this.ncTraffic = ncTraffic;
	}

	public String getPirBw() {
		return pirBw;
	}

	public void setPirBw(String pirBw) {
		this.pirBw = pirBw;
	}

	public String getPirBwUnit() {
		return pirBwUnit;
	}

	public void setPirBwUnit(String pirBwUnit) {
		this.pirBwUnit = pirBwUnit;
	}

	public String getQosTrafiicMode() {
		return qosTrafiicMode;
	}

	public void setQosTrafiicMode(String qosTrafiicMode) {
		this.qosTrafiicMode = qosTrafiicMode;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getSummationOfBw() {
		return summationOfBw;
	}

	public void setSummationOfBw(String summationOfBw) {
		this.summationOfBw = summationOfBw;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<ServiceCosCriteriaBean> getServiceCosCriteriaBeans() {
		return serviceCosCriteriaBeans;
	}

	public void setServiceCosCriteriaBeans(List<ServiceCosCriteriaBean> serviceCosCriteriaBeans) {
		this.serviceCosCriteriaBeans = serviceCosCriteriaBeans;
	}
}