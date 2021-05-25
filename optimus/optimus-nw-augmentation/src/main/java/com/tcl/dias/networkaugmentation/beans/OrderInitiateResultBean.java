package com.tcl.dias.networkaugmentation.beans;

import com.tcl.dias.networkaugment.entity.entities.MstStatus;
import org.joda.time.DateTime;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

public class OrderInitiateResultBean {

    // Order -
    String uuid;
    String opOrderCode;
    String orderType;
    String subject;
    String projectType;
    String scenarioType;
    String technologyType;
    String pmName;
    String pmContactEmail;
    String originatorName;
    String originatorContactNumber;
    Timestamp orderCreationDate;
    String originatorGroupId;
    String orderStatus;
    String projectName;
    String eorConfirmedBy;
    //String cityTier;
    String rfmRfs;
    String rfmRfsAssignTo;

    //Task details by order
    String taskName;
    Timestamp taskClaimTime;
    String taskStatus;

    // ServiceDetails -
    String sdUuid;
    String serviceGroupId;
    String scOrderUuid;
    String smName;
    String smEmail;
    MstStatus mstStatus;


    // EOREquipDetails -
    String hostName;
    String aEndMuxName;
    String aEndMuxPort;
    String aEndMuxPort2;
    String aggrParentRouterIp;
    String area;
    String areaOther;
    String bldg;
    String bldgOther;
    String city;
    String cityOther;
    String country;
    String coverage;
    String dcnReq;
    String deviceSubType;
    String deviceType;
    String endMuxAddress;
    String eorequipmentStatus;
    String eorEquipStatus;
    String eqpmntIp;
    String eqpmntName;
    String eqpmntPort;
    String eqpmntType;
    String floor;
    String interFibreReq;
    String interfaceToParent;
    String interfaceType;
    String isManEquipment;
    String isSwitchIpRequird;
    String majorPop;
    String microPop;
    String nocTeam;
    String nodeOwner;
    String nodeOwnerOther;
    String poolSize;
    String popName;
    String popOwner;
    String popType;
    String powerRating;
    String powerType;
    String ringType;
    String siteAddress;
    String spareScrap;
    String subnetIpPool1;
    String subnetIpPool2;
    String switchIpPool;
    String switchManagementIp;
    String switchManagementVlan;
    String switchSubnetMask;
    String vlan;
    String eorWimaxOrderSite;
    String siteEnd;
    String state;
    String siteName;
    String siteId;
    String rackUnitSpace;
    String cityTier;
    String tacacsRequired;
    String make;
    String model;
    String owner;
    String role;
    String hostName1;
    String aggr1EqpmntPort2;
    String aggr1ParentRouter;
    String aggr1Area;
    String aggr1AreaOther;
    String aggr1Bldg;
    String aggr1BldgOther;
    String aggr1City;
    String aggr1CityOther;
    String aggr1Country;
    String aggr1EndMuxAddress;
    String aggr1EqpmntIp;
    String aggr1EqpmntName;
    String aggr1EqpmntPort;
    String aggr1EqpmntType;
    String aggr1Floor;
    String aggr1MajorPop;
    String aggr1MicraPop;
    String aggr1SiteAddress;
    String aggr1State;
    String aggr1SiteName;
    String aggr1PopAddress;
    String aggr1SiteId;
    String hostName2;
    String aggr2EqpmntPort2;
    String aggr2ParentRouter;
    String aggr2Area;
    String aggr2AreaOther;
    String aggr2Floor;
    String aggr2Bldg;
    String aggr2BldgOther;
    String aggr2City;
    String aggr2CityOther;
    String aggr2Country;
    String aggr2EndMuxAddress;
    String aggr2EqpmntIp;
    String aggr2EqpmntName;
    String aggr2EqpmntPort;
    String aggr2EqpmntType;
    String aggr2MajorPop;
    String aggr2MicraPop;
    String aggr2SiteAddress;
    String aggr2State;
    String aggr2SiteName;
    String aggr2PopAddress;
    String aggr2SiteId;

    String sdNocSaNoc;
   // String ethernetAccess;
    String getEthernetAccessEorType;
   // String networkType;
    String nodeId;
    String aEndMuxName2;
    String secondEndMuxPort1;
    String getSecondEndMuxPort2;
    String wearhouseLocation;
    String serialNumber;
    String terminationLink;
    String priority;
    String rackFeasibility;
  //  String fiberFeasibility;
  //  String infraFeasibility;
  //  String gspiBranch;
    String parentOrder;
    String childOrder;
    String sibling;
    String nodeName;
    String nodeType;
    String mgmtIPAddress;
    String coverage2;
    String aEndRemoteNode;
    String portName1;
    String portDescription;
    String portName2;
    String portDescription2;
    String configUplink;
    String configUplink2;
    String configUplink3;
    String configUplink4;
    String zEndRemoteNode;
    String zEndEorId;
    String aEndLocalPort;
    String cfmMip;
    String zEndLocalPort;
    String deviceRole;
    String mgmntvlan;
    String vlanInterfaceDes;
    String cfmDefault;
    String cityName1;
    String switchLocation;
    String nodeName3;
 //   String controlVlanId;
 //   String erpsRingId;
 //   String gateWayIpAddressNtp;
    String protectedInstanceId;
    String subring;
    String portDescription3;
    String portDescription4;
    String switchName;
    String switchIp;
    String isSwitchIpPoolRequired;



    // AccessEORDetails -
    String orderCode;
    String aEndEorID;
    String aEndNeighbourHostName;
    String aEndNeighbourPortDetails;
    String bEndNeighbourPortDetails;
    String cfmMipLevel;
    String configureAEndUplinkPort;
    String controlVlanId;
    String descAEndNeighbour;
    String descNewDeviceAEndUplinkPort;
    String erpsRingId;
    String gateWayIpAddressNtp;
    String aedHostName;
    String initTemplate;
    String managementVlanDescription;

    // OrderDetailsExtnd
    String ethernetAccess;
    String ethernetAccessEorType;
    String fiberFeasibility;
    String fieldOpsTeam;
    String gspiBranch;
    String infraFeasibility;
    String ipPool;
    String isMuxDetailsChanged;
    String isTejasMumbaiPuneEci;
    String isTermExistingLinkReqd;
    String loopBackIp0;
    String loopBackIp1;
    String networkType;
    String poFrnNo;
    String iprm;
    String portFeasibility;
    String powerFeasibility;
    String serialNo;
    String softwareVersion;
    String warehouseLocation;
    String webReportAttached;
    String peDate;
    String peRequired;
    String fieldOps;
    String configrationManagment;
    String cramerTeam;

    //NwaRemarksDetails
    String nwaRemarksByUser;
    String nwaRemarksByUserGroup;
    String nwaRemarksJeopardyReason;
    String nwaRemarksReason;
    String comments;
    Timestamp nwaRemarksRemarksDate;
    String nwaRemarksScenarioType;

    //NwaLinkedOrderDetails
    String allMaterialReceived;
    String dispatchDetails;
    String mlGroup;
    String mrnType;
    String mAndLRequired;
    String siblingOrder;
    String linkedPe;
    String linkedOrderId;

    //Nwa Card Details
    String cardSrNumber;
    String sr_no;
    String cardType;
    String card_type;
    String uniqueId;
    String uid;
    String shelfDetails;
    String shelf_no;
    String shelfSlotDetails;
    String shelf_slot_no;
    String slotDetails;
    String card_slot_no;
    String cardDescription;
    String shelfType;
    String subSlotNo;
    String PopAddress;
    String sdnoc;
    String foDetails;
    private Map<String,Object> cardDetailsMap;
    private ArrayList<Map<String, Object>> nwaCardDetailsArrayList;

    //nwaBtsDetails
    String bhType;
    String deviceIp;
    String btsVlan;
    String circle;
    String eohsCategory;
    Timestamp infraAggrementExpiryDate;
    String infraProvider;
    String infraProviderSiteUpTime;
    String perAntennaDimension;
    String perOduWeight;
    String plannedDeinstallationDate;
    String plannedInstallationDate;
    String reasonForExist;
    //String siteId;
    //String siteName;
    String siteType;
    String btsSubnetMask;
    Timestamp tclAggrementStartDate;
    String wirelessTechSubType;

    public String getSr_no() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShelf_no() {
        return shelf_no;
    }

    public void setShelf_no(String shelf_no) {
        this.shelf_no = shelf_no;
    }

    public String getShelf_slot_no() {
        return shelf_slot_no;
    }

    public void setShelf_slot_no(String shelf_slot_no) {
        this.shelf_slot_no = shelf_slot_no;
    }

    public String getCard_slot_no() {
        return card_slot_no;
    }

    public void setCard_slot_no(String card_slot_no) {
        this.card_slot_no = card_slot_no;
    }

    public String getSubSlotNo() {
        return subSlotNo;
    }

    public void setSubSlotNo(String subSlotNo) {
        this.subSlotNo = subSlotNo;
    }

    private Map<String, Object> cardData;

    public Map<String, Object> getCardData() {
        return cardData;
    }

    public void setCardData(Map<String, Object> cardData) {
        this.cardData = cardData;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOpOrderCode() {
        return opOrderCode;
    }

    public void setOpOrderCode(String opOrderCode) {
        this.opOrderCode = opOrderCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getPmName() {
        return pmName;
    }

    public void setPmName(String pmName) {
        this.pmName = pmName;
    }

    public String getPmContactEmail() {
        return pmContactEmail;
    }

    public void setPmContactEmail(String pmContactEmail) {
        this.pmContactEmail = pmContactEmail;
    }

    public String getOriginatorName() {
        return originatorName;
    }

    public void setOriginatorName(String originatorName) {
        this.originatorName = originatorName;
    }

    public String getOriginatorContactNumber() {
        return originatorContactNumber;
    }

    public void setOriginatorContactNumber(String originatorContactNumber) {
        this.originatorContactNumber = originatorContactNumber;
    }

    public Timestamp getOrderCreationDate() {
        return orderCreationDate;
    }

    public void setOrderCreationDate(Timestamp orderCreationDate) {
        this.orderCreationDate = orderCreationDate;
    }

    public String getOriginatorGroupId() {
        return originatorGroupId;
    }

    public void setOriginatorGroupId(String originatorGroupId) {
        this.originatorGroupId = originatorGroupId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSdUuid() {
        return sdUuid;
    }

    public void setSdUuid(String sdUuid) {
        this.sdUuid = sdUuid;
    }

    public String getServiceGroupId() {
        return serviceGroupId;
    }

    public void setServiceGroupId(String serviceGroupId) {
        this.serviceGroupId = serviceGroupId;
    }

    public String getScOrderUuid() {
        return scOrderUuid;
    }

    public void setScOrderUuid(String scOrderUuid) {
        this.scOrderUuid = scOrderUuid;
    }

    public String getSmName() {
        return smName;
    }

    public void setSmName(String smName) {
        this.smName = smName;
    }

    public String getSmEmail() {
        return smEmail;
    }

    public void setSmEmail(String smEmail) {
        this.smEmail = smEmail;
    }

    public MstStatus getMstStatus() {
        return mstStatus;
    }

    public void setMstStatus(MstStatus mstStatus) {
        this.mstStatus = mstStatus;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public String getEorequipmentStatus() {
        return eorequipmentStatus;
    }

    public void setEorequipmentStatus(String eorequipmentStatus) {
        this.eorequipmentStatus = eorequipmentStatus;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getaEndEorID() {
        return aEndEorID;
    }

    public void setaEndEorID(String aEndEorID) {
        this.aEndEorID = aEndEorID;
    }

    public String getaEndNeighbourHostName() {
        return aEndNeighbourHostName;
    }

    public void setaEndNeighbourHostName(String aEndNeighbourHostName) {
        this.aEndNeighbourHostName = aEndNeighbourHostName;
    }

    public String getaEndNeighbourPortDetails() {
        return aEndNeighbourPortDetails;
    }

    public void setaEndNeighbourPortDetails(String aEndNeighbourPortDetails) {
        this.aEndNeighbourPortDetails = aEndNeighbourPortDetails;
    }

    public String getbEndNeighbourPortDetails() {
        return bEndNeighbourPortDetails;
    }

    public void setbEndNeighbourPortDetails(String bEndNeighbourPortDetails) {
        this.bEndNeighbourPortDetails = bEndNeighbourPortDetails;
    }

    public String getCfmMipLevel() {
        return cfmMipLevel;
    }

    public void setCfmMipLevel(String cfmMipLevel) {
        this.cfmMipLevel = cfmMipLevel;
    }

    public String getConfigureAEndUplinkPort() {
        return configureAEndUplinkPort;
    }

    public void setConfigureAEndUplinkPort(String configureAEndUplinkPort) {
        this.configureAEndUplinkPort = configureAEndUplinkPort;
    }

    public String getControlVlanId() {
        return controlVlanId;
    }

    public void setControlVlanId(String controlVlanId) {
        this.controlVlanId = controlVlanId;
    }

    public String getDescAEndNeighbour() {
        return descAEndNeighbour;
    }

    public void setDescAEndNeighbour(String descAEndNeighbour) {
        this.descAEndNeighbour = descAEndNeighbour;
    }

    public String getDescNewDeviceAEndUplinkPort() {
        return descNewDeviceAEndUplinkPort;
    }

    public void setDescNewDeviceAEndUplinkPort(String descNewDeviceAEndUplinkPort) {
        this.descNewDeviceAEndUplinkPort = descNewDeviceAEndUplinkPort;
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

    public String getAedHostName() {
        return aedHostName;
    }

    public void setAedHostName(String aedHostName) {
        this.aedHostName = aedHostName;
    }

    public String getInitTemplate() {
        return initTemplate;
    }

    public void setInitTemplate(String initTemplate) {
        this.initTemplate = initTemplate;
    }

    public String getManagementVlanDescription() {
        return managementVlanDescription;
    }

    public void setManagementVlanDescription(String managementVlanDescription) {
        this.managementVlanDescription = managementVlanDescription;
    }

    public String getEthernetAccess() {
        return ethernetAccess;
    }

    public void setEthernetAccess(String ethernetAccess) {
        this.ethernetAccess = ethernetAccess;
    }

    public String getEthernetAccessEorType() {
        return ethernetAccessEorType;
    }

    public void setEthernetAccessEorType(String ethernetAccessEorType) {
        this.ethernetAccessEorType = ethernetAccessEorType;
    }

    public String getFiberFeasibility() {
        return fiberFeasibility;
    }

    public void setFiberFeasibility(String fiberFeasibility) {
        this.fiberFeasibility = fiberFeasibility;
    }

    public String getFieldOpsTeam() {
        return fieldOpsTeam;
    }

    public void setFieldOpsTeam(String fieldOpsTeam) {
        this.fieldOpsTeam = fieldOpsTeam;
    }

    public String getGspiBranch() {
        return gspiBranch;
    }

    public void setGspiBranch(String gspiBranch) {
        this.gspiBranch = gspiBranch;
    }

    public String getInfraFeasibility() {
        return infraFeasibility;
    }

    public void setInfraFeasibility(String infraFeasibility) {
        this.infraFeasibility = infraFeasibility;
    }

    public String getIpPool() {
        return ipPool;
    }

    public void setIpPool(String ipPool) {
        this.ipPool = ipPool;
    }

    public String getIsMuxDetailsChanged() {
        return isMuxDetailsChanged;
    }

    public void setIsMuxDetailsChanged(String isMuxDetailsChanged) {
        this.isMuxDetailsChanged = isMuxDetailsChanged;
    }

    public String getIsTejasMumbaiPuneEci() {
        return isTejasMumbaiPuneEci;
    }

    public void setIsTejasMumbaiPuneEci(String isTejasMumbaiPuneEci) {
        this.isTejasMumbaiPuneEci = isTejasMumbaiPuneEci;
    }

    public String getIsTermExistingLinkReqd() {
        return isTermExistingLinkReqd;
    }

    public void setIsTermExistingLinkReqd(String isTermExistingLinkReqd) {
        this.isTermExistingLinkReqd = isTermExistingLinkReqd;
    }

    public String getLoopBackIp0() {
        return loopBackIp0;
    }

    public void setLoopBackIp0(String loopBackIp0) {
        this.loopBackIp0 = loopBackIp0;
    }

    public String getLoopBackIp1() {
        return loopBackIp1;
    }

    public void setLoopBackIp1(String loopBackIp1) {
        this.loopBackIp1 = loopBackIp1;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getPoFrnNo() {
        return poFrnNo;
    }

    public void setPoFrnNo(String poFrnNo) {
        this.poFrnNo = poFrnNo;
    }

    public String getIprm() {
        return iprm;
    }

    public void setIprm(String iprm) {
        this.iprm = iprm;
    }

    public String getPortFeasibility() {
        return portFeasibility;
    }

    public void setPortFeasibility(String portFeasibility) {
        this.portFeasibility = portFeasibility;
    }

    public String getPowerFeasibility() {
        return powerFeasibility;
    }

    public void setPowerFeasibility(String powerFeasibility) {
        this.powerFeasibility = powerFeasibility;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getWebReportAttached() {
        return webReportAttached;
    }

    public void setWebReportAttached(String webReportAttached) {
        this.webReportAttached = webReportAttached;
    }

    public String getPeDate() {
        return peDate;
    }

    public void setPeDate(String peDate) {
        this.peDate = peDate;
    }

    public String getPeRequired() {
        return peRequired;
    }

    public void setPeRequired(String peRequired) {
        this.peRequired = peRequired;
    }

    public String getFieldOps() {
        return fieldOps;
    }

    public void setFieldOps(String fieldOps) {
        this.fieldOps = fieldOps;
    }

    public String getAllMaterialReceived() {
        return allMaterialReceived;
    }

    public void setAllMaterialReceived(String allMaterialReceived) {
        this.allMaterialReceived = allMaterialReceived;
    }

    public String getDispatchDetails() {
        return dispatchDetails;
    }

    public void setDispatchDetails(String dispatchDetails) {
        this.dispatchDetails = dispatchDetails;
    }

    public String getMlGroup() {
        return mlGroup;
    }

    public void setMlGroup(String mlGroup) {
        this.mlGroup = mlGroup;
    }

    public String getMrnType() {
        return mrnType;
    }

    public void setMrnType(String mrnType) {
        this.mrnType = mrnType;
    }

    public String getmAndLRequired() {
        return mAndLRequired;
    }

    public void setmAndLRequired(String mAndLRequired) {
        this.mAndLRequired = mAndLRequired;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Timestamp getTaskClaimTime() {
        return taskClaimTime;
    }

    public void setTaskClaimTime(Timestamp taskClaimTime) {
        this.taskClaimTime = taskClaimTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getNwaRemarksByUser() {
        return nwaRemarksByUser;
    }

    public void setNwaRemarksByUser(String nwaRemarksByUser) {
        this.nwaRemarksByUser = nwaRemarksByUser;
    }

    public String getNwaRemarksByUserGroup() {
        return nwaRemarksByUserGroup;
    }

    public void setNwaRemarksByUserGroup(String nwaRemarksByUserGroup) {
        this.nwaRemarksByUserGroup = nwaRemarksByUserGroup;
    }

    public String getNwaRemarksJeopardyReason() {
        return nwaRemarksJeopardyReason;
    }

    public void setNwaRemarksJeopardyReason(String nwaRemarksJeopardyReason) {
        this.nwaRemarksJeopardyReason = nwaRemarksJeopardyReason;
    }

    public String getNwaRemarksReason() {
        return nwaRemarksReason;
    }

    public void setNwaRemarksReason(String nwaRemarksReason) {
        this.nwaRemarksReason = nwaRemarksReason;
    }

    public Timestamp getNwaRemarksRemarksDate() {
        return nwaRemarksRemarksDate;
    }

    public void setNwaRemarksRemarksDate(Timestamp nwaRemarksRemarksDate) {
        this.nwaRemarksRemarksDate = nwaRemarksRemarksDate;
    }

    public String getNwaRemarksScenarioType() {
        return nwaRemarksScenarioType;
    }

    public void setNwaRemarksScenarioType(String nwaRemarksScenarioType) {
        this.nwaRemarksScenarioType = nwaRemarksScenarioType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(String scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getTechnologyType() {
        return technologyType;
    }

    public void setTechnologyType(String technologyType) {
        this.technologyType = technologyType;
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

    public String getCardSrNumber() {
        return cardSrNumber;
    }

    public void setCardSrNumber(String cardSrNumber) {
        this.cardSrNumber = cardSrNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getShelfDetails() {
        return shelfDetails;
    }

    public void setShelfDetails(String shelfDetails) {
        this.shelfDetails = shelfDetails;
    }

    public String getShelfSlotDetails() {
        return shelfSlotDetails;
    }

    public void setShelfSlotDetails(String shelfSlotDetails) {
        this.shelfSlotDetails = shelfSlotDetails;
    }

    public String getSlotDetails() {
        return slotDetails;
    }

    public void setSlotDetails(String slotDetails) {
        this.slotDetails = slotDetails;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getShelfType() {
        return shelfType;
    }

    public void setShelfType(String shelfType) {
        this.shelfType = shelfType;
    }

    public String getPopAddress() {
        return PopAddress;
    }

    public void setPopAddress(String popAddress) {
        PopAddress = popAddress;
    }

    public String getSdNocSaNoc() {
        return sdNocSaNoc;
    }

    public void setSdNocSaNoc(String sdNocSaNoc) {
        this.sdNocSaNoc = sdNocSaNoc;
    }

    public String getGetEthernetAccessEorType() {
        return getEthernetAccessEorType;
    }

    public void setGetEthernetAccessEorType(String getEthernetAccessEorType) {
        this.getEthernetAccessEorType = getEthernetAccessEorType;
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

    public String getFoDetails() {
        return foDetails;
    }

    public void setFoDetails(String foDetails) {
        this.foDetails = foDetails;
    }

    public void setSdnoc(String sdnoc) {
        this.sdnoc = sdnoc;
    }

    public String getSdnoc() {
        return sdnoc;
    }

    public String getConfigrationManagment() {
        return configrationManagment;
    }

    public void setConfigrationManagment(String configrationManagment) {
        this.configrationManagment = configrationManagment;
    }

    public String getCramerTeam() {
        return cramerTeam;
    }

    public void setCramerTeam(String cramerTeam) {
        this.cramerTeam = cramerTeam;
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

    public String getIsSwitchIpPoolRequired() {
        return isSwitchIpPoolRequired;
    }

    public void setIsSwitchIpPoolRequired(String isSwitchIpPoolRequired) {
        this.isSwitchIpPoolRequired = isSwitchIpPoolRequired;
    }

    public String getSiblingOrder() {
        return siblingOrder;
    }

    public void setSiblingOrder(String siblingOrder) {
        this.siblingOrder = siblingOrder;
    }

    public String getLinkedPe() {
        return linkedPe;
    }

    public void setLinkedPe(String linkedPe) {
        this.linkedPe = linkedPe;
    }

    public String getLinkedOrderId() {
        return linkedOrderId;
    }

    public void setLinkedOrderId(String linkedOrderId) {
        this.linkedOrderId = linkedOrderId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEorConfirmedBy() {
        return eorConfirmedBy;
    }

    public void setEorConfirmedBy(String eorConfirmedBy) {
        this.eorConfirmedBy = eorConfirmedBy;
    }

    public String getRfmRfs() {
        return rfmRfs;
    }

    public void setRfmRfs(String rfmRfs) {
        this.rfmRfs = rfmRfs;
    }

    public String getRfmRfsAssignTo() {
        return rfmRfsAssignTo;
    }

    public void setRfmRfsAssignTo(String rfmRfsAssignTo) {
        this.rfmRfsAssignTo = rfmRfsAssignTo;
    }

    public String getBhType() {
        return bhType;
    }

    public void setBhType(String bhType) {
        this.bhType = bhType;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getBtsVlan() {
        return btsVlan;
    }

    public void setBtsVlan(String btsVlan) {
        this.btsVlan = btsVlan;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getEohsCategory() {
        return eohsCategory;
    }

    public void setEohsCategory(String eohsCategory) {
        this.eohsCategory = eohsCategory;
    }

    public Timestamp getInfraAggrementExpiryDate() {
        return infraAggrementExpiryDate;
    }

    public void setInfraAggrementExpiryDate(Timestamp infraAggrementExpiryDate) {
        this.infraAggrementExpiryDate = infraAggrementExpiryDate;
    }

    public String getInfraProvider() {
        return infraProvider;
    }

    public void setInfraProvider(String infraProvider) {
        this.infraProvider = infraProvider;
    }

    public String getInfraProviderSiteUpTime() {
        return infraProviderSiteUpTime;
    }

    public void setInfraProviderSiteUpTime(String infraProviderSiteUpTime) {
        this.infraProviderSiteUpTime = infraProviderSiteUpTime;
    }

    public String getPerAntennaDimension() {
        return perAntennaDimension;
    }

    public void setPerAntennaDimension(String perAntennaDimension) {
        this.perAntennaDimension = perAntennaDimension;
    }

    public String getPerOduWeight() {
        return perOduWeight;
    }

    public void setPerOduWeight(String perOduWeight) {
        this.perOduWeight = perOduWeight;
    }

    public String getPlannedDeinstallationDate() {
        return plannedDeinstallationDate;
    }

    public void setPlannedDeinstallationDate(String plannedDeinstallationDate) {
        this.plannedDeinstallationDate = plannedDeinstallationDate;
    }

    public String getPlannedInstallationDate() {
        return plannedInstallationDate;
    }

    public void setPlannedInstallationDate(String plannedInstallationDate) {
        this.plannedInstallationDate = plannedInstallationDate;
    }

    public String getReasonForExist() {
        return reasonForExist;
    }

    public void setReasonForExist(String reasonForExist) {
        this.reasonForExist = reasonForExist;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getBtsSubnetMask() {
        return btsSubnetMask;
    }

    public void setBtsSubnetMask(String btsSubnetMask) {
        this.btsSubnetMask = btsSubnetMask;
    }

    public Timestamp getTclAggrementStartDate() {
        return tclAggrementStartDate;
    }

    public void setTclAggrementStartDate(Timestamp tclAggrementStartDate) {
        this.tclAggrementStartDate = tclAggrementStartDate;
    }

    public String getWirelessTechSubType() {
        return wirelessTechSubType;
    }

    public void setWirelessTechSubType(String wirelessTechSubType) {
        this.wirelessTechSubType = wirelessTechSubType;
    }

    public Map<String, Object> getCardDetailsMap() {
        return cardDetailsMap;
    }

    public void setCardDetailsMap(Map<String, Object> cardDetailsMap) {
        this.cardDetailsMap = cardDetailsMap;
    }


    public ArrayList<Map<String, Object>> getNwaCardDetailsArrayList() {
        return nwaCardDetailsArrayList;
    }

    public void setNwaCardDetailsArrayList(ArrayList<Map<String, Object>> nwaCardDetailsArrayList) {
        this.nwaCardDetailsArrayList = nwaCardDetailsArrayList;
    }

    @Override
    public String toString() {
        return "OrderInitiateResultBean{" +
                "uuid='" + uuid + '\'' +
                ", opOrderCode='" + opOrderCode + '\'' +
                ", orderType='" + orderType + '\'' +
                ", subject='" + subject + '\'' +
                ", projectType='" + projectType + '\'' +
                ", scenarioType='" + scenarioType + '\'' +
                ", technologyType='" + technologyType + '\'' +
                ", pmName='" + pmName + '\'' +
                ", pmContactEmail='" + pmContactEmail + '\'' +
                ", originatorName='" + originatorName + '\'' +
                ", originatorContactNumber='" + originatorContactNumber + '\'' +
                ", orderCreationDate='" + orderCreationDate + '\'' +
                ", originatorGroupId='" + originatorGroupId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", projectName='" + projectName + '\'' +
                ", eorConfirmedBy='" + eorConfirmedBy + '\'' +
                ", rfmRfs='" + rfmRfs + '\'' +
                ", rfmRfsAssignTo='" + rfmRfsAssignTo + '\'' +
                ", taskName='" + taskName + '\'' +
                ", taskClaimTime=" + taskClaimTime +
                ", taskStatus='" + taskStatus + '\'' +
                ", sdUuid='" + sdUuid + '\'' +
                ", serviceGroupId='" + serviceGroupId + '\'' +
                ", scOrderUuid='" + scOrderUuid + '\'' +
                ", smName='" + smName + '\'' +
                ", smEmail='" + smEmail + '\'' +
                ", mstStatus=" + mstStatus +
                ", hostName='" + hostName + '\'' +
                ", aEndMuxName='" + aEndMuxName + '\'' +
                ", aEndMuxPort='" + aEndMuxPort + '\'' +
                ", aEndMuxPort2='" + aEndMuxPort2 + '\'' +
                ", aggrParentRouterIp='" + aggrParentRouterIp + '\'' +
                ", area='" + area + '\'' +
                ", areaOther='" + areaOther + '\'' +
                ", bldg='" + bldg + '\'' +
                ", bldgOther='" + bldgOther + '\'' +
                ", city='" + city + '\'' +
                ", cityOther='" + cityOther + '\'' +
                ", country='" + country + '\'' +
                ", coverage='" + coverage + '\'' +
                ", dcnReq='" + dcnReq + '\'' +
                ", deviceSubType='" + deviceSubType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", endMuxAddress='" + endMuxAddress + '\'' +
                ", eorequipmentStatus='" + eorequipmentStatus + '\'' +
                ", eorEquipStatus='" + eorEquipStatus + '\'' +
                ", eqpmntIp='" + eqpmntIp + '\'' +
                ", eqpmntName='" + eqpmntName + '\'' +
                ", eqpmntPort='" + eqpmntPort + '\'' +
                ", eqpmntType='" + eqpmntType + '\'' +
                ", floor='" + floor + '\'' +
                ", interFibreReq='" + interFibreReq + '\'' +
                ", interfaceToParent='" + interfaceToParent + '\'' +
                ", interfaceType='" + interfaceType + '\'' +
                ", isManEquipment='" + isManEquipment + '\'' +
                ", isSwitchIpRequird='" + isSwitchIpRequird + '\'' +
                ", majorPop='" + majorPop + '\'' +
                ", microPop='" + microPop + '\'' +
                ", nocTeam='" + nocTeam + '\'' +
                ", nodeOwner='" + nodeOwner + '\'' +
                ", nodeOwnerOther='" + nodeOwnerOther + '\'' +
                ", poolSize='" + poolSize + '\'' +
                ", popName='" + popName + '\'' +
                ", popOwner='" + popOwner + '\'' +
                ", popType='" + popType + '\'' +
                ", powerRating='" + powerRating + '\'' +
                ", powerType='" + powerType + '\'' +
                ", ringType='" + ringType + '\'' +
                ", siteAddress='" + siteAddress + '\'' +
                ", spareScrap='" + spareScrap + '\'' +
                ", subnetIpPool1='" + subnetIpPool1 + '\'' +
                ", subnetIpPool2='" + subnetIpPool2 + '\'' +
                ", switchIpPool='" + switchIpPool + '\'' +
                ", switchManagementIp='" + switchManagementIp + '\'' +
                ", switchManagementVlan='" + switchManagementVlan + '\'' +
                ", switchSubnetMask='" + switchSubnetMask + '\'' +
                ", vlan='" + vlan + '\'' +
                ", eorWimaxOrderSite='" + eorWimaxOrderSite + '\'' +
                ", siteEnd='" + siteEnd + '\'' +
                ", state='" + state + '\'' +
                ", siteName='" + siteName + '\'' +
                ", siteId='" + siteId + '\'' +
                ", rackUnitSpace='" + rackUnitSpace + '\'' +
                ", cityTier='" + cityTier + '\'' +
                ", tacacsRequired='" + tacacsRequired + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", owner='" + owner + '\'' +
                ", role='" + role + '\'' +
                ", hostName1='" + hostName1 + '\'' +
                ", aggr1EqpmntPort2='" + aggr1EqpmntPort2 + '\'' +
                ", aggr1ParentRouter='" + aggr1ParentRouter + '\'' +
                ", aggr1Area='" + aggr1Area + '\'' +
                ", aggr1AreaOther='" + aggr1AreaOther + '\'' +
                ", aggr1Bldg='" + aggr1Bldg + '\'' +
                ", aggr1BldgOther='" + aggr1BldgOther + '\'' +
                ", aggr1City='" + aggr1City + '\'' +
                ", aggr1CityOther='" + aggr1CityOther + '\'' +
                ", aggr1Country='" + aggr1Country + '\'' +
                ", aggr1EndMuxAddress='" + aggr1EndMuxAddress + '\'' +
                ", aggr1EqpmntIp='" + aggr1EqpmntIp + '\'' +
                ", aggr1EqpmntName='" + aggr1EqpmntName + '\'' +
                ", aggr1EqpmntPort='" + aggr1EqpmntPort + '\'' +
                ", aggr1EqpmntType='" + aggr1EqpmntType + '\'' +
                ", aggr1Floor='" + aggr1Floor + '\'' +
                ", aggr1MajorPop='" + aggr1MajorPop + '\'' +
                ", aggr1MicraPop='" + aggr1MicraPop + '\'' +
                ", aggr1SiteAddress='" + aggr1SiteAddress + '\'' +
                ", aggr1State='" + aggr1State + '\'' +
                ", aggr1SiteName='" + aggr1SiteName + '\'' +
                ", aggr1PopAddress='" + aggr1PopAddress + '\'' +
                ", aggr1SiteId='" + aggr1SiteId + '\'' +
                ", hostName2='" + hostName2 + '\'' +
                ", aggr2EqpmntPort2='" + aggr2EqpmntPort2 + '\'' +
                ", aggr2ParentRouter='" + aggr2ParentRouter + '\'' +
                ", aggr2Area='" + aggr2Area + '\'' +
                ", aggr2AreaOther='" + aggr2AreaOther + '\'' +
                ", aggr2Floor='" + aggr2Floor + '\'' +
                ", aggr2Bldg='" + aggr2Bldg + '\'' +
                ", aggr2BldgOther='" + aggr2BldgOther + '\'' +
                ", aggr2City='" + aggr2City + '\'' +
                ", aggr2CityOther='" + aggr2CityOther + '\'' +
                ", aggr2Country='" + aggr2Country + '\'' +
                ", aggr2EndMuxAddress='" + aggr2EndMuxAddress + '\'' +
                ", aggr2EqpmntIp='" + aggr2EqpmntIp + '\'' +
                ", aggr2EqpmntName='" + aggr2EqpmntName + '\'' +
                ", aggr2EqpmntPort='" + aggr2EqpmntPort + '\'' +
                ", aggr2EqpmntType='" + aggr2EqpmntType + '\'' +
                ", aggr2MajorPop='" + aggr2MajorPop + '\'' +
                ", aggr2MicraPop='" + aggr2MicraPop + '\'' +
                ", aggr2SiteAddress='" + aggr2SiteAddress + '\'' +
                ", aggr2State='" + aggr2State + '\'' +
                ", aggr2SiteName='" + aggr2SiteName + '\'' +
                ", aggr2PopAddress='" + aggr2PopAddress + '\'' +
                ", aggr2SiteId='" + aggr2SiteId + '\'' +
                ", sdNocSaNoc='" + sdNocSaNoc + '\'' +
                ", getEthernetAccessEorType='" + getEthernetAccessEorType + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", aEndMuxName2='" + aEndMuxName2 + '\'' +
                ", secondEndMuxPort1='" + secondEndMuxPort1 + '\'' +
                ", getSecondEndMuxPort2='" + getSecondEndMuxPort2 + '\'' +
                ", wearhouseLocation='" + wearhouseLocation + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", terminationLink='" + terminationLink + '\'' +
                ", priority='" + priority + '\'' +
                ", rackFeasibility='" + rackFeasibility + '\'' +
                ", parentOrder='" + parentOrder + '\'' +
                ", childOrder='" + childOrder + '\'' +
                ", sibling='" + sibling + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", mgmtIPAddress='" + mgmtIPAddress + '\'' +
                ", coverage2='" + coverage2 + '\'' +
                ", aEndRemoteNode='" + aEndRemoteNode + '\'' +
                ", portName1='" + portName1 + '\'' +
                ", portDescription='" + portDescription + '\'' +
                ", portName2='" + portName2 + '\'' +
                ", portDescription2='" + portDescription2 + '\'' +
                ", configUplink='" + configUplink + '\'' +
                ", configUplink2='" + configUplink2 + '\'' +
                ", configUplink3='" + configUplink3 + '\'' +
                ", configUplink4='" + configUplink4 + '\'' +
                ", zEndRemoteNode='" + zEndRemoteNode + '\'' +
                ", zEndEorId='" + zEndEorId + '\'' +
                ", aEndLocalPort='" + aEndLocalPort + '\'' +
                ", cfmMip='" + cfmMip + '\'' +
                ", zEndLocalPort='" + zEndLocalPort + '\'' +
                ", deviceRole='" + deviceRole + '\'' +
                ", mgmntvlan='" + mgmntvlan + '\'' +
                ", vlanInterfaceDes='" + vlanInterfaceDes + '\'' +
                ", cfmDefault='" + cfmDefault + '\'' +
                ", cityName1='" + cityName1 + '\'' +
                ", switchLocation='" + switchLocation + '\'' +
                ", nodeName3='" + nodeName3 + '\'' +
                ", protectedInstanceId='" + protectedInstanceId + '\'' +
                ", subring='" + subring + '\'' +
                ", portDescription3='" + portDescription3 + '\'' +
                ", portDescription4='" + portDescription4 + '\'' +
                ", switchName='" + switchName + '\'' +
                ", switchIp='" + switchIp + '\'' +
                ", isSwitchIpPoolRequired='" + isSwitchIpPoolRequired + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", aEndEorID='" + aEndEorID + '\'' +
                ", aEndNeighbourHostName='" + aEndNeighbourHostName + '\'' +
                ", aEndNeighbourPortDetails='" + aEndNeighbourPortDetails + '\'' +
                ", bEndNeighbourPortDetails='" + bEndNeighbourPortDetails + '\'' +
                ", cfmMipLevel='" + cfmMipLevel + '\'' +
                ", configureAEndUplinkPort='" + configureAEndUplinkPort + '\'' +
                ", controlVlanId='" + controlVlanId + '\'' +
                ", descAEndNeighbour='" + descAEndNeighbour + '\'' +
                ", descNewDeviceAEndUplinkPort='" + descNewDeviceAEndUplinkPort + '\'' +
                ", erpsRingId='" + erpsRingId + '\'' +
                ", gateWayIpAddressNtp='" + gateWayIpAddressNtp + '\'' +
                ", aedHostName='" + aedHostName + '\'' +
                ", initTemplate='" + initTemplate + '\'' +
                ", managementVlanDescription='" + managementVlanDescription + '\'' +
                ", ethernetAccess='" + ethernetAccess + '\'' +
                ", ethernetAccessEorType='" + ethernetAccessEorType + '\'' +
                ", fiberFeasibility='" + fiberFeasibility + '\'' +
                ", fieldOpsTeam='" + fieldOpsTeam + '\'' +
                ", gspiBranch='" + gspiBranch + '\'' +
                ", infraFeasibility='" + infraFeasibility + '\'' +
                ", ipPool='" + ipPool + '\'' +
                ", isMuxDetailsChanged='" + isMuxDetailsChanged + '\'' +
                ", isTejasMumbaiPuneEci='" + isTejasMumbaiPuneEci + '\'' +
                ", isTermExistingLinkReqd='" + isTermExistingLinkReqd + '\'' +
                ", loopBackIp0='" + loopBackIp0 + '\'' +
                ", loopBackIp1='" + loopBackIp1 + '\'' +
                ", networkType='" + networkType + '\'' +
                ", poFrnNo='" + poFrnNo + '\'' +
                ", iprm='" + iprm + '\'' +
                ", portFeasibility='" + portFeasibility + '\'' +
                ", powerFeasibility='" + powerFeasibility + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", softwareVersion='" + softwareVersion + '\'' +
                ", warehouseLocation='" + warehouseLocation + '\'' +
                ", webReportAttached='" + webReportAttached + '\'' +
                ", peDate='" + peDate + '\'' +
                ", peRequired='" + peRequired + '\'' +
                ", fieldOps='" + fieldOps + '\'' +
                ", configrationManagment='" + configrationManagment + '\'' +
                ", cramerTeam='" + cramerTeam + '\'' +
                ", nwaRemarksByUser='" + nwaRemarksByUser + '\'' +
                ", nwaRemarksByUserGroup='" + nwaRemarksByUserGroup + '\'' +
                ", nwaRemarksJeopardyReason='" + nwaRemarksJeopardyReason + '\'' +
                ", nwaRemarksReason='" + nwaRemarksReason + '\'' +
                ", comments='" + comments + '\'' +
                ", nwaRemarksRemarksDate=" + nwaRemarksRemarksDate +
                ", nwaRemarksScenarioType='" + nwaRemarksScenarioType + '\'' +
                ", allMaterialReceived='" + allMaterialReceived + '\'' +
                ", dispatchDetails='" + dispatchDetails + '\'' +
                ", mlGroup='" + mlGroup + '\'' +
                ", mrnType='" + mrnType + '\'' +
                ", mAndLRequired='" + mAndLRequired + '\'' +
                ", siblingOrder='" + siblingOrder + '\'' +
                ", linkedPe='" + linkedPe + '\'' +
                ", linkedOrderId='" + linkedOrderId + '\'' +
                ", cardSrNumber='" + cardSrNumber + '\'' +
                ", sr_no='" + sr_no + '\'' +
                ", cardType='" + cardType + '\'' +
                ", card_type='" + card_type + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                ", uid='" + uid + '\'' +
                ", shelfDetails='" + shelfDetails + '\'' +
                ", shelf_no='" + shelf_no + '\'' +
                ", shelfSlotDetails='" + shelfSlotDetails + '\'' +
                ", shelf_slot_no='" + shelf_slot_no + '\'' +
                ", slotDetails='" + slotDetails + '\'' +
                ", card_slot_no='" + card_slot_no + '\'' +
                ", cardDescription='" + cardDescription + '\'' +
                ", shelfType='" + shelfType + '\'' +
                ", subSlotNo='" + subSlotNo + '\'' +
                ", PopAddress='" + PopAddress + '\'' +
                ", sdnoc='" + sdnoc + '\'' +
                ", foDetails='" + foDetails + '\'' +
                ", bhType='" + bhType + '\'' +
                ", deviceIp='" + deviceIp + '\'' +
                ", btsVlan='" + btsVlan + '\'' +
                ", circle='" + circle + '\'' +
                ", eohsCategory='" + eohsCategory + '\'' +
                ", infraAggrementExpiryDate=" + infraAggrementExpiryDate +
                ", infraProvider='" + infraProvider + '\'' +
                ", infraProviderSiteUpTime='" + infraProviderSiteUpTime + '\'' +
                ", perAntennaDimension='" + perAntennaDimension + '\'' +
                ", perOduWeight='" + perOduWeight + '\'' +
                ", plannedDeinstallationDate=" + plannedDeinstallationDate +
                ", plannedInstallationDate=" + plannedInstallationDate +
                ", reasonForExist='" + reasonForExist + '\'' +
                ", siteType='" + siteType + '\'' +
                ", btsSubnetMask='" + btsSubnetMask + '\'' +
                ", tclAggrementStartDate=" + tclAggrementStartDate +
                ", wirelessTechSubType='" + wirelessTechSubType + '\'' +
                ", cardData=" + cardData +
                '}';
    }
}
