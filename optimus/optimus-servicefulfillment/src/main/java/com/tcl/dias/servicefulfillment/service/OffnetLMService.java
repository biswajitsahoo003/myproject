package com.tcl.dias.servicefulfillment.service;

import java.sql.Timestamp;
import java.util.*;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.ConfirmSupplierConfiguration;
import com.tcl.dias.servicefulfillment.beans.HandoffDetailsBean;
import com.tcl.dias.servicefulfillment.beans.LmDelivery;
import com.tcl.dias.servicefulfillment.beans.LmDeliveryAttributesBean;
import com.tcl.dias.servicefulfillment.beans.NegotiateCommercialsLMProvide;
import com.tcl.dias.servicefulfillment.beans.OffnetSiteSurvey;
import com.tcl.dias.servicefulfillment.beans.ProvideKlmVlanBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePOToCCProvider;
import com.tcl.dias.servicefulfillment.beans.ProvidePOToLMProvider;
import com.tcl.dias.servicefulfillment.beans.ProvidePOToLMProviderSite2;
import com.tcl.dias.servicefulfillment.beans.RaiseJeopardy;
import com.tcl.dias.servicefulfillment.beans.SupplierAcceptance;
import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstActivityDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MstStatusConstant;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * ReserveResourceService is used to perform CPE related tasks.
 *
 * @author arjayapa
 */

@Service
@Transactional(readOnly = true)
public class OffnetLMService extends ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OffnetLMService.class);

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	ProcessPlanRepository processPlanRepository;

	@Autowired
	ActivityPlanRepository activityPlanRepository;

	@Autowired
	TaskPlanRepository taskPlanRepository;

	@Autowired
	TaskCacheService taskCacheService;

	@Autowired
	MstTaskDefRepository mstTaskDefRepository;

	@Autowired
	MstActivityDefRepository mstActivityDefRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	RestClientService restClientService;


	@Value("${cramer.klmdetails.url}")
	private String cramerKlmdetailsUrl;
	
	@Value("${cramer.klmdetails.enabled:Y}")
	private String cramerKlmdetailsEnabled;
	
	 @Autowired
	 FlowableBaseService flowableBaseService;

	/**
	 * saves details related to negotiate commercials with LM provider task.
	 *
	 * @param taskId
	 * @param negotiateCommercialsLMProvide
	 * @return NegotiateCommercialsLMProvide
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public NegotiateCommercialsLMProvide negotiateCommercialsLMProvide(NegotiateCommercialsLMProvide negotiateCommercialsLMProvide) throws TclCommonException {
		Map<String, String> serviceMap = new HashMap<>();
		Task task = getTaskByIdAndWfTaskId(negotiateCommercialsLMProvide.getTaskId(),negotiateCommercialsLMProvide.getWfTaskId());
		validateInputs(task, negotiateCommercialsLMProvide);
		String lmType= commonFulfillmentUtils.getComponentAttributes(task.getServiceId(), AttributeConstants.COMPONENT_LM, "A").get("lmType");
		if(lmType.equalsIgnoreCase("OffnetWL") || lmType.equalsIgnoreCase("Offnet Wireline")){
			if (negotiateCommercialsLMProvide.getNegotiateOffnetARCCost()!=null)
				serviceMap.put("lm_arc_bw_offwl", negotiateCommercialsLMProvide.getNegotiateOffnetARCCost());
			if (negotiateCommercialsLMProvide.getNegotiateOffnetNRCCost()!=null)
				serviceMap.put("lm_otc_nrc_installation_offwl", negotiateCommercialsLMProvide.getNegotiateOffnetNRCCost());
		}
		if(lmType.equalsIgnoreCase("OffnetRF") || lmType.equalsIgnoreCase("Offnet Wireless")){
			if (negotiateCommercialsLMProvide.getNegotiateOffnetARCCost()!=null)
				serviceMap.put("lm_arc_bw_prov_ofrf", negotiateCommercialsLMProvide.getNegotiateOffnetARCCost());
			if (negotiateCommercialsLMProvide.getNegotiateOffnetNRCCost()!=null)
				serviceMap.put("lm_nrc_bw_prov_ofrf", negotiateCommercialsLMProvide.getNegotiateOffnetNRCCost());
			if (negotiateCommercialsLMProvide.getNegotiateOffnetMastCost()!=null)
				serviceMap.put("lm_nrc_mast_ofrf", negotiateCommercialsLMProvide.getNegotiateOffnetMastCost());
		}
		componentAndAttributeService.updateServiceAttributes(task.getServiceId(), serviceMap, "FEASIBILITY");

		if (negotiateCommercialsLMProvide.getDocumentIds() != null && !negotiateCommercialsLMProvide.getDocumentIds().isEmpty())
			negotiateCommercialsLMProvide.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", negotiateCommercialsLMProvide.getDelayReason(), null, negotiateCommercialsLMProvide);
		return (NegotiateCommercialsLMProvide) flowableBaseService.taskDataEntry(task, negotiateCommercialsLMProvide);
	}

	/**
	 * saves details related to provide PO details to LM provider task.
	 *
	 * @param taskId
	 * @param providePOToLMProvider
	 * @return ProvidePOToLMProvider
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvidePOToLMProvider providePOToLMProvider(ProvidePOToLMProvider providePOToLMProvider)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(providePOToLMProvider.getTaskId(),providePOToLMProvider.getWfTaskId());
		validateInputs(task, providePOToLMProvider);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("offnetInterStatePoNumber", providePOToLMProvider.getOffnetInterStatePoNumber());
		atMap.put("offnetInterStatePoDate", Objects.isNull(providePOToLMProvider.getOffnetInterStatePoDate()) ? ""
				: providePOToLMProvider.getOffnetInterStatePoDate().toString());
		atMap.put("offnetLocalPoNumber", providePOToLMProvider.getOffnetLocalPoNumber());
		atMap.put("offnetLocalPoDate", Objects.isNull(providePOToLMProvider.getOffnetLocalPoDate()) ? ""
				: providePOToLMProvider.getOffnetLocalPoDate().toString());
		atMap.put("supplierCAFNumber", providePOToLMProvider.getSupplierCAFNumber());
		atMap.put("offNetSuplierOrderId", providePOToLMProvider.getSuplierOrderId());
		atMap.put("offNetSuplierCircuitId", providePOToLMProvider.getSuplierCircuitId());
		atMap.put("offNetDelayReason", providePOToLMProvider.getDelayReason());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		if (providePOToLMProvider.getDocumentIds() != null && !providePOToLMProvider.getDocumentIds().isEmpty())
			providePOToLMProvider.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task, "CLOSED", providePOToLMProvider.getDelayReason(), null, providePOToLMProvider);
		return (ProvidePOToLMProvider) flowableBaseService.taskDataEntry(task, providePOToLMProvider);
	}

	/**
	 * saves details related to provide PO details to LM provider task for site 2.
	 *
	 * @author diksha garg
	 * @param taskId
	 * @param providePOToLMProviderSite2
	 * @return ProvidePOToLMProviderSite2
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvidePOToLMProviderSite2 providePOToLMProviderSite2(Integer taskId, ProvidePOToLMProviderSite2 providePOToLMProviderSite2)
			throws TclCommonException {
		Task task = getTaskById(taskId);
		validateInputs(task, providePOToLMProviderSite2);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("offnetInterStatePoNumber", providePOToLMProviderSite2.getOffnetInterStatePoNumber());
		atMap.put("offnetInterStatePoDate", Objects.isNull(providePOToLMProviderSite2.getOffnetInterStatePoDate()) ? ""
				: providePOToLMProviderSite2.getOffnetInterStatePoDate().toString());
		atMap.put("offnetLocalPoNumber3", providePOToLMProviderSite2.getOffnetLocalPoNumber3());
		atMap.put("offnetLocalPoDate3", Objects.isNull(providePOToLMProviderSite2.getOffnetLocalPoDate3()) ? ""
				: providePOToLMProviderSite2.getOffnetLocalPoDate3().toString());
		atMap.put("offNetDelayReason", providePOToLMProviderSite2.getDelayReason());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,
				task.getSiteType());

		if (providePOToLMProviderSite2.getDocumentIds() != null
				&& !providePOToLMProviderSite2.getDocumentIds().isEmpty())
			providePOToLMProviderSite2.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		processTaskLogDetails(task, "CLOSED", providePOToLMProviderSite2.getDelayReason(), null, providePOToLMProviderSite2);
		return (ProvidePOToLMProviderSite2) flowableBaseService.taskDataEntry(task, providePOToLMProviderSite2);
	}

	/**
	 * saves details related to provide PO details to Cross-Connect provider task.
	 *
	 * @param taskId
	 * @param providePOToCCProvider
	 * @return ProvidePOToCCProvider
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvidePOToCCProvider providePOToCCProvider(ProvidePOToCCProvider providePOToCCProvider)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(providePOToCCProvider.getTaskId(),providePOToCCProvider.getWfTaskId());
		validateInputs(task, providePOToCCProvider);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("crossConnectPONumber", providePOToCCProvider.getCrossConnectPONumber());
		atMap.put("crossConnectPODate", Objects.isNull(providePOToCCProvider.getCrossConnectPODate()) ? ""
				: providePOToCCProvider.getCrossConnectPODate().toString());
		atMap.put("crossConnectSupplierCAFNumber", providePOToCCProvider.getSupplierCAFNumber());
		atMap.put("crossConnectProvider", providePOToCCProvider.getCrossConnectProvider());
		atMap.put("crossConnectRequired", providePOToCCProvider.getCrossConnectRequired());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());

		if (providePOToCCProvider.getDocumentIds() != null && !providePOToCCProvider.getDocumentIds().isEmpty())
			providePOToCCProvider.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		processTaskLogDetails(task, "CLOSED", providePOToCCProvider.getDelayReason(), null, providePOToCCProvider);
		return (ProvidePOToCCProvider) flowableBaseService.taskDataEntry(task, providePOToCCProvider);
	}

	/**
	 * saves supplier acceptance details.
	 *
	 * @param taskId
	 * @param supplierAcceptance
	 * @return SupplierAcceptance
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public SupplierAcceptance getSupplierAcceptance(SupplierAcceptance supplierAcceptance)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(supplierAcceptance.getTaskId(),supplierAcceptance.getWfTaskId());

		validateInputs(task, supplierAcceptance);
		Map<String, String> atMap = new HashMap<>();
		atMap.put("offnetOrderReferenceId", supplierAcceptance.getProvideOrderreferenceId());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task, "CLOSED", supplierAcceptance.getDelayReason(), null, supplierAcceptance);
		return (SupplierAcceptance) flowableBaseService.taskDataEntry(task, supplierAcceptance);
	}

	/**
	 * Raise jeopardy task.
	 *
	 * @param taskId
	 * @param raiseJeopardy
	 * @return RaiseJeopardy
	 */
	@Transactional(readOnly = false)
	public RaiseJeopardy raiseJeopardy(RaiseJeopardy raiseJeopardy) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(raiseJeopardy.getTaskId(),raiseJeopardy.getWfTaskId());

		validateInputs(task, raiseJeopardy);
		processTaskLogDetails(task, "CLOSED", raiseJeopardy.getDelayReason(), null, raiseJeopardy);
		return (RaiseJeopardy) flowableBaseService.taskDataEntry(task, raiseJeopardy);
	}

	/**
	 * @author diksha garg
	 *
	 * @param taskId
	 * @param defineSowProjPlan
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
	public LmDelivery defineSowProjectPlan( LmDelivery lmDelivery) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(lmDelivery.getTaskId(),lmDelivery.getWfTaskId());

		validateInputs(task, lmDelivery);

		processTaskLogDetails(task, "CLOSED", lmDelivery.getDelayReason(), null, lmDelivery);
		String action = StringUtils.trimToEmpty(lmDelivery.getAction());
		LOGGER.info("action {}",action);
		Map<String,Object> flowableMap = new HashMap<>();
        flowableMap.put("offnetSowAction","close");

		Map<String, String> map = new HashMap<>();

		map.put("typeOfPoleErection",lmDelivery.getTypeOfPoleErection());
		map.put("poleHeight",lmDelivery.getTypeOfPoleErection());
		map.put("mastHeight",String.valueOf(lmDelivery.getMastHeight()));

		String mastOwner = StringUtils.trimToEmpty(lmDelivery.getMastOwner());
		map.put("mastOwner",mastOwner);

		if("TCL".equalsIgnoreCase(mastOwner)) {
			flowableMap.put("offnetMastRequired",lmDelivery.getMastRequired());
		}

		map.put("offnetMastRequired", lmDelivery.getMastRequired());
		map.put("offnetFotRequired", lmDelivery.getFotRequired());
		map.put("offnetMastHeight", String.valueOf(lmDelivery.getMastHeight()));
		map.put("offnetTypeOfMastAntennaErection", lmDelivery.getTypeOfMastAntennaErection());
		map.put("reasonForMast", lmDelivery.getReasonForMast());
		map.put("siteRediness", lmDelivery.getRaiseSiteRedinessIssue());
		map.put("proposedMastInstallationDate", lmDelivery.getMastInstallationDate());
		map.put("providerOrderLogDate",lmDelivery.getProviderOrderLogDate());
		map.put("supplierCategory",lmDelivery.getSupplierCategory());
		map.put("otherId",lmDelivery.getOtherId());
		LOGGER.info("providerOrderLogDate supplierCategory and otherId {} {} {} :",lmDelivery.getProviderOrderLogDate(),lmDelivery.getSupplierCategory(),lmDelivery.getOtherId());
		if(lmDelivery.getRaiseSiteRedinessIssue().equalsIgnoreCase("Not Ready")) {
			flowableMap.put("raiseSiteRedinessIssue", true);
		}else {
			flowableMap.put("raiseSiteRedinessIssue", false);
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), map, AttributeConstants.COMPONENT_LM,task.getSiteType());

		if (lmDelivery.getDocumentIds() != null && !lmDelivery.getDocumentIds().isEmpty()) {
			lmDelivery.getDocumentIds().forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		ProcessPlan processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(task.getServiceId(),
				"lm_impl_process", task.getSiteType()==null?"A":task.getSiteType());

		if (Objects.nonNull(processPlan)) {
			List<LmDeliveryAttributesBean> lmDeliveryAttributesBeanList = lmDelivery.getAttributes();
			lmDeliveryAttributesBeanList.stream().forEach(attr -> {
				ActivityPlan activityPlan = new ActivityPlan();
				LOGGER.info("create activity {}", attr.getKey());
				activityPlan.setMstActivityDef(mstActivityDefRepository.findByKey(attr.getKey()));
				activityPlan.setProcessPlan(processPlan);
				activityPlan.setServiceId(task.getServiceId());
				activityPlan.setServiceCode(task.getServiceCode());

				activityPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
				if (attr.getPlannedStartDate() != null) {
					activityPlan.setEstimatedStartTime(new Timestamp(DateUtil.convertStringToDateYYMMDD(attr.getPlannedStartDate()).getTime()));
					activityPlan.setPlannedStartTime(new Timestamp(DateUtil.convertStringToDateYYMMDD(attr.getPlannedStartDate()).getTime()));
					activityPlan.setTargettedStartTime(new Timestamp(DateUtil.convertStringToDateYYMMDD(attr.getPlannedStartDate()).getTime()));
				}
				if (attr.getPlannedEndDate() != null) {
					activityPlan.setEstimatedEndTime(new Timestamp(DateUtil.convertStringToDateYYMMDD(attr.getPlannedEndDate()).getTime()));
					activityPlan.setPlannedEndTime(new Timestamp(DateUtil.convertStringToDateYYMMDD(attr.getPlannedEndDate()).getTime()));
					activityPlan.setTargettedEndTime(new Timestamp(DateUtil.convertStringToDateYYMMDD(attr.getPlannedEndDate()).getTime()));
				}

				activityPlanRepository.save(activityPlan);
			});
		}


		return  (LmDelivery) flowableBaseService.taskDataEntry(task, lmDelivery,flowableMap);
	}


	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED)
	public HandoffDetailsBean provideOffnetKlmVlan(HandoffDetailsBean handoffDetails) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(handoffDetails.getTaskId(),handoffDetails.getWfTaskId());

		validateInputs(task, handoffDetails);
		String message="";

		if(handoffDetails.getDelayReason()!=null) {
			message=handoffDetails.getDelayReason();
		}
		if(handoffDetails.getRemarks()!=null) {
			message="Remarks:"+handoffDetails.getRemarks();
		}
		processTaskLogDetails(task, "CLOSED", message, null, handoffDetails);

		Map<String,Object> flowableMap = new HashMap<>();
        flowableMap.put("action","close");

		Map<String, String> map = new HashMap<>();
		if (handoffDetails != null) {
			map.put("nniId", handoffDetails.getNniId());
			map.put("customerInnerVlan", handoffDetails.getCustomerInnerVlan());
			map.put("handoverType", handoffDetails.getHandoverType());
			map.put("cloudName", handoffDetails.getCloudName());
			map.put("providerReferenceId", handoffDetails.getProviderReferenceId());
			map.put("au4", handoffDetails.getAu4());
			map.put("klm", handoffDetails.getKlm());
		}

		try{
			//if(cramerKlmdetailsEnabled!=null && "Y".equals(cramerKlmdetailsEnabled)) {
				if(handoffDetails!=null && Objects.nonNull(handoffDetails.getKlm())){
					Map<String, String> queryParams = new HashMap<>();
					String responseJson = null;
					RestResponse restResponse = restClientService.getWithQueryParam(cramerKlmdetailsUrl+ task.getServiceCode(), queryParams, new HttpHeaders());
					LOGGER.info("KLM details response for provideOffnetKlmVlan from cramer : {}",restResponse);
					responseJson = restResponse.getData();
					LOGGER.info("KLM details response for provideOffnetKlmVlan from cramer : {}",responseJson);
					Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
					ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
					if (task.getSiteType().equalsIgnoreCase("A")) {
						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "klmDetails",
								responseJson);
					} else if (task.getSiteType().equalsIgnoreCase("B")){
						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "klmDetails_b",
								responseJson);
					}
				}
			//}
		}catch (Exception e){
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), map, AttributeConstants.COMPONENT_LM,task.getSiteType());

		return  (HandoffDetailsBean) flowableBaseService.taskDataEntry(task, handoffDetails,flowableMap);
	}

	@Transactional(readOnly = false)
	public Map<String, Object> lmJeopardy(Integer taskId,String wfTaskId,Map<String, Object> map) throws TclCommonException{
		Task task = getTaskByIdAndWfTaskId(taskId,wfTaskId);

		Map<String,Object> flowableMap = new HashMap<>();

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("define-offfnet-project-plan")) {
			flowableMap.put("offnetSowAction","LMJEOPARDY");
		}else if (task.getMstTaskDef().getKey().equalsIgnoreCase("track-offnet-lm-delivery")) {
			flowableMap.put("offnetTrackAction","LMJEOPARDY");
		}else if (task.getMstTaskDef().getKey().equalsIgnoreCase("offnet-ss-details")) {
			flowableMap.put("offnetSSAction","LMJEOPARDY");
		}

		flowableBaseService.taskDataEntry(task, map,flowableMap);
		return map;
	}

	/**
	 * @author diksha garg
	 *
	 * @param taskId
	 * @param lmDelivery
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public LmDelivery trackCompleteLmDelivery( LmDelivery lmDelivery) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(lmDelivery.getTaskId(),lmDelivery.getWfTaskId());
		validateInputs(task, lmDelivery);
		updateActualDates(task, lmDelivery);

		if (lmDelivery.getHandoffDetails() != null && lmDelivery.getHandoffDetails().getDocumentIds() != null
				&& !lmDelivery.getHandoffDetails().getDocumentIds().isEmpty()) {
			lmDelivery.getHandoffDetails().getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		if ("close".equalsIgnoreCase(lmDelivery.getStatus())) {
			componentAndAttributeService.updateAttributes(task.getServiceId(),
					constructLmDeliveryDetails(lmDelivery), AttributeConstants.COMPONENT_LM,task.getSiteType());
			processTaskLogDetails(task, "CLOSED", lmDelivery.getDelayReason(), null, lmDelivery);
			return (LmDelivery) flowableBaseService.taskDataEntry(task, lmDelivery);
		} else {
			String beanData = Utils.convertObjectToJson(lmDelivery);
			saveTaskData(beanData, task);
			Map<String, String> map = new HashMap<>();
			if(StringUtils.isNotBlank(lmDelivery.getCableLengthCustomerServerRoom()))map.put("cableLengthCustomerServerRoom", lmDelivery.getCableLengthCustomerServerRoom());
			if(StringUtils.isNotBlank(lmDelivery.getAddtionalHardwareAtCustomer()))map.put("addtionalHardwareAtCustomer", lmDelivery.getAddtionalHardwareAtCustomer());
			if(StringUtils.isNotBlank(lmDelivery.getNeededHardware()))map.put("neededHardware", lmDelivery.getNeededHardware());
			if(StringUtils.isNotBlank(lmDelivery.getNeededHardwareOwner()))map.put("neededHardwareOwner", lmDelivery.getNeededHardwareOwner());
			if(!map.isEmpty()) {
				componentAndAttributeService.updateAttributes(task.getServiceId(),
					constructLmDeliveryDetails(lmDelivery), AttributeConstants.COMPONENT_LM,task.getSiteType());
			}
			return lmDelivery;
		}
	}

	/**
	 * @author vivek
	 * @param handoffDetails
	 * @return
	 */
	private Map<String, String> constructLmDeliveryDetails(LmDelivery lmDelivery) {
		Map<String, String> map = new HashMap<>();
		if (lmDelivery != null) {
			map.put("bsoCircuitId", lmDelivery.getBsoCircuitId());
			map.put("offnetSupplierBillStartDate", lmDelivery.getSupplierBillStartDate());
			map.put("offnetOrderReferenceId", lmDelivery.getProvideOrderreferenceId());
			map.put("cableLengthCustomerServerRoom", lmDelivery.getCableLengthCustomerServerRoom());
			map.put("addtionalHardwareAtCustomer", lmDelivery.getAddtionalHardwareAtCustomer());
			map.put("neededHardware", lmDelivery.getNeededHardware());
			map.put("neededHardwareOwner", lmDelivery.getNeededHardwareOwner());
			map.put("testIp", lmDelivery.getTestIp());
		}
		return map;
	}

	void updateActualDates(Task task, LmDelivery lmDelivery) {
		lmDelivery.getAttributes().stream().forEach(attr -> {
			LOGGER.info("Update activity {}", attr.getKey());
			ActivityPlan activityPlan = activityPlanRepository.findFirstByServiceIdAndMstActivityDefKey(task.getServiceId(),
					attr.getKey(), task.getServiceType()==null?"A":task.getSiteType());
			if (activityPlan != null) {
				if (StringUtils.isNotBlank(attr.getStartDate()))
					activityPlan.setActualStartTime(new Timestamp(DateUtil.convertStringToDateYYMMDD(attr.getStartDate()).getTime()));
				if (StringUtils.isNotBlank(attr.getEndDate()))
					activityPlan.setActualEndTime(new Timestamp(DateUtil.convertStringToDateYYMMDD(attr.getEndDate()).getTime()));

				String status = StringUtils.trimToEmpty(attr.getStatus());
				if (status.equalsIgnoreCase("InProgress")) {
					activityPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));
				} else if (status.equalsIgnoreCase("Completed")) {
					activityPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
				}

				activityPlanRepository.save(activityPlan);
			} else {
				LOGGER.info("ActivityPlan is null for {}", attr.getKey());
			}
		});
	}

	/**
	 * @author diksha garg
	 *
	 * @param taskId
	 * @param offnetSiteSurvey
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public OffnetSiteSurvey offnetSsDetails(OffnetSiteSurvey offnetSiteSurvey)
			throws TclCommonException {


		Task task = getTaskByIdAndWfTaskId(offnetSiteSurvey.getTaskId(),offnetSiteSurvey.getWfTaskId());



		Map<String, String> map = new HashMap<>();
		if (offnetSiteSurvey != null) {
			map.put("offnetMastRequired", offnetSiteSurvey.getMastRequired());
			map.put("offnetFotRequired", offnetSiteSurvey.getFotRequired());
			map.put("offnetMastHeight", String.valueOf(offnetSiteSurvey.getMastHeight()));
			map.put("offnetTypeOfMastAntennaErection", offnetSiteSurvey.getTypeOfMastAntennaErection());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), map, AttributeConstants.COMPONENT_LM,task.getSiteType());

		processTaskLogDetails(task, "CLOSED", offnetSiteSurvey.getDelayReason(), null, offnetSiteSurvey);

		if (offnetSiteSurvey.getDocumentIds() != null && !offnetSiteSurvey.getDocumentIds().isEmpty())
			offnetSiteSurvey.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));

		 return (OffnetSiteSurvey) flowableBaseService.taskDataEntry(task, offnetSiteSurvey);

	}


	/**
	 * @author diksha garg
	 *
	 * @param taskId
	 * @param defineSowProjPlan
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public HandoffDetailsBean selectCloudName(HandoffDetailsBean handoffDetails) throws TclCommonException {

		Task task = getTaskByIdAndWfTaskId(handoffDetails.getTaskId(),handoffDetails.getWfTaskId());

		validateInputs(task, handoffDetails);
		Map<String, String> map = new HashMap<>();
		if (handoffDetails != null) {
			LOGGER.info("cloudName={} ServiceCode={}", handoffDetails.getCloudName(), task.getServiceCode());
			map.put("cloudName",handoffDetails.getCloudName());
		}
		try {
			//if(cramerKlmdetailsEnabled!=null && "Y".equals(cramerKlmdetailsEnabled)) {
				Map<String, String> queryParams = new HashMap<>();
				String responseJson = null;
				RestResponse restResponse = restClientService.getWithQueryParam(cramerKlmdetailsUrl+ task.getServiceCode(), queryParams, new HttpHeaders());
				LOGGER.info("KLM details response from cramer {} :",restResponse);
				if(Objects.nonNull(restResponse) && Objects.nonNull(restResponse.getData())){
				responseJson = restResponse.getData();
				LOGGER.info("KLM details response data from cramer {} :",responseJson);
				Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
				ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
				if (task.getSiteType().equalsIgnoreCase("A")) {
					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "klmDetails",
							responseJson);
				} else if (task.getSiteType().equalsIgnoreCase("B")){
					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "klmDetails_b",
							responseJson);
				}
			  }
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), map, AttributeConstants.COMPONENT_LM, task.getSiteType());
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("action", "close");

		return (HandoffDetailsBean) flowableBaseService.taskDataEntry(task, handoffDetails, wfMap);
	}

	@Transactional(readOnly = false)
	public HandoffDetailsBean provideIORDetail(HandoffDetailsBean handoffDetails) throws TclCommonException {
		Task task =getTaskByIdAndWfTaskId(handoffDetails.getTaskId(),handoffDetails.getWfTaskId());

		validateInputs(task, handoffDetails);

		Map<String, String> map = new HashMap<>();
		if (handoffDetails != null) {
			LOGGER.info("iorId ={} ServiceCode={}",handoffDetails.getIorId (),task.getServiceCode());
			map.put("iorId", handoffDetails.getIorId());
			map.put("nnid", handoffDetails.getIorId());
			map.put("klm", handoffDetails.getKlm());
			map.put("au4", handoffDetails.getAu4());
		}

		componentAndAttributeService.updateAttributes(task.getServiceId(), map, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task, "CLOSED", handoffDetails.getDelayReason(), null, handoffDetails);
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("action", "close");

		return  (HandoffDetailsBean) flowableBaseService.taskDataEntry(task, handoffDetails,wfMap);
	}


	/**
	 * @author Sarath Kumar.M
	 *
	 * @param taskId
	 * @param provideKlmVlanDetails
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ProvideKlmVlanBean provideKlmVlanDetails(ProvideKlmVlanBean provideKlmVlanBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(provideKlmVlanBean.getTaskId(),provideKlmVlanBean.getWfTaskId());

		validateInputs(task, provideKlmVlanBean);

		Map<String, String> map = new HashMap<>();
		if (provideKlmVlanBean.getHandoffDetails() != null) {
			HandoffDetailsBean handoffDetails = provideKlmVlanBean.getHandoffDetails();
			if (handoffDetails != null) {
				map.put("nniId", handoffDetails.getNniId());
				map.put("customerInnerVlan", handoffDetails.getCustomerInnerVlan());
				map.put("handoverType", handoffDetails.getHandoverType());
				map.put("cloudName", handoffDetails.getCloudName());
				map.put("providerReferenceId", handoffDetails.getProviderReferenceId());
				map.put("au4", handoffDetails.getAu4());
				map.put("klm", handoffDetails.getKlm());

			}
		}
		map.put("cableLengthCustomerServerRoom ", provideKlmVlanBean.getCableLengthCustomerServerRoom());
		map.put("addtionalHardwareAtCustomer", provideKlmVlanBean.getAddtionalHardwareAtCustomer());
		map.put("neededHardware", provideKlmVlanBean.getNeededHardware());
		map.put("neededHardwareOwner", provideKlmVlanBean.getNeededHardwareOwner());

		componentAndAttributeService.updateAttributes(task.getServiceId(), map, AttributeConstants.COMPONENT_LM,task.getSiteType());
		processTaskLogDetails(task, "CLOSED", provideKlmVlanBean.getDelayReason(), null, provideKlmVlanBean);
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("action", "close");

		return  (ProvideKlmVlanBean) flowableBaseService.taskDataEntry(task, provideKlmVlanBean,wfMap);
	}

	/**
	 * @author diksha garg
	 *
	 * @param taskId
	 * @param confirmSupplierConfiguration
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public ConfirmSupplierConfiguration confirmSupplierConfiguration(ConfirmSupplierConfiguration confirmSupplierConfiguration) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(confirmSupplierConfiguration.getTaskId(),confirmSupplierConfiguration.getWfTaskId());
		validateInputs(task, confirmSupplierConfiguration);

		Map<String, String> map = new HashMap<>();
		map.put("bsoCircuitId", confirmSupplierConfiguration.getSupplierBsoCircuitId());

		componentAndAttributeService.updateAttributes(task.getServiceId(), map, AttributeConstants.COMPONENT_LM,
				task.getSiteType());
		processTaskLogDetails(task, "CLOSED", confirmSupplierConfiguration.getDelayReason(), null, confirmSupplierConfiguration);

		return (ConfirmSupplierConfiguration) flowableBaseService.taskDataEntry(task, confirmSupplierConfiguration);
	}

}
