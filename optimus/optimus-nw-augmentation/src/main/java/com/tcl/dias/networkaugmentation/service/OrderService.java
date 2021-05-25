package com.tcl.dias.networkaugmentation.service;

import com.tcl.dias.networkaugment.entity.entities.*;
import com.tcl.dias.networkaugment.entity.entities.Process;
import com.tcl.dias.networkaugment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.beans.MfTaskTrailBean;
import com.tcl.dias.networkaugmentation.beans.OrderInitiateResultBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class OrderService {

    @Autowired
    ScOrderRepository scOrderRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    StageRepository stageRepository;

    @Autowired
    ProcessRepository processRepository;

    @Autowired
    private TaskCacheService taskCacheService;

    @Autowired
    NetworkAugmentationService networkAugmentationService;

    @Autowired
    MfTaskTrailRepository mfTaskTrailRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    NwaEorEquipDetailsRepository nwaEorEquipDetailsRepository;

    public OrderInitiateResultBean orderInitate (Map<String, Object> payLoad) {

        ScOrder scOrder = scOrderRepository.findByOpOrderCode((String) payLoad.get("order_id"));

        if (scOrder == null) {
            return null;
        }
        NwaEorEquipDetails nwaEorEquipDetails1 = nwaEorEquipDetailsRepository.findByOrderCode((String) payLoad.get("order_id"));
        if(nwaEorEquipDetails1 != null){
       // if (payLoad.get("rejectionFlag") != null && (boolean) payLoad.get("rejectionFlag") == true ) {
            this.saveOrder(payLoad);
        } else {

        scOrder.setSubject((String) payLoad.get("subject"));
        scOrder.setPmName((String) payLoad.get("pmName"));
        scOrder.setPmContactEmail((String) payLoad.get("pmContactEmail"));
        scOrder.setProjectType((String) payLoad.get("projectType"));
        if (payLoad.get("pmName") == null) {
            scOrder.setPmName(scOrder.getOriginatorName());
        }
        scOrder.setOriginatorContactNumber((String) payLoad.get("originatorContactNumber"));
        scOrder.setProjectName((String) payLoad.get("projectName"));
        scOrder.setCityTier((String) payLoad.get("cityTier"));
        scOrder.setEorConfirmedBy((String) payLoad.get("eorConfirmedBy"));
        scOrder.setRfmRfs((String) payLoad.get("rfm"));
        scOrder.setRfmRfsAssignTo((String) payLoad.get("assignedTo"));

        Set<ScServiceDetail> tSsD = scOrder.getScServiceDetails();
        ScServiceDetail scServiceDetail = null;
        if (tSsD == null) {
            tSsD = new HashSet<>();
            scServiceDetail = new ScServiceDetail();
        } else {
            scServiceDetail = tSsD.iterator().next();
            if (scServiceDetail == null) {
                scServiceDetail = new ScServiceDetail();
            } else {
                scServiceDetail = tSsD.iterator().next();
            }
        }
        System.out.println("=============service details id" + scServiceDetail.getId());

        scServiceDetail.setScOrder(scOrder);
        tSsD.add(scServiceDetail);
        scOrder.setScServiceDetails(tSsD);

        Set<NwaEorEquipDetails> tNaEqd = scOrder.getNwaEorEquipDetails();
        if (tNaEqd == null) {
            tNaEqd = new HashSet<>();
        }

        NwaEorEquipDetails nwaEorEquipDetails = new NwaEorEquipDetails();
        nwaEorEquipDetails.setOrderCode(scOrder.getOpOrderCode());
        nwaEorEquipDetails.setHostName((String) payLoad.get("hostName"));
        nwaEorEquipDetails.setCountry((String) payLoad.get("country"));
        nwaEorEquipDetails.setState((String) payLoad.get("state"));
        nwaEorEquipDetails.setCity((String) payLoad.get("city"));
        nwaEorEquipDetails.setCityOther((String) payLoad.get("cityOther"));
        nwaEorEquipDetails.setArea((String) payLoad.get("area"));
        nwaEorEquipDetails.setAreaOther((String) payLoad.get("areaOther"));
        nwaEorEquipDetails.setBldg((String) payLoad.get("bldg"));
        nwaEorEquipDetails.setBldgOther((String) payLoad.get("bldgOther"));
        nwaEorEquipDetails.setFloor((String) payLoad.get("floor"));
        nwaEorEquipDetails.setSiteId((String) payLoad.get("siteId"));
        nwaEorEquipDetails.setSiteAddress((String) payLoad.get("siteAddress"));
        nwaEorEquipDetails.setEqpmntName((String) payLoad.get("eqpmntName"));
        nwaEorEquipDetails.setEqpmntIp((String) payLoad.get("eqpmntIp"));
        nwaEorEquipDetails.setDeviceType((String) payLoad.get("deviceType"));
        nwaEorEquipDetails.setEqpmntType((String) payLoad.get("eqpmntType"));
        nwaEorEquipDetails.setPowerType((String) payLoad.get("powerType"));
        nwaEorEquipDetails.setPowerRating((String) payLoad.get("powerRating"));
        nwaEorEquipDetails.setPopType((String) payLoad.get("popType"));
        nwaEorEquipDetails.setPopName((String) payLoad.get("popName"));
        nwaEorEquipDetails.setPopAddress((String) payLoad.get("popAddress"));
        nwaEorEquipDetails.setNodeOwner((String) payLoad.get("nodeOwner"));
        nwaEorEquipDetails.setNodeOwnerOther((String) payLoad.get("nodeOwnerOther"));
        nwaEorEquipDetails.setInterfaceToParent((String) payLoad.get("interfaceToParent"));
        nwaEorEquipDetails.setInterFibreReq((String) payLoad.get("interFibreReq"));
        nwaEorEquipDetails.setEorEquipStatus((String) payLoad.get("eorequipmentStatus"));
        if (payLoad.get("eorequipmentStatus") == null) {
            nwaEorEquipDetails.setEorEquipStatus((String) payLoad.get("eorEquipStatus"));
        }
        nwaEorEquipDetails.setMake((String) payLoad.get("make"));
        nwaEorEquipDetails.setModel((String) payLoad.get("model"));
        nwaEorEquipDetails.setCoverage((String) payLoad.get("coverage"));
        nwaEorEquipDetails.setOwner((String) payLoad.get("owner"));
        nwaEorEquipDetails.setRole((String) payLoad.get("role"));
        nwaEorEquipDetails.setPopOwner((String) payLoad.get("popOwner"));
        nwaEorEquipDetails.setIsManEquipment((String) payLoad.get("isManEquipment"));

        nwaEorEquipDetails.setHostName1((String) payLoad.get("hostName1"));
        nwaEorEquipDetails.setAggr1Country((String) payLoad.get("aggr1Country"));
        nwaEorEquipDetails.setAggr1State((String) payLoad.get("aggr1State"));
        nwaEorEquipDetails.setAggr1City((String) payLoad.get("aggr1City"));
        nwaEorEquipDetails.setAggr1Bldg((String) payLoad.get("aggr1Bldg"));
        //nwaEorEquipDetails.setAggr1BldgOther((String) payLoad.get("aggr1Area"));
        nwaEorEquipDetails.setAggr1Area((String) payLoad.get("aggr1Area"));
        nwaEorEquipDetails.setAggr1Floor((String) payLoad.get("aggr1Floor"));
        nwaEorEquipDetails.setAggr1SiteAddress((String) payLoad.get("aggr1SiteAddress"));
        nwaEorEquipDetails.setAggr1SiteId((String) payLoad.get("aggr1SiteId"));
        nwaEorEquipDetails.setAggr1PopAddress((String) payLoad.get("aggr1PopAddress"));
        nwaEorEquipDetails.setAggr1ParentRouter((String) payLoad.get("aggr1ParentRouter"));
        nwaEorEquipDetails.setAggr1EqpmntName((String) payLoad.get("eqpmntName"));
        nwaEorEquipDetails.setAggr1EqpmntPort((String) payLoad.get("aggr1EqpmntPort"));
        nwaEorEquipDetails.setAggr1EqpmntPort2((String) payLoad.get("aggr1EqpmntPort2"));
        nwaEorEquipDetails.setAggr1EqpmntIp((String) payLoad.get("aggr1EqpmntIp"));
        nwaEorEquipDetails.setAggr1EndMuxAddress((String) payLoad.get("aggr1EndMuxAddress"));

        nwaEorEquipDetails.setHostName2((String) payLoad.get("hostName2"));
        nwaEorEquipDetails.setAggr2Country((String) payLoad.get("aggr2Country"));
        nwaEorEquipDetails.setAggr2State((String) payLoad.get("aggr2State"));
        nwaEorEquipDetails.setAggr2City((String) payLoad.get("aggr2City"));
        nwaEorEquipDetails.setAggr2Bldg((String) payLoad.get("aggr2Bldg"));
        nwaEorEquipDetails.setAggr2Area((String) payLoad.get("aggr2Area"));
        nwaEorEquipDetails.setAggr2Floor((String) payLoad.get("aggr2Floor"));
        nwaEorEquipDetails.setAggr2SiteAddress((String) payLoad.get("aggr2SiteAddress"));
        nwaEorEquipDetails.setAggr2SiteId((String) payLoad.get("aggr2SiteId"));
        nwaEorEquipDetails.setAggr2PopAddress((String) payLoad.get("aggr2PopAddress"));
        nwaEorEquipDetails.setAggr2ParentRouter((String) payLoad.get("aggr2ParentRouter"));
        nwaEorEquipDetails.setAggr2EqpmntName((String) payLoad.get("eqpmntName"));
        nwaEorEquipDetails.setAggr2EqpmntPort((String) payLoad.get("aggr2EqpmntPort"));
        nwaEorEquipDetails.setAggr2EqpmntPort2((String) payLoad.get("aggr2EqpmntPort2"));
        nwaEorEquipDetails.setAggr2EqpmntIp((String) payLoad.get("aggr2EqpmntIp"));
        nwaEorEquipDetails.setAggr2EndMuxAddress((String) payLoad.get("aggr2EndMuxAddress"));
        nwaEorEquipDetails.setSdNocSaNoc((String) payLoad.get("sdnoc"));
        nwaEorEquipDetails.setEthernetAccess((String) payLoad.get("ethernetAccess"));
        //nwaEorEquipDetails.setGetEthernetAccessEorType((String) payLoad.get("getEthernetAccessEorType"));
        nwaEorEquipDetails.setNetworkType((String) payLoad.get("networkType"));
        nwaEorEquipDetails.setNodeId((String) payLoad.get("nodeId"));
        nwaEorEquipDetails.setaEndMuxName((String) payLoad.get("aEndMuxName"));
        nwaEorEquipDetails.setaEndMuxName2((String) payLoad.get("aEndMuxName2"));
        nwaEorEquipDetails.setSecondEndMuxPort1((String) payLoad.get("secondEndMuxPort1"));
        nwaEorEquipDetails.setGetSecondEndMuxPort2((String) payLoad.get("getSecondEndMuxPort2"));
        nwaEorEquipDetails.setWearhouseLocation((String) payLoad.get("wearhouseLocation"));
        nwaEorEquipDetails.setSerialNumber((String) payLoad.get("serialNumber"));
        nwaEorEquipDetails.setTerminationLink((String) payLoad.get("terminationLink"));
        nwaEorEquipDetails.setPriority((String) payLoad.get("priority"));
        nwaEorEquipDetails.setRackFeasibility((String) payLoad.get("rackFeasibility"));
        nwaEorEquipDetails.setFiberFeasibility((String) payLoad.get("fiberFeasibility"));
        nwaEorEquipDetails.setInfraFeasibility((String) payLoad.get("infraFeasibility"));
        nwaEorEquipDetails.setGspiBranch((String) payLoad.get("gspiBranch"));
        nwaEorEquipDetails.setParentOrder((String) payLoad.get("parentOrder"));
        nwaEorEquipDetails.setChildOrder((String) payLoad.get("childOrder"));
        nwaEorEquipDetails.setSibling((String) payLoad.get("sibling"));
        nwaEorEquipDetails.setNodeName3((String) payLoad.get("nodeName"));
        nwaEorEquipDetails.setNodeType((String) payLoad.get("nodeType"));
        nwaEorEquipDetails.setMgmtIPAddress((String) payLoad.get("mgmtIPAddress"));
        nwaEorEquipDetails.setCoverage2((String) payLoad.get("coverage2"));
        nwaEorEquipDetails.setaEndRemoteNode((String) payLoad.get("aEndRemoteNode"));
        nwaEorEquipDetails.setPortName1((String) payLoad.get("portName1"));
        nwaEorEquipDetails.setPortDescription2((String) payLoad.get("portDescription"));
        nwaEorEquipDetails.setPortName2((String) payLoad.get("portName2"));
        nwaEorEquipDetails.setPortDescription2((String) payLoad.get("portDescription2"));
        nwaEorEquipDetails.setConfigUplink((String) payLoad.get("configUplink"));
        nwaEorEquipDetails.setConfigUplink2((String) payLoad.get("configUplink2"));
        nwaEorEquipDetails.setConfigUplink3((String) payLoad.get("configUplink3"));
        nwaEorEquipDetails.setConfigUplink4((String) payLoad.get("configUplink4"));
        nwaEorEquipDetails.setzEndRemoteNode((String) payLoad.get("zEndRemoteNode"));
        nwaEorEquipDetails.setzEndEorId((String) payLoad.get("zEndEorId"));
        nwaEorEquipDetails.setaEndLocalPort((String) payLoad.get("aEndLocalPort"));
        nwaEorEquipDetails.setCfmMip((String) payLoad.get("cfmMip"));
        nwaEorEquipDetails.setzEndLocalPort((String) payLoad.get("zEndLocalPort"));
        nwaEorEquipDetails.setDeviceRole((String) payLoad.get("deviceRole"));
        nwaEorEquipDetails.setMgmntvlan((String) payLoad.get("mgmntvlan"));
        nwaEorEquipDetails.setVlanInterfaceDes((String) payLoad.get("vlanInterfaceDes"));
        nwaEorEquipDetails.setCfmDefault((String) payLoad.get("cfmDefault"));
        nwaEorEquipDetails.setCityName1((String) payLoad.get("cityName1"));
        nwaEorEquipDetails.setSwitchLocation((String) payLoad.get("switchLocation"));
        nwaEorEquipDetails.setNodeName3((String) payLoad.get("nodeName3"));
        nwaEorEquipDetails.setControlVlanId((String) payLoad.get("controlVlanId"));




        nwaEorEquipDetails.setErpsRingId((String) payLoad.get("erpsRingId"));
        nwaEorEquipDetails.setGateWayIpAddressNtp((String) payLoad.get("gateWayIpAddressNtp"));
        nwaEorEquipDetails.setProtectedInstanceId((String) payLoad.get("protectedInstanceId"));
        nwaEorEquipDetails.setSubring((String) payLoad.get("subring"));

        nwaEorEquipDetails.setPowerFeasibility((String) payLoad.get("powerFeasibility"));
        nwaEorEquipDetails.setRackUnitSpace((String) payLoad.get("rackUnitSpace"));
        nwaEorEquipDetails.setSwitchManagementIp((String) payLoad.get("switchManagementIp"));
        nwaEorEquipDetails.setVlan((String) payLoad.get("vlan"));
        nwaEorEquipDetails.setSubnetIpPool1((String) payLoad.get("subnetIpPool1"));
        nwaEorEquipDetails.setSubnetIpPool2((String) payLoad.get("subnetIpPool2"));
        nwaEorEquipDetails.setNodeName((String) payLoad.get("nodeName"));
        nwaEorEquipDetails.setPortDescription((String) payLoad.get("portDescription"));
        nwaEorEquipDetails.setPortDescription3((String) payLoad.get("portDescription3"));
        nwaEorEquipDetails.setPortDescription4((String) payLoad.get("portDescription4"));
        nwaEorEquipDetails.setControlVlanId((String) payLoad.get("controlVlanId"));
        nwaEorEquipDetails.setEthernetAccessEorType((String) payLoad.get("ethernetAccessEorType"));
        nwaEorEquipDetails.setDcnReq((String) payLoad.get("dcnReq"));
        nwaEorEquipDetails.setRingType((String) payLoad.get("ringType"));
        nwaEorEquipDetails.setSwitchName((String) payLoad.get("switchName"));
        nwaEorEquipDetails.setSwitchIp((String) payLoad.get("switchIp"));
        nwaEorEquipDetails.setIsSwitchPoolRequired((String) payLoad.get("isSwitchPoolRequired"));

        nwaEorEquipDetails.setScOrder(scOrder);
        tNaEqd.add(nwaEorEquipDetails);
        scOrder.setNwaEorEquipDetails(tNaEqd);

            ArrayList cardsArrayList = (ArrayList) payLoad.get("cards_array");
            if ("EORIP".equalsIgnoreCase((String) payLoad.get("orderType")) || "EORTXCARD".equalsIgnoreCase((String) payLoad.get("orderType"))) {
                List cardsArray = (List) payLoad.get("cards_array");
            if (cardsArray != null) {
                Set<NwaCardDetails> tNwCrDtls = new HashSet<>();
                for (int i = 0; i < cardsArray.size(); i++) {
                    Map<String, Object> cardsData = (Map<String, Object>) cardsArray.get(i);
                    NwaCardDetails nwaCardDetails = new NwaCardDetails();
                    nwaCardDetails.setCardSrNumber((String) cardsData.get("sr_no"));
                    nwaCardDetails.setCardType((String) cardsData.get("cardType"));
                    nwaCardDetails.setUniqueId((String) cardsData.get("uid"));
                    nwaCardDetails.setShelfDetails((String) cardsData.get("shelf_no"));
                    nwaCardDetails.setShelfSlotDetails((String) cardsData.get("shelf_slot_no"));
                    nwaCardDetails.setSlotDetails((String) cardsData.get("card_slot_no"));
                    nwaCardDetails.setSubSlotNo((String) cardsData.get("subSlotNo"));
                    nwaCardDetails.setSeqNo(i);

                    nwaCardDetails.setScOrder(scOrder);
                    tNwCrDtls.add(nwaCardDetails);
                }

                scOrder.setNwaCardDetails(tNwCrDtls);
            } else {
                throw new RuntimeException("Cards information missing");
            }
        }


        Set<NwaAccessEorDetails> tNaEd = new HashSet<>();
        NwaAccessEorDetails nwaAccessEorDetails = new NwaAccessEorDetails();
        nwaAccessEorDetails.setOrderCode((String) payLoad.get("opOrderCode"));
        nwaAccessEorDetails.setaEndEorID((String) payLoad.get("aEndEorID"));
        nwaAccessEorDetails.setaEndNeighbourHostName((String) payLoad.get("aEndNeighbourHostName"));
        nwaAccessEorDetails.setaEndNeighbourPortDetails((String) payLoad.get("aEndNeighbourPortDetails "));
        nwaAccessEorDetails.setbEndNeighbourPortDetails((String) payLoad.get("bEndNeighbourPortDetails "));
        nwaAccessEorDetails.setCfmMipLevel((String) payLoad.get("cfmMipLevel"));
        nwaAccessEorDetails.setConfigureAEndUplinkPort((String) payLoad.get("configureAEndUplinkPort "));
        nwaAccessEorDetails.setControlVlanId((String) payLoad.get("controlVlanId "));
        nwaAccessEorDetails.setDescAEndNeighbour((String) payLoad.get("descAEndNeighbour"));
        nwaAccessEorDetails.setDescNewDeviceAEndUplinkPort((String) payLoad.get("descNewDeviceAEndUplinkPort"));
        nwaAccessEorDetails.setErpsRingId((String) payLoad.get("erpsRingId"));
        nwaAccessEorDetails.setGateWayIpAddressNtp((String) payLoad.get("gateWayIpAddressNtp"));
        nwaAccessEorDetails.setHostName((String) payLoad.get("aedHostName"));
        nwaAccessEorDetails.setInitTemplate((String) payLoad.get("initTemplate "));
        nwaAccessEorDetails.setManagementVlanDescription((String) payLoad.get("managementVlanDescription"));

        nwaAccessEorDetails.setScOrder(scOrder);
        tNaEd.add(nwaAccessEorDetails);
        scOrder.setNwaAccessEorDetails(tNaEd);

        Set<NwaOrderDetailsExtnd> tNaEed = scOrder.getNwaOrderDetailsExtnds();
        if (tNaEed == null) {
            tNaEed = new HashSet<>();
        }

        NwaOrderDetailsExtnd nwaOrderDetailsExtnd = new NwaOrderDetailsExtnd();
        nwaOrderDetailsExtnd.setOrderCode(scOrder.getOpOrderCode());
        nwaOrderDetailsExtnd.setWarehouseLocation((String) payLoad.get("warehouseLocation"));
        nwaOrderDetailsExtnd.setSerialNo((String) payLoad.get("serialNo"));
        nwaOrderDetailsExtnd.setSoftwareVersion((String) payLoad.get("softwareVersion"));
        nwaOrderDetailsExtnd.setPoFrnNo((String) payLoad.get("poFrnNo"));
        nwaOrderDetailsExtnd.setIprm((String) payLoad.get("iprm"));
        nwaOrderDetailsExtnd.setIpPool((String) payLoad.get("ipPool"));
        nwaOrderDetailsExtnd.setLoopBackIp0((String) payLoad.get("loopBackIp0"));
        nwaOrderDetailsExtnd.setLoopBackIp1((String) payLoad.get("loopBackIp1"));
        nwaOrderDetailsExtnd.setPeDate((String) payLoad.get("peDate"));
        nwaOrderDetailsExtnd.setPeRequired((String) payLoad.get("peRequired"));
        if (payLoad.get("fieldOps") != null) {
            nwaOrderDetailsExtnd.setFieldOps((String) payLoad.get("fieldOps"));
        } else {
            nwaOrderDetailsExtnd.setFieldOps((String) payLoad.get("foDetails"));
        }
        nwaOrderDetailsExtnd.setConfigrationManagment((String) payLoad.get("configrationManagment"));
        nwaOrderDetailsExtnd.setCramerTeam((String) payLoad.get("cramerTeam"));

        nwaOrderDetailsExtnd.setScOrder(scOrder);
        tNaEed.add(nwaOrderDetailsExtnd);
        scOrder.setNwaOrderDetailsExtnds(tNaEed);

        Set<NwaLinkedOrderDetails> tNlordr = scOrder.getNwaLinkedOrderDetails();
        if (tNlordr == null) {
            tNlordr = new HashSet<>();
        }
        NwaLinkedOrderDetails nwaLinkedOrderDetails = new NwaLinkedOrderDetails();
        nwaLinkedOrderDetails.setOrderCode(scOrder.getOpOrderCode());
        nwaLinkedOrderDetails.setmAndLRequired((String) payLoad.get("mAndLRequired"));
        nwaLinkedOrderDetails.setMlGroup((String) payLoad.get("ml"));
        nwaLinkedOrderDetails.setDispatchDetails((String) payLoad.get("dispatchDetails"));
        nwaLinkedOrderDetails.setMrnType((String) payLoad.get("mrnType"));
        nwaLinkedOrderDetails.setAllMaterialReceived((String) payLoad.get("allMaterialReceived"));
        nwaLinkedOrderDetails.setMlGroup((String) payLoad.get("mlGroup"));
        nwaLinkedOrderDetails.setLinkedOrderId((String) payLoad.get("linkedOrderId"));
        nwaLinkedOrderDetails.setSiblingOrder((String) payLoad.get("siblingOrder"));
        nwaLinkedOrderDetails.setLinkedPe((String) payLoad.get("linkedPe"));

        nwaLinkedOrderDetails.setScOrder(scOrder);
        tNlordr.add(nwaLinkedOrderDetails);
        scOrder.setNwaLinkedOrderDetails(tNlordr);

        Set<NwaBtsDetails> nwaBtsDetailsSet = scOrder.getNwaBtsDetails();
        if(nwaBtsDetailsSet == null){
            nwaBtsDetailsSet = new HashSet<>();
        }
        NwaBtsDetails nwaBtsDetails = new NwaBtsDetails();
        nwaBtsDetails.setOrderCode(scOrder.getOpOrderCode());
        nwaBtsDetails.setBhType((String) payLoad.get("bhType"));
        nwaBtsDetails.setDeviceIp((String) payLoad.get("deviceIp"));
        nwaBtsDetails.setBtsVlan((String) payLoad.get("btsVlan"));
        nwaBtsDetails.setCircle((String) payLoad.get("circle"));
        nwaBtsDetails.setEohsCategory((String) payLoad.get("eohsCategory"));
        nwaBtsDetails.setInfraAggrementExpiryDate((Timestamp) payLoad.get("infraAggrementExpiryDate"));
        nwaBtsDetails.setInfraProvider((String) payLoad.get("infraProvider"));
        nwaBtsDetails.setInfraProviderSiteUpTime((String) payLoad.get("infraProviderSiteUpTime"));
        nwaBtsDetails.setPerAntennaDimension((String) payLoad.get("perAntennaDimension"));
        nwaBtsDetails.setPerOduWeight((String) payLoad.get("perOduWeight"));
        nwaBtsDetails.setPlannedInstallationDate((String) payLoad.get("plannedInstallationDate"));
        nwaBtsDetails.setPlannedDeinstallationDate((String) payLoad.get("plannedDeinstallationDate"));
        nwaBtsDetails.setReasonForExist((String) payLoad.get("reasonForExist"));
        nwaBtsDetails.setSiteId((String) payLoad.get("siteId"));
        nwaBtsDetails.setSiteName((String) payLoad.get("siteName"));
        nwaBtsDetails.setSiteType((String) payLoad.get("siteType"));
        nwaBtsDetails.setBtsSubnetMask((String) payLoad.get("btsSubnetMask"));
        nwaBtsDetails.setTclAggrementStartDate((Timestamp) payLoad.get("tclAggrementStartDate"));
        nwaBtsDetails.setWirelessTechSubType((String) payLoad.get("wirelessTechSubType"));

        nwaBtsDetails.setScOrder(scOrder);
        nwaBtsDetailsSet.add(nwaBtsDetails);
        scOrder.setNwaBtsDetails(nwaBtsDetailsSet);

        Optional<Task> taskOptional = taskRepository.findById((Integer) payLoad.get("taskId"));
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setScOrderId(scOrder.getId());
            task.setOrderCode(scOrder.getOpOrderCode());
            task.setOrderType(scOrder.getOrderType());
            task.setScenarioType(scOrder.getScenarioType());
            task.setTechnologyType(scOrder.getTechnologyType());
            task.setServiceId(scServiceDetail.getId());
            task.setServiceCode(scOrder.getOpOrderCode());
            if (scServiceDetail != null) {
                task.setScServiceDetail(scServiceDetail);
            }

            taskRepository.save(task);
            this.updateStage(task);

            Set<NwaRemarksDetails> tNrkDl = scOrder.getNwaRemarksDetails();
            if (tNrkDl == null) {
                tNrkDl = new HashSet<>();
            }
            NwaRemarksDetails nwaRemarksDetails = new NwaRemarksDetails();
            nwaRemarksDetails.setOrderCode(scOrder.getOpOrderCode());
            nwaRemarksDetails.setByUser((String) payLoad.get("originatorName"));
            nwaRemarksDetails.setByUserGroup((String) payLoad.get("originatorGroupId"));
            nwaRemarksDetails.setReason((String) payLoad.get("comments"));
            nwaRemarksDetails.setRemarksDate(Timestamp.valueOf(LocalDateTime.now()));
            nwaRemarksDetails.setScenarioType(scOrder.getScenarioType());
            nwaRemarksDetails.setTaskName(task.getMstTaskDef().getName());

            nwaRemarksDetails.setScOrder(scOrder);
            tNrkDl.add(nwaRemarksDetails);
            scOrder.setNwaRemarksDetails(tNrkDl);

            scOrder = scOrderRepository.save(scOrder);


            MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
            mfTaskTrailBean.setTaskId((Integer) payLoad.get("taskId"));
            mfTaskTrailBean.setAction("Order Initiate ");
            mfTaskTrailBean.setCompletedBy((String) payLoad.get("originatorName"));
            mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
            mfTaskTrailBean.setDescription("Order " + scOrder.getOpOrderCode() + " Initiated");
            mfTaskTrailBean.setUserGroup(scOrder.getOriginatorGroupId());
            mfTaskTrailBean.setScenario(scOrder.getScenarioType());
            mfTaskTrailBean.setComments((String) payLoad.get("comments"));
            mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
            taskService.setTaskTrail(mfTaskTrailBean);
        } else {
            System.out.println(" ========= TaskId not found for ");
        }
        return this.getOrderResultBean(scOrder);
    }
        return  this.getOrderResultBean(scOrder);
    }

    public OrderInitiateResultBean saveOrder (Map<String, Object> payLoad) {

        System.out.println("Save order called  ............");
        ScOrder scOrder = scOrderRepository.findByOpOrderCode((String) payLoad.get("order_id"));
        System.out.println("order is =============" + scOrder.getOpOrderCode() );
        if(scOrder == null){
            return null;
        }

        NwaEorEquipDetails nwaEorEquipDetails1 = nwaEorEquipDetailsRepository.findByOrderCode((String) payLoad.get("order_id"));
        if(nwaEorEquipDetails1.getCountry() == null ){
            System.out.println("Equipment data is null ==== ");
            this.orderInitate(payLoad);
            System.out.println("payload is ========== Equipment data not available ");
            System.out.println(" Equipment not available, Order initate called  ............");
        }else {

            System.out.println("payload is ==========" );
            System.out.println(" Equipment available ............");
            System.out.println(" Equipment not available, save order called  ............");
            scOrder.setSubject((String) payLoad.get("subject"));
            scOrder.setOriginatorContactNumber((String) payLoad.get("originatorContactNumber"));
            scOrder.setProjectType((String) payLoad.get("projectType"));
            scOrder.setProjectName((String) payLoad.get("projectName"));
            scOrder.setCityTier((String) payLoad.get("cityTier"));
            scOrder.setEorConfirmedBy((String) payLoad.get("eorConfirmedBy"));
            scOrder.setRfmRfs((String) payLoad.get("rfm"));
            scOrder.setRfmRfsAssignTo((String) payLoad.get("assignedTo"));
            System.out.println("originatorContactNumber==============" + (String) payLoad.get("originatorContactNumber"));


            Set<ScServiceDetail> tSsD = scOrder.getScServiceDetails();
            ScServiceDetail scServiceDetail = null;
            if (tSsD == null){
                tSsD = new HashSet<>();
                scServiceDetail = new ScServiceDetail();
            }else {
                scServiceDetail = tSsD.iterator().next();
                if (scServiceDetail == null){
                    scServiceDetail = new ScServiceDetail();
                }else {
                    scServiceDetail = tSsD.iterator().next();
                }
            }
            scServiceDetail.setScOrder(scOrder);
            tSsD.add(scServiceDetail);
            scOrder.setScServiceDetails(tSsD);

            Set<NwaEorEquipDetails> tNaEqd = scOrder.getNwaEorEquipDetails();
            NwaEorEquipDetails nwaEorEquipDetails = null;
            if(tNaEqd == null){
                tNaEqd = new HashSet<>();
                nwaEorEquipDetails = new NwaEorEquipDetails();
                System.out.println("=============New Equipment data ");
            }else {
                nwaEorEquipDetails = tNaEqd.iterator().next();
                if(nwaEorEquipDetails == null){
                    nwaEorEquipDetails = new NwaEorEquipDetails();
                }else {
                    nwaEorEquipDetails = tNaEqd.iterator().next();
                    System.out.println("=============Existing Equipment data ");
                }
            }
            nwaEorEquipDetails.setHostName((String) payLoad.get("hostName "));
            nwaEorEquipDetails.setCountry((String) payLoad.get("country"));
            nwaEorEquipDetails.setState((String) payLoad.get("state"));
            nwaEorEquipDetails.setCity((String) payLoad.get("city"));
            nwaEorEquipDetails.setCityOther((String) payLoad.get("cityOther"));
            nwaEorEquipDetails.setArea((String) payLoad.get("area"));
            nwaEorEquipDetails.setAreaOther((String) payLoad.get("areaOther"));
            nwaEorEquipDetails.setBldg((String) payLoad.get("bldg"));
            nwaEorEquipDetails.setBldgOther((String) payLoad.get("bldgOther"));
            nwaEorEquipDetails.setFloor((String) payLoad.get("floor"));
            nwaEorEquipDetails.setSiteId((String) payLoad.get("siteId"));
            nwaEorEquipDetails.setSiteAddress((String) payLoad.get("siteAddress"));
            nwaEorEquipDetails.setEqpmntName((String) payLoad.get("eqpmntName"));
            nwaEorEquipDetails.setEqpmntIp((String) payLoad.get("eqpmntIp"));
            nwaEorEquipDetails.setEqpmntType((String) payLoad.get("eqpmntType"));
            nwaEorEquipDetails.setPowerType((String) payLoad.get("powerType"));
            nwaEorEquipDetails.setPowerRating((String) payLoad.get("powerRating"));
            nwaEorEquipDetails.setPopType((String) payLoad.get("popType"));
            nwaEorEquipDetails.setPopName((String) payLoad.get("popName"));
            nwaEorEquipDetails.setPopAddress((String) payLoad.get("popAddress"));
            nwaEorEquipDetails.setNodeOwner((String) payLoad.get("nodeOwner"));
            nwaEorEquipDetails.setNodeOwnerOther((String) payLoad.get("nodeOwnerOther"));
            nwaEorEquipDetails.setInterfaceToParent((String) payLoad.get("interfaceToParent"));
            nwaEorEquipDetails.setInterFibreReq((String) payLoad.get("interFibreReq"));
            nwaEorEquipDetails.setEorEquipStatus((String) payLoad.get("eorequipmentStatus"));
            if(payLoad.get("eorequipmentStatus") == null){
                nwaEorEquipDetails.setEorEquipStatus((String) payLoad.get("eorEquipStatus"));
            }
            nwaEorEquipDetails.setMake((String) payLoad.get("make"));
            nwaEorEquipDetails.setModel((String) payLoad.get("model"));
            nwaEorEquipDetails.setCoverage((String) payLoad.get("coverage"));
            nwaEorEquipDetails.setRole((String) payLoad.get("role"));
            nwaEorEquipDetails.setOwner((String) payLoad.get("owner"));
            nwaEorEquipDetails.setPopOwner((String) payLoad.get("popOwner"));
            nwaEorEquipDetails.setIsManEquipment((String) payLoad.get("isManEquipment"));

            nwaEorEquipDetails.setHostName1((String) payLoad.get("hostName1"));
            nwaEorEquipDetails.setAggr1Country((String) payLoad.get("aggr1Country"));
            nwaEorEquipDetails.setAggr1State((String) payLoad.get("aggr1State"));
            nwaEorEquipDetails.setAggr1City((String) payLoad.get("aggr1City"));
            nwaEorEquipDetails.setAggr1Bldg((String) payLoad.get("aggr1Bldg"));
            nwaEorEquipDetails.setAggr1Area((String) payLoad.get("aggr1Area"));
            nwaEorEquipDetails.setAggr1Floor((String) payLoad.get("aggr1Floor"));
            nwaEorEquipDetails.setAggr1SiteAddress((String) payLoad.get("aggr1SiteAddress"));
            nwaEorEquipDetails.setAggr1SiteId((String) payLoad.get("aggr1SiteId"));
            nwaEorEquipDetails.setAggr1PopAddress((String) payLoad.get("aggr1PopAddress"));
            nwaEorEquipDetails.setAggr1ParentRouter((String) payLoad.get("aggr1ParentRouter"));
            nwaEorEquipDetails.setAggr1EqpmntPort((String) payLoad.get("aggr1EqpmntPort"));
            nwaEorEquipDetails.setAggr1EqpmntPort2((String) payLoad.get("aggr1EqpmntPort2"));
            nwaEorEquipDetails.setAggr1EqpmntIp((String) payLoad.get("aggr1EqpmntIp"));
            nwaEorEquipDetails.setAggr1EndMuxAddress((String) payLoad.get("aggr1EndMuxAddress"));

            nwaEorEquipDetails.setHostName2((String) payLoad.get("hostName2"));
            nwaEorEquipDetails.setAggr2Country((String) payLoad.get("aggr2Country"));
            nwaEorEquipDetails.setAggr2State((String) payLoad.get("aggr2State"));
            nwaEorEquipDetails.setAggr2City((String) payLoad.get("aggr2City"));
            nwaEorEquipDetails.setAggr2Bldg((String) payLoad.get("aggr2Bldg"));
            nwaEorEquipDetails.setAggr2Area((String) payLoad.get("aggr2Area"));
            nwaEorEquipDetails.setAggr2Floor((String) payLoad.get("aggr2Floor"));
            nwaEorEquipDetails.setAggr2SiteAddress((String) payLoad.get("aggr2SiteAddress"));
            nwaEorEquipDetails.setAggr2SiteId((String) payLoad.get("aggr2SiteId"));
            nwaEorEquipDetails.setAggr2PopAddress((String) payLoad.get("aggr2PopAddress"));
            nwaEorEquipDetails.setAggr2ParentRouter((String) payLoad.get("aggr2ParentRouter"));
            nwaEorEquipDetails.setAggr2EqpmntPort((String) payLoad.get("aggr2EqpmntPort"));
            nwaEorEquipDetails.setAggr2EqpmntPort2((String) payLoad.get("aggr2EqpmntPort2"));
            nwaEorEquipDetails.setAggr2EqpmntIp((String) payLoad.get("aggr2EqpmntIp"));
            nwaEorEquipDetails.setAggr2EndMuxAddress((String) payLoad.get("aggr2EndMuxAddress"));

            nwaEorEquipDetails.setSdNocSaNoc((String) payLoad.get("sdnoc"));
            nwaEorEquipDetails.setEthernetAccess((String) payLoad.get("ethernetAccess"));
            //nwaEorEquipDetails.setGetEthernetAccessEorType((String) payLoad.get("getEthernetAccessEorType"));
            nwaEorEquipDetails.setNetworkType((String) payLoad.get("networkType"));
            nwaEorEquipDetails.setNodeId((String) payLoad.get("nodeId"));
            nwaEorEquipDetails.setaEndMuxName((String) payLoad.get("aEndMuxName"));
            nwaEorEquipDetails.setaEndMuxName2((String) payLoad.get("aEndMuxName2"));
            nwaEorEquipDetails.setSecondEndMuxPort1((String) payLoad.get("secondEndMuxPort1"));
            nwaEorEquipDetails.setGetSecondEndMuxPort2((String) payLoad.get("getSecondEndMuxPort2"));
            nwaEorEquipDetails.setWearhouseLocation((String) payLoad.get("wearhouseLocation"));
            nwaEorEquipDetails.setSerialNumber((String) payLoad.get("serialNumber"));
            nwaEorEquipDetails.setTerminationLink((String) payLoad.get("terminationLink"));
            nwaEorEquipDetails.setPriority((String) payLoad.get("priority"));
            nwaEorEquipDetails.setRackFeasibility((String) payLoad.get("rackFeasibility"));
            nwaEorEquipDetails.setFiberFeasibility((String) payLoad.get("fiberFeasibility"));
            nwaEorEquipDetails.setInfraFeasibility((String) payLoad.get("infraFeasibility"));
            nwaEorEquipDetails.setGspiBranch((String) payLoad.get("gspiBranch"));
            nwaEorEquipDetails.setParentOrder((String) payLoad.get("parentOrder"));
            nwaEorEquipDetails.setChildOrder((String) payLoad.get("childOrder"));
            nwaEorEquipDetails.setSibling((String) payLoad.get("sibling"));
            nwaEorEquipDetails.setNodeName3((String) payLoad.get("nodeName"));
            nwaEorEquipDetails.setNodeType((String) payLoad.get("nodeType"));
            nwaEorEquipDetails.setMgmtIPAddress((String) payLoad.get("mgmtIPAddress"));
            nwaEorEquipDetails.setCoverage2((String) payLoad.get("coverage2"));
            nwaEorEquipDetails.setaEndRemoteNode((String) payLoad.get("aEndRemoteNode"));
            nwaEorEquipDetails.setPortName1((String) payLoad.get("portName1"));
            nwaEorEquipDetails.setPortDescription2((String) payLoad.get("portDescription"));
            nwaEorEquipDetails.setPortName2((String) payLoad.get("portName2"));
            nwaEorEquipDetails.setPortDescription2((String) payLoad.get("portDescription2"));
            nwaEorEquipDetails.setConfigUplink((String) payLoad.get("configUplink"));
            nwaEorEquipDetails.setConfigUplink2((String) payLoad.get("configUplink2"));
            nwaEorEquipDetails.setConfigUplink3((String) payLoad.get("configUplink3"));
            nwaEorEquipDetails.setConfigUplink4((String) payLoad.get("configUplink4"));
            nwaEorEquipDetails.setzEndRemoteNode((String) payLoad.get("zEndRemoteNode"));
            nwaEorEquipDetails.setzEndEorId((String) payLoad.get("zEndEorId"));
            nwaEorEquipDetails.setaEndLocalPort((String) payLoad.get("aEndLocalPort"));
            nwaEorEquipDetails.setCfmMip((String) payLoad.get("cfmMip"));
            nwaEorEquipDetails.setzEndLocalPort((String) payLoad.get("zEndLocalPort"));
            nwaEorEquipDetails.setDeviceRole((String) payLoad.get("deviceRole"));
            nwaEorEquipDetails.setMgmntvlan((String) payLoad.get("mgmntvlan"));
            nwaEorEquipDetails.setVlanInterfaceDes((String) payLoad.get("vlanInterfaceDes"));
            nwaEorEquipDetails.setCfmDefault((String) payLoad.get("cfmDefault"));
            nwaEorEquipDetails.setCityName1((String) payLoad.get("cityName1"));
            nwaEorEquipDetails.setSwitchLocation((String) payLoad.get("switchLocation"));
            nwaEorEquipDetails.setNodeName3((String) payLoad.get("nodeName3"));
            nwaEorEquipDetails.setControlVlanId((String) payLoad.get("controlVlanId"));
            nwaEorEquipDetails.setErpsRingId((String) payLoad.get("erpsRingId"));
            nwaEorEquipDetails.setGateWayIpAddressNtp((String) payLoad.get("gateWayIpAddressNtp"));
            nwaEorEquipDetails.setProtectedInstanceId((String) payLoad.get("protectedInstanceId"));
            nwaEorEquipDetails.setSubring((String) payLoad.get("subring"));

            nwaEorEquipDetails.setPowerFeasibility((String) payLoad.get("powerFeasibility"));
            nwaEorEquipDetails.setRackUnitSpace((String) payLoad.get("rackUnitSpace"));
            nwaEorEquipDetails.setSwitchManagementIp((String) payLoad.get("switchManagementIp"));
            nwaEorEquipDetails.setVlan((String) payLoad.get("vlan"));
            nwaEorEquipDetails.setSubnetIpPool1((String) payLoad.get("subnetIpPool1"));
            nwaEorEquipDetails.setSubnetIpPool2((String) payLoad.get("subnetIpPool2"));
            nwaEorEquipDetails.setNodeName((String) payLoad.get("nodeName"));
            nwaEorEquipDetails.setPortDescription((String) payLoad.get("portDescription"));
            nwaEorEquipDetails.setPortDescription3((String) payLoad.get("portDescription3"));
            nwaEorEquipDetails.setPortDescription4((String) payLoad.get("portDescription4"));
            nwaEorEquipDetails.setControlVlanId((String) payLoad.get("controlVlanId"));
            nwaEorEquipDetails.setEthernetAccessEorType((String) payLoad.get("ethernetAccessEorType"));
            nwaEorEquipDetails.setDcnReq((String) payLoad.get("dcnReq"));
            nwaEorEquipDetails.setRingType((String) payLoad.get("ringType"));
            nwaEorEquipDetails.setSwitchName((String) payLoad.get("switchName"));
            nwaEorEquipDetails.setSwitchIp((String) payLoad.get("switchIp"));
            nwaEorEquipDetails.setIsSwitchPoolRequired((String) payLoad.get("isSwitchPoolRequired"));


            nwaEorEquipDetails.setScOrder(scOrder);
            tNaEqd.add(nwaEorEquipDetails);
            scOrder.setNwaEorEquipDetails(tNaEqd);

            ArrayList cardsArrayList = (ArrayList) payLoad.get("cards_array");
            if ("EORIP".equalsIgnoreCase((String) payLoad.get("orderType")) || "EORTXCARD".equalsIgnoreCase((String) payLoad.get("orderType"))) {
                List cardsArray = (List) payLoad.get("cards_array");
                if (cardsArray != null) {
                    Set<NwaCardDetails> tNwCrDtls = new HashSet<>();
                    for (int i = 0; i < cardsArray.size(); i++) {
                        Map<String, Object> cardsData = (Map<String, Object>) cardsArray.get(i);
                        NwaCardDetails nwaCardDetails = new NwaCardDetails();
                        nwaCardDetails.setCardSrNumber((String) cardsData.get("sr_no"));
                        nwaCardDetails.setCardType((String) cardsData.get("cardType"));
                        nwaCardDetails.setUniqueId((String) cardsData.get("uid"));
                        nwaCardDetails.setShelfDetails((String) cardsData.get("shelf_no"));
                        nwaCardDetails.setShelfSlotDetails((String) cardsData.get("shelf_slot_no"));
                        nwaCardDetails.setSlotDetails((String) cardsData.get("card_slot_no"));
                        nwaCardDetails.setSubSlotNo((String) cardsData.get("subSlotNo"));
                        nwaCardDetails.setSeqNo(i);

                        nwaCardDetails.setScOrder(scOrder);
                        tNwCrDtls.add(nwaCardDetails);
                    }

                    scOrder.setNwaCardDetails(tNwCrDtls);
                } else {
                    throw new RuntimeException("Cards information missing");
                }
            }

        Set<NwaAccessEorDetails> tNaEd = scOrder.getNwaAccessEorDetails();
        NwaAccessEorDetails nwaAccessEorDetails = null;
        if (tNaEd == null){
            tNaEd = new HashSet<>();
            nwaAccessEorDetails = new NwaAccessEorDetails();
        }else {
            nwaAccessEorDetails = tNaEd.iterator().next();
            if (nwaAccessEorDetails == null){
                nwaAccessEorDetails = new NwaAccessEorDetails();
            }else {
                nwaAccessEorDetails = tNaEd.iterator().next();
            }
        }

        nwaAccessEorDetails.setOrderCode((String) payLoad.get("opOrderCode"));
        nwaAccessEorDetails.setaEndEorID((String) payLoad.get("aEndEorID"));
        nwaAccessEorDetails.setaEndNeighbourHostName((String) payLoad.get("aEndNeighbourHostName"));
        nwaAccessEorDetails.setaEndNeighbourPortDetails((String) payLoad.get("aEndNeighbourPortDetails"));
        nwaAccessEorDetails.setbEndNeighbourPortDetails((String) payLoad.get("bEndNeighbourPortDetails"));
        nwaAccessEorDetails.setCfmMipLevel((String) payLoad.get("cfmMipLevel"));
        nwaAccessEorDetails.setConfigureAEndUplinkPort((String) payLoad.get("configureAEndUplinkPort"));
        nwaAccessEorDetails.setControlVlanId((String) payLoad.get("controlVlanId"));
        nwaAccessEorDetails.setDescAEndNeighbour((String) payLoad.get("descAEndNeighbour"));
        nwaAccessEorDetails.setDescNewDeviceAEndUplinkPort((String) payLoad.get("descNewDeviceAEndUplinkPort"));
        nwaAccessEorDetails.setErpsRingId((String) payLoad.get("erpsRingId"));
        nwaAccessEorDetails.setGateWayIpAddressNtp((String) payLoad.get("gateWayIpAddressNtp"));
        nwaAccessEorDetails.setHostName((String) payLoad.get("aedHostName"));
        nwaAccessEorDetails.setInitTemplate((String) payLoad.get("initTemplate "));
        nwaAccessEorDetails.setManagementVlanDescription((String) payLoad.get("managementVlanDescription"));

        nwaAccessEorDetails.setScOrder(scOrder);
		tNaEd.add(nwaAccessEorDetails);
		scOrder.setNwaAccessEorDetails(tNaEd);

            Set<NwaOrderDetailsExtnd> tNaEed = scOrder.getNwaOrderDetailsExtnds();
            NwaOrderDetailsExtnd nwaOrderDetailsExtnd = null;
            if (tNaEed == null){
                tNaEed = new HashSet<>();
                nwaOrderDetailsExtnd = new NwaOrderDetailsExtnd();
            }else {
                nwaOrderDetailsExtnd = tNaEed.iterator().next();
                if (nwaOrderDetailsExtnd == null){
                    nwaOrderDetailsExtnd = new NwaOrderDetailsExtnd();
                }else {
                    nwaOrderDetailsExtnd = tNaEed.iterator().next();
                }
            }

            nwaOrderDetailsExtnd.setWarehouseLocation((String) payLoad.get("warehouseLocation"));
            nwaOrderDetailsExtnd.setSerialNo((String) payLoad.get("serialNo"));
            nwaOrderDetailsExtnd.setSoftwareVersion((String) payLoad.get("softwareVersion"));
            nwaOrderDetailsExtnd.setPoFrnNo((String) payLoad.get("poFrnNo"));
            nwaOrderDetailsExtnd.setIprm((String) payLoad.get("iprm"));
            nwaOrderDetailsExtnd.setIpPool((String) payLoad.get("ipPool"));
            nwaOrderDetailsExtnd.setLoopBackIp0((String) payLoad.get("loopBackIp0"));
            nwaOrderDetailsExtnd.setLoopBackIp1((String) payLoad.get("loopBackIp1"));
            nwaOrderDetailsExtnd.setPeDate((String) payLoad.get("peDate"));
            nwaOrderDetailsExtnd.setPeRequired((String) payLoad.get("peRequired"));
            nwaOrderDetailsExtnd.setFieldOps((String) payLoad.get("fieldOps"));

            nwaOrderDetailsExtnd.setScOrder(scOrder);
            tNaEed.add(nwaOrderDetailsExtnd);
            scOrder.setNwaOrderDetailsExtnds(tNaEed);

            Set<NwaLinkedOrderDetails> tNlordr = scOrder.getNwaLinkedOrderDetails();
            NwaLinkedOrderDetails nwaLinkedOrderDetails = null;
            if (tNlordr == null){
                tNlordr = new HashSet<>();
                nwaLinkedOrderDetails = new NwaLinkedOrderDetails();
            } else {
                nwaLinkedOrderDetails = tNlordr.iterator().next();
                if(nwaLinkedOrderDetails == null){
                    nwaLinkedOrderDetails = new NwaLinkedOrderDetails();
                }else {
                    nwaLinkedOrderDetails = tNlordr.iterator().next();
                }
            }

            nwaLinkedOrderDetails.setmAndLRequired((String) payLoad.get("mAndLRequired"));
            nwaLinkedOrderDetails.setMlGroup((String) payLoad.get("ml"));
            nwaLinkedOrderDetails.setDispatchDetails((String) payLoad.get("dispatchDetails"));
            nwaLinkedOrderDetails.setMrnType((String) payLoad.get("mrnType"));
            nwaLinkedOrderDetails.setAllMaterialReceived((String) payLoad.get("allMaterialReceived"));
            nwaLinkedOrderDetails.setMlGroup((String) payLoad.get("mlGroup"));
            nwaLinkedOrderDetails.setLinkedOrderId((String) payLoad.get("linkedOrderId"));
            nwaLinkedOrderDetails.setSiblingOrder((String) payLoad.get("siblingOrder"));
            nwaLinkedOrderDetails.setLinkedPe((String) payLoad.get("linkedPe"));
            
            nwaLinkedOrderDetails.setScOrder(scOrder);
            tNlordr.add(nwaLinkedOrderDetails);
            scOrder.setNwaLinkedOrderDetails(tNlordr);

            Set<NwaBtsDetails> nwaBtsDetailsSet = scOrder.getNwaBtsDetails();
            if(nwaBtsDetailsSet == null){
                nwaBtsDetailsSet = new HashSet<>();
            }
            NwaBtsDetails nwaBtsDetails = new NwaBtsDetails();
            nwaBtsDetails.setOrderCode(scOrder.getOpOrderCode());
            nwaBtsDetails.setBhType((String) payLoad.get("bhType"));
            nwaBtsDetails.setDeviceIp((String) payLoad.get("deviceIp"));
            nwaBtsDetails.setBtsVlan((String) payLoad.get("btsVlan"));
            nwaBtsDetails.setCircle((String) payLoad.get("circle"));
            nwaBtsDetails.setEohsCategory((String) payLoad.get("eohsCategory"));
            nwaBtsDetails.setInfraAggrementExpiryDate((Timestamp) payLoad.get("infraAggrementExpiryDate"));
            nwaBtsDetails.setInfraProvider((String) payLoad.get("infraProvider"));
            nwaBtsDetails.setInfraProviderSiteUpTime((String) payLoad.get("infraProviderSiteUpTime"));
            nwaBtsDetails.setPerAntennaDimension((String) payLoad.get("perAntennaDimension"));
            nwaBtsDetails.setPerOduWeight((String) payLoad.get("perOduWeight"));
            nwaBtsDetails.setPlannedInstallationDate((String) payLoad.get("plannedInstallationDate"));
            nwaBtsDetails.setPlannedDeinstallationDate((String) payLoad.get("plannedDeinstallationDate"));
            nwaBtsDetails.setReasonForExist((String) payLoad.get("reasonForExist"));
            nwaBtsDetails.setSiteId((String) payLoad.get("siteId"));
            nwaBtsDetails.setSiteName((String) payLoad.get("siteName"));
            nwaBtsDetails.setSiteType((String) payLoad.get("siteType"));
            nwaBtsDetails.setBtsSubnetMask((String) payLoad.get("btsSubnetMask"));
            nwaBtsDetails.setTclAggrementStartDate((Timestamp) payLoad.get("tclAggrementStartDate"));
            nwaBtsDetails.setWirelessTechSubType((String) payLoad.get("wirelessTechSubType"));

            nwaBtsDetails.setScOrder(scOrder);
            nwaBtsDetailsSet.add(nwaBtsDetails);
            scOrder.setNwaBtsDetails(nwaBtsDetailsSet);

            Set<NwaRemarksDetails> tNrkDl = scOrder.getNwaRemarksDetails();
            NwaRemarksDetails nwaRemarksDetails = new NwaRemarksDetails();

            nwaRemarksDetails.setOrderCode((String) payLoad.get("opOrderCode"));
            nwaRemarksDetails.setByUser((String) payLoad.get("originatorName"));
            nwaRemarksDetails.setByUserGroup((String) payLoad.get("originatorGroupId"));
            nwaRemarksDetails.setReason((String) payLoad.get("comments"));
            nwaRemarksDetails.setRemarksDate(Timestamp.valueOf(LocalDateTime.now()));
            nwaRemarksDetails.setScenarioType(scOrder.getScenarioType());
            Task task = taskService.getTaskById((Integer) payLoad.get("taskId")) ;

            nwaRemarksDetails.setTaskName(task.getMstTaskDef().getName());

            nwaRemarksDetails.setScOrder(scOrder);
            tNrkDl.add(nwaRemarksDetails);
            scOrder.setNwaRemarksDetails(tNrkDl);

            scOrder = scOrderRepository.save(scOrder);

            MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
            mfTaskTrailBean.setTaskId((Integer) payLoad.get("taskId"));
            mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
            mfTaskTrailBean.setAction("Save Order");
            mfTaskTrailBean.setCompletedBy(task.getAssignee());
            mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
            mfTaskTrailBean.setDescription((String) payLoad.get("comments"));
            mfTaskTrailBean.setUserGroup(task.getMstTaskDef().getOwnerGroup());
            mfTaskTrailBean.setScenario(scOrder.getScenarioType());
            mfTaskTrailBean.setComments((String) payLoad.get("comments"));
            mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
            taskService.setTaskTrail(mfTaskTrailBean);

        }
        return  this.getOrderResultBean(scOrder);

    }

    private void updateStage(Task task){
        Process process = processRepository.getOne(task.getProcessId());
        Stage stage = process.getStage();
        stage.setServiceCode(task.getOrderCode());
        stage.setScOrderId(task.getServiceId());

        stageRepository.save(stage);
    }

    public OrderInitiateResultBean findScOrderByOrderCode (String opOrderCode) {
        ScOrder scOrderOptional = this.scOrderRepository.findByOpOrderCode(opOrderCode);

        if(scOrderOptional != null) {

            System.out.println("======>> NOT NULL");
            return this.getOrderResultBean(scOrderOptional);
        } else {
            System.out.println("======>>  NULL");
            return null;
        }
    }

    private OrderInitiateResultBean getOrderResultBean(ScOrder scOrder) {
        OrderInitiateResultBean orderInitiateResultBean = new OrderInitiateResultBean();

        ScServiceDetail scServiceDetail;
        Set<ScServiceDetail> serviceDetailSet = scOrder.getScServiceDetails();
        if(serviceDetailSet != null) {
            Iterator<ScServiceDetail> scServiceDetailIterator = serviceDetailSet.iterator();
            if(scServiceDetailIterator != null && scServiceDetailIterator.hasNext()) {
                scServiceDetail = scServiceDetailIterator.next();
            } else {
                scServiceDetail = new ScServiceDetail();
            }
        } else {
            scServiceDetail = new ScServiceDetail();
        }

        NwaEorEquipDetails nwaEorEquipDetails;
        Set<NwaEorEquipDetails> nwaEorEquipDetailsSet = scOrder.getNwaEorEquipDetails();
        if(nwaEorEquipDetailsSet != null) {
            Iterator<NwaEorEquipDetails> nwaEorEquipDetailsIterator = nwaEorEquipDetailsSet.iterator();
            if(nwaEorEquipDetailsIterator != null && nwaEorEquipDetailsIterator.hasNext()) {
                nwaEorEquipDetails = nwaEorEquipDetailsIterator.next();
            } else {
                nwaEorEquipDetails = new NwaEorEquipDetails();
            }
        } else {
            nwaEorEquipDetails = new NwaEorEquipDetails();
        }

        NwaAccessEorDetails nwaAccessEorDetails;
        Set<NwaAccessEorDetails> nwaAccessEorDetailsSet = scOrder.getNwaAccessEorDetails();
        if(nwaAccessEorDetailsSet != null) {
            Iterator<NwaAccessEorDetails> nwaAccessEorDetailsIterator = nwaAccessEorDetailsSet.iterator();
            if(nwaAccessEorDetailsIterator != null && nwaAccessEorDetailsIterator.hasNext()) {
                nwaAccessEorDetails = nwaAccessEorDetailsIterator.next();
            } else {
                nwaAccessEorDetails = new NwaAccessEorDetails();
            }
        } else {
            nwaAccessEorDetails = new NwaAccessEorDetails();
        }

        NwaOrderDetailsExtnd nwaOrderDetailsExtnd;
        Set<NwaOrderDetailsExtnd> nwaOrderDetailsExtndSet = scOrder.getNwaOrderDetailsExtnds();
        if(nwaOrderDetailsExtndSet != null) {
            Iterator<NwaOrderDetailsExtnd> nwaOrderDetailsExtndIterator = nwaOrderDetailsExtndSet.iterator();
            if(nwaOrderDetailsExtndIterator != null && nwaOrderDetailsExtndIterator.hasNext()) {
                nwaOrderDetailsExtnd = nwaOrderDetailsExtndIterator.next();
            } else {
                nwaOrderDetailsExtnd = new NwaOrderDetailsExtnd();
            }
        } else {
            nwaOrderDetailsExtnd = new NwaOrderDetailsExtnd();
        }

        NwaLinkedOrderDetails nwaLinkedOrderDetails;
        Set<NwaLinkedOrderDetails> nwaLinkedOrderDetailsSet = scOrder.getNwaLinkedOrderDetails();
        if(nwaLinkedOrderDetailsSet != null) {
            Iterator<NwaLinkedOrderDetails> nwaLinkedOrderDetailsIterator = nwaLinkedOrderDetailsSet.iterator();
            if(nwaLinkedOrderDetailsIterator != null && nwaLinkedOrderDetailsIterator.hasNext()) {
                nwaLinkedOrderDetails = nwaLinkedOrderDetailsIterator.next();
            } else {
                nwaLinkedOrderDetails = new NwaLinkedOrderDetails();
            }
        } else {
            nwaLinkedOrderDetails = new NwaLinkedOrderDetails();
        }

        NwaRemarksDetails nwaRemarksDetails;
        Set<NwaRemarksDetails> nwaRemarksDetailsSet = scOrder.getNwaRemarksDetails();
        if(nwaAccessEorDetailsSet != null) {
            Iterator<NwaRemarksDetails> nwaRemarksDetailsIterator = nwaRemarksDetailsSet.iterator();
            if(nwaRemarksDetailsIterator != null && nwaRemarksDetailsIterator.hasNext()){
                nwaRemarksDetails = nwaRemarksDetailsIterator.next();
            }else {
                nwaRemarksDetails = new NwaRemarksDetails();
            }

        }else {
            nwaRemarksDetails = new NwaRemarksDetails();
        }

        NwaCardDetails nwaCardDetails;
        Set<NwaCardDetails> nwaCardDetailsSet = scOrder.getNwaCardDetails();
        if(nwaCardDetailsSet != null){
            Iterator<NwaCardDetails> nwaCardDetailsIterator = nwaCardDetailsSet.iterator();
            ArrayList<Map<String, Object>> nwaCardDetailsArrayList = new ArrayList<>();
            while (nwaCardDetailsIterator != null &&nwaCardDetailsIterator.hasNext()){

                nwaCardDetails = nwaCardDetailsIterator.next();
                    Map<String,Object> cardDetailsMap = new HashMap<>();
                    cardDetailsMap.put("sr_no",nwaCardDetails.getCardSrNumber());
                    cardDetailsMap.put("cardType",nwaCardDetails.getCardType());
                    cardDetailsMap.put("uid",nwaCardDetails.getUniqueId());
                    cardDetailsMap.put("shelf_no",nwaCardDetails.getShelfDetails());
                    cardDetailsMap.put("shelf_slot_no",nwaCardDetails.getShelfSlotDetails());
                    cardDetailsMap.put("card_slot_no",nwaCardDetails.getSlotDetails());
                    cardDetailsMap.put("subSlotNo",nwaCardDetails.getSubSlotNo());
                    cardDetailsMap.put("shelfType",nwaCardDetails.getShelfType());
                    cardDetailsMap.put("cardDescription",nwaCardDetails.getCardDescription());
                    cardDetailsMap.put("cardSrNumber",nwaCardDetails.getCardSrNumber());
                    nwaCardDetailsArrayList.add(cardDetailsMap);
                orderInitiateResultBean.setNwaCardDetailsArrayList(nwaCardDetailsArrayList);
            }
        }else {
            nwaCardDetails = new NwaCardDetails();
        }
        
        NwaBtsDetails nwaBtsDetails = null;
        Set<NwaBtsDetails> nwaBtsDetailsSet = scOrder.getNwaBtsDetails();
        if(nwaBtsDetailsSet != null){
            Iterator<NwaBtsDetails> nwaBtsDetailsIterator = nwaBtsDetailsSet.iterator();
            if(nwaBtsDetailsIterator != null){
                nwaBtsDetails = nwaBtsDetailsIterator.next();
            }
        }
        /*Task task;
        Set<Task> taskSet = scServiceDetail.getTasks();
        if(taskSet != null) {
            Iterator<Task> taskIterator = taskSet.iterator();
            if(taskIterator != null && taskIterator.hasNext()) {
                task = taskIterator.next();
            } else {
                task = new Task();
            }
        } else {
            task = new Task();
        }

        orderInitiateResultBean.setTaskName(task.getMstTaskDef().getName());
        orderInitiateResultBean.setTaskStatus(task.getMstStatus().getCode());
        orderInitiateResultBean.setTaskClaimTime(task.getClaimTime());*/

        // orderInitiateResultBean.setOpOrderCode(scOrder.getOpOrderCode());
        orderInitiateResultBean.setOpOrderCode(scOrder.getOpOrderCode());
        System.out.println("Operation Order Code =================" + scOrder.getOpOrderCode());
        orderInitiateResultBean.setScOrderUuid(scOrder.getUuid());
        orderInitiateResultBean.setOrderType(scOrder.getOrderType());
        orderInitiateResultBean.setSubject(scOrder.getSubject());
        orderInitiateResultBean.setProjectType(scOrder.getProjectType());
        orderInitiateResultBean.setScenarioType(scOrder.getScenarioType());
        orderInitiateResultBean.setTechnologyType(scOrder.getTechnologyType());
        orderInitiateResultBean.setPmName(scOrder.getPmName());
        orderInitiateResultBean.setPmContactEmail(scOrder.getPmContactEmail());
        orderInitiateResultBean.setOriginatorName(scOrder.getOriginatorName());
        orderInitiateResultBean.setOriginatorContactNumber(scOrder.getOriginatorContactNumber());
        //String orderCreateDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(scOrder.getOrderCreationDate());
        orderInitiateResultBean.setOrderCreationDate(scOrder.getOrderCreationDate());
        System.out.println("Order creationDate is "+scOrder.getOrderCreationDate());
        orderInitiateResultBean.setOriginatorGroupId(scOrder.getOriginatorGroupId());
        orderInitiateResultBean.setUuid(scOrder.getUuid());
        orderInitiateResultBean.setServiceGroupId(scOrder.getOriginatorGroupId());
        orderInitiateResultBean.setProjectName(scOrder.getProjectName());
        orderInitiateResultBean.setCityTier(scOrder.getCityTier());
        orderInitiateResultBean.setEorConfirmedBy(scOrder.getEorConfirmedBy());
        orderInitiateResultBean.setRfmRfs(scOrder.getRfmRfs());
        orderInitiateResultBean.setRfmRfsAssignTo(scOrder.getRfmRfsAssignTo());

        orderInitiateResultBean.setScOrderUuid(scServiceDetail.getUuid());
        orderInitiateResultBean.setSmName(scServiceDetail.getSmName());
        orderInitiateResultBean.setSmEmail(scServiceDetail.getSmEmail());
        //orderInitiateResultBean.setOrderStatus(scOrder.getOrderStatus());
        orderInitiateResultBean.setOrderStatus(scServiceDetail.getMstStatus().getCode());

        orderInitiateResultBean.setHostName(nwaEorEquipDetails.getHostName());
        orderInitiateResultBean.setaEndMuxName(nwaEorEquipDetails.getaEndMuxName());
        orderInitiateResultBean.setaEndMuxPort(nwaEorEquipDetails.getaEndMuxPort());
        orderInitiateResultBean.setaEndMuxPort2(nwaEorEquipDetails.getaEndMuxPort2());
        orderInitiateResultBean.setAggrParentRouterIp(nwaEorEquipDetails.getAggrParentRouterIp());
        orderInitiateResultBean.setCountry(nwaEorEquipDetails.getCountry());
        orderInitiateResultBean.setState(nwaEorEquipDetails.getState());
        orderInitiateResultBean.setCity(nwaEorEquipDetails.getCity());
        orderInitiateResultBean.setCityOther(nwaEorEquipDetails.getCityOther());
        orderInitiateResultBean.setArea(nwaEorEquipDetails.getArea());
        orderInitiateResultBean.setAreaOther(nwaEorEquipDetails.getAreaOther());
        orderInitiateResultBean.setBldg(nwaEorEquipDetails.getBldg());
        System.out.println("BLDG===================" + nwaEorEquipDetails.getBldg() );
        orderInitiateResultBean.setBldgOther(nwaEorEquipDetails.getBldgOther());
        orderInitiateResultBean.setCoverage(nwaEorEquipDetails.getCoverage());
        orderInitiateResultBean.setFloor(nwaEorEquipDetails.getFloor());
        orderInitiateResultBean.setSiteName(nwaEorEquipDetails.getSiteName());
        orderInitiateResultBean.setSiteAddress(nwaEorEquipDetails.getSiteAddress());
        orderInitiateResultBean.setEqpmntName(nwaEorEquipDetails.getEqpmntName());
        orderInitiateResultBean.setEqpmntIp(nwaEorEquipDetails.getEqpmntIp());
        orderInitiateResultBean.setDeviceType(nwaEorEquipDetails.getDeviceType());
        orderInitiateResultBean.setEqpmntType(nwaEorEquipDetails.getEqpmntType());
        orderInitiateResultBean.setPowerType(nwaEorEquipDetails.getPowerType());
        orderInitiateResultBean.setPowerRating(nwaEorEquipDetails.getPowerRating());
        orderInitiateResultBean.setPopType(nwaEorEquipDetails.getPopType());
        orderInitiateResultBean.setPopName(nwaEorEquipDetails.getPopName());
        orderInitiateResultBean.setPopAddress(nwaEorEquipDetails.getPopAddress());
        orderInitiateResultBean.setNodeOwner(nwaEorEquipDetails.getNodeOwner());
        orderInitiateResultBean.setNodeOwnerOther(nwaEorEquipDetails.getNodeOwnerOther());
        orderInitiateResultBean.setInterfaceToParent(nwaEorEquipDetails.getInterfaceToParent());
        orderInitiateResultBean.setInterFibreReq(nwaEorEquipDetails.getInterFibreReq());
        orderInitiateResultBean.setEorEquipStatus(nwaEorEquipDetails.getEorEquipStatus());
        orderInitiateResultBean.setEorequipmentStatus(nwaEorEquipDetails.getEorEquipStatus());
        orderInitiateResultBean.setCoverage(nwaEorEquipDetails.getCoverage());
        orderInitiateResultBean.setRole(nwaEorEquipDetails.getRole());
        orderInitiateResultBean.setPopOwner(nwaEorEquipDetails.getPopOwner());
       // orderInitiateResultBean.setPopOwner(nwaEorEquipDetails.getPopOwner());
        orderInitiateResultBean.setIsManEquipment(nwaEorEquipDetails.getIsManEquipment());
        orderInitiateResultBean.setIsSwitchIpRequird(nwaEorEquipDetails.getIsSwitchIpRequird());
        orderInitiateResultBean.setMajorPop(nwaEorEquipDetails.getMajorPop());
        orderInitiateResultBean.setMicroPop(nwaEorEquipDetails.getMicroPop());
        //orderInitiateResultBean.setNocTeam(nwaEorEquipDetails.setNocTeam());
        orderInitiateResultBean.setNodeOwner(nwaEorEquipDetails.getNodeOwner());
        orderInitiateResultBean.setNodeOwnerOther(nwaEorEquipDetails.getNodeOwnerOther());
        orderInitiateResultBean.setPoolSize(nwaEorEquipDetails.getPoolSize());
        orderInitiateResultBean.setPopName(nwaEorEquipDetails.getPopName());
        orderInitiateResultBean.setPopOwner(nwaEorEquipDetails.getPopOwner());
        orderInitiateResultBean.setPopType(nwaEorEquipDetails.getPopType());
        orderInitiateResultBean.setPopAddress(nwaEorEquipDetails.getPopAddress());
        orderInitiateResultBean.setPowerRating(nwaEorEquipDetails.getPowerRating());
        orderInitiateResultBean.setPowerType(nwaEorEquipDetails.getPowerType());
        orderInitiateResultBean.setRingType(nwaEorEquipDetails.getRingType());
        orderInitiateResultBean.setSiteAddress(nwaEorEquipDetails.getSiteAddress());
        orderInitiateResultBean.setSpareScrap(nwaEorEquipDetails.getSpareScrap());
        orderInitiateResultBean.setSubnetIpPool1(nwaEorEquipDetails.getSubnetIpPool1());
        orderInitiateResultBean.setSubnetIpPool2(nwaEorEquipDetails.getSubnetIpPool2());
        orderInitiateResultBean.setSwitchIpPool(nwaEorEquipDetails.getSwitchIpPool());
        orderInitiateResultBean.setSwitchManagementIp(nwaEorEquipDetails.getSwitchManagementIp());
        orderInitiateResultBean.setSwitchManagementVlan(nwaEorEquipDetails.getSwitchManagementVlan());
        orderInitiateResultBean.setSwitchSubnetMask(nwaEorEquipDetails.getSwitchSubnetMask());
        orderInitiateResultBean.setVlan(nwaEorEquipDetails.getVlan());
        orderInitiateResultBean.setEorWimaxOrderSite(nwaEorEquipDetails.getEorWimaxOrderSite());
        orderInitiateResultBean.setSiteEnd(nwaEorEquipDetails.getSiteEnd());
        orderInitiateResultBean.setState(nwaEorEquipDetails.getState());
        orderInitiateResultBean.setSiteName(nwaEorEquipDetails.getSiteName());
        orderInitiateResultBean.setSiteId(nwaEorEquipDetails.getSiteId());
        orderInitiateResultBean.setRackUnitSpace(nwaEorEquipDetails.getRackUnitSpace());
        orderInitiateResultBean.setCityTier(nwaEorEquipDetails.getCityTier());
        orderInitiateResultBean.setTacacsRequired(nwaEorEquipDetails.getTacacsRequired());
        orderInitiateResultBean.setMake(nwaEorEquipDetails.getMake());
        orderInitiateResultBean.setModel(nwaEorEquipDetails.getModel());
        orderInitiateResultBean.setOwner(nwaEorEquipDetails.getOwner());
        orderInitiateResultBean.setCoverage(nwaEorEquipDetails.getCoverage());

        orderInitiateResultBean.setHostName1(nwaEorEquipDetails.getHostName1());
        orderInitiateResultBean.setAggr1EqpmntPort2(nwaEorEquipDetails.getAggr1EqpmntPort2());
        orderInitiateResultBean.setAggr1ParentRouter(nwaEorEquipDetails.getAggr1ParentRouter());
        orderInitiateResultBean.setAggr1Area(nwaEorEquipDetails.getAggr1Area());
        orderInitiateResultBean.setAggr1AreaOther(nwaEorEquipDetails.getAggr1AreaOther());
        orderInitiateResultBean.setAggr1Bldg(nwaEorEquipDetails.getAggr1Bldg());
        orderInitiateResultBean.setAggr1BldgOther(nwaEorEquipDetails.getAggr1BldgOther());
        orderInitiateResultBean.setAggr1City(nwaEorEquipDetails.getAggr1City());
        orderInitiateResultBean.setAggr1CityOther(nwaEorEquipDetails.getAggr1CityOther());
        orderInitiateResultBean.setAggr1Country(nwaEorEquipDetails.getAggr1Country());
        orderInitiateResultBean.setAggr1EndMuxAddress(nwaEorEquipDetails.getAggr1EndMuxAddress());
        orderInitiateResultBean.setAggr1EqpmntIp(nwaEorEquipDetails.getAggr1EqpmntIp());
        orderInitiateResultBean.setAggr1EqpmntName(nwaEorEquipDetails.getAggr1EqpmntName());
        orderInitiateResultBean.setAggr1EqpmntPort(nwaEorEquipDetails.getAggr1EqpmntPort());
        orderInitiateResultBean.setAggr1EqpmntType(nwaEorEquipDetails.getAggr1EqpmntType());
        orderInitiateResultBean.setAggr1Floor(nwaEorEquipDetails.getAggr1Floor());
        orderInitiateResultBean.setAggr1MajorPop(nwaEorEquipDetails.getAggr1MajorPop());
        orderInitiateResultBean.setAggr1MicraPop(nwaEorEquipDetails.getAggr1MicraPop());
        orderInitiateResultBean.setAggr1SiteAddress(nwaEorEquipDetails.getAggr1SiteAddress());
        orderInitiateResultBean.setAggr1State(nwaEorEquipDetails.getAggr1State());
        orderInitiateResultBean.setAggr1SiteName(nwaEorEquipDetails.getAggr1SiteName());
        orderInitiateResultBean.setAggr1PopAddress(nwaEorEquipDetails.getAggr1PopAddress());
        orderInitiateResultBean.setAggr1SiteId(nwaEorEquipDetails.getAggr1SiteId());

        orderInitiateResultBean.setHostName2(nwaEorEquipDetails.getHostName2());
        orderInitiateResultBean.setAggr2EqpmntPort2(nwaEorEquipDetails.getAggr2EqpmntPort2());
        orderInitiateResultBean.setAggr2ParentRouter(nwaEorEquipDetails.getAggr2ParentRouter());
        orderInitiateResultBean.setAggr2Area(nwaEorEquipDetails.getAggr2Area());
        orderInitiateResultBean.setAggr2AreaOther(nwaEorEquipDetails.getAggr2AreaOther());
        orderInitiateResultBean.setAggr2Bldg(nwaEorEquipDetails.getAggr2Bldg());
        orderInitiateResultBean.setAggr2BldgOther(nwaEorEquipDetails.getAggr2BldgOther());
        orderInitiateResultBean.setAggr2City(nwaEorEquipDetails.getAggr2City());
        orderInitiateResultBean.setAggr2CityOther(nwaEorEquipDetails.getAggr2CityOther());
        orderInitiateResultBean.setAggr2Country(nwaEorEquipDetails.getAggr2Country());
        System.out.println("==========Aggr2 Country = " + nwaEorEquipDetails.getAggr2Country());
        orderInitiateResultBean.setAggr2EndMuxAddress(nwaEorEquipDetails.getAggr2EndMuxAddress());
        orderInitiateResultBean.setAggr2EqpmntIp(nwaEorEquipDetails.getAggr2EqpmntIp());
        orderInitiateResultBean.setAggr2EqpmntName(nwaEorEquipDetails.getAggr2EqpmntName());
        orderInitiateResultBean.setAggr2EqpmntPort(nwaEorEquipDetails.getAggr2EqpmntPort());
        orderInitiateResultBean.setAggr2EqpmntType(nwaEorEquipDetails.getAggr2EqpmntType());
        orderInitiateResultBean.setAggr2Floor(nwaEorEquipDetails.getAggr2Floor());
        orderInitiateResultBean.setAggr2MajorPop(nwaEorEquipDetails.getAggr2MajorPop());
        orderInitiateResultBean.setAggr2MicraPop(nwaEorEquipDetails.getAggr2MicraPop());
        orderInitiateResultBean.setAggr2SiteAddress(nwaEorEquipDetails.getAggr2SiteAddress());
        orderInitiateResultBean.setAggr2State(nwaEorEquipDetails.getAggr2State());
        orderInitiateResultBean.setAggr2SiteName(nwaEorEquipDetails.getAggr2SiteName());
        orderInitiateResultBean.setAggr2PopAddress(nwaEorEquipDetails.getAggr2PopAddress());
        orderInitiateResultBean.setAggr2SiteId(nwaEorEquipDetails.getAggr2SiteId());

        orderInitiateResultBean.setPopType(nwaEorEquipDetails.getPopType());
        orderInitiateResultBean.setSdNocSaNoc(nwaEorEquipDetails.getSdNocSaNoc());
        orderInitiateResultBean.setSdnoc(nwaEorEquipDetails.getSdNocSaNoc());
        orderInitiateResultBean.setEthernetAccess(nwaEorEquipDetails.getEthernetAccess());
        //orderInitiateResultBean.setGetEthernetAccessEorType(nwaEorEquipDetails.getGetEthernetAccessEorType());
        orderInitiateResultBean.setNetworkType(nwaEorEquipDetails.getNetworkType());
        orderInitiateResultBean.setNodeId(nwaEorEquipDetails.getNodeId());
        orderInitiateResultBean.setaEndMuxName(nwaEorEquipDetails.getaEndMuxName());
        orderInitiateResultBean.setaEndMuxName2(nwaEorEquipDetails.getaEndMuxName2());
        orderInitiateResultBean.setSecondEndMuxPort1(nwaEorEquipDetails.getSecondEndMuxPort1());
        orderInitiateResultBean.setGetSecondEndMuxPort2(nwaEorEquipDetails.getGetSecondEndMuxPort2());
        orderInitiateResultBean.setWearhouseLocation(nwaEorEquipDetails.getWearhouseLocation());
        orderInitiateResultBean.setSerialNumber(nwaEorEquipDetails.getSerialNumber());
        orderInitiateResultBean.setTerminationLink(nwaEorEquipDetails.getTerminationLink());
        orderInitiateResultBean.setPriority(nwaEorEquipDetails.getPriority());
        orderInitiateResultBean.setRackFeasibility(nwaEorEquipDetails.getRackFeasibility());
        orderInitiateResultBean.setFiberFeasibility(nwaEorEquipDetails.getFiberFeasibility());
        orderInitiateResultBean.setInfraFeasibility(nwaEorEquipDetails.getInfraFeasibility());
        orderInitiateResultBean.setGspiBranch(nwaEorEquipDetails.getGspiBranch());
        orderInitiateResultBean.setParentOrder(nwaEorEquipDetails.getParentOrder());
        orderInitiateResultBean.setChildOrder(nwaEorEquipDetails.getChildOrder());
        orderInitiateResultBean.setSibling(nwaEorEquipDetails.getSibling());
        orderInitiateResultBean.setNodeName3(nwaEorEquipDetails.getNodeName3());
        orderInitiateResultBean.setNodeType(nwaEorEquipDetails.getNodeType());
        orderInitiateResultBean.setMgmtIPAddress(nwaEorEquipDetails.getMgmtIPAddress());
        orderInitiateResultBean.setCoverage2(nwaEorEquipDetails.getCoverage2());
        orderInitiateResultBean.setaEndRemoteNode(nwaEorEquipDetails.getaEndRemoteNode());
        orderInitiateResultBean.setPortName1(nwaEorEquipDetails.getPortName1());
        orderInitiateResultBean.setPortDescription2(nwaEorEquipDetails.getPortDescription2());
        orderInitiateResultBean.setPortName2(nwaEorEquipDetails.getPortName2());
        orderInitiateResultBean.setPortDescription2(nwaEorEquipDetails.getPortDescription2());
        orderInitiateResultBean.setConfigUplink(nwaEorEquipDetails.getConfigUplink());
        orderInitiateResultBean.setConfigUplink2(nwaEorEquipDetails.getConfigUplink2());
        orderInitiateResultBean.setConfigUplink3(nwaEorEquipDetails.getConfigUplink3());
        orderInitiateResultBean.setConfigUplink4(nwaEorEquipDetails.getConfigUplink4());
        orderInitiateResultBean.setzEndRemoteNode(nwaEorEquipDetails.getzEndRemoteNode());
        orderInitiateResultBean.setzEndEorId(nwaEorEquipDetails.getzEndEorId());
        orderInitiateResultBean.setaEndLocalPort(nwaEorEquipDetails.getaEndLocalPort());
        orderInitiateResultBean.setCfmMip(nwaEorEquipDetails.getCfmMip());
        orderInitiateResultBean.setzEndLocalPort(nwaEorEquipDetails.getzEndLocalPort());
        orderInitiateResultBean.setDeviceRole(nwaEorEquipDetails.getDeviceRole());
        orderInitiateResultBean.setMgmntvlan(nwaEorEquipDetails.getMgmntvlan());
        orderInitiateResultBean.setVlanInterfaceDes(nwaEorEquipDetails.getVlanInterfaceDes());
        orderInitiateResultBean.setCfmDefault(nwaEorEquipDetails.getCfmDefault());
        orderInitiateResultBean.setCityName1(nwaEorEquipDetails.getCityName1());
        orderInitiateResultBean.setSwitchLocation(nwaEorEquipDetails.getSwitchLocation());
        orderInitiateResultBean.setNodeName3(nwaEorEquipDetails.getNodeName3());
        orderInitiateResultBean.setControlVlanId(nwaEorEquipDetails.getControlVlanId());


        orderInitiateResultBean.setErpsRingId(nwaEorEquipDetails.getErpsRingId());
        orderInitiateResultBean.setGateWayIpAddressNtp(nwaEorEquipDetails.getGateWayIpAddressNtp());
        orderInitiateResultBean.setProtectedInstanceId(nwaEorEquipDetails.getProtectedInstanceId());
        orderInitiateResultBean.setSubring(nwaEorEquipDetails.getSubring());

        orderInitiateResultBean.setPowerFeasibility(nwaEorEquipDetails.getPowerFeasibility());
        orderInitiateResultBean.setRackUnitSpace(nwaEorEquipDetails.getRackUnitSpace());
        orderInitiateResultBean.setSwitchManagementIp(nwaEorEquipDetails.getSwitchManagementIp());
        orderInitiateResultBean.setVlan(nwaEorEquipDetails.getVlan());
        orderInitiateResultBean.setSubnetIpPool1(nwaEorEquipDetails.getSubnetIpPool1());
        orderInitiateResultBean.setSubnetIpPool2(nwaEorEquipDetails.getSubnetIpPool2());
        orderInitiateResultBean.setNodeName(nwaEorEquipDetails.getNodeName());
        orderInitiateResultBean.setPortDescription(nwaEorEquipDetails.getPortDescription());
        orderInitiateResultBean.setPortDescription3(nwaEorEquipDetails.getPortDescription3());
        orderInitiateResultBean.setPortDescription4(nwaEorEquipDetails.getPortDescription4());
        orderInitiateResultBean.setControlVlanId(nwaEorEquipDetails.getControlVlanId());
        orderInitiateResultBean.setEthernetAccessEorType(nwaEorEquipDetails.getEthernetAccessEorType());
        orderInitiateResultBean.setDcnReq(nwaEorEquipDetails.getDcnReq());
        orderInitiateResultBean.setSwitchName(nwaEorEquipDetails.getSwitchName());
        orderInitiateResultBean.setSwitchIp(nwaEorEquipDetails.getSwitchName());
        orderInitiateResultBean.setIsSwitchIpRequird(nwaEorEquipDetails.getIsSwitchIpRequird());

        //orderInitiateResultBean.setOpOrderCode(nwaAccessEorDetails.getOrderCode());
        orderInitiateResultBean.setaEndEorID(nwaAccessEorDetails.getaEndEorID());
        orderInitiateResultBean.setaEndNeighbourHostName(nwaAccessEorDetails.getaEndNeighbourHostName());
        orderInitiateResultBean.setaEndNeighbourPortDetails(nwaAccessEorDetails.getaEndNeighbourPortDetails());
        orderInitiateResultBean.setbEndNeighbourPortDetails(nwaAccessEorDetails.getbEndNeighbourPortDetails());
        orderInitiateResultBean.setCfmMipLevel(nwaAccessEorDetails.getCfmMipLevel());
        orderInitiateResultBean.setConfigureAEndUplinkPort(nwaAccessEorDetails.getConfigureAEndUplinkPort());
       // orderInitiateResultBean.setControlVlanId(nwaAccessEorDetails.getControlVlanId());
        orderInitiateResultBean.setDescAEndNeighbour(nwaAccessEorDetails.getDescAEndNeighbour());
        orderInitiateResultBean.setDescNewDeviceAEndUplinkPort(nwaAccessEorDetails.getDescNewDeviceAEndUplinkPort());
        orderInitiateResultBean.setErpsRingId(nwaAccessEorDetails.getErpsRingId());
        orderInitiateResultBean.setGateWayIpAddressNtp(nwaAccessEorDetails.getGateWayIpAddressNtp());
        orderInitiateResultBean.setAedHostName(nwaAccessEorDetails.getHostName());
        orderInitiateResultBean.setInitTemplate(nwaAccessEorDetails.getInitTemplate());
        orderInitiateResultBean.setManagementVlanDescription(nwaAccessEorDetails.getManagementVlanDescription());



        orderInitiateResultBean.setWarehouseLocation(nwaOrderDetailsExtnd.getWarehouseLocation());
        //String peFormattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(nwaOrderDetailsExtnd.getPeDate());
        orderInitiateResultBean.setPeDate(nwaOrderDetailsExtnd.getPeDate());
        /*String peDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(nwaOrderDetailsExtnd.getPeDate());
        orderInitiateResultBean.setPeDate(peDate);*/
        orderInitiateResultBean.setPeRequired(nwaOrderDetailsExtnd.getPeRequired());
        orderInitiateResultBean.setFieldOps(nwaOrderDetailsExtnd.getFieldOps());
        orderInitiateResultBean.setFoDetails(nwaOrderDetailsExtnd.getFieldOps());
        orderInitiateResultBean.setSerialNo(nwaOrderDetailsExtnd.getSerialNo());
        orderInitiateResultBean.setSoftwareVersion(nwaOrderDetailsExtnd.getSoftwareVersion());
        orderInitiateResultBean.setPoFrnNo(nwaOrderDetailsExtnd.getPoFrnNo());
        orderInitiateResultBean.setIprm(nwaOrderDetailsExtnd.getIprm());
        orderInitiateResultBean.setIpPool(nwaOrderDetailsExtnd.getIpPool());
        orderInitiateResultBean.setLoopBackIp0(nwaOrderDetailsExtnd.getLoopBackIp0());
        orderInitiateResultBean.setLoopBackIp1(nwaOrderDetailsExtnd.getLoopBackIp1());
        orderInitiateResultBean.setConfigrationManagment(nwaOrderDetailsExtnd.getConfigrationManagment());
        orderInitiateResultBean.setCramerTeam(nwaOrderDetailsExtnd.getCramerTeam());

        orderInitiateResultBean.setAllMaterialReceived(nwaLinkedOrderDetails.getAllMaterialReceived());
        orderInitiateResultBean.setmAndLRequired(nwaLinkedOrderDetails.getmAndLRequired());
        orderInitiateResultBean.setDispatchDetails(nwaLinkedOrderDetails.getDispatchDetails());
        orderInitiateResultBean.setMlGroup(nwaLinkedOrderDetails.getMlGroup());
        orderInitiateResultBean.setMrnType(nwaLinkedOrderDetails.getMrnType());
        orderInitiateResultBean.setLinkedOrderId(nwaLinkedOrderDetails.getLinkedOrderId());
        orderInitiateResultBean.setLinkedPe(nwaLinkedOrderDetails.getLinkedPe());
        orderInitiateResultBean.setSiblingOrder(nwaLinkedOrderDetails.getSiblingOrder());
        
        orderInitiateResultBean.setBhType(nwaBtsDetails.getBhType());
        orderInitiateResultBean.setDeviceIp(nwaBtsDetails.getDeviceIp());
        orderInitiateResultBean.setBtsVlan(nwaBtsDetails.getBtsVlan());
        orderInitiateResultBean.setCircle(nwaBtsDetails.getCircle());
        orderInitiateResultBean.setEohsCategory(nwaBtsDetails.getEohsCategory());
        orderInitiateResultBean.setInfraAggrementExpiryDate(nwaBtsDetails.getInfraAggrementExpiryDate());
        orderInitiateResultBean.setInfraProvider(nwaBtsDetails.getInfraProvider());
        orderInitiateResultBean.setInfraProviderSiteUpTime(nwaBtsDetails.getInfraProviderSiteUpTime());
        orderInitiateResultBean.setPerAntennaDimension(nwaBtsDetails.getPerAntennaDimension());
        orderInitiateResultBean.setPerAntennaDimension(nwaBtsDetails.getPerAntennaDimension());
        orderInitiateResultBean.setPerOduWeight(nwaBtsDetails.getPerOduWeight());
        orderInitiateResultBean.setPlannedDeinstallationDate(nwaBtsDetails.getPlannedDeinstallationDate());
        orderInitiateResultBean.setPlannedInstallationDate(nwaBtsDetails.getPlannedInstallationDate());
        orderInitiateResultBean.setReasonForExist(nwaBtsDetails.getReasonForExist());
        orderInitiateResultBean.setSiteId(nwaBtsDetails.getSiteId());
        orderInitiateResultBean.setSiteName(nwaBtsDetails.getSiteName());
        orderInitiateResultBean.setSiteType(nwaBtsDetails.getSiteType());
        orderInitiateResultBean.setBtsSubnetMask(nwaBtsDetails.getBtsSubnetMask());
        orderInitiateResultBean.setTclAggrementStartDate(nwaBtsDetails.getTclAggrementStartDate());
        orderInitiateResultBean.setWirelessTechSubType(nwaBtsDetails.getWirelessTechSubType());

        /*orderInitiateResultBean.setNwaRemarksByUser(nwaRemarksDetails.getByUser());
        orderInitiateResultBean.setNwaRemarksByUserGroup(nwaRemarksDetails.getByUserGroup());
        orderInitiateResultBean.setNwaRemarksReason(nwaRemarksDetails.getReason());
        orderInitiateResultBean.setComments(nwaRemarksDetails.getReason());
        orderInitiateResultBean.setNwaRemarksJeopardyReason(nwaRemarksDetails.setJeopardyReason());
        orderInitiateResultBean.setNwaRemarksReason(nwaRemarksDetails.setReason());
        orderInitiateResultBean.setNwaRemarksRemarksDate(nwaRemarksDetails.setRemarksDate());*/


        return orderInitiateResultBean;
    }

    public String getOrderDataCopy(String orderCode){

        String orderCopyFunction = scOrderRepository.getOrderDataCopy(orderCode);

        return orderCopyFunction;
    }


}
