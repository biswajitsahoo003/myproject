package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * ServiceQo Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class ServiceQoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer serviceQosId;
	private String cosPackage;
	private String cosProfile;
	private String cosType;
	private String cosUpdateAction;
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
	private String qosPolicyname;
	private String childqosPolicyname;

	private boolean isEdited;

	private List<ServiceCosCriteriaBean> serviceCosCriterias;

	public Integer getServiceQosId() {
		return serviceQosId;
	}

	public void setServiceQosId(Integer serviceQosId) {
		this.serviceQosId = serviceQosId;
	}

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

	public String getCosUpdateAction() {
		return cosUpdateAction;
	}

	public void setCosUpdateAction(String cosUpdateAction) {
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

	public List<ServiceCosCriteriaBean> getServiceCosCriterias() {

		if(serviceCosCriterias == null){
			serviceCosCriterias = new ArrayList<>();
		}

		return serviceCosCriterias;
	}

	public void setServiceCosCriterias(List<ServiceCosCriteriaBean> serviceCosCriterias) {
		this.serviceCosCriterias = serviceCosCriterias;
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

	public String getQosPolicyname() {
		return qosPolicyname;
	}

	public void setQosPolicyname(String qosPolicyname) {
		this.qosPolicyname = qosPolicyname;
	}

	public String getChildqosPolicyname() {
		return childqosPolicyname;
	}

	public void setChildqosPolicyname(String childqosPolicyname) {
		this.childqosPolicyname = childqosPolicyname;
	}
}