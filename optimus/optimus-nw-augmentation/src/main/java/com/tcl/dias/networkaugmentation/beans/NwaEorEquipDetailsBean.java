package com.tcl.dias.networkaugmentation.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NwaEorEquipDetailsBean {

    private Integer orderId;

    private String orderCode;

    private String aEndMuxName;

    private String aEndMuxPort;

    private String aEndMuxPort2;

    private String aggrParentRouterIp;

    private String area;

    private String areaOther;

    private String bldg;

    private String bldgOther;

    private String city;

    private String cityOther;

    private String country;

    private String coverage;

    private String dcnReq;

    private String deviceSubType;

    private String deviceType;

    private String endMuxAddress;

    private String eorEquipStatus;

    private String eqpmntIp;

    private String eqpmntName;

    private String eqpmntPort;

    private String eqpmntType;

    private String floor;

    private String hostName;

    private String hostName1;

    private String hostName2;

    private String interFibreReq;

    private String interfaceToParent;

    private String interfaceType;

    private String isManEquipment;

    private String isSwitchIpRequird;

    private String majorPop;

    private String microPop;

    private String nocTeam;

    private String nodeOwner;

    private String nodeOwnerOther;

    private String poolSize;

    private String popName;

    private String popOwner;

    private String popType;

    private String powerRating;

    private String powerType;

    private String ringType;

    private String siteAddress;

    private String spareScrap;

    private String subnetIpPool1;

    private String subnetIpPool2;

    private String switchIpPool;

    private String switchManagementIp;

    private String switchManagementVlan;

    private String switchSubnetMask;

    private String vlan;

    private String eorWimaxOrderSite;

    private String siteEnd;

    private String state;

    private String siteName;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getaEndMuxName() {
        return aEndMuxName;
    }

    public void setaEndMuxName(String aEndMuxName) {
        this.aEndMuxName = aEndMuxName;
    }

    public String getaEndMuxPort() {
        return aEndMuxPort;
    }

    public void setaEndMuxPort(String aEndMuxPort) {
        this.aEndMuxPort = aEndMuxPort;
    }

    public String getaEndMuxPort2() {
        return aEndMuxPort2;
    }

    public void setaEndMuxPort2(String aEndMuxPort2) {
        this.aEndMuxPort2 = aEndMuxPort2;
    }

    public String getAggrParentRouterIp() {
        return aggrParentRouterIp;
    }

    public void setAggrParentRouterIp(String aggrParentRouterIp) {
        this.aggrParentRouterIp = aggrParentRouterIp;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaOther() {
        return areaOther;
    }

    public void setAreaOther(String areaOther) {
        this.areaOther = areaOther;
    }

    public String getBldg() {
        return bldg;
    }

    public void setBldg(String bldg) {
        this.bldg = bldg;
    }

    public String getBldgOther() {
        return bldgOther;
    }

    public void setBldgOther(String bldgOther) {
        this.bldgOther = bldgOther;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityOther() {
        return cityOther;
    }

    public void setCityOther(String cityOther) {
        this.cityOther = cityOther;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getDcnReq() {
        return dcnReq;
    }

    public void setDcnReq(String dcnReq) {
        this.dcnReq = dcnReq;
    }

    public String getDeviceSubType() {
        return deviceSubType;
    }

    public void setDeviceSubType(String deviceSubType) {
        this.deviceSubType = deviceSubType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getEndMuxAddress() {
        return endMuxAddress;
    }

    public void setEndMuxAddress(String endMuxAddress) {
        this.endMuxAddress = endMuxAddress;
    }

    public String getEorEquipStatus() {
        return eorEquipStatus;
    }

    public void setEorEquipStatus(String eorEquipStatus) {
        this.eorEquipStatus = eorEquipStatus;
    }

    public String getEqpmntIp() {
        return eqpmntIp;
    }

    public void setEqpmntIp(String eqpmntIp) {
        this.eqpmntIp = eqpmntIp;
    }

    public String getEqpmntName() {
        return eqpmntName;
    }

    public void setEqpmntName(String eqpmntName) {
        this.eqpmntName = eqpmntName;
    }

    public String getEqpmntPort() {
        return eqpmntPort;
    }

    public void setEqpmntPort(String eqpmntPort) {
        this.eqpmntPort = eqpmntPort;
    }

    public String getEqpmntType() {
        return eqpmntType;
    }

    public void setEqpmntType(String eqpmntType) {
        this.eqpmntType = eqpmntType;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getInterFibreReq() {
        return interFibreReq;
    }

    public void setInterFibreReq(String interFibreReq) {
        this.interFibreReq = interFibreReq;
    }

    public String getInterfaceToParent() {
        return interfaceToParent;
    }

    public void setInterfaceToParent(String interfaceToParent) {
        this.interfaceToParent = interfaceToParent;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getIsManEquipment() {
        return isManEquipment;
    }

    public void setIsManEquipment(String isManEquipment) {
        this.isManEquipment = isManEquipment;
    }

    public String getIsSwitchIpRequird() {
        return isSwitchIpRequird;
    }

    public void setIsSwitchIpRequird(String isSwitchIpRequird) {
        this.isSwitchIpRequird = isSwitchIpRequird;
    }

    public String getMajorPop() {
        return majorPop;
    }

    public void setMajorPop(String majorPop) {
        this.majorPop = majorPop;
    }

    public String getMicroPop() {
        return microPop;
    }

    public void setMicroPop(String microPop) {
        this.microPop = microPop;
    }

    public String getNocTeam() {
        return nocTeam;
    }

    public void setNocTeam(String nocTeam) {
        this.nocTeam = nocTeam;
    }

    public String getNodeOwner() {
        return nodeOwner;
    }

    public void setNodeOwner(String nodeOwner) {
        this.nodeOwner = nodeOwner;
    }

    public String getNodeOwnerOther() {
        return nodeOwnerOther;
    }

    public void setNodeOwnerOther(String nodeOwnerOther) {
        this.nodeOwnerOther = nodeOwnerOther;
    }

    public String getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(String poolSize) {
        this.poolSize = poolSize;
    }

    public String getPopName() {
        return popName;
    }

    public void setPopName(String popName) {
        this.popName = popName;
    }

    public String getPopOwner() {
        return popOwner;
    }

    public void setPopOwner(String popOwner) {
        this.popOwner = popOwner;
    }

    public String getPopType() {
        return popType;
    }

    public void setPopType(String popType) {
        this.popType = popType;
    }

    public String getPowerRating() {
        return powerRating;
    }

    public void setPowerRating(String powerRating) {
        this.powerRating = powerRating;
    }

    public String getPowerType() {
        return powerType;
    }

    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }

    public String getRingType() {
        return ringType;
    }

    public void setRingType(String ringType) {
        this.ringType = ringType;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getSpareScrap() {
        return spareScrap;
    }

    public void setSpareScrap(String spareScrap) {
        this.spareScrap = spareScrap;
    }

    public String getSubnetIpPool1() {
        return subnetIpPool1;
    }

    public void setSubnetIpPool1(String subnetIpPool1) {
        this.subnetIpPool1 = subnetIpPool1;
    }

    public String getSubnetIpPool2() {
        return subnetIpPool2;
    }

    public void setSubnetIpPool2(String subnetIpPool2) {
        this.subnetIpPool2 = subnetIpPool2;
    }

    public String getSwitchIpPool() {
        return switchIpPool;
    }

    public void setSwitchIpPool(String switchIpPool) {
        this.switchIpPool = switchIpPool;
    }

    public String getSwitchManagementIp() {
        return switchManagementIp;
    }

    public void setSwitchManagementIp(String switchManagementIp) {
        this.switchManagementIp = switchManagementIp;
    }

    public String getSwitchManagementVlan() {
        return switchManagementVlan;
    }

    public void setSwitchManagementVlan(String switchManagementVlan) {
        this.switchManagementVlan = switchManagementVlan;
    }

    public String getSwitchSubnetMask() {
        return switchSubnetMask;
    }

    public void setSwitchSubnetMask(String switchSubnetMask) {
        this.switchSubnetMask = switchSubnetMask;
    }

    public String getVlan() {
        return vlan;
    }

    public void setVlan(String vlan) {
        this.vlan = vlan;
    }

    public String getEorWimaxOrderSite() {
        return eorWimaxOrderSite;
    }

    public void setEorWimaxOrderSite(String eorWimaxOrderSite) {
        this.eorWimaxOrderSite = eorWimaxOrderSite;
    }

    public String getSiteEnd() {
        return siteEnd;
    }

    public void setSiteEnd(String siteEnd) {
        this.siteEnd = siteEnd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getHostName1() {
        return hostName1;
    }

    public void setHostName1(String hostName1) {
        this.hostName1 = hostName1;
    }

    public String getHostName2() {
        return hostName2;
    }

    public void setHostName2(String hostName2) {
        this.hostName2 = hostName2;
    }
}
