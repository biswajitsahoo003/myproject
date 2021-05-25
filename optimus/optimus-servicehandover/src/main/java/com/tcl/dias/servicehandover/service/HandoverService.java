package com.tcl.dias.servicehandover.service;

import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillmentutils.beans.*;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * LmService this class is used to perform last mile implementation related
 * tasks.
 * 
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited LmService used for the lm related
 *            implementation endpoint
 */

@Service
@Transactional(readOnly = true)
public class HandoverService extends ServiceFulfillmentBaseService{
	
	private static final Logger logger = LoggerFactory.getLogger(HandoverService.class);
	public static final String UA="\u00A0";

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	MstTaskDefRepository mstTaskDefRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
			
	@Autowired
	RuntimeService runtimeService;	
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	FlowableBaseService flowableBaseService;
	  
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;

	@Autowired
	MQUtils mqUtils;

	@Value("${queue.rf.e2e.inventory}")
	String rfE2eInventoryQueue;

	 /**
		 * Service Acceptance task.
		 * @param taskId
		 * @param serviceAcceptanceBean
		 * @return
		 * @throws TclCommonException
		 */
		@Transactional(readOnly = false)
		public ServiceAcceptanceBean serviceAcceptance( ServiceAcceptanceBean serviceAcceptanceBean) throws TclCommonException {
			Task task = getTaskByIdAndWfTaskId(serviceAcceptanceBean.getTaskId(), serviceAcceptanceBean.getWfTaskId());
			validateInputs(task, serviceAcceptanceBean);
			
			Map<String, String> atMap = new HashMap<>();
			
			String issueType = StringUtils.trimToEmpty(serviceAcceptanceBean.getIssueType());
			atMap.put("isServiceAccepted", String.valueOf(serviceAcceptanceBean.getServiceAccepted()));
			atMap.put("serviceAcceptanceIssueType",issueType);
			
			Map<String, Object> wfMap = new HashMap<>();
			wfMap.put("serviceAccepted", serviceAcceptanceBean.getServiceAccepted());
			wfMap.put("serviceAcceptanceIssueType",issueType);
						
			if(!serviceAcceptanceBean.getServiceAccepted()) {
				String errorMessage = serviceAcceptanceBean.getIssueDescription();
				Optional<ScServiceDetail> scServiceDetail=	scServiceDetailRepository.findById(task.getServiceId());
				if (scServiceDetail.isPresent() && StringUtils.isNotBlank(errorMessage)) {					

					String errorKey ="billingIssueReason";
					if(issueType.contains("Service")) errorKey ="serviceIssueReason";
					else if(issueType.contains("Turnup")) errorKey ="turnupIssueReason";

					if(task.getOrderCode()!=null && task.getOrderCode().toLowerCase().contains("izosdwan")){
						logger.info("serviceAcceptance for Sdwan");
						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
								errorKey, componentAndAttributeService.getErrorMessageDetails(errorMessage, serviceAcceptanceBean.getIssueType()),
								AttributeConstants.ERROR_MESSAGE, "sdwan-service-acceptance");
					}else {
						componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
								errorKey, componentAndAttributeService.getErrorMessageDetails(errorMessage, serviceAcceptanceBean.getIssueType()),
								AttributeConstants.ERROR_MESSAGE, "service-acceptance");
					}
					
					processTaskLogDetails(task,"CLOSED",serviceAcceptanceBean.getIssueType(),null, serviceAcceptanceBean);

				}
			}else {
				atMap.put("customerAcceptanceDate",DateUtil.convertDateToString(new Date()));
				atMap.put("deemedAcceptance", "No");
				processTaskLogDetails(task,"CLOSED","Accepted by Customer",null, serviceAcceptanceBean);
				
				if(task.getOrderCode()!=null && (task.getOrderCode().toLowerCase().contains("ias") || task.getOrderCode().toLowerCase().contains("gvpn"))){
					ScComponentAttribute cpeManagementTypeAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(task.getServiceId(),"cpeManagementType","LM","A");
					if(cpeManagementTypeAttribute!=null && cpeManagementTypeAttribute.getAttributeValue()!=null && (cpeManagementTypeAttribute.getAttributeValue().toLowerCase().contains("proactive")
							|| cpeManagementTypeAttribute.getAttributeValue().toLowerCase().contains("config") || cpeManagementTypeAttribute.getAttributeValue().toLowerCase().contains("hardware"))) {
						Map<String, String> serviceMap = new HashMap<>();
						serviceMap.put("CPE Basic Chassis",null);
						serviceMap.put("cpe_chassis_changed","No");
						serviceMap.put("cpe_variant",null);
						componentAndAttributeService.updateServiceAttributesWithNull(task.getServiceId(), serviceMap);
					}
				}
			}
			
			if (serviceAcceptanceBean.getDocumentIds() != null && !serviceAcceptanceBean.getDocumentIds().isEmpty()) {
				serviceAcceptanceBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
			
			if(task.getOrderCode()!=null && task.getOrderCode().toLowerCase().contains("izosdwan")){
				 logger.info("IZOSDWAN Order {}", task.getServiceId());
				if(Objects.nonNull(serviceAcceptanceBean.getUnderlayDetails()) && !serviceAcceptanceBean.getUnderlayDetails().isEmpty()){
					logger.info("Service Acceptance Underlay Exists {}", task.getServiceId());
					serviceAcceptanceBean.getUnderlayDetails().forEach(underlay -> {
						componentAndAttributeService.updateAttributes(underlay.getServiceId(), atMap,
								AttributeConstants.COMPONENT_LM,"A");
						Optional<ScServiceDetail> scServiceDetailOpUnderlay = scServiceDetailRepository.findById(underlay.getServiceId());
						if (serviceAcceptanceBean.getDocumentIds() != null && !serviceAcceptanceBean.getDocumentIds().isEmpty()) {
							serviceAcceptanceBean.getDocumentIds()
	                                .forEach(attachmentIdBean -> makeEntryInScAttachment(scServiceDetailOpUnderlay.get(), attachmentIdBean.getAttachmentId()));
	                        logger.info("ServiceAcceptance document id persisted for underlay:{}", underlay.getServiceId());
	                    }

						if("BYON Internet".equalsIgnoreCase(underlay.getProductName())){
							logger.info("Byon underlay: {} for overlay serviceID: {}",underlay.getServiceId(),task.getServiceId());
							if (underlay.getDocumentIds() != null && !underlay.getDocumentIds().isEmpty()) {
								for(AttachmentIdBean attachmentIdBean:underlay.getDocumentIds()) {
									Optional<Attachment> attachmentOptional = attachmentRepository.findById(attachmentIdBean.getAttachmentId());
									if(attachmentOptional.isPresent()){
										logger.info("Escalation Matrix Exists");
										Attachment attachment=attachmentOptional.get();
										List<ScAttachment> scAttachmentList=scAttachmentRepository.findAllByScServiceDetail_IdAndAttachment
												(underlay.getServiceId(),attachment);
										if(scAttachmentList==null || scAttachmentList.isEmpty()){
											logger.info("Escalation Matrix persisted");
											makeEntryInScAttachment(scServiceDetailOpUnderlay.get(), attachmentIdBean.getAttachmentId()); //diff method
										}
									}
								}
							}
						}
						logger.info("serviceAcceptance persisted underlay component attribute: {}",underlay.getServiceId());
					});
				}
				
				if(serviceAcceptanceBean.getServiceAccepted()){
					 logger.info("Service Accepted for Service Id::{}",task.getServiceId());
					ScSolutionComponent isOverlaySolutionComponent=scSolutionComponentRepository.findByScServiceDetail1_idAndOrderCodeAndIsActive(task.getServiceId(),task.getOrderCode(),"Y");
					if(isOverlaySolutionComponent!=null && isOverlaySolutionComponent.getComponentGroup().equalsIgnoreCase("OVERLAY")){
						logger.info("Service Accepted for Overlay Service Id::{}",task.getServiceId());
						List<Integer> underlayServiceList=scSolutionComponentRepository.findUnderlayServiceIdByParentServiceId(task.getServiceId(),"Y");
						if(underlayServiceList!=null && !underlayServiceList.isEmpty()){
							logger.info("Underlay Service Exists::{}",underlayServiceList.size());
							for(Integer underlayServiceId:underlayServiceList){
								logger.info("Underlay Service Id::{}",underlayServiceId);
								 List<Task> underlayAcceptanceWaitTasks=taskRepository.findByServiceIdAndMstTaskDef_keyAndMstStatus_codeNot(underlayServiceId, "sdwan-underlay","CLOSED");
								 for(Task underlayAcceptanceWaitTask: underlayAcceptanceWaitTasks){
									 Execution execution = runtimeService.createExecutionQuery().processInstanceId(underlayAcceptanceWaitTask.getWfProcessInstId())
												.activityId("sdwan-underlay").singleResult();
									 if(execution!=null){
										 logger.info("Service Acceptance Execution exists for task Id::{},service Id::{},with excution id::{}",underlayAcceptanceWaitTask.getId(),underlayAcceptanceWaitTask.getServiceId(),execution.getId());
										 runtimeService.trigger(execution.getId());
									 }
								 }
							}
						}
					}
				}
			}
			return (ServiceAcceptanceBean) flowableBaseService.taskDataEntry(task, serviceAcceptanceBean,wfMap);
		}
	

		/**
		 * @author diksha garg
		 * 
		 * @param taskId
		 * @param serviceHandoverBean
		 * @return
		 * @throws TclCommonException
		 */
	@Transactional(readOnly = false)
	public ServiceHandoverBean serviceHandover(ServiceHandoverBean serviceHandoverBean)
			throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(serviceHandoverBean.getTaskId(), serviceHandoverBean.getWfTaskId());
		validateInputs(task, serviceHandoverBean);

		Map<String, Object> fMap = new HashMap<>();
		fMap.put("serviceHandoverAction", serviceHandoverBean.getServiceHandoverAction());
		if(task.getMstTaskDef().getKey().equals("service-handover-SALES_ASSIST")||
				task.getMstTaskDef().getKey().equalsIgnoreCase("service-handover-OSP")||
				task.getMstTaskDef().getKey().equalsIgnoreCase("service-handover-CIM")) {
			Map<String, String> atMap = new HashMap<>();
			atMap.put("demarcationBuildingName", serviceHandoverBean.getDemarcationBuildingName());
			atMap.put("demarcationFloor", serviceHandoverBean.getDemarcationFloor());
			atMap.put("demarcationRoom", serviceHandoverBean.getDemarcationRoom());
			atMap.put("demarcationWing", serviceHandoverBean.getDemarcationWing());
			atMap.put("cpeAmcEndDate", serviceHandoverBean.getCpeAmcEndDate());
			atMap.put("cpeSerialNumber", serviceHandoverBean.getCpeSerialNumber());
			atMap.put("cpeAmcStartDate", serviceHandoverBean.getCpeAmcStartDate());
			atMap.put("localItContactName", serviceHandoverBean.getLocalItContactName());
			atMap.put("localItContactMobile", serviceHandoverBean.getLocalItContactMobile());
			atMap.put("localItContactEmailId", serviceHandoverBean.getLocalItContactEmailId());
			componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		}
		if (!CollectionUtils.isEmpty(serviceHandoverBean.getDependencyDetails()))
			saveTeamErrorAsComponentAdditionalAttributes(task, serviceHandoverBean.getDependencyDetails());
		if (serviceHandoverBean.getServiceHandoverAction().equalsIgnoreCase("CLOSE")) {
			processTaskLogDetails(task, "CLOSED", serviceHandoverBean.getServiceHandoverAction(), null, serviceHandoverBean);
			if (serviceHandoverBean.getDocumentIds() != null && !serviceHandoverBean.getDocumentIds().isEmpty()) {
				serviceHandoverBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}

			handoverServicePushtoRfInventory(task);
			return (ServiceHandoverBean) flowableBaseService.taskDataEntry(task, serviceHandoverBean, fMap);
		} else {
			processTaskLogDetails(task, "HOLD", serviceHandoverBean.getServiceHandoverAction(), null, serviceHandoverBean);
			
			return (ServiceHandoverBean) flowableBaseService.taskDataEntryHold(task, serviceHandoverBean, fMap);
		}
	}
	
	
	@Transactional(readOnly = false)
	public void handoverServicePushtoRfInventory(Task task) {
		try{

			String request= Utils.convertObjectToJson(new HashMap<String,String>(){{ put("SERVICE_ID", task.getServiceCode());}});
			mqUtils.send(rfE2eInventoryQueue, request);
			logger.info("handoverServicePushtoRfInventory serviceCode={} rfE2eInventoryQueue={}", task.getServiceCode(), request);
		} catch (TclCommonException e) {
			logger.error("handoverServicePushtoRfInventoryException {} ", e);
			e.printStackTrace();
		}
	}

	private void saveTeamErrorAsComponentAdditionalAttributes(Task task, List<DependencyDetails> dependencyDetails) {
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
		scServiceDetail.ifPresent(serviceDetail -> {
			dependencyDetails.forEach(dependency -> {
				String attributeName = "serviceHandoverAction" + dependency.getTeam() + "Error";
				try {
					componentAndAttributeService.updateAdditionalAttributes(serviceDetail, attributeName, componentAndAttributeService.getErrorMessageDetails(dependency.getDescription(), "0000"),
							AttributeConstants.ERROR_MESSAGE, task.getMstTaskDef().getKey());
				} catch (TclCommonException e) {
					logger.error("Exception occurred in saveTeamErrorAsComponentAdditionalAttributes : {}", e);
				}
			});
		});
	}


	@Transactional(readOnly = false)
	    public ProvideHandoverBean provideHandoverNote(ProvideHandoverBean provideHandoverBean) throws TclCommonException {
	        Task task = getTaskByIdAndWfTaskId(provideHandoverBean.getTaskId(), provideHandoverBean.getWfTaskId());
	        validateInputs(task, provideHandoverBean);
	        processTaskLogDetails(task,"CLOSED",provideHandoverBean.getServiceIssue(),null, provideHandoverBean);
	        return (ProvideHandoverBean) flowableBaseService.taskDataEntry(task, provideHandoverBean);
	    }

	@Transactional(readOnly = false)
	public ExperienceSurveyBean experienceSurvey(ExperienceSurveyBean experienceSurveyBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(experienceSurveyBean.getTaskId(),experienceSurveyBean.getWfTaskId());
		validateInputs(task, experienceSurveyBean);
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS,experienceSurveyBean.getDelayReason(),null, experienceSurveyBean);
		return (ExperienceSurveyBean) flowableBaseService.taskDataEntry(task, experienceSurveyBean);
	}

	@Transactional(readOnly = false)
	public TerminateBillingAccBean terminateExistingBillingAccount(TerminateBillingAccBean terminateBillingAccBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(terminateBillingAccBean.getTaskId(), terminateBillingAccBean.getWfTaskId());
		validateInputs(task, terminateBillingAccBean);
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS,terminateBillingAccBean.getDelayReason(),null, terminateBillingAccBean);
		return (TerminateBillingAccBean) flowableBaseService.taskDataEntry(task, terminateBillingAccBean);
	}

	@Transactional(readOnly = false)
	public SdwanServiceHandoverBean sdwanServiceHandover(SdwanServiceHandoverBean serviceHandoverBean)
			throws TclCommonException {
		logger.info("sdwanServiceHandover method invoked::{}", serviceHandoverBean);
		Task task = getTaskByIdAndWfTaskId(serviceHandoverBean.getTaskId(), serviceHandoverBean.getWfTaskId());
		validateInputs(task, serviceHandoverBean);

		Map<String, Object> fMap = new HashMap<>();
		if(serviceHandoverBean.getServiceHandoverAction().contains("CMIP_CAT3_CAT4")){
		     fMap.put("serviceHandoverAction", serviceHandoverBean.getServiceHandoverAction().replace("CMIP_CAT3_CAT4","CAT3_CAT4"));
		}else{
			 fMap.put("serviceHandoverAction", serviceHandoverBean.getServiceHandoverAction());
		}
		logger.info(
				"Assurance Handover for taskd Id::{},with fmap::{}",task.getId(),fMap);
		if (task.getMstTaskDef().getKey().equals("sdwan-service-handover-SALES_ASSIST")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-service-handover-OSP")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-service-handover-CIM")) {
			logger.info("Sdwan Handover Task::{}", task.getMstTaskDef().getKey());
			if (serviceHandoverBean.getUnderlayBeans() != null && !serviceHandoverBean.getUnderlayBeans().isEmpty()) {
				logger.info("Sdwan Underlay Exists Id::{}", serviceHandoverBean.getUnderlayBeans().size());
				for (UnderlayBean underlayBean : serviceHandoverBean.getUnderlayBeans()) {
					logger.info("Sdwan Underlay Service Id::{}", underlayBean.getServiceId());
					if("IAS".equalsIgnoreCase(underlayBean.getProductName()) || ("GVPN".equalsIgnoreCase(underlayBean.getProductName()) && underlayBean.getCountry().equalsIgnoreCase("India"))){
						logger.info("Product Name::{}", underlayBean.getProductName());
						Map<String, String> atMap = new HashMap<>();
						atMap.put("demarcationBuildingName", underlayBean.getDemarcationBuildingName());
						atMap.put("demarcationFloor", underlayBean.getDemarcationFloor());
						atMap.put("demarcationRoom", underlayBean.getDemarcationRoom());
						atMap.put("demarcationWing", underlayBean.getDemarcationWing());
						atMap.put("localItContactName", underlayBean.getLocalItContactName());
						atMap.put("localItContactMobile", underlayBean.getLocalItContactMobile());
						atMap.put("localItContactEmailId", underlayBean.getLocalItContactEmailId());
						componentAndAttributeService.updateAttributes(underlayBean.getServiceId(), atMap,
								AttributeConstants.COMPONENT_LM, task.getSiteType());
					}
					if (underlayBean.getDocumentIds() != null && !underlayBean.getDocumentIds().isEmpty()) {
						logger.info("Sdwan Underlay Document Exists::{}", underlayBean.getServiceId());
						Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository
								.findById(underlayBean.getServiceId());
						if (scServiceDetailOptional.isPresent()) {
							logger.info("ScServiceDetail Exists");
							underlayBean.getDocumentIds()
									.forEach(attachmentIdBean -> makeEntryInScAttachment(scServiceDetailOptional.get(),
											attachmentIdBean.getAttachmentId()));
						}
					}
				}
			}
			if (task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-service-handover-CIM")
					&& serviceHandoverBean.getCpeDetailBeans() != null
					&& !serviceHandoverBean.getCpeDetailBeans().isEmpty()) {
				logger.info("Sdwan Cpe Exists Id::{}", serviceHandoverBean.getCpeDetailBeans().size());
				for (CpeInstallationBean cpeInstallationBean : serviceHandoverBean.getCpeDetailBeans()) {
					logger.info("Sdwan ServiceComponent Id::{}", cpeInstallationBean.getComponentId());
					Map<String, String> atMap = new HashMap<>();
					atMap.put("localItContactName", cpeInstallationBean.getLocalItContactName());
					atMap.put("localItContactMobile", cpeInstallationBean.getLocalContactNumber());
					atMap.put("localItContactEmailId", cpeInstallationBean.getLocalContactEmailId());
					atMap.put("cpeAmcEndDate", cpeInstallationBean.getCpeAmcEndDate());
					atMap.put("cpeSerialNumber", cpeInstallationBean.getCpeSerialNumber());
					atMap.put("cpeAmcStartDate", cpeInstallationBean.getCpeAmcStartDate());
					componentAndAttributeService.updateCompAttributes(task.getServiceId(), atMap,
							cpeInstallationBean.getComponentId());
				}
			}
		}
		if (!CollectionUtils.isEmpty(serviceHandoverBean.getDependencyDetails()))
			saveTeamErrorAsComponentAdditionalAttributes(task, serviceHandoverBean.getDependencyDetails());
		if (serviceHandoverBean.getServiceHandoverAction().equalsIgnoreCase("CLOSE")) {
			processTaskLogDetails(task, "CLOSED", serviceHandoverBean.getServiceHandoverAction(), null,
					serviceHandoverBean);
			if (serviceHandoverBean.getDocumentIds() != null && !serviceHandoverBean.getDocumentIds().isEmpty()) {
				serviceHandoverBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			logger.info("Sdwan Service Id::{}", task.getServiceId());
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-service-handover")){
				logger.info("Sdwan Service Handover Task");
				ScSolutionComponent isOverlaySolutionComponent = scSolutionComponentRepository
						.findByScServiceDetail1_idAndOrderCodeAndIsActive(task.getServiceId(), task.getOrderCode(), "Y");
				if (isOverlaySolutionComponent != null
						&& isOverlaySolutionComponent.getComponentGroup().equalsIgnoreCase("OVERLAY")) {
					logger.info("Assurance Handover for Overlay Service Id::{}", task.getServiceId());
					List<Integer> underlayServiceList = scSolutionComponentRepository
							.findUnderlayServiceIdByParentServiceId(task.getServiceId(), "Y");
					if (underlayServiceList != null && !underlayServiceList.isEmpty()) {
						logger.info("Underlay Service Exists::{}", underlayServiceList.size());
						for (Integer underlayServiceId : underlayServiceList) {
							logger.info("Underlay Service Id::{}", underlayServiceId);
							List<Task> underlayAcceptanceWaitTasks = taskRepository
									.findByServiceIdAndMstTaskDef_keyAndMstStatus_codeNot(underlayServiceId,
											"sdwan-underlay-service-handover", "CLOSED");
							for (Task underlayAcceptanceWaitTask : underlayAcceptanceWaitTasks) {
								Execution execution = runtimeService.createExecutionQuery()
										.processInstanceId(underlayAcceptanceWaitTask.getWfProcessInstId())
										.activityId("sdwan-underlay-service-handover").singleResult();
								if (execution != null) {
									logger.info(
											"Assurance Handover Execution exists for taskd Id::{},service Id::{},with excution id::{}",
											underlayAcceptanceWaitTask.getId(), underlayAcceptanceWaitTask.getServiceId(),
											execution.getId());
									runtimeService.trigger(execution.getId());
								}
							}
						}
					}
				}
			}
			return (SdwanServiceHandoverBean) flowableBaseService.taskDataEntry(task, serviceHandoverBean, fMap);
		} else {
			processTaskLogDetails(task, "HOLD", serviceHandoverBean.getServiceHandoverAction(), null,
					serviceHandoverBean);

			return (SdwanServiceHandoverBean) flowableBaseService.taskDataEntryHold(task, serviceHandoverBean, fMap);
		}
	}
}
