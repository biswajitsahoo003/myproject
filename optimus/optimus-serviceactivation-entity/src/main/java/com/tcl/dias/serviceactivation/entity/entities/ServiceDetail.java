package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 
 * ServiceDetail Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "service_details")
@NamedQuery(name = "ServiceDetail.findAll", query = "SELECT s FROM ServiceDetail s")
public class ServiceDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "sc_service_detail_id")
	private Integer scServiceDetailId;

	@Column(name = "alu_svc_name")
	private String aluSvcName;

	@Column(name = "alu_svcid")
	private String aluSvcid;

	@Column(name = "burstable_bw")
	private Float burstableBw;

	@Column(name = "burstable_bw_unit")
	private String burstableBwUnit;

	@Column(name = "css_sammgr_id")
	private Integer cssSammgrId;

	@Column(name = "data_transfer_commit")
	private String dataTransferCommit;

	@Column(name = "router_make")
	private String routerMake;

	@Column(name = "data_transfer_commit_unit")
	private String dataTransferCommitUnit;

	private String description;

	@Column(name = "eligible_for_revision")
	private Byte eligibleForRevision;

	@Column(name = "end_date")
	private Timestamp endDate;

	@Column(name = "expedite_terminate")
	private Byte expediteTerminate;

	@Column(name = "external_refid")
	private String externalRefid;

	@Column(name = "inteface_desc_svctag")
	private String intefaceDescSvctag;

	@Column(name = "is_idc_service")
	private Byte isIdcService;

	@Column(name = "is_manualpostvalidation_reqd")
	private Byte isManualpostvalidationReqd;

	@Column(name = "iscustom_config_reqd")
	private Byte iscustomConfigReqd;

	@Column(name = "isdowntime_reqd")
	private Byte isdowntimeReqd;
	
	@Column(name = "istxdowntime_reqd")
	private Byte istxdowntimeReqd;

	@Column(name = "isport_changed")
	private Byte isportChanged;

	private Byte isrevised;
	
	@Column(name = "resource_path")
	private String resourcePath;

	@Column(name = "last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name = "lastmile_provider")
	private String lastmileProvider;

	@Column(name = "lastmile_type")
	private String lastmileType;

	@Column(name = "mgmt_type")
	private String mgmtType;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "netp_refid")
	private String netpRefid;

	@Column(name = "old_service_id")
	private String oldServiceId;

	@Column(name = "redundancy_role")
	private String redundancyRole;

	@Column(name = "scope_of_mgmt")
	private String scopeOfMgmt;

	@Column(name = "service_bandwidth")
	private Float serviceBandwidth;

	@Column(name = "service_bandwidth_unit")
	private String serviceBandwidthUnit;

	@Column(name = "service_componenttype")
	private String serviceComponenttype;

	@Column(name = "service_id")
	private String serviceId;

	@Column(name = "service_state",nullable=false)
	private String serviceState;

	@Column(name = "service_subtype")
	private String serviceSubtype;

	@Column(name = "service_type")
	private String serviceType;
	
	@Column(name = "order_sub_category")
	private String orderSubCategory;

	@Column(name = "skip_dummy_config")
	private Byte skipDummyConfig;

	@Column(name = "solution_id")
	private String solutionId;

	@Column(name = "solution_name")
	private String solutionName;

	@Column(name = "start_date")
	private Timestamp startDate;

	@Column(name = "svclink_role")
	private String svclinkRole;

	@Column(name = "svclink_srvid")
	private String svclinkSrvid;

	@Column(name = "trfs_date")
	private Timestamp trfsDate;

	@Column(name = "trfs_trigger_date")
	private Timestamp trfsTriggerDate;

	@Column(name = "trigger_nccm_order")
	private Byte triggerNccmOrder;

	@Column(name = "usage_model")
	private String usageModel;

	private Integer version;

	@Column(name = "address_line1")
	private String addressLine1;

	@Column(name = "address_line2")
	private String addressLine2;

	@Column(name = "address_line3")
	private String addressLine3;

	@Column(name = "pincode")
	private String pincode;

	@Column(name = "country")
	private String country;

	private String city;

	private String state;
	
	@Column(name = "service_order_type")
	private String serviceOrderType;

	@Column(name = "lat")
	private String lat;

	@Column(name = "longi_tude")
	private String longiTude;
	
	@Column(name = "downtime_duration")
	private String downtimeDuration;
	
	private String downtime;
	
	@Column(name = "from_time")
	private String fromTime;
	
	@Column(name = "to_time")
	private String toTime;
	
	@Column(name = "order_type")
	private String orderType;
	
	
	@Column(name = "order_category")
	private String orderCategory;
	
	@Column(name = "product_name")
	private String productName;

	// bi-directional many-to-one association to AluSchedulerPolicy
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<AluSchedulerPolicy> aluSchedulerPolicies;

	// bi-directional many-to-one association to CiscoImportMap
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<CiscoImportMap> ciscoImportMaps;

	// bi-directional many-to-one association to InterfaceProtocolMapping
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<InterfaceProtocolMapping> interfaceProtocolMappings;

	// bi-directional many-to-one association to IpAddressDetail
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<IpAddressDetail> ipAddressDetails;

	// bi-directional many-to-one association to LmComponent
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<LmComponent> lmComponents;

	// bi-directional many-to-one association to Multicasting
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<Multicasting> multicastings;

	// bi-directional many-to-one association to OrderDetail
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "order_details_order_id")
	private OrderDetail orderDetail;

	// bi-directional many-to-one association to ServiceQo
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	@OrderBy("serviceQosId ASC")
	private Set<ServiceQo> serviceQos;

	// bi-directional many-to-one association to Topology
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<Topology> topologies;

	// bi-directional many-to-one association to VpnSolution
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<VpnSolution> vpnSolutions;

	// bi-directional many-to-one association to Vrf
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<Vrf> vrfs;

// bi-directional many-to-one association to VpnSolution
	@OneToMany(mappedBy = "serviceDetail", fetch = FetchType.EAGER)
	private Set<VpnMetatData> vpnMetatDatas;
	
	

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the orderCategory
	 */
	public String getOrderCategory() {
		return orderCategory;
	}

	/**
	 * @param orderCategory the orderCategory to set
	 */
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public ServiceDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAluSvcName() {
		return this.aluSvcName;
	}

	public void setAluSvcName(String aluSvcName) {
		this.aluSvcName = aluSvcName;
	}

	public String getAluSvcid() {
		return this.aluSvcid;
	}

	public void setAluSvcid(String aluSvcid) {
		this.aluSvcid = aluSvcid;
	}

	

	public String getBurstableBwUnit() {
		return this.burstableBwUnit;
	}

	public void setBurstableBwUnit(String burstableBwUnit) {
		this.burstableBwUnit = burstableBwUnit;
	}

	public Integer getCssSammgrId() {
		return this.cssSammgrId;
	}

	public void setCssSammgrId(Integer cssSammgrId) {
		this.cssSammgrId = cssSammgrId;
	}

	public String getDataTransferCommit() {
		return this.dataTransferCommit;
	}

	public void setDataTransferCommit(String dataTransferCommit) {
		this.dataTransferCommit = dataTransferCommit;
	}

	public String getDataTransferCommitUnit() {
		return this.dataTransferCommitUnit;
	}

	public void setDataTransferCommitUnit(String dataTransferCommitUnit) {
		this.dataTransferCommitUnit = dataTransferCommitUnit;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getEligibleForRevision() {
		return this.eligibleForRevision;
	}

	public void setEligibleForRevision(Byte eligibleForRevision) {
		this.eligibleForRevision = eligibleForRevision;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getExpediteTerminate() {
		return this.expediteTerminate;
	}

	public void setExpediteTerminate(Byte expediteTerminate) {
		this.expediteTerminate = expediteTerminate;
	}

	public String getExternalRefid() {
		return this.externalRefid;
	}

	public void setExternalRefid(String externalRefid) {
		this.externalRefid = externalRefid;
	}

	public String getIntefaceDescSvctag() {
		return this.intefaceDescSvctag;
	}

	public void setIntefaceDescSvctag(String intefaceDescSvctag) {
		this.intefaceDescSvctag = intefaceDescSvctag;
	}

	public Byte getIsIdcService() {
		return this.isIdcService;
	}

	public void setIsIdcService(Byte isIdcService) {
		this.isIdcService = isIdcService;
	}

	public Byte getIsManualpostvalidationReqd() {
		return this.isManualpostvalidationReqd;
	}

	public void setIsManualpostvalidationReqd(Byte isManualpostvalidationReqd) {
		this.isManualpostvalidationReqd = isManualpostvalidationReqd;
	}

	public Byte getIscustomConfigReqd() {
		return this.iscustomConfigReqd;
	}

	public void setIscustomConfigReqd(Byte iscustomConfigReqd) {
		this.iscustomConfigReqd = iscustomConfigReqd;
	}

	public Byte getIsdowntimeReqd() {
		return this.isdowntimeReqd;
	}

	public void setIsdowntimeReqd(Byte isdowntimeReqd) {
		this.isdowntimeReqd = isdowntimeReqd;
	}

	public Byte getIsportChanged() {
		return this.isportChanged;
	}

	public void setIsportChanged(Byte isportChanged) {
		this.isportChanged = isportChanged;
	}

	public Byte getIsrevised() {
		return this.isrevised;
	}

	public void setIsrevised(Byte isrevised) {
		this.isrevised = isrevised;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastmileProvider() {
		return this.lastmileProvider;
	}

	public void setLastmileProvider(String lastmileProvider) {
		this.lastmileProvider = lastmileProvider;
	}

	public String getLastmileType() {
		return this.lastmileType;
	}

	public void setLastmileType(String lastmileType) {
		this.lastmileType = lastmileType;
	}

	public String getMgmtType() {
		return this.mgmtType;
	}

	public void setMgmtType(String mgmtType) {
		this.mgmtType = mgmtType;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getNetpRefid() {
		return this.netpRefid;
	}

	public void setNetpRefid(String netpRefid) {
		this.netpRefid = netpRefid;
	}

	public String getOldServiceId() {
		return this.oldServiceId;
	}

	public void setOldServiceId(String oldServiceId) {
		this.oldServiceId = oldServiceId;
	}

	public String getRedundancyRole() {
		return this.redundancyRole;
	}

	public void setRedundancyRole(String redundancyRole) {
		this.redundancyRole = redundancyRole;
	}

	public String getScopeOfMgmt() {
		return this.scopeOfMgmt;
	}

	public void setScopeOfMgmt(String scopeOfMgmt) {
		this.scopeOfMgmt = scopeOfMgmt;
	}



	public String getServiceBandwidthUnit() {
		return this.serviceBandwidthUnit;
	}

	public void setServiceBandwidthUnit(String serviceBandwidthUnit) {
		this.serviceBandwidthUnit = serviceBandwidthUnit;
	}

	public String getServiceComponenttype() {
		return this.serviceComponenttype;
	}

	public void setServiceComponenttype(String serviceComponenttype) {
		this.serviceComponenttype = serviceComponenttype;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceState() {
		return this.serviceState;
	}

	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}

	public String getServiceSubtype() {
		return this.serviceSubtype;
	}

	public void setServiceSubtype(String serviceSubtype) {
		this.serviceSubtype = serviceSubtype;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Byte getSkipDummyConfig() {
		return this.skipDummyConfig;
	}

	public void setSkipDummyConfig(Byte skipDummyConfig) {
		this.skipDummyConfig = skipDummyConfig;
	}

	public String getSolutionId() {
		return this.solutionId;
	}

	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

	public String getSolutionName() {
		return this.solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getSvclinkRole() {
		return this.svclinkRole;
	}

	public void setSvclinkRole(String svclinkRole) {
		this.svclinkRole = svclinkRole;
	}

	public String getSvclinkSrvid() {
		return this.svclinkSrvid;
	}

	public void setSvclinkSrvid(String svclinkSrvid) {
		this.svclinkSrvid = svclinkSrvid;
	}

	public Timestamp getTrfsDate() {
		return this.trfsDate;
	}

	public void setTrfsDate(Timestamp trfsDate) {
		this.trfsDate = trfsDate;
	}

	public Timestamp getTrfsTriggerDate() {
		return this.trfsTriggerDate;
	}

	public void setTrfsTriggerDate(Timestamp trfsTriggerDate) {
		this.trfsTriggerDate = trfsTriggerDate;
	}

	public Byte getTriggerNccmOrder() {
		return this.triggerNccmOrder;
	}

	public void setTriggerNccmOrder(Byte triggerNccmOrder) {
		this.triggerNccmOrder = triggerNccmOrder;
	}

	public String getUsageModel() {
		return this.usageModel;
	}

	public void setUsageModel(String usageModel) {
		this.usageModel = usageModel;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Set<AluSchedulerPolicy> getAluSchedulerPolicies() {
		return this.aluSchedulerPolicies;
	}

	public void setAluSchedulerPolicies(Set<AluSchedulerPolicy> aluSchedulerPolicies) {
		this.aluSchedulerPolicies = aluSchedulerPolicies;
	}

	public AluSchedulerPolicy addAluSchedulerPolicy(AluSchedulerPolicy aluSchedulerPolicy) {
		getAluSchedulerPolicies().add(aluSchedulerPolicy);
		aluSchedulerPolicy.setServiceDetail(this);

		return aluSchedulerPolicy;
	}

	public AluSchedulerPolicy removeAluSchedulerPolicy(AluSchedulerPolicy aluSchedulerPolicy) {
		getAluSchedulerPolicies().remove(aluSchedulerPolicy);
		aluSchedulerPolicy.setServiceDetail(null);

		return aluSchedulerPolicy;
	}

	public Set<CiscoImportMap> getCiscoImportMaps() {
		return this.ciscoImportMaps;
	}

	public void setCiscoImportMaps(Set<CiscoImportMap> ciscoImportMaps) {
		this.ciscoImportMaps = ciscoImportMaps;
	}

	public CiscoImportMap addCiscoImportMap(CiscoImportMap ciscoImportMap) {
		getCiscoImportMaps().add(ciscoImportMap);
		ciscoImportMap.setServiceDetail(this);

		return ciscoImportMap;
	}

	public CiscoImportMap removeCiscoImportMap(CiscoImportMap ciscoImportMap) {
		getCiscoImportMaps().remove(ciscoImportMap);
		ciscoImportMap.setServiceDetail(null);

		return ciscoImportMap;
	}

	public Set<InterfaceProtocolMapping> getInterfaceProtocolMappings() {
		return this.interfaceProtocolMappings;
	}

	public void setInterfaceProtocolMappings(Set<InterfaceProtocolMapping> interfaceProtocolMappings) {
		this.interfaceProtocolMappings = interfaceProtocolMappings;
	}

	public InterfaceProtocolMapping addInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().add(interfaceProtocolMapping);
		interfaceProtocolMapping.setServiceDetail(this);

		return interfaceProtocolMapping;
	}

	public InterfaceProtocolMapping removeInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().remove(interfaceProtocolMapping);
		interfaceProtocolMapping.setServiceDetail(null);

		return interfaceProtocolMapping;
	}

	public Set<IpAddressDetail> getIpAddressDetails() {
		return this.ipAddressDetails;
	}

	public void setIpAddressDetails(Set<IpAddressDetail> ipAddressDetails) {
		this.ipAddressDetails = ipAddressDetails;
	}

	public IpAddressDetail addIpAddressDetail(IpAddressDetail ipAddressDetail) {
		getIpAddressDetails().add(ipAddressDetail);
		ipAddressDetail.setServiceDetail(this);

		return ipAddressDetail;
	}

	public IpAddressDetail removeIpAddressDetail(IpAddressDetail ipAddressDetail) {
		getIpAddressDetails().remove(ipAddressDetail);
		ipAddressDetail.setServiceDetail(null);

		return ipAddressDetail;
	}

	public Set<LmComponent> getLmComponents() {
		return this.lmComponents;
	}

	public void setLmComponents(Set<LmComponent> lmComponents) {
		this.lmComponents = lmComponents;
	}

	public LmComponent addLmComponent(LmComponent lmComponent) {
		getLmComponents().add(lmComponent);
		lmComponent.setServiceDetail(this);

		return lmComponent;
	}

	public LmComponent removeLmComponent(LmComponent lmComponent) {
		getLmComponents().remove(lmComponent);
		lmComponent.setServiceDetail(null);

		return lmComponent;
	}

	public Set<Multicasting> getMulticastings() {
		return this.multicastings;
	}

	public void setMulticastings(Set<Multicasting> multicastings) {
		this.multicastings = multicastings;
	}

	public Multicasting addMulticasting(Multicasting multicasting) {
		getMulticastings().add(multicasting);
		multicasting.setServiceDetail(this);

		return multicasting;
	}

	public Multicasting removeMulticasting(Multicasting multicasting) {
		getMulticastings().remove(multicasting);
		multicasting.setServiceDetail(null);

		return multicasting;
	}

	public OrderDetail getOrderDetail() {
		return this.orderDetail;
	}

	public void setOrderDetail(OrderDetail orderDetail) {
		this.orderDetail = orderDetail;
	}

	public Set<ServiceQo> getServiceQos() {
		return this.serviceQos;
	}

	public void setServiceQos(Set<ServiceQo> serviceQos) {
		this.serviceQos = serviceQos;
	}

	public String getRouterMake() {
		return routerMake;
	}

	public void setRouterMake(String routerMake) {
		this.routerMake = routerMake;
	}

	public ServiceQo addServiceQo(ServiceQo serviceQo) {
		getServiceQos().add(serviceQo);
		serviceQo.setServiceDetail(this);

		return serviceQo;
	}

	public ServiceQo removeServiceQo(ServiceQo serviceQo) {
		getServiceQos().remove(serviceQo);
		serviceQo.setServiceDetail(null);

		return serviceQo;
	}

	public Set<Topology> getTopologies() {
		return this.topologies;
	}

	public void setTopologies(Set<Topology> topologies) {
		this.topologies = topologies;
	}

	public Topology addTopology(Topology topology) {
		getTopologies().add(topology);
		topology.setServiceDetail(this);

		return topology;
	}

	public Topology removeTopology(Topology topology) {
		getTopologies().remove(topology);
		topology.setServiceDetail(null);

		return topology;
	}

	public Set<VpnSolution> getVpnSolutions() {
		return this.vpnSolutions;
	}

	public void setVpnSolutions(Set<VpnSolution> vpnSolutions) {
		this.vpnSolutions = vpnSolutions;
	}

	public VpnSolution addVpnSolution(VpnSolution vpnSolution) {
		getVpnSolutions().add(vpnSolution);
		vpnSolution.setServiceDetail(this);

		return vpnSolution;
	}

	public VpnSolution removeVpnSolution(VpnSolution vpnSolution) {
		getVpnSolutions().remove(vpnSolution);
		vpnSolution.setServiceDetail(null);

		return vpnSolution;
	}

	public Set<Vrf> getVrfs() {
		return this.vrfs;
	}

	public void setVrfs(Set<Vrf> vrfs) {
		this.vrfs = vrfs;
	}

	public Vrf addVrf(Vrf vrf) {
		getVrfs().add(vrf);
		vrf.setServiceDetail(this);

		return vrf;
	}

	public Vrf removeVrf(Vrf vrf) {
		getVrfs().remove(vrf);
		vrf.setServiceDetail(null);

		return vrf;
	}

	public Set<VpnMetatData> getVpnMetatDatas() {
		return vpnMetatDatas;
	}

	public void setVpnMetatDatas(Set<VpnMetatData> vpnMetatDatas) {
		this.vpnMetatDatas = vpnMetatDatas;
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

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLongiTude() {
		return longiTude;
	}

	public void setLongiTude(String longiTude) {
		this.longiTude = longiTude;

	}

	public Integer getScServiceDetailId() {
		return scServiceDetailId;
	}

	public void setScServiceDetailId(Integer scServiceDetailId) {
		this.scServiceDetailId = scServiceDetailId;
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
		if(serviceOrderType==null)return serviceType;
		else return serviceOrderType;
	}

	public void setServiceOrderType(String serviceOrderType) {
		this.serviceOrderType = serviceOrderType;
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
	
	public Byte getIstxdowntimeReqd() {
		return istxdowntimeReqd;
	}

	public void setIstxdowntimeReqd(Byte istxdowntimeReqd) {
		this.istxdowntimeReqd = istxdowntimeReqd;
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

	public String getDowntime() {
		return downtime;
	}

	public void setDowntime(String downtime) {
		this.downtime = downtime;
	}	

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}