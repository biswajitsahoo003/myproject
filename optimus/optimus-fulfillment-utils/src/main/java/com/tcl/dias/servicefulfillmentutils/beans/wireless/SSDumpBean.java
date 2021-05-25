package com.tcl.dias.servicefulfillmentutils.beans.wireless;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SSDumpBean {

    @JsonProperty("antenna_beamwidth")
    private String antennaBeamwidth;
    @JsonProperty("antenna_height")
    private String antennaHeight;
    @JsonProperty("antenna_type")
    private String antennaType;
    @JsonProperty("appln_version")
    private String applnVersion;
    @JsonProperty("auto_negotiation")
    private String autoNegotiation;
    private String beacon;
    @JsonProperty("bs_ip")
    private String bsIp;
    @JsonProperty("bs_name")
    private String bsName;
    @JsonProperty("building_height")
    private String buildingHeight;
    @JsonProperty("cable_length")
    private String cableLength;
    @JsonProperty("cinr_during_acceptance")
    private String cinrDuringAcceptance;
    @JsonProperty("circuit_id")
    private String circuitId;
    private String city;
    @JsonProperty("commanded_rx_power")
    private String commandedRxPower;
    @JsonProperty("crc_errors")
    private String crcErrors;
    @JsonProperty("customer_address")
    private String customerAddress;
    @JsonProperty("customer_name")
    private String customerName;
    @JsonProperty("data_vlan")
    private String dataVlan;
    @JsonProperty("date_of_acceptance")
    private String dateOfAcceptance;
    @JsonProperty("device_uptime")
    private String deviceUptime;
    @JsonProperty("dl_cinr")
    private String dlCinr;
    @JsonProperty("dl_fec")
    private String dlFec;
    @JsonProperty("dl_jitter")
    private String dlJitter;
    @JsonProperty("dl_jitter_max")
    private String dlJitterMax;
    @JsonProperty("dl_jitter_min")
    private String dlJitterMin;
    @JsonProperty("dl_modulation")
    private String dlModulation;
    @JsonProperty("dl_modulation_change")
    private String dlModulationChange;
    @JsonProperty("dl_modulation_max")
    private String dlModulationMax;
    @JsonProperty("dl_modulation_min")
    private String dlModulationMin;
    @JsonProperty("dl_rssi")
    private String dlRssi;
    @JsonProperty("dl_rssi1")
    private String dlRssi1;
    @JsonProperty("dl_rssi1_avg")
    private String dlRssi1Avg;
    @JsonProperty("dl_rssi1_max")
    private String dlRssi1Max;
    @JsonProperty("dl_rssi1_min")
    private String dlRssi1Min;
    @JsonProperty("dl_rssi2")
    private String dlRssi2;
    @JsonProperty("dl_rssi2_avg")
    private String dlRssi2Avg;
    @JsonProperty("dl_rssi2_max")
    private String dlRssi2Max;
    @JsonProperty("dl_rssi2_min")
    private String dlRssi2Min;
    @JsonProperty("dl_snr1_avg")
    private String dlSnr1Avg;
    @JsonProperty("dl_snr1_max")
    private String dlSnr1Max;
    @JsonProperty("dl_snr1_min")
    private String dlSnr1Min;
    @JsonProperty("dl_snr2_avg")
    private String dlSnr2Avg;
    @JsonProperty("dl_snr2_max")
    private String dlSnr2Max;
    @JsonProperty("dl_snr2_min")
    private String dlSnr2Min;
    @JsonProperty("dl_uas_sum_of_24_hours_value")
    private String dlUasSumOf24HoursValue;
    private String duplex;
    private String errors;
    @JsonProperty("estimated_thrput_dl_avg")
    private String estimatedThrputDlAvg;
    @JsonProperty("estimated_thrput_ul_avg")
    private String estimatedThrputUlAvg;
    @JsonProperty("ethernet_extender")
    private String ethernetExtender;
    @JsonProperty("ethernet_link")
    private String ethernetLink;
    private String frequency;
    @JsonProperty("hbs_ip")
    private String hbsIp;
    @JsonProperty("hsu_ip")
    private String hsuIp;
    @JsonProperty("hsu_mac")
    private String hsuMac;
    @JsonProperty("hsu_power")
    private String hsuPower;
    @JsonProperty("hw_version")
    private String hwVersion;
    @JsonProperty("intrf_dl")
    private String intrfDl;
    @JsonProperty("intrf_ul")
    private String intrfUl;
    private String latency;
    private String latitude;
    @JsonProperty("lens_reflector")
    private String lensReflector;
    private String link;
    @JsonProperty("link_distance")
    private String linkDistance;
    private String longitude;
    private String mac;
    @JsonProperty("management_vlan")
    private String managementVlan;
    @JsonProperty("maximum_ts_used")
    private String maximumTsUsed;
    @JsonProperty("mimo_modes")
    private String mimoModes;
    @JsonProperty("Name")
    private String name;
    private String pd;
    @JsonProperty("pmp_port")
    private String pmpPort;
    private String polarisation;
    private String polarization;
    @JsonProperty("polled_ap_ip")
    private String polledApIp;
    @JsonProperty("polled_frequency")
    private String polledFrequency;
    @JsonProperty("polled_sector_id")
    private String polledSectorId;
    @JsonProperty("polled_ss_mac")
    private String polledSsMac;
    @JsonProperty("product_type")
    private String productType;
    private String ptx;
    @JsonProperty("qos_bw")
    private String qosBw;
    @JsonProperty("qos_dl")
    private String qosDl;
    @JsonProperty("qos_ul")
    private String qosUl;
    @JsonProperty("re_reg_count_sum_of_24_hours_value")
    private String reRegCountSumOf24HoursValue;
    @JsonProperty("reg_count")
    private String regCount;
    @JsonProperty("reg_count_sum_of_24_hours_value")
    private String regCountSumOf24HoursValue;
    private String region;
    @JsonProperty("region_circle")
    private String regionCircle;
    @JsonProperty("rereg_count")
    private String reregCount;
    @JsonProperty("rssi_during_acceptance")
    private String rssiDuringAcceptance;
    @JsonProperty("sector_id")
    private String sectorId;
    @JsonProperty("serial_number")
    private String serialNumber;
    @JsonProperty("session_uptime")
    private String sessionUptime;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("software_version")
    private String softwareVersion;
    private String speed;
    @JsonProperty("ss_boot_rom_version")
    private String ssBootRomVersion;
    @JsonProperty("ss_ip")
    private String ssIp;
    @JsonProperty("ss_mac")
    private String ssMac;
    @JsonProperty("ss_mount_type")
    private String ssMountType;
    private String state;
    @JsonProperty("static_assigned_ts")
    private String staticAssignedTs;
    @JsonProperty("sw_version")
    private String swVersion;
    @JsonProperty("tower_pole_height")
    private String towerPoleHeight;
    @JsonProperty("ul_cinr")
    private String ulCinr;
    @JsonProperty("ul_fec")
    private String ulFec;
    @JsonProperty("ul_jitter")
    private String ulJitter;
    @JsonProperty("ul_jitter_max")
    private String ulJitterMax;
    @JsonProperty("ul_jitter_min")
    private String ulJitterMin;
    @JsonProperty("ul_modulation_min")
    private String ulModulationMin;
    @JsonProperty("ul_rssi")
    private String ulRssi;
    @JsonProperty("ul_rssi1")
    private String ulRssi1;
    @JsonProperty("ul_rssi1_avg")
    private String ulRssi1Avg;
    @JsonProperty("ul_rssi1_max")
    private String ulRssi1Max;
    @JsonProperty("ul_rssi1_min")
    private String ulRssi1Min;
    @JsonProperty("ul_rssi2")
    private String ulRssi2;
    @JsonProperty("ul_rssi2_avg")
    private String ulRssi2Avg;
    @JsonProperty("ul_rssi2_max")
    private String ulRssi2Max;
    @JsonProperty("ul_rssi2_min")
    private String ulRssi2Min;
    @JsonProperty("ul_snr1_avg")
    private String ulSnr1Avg;
    @JsonProperty("ul_snr1_max")
    private String ulSnr1Max;
    @JsonProperty("ul_snr1_min")
    private String ulSnr1Min;
    @JsonProperty("ul_snr2_avg")
    private String ulSnr2Avg;
    @JsonProperty("ul_snr2_max")
    private String ulSnr2Max;
    @JsonProperty("ul_snr2_min")
    private String ulSnr2Min;
    @JsonProperty("utilisation_dl")
    private String utilisationDl;
    @JsonProperty("utilisation_dl_max")
    private String utilisationDlMax;
    @JsonProperty("utilisation_ul")
    private String utilisationUl;
    @JsonProperty("utilisation_ul_max")
    private String utilisationUlMax;
    private String vendor;
    private String vlan;

    public String getAntennaBeamwidth() {
        return antennaBeamwidth;
    }

    public void setAntennaBeamwidth(String antennaBeamwidth) {
        this.antennaBeamwidth = antennaBeamwidth;
    }

    public String getAntennaHeight() {
        return antennaHeight;
    }

    public void setAntennaHeight(String antennaHeight) {
        this.antennaHeight = antennaHeight;
    }

    public String getAntennaType() {
        return antennaType;
    }

    public void setAntennaType(String antennaType) {
        this.antennaType = antennaType;
    }

    public String getApplnVersion() {
        return applnVersion;
    }

    public void setApplnVersion(String applnVersion) {
        this.applnVersion = applnVersion;
    }

    public String getAutoNegotiation() {
        return autoNegotiation;
    }

    public void setAutoNegotiation(String autoNegotiation) {
        this.autoNegotiation = autoNegotiation;
    }

    public String getBeacon() {
        return beacon;
    }

    public void setBeacon(String beacon) {
        this.beacon = beacon;
    }

    public String getBsIp() {
        return bsIp;
    }

    public void setBsIp(String bsIp) {
        this.bsIp = bsIp;
    }

    public String getBsName() {
        return bsName;
    }

    public void setBsName(String bsName) {
        this.bsName = bsName;
    }

    public String getBuildingHeight() {
        return buildingHeight;
    }

    public void setBuildingHeight(String buildingHeight) {
        this.buildingHeight = buildingHeight;
    }

    public String getCableLength() {
        return cableLength;
    }

    public void setCableLength(String cableLength) {
        this.cableLength = cableLength;
    }

    public String getCinrDuringAcceptance() {
        return cinrDuringAcceptance;
    }

    public void setCinrDuringAcceptance(String cinrDuringAcceptance) {
        this.cinrDuringAcceptance = cinrDuringAcceptance;
    }

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCommandedRxPower() {
        return commandedRxPower;
    }

    public void setCommandedRxPower(String commandedRxPower) {
        this.commandedRxPower = commandedRxPower;
    }

    public String getCrcErrors() {
        return crcErrors;
    }

    public void setCrcErrors(String crcErrors) {
        this.crcErrors = crcErrors;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDataVlan() {
        return dataVlan;
    }

    public void setDataVlan(String dataVlan) {
        this.dataVlan = dataVlan;
    }

    public String getDateOfAcceptance() {
        return dateOfAcceptance;
    }

    public void setDateOfAcceptance(String dateOfAcceptance) {
        this.dateOfAcceptance = dateOfAcceptance;
    }

    public String getDeviceUptime() {
        return deviceUptime;
    }

    public void setDeviceUptime(String deviceUptime) {
        this.deviceUptime = deviceUptime;
    }

    public String getDlCinr() {
        return dlCinr;
    }

    public void setDlCinr(String dlCinr) {
        this.dlCinr = dlCinr;
    }

    public String getDlFec() {
        return dlFec;
    }

    public void setDlFec(String dlFec) {
        this.dlFec = dlFec;
    }

    public String getDlJitter() {
        return dlJitter;
    }

    public void setDlJitter(String dlJitter) {
        this.dlJitter = dlJitter;
    }

    public String getDlJitterMax() {
        return dlJitterMax;
    }

    public void setDlJitterMax(String dlJitterMax) {
        this.dlJitterMax = dlJitterMax;
    }

    public String getDlJitterMin() {
        return dlJitterMin;
    }

    public void setDlJitterMin(String dlJitterMin) {
        this.dlJitterMin = dlJitterMin;
    }

    public String getDlModulation() {
        return dlModulation;
    }

    public void setDlModulation(String dlModulation) {
        this.dlModulation = dlModulation;
    }

    public String getDlModulationChange() {
        return dlModulationChange;
    }

    public void setDlModulationChange(String dlModulationChange) {
        this.dlModulationChange = dlModulationChange;
    }

    public String getDlModulationMax() {
        return dlModulationMax;
    }

    public void setDlModulationMax(String dlModulationMax) {
        this.dlModulationMax = dlModulationMax;
    }

    public String getDlModulationMin() {
        return dlModulationMin;
    }

    public void setDlModulationMin(String dlModulationMin) {
        this.dlModulationMin = dlModulationMin;
    }

    public String getDlRssi() {
        return dlRssi;
    }

    public void setDlRssi(String dlRssi) {
        this.dlRssi = dlRssi;
    }

    public String getDlRssi1() {
        return dlRssi1;
    }

    public void setDlRssi1(String dlRssi1) {
        this.dlRssi1 = dlRssi1;
    }

    public String getDlRssi1Avg() {
        return dlRssi1Avg;
    }

    public void setDlRssi1Avg(String dlRssi1Avg) {
        this.dlRssi1Avg = dlRssi1Avg;
    }

    public String getDlRssi1Max() {
        return dlRssi1Max;
    }

    public void setDlRssi1Max(String dlRssi1Max) {
        this.dlRssi1Max = dlRssi1Max;
    }

    public String getDlRssi1Min() {
        return dlRssi1Min;
    }

    public void setDlRssi1Min(String dlRssi1Min) {
        this.dlRssi1Min = dlRssi1Min;
    }

    public String getDlRssi2() {
        return dlRssi2;
    }

    public void setDlRssi2(String dlRssi2) {
        this.dlRssi2 = dlRssi2;
    }

    public String getDlRssi2Avg() {
        return dlRssi2Avg;
    }

    public void setDlRssi2Avg(String dlRssi2Avg) {
        this.dlRssi2Avg = dlRssi2Avg;
    }

    public String getDlRssi2Max() {
        return dlRssi2Max;
    }

    public void setDlRssi2Max(String dlRssi2Max) {
        this.dlRssi2Max = dlRssi2Max;
    }

    public String getDlRssi2Min() {
        return dlRssi2Min;
    }

    public void setDlRssi2Min(String dlRssi2Min) {
        this.dlRssi2Min = dlRssi2Min;
    }

    public String getDlSnr1Avg() {
        return dlSnr1Avg;
    }

    public void setDlSnr1Avg(String dlSnr1Avg) {
        this.dlSnr1Avg = dlSnr1Avg;
    }

    public String getDlSnr1Max() {
        return dlSnr1Max;
    }

    public void setDlSnr1Max(String dlSnr1Max) {
        this.dlSnr1Max = dlSnr1Max;
    }

    public String getDlSnr1Min() {
        return dlSnr1Min;
    }

    public void setDlSnr1Min(String dlSnr1Min) {
        this.dlSnr1Min = dlSnr1Min;
    }

    public String getDlSnr2Avg() {
        return dlSnr2Avg;
    }

    public void setDlSnr2Avg(String dlSnr2Avg) {
        this.dlSnr2Avg = dlSnr2Avg;
    }

    public String getDlSnr2Max() {
        return dlSnr2Max;
    }

    public void setDlSnr2Max(String dlSnr2Max) {
        this.dlSnr2Max = dlSnr2Max;
    }

    public String getDlSnr2Min() {
        return dlSnr2Min;
    }

    public void setDlSnr2Min(String dlSnr2Min) {
        this.dlSnr2Min = dlSnr2Min;
    }

    public String getDlUasSumOf24HoursValue() {
        return dlUasSumOf24HoursValue;
    }

    public void setDlUasSumOf24HoursValue(String dlUasSumOf24HoursValue) {
        this.dlUasSumOf24HoursValue = dlUasSumOf24HoursValue;
    }

    public String getDuplex() {
        return duplex;
    }

    public void setDuplex(String duplex) {
        this.duplex = duplex;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public String getEstimatedThrputDlAvg() {
        return estimatedThrputDlAvg;
    }

    public void setEstimatedThrputDlAvg(String estimatedThrputDlAvg) {
        this.estimatedThrputDlAvg = estimatedThrputDlAvg;
    }

    public String getEstimatedThrputUlAvg() {
        return estimatedThrputUlAvg;
    }

    public void setEstimatedThrputUlAvg(String estimatedThrputUlAvg) {
        this.estimatedThrputUlAvg = estimatedThrputUlAvg;
    }

    public String getEthernetExtender() {
        return ethernetExtender;
    }

    public void setEthernetExtender(String ethernetExtender) {
        this.ethernetExtender = ethernetExtender;
    }

    public String getEthernetLink() {
        return ethernetLink;
    }

    public void setEthernetLink(String ethernetLink) {
        this.ethernetLink = ethernetLink;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getHbsIp() {
        return hbsIp;
    }

    public void setHbsIp(String hbsIp) {
        this.hbsIp = hbsIp;
    }

    public String getHsuIp() {
        return hsuIp;
    }

    public void setHsuIp(String hsuIp) {
        this.hsuIp = hsuIp;
    }

    public String getHsuMac() {
        return hsuMac;
    }

    public void setHsuMac(String hsuMac) {
        this.hsuMac = hsuMac;
    }

    public String getHsuPower() {
        return hsuPower;
    }

    public void setHsuPower(String hsuPower) {
        this.hsuPower = hsuPower;
    }

    public String getHwVersion() {
        return hwVersion;
    }

    public void setHwVersion(String hwVersion) {
        this.hwVersion = hwVersion;
    }

    public String getIntrfDl() {
        return intrfDl;
    }

    public void setIntrfDl(String intrfDl) {
        this.intrfDl = intrfDl;
    }

    public String getIntrfUl() {
        return intrfUl;
    }

    public void setIntrfUl(String intrfUl) {
        this.intrfUl = intrfUl;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLensReflector() {
        return lensReflector;
    }

    public void setLensReflector(String lensReflector) {
        this.lensReflector = lensReflector;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkDistance() {
        return linkDistance;
    }

    public void setLinkDistance(String linkDistance) {
        this.linkDistance = linkDistance;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getManagementVlan() {
        return managementVlan;
    }

    public void setManagementVlan(String managementVlan) {
        this.managementVlan = managementVlan;
    }

    public String getMaximumTsUsed() {
        return maximumTsUsed;
    }

    public void setMaximumTsUsed(String maximumTsUsed) {
        this.maximumTsUsed = maximumTsUsed;
    }

    public String getMimoModes() {
        return mimoModes;
    }

    public void setMimoModes(String mimoModes) {
        this.mimoModes = mimoModes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getPmpPort() {
        return pmpPort;
    }

    public void setPmpPort(String pmpPort) {
        this.pmpPort = pmpPort;
    }

    public String getPolarisation() {
        return polarisation;
    }

    public void setPolarisation(String polarisation) {
        this.polarisation = polarisation;
    }

    public String getPolarization() {
        return polarization;
    }

    public void setPolarization(String polarization) {
        this.polarization = polarization;
    }

    public String getPolledApIp() {
        return polledApIp;
    }

    public void setPolledApIp(String polledApIp) {
        this.polledApIp = polledApIp;
    }

    public String getPolledFrequency() {
        return polledFrequency;
    }

    public void setPolledFrequency(String polledFrequency) {
        this.polledFrequency = polledFrequency;
    }

    public String getPolledSectorId() {
        return polledSectorId;
    }

    public void setPolledSectorId(String polledSectorId) {
        this.polledSectorId = polledSectorId;
    }

    public String getPolledSsMac() {
        return polledSsMac;
    }

    public void setPolledSsMac(String polledSsMac) {
        this.polledSsMac = polledSsMac;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getPtx() {
        return ptx;
    }

    public void setPtx(String ptx) {
        this.ptx = ptx;
    }

    public String getQosBw() {
        return qosBw;
    }

    public void setQosBw(String qosBw) {
        this.qosBw = qosBw;
    }

    public String getQosDl() {
        return qosDl;
    }

    public void setQosDl(String qosDl) {
        this.qosDl = qosDl;
    }

    public String getQosUl() {
        return qosUl;
    }

    public void setQosUl(String qosUl) {
        this.qosUl = qosUl;
    }

    public String getReRegCountSumOf24HoursValue() {
        return reRegCountSumOf24HoursValue;
    }

    public void setReRegCountSumOf24HoursValue(String reRegCountSumOf24HoursValue) {
        this.reRegCountSumOf24HoursValue = reRegCountSumOf24HoursValue;
    }

    public String getRegCount() {
        return regCount;
    }

    public void setRegCount(String regCount) {
        this.regCount = regCount;
    }

    public String getRegCountSumOf24HoursValue() {
        return regCountSumOf24HoursValue;
    }

    public void setRegCountSumOf24HoursValue(String regCountSumOf24HoursValue) {
        this.regCountSumOf24HoursValue = regCountSumOf24HoursValue;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionCircle() {
        return regionCircle;
    }

    public void setRegionCircle(String regionCircle) {
        this.regionCircle = regionCircle;
    }

    public String getReregCount() {
        return reregCount;
    }

    public void setReregCount(String reregCount) {
        this.reregCount = reregCount;
    }

    public String getRssiDuringAcceptance() {
        return rssiDuringAcceptance;
    }

    public void setRssiDuringAcceptance(String rssiDuringAcceptance) {
        this.rssiDuringAcceptance = rssiDuringAcceptance;
    }

    public String getSectorId() {
        return sectorId;
    }

    public void setSectorId(String sectorId) {
        this.sectorId = sectorId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSessionUptime() {
        return sessionUptime;
    }

    public void setSessionUptime(String sessionUptime) {
        this.sessionUptime = sessionUptime;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSsBootRomVersion() {
        return ssBootRomVersion;
    }

    public void setSsBootRomVersion(String ssBootRomVersion) {
        this.ssBootRomVersion = ssBootRomVersion;
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

    public String getSsMountType() {
        return ssMountType;
    }

    public void setSsMountType(String ssMountType) {
        this.ssMountType = ssMountType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStaticAssignedTs() {
        return staticAssignedTs;
    }

    public void setStaticAssignedTs(String staticAssignedTs) {
        this.staticAssignedTs = staticAssignedTs;
    }

    public String getSwVersion() {
        return swVersion;
    }

    public void setSwVersion(String swVersion) {
        this.swVersion = swVersion;
    }

    public String getTowerPoleHeight() {
        return towerPoleHeight;
    }

    public void setTowerPoleHeight(String towerPoleHeight) {
        this.towerPoleHeight = towerPoleHeight;
    }

    public String getUlCinr() {
        return ulCinr;
    }

    public void setUlCinr(String ulCinr) {
        this.ulCinr = ulCinr;
    }

    public String getUlFec() {
        return ulFec;
    }

    public void setUlFec(String ulFec) {
        this.ulFec = ulFec;
    }

    public String getUlJitter() {
        return ulJitter;
    }

    public void setUlJitter(String ulJitter) {
        this.ulJitter = ulJitter;
    }

    public String getUlJitterMax() {
        return ulJitterMax;
    }

    public void setUlJitterMax(String ulJitterMax) {
        this.ulJitterMax = ulJitterMax;
    }

    public String getUlJitterMin() {
        return ulJitterMin;
    }

    public void setUlJitterMin(String ulJitterMin) {
        this.ulJitterMin = ulJitterMin;
    }

    public String getUlModulationMin() {
        return ulModulationMin;
    }

    public void setUlModulationMin(String ulModulationMin) {
        this.ulModulationMin = ulModulationMin;
    }

    public String getUlRssi() {
        return ulRssi;
    }

    public void setUlRssi(String ulRssi) {
        this.ulRssi = ulRssi;
    }

    public String getUlRssi1() {
        return ulRssi1;
    }

    public void setUlRssi1(String ulRssi1) {
        this.ulRssi1 = ulRssi1;
    }

    public String getUlRssi1Avg() {
        return ulRssi1Avg;
    }

    public void setUlRssi1Avg(String ulRssi1Avg) {
        this.ulRssi1Avg = ulRssi1Avg;
    }

    public String getUlRssi1Max() {
        return ulRssi1Max;
    }

    public void setUlRssi1Max(String ulRssi1Max) {
        this.ulRssi1Max = ulRssi1Max;
    }

    public String getUlRssi1Min() {
        return ulRssi1Min;
    }

    public void setUlRssi1Min(String ulRssi1Min) {
        this.ulRssi1Min = ulRssi1Min;
    }

    public String getUlRssi2() {
        return ulRssi2;
    }

    public void setUlRssi2(String ulRssi2) {
        this.ulRssi2 = ulRssi2;
    }

    public String getUlRssi2Avg() {
        return ulRssi2Avg;
    }

    public void setUlRssi2Avg(String ulRssi2Avg) {
        this.ulRssi2Avg = ulRssi2Avg;
    }

    public String getUlRssi2Max() {
        return ulRssi2Max;
    }

    public void setUlRssi2Max(String ulRssi2Max) {
        this.ulRssi2Max = ulRssi2Max;
    }

    public String getUlRssi2Min() {
        return ulRssi2Min;
    }

    public void setUlRssi2Min(String ulRssi2Min) {
        this.ulRssi2Min = ulRssi2Min;
    }

    public String getUlSnr1Avg() {
        return ulSnr1Avg;
    }

    public void setUlSnr1Avg(String ulSnr1Avg) {
        this.ulSnr1Avg = ulSnr1Avg;
    }

    public String getUlSnr1Max() {
        return ulSnr1Max;
    }

    public void setUlSnr1Max(String ulSnr1Max) {
        this.ulSnr1Max = ulSnr1Max;
    }

    public String getUlSnr1Min() {
        return ulSnr1Min;
    }

    public void setUlSnr1Min(String ulSnr1Min) {
        this.ulSnr1Min = ulSnr1Min;
    }

    public String getUlSnr2Avg() {
        return ulSnr2Avg;
    }

    public void setUlSnr2Avg(String ulSnr2Avg) {
        this.ulSnr2Avg = ulSnr2Avg;
    }

    public String getUlSnr2Max() {
        return ulSnr2Max;
    }

    public void setUlSnr2Max(String ulSnr2Max) {
        this.ulSnr2Max = ulSnr2Max;
    }

    public String getUlSnr2Min() {
        return ulSnr2Min;
    }

    public void setUlSnr2Min(String ulSnr2Min) {
        this.ulSnr2Min = ulSnr2Min;
    }

    public String getUtilisationDl() {
        return utilisationDl;
    }

    public void setUtilisationDl(String utilisationDl) {
        this.utilisationDl = utilisationDl;
    }

    public String getUtilisationDlMax() {
        return utilisationDlMax;
    }

    public void setUtilisationDlMax(String utilisationDlMax) {
        this.utilisationDlMax = utilisationDlMax;
    }

    public String getUtilisationUl() {
        return utilisationUl;
    }

    public void setUtilisationUl(String utilisationUl) {
        this.utilisationUl = utilisationUl;
    }

    public String getUtilisationUlMax() {
        return utilisationUlMax;
    }

    public void setUtilisationUlMax(String utilisationUlMax) {
        this.utilisationUlMax = utilisationUlMax;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVlan() {
        return vlan;
    }

    public void setVlan(String vlan) {
        this.vlan = vlan;
    }

}
