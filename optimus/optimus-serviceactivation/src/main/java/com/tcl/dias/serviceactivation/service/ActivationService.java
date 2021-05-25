package com.tcl.dias.serviceactivation.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;


import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.serviceactivation.beans.CgwLogsUploadBean;
import com.tcl.dias.serviceactivation.beans.CustomerAcceptanceIssueBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.serviceactivation.beans.SdwanProvisionDetailsBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.activation.utils.KafkaProducer;
import com.tcl.dias.serviceactivation.beans.CustomerDownTimeBean;
import com.tcl.dias.serviceactivation.beans.MuxJeopardyBean;
import com.tcl.dias.serviceactivation.beans.TxJeopardyBean;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.NodeToBeConfigured;
import com.tcl.dias.serviceactivation.entity.entities.MstBsIPSsIPMapping;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.entities.NodeToConfigure;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.RfTestResult;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.repository.MstBsIPSsIPMappingRepository;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.serviceactivation.entity.repository.NodeToConfigureRepository;
import com.tcl.dias.serviceactivation.entity.repository.RfTestResultRepository;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.entity.repository.TxConfigurationRepository;
import com.tcl.dias.serviceactivation.integratemux.beans.NodeInfoRequestBean;
import com.tcl.dias.servicefulfillment.entity.entities.DownTimeDetails;
import com.tcl.dias.servicefulfillment.entity.entities.PlannedEvent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.DownTimeDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.PlannedEventRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.CreatePlannedEventBean;
import com.tcl.dias.servicefulfillmentutils.beans.CustomerCloudConfigurationBean;
import com.tcl.dias.servicefulfillmentutils.beans.NodeConfigurations;
import com.tcl.dias.servicefulfillmentutils.beans.NodeToConfigureBean;
import com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.PlannedEventBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceConfigurationBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.beans.TxConfigurationJeopardyBean;
import com.tcl.dias.servicefulfillmentutils.beans.UnderlayBean;
import com.tcl.dias.servicefulfillmentutils.beans.servicenow.CreateMuxPlannedEventBean;
import com.tcl.dias.servicefulfillmentutils.beans.servicenow.CreateMuxPlannedEventRequest;
import com.tcl.dias.servicefulfillmentutils.beans.servicenow.PlannedEventResponseBean;
import com.tcl.dias.servicefulfillmentutils.beans.servicenow.VariableList;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.GetPingStabilityResponse;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SSDumpResponseBean;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SetPingStabilityResponseBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import om.tcl.dias.serviceactivation.beans.izopc.WanIpDetailsBean;

@Service
@Transactional(readOnly = true)
public class ActivationService extends ServiceFulfillmentBaseService {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SET_PING_STABILITY = "set_ping_stability";
    public static final String SS_DUMP = "ss_dump";
    public static final String GET_PING_STABILITY = "get_ping_stability";
    private static final Logger logger = LoggerFactory.getLogger(ActivationService.class);
    @Value("${kafka.pushnodeinfo.topic}")
    String pushNodeInfoTopic;

    @Value("${planned.event.user}")
    String plannedEventUser;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;

    @Autowired
    PlannedEventRepository plannedEventRepository;
    
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;


    @Autowired
    NetworkInventoryRepository networkInventoryRepository;
    @Autowired
    RfTestResultRepository rfTestResultRepository;
    @Autowired
    RestClientService restClientService;
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;
    @Autowired
    TxConfigurationRepository txConfigurationRepository;
    @Autowired
    NodeToConfigureRepository nodeToConfigureRepository;
    @Autowired
    CramerService cramerService;
    @Value("${wireless.integration.base.url}")
    String wirelessBaseUrl;
    @Value("${wireless.integration.username}")
    String wireless1User;
    @Value("${wireless.integration.password}")
    String wireless1UserPwd;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Value("${planned.event.service.now.url}")
    private String plannedEventUrl;
    @Value("${app.id}")
    private String userName;
    @Value("${secret.key}")
    private String password;
    @Autowired
    private UserInfoUtils userInfoUtils;

	@Autowired
	TaskService taskService;


	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	ServiceDetailRepository serviceDetailRepository;
	
	@Autowired
	FlowableBaseService flowableBaseService;

	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	DownTimeDetailsRepository downTimeDetailsRepository;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;

	@Autowired
	ServiceActivationService serviceActivationService;
	
	@Autowired
	ScOrderRepository scOrderRepository;

    @Autowired
    ScComponentRepository scComponentRepository;

    @Autowired
    MQUtils mqUtils;

    @Value("${slave.fulfillment.trigger.queue}")
    String slaveFulfillmentTrigger;
    
    @Autowired
    MstBsIPSsIPMappingRepository mstBsIPSsIPMappingRepository;

    /**
     * This method is used to raise Planned Event.
     *
     * @param taskId
     * @param plannedEventBean
     * @return
     * @throws TclCommonException
     * @author Vishesh Awasthi
     */
    @Transactional(readOnly = false)
    public PlannedEventBean checkPlannedEvent( PlannedEventBean plannedEventBean)
            throws TclCommonException {
        Task task = getTaskByIdAndWfTaskId(plannedEventBean.getTaskId(),plannedEventBean.getWfTaskId());
        Map<String, String> muxDetailsFromScComponentAttributes = getMuxDetailsFromScComponent(task.getServiceId(),"LM",task.getSiteType());
        String muxName = muxDetailsFromScComponentAttributes.getOrDefault("endMuxNodeName", "");
        PlannedEvent plannedEvent = triggerMuxIntegrationRequest(plannedEventBean, task.getServiceCode(), muxDetailsFromScComponentAttributes, muxName, false, null);

        return new PlannedEventBean(plannedEvent);
    }

    private PlannedEvent triggerMuxIntegrationRequest(PlannedEventBean plannedEventBean, String serviceCode, Map<String, String> muxDetailsFromScComponentAttributes, String muxName, Boolean isPreFetched, String processInstanceId) {
        PlannedEvent plannedEvent = savePlannedEvent(plannedEventBean, "C-PROGRESS", "", muxName, isPreFetched, serviceCode, processInstanceId);
        try {
            NodeInfoRequestBean nodeInfoRequestBean = constructAndSendNodeInfoRequest(plannedEvent, muxDetailsFromScComponentAttributes);
            saveInNetworkInventory(nodeInfoRequestBean, serviceCode);
        } catch (TclCommonException e) {
            logger.error("Exception occurred while constructing asd sending mux info request --> {}", e);
        }
        return plannedEvent;
    }

    private NodeInfoRequestBean constructAndSendNodeInfoRequest(PlannedEvent plannedEvent, Map<String, String> muxDetailsFromScComponent) throws TclCommonException {
        NodeInfoRequestBean nodeInfoRequestBean = new NodeInfoRequestBean();
        if (!CollectionUtils.isEmpty(muxDetailsFromScComponent)) {
            muxDetailsFromScComponent.forEach((k, v) -> {
                if (k.equalsIgnoreCase("endMuxNodeName"))
                    nodeInfoRequestBean.setNodeFieldName(v);
                else if (k.equalsIgnoreCase("endMuxNodeIp"))
                    nodeInfoRequestBean.setNodeIP(v);
                else if (k.equalsIgnoreCase("eorDetails"))
                    nodeInfoRequestBean.setEOR(v);
            });
            nodeInfoRequestBean.setOptimusPEid(plannedEvent.getPlannedEventId());
            nodeInfoRequestBean.setRequestid(plannedEvent.getPlannedEventId());
            nodeInfoRequestBean.setPEstarttime(plannedEvent.getStartTime());
            nodeInfoRequestBean.setPEendtime(plannedEvent.getEndTime());
            nodeInfoRequestBean.setRequestingSystem("Optimus");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            nodeInfoRequestBean.setRequestTime(LocalDateTime.now().format(dateTimeFormatter));
        }
        logger.info("Request to crammer for mux integration : {}", nodeInfoRequestBean.toString());
        kafkaProducer.sendMessage(pushNodeInfoTopic, Utils.convertObjectToJson(nodeInfoRequestBean));
        return nodeInfoRequestBean;
    }

    @Transactional(readOnly = false)
    public PlannedEventBean getPlannedEventInfo(String peId) {
        return new PlannedEventBean(plannedEventRepository.findByPlannedEventId(peId));
    }

    /**
     * Method to save the planned event entries in the table
     *
     * @param plannedEventBean
     * @param optimusStatus
     * @param plannedEventStatus
     * @param nodeName
     * @param serviceCode
     * @param isPreFetched
     * @param processInstanceId
     * @return
     */
    private PlannedEvent savePlannedEvent(PlannedEventBean plannedEventBean, String optimusStatus, String plannedEventStatus, String nodeName, Boolean isPreFetched, String serviceCode, String processInstanceId) {
        PlannedEvent plannedEvent = new PlannedEvent();
        Instant instant = Instant.now();
        long timeStampMillis = instant.toEpochMilli();
        plannedEvent.setPlannedEventId("PE_" + timeStampMillis);
        plannedEvent.setMuxName(nodeName);
        plannedEvent.setPlannedEventStatus(plannedEventStatus);
        plannedEvent.setOptimusStatus(optimusStatus);
        plannedEvent.setStartTime(plannedEventBean.getPeStartDateAndTime());
        plannedEvent.setEndTime(plannedEventBean.getPeEndDateAndTime());
        plannedEvent.setPreFetched(isPreFetched);
        plannedEvent.setServiceCode(serviceCode);
        plannedEvent.setProcessInstanceId(processInstanceId);
        return plannedEventRepository.save(plannedEvent);
    }


    public Map<String, String> getMuxDetailsFromScComponent(Integer serviceId,String componentType,String siteType) {        
        return commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("endMuxNodeName", "endMuxNodeIp", "eorDetails", "muxMakeModel"), serviceId, "LM", siteType);
    }


    @Transactional(readOnly = false)
    private void saveInNetworkInventory(NodeInfoRequestBean nodeInfoRequestBean, String serviceCode) throws TclCommonException {
        NetworkInventory networkInventory = new NetworkInventory();
        networkInventory.setRequestId(nodeInfoRequestBean.getRequestid());
        networkInventory.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        networkInventory.setRequest(Utils.convertObjectToJson(nodeInfoRequestBean));
        networkInventory.setType("MUX_INTEGRATION");
        networkInventory.setServiceCode(serviceCode);
        networkInventoryRepository.save(networkInventory);
    }

    /**
     * Create Planned event.
     *
     * @param taskId
     * @param createMuxPlannedEventBean
     * @return {@link CreateMuxPlannedEventBean}
     * @throws TclCommonException
     */
    @Transactional(readOnly = false)
	public PlannedEventResponseBean createPlannedEvent(CreateMuxPlannedEventBean createMuxPlannedEventBean)
			throws TclCommonException {
		PlannedEventResponseBean plannedEventResponseBean;
		Task task = getTaskByIdAndWfTaskId(createMuxPlannedEventBean.getTaskId(),
				createMuxPlannedEventBean.getWfTaskId());

		if (createMuxPlannedEventBean.getRaisePlannedEvent() != null
				&& CommonConstants.NO.equalsIgnoreCase(createMuxPlannedEventBean.getRaisePlannedEvent())) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("plannedEventId", createMuxPlannedEventBean.getPlannedEventId());
			atMap.put("plannedEventremarks", createMuxPlannedEventBean.getRemarks());
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
					task.getSiteType());

			flowableBaseService.taskDataEntry(task, createMuxPlannedEventBean);

			return new PlannedEventResponseBean();

		} else {
			CreateMuxPlannedEventRequest createMuxPlannedEventRequest = new CreateMuxPlannedEventRequest();

			BeanUtils.copyProperties(createMuxPlannedEventBean, createMuxPlannedEventRequest);

			PlannedEventBean plannedEventBean = getPlannedEventInfo(createMuxPlannedEventBean.getOptimusPeId());

			createMuxPlannedEventRequest.setPlannedStartDate(plannedEventBean.getPeStartDateAndTime());
			createMuxPlannedEventRequest.setPlannedEndDate(plannedEventBean.getPeEndDateAndTime());

			enrichCreateMuxPlannedEventBean(createMuxPlannedEventRequest, task.getServiceId(), plannedEventBean, task);
			RestResponse response = restClientService.postWithBasicAuthentication(plannedEventUrl,
					Utils.convertObjectToJson(createMuxPlannedEventRequest), createCommonHeader(), userName, password);

			logger.info("Service now response status :{}, response :{}, errorMessage : {}", response.getStatus(),
					response.getData(), response.getErrorMessage());

			if (Objects.equals(response.getStatus(), Status.SUCCESS)) {
				plannedEventResponseBean = Utils.convertJsonToObject(response.getData(),
						PlannedEventResponseBean.class);
				if (!StringUtils.isEmpty(plannedEventResponseBean.getPeId())) {
					Optional.ofNullable(plannedEventRepository.findByPlannedEventId(plannedEventResponseBean.getPeId()))
							.ifPresent(plannedEvent -> plannedEvent.setOptimusStatus("S-CLOSED"));
				}
			} else if (Objects.equals(response.getStatus().getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR.value())
					|| Objects.equals(response.getStatus().getStatusCode(), HttpStatus.BAD_REQUEST.value())) {
				plannedEventResponseBean = Utils.convertJsonToObject(response.getData(),
						PlannedEventResponseBean.class);
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
			validateInputs(task, createMuxPlannedEventBean);
			processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, plannedEventResponseBean.getMessage(), null,
					createMuxPlannedEventBean);

			Map<String, String> atMap = new HashMap<>();
			atMap.put("plannedEventId", createMuxPlannedEventRequest.getOptimusPeId());
			atMap.put("plannedEventStartTime", createMuxPlannedEventRequest.getPlannedStartDate());
			atMap.put("plannedEventEndTime", createMuxPlannedEventRequest.getPlannedEndDate());
			atMap.put("plannedEventStatus", plannedEventBean.getPlannedEventStatus());
			atMap.put("raisePlannedEvent", createMuxPlannedEventBean.getRaisePlannedEvent());

			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
					task.getSiteType());

			flowableBaseService.taskDataEntry(task, createMuxPlannedEventBean);
			return plannedEventResponseBean;
		}
	}

    /**
     * This method will further enrich the bean which will be sent as service now request for mux integration.
     *
     * @param createMuxPlannedEventBean
     * @param serviceId
     * @return {@link CreateMuxPlannedEventBean}
     */
    private CreateMuxPlannedEventRequest enrichCreateMuxPlannedEventBean(CreateMuxPlannedEventRequest createMuxPlannedEventRequest, Integer serviceId,
                                                                      PlannedEventBean plannedEventBean,Task task) {
    	createMuxPlannedEventRequest.setCategory("u_planned_event");
    	createMuxPlannedEventRequest.setSubCategory("u_mux_installation");
    	createMuxPlannedEventRequest.setResourceType("Node");
    	createMuxPlannedEventRequest.setChannel("Optimus");
    	createMuxPlannedEventRequest.setReason("Network Upgrade");
    	createMuxPlannedEventRequest.setEnableNotification("true");

        Map<String, String> muxDetailsFromScComponent = getMuxDetailsFromScComponent(serviceId,"LM",task.getSiteType());
        createMuxPlannedEventRequest.setVariableList(new VariableList(muxDetailsFromScComponent.getOrDefault("muxMakeModel", ""),
                muxDetailsFromScComponent.getOrDefault("muxMakeModel", ""),
                muxDetailsFromScComponent.getOrDefault("endMuxNodeIp", "")));
        createMuxPlannedEventRequest.setResourceName(muxDetailsFromScComponent.getOrDefault("endMuxNodeName", ""));
        return createMuxPlannedEventRequest;
    }

    private Map<String, String> createCommonHeader() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        //headers.put("YAuthorization", userInfoUtils.getUserInformation().getUserId());
        headers.put("YAuthorization", plannedEventUser);
        return headers;
    }


    /**
     * This method will create planned event slots.
     *
     * @return List of {@link PlannedEventBean}
     */
    private List<PlannedEventBean> constructSlot() {
        List<PlannedEventBean> plannedEventBeanList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDateTime localDateTime = ZonedDateTime.now().toLocalDateTime();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        LocalDateTime gmtDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("GMT"));
        logger.info("Local time :{},GMT :{}", localDateTime, gmtDateTime);
        for (int i = 1; i <= 5; i++) {
            //First slot 11PM - 2AM
            PlannedEventBean plannedEventBean = new PlannedEventBean();
            LocalDateTime slot1startDateAndTime = gmtDateTime.withHour(17).withMinute(30).withSecond(0);
            LocalDateTime slot1sEndDateAndTime = slot1startDateAndTime.plusHours(3);
            plannedEventBean.setPeStartDateAndTime(slot1startDateAndTime.format(dateTimeFormatter));
            plannedEventBean.setPeEndDateAndTime(slot1sEndDateAndTime.format(dateTimeFormatter));
            //Second slot 3AM - 6PM
            PlannedEventBean plannedEventBean1 = new PlannedEventBean();
            LocalDateTime slot2startDateAndTime = slot1sEndDateAndTime.withHour(3).withMinute(0).withSecond(0);
            LocalDateTime slot2sEndDateAndTime = slot2startDateAndTime.plusHours(3);
            plannedEventBean1.setPeStartDateAndTime(slot2startDateAndTime.format(dateTimeFormatter));
            plannedEventBean1.setPeEndDateAndTime(slot2sEndDateAndTime.format(dateTimeFormatter));
            gmtDateTime = gmtDateTime.plusDays(1);
            plannedEventBeanList.add(plannedEventBean);
            plannedEventBeanList.add(plannedEventBean1);
        }
        return plannedEventBeanList;
    }

    @Transactional(readOnly = false)
    public List<PlannedEventBean> triggerAvailabilitySlots(CreatePlannedEventBean createPlannedEventBean) {
        String serviceCode = createPlannedEventBean.getServiceCode();
        Map<String, String> muxDetailsFromScComponentAttributes = getMuxDetailsFromScComponent(createPlannedEventBean.getServiceId(),"LM",createPlannedEventBean.getSiteType());
        String muxName = muxDetailsFromScComponentAttributes.containsKey("endMuxNodeName") ? muxDetailsFromScComponentAttributes.get("endMuxNodeName") : "";
        List<PlannedEventBean> plannedEventBeanList = new ArrayList<>();
        constructSlot().forEach(plannedEventBean -> {
            plannedEventBeanList.add(new PlannedEventBean(triggerMuxIntegrationRequest(plannedEventBean, serviceCode, muxDetailsFromScComponentAttributes, muxName, true, createPlannedEventBean.getProcessInstanceId())));
        });
        return plannedEventBeanList;
    }

    /**
     * returns a list of nodes which are need to be configured for particular serviceCode.
     *
     * @param serviceCode
     * @return List of {@link NodeToBeConfigured}
     */
    public List<NodeToConfigureBean> getNodesToBeConfigured(String serviceCode) {
        List<NodeToConfigureBean> nodeToBeConfiguredList = new ArrayList<>();
        Optional.ofNullable(txConfigurationRepository.findFirstByServiceIdAndStatusOrderByIdDesc(serviceCode,"ISSUED"))
                .ifPresent(txConfiguration -> {
                    nodeToBeConfiguredList.addAll(txConfiguration
                            .getNodeToConfigures()
                            .stream()
                            .filter(node -> node.getIsNocActionRequired().equalsIgnoreCase("Y"))
                            .map(this::constructNodeToBeConfigured)
                            .collect(Collectors.toList()));
                });
        return nodeToBeConfiguredList;
    }

    private NodeToConfigureBean constructNodeToBeConfigured(NodeToConfigure nodeToConfigure) {
        NodeToConfigureBean nodeToBeConfigured = new NodeToConfigureBean();
        nodeToBeConfigured.setId(nodeToConfigure.getId());
        nodeToBeConfigured.setNodeName(nodeToConfigure.getNodeName());
        nodeToBeConfigured.setNodeType(nodeToBeConfigured.getNodeType());
        nodeToBeConfigured.setNodeTypeId(Long.parseLong(nodeToConfigure.getNodeTypeId()));
        nodeToBeConfigured.setNodeAlias1(nodeToConfigure.getNodeAlias1());
        nodeToBeConfigured.setNodeAlias2(nodeToConfigure.getNodeAlias2());
        nodeToBeConfigured.setNodeDef(nodeToConfigure.getNodeDef());
        nodeToBeConfigured.setNodeDefId(Long.parseLong(nodeToConfigure.getNodeDefId()));
        nodeToBeConfigured.setACEActionRequired("Y".equalsIgnoreCase(nodeToConfigure.getIsAceActionRequired()) ? true : false);
        nodeToBeConfigured.setCienaActionRequired("Y".equalsIgnoreCase(nodeToConfigure.getIsCienaActionRequired()) ? true : false);
        nodeToBeConfigured.setNOCActionRequired("Y".equalsIgnoreCase(nodeToConfigure.getIsNocActionRequired()) ? true : false);
        return nodeToBeConfigured;
    }

    @Transactional(readOnly = false)
    public List<NodeToConfigureBean> saveNodesToBeConfigured(Integer taskId, String wfTaskId, NodeConfigurations nodesToBeConfigured) throws TclCommonException {
        List<NodeToConfigureBean> configuredNodes = new ArrayList<>();
        Task task = getTaskByIdAndWfTaskId(taskId,wfTaskId);
        nodesToBeConfigured.getNodeToBeConfigured()
                .forEach(nodeToConfigureBean -> {
                    NodeToConfigure node = nodeToConfigureRepository.findById(nodeToConfigureBean.getId()).get();
                    node.setIsAceActionRequired("Y");
                    node.setIsNocActionRequired("Y");
                    node.setConfiguredDate(nodeToConfigureBean.getConfiguredDate());
                    configuredNodes.add(constructNodeToBeConfigured(nodeToConfigureRepository.save(node)));
                });

        flowableBaseService.taskDataEntry(task, nodesToBeConfigured);
        return configuredNodes;
    }

    /**
     * save ss-dump details for specified circuitId/serviceId.
     *
     * @param processInstanceId
     * @return {@link SSDumpResponseBean}
     * @throws TclCommonException
     */
    @Transactional(readOnly = false)
    public SSDumpResponseBean getSSDumpDetails(String processInstanceId) throws TclCommonException {
        NetworkInventory networkInventory = new NetworkInventory();
        String[] values = processInstanceId.split("#");
        String url = constructWirelessUrl(SS_DUMP, wireless1User, wireless1UserPwd, values[1]);
        networkInventory.setRequest(url);
        networkInventory.setServiceCode(values[1]);
        networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
        networkInventory.setRequestId(values[0]);
        networkInventory.setType("GET_SS_DUMPS");
        RestResponse response = restClientService.get(url, new HashMap<>(), true);
        if (response.getStatus() == Status.SUCCESS) {
            try {
                SSDumpResponseBean ssDumpBean = Utils.convertJsonToObject(response.getData(), SSDumpResponseBean.class);
                rfTestResultRepository.findFirstByCircuitIdOrderByIdDesc(values[1])
                        .ifPresent(rfTestResult -> {
                            rfTestResult.setSsDumpDetail(response.getData());
                            rfTestResultRepository.save(rfTestResult);
                        });
                networkInventory.setResponse(response.getData());
                return ssDumpBean;
            } catch (TclCommonException e) {
                logger.error("Exception Occurred in getPingTestResult", e);
                networkInventory.setResponse(response.getData());
                rfTestResultRepository.findFirstByCircuitIdOrderByIdDesc(values[1])
                        .ifPresent(rfTestResult -> {
                            rfTestResult.setSsDumpDetail(response.getData());
                            rfTestResultRepository.save(rfTestResult);
                        });
            }
            networkInventoryRepository.save(networkInventory);
        }
        return null;
    }

    /**
     * sets ping stability for specified circuitId/serviceId.
     *
     * @param processInstanceId
     * @return uniqueId
     * @throws TclCommonException
     */
    @Transactional(readOnly = false)
    public String setPingStability(String processInstanceId) {
        String res = "";
        NetworkInventory networkInventory = new NetworkInventory();
        String[] values = processInstanceId.split("#");
        RestResponse response = null;
        try {
            logger.info("Setting Ping Stability for service/circuit id : {}", values[0]);
            String url = constructWirelessUrl(SET_PING_STABILITY, wireless1User, wireless1UserPwd, values[1]);
            networkInventory.setRequest(url);
            networkInventory.setServiceCode(values[1]);
            networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
            networkInventory.setRequestId(values[0]);
            networkInventory.setType("SET_PING_STABILITY");
            response = restClientService.get(url, new HashMap<>(), true);
            if (response.getStatus() == Status.SUCCESS && !response.getData().equalsIgnoreCase("Circuit ID not found")) {
                SetPingStabilityResponseBean setPingStabilityResponseBean = Utils.convertJsonToObject(response.getData(), SetPingStabilityResponseBean.class);
                res = setPingStabilityResponseBean.getSetPingStablility().getSsUniqueId();
                if (!StringUtils.isEmpty(res)) {
                    RfTestResult rfTestResult = new RfTestResult();
                    rfTestResult.setCircuitId(values[1]);
                    rfTestResult.setUniqueId(res);
                    rfTestResultRepository.save(rfTestResult);
                }
                res = response.getData();
                networkInventory.setResponse(response.getData());
                networkInventoryRepository.save(networkInventory);
            }
        } catch (Exception e) {
            logger.error("Exception occurred in setPingStability :{}", e);
            RfTestResult rfTestResult = new RfTestResult();
            rfTestResult.setCircuitId(values[1]);
            rfTestResult.setUniqueId(res);
            rfTestResultRepository.save(rfTestResult);
            networkInventory.setResponse(response.getData());
            networkInventoryRepository.save(networkInventory);
        }
        return res;
    }

    /**
     * Gets ping test details for given circuitId.
     *
     * @param processInstanceId
     * @return {@link GetPingStabilityResponse}
     * @throws TclCommonException
     */
    @Transactional(readOnly = false)
    public GetPingStabilityResponse  getPingTestResult(String processInstanceId) {
        NetworkInventory networkInventory = new NetworkInventory();
        String[] values = processInstanceId.split("#");
        logger.info("Getting Ping Stability test details for service/circuit id : {}", values[1]);
        RfTestResult testResult = rfTestResultRepository.findFirstByCircuitIdOrderByIdDesc(values[1])
                .orElseThrow(() -> new TclCommonRuntimeException("Record not found for Circuit/service Id : " + values[1]));

        if(!StringUtils.isEmpty(testResult.getUniqueId())) {
            String uniqueId = testResult.getUniqueId();
            String url = constructWirelessUrlforPingTest(GET_PING_STABILITY, wireless1User, wireless1UserPwd, uniqueId);
            networkInventory.setRequest(url);
            networkInventory.setRequestId(values[0]);
            networkInventory.setServiceCode(values[1]);
            networkInventory.setType("GET_PING_TEST_DETAILS");
            networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
            RestResponse response = restClientService.get(url, new HashMap<>(), true);
            if (response.getStatus() == Status.SUCCESS) {
                try {
                    GetPingStabilityResponse pingTestResult = Utils.convertJsonToObject(response.getData(), GetPingStabilityResponse.class);
                    rfTestResultRepository.findByUniqueId(uniqueId)
                            .ifPresent(rfTestResult -> {
                                rfTestResult.setPingTestDetail(response.getData());
                                rfTestResultRepository.save(rfTestResult);
                            });
                    networkInventory.setResponse(response.getData());
                    return pingTestResult;
                } catch (TclCommonException e) {
                    logger.error("Exception Occurred in getPingTestResult", e);
                    networkInventory.setResponse(response.getData());
                    rfTestResultRepository.findFirstByCircuitIdOrderByIdDesc(values[1])
                            .ifPresent(rfTestResult -> {
                                rfTestResult.setSsDumpDetail(response.getData());
                                rfTestResultRepository.save(rfTestResult);
                            });
                    networkInventoryRepository.save(networkInventory);
                }
            }
        }
        return null;
    }

    private String constructWirelessUrl(String api, String username, String password, String circuitId) {
        return wirelessBaseUrl + "/" + api + "/?circuit_id=" + circuitId + "&username=" + username + "&password=" + password;
    }

    private String constructWirelessUrlforPingTest(String api, String username, String password, String uniqueId) {
        return wirelessBaseUrl + "/" + api + "/?unique_id=" + uniqueId + "&username=" + username + "&password=" + password;
    }

    @Transactional(readOnly = false)
    public MuxJeopardyBean raiseJeopardy(MuxJeopardyBean muxJeopardyBean) throws TclCommonException {
        Task task = getTaskByIdAndWfTaskId(muxJeopardyBean.getTaskId(), muxJeopardyBean.getWfTaskId());
    
        Map<String, Object> map = new HashMap<>();
        map.put("action", muxJeopardyBean.getAction());

        if(muxJeopardyBean!=null && (!StringUtils.isEmpty(muxJeopardyBean.getEndMuxNodeName()) || !StringUtils.isEmpty(muxJeopardyBean.getPopSiteCode()))) {
        	updateMuxRelatedAttributes(task.getServiceId(), muxJeopardyBean,task);
        }
        
         if(task.getMstTaskDef().getKey().toLowerCase().contains("billing-issue") && muxJeopardyBean.getCommissioningDate()!=null){
			String commissioningDate = StringUtils.trimToEmpty(muxJeopardyBean.getCommissioningDate());
			logger.info("billing-issue=>commissioningDate={}",commissioningDate);
			
			if (muxJeopardyBean.getDocumentIds() != null ) {
				muxJeopardyBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			 Optional<ScServiceDetail> scServiceDetailOp = scServiceDetailRepository.findById(task.getServiceId());
			 if(scServiceDetailOp.isPresent()) {
				 cramerService.generateBillStartDate(scServiceDetailOp.get(),commissioningDate,null,null);
			 }
		}
         
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("enrich-service-design-jeopardy")
				&& !"retry".equalsIgnoreCase(muxJeopardyBean.getAction())) {
			logger.info("enrich-service-design-jeopardy amendedOrder with Onnet RF NEW or PARALLEL or Other than retry: {}", task.getServiceCode());
			try {

				ServiceDetail serviceDetail=saveServiceDetails(task);
                if("NEW".equalsIgnoreCase(task.getScServiceDetail().getScOrder().getOrderType()) && task.getScServiceDetail().getMultiVrfSolution()!=null
                        && CommonConstants.Y.equalsIgnoreCase(task.getScServiceDetail().getMultiVrfSolution())
                        && CommonConstants.Y.equalsIgnoreCase(task.getScServiceDetail().getIsMultiVrf())){
                    logger.info("triggerSlaveFulfillmentFlow master vrf id : {}",task.getScServiceDetail().getId());
                    triggerSlaveFulfillmentFlow(task.getScServiceDetail().getUuid());
                }
                if (task.getScServiceDetail().getIsAmended() != null
						&& task.getScServiceDetail().getIsAmended().equalsIgnoreCase(CommonConstants.Y)) {
                	  logger.info("enrich-service-design-jeopardy amendedOrder with Service Id : {}",task.getScServiceDetail().getId());
                	ScComponentAttribute lmTypeComponentAttribute = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									task.getServiceId(), "lmType",
									AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
	                 if (task.getScServiceDetail().getOrderType().equalsIgnoreCase(CramerConstants.TYPE_MACD) && !"ADD_SITE".equalsIgnoreCase(task.getScServiceDetail().getOrderCategory())
	    						&& ((Objects.nonNull(task.getScServiceDetail().getOrderSubCategory()) && !task.getScServiceDetail().getOrderSubCategory().toLowerCase().contains("parallel")))
	    						&& (lmTypeComponentAttribute!=null && lmTypeComponentAttribute.getAttributeValue()!=null 
	    							&& (lmTypeComponentAttribute.getAttributeValue().equalsIgnoreCase("OnnetRF") || lmTypeComponentAttribute.getAttributeValue().equalsIgnoreCase("onnet wireless")))) {
	    				  logger.info("enrich-service-design-jeopardy amendedOrder with Onnet RF MACD: {}", task.getServiceCode());
	    				  cramerService.updateOnnetWirelessDetails(task.getServiceCode(),serviceDetail, task.getScServiceDetail().getOrderSubCategory());
	    			  }else{
	    				  logger.info("enrich-service-design-jeopardy amendedOrder with Onnet RF NEW or PARALLEL or ADD_SITE: {}", task.getServiceCode());
	    				  cramerService.constructOnnetWirelessDetails(task.getServiceCode(),task.getServiceId());
	    			  }
				}
			} catch (Exception e) {
				logger.error("saveServiceDetails invoked for clr with service:{} and error :{}",task.getServiceCode(),e);

			}
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("enrich-service-design-jeopardy-dependency")) {
			Map<String, String> componentMap = new HashMap<>();
			componentMap.put("eorIorDependencyReason", muxJeopardyBean.getEorIorDependencyReason());
			componentAndAttributeService.updateAttributes(muxJeopardyBean.getServiceId(), componentMap,
					AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
		}
		
        
        flowableBaseService.taskDataEntry(task, muxJeopardyBean, map);
        if(StringUtils.isNotBlank(muxJeopardyBean.getRemarks()))processTaskLogDetails(task,TaskStatusConstants.REMARKS,muxJeopardyBean.getRemarks(),null, muxJeopardyBean);
        
        
        String message="";
        if(muxJeopardyBean.getReleasedBy()!=null && !muxJeopardyBean.getReleasedBy().isEmpty()) {
        	if(muxJeopardyBean.getDelayReason()!=null && !muxJeopardyBean.getDelayReason().isEmpty()) {
        		message=muxJeopardyBean.getReleasedBy()+" and "+muxJeopardyBean.getDelayReason();
        	} else {
        		message=muxJeopardyBean.getReleasedBy();
        	}
        } else {
        	
        		message=muxJeopardyBean.getDelayReason();
        	
        }
        processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,message,null,muxJeopardyBean);

        return muxJeopardyBean;
    }
    
    private ServiceDetail saveServiceDetails(Task task) {
		logger.info("saveServiceDetails invoked");
		ServiceDetail serviceDetail = serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByVersionDesc(
				task.getServiceCode(), TaskStatusConstants.ISSUED);
		if (serviceDetail!=null) {
			serviceDetail.setServiceState(TaskStatusConstants.CANCELLED);
			serviceDetailRepository.saveAndFlush(serviceDetail);
		}
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(task.getOrderCode(), "Y");
		ServiceDetail activeServiceDetail = serviceDetailRepository
				.findFirstByServiceIdAndServiceStateOrderByVersionDesc(task.getServiceCode(),
						TaskStatusConstants.ACTIVE);
		Integer version = null;
		if (Objects.nonNull(activeServiceDetail)) {
			version = activeServiceDetail.getVersion();
		}
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();

		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
				.getComponentAttributes(task.getServiceId(), AttributeConstants.COMPONENT_LM, "A");

		serviceDetail = serviceActivationService.saveServiceDetailsActivation(task.getServiceCode(), version,
				scOrder, scServiceDetail, scComponentAttributesAMap);
		return serviceDetail;
	}


    private void updateMuxRelatedAttributes(Integer serviceId, MuxJeopardyBean muxJeopardyBean, Task task) {
        Map<String, String> muxDetailsMap = new HashMap<>();
        muxDetailsMap.put("endMuxNodeName", muxJeopardyBean.getEndMuxNodeName());
        muxDetailsMap.put("endMuxNodeIp", muxJeopardyBean.getEndMuxNodeIp());
        muxDetailsMap.put("endMuxNodePort", muxJeopardyBean.getEndMuxNodePort());
        muxDetailsMap.put("popSiteCode", muxJeopardyBean.getPopSiteCode());
        
        if(task.getServiceType().equalsIgnoreCase("NPL")) {
        	 Map<String, String> muxDetailsMapB = new HashMap<>();
        	 muxDetailsMapB.put("endMuxNodeName", muxJeopardyBean.getEndMuxNodeNameB());
        	 muxDetailsMapB.put("endMuxNodeIp", muxJeopardyBean.getEndMuxNodeIpB());
        	 muxDetailsMapB.put("endMuxNodePort", muxJeopardyBean.getEndMuxNodePortB());
        	 muxDetailsMapB.put("popSiteCode", muxJeopardyBean.getPopSiteCodeB());
             componentAndAttributeService.updateAttributes(serviceId, muxDetailsMapB, "LM","B");
        	
        }
        componentAndAttributeService.updateAttributes(serviceId, muxDetailsMap, "LM","A");
    }
    @Transactional(readOnly = false)
    public TxJeopardyBean raiseTxJeopardy(TxJeopardyBean txJeopardyBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(txJeopardyBean.getTaskId(), txJeopardyBean.getWfTaskId());
		Map<String, Object> map = new HashMap<>();
		Map<String, String> attributeMap = new HashMap<>();
		map.put("txConfigAction", txJeopardyBean.getAction());
		map.put("remarks", txJeopardyBean.getRemarks());
		if (txJeopardyBean.getOwner() != null && !txJeopardyBean.getOwner().isEmpty())
			map.put("owner", txJeopardyBean.getOwner());
		if (txJeopardyBean.getReason() != null && !txJeopardyBean.getReason().isEmpty())
			map.put("reason", txJeopardyBean.getReason());
		if (txJeopardyBean.getTxResourcePathType() != null && !txJeopardyBean.getTxResourcePathType().isEmpty()){
			logger.info("TxResourcePathType::{}", txJeopardyBean.getTxResourcePathType());
			ServiceDetail serviceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(task.getServiceCode(),"ISSUED");
			String txResourcePathType = StringUtils.trimToEmpty(txJeopardyBean.getTxResourcePathType());
						if (txJeopardyBean.getIsTxDowntimeReqd()){
				attributeMap.put("isTxDowntimeReqd", "true");
			}else {
				attributeMap.put("isTxDowntimeReqd", "false");
				
			}				
			if(Objects.nonNull(serviceDetail)){
				logger.info("serviceDetail Exists::{}", serviceDetail.getId());
				serviceDetail.setResourcePath(txResourcePathType);
				serviceDetail.setIstxdowntimeReqd(txJeopardyBean.getIsTxDowntimeReqd()==true?(byte)1:(byte)0);
				serviceDetailRepository.save(serviceDetail);
			}
			map.put("txResourcePathType", txResourcePathType);
			attributeMap.put("txResourcePathType", txJeopardyBean.getTxResourcePathType());
		}else if (txJeopardyBean.getIsTxDowntimeReqd() != null){
			logger.info("TxResourcePathType not exists");
			map.put("action", "manual");
			if (txJeopardyBean.getIsTxDowntimeReqd()){
				attributeMap.put("isTxDowntimeReqd", "true");
			}else {
				attributeMap.put("isTxDowntimeReqd", "false");
				
			}				
			
			ServiceDetail serviceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(task.getServiceCode(),"ISSUED");
			
			if(Objects.nonNull(serviceDetail)){
				logger.info("serviceDetail Exists::{}", serviceDetail.getId());
				
				serviceDetail.setIstxdowntimeReqd(txJeopardyBean.getIsTxDowntimeReqd()==true?(byte)1:(byte)0);
				serviceDetailRepository.save(serviceDetail);
			}
			
		}
		
		if(!"bop".equalsIgnoreCase(txJeopardyBean.getAction())
				&& !"fnoc".equalsIgnoreCase(txJeopardyBean.getAction())
				&& !"ntac".equalsIgnoreCase(txJeopardyBean.getAction())
				&& !"ace_support".equalsIgnoreCase(txJeopardyBean.getAction())
				&& !"mpls_tp".equalsIgnoreCase(txJeopardyBean.getAction())
				&& !"sdnw".equalsIgnoreCase(txJeopardyBean.getAction())
				&& task.getOrderCode().toLowerCase().contains("izosdwan")
				&& attributeMap.containsKey("isTxDowntimeReqd")){
			logger.info("IZOSDWAN DownTimeDetail exists for service id:{} with TxDowntime as::{}", task.getServiceId(),attributeMap.get("isTxDowntimeReqd"));
			DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstByOrderCodeAndScServiceDetailIdOrderByIdDesc(task.getOrderCode(),task.getServiceId());
			if(downTimeDetails!=null){
				logger.info("DownTimeDetail exists for service id:{}",task.getServiceId());
				if("true".equalsIgnoreCase(attributeMap.get("isTxDowntimeReqd"))){
					downTimeDetails.setIsTxDownTimeRequired("Y");
				}else{
					downTimeDetails.setIsTxDownTimeRequired("N");
				}
				downTimeDetails.setUpdatedBy(task.getAssignee());
				downTimeDetails.setUpdatedDate(new Timestamp(new Date().getTime()));
				downTimeDetailsRepository.save(downTimeDetails);
			}
		}

		if(task.getOrderCode()!=null && !task.getOrderCode().toLowerCase().contains("izosdwan") 
				&& "confirm-tx-configuration".equalsIgnoreCase(task.getMstTaskDef().getKey()) && 
				txJeopardyBean.getTxResourcePathType() != null && !txJeopardyBean.getTxResourcePathType().isEmpty()
				&& "samePort".equalsIgnoreCase(txJeopardyBean.getTxResourcePathType())) {
			logger.info("Task for Confirm Tx with Resource Path Type as SamePort::",task.getServiceId());
			List<Task> waitForCustomerTasks = taskRepository.findByServiceIdAndMstTaskDef_key(task.getServiceId(),
					"wait-for-downtime-from-customer");
			if (waitForCustomerTasks != null) {
				logger.info("waitForCustomerTasks exists for IP");
				for (Task waitForCustomerTask : waitForCustomerTasks) {
					Execution execution = runtimeService.createExecutionQuery()
							.processInstanceId(waitForCustomerTask.getWfProcessInstId())
							.activityId("wait-for-downtime-from-customer").singleResult();
					if (execution != null) {
						logger.info("Execution exists for IP wait for downtime::{}", execution.getId());
						runtimeService.setVariable(execution.getId(), "checkDowntimeAction", "manual");
						runtimeService.trigger(execution.getId());
					}
				}

			}
		}
		
		if(txJeopardyBean.getInternalCablingRequiredAt()!=null && !txJeopardyBean.getInternalCablingRequiredAt().isEmpty()){
			logger.info("internalCablingRequiredAt::{}", txJeopardyBean.getInternalCablingRequiredAt());
			if("Customer Side".equalsIgnoreCase(txJeopardyBean.getInternalCablingRequiredAt())){
				logger.info("Customer Side");
				map.put("isCEInternalCablingRequired", true);
				map.put("isPEInternalCablingRequired", false);
			}else if("POP Side".equalsIgnoreCase(txJeopardyBean.getInternalCablingRequiredAt())){
				logger.info("POP Side");
				map.put("isPEInternalCablingRequired", true);
				map.put("isCEInternalCablingRequired", false);
			}else if("Both".equalsIgnoreCase(txJeopardyBean.getInternalCablingRequiredAt())){
				logger.info("Both");
				map.put("isCEInternalCablingRequired", true);
				map.put("isPEInternalCablingRequired", true);
			}else if("None".equalsIgnoreCase(txJeopardyBean.getInternalCablingRequiredAt())){
				logger.info("None");
				map.put("isCEInternalCablingRequired", false);
				map.put("isPEInternalCablingRequired", false);
			}			
		}
		
		if(txJeopardyBean.getAction().equalsIgnoreCase("manual")) {
			logger.info("Manualtxconfig-selected-setting-serviceContents-empty");
			map.put("serviceContents", "");
		}
		flowableBaseService.taskDataEntry(task, txJeopardyBean, map);
		
		if ("bop".equalsIgnoreCase(txJeopardyBean.getAction())) {
			try {

				componentAndAttributeService.updateAdditionalAttributes(task.getServiceId(),
						"serviceDesignTxCallFailureReason",
						componentAndAttributeService.getErrorMessageDetails(txJeopardyBean.getRemarks(), txJeopardyBean.getReason()),
						AttributeConstants.ERROR_MESSAGE, "enrich-service-design");

			} catch (Exception e) {
				logger.error("raiseTxJeopardy getting error message details", e);
			}
		}
		
		if ("fnoc".equalsIgnoreCase(txJeopardyBean.getAction())
			|| "ntac".equalsIgnoreCase(txJeopardyBean.getAction())
			|| "ace_support".equalsIgnoreCase(txJeopardyBean.getAction())
			|| "mpls_tp".equalsIgnoreCase(txJeopardyBean.getAction())
			|| "sdnw".equalsIgnoreCase(txJeopardyBean.getAction()))  {
			
			componentAndAttributeService.updateAdditionalAttributes(task.getServiceId(),
					"txFailureRemarks",
					componentAndAttributeService.getErrorMessageDetails(txJeopardyBean.getRemarks(), txJeopardyBean.getReason()),
					AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());
		}
		
		if(!attributeMap.isEmpty())componentAndAttributeService.updateAttributes(task.getServiceId(), attributeMap,"LM", task.getSiteType());

		if (StringUtils.isNotBlank(txJeopardyBean.getRemarks())) {
			processTaskLogDetails(task, TaskStatusConstants.REMARKS, txJeopardyBean.getRemarks(), null, txJeopardyBean);
		}
		

		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, txJeopardyBean.getDelayReason(), null,
				txJeopardyBean);

		return txJeopardyBean;
	}

    @Transactional(readOnly = false)
	public CustomerDownTimeBean persistDownTimeDetails(Integer taskId,CustomerDownTimeBean customerDownTimeBean) throws TclCommonException {
		logger.info("persistDownTimeDetails invoked:: {},{},{}",customerDownTimeBean.getDowntimeDuration(),customerDownTimeBean.getFromTime(),customerDownTimeBean.getToTime());
		Task task = taskRepository.findById(taskId).orElse(null);
		if(task!=null){
			if(task.getOrderCode().toLowerCase().contains("izosdwan")){
				logger.info("IZOSDWAN Task exists for taskId::{},ordercode::{},serviceId::{}",task.getId(),task.getOrderCode(),task.getServiceId());
				if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-provide-downtime-for-migration")){
					 logger.info("Task key::{} with task Id::{}, with ordercode::{}, with serviceId::{}",task.getMstTaskDef().getKey(),task.getId(),task.getOrderCode(),task.getServiceId());
					 Map<String, Object> flowableMap = new HashMap<>();
					 Map<String, String> customerAppointmentMap = new HashMap<>();
					 customerAppointmentMap.put("migrationDowntimeDuration", customerDownTimeBean.getDowntimeDuration());
					 customerAppointmentMap.put("migrationDowntimeFromTime", customerDownTimeBean.getFromTime());
					 customerAppointmentMap.put("migrationDowntimeToTime", customerDownTimeBean.getToTime());
					 customerAppointmentMap.put("migrationDowntimeContactPerson", customerDownTimeBean.getContactPerson());
					 customerAppointmentMap.put("migrationDowntimeContactMobile", customerDownTimeBean.getContactMobileNumber());
					 customerAppointmentMap.put("migrationDowntimeContactMailId", customerDownTimeBean.getContactMailId());
					 logger.info("Task ServiceId::{}",task.getServiceId());
					 String customerAppointmentTime="";
					 customerAppointmentTime=getFlowableDownTimeDetail(customerAppointmentTime,customerDownTimeBean);
					 logger.info("CustomerAppointmentTime::{}",customerAppointmentTime);
					 flowableMap.put("migrationDowntimeWindow", customerAppointmentTime);
					 customerAppointmentMap.put("migrationDowntime", customerAppointmentTime);
					 componentAndAttributeService.updateAttributes(task.getServiceId(), customerAppointmentMap, "LM","A");
					 saveDownTimeDetail(customerDownTimeBean,customerAppointmentTime,task.getServiceCode());
					 logger.info("Close Customer Appointment task::{}",flowableMap.get("migrationDowntimeWindow"));
					 processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,"MigrationDowntime",null, customerDownTimeBean);
					 flowableMap.put("action", "close");
					 return (CustomerDownTimeBean) flowableBaseService.taskDataEntry(task, customerDownTimeBean, flowableMap);
				}else{
					 logger.info("Task key::{} with task Id::{}, with ordercode::{}, with serviceId::{}",task.getMstTaskDef().getKey(),task.getId(),task.getOrderCode(),task.getServiceId());
					 Map<String, Object> flowableMap = new HashMap<>();
					 Map<String, String> downTimeDetailMap = new HashMap<>();
					 downTimeDetailMap.put("downtimeDuration", customerDownTimeBean.getDowntimeDuration());
					 downTimeDetailMap.put("fromTime", customerDownTimeBean.getFromTime());
					 downTimeDetailMap.put("toTime", customerDownTimeBean.getToTime());
					 downTimeDetailMap.put("downTimeContactPerson", customerDownTimeBean.getContactPerson());
					 downTimeDetailMap.put("downTimeContactMobile", customerDownTimeBean.getContactMobileNumber());
					 downTimeDetailMap.put("downTimeContactMailId", customerDownTimeBean.getContactMailId());
					 logger.info("Task ServiceId::{}",task.getServiceId());
					 ScSolutionComponent scSolutionComponent=scSolutionComponentRepository.findByScServiceDetail1_idAndOrderCodeAndIsActive(task.getServiceId(),task.getOrderCode(),"Y");
					 if(scSolutionComponent!=null){
						 List<Integer> serviceList=null;
						 logger.info("ScSolutionExists::{}",scSolutionComponent.getId());
						 if("OVERLAY".equalsIgnoreCase(scSolutionComponent.getComponentGroup())){
							 logger.info("OVERLAY enters to downtime::{}",task.getId());
							 serviceList=scSolutionComponentRepository.getServiceDetailsBySolutionIdAndOverlayId(scSolutionComponent.getScServiceDetail3().getId(),scSolutionComponent.getScServiceDetail1().getId(),scSolutionComponent.getScServiceDetail1().getId(),"Y");
						 }else if("UNDERLAY".equalsIgnoreCase(scSolutionComponent.getComponentGroup())){
							 logger.info("UNDERLAY enters to downtime::{}",task.getId());
							 serviceList=scSolutionComponentRepository.getServiceDetailsByOrderCodeAndUnderlayId(task.getOrderCode(),task.getServiceId(),"Y");
							 if(serviceList!=null && !serviceList.isEmpty()){
								 logger.info("Adding overlay id for underlay::{}",scSolutionComponent.getScServiceDetail2().getId());
								 serviceList.add(scSolutionComponent.getScServiceDetail2().getId());
							 }
						 }
						 if(serviceList!=null && !serviceList.isEmpty()){
							 logger.info("ServiceList exists::{}",serviceList.size());
							/* List<String> cpeIpTxList = new ArrayList<>();
							 cpeIpTxList.add("tx-downtime-end-trigger");
							 cpeIpTxList.add("tx-downtime-trigger");
							 cpeIpTxList.add("ip-downtime-trigger");
							 cpeIpTxList.add("ip-downtime-end-trigger");
							 cpeIpTxList.add("actvitiy-go-ahead-downtime-trigger");
							 cpeIpTxList.add("overlay-downtime-trigger");
							 List<String> statusList = new ArrayList<>();
							 statusList.add("REOPEN");
							 statusList.add("OPEN");*/
							 for(Integer serviceId:serviceList){
								 logger.info("serviceId::{}",serviceId);
								 List<Task> waitForCustomerTasks=taskRepository.findByServiceIdAndMstTaskDef_keyAndMstStatus_codeNot(serviceId, "wait-for-downtime-from-customer","CLOSED");
								 for(Task waitForCustomerTask: waitForCustomerTasks){
									 Execution execution = runtimeService.createExecutionQuery().processInstanceId(waitForCustomerTask.getWfProcessInstId())
												.activityId("wait-for-downtime-from-customer").singleResult();
									 if(execution!=null){
										 logger.info("Execution exists for service Id::{},with excution id::{}",waitForCustomerTask.getServiceId(),execution.getId());
										 String flowableDownTime="";
										 flowableDownTime=getFlowableDownTimeDetail(flowableDownTime,customerDownTimeBean);
										 logger.info("FlowDownTime::{}",flowableDownTime);
										 downTimeDetailMap.put("downtime", flowableDownTime);
										 componentAndAttributeService.updateAttributes(waitForCustomerTask.getServiceId(), downTimeDetailMap, "LM","A");
										 saveDownTimeDetail(customerDownTimeBean,flowableDownTime,waitForCustomerTask.getServiceCode());
										 flowableMap.put("downtimeWindow", flowableDownTime);
										 runtimeService.setVariable(execution.getId(),"downtimeWindow", flowableDownTime);	
										 runtimeService.trigger(execution.getId());
									 }
								 }
								/* List<Task> ipTxTriggerTasks=taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeNot(serviceId, cpeIpTxList,"CLOSED");
								 for(Task ipTxTask: ipTxTriggerTasks){
									 logger.info("CpeipTxTask Id::{},IpTx service Id::{},IpTxTask::{},with excution id::{}",ipTxTask.getId(),ipTxTask.getServiceId(),ipTxTask.getMstTaskDef().getKey(),ipTxTask.getWfProcessInstId());
									 Execution execution = runtimeService.createExecutionQuery().processInstanceId(ipTxTask.getWfProcessInstId())
												.activityId(ipTxTask.getMstTaskDef().getKey()).singleResult();
									 if(execution!=null){
										 logger.info("Execution exists for ipTxTask Id::{},IpTx service Id::{},with excution id::{}",ipTxTask.getId(),ipTxTask.getServiceId(),execution.getId());
										 String flowableDownTime="";
										 flowableDownTime=getFlowableDownTimeDetail(flowableDownTime,customerDownTimeBean);
										 logger.info("FlowDownTime::{}",flowableDownTime);
										 downTimeDetailMap.put("downtime", flowableDownTime);
										 componentAndAttributeService.updateAttributes(ipTxTask.getServiceId(), downTimeDetailMap, "LM","A");
										 saveDownTimeDetail(customerDownTimeBean,flowableDownTime,ipTxTask.getServiceCode());
										 flowableMap.put("downtimeWindow", flowableDownTime);
										 runtimeService.setVariable(execution.getId(),"downtimeWindow", flowableDownTime);	
										 runtimeService.trigger(execution.getId());
									 }
								 }*/
								 if(Objects.isNull(waitForCustomerTasks) || waitForCustomerTasks.isEmpty() || flowableMap.get("downtimeWindow")==null){
									 logger.info("waitForCustomerTasks is Empty for service Id::{}",serviceId);
									 	String flowableDownTime="";
									 	flowableDownTime=getFlowableDownTimeDetail(flowableDownTime,customerDownTimeBean);
									    logger.info("FlowDownTime::{}",flowableDownTime);
									    flowableMap.put("downtimeWindow", flowableDownTime);
									    downTimeDetailMap.put("downtime", flowableDownTime);
										componentAndAttributeService.updateAttributes(task.getServiceId(), downTimeDetailMap, "LM","A");
										saveDownTimeDetail(customerDownTimeBean,flowableDownTime,task.getServiceCode());
								 }
							 }
							 logger.info("Close Provide DownTime task::{}",flowableMap.get("downtimeWindow"));
							 processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,"CustomerDownTime",null, customerDownTimeBean);
							 flowableMap.put("action", "close");
							 return (CustomerDownTimeBean) flowableBaseService.taskDataEntry(task, customerDownTimeBean, flowableMap);
						 }
					 }
				}
				 
			}else{
				 logger.info("Task exists for taskId::{},ordercode::{},serviceId::{}",task.getId(),task.getOrderCode(),task.getServiceId());
				 Map<String, Object> flowableMap = new HashMap<>();
				 Map<String, String> downTimeDetailMap = new HashMap<>();
				 downTimeDetailMap.put("downtimeDuration", customerDownTimeBean.getDowntimeDuration());
				 downTimeDetailMap.put("fromTime", customerDownTimeBean.getFromTime());
				 downTimeDetailMap.put("toTime", customerDownTimeBean.getToTime());
				 downTimeDetailMap.put("downTimeContactPerson", customerDownTimeBean.getContactPerson());
				 downTimeDetailMap.put("downTimeContactMobile", customerDownTimeBean.getContactMobileNumber());
				 downTimeDetailMap.put("downTimeContactMailId", customerDownTimeBean.getContactMailId());
				 logger.info("Task ServiceId::{}",task.getServiceId());
				 List<Task> waitForCustomerTasks=taskRepository.findByServiceIdAndMstTaskDef_key(task.getServiceId(), "wait-for-downtime-from-customer");
				 for(Task waitForCustomerTask: waitForCustomerTasks){
					 Execution execution = runtimeService.createExecutionQuery().processInstanceId(waitForCustomerTask.getWfProcessInstId())
								.activityId("wait-for-downtime-from-customer").singleResult();
					 if(execution!=null){
						 logger.info("Execution exists::{}",execution.getId());
						 String flowableDownTime="";
						 flowableDownTime=getFlowableDownTimeDetail(flowableDownTime,customerDownTimeBean);
						 logger.info("FlowDownTime::{}",flowableDownTime);
						 downTimeDetailMap.put("downtime", flowableDownTime);
						 componentAndAttributeService.updateAttributes(task.getServiceId(), downTimeDetailMap, "LM","A");
						 saveDownTimeDetail(customerDownTimeBean,flowableDownTime,task.getServiceCode());
						 flowableMap.put("downtimeWindow", flowableDownTime);
						 runtimeService.setVariable(execution.getId(),"downtimeWindow", flowableDownTime);	
						 runtimeService.trigger(execution.getId());
					 }
				 }
				 if(Objects.isNull(waitForCustomerTasks) || waitForCustomerTasks.isEmpty() || flowableMap.get("downtimeWindow")==null){
					 logger.info("waitForCustomerTasks is Empty");
					 	String flowableDownTime="";
					 	flowableDownTime=getFlowableDownTimeDetail(flowableDownTime,customerDownTimeBean);
					    logger.info("FlowDownTime::{}",flowableDownTime);
					    flowableMap.put("downtimeWindow", flowableDownTime);
					    downTimeDetailMap.put("downtime", flowableDownTime);
						componentAndAttributeService.updateAttributes(task.getServiceId(), downTimeDetailMap, "LM","A");
						saveDownTimeDetail(customerDownTimeBean,flowableDownTime,task.getServiceCode());
				 }
				 logger.info("Close Provide DownTime task::{}",flowableMap.get("downtimeWindow"));
				 processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,"CustomerDownTime",null, customerDownTimeBean);
				 flowableMap.put("action", "close");
				 return (CustomerDownTimeBean) flowableBaseService.taskDataEntry(task, customerDownTimeBean, flowableMap);
			}
		}
		return customerDownTimeBean;
	}

	private String getFlowableDownTimeDetail(String flowableDownTime, CustomerDownTimeBean customerDownTimeBean) throws TclCommonException {
		logger.info("getFlowableDownTimeDetail");
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String currentDate = formatter.format(new Date());
	    if(currentDate.equals(customerDownTimeBean.getDowntimeDuration())){
	    	logger.info("Equal");
	    	Calendar date = Calendar.getInstance();
	    	long t= date.getTimeInMillis();
	    	TimeZone tz = TimeZone.getTimeZone("UTC");
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
	        df.setTimeZone(tz);
	        flowableDownTime = df.format(new Date(t + (10 * 180000)));
	    }else{
	    	logger.info("Not equal");
	    	flowableDownTime=customerDownTimeBean.getDowntimeDuration()+"T00:00Z";
	    }*/
		try{
			//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    //String currentDate = formatter.format(new Date());
		   
		    String inputTime=customerDownTimeBean.getDowntimeDuration()+" "+customerDownTimeBean.getFromTime();
			logger.info("inputTime::{}",inputTime);
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date inputDate=inputDateFormatter.parse(inputTime);
			LocalDateTime localDateTime=null;
			String currentDate = inputDateFormatter.format(new Date());
			logger.info("Current Date::{}",currentDate);
			Date currentParsedDate = inputDateFormatter.parse(currentDate);
			long diff = inputDate.getTime() - currentParsedDate.getTime();
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			logger.info("Hour Difference::{}",diffHours);
			logger.info("Day Difference::{}",diffDays);
			if(diffDays==0 && diffHours<24){
				logger.info("Current Date::{}",currentDate);
				localDateTime=LocalDateTime.ofInstant(currentParsedDate.toInstant(), ZoneId.of("UTC")).plusMinutes(5);
			}else{
				logger.info("Other than Current Date::{}",customerDownTimeBean.getDowntimeDuration());
				localDateTime=LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.of("UTC")).minusHours(24);
			}
			flowableDownTime=localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			/*DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date inputDate=inputDateFormatter.parse	(inputTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(inputDate);
			cal.add(Calendar.HOUR, -24);
			Date intervalDate = cal.getTime();
			logger.info("intervalDate::{}",intervalDate);
			TimeZone tz = TimeZone.getTimeZone("UTC");
		    DateFormat utcFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		    utcFormatter.setTimeZone(tz);
			flowableDownTime=utcFormatter.format(intervalDate);*/
			logger.info("Derived flowableDownTime::{}",flowableDownTime);
		    return flowableDownTime;
		} catch (Exception ex) {
			logger.error("Exception for getFlowableDownTimeDetail:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	private void saveDownTimeDetail(CustomerDownTimeBean customerDownTimeBean, String flowableDownTime,String serviceCode) {
		 logger.info("saveDownTimeDetail");
		 ServiceDetail issuedServiceDetail=serviceDetailRepository.findFirstByServiceIdAndServiceStateOrderByIdDesc(serviceCode,"ISSUED");
		 if(Objects.nonNull(issuedServiceDetail)){
			 logger.info("Issued record exists");
			 issuedServiceDetail.setDowntimeDuration(customerDownTimeBean.getDowntimeDuration());
			 issuedServiceDetail.setFromTime(customerDownTimeBean.getFromTime());
			 issuedServiceDetail.setToTime(customerDownTimeBean.getToTime());
			 issuedServiceDetail.setDowntime(flowableDownTime);
			 serviceDetailRepository.save(issuedServiceDetail);
		 }
		
	}

	 @Transactional(readOnly = false)
	public TxConfigurationJeopardyBean txConfigJeopardy(
			TxConfigurationJeopardyBean txConfigurationJeopardyBean) throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(txConfigurationJeopardyBean.getTaskId(),txConfigurationJeopardyBean.getWfTaskId());
		validateInputs(task, txConfigurationJeopardyBean);
		/*
		 * Map<String, String> atMap = new HashMap<>();
		 * atMap.put("rfDataJeopardyRemarks",
		 * provideRfDataJeopardyBean.getRfDataJeopardyRemarks());
		 * componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
		 * AttributeConstants.COMPONENT_LM,task.getSiteType());
		 */
		// flowableBaseService.taskDataEntry(task, txConfigurationJeopardyBean);
		Map<String, Object> map = new HashMap<>();
		map.put("txConfigAction", txConfigurationJeopardyBean.getAction());
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, txConfigurationJeopardyBean.getIsResolved(),
				null, txConfigurationJeopardyBean);
		return (TxConfigurationJeopardyBean) flowableBaseService.taskDataEntry(task, txConfigurationJeopardyBean,map);
	}
	public void terminateService(Integer serviceId){
        if(Objects.nonNull(serviceId)) {
            ServiceDetail serviceDetail = serviceDetailRepository.
                    findFirstByScServiceDetailIdAndServiceStateOrderByVersionDesc(serviceId, TaskStatusConstants.ACTIVE);
            if (Objects.nonNull(serviceDetail)) {
                logger.info("Service detail from service activation : {}", serviceDetail.getId());
                serviceDetail.setState("TERMINATED");
                serviceDetailRepository.save(serviceDetail);
                logger.info("Service detail Terminated succesfully");
            }
        }
    }
	
	@Transactional(readOnly = false)
	public CustomerAcceptanceIssueBean billingIssueUcwb(CustomerAcceptanceIssueBean ucwbBillingIssueBean)throws TclCommonException {
		logger.info("billingIssueucwb for service id: {}", ucwbBillingIssueBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(ucwbBillingIssueBean.getTaskId(), ucwbBillingIssueBean.getWfTaskId());
		validateInputs(task, ucwbBillingIssueBean);

		Map<String, Object> map = new HashMap<>();
		map.put("action", ucwbBillingIssueBean.getAction());

		if (ucwbBillingIssueBean.getDocumentIds() != null && !ucwbBillingIssueBean.getDocumentIds().isEmpty()) {
			logger.info("billingIssueucwb document id exists !!");
			ucwbBillingIssueBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			logger.info("billingIssueucwb document id persisted for overlay");
		}

		if (ucwbBillingIssueBean.getCommissioningDate() != null) {
			String commissioningDate = StringUtils.trimToEmpty(ucwbBillingIssueBean.getCommissioningDate());
			logger.info("ucwb billing-issue=>commissioningDate={}", commissioningDate);
			Optional<ScServiceDetail> scServiceDetailOp = scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetailOp.isPresent()) {
				cramerService.generateBillStartDate(scServiceDetailOp.get(), commissioningDate, null, null);
			}

			if (Objects.nonNull(ucwbBillingIssueBean.getUnderlayBeans())
					&& !ucwbBillingIssueBean.getUnderlayBeans().isEmpty()) {
				logger.info("billingIssueucwb for underlays commissioning date");
				ucwbBillingIssueBean.getUnderlayBeans().stream().forEach(underlay -> {
					Optional<ScServiceDetail> scServiceDetailOpUnderlay = scServiceDetailRepository
							.findById(underlay.getServiceId());

					if (underlay.getCommissioningDate() != null) {
						String commissioningDateUnderlay = StringUtils.trimToEmpty(underlay.getCommissioningDate());
						logger.info("ucwb billing-issue=>commissioningDate for underlay: {},{}",
								commissioningDateUnderlay, underlay.getServiceId());

						if (scServiceDetailOpUnderlay.isPresent()) {
							logger.info("billingIssueucwb sc service exist for underlay: {}",
									scServiceDetailOpUnderlay.get().getId());
							try {
								cramerService.generateBillStartDate(scServiceDetailOpUnderlay.get(),
										commissioningDateUnderlay, null, null);
								logger.info("billingIssueucwb generateBillStartDate completed for underlay");
							} catch (TclCommonException e) {
								e.printStackTrace();
							}
						}
					}
					if (ucwbBillingIssueBean.getDocumentIds() != null
							&& !ucwbBillingIssueBean.getDocumentIds().isEmpty()) {
						ucwbBillingIssueBean.getDocumentIds()
								.forEach(attachmentIdBean -> makeEntryInScAttachment(scServiceDetailOpUnderlay.get(),
										attachmentIdBean.getAttachmentId()));
						logger.info("billingIssueucwb document id persisted for underlay {}", underlay.getServiceId());
					}
				});
			}
		}

		flowableBaseService.taskDataEntry(task, ucwbBillingIssueBean, map);
		if (StringUtils.isNotBlank(ucwbBillingIssueBean.getRemarks()))
			processTaskLogDetails(task, TaskStatusConstants.REMARKS, ucwbBillingIssueBean.getRemarks(), null,
					ucwbBillingIssueBean);

		String message = "";
		if (ucwbBillingIssueBean.getReleasedBy() != null && !ucwbBillingIssueBean.getReleasedBy().isEmpty()) {
			logger.info("billingIssueucwb ReleasedBy is not null");
			if (ucwbBillingIssueBean.getDelayReason() != null && !ucwbBillingIssueBean.getDelayReason().isEmpty()) {
				message = ucwbBillingIssueBean.getReleasedBy() + " and " + ucwbBillingIssueBean.getDelayReason();
			} else {
				message = ucwbBillingIssueBean.getReleasedBy();
			}
		} else {
			message = ucwbBillingIssueBean.getDelayReason();
		}
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, message, null, ucwbBillingIssueBean);
		logger.info("billingIssueucwb persisted");
		return ucwbBillingIssueBean;
	}
	
	@Transactional(readOnly = false)
	public CustomerAcceptanceIssueBean updateServiceIssueTaskDetails(
			CustomerAcceptanceIssueBean customerAcceptanceIssueBean) throws TclCommonException {
		logger.info("Inside updateServiceIssueTaskDetails method for serviceCode {} with TaskBean {}",
				customerAcceptanceIssueBean.getServiceCode(), Utils.convertObjectToJson(customerAcceptanceIssueBean));

		if (customerAcceptanceIssueBean == null || customerAcceptanceIssueBean.getTaskId() == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		Task task = taskRepository.findById(customerAcceptanceIssueBean.getTaskId()).orElse(null);
		if (task == null) {
			throw new TclCommonException(ExceptionConstants.TASK_NOT_FOUND, ResponseResource.R_CODE_BAD_REQUEST);
		}

		if (customerAcceptanceIssueBean.getCommissioningDate() != null) {
			String taskKey = task.getMstTaskDef().getKey();
			logger.info("Task key is {}", taskKey);
			String commissioningDate = StringUtils.trimToEmpty(customerAcceptanceIssueBean.getCommissioningDate());
			Optional<ScServiceDetail> scServiceDetailOp = scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetailOp.isPresent()) {
				cramerService.generateBillStartDate(scServiceDetailOp.get(), commissioningDate, null, null);
			}
			if (customerAcceptanceIssueBean.getUnderlayBeans() != null
					&& !customerAcceptanceIssueBean.getUnderlayBeans().isEmpty()) {
				for (UnderlayBean underlayBean : customerAcceptanceIssueBean.getUnderlayBeans()) {

					String commissioningDateUnderlay = StringUtils.trimToEmpty(underlayBean.getCommissioningDate());
					Optional<ScServiceDetail> scServiceDetailOpUnderlay = scServiceDetailRepository
							.findById(underlayBean.getServiceId());
					if (scServiceDetailOpUnderlay.isPresent()) {
						cramerService.generateBillStartDate(scServiceDetailOpUnderlay.get(), commissioningDateUnderlay,
								null, null);
					}

				}
			}
		}
		if (customerAcceptanceIssueBean.getDocumentIds() != null
				&& !customerAcceptanceIssueBean.getDocumentIds().isEmpty()) {
			customerAcceptanceIssueBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		Map<String, Object> map = new HashMap<>();
		map.put("action", customerAcceptanceIssueBean.getAction());
		flowableBaseService.taskDataEntry(task, customerAcceptanceIssueBean, map);
		if (StringUtils.isNotBlank(customerAcceptanceIssueBean.getRemarks())) {
			processTaskLogDetails(task, TaskStatusConstants.REMARKS, customerAcceptanceIssueBean.getRemarks(), null,
					customerAcceptanceIssueBean);
		}
		String message = "";
		if (customerAcceptanceIssueBean.getReleasedBy() != null
				&& !customerAcceptanceIssueBean.getReleasedBy().isEmpty()) {
			logger.info("serviceIssueucwb ReleasedBy is not null");
			if (customerAcceptanceIssueBean.getDelayReason() != null
					&& !customerAcceptanceIssueBean.getDelayReason().isEmpty()) {
				message = customerAcceptanceIssueBean.getReleasedBy() + " and "
						+ customerAcceptanceIssueBean.getDelayReason();
			} else {
				message = customerAcceptanceIssueBean.getReleasedBy();
			}
		} else {
			message = customerAcceptanceIssueBean.getDelayReason();
		}
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, message, null, customerAcceptanceIssueBean);
		logger.info("Exiting updateServiceIssueTaskDetails method for serviceCode : {}",
				customerAcceptanceIssueBean.getServiceCode());
		return customerAcceptanceIssueBean;
	}

	
	@Transactional(readOnly = false)
	public ServiceConfigurationBean saveFailoverTestingDetails(
			ServiceConfigurationBean failoverTestingBean) throws TclCommonException {
		logger.info("Enter saveFailoverTestingDetails for serviceId:{}",failoverTestingBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(failoverTestingBean.getTaskId(), failoverTestingBean.getWfTaskId());
		try {
            Map<String, String> failoverTestingAttributes = new HashMap<>();
            if (failoverTestingBean.getDocumentIds() != null && !failoverTestingBean.getDocumentIds().isEmpty()) {
                logger.info("Document exists::{}", failoverTestingBean.getDocumentIds().size());
                failoverTestingBean.getDocumentIds()
                        .forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
            }

            if (task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-failover-testing-tda")) {
                failoverTestingAttributes.put("failoverTestingTdaStatus", failoverTestingBean.getStatus());
                componentAndAttributeService.updateServiceAttributes(task.getServiceId(), failoverTestingAttributes, "FAILOVER_TESTING_TDA");
                logger.info("Saved failover testing details TDA: {}",failoverTestingBean.getServiceId());
                processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, "FailoverTestingTdaStatus", null, failoverTestingBean);
            } else {
                failoverTestingAttributes.put("failoverTestingStatus", failoverTestingBean.getStatus());
                componentAndAttributeService.updateServiceAttributes(task.getServiceId(), failoverTestingAttributes, "FAILOVER_TESTING");
                logger.info("Saved failover testing details {}",failoverTestingBean.getServiceId());
                processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, "FailoverTesting", null, failoverTestingBean);
            }
		}
		catch (Exception e) {
			logger.error("saveFailoverTestingDetails :{}", e);
		}
		return (ServiceConfigurationBean) flowableBaseService.taskDataEntry(task, failoverTestingBean);
	}
	
	/**
	 * 
	 * This API is the save the cpe config task data for Izosdwan
	 * @param izosdwanCpeConfigBean
	 * @return
	 * @throws TclCommonException
	 */
    @Transactional(readOnly = false)
	public ServiceConfigurationBean saveCpeConfigDetailsIzosdwan(ServiceConfigurationBean izosdwanCpeConfigBean)
			throws TclCommonException {
		logger.info("Enter saveCpeConfigDetailsIzosdwan for serviceId:{}", izosdwanCpeConfigBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(izosdwanCpeConfigBean.getTaskId(), izosdwanCpeConfigBean.getWfTaskId());
		try {
			Map<String, String> cpeConfigAttributes = new HashMap<>();
            cpeConfigAttributes.put("cpeConfigStatus", izosdwanCpeConfigBean.getStatus());
			componentAndAttributeService.updateServiceAttributes(task.getServiceId(), cpeConfigAttributes,
					"CPE_CONFIG");
			if (izosdwanCpeConfigBean.getDocumentIds() != null && !izosdwanCpeConfigBean.getDocumentIds().isEmpty()) {
				logger.info("Document exists::{}", izosdwanCpeConfigBean.getDocumentIds().size());
				izosdwanCpeConfigBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			logger.info("Saved Cpe Config Details {}", izosdwanCpeConfigBean.getServiceId());
		} catch (Exception e) {
			logger.error("saveCpeConfigDetailsIzosdwan :{}", e);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,"CpeConfig",null, izosdwanCpeConfigBean);
		return (ServiceConfigurationBean) flowableBaseService.taskDataEntry(task, izosdwanCpeConfigBean);
	}
	
	/**
	 * 
	 * This API is the save the cpe config tda task data for Izosdwan
	 * @param izosdwanCpeConfigBean
	 * @return
	 * @throws TclCommonException
	 */
    @Transactional(readOnly = false)
	public ServiceConfigurationBean saveCpeConfigTdaDetailsIzosdwan(ServiceConfigurationBean izosdwanCpeConfigBean)
			throws TclCommonException {
		logger.info("Enter saveCpeConfigTdaDetailsIzosdwan for serviceId:{}", izosdwanCpeConfigBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(izosdwanCpeConfigBean.getTaskId(), izosdwanCpeConfigBean.getWfTaskId());
		try {
			Map<String, String> cpeConfigTdaAttributes = new HashMap<>();
            cpeConfigTdaAttributes.put("cpeConfigTdaStatus", izosdwanCpeConfigBean.getStatus());
			componentAndAttributeService.updateServiceAttributes(task.getServiceId(), cpeConfigTdaAttributes,
					"CPE_CONFIG_TDA");
			if (izosdwanCpeConfigBean.getDocumentIds() != null && !izosdwanCpeConfigBean.getDocumentIds().isEmpty()) {
				logger.info("Document exists::{}", izosdwanCpeConfigBean.getDocumentIds().size());
				izosdwanCpeConfigBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			logger.info("Saved Cpe Config Tda Details {}", izosdwanCpeConfigBean.getServiceId());
		} catch (Exception e) {
			logger.error("saveCpeConfigTdaDetailsIzosdwan error :{}", e);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,"CpeConfigTda",null, izosdwanCpeConfigBean);
		return (ServiceConfigurationBean) flowableBaseService.taskDataEntry(task, izosdwanCpeConfigBean);
	}

    @Transactional(readOnly = false)
    public CustomerAcceptanceIssueBean billingIssueSdwan(CustomerAcceptanceIssueBean sdwanBillingIssueBean) throws TclCommonException {
        logger.info("billingIssueSdwan for service id: {}", sdwanBillingIssueBean.getServiceId());
        Task task = getTaskByIdAndWfTaskId(sdwanBillingIssueBean.getTaskId(), sdwanBillingIssueBean.getWfTaskId());
        validateInputs(task, sdwanBillingIssueBean);

        Map<String, Object> map = new HashMap<>();
        map.put("action", sdwanBillingIssueBean.getAction());
        
        if (sdwanBillingIssueBean.getDocumentIds() != null && !sdwanBillingIssueBean.getDocumentIds().isEmpty()) {
            logger.info("billingIssueSdwan document id exists !!");
            sdwanBillingIssueBean.getDocumentIds()
                    .forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
            logger.info("billingIssueSdwan document id persisted for overlay");
        }

        if(sdwanBillingIssueBean.getCommissioningDate()!=null){
            String commissioningDate = StringUtils.trimToEmpty(sdwanBillingIssueBean.getCommissioningDate());
            logger.info("sdwan billing-issue=>commissioningDate={}",commissioningDate);
            Optional<ScServiceDetail> scServiceDetailOp = scServiceDetailRepository.findById(task.getServiceId());
            if(scServiceDetailOp.isPresent()) {
                cramerService.generateBillStartDate(scServiceDetailOp.get(),commissioningDate,null,null);
            }

            if(Objects.nonNull(sdwanBillingIssueBean.getUnderlayBeans()) && !sdwanBillingIssueBean.getUnderlayBeans().isEmpty()){
                logger.info("billingIssueSdwan for underlays commissioning date");
                sdwanBillingIssueBean.getUnderlayBeans().stream().forEach(underlay ->{
                    Optional<ScServiceDetail> scServiceDetailOpUnderlay = scServiceDetailRepository.findById(underlay.getServiceId());

                    if(underlay.getCommissioningDate()!= null){
                        String commissioningDateUnderlay = StringUtils.trimToEmpty(underlay.getCommissioningDate());
                        logger.info("sdwan billing-issue=>commissioningDate for underlay: {},{}",commissioningDateUnderlay,
                                underlay.getServiceId());

                        if(scServiceDetailOpUnderlay.isPresent()) {
                            logger.info("billingIssueSdwan sc service exist for underlay: {}",scServiceDetailOpUnderlay.get().getId());
                            try {
                                cramerService.generateBillStartDate(scServiceDetailOpUnderlay.get(),commissioningDateUnderlay,null,null);
                                logger.info("billingIssueSdwan generateBillStartDate completed for underlay");
                            } catch (TclCommonException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (sdwanBillingIssueBean.getDocumentIds() != null && !sdwanBillingIssueBean.getDocumentIds().isEmpty()) {
                        sdwanBillingIssueBean.getDocumentIds()
                                .forEach(attachmentIdBean -> makeEntryInScAttachment(scServiceDetailOpUnderlay.get(), attachmentIdBean.getAttachmentId()));
                        logger.info("billingIssueSdwan document id persisted for underlay {}", underlay.getServiceId());
                    }
                });
            }
        }

        flowableBaseService.taskDataEntry(task, sdwanBillingIssueBean, map);
        if(StringUtils.isNotBlank(sdwanBillingIssueBean.getRemarks()))
            processTaskLogDetails(task,TaskStatusConstants.REMARKS,sdwanBillingIssueBean.getRemarks(),null, sdwanBillingIssueBean);

        String message="";
        if(sdwanBillingIssueBean.getReleasedBy()!=null && !sdwanBillingIssueBean.getReleasedBy().isEmpty()) {
            logger.info("billingIssueSdwan ReleasedBy is not null");
            if(sdwanBillingIssueBean.getDelayReason()!=null && !sdwanBillingIssueBean.getDelayReason().isEmpty()) {
                message=sdwanBillingIssueBean.getReleasedBy()+" and "+sdwanBillingIssueBean.getDelayReason();
            } else {
                message=sdwanBillingIssueBean.getReleasedBy();
            }
        } else {
            message=sdwanBillingIssueBean.getDelayReason();
        }
        processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,message,null,sdwanBillingIssueBean);
        logger.info("billingIssueSdwan persisted");
        return sdwanBillingIssueBean;
    }
    
    /**
     * 
     *  This API is used to update Raise turnup Issue task data
     *  @author AnandhiV
     * @param customerAcceptanceIssueBean
     * @return
     * @throws TclCommonException
     */
    @Transactional(readOnly = false)
   	public CustomerAcceptanceIssueBean updateTurnupIssueTaskDetails(CustomerAcceptanceIssueBean customerAcceptanceIssueBean)
			throws TclCommonException {
		logger.info("Inside updateTurnupIssueTaskDetails method for serviceCode {} with TaskBean {}",
				customerAcceptanceIssueBean.getServiceCode(), Utils.convertObjectToJson(customerAcceptanceIssueBean));
		if (customerAcceptanceIssueBean == null || customerAcceptanceIssueBean.getTaskId() == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		Task task = taskRepository.findById(customerAcceptanceIssueBean.getTaskId()).orElse(null);
		if (task == null) {
			throw new TclCommonException(ExceptionConstants.TASK_NOT_FOUND, ResponseResource.R_CODE_BAD_REQUEST);
		}
		
		Map<String, String> atMap = new HashMap<>();
		atMap.put("turnupRequested", "Yes");
		atMap.put("turnupCompletedDate", DateUtil.convertDateToString(new Date()));
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		
		if (customerAcceptanceIssueBean.getCommissioningDate()!=null) {
			String taskKey = task.getMstTaskDef().getKey();
			logger.info("Task key is {}", taskKey);
			String commissioningDate = StringUtils.trimToEmpty(customerAcceptanceIssueBean.getCommissioningDate());
			Optional<ScServiceDetail> scServiceDetailOp = scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetailOp.isPresent()) {
				cramerService.generateBillStartDate(scServiceDetailOp.get(), commissioningDate, null, null);
			}
			if (customerAcceptanceIssueBean.getUnderlayBeans() != null
					&& !customerAcceptanceIssueBean.getUnderlayBeans().isEmpty()) {
				for (UnderlayBean underlayBean : customerAcceptanceIssueBean.getUnderlayBeans()) {

					String commissioningDateUnderlay = StringUtils.trimToEmpty(underlayBean.getCommissioningDate());
					Map<String, String> underlayAtMap = new HashMap<>();

					underlayAtMap.put("turnupRequested", "Yes");
					underlayAtMap.put("turnupCompletedDate", DateUtil.convertDateToString(new Date()));
					componentAndAttributeService.updateAttributes(task.getServiceId(), atMap,
							AttributeConstants.COMPONENT_LM, "A");
					Optional<ScServiceDetail> scServiceDetailOpUnderlay = scServiceDetailRepository
							.findById(underlayBean.getServiceId());
					if (scServiceDetailOpUnderlay.isPresent()) {
						cramerService.generateBillStartDate(scServiceDetailOpUnderlay.get(), commissioningDateUnderlay,
								null, null);
					}

				}
			}
		}
		if (customerAcceptanceIssueBean.getDocumentIds() != null
				&& !customerAcceptanceIssueBean.getDocumentIds().isEmpty()) {
			customerAcceptanceIssueBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		Map<String, Object> map = new HashMap<>();
        map.put("action", customerAcceptanceIssueBean.getAction());
        flowableBaseService.taskDataEntry(task, customerAcceptanceIssueBean, map);
        if(StringUtils.isNotBlank(customerAcceptanceIssueBean.getRemarks())){
            processTaskLogDetails(task,TaskStatusConstants.REMARKS,customerAcceptanceIssueBean.getRemarks(),null, customerAcceptanceIssueBean);
        }
        String message="";
        if(customerAcceptanceIssueBean.getReleasedBy()!=null && !customerAcceptanceIssueBean.getReleasedBy().isEmpty()) {
            logger.info("trunUpIssueSdwan ReleasedBy is not null");
            if(customerAcceptanceIssueBean.getDelayReason()!=null && !customerAcceptanceIssueBean.getDelayReason().isEmpty()) {
                message=customerAcceptanceIssueBean.getReleasedBy()+" and "+customerAcceptanceIssueBean.getDelayReason();
            } else {
                message=customerAcceptanceIssueBean.getReleasedBy();
            }
        } else {
            message=customerAcceptanceIssueBean.getDelayReason();
        }
        processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,message,null,customerAcceptanceIssueBean);
		logger.info("Exiting updateTurnupIssueTaskDetails method for serviceCode : {}",
				customerAcceptanceIssueBean.getServiceCode());
		return customerAcceptanceIssueBean;
	}

    @Transactional(readOnly = false)
    public CgwLogsUploadBean cgwLogsUpload(CgwLogsUploadBean cgwLogsUploadBean)
            throws TclCommonException {
        logger.info("cgwLogUpload for serviceId:{}", cgwLogsUploadBean.getServiceId());
        Task task = getTaskByIdAndWfTaskId(cgwLogsUploadBean.getTaskId(),cgwLogsUploadBean.getWfTaskId());
        validateInputs(task, cgwLogsUploadBean);

        if (cgwLogsUploadBean.getCgwLogs() != null && !cgwLogsUploadBean.getCgwLogs().isEmpty()) {
            cgwLogsUploadBean.getCgwLogs()
                    .forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
            logger.info("Cgw Log persisted persisted");
        }

        processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,cgwLogsUploadBean.getDelayReason(),null, cgwLogsUploadBean);
        return (CgwLogsUploadBean) flowableBaseService.taskDataEntry(task, cgwLogsUploadBean);

    }

    @Transactional(readOnly = false)
    public ServiceConfigurationBean saveE2eTestingDetailsSdwan(ServiceConfigurationBean e2eSdwanTesting)
            throws TclCommonException {
        logger.info("saveE2eTestingDetailsSdwan for serviceId:{}", e2eSdwanTesting.getServiceId());
        Task task = getTaskByIdAndWfTaskId(e2eSdwanTesting.getTaskId(),e2eSdwanTesting.getWfTaskId());
        validateInputs(task, e2eSdwanTesting);

        if (e2eSdwanTesting.getDocumentIds() != null && !e2eSdwanTesting.getDocumentIds().isEmpty()) {
            e2eSdwanTesting.getDocumentIds()
                    .forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
            logger.info("Sdwan E2E testing document persisted");
        }
        processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,e2eSdwanTesting.getDelayReason(),null, e2eSdwanTesting);
        return (ServiceConfigurationBean) flowableBaseService.taskDataEntry(task, e2eSdwanTesting);
    }

    @Transactional(readOnly = false)
    public SdwanProvisionDetailsBean saveSdwanProvisionDetails(SdwanProvisionDetailsBean sdwanProvisionDetailsBean)
            throws TclCommonException {
        logger.info("saveSdwanProvisionDetails for serviceId:{}", sdwanProvisionDetailsBean.getServiceId());
        Task task = getTaskByIdAndWfTaskId(sdwanProvisionDetailsBean.getTaskId(),sdwanProvisionDetailsBean.getWfTaskId());
        validateInputs(task, sdwanProvisionDetailsBean);
        ScComponent scComponent = scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(task.getServiceId(),"LM",task.getSiteType());
        Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(sdwanProvisionDetailsBean.getServiceId());
        Map<String, String> atMap = new HashMap<>();
        if(scComponent!=null) {
            logger.info("scComponent exists:: {}", scComponent.getId());
            if (sdwanProvisionDetailsBean.getLinkProvisionStatus() != null) {
                atMap.put("linkProvisionStatus", sdwanProvisionDetailsBean.getLinkProvisionStatus());
                componentAndAttributeService.updateAttributesByScComponent(task.getServiceId(), atMap, scComponent.getId());
            }
            if (sdwanProvisionDetailsBean.getIpAddress() != null) {
                atMap.put("ipAddress", sdwanProvisionDetailsBean.getIpAddress());
                componentAndAttributeService.updateAttributesByScComponent(task.getServiceId(), atMap, scComponent.getId());
            }
        }
        if(sdwanProvisionDetailsBean.getLegacyServiceId() != null && !sdwanProvisionDetailsBean.getLegacyServiceId().isEmpty()){
        	 logger.info("Legacy ServiceId exists:: {}", sdwanProvisionDetailsBean.getLegacyServiceId());
            if (scServiceDetailOpt.isPresent()) {
                ScServiceDetail scServiceDetail = scServiceDetailOpt.get();
                logger.info("Updating tps service id for serviceId::{}", scServiceDetail.getId());
                scServiceDetail.setTpsServiceId(sdwanProvisionDetailsBean.getLegacyServiceId());
                scServiceDetailRepository.saveAndFlush(scServiceDetail);
            }
        }
        processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,sdwanProvisionDetailsBean.getDelayReason(),null, sdwanProvisionDetailsBean);
        return (SdwanProvisionDetailsBean) flowableBaseService.taskDataEntry(task, sdwanProvisionDetailsBean);
    }

    private void triggerSlaveFulfillmentFlow(String serviceCode)throws  TclCommonException{
        logger.info("inside triggerSlaveServiceFlow master vrf service code : {}",serviceCode);
        ScServiceDetail scServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
        if(scServiceDetail!=null && scServiceDetail.getMultiVrfSolution()!=null && CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getMultiVrfSolution()) && CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getIsMultiVrf())){
            logger.info("master Vrf id : {}",scServiceDetail.getId());
            mqUtils.send(slaveFulfillmentTrigger,serviceCode);
        }
    }

	public String getSsIpDetails(String serviceCode,String bsIp) {
		logger.info("Inside getSsIpDetails invoked for serviceCode:{} with bsIp: {}",serviceCode,bsIp);
		String ssIp="0.0.0.0";
		if(bsIp!=null) {
			MstBsIPSsIPMapping mstBsSsIpMapping=mstBsIPSsIPMappingRepository.findFirstByBsIp(bsIp.trim());
			if(mstBsSsIpMapping!=null && mstBsSsIpMapping.getSsIp()!=null) {
				logger.info("MstBsIPSsIPMapping exists:{} with ssIp::{} for serviceCode::{}",mstBsSsIpMapping.getId(),mstBsSsIpMapping.getSsIp(),serviceCode);
				String[] ipSplit=getIpAddressSplit(mstBsSsIpMapping.getSsIp().trim());
				if(ipSplit.length>0) {
					logger.info("ipSplit exists for serviceCode::{}",serviceCode);
					Integer ipSplitLastValue=Integer.parseInt(ipSplit[ipSplit.length-1]);
					if(ipSplitLastValue<21) {
						logger.info("ipSplitLastValue less than 21 for serviceCode::{}",serviceCode);
						ssIp=getIpAddress(ipSplit,21);
					}else {
						logger.info("ipSplitLastValue not less than 21 for serviceCode::{}",serviceCode);
						ssIp=getIpAddress(ipSplit,1);
						String[] ssIpSplit=getIpAddressSplit(ssIp);
						if(ssIpSplit.length>0) {
							logger.info("ssIpSplit exists for serviceCode::{}",serviceCode);
							Integer ssIpSplitLastValue=Integer.parseInt(ssIpSplit[ssIpSplit.length-1]);
							if(ssIpSplitLastValue==256) {
								logger.info("ssIpSplitLastValue equals to 256 exists for serviceCode::{}",serviceCode);
								logger.info("Inside getSsIpDetails invoked for serviceCode:{} with bsIp: {}",serviceCode,bsIp);
								return "0.0.0.0";
							}
						}
					}
				}
			}
		}
		return ssIp;
	}
    
	@Transactional(readOnly = false)
	public void updateSsIpDetails(String serviceCode,String bsIp,String ssIp) {
		logger.info("Inside updateSsIpDetails invoked for serviceCode:{} with bsIp: {} and ssIp::{}",serviceCode,bsIp,ssIp);
		if(bsIp!=null && ssIp!=null && !ssIp.equalsIgnoreCase("0.0.0.0")) {
			logger.info("BsIp exists for service code::{}",serviceCode);
			MstBsIPSsIPMapping mstBsSsIpMapping=mstBsIPSsIPMappingRepository.findFirstByBsIp(bsIp.trim());
			if(mstBsSsIpMapping!=null && mstBsSsIpMapping.getSsIp()!=null) {
				logger.info("updateSsIpDetails.MstBsIPSsIPMapping exists:{} with ssIp::{} for serviceCode::{}",mstBsSsIpMapping.getId(),mstBsSsIpMapping.getSsIp(),serviceCode);
				mstBsSsIpMapping.setSsIp(ssIp);
				mstBsSsIpMapping.setUpdatedBy("OPTIMUS_O2C");
				mstBsSsIpMapping.setUpdatedDate(new Timestamp(new Date().getTime()));
				mstBsSsIpMapping.setServiceCode(serviceCode);
				mstBsIPSsIPMappingRepository.saveAndFlush(mstBsSsIpMapping);
			}
		}
	}
	
	
	public  String[] getIpAddressSplit(String ipAddress){
		logger.info("Inside getIpAddressSplit invoked : {}",ipAddress);
		String array[]=ipAddress.split("/");
		String ssMgmtIp=array[0];
	    String[] splitValue = ssMgmtIp.split("\\.");
	    return splitValue;
	}
	
	public  String getIpAddress(String[] ipSplitValue,int count){
		logger.info("Inside getIpAddress invoked with length: {}",ipSplitValue.length);
		ipSplitValue[ipSplitValue.length-1] = String.valueOf(Integer.parseInt(ipSplitValue[ipSplitValue.length-1])+count);
	    String output = Arrays.asList(ipSplitValue).stream().map(eachVal -> eachVal.toString()).collect(Collectors.joining("."));
	    return output;
	}
    
    @Transactional(readOnly = false)
    public WanIpDetailsBean saveServiceDetails(WanIpDetailsBean wanIpDetailsBean) throws TclCommonException {
    	logger.info("saveServiceDetails for Izopc : {}",wanIpDetailsBean.getTaskId());
        Task task = getTaskByIdAndWfTaskId(wanIpDetailsBean.getTaskId(), wanIpDetailsBean.getWfTaskId());
        Map<String, Object> map = new HashMap<>();
        map.put("action", wanIpDetailsBean.getAction());
		Map<String, String> componentMap = new HashMap<>();
		if(wanIpDetailsBean.getWanIpAddress()!=null && !wanIpDetailsBean.getWanIpAddress().isEmpty()) {
			componentMap.put("wanIpAddress", wanIpDetailsBean.getWanIpAddress());
		}
		if(wanIpDetailsBean.getSecondaryWanIpAddress()!=null && !wanIpDetailsBean.getSecondaryWanIpAddress().isEmpty()) {
			componentMap.put("secondaryWanIpAddress", wanIpDetailsBean.getSecondaryWanIpAddress());
		}
		if(wanIpDetailsBean.getVsnlWanIpAddress()!=null && !wanIpDetailsBean.getVsnlWanIpAddress().isEmpty()) {
			componentMap.put("vsnlWanIpAddress", wanIpDetailsBean.getVsnlWanIpAddress());
		}
		if(wanIpDetailsBean.getSecondaryVsnlWanIpAddress()!=null && !wanIpDetailsBean.getSecondaryVsnlWanIpAddress().isEmpty()) {
			componentMap.put("secondaryVsnlWanIpAddress", wanIpDetailsBean.getSecondaryVsnlWanIpAddress());
		}
		if(wanIpDetailsBean.getWanIpPool()!=null && !wanIpDetailsBean.getWanIpPool().isEmpty()) {
			componentMap.put("wanIpPool", wanIpDetailsBean.getWanIpPool());
		}
		if(wanIpDetailsBean.getSecondaryWanIpPool()!=null && !wanIpDetailsBean.getSecondaryWanIpPool().isEmpty()) {
			componentMap.put("secondaryWanIpPool", wanIpDetailsBean.getSecondaryWanIpPool());
		}
		if(wanIpDetailsBean.getPublicNATIp()!=null && !wanIpDetailsBean.getPublicNATIp().isEmpty()) {
			componentMap.put("publicNATIp", wanIpDetailsBean.getPublicNATIp());
		}
		 logger.info("Service Id::{} with componentMap : {}",task.getServiceId(),componentMap);
		componentAndAttributeService.updateAttributes(task.getServiceId(), componentMap,
				AttributeConstants.COMPONENT_LM, task.getSiteType());
        flowableBaseService.taskDataEntry(task, wanIpDetailsBean, map);
        if(StringUtils.isNotBlank(wanIpDetailsBean.getRemarks()))processTaskLogDetails(task,TaskStatusConstants.REMARKS,wanIpDetailsBean.getRemarks(),null, wanIpDetailsBean);
        String message="";
        if(wanIpDetailsBean.getReleasedBy()!=null && !wanIpDetailsBean.getReleasedBy().isEmpty()) {
        	if(wanIpDetailsBean.getDelayReason()!=null && !wanIpDetailsBean.getDelayReason().isEmpty()) {
        		message=wanIpDetailsBean.getReleasedBy()+" and "+wanIpDetailsBean.getDelayReason();
        	} else {
        		message=wanIpDetailsBean.getReleasedBy();
        	}
        } else {
        		message=wanIpDetailsBean.getDelayReason();
        }
        serviceActivationService.saveServiceDetails(task.getServiceCode(),task.getOrderCode(),task.getServiceId());
        processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,message,null,wanIpDetailsBean);
        return wanIpDetailsBean;
    }
    
    
	/**
	 * 
	 * This API is the save the  cloud configuration task data for izopc
	 * @param serviceConfigurationBean
	 * @return
	 * @throws TclCommonException
	 */
    @Transactional(readOnly = false)
	public ServiceConfigurationBean saveCloudConfiguration(ServiceConfigurationBean serviceConfigurationBean)
			throws TclCommonException {
		logger.info("Enter saveCloudConfiguration for serviceId:{}", serviceConfigurationBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(serviceConfigurationBean.getTaskId(), serviceConfigurationBean.getWfTaskId());
		try {
			if (serviceConfigurationBean.getDocumentIds() != null && !serviceConfigurationBean.getDocumentIds().isEmpty()) {
				logger.info("Document exists::{} for taskId::{}", serviceConfigurationBean.getDocumentIds().size(),task.getId());
				serviceConfigurationBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			logger.info("Saved Cloud Configuration Details {}", serviceConfigurationBean.getServiceId());
		} catch (Exception e) {
			logger.error("saveCloudConfiguration :{}", e);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,"Config",null, serviceConfigurationBean);
		return (ServiceConfigurationBean) flowableBaseService.taskDataEntry(task, serviceConfigurationBean);
	}
    
    /**
	 * \
	 * This API is the save the  customer cloud configuration task data for izopc
	 * @param customerCloudConfigurationBean
	 * @return
	 * @throws TclCommonException
	 */
    @Transactional(readOnly = false)
	public CustomerCloudConfigurationBean saveCustomerCloudConfiguration(CustomerCloudConfigurationBean customerCloudConfigurationBean)
			throws TclCommonException {
		logger.info("Enter saveCustomerCloudConfiguration for serviceId:{}", customerCloudConfigurationBean.getServiceId());
		Task task = getTaskByIdAndWfTaskId(customerCloudConfigurationBean.getTaskId(), customerCloudConfigurationBean.getWfTaskId());
		try {
			if (customerCloudConfigurationBean.getDocumentIds() != null && !customerCloudConfigurationBean.getDocumentIds().isEmpty()) {
				logger.info("Document exists::{} for taskId::{}", customerCloudConfigurationBean.getDocumentIds().size(),task.getId());
				customerCloudConfigurationBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			Map<String, String> componentMap = new HashMap<>();
			if(customerCloudConfigurationBean.getRemarks()!=null && !customerCloudConfigurationBean.getRemarks().isEmpty()) {
				logger.info("Remarks exists::{} for taskId::{}", customerCloudConfigurationBean.getRemarks(),task.getId());
				componentMap.put("customerCloudConfigRemarks", customerCloudConfigurationBean.getRemarks());
				componentAndAttributeService.updateAttributes(task.getServiceId(), componentMap,
						AttributeConstants.COMPONENT_LM, task.getSiteType());
			}
			logger.info("Saved Customer Cloud Configuration Details {}", customerCloudConfigurationBean.getServiceId());
		} catch (Exception e) {
			logger.error("saveCustomerCloudConfiguration :{}", e);
		}
		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,"Config",null, customerCloudConfigurationBean);
		return (CustomerCloudConfigurationBean) flowableBaseService.taskDataEntry(task, customerCloudConfigurationBean);
	}
    
    @Transactional(readOnly = false)
    public void refreshNetpRefId(Integer serviceId){
    	  logger.info("refreshNetpRefId method invoked for serviceId : {}", serviceId);
		try {
			if (Objects.nonNull(serviceId)) {
				ServiceDetail serviceDetail = serviceDetailRepository
						.findFirstByScServiceDetailIdAndServiceStateOrderByVersionDesc(serviceId,
								TaskStatusConstants.ACTIVE);
				if (Objects.nonNull(serviceDetail)) {
					String ftiServiceCode = serviceDetail.getServiceId();
					logger.info("Service detail from service activation : {}", serviceDetail.getId());
					Map<String, String> ftiDetails = serviceActivationService.getFtiDetails(ftiServiceCode,
							"SIVXVPNAccessPath");
					if (CollectionUtils.isEmpty(ftiDetails)) {
						logger.info("No Data from FTI for service Code {}. Retrying with different Access Path.",
								ftiServiceCode);
						ftiDetails = serviceActivationService.getFtiDetails(ftiServiceCode, "SIApIPPath");
						if (CollectionUtils.isEmpty(ftiDetails)) {
							logger.info("No Data from FTI for service Code {} with SIApIPPath", ftiServiceCode);
						}
					}
					// rfu5 value
					if (ftiDetails.containsKey("rfu5") && ftiDetails.get("rfu5") != null
							&& !ftiDetails.get("rfu5").isEmpty()) {
						serviceDetail.setNetpRefid(ftiDetails.get("rfu5"));
						logger.info("service code {} has rfu5 value {} from fti and it is updated", ftiServiceCode,
								ftiDetails.get("rfu5"));
						serviceDetail.setModifiedBy("FTI");
						serviceDetail.setLastModifiedDate(new Timestamp(new Date().getTime()));
						serviceDetailRepository.saveAndFlush(serviceDetail);
					}
				}
			}
		}catch(Exception e) {
			logger.error("Exception occured in refreshNetpRefId method::{}", e);
    	 }
    }
}
