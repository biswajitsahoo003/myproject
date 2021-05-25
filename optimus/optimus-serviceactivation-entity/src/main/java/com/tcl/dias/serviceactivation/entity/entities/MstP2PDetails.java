package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mst_p2p_details")
@NamedQuery(name = "MstP2PDetails.findAll", query = "SELECT m FROM MstP2PDetails m")
public class MstP2PDetails implements Serializable {
		private static final long serialVersionUID = 1L;
	
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private int id;
		private String city;
		private String state;
		private String region;
		@Column(name = "service_id")
		private String serviceCode;
		@Column(name = "site_id")
		private String siteId;
		@Column(name = "circuit_type")
		private String circuitType;
		@Column(name = "customer_name")
		private String customerName;
		@Column(name = "bs_address")
		private String bsAddress;
		@Column(name = "bs_name")
		private String bsName;
		private String qos;
		private String latitude;
		private String longitude;
		@Column(name = "mimo_diversity")
		private String mimoDiversity;
		@Column(name = "antenna_height")
		private String antennaHeight;
		private String polarization;
		@Column(name = "antenna_type")
		private String antennaType;
		@Column(name = "antenna_gain")
		private String antennaGain;
		@Column(name = "antenna_mount_type")
		private String antennaMountType;
		@Column(name = "ethernet_extender")
		private String ethernetExtender;
		@Column(name = "building_height")
		private String buildingHeight;
		@Column(name = "pole_height")
		private String poleHeight;
		@Column(name = "cable_length")
		private String cableLength;
		@Column(name = "rssi_during_acceptance")
		private String rssiDuringAcceptance;
		@Column(name = "throughput_during_acceptance")
		private String throughputDuringAcceptance;
		@Column(name = "date_of_acceptance")
		private String dateOfAcceptance;
		@Column(name = "bh_bso")
		private String bhBso;
		private String ip;
		private String mac;
		@Column(name = "hssu_used")
		private String hssuUsed;
		@Column(name = "hssu_port")
		private String hssuPort;
		@Column(name = "bs_switch_ip")
		private String bsSwitchIp;
		@Column(name = "aggregation_switch")
		private String aggregationSwitch;
		@Column(name = "aggregation_switch_port")
		private String  aggregationSwitchPort;
		@Column(name = "bs_converter_ip")
		private String bsConverterIp;
		@Column(name = "pop_converter_ip")
		private String popConverterIp;
		@Column(name = "converter_type")
		private String converterType;
		@Column(name = "bh_config_switch_converter")
		private String bhConfigSwitchConverter;
		@Column(name = "switch_converter_port")
		private String switchConverterPort;
		@Column(name = "bh_capacity")
		private String bhCapacity;
		@Column(name = "bh_offnet_onnet")
		private String bhOffnetOnnet;
		@Column(name = "backhaul_type")
		private String backhaulType;
		@Column(name = "bso_circuit_id")
		private String bsoCircuitId;
		@Column(name = "pe_hostname")
		private String peHostname;
		@Column(name = "pe_ip")
		private String peIp;
		@Column(name = "ss_city")
		private String ssCity;
		@Column(name = "ss_state")
		private String ssState;
		@Column(name = "ss_circuit_id")
		private String ssCircuitId;
		@Column(name = "ss_customer_name")
		private String ssCustomerName;
		@Column(name = "ss_customer_address")
		private String ssCustomerAddress;
		@Column(name = "ss_bs_name")
		private String ssBsName;
		@Column(name = "ss_qos")
		private String ssQos;
		@Column(name = "ss_latitude")
		private String ssLatitude;
		@Column(name = "ss_longitude")
		private String ssLongitude;
		@Column(name = "ss_antenna_height")
		private String ssAntennaHeight;
		@Column(name = "ss_polarization")
		private String ssPolarization;
		@Column(name = "ss_antenna_type")
		private String ssAntennaType;
		@Column(name = "ss_antenna_mount_type")
		private String ssAntennaMountType;
		@Column(name = "ss_ethernet_extender")
		private String ssEthernetExtender;
		@Column(name = "ss_building_height")
		private String ssBuildingHeight;
		@Column(name = "ss_pole_height")
		private String ssPoleHeight;
		@Column(name = "ss_cable_length")
		private String ssCableLength;
		@Column(name = "ss_rssi_during_acceptance")
		private String ssRssiDuringAcceptance;
		@Column(name = "ss_throughput_during_acceptance")
		private String ssThroughputDuringAcceptance;
		@Column(name = "ss_date_of_acceptance")
		private String ssDateOfAcceptance;
		@Column(name = "ss_bh_bso")
		private String ssBhBso;
		@Column(name = "ss_ip")
		private String ssIp;
		@Column(name = "ss_mac")
		private String ssMac;
		@Column(name = "near_end_product_type")
		private String nearEndProductType;
		@Column(name = "near_end_bs_ssid")
		private String nearEndEsSsid;
		@Column(name = "near_end_master_slave")
		private String nearEndMasterSlave;
		@Column(name = "near_end_frequency")
		private String nearEndFrequency;
		@Column(name = "near_end_uas")
		private String nearEndUas;
		@Column(name = "near_end_rssi")
		private String nearEndRssi;
		@Column(name = "near_end_estimated_throughput")
		private String nearEndEstimatedThroughput;
		@Column(name = "near_end_utilisation_ul")
		private String nearEndUtilisationUl;
		@Column(name = "near_end_utilisation_dl")
		private String nearEndUtilisationDl;
		@Column(name = "near_end_uptime")
		private String nearEndUptime;
		@Column(name = "near_end_link_distance")
		private String nearEndLinkDistance;
		@Column(name = "near_end_cbw")
		private String nearEndCbw;
		@Column(name = "near_end_latency")
		private String nearEndLatency;
		@Column(name = "near_end_pd")
		private String nearEndPd;
		@Column(name = "near_end_auto_negotiation")
		private String nearEndAutoNegotiation;
		@Column(name = "near_end_duplex")
		private String nearEndDuplex;
		@Column(name = "near_end_speed")
		private String nearEndSpeed;
		@Column(name = "near_end_link")
		private String nearEndLink;
		@Column(name = "near_end_idu_s_n")
		private String nearEndIduSN;
		@Column(name = "near_end_odu_s_n")
		private String nearEndOduSN;
		@Column(name = "near_end_crc_errors")
		private String nearEndCRCErrors;
		@Column(name = "near_end_sw_version")
		private String nearEndSWVersion;
		@Column(name = "near_end_rssi_min")
		private String nearEndRSSIMin;
		@Column(name = "near_end_rssi_max")
		private String nearEndRSSIMax;
		@Column(name = "far_end_product_type")
		private String farEndProductType;
		@Column(name = "far_end_ssid")
		private String farEndSsid;
		@Column(name = "far_end_master_slave")
		private String farEndMasterSlave;
		@Column(name = "far_end_frequency")
		private String farEndFrequency;
		@Column(name = "far_end_uas")
		private String farEndUas;
		@Column(name = "far_end_rssi")
		private String farEndRssi;
		@Column(name = "far_end_estimated_throughput")
		private String farEndEstimatedThroughput;
		@Column(name = "far_end_utilisation_ul")
		private String farEndUtilisationUl;
		@Column(name = "far_end_utilisation_dl")
		private String farEndUtilisationDl;
		@Column(name = "far_end_uptime")
		private String farEndUptime;
		@Column(name = "far_end_link_distance")
		private String farEndLinkDistance;
		@Column(name = "far_end_cbw")
		private String farEndCbw;
		@Column(name = "far_end_latency")
		private String farEndLatency;
		@Column(name = "far_end_pd")
		private String farEndPd;
		@Column(name = "far_end_auto_negotiation")
		private String farEndAutoNegotiation;
		@Column(name = "far_end_duplex")
		private String farEndDuplex;
		@Column(name = "far_end_speed")
		private String farEndSpeed;
		@Column(name = "far_end_link")
		private String farEndLink;
		@Column(name = "far_end_idu_s_n")
		private String farEndIduSN;
		@Column(name = "far_end_odu_s_n")
		private String farEndOduSN;
		@Column(name = "far_end_crc_errors")
		private String farEndCRCErrors;
		@Column(name = "far_end_sw_version")
		private String farEndSWVersion;
		@Column(name = "far_end_rssi_max")
		private String farEndRssiMax;
		@Column(name = "far_end_rssi_min")
		private String farEndRssiMin;
		@Column(name = "is_active")
		private String isActive;
		@Column(name = "last_modified_by")
		private String lastModifiedBy;
		@Column(name = "last_modified_date")
		private String lastModifiedDate;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
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
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getServiceCode() {
			return serviceCode;
		}
		public void setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
		}
		public String getSiteId() {
			return siteId;
		}
		public void setSiteId(String siteId) {
			this.siteId = siteId;
		}
		public String getCircuitType() {
			return circuitType;
		}
		public void setCircuitType(String circuitType) {
			this.circuitType = circuitType;
		}
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		public String getBsAddress() {
			return bsAddress;
		}
		public void setBsAddress(String bsAddress) {
			this.bsAddress = bsAddress;
		}
		public String getBsName() {
			return bsName;
		}
		public void setBsName(String bsName) {
			this.bsName = bsName;
		}
		public String getQos() {
			return qos;
		}
		public void setQos(String qos) {
			this.qos = qos;
		}
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public String getMimoDiversity() {
			return mimoDiversity;
		}
		public void setMimoDiversity(String mimoDiversity) {
			this.mimoDiversity = mimoDiversity;
		}
		public String getAntennaHeight() {
			return antennaHeight;
		}
		public void setAntennaHeight(String antennaHeight) {
			this.antennaHeight = antennaHeight;
		}
		public String getPolarization() {
			return polarization;
		}
		public void setPolarization(String polarization) {
			this.polarization = polarization;
		}
		public String getAntennaType() {
			return antennaType;
		}
		public void setAntennaType(String antennaType) {
			this.antennaType = antennaType;
		}
		public String getAntennaGain() {
			return antennaGain;
		}
		public void setAntennaGain(String antennaGain) {
			this.antennaGain = antennaGain;
		}
		public String getAntennaMountType() {
			return antennaMountType;
		}
		public void setAntennaMountType(String antennaMountType) {
			this.antennaMountType = antennaMountType;
		}
		public String getEthernetExtender() {
			return ethernetExtender;
		}
		public void setEthernetExtender(String ethernetExtender) {
			this.ethernetExtender = ethernetExtender;
		}
		public String getBuildingHeight() {
			return buildingHeight;
		}
		public void setBuildingHeight(String buildingHeight) {
			this.buildingHeight = buildingHeight;
		}
		public String getPoleHeight() {
			return poleHeight;
		}
		public void setPoleHeight(String poleHeight) {
			this.poleHeight = poleHeight;
		}
		public String getCableLength() {
			return cableLength;
		}
		public void setCableLength(String cableLength) {
			this.cableLength = cableLength;
		}
		public String getRssiDuringAcceptance() {
			return rssiDuringAcceptance;
		}
		public void setRssiDuringAcceptance(String rssiDuringAcceptance) {
			this.rssiDuringAcceptance = rssiDuringAcceptance;
		}
		public String getThroughputDuringAcceptance() {
			return throughputDuringAcceptance;
		}
		public void setThroughputDuringAcceptance(String throughputDuringAcceptance) {
			this.throughputDuringAcceptance = throughputDuringAcceptance;
		}
		public String getDateOfAcceptance() {
			return dateOfAcceptance;
		}
		public void setDateOfAcceptance(String dateOfAcceptance) {
			this.dateOfAcceptance = dateOfAcceptance;
		}
		public String getBhBso() {
			return bhBso;
		}
		public void setBhBso(String bhBso) {
			this.bhBso = bhBso;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getMac() {
			return mac;
		}
		public void setMac(String mac) {
			this.mac = mac;
		}
		public String getHssuUsed() {
			return hssuUsed;
		}
		public void setHssuUsed(String hssuUsed) {
			this.hssuUsed = hssuUsed;
		}
		public String getHssuPort() {
			return hssuPort;
		}
		public void setHssuPort(String hssuPort) {
			this.hssuPort = hssuPort;
		}
		public String getBsSwitchIp() {
			return bsSwitchIp;
		}
		public void setBsSwitchIp(String bsSwitchIp) {
			this.bsSwitchIp = bsSwitchIp;
		}
		public String getAggregationSwitch() {
			return aggregationSwitch;
		}
		public void setAggregationSwitch(String aggregationSwitch) {
			this.aggregationSwitch = aggregationSwitch;
		}
		public String getAggregationSwitchPort() {
			return aggregationSwitchPort;
		}
		public void setAggregationSwitchPort(String aggregationSwitchPort) {
			this.aggregationSwitchPort = aggregationSwitchPort;
		}
		public String getBsConverterIp() {
			return bsConverterIp;
		}
		public void setBsConverterIp(String bsConverterIp) {
			this.bsConverterIp = bsConverterIp;
		}
		public String getPopConverterIp() {
			return popConverterIp;
		}
		public void setPopConverterIp(String popConverterIp) {
			this.popConverterIp = popConverterIp;
		}
		public String getConverterType() {
			return converterType;
		}
		public void setConverterType(String converterType) {
			this.converterType = converterType;
		}
		public String getBhConfigSwitchConverter() {
			return bhConfigSwitchConverter;
		}
		public void setBhConfigSwitchConverter(String bhConfigSwitchConverter) {
			this.bhConfigSwitchConverter = bhConfigSwitchConverter;
		}
		public String getSwitchConverterPort() {
			return switchConverterPort;
		}
		public void setSwitchConverterPort(String switchConverterPort) {
			this.switchConverterPort = switchConverterPort;
		}
		public String getBhCapacity() {
			return bhCapacity;
		}
		public void setBhCapacity(String bhCapacity) {
			this.bhCapacity = bhCapacity;
		}
		public String getBhOffnetOnnet() {
			return bhOffnetOnnet;
		}
		public void setBhOffnetOnnet(String bhOffnetOnnet) {
			this.bhOffnetOnnet = bhOffnetOnnet;
		}
		public String getBackhaulType() {
			return backhaulType;
		}
		public void setBackhaulType(String backhaulType) {
			this.backhaulType = backhaulType;
		}
		public String getBsoCircuitId() {
			return bsoCircuitId;
		}
		public void setBsoCircuitId(String bsoCircuitId) {
			this.bsoCircuitId = bsoCircuitId;
		}
		public String getPeHostname() {
			return peHostname;
		}
		public void setPeHostname(String peHostname) {
			this.peHostname = peHostname;
		}
		public String getPeIp() {
			return peIp;
		}
		public void setPeIp(String peIp) {
			this.peIp = peIp;
		}
		public String getSsCity() {
			return ssCity;
		}
		public void setSsCity(String ssCity) {
			this.ssCity = ssCity;
		}
		public String getSsState() {
			return ssState;
		}
		public void setSsState(String ssState) {
			this.ssState = ssState;
		}
		public String getSsCircuitId() {
			return ssCircuitId;
		}
		public void setSsCircuitId(String ssCircuitId) {
			this.ssCircuitId = ssCircuitId;
		}
		public String getSsCustomerName() {
			return ssCustomerName;
		}
		public void setSsCustomerName(String ssCustomerName) {
			this.ssCustomerName = ssCustomerName;
		}
		public String getSsCustomerAddress() {
			return ssCustomerAddress;
		}
		public void setSsCustomerAddress(String ssCustomerAddress) {
			this.ssCustomerAddress = ssCustomerAddress;
		}
		public String getSsBsName() {
			return ssBsName;
		}
		public void setSsBsName(String ssBsName) {
			this.ssBsName = ssBsName;
		}
		public String getSsQos() {
			return ssQos;
		}
		public void setSsQos(String ssQos) {
			this.ssQos = ssQos;
		}
		public String getSsLatitude() {
			return ssLatitude;
		}
		public void setSsLatitude(String ssLatitude) {
			this.ssLatitude = ssLatitude;
		}
		public String getSsLongitude() {
			return ssLongitude;
		}
		public void setSsLongitude(String ssLongitude) {
			this.ssLongitude = ssLongitude;
		}
		public String getSsAntennaHeight() {
			return ssAntennaHeight;
		}
		public void setSsAntennaHeight(String ssAntennaHeight) {
			this.ssAntennaHeight = ssAntennaHeight;
		}
		public String getSsPolarization() {
			return ssPolarization;
		}
		public void setSsPolarization(String ssPolarization) {
			this.ssPolarization = ssPolarization;
		}
		public String getSsAntennaType() {
			return ssAntennaType;
		}
		public void setSsAntennaType(String ssAntennaType) {
			this.ssAntennaType = ssAntennaType;
		}
		public String getSsAntennaMountType() {
			return ssAntennaMountType;
		}
		public void setSsAntennaMountType(String ssAntennaMountType) {
			this.ssAntennaMountType = ssAntennaMountType;
		}
		public String getSsEthernetExtender() {
			return ssEthernetExtender;
		}
		public void setSsEthernetExtender(String ssEthernetExtender) {
			this.ssEthernetExtender = ssEthernetExtender;
		}
		public String getSsBuildingHeight() {
			return ssBuildingHeight;
		}
		public void setSsBuildingHeight(String ssBuildingHeight) {
			this.ssBuildingHeight = ssBuildingHeight;
		}
		public String getSsPoleHeight() {
			return ssPoleHeight;
		}
		public void setSsPoleHeight(String ssPoleHeight) {
			this.ssPoleHeight = ssPoleHeight;
		}
		public String getSsCableLength() {
			return ssCableLength;
		}
		public void setSsCableLength(String ssCableLength) {
			this.ssCableLength = ssCableLength;
		}
		public String getSsRssiDuringAcceptance() {
			return ssRssiDuringAcceptance;
		}
		public void setSsRssiDuringAcceptance(String ssRssiDuringAcceptance) {
			this.ssRssiDuringAcceptance = ssRssiDuringAcceptance;
		}
		public String getSsThroughputDuringAcceptance() {
			return ssThroughputDuringAcceptance;
		}
		public void setSsThroughputDuringAcceptance(String ssThroughputDuringAcceptance) {
			this.ssThroughputDuringAcceptance = ssThroughputDuringAcceptance;
		}
		public String getSsDateOfAcceptance() {
			return ssDateOfAcceptance;
		}
		public void setSsDateOfAcceptance(String ssDateOfAcceptance) {
			this.ssDateOfAcceptance = ssDateOfAcceptance;
		}
		public String getSsBhBso() {
			return ssBhBso;
		}
		public void setSsBhBso(String ssBhBso) {
			this.ssBhBso = ssBhBso;
		}
		public String getSsIp() {
			return ssIp;
		}
		public void setSsIp(String ssIp) {
			this.ssIp = ssIp;
		}
		public String getSsMac() {
			return ssMac;
		}
		public void setSsMac(String ssMac) {
			this.ssMac = ssMac;
		}
		public String getNearEndProductType() {
			return nearEndProductType;
		}
		public void setNearEndProductType(String nearEndProductType) {
			this.nearEndProductType = nearEndProductType;
		}
		public String getNearEndEsSsid() {
			return nearEndEsSsid;
		}
		public void setNearEndEsSsid(String nearEndEsSsid) {
			this.nearEndEsSsid = nearEndEsSsid;
		}
		public String getNearEndMasterSlave() {
			return nearEndMasterSlave;
		}
		public void setNearEndMasterSlave(String nearEndMasterSlave) {
			this.nearEndMasterSlave = nearEndMasterSlave;
		}
		public String getNearEndFrequency() {
			return nearEndFrequency;
		}
		public void setNearEndFrequency(String nearEndFrequency) {
			this.nearEndFrequency = nearEndFrequency;
		}
		public String getNearEndUas() {
			return nearEndUas;
		}
		public void setNearEndUas(String nearEndUas) {
			this.nearEndUas = nearEndUas;
		}
		public String getNearEndRssi() {
			return nearEndRssi;
		}
		public void setNearEndRssi(String nearEndRssi) {
			this.nearEndRssi = nearEndRssi;
		}
		public String getNearEndEstimatedThroughput() {
			return nearEndEstimatedThroughput;
		}
		public void setNearEndEstimatedThroughput(String nearEndEstimatedThroughput) {
			this.nearEndEstimatedThroughput = nearEndEstimatedThroughput;
		}
		public String getNearEndUtilisationUl() {
			return nearEndUtilisationUl;
		}
		public void setNearEndUtilisationUl(String nearEndUtilisationUl) {
			this.nearEndUtilisationUl = nearEndUtilisationUl;
		}
		public String getNearEndUtilisationDl() {
			return nearEndUtilisationDl;
		}
		public void setNearEndUtilisationDl(String nearEndUtilisationDl) {
			this.nearEndUtilisationDl = nearEndUtilisationDl;
		}
		public String getNearEndUptime() {
			return nearEndUptime;
		}
		public void setNearEndUptime(String nearEndUptime) {
			this.nearEndUptime = nearEndUptime;
		}
		public String getNearEndLinkDistance() {
			return nearEndLinkDistance;
		}
		public void setNearEndLinkDistance(String nearEndLinkDistance) {
			this.nearEndLinkDistance = nearEndLinkDistance;
		}
		public String getNearEndCbw() {
			return nearEndCbw;
		}
		public void setNearEndCbw(String nearEndCbw) {
			this.nearEndCbw = nearEndCbw;
		}
		public String getNearEndLatency() {
			return nearEndLatency;
		}
		public void setNearEndLatency(String nearEndLatency) {
			this.nearEndLatency = nearEndLatency;
		}
		public String getNearEndPd() {
			return nearEndPd;
		}
		public void setNearEndPd(String nearEndPd) {
			this.nearEndPd = nearEndPd;
		}
		public String getNearEndAutoNegotiation() {
			return nearEndAutoNegotiation;
		}
		public void setNearEndAutoNegotiation(String nearEndAutoNegotiation) {
			this.nearEndAutoNegotiation = nearEndAutoNegotiation;
		}
		public String getNearEndDuplex() {
			return nearEndDuplex;
		}
		public void setNearEndDuplex(String nearEndDuplex) {
			this.nearEndDuplex = nearEndDuplex;
		}
		public String getNearEndSpeed() {
			return nearEndSpeed;
		}
		public void setNearEndSpeed(String nearEndSpeed) {
			this.nearEndSpeed = nearEndSpeed;
		}
		public String getNearEndLink() {
			return nearEndLink;
		}
		public void setNearEndLink(String nearEndLink) {
			this.nearEndLink = nearEndLink;
		}
		public String getNearEndIduSN() {
			return nearEndIduSN;
		}
		public void setNearEndIduSN(String nearEndIduSN) {
			this.nearEndIduSN = nearEndIduSN;
		}
		public String getNearEndOduSN() {
			return nearEndOduSN;
		}
		public void setNearEndOduSN(String nearEndOduSN) {
			this.nearEndOduSN = nearEndOduSN;
		}
		public String getNearEndCRCErrors() {
			return nearEndCRCErrors;
		}
		public void setNearEndCRCErrors(String nearEndCRCErrors) {
			this.nearEndCRCErrors = nearEndCRCErrors;
		}
		public String getNearEndSWVersion() {
			return nearEndSWVersion;
		}
		public void setNearEndSWVersion(String nearEndSWVersion) {
			this.nearEndSWVersion = nearEndSWVersion;
		}
		public String getNearEndRSSIMin() {
			return nearEndRSSIMin;
		}
		public void setNearEndRSSIMin(String nearEndRSSIMin) {
			this.nearEndRSSIMin = nearEndRSSIMin;
		}
		public String getNearEndRSSIMax() {
			return nearEndRSSIMax;
		}
		public void setNearEndRSSIMax(String nearEndRSSIMax) {
			this.nearEndRSSIMax = nearEndRSSIMax;
		}
		public String getFarEndProductType() {
			return farEndProductType;
		}
		public void setFarEndProductType(String farEndProductType) {
			this.farEndProductType = farEndProductType;
		}
		public String getFarEndSsid() {
			return farEndSsid;
		}
		public void setFarEndSsid(String farEndSsid) {
			this.farEndSsid = farEndSsid;
		}
		public String getFarEndMasterSlave() {
			return farEndMasterSlave;
		}
		public void setFarEndMasterSlave(String farEndMasterSlave) {
			this.farEndMasterSlave = farEndMasterSlave;
		}
		public String getFarEndFrequency() {
			return farEndFrequency;
		}
		public void setFarEndFrequency(String farEndFrequency) {
			this.farEndFrequency = farEndFrequency;
		}
		public String getFarEndUas() {
			return farEndUas;
		}
		public void setFarEndUas(String farEndUas) {
			this.farEndUas = farEndUas;
		}
		public String getFarEndRssi() {
			return farEndRssi;
		}
		public void setFarEndRssi(String farEndRssi) {
			this.farEndRssi = farEndRssi;
		}
		public String getFarEndEstimatedThroughput() {
			return farEndEstimatedThroughput;
		}
		public void setFarEndEstimatedThroughput(String farEndEstimatedThroughput) {
			this.farEndEstimatedThroughput = farEndEstimatedThroughput;
		}
		public String getFarEndUtilisationUl() {
			return farEndUtilisationUl;
		}
		public void setFarEndUtilisationUl(String farEndUtilisationUl) {
			this.farEndUtilisationUl = farEndUtilisationUl;
		}
		public String getFarEndUtilisationDl() {
			return farEndUtilisationDl;
		}
		public void setFarEndUtilisationDl(String farEndUtilisationDl) {
			this.farEndUtilisationDl = farEndUtilisationDl;
		}
		public String getFarEndUptime() {
			return farEndUptime;
		}
		public void setFarEndUptime(String farEndUptime) {
			this.farEndUptime = farEndUptime;
		}
		public String getFarEndLinkDistance() {
			return farEndLinkDistance;
		}
		public void setFarEndLinkDistance(String farEndLinkDistance) {
			this.farEndLinkDistance = farEndLinkDistance;
		}
		public String getFarEndCbw() {
			return farEndCbw;
		}
		public void setFarEndCbw(String farEndCbw) {
			this.farEndCbw = farEndCbw;
		}
		public String getFarEndLatency() {
			return farEndLatency;
		}
		public void setFarEndLatency(String farEndLatency) {
			this.farEndLatency = farEndLatency;
		}
		public String getFarEndPd() {
			return farEndPd;
		}
		public void setFarEndPd(String farEndPd) {
			this.farEndPd = farEndPd;
		}
		public String getFarEndAutoNegotiation() {
			return farEndAutoNegotiation;
		}
		public void setFarEndAutoNegotiation(String farEndAutoNegotiation) {
			this.farEndAutoNegotiation = farEndAutoNegotiation;
		}
		public String getFarEndDuplex() {
			return farEndDuplex;
		}
		public void setFarEndDuplex(String farEndDuplex) {
			this.farEndDuplex = farEndDuplex;
		}
		public String getFarEndSpeed() {
			return farEndSpeed;
		}
		public void setFarEndSpeed(String farEndSpeed) {
			this.farEndSpeed = farEndSpeed;
		}
		public String getFarEndLink() {
			return farEndLink;
		}
		public void setFarEndLink(String farEndLink) {
			this.farEndLink = farEndLink;
		}
		public String getFarEndIduSN() {
			return farEndIduSN;
		}
		public void setFarEndIduSN(String farEndIduSN) {
			this.farEndIduSN = farEndIduSN;
		}
		public String getFarEndOduSN() {
			return farEndOduSN;
		}
		public void setFarEndOduSN(String farEndOduSN) {
			this.farEndOduSN = farEndOduSN;
		}
		public String getFarEndCRCErrors() {
			return farEndCRCErrors;
		}
		public void setFarEndCRCErrors(String farEndCRCErrors) {
			this.farEndCRCErrors = farEndCRCErrors;
		}
		public String getFarEndSWVersion() {
			return farEndSWVersion;
		}
		public void setFarEndSWVersion(String farEndSWVersion) {
			this.farEndSWVersion = farEndSWVersion;
		}
		public String getFarEndRssiMax() {
			return farEndRssiMax;
		}
		public void setFarEndRssiMax(String farEndRssiMax) {
			this.farEndRssiMax = farEndRssiMax;
		}
		public String getFarEndRssiMin() {
			return farEndRssiMin;
		}
		public void setFarEndRssiMin(String farEndRssiMin) {
			this.farEndRssiMin = farEndRssiMin;
		}
		public String getIsActive() {
			return isActive;
		}
		public void setIsActive(String isActive) {
			this.isActive = isActive;
		}
		public String getLastModifiedBy() {
			return lastModifiedBy;
		}
		public void setLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
		}
		public String getLastModifiedDate() {
			return lastModifiedDate;
		}
		public void setLastModifiedDate(String lastModifiedDate) {
			this.lastModifiedDate = lastModifiedDate;
		}
		
}
