package com.tcl.dias.servicefulfillmentutils.service.v1;


import java.sql.Timestamp;
import java.util.*;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.serviceinventory.bean.RfDumpWirelessOneBean;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.ScServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.tcl.dias.common.beans.ServiceDetailBean;
import com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean;
//import com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.SuipListBean;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
* THis service class is used to populate OnnetRf details.
*
* @author Vishesh Awasthi
*/
@Service
public class RfService extends ServiceFulfillmentBaseService {

    private static final Logger log = LoggerFactory.getLogger(RfService.class);

    @Autowired
    MQUtils mqUtils;
    @Autowired
    ScComponentAttributesRepository scComponentAttributesRepository;
    @Autowired
    ScServiceAttributeRepository scServiceAttributeRepository;
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;
    @Autowired
    ComponentAndAttributeService componentAndAttributeService;
    @Autowired
    ScOrderRepository scOrderRepository;
    @Autowired
    TaskRepository taskRepository;
    @Value("${activation.service.detail}")
    private String serviceDetailQueue;
    @Value("${activation.service.details}")
    private String serviceDetailsQueue;
    @Value("${queue.rfinventory}")
    String rfInventoryHandoverQueue;

    @Autowired
   CommonFulfillmentUtils commonFulfillmentUtils;
    
    @Autowired
    FlowableBaseService flowableBaseService;
    
    
    @Transactional(readOnly=false)
    public OptimusRfDataBean enrichRfDetails(String serviceCode) {
        ScServiceDetail scServiceDetail;
        scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
        if(Objects.isNull(scServiceDetail)) scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"ACTIVE");
        OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
        Map<String, String> attributeMap=commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");

        ServiceDetailBean serviceDetailBean = new ServiceDetailBean();
        try {
            String currentServiceDetails = (String) mqUtils.sendAndReceive(serviceDetailQueue, scServiceDetail.getUuid());
            log.info("Received current service details as : []",currentServiceDetails);
            if (Objects.nonNull(currentServiceDetails))
                serviceDetailBean = Utils.convertJsonToObject(currentServiceDetails, ServiceDetailBean.class);
        } catch (TclCommonException e) {
            log.error("Exception occurred while getting current service details : {}", e);
        }
        setOptimusRfData(scServiceDetail, attributeMap, optimusRfDataBean, serviceDetailBean);

        return optimusRfDataBean;
    }
    
    @Transactional(readOnly=false)
    public OptimusRfDataBean enrichRfDetails(ScServiceDetail scServiceDetail, String taskStage) {


        OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();

            Map<String, String> attributeMap=commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
          
            ServiceDetailBean serviceDetailBean = new ServiceDetailBean();
            try {
                String currentServiceDetails = (String) mqUtils.sendAndReceive(serviceDetailQueue, scServiceDetail.getUuid());
                log.info("Received current service details as : []",currentServiceDetails);
                if (Objects.nonNull(currentServiceDetails))
                    serviceDetailBean = Utils.convertJsonToObject(currentServiceDetails, ServiceDetailBean.class);
            } catch (TclCommonException e) {
                log.error("Exception occurred while getting current service details : {}", e);
            }
            setOptimusRfTaskStageData(scServiceDetail, taskStage, optimusRfDataBean);
            setOptimusRfData(scServiceDetail, attributeMap, optimusRfDataBean, serviceDetailBean);

        return optimusRfDataBean;
    }

    private void setOptimusRfTaskStageData( ScServiceDetail scServiceDetail, String taskStage, OptimusRfDataBean optimusRfDataBean) {
        optimusRfDataBean.setLastUpdatedBy("OPTIMUS");
        optimusRfDataBean.setLastUpdatedDate((new Timestamp(new Date().getTime())).toString());
        optimusRfDataBean.setTaskStage(taskStage);
        if ("E2E".equals(taskStage))
            optimusRfDataBean.setStatus("ACTIVE");
        else
            optimusRfDataBean.setStatus(Objects.nonNull(scServiceDetail.getServiceAceptanceStatus())? scServiceDetail.getServiceAceptanceStatus() : "PENDING");
        optimusRfDataBean.setServiceStatus(Objects.nonNull(scServiceDetail.getMstStatus())? scServiceDetail.getMstStatus().getCode().replace("INPROGRESS","IN-PROGRESS") : "IN-PROGRESS");
        if("TERMINATE".equals(optimusRfDataBean.getServiceStatus()))
            optimusRfDataBean.setStatus("TERMINATE");
    }

    private void setOptimusRfData(ScServiceDetail scServiceDetail, Map<String, String> attributeMap, OptimusRfDataBean optimusRfDataBean, ServiceDetailBean serviceDetailBean) {
        optimusRfDataBean.setServiceType(getServiceType(scServiceDetail));
        optimusRfDataBean.setTypeOfOrder(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder()));
        optimusRfDataBean.setActionType(scServiceDetail.getOrderSubCategory());
//        optimusRfDataBean.setLmAction("NEW");
        optimusRfDataBean.setLmAction(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder()));
        optimusRfDataBean.setLmType(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder()));
//        optimusRfDataBean.setDeviceType(attributeMap.getOrDefault("deviceType", null));
        optimusRfDataBean.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
        String state = attributeMap.get("destinationState");
        if (org.springframework.util.StringUtils.isEmpty(state))
            state=scServiceDetail.getDestinationState();
        if (!org.springframework.util.StringUtils.isEmpty(state))
            optimusRfDataBean.setState(state);
        String city = attributeMap.get("destinationCity");
        if (org.springframework.util.StringUtils.isEmpty(city))
            city=scServiceDetail.getDestinationCity();
        if (!org.springframework.util.StringUtils.isEmpty(city))
            optimusRfDataBean.setCity(city);


        optimusRfDataBean.setCircuitId(scServiceDetail.getUuid());
        optimusRfDataBean.setCommissionDate(Objects.nonNull(scServiceDetail.getCommissionedDate())? scServiceDetail.getCommissionedDate().toString() : attributeMap.get("commissioningDate"));
        optimusRfDataBean.setDateOfAcceptance(Objects.nonNull(scServiceDetail.getServiceAceptanceDate())? scServiceDetail.getServiceAceptanceDate().toString() : null);
//        optimusRfDataBean.setTerminationDate(Objects.nonNull(scServiceDetail.getServiceTerminationDate()) ? scServiceDetail.getServiceTerminationDate().toString(): null);
        String portSpeed=  (org.springframework.util.StringUtils.isEmpty(scServiceDetail.getBwPortspeed())) ? scServiceDetail.getBwPortspeedAltName() : scServiceDetail.getBwPortspeed();
        optimusRfDataBean.setQosBw(setLastMileBwInKbps(portSpeed,scServiceDetail.getBwUnit()));
        setLatLong(optimusRfDataBean, scServiceDetail,attributeMap);
        optimusRfDataBean.setBuildingHeight(attributeMap.getOrDefault("buildingHeight", null));
        optimusRfDataBean.setStructureType(attributeMap.getOrDefault("structureType", null));
        //Based on structure type, tower height and pole height will be set
        setTowerOrPoleHeight(optimusRfDataBean, attributeMap);
        optimusRfDataBean.setAntennaHeight(attributeMap.getOrDefault("antennaHeight", (attributeMap.getOrDefault("hsuHeightFromGround",null))));
        optimusRfDataBean.setAntennaBeamWidth(attributeMap.getOrDefault("antennaBw", null));
        optimusRfDataBean.setPolarisation(attributeMap.getOrDefault("polarization", "").split(" ")[0]);
        optimusRfDataBean.setEthernetExtender(attributeMap.getOrDefault("ethernetExtenderUsed", "No"));
        optimusRfDataBean.setCableLength(attributeMap.getOrDefault("cableLength", null));
        optimusRfDataBean.setAntennaType(attributeMap.getOrDefault("typeOfAntenna", null));
        optimusRfDataBean.setCustomerAddress(attributeMap.get("siteAddress"));

        ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
                .findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(scServiceDetail.getId(), "solution_type");
        if(Objects.nonNull(scServiceAttribute))optimusRfDataBean.setProvider(scServiceAttribute.getAttributeValue());
        optimusRfDataBean.setSsIp(attributeMap.getOrDefault("suIp", (attributeMap.getOrDefault("hsuIp",null))));
        String mac=attributeMap.getOrDefault("suMacAddress", "").replace(':',' ');
        optimusRfDataBean.setMac(mac);
        optimusRfDataBean.setApIp(attributeMap.getOrDefault("btsIp", ""));
        optimusRfDataBean.setBsName(attributeMap.getOrDefault("btsName",null));
        optimusRfDataBean.setSectorId(attributeMap.getOrDefault("sectorId","1"));
        optimusRfDataBean.setVendor(attributeMap.getOrDefault("rfMakeModel", null));
    }

    private String getServiceType(ScServiceDetail scServiceDetail) {
        if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IAS"))
            return "ILL";

        else
            return scServiceDetail.getErfPrdCatalogProductName();
    }

    private void setLatLong(OptimusRfDataBean optimusRfDataBean, ScServiceDetail scServiceDetail, Map<String, String> attributeMap) {
        String latLong = attributeMap.get("latLong");
        if (!StringUtils.isEmpty(latLong)) {
            String[] latlong = latLong.split(",");
            optimusRfDataBean.setLatitude(latlong[0]);
            optimusRfDataBean.setLongitude(latlong[1]);
        }
    }

    private void setTowerOrPoleHeight(OptimusRfDataBean optimusRfDataBean, Map<String, String> attributeMap) {
        String structureType = optimusRfDataBean.getStructureType();
        if (!StringUtils.isEmpty(structureType)) {
            if ("mast".equalsIgnoreCase(structureType) || "tower".equalsIgnoreCase(structureType))
                optimusRfDataBean.setTowerHeight(attributeMap.getOrDefault("towerHeight", null));
            else
                optimusRfDataBean.setPoleHeight(attributeMap.getOrDefault("poleHeight", null));
        }
    }

    private String setLastMileBwInKbps(String lastmileBw,String lastmileBwUnit) {
        if ("Mbps".equalsIgnoreCase(lastmileBwUnit))
            lastmileBw = String.valueOf(Float.valueOf(lastmileBw) * 1024);
        else if ("Gbps".equalsIgnoreCase(lastmileBwUnit))
            lastmileBw = String.valueOf(Float.valueOf(lastmileBw) * 1024 * 1024);
        return lastmileBw;
    }

    @Transactional(readOnly=false)
    public List<RfDumpWirelessOneBean> enrichRfDetailList(SuipListBean suipListBean, String provider)
   {
      List <RfDumpWirelessOneBean>optimusRfDataListBean = new ArrayList<>();
        if(!StringUtils.isEmpty(provider))
        {
      if (provider.contains("p2p") || provider.equalsIgnoreCase("Radwin from TCL POP")) {
            List<Integer> taskIds = taskRepository.findByMstTaskDef_key("rf-config-p2p").stream().map(Task::getId).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(taskIds))
            {
                taskIds.forEach(taskId -> {

                        try {
                            TaskData p2pRfData = taskDataRepository.findFirstByTask_idOrderByCreatedTimeDesc(taskId);
                            if (p2pRfData!=null) {
                                OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
                                RfDumpWirelessOneBean rfDumpWirelessOneBean = new RfDumpWirelessOneBean();
                                ScServiceDetail scServiceDetail = new ScServiceDetail();
                                if (taskRepository.findById(taskId).isPresent())
                                    scServiceDetail = taskRepository.findById(taskId).get().getScServiceDetail();
                                Map<String, String> attributeMap = commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
                                optimusRfDataBean = Utils.convertJsonToObject(p2pRfData.getData(), OptimusRfDataBean.class);
                                optimusRfDataBean.setProvider("Radwin from TCL POP");
                                optimusRfDataBean.setCircuitId(scServiceDetail.getUuid());
                                optimusRfDataBean.setServiceType(getServiceType(scServiceDetail));
								String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder());

                                optimusRfDataBean.setTypeOfOrder(orderCategory);
                                optimusRfDataBean.setActionType(scServiceDetail.getOrderSubCategory());
                                optimusRfDataBean.setLmAction(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder()));
                                optimusRfDataBean.setLmType(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder()));
                                optimusRfDataBean.setCommissionDate(Objects.nonNull(scServiceDetail.getCommissionedDate())? scServiceDetail.getCommissionedDate().toString() : attributeMap.get("commissioningDate"));
                                optimusRfDataBean.setDateOfAcceptance(Objects.nonNull(scServiceDetail.getServiceAceptanceDate())? scServiceDetail.getServiceAceptanceDate().toString() : null);
                                optimusRfDataBean.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
                                String state = attributeMap.get("destinationState");
                                if (org.springframework.util.StringUtils.isEmpty(state))
                                    state=scServiceDetail.getDestinationState();
                                if (!org.springframework.util.StringUtils.isEmpty(state))
                                    optimusRfDataBean.setState(state);
                                String city = attributeMap.get("destinationCity");
                                if (org.springframework.util.StringUtils.isEmpty(city))
                                    city=scServiceDetail.getDestinationCity();
                                if (!org.springframework.util.StringUtils.isEmpty(city))
                                    optimusRfDataBean.setCity(city);
                                optimusRfDataBean.setCustomerAddress(attributeMap.get("siteAddress"));
                                optimusRfDataBean.setVendor("Radwin");
                                setOptimusRfTaskStageData(scServiceDetail, null , optimusRfDataBean);
                                String portSpeed=  (org.springframework.util.StringUtils.isEmpty(scServiceDetail.getBwPortspeed())) ? scServiceDetail.getBwPortspeedAltName() : scServiceDetail.getBwPortspeed();
                                optimusRfDataBean.setQosBw(setLastMileBwInKbps(portSpeed,scServiceDetail.getBwUnit()));
                                BeanUtils.copyProperties(optimusRfDataBean, rfDumpWirelessOneBean);
                                optimusRfDataListBean.add(rfDumpWirelessOneBean);
                            }
                        } catch (TclCommonException e) {
                            log.error("Exception occurred while getting P2P details : {}", e);
                        }
                });

            }
        }
        }
        else {
            ServiceDetailBean[] serviceDetailBean = null;
            List<ServiceDetailBean> serviceDetailBeans = new ArrayList<ServiceDetailBean>();
            List<ScServiceDetail> scServiceDetails = new ArrayList<>();
            Map<String, List<ServiceDetailBean>> serviceDetailBeanMap = new HashMap<>();
            List<String> attributeNames = new ArrayList<>();
            attributeNames.add("suIp");
            attributeNames.add("hsuIp");
            List<Integer> serviceDetailId = scComponentAttributesRepository.findAllByAttributeValueInAndAttributeNameInAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(suipListBean.getSuipList(), attributeNames, "LM", "A")
                    .stream()
                    .map(ScComponentAttribute::getScServiceDetailId)
                    .collect(Collectors.toList());


            List<ScServiceDetail> scServiceDetailList = new ArrayList<>(scServiceDetailRepository.findAllByIdIn(serviceDetailId));


            List<String> scServiceDetailUuids = scServiceDetailList
                    .stream()
                    .map(ScServiceDetail::getUuid)
                    .collect(Collectors.toList());


            //Get current version of service details from activation

            try {
                String currentServiceDetails = (String) mqUtils.sendAndReceive(serviceDetailsQueue, Utils.convertObjectToJson(scServiceDetailUuids));
                log.info("Received current service details as : {}", currentServiceDetails);
                if (Objects.nonNull(currentServiceDetails))
                    serviceDetailBean = Utils.convertJsonToObject(currentServiceDetails, ServiceDetailBean[].class);
            } catch (TclCommonException e) {
                log.error("Exception occurred while getting current service details : {}", e);
            }

            if (Objects.nonNull(serviceDetailBean)) {
                serviceDetailBeanMap = Stream.of(serviceDetailBean)
                        .filter(serviceDetailBean1 -> Objects.nonNull(serviceDetailBean1.getServiceId()))
                        .collect(Collectors.groupingBy(ServiceDetailBean::getServiceId));
            }

            for (ScServiceDetail s : scServiceDetailList) {
                OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
                RfDumpWirelessOneBean rfDumpWirelessOneBean = new RfDumpWirelessOneBean();
                Map<String, String> attributeMap=commonFulfillmentUtils.getComponentAttributes(s.getId(), "LM", "A");
              
                ServiceDetailBean detailBean = null;
                if (serviceDetailBeanMap.containsKey(s.getUuid()) && !org.springframework.util.CollectionUtils.isEmpty(serviceDetailBeanMap.get(s.getUuid())))
                    detailBean = serviceDetailBeanMap.get(s.getUuid())
                            .stream()
                            .findFirst()
                            .get();
                else
                    detailBean = new ServiceDetailBean();

                setOptimusRfData(s, attributeMap, optimusRfDataBean, detailBean);
                BeanUtils.copyProperties(optimusRfDataBean,rfDumpWirelessOneBean);
                optimusRfDataListBean.add(rfDumpWirelessOneBean);
            }


            suipListBean.getSuipList().forEach(suIp -> {
                OptimusRfDataBean optimusRfDataBean = new OptimusRfDataBean();
                RfDumpWirelessOneBean rfDumpWirelessOneBean = new RfDumpWirelessOneBean();
                Integer sDetailId = scComponentAttributesRepository.findFirstByAttributeValueAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(suIp, "suIp", "LM", "A")
                        .map(ScComponentAttribute::getScServiceDetailId)
                        .orElse(scComponentAttributesRepository.findFirstByAttributeValueAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(suIp, "hsuIp", "LM", "A")
                                .map(ScComponentAttribute::getScServiceDetailId).orElse(null));
                if (Objects.isNull(sDetailId)) {
                    rfDumpWirelessOneBean.setErrorMessage("No data found for the given SuIp : " + suIp);
                    optimusRfDataListBean.add(rfDumpWirelessOneBean);
                }
            });

        }

        return optimusRfDataListBean;
    }

    @Transactional(readOnly = false)
    public com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean enrichRfP2PData(com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean optimusRfDataBean)
            throws TclCommonException {

        Task task = getTaskByIdAndWfTaskId(optimusRfDataBean.getTaskId(),optimusRfDataBean.getWfTaskId());
        Map<String, String> atMap = new HashMap<>();
    	String action = "CLOSED";
    	Map<String, Object> wfMap = new HashMap<>();
		if (optimusRfDataBean.getAction() != null && optimusRfDataBean.getAction().equalsIgnoreCase("save")) {
			action = "SAVED";
		}

        populateMap(atMap, optimusRfDataBean);
        componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
        validateInputs(task, optimusRfDataBean);
        saveRfP2PData(optimusRfDataBean);
        processTaskLogDetails(task,action,optimusRfDataBean.getDelayReason(),null, optimusRfDataBean);
       
		if (action.equalsIgnoreCase("CLOSED")) {
			wfMap.put("action", action);
			return (com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean) flowableBaseService.taskDataEntry(task,
					optimusRfDataBean,wfMap);

		} else {

			return (com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean) flowableBaseService.taskDataEntrySave(task,
					optimusRfDataBean);

		}

    }

    

    private void populateMap(Map<String, String> atMap, com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean optimusRfDataBean)
    {
        atMap.put("bsAddress", optimusRfDataBean.getBsAddress());
        atMap.put("bsMimoDiversity", optimusRfDataBean.getBsMimoDiversity());
        atMap.put("ssTowerHeight", optimusRfDataBean.getSsTowerHeight());
        atMap.put("provider", optimusRfDataBean.getProvider());
        atMap.put("aggregationSwitch", optimusRfDataBean.getAggregationSwitch());
        atMap.put("bhCapacity", optimusRfDataBean.getBhCapacity());
        atMap.put("ssLatitude", optimusRfDataBean.getSsLatitude());
        atMap.put("converterIp", optimusRfDataBean.getConverterIp());
        atMap.put("dateOfAcceptance", optimusRfDataBean.getDateOfAcceptance());
        atMap.put("ssDateAcceptance", optimusRfDataBean.getSsDateAcceptance());
        atMap.put("bsEthernetExtender", optimusRfDataBean.getBsEthernetExtender());
        atMap.put("throughputAcceptance", optimusRfDataBean.getThroughputAcceptance());
        atMap.put("ssMac", optimusRfDataBean.getSsMac());
        atMap.put("bsIp", optimusRfDataBean.getBsIp());
        atMap.put("bsAntennaGain", optimusRfDataBean.getBsAntennaGain());
        atMap.put("bsPoleHeight", optimusRfDataBean.getBsPoleHeight());
        atMap.put("bsBuildingHeight", optimusRfDataBean.getBsBuildingHeight());
        atMap.put("ssAntennaHeight", optimusRfDataBean.getSsAntennaHeight());
        atMap.put("bsAntennaType", optimusRfDataBean.getBsAntennaType());
        atMap.put("custBuildingHeight", optimusRfDataBean.getCustBuildingHeight());
        atMap.put("ssLongitude", optimusRfDataBean.getSsLongitude());
        atMap.put("ethconverterIp", optimusRfDataBean.getEthconverterIp());
        atMap.put("bsLongitude", optimusRfDataBean.getBsLongitude());
        atMap.put("backhaulType", optimusRfDataBean.getBackhaulType());
        atMap.put("hssuUsed", optimusRfDataBean.getHssuUsed());
        atMap.put("btsName", optimusRfDataBean.getBsName());
        atMap.put("ssAntennaGain", optimusRfDataBean.getSsAntennaGain());
        atMap.put("btsSiteId", optimusRfDataBean.getBtsSiteId());
        atMap.put("bsLatitude", optimusRfDataBean.getBsLatitude());
        atMap.put("ssAntennaMountType", optimusRfDataBean.getSsAntennaMountType() );
        atMap.put("hssuPort", optimusRfDataBean.getHssuPort());
        atMap.put("ssAntennaType", optimusRfDataBean.getSsAntennaType());
        atMap.put("bsPolarisation", optimusRfDataBean.getBsPolarisation());
        atMap.put("bsTowerHeight", optimusRfDataBean.getBsTowerHeight());
        atMap.put("bsCableLength", optimusRfDataBean.getBsCableLength());
        atMap.put("ssEthernetExtender", optimusRfDataBean.getSsEthernetExtender());
        atMap.put("ssBsName", optimusRfDataBean.getSsBsName());
        atMap.put("structureType", optimusRfDataBean.getStructureType());
        atMap.put("bsAntennaMountType", optimusRfDataBean.getBsAntennaMountType());
        atMap.put("ssCableLength", optimusRfDataBean.getSsCableLength());
        atMap.put("bsMac", optimusRfDataBean.getBsMac());
        atMap.put("ssMimoDiversity", optimusRfDataBean.getSsMimoDiversity());
        atMap.put("converterType", optimusRfDataBean.getConverterType());
        atMap.put("bsAntennaHeight", optimusRfDataBean.getBsAntennaHeight());
        atMap.put("suIp", optimusRfDataBean.getSsIp());
        atMap.put("popConverterIp", optimusRfDataBean.getPopConverterIp());
        atMap.put("errorMessage", optimusRfDataBean.getErrorMessage());
        atMap.put("infraColoProviderId", optimusRfDataBean.getInfraColoProviderId());
        atMap.put("infraColoProviderName", optimusRfDataBean.getInfraColoProviderName());

        atMap.put("smsNoBtsEnd", optimusRfDataBean.getSmsNoBtsEnd());
        atMap.put("poeSiNoBtsEnd", optimusRfDataBean.getPoeSiNoBtsEnd());
        atMap.put("smsNoCustomerEnd", optimusRfDataBean.getSmsNoCustomerEnd());
        atMap.put("poeSiNoCustomerEnd", optimusRfDataBean.getPoeSiNoCustomerEnd());
        atMap.put("phaseNetural", optimusRfDataBean.getPhaseNetural());
        atMap.put("phaseEarth", optimusRfDataBean.getPhaseEarth());
        atMap.put("earthNeutral", optimusRfDataBean.getEarthNeutral());
        atMap.put("radwinServerReachRfBtsEnd", optimusRfDataBean.getRadwinServerReachRfBtsEnd());
        atMap.put("radwinServerReachRfCustomerEnd", optimusRfDataBean.getRadwinServerReachRfCustomerEnd());
        atMap.put("mastHeight", optimusRfDataBean.getMastHeight());
        atMap.put("poleHeight", optimusRfDataBean.getPoleHeight());
        atMap.put("hsuHeightFromGround", optimusRfDataBean.getHsuHeightFromGround());
        atMap.put("averageMeanSeaLevel", optimusRfDataBean.getAverageMeanSeaLevel());
        atMap.put("hopDistance", optimusRfDataBean.getHopDistance());
        atMap.put("lClamPoleUsedCustomerEnd", optimusRfDataBean.getlClamPoleUsedCustomerEnd());
        atMap.put("rfCableLengthCustomerEnd", optimusRfDataBean.getRfCableLengthCustomerEnd());
        atMap.put("earthingCableLengthCustomerEnd", optimusRfDataBean.getEarthingCableLengthCustomerEnd());
        atMap.put("noOfInstallationkitsCustomerEnd", optimusRfDataBean.getNoOfInstallationkitsCustomerEnd());
        atMap.put("typeOfPole", optimusRfDataBean.getTypeOfPole());
        atMap.put("noOfWeatherProofInstallationKits", optimusRfDataBean.getNoOfWeatherProofInstallationKits());
        atMap.put("noOfPatchCordUsed", optimusRfDataBean.getNoOfPatchCordUsed());
        atMap.put("suPvcConduit", optimusRfDataBean.getSuPvcConduit());
        atMap.put("SuSerialNumber", optimusRfDataBean.getSuSerialNumber());
        atMap.put("cableType", optimusRfDataBean.getCableType());
        atMap.put("patchCordCustomerEnd", optimusRfDataBean.getPatchCordCustomerEnd());
        atMap.put("rfConnectorTypeCustomerEnd", optimusRfDataBean.getRfConnectorTypeCustomerEnd());
        atMap.put("antennaBeamWidth", optimusRfDataBean.getAntennaBeamWidth());
        atMap.put("lClampPoleUsedBtsEnd", optimusRfDataBean.getlClampPoleUsedBtsEnd());
        atMap.put("rfCableLengthBtsEnd", optimusRfDataBean.getRfCableLengthBtsEnd());
        atMap.put("earthingCableLengthBtsEnd", optimusRfDataBean.getEarthingCableLengthBtsEnd());
        atMap.put("powerCableLengthBtsEnd", optimusRfDataBean.getPowerCableLengthBtsEnd());
        atMap.put("noOfMcb", optimusRfDataBean.getNoOfMcb());
        atMap.put("noOfInstallationkitsBtsEnd", optimusRfDataBean.getNoOfInstallationkitsBtsEnd());
        atMap.put("btspvcConduit", optimusRfDataBean.getBtspvcConduit());
        atMap.put("btsSuSerialNumber", optimusRfDataBean.getBtsSuSerialNumber());
        atMap.put("patchCordBtsEnd", optimusRfDataBean.getPatchCordBtsEnd());
        atMap.put("rfConnectorTypeBtsEnd", optimusRfDataBean.getRfConnectorTypeBtsEnd());

    }

    @Transactional(readOnly = false)
    public void saveRfP2PData( com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean optimusRfDataBean)
            throws TclCommonException {

        Task task = getTaskById(optimusRfDataBean.getTaskId());
        Map<String, String> atMap = new HashMap<>();
        populateMap(atMap, optimusRfDataBean);

        componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
        if (optimusRfDataBean.getDocumentIds() != null && !optimusRfDataBean.getDocumentIds().isEmpty())
            optimusRfDataBean.getDocumentIds()
                    .forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
        try{
            populateP2PtoRfInventory(optimusRfDataBean);
            }
        catch (TclCommonException e)
        {
            log.error("Exception occurred while populateP2PtoRfInventory : {}", e);
             }
    }


    @Transactional(readOnly = false)
    public void populateP2PtoRfInventory(com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean optimusRfDataBean) throws TclCommonException {
        ScServiceDetailBean scServiceDetailBean= new ScServiceDetailBean();
        ScServiceDetail scServiceDetail = new ScServiceDetail();
        Task task = getTaskByIdAndWfTaskId(optimusRfDataBean.getTaskId(),optimusRfDataBean.getWfTaskId());
        Map<String, String> attributeMap=commonFulfillmentUtils.getComponentAttributes(task.getServiceId(), "LM", "A");
        scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(task.getServiceCode(),"INPROGRESS");
        if(Objects.isNull(scServiceDetail)) scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(task.getServiceCode(),"ACTIVE");
        com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean optimusRfDataBean1 = Utils.convertJsonToObject(Utils.convertObjectToJson(optimusRfDataBean), com.tcl.dias.common.serviceinventory.bean.OptimusRfDataBean.class);

        optimusRfDataBean1.setProvider("Radwin from TCL POP");
        optimusRfDataBean1.setCircuitId(scServiceDetail.getUuid());
        optimusRfDataBean1.setServiceType(getServiceType(scServiceDetail));
        optimusRfDataBean1.setTypeOfOrder(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder()));
        optimusRfDataBean1.setActionType(scServiceDetail.getOrderSubCategory());
        optimusRfDataBean1.setLmAction(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder()));
        optimusRfDataBean1.setLmType(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder()));

        optimusRfDataBean1.setCommissionDate(Objects.nonNull(scServiceDetail.getCommissionedDate())? scServiceDetail.getCommissionedDate().toString() : attributeMap.get("commissioningDate"));
        optimusRfDataBean1.setDateOfAcceptance(Objects.nonNull(scServiceDetail.getServiceAceptanceDate())? scServiceDetail.getServiceAceptanceDate().toString() : null);
        optimusRfDataBean1.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
        optimusRfDataBean1.setVendor("Radwin");
        String state = attributeMap.get("destinationState");
        if (org.springframework.util.StringUtils.isEmpty(state))
            state=scServiceDetail.getDestinationState();
        if (!org.springframework.util.StringUtils.isEmpty(state))
            optimusRfDataBean1.setState(state);
        String city = attributeMap.get("destinationCity");
        if (org.springframework.util.StringUtils.isEmpty(city))
            city=scServiceDetail.getDestinationCity();
        if (!org.springframework.util.StringUtils.isEmpty(city))
            optimusRfDataBean1.setCity(city);
        optimusRfDataBean1.setCustomerAddress(attributeMap.get("siteAddress"));
        String portSpeed=  (org.springframework.util.StringUtils.isEmpty(scServiceDetail.getBwPortspeed())) ? scServiceDetail.getBwPortspeedAltName() : scServiceDetail.getBwPortspeed();
        optimusRfDataBean1.setQosBw(setLastMileBwInKbps(portSpeed,scServiceDetail.getBwUnit()));
        setOptimusRfTaskStageData(scServiceDetail,"CMIP", optimusRfDataBean1);
        scServiceDetailBean.setOptimusRfDataBean(optimusRfDataBean1);
        scServiceDetailBean.setUuid(scServiceDetail.getUuid());
        mqUtils.send(rfInventoryHandoverQueue, Utils.convertObjectToJson(scServiceDetailBean));
    }

    @Transactional(readOnly = false)
    public void pushP2ptoRfInventory(Integer serviceId){
        taskRepository.findByServiceIdAndMstTaskDef_key(serviceId, "rf-config-p2p").
                stream(). findFirst().
                ifPresent(t ->
                {
                    TaskData p2pRfData = taskDataRepository.findFirstByTask_idOrderByCreatedTimeDesc(t.getId());
                    com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean optimusRfDataBean;
                    try {
                        optimusRfDataBean = Utils.convertJsonToObject(p2pRfData.getData(), com.tcl.dias.servicefulfillmentutils.OptimusRfDataBean.class);
                        populateP2PtoRfInventory(optimusRfDataBean);
                    }
                    catch (TclCommonException e)
                    {
                        e.printStackTrace();
                    }
                });

    }

    public void populateP2PandPmpTerminationData(ScServiceDetail scServiceDetail) {
        log.info("populateP2PandPmpTerminationData  invoked for {} :",scServiceDetail.getUuid());
        try {
            com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean scServiceDetailBean = new com.tcl.dias.common.serviceinventory.bean.ScServiceDetailBean();
            ScComponentAttribute lastMileProvider = scComponentAttributesRepository
                    .findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetail.getId(), "lastMileProvider", "LM", "A");
            if(lastMileProvider!=null && (org.apache.commons.lang3.StringUtils.containsIgnoreCase("p2p",lastMileProvider.getAttributeValue()) || org.apache.commons.lang3.StringUtils.containsIgnoreCase("pmp",lastMileProvider.getAttributeValue()))) {
                log.info("Last Mile provider name {} :", lastMileProvider.getAttributeValue());
                OptimusRfDataBean optimusRfDataBean = enrichRfDetails(scServiceDetail, "TERMINATE");
                if (optimusRfDataBean != null) {
                    log.info("RF data provider {} :", optimusRfDataBean.getProvider());
                    scServiceDetailBean.setOptimusRfDataBean(optimusRfDataBean);
                    scServiceDetailBean.setUuid(scServiceDetail.getUuid());
                    String rfInventoryData=Utils.convertObjectToJson(scServiceDetailBean);
                    log.info("RF inventory data {} :",rfInventoryData);
                    mqUtils.send(rfInventoryHandoverQueue, rfInventoryData);
                }
            }
        } catch (Exception e) {
            log.error("populateP2PandPmpTerminationData Exception :{} ", e);
        }
    }
}

