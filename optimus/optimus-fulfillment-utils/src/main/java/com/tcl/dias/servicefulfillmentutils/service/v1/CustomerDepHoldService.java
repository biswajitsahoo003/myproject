package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.servicefulfillment.entity.entities.NotificationTrigger;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskRemark;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceStatusDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRemarkRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.SalesNegotiationConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Service
@Transactional(readOnly = true)
public class CustomerDepHoldService extends ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDepHoldService.class);

	@Autowired
	TaskService taskService;

	@Autowired
	TaskRemarkRepository taskRemarkRepository;

	@Autowired
	protected TaskCacheService taskCacheService;

	@Autowired
	ServiceStatusDetailsRepository serviceStatusDetailsRepository;

	@Autowired
	protected UserInfoUtils userInfoUtils;

	@Autowired
	NotificationService notificationService;

	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void notifyForHoldAndReleaseResources(ScServiceDetail scServiceDetail, Task task, Integer serviceId, String customerEmail,
			String customerName, String customerUrl, List<String> toAddresses, List<String> ccAddresses,
			NotificationTrigger notificationTrigger) throws TclCommonException {

			notificationTrigger
					.setDayLogger(notificationTrigger.getDayLogger() + "& Inside notifyForHoldAndReleaseResources");

			if (scServiceDetail.getMstStatus() != null && scServiceDetail.getMstStatus().getCode() != null
					&& scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)) {

				Boolean isEligible = checkOnHoldEligibility(serviceId);
				if (isEligible) {
					LOGGER.info("Inside eligible block for service Id {} isEligible", serviceId);
					saveTaskRemarks(scServiceDetail);
					triggerReleaseResourceWorkflow(scServiceDetail,SalesNegotiationConstants.CUSTOMERDEPENDENCY,SalesNegotiationConstants.CUSTOMERTASKDELAYREASON);
					LOGGER.info("Inside notifyOverDueReminderCustomerOnHold for service Id {} for notification",
							serviceId);
					notificationService.notifyOverDueReminderCustomerOnHold(customerEmail, task.getScOrderId(),
							customerName, task.getServiceCode(), customerUrl, task.getMstTaskDef().getName(),
							task.getOrderCode(), toAddresses, ccAddresses, notificationTrigger);

				} else {
					LOGGER.info("Inside non eligible block for service Id {} is not Eligible", serviceId);
				}
			}
			else {
				LOGGER.info("Inside service ID in some other status service Id {} is not Eligible", serviceId);

			}

	}

	private Boolean checkOnHoldEligibility(Integer serviceId) {
		LOGGER.info("Inside checkOnHoldEligibility for service Id {} ", serviceId);

		Boolean isEligible = false;
		List<String> mstTastDefKey = Arrays.asList("sales-negotiation", "sales-negotiation-waiting-task","product-commissioning-jeopardy","sales-assist-ordering","customer-hold-negotaition-CIM","sales-negotiation-SALES");
		List<String> mstStatus = Arrays.asList(TaskStatusConstants.OPENED, TaskStatusConstants.INPROGRESS,
				TaskStatusConstants.REOPEN);
		List<Task> tasksSc = taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(serviceId,
				mstTastDefKey, mstStatus);
		if (tasksSc == null || tasksSc.isEmpty()) {
			isEligible = true;
		}

		LOGGER.info("Executed checkOnHoldEligibility for service Id {} with value isEligible {} ", serviceId,
				isEligible);
		return isEligible;
	}

	public boolean is30DayHoldTrigger(Task task) {
		return !task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD) && task.getDuedate() != null
				&& LocalDateTime.now().isAfter(task.getDuedate().toLocalDateTime())
				&& ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()) >= 30;
	}

	public boolean is24DayTrigger(Task task) {
		return !task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD) && task.getDuedate() != null
				&& LocalDateTime.now().isAfter(task.getDuedate().toLocalDateTime())
				&& ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()) > 24
				&& ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()) <= 29;
	}

	private void saveTaskRemarks(ScServiceDetail scServiceDetail) {
		LOGGER.info("Inside saveTaskRemarks for service Id {} and service code {}", scServiceDetail.getId(),
				scServiceDetail.getUuid());

		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks("Customer task was open for more than 30 days so automated Resource Release of service Id started");
		taskRemark.setUsername("SYSTEM");
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}

	public  void triggerReleaseResourceWorkflow(ScServiceDetail scServiceDetail,String onHoldCategory,String holdReason)
			throws TclCommonException {
		LOGGER.info(
				"Inside setServiceOnHoldReasonCategoryAndTriggerSalesNegotiation for service Id {} and service code {}",
				scServiceDetail.getId(), scServiceDetail.getUuid());

		scServiceDetail.setOnHoldCategory(onHoldCategory);
		scServiceDetail.setHoldReason(holdReason);
		boolean resourceReleaseRequired=releaseResourceFlowRequire(scServiceDetail.getId());
		if(resourceReleaseRequired) {
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.RESOURCE_RELEASED_INITIATED,
					onHoldCategory);
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.RESOURCE_RELEASED_INITIATED));
		}
		taskService.processReleaseResources(scServiceDetail, SalesNegotiationConstants.AUTORESOURCECANCELLATION, holdReason, true, true,resourceReleaseRequired,false);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public  Map<String, Object>  addReleaseResourceVariableWorkflow(ScServiceDetail scServiceDetail,String onHoldCategory,String holdReason,String cancellationInitaitedBy)
			throws TclCommonException {
		LOGGER.info(
				"Inside setServiceOnHoldReasonCategoryAndTriggerSalesNegotiation for service Id {} and service code {}",
				scServiceDetail.getId(), scServiceDetail.getUuid());

		if(onHoldCategory!=null) {
		scServiceDetail.setOnHoldCategory(onHoldCategory);
		}
		if(holdReason!=null) {
		scServiceDetail.setHoldReason(holdReason);
		}
		updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.RESOURCE_RELEASED_INITIATED,
				onHoldCategory);
		scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.RESOURCE_RELEASED_INITIATED));
		Map<String, Object> processVar = new HashMap<>();
		processVar.put("retainExistingNwresource", "No");
		processVar.put("cancellationInitiatedBy", cancellationInitaitedBy);
		processVar.put("cpeFlowRequired","No");
		processVar.put("isConfirmIPCancel",false);
		processVar.put("isCancelTxConfig",false);
		processVar.put("isRFExists",false);
		processVar.put("isCancelClr",false);
		processVar.put("csmTaskRequired", false);
		processVar.put("isBillingRequired", false);


		
		
			isEligibleForCancellation(scServiceDetail.getId(), processVar);

			LOGGER.info("Cancellation service details {}", scServiceDetail.getId());
			Map<String, String> atMap = new HashMap<>();
			atMap.put("customerRequestorDate",DateUtil.convertDateToString(new Date()));
			atMap.put("cancellationFlowTriggered", CommonConstants.YES);
			atMap.put("cancellationReason", holdReason);
			atMap.put("retainExistingNwresource", "No");
			atMap.put("cancellationInitiatedBy", cancellationInitaitedBy);

			saveRemarksForCancellation(scServiceDetail, true);
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
					AttributeConstants.COMPONENT_LM, "A");
		LOGGER.info("inside processCancellationFlowVariable with service id : {} ", scServiceDetail.getId());
		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
				.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

		processVar.put("customerUserName",
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getErfCustCustomerName()));
		processVar.put(MasterDefConstants.ORDER_CODE,
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
		processVar.put(MasterDefConstants.ORDER_TYPE,
				StringUtils.trimToEmpty(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CATEGORY,
				StringUtils.trimToEmpty(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CREATED_DATE, scServiceDetail.getScOrder().getOrderStartDate());
		LOGGER.info("inside processCancellationFlowVariable with order details : {} {} {}",
				scServiceDetail.getScOrder().getOpOrderCode(),scServiceDetail.getScOrder().getOrderType(),
				scServiceDetail.getScOrder().getOrderCategory());
		processVar.put(MasterDefConstants.PRODUCT_NAME,
				StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
		if (scServiceDetail.getErfPrdCatalogProductName().contains(CommonConstants.NPL)
				|| scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(CommonConstants.NPL)) {
			processVar.put("productType", StringUtils.trimToEmpty(CommonConstants.NPL));
		}
		processVar.put(MasterDefConstants.OFFERING_NAME,
				StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
		processVar.put(MasterDefConstants.SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
		processVar.put(MasterDefConstants.SERVICE_ID, scServiceDetail.getId());
		processVar.put(MasterDefConstants.SITE_ADDRESS,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
		LOGGER.info("inside processCancellationFlowVariable with service details : {} {} {} {}",
				scServiceDetail.getId(), scServiceDetail.getScOrder().getOrderType(),
				scComponentAttributesAMap.get("siteAddress"), scServiceDetail.getScOrder().getOrderCategory());
		processVar.put(MasterDefConstants.CITY,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
		processVar.put(MasterDefConstants.STATE,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
		processVar.put(MasterDefConstants.COUNTRY,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_MOBILE,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_NAME,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
		LOGGER.info("inside processCancellationFlowVariable with local IT details : {} {}",
				scComponentAttributesAMap.get("localItContactName"), scComponentAttributesAMap.get("destinationCity"));
		String lastMileType = Objects.nonNull(scComponentAttributesAMap.get("lmType"))
				? StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"))
				: null;
		processVar.put(MasterDefConstants.LM_TYPE, lastMileType);
		processVar.put(MasterDefConstants.LM_CONNECTION_TYPE,
				Objects.nonNull(scComponentAttributesAMap.get("lmConnectionType"))
						? StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmConnectionType"))
						: null);
		processVar.put("lastMileScenario",
				Objects.nonNull(scComponentAttributesAMap.get("lastMileScenario"))
						? StringUtils.trimToEmpty(scComponentAttributesAMap.get("lastMileScenario"))
						: null);
		LOGGER.info("inside processCancellationFlowVariable with last mile details : {} {} {}",
				lastMileType, scComponentAttributesAMap.get("lmConnectionType"),scComponentAttributesAMap.get("lastMileScenario"));
		processVar.put("cancellationFlowTriggered", "Yes");
		processVar.put("remainderCycle", "R60/PT24H");
		if (Objects.nonNull(lastMileType) && !lastMileType.isEmpty()) {
			if (lastMileType.toLowerCase().contains("offnet")) {
				processVar.put("parentLmType", "offnet");
			} else {
				processVar.put("parentLmType", lastMileType);
			}
		}
		processVar.put("isBillingRequired", false);

		
		return processVar;

	}
	
	/**
	 * @author vivek
	 *
	 * @param id
	 * @param processVar,Integer serviceId 
	 * @throws TclCommonException 
	 */
	private void isEligibleForCancellation(Integer serviceId, Map<String, Object> processVar) throws TclCommonException {
		
		List<Task> tasks=taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(serviceId, Arrays.asList("product-commissioning-jeopardy"), Arrays.asList(TaskStatusConstants.OPENED,TaskStatusConstants.CLOSED_STATUS,TaskStatusConstants.INPROGRESS,TaskStatusConstants.REOPEN));

		if(!tasks.isEmpty()) {
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_ELIGIBLE_FOR_ORDER_AMENDMENT,
					ResponseResource.R_CODE_NOT_FOUND);
		}
	
		if(Objects.nonNull(processVar.get("retainExistingNwresource")) &&
				CommonConstants.YES.equalsIgnoreCase(String.valueOf(processVar.get("retainExistingNwresource")))) {
			
			processVar.put("cpeFlowRequired","No");
			processVar.put("isConfirmIPCancel",false);
			processVar.put("isCancelTxConfig",false);
			processVar.put("isRFExists",false);
			processVar.put("isCancelClr",false);
		} else {

			Map<String, Boolean> processVarForCancelFlow = cancelationFlow(serviceId);
			if (Objects.nonNull(processVarForCancelFlow) && !processVarForCancelFlow.isEmpty()) {
				if (processVarForCancelFlow.containsKey("bop") && processVarForCancelFlow.get("bop") == true) {
					LOGGER.info("isCancelClr service details true");
					processVar.put("isCancelClr", true);
				}
				if (processVarForCancelFlow.containsKey("cmip") && processVarForCancelFlow.get("cmip") == true) {
					LOGGER.info("isIPCancel service details true");
					processVar.put("isIPCancel", true);
				}
				if (processVarForCancelFlow.containsKey("rfnoc") && processVarForCancelFlow.get("rfnoc") == true) {
					LOGGER.info("isRFExists service details true");
					processVar.put("isRFExists", true);
				}
				if (processVarForCancelFlow.containsKey("sdnoc") && processVarForCancelFlow.get("sdnoc") == true) {
					LOGGER.info("isCancelTxConfig service details true");
					processVar.put("isCancelTxConfig", true);
				}
				if (processVarForCancelFlow.containsKey("asp") && processVarForCancelFlow.get("asp") == true) {
					LOGGER.info("isAspTask service details true");
					processVar.put("isAspTask", true);
				}
				if (processVarForCancelFlow.containsKey("scmml") && processVarForCancelFlow.get("scmml") == true) {
					LOGGER.info("cpeFlowRequired service details true");
					processVar.put("cpeFlowRequired", CommonConstants.YES);
				}
			}

		}
		
		List<Task> holdTask=taskRepository.findByServiceIdAndMstStatus_codeIn(serviceId, Arrays.asList(TaskStatusConstants.OPENED,TaskStatusConstants.INPROGRESS,TaskStatusConstants.REOPEN));

		if(!holdTask.isEmpty()) {
			holdTask.forEach(hold->{
				if(!hold.getMstTaskDef().getKey().equalsIgnoreCase("sales-negotiation")) {
					hold.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
				}
				taskRepository.save(hold);
			});
		}
		
	}

	public Map<String, Boolean> cancelationFlow(Integer serviceId) {
		Map<String, Boolean> cancelMap = new HashMap<>();
		List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_codeIn(serviceId,
				Arrays.asList(TaskStatusConstants.CLOSED_STATUS));
		List<String> status = tasks.stream().map(t -> t.getMstTaskDef().getKey()).collect(Collectors.toList());
		if (status.contains("enrich-service-design") || status.contains("enrich-service-design-jeopardy")) {
			cancelMap.put("bop", true);
		}
		if (status.contains("manual-service-configuration") || status.contains("service-configuration")) {
			cancelMap.put("cmip", true);
		}
		if (status.contains("manual-rf-configuration") || status.contains("rf-configuration")) {
			cancelMap.put("rfnoc", true);
		}
		if (status.contains("tx-configuration-sdh")||status.contains("tx-configuration-mpls")||status.contains("tx-configurations")||status.contains("tx-sdh-configuration-manual") || status.contains("tx-mpls-configuration-manual")) {
			cancelMap.put("sdnoc", true);
		}
		
		if (status.contains("offnet-po") || status.contains("provide-po-colo") || status.contains("po-offnet-lm-provider") ) {
			cancelMap.put("asp", true);
		}
		if(status.contains("cpe-license-pr") || status.contains("cpe-hardware-pr")
			|| status.contains("provide-wbsglcc-details")){
			cancelMap.put("scmml", true);
		}

		return cancelMap;
	}
	private void saveRemarksForCancellation(ScServiceDetail scServiceDetail,boolean autoCancelation) {
		String userName = "";
		if(autoCancelation) {
			userName="AUTOCANCELLATION";
		}
		else {
			userName = userInfoUtils.getUserInformation().getUserId();

		}

		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		
			taskRemark.setRemarks("Cancellation request on :" + new Date() + " by user :" + userName);

		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}
	
	public boolean releaseResourceFlowRequire(Integer serviceId) throws TclCommonException {
		
		List<Task> billingTask=taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(serviceId, Arrays.asList("product-commissioning-jeopardy"), Arrays.asList(TaskStatusConstants.OPENED,TaskStatusConstants.CLOSED_STATUS,TaskStatusConstants.INPROGRESS,TaskStatusConstants.REOPEN));

		if(!billingTask.isEmpty()) {
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_ELIGIBLE_FOR_ORDER_AMENDMENT,
					ResponseResource.R_CODE_NOT_FOUND);
		}
		List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_codeIn(serviceId,
				Arrays.asList(TaskStatusConstants.CLOSED_STATUS));
		List<String> status = tasks.stream().map(t -> t.getMstTaskDef().getKey()).collect(Collectors.toList());
		if (status.contains("enrich-service-design") || status.contains("enrich-service-design-jeopardy")) {
			return true;
		}
		if (status.contains("manual-service-configuration") || status.contains("service-configuration")) {
			return true;
		}
		if (status.contains("manual-rf-configuration") || status.contains("rf-configuration")) {
			return true;
		}
		if (status.contains("tx-configuration-sdh")||status.contains("tx-configuration-mpls")||status.contains("tx-configurations")||status.contains("tx-sdh-configuration-manual") || status.contains("tx-mpls-configuration-manual")) {
			return true;
		}
		
		if (status.contains("offnet-po") || status.contains("provide-po-colo") || status.contains("po-offnet-lm-provider") ) {
			return true;
		}
		if(status.contains("cpe-license-pr") || status.contains("cpe-hardware-pr")
			|| status.contains("provide-wbsglcc-details")){
			return true;
		}

		return false;
	}
}
