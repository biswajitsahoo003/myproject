package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nwa_eor_equip_details")
public class NwaEorEquipDetails implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="order_code")
    private String orderCode;

    @Column(name = "a_end_mux_name")
    private String aEndMuxName;

    @Column(name = "a_end_mux_port")
    private String aEndMuxPort;

    @Column(name = "a_end_mux_port2")
    private String aEndMuxPort2;

    @Column(name = "aggr_parent_router_ip")
    private String aggrParentRouterIp;

    @Column(name = "area")
    private String area;

    @Column(name = "area_other")
    private String areaOther;

    @Column(name = "bldg")
    private String bldg;

    @Column(name = "bldg_other")
    private String bldgOther;

    @Column(name = "city")
    private String city;

    @Column(name = "city_other")
    private String cityOther;

    @Column(name = "country")
    private String country;

    @Column(name = "coverage")
    private String coverage;

    @Column(name = "dcn_req")
    private String dcnReq;

    @Column(name = "device_sub_type")
    private String deviceSubType;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "end_mux_address")
    private String endMuxAddress;

    @Column(name = "eor_equip_status")
    private String eorEquipStatus;

    @Column(name = "eqpmnt_ip")
    private String eqpmntIp;

    @Column(name = "eqpmnt_name")
    private String eqpmntName;

    @Column(name = "eqpmnt_port")
    private String eqpmntPort;

    @Column(name = "equipment_type")
    private String eqpmntType;

    @Column(name = "floor")
    private String floor;

    @Column(name = "hostname")
    private String hostName;

    @Column(name = "inter_fibre_req")
    private String interFibreReq;

    @Column(name = "interface_to_parent")
    private String interfaceToParent;

    @Column(name = "interface_type")
    private String interfaceType;

    @Column(name = "is_man_equipment")
    private String isManEquipment;

    @Column(name = "is_switch_ip_requird")
    private String isSwitchIpRequird;

    @Column(name = "major_pop")
    private String majorPop;

    @Column(name = "micro_pop")
    private String microPop;

    @Column(name = "noc_team")
    private String nocTeam;

    @Column(name = "node_owner")
    private String nodeOwner;

    @Column(name = "node_owner_other")
    private String nodeOwnerOther;

    @Column(name = "pool_size")
    private String poolSize;

    @Column(name = "pop_name")
    private String popName;

    @Column(name = "pop_owner")
    private String popOwner;

    @Column(name = "pop_type")
    private String popType;

    @Column(name = "pop_address")
    private String popAddress;

    @Column(name = "power_rating")
    private String powerRating;

    @Column(name = "power_type")
    private String powerType;

    @Column(name = "ring_type")
    private String ringType;

    @Column(name = "site_address")
    private String siteAddress;

    @Column(name = "spare_scrap")
    private String spareScrap;

    @Column(name = "subnet_ip_pool_1")
    private String subnetIpPool1;

    @Column(name = "subnet_ip_pool_2")
    private String subnetIpPool2;

    @Column(name = "switch_ip_pool")
    private String switchIpPool;

    @Column(name = "switch_management_ip")
    private String switchManagementIp;

    @Column(name = "switch_management_vlan")
    private String switchManagementVlan;

    @Column(name = "switch_subnet_mask")
    private String switchSubnetMask;

    @Column(name = "vlan")
    private String vlan;

    @Column(name = "eor_wimax_order_site")
    private String eorWimaxOrderSite;

    @Column(name = "site_end")
    private String siteEnd;

    @Column(name = "state")
    private String state;

    @Column(name="site_name")
    private String siteName;

    @Column(name="site_id")
    private String siteId;

    @Column(name="rack_unit_space")
    private String rackUnitSpace;

    @Column(name="city_tier")
    private String cityTier;

    @Column(name="tacacs_required")
    private String tacacsRequired;

    @Column(name = "make")
    String make;

    @Column(name = "model")
    String model;

    @Column(name = "owner")
    String owner;

    @Column(name = "role")
    String role;

    @Column(name = "hostname1")
    private String hostName1;

    @Column(name = "aggr1_eqpmnt_port2")
    private String aggr1EqpmntPort2;

    @Column(name = "aggr1_parent_router")
    private String aggr1ParentRouter;

    @Column(name = "aggr1_area")
    private String aggr1Area;

    @Column(name = "aggr1_area_other")
    private  String aggr1AreaOther;

    @Column(name = "aggr1_bldg")
    private String aggr1Bldg;

    @Column(name = "aggr1_bldg_other")
    private String aggr1BldgOther;

    @Column(name = "aggr1_city")
    private String aggr1City;

    @Column(name = "aggr1_city_other")
    private String aggr1CityOther;

    @Column(name = "aggr1_country")
    private String aggr1Country;

    @Column(name = "aggr1_end_mux_address")
    private String aggr1EndMuxAddress;

    @Column(name = "aggr1_eqpmnt_ip")
    private String aggr1EqpmntIp;

    @Column(name = "aggr1_eqpmnt_name")
    private String aggr1EqpmntName;

    @Column(name = "aggr1_eqpmnt_port")
    private String aggr1EqpmntPort;

    @Column(name = "aggr1_eqpmnt_type")
    private String aggr1EqpmntType;

    @Column(name = "aggr1_floor")
    private String aggr1Floor;

    @Column(name = "aggr1_major_pop")
    private String aggr1MajorPop;

    @Column(name = "aggr1_micra_pop")
    private String aggr1MicraPop;

    @Column(name = "aggr1_site_address")
    private String aggr1SiteAddress;

    @Column(name = "aggr1_state")
    private String aggr1State;

    @Column(name = "aggr1_site_name")
    private String aggr1SiteName;

    @Column(name = "aggr1_pop_address")
    private String aggr1PopAddress;

    @Column(name = "aggr1_site_id")
    private String aggr1SiteId;

    @Column(name = "hostname2")
    private String hostName2;

    @Column(name = "aggr2_eqpmnt_port2")
    private String aggr2EqpmntPort2;

    @Column(name = "aggr2_parent_router")
    private String aggr2ParentRouter;

    @Column(name = "aggr2_area")
    private String aggr2Area;

    @Column(name = "aggr2_area_other")
    private  String aggr2AreaOther;

    @Column(name = "aggr2_floor")
    private String aggr2Floor;

    @Column(name = "aggr2_bldg")
    private String aggr2Bldg;

    @Column(name = "aggr2_bldg_other")
    private String aggr2BldgOther;

    @Column(name = "aggr2_city")
    private String aggr2City;

    @Column(name = "aggr2_city_other")
    private String aggr2CityOther;

    @Column(name = "aggr2_country")
    private String aggr2Country;

    @Column(name = "aggr2_end_mux_address")
    private String aggr2EndMuxAddress;

    @Column(name = "aggr2_eqpmnt_ip")
    private String aggr2EqpmntIp;

    @Column(name = "aggr2_eqpmnt_name")
    private String aggr2EqpmntName;

    @Column(name = "aggr2_eqpmnt_port")
    private String aggr2EqpmntPort;

    @Column(name = "aggr2_eqpmnt_type")
    private String aggr2EqpmntType;

    @Column(name = "aggr2_major_pop")
    private String aggr2MajorPop;

    @Column(name = "aggr2_micra_pop")
    private String aggr2MicraPop;

    @Column(name = "aggr2_site_address")
    private String aggr2SiteAddress;

    @Column(name = "aggr2_state")
    private String aggr2State;

    @Column(name = "aggr2_site_name")
    private String aggr2SiteName;

    @Column(name = "aggr2_pop_address")
    private String aggr2PopAddress;

    @Column(name = "aggr2_site_id")
    private String aggr2SiteId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private	ScOrder scOrder;

    @Column(name = "sdnoc_sanoc")
    private String sdNocSaNoc;

    @Column(name = "ethernet_access")
    private String ethernetAccess;

    /*@Column(name = "ethernet_access_eor_type")
    private String getEthernetAccessEorType;*/

    @Column(name = "network_type")
    private String networkType;

    @Column(name = "node_id")
    private String nodeId;

    @Column(name = "a_end_mux_name2")
    private String aEndMuxName2;

    @Column(name = "second_end_mux_port1")
    private String secondEndMuxPort1;

    @Column(name = "second_end_mux_port2")
    private String getSecondEndMuxPort2;

    @Column(name = "wearhouse_location")
    private String wearhouseLocation;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "termination_link")
    private String terminationLink;

    @Column(name = "priority")
    private String priority;

    @Column(name = "rack_feasibility")
    private String rackFeasibility;

    @Column(name = "fiber_feasibility")
    private String fiberFeasibility;

    @Column(name = "infra_feasibility")
    private String infraFeasibility;

    @Column(name = "gspi_branch")
    private String gspiBranch;

    @Column(name = "parent_order")
    private String parentOrder;

    @Column(name = "child_order")
    private String childOrder;

    @Column(name = "sibling")
    private String sibling;

    @Column(name = "node_name")
    private String nodeName;

    @Column(name = "node_type")
    private String nodeType;

    @Column(name = "management_ip_address")
    private String mgmtIPAddress;

    @Column(name = "coverage2")
    private String coverage2;

    @Column(name = "a_end_remote_node")
    private String aEndRemoteNode;

    @Column(name = "port_name1")
    private String portName1;

    @Column(name = "port_description")
    private String portDescription;

    @Column(name = "port_name2")
    private String portName2;

    @Column(name = "port_description2")
    private String portDescription2;

    @Column(name = "config_uplink")
    private String configUplink;

    @Column(name = "config_uplink2")
    private String configUplink2;

    @Column(name = "config_uplink3")
    private String configUplink3;

    @Column(name = "config_uplink4")
    private String configUplink4;

    @Column(name = "z_end_remote_node")
    private String zEndRemoteNode;

    @Column(name = "z_end_eor_id")
    private String zEndEorId;

    @Column(name = "a_end_local_port")
    private String aEndLocalPort;

    @Column(name = "cfm_mip")
    private String cfmMip;

    @Column(name = "z_end_local_port")
    private String zEndLocalPort;

    @Column(name = "device_role")
    private String deviceRole;

    @Column(name = "management_vlan")
    private String mgmntvlan;

    @Column(name = "vlan_interface_description")
    private String vlanInterfaceDes;

    @Column(name = "cfm_default")
    private String cfmDefault;

    @Column(name = "city_name1")
    private String cityName1;

    @Column(name = "switch_location")
    private String switchLocation;

    @Column(name = "node_name3")
    private String nodeName3;

    @Column(name = "control_vlan_id")
    private String controlVlanId;

    @Column(name = "erps_ring_id")
    private String erpsRingId;

    @Column(name = "gate_way_ip_address_ntp")
    private String gateWayIpAddressNtp;

    @Column(name = "protected_instance_id")
    private String protectedInstanceId;

    @Column(name = "subring")
    private String subring;

    @Column(name = "power_feasibility")
    private String powerFeasibility;

    @Column(name = "port_description3")
    private String portDescription3;

    @Column(name = "port_description4")
    private String portDescription4;

    @Column(name = "ethernet_access_eor_type")
    private String ethernetAccessEorType;

    @Column(name = "switch_name")
    private String switchName;

    @Column(name =  "switch_ip")
    private String switchIp;

    @Column(name = "is_switch_pool_required")
    private String isSwitchPoolRequired;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setMicroPop(String micraPop) {
        this.microPop = micraPop;
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

    public ScOrder getScOrder() {
        return scOrder;
    }

    public void setScOrder(ScOrder scOrder) {
        this.scOrder = scOrder;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getRackUnitSpace() {
        return rackUnitSpace;
    }

    public void setRackUnitSpace(String rackUnitSpace) {
        this.rackUnitSpace = rackUnitSpace;
    }

    public String getCityTier() {
        return cityTier;
    }

    public void setCityTier(String cityTier) {
        this.cityTier = cityTier;
    }

    public String getTacacsRequired() {
        return tacacsRequired;
    }

    public void setTacacsRequired(String tacacsRequired) {
        this.tacacsRequired = tacacsRequired;
    }

    public String getAggr1EqpmntPort2() {
        return aggr1EqpmntPort2;
    }

    public void setAggr1EqpmntPort2(String aggr1EqpmntPort2) {
        this.aggr1EqpmntPort2 = aggr1EqpmntPort2;
    }

    public String getAggr1ParentRouter() {
        return aggr1ParentRouter;
    }

    public void setAggr1ParentRouter(String aggr1ParentRouter) {
        this.aggr1ParentRouter = aggr1ParentRouter;
    }

    public String getAggr1Area() {
        return aggr1Area;
    }

    public void setAggr1Area(String aggr1Area) {
        this.aggr1Area = aggr1Area;
    }

    public String getAggr1AreaOther() {
        return aggr1AreaOther;
    }

    public void setAggr1AreaOther(String aggr1AreaOther) {
        this.aggr1AreaOther = aggr1AreaOther;
    }

    public String getAggr1Bldg() {
        return aggr1Bldg;
    }

    public void setAggr1Bldg(String aggr1Bldg) {
        this.aggr1Bldg = aggr1Bldg;
    }

    public String getAggr1BldgOther() {
        return aggr1BldgOther;
    }

    public void setAggr1BldgOther(String aggr1BldgOther) {
        this.aggr1BldgOther = aggr1BldgOther;
    }

    public String getAggr1City() {
        return aggr1City;
    }

    public void setAggr1City(String aggr1City) {
        this.aggr1City = aggr1City;
    }

    public String getAggr1CityOther() {
        return aggr1CityOther;
    }

    public void setAggr1CityOther(String aggr1CityOther) {
        this.aggr1CityOther = aggr1CityOther;
    }

    public String getAggr1Country() {
        return aggr1Country;
    }

    public void setAggr1Country(String aggr1Country) {
        this.aggr1Country = aggr1Country;
    }

    public String getAggr1EndMuxAddress() {
        return aggr1EndMuxAddress;
    }

    public void setAggr1EndMuxAddress(String aggr1EndMuxAddress) {
        this.aggr1EndMuxAddress = aggr1EndMuxAddress;
    }

    public String getAggr1EqpmntIp() {
        return aggr1EqpmntIp;
    }

    public void setAggr1EqpmntIp(String aggr1EqpmntIp) {
        this.aggr1EqpmntIp = aggr1EqpmntIp;
    }

    public String getAggr1EqpmntName() {
        return aggr1EqpmntName;
    }

    public void setAggr1EqpmntName(String aggr1EqpmntName) {
        this.aggr1EqpmntName = aggr1EqpmntName;
    }

    public String getAggr1EqpmntPort() {
        return aggr1EqpmntPort;
    }

    public void setAggr1EqpmntPort(String aggr1EqpmntPort) {
        this.aggr1EqpmntPort = aggr1EqpmntPort;
    }

    public String getAggr1EqpmntType() {
        return aggr1EqpmntType;
    }

    public void setAggr1EqpmntType(String aggr1EqpmntType) {
        this.aggr1EqpmntType = aggr1EqpmntType;
    }

    public String getAggr1Floor() {
        return aggr1Floor;
    }

    public void setAggr1Floor(String aggr1Floor) {
        this.aggr1Floor = aggr1Floor;
    }

    public String getAggr1MajorPop() {
        return aggr1MajorPop;
    }

    public void setAggr1MajorPop(String aggr1MajorPop) {
        this.aggr1MajorPop = aggr1MajorPop;
    }

    public String getAggr1MicraPop() {
        return aggr1MicraPop;
    }

    public void setAggr1MicraPop(String aggr1MicraPop) {
        this.aggr1MicraPop = aggr1MicraPop;
    }

    public String getAggr1SiteAddress() {
        return aggr1SiteAddress;
    }

    public void setAggr1SiteAddress(String aggr1SiteAddress) {
        this.aggr1SiteAddress = aggr1SiteAddress;
    }

    public String getAggr1State() {
        return aggr1State;
    }

    public void setAggr1State(String aggr1State) {
        this.aggr1State = aggr1State;
    }

    public String getAggr1SiteName() {
        return aggr1SiteName;
    }

    public void setAggr1SiteName(String aggr1SiteName) {
        this.aggr1SiteName = aggr1SiteName;
    }

    public String getAggr1PopAddress() {
        return aggr1PopAddress;
    }

    public void setAggr1PopAddress(String aggr1PopAddress) {
        this.aggr1PopAddress = aggr1PopAddress;
    }

    public String getAggr1SiteId() {
        return aggr1SiteId;
    }

    public void setAggr1SiteId(String aggr1SiteId) {
        this.aggr1SiteId = aggr1SiteId;
    }

    public String getAggr2EqpmntPort2() {
        return aggr2EqpmntPort2;
    }

    public void setAggr2EqpmntPort2(String aggr2EqpmntPort2) {
        this.aggr2EqpmntPort2 = aggr2EqpmntPort2;
    }

    public String getAggr2ParentRouter() {
        return aggr2ParentRouter;
    }

    public void setAggr2ParentRouter(String aggr2ParentRouter) {
        this.aggr2ParentRouter = aggr2ParentRouter;
    }

    public String getAggr2Area() {
        return aggr2Area;
    }

    public void setAggr2Area(String aggr2Area) {
        this.aggr2Area = aggr2Area;
    }

    public String getAggr2AreaOther() {
        return aggr2AreaOther;
    }

    public void setAggr2AreaOther(String aggr2AreaOther) {
        this.aggr2AreaOther = aggr2AreaOther;
    }

    public String getAggr2Floor() {
        return aggr2Floor;
    }

    public void setAggr2Floor(String aggr2Floor) {
        this.aggr2Floor = aggr2Floor;
    }

    public String getAggr2Bldg() {
        return aggr2Bldg;
    }

    public void setAggr2Bldg(String aggr2Bldg) {
        this.aggr2Bldg = aggr2Bldg;
    }

    public String getAggr2BldgOther() {
        return aggr2BldgOther;
    }

    public void setAggr2BldgOther(String aggr2BldgOther) {
        this.aggr2BldgOther = aggr2BldgOther;
    }

    public String getAggr2City() {
        return aggr2City;
    }

    public void setAggr2City(String aggr2City) {
        this.aggr2City = aggr2City;
    }

    public String getAggr2CityOther() {
        return aggr2CityOther;
    }

    public void setAggr2CityOther(String aggr2CityOther) {
        this.aggr2CityOther = aggr2CityOther;
    }

    public String getAggr2Country() {
        return aggr2Country;
    }

    public void setAggr2Country(String aggr2Country) {
        this.aggr2Country = aggr2Country;
    }

    public String getAggr2EndMuxAddress() {
        return aggr2EndMuxAddress;
    }

    public void setAggr2EndMuxAddress(String aggr2EndMuxAddress) {
        this.aggr2EndMuxAddress = aggr2EndMuxAddress;
    }

    public String getAggr2EqpmntIp() {
        return aggr2EqpmntIp;
    }

    public void setAggr2EqpmntIp(String aggr2EqpmntIp) {
        this.aggr2EqpmntIp = aggr2EqpmntIp;
    }

    public String getAggr2EqpmntName() {
        return aggr2EqpmntName;
    }

    public void setAggr2EqpmntName(String aggr2EqpmntName) {
        this.aggr2EqpmntName = aggr2EqpmntName;
    }

    public String getAggr2EqpmntPort() {
        return aggr2EqpmntPort;
    }

    public void setAggr2EqpmntPort(String aggr2EqpmntPort) {
        this.aggr2EqpmntPort = aggr2EqpmntPort;
    }

    public String getAggr2EqpmntType() {
        return aggr2EqpmntType;
    }

    public void setAggr2EqpmntType(String aggr2EqpmntType) {
        this.aggr2EqpmntType = aggr2EqpmntType;
    }

    public String getAggr2MajorPop() {
        return aggr2MajorPop;
    }

    public void setAggr2MajorPop(String aggr2MajorPop) {
        this.aggr2MajorPop = aggr2MajorPop;
    }

    public String getAggr2MicraPop() {
        return aggr2MicraPop;
    }

    public void setAggr2MicraPop(String aggr2MicraPop) {
        this.aggr2MicraPop = aggr2MicraPop;
    }

    public String getAggr2SiteAddress() {
        return aggr2SiteAddress;
    }

    public void setAggr2SiteAddress(String aggr2SiteAddress) {
        this.aggr2SiteAddress = aggr2SiteAddress;
    }

    public String getAggr2State() {
        return aggr2State;
    }

    public void setAggr2State(String aggr2State) {
        this.aggr2State = aggr2State;
    }

    public String getAggr2SiteName() {
        return aggr2SiteName;
    }

    public void setAggr2SiteName(String aggr2SiteName) {
        this.aggr2SiteName = aggr2SiteName;
    }

    public String getAggr2PopAddress() {
        return aggr2PopAddress;
    }

    public void setAggr2PopAddress(String aggr2PopAddress) {
        this.aggr2PopAddress = aggr2PopAddress;
    }

    public String getAggr2SiteId() {
        return aggr2SiteId;
    }

    public void setAggr2SiteId(String aggr2SiteId) {
        this.aggr2SiteId = aggr2SiteId;
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

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPopAddress() {
        return popAddress;
    }

    public void setPopAddress(String popAddress) {
        this.popAddress = popAddress;
    }

    public String getSdNocSaNoc() {
        return sdNocSaNoc;
    }

    public void setSdNocSaNoc(String sdNocSaNoc) {
        this.sdNocSaNoc = sdNocSaNoc;
    }

    public String getEthernetAccess() {
        return ethernetAccess;
    }

    public void setEthernetAccess(String ethernetAccess) {
        this.ethernetAccess = ethernetAccess;
    }

    /*public String getGetEthernetAccessEorType() {
        return getEthernetAccessEorType;
    }

    public void setGetEthernetAccessEorType(String getEthernetAccessEorType) {
        this.getEthernetAccessEorType = getEthernetAccessEorType;
    }*/

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getaEndMuxName2() {
        return aEndMuxName2;
    }

    public void setaEndMuxName2(String aEndMuxName2) {
        this.aEndMuxName2 = aEndMuxName2;
    }

    public String getSecondEndMuxPort1() {
        return secondEndMuxPort1;
    }

    public void setSecondEndMuxPort1(String secondEndMuxPort1) {
        this.secondEndMuxPort1 = secondEndMuxPort1;
    }

    public String getGetSecondEndMuxPort2() {
        return getSecondEndMuxPort2;
    }

    public void setGetSecondEndMuxPort2(String getSecondEndMuxPort2) {
        this.getSecondEndMuxPort2 = getSecondEndMuxPort2;
    }

    public String getWearhouseLocation() {
        return wearhouseLocation;
    }

    public void setWearhouseLocation(String wearhouseLocation) {
        this.wearhouseLocation = wearhouseLocation;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTerminationLink() {
        return terminationLink;
    }

    public void setTerminationLink(String terminationLink) {
        this.terminationLink = terminationLink;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRackFeasibility() {
        return rackFeasibility;
    }

    public void setRackFeasibility(String rackFeasibility) {
        this.rackFeasibility = rackFeasibility;
    }

    public String getFiberFeasibility() {
        return fiberFeasibility;
    }

    public void setFiberFeasibility(String fiberFeasibility) {
        this.fiberFeasibility = fiberFeasibility;
    }

    public String getInfraFeasibility() {
        return infraFeasibility;
    }

    public void setInfraFeasibility(String infraFeasibility) {
        this.infraFeasibility = infraFeasibility;
    }

    public String getGspiBranch() {
        return gspiBranch;
    }

    public void setGspiBranch(String gspiBranch) {
        this.gspiBranch = gspiBranch;
    }

    public String getParentOrder() {
        return parentOrder;
    }

    public void setParentOrder(String parentOrder) {
        this.parentOrder = parentOrder;
    }

    public String getChildOrder() {
        return childOrder;
    }

    public void setChildOrder(String childOrder) {
        this.childOrder = childOrder;
    }

    public String getSibling() {
        return sibling;
    }

    public void setSibling(String sibling) {
        this.sibling = sibling;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getMgmtIPAddress() {
        return mgmtIPAddress;
    }

    public void setMgmtIPAddress(String mgmtIPAddress) {
        this.mgmtIPAddress = mgmtIPAddress;
    }

    public String getCoverage2() {
        return coverage2;
    }

    public void setCoverage2(String coverage2) {
        this.coverage2 = coverage2;
    }

    public String getaEndRemoteNode() {
        return aEndRemoteNode;
    }

    public void setaEndRemoteNode(String aEndRemoteNode) {
        this.aEndRemoteNode = aEndRemoteNode;
    }

    public String getPortName1() {
        return portName1;
    }

    public void setPortName1(String portName1) {
        this.portName1 = portName1;
    }

    public String getPortDescription() {
        return portDescription;
    }

    public void setPortDescription(String portDescription) {
        this.portDescription = portDescription;
    }

    public String getPortName2() {
        return portName2;
    }

    public void setPortName2(String portName2) {
        this.portName2 = portName2;
    }

    public String getPortDescription2() {
        return portDescription2;
    }

    public void setPortDescription2(String portDescription2) {
        this.portDescription2 = portDescription2;
    }

    public String getConfigUplink() {
        return configUplink;
    }

    public void setConfigUplink(String configUplink) {
        this.configUplink = configUplink;
    }

    public String getConfigUplink2() {
        return configUplink2;
    }

    public void setConfigUplink2(String configUplink2) {
        this.configUplink2 = configUplink2;
    }

    public String getConfigUplink3() {
        return configUplink3;
    }

    public void setConfigUplink3(String configUplink3) {
        this.configUplink3 = configUplink3;
    }

    public String getConfigUplink4() {
        return configUplink4;
    }

    public void setConfigUplink4(String configUplink4) {
        this.configUplink4 = configUplink4;
    }

    public String getzEndRemoteNode() {
        return zEndRemoteNode;
    }

    public void setzEndRemoteNode(String zEndRemoteNode) {
        this.zEndRemoteNode = zEndRemoteNode;
    }

    public String getzEndEorId() {
        return zEndEorId;
    }

    public void setzEndEorId(String zEndEorId) {
        this.zEndEorId = zEndEorId;
    }

    public String getaEndLocalPort() {
        return aEndLocalPort;
    }

    public void setaEndLocalPort(String aEndLocalPort) {
        this.aEndLocalPort = aEndLocalPort;
    }

    public String getCfmMip() {
        return cfmMip;
    }

    public void setCfmMip(String cfmMip) {
        this.cfmMip = cfmMip;
    }

    public String getzEndLocalPort() {
        return zEndLocalPort;
    }

    public void setzEndLocalPort(String zEndLocalPort) {
        this.zEndLocalPort = zEndLocalPort;
    }

    public String getDeviceRole() {
        return deviceRole;
    }

    public void setDeviceRole(String deviceRole) {
        this.deviceRole = deviceRole;
    }

    public String getMgmntvlan() {
        return mgmntvlan;
    }

    public void setMgmntvlan(String mgmntvlan) {
        this.mgmntvlan = mgmntvlan;
    }

    public String getVlanInterfaceDes() {
        return vlanInterfaceDes;
    }

    public void setVlanInterfaceDes(String vlanInterfaceDes) {
        this.vlanInterfaceDes = vlanInterfaceDes;
    }

    public String getCfmDefault() {
        return cfmDefault;
    }

    public void setCfmDefault(String cfmDefault) {
        this.cfmDefault = cfmDefault;
    }

    public String getCityName1() {
        return cityName1;
    }

    public void setCityName1(String cityName1) {
        this.cityName1 = cityName1;
    }

    public String getSwitchLocation() {
        return switchLocation;
    }

    public void setSwitchLocation(String switchLocation) {
        this.switchLocation = switchLocation;
    }

    public String getNodeName3() {
        return nodeName3;
    }

    public void setNodeName3(String nodeName3) {
        this.nodeName3 = nodeName3;
    }

    public String getControlVlanId() {
        return controlVlanId;
    }

    public void setControlVlanId(String controlVlanId) {
        this.controlVlanId = controlVlanId;
    }

    public String getErpsRingId() {
        return erpsRingId;
    }

    public void setErpsRingId(String erpsRingId) {
        this.erpsRingId = erpsRingId;
    }

    public String getGateWayIpAddressNtp() {
        return gateWayIpAddressNtp;
    }

    public void setGateWayIpAddressNtp(String gateWayIpAddressNtp) {
        this.gateWayIpAddressNtp = gateWayIpAddressNtp;
    }

    public String getProtectedInstanceId() {
        return protectedInstanceId;
    }

    public void setProtectedInstanceId(String protectedInstanceId) {
        this.protectedInstanceId = protectedInstanceId;
    }

    public String getSubring() {
        return subring;
    }

    public void setSubring(String subring) {
        this.subring = subring;
    }

    public String getPowerFeasibility() {
        return powerFeasibility;
    }

    public void setPowerFeasibility(String powerFeasibility) {
        this.powerFeasibility = powerFeasibility;
    }

    public String getPortDescription3() {
        return portDescription3;
    }

    public void setPortDescription3(String portDescription3) {
        this.portDescription3 = portDescription3;
    }

    public String getPortDescription4() {
        return portDescription4;
    }

    public void setPortDescription4(String portDescription4) {
        this.portDescription4 = portDescription4;
    }

    public String getEthernetAccessEorType() {
        return ethernetAccessEorType;
    }

    public void setEthernetAccessEorType(String ethernetAccessEorType) {
        this.ethernetAccessEorType = ethernetAccessEorType;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getSwitchIp() {
        return switchIp;
    }

    public void setSwitchIp(String switchIp) {
        this.switchIp = switchIp;
    }

    public String getIsSwitchPoolRequired() {
        return isSwitchPoolRequired;
    }

    public void setIsSwitchPoolRequired(String isSwitchPoolRequired) {
        this.isSwitchPoolRequired = isSwitchPoolRequired;
    }
}
