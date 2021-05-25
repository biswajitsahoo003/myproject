package com.tcl.dias.servicefulfillment.service.teamsdr;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.teamsdr.*;
import com.tcl.dias.servicefulfillment.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.GscFlowGroupRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.SetCLRSyncBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TeamsDRFulfillmentConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TeamsDRService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants.*;

/**
 * Service class for teams DR implementations
 *
 * @author Srinivasa Raghavan
 */
@Service
public class TeamsDRImplementationService extends ServiceFulfillmentBaseService {

	Logger LOGGER = LoggerFactory.getLogger(TeamsDRImplementationService.class);

	@Autowired
	private ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ProcessTaskLogRepository processTaskLogRepository;

	@Autowired
	private UserInfoUtils userInfoUtils;

	@Autowired
	private FlowableBaseService flowableBaseService;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	TeamsDRService teamsDRService;
	
	@Autowired
	GscFlowGroupRepository gscFlowGroupRepository;

	@Value("${queue.setservice.active}")
	String setServiceActiveQueue;

	@Autowired
	MQUtils mqUtils;

	@Transactional(readOnly = false)
	protected ProcessTaskLog createProcessTaskLog(Task task, String action, String description, String actionTo,
			BaseRequest baseRequest) {
		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setTask(task);
		processTaskLog.setGroupFrom(task.getMstTaskDef().getAssignedGroup());
		processTaskLog.setOrderCode(task.getOrderCode());
		processTaskLog.setScOrderId(task.getScOrderId());
		processTaskLog.setServiceId(task.getServiceId());
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			processTaskLog.setActionFrom(userInfoUtils.getUserInformation().getUserId());
		}
		processTaskLog.setActionTo(actionTo);
		processTaskLog.setServiceCode(task.getServiceCode());
		if (action.equals("CLOSED"))
			action = TaskLogConstants.CLOSED;
		processTaskLog.setAction(action);
		processTaskLog.setQuoteCode(task.getQuoteCode());
		processTaskLog.setQuoteId(task.getQuoteId());
		processTaskLog.setDescrption(description);
		if (baseRequest != null) {
			processTaskLog.setCategory(baseRequest.getDelayReasonCategory());
			processTaskLog.setSubCategory(baseRequest.getDelayReasonSubCategory());
		}

		return processTaskLog;

	}

	/**
	 * Get PR/PO trigger date
	 *
	 * @param prpoTriggerDate
	 * @return
	 * @throws TclCommonException
	 */
	private Object getPrPoTriggeredDate(String prpoTriggerDate) throws TclCommonException {
		try {
			LOGGER.info("effective date for prpoTriggerDate{}::", prpoTriggerDate);
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			LocalDateTime inputLocalTime = LocalDateTime
					.ofInstant(inputDateFormatter.parse(prpoTriggerDate).toInstant(), ZoneId.of("UTC"));
			LocalDateTime currentLocalTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("UTC"))
					.plusMinutes(15);
			LOGGER.info("Current Local Time :{}", currentLocalTime);
			LOGGER.info("Input Local Time :{}", inputLocalTime);
			if (inputLocalTime.isAfter(currentLocalTime)) {
				prpoTriggerDate = inputLocalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			} else {
				prpoTriggerDate = currentLocalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			}
			LOGGER.info("Derived effective date for prpoTriggerDate::{}", prpoTriggerDate);
			return prpoTriggerDate;
		} catch (Exception ex) {
			LOGGER.error("Exception for getPrPoTriggeredDate:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * Provide WBSGLCC details for MG
	 *
	 * @param provideWbsglccDetailBean
	 * @param type
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvideWbsglccTeamsDRDetailBean provideWbsglccDetails(
			ProvideWbsglccTeamsDRDetailBean provideWbsglccDetailBean, String type) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(provideWbsglccDetailBean.getTaskId(),
				provideWbsglccDetailBean.getWfTaskId());
		validateInputs(task, provideWbsglccDetailBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("level5Wbs", provideWbsglccDetailBean.getLevel5Wbs());
		atMap.put("demandIdNo", provideWbsglccDetailBean.getDemandIdNo());
		atMap.put("glCode", provideWbsglccDetailBean.getGlCode());
		atMap.put("costCenter", provideWbsglccDetailBean.getCostCenter());
		atMap.put("supportDemandIdNo", provideWbsglccDetailBean.getSupportDemandIdNo());
		atMap.put("supportGlCode", provideWbsglccDetailBean.getSupportGlCode());
		atMap.put("supportCostCenter", provideWbsglccDetailBean.getSupportCostCenter());
		atMap.put("licenceDemandIdNo", provideWbsglccDetailBean.getLicenceDemandIdNo());
		atMap.put("licenceGlCode", provideWbsglccDetailBean.getLicenceGlCode());
		atMap.put("licenceCostCenter", provideWbsglccDetailBean.getLicenceCostCenter());

		String endpointDeliveryRequired = provideWbsglccDetailBean.getEndpointDeliveryRequired();
		atMap.put("endpointDeliveryRequired", endpointDeliveryRequired);
		Map<String, Object> flowableMap = new HashMap<>();

		if (type.equalsIgnoreCase("endpoint")) {
			componentAndAttributeService
					.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());
		}

		if (endpointDeliveryRequired.equalsIgnoreCase("no")) {
			flowableMap.put("cpeSiScope", "");
			atMap.put("endpointDeliveryRequiredRejectionReason",
					provideWbsglccDetailBean.getEndpointDeliveryRequiredRejectionReason());
			flowableMap.put("isCPEArrangedByCustomer", true);
		}
		if (task.getOrderCode().toLowerCase().startsWith(CommonConstants.UCDR)) {
			LOGGER.info("Provide Wbs for TeamsDR Order::{}", task.getServiceId());
			if (provideWbsglccDetailBean.getEndpointRequiredDate() != null) {
				LOGGER.info("Cpe Required Date::{}", provideWbsglccDetailBean.getEndpointRequiredDate());
				atMap.put("cpeBillStartDate", provideWbsglccDetailBean.getEndpointRequiredDate().concat(" 00:00"));
			}
			if (provideWbsglccDetailBean.getVendorPORaisedDate() != null) {
				LOGGER.info("Vendor PO RaisedDate::{}", provideWbsglccDetailBean.getVendorPORaisedDate());
				atMap.put("vendorPORaisedDate", provideWbsglccDetailBean.getVendorPORaisedDate());
				flowableMap.put("prPoTriggerDate",
						getPrPoTriggeredDate(provideWbsglccDetailBean.getVendorPORaisedDate().concat(" 09:00")));
			}
		}

		processTaskLogDetails(task, "CLOSED", provideWbsglccDetailBean.getDelayReason(), null,
				provideWbsglccDetailBean);
		return (ProvideWbsglccTeamsDRDetailBean) flowableBaseService
				.taskDataEntry(task, provideWbsglccDetailBean, flowableMap);
	}

	/**
	 * Provide PR for media gateway
	 *
	 * @param prForTeamsDRMediaGatewayBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvidePrForTeamsDREndpointBean providePrForTeamsDRMediaGateway(
			ProvidePrForTeamsDREndpointBean prForTeamsDRMediaGatewayBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(prForTeamsDRMediaGatewayBean.getTaskId(),
				prForTeamsDRMediaGatewayBean.getWfTaskId());

		validateInputs(task, prForTeamsDRMediaGatewayBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(prForTeamsDRMediaGatewayBean.getTeamsdrEndpointPrNumber()) && Objects
				.nonNull(prForTeamsDRMediaGatewayBean.getTeamsdrEndpointPrDate()) && Objects
				.nonNull(prForTeamsDRMediaGatewayBean.getTeamsdrEndpointPrVendorName())) {
			atMap.put("teamsdrEndpointPrNumber", prForTeamsDRMediaGatewayBean.getTeamsdrEndpointPrNumber());
			atMap.put("teamsdrEndpointPrDate", prForTeamsDRMediaGatewayBean.getTeamsdrEndpointPrDate());
			atMap.put("teamsdrEndpointPrVendorName", prForTeamsDRMediaGatewayBean.getTeamsdrEndpointPrVendorName());
		}

		componentAndAttributeService
				.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		if (prForTeamsDRMediaGatewayBean.getDocumentIds() != null && !prForTeamsDRMediaGatewayBean.getDocumentIds()
				.isEmpty())
			prForTeamsDRMediaGatewayBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", prForTeamsDRMediaGatewayBean.getDelayReason(), null,
				prForTeamsDRMediaGatewayBean);
		return (ProvidePrForTeamsDREndpointBean) flowableBaseService.taskDataEntry(task, prForTeamsDRMediaGatewayBean);
	}

	/**
	 * Prepare PR for media gateway install
	 *
	 * @param prForEndpoint
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public PreparePrForTeamsDREndpointBean preparePrForMediaGatewayInstall(
			PreparePrForTeamsDREndpointBean prForEndpoint) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(prForEndpoint.getTaskId(), prForEndpoint.getWfTaskId());

		validateInputs(task, prForEndpoint);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(prForEndpoint)) {
			atMap.put("endpointInstallationPrNumber", prForEndpoint.getEndpointInstallationPrNumber());
			atMap.put("endpointInstallationPrVendorName", prForEndpoint.getEndpointInstallationPrVendorName());
			atMap.put("endpointInstallationPrDate", prForEndpoint.getEndpointInstallationPrDate());
			atMap.put("endpointSupportPrNumber", prForEndpoint.getEndpointSupportPrNumber());
			atMap.put("endpointSupportPrVendorName", prForEndpoint.getEndpointSupportPrVendorName());
			atMap.put("endpointSupportPrDate", prForEndpoint.getEndpointSupportPrDate());
		}

		componentAndAttributeService
				.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		if (prForEndpoint.getDocumentIds() != null && !prForEndpoint.getDocumentIds().isEmpty())
			prForEndpoint.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", prForEndpoint.getDelayReason(), null, prForEndpoint);
		return (PreparePrForTeamsDREndpointBean) flowableBaseService.taskDataEntry(task, prForEndpoint);
	}

	/**
	 * To provide PO for TeamsDR media gateway
	 *
	 * @param poForTeamsDREndpoint
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvidePoForTeamsDREndpointBean providePoForMediaGateway(
			ProvidePoForTeamsDREndpointBean poForTeamsDREndpoint) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poForTeamsDREndpoint.getTaskId(), poForTeamsDREndpoint.getWfTaskId());

		validateInputs(task, poForTeamsDREndpoint);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poForTeamsDREndpoint.getTeamsDrEndpointPoNumber()) && Objects
				.nonNull(poForTeamsDREndpoint.getTeamsDrEndpointPoDate())) {
			atMap.put("teamsdrEndpointPoNumber", poForTeamsDREndpoint.getTeamsDrEndpointPoNumber());
			atMap.put("teamsdrEndpointPoDate", poForTeamsDREndpoint.getTeamsDrEndpointPoDate());
		}

		componentAndAttributeService
				.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		if (poForTeamsDREndpoint.getDocumentIds() != null && !poForTeamsDREndpoint.getDocumentIds().isEmpty())
			poForTeamsDREndpoint.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", poForTeamsDREndpoint.getDelayReason(), null, poForTeamsDREndpoint);
		return (ProvidePoForTeamsDREndpointBean) flowableBaseService.taskDataEntry(task, poForTeamsDREndpoint);
	}

	/**
	 * Create PO for media gateway install support
	 *
	 * @param poForEndpoint
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public CreatePoForEndpointBean createPoForEndpointInstall(CreatePoForEndpointBean poForEndpoint)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poForEndpoint.getTaskId(), poForEndpoint.getWfTaskId());

		validateInputs(task, poForEndpoint);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poForEndpoint)) {
			atMap.put("endpointInstallationPoNumber", poForEndpoint.getEndpointInstallationPoNumber());
			atMap.put("endpointInstallationPoDate", poForEndpoint.getEndpointInstallationPoDate());
			atMap.put("endpointSupportPoNumber", poForEndpoint.getEndpointSupportPoNumber());
			atMap.put("endpointSupportPoDate", poForEndpoint.getEndpointSupportPoDate());
		}

		componentAndAttributeService
				.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		if (poForEndpoint.getDocumentIds() != null && !poForEndpoint.getDocumentIds().isEmpty())
			poForEndpoint.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", poForEndpoint.getDelayReason(), null, poForEndpoint);
		return (CreatePoForEndpointBean) flowableBaseService.taskDataEntry(task, poForEndpoint);
	}

	/**
	 * To provide release for media gateway
	 *
	 * @param poReleaseForTeamsDREndpoint
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvidePoReleaseForTeamsDREndpointBean providePoReleaseForMediaGateway(
			ProvidePoReleaseForTeamsDREndpointBean poReleaseForTeamsDREndpoint) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poReleaseForTeamsDREndpoint.getTaskId(),
				poReleaseForTeamsDREndpoint.getWfTaskId());

		validateInputs(task, poReleaseForTeamsDREndpoint);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poReleaseForTeamsDREndpoint.getTeamsDrEndpointPoRelease())) {
			atMap.put("teamsdrEndpointPoRelease", poReleaseForTeamsDREndpoint.getTeamsDrEndpointPoRelease());
			atMap.put("endpointPoReleaseCompletionDate",
					poReleaseForTeamsDREndpoint.getEndpointPoReleaseCompletionDate());
			if (poReleaseForTeamsDREndpoint.getTeamsDrEndpointPoRelease().equalsIgnoreCase("yes")) {
				atMap.put("teamsdrEndpointPoStatus", "PO Released");
			}
		}

		componentAndAttributeService
				.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		processTaskLogDetails(task, "CLOSED", poReleaseForTeamsDREndpoint.getDelayReason(), null,
				poReleaseForTeamsDREndpoint);
		return (ProvidePoReleaseForTeamsDREndpointBean) flowableBaseService
				.taskDataEntry(task, poReleaseForTeamsDREndpoint);
	}

	/**
	 * To release PO for media gateway
	 *
	 * @param poForEndpointBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ReleasePoForEndpointBean releasePoForEndpointInstall(ReleasePoForEndpointBean poForEndpointBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(poForEndpointBean.getTaskId(), poForEndpointBean.getWfTaskId());

		validateInputs(task, poForEndpointBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(poForEndpointBean)) {
			atMap.put("poReleaseEndpointInstall", poForEndpointBean.getPoReleaseEndpointInstall());
			if (poForEndpointBean.getPoReleaseEndpointInstall().equalsIgnoreCase("yes")) {
				atMap.put("teamsdrEndpointInstallPoStatus", "PO Released");
				atMap.put("endpointInstallPoReleaseCompletionDate",
						poForEndpointBean.getEndpointInstallPoReleaseCompletionDate());
			}
			atMap.put("poReleaseEndpointSupport", poForEndpointBean.getPoReleaseEndpointSupport());
			if (poForEndpointBean.getPoReleaseEndpointSupport().equalsIgnoreCase("yes")) {
				atMap.put("teamsdrEndpointSupportPoStatus", "PO Released");
				atMap.put("endpointSupportPoReleaseCompletionDate",
						poForEndpointBean.getEndpointSupportPoReleaseCompletionDate());
			}
			atMap.put("endpointInstallPoReleaseCompletionDate",
					poForEndpointBean.getEndpointInstallPoReleaseCompletionDate());
			atMap.put("endpointSupportPoReleaseCompletionDate",
					poForEndpointBean.getEndpointSupportPoReleaseCompletionDate());
		}
		componentAndAttributeService
				.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		processTaskLogDetails(task, "CLOSED", poForEndpointBean.getDelayReason(), null, poForEndpointBean);
		return (ReleasePoForEndpointBean) flowableBaseService.taskDataEntry(task, poForEndpointBean);
	}

	/**
	 * Confirm material availability media gateway
	 *
	 * @param materialAvailability
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ConfirmMaterialAvailabilityBean confirmMaterialAvailability(
			ConfirmMaterialAvailabilityBean materialAvailability) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(materialAvailability.getTaskId(), materialAvailability.getWfTaskId());

		validateInputs(task, materialAvailability);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(materialAvailability)) {
			atMap.put("expectedETADate", materialAvailability.getExpectedETADate());
			atMap.put("grnNumber", materialAvailability.getGrnNumber());
			atMap.put("materialReceived", materialAvailability.getMaterialReceived());
			atMap.put("materialReceivedDate", materialAvailability.getMaterialReceivedDate());
			atMap.put("action", materialAvailability.getAction());
			if (materialAvailability.getSerialNumber() != null) {
				materialAvailability.getSerialNumber().stream().forEach(serialNumberBean -> {
					Map<String, String> atMap1 = new HashMap<>();
					atMap1.put("serialNumber", serialNumberBean.getSerialNumber());
					LOGGER.info("serialNumberId: {}, ATMAPvalue: {}", serialNumberBean.getSerialNumber(),
							serialNumberBean.getId());
					componentAndAttributeService
							.updateAttributesEndpoint(task.getServiceId(), serialNumberBean.getId(), atMap1,
									task.getSiteType());
				});
			}
		}
		componentAndAttributeService
				.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());
		processTaskLogDetails(task, "CLOSED", materialAvailability.getDelayReason(), null, materialAvailability);
		return (ConfirmMaterialAvailabilityBean) flowableBaseService.taskDataEntry(task, materialAvailability);
	}

	/**
	 * Method to dispatch endpoint for teamsdr.
	 * @param dispatchEndpointBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public DispatchEndpointBean dispatchEndpoint(DispatchEndpointBean dispatchEndpointBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(dispatchEndpointBean.getTaskId(), dispatchEndpointBean.getWfTaskId());

		validateInputs(task, dispatchEndpointBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(dispatchEndpointBean)) {
			atMap.put(ENDPOINT_MRN_NO, dispatchEndpointBean.getEndpointMrnNumber());
			atMap.put(ENDPOINT_MIN_NO, dispatchEndpointBean.getEndpointMinNumber());
			atMap.put(COURIER_DISPATCH_VENDOR_NAME, dispatchEndpointBean.getCourierDispatchVendorName());
			atMap.put(COURIER_TRACK_NUMBER, dispatchEndpointBean.getCourierTrackNumber());
			atMap.put(ENDPOINT_DISPATCH_DATE, dispatchEndpointBean.getEndpointDispatchDate());
			atMap.put(DISTRIBUTION_CENTER_NAME, dispatchEndpointBean.getDistributionCenterName());
			atMap.put(DISTRIBUTION_CENTER_ADDRESS, dispatchEndpointBean.getDistributionCenterAddress());
			atMap.put(ENDPOINT_SERIAL_NUMBER, dispatchEndpointBean.getEndpointSerialNumber());
			if (Objects.nonNull(dispatchEndpointBean.getSerialNumber())) {
				dispatchEndpointBean.getSerialNumber().stream().forEach(serialNumberBean -> {
					Map<String, String> atMap1 = new HashMap<>();
					atMap1.put(ENDPOINT_DELIVERY_DATE, serialNumberBean.getDeliveryDate());
					atMap1.put(ENDPOINT_END_OF_SALE, serialNumberBean.getEndOfSale());
					atMap1.put(ENDPOINT_END_OF_LIFE, serialNumberBean.getEndOfLife());
					componentAndAttributeService.updateAttributesEndpoint(task.getServiceId(), serialNumberBean.getId(),
							atMap1, task.getSiteType());
				});
			}
			if (dispatchEndpointBean.getDocumentIds() != null && !dispatchEndpointBean.getDocumentIds().isEmpty())
				dispatchEndpointBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		processTaskLogDetails(task, CLOSED, dispatchEndpointBean.getDelayReason(), null, dispatchEndpointBean);
		return (DispatchEndpointBean) flowableBaseService.taskDataEntry(task, dispatchEndpointBean);
	}

	/**
	 * Method to track endpoint for teamsdr.
	 * @param trackEndpointBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public TrackEndpointBean trackEndpoint(TrackEndpointBean trackEndpointBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(trackEndpointBean.getTaskId(), trackEndpointBean.getWfTaskId());

		validateInputs(task, trackEndpointBean);

		Map<String, String> atMap = new HashMap<>();

		atMap.put(DELIVERY_STATUS, trackEndpointBean.getDeliveryStatus());
		atMap.put(ENDPOINT_DELIVERY_DATE, trackEndpointBean.getEndpointDeliveryDate());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		processTaskLogDetails(task, CLOSED, trackEndpointBean.getDelayReason(), null, trackEndpointBean);
		return (TrackEndpointBean) flowableBaseService.taskDataEntry(task, trackEndpointBean);
	}

	/**
	 * This method is used to Install Endpoint
	 *
	 * @param teamsDRCommonRequestBean
	 * @return installEndpointBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public TeamsDRCommonRequestBean installEndpoint(TeamsDRCommonRequestBean teamsDRCommonRequestBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(teamsDRCommonRequestBean.getTaskId(),
				teamsDRCommonRequestBean.getWfTaskId());
		validateInputs(task, teamsDRCommonRequestBean);

		Map<String, String> atMap = new HashMap<>();
		atMap.put(AttributeConstants.INSTALLATION_COMPLETED, teamsDRCommonRequestBean.getInstallationCompleted());
		atMap.put(AttributeConstants.INSTALLATION_DATE, teamsDRCommonRequestBean.getInstallationDate());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (Objects.nonNull(teamsDRCommonRequestBean.getMediagateways()) && !teamsDRCommonRequestBean.getMediagateways()
				.isEmpty()) {
			teamsDRCommonRequestBean.getMediagateways().forEach(teamsDRMgRequestBean -> {
				Map<String, String> atMap1 = new HashMap<>();
				atMap1.put(AttributeConstants.GATEWAY_REMOTELY_ACCESSIBLE,
						teamsDRMgRequestBean.getMgRemotelyAccessible());
//				teamsDRMgRequestBean.getSerialNumberBean().forEach(serialNumberBean -> {
//					atMap1.put("endpointDeliveryDate", serialNumberBean.getDeliveryDate());
//					atMap1.put("endpointEndOfSale", serialNumberBean.getEndOfSale());
//					atMap1.put("endpointEndOfLife", serialNumberBean.getEndOfLife());
//					atMap1.put("endpointAmcStartDate", serialNumberBean.getAmcStartDate());
//					atMap1.put("endpointAmcEndDate", serialNumberBean.getAmcEndDate());
//				});
				componentAndAttributeService.updateAttributesEndpoint(task.getServiceId(),
						teamsDRMgRequestBean.getMgId(), atMap1, task.getSiteType());
			});
		}

		processTaskLogDetails(task, CLOSED, teamsDRCommonRequestBean.getDelayReason(), null, teamsDRCommonRequestBean);
		return (TeamsDRCommonRequestBean) flowableBaseService.taskDataEntry(task, teamsDRCommonRequestBean);
	}

	/**
	 * Advanced enrichment for media gateway
	 *
	 * @param advancedEnrichmentMgBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public AdvancedEnrichmentMediaGatewayBean advancedEnrichmentForMediaGateway(
			AdvancedEnrichmentMediaGatewayBean advancedEnrichmentMgBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(advancedEnrichmentMgBean.getTaskId(),
				advancedEnrichmentMgBean.getWfTaskId());

		validateInputs(task, advancedEnrichmentMgBean);

		Map<String, String> atMap = new HashMap<>();
		advancedEnrichmentMgBean.getAdvancedEnrichmentAttributes().forEach(advancedEnrichmentAttribute -> {
			atMap.put(AttributeConstants.MANAGEMENT_INTERFACE_IP,
					advancedEnrichmentAttribute.getManagementInterfaceIp());
			atMap.put(AttributeConstants.GATEWAY_IP, advancedEnrichmentAttribute.getGatewayIp());
			atMap.put(AttributeConstants.SUBNET_MASK, advancedEnrichmentAttribute.getSubnetMask());
			atMap.put(AttributeConstants.SBC_LOCATED_BEHIND_FIREWALL,
					advancedEnrichmentAttribute.getSbcLocatedBehindFirewall());
			componentAndAttributeService
					.updateAttributes(task.getServiceId(), atMap, advancedEnrichmentAttribute.getMediaGatewayName(),
							task.getSiteType());
		});
		processTaskLogDetails(task, "CLOSED", advancedEnrichmentMgBean.getDelayReason(), null,
				advancedEnrichmentMgBean);
		return (AdvancedEnrichmentMediaGatewayBean) flowableBaseService.taskDataEntry(task, advancedEnrichmentMgBean);
	}

	/**
	 * SRN Generation Media Gateway
	 *
	 * @param srnGenerationEndpointBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public SrnGenerationEndpointBean srnGenerationEndpoint(SrnGenerationEndpointBean srnGenerationEndpointBean)
			throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(srnGenerationEndpointBean.getTaskId(),
				srnGenerationEndpointBean.getWfTaskId());

		validateInputs(task, srnGenerationEndpointBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(srnGenerationEndpointBean)) {
			atMap.put(AttributeConstants.SRN_DATE, srnGenerationEndpointBean.getSrnDate());

		}

		componentAndAttributeService
				.updateAttributes(task.getScServiceDetail().getId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());

		if (srnGenerationEndpointBean.getDocumentIds() != null && !srnGenerationEndpointBean.getDocumentIds().isEmpty())
			srnGenerationEndpointBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", srnGenerationEndpointBean.getDelayReason(), null,
				srnGenerationEndpointBean);
		return (SrnGenerationEndpointBean) flowableBaseService.taskDataEntry(task, srnGenerationEndpointBean);
	}

	/**
	 * Customer Handover Media Gateway
	 *
	 * @param customerHandoverBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public CustomerHandoverBean customerHandoverMediaGateway(CustomerHandoverBean customerHandoverBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(customerHandoverBean.getTaskId(), customerHandoverBean.getWfTaskId());

		validateInputs(task, customerHandoverBean);

		Map<String, String> atMap = new HashMap<>();

		if (Objects.nonNull(customerHandoverBean)) {
			atMap.put(AttributeConstants.CUSTOMER_HANDOVER_COMPLETED,
					customerHandoverBean.getCustomerHandoverCompleted());
			atMap.put(AttributeConstants.CUSTOMER_HANDOVER_COMPLETED_DATE,
					customerHandoverBean.getCustomerHandoverCompletedDate());
		}
		componentAndAttributeService
				.updateAttributes(task.getScServiceDetail().getId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());
		processTaskLogDetails(task, "CLOSED", customerHandoverBean.getDelayReason(), null, customerHandoverBean);
		return (CustomerHandoverBean) flowableBaseService.taskDataEntry(task, customerHandoverBean);
	}

	/**
	 * Method to save docs and configure endpoint.
	 *
	 * @param teamsDRCommonRequestBean
	 */
	@Transactional
	public TeamsDRCommonRequestBean saveDocsAndConfigureEndpoint(TeamsDRCommonRequestBean teamsDRCommonRequestBean)
			throws TclCommonException {
		LOGGER.info("Configuring endpoint for task Id  :: {}", teamsDRCommonRequestBean.getTaskId());
		Task task = getTaskByIdAndWfTaskId(teamsDRCommonRequestBean.getTaskId(),
				teamsDRCommonRequestBean.getWfTaskId());
		validateInputs(task, teamsDRCommonRequestBean);

		Map<String, String> atMap = new HashMap<>();
		atMap.put(AttributeConstants.CONFIGURATION_COMPLETED, teamsDRCommonRequestBean.getConfigurationCompleted());
		atMap.put(AttributeConstants.CONFIGURATION_DATE, teamsDRCommonRequestBean.getConfigurationDate());

		componentAndAttributeService.updateAttributes(task.getScServiceDetail().getId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (Objects.nonNull(teamsDRCommonRequestBean.getMediagateways())
				&& !teamsDRCommonRequestBean.getMediagateways().isEmpty()) {
			teamsDRCommonRequestBean.getMediagateways().forEach(teamsDRMgRequestBean -> {
				Map<String, String> atMap1 = new HashMap<>();
				if (Objects.nonNull(teamsDRMgRequestBean.getGenerateCSR())) {
					atMap1.put(AttributeConstants.GENERATE_CSR, teamsDRMgRequestBean.getMgRemotelyAccessible());
					if ("yes".equalsIgnoreCase(teamsDRMgRequestBean.getGenerateCSR())) {
						attachmentRepository.findById(teamsDRMgRequestBean.getAttachmentId()).ifPresent(attachment -> {
							List<ScAttachment> scLLDAttachmentList = scAttachmentRepository
									.findAllByScServiceDetail_IdAndSiteIdAndAttachment(task.getScServiceDetail().getId(),
											teamsDRMgRequestBean.getMgId(), attachment);
							LOGGER.info("ScAttachments Present for saveDocsAndConfigureEndpoint :: {}",
									!scLLDAttachmentList.isEmpty());
							if (scLLDAttachmentList.isEmpty()) {
								scAttachmentRepository.save(constructScAttachmentForMediaGateway(task,
										teamsDRMgRequestBean.getAttachmentId(), teamsDRMgRequestBean.getMgId()));
							}
						});
					}
				}
				atMap1.put(AttributeConstants.DNS_FOR_FQDN, teamsDRMgRequestBean.getDnsForFQDN());
				componentAndAttributeService.updateAttributesEndpoint(task.getScServiceDetail().getId(),
						teamsDRMgRequestBean.getMgId(), atMap1, task.getSiteType());
			});
		}

		processTaskLogDetails(task, "CLOSED", teamsDRCommonRequestBean.getDelayReason(), null,
				teamsDRCommonRequestBean);
		return (TeamsDRCommonRequestBean) flowableBaseService.taskDataEntry(task, teamsDRCommonRequestBean);
	}

	/**
	 * Method to save gsmc firewall details.
	 *
	 * @param teamsDRCommonRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRCommonRequestBean saveGsmcFirewallDetails(TeamsDRCommonRequestBean teamsDRCommonRequestBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(teamsDRCommonRequestBean.getTaskId(),
				teamsDRCommonRequestBean.getWfTaskId());
		validateInputs(task, teamsDRCommonRequestBean);

		Map<String, String> atMap = new HashMap<>();
		atMap.put(AttributeConstants.GSMC_TICKET, teamsDRCommonRequestBean.getGsmcTicket());
		atMap.put(AttributeConstants.FIREWALL_OPENED, teamsDRCommonRequestBean.getFirewallOpened());
		componentAndAttributeService.updateAttributes(task.getScServiceDetail().getId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		processTaskLogDetails(task, CLOSED, teamsDRCommonRequestBean.getDelayReason(), null, teamsDRCommonRequestBean);
		return (TeamsDRCommonRequestBean) flowableBaseService.taskDataEntry(task, teamsDRCommonRequestBean);
	}

	/**
	 * Method to save uat details for mg.
	 *
	 * @param teamsDRCommonRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRCommonRequestBean saveUatForMgDetails(TeamsDRCommonRequestBean teamsDRCommonRequestBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(teamsDRCommonRequestBean.getTaskId(),
				teamsDRCommonRequestBean.getWfTaskId());
		validateInputs(task, teamsDRCommonRequestBean);

		Map<String, String> atMap = new HashMap<>();
		atMap.put(AttributeConstants.UAT_COMPLETED, teamsDRCommonRequestBean.getUatCompleted());
		atMap.put(AttributeConstants.UAT_DATE, teamsDRCommonRequestBean.getUatDate());
		componentAndAttributeService.updateAttributes(task.getScServiceDetail().getId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (Objects.nonNull(teamsDRCommonRequestBean.getMediagateways())
				&& !teamsDRCommonRequestBean.getMediagateways().isEmpty()) {
			teamsDRCommonRequestBean.getMediagateways().forEach(teamsDRMgRequestBean -> {
				Map<String, String> atMap1 = new HashMap<>();
				atMap1.put(AttributeConstants.REMOTE_ACCESS_AFTER_HARDENING,
						teamsDRMgRequestBean.getRemoteAccessAfterHardening());
				atMap1.put(AttributeConstants.VERIFY_SBC_IS_ONBOARDED_ON_EMS,
						teamsDRMgRequestBean.getVerifySbcOnboarded());
				atMap1.put(AttributeConstants.VERIFY_SNMP_TRAPS_POLLING,
						teamsDRMgRequestBean.getVerfiySnmsTrapsAndPolling());
				componentAndAttributeService.updateAttributesEndpoint(task.getScServiceDetail().getId(),
						teamsDRMgRequestBean.getMgId(), atMap1, task.getSiteType());
			});
		}

		processTaskLogDetails(task, CLOSED, teamsDRCommonRequestBean.getDelayReason(), null, teamsDRCommonRequestBean);
		return (TeamsDRCommonRequestBean) flowableBaseService.taskDataEntry(task, teamsDRCommonRequestBean);
	}

	/**
	 * Method to process advance enrichment details.
	 *
	 * @param teamsDRMSAdvanceEnrichmentBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRMSAdvanceEnrichmentBean saveMSAdvanceEnrichmentDetails(
			TeamsDRMSAdvanceEnrichmentBean teamsDRMSAdvanceEnrichmentBean, String action) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(teamsDRMSAdvanceEnrichmentBean.getTaskId(),
				teamsDRMSAdvanceEnrichmentBean.getWfTaskId());
		validateInputs(task, teamsDRMSAdvanceEnrichmentBean);
		if (TeamsDRFulfillmentConstants.SAVE.equalsIgnoreCase(action)) {
			flowableBaseService.taskDataEntrySave(task, teamsDRMSAdvanceEnrichmentBean);
		} else if (TeamsDRFulfillmentConstants.CLOSE.equalsIgnoreCase(action)) {
			if (Objects.nonNull(teamsDRMSAdvanceEnrichmentBean.getManagedServicesDetails()))
				processManagedServicesDetails(teamsDRMSAdvanceEnrichmentBean.getManagedServicesDetails(), task);
			if (Objects.nonNull(teamsDRMSAdvanceEnrichmentBean.getManagementAndMonitoringDetails())) {
				//filtering countries that do not have any site
				if (Objects.nonNull(
						teamsDRMSAdvanceEnrichmentBean.getManagementAndMonitoringDetails().getTenantAlertConfig()))
					teamsDRMSAdvanceEnrichmentBean.getManagementAndMonitoringDetails().setTenantAlertConfig(
							teamsDRMSAdvanceEnrichmentBean.getManagementAndMonitoringDetails().getTenantAlertConfig()
									.stream().filter(tenantAlertConfig -> Objects.nonNull(tenantAlertConfig) && Objects
									.nonNull(tenantAlertConfig.getManagementAndMonitoringSites()) && (!tenantAlertConfig
									.getManagementAndMonitoringSites().isEmpty() && tenantAlertConfig
									.getManagementAndMonitoringSites().stream().anyMatch(
											mmS -> Objects.nonNull(mmS.getCity()) && StringUtils
													.isNotBlank(mmS.getCity())))).collect(Collectors.toList()));
				processManagementAndMonitoringDetails(
						teamsDRMSAdvanceEnrichmentBean.getManagementAndMonitoringDetails(), task);
			}
			if (Objects.nonNull(teamsDRMSAdvanceEnrichmentBean.getTrainingTaskDetails()))
				processTrainingDetails(teamsDRMSAdvanceEnrichmentBean.getTrainingTaskDetails(), task);
			processTaskLogDetails(task, CLOSED, teamsDRMSAdvanceEnrichmentBean.getDelayReason(), null,
					teamsDRMSAdvanceEnrichmentBean);
			flowableBaseService.taskDataEntry(task, teamsDRMSAdvanceEnrichmentBean);
		}
		return teamsDRMSAdvanceEnrichmentBean;
	}

	/**
	 * Method to process managed services details
	 *
	 * @param teamsDRMSAdvanceEnrichmentBean
	 * @param task
	 */
	public TeamsDRManagedServiceBean processManagedServicesDetails(TeamsDRManagedServiceBean teamsDRManagedServiceBean,
			Task task) {
		LOGGER.info("Inside processManagedServicesDetails  taskId : {}", task.getId());
		validateInputs(task, teamsDRManagedServiceBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put(AttributeConstants.DELEGATE_ADMIN_ACCESS, teamsDRManagedServiceBean.getDelegateAdminAccess());
		atMap.put(TENANT_LOGIN_NAME, teamsDRManagedServiceBean.getTenantLoginName());
		atMap.put(AttributeConstants.TENANT_LOGIN_PASSWORD, teamsDRManagedServiceBean.getTenantLoginPassWord());
		teamsDRManagedServiceBean.getSiteDetails().forEach(siteDetail -> {
			saveSite(siteDetail, task);
		});
		componentAndAttributeService
				.updateAttributes(task.getScServiceDetail().getId(), atMap, AttributeConstants.COMPONENT_LM,
						task.getSiteType());
		return teamsDRManagedServiceBean;
	}

	/**
	 * Create SC Component
	 *
	 * @param orderUuid
	 * @param serviceDetailId
	 * @param componentName
	 * @return
	 */
	private ScComponent createOrUpdateScComponent(String orderUuid, Integer serviceDetailId, String componentName, String siteType) {
		ScComponent scComponent = scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceDetailId, componentName, siteType);
		if(Objects.isNull(scComponent))
			scComponent = new ScComponent();
		scComponent.setComponentName(componentName);
		scComponent.setScServiceDetailId(serviceDetailId);
		scComponent.setSiteType(siteType);
		scComponent.setUuid(orderUuid);
		scComponent.setScServiceDetailId(serviceDetailId);
		scComponent.setIsActive(CommonConstants.Y);
		scComponent.setCreatedBy(Utils.getSource());
		scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
		scComponent = scComponentRepository.save(scComponent);
		return scComponent;
	}

	/**
	 * Method to process management and monitoring details
	 *
	 * @param teamsDRMSAdvanceEnrichmentBean
	 * @param task
	 * @throws TclCommonException
	 */
	public TeamsDRManagementAndMonitoringBean processManagementAndMonitoringDetails(
			TeamsDRManagementAndMonitoringBean teamsDRManagementAndMonitoringBean, Task task) throws TclCommonException {
		LOGGER.info("Inside processManagementAndMonitoringDetails  taskId : {}", task.getId());
		validateInputs(task, teamsDRManagementAndMonitoringBean);
		teamsDRService.createOrUpdateAdditionalParam(AttributeConstants.MANAGEMENT_AND_MONITORING_DATA,
				Utils.convertObjectToJson(teamsDRManagementAndMonitoringBean),task.getScServiceDetail());
		return teamsDRManagementAndMonitoringBean;
	}

	/**
	 * Method to process training details.
	 *
	 * @param teamsDRMSAdvanceEnrichmentBean
	 * @param task
	 * @throws TclCommonException
	 */
	public TeamsDRTrainingBean processTrainingDetails(TeamsDRTrainingBean teamsDRTrainingBean, Task task)
			throws TclCommonException {
		LOGGER.info("Inside processTrainingDetails  taskId : {}", task.getId());
		validateInputs(task, teamsDRTrainingBean);
		teamsDRTrainingBean.getTrainingDetails();
		Map<String, String> atMap = new HashMap<>();
		teamsDRService.createOrUpdateAdditionalParam(AttributeConstants.COMMON_TRAINING_DATA,
				Utils.convertObjectToJson(teamsDRTrainingBean.getCommonData()),task.getScServiceDetail());
		List<TeamsDRTrainingDetailsBean> endUserBeans = teamsDRTrainingBean.getTrainingDetails().stream()
				.filter(trainingData -> TeamsDRFulfillmentConstants.END_USER_TRAINING
						.equalsIgnoreCase(trainingData.getTrainingType())).collect(Collectors.toList());
		List<TeamsDRTrainingDetailsBean> advanceLevelBeans = teamsDRTrainingBean.getTrainingDetails().stream()
				.filter(trainingData -> TeamsDRFulfillmentConstants.ADVANCE_LEVEL_TRAINING
						.equalsIgnoreCase(trainingData.getTrainingType())).collect(Collectors.toList());
		if (!endUserBeans.isEmpty()) {
			teamsDRService.createOrUpdateAdditionalParam(AttributeConstants.USER_TRAINING_DATA,
					Utils.convertObjectToJson(endUserBeans),task.getScServiceDetail());
			atMap.put(IS_END_USER_TRAINING, YES);
		}
		if (!advanceLevelBeans.isEmpty()) {
			teamsDRService.createOrUpdateAdditionalParam(AttributeConstants.ADVANCED_TRAINING_DATA,
					Utils.convertObjectToJson(advanceLevelBeans),task.getScServiceDetail());
			atMap.put(IS_ADVANCED_LEVEL_TRAINING, YES);
		}
		componentAndAttributeService
				.updateAttributes(task.getScServiceDetail().getId(), atMap, AttributeConstants.COMPONENT_LM, task.getSiteType());
		return teamsDRTrainingBean;
	}

	/**
	 * Save country details
	 *
	 * @param enrichmentSiteBean
	 * @return
	 */
	@Transactional
	public TeamsDRMSSiteBean saveSite(TeamsDRMSSiteBean enrichmentSiteBean, Task task) {
		validateInputs(task, enrichmentSiteBean);
		Map<String, String> atMap = new HashMap<>();
		enrichmentSiteBean.getSites().forEach(teamsDRMSSiteDetails -> {
			createOrUpdateScComponent(task.getScServiceDetail().getScOrderUuid(), task.getScServiceDetail().getId(),
					enrichmentSiteBean.getCountryName() + CommonConstants.UNDERSCORE + teamsDRMSSiteDetails
							.getSiteName(), DR_SITE);
			atMap.put(AttributeConstants.COUNTRY_NAME, enrichmentSiteBean.getCountryName());
			atMap.put(AttributeConstants.SITE_NAME, teamsDRMSSiteDetails.getSiteName());
			atMap.put(AttributeConstants.TATA_SCOPE_OF_WORK, teamsDRMSSiteDetails.getTataScopeOfWork());
			atMap.put(AttributeConstants.NO_OF_USERS_ON_SITE, teamsDRMSSiteDetails.getNoOfUsersOnSite());
			atMap.put(AttributeConstants.SITE_TESTING_PROVISION_SPOC,
					teamsDRMSSiteDetails.getSiteTestingProvisioningSpoc());
			atMap.put(AttributeConstants.SPOC_CONTACT, teamsDRMSSiteDetails.getSpocContactNumber());
			atMap.put(AttributeConstants.PRIMARY_TEST_USER_DOMAIN_ACTIVATION,
					teamsDRMSSiteDetails.getPrimaryTestUserDomainActivation());
			atMap.put(AttributeConstants.TEST_USER_1_CREDENTIALS, teamsDRMSSiteDetails.getTestUser1Credentials());
			atMap.put(AttributeConstants.SECONDARY_TEST_USER_DOMAIN_ACTIVATION,
					teamsDRMSSiteDetails.getSecondaryTestUserDomainActivation());
			atMap.put(AttributeConstants.TEST_USER_2_CREDENTIALS, teamsDRMSSiteDetails.getTestUser2Credentials());
			componentAndAttributeService.updateAttributes(task.getScServiceDetail().getId(), atMap,
					enrichmentSiteBean.getCountryName() + CommonConstants.UNDERSCORE + teamsDRMSSiteDetails
							.getSiteName(), DR_SITE);
		});
		return enrichmentSiteBean;
	}
	
	/**
	 * Perform UAT Testing.
	 * 
	 * @param teamsDRUatTestingBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRUatTestingBean saveMSUATTestingDetails(TeamsDRUatTestingBean teamsDRUatTestingBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(teamsDRUatTestingBean.getTaskId(),
				teamsDRUatTestingBean.getWfTaskId());
		
		validateInputs(task, teamsDRUatTestingBean);
		
		Map<String, String> atMap = new HashMap<>();
		atMap.put(AttributeConstants.MS_UAT_TESTING_STATUS, teamsDRUatTestingBean.getMsUatTestingStatus());
		atMap.put(AttributeConstants.MS_UAT_COMPLETION_DATE, teamsDRUatTestingBean.getMsUatCompletionDate());
		if(Objects.nonNull(teamsDRUatTestingBean.getMsUatDocIds())) {
			atMap.put(AttributeConstants.MS_UAT_ATT_ID, teamsDRUatTestingBean.getMsUatDocIds().stream().findFirst().get().getAttachmentId().toString());
		}		
		
		Optional<GscFlowGroup> flowGroup= gscFlowGroupRepository.findById(teamsDRUatTestingBean.getBatchId());
		
		teamsDRService.updateAttributes(task.getScServiceDetail().getId(), atMap, flowGroup.get());
		
		processTaskLogDetails(task, CLOSED, teamsDRUatTestingBean.getDelayReason(), null,
				teamsDRUatTestingBean);
		flowableBaseService.taskDataEntry(task, teamsDRUatTestingBean);
		return teamsDRUatTestingBean;
	}

	/**
	 * Save user mapping task
	 *
	 * @param teamsDRUserMappingBean
	 * @return
	 * @throws TclCommonException
	 */
	public TeamsDRUserMappingBean saveUserMapping(TeamsDRUserMappingBean teamsDRUserMappingBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(teamsDRUserMappingBean.getTaskId(), teamsDRUserMappingBean.getWfTaskId());
		validateInputs(task, teamsDRUserMappingBean);

		Map<String, String> atMap = new HashMap<>();
		atMap.put(AttributeConstants.DID_RANGE_ALLOCATED, teamsDRUserMappingBean.getDidRangeAllocated());
		atMap.put(AttributeConstants.BATCH_DATE, teamsDRUserMappingBean.getBatchDate());
		atMap.put(AttributeConstants.BATCH_TIME, teamsDRUserMappingBean.getBatchTime());
		atMap.put(AttributeConstants.BATCH_USER_COUNT, String.valueOf(teamsDRUserMappingBean.getBatchUserCount()));
		if (Objects.nonNull(teamsDRUserMappingBean.getDocumentIds()) && !teamsDRUserMappingBean.getDocumentIds().isEmpty())
			atMap.put(AttributeConstants.MS_USER_MAPPING_ATTACHMENT_ID,
					String.valueOf(teamsDRUserMappingBean.getDocumentIds().stream().findAny().get().getAttachmentId()));
		GscFlowGroup flowGroup = teamsDRService.generateFlowGroup(
				teamsDRUserMappingBean.getComponentName() + CommonConstants.UNDERSCORE + Utils.generateUid(4), null,
				String.valueOf(teamsDRUserMappingBean.getComponentId()), AttributeConstants.COMPONENT,
				Utils.getSource());
		teamsDRService.updateAttributes(task.getScServiceDetail().getId(), atMap, flowGroup);

		// to update the pending user count
		teamsDRService.updatePendingUserCount(task.getScServiceDetail().getId(),task.getScComponent());

		processTaskLogDetails(task, CLOSED, teamsDRUserMappingBean.getDelayReason(), null, teamsDRUserMappingBean);
		return (TeamsDRUserMappingBean) flowableBaseService.taskDataEntry(task, teamsDRUserMappingBean);
	}

	/**
	 * Method to save direct routing details.
	 * @param teamsDRMSDirectRoutingBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRMSDirectRoutingBean saveMSDirectRoutingDetails(TeamsDRMSDirectRoutingBean teamsDRMSDirectRoutingBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(teamsDRMSDirectRoutingBean.getTaskId(),
				teamsDRMSDirectRoutingBean.getWfTaskId());
		validateInputs(task, teamsDRMSDirectRoutingBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put(AttributeConstants.MS_DR_CONFIG_STATUS, teamsDRMSDirectRoutingBean.getMsDrConfigStatus());
		atMap.put(AttributeConstants.MS_DR_CONFIG_DATE, teamsDRMSDirectRoutingBean.getMsDrConfigDate());
		if(Objects.nonNull(teamsDRMSDirectRoutingBean.getDrAttachmentIds())) {
			teamsDRMSDirectRoutingBean.getDrAttachmentIds().forEach(drAttachmentId->
					atMap.put(AttributeConstants.MS_DR_ATT_ID, String.valueOf(drAttachmentId.getAttachmentId())));
		}
		gscFlowGroupRepository.findById(teamsDRMSDirectRoutingBean.getBatchId())
				.ifPresent(flowGroup-> teamsDRService.updateAttributes(task.getScServiceDetail().getId(), atMap, flowGroup));
		processTaskLogDetails(task, CLOSED, teamsDRMSDirectRoutingBean.getDelayReason(), null,
				teamsDRMSDirectRoutingBean);
		return (TeamsDRMSDirectRoutingBean) flowableBaseService.taskDataEntry(task, teamsDRMSDirectRoutingBean);
	}

	/**
	 * Method to save endUserTraining Details
	 * @param teamsDRTrainingBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRTrainingBean saveTrainingDetails(TeamsDRTrainingBean teamsDRTrainingBean, String type)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(teamsDRTrainingBean.getTaskId(),
				teamsDRTrainingBean.getWfTaskId());
		validateInputs(task, teamsDRTrainingBean);
		List<TeamsDRTrainingDetailsBean> teamsDRTrainingBeans = teamsDRTrainingBean.getTrainingDetails();
		if (Objects.nonNull(type) && Objects.nonNull(teamsDRTrainingBeans) && !teamsDRTrainingBeans.isEmpty()) {
			if(END_USER_TRAINING.equals(type)){
				teamsDRService.createOrUpdateAdditionalParam(AttributeConstants.USER_TRAINING_DATA,
                        Utils.convertObjectToJson(teamsDRTrainingBeans),task.getScServiceDetail());
			}else if(ADVANCED_LEVEL_TRAINING.equals(type)){
				teamsDRService.createOrUpdateAdditionalParam(AttributeConstants.ADVANCED_TRAINING_DATA,
                        Utils.convertObjectToJson(teamsDRTrainingBeans),task.getScServiceDetail());
			}
		}
		return (TeamsDRTrainingBean) flowableBaseService.taskDataEntry(task, teamsDRTrainingBean);
	}

	/**
	 * Method to save direct routing details.
	 * @param teamsDRManagementAndMonitoringBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TeamsDRManagementAndMonitoringBean saveMSManagementAndMonitoring(TeamsDRManagementAndMonitoringBean teamsDRManagementAndMonitoringBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(teamsDRManagementAndMonitoringBean.getTaskId(),
				teamsDRManagementAndMonitoringBean.getWfTaskId());
		validateInputs(task, teamsDRManagementAndMonitoringBean);
		Map<String, String> atMap = new HashMap<>();
		atMap.put(AttributeConstants.TENANT_CONFIG_COMPLETED, teamsDRManagementAndMonitoringBean.getTenantConfigurationCompleted());
		atMap.put(AttributeConstants.TENANT_ALERT_COMPLETED, teamsDRManagementAndMonitoringBean.getTenantAlertConfigCompleted());
		gscFlowGroupRepository.findById(teamsDRManagementAndMonitoringBean.getBatchId())
				.ifPresent(flowGroup-> teamsDRService.updateAttributes(task.getScServiceDetail().getId(), atMap, flowGroup));
		processTaskLogDetails(task, CLOSED, teamsDRManagementAndMonitoringBean.getDelayReason(), null,
				teamsDRManagementAndMonitoringBean);
		return (TeamsDRManagementAndMonitoringBean) flowableBaseService.taskDataEntry(task, teamsDRManagementAndMonitoringBean);
	}

	/**
	 * Method to close teamsdr service assurance task.
	 * @param taskDetailsBaseBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TaskDetailsBaseBean saveTeamsDRServiceAssurance(TaskDetailsBaseBean taskDetailsBaseBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(taskDetailsBaseBean.getTaskId(),
				taskDetailsBaseBean.getWfTaskId());
		validateInputs(task, taskDetailsBaseBean);
		processTaskLogDetails(task, CLOSED, taskDetailsBaseBean.getDelayReason(), null,
				taskDetailsBaseBean);

		// Checking if all the drSite have 0 pending count
		// if yes then updating service id from INPROGRESS --> ACTIVE
		if (teamsDRService.checkPendingCount(task.getScComponent().getId())) {
			SetCLRSyncBean setCLRSyncBean = new SetCLRSyncBean();
			setCLRSyncBean.setServiceId(task.getServiceId());
			setCLRSyncBean.setObjectName(task.getServiceCode());
			setCLRSyncBean.setOrderCode(task.getOrderCode());
			String request = Utils.convertObjectToJson(setCLRSyncBean);
			LOGGER.info("Request for setServiceActiveQueue :: {}",request);
			String setServiceActiveResponse = (String) mqUtils.sendAndReceive(setServiceActiveQueue, request);
			LOGGER.info("setServiceActiveResponse => {} ", setServiceActiveResponse);
		}

		return (TaskDetailsBaseBean) flowableBaseService.taskDataEntry(task, taskDetailsBaseBean);
	}

	/**
	 * Method to close teamsdr customer handover.
	 * @param taskDetailsBaseBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional
	public TaskDetailsBaseBean saveTeamsDRCustomerHandover(TaskDetailsBaseBean taskDetailsBaseBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(taskDetailsBaseBean.getTaskId(),
				taskDetailsBaseBean.getWfTaskId());
		validateInputs(task, taskDetailsBaseBean);
		processTaskLogDetails(task, CLOSED, taskDetailsBaseBean.getDelayReason(), null,
				taskDetailsBaseBean);
		return (TaskDetailsBaseBean) flowableBaseService.taskDataEntry(task, taskDetailsBaseBean);
	}
}
