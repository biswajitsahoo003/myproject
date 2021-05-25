package com.tcl.dias.common.serviceinventory.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RfDumpWirelessOneBean implements Serializable {

	@JsonProperty("device_type")
	private String deviceType;

	@JsonProperty("bs_address")
	private String bsAddress;

	@JsonProperty("bs_mimo_diversity")
	private String bsMimoDiversity;

	@JsonProperty("ss_tower_height")
	private String ssTowerHeight;

	@JsonProperty("provider ")
	private String provider;

	@JsonProperty("aggregation_switch")
	private String aggregationSwitch;

	@JsonProperty("commission_date ")
	private String commissionDate;

	@JsonProperty("backhaul_provider")
	private String backhaulProvider;

	@JsonProperty("bh_offnet_onnet")
	private String bhOffnetOnnet;

	@JsonProperty("state")
	private String state;

	@JsonProperty("customer_address")
	private String customerAddress;

	@JsonProperty("ss_latitude")
	private String ssLatitude;

	@JsonProperty("sr_no")
	private String srNo;

	@JsonProperty("converter_ip")
	private String converterIp;

	@JsonProperty("aggregation_switch_port")
	private String aggregationSwitchPort;

	@JsonProperty("bh_capacity")
	private String bhCapacity;

	@JsonProperty("bs_ethernet_extender")
	private String bsEthernetExtender;

	@JsonProperty("seq_rfc_radwin")
	private String seqRfcRadwin;

	@JsonProperty("service_status")
	private String serviceStatus;

	@JsonProperty("building_height")
	private String buildingHeight;

	@JsonProperty("status")
	private String status;

	@JsonProperty("flag")
	private String flag;

	@JsonProperty("throughput_acceptance")
	private String throughputAcceptance;

	@JsonProperty("date_of_acceptance")
	private String dateOfAcceptance;

	@JsonProperty("city")
	private String city;

	@JsonProperty("latitude")
	private String latitude;

	@JsonProperty("polarisation")
	private String polarisation;

	@JsonProperty("ss_date_acceptance")
	private String ssDateAcceptance;

	@JsonProperty("bh_config_switch_conv")
	private String bhConfigSwitchConv;

	@JsonProperty("mac")
	private String mac;

	@JsonProperty("ss_mac")
	private String ssMac;

	@JsonProperty("bs_ip")
	private String bsIp;

	@JsonProperty("bs_antenna_gain")
	private String bsAntennaGain;

	@JsonProperty("bs_pole_height")
	private String bsPoleHeight;

	@JsonProperty("bs_building_height")
	private String bsBuildingHeight;

	@JsonProperty("vendor")
	private String vendor;

	@JsonProperty("bh_circuit_id")
	private String bhCircuitId;

	@JsonProperty("circuit_id")
	private String circuitId;

	@JsonProperty("ss_antenna_height")
	private String ssAntennaHeight;

	@JsonProperty("ethernet_extender")
	private String ethernetExtender;

	@JsonProperty("antenna_height")
	private String antennaHeight;

	@JsonProperty("bs_antenna_type")
	private String bsAntennaType;

	@JsonProperty("cust_building_height")
	private String custBuildingHeight;

	@JsonProperty("ss_longitude")
	private String ssLongitude;

	@JsonProperty("ethconverter_ip")
	private String ethconverterIp;

	@JsonProperty("bs_longitude")
	private String bsLongitude;

	@JsonProperty("backhaul_type")
	private String backhaulType;

	@JsonProperty("ss_during_accept")
	private String ssDuringAccept;

	@JsonProperty("sector_id")
	private String sectorId;

	@JsonProperty("termination_date")
	private String terminationDate;

	@JsonProperty("hssu_used")
	private String hssuUsed;

	@JsonProperty("antenna_beam_width")
	private String antennaBeamWidth;

	@JsonProperty("bs_name")
	private String bsName;

	@JsonProperty("ss_antenna_gain")
	private String ssAntennaGain;

	@JsonProperty("bts_site_id")
	private String btsSiteId;

	@JsonProperty("customer_name")
	private String customerName;

	@JsonProperty("dl_cinr_during_acceptance")
	private String dlCinrDuringAcceptance;

	@JsonProperty("switch_ip")
	private String switchIp;

	@JsonProperty("bs_latitude")
	private String bsLatitude;

	@JsonProperty("ss_pole_height")
	private String ssPoleHeight;

	@JsonProperty("cable_length")
	private String cableLength;

	@JsonProperty("ss_polarisation")
	private String ssPolarisation;

	@JsonProperty("qos_bw")
	private String qosBw;

	@JsonProperty("longitude")
	private String longitude;

	@JsonProperty("ss_antenna_mount_type")
	private String ssAntennaMountType;

	@JsonProperty("component_id")
	private String componentId;

	@JsonProperty("hssu_port")
	private String hssuPort;

	@JsonProperty("ss_antenna_type")
	private String ssAntennaType;

	@JsonProperty("tower_height")
	private String towerHeight;

	@JsonProperty("lm_type")
	private String lmType;

	@JsonProperty("ss_rssi_acceptance")
	private String ssRssiAcceptance;

	@JsonProperty("bs_polarisation")
	private String bsPolarisation;

	@JsonProperty("bs_tower_height")
	private String bsTowerHeight;

	@JsonProperty("pe_hostname")
	private String peHostname;

	@JsonProperty("bso_ckt_id")
	private String bsoCktId;

	@JsonProperty("ap_ip")
	private String apIp;

	@JsonProperty("bs_cable_length")
	private String bsCableLength;

	@JsonProperty("ss_ethernet_extender")
	private String ssEthernetExtender;

	@JsonProperty("bs_rss_during_acceptance")
	private String bsRssDuringAcceptance;

	@JsonProperty("switch_converter_port")
	private String switchConverterPort;

	@JsonProperty("ss_bs_name")
	private String ssBsName;

	@JsonProperty("structure_type")
	private String structureType;

	@JsonProperty("type_of_order")
	private String typeOfOrder;

	@JsonProperty("pole_height")
	private String poleHeight;

	@JsonProperty("bs_antenna_mount_type")
	private String bsAntennaMountType;

	@JsonProperty("ss_cable_length")
	private String ssCableLength;

	@JsonProperty("bs_mac")
	private String bsMac;

	@JsonProperty("ss_mimo_diversity")
	private String ssMimoDiversity;

	@JsonProperty("dl_rssi_during_acceptance")
	private String dlRssiDuringAcceptance;

	@JsonProperty("bh_bso")
	private String bhBso;

	@JsonProperty("converter_type")
	private String converterType;

	@JsonProperty("pe_ip")
	private String peIp;

	@JsonProperty("sr_nonumber")
	private String srNonumber;

	@JsonProperty("lm_action")
	private String lmAction;

	@JsonProperty("bs_antenna_height")
	private String bsAntennaHeight;

	@JsonProperty("ss_ip")
	private String ssIp;

	@JsonProperty("pop_converter_ip")
	private String popConverterIp;

	@JsonProperty("service_type")
	private String serviceType;

	@JsonProperty("ss_bh_bso")
	private String ssBhBso;

	@JsonProperty("lens_reflector")
	private String lensReflector;

	@JsonProperty("antenna_type")
	private String antennaType;

	@JsonProperty("action_type")
	private String actionType;

	@JsonProperty("Infra_colo_provider_id")
	private String infraColoProviderId;

	@JsonProperty("Infra_colo_provider_name")
	private String infraColoProviderName;

	@JsonProperty("last_updated_by")
	private String lastUpdatedBy;

	@JsonProperty("last_updated_date")
	private String lastUpdatedDate;

	@JsonProperty("task_stage")
	private String taskStage;

	@JsonProperty("sms_no_bts_end")
	private String smsNoBtsEnd;

	@JsonProperty("poe_si_no_bts_end")
	private String poeSiNoBtsEnd;

	@JsonProperty("sms_no_customer_end")
	private String smsNoCustomerEnd;

	@JsonProperty("poe_si_no_customer_end")
	private String poeSiNoCustomerEnd;

	@JsonProperty("phase_neutral")
	private String phaseNetural;

	@JsonProperty("phase_earth")
	private String phaseEarth;

	@JsonProperty("earth_neutral")
	private String earthNeutral;

	@JsonProperty("radwin_server_reach_rf_bts_end")
	private String radwinServerReachRfBtsEnd;

	@JsonProperty("radwin_server_reach_rf_customer_end")
	private String radwinServerReachRfCustomerEnd;

	@JsonProperty("mast_height")
	private String mastHeight;
	// private String poleHeight;
	@JsonProperty("hsu_height_from_ground")
	private String hsuHeightFromGround;

	@JsonProperty("average_mean_sea_level")
	private String averageMeanSeaLevel;

	@JsonProperty("hop_distance")
	private String hopDistance;

	@JsonProperty("i_clam_pole_used_customer_end")
	private String lClamPoleUsedCustomerEnd;

	@JsonProperty("rf_cable_length_customer_end")
	private String rfCableLengthCustomerEnd;

	@JsonProperty("earthing_cable_length_customer_end")
	private String earthingCableLengthCustomerEnd;

	@JsonProperty("no_of_installationkits_customer_end")
	private String noOfInstallationkitsCustomerEnd;

	@JsonProperty("type_of_pole")
	private String typeOfPole;

	@JsonProperty("no_of_weather_proof_installation_kits")
	private String noOfWeatherProofInstallationKits;

	@JsonProperty("no_of_patch_cord_used")
	private String noOfPatchCordUsed;

	@JsonProperty("su_pvc_conduit")
	private String suPvcConduit;

	@JsonProperty("su_serial_number")
	private String suSerialNumber;

	@JsonProperty("cable_type")
	private String cableType;

	@JsonProperty("patch_cord_customer_end")
	private String patchCordCustomerEnd;

	@JsonProperty("rf_connector_type_customer_end")
	private String rfConnectorTypeCustomerEnd;
	// private String antennaBeamWidth;

	@JsonProperty("i_clamp_pole_used_bts_end")
	private String lClampPoleUsedBtsEnd;

	@JsonProperty("rf_cable_length_bts_end")
	private String rfCableLengthBtsEnd;

	@JsonProperty("earthing_cable_length_bts_end")
	private String earthingCableLengthBtsEnd;

	@JsonProperty("power_cable_length_bts_end")
	private String powerCableLengthBtsEnd;

	@JsonProperty("no_of_mcb")
	private String noOfMcb;

	@JsonProperty("no_of_installationkits_bts_end")
	private String noOfInstallationkitsBtsEnd;

	@JsonProperty("btspvc_conduit")
	private String btspvcConduit;

	@JsonProperty("bts_su_serial_number")
	private String btsSuSerialNumber;

	@JsonProperty("patch_cord_bts_end")
	private String patchCordBtsEnd;

	@JsonProperty("rf_connector_type_bts_end")
	private String rfConnectorTypeBtsEnd;

	@JsonProperty("error_message")
	private String errorMessage;

	@JsonProperty("delay_reason")
	private String delayReason;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getBsAddress() {
		return bsAddress;
	}

	public void setBsAddress(String bsAddress) {
		this.bsAddress = bsAddress;
	}

	public String getBsMimoDiversity() {
		return bsMimoDiversity;
	}

	public void setBsMimoDiversity(String bsMimoDiversity) {
		this.bsMimoDiversity = bsMimoDiversity;
	}

	public String getSsTowerHeight() {
		return ssTowerHeight;
	}

	public void setSsTowerHeight(String ssTowerHeight) {
		this.ssTowerHeight = ssTowerHeight;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getAggregationSwitch() {
		return aggregationSwitch;
	}

	public void setAggregationSwitch(String aggregationSwitch) {
		this.aggregationSwitch = aggregationSwitch;
	}

	public String getCommissionDate() {
		return commissionDate;
	}

	public void setCommissionDate(String commissionDate) {
		this.commissionDate = commissionDate;
	}

	public String getBackhaulProvider() {
		return backhaulProvider;
	}

	public void setBackhaulProvider(String backhaulProvider) {
		this.backhaulProvider = backhaulProvider;
	}

	public String getBhOffnetOnnet() {
		return bhOffnetOnnet;
	}

	public void setBhOffnetOnnet(String bhOffnetOnnet) {
		this.bhOffnetOnnet = bhOffnetOnnet;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getSsLatitude() {
		return ssLatitude;
	}

	public void setSsLatitude(String ssLatitude) {
		this.ssLatitude = ssLatitude;
	}

	public String getSrNo() {
		return srNo;
	}

	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}
        public RfDumpWirelessOneBean() {
        }

      

	public String getConverterIp() {
		return converterIp;
	}

	public void setConverterIp(String converterIp) {
		this.converterIp = converterIp;
	}

	public String getAggregationSwitchPort() {
		return aggregationSwitchPort;
	}

	public void setAggregationSwitchPort(String aggregationSwitchPort) {
		this.aggregationSwitchPort = aggregationSwitchPort;
	}

	public String getBhCapacity() {
		return bhCapacity;
	}

	public void setBhCapacity(String bhCapacity) {
		this.bhCapacity = bhCapacity;
	}

	public String getBsEthernetExtender() {
		return bsEthernetExtender;
	}

	public void setBsEthernetExtender(String bsEthernetExtender) {
		this.bsEthernetExtender = bsEthernetExtender;
	}

	public String getSeqRfcRadwin() {
		return seqRfcRadwin;
	}

	public void setSeqRfcRadwin(String seqRfcRadwin) {
		this.seqRfcRadwin = seqRfcRadwin;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getBuildingHeight() {
		return buildingHeight;
	}

	public void setBuildingHeight(String buildingHeight) {
		this.buildingHeight = buildingHeight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getThroughputAcceptance() {
		return throughputAcceptance;
	}

	public void setThroughputAcceptance(String throughputAcceptance) {
		this.throughputAcceptance = throughputAcceptance;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDateOfAcceptance() {
		return dateOfAcceptance;
	}

	public void setDateOfAcceptance(String dateOfAcceptance) {
		this.dateOfAcceptance = dateOfAcceptance;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getPolarisation() {
		return polarisation;
	}

	public void setPolarisation(String polarisation) {
		this.polarisation = polarisation;
	}

	public String getSsDateAcceptance() {
		return ssDateAcceptance;
	}

	public void setSsDateAcceptance(String ssDateAcceptance) {
		this.ssDateAcceptance = ssDateAcceptance;
	}

	public String getBhConfigSwitchConv() {
		return bhConfigSwitchConv;
	}

	public void setBhConfigSwitchConv(String bhConfigSwitchConv) {
		this.bhConfigSwitchConv = bhConfigSwitchConv;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getSsMac() {
		return ssMac;
	}

	public void setSsMac(String ssMac) {
		this.ssMac = ssMac;
	}

	public String getBsIp() {
		return bsIp;
	}

	public void setBsIp(String bsIp) {
		this.bsIp = bsIp;
	}

	public String getBsAntennaGain() {
		return bsAntennaGain;
	}

	public void setBsAntennaGain(String bsAntennaGain) {
		this.bsAntennaGain = bsAntennaGain;
	}

	public String getBsPoleHeight() {
		return bsPoleHeight;
	}

	public void setBsPoleHeight(String bsPoleHeight) {
		this.bsPoleHeight = bsPoleHeight;
	}

	public String getBsBuildingHeight() {
		return bsBuildingHeight;
	}

	public void setBsBuildingHeight(String bsBuildingHeight) {
		this.bsBuildingHeight = bsBuildingHeight;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getBhCircuitId() {
		return bhCircuitId;
	}

	public void setBhCircuitId(String bhCircuitId) {
		this.bhCircuitId = bhCircuitId;
	}

	public String getCircuitId() {
		return circuitId;
	}

	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	public String getSsAntennaHeight() {
		return ssAntennaHeight;
	}

	public void setSsAntennaHeight(String ssAntennaHeight) {
		this.ssAntennaHeight = ssAntennaHeight;
	}

	public String getEthernetExtender() {
		return ethernetExtender;
	}

	public void setEthernetExtender(String ethernetExtender) {
		this.ethernetExtender = ethernetExtender;
	}

	public String getAntennaHeight() {
		return antennaHeight;
	}

	public void setAntennaHeight(String antennaHeight) {
		this.antennaHeight = antennaHeight;
	}

	public String getBsAntennaType() {
		return bsAntennaType;
	}

	public void setBsAntennaType(String bsAntennaType) {
		this.bsAntennaType = bsAntennaType;
	}

	public String getCustBuildingHeight() {
		return custBuildingHeight;
	}

	public void setCustBuildingHeight(String custBuildingHeight) {
		this.custBuildingHeight = custBuildingHeight;
	}

	public String getSsLongitude() {
		return ssLongitude;
	}

	public void setSsLongitude(String ssLongitude) {
		this.ssLongitude = ssLongitude;
	}

	public String getEthconverterIp() {
		return ethconverterIp;
	}

	public void setEthconverterIp(String ethconverterIp) {
		this.ethconverterIp = ethconverterIp;
	}

	public String getBsLongitude() {
		return bsLongitude;
	}

	public void setBsLongitude(String bsLongitude) {
		this.bsLongitude = bsLongitude;
	}

	public String getBackhaulType() {
		return backhaulType;
	}

	public void setBackhaulType(String backhaulType) {
		this.backhaulType = backhaulType;
	}

	public String getSsDuringAccept() {
		return ssDuringAccept;
	}

	public void setSsDuringAccept(String ssDuringAccept) {
		this.ssDuringAccept = ssDuringAccept;
	}

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public String getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(String terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getHssuUsed() {
		return hssuUsed;
	}

	public void setHssuUsed(String hssuUsed) {
		this.hssuUsed = hssuUsed;
	}

	public String getAntennaBeamWidth() {
		return antennaBeamWidth;
	}

	public void setAntennaBeamWidth(String antennaBeamWidth) {
		this.antennaBeamWidth = antennaBeamWidth;
	}

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public String getSsAntennaGain() {
		return ssAntennaGain;
	}

	public void setSsAntennaGain(String ssAntennaGain) {
		this.ssAntennaGain = ssAntennaGain;
	}

	public String getBtsSiteId() {
		return btsSiteId;
	}

	public void setBtsSiteId(String btsSiteId) {
		this.btsSiteId = btsSiteId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDlCinrDuringAcceptance() {
		return dlCinrDuringAcceptance;
	}

	public void setDlCinrDuringAcceptance(String dlCinrDuringAcceptance) {
		this.dlCinrDuringAcceptance = dlCinrDuringAcceptance;
	}

	public String getSwitchIp() {
		return switchIp;
	}

	public void setSwitchIp(String switchIp) {
		this.switchIp = switchIp;
	}

	public String getBsLatitude() {
		return bsLatitude;
	}

	public void setBsLatitude(String bsLatitude) {
		this.bsLatitude = bsLatitude;
	}

	public String getSsPoleHeight() {
		return ssPoleHeight;
	}

	public void setSsPoleHeight(String ssPoleHeight) {
		this.ssPoleHeight = ssPoleHeight;
	}

	public String getCableLength() {
		return cableLength;
	}

	public void setCableLength(String cableLength) {
		this.cableLength = cableLength;
	}

	public String getSsPolarisation() {
		return ssPolarisation;
	}

	public void setSsPolarisation(String ssPolarisation) {
		this.ssPolarisation = ssPolarisation;
	}

	public String getQosBw() {
		return qosBw;
	}

	public void setQosBw(String qosBw) {
		this.qosBw = qosBw;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getSsAntennaMountType() {
		return ssAntennaMountType;
	}

	public void setSsAntennaMountType(String ssAntennaMountType) {
		this.ssAntennaMountType = ssAntennaMountType;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getHssuPort() {
		return hssuPort;
	}

	public void setHssuPort(String hssuPort) {
		this.hssuPort = hssuPort;
	}

	public String getSsAntennaType() {
		return ssAntennaType;
	}

	public void setSsAntennaType(String ssAntennaType) {
		this.ssAntennaType = ssAntennaType;
	}

	public String getTowerHeight() {
		return towerHeight;
	}

	public void setTowerHeight(String towerHeight) {
		this.towerHeight = towerHeight;
	}

	public String getLmType() {
		return lmType;
	}

	public void setLmType(String lmType) {
		this.lmType = lmType;
	}

	public String getSsRssiAcceptance() {
		return ssRssiAcceptance;
	}

	public void setSsRssiAcceptance(String ssRssiAcceptance) {
		this.ssRssiAcceptance = ssRssiAcceptance;
	}

	public String getBsPolarisation() {
		return bsPolarisation;
	}

	public void setBsPolarisation(String bsPolarisation) {
		this.bsPolarisation = bsPolarisation;
	}

	public String getBsTowerHeight() {
		return bsTowerHeight;
	}

	public void setBsTowerHeight(String bsTowerHeight) {
		this.bsTowerHeight = bsTowerHeight;
	}

	public String getPeHostname() {
		return peHostname;
	}

	public void setPeHostname(String peHostname) {
		this.peHostname = peHostname;
	}

	public String getBsoCktId() {
		return bsoCktId;
	}

	public void setBsoCktId(String bsoCktId) {
		this.bsoCktId = bsoCktId;
	}

	public String getApIp() {
		return apIp;
	}

	public void setApIp(String apIp) {
		this.apIp = apIp;
	}

	public String getBsCableLength() {
		return bsCableLength;
	}

	public void setBsCableLength(String bsCableLength) {
		this.bsCableLength = bsCableLength;
	}

	public String getSsEthernetExtender() {
		return ssEthernetExtender;
	}

	public void setSsEthernetExtender(String ssEthernetExtender) {
		this.ssEthernetExtender = ssEthernetExtender;
	}

	public String getBsRssDuringAcceptance() {
		return bsRssDuringAcceptance;
	}

	public void setBsRssDuringAcceptance(String bsRssDuringAcceptance) {
		this.bsRssDuringAcceptance = bsRssDuringAcceptance;
	}

	public String getSwitchConverterPort() {
		return switchConverterPort;
	}

	public void setSwitchConverterPort(String switchConverterPort) {
		this.switchConverterPort = switchConverterPort;
	}

	public String getSsBsName() {
		return ssBsName;
	}

	public void setSsBsName(String ssBsName) {
		this.ssBsName = ssBsName;
	}

	public String getStructureType() {
		return structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}

	public String getTypeOfOrder() {
		return typeOfOrder;
	}

	public void setTypeOfOrder(String typeOfOrder) {
		this.typeOfOrder = typeOfOrder;
	}

	public String getPoleHeight() {
		return poleHeight;
	}

	public void setPoleHeight(String poleHeight) {
		this.poleHeight = poleHeight;
	}

	public String getBsAntennaMountType() {
		return bsAntennaMountType;
	}

	public void setBsAntennaMountType(String bsAntennaMountType) {
		this.bsAntennaMountType = bsAntennaMountType;
	}

	public String getSsCableLength() {
		return ssCableLength;
	}

	public void setSsCableLength(String ssCableLength) {
		this.ssCableLength = ssCableLength;
	}

	public String getBsMac() {
		return bsMac;
	}

	public void setBsMac(String bsMac) {
		this.bsMac = bsMac;
	}

	public String getSsMimoDiversity() {
		return ssMimoDiversity;
	}

	public void setSsMimoDiversity(String ssMimoDiversity) {
		this.ssMimoDiversity = ssMimoDiversity;
	}

	public String getDlRssiDuringAcceptance() {
		return dlRssiDuringAcceptance;
	}

	public void setDlRssiDuringAcceptance(String dlRssiDuringAcceptance) {
		this.dlRssiDuringAcceptance = dlRssiDuringAcceptance;
	}

	public String getBhBso() {
		return bhBso;
	}

	public void setBhBso(String bhBso) {
		this.bhBso = bhBso;
	}

	public String getConverterType() {
		return converterType;
	}

	public void setConverterType(String converterType) {
		this.converterType = converterType;
	}

	public String getPeIp() {
		return peIp;
	}

	public void setPeIp(String peIp) {
		this.peIp = peIp;
	}

	public String getSrNonumber() {
		return srNonumber;
	}

	public void setSrNonumber(String srNonumber) {
		this.srNonumber = srNonumber;
	}

	public String getLmAction() {
		return lmAction;
	}

	public void setLmAction(String lmAction) {
		this.lmAction = lmAction;
	}

	public String getBsAntennaHeight() {
		return bsAntennaHeight;
	}

	public void setBsAntennaHeight(String bsAntennaHeight) {
		this.bsAntennaHeight = bsAntennaHeight;
	}

	public String getSsIp() {
		return ssIp;
	}

	public void setSsIp(String ssIp) {
		this.ssIp = ssIp;
	}

	public String getPopConverterIp() {
		return popConverterIp;
	}

	public void setPopConverterIp(String popConverterIp) {
		this.popConverterIp = popConverterIp;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSsBhBso() {
		return ssBhBso;
	}

	public void setSsBhBso(String ssBhBso) {
		this.ssBhBso = ssBhBso;
	}

	public String getLensReflector() {
		return lensReflector;
	}

	public void setLensReflector(String lensReflector) {
		this.lensReflector = lensReflector;
	}

	public String getAntennaType() {
		return antennaType;
	}

	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

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

	public String getSmsNoBtsEnd() {
		return smsNoBtsEnd;
	}

	public void setSmsNoBtsEnd(String smsNoBtsEnd) {
		this.smsNoBtsEnd = smsNoBtsEnd;
	}

	public String getPoeSiNoBtsEnd() {
		return poeSiNoBtsEnd;
	}

	public void setPoeSiNoBtsEnd(String poeSiNoBtsEnd) {
		this.poeSiNoBtsEnd = poeSiNoBtsEnd;
	}

	public String getSmsNoCustomerEnd() {
		return smsNoCustomerEnd;
	}

	public void setSmsNoCustomerEnd(String smsNoCustomerEnd) {
		this.smsNoCustomerEnd = smsNoCustomerEnd;
	}

	public String getPoeSiNoCustomerEnd() {
		return poeSiNoCustomerEnd;
	}

	public void setPoeSiNoCustomerEnd(String poeSiNoCustomerEnd) {
		this.poeSiNoCustomerEnd = poeSiNoCustomerEnd;
	}

	public String getPhaseNetural() {
		return phaseNetural;
	}

	public void setPhaseNetural(String phaseNetural) {
		this.phaseNetural = phaseNetural;
	}

	public String getPhaseEarth() {
		return phaseEarth;
	}

	public void setPhaseEarth(String phasecarth) {
		this.phaseEarth = phasecarth;
	}

	public String getEarthNeutral() {
		return earthNeutral;
	}

	public void setEarthNeutral(String earthNeutral) {
		this.earthNeutral = earthNeutral;
	}

	public String getRadwinServerReachRfBtsEnd() {
		return radwinServerReachRfBtsEnd;
	}

	public void setRadwinServerReachRfBtsEnd(String radwinServerReachRfBtsEnd) {
		this.radwinServerReachRfBtsEnd = radwinServerReachRfBtsEnd;
	}

	public String getRadwinServerReachRfCustomerEnd() {
		return radwinServerReachRfCustomerEnd;
	}

	public void setRadwinServerReachRfCustomerEnd(String radwinServerReachRfCustomerEnd) {
		this.radwinServerReachRfCustomerEnd = radwinServerReachRfCustomerEnd;
	}

	public String getMastHeight() {
		return mastHeight;
	}

	public void setMastHeight(String mastHeight) {
		this.mastHeight = mastHeight;
	}

	public String getHsuHeightFromGround() {
		return hsuHeightFromGround;
	}

	public void setHsuHeightFromGround(String hsuHeightFromGround) {
		this.hsuHeightFromGround = hsuHeightFromGround;
	}

	public String getAverageMeanSeaLevel() {
		return averageMeanSeaLevel;
	}

	public void setAverageMeanSeaLevel(String averageMeanSeaLevel) {
		this.averageMeanSeaLevel = averageMeanSeaLevel;
	}

	public String getHopDistance() {
		return hopDistance;
	}

	public void setHopDistance(String hopDistance) {
		this.hopDistance = hopDistance;
	}

	public String getlClamPoleUsedCustomerEnd() {
		return lClamPoleUsedCustomerEnd;
	}

	public void setlClamPoleUsedCustomerEnd(String lClamPoleUsedCustomerEnd) {
		this.lClamPoleUsedCustomerEnd = lClamPoleUsedCustomerEnd;
	}

	public String getRfCableLengthCustomerEnd() {
		return rfCableLengthCustomerEnd;
	}

	public void setRfCableLengthCustomerEnd(String rfCableLengthCustomerEnd) {
		this.rfCableLengthCustomerEnd = rfCableLengthCustomerEnd;
	}

	public String getEarthingCableLengthCustomerEnd() {
		return earthingCableLengthCustomerEnd;
	}

	public void setEarthingCableLengthCustomerEnd(String earthingCableLengthCustomerEnd) {
		this.earthingCableLengthCustomerEnd = earthingCableLengthCustomerEnd;
	}

	public String getNoOfInstallationkitsCustomerEnd() {
		return noOfInstallationkitsCustomerEnd;
	}

	public void setNoOfInstallationkitsCustomerEnd(String noOfInstallationkitsCustomerEnd) {
		this.noOfInstallationkitsCustomerEnd = noOfInstallationkitsCustomerEnd;
	}

	public String getTypeOfPole() {
		return typeOfPole;
	}

	public void setTypeOfPole(String typeOfPole) {
		this.typeOfPole = typeOfPole;
	}

	public String getNoOfWeatherProofInstallationKits() {
		return noOfWeatherProofInstallationKits;
	}

	public void setNoOfWeatherProofInstallationKits(String noOfWeatherProofInstallationKits) {
		this.noOfWeatherProofInstallationKits = noOfWeatherProofInstallationKits;
	}

	public String getNoOfPatchCordUsed() {
		return noOfPatchCordUsed;
	}

	public void setNoOfPatchCordUsed(String noOfPatchCordUsed) {
		this.noOfPatchCordUsed = noOfPatchCordUsed;
	}

	public String getSuPvcConduit() {
		return suPvcConduit;
	}

	public void setSuPvcConduit(String suPvcConduit) {
		this.suPvcConduit = suPvcConduit;
	}

	public String getSuSerialNumber() {
		return suSerialNumber;
	}

	public void setSuSerialNumber(String suSerialNumber) {
		this.suSerialNumber = suSerialNumber;
	}

	public String getCableType() {
		return cableType;
	}

	public void setCableType(String cableType) {
		this.cableType = cableType;
	}

	public String getPatchCordCustomerEnd() {
		return patchCordCustomerEnd;
	}

	public void setPatchCordCustomerEnd(String patchCordCustomerEnd) {
		this.patchCordCustomerEnd = patchCordCustomerEnd;
	}

	public String getRfConnectorTypeCustomerEnd() {
		return rfConnectorTypeCustomerEnd;
	}

	public void setRfConnectorTypeCustomerEnd(String rfConnectorTypeCustomerEnd) {
		this.rfConnectorTypeCustomerEnd = rfConnectorTypeCustomerEnd;
	}

	public String getlClampPoleUsedBtsEnd() {
		return lClampPoleUsedBtsEnd;
	}

	public void setlClampPoleUsedBtsEnd(String lClampPoleUsedBtsEnd) {
		this.lClampPoleUsedBtsEnd = lClampPoleUsedBtsEnd;
	}

	public String getRfCableLengthBtsEnd() {
		return rfCableLengthBtsEnd;
	}

	public void setRfCableLengthBtsEnd(String rfCableLengthBtsEnd) {
		this.rfCableLengthBtsEnd = rfCableLengthBtsEnd;
	}

	public String getEarthingCableLengthBtsEnd() {
		return earthingCableLengthBtsEnd;
	}

	public void setEarthingCableLengthBtsEnd(String earthingCableLengthBtsEnd) {
		this.earthingCableLengthBtsEnd = earthingCableLengthBtsEnd;
	}

	public String getPowerCableLengthBtsEnd() {
		return powerCableLengthBtsEnd;
	}

	public void setPowerCableLengthBtsEnd(String powerCableLengthBtsEnd) {
		this.powerCableLengthBtsEnd = powerCableLengthBtsEnd;
	}

	public String getNoOfMcb() {
		return noOfMcb;
	}

	public void setNoOfMcb(String noOfMcb) {
		this.noOfMcb = noOfMcb;
	}

	public String getNoOfInstallationkitsBtsEnd() {
		return noOfInstallationkitsBtsEnd;
	}

	public void setNoOfInstallationkitsBtsEnd(String noOfInstallationkitsBtsEnd) {
		this.noOfInstallationkitsBtsEnd = noOfInstallationkitsBtsEnd;
	}

	public String getBtspvcConduit() {
		return btspvcConduit;
	}

	public void setBtspvcConduit(String btspvcConduit) {
		this.btspvcConduit = btspvcConduit;
	}

	public String getBtsSuSerialNumber() {
		return btsSuSerialNumber;
	}

	public void setBtsSuSerialNumber(String btsSuSerialNumber) {
		this.btsSuSerialNumber = btsSuSerialNumber;
	}

	public String getPatchCordBtsEnd() {
		return patchCordBtsEnd;
	}

	public void setPatchCordBtsEnd(String patchCordBtsEnd) {
		this.patchCordBtsEnd = patchCordBtsEnd;
	}

	public String getRfConnectorTypeBtsEnd() {
		return rfConnectorTypeBtsEnd;
	}

	public void setRfConnectorTypeBtsEnd(String rfConnectorTypeBtsEnd) {
		this.rfConnectorTypeBtsEnd = rfConnectorTypeBtsEnd;
	}

	public String getInfraColoProviderId() {
		return infraColoProviderId;
	}

	public void setInfraColoProviderId(String infraColoProviderId) {
		this.infraColoProviderId = infraColoProviderId;
	}

	public String getInfraColoProviderName() {
		return infraColoProviderName;
	}

	public void setInfraColoProviderName(String infraColoProviderName) {
		this.infraColoProviderName = infraColoProviderName;
	}

}


