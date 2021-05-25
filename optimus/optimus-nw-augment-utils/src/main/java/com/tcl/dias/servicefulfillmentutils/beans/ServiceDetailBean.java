package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceDetails Bean class
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 * 
 */

public class ServiceDetailBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String aluSvcName;
	private String aluSvcid;
	private Float burstableBw;
	private String burstableBwUnit;
	private String csoSammgrId;
	private String dataTransferCommit;
	private String dataTransferCommitUnit;
	private String description;
	private Boolean eligibleForRevision;
	private Timestamp endDate;
	private Boolean expediteTerminate;
	private String externalRefid;
	private String intefaceDescSvctag;
	private Boolean isIdcService;
	private Boolean iscustomConfigReqd;
	private Boolean isdowntimeReqd;
	private Boolean isTxDowntimeReqd;
	private String downtimeDuration;
	private String fromTime;
	private String toTime;
	private Boolean isrevised;
	private Timestamp lastModifiedDate;
	private String lastmileProvider;
	private String lastmileType;
	private String mgmtType;
	private String modifiedBy;
	private String netpRefid;
	private String oldServiceId;
	private String redundancyRole;
	private String scopeOfMgmt;
	private Integer scServiceDetailId;
	private Float serviceBandwidth;
	private String serviceBandwidthUnit;
	private String serviceComponenttype;
	private String serviceId;
	private String serviceState;
	private String serviceSubtype;
	private String serviceType;
	private Boolean skipDummyConfig;
	private String solutionId;
	private String solutionName;
	private Timestamp startDate;
	private String svclinkRole;
	private String svclinkSrvid;
	private Timestamp trfsDate;
	private Timestamp trfsTriggerDate;
	private Boolean triggerNccmOrder;
	private String usageModel;
	private Integer version;
	private String routerMake;
	private boolean isEdited;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String state;
	private String pincode;
	private String country;
	private String lat;
	private String serviceOrderType;
	private Boolean isPortChanged;
	private String longitude;
	private String orderSubCategory;




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAluSvcName() {
		return aluSvcName;
	}

	public void setAluSvcName(String aluSvcName) {
		this.aluSvcName = aluSvcName;
	}

	public String getAluSvcid() {
		return aluSvcid;
	}

	public void setAluSvcid(String aluSvcid) {
		this.aluSvcid = aluSvcid;
	}

	
	public String getBurstableBwUnit() {
		return burstableBwUnit;
	}

	public void setBurstableBwUnit(String burstableBwUnit) {
		this.burstableBwUnit = burstableBwUnit;
	}

	public String getCsoSammgrId() {
		return csoSammgrId;
	}

	public void setCsoSammgrId(String csoSammgrId) {
		this.csoSammgrId = csoSammgrId;
	}

	public String getDataTransferCommit() {
		return dataTransferCommit;
	}

	public void setDataTransferCommit(String dataTransferCommit) {
		this.dataTransferCommit = dataTransferCommit;
	}

	public String getDataTransferCommitUnit() {
		return dataTransferCommitUnit;
	}

	public void setDataTransferCommitUnit(String dataTransferCommitUnit) {
		this.dataTransferCommitUnit = dataTransferCommitUnit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEligibleForRevision() {
		return eligibleForRevision;
	}

	public void setEligibleForRevision(Boolean eligibleForRevision) {
		this.eligibleForRevision = eligibleForRevision;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Boolean getExpediteTerminate() {
		return expediteTerminate;
	}

	public void setExpediteTerminate(Boolean expediteTerminate) {
		this.expediteTerminate = expediteTerminate;
	}

	public String getExternalRefid() {
		return externalRefid;
	}

	public void setExternalRefid(String externalRefid) {
		this.externalRefid = externalRefid;
	}

	public String getIntefaceDescSvctag() {
		return intefaceDescSvctag;
	}

	public void setIntefaceDescSvctag(String intefaceDescSvctag) {
		this.intefaceDescSvctag = intefaceDescSvctag;
	}

	public Boolean getIsIdcService() {
		return isIdcService;
	}

	public void setIsIdcService(Boolean isIdcService) {
		this.isIdcService = isIdcService;
	}

	public Boolean getIscustomConfigReqd() {
		return iscustomConfigReqd;
	}

	public void setIscustomConfigReqd(Boolean iscustomConfigReqd) {
		this.iscustomConfigReqd = iscustomConfigReqd;
	}

	public Boolean getIsdowntimeReqd() {
		return isdowntimeReqd;
	}

	public void setIsdowntimeReqd(Boolean isdowntimeReqd) {
		this.isdowntimeReqd = isdowntimeReqd;
	}

	public Boolean getIsrevised() {
		return isrevised;
	}

	public void setIsrevised(Boolean isrevised) {
		this.isrevised = isrevised;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastmileProvider() {
		return lastmileProvider;
	}

	public void setLastmileProvider(String lastmileProvider) {
		this.lastmileProvider = lastmileProvider;
	}

	public String getLastmileType() {
		return lastmileType;
	}

	public void setLastmileType(String lastmileType) {
		this.lastmileType = lastmileType;
	}

	public String getMgmtType() {
		return mgmtType;
	}

	public void setMgmtType(String mgmtType) {
		this.mgmtType = mgmtType;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getNetpRefid() {
		return netpRefid;
	}

	public void setNetpRefid(String netpRefid) {
		this.netpRefid = netpRefid;
	}

	public String getOldServiceId() {
		return oldServiceId;
	}

	public void setOldServiceId(String oldServiceId) {
		this.oldServiceId = oldServiceId;
	}

	public String getRedundancyRole() {
		return redundancyRole;
	}

	public void setRedundancyRole(String redundancyRole) {
		this.redundancyRole = redundancyRole;
	}

	public String getScopeOfMgmt() {
		return scopeOfMgmt;
	}

	public void setScopeOfMgmt(String scopeOfMgmt) {
		this.scopeOfMgmt = scopeOfMgmt;
	}



	public String getServiceBandwidthUnit() {
		return serviceBandwidthUnit;
	}

	public void setServiceBandwidthUnit(String serviceBandwidthUnit) {
		this.serviceBandwidthUnit = serviceBandwidthUnit;
	}

	public String getServiceComponenttype() {
		return serviceComponenttype;
	}

	public void setServiceComponenttype(String serviceComponenttype) {
		this.serviceComponenttype = serviceComponenttype;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceState() {
		return serviceState;
	}

	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}

	public String getServiceSubtype() {
		return serviceSubtype;
	}

	public void setServiceSubtype(String serviceSubtype) {
		this.serviceSubtype = serviceSubtype;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Boolean getSkipDummyConfig() {
		return skipDummyConfig;
	}

	public void setSkipDummyConfig(Boolean skipDummyConfig) {
		this.skipDummyConfig = skipDummyConfig;
	}

	public String getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getSvclinkRole() {
		return svclinkRole;
	}

	public void setSvclinkRole(String svclinkRole) {
		this.svclinkRole = svclinkRole;
	}

	public String getSvclinkSrvid() {
		return svclinkSrvid;
	}

	public void setSvclinkSrvid(String svclinkSrvid) {
		this.svclinkSrvid = svclinkSrvid;
	}

	public Timestamp getTrfsDate() {
		return trfsDate;
	}

	public void setTrfsDate(Timestamp trfsDate) {
		this.trfsDate = trfsDate;
	}

	public Timestamp getTrfsTriggerDate() {
		return trfsTriggerDate;
	}

	public void setTrfsTriggerDate(Timestamp trfsTriggerDate) {
		this.trfsTriggerDate = trfsTriggerDate;
	}

	public Boolean getTriggerNccmOrder() {
		return triggerNccmOrder;
	}

	public void setTriggerNccmOrder(Boolean triggerNccmOrder) {
		this.triggerNccmOrder = triggerNccmOrder;
	}

	public String getUsageModel() {
		return usageModel;
	}

	public void setUsageModel(String usageModel) {
		this.usageModel = usageModel;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getRouterMake() {
		return routerMake;
	}

	public void setRouterMake(String routerMake) {
		this.routerMake = routerMake;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getServiceOrderType() {
		return serviceOrderType;
	}

	public void setServiceOrderType(String serviceOrderType) {
		this.serviceOrderType = serviceOrderType;
	}

	public Boolean getIdcService() {
		return isIdcService;
	}

	public void setIdcService(Boolean idcService) {
		isIdcService = idcService;
	}

	public Boolean getPortChanged() {
		return isPortChanged;
	}

	public void setPortChanged(Boolean portChanged) {
		isPortChanged = portChanged;
	}

	public Float getBurstableBw() {
		return burstableBw;
	}

	public void setBurstableBw(Float burstableBw) {
		this.burstableBw = burstableBw;
	}

	public Float getServiceBandwidth() {
		return serviceBandwidth;
	}

	public void setServiceBandwidth(Float serviceBandwidth) {
		this.serviceBandwidth = serviceBandwidth;
	}

	public Boolean getIsTxDowntimeReqd() {
		return isTxDowntimeReqd;
	}

	public void setIsTxDowntimeReqd(Boolean isTxDowntimeReqd) {
		this.isTxDowntimeReqd = isTxDowntimeReqd;
	}

	public String getDowntimeDuration() {
		return downtimeDuration;
	}

	public void setDowntimeDuration(String downtimeDuration) {
		this.downtimeDuration = downtimeDuration;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Integer getScServiceDetailId() {
		return scServiceDetailId;
	}

	public void setScServiceDetailId(Integer scServiceDetailId) {
		this.scServiceDetailId = scServiceDetailId;
	}

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}
}