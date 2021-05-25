/**
 * 
 */
package com.tcl.dias.preparefulfillment.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.tcl.dias.servicefulfillmentutils.beans.TerminateValidateSupportingDocBean;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TerminationNegotiationResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.fulfillment.beans.OdrAttachmentBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;
import com.tcl.dias.common.fulfillment.beans.TerminationDropRequest;
import com.tcl.dias.common.fulfillment.beans.TerminationDropResponse;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.Appointment;
import com.tcl.dias.servicefulfillment.entity.entities.AppointmentDocuments;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.MstAppointmentSlots;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceLogs;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskRemark;
import com.tcl.dias.servicefulfillment.entity.repository.AppointmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstAppointmentDocumentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstAppointmentSlotsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceLogsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRemarkRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.ArrangeFieldEngineerForMuxRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ArrangeVendorForCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ArrangeVendorForMastDismantling;
import com.tcl.dias.servicefulfillmentutils.beans.ArrangeVendorForRFRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmBlockedResources;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmMastDisMantling;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmMuxRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmRfRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.CustomerAppointmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationStatusChangeRequest;
import com.tcl.dias.servicefulfillmentutils.beans.PoForCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.PoReleaseForCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.PrForCpeRecovery;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateBackhaulPo;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateOffnetBackhaulPoCustomerReatined;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateOffnetBackhaulPoExtension;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateOffnetPoCustomerReatined;
import com.tcl.dias.servicefulfillmentutils.beans.TerminateOffnetPoExtension;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationConfirmZeroNode;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationNotEligibleNoticationBean;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationSalesnegotiationBean;
import com.tcl.dias.servicefulfillmentutils.beans.VendorDetailsRequest;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SSDumpBean;
import com.tcl.dias.servicefulfillmentutils.beans.wireless.SSDumpResponseBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MstStatusConstant;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * @author NKalipan
 *
 */
@Service
@Transactional(readOnly = true)
public class TerminationService extends ServiceFulfillmentBaseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceFulfillmentBaseService.class);

	@Autowired
	private ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	private ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	private ScOrderRepository scOrderRepository;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ScComponentRepository scComponentRepository;

	@Autowired
	private TaskRemarkRepository taskRemarkRepository;

	@Autowired
	private FlowableBaseService flowableBaseService;
	
	@Autowired
	private MQUtils mqUtils;
	
	@Autowired
	private MstTaskDefRepository mstTaskDefRepository;
	
	@Autowired
	private MstAppointmentDocumentRepository mstAppointmentDocumentRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private MstAppointmentSlotsRepository mstAppointmentSlotsRepository;
	
	@Autowired
	private ScAdditionalServiceParamRepository scAdditionalServiceParamRepo;
	
	@Autowired
	private ScServiceAttributeRepository scServiceAttributeRepository;
	
	@Value("${termination.l20.queue}")
	private String terminationL20Queue;

	
	@Autowired
	private ProcessL2OService processL2OService;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private ServiceLogsRepository serviceLogsRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Value("${rabbitmq.macd.detail.commissioned}")
	private String macdDetailCommissioned;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Transactional(readOnly = false)
	public TerminationNegotiationResponse intiateTreminationWith10PrencentChance(OdrOrderBean odrOrderBean) throws TclCommonException {

		LOGGER.info("Intiate Termination process start for order code {}", odrOrderBean.getOpOrderCode());

		List<TerminationNotEligibleNoticationBean> terminationNotEligibleNoticationBeans = new ArrayList<>();
		
		for (OdrServiceDetailBean odrServiceDetailBean : odrOrderBean.getOdrServiceDetails()) {
			String reason = processL2OService.getTerminationNotEligibeReason(odrServiceDetailBean, true);
			
			if(reason != null) {
				LOGGER.info("Termination Validation check for service code {} and reason {}", odrServiceDetailBean.getUuid(), reason);
				TerminationNotEligibleNoticationBean terminationNotEligibleNoticationBean = new TerminationNotEligibleNoticationBean();
				terminationNotEligibleNoticationBean.setOrderCode(odrOrderBean.getOpOrderCode());
				terminationNotEligibleNoticationBean.setReason(reason);
				terminationNotEligibleNoticationBean.setServiceCode(odrServiceDetailBean.getUuid());
				terminationNotEligibleNoticationBean.setTerminationEffectiveDate(odrServiceDetailBean.getTerminationEffectiveDate());
				terminationNotEligibleNoticationBeans.add(terminationNotEligibleNoticationBean);
			}
		}
		
		if (!terminationNotEligibleNoticationBeans.isEmpty()) {
			LOGGER.info("Termination order not eligible for order code {} and List Size {} ", odrOrderBean.getOpOrderCode(), terminationNotEligibleNoticationBeans.size());
			processL2OService.sendTerminationNotEligibleNotification(terminationNotEligibleNoticationBeans,
					odrOrderBean.getOpOrderCode(), "10%");
			
			TerminationNegotiationResponse terminationNegotiationResponse = new TerminationNegotiationResponse();
			terminationNegotiationResponse.setStatus("ERROR");
			terminationNegotiationResponse.setOrderCode(odrOrderBean.getOpOrderCode());
			return terminationNegotiationResponse;
		}
		
		ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActiveAndIsMigratedOrder(odrOrderBean.getOpOrderCode(), "Y", "N");
		if(scOrder == null) {
			scOrder = saveOrder(odrOrderBean);
		}

		for (OdrServiceDetailBean odrServiceDetailBean : odrOrderBean.getOdrServiceDetails()) {
			
			Map<String, String> componentMap = new HashedMap<>();

			
			ScServiceDetail scServiceDetails = scServiceDetailRepository.findByUuidAndScOrderUuid(odrServiceDetailBean.getUuid(),  odrOrderBean.getOpOrderCode());
			if (scServiceDetails != null) {

				TerminationNegotiationResponse terminationNegotiationResponse = new TerminationNegotiationResponse();
				if (!scServiceDetails.getMstStatus().getCode()
						.equalsIgnoreCase(TaskStatusConstants.TERMINATION_INITIATED)) {
					terminationNegotiationResponse.setStatus("ERROR");
					terminationNegotiationResponse.setErrorMsg(
							"The requested Order has been placed already. Can't able to update TRF Extension date");
					return terminationNegotiationResponse;
				}
				
				
				String previousEffectiveDateStr = scServiceDetails.getTerminationEffectiveDate();
				
				//Update Attributes to show CSM Task
				updateAttributesFromL20(odrServiceDetailBean, componentMap, scServiceDetails);
				
				if (odrServiceDetailBean.getOdrAttachments() != null
						&& !odrServiceDetailBean.getOdrAttachments().isEmpty()) {
					odrServiceDetailBean.getOdrAttachments().forEach(att -> {
						ScAttachment scAttachment = mapServiceAttachmentToEntity(att, scServiceDetails);
						attachmentRepository.save(scAttachment.getAttachment());
						scAttachmentRepository.save(scAttachment);
					});
				}
				
				String terminationEffectiveDateStr = odrServiceDetailBean.getTerminationEffectiveDate();

				LOGGER.info("TRF date extension flow ==> previousEffectiveDate {}, terminationEffectiveDate {}, servicecode {} ",
						previousEffectiveDateStr, terminationEffectiveDateStr, scServiceDetails.getUuid());
				
				if(previousEffectiveDateStr != null) {
					Date  previousEffectiveDate = DateUtil.convertStringToDateYYMMDD(previousEffectiveDateStr);
					Date  terminationEffectiveDate = DateUtil.convertStringToDateYYMMDD(terminationEffectiveDateStr);
					
					if(previousEffectiveDate.compareTo(terminationEffectiveDate) == 0) {
						LOGGER.info("TRF date extension flow  terminationEffectiveDate is not changed servicecode {}", scServiceDetails.getUuid());
						terminationNegotiationResponse.setStatus("SUCCESS");
						return terminationNegotiationResponse;
					}
					
					LOGGER.info("TRF date extension flow  terminationEffectiveDate is changed servicecode {}", scServiceDetails.getUuid());
					
				}
				
				String lmTypeA = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_A, "lmType");
				String lmTypeB = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_B, "lmType");
				
				String offnetBackHaulA = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_A, "offnetBackHaul");
				
				String offnetBackHaulB = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_B, "offnetBackHaul");
				
				LOGGER.info("lmTypeA {}, lmTypeB {}, offnetBackHaulA {}, offnetBackHaulB {} and service code {}", lmTypeA, lmTypeB, offnetBackHaulA, offnetBackHaulB, odrServiceDetailBean.getUuid());
				
				if((offnetBackHaulA != null && offnetBackHaulA.equalsIgnoreCase("Yes")) || (offnetBackHaulB != null && offnetBackHaulB.equalsIgnoreCase("Yes")) ||
						(lmTypeA != null && lmTypeA.toLowerCase().contains("offnet")) || (lmTypeB != null && lmTypeB.toLowerCase().contains("offnet"))
						|| (lmTypeA != null && lmTypeA.equalsIgnoreCase("OffnetWL")) || (lmTypeB != null && lmTypeB.equalsIgnoreCase("OffnetWL"))) {
					LOGGER.info("Termination Extension for Offnet service id {}:", scServiceDetails.getId());
					return processL2OService.processTerminationOffnetPoTrfExtension(odrServiceDetailBean,scServiceDetails);
				}else {
					LOGGER.info("Termination Extension for other than Offent service id {}:", scServiceDetails.getId());
					terminationNegotiationResponse.setStatus("SUCCESS");
					terminationNegotiationResponse.setOrderCode(scOrder.getUuid());
					return terminationNegotiationResponse;
				}
			}
			
			ScServiceDetail scServiceDetail = saveServiceDetails(odrServiceDetailBean, scOrder);

			if (odrServiceDetailBean.getOdrAttachments() != null
					&& !odrServiceDetailBean.getOdrAttachments().isEmpty()) {
				odrServiceDetailBean.getOdrAttachments().forEach(att -> {
					ScAttachment scAttachment = mapServiceAttachmentToEntity(att, scServiceDetail);
					attachmentRepository.save(scAttachment.getAttachment());
					scAttachmentRepository.save(scAttachment);
				});
			}

			Set<OdrServiceAttributeBean> odrServiceAttributeBeans = odrServiceDetailBean.getOdrServiceAttributes();
			List<String> componentLis = Arrays.asList("terminationSubType", "terminationSubReason", "oem",
					"cpeSerialNumber");
			for (OdrServiceAttributeBean odrServiceAttributeBean : odrServiceAttributeBeans) {
				if (odrServiceAttributeBean.getAttributeName().equals("Service Variant")) {
					scServiceDetail.setServiceVariant(odrServiceAttributeBean.getAttributeValue());
				}
				if (componentLis.contains(odrServiceAttributeBean.getAttributeName())) {
					componentMap.put(odrServiceAttributeBean.getAttributeName(),
							odrServiceAttributeBean.getAttributeValue());

				}
				saveServiceAttrEntityToBean(odrServiceAttributeBean, scServiceDetail);
			}

			Set<OdrComponentBean> odrComponentBeans = odrServiceDetailBean.getOdrComponentBeans();

			if (odrComponentBeans != null && !odrComponentBeans.isEmpty()) {
				odrComponentBeans.forEach(comp -> {
					componentMap.put("csmEmail", odrServiceDetailBean.getCsmEmail());
					componentMap.put("csmUserName", odrServiceDetailBean.getCsmUserName());
					componentMap.put("approvalMailAvailable", odrServiceDetailBean.getApprovalMailAvailable());
					saveScComponent(scServiceDetail, comp, componentMap);
					
					componentAndAttributeService.updateAttributes(scServiceDetail.getId(), componentMap,
							AttributeConstants.COMPONENT_LM, comp.getSiteType());
				});
			}
			
			
			scServiceDetailRepository.save(scServiceDetail);

			taskService.processTermination10PercentFlow(odrOrderBean, odrServiceDetailBean);
		}
		
		LOGGER.info("Intiate Termination process end for order code {}", odrOrderBean.getOpOrderCode());
		
		TerminationNegotiationResponse terminationNegotiationResponse = new TerminationNegotiationResponse();
		terminationNegotiationResponse.setStatus("SUCCESS");
		terminationNegotiationResponse.setOrderCode(scOrder.getUuid());

		return terminationNegotiationResponse;

	}
	

	@Transactional(readOnly = false)
	public TerminationDropResponse dropTerminationQuote(TerminationDropRequest terminationDropRequest) {
		TerminationDropResponse terminationDropResponse = new TerminationDropResponse();
		terminationDropResponse.setOpOrderCode(terminationDropRequest.getOpOrderCode());
		LOGGER.info("Inside drop Termination Quote {}:", terminationDropRequest.getOpOrderCode());
		try {
			ScOrder scOrder = scOrderRepository
					.findByOpOrderCodeAndIsActiveAndIsMigratedOrder(terminationDropRequest.getOpOrderCode(), "Y", "N");
			if (scOrder != null) {
				List<ScServiceDetail> scServiceDetails = null;
				if(terminationDropRequest.getServiceCodes() != null && !terminationDropRequest.getServiceCodes().isEmpty()) {
					LOGGER.info("Drop Termination Quote for Service Ids: {} ", terminationDropRequest.getServiceCodes());
					scServiceDetails = scServiceDetailRepository.findByScOrder_idAndUuidIn(scOrder.getId(), terminationDropRequest.getServiceCodes());
				}else {
					scServiceDetails = scServiceDetailRepository.findByScOrderId(scOrder.getId());
				}
				
				if (scServiceDetails != null && !scServiceDetails.isEmpty()) {
					for (ScServiceDetail scServiceDetail : scServiceDetails) {
						if (scServiceDetail.getMstStatus().getCode()
								.equals(TaskStatusConstants.TERMINATION_INITIATED)) {
							LOGGER.info("Closing all Termination related tasks => Seriviec code {}: and Order Code {}:", scServiceDetail.getUuid(), scOrder.getUuid());
							List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_codeNotIn(
									scServiceDetail.getId(), Arrays.asList(MstStatusConstant.CLOSED));
							if (tasks != null && !tasks.isEmpty()) {
								for (Task task : tasks) {
									Map<String, Object> map = new HashedMap<>();
									if (task.getMstTaskDef().getKey().equals("sales-negotiation-termination")) {
										map.put("terminationReason", terminationDropRequest.getReason());
									}
									flowableBaseService.taskDataEntry(task, map, new HashMap<>());
									processTaskLogDetails(task, TaskLogConstants.CLOSED, "", null,
											constructBaseRequest(map));
								}
							}
							Map<String, String> componentMap = new HashedMap<>();
							componentMap.put("terminationReason", terminationDropRequest.getReason());
							componentAndAttributeService.updateAttributes(scServiceDetail.getId(), componentMap,
									AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
							LOGGER.info("updating service status as CANCEL_TERMINATION => Seriviec code {}: and Order Code {}:", scServiceDetail.getUuid(), scOrder.getUuid());
							processL2OService.processTerminationOffnetRetained(scServiceDetail.getUuid());
							scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CANCEL_TERMINATION));
							updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.CANCEL_TERMINATION,
									TaskStatusConstants.CANCEL_TERMINATION);
							scServiceDetailRepository.save(scServiceDetail);
						}
					}
					terminationDropResponse.setStatus("SUCCESS");
					return terminationDropResponse;
				} else {
					LOGGER.error("Circuit Details Not Exist for Termination Quote {}:",
							terminationDropRequest.getOpOrderCode());
					terminationDropResponse.setErrorMsg("Circuit Details Not Exist");
				}
			} else {
				LOGGER.error("Order Details Not Exist for Termination Quote {}:",
						terminationDropRequest.getOpOrderCode());
				terminationDropResponse.setErrorMsg("Order Not Exist");
			}
		} catch (Exception e) {
			LOGGER.error("Error while drop termination quote {}: and error {}:", terminationDropRequest.getOpOrderCode(), e);
			terminationDropResponse.setErrorMsg("System Error");
			terminationDropResponse.setStatus("ERROR");
		}
		terminationDropResponse.setStatus("ERROR");
		return terminationDropResponse;

	}

	private void updateAttributesFromL20(OdrServiceDetailBean odrServiceDetailBean, Map<String, String> componentMap,
			ScServiceDetail scServiceDetails) {
		componentMap.put("etcValue", odrServiceDetailBean.getEtcValue());
		componentMap.put("etcWaiver", odrServiceDetailBean.getEtcWaiver());
		
		String contractEndDate = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_A, "contractEndDate");
		String contractStartDate = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_A, "contractStartDate");
		String orderTermInMonths = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_A, "orderTermInMonths");
		
		componentMap.put("contractEndDate", contractEndDate);
		componentMap.put("contractStartDate", contractStartDate);
		componentMap.put("orderTermInMonths", orderTermInMonths);
		
		componentAndAttributeService.updateAttributes(scServiceDetails.getId(), componentMap,
				AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
	}
	
	private String getComponentValue(Set<OdrComponentBean> odrComponentBeans, String siteType, String attName) {
		if(odrComponentBeans != null) {
			for (OdrComponentBean odrComponentBean : odrComponentBeans) {
				if(odrComponentBean.getSiteType() != null && odrComponentBean.getSiteType().equals(siteType) &&
						odrComponentBean.getOdrComponentAttributeBeans() != null) {
					Optional<OdrComponentAttributeBean> odrOptional = odrComponentBean.getOdrComponentAttributeBeans().stream()
							.filter(comp -> comp.getName().equals(attName)).findFirst();
					if(odrOptional.isPresent()) {
						return odrOptional.get().getValue();
					}
				}
			}
		}
		return null;
	}

	@Transactional(readOnly = false)
	public TerminationSalesnegotiationBean closeSalesNegotiation(
			TerminationSalesnegotiationBean terminationSalesnegotiationBean) throws TclCommonException {
		LOGGER.info("Inside Process Sales Negotiation for termination {}", terminationSalesnegotiationBean.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Sales negotiation for termination is closed by " + userName;
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();

		componentMap.put("terminationNegotiationReason", terminationSalesnegotiationBean.getReason());
		componentMap.put("negotiationOutput", terminationSalesnegotiationBean.getNegotiationOutput());
		
		
		Task task = getTaskByIdAndWfTaskId(terminationSalesnegotiationBean.getTaskId(),
				terminationSalesnegotiationBean.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		if (terminationSalesnegotiationBean.getNegotiationOutput() != null
				&& terminationSalesnegotiationBean.getNegotiationOutput().equalsIgnoreCase("Retained")) {
			processL2OService.processTerminationOffnetRetained(scServiceDetail.getUuid());
		}
		if(terminationSalesnegotiationBean.getTerminationSubType()!=null) {
		componentMap.put("terminationSubType", terminationSalesnegotiationBean.getTerminationSubType());
		}
		if(terminationSalesnegotiationBean.getTerminationSubReason()!=null) {
		componentMap.put("terminationSubReason", terminationSalesnegotiationBean.getTerminationSubReason());
		}
		if(terminationSalesnegotiationBean.getTerminationReason()!=null) {
		componentMap.put("terminationReason", terminationSalesnegotiationBean.getTerminationReason());
		}
		if(terminationSalesnegotiationBean.getTerminationRemark()!=null) {
			componentMap.put("terminationRemarks", terminationSalesnegotiationBean.getTerminationRemark());
		}
		if(terminationSalesnegotiationBean.getRegrettedNonRegrettedTermination()!=null) {
			componentMap.put("regrettedNonRegrettedTermination", terminationSalesnegotiationBean.getRegrettedNonRegrettedTermination());
		}

		
		if(terminationSalesnegotiationBean.getDocumentIds()!=null && !CollectionUtils.isEmpty(terminationSalesnegotiationBean.getDocumentIds())){
			terminationSalesnegotiationBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		
		
		closeCurrentTask(terminationSalesnegotiationBean, task, scServiceDetail, userName, description, varMap, componentMap);
		
		TerminationNegotiationResponse terminationNegotiationResponse = new TerminationNegotiationResponse();
		terminationNegotiationResponse.setOrderCode(scServiceDetail.getScOrder().getOpOrderCode());
		terminationNegotiationResponse.setServiceCode(scServiceDetail.getUuid());
		terminationNegotiationResponse.setReason(terminationSalesnegotiationBean.getReason());
		terminationNegotiationResponse.setNegotiationResponse(terminationSalesnegotiationBean.getNegotiationOutput());
		terminationNegotiationResponse.setTerminationReason(terminationSalesnegotiationBean.getTerminationReason());
		terminationNegotiationResponse.setTerminationSubType(terminationSalesnegotiationBean.getTerminationSubType());
		terminationNegotiationResponse.setTerminationSubReason(terminationSalesnegotiationBean.getTerminationSubReason());
		terminationNegotiationResponse.setTerminationRemarks(terminationSalesnegotiationBean.getTerminationRemark());
		terminationNegotiationResponse.setRegrettedNonRegrettedTermination(terminationSalesnegotiationBean.getRegrettedNonRegrettedTermination());
		LOGGER.info("Sales Negotiation request to L20 End {}", Utils.convertObjectToJson(terminationNegotiationResponse));

		String l20Response = (String) mqUtils.sendAndReceive(terminationL20Queue,
				Utils.convertObjectToJson(terminationNegotiationResponse));
		
		LOGGER.info("Sales Negotiation response from L20 End {}", l20Response);


		LOGGER.info("Sales Negotiation for termination end {}", terminationSalesnegotiationBean.getServiceId());
		if(terminationSalesnegotiationBean.getNegotiationOutput() != null
				&& terminationSalesnegotiationBean.getNegotiationOutput().equalsIgnoreCase("Retained")) {
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CANCEL_TERMINATION));
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.CANCEL_TERMINATION,
					TaskStatusConstants.CANCEL_TERMINATION);
			scServiceDetailRepository.save(scServiceDetail);
		}
		
		return terminationSalesnegotiationBean;

	}
	
	@Transactional(readOnly = false)
	public TerminationConfirmZeroNode closeConfirmZeroNodeMux(TerminationConfirmZeroNode terminationConfirmZeroNode) throws TclCommonException {
		LOGGER.info("Inside Confirm Zero Node Mux for termination {}", terminationConfirmZeroNode.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Confirm Zero Node Mux for termination is closed by " + userName;
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		componentMap.put("terminationIsZeroNodeOrNot", terminationConfirmZeroNode.getZeroNodeOrNot());
		if(terminationConfirmZeroNode.getZeroNodeOrNot() != null && terminationConfirmZeroNode.getZeroNodeOrNot().equalsIgnoreCase("Y")) {
			componentMap.put("terminationEorDetails", terminationConfirmZeroNode.getEorDetails());
		}
		
		if (terminationConfirmZeroNode.getZeroNodeOrNot() != null
				&& terminationConfirmZeroNode.getZeroNodeOrNot().equalsIgnoreCase("Yes")) {
			varMap.put("terminationIsZeroNodeOrNot", "Yes");

		} else {
			varMap.put("terminationIsZeroNodeOrNot", "No");

		}
		
		Task task = getTaskByIdAndWfTaskId(terminationConfirmZeroNode.getTaskId(),
				terminationConfirmZeroNode.getWfTaskId());
		
		if(terminationConfirmZeroNode.getDocumentIds()!=null && !CollectionUtils.isEmpty(terminationConfirmZeroNode.getDocumentIds())){
			terminationConfirmZeroNode.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		closeCurrentTask(terminationConfirmZeroNode, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Confirm Zero Node Mux end {}", terminationConfirmZeroNode.getServiceId());
		return terminationConfirmZeroNode;
		
	}
	
	
	@Transactional(readOnly = false)
	public Object scheduleCustomerAppointmentForMuxRecovery(CustomerAppointmentBean customerAppointmentBean)
			throws TclCommonException {
		Map<String, Object> flowableMap = new HashMap<>();
		Task appointmentTask = getTaskByIdAndWfTaskId(customerAppointmentBean.getTaskId(),
				customerAppointmentBean.getWfTaskId());
		String taskDefKey = appointmentTask.getMstTaskDef().getKey();
		flowableMap.put(taskDefKey + "-end-time",
				DateUtil.convertStringToDateYYMMDD(customerAppointmentBean.getAppointmentDate()));
		flowableMap.put(taskDefKey + "-time-slot", customerAppointmentBean.getAppointmentSlot());

		List<MstTaskDef> mstTaskDefs = mstTaskDefRepository.findByDependentTaskKey(taskDefKey);

		if (!mstTaskDefs.isEmpty()) {

			mstTaskDefs.forEach(mstdef -> {
				flowableMap.put(mstdef.getFormKey() + "-start-time",
						DateUtil.convertStringToDateYYMMDD(customerAppointmentBean.getAppointmentDate()));
				flowableMap.put(mstdef.getFormKey() + "-time-slot", customerAppointmentBean.getAppointmentSlot());
			});
		}
		Task task = saveAppointmentDetails(appointmentTask, customerAppointmentBean);
		if (customerAppointmentBean.getAttachmentPermissionId() != null) {
			makeEntryInScAttachment(task, customerAppointmentBean.getAttachmentPermissionId());
		}
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, customerAppointmentBean.getDelayReason(), null,
				customerAppointmentBean);
		flowableBaseService.taskDataEntry(task, customerAppointmentBean, flowableMap);
		return customerAppointmentBean;
	}
	
	@Transactional(readOnly = false)
	public ArrangeFieldEngineerForMuxRecovery closeArrangeFieldEngineerForMuxRecovery(
			ArrangeFieldEngineerForMuxRecovery arrangeFieldEngineerForMuxRecovery) throws TclCommonException {
		LOGGER.info("Inside Arrange Field Engineer For MUX Recovery for termination {}",
				arrangeFieldEngineerForMuxRecovery.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Arrange Field Engineer For MUX Recovery is closed by " + userName;

		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		componentMap.put("offnetTerminationDate", arrangeFieldEngineerForMuxRecovery.getOffnetTerminationDate());

		Task task = getTaskByIdAndWfTaskId(arrangeFieldEngineerForMuxRecovery.getTaskId(),
				arrangeFieldEngineerForMuxRecovery.getWfTaskId());
		updateVendorAndFieldEngineerDetails(task, arrangeFieldEngineerForMuxRecovery);

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		closeCurrentTask(arrangeFieldEngineerForMuxRecovery, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Arrange Field Engineer For MUX Recovery is end {}",
				arrangeFieldEngineerForMuxRecovery.getServiceId());
		return arrangeFieldEngineerForMuxRecovery;

	}
	
	
	@Transactional(readOnly = false)
	public ArrangeVendorForRFRecovery closeArrangeVendorForRfRecovery(
			ArrangeVendorForRFRecovery arrangeVendorForRFRecovery) throws TclCommonException {
		LOGGER.info("Inside Arrange Vendor for RF Recovery for termination {}",
				arrangeVendorForRFRecovery.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Arrange Vendor for RF Recovery is closed by " + userName;

		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();

		Task task = getTaskByIdAndWfTaskId(arrangeVendorForRFRecovery.getTaskId(),
				arrangeVendorForRFRecovery.getWfTaskId());
		updateVendorAndFieldEngineerDetails(task, arrangeVendorForRFRecovery);

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		closeCurrentTask(arrangeVendorForRFRecovery, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Arrange Vendor for RF Recovery is end {}",
				arrangeVendorForRFRecovery.getServiceId());
		return arrangeVendorForRFRecovery;

	}
	
	@Transactional(readOnly = false)
	public ArrangeVendorForMastDismantling closeArrangeVendorForMastDismantling(
			ArrangeVendorForMastDismantling arrangeVendorForMastDismantling) throws TclCommonException {
		LOGGER.info("Inside Arrange Vendor for MAST Dismantling for termination {}",
				arrangeVendorForMastDismantling.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Arrange Vendor for MAST Dismantling is closed by " + userName;

		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();

		Task task = getTaskByIdAndWfTaskId(arrangeVendorForMastDismantling.getTaskId(),
				arrangeVendorForMastDismantling.getWfTaskId());
		updateVendorAndFieldEngineerDetails(task, arrangeVendorForMastDismantling);

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		closeCurrentTask(arrangeVendorForMastDismantling, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Arrange Vendor for MAST Dismantling is end {}",
				arrangeVendorForMastDismantling.getServiceId());
		return arrangeVendorForMastDismantling;

	}
	
	@Transactional(readOnly = false)
	public ArrangeVendorForCpeRecovery closeArrangeVendorForCpeRecovery(
			ArrangeVendorForCpeRecovery arrangeVendorForCpeRecovery) throws TclCommonException {
		LOGGER.info("Inside Arrange Vendor for CPE Recovery for termination {}",
				arrangeVendorForCpeRecovery.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Arrange Vendor for CPE Recovery is closed by " + userName;

		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		componentMap.put("vendorPoForCpeRecovery", arrangeVendorForCpeRecovery.getVendorPoForCpeRecovery());
		Task task = getTaskByIdAndWfTaskId(arrangeVendorForCpeRecovery.getTaskId(),
				arrangeVendorForCpeRecovery.getWfTaskId());
		updateVendorAndFieldEngineerDetails(task, arrangeVendorForCpeRecovery);


		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		closeCurrentTask(arrangeVendorForCpeRecovery, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Arrange Vendor for CPE Recovery is end {}",
				arrangeVendorForCpeRecovery.getServiceId());
		return arrangeVendorForCpeRecovery;

	}
	
	
	
	@Transactional(readOnly = false)
	public ConfirmMuxRecovery closeConfirmMuxRecoveryMuxRecovery(
			ConfirmMuxRecovery confirmMuxRecovery) throws TclCommonException {
		LOGGER.info("Inside Confirm Mux Recoverd for termination {}",
				confirmMuxRecovery.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Confirm Mux Recoverd is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(confirmMuxRecovery.getTaskId(),
				confirmMuxRecovery.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		if (confirmMuxRecovery.getDocumentIds() != null && !confirmMuxRecovery.getDocumentIds().isEmpty()) {
			confirmMuxRecovery.getDocumentIds()
			.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		componentMap.put("muxSerialNumber", confirmMuxRecovery.getMuxSerialNumber());
		componentMap.put("isMuxRecovered", confirmMuxRecovery.getIsMuxRecovered());
		varMap.put("action", confirmMuxRecovery.getAction());


		closeCurrentTask(confirmMuxRecovery, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Confirm Mux Recoverd is end {}",
				confirmMuxRecovery.getServiceId());
		return confirmMuxRecovery;

	}
	
	@Transactional(readOnly = false)
	public ConfirmMastDisMantling closeConfirmMastDisMantling(
			ConfirmMastDisMantling confirmMastDisMantling) throws TclCommonException {
		LOGGER.info("Inside Confirm MAST dismantling for termination {}",
				confirmMastDisMantling.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Confirm MAST dismantling is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(confirmMastDisMantling.getTaskId(),
				confirmMastDisMantling.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		if (confirmMastDisMantling.getDocumentIds() != null && !confirmMastDisMantling.getDocumentIds().isEmpty()) {
			confirmMastDisMantling.getDocumentIds()
			.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		varMap.put("action", confirmMastDisMantling.getAction());

		componentMap.put("isMastDismantled", confirmMastDisMantling.getIsMastDismantled());
		componentMap.put("ownerShipTransfer", confirmMastDisMantling.getOwnerShipTransfer());

		closeCurrentTask(confirmMastDisMantling, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Confirm MAST dismantling is end {}",
				confirmMastDisMantling.getServiceId());
		return confirmMastDisMantling;

	}
	
	@Transactional(readOnly = false)
	public ConfirmCpeRecovery closeConfirmCpeRecovery(
			ConfirmCpeRecovery confirmCpeRecovery) throws TclCommonException {
		LOGGER.info("Inside Confirm Cpe Recoverd for termination {}",
				confirmCpeRecovery.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Confirm Cpe Recoverd is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(confirmCpeRecovery.getTaskId(),
				confirmCpeRecovery.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		if (confirmCpeRecovery.getDocumentIds() != null && !confirmCpeRecovery.getDocumentIds().isEmpty()) {
			confirmCpeRecovery.getDocumentIds()
			.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		Map<String, Object> varMap = new HashMap<>();
		varMap.put("action", confirmCpeRecovery.getAction());

		Map<String, String> componentMap = new HashMap<>();
		componentMap.put("isCpeRecovered", confirmCpeRecovery.getIsCpeRecovered());
		componentMap.put("distributionCenterName", confirmCpeRecovery.getDistributionCenterName());
		componentMap.put("distributionCenterAddress", confirmCpeRecovery.getDistributionCenterAddress());

		closeCurrentTask(confirmCpeRecovery, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Confirm Cpe Recoverd is end {}",
				confirmCpeRecovery.getServiceId());
		return confirmCpeRecovery;

	}
	
	
	@Transactional(readOnly = false)
	public ConfirmRfRecovery closeConfirmRfRecovery(
			ConfirmRfRecovery confirmRfRecovery) throws TclCommonException {
		LOGGER.info("Inside Confirm Rf Recoverd for termination {}",
				confirmRfRecovery.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Confirm Rf Recoverd is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(confirmRfRecovery.getTaskId(),
				confirmRfRecovery.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		if (confirmRfRecovery.getDocumentIds() != null && !confirmRfRecovery.getDocumentIds().isEmpty()) {
			confirmRfRecovery.getDocumentIds()
			.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		componentMap.put("isRfRecovered", confirmRfRecovery.getIsRfRecovered());
		componentMap.put("isLanExtentionUsed", confirmRfRecovery.getIsLanExtentionUsed());
		varMap.put("action", confirmRfRecovery.getAction());


		closeCurrentTask(confirmRfRecovery, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Confirm Rf Recoverd is end {}",
				confirmRfRecovery.getServiceId());
		return confirmRfRecovery;

	}
	
	@Transactional(readOnly = false)
	public TerminateBackhaulPo closeTerminateBackhaulPo(
			TerminateBackhaulPo terminateBackhaulPo) throws TclCommonException {
		LOGGER.info("Inside Terminate Backhaul PO {}",
				terminateBackhaulPo.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Terminate Backhaul PO is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(terminateBackhaulPo.getTaskId(),
				terminateBackhaulPo.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		
		Map<String, String> atMap = new HashMap<>();
			
		atMap.put("backHaulcancellationDate", terminateBackhaulPo.getOffnetTerminationDate());			
		atMap.put("backHaulisSupplierEtcAvailable", terminateBackhaulPo.getIsSupplierEtcAvailable());					
		atMap.put("backHaulsupplierEtcCharges", terminateBackhaulPo.getSupplierEtcCharges());		
		atMap.put("backHaulterminationDate", terminateBackhaulPo.getOffnetTerminationDate());
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		if(!CollectionUtils.isEmpty(terminateBackhaulPo.getDocumentIds())){
			terminateBackhaulPo.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		closeCurrentTask(terminateBackhaulPo, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Terminate Backhaul PO is end {}",
				terminateBackhaulPo.getServiceId());
		return terminateBackhaulPo;

	}
	
	@Transactional(readOnly = false)
	public TerminateOffnetBackhaulPoExtension closeTerminateOffnetBackhaulPoExtension(
			TerminateOffnetBackhaulPoExtension terminateOffnetBackhaulPoExtension) throws TclCommonException {
		LOGGER.info("Inside Terminate Offnet Backhaul PO TRF Date Extension {}",
				terminateOffnetBackhaulPoExtension.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Terminate Offnet Backhaul PO TRF Date Extension is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(terminateOffnetBackhaulPoExtension.getTaskId(),
				terminateOffnetBackhaulPoExtension.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		if(terminateOffnetBackhaulPoExtension.getExtensionApproved() != null) {
			componentMap.put("offnetBackhaulPoextensionApproved", terminateOffnetBackhaulPoExtension.getExtensionApproved());
		}
		
		componentMap.put("backHaulcancellationDate", terminateOffnetBackhaulPoExtension.getOffnetTerminationDate());			
		componentMap.put("backHaulisSupplierEtcAvailable", terminateOffnetBackhaulPoExtension.getIsSupplierEtcAvailable());					
		componentMap.put("backHaulsupplierEtcCharges", terminateOffnetBackhaulPoExtension.getSupplierEtcCharges());		
		componentMap.put("backHaulTerminationDate", terminateOffnetBackhaulPoExtension.getOffnetTerminationDate());
		componentMap.put("terminationDate", terminateOffnetBackhaulPoExtension.getOffnetTerminationDate());
		if(!CollectionUtils.isEmpty(terminateOffnetBackhaulPoExtension.getDocumentIds())){
			terminateOffnetBackhaulPoExtension.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		String type = "terminate-offnet-backhaul-po-trf-date-extension";
		
		updateOffnetTerminationDate(scServiceDetail, type, terminateOffnetBackhaulPoExtension.getOffnetTerminationDate());
		
		closeCurrentTask(terminateOffnetBackhaulPoExtension, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Terminate Offnet Backhaul PO TRF Date Extension is end {}",
				terminateOffnetBackhaulPoExtension.getServiceId());
		return terminateOffnetBackhaulPoExtension;

	}

	@Transactional(readOnly = false)
	public TerminateOffnetPoExtension closeTerminateOffnetPoExtension(
			TerminateOffnetPoExtension terminateOffnetPoExtension) throws TclCommonException {
		LOGGER.info("Inside Terminate Offnet PO TRF Date Extension  {}",
				terminateOffnetPoExtension.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Terminate Offnet PO TRF Date Extension is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(terminateOffnetPoExtension.getTaskId(),
				terminateOffnetPoExtension.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();


		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();

		if(terminateOffnetPoExtension.getExtensionApproved() != null) {
			componentMap.put("offnetPoExtensionApproved", terminateOffnetPoExtension.getExtensionApproved());
		}

		componentMap.put("cancellationDate", terminateOffnetPoExtension.getOffnetTerminationDate());
		componentMap.put("isSupplierEtcAvailable", terminateOffnetPoExtension.getIsSupplierEtcAvailable());
		componentMap.put("supplierEtcCharges", terminateOffnetPoExtension.getSupplierEtcCharges());
		componentMap.put("offnetPoExtensionTerminationDate", terminateOffnetPoExtension.getOffnetTerminationDate());
		componentMap.put("terminationDate", terminateOffnetPoExtension.getOffnetTerminationDate());
		if(!CollectionUtils.isEmpty(terminateOffnetPoExtension.getDocumentIds())){
			terminateOffnetPoExtension.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		String type = "terminate-offnet-po-trf-date-extension";

		updateOffnetTerminationDate(scServiceDetail, type, terminateOffnetPoExtension.getOffnetTerminationDate());

		closeCurrentTask(terminateOffnetPoExtension, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Terminate Offnet PO TRF Date Extension is end {}",
				terminateOffnetPoExtension.getServiceId());
		return terminateOffnetPoExtension;

	}
	
	@Transactional(readOnly = false)
	public TerminateOffnetBackhaulPoCustomerReatined closeTerminateOffnetBackhaulPoRetained(
			TerminateOffnetBackhaulPoCustomerReatined terminateOffnetBackhaulPoCustomerReatined) throws TclCommonException {
		LOGGER.info("Inside Terminate Offnet Backhaul PO Customer Retained {}",
				terminateOffnetBackhaulPoCustomerReatined.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Terminate Offnet Backhaul PO Customer Retained is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(terminateOffnetBackhaulPoCustomerReatined.getTaskId(),
				terminateOffnetBackhaulPoCustomerReatined.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		
		componentMap.put("isSupplierEtcAvailable", terminateOffnetBackhaulPoCustomerReatined.getIsSupplierEtcAvailable());					
		componentMap.put("supplierEtcCharges", terminateOffnetBackhaulPoCustomerReatined.getSupplierEtcCharges());		
		if(!CollectionUtils.isEmpty(terminateOffnetBackhaulPoCustomerReatined.getDocumentIds())){
			terminateOffnetBackhaulPoCustomerReatined.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		
		closeCurrentTask(terminateOffnetBackhaulPoCustomerReatined, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Terminate Offnet Backhaul PO Customer Retained is end {}",
				terminateOffnetBackhaulPoCustomerReatined.getServiceId());
		return terminateOffnetBackhaulPoCustomerReatined;

	}
	
	@Transactional(readOnly = false)
	public TerminateOffnetPoCustomerReatined closeTerminateOffnetRetained(
			TerminateOffnetPoCustomerReatined terminateOffnetPoCustomerReatined) throws TclCommonException {
		LOGGER.info("Inside Terminate Offnet PO Customer Retained  {}",
				terminateOffnetPoCustomerReatined.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Terminate Offnet PO Customer Retained is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(terminateOffnetPoCustomerReatined.getTaskId(),
				terminateOffnetPoCustomerReatined.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		
		componentMap.put("isSupplierEtcAvailable", terminateOffnetPoCustomerReatined.getIsSupplierEtcAvailable());					
		componentMap.put("supplierEtcCharges", terminateOffnetPoCustomerReatined.getSupplierEtcCharges());		
		if(!CollectionUtils.isEmpty(terminateOffnetPoCustomerReatined.getDocumentIds())){
			terminateOffnetPoCustomerReatined.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		
		
		closeCurrentTask(terminateOffnetPoCustomerReatined, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Terminate Offnet PO Customer Retained is end {}",
				terminateOffnetPoCustomerReatined.getServiceId());
		return terminateOffnetPoCustomerReatined;

	}
	
	
	@Transactional(readOnly = false)
	public PrForCpeRecovery closePrForCpeRecovery(
			PrForCpeRecovery prForCpeRecovery) throws TclCommonException {
		LOGGER.info("Inside PR for Cpe Order Recovery {}",
				prForCpeRecovery.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "PR for Cpe Order Recovery is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(prForCpeRecovery.getTaskId(),
				prForCpeRecovery.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		
		if(!CollectionUtils.isEmpty(prForCpeRecovery.getDocumentIds())){
			prForCpeRecovery.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		componentMap.put("cpeRecoverySupplyHardwarePrNumber", prForCpeRecovery.getCpeSupplyHardwarePrNumber());
		componentMap.put("cpeRecoverySupplyHardwarePrVendorName", prForCpeRecovery.getCpeSupplyHardwarePrVendorName());
		componentMap.put("cpeRecoverySupplyHardwarePrDate", prForCpeRecovery.getCpeSupplyHardwarePrDate());
		componentMap.put("cpeRecoveryLicencePrNumber", prForCpeRecovery.getCpeLicencePrNumber());
		componentMap.put("cpeRecoveryLicencePrDate", prForCpeRecovery.getCpeLicencePrDate());
		componentMap.put("cpeRecoveryLicenseVendorName",prForCpeRecovery.getCpeLicenseVendorName());
		componentMap.put("cpeRecoverySupplyHardwarePrStatus", "Success");
		componentMap.put("cpeRecoveryLicencePoStatus", "Success");
		
		
		closeCurrentTask(prForCpeRecovery, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("PR for Cpe Order Recovery is end {}",
				prForCpeRecovery.getServiceId());
		return prForCpeRecovery;

	}
	
	@Transactional(readOnly = false)
	public PoForCpeRecovery closePoForCpeRecovery(
			PoForCpeRecovery poForCpeRecovery) throws TclCommonException {
		LOGGER.info("Inside PO for Cpe Order Recovery {}",
				poForCpeRecovery.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "PO for Cpe Order Recovery is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(poForCpeRecovery.getTaskId(),
				poForCpeRecovery.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		
		if(StringUtils.isNotBlank(poForCpeRecovery.getCpeSupplyHardwarePoNumber())) {
			componentMap.put("cpeRecoverySupplyHardwarePoNumber", poForCpeRecovery.getCpeSupplyHardwarePoNumber());
			componentMap.put("cpeRecoverySupplyHardwareVendorName", poForCpeRecovery.getCpeSupplyHardwareVendorName());
			componentMap.put("cpeRecoverySupplyHardwarePoDate", poForCpeRecovery.getCpeSupplyHardwarePoDate());
		}
		if(StringUtils.isNotBlank(poForCpeRecovery.getCpeInstallationHardwarePoNumber())) {
			componentMap.put("cpeRecoveryInstallationPoNumber", poForCpeRecovery.getCpeInstallationHardwarePoNumber());
			componentMap.put("cpeRecoveryInstallationVendorName", poForCpeRecovery.getCpeInstallationHardwarePoNumber());
			componentMap.put("cpeRecoveryInstallationPoDate", poForCpeRecovery.getCpeInstallationHardwarePoDate());
		}

		if(StringUtils.isNotBlank(poForCpeRecovery.getCpeSupportPoNumber())) {
			componentMap.put("cpeRecoverySupportVendorName", poForCpeRecovery.getCpeSupportVendorName());
			componentMap.put("cpeRecoverySupportPoNumber", poForCpeRecovery.getCpeSupportPoNumber());
			componentMap.put("cpeRecoverySupportPoDate", poForCpeRecovery.getCpeSupportPoDate());
		}

		if(Objects.nonNull(poForCpeRecovery.getCpeLicencePoNumber()) && Objects.nonNull(poForCpeRecovery.getCpeLicencePoDate())){
			componentMap.put("cpeRecoveryLicencePoNumber", poForCpeRecovery.getCpeLicencePoNumber());
			componentMap.put("cpeRecoveryLicencePoDate", poForCpeRecovery.getCpeLicencePoDate());
		}
		
		if(!CollectionUtils.isEmpty(poForCpeRecovery.getDocumentIds())){
			poForCpeRecovery.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		
		closeCurrentTask(poForCpeRecovery, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("PO for Cpe Order Recovery is end {}",
				poForCpeRecovery.getServiceId());
		return poForCpeRecovery;

	}
	
	@Transactional(readOnly = false)
	public PoReleaseForCpeRecovery closePoReleaseForCpeRecovery(
			PoReleaseForCpeRecovery poReleaseForCpeRecovery) throws TclCommonException {
		LOGGER.info("Inside PO Release for Cpe Order Recovery {}",
				poReleaseForCpeRecovery.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "PO Release for Cpe Order Recovery is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(poReleaseForCpeRecovery.getTaskId(),
				poReleaseForCpeRecovery.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		
		componentMap.put("cpeRecoverySupplyHardwarePoReleased", StringUtils.trimToEmpty(poReleaseForCpeRecovery.getPoRelease()));
		componentMap.put("cpeRecoveryLicencePoReleased", StringUtils.trimToEmpty(poReleaseForCpeRecovery.getLicencePoRelease()));
		if(poReleaseForCpeRecovery.getPoRelease().equalsIgnoreCase("yes")) {
			varMap.put("cpeRecoveryHardwarePoReleased", true);
			varMap.put("cpeRecoveryLicencePoReleased", true);
			varMap.put("cpeRecoveryLicencePoStatus", "PO Released");
			varMap.put("cpeRecoveryHardwarePoStatus", "PO Released");
			componentMap.put("cpeRecoveryLicencePoStatus", "PO Released");
			componentMap.put("cpeRecoverySupplyHardwarePoStatus", "PO Released");
		}
		
		closeCurrentTask(poReleaseForCpeRecovery, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("PO Release for Cpe Order Recovery is end {}",
				poReleaseForCpeRecovery.getServiceId());
		return poReleaseForCpeRecovery;

	}

	private void updateOffnetTerminationDate(ScServiceDetail scServiceDetail, String type, String changedValue) {
		ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), "terminationDate", AttributeConstants.COMPONENT_LM,
						AttributeConstants.SITETYPE_A);
		if (scComponentAttribute != null) {
			updateServiceLogs(scComponentAttribute.getAttributeValue(),
					changedValue, scServiceDetail.getId(), type);
		}
	}
	
	/**
	 * @author vivek
	 *
	 * @param confirmBlockedResources
	 * @return
	 * @throws TclCommonException 
	 */
	@Transactional(readOnly = false)
	public ConfirmBlockedResources confirmBlockResource(ConfirmBlockedResources confirmBlockedResources) throws TclCommonException {
		LOGGER.info("Inside Terminate Backhaul PO {}",
				confirmBlockedResources.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Terminate Backhaul PO is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(confirmBlockedResources.getTaskId(),
				confirmBlockedResources.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		varMap.put("action",
				confirmBlockedResources.getAction() == null ? "CLOSED" : confirmBlockedResources.getAction());
		componentMap.put("action",
				confirmBlockedResources.getAction() == null ? "CLOSED" : confirmBlockedResources.getAction());


		closeCurrentTask(confirmBlockedResources, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Terminate Backhaul PO is end {}",
				confirmBlockedResources.getServiceId());
		return confirmBlockedResources;

	}

	private Task saveAppointmentDetails(Task task, CustomerAppointmentBean customerAppointmentBean) {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(new Timestamp(
				DateUtil.convertStringToDateYYMMDD(customerAppointmentBean.getAppointmentDate()).getTime()));
		appointment.setLocalItname(customerAppointmentBean.getLocalContactName());
		appointment.setLocalItEmail(customerAppointmentBean.getLocalContactEMail());
		appointment.setLocalItContactMobile(customerAppointmentBean.getLocalContactNumber());
		appointment.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
		appointment.setServiceId(task.getServiceId());
		appointment.setDescription(customerAppointmentBean.getOtherDocument());
		appointment.setMstAppointmentSlot(getMstAppointmentSlotById(customerAppointmentBean.getAppointmentSlot()));
		appointment.setTask(task);
		appointment.setAppointmentType(getMstAppointmentType(task.getMstTaskDef().getKey()));

		Set<AppointmentDocuments> appointmentDocuments = new HashSet<>();
		customerAppointmentBean.getDocumentAttachments().forEach(attachment -> {
			AppointmentDocuments appointmentDocument = new AppointmentDocuments();
			appointmentDocument.setAppointment(appointment);
			appointmentDocument.setMstAppointmentDocuments(mstAppointmentDocumentRepository.findById(attachment).get());
			appointmentDocuments.add(appointmentDocument);
		});
		appointment.setAppointmentDocuments(appointmentDocuments);
		appointmentRepository.save(appointment);
		return task;
	}

	private MstAppointmentSlots getMstAppointmentSlotById(Integer slotId) {
		return mstAppointmentSlotsRepository.findById(slotId).orElseThrow(
				() -> new TclCommonRuntimeException(ExceptionConstants.TASK_NOT_FOUND, ResponseResource.R_CODE_ERROR));
	}

	public String getMstAppointmentType(String type) {
		if (!type.contains("appointment")) {
			return type;
		}
		String[] app = type.split("appointment");
		String s = app[1];
		return s.substring(1, s.length());
	}



	private void closeCurrentTask(Object request, Task task,
			ScServiceDetail scServiceDetail, String userName, String description, Map<String, Object> varMap,
			Map<String, String> componentMap) throws TclCommonException {
		if (!componentMap.isEmpty()) {
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), componentMap,
					AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
		}
		BaseRequest baseRequest = (BaseRequest) request;
		saveTaskRemarks(scServiceDetail, description, userName);
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, baseRequest.getDelayReason(), null,
				baseRequest);
		// Close Current Task
		flowableBaseService.taskDataEntry(task, request, varMap);
	}
	
	private void updateServiceLogs(String currentValue, String changedValue, Integer serviceId, String type) {

		ServiceLogs serviceLogs = new ServiceLogs();
		serviceLogs.setAttributeValue(currentValue);
		serviceLogs.setChangedAttributeValue(changedValue);
		serviceLogs.setServiceId(serviceId);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			serviceLogs.setCreatedBy(userInfoUtils.getUserInformation().getUserId());
		}
		serviceLogs.setType(type);
		serviceLogs.setCreatedTime(new Timestamp(new Date().getTime()));
		serviceLogs.setUpdatedTime(new Timestamp(new Date().getTime()));
		serviceLogsRepository.save(serviceLogs);
	}

	private void saveTaskRemarks(ScServiceDetail scServiceDetail, String remarks, String userName) {
		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks(remarks);
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}

	private ScComponent saveScComponent(ScServiceDetail scServiceDetail,
			OdrComponentBean odrComponentBean, Map<String, String> componentMap) {
		ScComponent scComponent = new ScComponent();
		scComponent.setComponentName(odrComponentBean.getComponentName());
		scComponent.setCreatedBy("");
		scComponent.setCreatedDate(new Timestamp(new Date().getTime()));
		scComponent.setIsActive(CommonConstants.Y);
		scComponent.setSiteType(odrComponentBean.getSiteType());
		scComponent.setUuid(Utils.generateUid());
		scComponent.setScServiceDetailId(scServiceDetail.getId());

		for (OdrComponentAttributeBean odrComponentAttributeBean : odrComponentBean.getOdrComponentAttributeBeans()) {
			if(odrComponentAttributeBean.getName().equalsIgnoreCase("customerMailReceiveDate")) {
				componentMap.put("customerRequestorDate", odrComponentAttributeBean.getValue());
			}
			componentMap.put(odrComponentAttributeBean.getName(),odrComponentAttributeBean.getValue() );
			String attName = odrComponentAttributeBean.getName();
			switch (attName) {
			case "destinationCity":
                scServiceDetail.setDestinationCity(odrComponentAttributeBean.getValue());
				break;
			case "destinationAddressLineOne":
				scServiceDetail.setDestinationAddressLineOne(odrComponentAttributeBean.getValue());
				break;
			case "portBandwidth":
				scServiceDetail.setBwPortspeed(odrComponentAttributeBean.getValue());
				break;
			case "localItContactEmailId":
				scServiceDetail.setLocalItContactEmail(odrComponentAttributeBean.getValue());
				break;
			case "destinationPincode":
				scServiceDetail.setDestinationPincode(odrComponentAttributeBean.getValue());
				break;
			case "lastMileProvider":
				scServiceDetail.setLastmileProvider(odrComponentAttributeBean.getValue());
				break;
			case "sourceCity":
				scServiceDetail.setSourceCity(odrComponentAttributeBean.getValue());
				break;
			case "sourceAddressLineTwo":
				scServiceDetail.setSourceAddressLineTwo(odrComponentAttributeBean.getValue());
				break;
			case "destinationAddressLineTwo":
				scServiceDetail.setDestinationAddressLineTwo(odrComponentAttributeBean.getValue());
				break;
			case "localItContactName":
				scServiceDetail.setLocalItContactName(odrComponentAttributeBean.getValue());
				break;
			case "destinationCountry":
				scServiceDetail.setDestinationCountry(odrComponentAttributeBean.getValue());
				break;
			case "lmType":
				scServiceDetail.setLastmileType(odrComponentAttributeBean.getValue());
				break;
			case "destinationState":
				scServiceDetail.setDestinationState(odrComponentAttributeBean.getValue());
				break;
			case "sourceState":
				scServiceDetail.setSourceState(odrComponentAttributeBean.getValue());
				break;
			case "sourceCountry":
				scServiceDetail.setSourceCountry(odrComponentAttributeBean.getValue());
				break;
			case "sourceAddressLineOne":
				scServiceDetail.setSourceAddressLineOne(odrComponentAttributeBean.getValue());
				break;
			case "localItContactMobile":
				scServiceDetail.setLocalItContactMobile(odrComponentAttributeBean.getValue());
				break;
			case "siteAddress":
				scServiceDetail.setSiteAddress(odrComponentAttributeBean.getValue());
				break;
			case "sourcePincode":
				scServiceDetail.setSourcePincode(odrComponentAttributeBean.getValue());
				break;
			case "burstableBwUnit":
				scServiceDetail.setBurstableBwUnit(odrComponentAttributeBean.getValue());
				break;
			case "lastMileScenario":
				scServiceDetail.setLastmileScenario(odrComponentAttributeBean.getValue());
				break;
			case "lmConnectionType":
				scServiceDetail.setLastmileConnectionType(odrComponentAttributeBean.getValue());
				break;
			case "customerMailReceiveDate":
				scServiceDetail.setCustomerRequestorDate(odrComponentAttributeBean.getValue());
				break;

			default:
				break;
			}
			
			
		}

		return scComponentRepository.save(scComponent);
	}

	private ScServiceDetail saveServiceDetails(OdrServiceDetailBean odrServiceDetailBean, ScOrder scOrder) {
		ScServiceDetail scServiceDetail = new ScServiceDetail();
		scServiceDetail.setUuid(odrServiceDetailBean.getUuid());
		scServiceDetail.setScOrder(scOrder);
		scServiceDetail.setAccessType(odrServiceDetailBean.getAccessType());
		scServiceDetail.setErfPrdCatalogProductId(odrServiceDetailBean.getErfPrdCatalogProductId());
		scServiceDetail.setErfPrdCatalogOfferingName(odrServiceDetailBean.getErfPrdCatalogOfferingName());
		scServiceDetail.setErfPrdCatalogParentProductName(odrServiceDetailBean.getErfPrdCatalogParentProductName());
		scServiceDetail.setErfPrdCatalogProductName(odrServiceDetailBean.getErfPrdCatalogProductName());
		scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.TERMINATION_INITIATED));
		scServiceDetail.setOrderSubCategory(odrServiceDetailBean.getOrderSubCategory());
		scServiceDetail.setScOrderUuid(scOrder.getUuid());
		scServiceDetail.setIsActive("Y");
		scServiceDetail.setIsMigratedOrder("N");
		scServiceDetail.setOrderType(scOrder.getOrderType());
		scServiceDetail.setTerminationEffectiveDate(odrServiceDetailBean.getTerminationEffectiveDate());
		scServiceDetail.setCustomerRequestorDate(odrServiceDetailBean.getCustomerRequestorDate());
		scServiceDetail.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		scServiceDetailRepository.save(scServiceDetail);
		return scServiceDetail;
	}

	private ScOrder saveOrder(OdrOrderBean OdrOrderBean) {
		ScOrder scOrder = new ScOrder();
		scOrder.setUuid(OdrOrderBean.getOpOrderCode());
		scOrder.setErfCustCustomerName(OdrOrderBean.getErfCustCustomerName());
		scOrder.setErfCustLeName(OdrOrderBean.getErfCustLeName());
		scOrder.setErfCustSpLeName(OdrOrderBean.getErfCustSpLeName());
		scOrder.setTpsSfdcCuid(OdrOrderBean.getTpsSfdcCuid());
		scOrder.setErfCustLeId(OdrOrderBean.getErfCustLeId());
		scOrder.setErfCustCustomerId(OdrOrderBean.getErfCustCustomerId());
		scOrder.setSfdcAccountId(OdrOrderBean.getSfdcAccountId());
		scOrder.setTpsSfdcOptyId(OdrOrderBean.getSfdcOptyId());
		scOrder.setOrderType(OdrOrderBean.getOrderType());
		scOrder.setOrderCategory(OdrOrderBean.getOrderCategory());
		scOrder.setOrderSource(OdrOrderBean.getOrderSource());
		scOrder.setCreatedBy(OdrOrderBean.getCreatedBy());
		scOrder.setOpOrderCode(OdrOrderBean.getOpOrderCode());
		scOrder.setParentOrderType(OdrOrderBean.getParentOrderType());
		scOrder.setOrderType(OdrOrderBean.getOrderType());
		scOrder.setParentOpOrderCode(OdrOrderBean.getTerminationOrderCode());
		scOrder.setIsActive("Y");
		scOrder.setIsMigratedOrder("N");
		scOrder.setCreatedDate(new Timestamp(new Date().getTime()));
		scOrderRepository.save(scOrder);
		return scOrder;

	}

	
	
	private ScAttachment mapServiceAttachmentToEntity(OdrAttachmentBean odrAttachmentBean, ScServiceDetail serviceEntity) {
		Attachment attachment = new Attachment();
		attachment.setCategory(odrAttachmentBean.getCategory()==null?odrAttachmentBean.getType():odrAttachmentBean.getCategory());
		attachment.setContentTypeHeader(odrAttachmentBean.getContentTypeHeader());
		attachment.setCreatedBy(odrAttachmentBean.getCreatedBy());
		attachment.setCreatedDate(odrAttachmentBean.getCreatedDate());
		attachment.setIsActive(CommonConstants.Y);
		attachment.setName(odrAttachmentBean.getName());
		LOGGER.info("odrAttachmentBean.getProductName() {} and attachment.getCategory() {}" , odrAttachmentBean.getProductName(),attachment.getCategory());
		attachment.setUriPathOrUrl(odrAttachmentBean.getStoragePathUrl());
		attachment.setUriPathOrUrl(odrAttachmentBean.getStoragePathUrl());
		attachment.setType(odrAttachmentBean.getType());
		attachmentRepository.save(attachment);
		ScAttachment scAttachment = new ScAttachment();
		scAttachment.setAttachment(attachment);
		scAttachment.setIsActive(CommonConstants.Y);
		scAttachment.setOfferingName(odrAttachmentBean.getOfferingName());
		scAttachment.setOrderId(odrAttachmentBean.getOrderId());
		scAttachment.setProductName(odrAttachmentBean.getProductName());
		scAttachment.setScServiceDetail(serviceEntity);
		scAttachment.setServiceCode(serviceEntity.getUuid());
		scAttachment.setSiteType(serviceEntity.getSiteType() == null ? "A": serviceEntity.getSiteType());
		scAttachment.setSiteId(odrAttachmentBean.getSiteId());
		
		scAttachmentRepository.save(scAttachment);
		
		return scAttachment;
	}
	
		public ScServiceAttribute saveServiceAttrEntityToBean(OdrServiceAttributeBean odrServiceAttribute,ScServiceDetail scServiceDetail) {
			ScServiceAttribute scServiceAttribute = new ScServiceAttribute();
			scServiceAttribute.setAttributeAltValueLabel(odrServiceAttribute.getAttributeAltValueLabel());
			scServiceAttribute.setAttributeName(odrServiceAttribute.getAttributeName());
			scServiceAttribute.setAttributeValue(odrServiceAttribute.getAttributeValue());
			scServiceAttribute.setCategory(odrServiceAttribute.getCategory());
			scServiceAttribute.setCreatedBy(odrServiceAttribute.getCreatedBy());
			scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setIsActive(CommonConstants.Y);
			scServiceAttribute.setUpdatedBy(odrServiceAttribute.getUpdatedBy());
			scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setIsAdditionalParam(odrServiceAttribute.getIsAdditionalParam());
			scServiceAttribute.setScServiceDetail(scServiceDetail);
			if(odrServiceAttribute.getIsAdditionalParam().equals(CommonConstants.Y)) {
				ScAdditionalServiceParam scAdditionalServiceParam=new ScAdditionalServiceParam();
				scAdditionalServiceParam.setAttribute(odrServiceAttribute.getOdrAdditionalServiceParam().getAttribute());
				scAdditionalServiceParam.setCategory(odrServiceAttribute.getOdrAdditionalServiceParam().getCategory());
				scAdditionalServiceParam.setCreatedBy(odrServiceAttribute.getOdrAdditionalServiceParam().getCreatedBy());
				scAdditionalServiceParam.setCreatedTime(new Timestamp(new Date().getTime()));
				scAdditionalServiceParam.setIsActive(CommonConstants.Y);
				scAdditionalServiceParam.setReferenceType(odrServiceAttribute.getOdrAdditionalServiceParam().getReferenceType());
				scAdditionalServiceParam.setReferenceId(odrServiceAttribute.getOdrAdditionalServiceParam().getReferenceId());
				scAdditionalServiceParam.setValue(odrServiceAttribute.getOdrAdditionalServiceParam().getValue());
				scAdditionalServiceParamRepo.save(scAdditionalServiceParam);
				scServiceAttribute.setAttributeValue(scAdditionalServiceParam.getId()+CommonConstants.EMPTY);
			}
			scServiceAttributeRepository.save(scServiceAttribute);
			return scServiceAttribute;
		}
		
		private void updateVendorAndFieldEngineerDetails(Task task,VendorDetailsRequest vendorDetailsBean) {
			saveVendorDetail(task, vendorDetailsBean);
			
				FieldEngineerDetailsBean fieldEngineerDetail=new FieldEngineerDetailsBean();
				fieldEngineerDetail.setName(vendorDetailsBean.getOspFeName());
				fieldEngineerDetail.setContactNumber(vendorDetailsBean.getOspFeContactNumber());
				fieldEngineerDetail.setEmailId(vendorDetailsBean.getOspFeEmailId());
				fieldEngineerDetail.setSecondaryContactNumber(vendorDetailsBean.getOspSecondaryFeContactNumber());
				fieldEngineerDetail.setSecondaryEmailId(vendorDetailsBean.getOspSecondaryFeEmailId());
				fieldEngineerDetail.setSecondaryName(vendorDetailsBean.getOspSecondaryFeName());
				saveFieldEngineer(task, fieldEngineerDetail);
			}

	@Transactional(readOnly = false)
	public TerminationStatusChangeRequest processTerminationHold(TerminationStatusChangeRequest holdTermination) throws TclCommonException {
		LOGGER.info("Inside  processTerminationHold{}", holdTermination.getServiceId());
		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "service status changed from termination inprogress to hold by " + userName;
		String terminationEffectiveDateChangeRemark ="";
		String currentDate ="";
		Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository
				.findById(holdTermination.getServiceId());
		// set status to hold
		if (scServiceDetailOptional.isPresent()) {
			ScServiceDetail scServiceDetail = scServiceDetailOptional.get();
			currentDate = scServiceDetail.getTerminationEffectiveDate();
			if (!scServiceDetail.getScOrder().getOrderType().equalsIgnoreCase("Termination")) {
				throw new TclCommonRuntimeException(ExceptionConstants.TERMINATION_CANCELLATION_RESTRICTED,
						ResponseResource.R_CODE_ERROR);

			}
			
			Map<String, Object> varMap = new HashMap<>();
			Map<String, String> componentMap = new HashMap<>();
				
			if(holdTermination.getStatus().equalsIgnoreCase("hold")) {
				LOGGER.info("Termination Hold Process for service Id {} ", holdTermination.getServiceId());
				scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
				if(holdTermination.getTerminateEffectiveDate()!=null) {
					scServiceDetail.setTerminationEffectiveDate(holdTermination.getTerminateEffectiveDate());
				}
				scServiceDetailRepository.save(scServiceDetail);
				// set remarks
				saveTaskRemarks(scServiceDetail, description, userName);
				if (holdTermination.getTerminationHold() != null) {
					componentMap.put("terminationHold", "yes");
					varMap.put("terminationHold", holdTermination.getTerminationHold());
				}
				if (holdTermination.getTerminationDateChange() != null) {
					addTerminationEffectiveDateChangeRemarks(holdTermination, userName, currentDate, varMap,
							componentMap);
					componentMap.put("terminationDateChange", holdTermination.getTerminationDateChange());
					varMap.put("terminationDateChange", holdTermination.getTerminationDateChange());
				}
				if (holdTermination.getTerminateEffectiveDate() != null) {
					componentMap.put("terminationEffectiveDate", holdTermination.getTerminateEffectiveDate());
					String terminationEffectiveDate = getEffectiveDateForTermination(holdTermination.getTerminateEffectiveDate());
					varMap.put("terminationEmailDate", terminationEffectiveDate);
					varMap.put("terminationEffectiveDate", terminationEffectiveDate);
				}
				varMap.put("terminationReschedule", "No");
				// set component attributes
				if (!componentMap.isEmpty()) {
					componentAndAttributeService.updateAttributes(scServiceDetail.getId(), componentMap,
							AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
				}
				varMap.put("terminationHold", "Yes");		
				
				closeTerminationTriggerTask(holdTermination, varMap);
				
				Task emailTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
						holdTermination.getServiceId(), "termination-email-trigger");
				
				if (emailTask != null 
						&& !emailTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
					String executionId = emailTask.getWfTaskId();
					runtimeService.setVariable(executionId, "skipTerminationEmail", "Yes");
					runtimeService.trigger(executionId);
				} 
			}else if(holdTermination.getStatus().equalsIgnoreCase("cancel")) {
				
				LOGGER.info("Termination cancel Process for service Id {} ", holdTermination.getServiceId());
				varMap.put("cancelTermination", "Yes");
				varMap.put("terminationHold", "No");	
				varMap.put("terminationReschedule", "No");
				//Close All Manual Tasks
				List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_codeNotIn(holdTermination.getServiceId(),
						Arrays.asList(MstStatusConstant.CLOSED));
				
				//Close All Opened tasks 
				if(tasks != null && !tasks.isEmpty()) {
					for (Task manualTask : tasks) {
						if(!manualTask.getMstTaskDef().getKey().equalsIgnoreCase("termination-trigger-task")) {
							processTaskLogDetails(manualTask, "CLOSED", "Termination Cancelled", null, null);
							flowableBaseService.taskDataEntry(manualTask, holdTermination);
						}
					}
				}
				
				
				List<String> attributesNames = Arrays.asList("lmType", "offnetBackHaul");
				
				Map<String, String> siteAComMap = commonFulfillmentUtils.getComponentAttributesDetails(attributesNames,
						holdTermination.getServiceId(), AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
				
				boolean offnetLm = false;
				
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NPL")) {
					varMap.put("siteBType", AttributeConstants.SITETYPE_B);
					Map<String, String> siteBComMap = commonFulfillmentUtils.getComponentAttributesDetails(attributesNames,
							holdTermination.getServiceId(), AttributeConstants.COMPONENT_LM,
							AttributeConstants.SITETYPE_B);
					if (siteBComMap != null && !siteBComMap.isEmpty()) {

						if (siteBComMap.containsKey("lmType") && siteBComMap.get("lmType") != null
								&& siteBComMap.get("lmType").toLowerCase().contains("offnet")) {
							offnetLm = true;
							varMap.put("offnetPoExtensionRequiredB", "Yes");
							LOGGER.info("offnetPoExtensionRequiredB for service code {}", scServiceDetail.getUuid());
						}
						if (siteBComMap.containsKey("offnetBackHaul") && siteBComMap.get("offnetBackHaul") != null
								&& siteBComMap.get("offnetBackHaul").equalsIgnoreCase("Yes")) {
							offnetLm = true;
							varMap.put("offnetHaulExtensionRequiredB", "Yes");
							LOGGER.info("offnetHaulExtensionRequiredB for service code {}", scServiceDetail.getUuid());
						}
					}
				}
				
				if (siteAComMap != null && !siteAComMap.isEmpty()) {
					if (siteAComMap.containsKey("lmType") && siteAComMap.get("lmType") != null
							&& siteAComMap.get("lmType").toLowerCase().contains("offnet")) {
						offnetLm = true;
						varMap.put("offnetPoExtensionRequired", "Yes");
						LOGGER.info("offnetHaulExtensionRequired for service code {}", scServiceDetail.getUuid());
					}

					if (siteAComMap.containsKey("offnetBackHaul") && siteAComMap.get("offnetBackHaul") != null
							&& siteAComMap.get("offnetBackHaul").equalsIgnoreCase("Yes")) {
						offnetLm = true;
						varMap.put("offnetHaulExtensionRequired", "Yes");
						LOGGER.info("offnetPoExtensionRequired for service code {}", scServiceDetail.getUuid());
					}

				}

				if (offnetLm) {
					varMap.put("trfsExtensionRequired", "Yes");
				}
				
				closeTerminationTriggerTask(holdTermination, varMap);
				
				scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CANCEL_TERMINATION));
				updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.CANCEL_TERMINATION,
						TaskStatusConstants.CANCEL_TERMINATION);
				scServiceDetailRepository.save(scServiceDetail);
				
				
			}else if(holdTermination.getStatus().equalsIgnoreCase("extension")) {
				LOGGER.info("Termination  extension for service Id {} ", holdTermination.getServiceId());
				
				if (holdTermination.getTerminateEffectiveDate() != null && DateUtil.convertStringToDateYYMMDD(holdTermination.getTerminateEffectiveDate())
						.compareTo(new Date()) > 0) {

					
					addTerminationEffectiveDateChangeRemarks(holdTermination, userName, currentDate, varMap,
							componentMap);
					
					
					scServiceDetail.setTerminationEffectiveDate(holdTermination.getTerminateEffectiveDate());
					scServiceDetail.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
					scServiceDetail.setUpdatedBy(userInfoUtils.getUserInformation().getUserId());
					scServiceDetailRepository.save(scServiceDetail);

					String terminationEffectiveDate = getEffectiveDateForTermination(holdTermination.getTerminateEffectiveDate());
					varMap.put("terminationEmailDate", terminationEffectiveDate);
					varMap.put("terminationEffectiveDate", terminationEffectiveDate);
					varMap.put("terminationHold", "No");
					varMap.put("terminationReschedule", "Yes");
					componentMap.put("terminationEffectiveDate", holdTermination.getTerminateEffectiveDate());
					componentMap.put("etcValue", holdTermination.getEtcValue());
					componentMap.put("terminationExtensionSfdcOpportunityId",holdTermination.getTerminationSfdcOpportunityId());
					LOGGER.info("Termination extension sfdc opportunity Id is updated for service Id {} ", holdTermination.getServiceId());
					componentAndAttributeService.updateAttributes(scServiceDetail.getId(), componentMap,
							AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
					
					closeTerminationTriggerTask(holdTermination, varMap);
					
					OdrServiceDetailBean odrServiceDetailBean = new OdrServiceDetailBean();
					odrServiceDetailBean.setTerminationEffectiveDate(holdTermination.getTerminateEffectiveDate());
					processL2OService.processTerminationOffnetPoTrfExtension(odrServiceDetailBean, scServiceDetail);
					
					if(scServiceDetail.getOrderSubCategory() != null && scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")) {
						mqUtils.send(macdDetailCommissioned, scServiceDetail.getParentUuid());
					}else {
						mqUtils.send(macdDetailCommissioned, scServiceDetail.getUuid());
					}
					
				}else {
					
					LOGGER.info("Termination  extension date is not valid for service Id {} ", holdTermination.getServiceId());
				}
			}
			}
		return holdTermination;
		}


	private void closeTerminationTriggerTask(TerminationStatusChangeRequest holdTermination,
			Map<String, Object> varMap) {
		Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyAndMstStatus_codeNotOrderByCreatedTimeDesc(
				holdTermination.getServiceId(), "termination-trigger-task", TaskStatusConstants.CLOSED_STATUS);
		if (task != null 
				&& !task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
			
			if(!CollectionUtils.isEmpty(holdTermination.getDocumentIds())){
				holdTermination.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
			}
			
			String executionId = task.getWfTaskId();
			varMap.forEach((k, v) -> runtimeService.setVariable(executionId, k, v));
			runtimeService.trigger(executionId);
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.TERMINATION_CANCELLATION_RESTRICTED, ResponseResource.R_CODE_ERROR);
			
		}
	}



	private void addTerminationEffectiveDateChangeRemarks(TerminationStatusChangeRequest holdTermination,
			String userName, String currentDate, Map<String, Object> varMap, Map<String, String> componentMap) {
		String terminationEffectiveDateChangeRemark;
		terminationEffectiveDateChangeRemark="Termination Effective Date is changed from "+currentDate+" to "+holdTermination.getTerminationDateChange()+" by "+userName;
		componentMap.put("terminationEffectiveDateChangeRemark", terminationEffectiveDateChangeRemark);
	}

	@Transactional(readOnly = false)
	public TerminationStatusChangeRequest processManualTerminationHold(TerminationStatusChangeRequest holdTermination) throws TclCommonException {
		LOGGER.info("Inside  processManualTerminationHold{}", holdTermination.getServiceId());
		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Hold Manual Termination is closed by " + userName;
		Map<String, String> componentMap = new HashMap<>();
		Map<String, Object> varMap = new HashMap<>();
		if (holdTermination.getTerminationHold() != null) {
			componentMap.put("terminationHold", holdTermination.getTerminationHold());
			varMap.put("terminationHold", holdTermination.getTerminationHold());
		}else {
			varMap.put("terminationHold","No");
		}
		if (holdTermination.getTerminationDateChange() != null) {
			componentMap.put("terminationDateChange", holdTermination.getTerminationDateChange());
			varMap.put("terminationDateChange", holdTermination.getTerminationDateChange());
		}
		if (holdTermination.getTerminateEffectiveDate() != null) {
			componentMap.put("terminationEffectiveDate", holdTermination.getTerminateEffectiveDate());
			String terminationEffectiveDate = getEffectiveDateForTermination(holdTermination.getTerminateEffectiveDate());
			varMap.put("terminationEmailDate", terminationEffectiveDate);
			varMap.put("terminationEffectiveDate", terminationEffectiveDate);
			LOGGER.info("Inside  processManualTerminationHold Service ID={} terminationEmailDate={}", holdTermination.getServiceId(),terminationEffectiveDate);
		}
		//Task task = getTaskByIdAndWfTaskId(holdTermination.getTaskId(), holdTermination.getWfTaskId());
		
		Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(holdTermination.getServiceId(), "termination-desk-hold");

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.TERMINATION_INPROGRESS));
		if (holdTermination.getTerminateEffectiveDate() != null) {
			scServiceDetail.setTerminationEffectiveDate(holdTermination.getTerminateEffectiveDate());
		}
		scServiceDetailRepository.save(scServiceDetail);
		closeCurrentTask(holdTermination, task, scServiceDetail, userName, description, varMap, componentMap);
		
		
		Task emailTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
				holdTermination.getServiceId(), "termination-email-trigger");
		
		if (emailTask != null 
				&& !emailTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
			String executionId = emailTask.getWfTaskId();
			runtimeService.trigger(executionId);
		} else {
			//throw new TclCommonRuntimeException(ExceptionConstants.TERMINATION_CANCELLATION_RESTRICTED, ResponseResource.R_CODE_ERROR);

		}
		
		LOGGER.info("hold termination manual {}", holdTermination.getServiceId());
		return holdTermination;

	}
	
	/**
	 * @author vivek
	 *
	 * @param terminateBackhaulPo
	 * @return
	 * @throws TclCommonException 
	 */
	@Transactional(readOnly=false,isolation=Isolation.READ_UNCOMMITTED)
	public boolean processTerminationExtension(TerminateBackhaulPo terminateBackhaulPo) throws TclCommonException {

		ScServiceDetail scServiceDetails = scServiceDetailRepository.findById(terminateBackhaulPo.getServiceId()).get();
		if (scServiceDetails != null) {

			if (terminateBackhaulPo.getTerminationEffectiveDate() != null) {
				scServiceDetails.setTerminationEffectiveDate(terminateBackhaulPo.getTerminationEffectiveDate());
			}

			processL2OService.processTerminationOffnetPoTrfExtensionWorkAround(scServiceDetails);
		}

		return true;
	}

	@Transactional(readOnly = false)
	public TerminateOffnetPoExtension cancelTerminateOffnetPo(
			TerminateOffnetPoExtension terminateOffnetPoExtension) throws TclCommonException {
		LOGGER.info("cancelTerminateOffnetPo method invoked for ServiceId:{}",
				terminateOffnetPoExtension.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		

		Task task = getTaskByIdAndWfTaskId(terminateOffnetPoExtension.getTaskId(),
				terminateOffnetPoExtension.getWfTaskId());
		if("cancel-terminate-offnet-po-trf-date-extension".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
			description = "Cancel Terminate Offnet PO is closed by " + userName;
		}else if("cancel-terminate-offnet-backhaul-po-trf-date-extension".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
			description = "Cancel Terminate Offnet Backhaul PO is closed by " + userName;
		}

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();


		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		if(!CollectionUtils.isEmpty(terminateOffnetPoExtension.getDocumentIds())){
			terminateOffnetPoExtension.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		closeCurrentTask(terminateOffnetPoExtension, task, scServiceDetail, userName, description, varMap, componentMap);
		return terminateOffnetPoExtension;

	}

	@Transactional(readOnly = false)
	public TerminateValidateSupportingDocBean closeTerminateDeskValidateSupportingDocument(
			TerminateValidateSupportingDocBean terminateValidateSupportingDocBean) throws TclCommonException {
		LOGGER.info("Inside Terminate Validate Supporting Document {}",
				terminateValidateSupportingDocBean.getServiceId());

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		description = "Terminate Validate Supporting Document is closed by " + userName;

		Task task = getTaskByIdAndWfTaskId(terminateValidateSupportingDocBean.getTaskId(),
				terminateValidateSupportingDocBean.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();


		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();


		componentMap.put("terminationDocRemarks", terminateValidateSupportingDocBean.getTerminationDocRemarks());
		if(!CollectionUtils.isEmpty(terminateValidateSupportingDocBean.getDocumentIds())){
			terminateValidateSupportingDocBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		closeCurrentTask(terminateValidateSupportingDocBean, task, scServiceDetail, userName, description, varMap, componentMap);
		LOGGER.info("Terminate Validate Supporting Document is end {}",
				terminateValidateSupportingDocBean.getServiceId());
		return terminateValidateSupportingDocBean;

	}


}
