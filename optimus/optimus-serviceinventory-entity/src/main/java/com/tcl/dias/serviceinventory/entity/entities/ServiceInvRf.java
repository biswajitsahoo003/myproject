package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the service_inv_rf_data database table.
 * 
 */
@Entity
@Table(name="service_inv_rf_data")
@NamedQuery(name="ServiceInvRf.findAll", query="SELECT s FROM ServiceInvRf s")
public class ServiceInvRf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="action_type", length=45)
	private String actionType;

	@Column(name="aggregation_switch", length=45)
	private String aggregationSwitch;

	@Column(name="aggregation_switch_port", length=45)
	private String aggregationSwitchPort;

	@Column(name="antenna_beam_width", length=45)
	private String antennaBeamWidth;

	@Column(name="antenna_height", length=45)
	private String antennaHeight;

	@Column(name="antenna_type", length=45)
	private String antennaType;

	@Column(name="ap_ip", length=45)
	private String apIp;

	@Column(name="backhaul_provider", length=45)
	private String backhaulProvider;

	@Column(name="backhaul_type", length=45)
	private String backhaulType;

	@Column(name="bh_bso", length=45)
	private String bhBso;

	@Column(name="bh_capacity", length=45)
	private String bhCapacity;

	@Column(name="bh_circuit_id", length=45)
	private String bhCircuitId;

	@Column(name="bh_config_switch_conv", length=45)
	private String bhConfigSwitchConv;

	@Column(name="bh_offnet_onnet", length=45)
	private String bhOffnetOnnet;

	@Column(name="bs_address", length=300)
	private String bsAddress;

	@Column(name="bs_antenna_gain", length=45)
	private String bsAntennaGain;

	@Column(name="bs_antenna_height", length=45)
	private String bsAntennaHeight;

	@Column(name="bs_antenna_mount_type", length=45)
	private String bsAntennaMountType;

	@Column(name="bs_antenna_type", length=45)
	private String bsAntennaType;

	@Column(name="bs_building_height", length=45)
	private String bsBuildingHeight;

	@Column(name="bs_cable_length", length=45)
	private String bsCableLength;

	@Column(name="bs_ethernet_extender", length=45)
	private String bsEthernetExtender;

	@Column(name="bs_ip", length=45)
	private String bsIp;

	@Column(name="bs_latitude", length=45)
	private String bsLatitude;

	@Column(name="bs_longitude", length=45)
	private String bsLongitude;

	@Column(name="bs_mac", length=45)
	private String bsMac;

	@Column(name="bs_mimo_diversity", length=45)
	private String bsMimoDiversity;

	@Column(name="bs_name", length=45)
	private String bsName;

	@Column(name="bs_polarisation", length=45)
	private String bsPolarisation;

	@Column(name="bs_pole_height", length=45)
	private String bsPoleHeight;

	@Column(name="bs_rss_during_acceptance", length=45)
	private String bsRssDuringAcceptance;

	@Column(name="bs_tower_height", length=45)
	private String bsTowerHeight;

	@Column(name="bso_ckt_id", length=45)
	private String bsoCktId;

	@Column(name="bts_site_id", length=45)
	private String btsSiteId;

	@Column(name="building_height", length=45)
	private String buildingHeight;

	@Column(name="cable_length", length=45)
	private String cableLength;

	@Column(name="circuit_id", length=45)
	private String circuitId;

	@Column(length=45)
	private String city;

	@Column(name="commission_date", length=45)
	private String commissionDate;

	@Column(name="component_id", length=45)
	private String componentId;

	@Column(name="converter_ip", length=45)
	private String converterIp;

	@Column(name="converter_type", length=45)
	private String converterType;

	@Column(name="cust_building_height", length=45)
	private String custBuildingHeight;

	@Column(name="customer_address", length=300)
	private String customerAddress;

	@Column(name="customer_name", length=45)
	private String customerName;

	@Column(name="date_of_acceptance", length=45)
	private String dateOfAcceptance;

	@Column(name="device_type", length=45)
	private String deviceType;

	@Column(name="dl_cinr_during_acceptance", length=45)
	private String dlCinrDuringAcceptance;

	@Column(name="dl_rssi_during_acceptance", length=45)
	private String dlRssiDuringAcceptance;

	@Column(name="ethconverter_ip", length=45)
	private String ethconverterIp;

	@Column(name="ethernet_extender", length=45)
	private String ethernetExtender;

	@Column(length=45)
	private String flag;

	@Column(name="hssu_port", length=45)
	private String hssuPort;

	@Column(name="hssu_used", length=45)
	private String hssuUsed;

	@Column(length=45)
	private String latitude;

	@Column(name="lens_reflector", length=45)
	private String lensReflector;

	@Column(name="lm_action", length=45)
	private String lmAction;

	@Column(name="lm_type", length=45)
	private String lmType;

	@Column(length=45)
	private String longitude;

	@Column(length=45)
	private String mac;

	@Column(name="pe_hostname", length=45)
	private String peHostname;

	@Column(name="pe_ip", length=45)
	private String peIp;

	@Column(length=45)
	private String polarisation;

	@Column(name="pole_height", length=45)
	private String poleHeight;

	@Column(name="pop_converter_ip", length=45)
	private String popConverterIp;

	@Column(length=45)
	private String provider;

	@Column(name="qos_bw", length=45)
	private String qosBw;

	@Column(name="sector_id", length=45)
	private String sectorId;

	@Column(name="seq_rfc_radwin", length=45)
	private String seqRfcRadwin;

	@Column(name="service_status", length=45)
	private String serviceStatus;

	@Column(name="service_type", length=45)
	private String serviceType;

	@Column(name="sr_no", length=45)
	private String srNo;

	@Column(name="sr_nonumber", length=45)
	private String srNonumber;

	@Column(name="ss_antenna_gain", length=45)
	private String ssAntennaGain;

	@Column(name="ss_antenna_height", length=45)
	private String ssAntennaHeight;

	@Column(name="ss_antenna_mount_type", length=45)
	private String ssAntennaMountType;

	@Column(name="ss_antenna_type", length=45)
	private String ssAntennaType;

	@Column(name="ss_bh_bso", length=45)
	private String ssBhBso;

	@Column(name="ss_bs_name", length=45)
	private String ssBsName;

	@Column(name="ss_cable_length", length=45)
	private String ssCableLength;

	@Column(name="ss_date_acceptance", length=45)
	private String ssDateAcceptance;

	@Column(name="ss_during_accept", length=45)
	private String ssDuringAccept;

	@Column(name="ss_ethernet_extender", length=45)
	private String ssEthernetExtender;

	@Column(name="ss_ip", length=45)
	private String ssIp;

	@Column(name="ss_latitude", length=45)
	private String ssLatitude;

	@Column(name="ss_longitude", length=45)
	private String ssLongitude;

	@Column(name="ss_mac", length=45)
	private String ssMac;

	@Column(name="ss_mimo_diversity", length=45)
	private String ssMimoDiversity;

	@Column(name="ss_polarisation", length=45)
	private String ssPolarisation;

	@Column(name="ss_pole_height", length=45)
	private String ssPoleHeight;

	@Column(name="ss_rssi_acceptance", length=45)
	private String ssRssiAcceptance;

	@Column(name="ss_tower_height", length=45)
	private String ssTowerHeight;

	@Column(length=45)
	private String state;

	@Column(length=45)
	private String status;

	@Column(name="structure_type", length=45)
	private String structureType;

	@Column(name="switch_converter_port", length=45)
	private String switchConverterPort;

	@Column(name="switch_ip", length=45)
	private String switchIp;

	@Column(name="termination_date", length=45)
	private String terminationDate;

	@Column(name="throughput_acceptance", length=45)
	private String throughputAcceptance;

	@Column(name="tower_height", length=45)
	private String towerHeight;

	@Column(name="type_of_order", length=45)
	private String typeOfOrder;

	@Column(length=45)
	private String vendor;
	
	@Column(name = "service_code")
	private String serviceCode;
	
	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "Infra_colo_provider_id")
	private String infraColoProviderId;

	@Column(name = "Infra_colo_provider_name")
	private String infraColoProviderName;
	
	@Column(name = "last_updated_by")
	private String lastUpdatedBy;

	@Column(name = "last_updated_date")
	private String lastUpdatedDate;

	@Column(name = "task_stage")
	private String taskStage; 

	public ServiceInvRf() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getActionType() {
		return this.actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getAggregationSwitch() {
		return this.aggregationSwitch;
	}

	public void setAggregationSwitch(String aggregationSwitch) {
		this.aggregationSwitch = aggregationSwitch;
	}

	public String getAggregationSwitchPort() {
		return this.aggregationSwitchPort;
	}

	public void setAggregationSwitchPort(String aggregationSwitchPort) {
		this.aggregationSwitchPort = aggregationSwitchPort;
	}

	public String getAntennaBeamWidth() {
		return this.antennaBeamWidth;
	}

	public void setAntennaBeamWidth(String antennaBeamWidth) {
		this.antennaBeamWidth = antennaBeamWidth;
	}

	public String getAntennaHeight() {
		return this.antennaHeight;
	}

	public void setAntennaHeight(String antennaHeight) {
		this.antennaHeight = antennaHeight;
	}

	public String getAntennaType() {
		return this.antennaType;
	}

	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
	}

	public String getApIp() {
		return this.apIp;
	}

	public void setApIp(String apIp) {
		this.apIp = apIp;
	}

	public String getBackhaulProvider() {
		return this.backhaulProvider;
	}

	public void setBackhaulProvider(String backhaulProvider) {
		this.backhaulProvider = backhaulProvider;
	}

	public String getBackhaulType() {
		return this.backhaulType;
	}

	public void setBackhaulType(String backhaulType) {
		this.backhaulType = backhaulType;
	}

	public String getBhBso() {
		return this.bhBso;
	}

	public void setBhBso(String bhBso) {
		this.bhBso = bhBso;
	}

	public String getBhCapacity() {
		return this.bhCapacity;
	}

	public void setBhCapacity(String bhCapacity) {
		this.bhCapacity = bhCapacity;
	}

	public String getBhCircuitId() {
		return this.bhCircuitId;
	}

	public void setBhCircuitId(String bhCircuitId) {
		this.bhCircuitId = bhCircuitId;
	}

	public String getBhConfigSwitchConv() {
		return this.bhConfigSwitchConv;
	}

	public void setBhConfigSwitchConv(String bhConfigSwitchConv) {
		this.bhConfigSwitchConv = bhConfigSwitchConv;
	}

	public String getBhOffnetOnnet() {
		return this.bhOffnetOnnet;
	}

	public void setBhOffnetOnnet(String bhOffnetOnnet) {
		this.bhOffnetOnnet = bhOffnetOnnet;
	}

	public String getBsAddress() {
		return this.bsAddress;
	}

	public void setBsAddress(String bsAddress) {
		this.bsAddress = bsAddress;
	}

	public String getBsAntennaGain() {
		return this.bsAntennaGain;
	}

	public void setBsAntennaGain(String bsAntennaGain) {
		this.bsAntennaGain = bsAntennaGain;
	}

	public String getBsAntennaHeight() {
		return this.bsAntennaHeight;
	}

	public void setBsAntennaHeight(String bsAntennaHeight) {
		this.bsAntennaHeight = bsAntennaHeight;
	}

	public String getBsAntennaMountType() {
		return this.bsAntennaMountType;
	}

	public void setBsAntennaMountType(String bsAntennaMountType) {
		this.bsAntennaMountType = bsAntennaMountType;
	}

	public String getBsAntennaType() {
		return this.bsAntennaType;
	}

	public void setBsAntennaType(String bsAntennaType) {
		this.bsAntennaType = bsAntennaType;
	}

	public String getBsBuildingHeight() {
		return this.bsBuildingHeight;
	}

	public void setBsBuildingHeight(String bsBuildingHeight) {
		this.bsBuildingHeight = bsBuildingHeight;
	}

	public String getBsCableLength() {
		return this.bsCableLength;
	}

	public void setBsCableLength(String bsCableLength) {
		this.bsCableLength = bsCableLength;
	}

	public String getBsEthernetExtender() {
		return this.bsEthernetExtender;
	}

	public void setBsEthernetExtender(String bsEthernetExtender) {
		this.bsEthernetExtender = bsEthernetExtender;
	}

	public String getBsIp() {
		return this.bsIp;
	}

	public void setBsIp(String bsIp) {
		this.bsIp = bsIp;
	}

	public String getBsLatitude() {
		return this.bsLatitude;
	}

	public void setBsLatitude(String bsLatitude) {
		this.bsLatitude = bsLatitude;
	}

	public String getBsLongitude() {
		return this.bsLongitude;
	}

	public void setBsLongitude(String bsLongitude) {
		this.bsLongitude = bsLongitude;
	}

	public String getBsMac() {
		return this.bsMac;
	}

	public void setBsMac(String bsMac) {
		this.bsMac = bsMac;
	}

	public String getBsMimoDiversity() {
		return this.bsMimoDiversity;
	}

	public void setBsMimoDiversity(String bsMimoDiversity) {
		this.bsMimoDiversity = bsMimoDiversity;
	}

	public String getBsName() {
		return this.bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public String getBsPolarisation() {
		return this.bsPolarisation;
	}

	public void setBsPolarisation(String bsPolarisation) {
		this.bsPolarisation = bsPolarisation;
	}

	public String getBsPoleHeight() {
		return this.bsPoleHeight;
	}

	public void setBsPoleHeight(String bsPoleHeight) {
		this.bsPoleHeight = bsPoleHeight;
	}

	public String getBsRssDuringAcceptance() {
		return this.bsRssDuringAcceptance;
	}

	public void setBsRssDuringAcceptance(String bsRssDuringAcceptance) {
		this.bsRssDuringAcceptance = bsRssDuringAcceptance;
	}

	public String getBsTowerHeight() {
		return this.bsTowerHeight;
	}

	public void setBsTowerHeight(String bsTowerHeight) {
		this.bsTowerHeight = bsTowerHeight;
	}

	public String getBsoCktId() {
		return this.bsoCktId;
	}

	public void setBsoCktId(String bsoCktId) {
		this.bsoCktId = bsoCktId;
	}

	public String getBtsSiteId() {
		return this.btsSiteId;
	}

	public void setBtsSiteId(String btsSiteId) {
		this.btsSiteId = btsSiteId;
	}

	public String getBuildingHeight() {
		return this.buildingHeight;
	}

	public void setBuildingHeight(String buildingHeight) {
		this.buildingHeight = buildingHeight;
	}

	public String getCableLength() {
		return this.cableLength;
	}

	public void setCableLength(String cableLength) {
		this.cableLength = cableLength;
	}

	public String getCircuitId() {
		return this.circuitId;
	}

	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCommissionDate() {
		return this.commissionDate;
	}

	public void setCommissionDate(String commissionDate) {
		this.commissionDate = commissionDate;
	}

	public String getComponentId() {
		return this.componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getConverterIp() {
		return this.converterIp;
	}

	public void setConverterIp(String converterIp) {
		this.converterIp = converterIp;
	}

	public String getConverterType() {
		return this.converterType;
	}

	public void setConverterType(String converterType) {
		this.converterType = converterType;
	}

	public String getCustBuildingHeight() {
		return this.custBuildingHeight;
	}

	public void setCustBuildingHeight(String custBuildingHeight) {
		this.custBuildingHeight = custBuildingHeight;
	}

	public String getCustomerAddress() {
		return this.customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDateOfAcceptance() {
		return this.dateOfAcceptance;
	}

	public void setDateOfAcceptance(String dateOfAcceptance) {
		this.dateOfAcceptance = dateOfAcceptance;
	}

	public String getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDlCinrDuringAcceptance() {
		return this.dlCinrDuringAcceptance;
	}

	public void setDlCinrDuringAcceptance(String dlCinrDuringAcceptance) {
		this.dlCinrDuringAcceptance = dlCinrDuringAcceptance;
	}

	public String getDlRssiDuringAcceptance() {
		return this.dlRssiDuringAcceptance;
	}

	public void setDlRssiDuringAcceptance(String dlRssiDuringAcceptance) {
		this.dlRssiDuringAcceptance = dlRssiDuringAcceptance;
	}

	public String getEthconverterIp() {
		return this.ethconverterIp;
	}

	public void setEthconverterIp(String ethconverterIp) {
		this.ethconverterIp = ethconverterIp;
	}

	public String getEthernetExtender() {
		return this.ethernetExtender;
	}

	public void setEthernetExtender(String ethernetExtender) {
		this.ethernetExtender = ethernetExtender;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getHssuPort() {
		return this.hssuPort;
	}

	public void setHssuPort(String hssuPort) {
		this.hssuPort = hssuPort;
	}

	public String getHssuUsed() {
		return this.hssuUsed;
	}

	public void setHssuUsed(String hssuUsed) {
		this.hssuUsed = hssuUsed;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLensReflector() {
		return this.lensReflector;
	}

	public void setLensReflector(String lensReflector) {
		this.lensReflector = lensReflector;
	}

	public String getLmAction() {
		return this.lmAction;
	}

	public void setLmAction(String lmAction) {
		this.lmAction = lmAction;
	}

	public String getLmType() {
		return this.lmType;
	}

	public void setLmType(String lmType) {
		this.lmType = lmType;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMac() {
		return this.mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getPeHostname() {
		return this.peHostname;
	}

	public void setPeHostname(String peHostname) {
		this.peHostname = peHostname;
	}

	public String getPeIp() {
		return this.peIp;
	}

	public void setPeIp(String peIp) {
		this.peIp = peIp;
	}

	public String getPolarisation() {
		return this.polarisation;
	}

	public void setPolarisation(String polarisation) {
		this.polarisation = polarisation;
	}

	public String getPoleHeight() {
		return this.poleHeight;
	}

	public void setPoleHeight(String poleHeight) {
		this.poleHeight = poleHeight;
	}

	public String getPopConverterIp() {
		return this.popConverterIp;
	}

	public void setPopConverterIp(String popConverterIp) {
		this.popConverterIp = popConverterIp;
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getQosBw() {
		return this.qosBw;
	}

	public void setQosBw(String qosBw) {
		this.qosBw = qosBw;
	}

	public String getSectorId() {
		return this.sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public String getSeqRfcRadwin() {
		return this.seqRfcRadwin;
	}

	public void setSeqRfcRadwin(String seqRfcRadwin) {
		this.seqRfcRadwin = seqRfcRadwin;
	}

	public String getServiceStatus() {
		return this.serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSrNo() {
		return this.srNo;
	}

	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}

	public String getSrNonumber() {
		return this.srNonumber;
	}

	public void setSrNonumber(String srNonumber) {
		this.srNonumber = srNonumber;
	}

	public String getSsAntennaGain() {
		return this.ssAntennaGain;
	}

	public void setSsAntennaGain(String ssAntennaGain) {
		this.ssAntennaGain = ssAntennaGain;
	}

	public String getSsAntennaHeight() {
		return this.ssAntennaHeight;
	}

	public void setSsAntennaHeight(String ssAntennaHeight) {
		this.ssAntennaHeight = ssAntennaHeight;
	}

	public String getSsAntennaMountType() {
		return this.ssAntennaMountType;
	}

	public void setSsAntennaMountType(String ssAntennaMountType) {
		this.ssAntennaMountType = ssAntennaMountType;
	}

	public String getSsAntennaType() {
		return this.ssAntennaType;
	}

	public void setSsAntennaType(String ssAntennaType) {
		this.ssAntennaType = ssAntennaType;
	}

	public String getSsBhBso() {
		return this.ssBhBso;
	}

	public void setSsBhBso(String ssBhBso) {
		this.ssBhBso = ssBhBso;
	}

	public String getSsBsName() {
		return this.ssBsName;
	}

	public void setSsBsName(String ssBsName) {
		this.ssBsName = ssBsName;
	}

	public String getSsCableLength() {
		return this.ssCableLength;
	}

	public void setSsCableLength(String ssCableLength) {
		this.ssCableLength = ssCableLength;
	}

	public String getSsDateAcceptance() {
		return this.ssDateAcceptance;
	}

	public void setSsDateAcceptance(String ssDateAcceptance) {
		this.ssDateAcceptance = ssDateAcceptance;
	}

	public String getSsDuringAccept() {
		return this.ssDuringAccept;
	}

	public void setSsDuringAccept(String ssDuringAccept) {
		this.ssDuringAccept = ssDuringAccept;
	}

	public String getSsEthernetExtender() {
		return this.ssEthernetExtender;
	}

	public void setSsEthernetExtender(String ssEthernetExtender) {
		this.ssEthernetExtender = ssEthernetExtender;
	}

	public String getSsIp() {
		return this.ssIp;
	}

	public void setSsIp(String ssIp) {
		this.ssIp = ssIp;
	}

	public String getSsLatitude() {
		return this.ssLatitude;
	}

	public void setSsLatitude(String ssLatitude) {
		this.ssLatitude = ssLatitude;
	}

	public String getSsLongitude() {
		return this.ssLongitude;
	}

	public void setSsLongitude(String ssLongitude) {
		this.ssLongitude = ssLongitude;
	}

	public String getSsMac() {
		return this.ssMac;
	}

	public void setSsMac(String ssMac) {
		this.ssMac = ssMac;
	}

	public String getSsMimoDiversity() {
		return this.ssMimoDiversity;
	}

	public void setSsMimoDiversity(String ssMimoDiversity) {
		this.ssMimoDiversity = ssMimoDiversity;
	}

	public String getSsPolarisation() {
		return this.ssPolarisation;
	}

	public void setSsPolarisation(String ssPolarisation) {
		this.ssPolarisation = ssPolarisation;
	}

	public String getSsPoleHeight() {
		return this.ssPoleHeight;
	}

	public void setSsPoleHeight(String ssPoleHeight) {
		this.ssPoleHeight = ssPoleHeight;
	}

	public String getSsRssiAcceptance() {
		return this.ssRssiAcceptance;
	}

	public void setSsRssiAcceptance(String ssRssiAcceptance) {
		this.ssRssiAcceptance = ssRssiAcceptance;
	}

	public String getSsTowerHeight() {
		return this.ssTowerHeight;
	}

	public void setSsTowerHeight(String ssTowerHeight) {
		this.ssTowerHeight = ssTowerHeight;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStructureType() {
		return this.structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}

	public String getSwitchConverterPort() {
		return this.switchConverterPort;
	}

	public void setSwitchConverterPort(String switchConverterPort) {
		this.switchConverterPort = switchConverterPort;
	}

	public String getSwitchIp() {
		return this.switchIp;
	}

	public void setSwitchIp(String switchIp) {
		this.switchIp = switchIp;
	}

	public String getTerminationDate() {
		return this.terminationDate;
	}

	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getThroughputAcceptance() {
		return this.throughputAcceptance;
	}

	public void setThroughputAcceptance(String throughputAcceptance) {
		this.throughputAcceptance = throughputAcceptance;
	}

	public String getTowerHeight() {
		return this.towerHeight;
	}

	public void setTowerHeight(String towerHeight) {
		this.towerHeight = towerHeight;
	}

	public String getTypeOfOrder() {
		return this.typeOfOrder;
	}

	public void setTypeOfOrder(String typeOfOrder) {
		this.typeOfOrder = typeOfOrder;
	}

	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getInfraColoProviderId() { return infraColoProviderId; }

	public void setInfraColoProviderId(String infraColoProviderId) { this.infraColoProviderId = infraColoProviderId; }

	public String getInfraColoProviderName() { return infraColoProviderName; }

	public void setInfraColoProviderName(String infraColoProviderName) { this.infraColoProviderName = infraColoProviderName; }

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getTaskStage() {
		return taskStage;
	}

	public void setTaskStage(String taskStage) {
		this.taskStage = taskStage;
	}
	
}