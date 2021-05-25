package com.tcl.dias.servicefulfillmentutils.delegates;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.DownTimeDetails;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.DownTimeDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("checkDowntimeTaskDelegate")
public class CheckDowntimeTaskDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(GetTxDownTimeDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired 
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired 
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Autowired
	com.tcl.dias.servicefulfillment.entity.repository.ProcessRepository processRepository;
	
	@Autowired 
	DownTimeDetailsRepository downTimeDetailsRepository;
	
	@Autowired
	RuntimeService runtimeService;
	
	 @Override
	 public void execute(DelegateExecution execution) {
		 logger.info("CheckDowntimeTaskDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		 Map<String, Object> varibleMap = execution.getVariables();
		 Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		 String orderCode = (String) varibleMap.get(MasterDefConstants.ORDER_CODE);
		 String productName = StringUtils.trimToEmpty((String)varibleMap.get("productName"));
		 if(orderCode!=null && !orderCode.isEmpty()){
			 if(orderCode.startsWith("IZOSDWAN")){
				 String flowType = StringUtils.trimToEmpty((String)varibleMap.get("flowType"));
				 Map<String, String> downTimeMap = new HashMap<>();
				 Boolean isReadyForDowntime=false;
				 List<Integer> serviceIdList= new ArrayList<>();
				 Integer solutionId = (Integer) varibleMap.get("solutionId");
				 String destinationCountry = (String) varibleMap.get("country");
				 if(flowType.equalsIgnoreCase("HYBRID")){
					 logger.info("IZOSDWAN SolutionId::{},OrderCode::{},ProductName::{},ServiceId for CheckDowntimeTaskDelegate::{},FlowType::{}", solutionId,orderCode,productName,serviceId,flowType);
					 if("IAS".equalsIgnoreCase(productName) || "GVPN".equalsIgnoreCase(productName) 
							 || "IZO Internet WAN".equalsIgnoreCase(productName) || "DIA".equalsIgnoreCase(productName)){
						 Integer overlayId = (Integer) varibleMap.get("overlayId");
						 logger.info("Product Name::{} with OverlayId::{}", productName,overlayId);
						 isReadyForDowntime=checkDownTimeForHybridSDWAN(solutionId,overlayId,serviceIdList);
					 }else if("IZOSDWAN".equalsIgnoreCase(productName) || "IZO SDWAN".equalsIgnoreCase(productName)){
						 logger.info("Product Name::{} with OverlayId::{}", productName,serviceId);
						 isReadyForDowntime=checkDownTimeForHybridSDWAN(solutionId,serviceId,serviceIdList);
					 }
					 logger.info("HYBRID isReadyForDowntime::{}", isReadyForDowntime);
				 }else{
					 logger.info("IZOSDWAN SolutionId::{},OrderCode::{},ProductName::{},ServiceId for CheckDowntimeTaskDelegate::{},FlowType::{}", solutionId,orderCode,productName,serviceId,flowType);
					 if("BYON Internet".equalsIgnoreCase(productName) || "IAS".equalsIgnoreCase(productName)
							 || "GVPN".equalsIgnoreCase(productName)  || "IZO Internet WAN".equalsIgnoreCase(productName) || "DIA".equalsIgnoreCase(productName)){
						 Integer overlayId = (Integer) varibleMap.get("overlayId");
						 logger.info("Product Name::{} with OverlayId::{}", productName,overlayId);
						 isReadyForDowntime=checkDownTimeForSDWAN(solutionId,overlayId,serviceIdList);
					 }else if("IZOSDWAN".equalsIgnoreCase(productName) || "IZO SDWAN".equalsIgnoreCase(productName)){
						 logger.info("Product Name::{} with OverlayId::{}", productName,serviceId);
						 isReadyForDowntime=checkDownTimeForSDWAN(solutionId,serviceId,serviceIdList);
					 }
					 logger.info("MACD isReadyForDowntime::{}", isReadyForDowntime);
				 }
				 if(isReadyForDowntime){
						 logger.info("ReadyForDowntime for solutionId::{},orderCode::{} and related serviceId::{}", solutionId,orderCode,serviceId);
						 Boolean isDownTimeTaskOpenedAlready=checkDownTimeTaskOpenedAlready(serviceIdList,orderCode);
						 Map<Integer,Boolean> downTimeSkipMap=new HashMap<>();
						 Boolean isSkip=false;
						 if(destinationCountry.equalsIgnoreCase("India")) {
							 logger.info("ReadyForDowntime.Domestic DestinationCountry for serviceId exists::{}", serviceId);
							 isSkip=checkDownTimeTaskNeeded(solutionId,serviceIdList,orderCode,downTimeSkipMap);
						 }
						 if(isDownTimeTaskOpenedAlready || isSkip){
							 logger.info("isDownTimeTaskOpenedAlready for solutionId::{},orderCode::{} and related serviceId::{}", solutionId,orderCode,serviceId);
							 if(!downTimeSkipMap.isEmpty()){
								 logger.info("downTimeSkipMap exists::{}",downTimeSkipMap.size());
								 for (Integer skipServiceId : downTimeSkipMap.keySet()){
									 logger.info("skipServiceId::{}", skipServiceId);
									 List<Task> waitForCustomerTasks=taskRepository.findByServiceIdAndMstTaskDef_keyAndMstStatus_codeNot(skipServiceId, "wait-for-downtime-from-customer","CLOSED");
									 for(Task waitForCustomerTask: waitForCustomerTasks){
										 logger.info("Task::{}", waitForCustomerTask.getId());
										 Execution downTimeExecution = runtimeService.createExecutionQuery().processInstanceId(waitForCustomerTask.getWfProcessInstId())
													.activityId("wait-for-downtime-from-customer").singleResult();
										if (execution != null) {
											logger.info("Execution exists for service Id::{},with excution id::{}",
													waitForCustomerTask.getServiceId(), downTimeExecution.getId());
											DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
											inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
											String currentDate = inputDateFormatter.format(new Date());
											Date currentParsedDate=null;
											try {
												currentParsedDate = inputDateFormatter.parse(currentDate);
											} catch (ParseException e) {
												logger.error("Error in parsing::{}",e);
											}
											LocalDateTime localDateTime = LocalDateTime.ofInstant(currentParsedDate.toInstant(),
													ZoneId.of("UTC")).plusMinutes(1);
											String flowableDownTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
											logger.info("FlowDownTime::{}", flowableDownTime);
											runtimeService.setVariable(downTimeExecution.getId(), "downtimeWindow", flowableDownTime);
											runtimeService.trigger(downTimeExecution.getId());
										}
									 }
								 }
							 }
							 
							 if(varibleMap.containsKey("downtimeSource") && varibleMap.get("downtimeSource")!=null){
								 String downtimeSource = (String) varibleMap.get("downtimeSource");
								 logger.info("DownTimeSource exists::{}",downtimeSource);
								 if(downtimeSource.contains("end")){
									 logger.info("DownTimeSource::{}",downtimeSource);
									 downTimeMap.put("checkDowntimeAction", "skip");
								 }else{
									 logger.info("DownTimeSource::{}",downtimeSource);
									 downTimeMap.put("checkDowntimeAction", "wait");
								 }
							 }else{
								 logger.info("DownTimeSource not exists");
								 if(isDownTimeTaskOpenedAlready){
									 logger.info("isDownTimeTaskOpenedAlready::",isDownTimeTaskOpenedAlready);
									 downTimeMap.put("checkDowntimeAction", "skip");
								 }else{
									 logger.info("isDownTimeTask Not OpenedAlready::",isDownTimeTaskOpenedAlready);
									 downTimeMap.put("checkDowntimeAction", "open");
								 }
							 }
						 }else{
							 logger.info("isDownTimeTask Not OpenedAlready for solutionId::{},orderCode::{} and related serviceId::{}", solutionId,orderCode,serviceId);
							 downTimeMap.put("checkDowntimeAction", "open");
						 }
					 }else{
						 logger.info("Not ReadyForDowntime for solutionId::{},orderCode::{} and related serviceId::{}", solutionId,orderCode,serviceId);
						 downTimeMap.put("checkDowntimeAction", "wait");
						 Map<Integer,Boolean> downTimeSkipMap=new HashMap<>();
						 Boolean isSkip=false;
						 if(destinationCountry.equalsIgnoreCase("India")) {
							 logger.info("Not ReadyForDowntime.Domestic DestinationCountry for serviceId exists::{}", serviceId);
							 isSkip=checkDownTimeTaskNeeded(solutionId,serviceIdList,orderCode,downTimeSkipMap);
						 }
						if (isSkip && !downTimeSkipMap.isEmpty()) {
							logger.info("downTimeSkipMap exists::{}", downTimeSkipMap.size());
							for (Integer skipServiceId : downTimeSkipMap.keySet()) {
								logger.info("skipServiceId::{}", skipServiceId);
								List<Task> waitForCustomerTasks = taskRepository
										.findByServiceIdAndMstTaskDef_keyAndMstStatus_codeNot(skipServiceId, "wait-for-downtime-from-customer","CLOSED");
								for (Task waitForCustomerTask : waitForCustomerTasks) {
									logger.info("Task::{}", waitForCustomerTask.getId());
									Execution downTimeExecution = runtimeService.createExecutionQuery()
											.processInstanceId(waitForCustomerTask.getWfProcessInstId())
											.activityId("wait-for-downtime-from-customer").singleResult();
									if (execution != null) {
										logger.info("Execution exists for service Id::{},with excution id::{}",
												waitForCustomerTask.getServiceId(), downTimeExecution.getId());
										DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
										inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
										String currentDate = inputDateFormatter.format(new Date());
										Date currentParsedDate = null;
										try {
											currentParsedDate = inputDateFormatter.parse(currentDate);
										} catch (ParseException e) {
											logger.error("Error in parsing::{}", e);
										}
										LocalDateTime localDateTime = LocalDateTime
												.ofInstant(currentParsedDate.toInstant(), ZoneId.of("UTC")).plusMinutes(1);
										String flowableDownTime = localDateTime
												.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
										logger.info("FlowDownTime::{}", flowableDownTime);
										runtimeService.setVariable(downTimeExecution.getId(), "downtimeWindow",
												flowableDownTime);
										runtimeService.trigger(downTimeExecution.getId());
									}
								}
							}
							//downTimeMap.put("checkDowntimeAction", "skip");
						}
						
						if(varibleMap.containsKey("downtimeSource") && varibleMap.get("downtimeSource")!=null){
							 String downtimeSource = (String) varibleMap.get("downtimeSource");
							 logger.info("DownTimeSource exists::{}",downtimeSource);
							 if(downtimeSource.contains("end")){
								 logger.info("DownTimeSource::{}",downtimeSource);
								 downTimeMap.put("checkDowntimeAction", "skip");
							 }else{
								 logger.info("DownTimeSource::{}",downtimeSource);
								 downTimeMap.put("checkDowntimeAction", "wait");
							 }
						 }else{
							 logger.info("DownTimeSource not exists and not ready for downtime");
							 downTimeMap.put("checkDowntimeAction", "wait");
						 }
					 }
					 logger.info("CheckDowntime::{}",downTimeMap.get("checkDowntimeAction"));
					 execution.setVariable("checkDowntimeAction", downTimeMap.get("checkDowntimeAction"));
					 workFlowService.processServiceTaskCompletion(execution,"");	
			 }else{
				 logger.info("OTHER THAN IZOSDWAN ServiceId for CheckDowntimeTaskDelegate::{}", serviceId);
				 Boolean txRequired = (Boolean) varibleMap.get("txRequired");
				 logger.info("TxRequired for CheckDowntimeTaskDelegate::{}", txRequired);
				 Integer stageId=(Integer) varibleMap.get("service_implementation_stage_ID");
				 String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
				 if(StringUtils.isBlank(siteType))siteType="A";
				 logger.info("StageId for CheckDowntimeTaskDelegate::{} siteType::{}", stageId,siteType);
				
				/* List<Task> provideDownTimeTaskList=taskRepository.findByServiceIdAndMstTaskDef_key(serviceId, "provide-downtime");
				 if(Objects.nonNull(provideDownTimeTaskList) && !provideDownTimeTaskList.isEmpty()){
					 logger.info("Provide DownTime task exists");
					 for(Task provideDownTimeTask:provideDownTimeTaskList){
						 if("OPEN".equalsIgnoreCase(provideDownTimeTask.getMstStatus().getCode()) 
								 || "REOPEN".equalsIgnoreCase(provideDownTimeTask.getMstStatus().getCode())){
							 execution.setVariable("checkDowntimeAction", "wait");
						 }else if("CLOSED".equalsIgnoreCase(provideDownTimeTask.getMstStatus().getCode())){
							 execution.setVariable("checkDowntimeAction", "skip");
							 ScComponentAttribute scComponentAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId, "downTime", "LM", "A");
							 if(Objects.nonNull(scComponentAttribute) && Objects.nonNull(scComponentAttribute.getAttributeValue())){
								 logger.info("Skip::{}",scComponentAttribute.getAttributeValue());
								 execution.setVariable("downtimeWindow", scComponentAttribute.getAttributeValue());
							 }
						 }
					 }
				 }else{
					 logger.info("Provide DownTime task doesn't exists");
					 execution.setVariable("checkDowntimeAction", "open");
				 }*/
				 Map<String, String> downTimeMap = new HashMap<>();
				 downTimeMap.put("checkDowntimeAction", "wait");
				 boolean isIpDownTimeTaskClosed=false;
				 ScComponentAttribute scComponentIpDownTimeAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId, "isIpDownTimeRequired", "LM", "A");
				 if(Objects.nonNull(scComponentIpDownTimeAttribute) && Objects.nonNull(scComponentIpDownTimeAttribute.getAttributeValue()) && !scComponentIpDownTimeAttribute.getAttributeValue().isEmpty() && scComponentIpDownTimeAttribute.getAttributeValue().equals("true")){
					 logger.info("isIpDownTimeRequired::{}", scComponentIpDownTimeAttribute.getAttributeValue());
					 com.tcl.dias.servicefulfillment.entity.entities.Process process=processRepository.findFirstByStage_idAndMstProcessDef_keyOrderByIdDesc(stageId,"assign-dummy-config-process");
					 if(Objects.nonNull(process) && process.getMstStatus().getCode().equals("CLOSED")){
						 logger.info("DownTime assign-dummy-config-process closed");
						 isIpDownTimeTaskClosed=true;
					 }
				 }else {
					 logger.info("isIpDownTimeRequired::{} hence setting_isIpDownTimeTaskClosed_as_true "); 
					 isIpDownTimeTaskClosed = true;
				 }
				 
				 
				 if(isIpDownTimeTaskClosed){
					 logger.info("IpDownTimeTaskClosed");
					 if(txRequired){
						 logger.info("txRequired");
						 ScComponentAttribute scComponentTxDownTimeAttribute= scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId, "isTxDowntimeReqd", "LM", "A");
						 if(Objects.nonNull(scComponentTxDownTimeAttribute) && Objects.nonNull(scComponentTxDownTimeAttribute.getAttributeValue()) && !scComponentTxDownTimeAttribute.getAttributeValue().isEmpty() && scComponentTxDownTimeAttribute.getAttributeValue().equals("true")){
							 logger.info("isTxDowntimeReqd::{}",scComponentTxDownTimeAttribute.getAttributeValue());
							 com.tcl.dias.servicefulfillment.entity.entities.Process process=processRepository.findFirstByStage_idAndMstProcessDef_keyOrderByIdDesc(stageId,"lm_cable_extension_process");
							 if(Objects.nonNull(process) && process.getMstStatus().getCode().equals("CLOSED") || productName.equalsIgnoreCase("npl") ){
								 logger.info("lm_cable_extension_process closed");
								 downTimeMap.put("checkDowntimeAction", "open");
							 }else{
								 logger.info("lm_cable_extension_process not closed");
								 downTimeMap.put("checkDowntimeAction", "wait");
							 }
						 }else{
							 logger.info("isTxDowntimeReqd not exists or not required");
							 downTimeMap.put("checkDowntimeAction", "open");
						 }
					 }else{
						 logger.info("tx not required");
						 downTimeMap.put("checkDowntimeAction", "open");
					 }
				 }else{
					 logger.info("isIpDownTimeTask Not Closed");
					 downTimeMap.put("checkDowntimeAction", "wait");
				 }
				 
				 if(txRequired){
					 logger.info("txRequired");
					 ScComponentAttribute scComponentTxDownTimeAttribute= scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId, "isTxDowntimeReqd", "LM", "A");
					 if(Objects.nonNull(scComponentTxDownTimeAttribute) && Objects.nonNull(scComponentTxDownTimeAttribute.getAttributeValue()) && !scComponentTxDownTimeAttribute.getAttributeValue().isEmpty() && scComponentTxDownTimeAttribute.getAttributeValue().equals("true")){
						 logger.info("isTxDowntimeReqd::{}",scComponentTxDownTimeAttribute.getAttributeValue());
						 com.tcl.dias.servicefulfillment.entity.entities.Process process=processRepository.findFirstByStage_idAndMstProcessDef_keyOrderByIdDesc(stageId,"lm_cable_extension_process");
						 if(Objects.nonNull(process) && process.getMstStatus().getCode().equals("CLOSED") || productName.equalsIgnoreCase("npl") ){
							 logger.info("lm_cable_extension_process closed");
							 downTimeMap.put("checkDowntimeAction", "open");
						 }else{
							 logger.info("lm_cable_extension_process not closed");
							 downTimeMap.put("checkDowntimeAction", "wait");
						 }
					 }else{
						 logger.info("tx not required, validating Ip");
						 if(Objects.nonNull(scComponentIpDownTimeAttribute) && Objects.nonNull(scComponentIpDownTimeAttribute.getAttributeValue()) && !scComponentIpDownTimeAttribute.getAttributeValue().isEmpty() && scComponentIpDownTimeAttribute.getAttributeValue().equals("true")){
							 logger.info("IpDownTimeAttributeValue::{}",scComponentIpDownTimeAttribute.getAttributeValue());
							 if(isIpDownTimeTaskClosed){
								 logger.info("isIpDownTimeTaskClosed");
								 downTimeMap.put("checkDowntimeAction", "open");
							 }else{
								 logger.info("IpDownTimeTask not Closed");
								 downTimeMap.put("checkDowntimeAction", "wait");
							 }
						 }else{
							 logger.info("IpDownTimeAttributeValue not exists or not required");
							 downTimeMap.put("checkDowntimeAction", "open");
						 }
					 }
				 }
				 logger.info("CheckDowntime::{}",downTimeMap.get("checkDowntimeAction"));
				 execution.setVariable("checkDowntimeAction", downTimeMap.get("checkDowntimeAction"));
				 workFlowService.processServiceTaskCompletion(execution,"");
			 }
		 }
		
	 }

	private Boolean checkDownTimeForHybridSDWAN(Integer solutionId, Integer overlayId, List<Integer> serviceIdList) {
		logger.info("checkDownTimeForHybridSDWAN with solutionId::{},overlayId::{}",solutionId,overlayId);
		List<String> componentGroups = new ArrayList<>();
		componentGroups.add("OVERLAY");
		componentGroups.add("UNDERLAY");
		Boolean[] isReadyForDowntime={false};
		List<ScSolutionComponent> scSolutionComponentList=scSolutionComponentRepository.findByScServiceDetail3_idAndScServiceDetail2_idOrScServiceDetail1_idAndComponentGroupInAndIsActive(solutionId, overlayId,overlayId,componentGroups, "Y");
		if(scSolutionComponentList!=null && !scSolutionComponentList.isEmpty()){
			logger.info("ScSolutionComponentList size::{}",scSolutionComponentList.size());
			for(ScSolutionComponent scSolComp:scSolutionComponentList){
				if(("OVERLAY".equalsIgnoreCase(scSolComp.getComponentGroup()))||(("IAS".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
						|| "GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
						|| "IZO Internet WAN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
						|| "DIA".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()))
				&& (scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("MACD") &&
						((!scSolComp.getScServiceDetail1().getOrderCategory().equalsIgnoreCase("ADD_SITE") && 
						scSolComp.getScServiceDetail1().getOrderSubCategory()!=null && !scSolComp.getScServiceDetail1().getOrderSubCategory().toLowerCase().contains("parallel")) 
						|| ("CHANGE_ORDER".equalsIgnoreCase(scSolComp.getScServiceDetail1().getOrderCategory())
								&& scSolComp.getScServiceDetail1().getOrderSubCategory()==null))))){
					logger.info("MACD IASOrGVPN::{}",scSolComp.getScServiceDetail1().getId());
					serviceIdList.add(scSolComp.getScServiceDetail1().getId());
				}
			}
			for(ScSolutionComponent scSolComp:scSolutionComponentList){
				if("OVERLAY".equalsIgnoreCase(scSolComp.getComponentGroup())){
					isReadyForDowntime[0]=checkDownTimeforOverlay(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForDowntime[0]);
					logger.info("Overlay isReadyForDowntime::{}",isReadyForDowntime[0]);
				}else if((("IZO Internet WAN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) && !scSolComp.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India"))
						|| "DIA".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
						|| ("GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) 
								&& !scSolComp.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India")))
						&& (scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("MACD") &&
								((!scSolComp.getScServiceDetail1().getOrderCategory().equalsIgnoreCase("ADD_SITE") && 
								scSolComp.getScServiceDetail1().getOrderSubCategory()!=null && !scSolComp.getScServiceDetail1().getOrderSubCategory().toLowerCase().contains("parallel")) 
								|| ("CHANGE_ORDER".equalsIgnoreCase(scSolComp.getScServiceDetail1().getOrderCategory())
										&& scSolComp.getScServiceDetail1().getOrderSubCategory()==null)))){
					isReadyForDowntime[0]=checkDowntimeforDIAIWANGVPNInternational(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForDowntime[0]);
					logger.info("IWANDIAGVPNIntl Underlay isReadyForDowntime::{}",isReadyForDowntime[0]);
				}else if(("IAS".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
						|| "GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()))
						&& (scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("MACD") &&
						((!scSolComp.getScServiceDetail1().getOrderCategory().equalsIgnoreCase("ADD_SITE") && 
						scSolComp.getScServiceDetail1().getOrderSubCategory()!=null && !scSolComp.getScServiceDetail1().getOrderSubCategory().toLowerCase().contains("parallel")) 
						|| ("CHANGE_ORDER".equalsIgnoreCase(scSolComp.getScServiceDetail1().getOrderCategory())
								&& scSolComp.getScServiceDetail1().getOrderSubCategory()==null)))){
					isReadyForDowntime[0]=checkDownTimeforIASGVPN(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForDowntime[0]);
					logger.info("IAS or GVPN Underlay isReadyForDowntime::{}",isReadyForDowntime[0]);
				}else{
					logger.info("checkDownTimeForHybridSDWAN skip service id::{} for group::{}",scSolComp.getScServiceDetail1().getId(),scSolComp.getComponentGroup());
					continue;
				}
				if(!isReadyForDowntime[0]){
					logger.info("isReadyForDowntime breaks for componentGroup::{} with service id::{}",scSolComp.getComponentGroup(),scSolComp.getScServiceDetail1().getId());
					break;
				}
			}
		}
		return isReadyForDowntime[0];
	}

	private Boolean checkDowntimeforDIAIWANGVPNInternational(Integer solutionId, Integer serviceId,Boolean isReadyForDowntime) {
		logger.info("checkDowntimeforDIAIWANGVPNInternational SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if(downTimeDetails!=null && "Y".equalsIgnoreCase(downTimeDetails.getIsProvisionReadyForDownTime())){
			 logger.info("IWANDIAGVPNIntl Service Id::{} Ready For Downtime::{}",serviceId,downTimeDetails.getId());
			 isReadyForDowntime=true;
		}else{
			 logger.info("IWANDIAGVPNIntl Service Id::{} Not Ready For Downtime::{}",serviceId,downTimeDetails.getId());
			 isReadyForDowntime=false;
		}
		return isReadyForDowntime;
	}
	
	private Boolean checkDownTimeTaskNeeded(Integer solutionId, List<Integer> serviceIdList, String orderCode, Map<Integer, Boolean> downTimeSkipMap) {
		logger.info("checkDownTimeTaskNeeded with solutionId::{},serviceId::{},orderCode::{}",solutionId,serviceIdList,orderCode);
		List<String> productList= new ArrayList<>();
		productList.add("IAS");
		productList.add("GVPN");
		Boolean isSkip=false;
		List<DownTimeDetails> downTimeDetailList=downTimeDetailsRepository.findByOrderCodeAndSolutionIdAndScServiceDetailIdInAndIsCpeAlreadyManagedAndProductNameInOrderByIdDesc(orderCode,solutionId, serviceIdList,"NA",productList);
		for(DownTimeDetails downTimeDetails:downTimeDetailList){
			logger.info("DownTime Id::{}",downTimeDetails.getId());
			if("N".equalsIgnoreCase(downTimeDetails.getIsTxDownTimeRequired()) 
					&& "N".equalsIgnoreCase(downTimeDetails.getIsIpDownTimeRequired())
					&& "N".equalsIgnoreCase(downTimeDetails.getIsConfigCompleted())){
				logger.info("Skip as there is no downtime task for ip and tx and still config not completed for solution::{},serviceId::{}",solutionId,downTimeDetails.getScServiceDetailId());
				isSkip= true;
				downTimeSkipMap.put(downTimeDetails.getScServiceDetailId(), true);
			}
		}
		return isSkip;
	}

	private Boolean checkDownTimeTaskOpenedAlready(List<Integer> serviceIdList, String orderCode) {
		logger.info("checkDownTimeTaskOpenedAlready with serviceIdList::{},orderCode::{}",serviceIdList.size(),orderCode);
		Task provideDownTimeTask=taskRepository.findFirstByServiceIdInAndOrderCodeAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceIdList, orderCode, "provide-downtime");
		if(provideDownTimeTask!=null){
			logger.info("ProvideDowntimetask exists already::{}",provideDownTimeTask.getId());
			return true;
		}else{
			logger.info("ProvideDowntimetask not exists for orderCode::{}",orderCode);
			return false;
		}
	}

	private Boolean checkDownTimeForSDWAN(Integer solutionId, Integer overlayId, List<Integer> serviceIdList) {
		logger.info("checkDownTimeForSDWAN with solutionId::{},overlayId::{}",solutionId,overlayId);
		List<String> componentGroups = new ArrayList<>();
		componentGroups.add("OVERLAY");
		componentGroups.add("UNDERLAY");
		Boolean[] isReadyForDowntime={false};
		List<ScSolutionComponent> scSolutionComponentList=scSolutionComponentRepository.findByScServiceDetail3_idAndScServiceDetail2_idOrScServiceDetail1_idAndComponentGroupInAndIsActive(solutionId, overlayId,overlayId,componentGroups, "Y");
		if(scSolutionComponentList!=null && !scSolutionComponentList.isEmpty()){
			logger.info("ScSolutionComponentList size::{}",scSolutionComponentList.size());
			for(ScSolutionComponent scSolComp:scSolutionComponentList){
				serviceIdList.add(scSolComp.getScServiceDetail1().getId());
			}
			for(ScSolutionComponent scSolComp:scSolutionComponentList){
				if("OVERLAY".equalsIgnoreCase(scSolComp.getComponentGroup())){
					isReadyForDowntime[0]=checkDownTimeforOverlay(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForDowntime[0]);
					logger.info("Overlay isReadyForDowntime::{}",isReadyForDowntime[0]);
				}else if("BYON Internet".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())){
					isReadyForDowntime[0]=checkDownTimeforBYONInternet(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForDowntime[0]);
					logger.info("BYON Underlay isReadyForDowntime::{}",isReadyForDowntime[0]);
				}else if(("IZO Internet WAN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) && !scSolComp.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India"))
						|| "DIA".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
						|| ("GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()) 
								&& !scSolComp.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India"))){
					isReadyForDowntime[0]=checkDowntimeforDIAIWANGVPNInternational(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForDowntime[0]);
					logger.info("IWANDIAGVPNIntl Underlay isReadyForDowntime::{}",isReadyForDowntime[0]);
				}else if("IAS".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
								|| "GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())){
					isReadyForDowntime[0]=checkDownTimeforIASGVPN(solutionId,scSolComp.getScServiceDetail1().getId(),isReadyForDowntime[0]);
					logger.info("IAS or GVPN Underlay isReadyForDowntime::{}",isReadyForDowntime[0]);
				}
				if(!isReadyForDowntime[0]){
					logger.info("isReadyForDowntime breaks for componentGroup::{} with service id::{}",scSolComp.getComponentGroup(),scSolComp.getScServiceDetail1().getId());
					break;
				}
			}
		}
		return isReadyForDowntime[0];
	}

	private Boolean checkDownTimeforIASGVPN(Integer solutionId,Integer serviceId, Boolean isReadyForDowntime) {
		logger.info("checkDownTimeforIASGVPN SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if(downTimeDetails!=null){
			logger.info("DownTimeDetails exists with Id::{}", downTimeDetails.getId());
			if("Y".equalsIgnoreCase(downTimeDetails.getIsCpeAlreadyManaged())){
				logger.info("Is Cpe Already Managed for service id::{}", serviceId);
				isReadyForDowntime = true;
				return isReadyForDowntime;
			}
			if(!"NA".equalsIgnoreCase(downTimeDetails.getIsIpDownTimeRequired())){
				if ("Y".equalsIgnoreCase(downTimeDetails.getIsIpDownTimeRequired())) {
					logger.info("Ip Task Not NA for service id::{}", serviceId);
					if ("Y".equalsIgnoreCase(downTimeDetails.getIsIpReadyForDownTime())) {
						logger.info("Ip Ready for downtime::{}",downTimeDetails.getId());
						isReadyForDowntime = true;
					} else {
						logger.info("Ip Not Ready for downtime::{}",downTimeDetails.getId());
						isReadyForDowntime = false;
						return isReadyForDowntime;
					}
				}else{
					logger.info("Ip Task N for service id::{}",downTimeDetails.getId());
					isReadyForDowntime = true;
				}
			}else{
				logger.info("Ip Task NA for service id::{}",downTimeDetails.getId());
				isReadyForDowntime = false;
				return isReadyForDowntime;
			}
			
			if(!"NA".equalsIgnoreCase(downTimeDetails.getIsTxDownTimeRequired())){
				logger.info("Tx Task Not NA for service id::{}", serviceId);
				if ("Y".equalsIgnoreCase(downTimeDetails.getIsTxDownTimeRequired())) {
					logger.info("TxDowntime Required for service id::{}", serviceId);
					if ("Y".equalsIgnoreCase(downTimeDetails.getIsTxReadyForDownTime())) {
						logger.info("Tx Ready for downtime::{}",downTimeDetails.getId());
						isReadyForDowntime = true;
					} else {
						logger.info("Tx Not Ready for downtime::{}",downTimeDetails.getId());
						isReadyForDowntime = false;
						return isReadyForDowntime;
					}
				}else{
					logger.info("Tx Task N for service id::{}",downTimeDetails.getId());
					isReadyForDowntime = true;
				}
			}else{
				logger.info("Tx Task NA for service id::{}",downTimeDetails.getId());
				isReadyForDowntime = false;
				return isReadyForDowntime;
			}
			
			/*if (!"NA".equalsIgnoreCase(downTimeDetails.getIsTxDownTimeRequired()) && !"NA".equalsIgnoreCase(downTimeDetails.getIsIpDownTimeRequired())
					&& serviceIdList.size()>2) {
				logger.info("Check Config completed::{}",downTimeDetails.getId());
				if ("Y".equalsIgnoreCase(downTimeDetails.getIsConfigCompleted())) {
					logger.info("Activation Task closed::{}",downTimeDetails.getId());
					isReadyForDowntime = true;
				} else {
					logger.info("Activation Task Not Closed for service id::{}",downTimeDetails.getId());
					isReadyForDowntime = false;
				}
			}*//*else if("NA".equalsIgnoreCase(downTimeDetails.getIsTxDownTimeRequired()) && "NA".equalsIgnoreCase(downTimeDetails.getIsIpDownTimeRequired())
					&& serviceIdList.size()==2){
				logger.info("Only Overlay and IAS or GVPN Underlay::{}",downTimeDetails.getId());
				isReadyForDowntime = true;
			}*/
		}
		return isReadyForDowntime;
	}

	private Boolean checkDownTimeforOverlay(Integer solutionId,Integer serviceId, Boolean isReadyForDowntime) {
		logger.info("checkDownTimeforOverlay SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if(downTimeDetails!=null && "Y".equalsIgnoreCase(downTimeDetails.getIsCpeDowntimeRequired())){
			logger.info("Cpe Downtime Required::{}",downTimeDetails.getId());
			if("Y".equalsIgnoreCase(downTimeDetails.getIsCpeReadyForDownTime())){
				 logger.info("Cpe Ready For DownTime::{}",downTimeDetails.getId());
				 isReadyForDowntime=true;
			}else{
				 logger.info("Cpe Not Ready For DownTime::{}",downTimeDetails.getId());
				 isReadyForDowntime=false;
			}
		}
		return isReadyForDowntime;
	}

	private Boolean checkDownTimeforBYONInternet(Integer solutionId,Integer serviceId, Boolean isReadyForDowntime) {
		logger.info("checkDownTimeforBYONInternet SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if(downTimeDetails!=null && "Y".equalsIgnoreCase(downTimeDetails.getIsByonReadyForDownTime())){
			 logger.info("Byon Ready For DownTime::{}",downTimeDetails.getId());
			 isReadyForDowntime=true;
		}else{
			 logger.info("Byon Not Ready For DownTime::{}",downTimeDetails.getId());
			 isReadyForDowntime=false;
		}
		return isReadyForDowntime;
	}

}
