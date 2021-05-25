package com.tcl.dias.serviceactivation.rule.engine.bean;

import java.sql.Timestamp;

public class ServiceDetailsBean {
	
	private Integer id;

	private String aluSvcName;

	private String aluSvcid;

	private Integer burstableBw;

	private String burstableBwUnit;

	private String csoSammgrId;

	private String dataTransferCommit;

	private String dataTransferCommitUnit;

	private String description;

	private Byte eligibleForRevision;

	private Timestamp endDate;

	private Byte expediteTerminate;

	private String externalRefid;

	private String intefaceDescSvctag;

	private Byte isIdcService;

	private Byte iscustomConfigReqd;

	private Byte isdowntimeReqd;

	private Byte isrevised;

	private Timestamp lastModifiedDate;

	private String lastmileProvider;

	private String lastmileType;

	private String mgmtType;

	private String modifiedBy;

	private String netpRefid;

	private String oldServiceId;

	private String redundancyRole;

	private String scopeOfMgmt;

	private Integer serviceBandwidth;

	private String serviceBandwidthUnit;

	private String serviceComponenttype;

	private String serviceId;

	private String serviceState;

	private String serviceSubtype;

	private String serviceType;

	private Byte skipDummyConfig;

	private String solutionId;

	private String solutionName;

	private Timestamp startDate;

	private String svclinkRole;

	private String svclinkSrvid;

	private Timestamp trfsDate;

	private Timestamp trfsTriggerDate;

	private Byte triggerNccmOrder;

	private String usageModel;

	private Integer version;

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

	public Integer getBurstableBw() {
		return burstableBw;
	}

	public void setBurstableBw(Integer burstableBw) {
		this.burstableBw = burstableBw;
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

	public Byte getEligibleForRevision() {
		return eligibleForRevision;
	}

	public void setEligibleForRevision(Byte eligibleForRevision) {
		this.eligibleForRevision = eligibleForRevision;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getExpediteTerminate() {
		return expediteTerminate;
	}

	public void setExpediteTerminate(Byte expediteTerminate) {
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

	public Byte getIsIdcService() {
		return isIdcService;
	}

	public void setIsIdcService(Byte isIdcService) {
		this.isIdcService = isIdcService;
	}

	public Byte getIscustomConfigReqd() {
		return iscustomConfigReqd;
	}

	public void setIscustomConfigReqd(Byte iscustomConfigReqd) {
		this.iscustomConfigReqd = iscustomConfigReqd;
	}

	public Byte getIsdowntimeReqd() {
		return isdowntimeReqd;
	}

	public void setIsdowntimeReqd(Byte isdowntimeReqd) {
		this.isdowntimeReqd = isdowntimeReqd;
	}

	public Byte getIsrevised() {
		return isrevised;
	}

	public void setIsrevised(Byte isrevised) {
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

	public Integer getServiceBandwidth() {
		return serviceBandwidth;
	}

	public void setServiceBandwidth(Integer serviceBandwidth) {
		this.serviceBandwidth = serviceBandwidth;
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

	public Byte getSkipDummyConfig() {
		return skipDummyConfig;
	}

	public void setSkipDummyConfig(Byte skipDummyConfig) {
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

	public Byte getTriggerNccmOrder() {
		return triggerNccmOrder;
	}

	public void setTriggerNccmOrder(Byte triggerNccmOrder) {
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
	
	

}
