package com.tcl.dias.l2oworkflowutils.service.v1;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.FetchResponseBean;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.MfLinkDetailsBean;
import com.tcl.dias.common.beans.MfNplResponseDetailBean;
import com.tcl.dias.common.beans.MfResponseDetailBean;
import com.tcl.dias.common.beans.MfSiteADetailsBean;
import com.tcl.dias.common.beans.MfSiteBDetailsBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfNplResponseDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfResponseDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.repository.MfDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfNplResponseDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfResponseDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
import com.tcl.dias.l2oworkflowutils.beans.LinkDetailsManualBean;
import com.tcl.dias.l2oworkflowutils.beans.OmsTaskDetailBean;
import com.tcl.dias.l2oworkflowutils.beans.QuoteDetailForTask;
import com.tcl.dias.l2oworkflowutils.constants.ExceptionConstants;
import com.tcl.dias.l2oworkflowutils.constants.ManualFeasibilityWFConstants;
import com.tcl.dias.l2oworkflowutils.constants.TaskLogConstants;
import com.tcl.dias.l2oworkflowutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;


@Service
public class NPLTaskService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NPLTaskService.class);
	
	@Autowired
	MfResponseDetailRepository mfResponseDetailRepository;

	@Autowired
	TaskService taskService;
	
	@Autowired
	CmmnRuntimeService cmmnRuntimeService;
	
	@Autowired
	protected TaskRepository taskRepository;
	
	@Autowired
	MfDetailRepository mfDetailRepository;
	
	@Autowired
	MfNplResponseDetailRepository mfNplResponseDetailRepository;
	
	@Value("${rabbitmq.fetch.mf.response.in.oms.mq}")
	String fetchMFResponseInOmsMQ;
	
	@Value("${rabbitmq.save.mf.npl.response.in.oms.mq}")
	String saveMFResponseInOmsMQ;
	
	@Autowired
	MfTaskDetailRepository mfTaskDetailRepository;
	

	
	@Autowired
	MQUtils mqUtils;
	
	public static final String RETURNED = "Return";
	public static final String FEASIBLE = "FEASIBLE";


	
	/**
	 * used to complete an mf task manually
	 * 
	 * @param taskId
	 * @param map
	 * @return
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public String completeMfTask(Integer taskId, Map<String, Object> map, boolean isDependantTask, String mfStatus) {
		try {
			Task task = taskService.getTaskById(taskId);
			String group = "";
			String type = ManualFeasibilityWFConstants.PRIMARY; // assume it when not supplied
			if (map != null && !map.isEmpty()) {
				group = (String) map.get("group");
				type = (String) map.get("type");
			}
			if (mfStatus.equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN)) {
				taskService.mfTaskDataEntry(task, map, map, isDependantTask, TaskStatusConstants.RETURNED);
				taskService.processMfTaskLogDetails(task, TaskLogConstants.RETURNED, "", null, group);
				processTaskCompletion(task, map, mfStatus, type);
			} else {
				taskService.mfTaskDataEntry(task, map, map, isDependantTask, TaskStatusConstants.CLOSED_STATUS);
				taskService.processMfTaskLogDetails(task, TaskLogConstants.CLOSED, "", null, group);
				processTaskCompletion(task, map, mfStatus, type);
			}
			// Trigger Mail
			taskService.decideMailTriggerBasedOnStatus(mfStatus, task);

			return "SUCCESS";
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}
	
	public void processTaskCompletion(Task task, Map<String, Object> map, String mfStatus, String type)
			throws TclCommonException {

		if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)) {
			taskService.closeAllMfManualTasks(task.getWfProcessInstId(), map);
				processMfResponse(task, mfStatus);
		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP)) {
				processMfResponse(task, mfStatus);
		}
	}

	
	public void processMfResponse(Task task, String mfStatus) {

		Integer siteId = task.getSiteId();
		Integer quoteId = task.getQuoteId();
		MfDetail mfDetail = task.getMfDetail();
		Map<String, Object> map = new HashMap<>();
		Map<Integer, String> siteStatus = (HashMap<Integer, String>) cmmnRuntimeService
				.getVariables(task.getWfProcessInstId()).get("siteStatus");
		String siteMfStatus = siteStatus.get(siteId);
		if (StringUtils.isEmpty(siteMfStatus)
				|| ManualFeasibilityWFConstants.NOT_FEASIBLE.equalsIgnoreCase(siteMfStatus)
				|| ManualFeasibilityWFConstants.RETURN.equalsIgnoreCase(siteMfStatus))
			siteStatus.put(siteId, mfStatus);

		cmmnRuntimeService.setVariable(task.getWfProcessInstId(), ManualFeasibilityWFConstants.SITESTATUS, siteStatus);

		LOGGER.info("ProcessMFResponse invoked for {} Id={} mfDetailId={}", task.getMstTaskDef().getKey(), task.getId(),
				mfDetail.getId());
		if (mfDetail != null) {
			LOGGER.info("MF detail ID : {} ", mfDetail.getId());
			try {
				// Update task closed status to MfDetail
				mfDetail.setStatus(TaskStatusConstants.CLOSED_STATUS);
				mfDetail.setIsActive(CommonConstants.INACTIVE);
				mfDetailRepository.save(mfDetail);
				LOGGER.info("Saving close status to mfDetail {} of the task {}", mfDetail.getId(),task.getId());
			} catch (IllegalArgumentException e) {
				LOGGER.error("MF response process failed ", e);
			}
			
			map.put("quoteId", quoteId);
		
			if(taskService.checkIfTaskClosed(task.getWfProcessInstId(), "asp")
					&& taskService.checkIfTaskClosed(task.getWfProcessInstId(), "afm")) {
				selectRelevantManualFeasibleResponse(siteId, mfStatus, mfDetail);
			}
			try {
				List<Task> mfTaskList = taskRepository.findByQuoteId(quoteId).stream()
						.filter(mfTask -> mfTask.getMfDetail() != null).collect(Collectors.toList());
				Set<String> caseInstanceIds = new HashSet<String>();
				mfTaskList.stream().forEach(mfTask -> {
							caseInstanceIds.add(mfTask.getWfProcessInstId());
					});
				LOGGER.info("Total tasks for a quote : {}", mfTaskList.size());
				List<Task> completedTasks = null;
				Map<Integer,String> allSiteStatusesOfQuote = new HashedMap<Integer,String>();
				
				if (!ManualFeasibilityWFConstants.RETURN.equalsIgnoreCase(mfStatus)) {
					LOGGER.info("Checking if tasks closed for all sites of the quote. ");
					completedTasks = mfTaskList.stream().filter(taskDetail -> taskDetail.getMstStatus().getCode()
							.equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)
							|| taskDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.DELETED))
							.collect(Collectors.toList());
					if (mfTaskList.size() == completedTasks.size()) {
						LOGGER.info("All tasks closed for all sites of the quote {} ", quoteId);
						
						LOGGER.info("Total case instances for the quote {}  : {} ", quoteId, caseInstanceIds.size());
						
						
						caseInstanceIds.forEach(id ->  {
							HashMap<Integer, String> individualStatus= (HashMap<Integer, String>) cmmnRuntimeService.getVariable(id,
									ManualFeasibilityWFConstants.SITESTATUS);
							individualStatus.entrySet().stream().forEach(indStatusEntry -> {
								if (allSiteStatusesOfQuote.containsKey(indStatusEntry.getKey())) {
									String status = allSiteStatusesOfQuote.get(indStatusEntry.getKey());
									LOGGER.info("Status in allSiteStatusMap for site Id {} is {} ",
											indStatusEntry.getKey(), indStatusEntry.getValue());
									if (!status.equalsIgnoreCase(ManualFeasibilityWFConstants.FEASIBLE)
											&& !status.equalsIgnoreCase(indStatusEntry.getValue())) {
										LOGGER.info("Updating allSiteStatusMap for site {} from {} to {}",
												indStatusEntry.getKey(), status, indStatusEntry.getValue());
										allSiteStatusesOfQuote.put(indStatusEntry.getKey(), indStatusEntry.getValue());
									}
								} else {
									LOGGER.info("Status not present in the map for site {} ", indStatusEntry.getKey());
									allSiteStatusesOfQuote.put(indStatusEntry.getKey(), indStatusEntry.getValue());
								}
							});
						});
						LOGGER.info("Total Number of site status maps : {}",allSiteStatusesOfQuote.size());
						
						 checkForSystemLinks(allSiteStatusesOfQuote);
						 // If both the sites are not feasible then both tasks are available in WBench
						checkForLinksInMFTasks(quoteId,allSiteStatusesOfQuote);
						
						// Now at the point sites which have feasible links will have linkJson attribute in mfDetail bean.
						// based on that we will populate mf_npl_response_detail table
						saveMfResponseDetailsInLinkResponseTable(quoteId, allSiteStatusesOfQuote, task.getId(),task.getServiceType(),mfDetail);
						saveMfNplResponseDetailsInOms(quoteId);

					}
				} else {
					// return scenario
					LOGGER.info("Site status of siteId {}  is {} ",siteId, mfStatus);

					allSiteStatusesOfQuote.put(siteId, mfStatus);
					List<MfDetail> mfDetailEnitity = mfDetailRepository.findBySiteId(siteId);
					if (mfDetailEnitity != null && !mfDetailEnitity.isEmpty()) {
						List<MfNplResponseDetailBean> mfResponseDetailBeans = new ArrayList<MfNplResponseDetailBean>();
						MfNplResponseDetailBean respBean = new MfNplResponseDetailBean();
						respBean.setReturnedSiteType(mfDetailEnitity.get(0).getSiteType().equals("SiteA")? "SiteA" :"SiteB");
						respBean.setLinkId(mfDetailEnitity.get(0).getLinkId());
						respBean.setFeasibilityStatus(ManualFeasibilityWFConstants.RETURN);
						respBean.setFeasibilityCheck("Manual");
						respBean.setQuoteId(quoteId);
						respBean.setIsSelected(0);
						mfResponseDetailBeans.add(respBean);
						saveMfResponseDetailMQ(mfResponseDetailBeans);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error in fetching response details {}", e.getMessage());
			}
		}
	}
	
	
	public MfResponseDetail selectRelevantManualFeasibleResponse(Integer siteId, String siteStatus, MfDetail mfDetail) {
		MfResponseDetail selectedResponse = null;
		if (!siteStatus.equalsIgnoreCase(RETURNED)) {

			List<MfResponseDetail> mfResponsDetails = mfResponseDetailRepository.findBySiteId(siteId);

			List<MfResponseDetail> mfResponseForAEnd = new ArrayList<MfResponseDetail>();
			List<MfResponseDetail> mfResponseForBEnd = new ArrayList<MfResponseDetail>();


			MfDetailAttributes mfDetailAttr = null;
			try {
				mfDetailAttr = Utils.convertJsonToObject(mfDetail.getMfDetails(), MfDetailAttributes.class);
			} catch (TclCommonException e) {
				LOGGER.error("Error while converting json to object", e);
			}

			if (mfDetail.getSiteType().equalsIgnoreCase("SiteA")) {
				mfResponseForAEnd = mfResponsDetails.stream().filter(x -> x.getType() != null)
						.filter(y -> y.getType().equals("A-End")).collect(Collectors.toList());
				if(mfDetailAttr.isRetriggerTaskForFeasibleSites()){
					LOGGER.info("setting all response isSelected to 0 -- site a loop");
					mfResponseForAEnd.forEach( mfResponseDetailA -> {
						mfResponseDetailA.setIsSelected(0);
						mfResponseDetailRepository.save(mfResponseDetailA);
					});
				}
			
			if (mfDetailAttr.getMfLinkType().equals("Intracity")) {
				List<MfResponseDetail> p2pList = mfResponsDetails.stream().filter(x -> x.getType() != null)
						.filter(y -> y.getType().equals("P2P")).collect(Collectors.toList());

				if (!CollectionUtils.isEmpty(p2pList)) {
					mfResponseForAEnd.clear();
					mfResponseForAEnd.addAll(p2pList);

				}else {
					List<MfResponseDetail> mfResponseForIntraB = mfResponsDetails.stream().filter(x -> x.getType() != null)
							.filter(y -> y.getType().equals("B-End")).collect(Collectors.toList());
					if(mfDetailAttr.isRetriggerTaskForFeasibleSites())
						mfResponseForIntraB.forEach( mfResponseDetailB -> {
									mfResponseDetailB.setIsSelected(0);
									mfResponseDetailRepository.save(mfResponseDetailB);
								}
						);

					MfResponseDetail selectedItraB = segregateResponse(siteId,mfResponseForIntraB);
					taskService.saveIsSelectedForSelectedMfResponse(selectedItraB);
				}
			}
			selectedResponse = segregateResponse(siteId, mfResponseForAEnd);
			}

			if (mfDetail.getSiteType().equals("SiteB")) {
		
					mfResponseForBEnd = mfResponsDetails.stream().filter(x -> x.getType() != null)
							.filter(y -> y.getType().equals("B-End")).collect(Collectors.toList());
				if(mfDetailAttr.isRetriggerTaskForFeasibleSites()){
					LOGGER.info("setting all  b response isSelected to 0 -- site b loop");
					mfResponseForBEnd.forEach( mfResponseDetailB -> {
						mfResponseDetailB.setIsSelected(0);
						mfResponseDetailRepository.save(mfResponseDetailB);
					});
				}
				
				if (mfDetailAttr.getMfLinkType().equals("Intracity")) {
					List<MfResponseDetail> p2pList = mfResponsDetails.stream().filter(x -> x.getType() != null)
							.filter(y -> y.getType().equals("P2P")).collect(Collectors.toList());
					// For intra City Bend ,Aend , and P2p responses are posible.
			
					if (!CollectionUtils.isEmpty(p2pList)) {
						mfResponseForBEnd.clear();
						mfResponseForBEnd.addAll(p2pList);

					}else {
						
						List<MfResponseDetail> mfResponseForIntraA = mfResponsDetails.stream().filter(x -> x.getType() != null)
								.filter(y -> y.getType().equals("A-End")).collect(Collectors.toList());
						// putting rank for both Aend bends response if intracity P2p response is not available.
						if(mfDetailAttr.isRetriggerTaskForFeasibleSites())
							mfResponseForIntraA.forEach( mfResponseDetailA -> {
										mfResponseDetailA.setIsSelected(0);
										mfResponseDetailRepository.save(mfResponseDetailA);
									}
							);

						MfResponseDetail selectedItraA = segregateResponse(siteId,mfResponseForIntraA);
						taskService.saveIsSelectedForSelectedMfResponse(selectedItraA);

					}
				}
				selectedResponse=segregateResponse(siteId, mfResponseForBEnd);
			}
		}
		taskService.saveIsSelectedForSelectedMfResponse(selectedResponse);
		return selectedResponse;
	}
	
	
	
	
	/**
	 * @param allSiteStatusesOfQuote
	 * @return 
	 */
	private void checkForSystemLinks(Map<Integer, String> allSiteStatusesOfQuote) {
		LOGGER.info("Inside NPLTaskService#checkForSystemLinks method");

		allSiteStatusesOfQuote.forEach( (k,v) ->{
			LOGGER.info("Status of siteID {} is {}", k,v);
			if(v.equalsIgnoreCase("Feasible")) {
			List<MfDetail> mfDetailList=mfDetailRepository.findBySiteId(k);
			// for macd we need to list of mfDetails
			MfDetail mfDetailOpt  = null;
			MfDetailsBean mfDetailsBean =null;
			if(mfDetailList!=null && !mfDetailList.isEmpty()) {
		    	 mfDetailOpt = mfDetailList.get(0);
		    	 mfDetailsBean =getMfDetailsBean(mfDetailOpt);
		    	 mfDetailsBean.setSiteId(mfDetailOpt.getSiteId());
			}
				
				MfDetailAttributes mfDetailAttrs = mfDetailsBean.getMfDetails();
				if (mfDetailOpt!=null && !mfDetailsBean.getMfDetails().getMfLinkEndType().equalsIgnoreCase("P2P")) {
					LOGGER.info("Inside non P2p case .The siteId {} has linkType {}", k, mfDetailsBean.getMfDetails().getMfLinkEndType());
					if ( mfDetailOpt.getSiteType().equalsIgnoreCase("SiteA")) {
						MfDetail siteBAvailablility = mfDetailRepository
								.findByLinkIdAndSiteTypeAndStatus(mfDetailOpt.getLinkId(), "SiteB","CLOSED");
						if (siteBAvailablility == null
								&& mfDetailAttrs.getbEndPredictedAcessFeasibility().startsWith("Feasible")) {
							Map<String, Object> systemAttrMap = filterSystemResponseBasedOnPrefix("b_",
									mfDetailOpt.getSystemLinkResponseJson());
							LOGGER.info("Site A Task available and siteB is system feasible");
							LOGGER.info("b_ filtered system prefix",systemAttrMap);

							MfResponseDetail selectedResponse = mfResponseDetailRepository
									.findBySiteIdAndTypeAndIsSelected(mfDetailsBean.getSiteId(), "A-End", 1);
						   
							Map<String, Object> selectedMap = filterSystemResponseBasedOnPrefix(null,selectedResponse.getCreateResponseJson());
							selectedMap.putAll(systemAttrMap);
							JSONObject json = new JSONObject();
						    json.putAll(selectedMap);
							LOGGER.info("Combined link json when B is system feasible",json);
							//mfDetailOpt.setMfLinkResponseJson(json.toString());
							
							mfDetailList.forEach( x -> x.setMfLinkResponseJson(json.toString()));
							    
						}

					}else if(mfDetailOpt.getSiteType().equalsIgnoreCase("SiteB")) {
						MfDetail siteAAvailablility = mfDetailRepository
								.findByLinkIdAndSiteTypeAndStatus(mfDetailOpt.getLinkId(), "SiteA","CLOSED");
						if (siteAAvailablility == null
								&& mfDetailAttrs.getaEndPredictedAcessFeasibility().startsWith("Feasible")) {
							Map<String, Object> systemAttrMap =  filterSystemResponseBasedOnPrefix("a_",mfDetailOpt.getSystemLinkResponseJson());
							
							LOGGER.info("Site B Task available and siteA is system feasible");
							LOGGER.info("a_ filtered system prefix",systemAttrMap);
							MfResponseDetail selectedResponse = mfResponseDetailRepository
									.findBySiteIdAndTypeAndIsSelected(mfDetailsBean.getSiteId(), "B-End", 1);
						   
							Map<String, Object> selectedMap = filterSystemResponseBasedOnPrefix(null,selectedResponse.getCreateResponseJson());
							selectedMap.putAll(systemAttrMap);
							JSONObject json = new JSONObject();
						    json.putAll(selectedMap);
							LOGGER.info("Combined link json when A is system feasible",json);
						 //   mfDetailList.setMfLinkResponseJson(json.toString());
							mfDetailList.forEach( x -> x.setMfLinkResponseJson(json.toString()));
						}
					}
				} else {
					// P2P case
					LOGGER.info("Inside  P2p case .The siteId {} has linkType {}", k, mfDetailsBean.getMfDetails().getMfLinkEndType());

					MfResponseDetail selectedP2PResponse = mfResponseDetailRepository
							.findBySiteIdAndTypeAndIsSelected(mfDetailsBean.getSiteId(), "P2P", 1);
					if(Objects.isNull(selectedP2PResponse)) {
						// if p2p selection is not there check for AEnd and BEnd selections
						MfResponseDetail selectedAEndResp = mfResponseDetailRepository
								.findBySiteIdAndTypeAndIsSelected(mfDetailsBean.getSiteId(), "A-End", 1);
						MfResponseDetail selectedBEndResp = mfResponseDetailRepository
								.findBySiteIdAndTypeAndIsSelected(mfDetailsBean.getSiteId(), "B-End", 1);
						
						// case 1 : Aend selected response and Bend selected response both are available
						if(selectedAEndResp!=null && selectedBEndResp!=null) {
							LOGGER.info("Inside  P2p case. Aend selected response and Bend selected response both are available.");

							   Map<String, Object> selectedMapA = filterSystemResponseBasedOnPrefix(null,selectedAEndResp.getCreateResponseJson());
							   Map<String, Object> selectedMapB = filterSystemResponseBasedOnPrefix(null,selectedBEndResp.getCreateResponseJson());

							   selectedMapA.putAll(selectedMapB);
								JSONObject json = new JSONObject();
							    json.putAll(selectedMapA);
							   // mfDetailList.setMfLinkResponseJson(json.toString());
								mfDetailList.forEach( x -> x.setMfLinkResponseJson(json.toString()));

						}
						
						// case 2 : Aend selected response and Bend no slection available
						if(selectedAEndResp!=null && selectedBEndResp==null) {
							LOGGER.info("Inside  Inta non P2p case. Aend selected response available and Bend no selected response available.");

							   Map<String, Object> selectedMapA = filterSystemResponseBasedOnPrefix(null,selectedAEndResp.getCreateResponseJson());
							   Map<String, Object> systemAttrMap =  filterSystemResponseBasedOnPrefix("b_",mfDetailOpt.getSystemLinkResponseJson());
								
								if(systemAttrMap!=null && !systemAttrMap.isEmpty()) {
									Object bEndFeasibilityStat = systemAttrMap.get("b_Predicted_Access_Feasibility");
								
									String bSiteFeasibility = String.valueOf(bEndFeasibilityStat);
									if(bSiteFeasibility.equalsIgnoreCase("Feasible")) {
										LOGGER.info("Site A Task available and siteB is system feasible");
										LOGGER.info("b_ filtered system prefix",systemAttrMap);
										selectedMapA.putAll(systemAttrMap);
									}
								}
								
								JSONObject json = new JSONObject();
							    json.putAll(selectedMapA);
								LOGGER.info("Combined link json when B is system feasible",json);
								mfDetailList.forEach( x -> x.setMfLinkResponseJson(json.toString()));
						}
						
						// case 3  Bend selected response and Aend no slection available
						if(selectedAEndResp ==null && selectedBEndResp!=null) {
							LOGGER.info("Inside  Inta non P2p case. Aend selected response not available and Bend selected response available.");

							   Map<String, Object> selectedMapB = filterSystemResponseBasedOnPrefix(null,selectedAEndResp.getCreateResponseJson());
							   Map<String, Object> systemAttrMap =  filterSystemResponseBasedOnPrefix("a_",mfDetailOpt.getSystemLinkResponseJson());
								
								if(systemAttrMap!=null && !systemAttrMap.isEmpty()) {
									Object aEndFeasibilityStat = systemAttrMap.get("a_Predicted_Access_Feasibility");
								
									String aSiteFeasibility = String.valueOf(aEndFeasibilityStat);
									if(aSiteFeasibility.equalsIgnoreCase("Feasible")) {
										LOGGER.info("Site B Task available and siteA is system feasible");
										LOGGER.info("a_ filtered system prefix",systemAttrMap);
										selectedMapB.putAll(systemAttrMap);
									}
								}
								
								JSONObject json = new JSONObject();
							    json.putAll(selectedMapB);
								LOGGER.info("Combined link json when B is system feasible",json);
								mfDetailList.forEach( x -> x.setMfLinkResponseJson(json.toString()));
						}
					}else {
					mfDetailList.forEach( x -> x.setMfLinkResponseJson(selectedP2PResponse.getCreateResponseJson()));
					}
				}
				
			} // feasibility check end
				
		});
	}

	
	private void checkForLinksInMFTasks(Integer quoteId,Map<Integer, String> allSiteStatusesOfQuote) {
		LOGGER.info("Inside NPLTaskService#checkForLinksInMFTasks method");

		List<MfDetail> listOfMfDetails = mfDetailRepository.findByQuoteId(quoteId);
		Set<Integer> linkIdSet=listOfMfDetails.stream().map(MfDetail:: getLinkId).collect(Collectors.toSet());
		
		linkIdSet.stream().forEach( x -> {
			List<MfDetail> mfDetailsList=mfDetailRepository.findByLinkIdAndStatus(x,"CLOSED");
			if(mfDetailsList!=null && mfDetailsList.size() ==2) {
				LOGGER.info("Both AEnd and Bend is not system feasible and has MF tasks");
			String	firstSiteFeasiibilityStat = allSiteStatusesOfQuote.get(mfDetailsList.get(0).getSiteId());
			String	secondSiteFeasiibilityStat = allSiteStatusesOfQuote.get(mfDetailsList.get(1).getSiteId());
		 Optional<MfDetail> mfDetailofA = mfDetailsList.stream().filter( z -> z.getSiteType().equals("SiteA")).findFirst();
		 Optional<MfDetail> mfDetailofB = mfDetailsList.stream().filter( z -> z.getSiteType().equals("SiteB")).findFirst();

			if(firstSiteFeasiibilityStat.equalsIgnoreCase("Feasible") && secondSiteFeasiibilityStat.equalsIgnoreCase("Feasible")) {
				Map<String, Object> selectedMapA= new HashMap<String,Object>();
				Map<String, Object> selectedMapB= new HashMap<String,Object>();

				if(mfDetailofA.isPresent()) {
				MfResponseDetail selectedResponseA = mfResponseDetailRepository
						.findBySiteIdAndTypeAndIsSelected(mfDetailofA.get().getSiteId(), "A-End", 1);
			   selectedMapA = filterSystemResponseBasedOnPrefix(null,selectedResponseA.getCreateResponseJson());
				LOGGER.info("Site A selected Json",selectedMapA );

				}
				
				if(mfDetailofB.isPresent()) {
					MfResponseDetail selectedResponseB= mfResponseDetailRepository
							.findBySiteIdAndTypeAndIsSelected(mfDetailofB.get().getSiteId(), "B-End", 1);
					 selectedMapB = filterSystemResponseBasedOnPrefix(null,selectedResponseB.getCreateResponseJson());
						LOGGER.info("Site B selected Json",selectedMapB );

				}
				selectedMapA.putAll(selectedMapB);
				JSONObject json = new JSONObject();
			    json.putAll(selectedMapA);
				LOGGER.info("Combned Json of both ends ",json );

			    // For all feasible links linkResponse Json attribute will be available
			    mfDetailofA.get().setMfLinkResponseJson(json.toJSONString());
			    mfDetailofB.get().setMfLinkResponseJson(json.toJSONString());
			}

			}else {
				LOGGER.info("MFDetail entry for this LinkID {} is null or more than 2" , x);
			}
		});
	
	}

	private void saveMfResponseDetailsInLinkResponseTable(Integer quoteId, Map<Integer, String> siteStatusesMap,
			Integer id, String productName, MfDetail mfDetail) {
		LOGGER.info("Inside NPLTaskService#saveMfResponseDetailsInLinkResponseTable method");

		HashMap<Integer, String> linksToSitesMap = new HashMap<Integer, String>();

		constructLinkToSiteMap(quoteId, linksToSitesMap);
		
		LOGGER.info( " Link to site map {}", linksToSitesMap);
		ArrayList<MfLinkDetailsBean> linkBeanList = new ArrayList<MfLinkDetailsBean>();

		linksToSitesMap.forEach((linkId, siteIds) -> {
			String[] splitArr = siteIds.split(",");
            LOGGER.info("siteID split ::{} ", Arrays.toString(splitArr));
			MfLinkDetailsBean linkBean = new MfLinkDetailsBean();
			linkBeanList.add(linkBean);
			if(ArrayUtils.isNotEmpty(splitArr)) {
				try {
				List<String> uniqueList=Arrays.asList(splitArr).stream().distinct().collect(Collectors.toList());
				splitArr = 	uniqueList.stream().toArray(String[] :: new);
	            LOGGER.info("siteID splitArray after removing duplicates ::{} ", Arrays.toString(splitArr));
				}catch(Exception e) {
					LOGGER.error("Error in processing splitArr# constructLinkToSiteMap",e);
					e.printStackTrace();
				}
			}
				Integer siteId1 = splitArr[0].length() > 0? Integer.valueOf(splitArr[0]) : null;
				Integer siteId2 = splitArr.length >=2 ? Integer.valueOf(splitArr[1]) : null;

				linkBean.setQuoteId(quoteId);
				linkBean.setLinkId(linkId);
				
				MfDetail mfDetailEnitity = null;
			if (siteId1 != null) {
				// site 1
				List<MfDetail> mfDetailEnitityList = mfDetailRepository.findBySiteIdAndStatus(siteId1,"CLOSED");
				if (mfDetailEnitityList != null && !mfDetailEnitityList.isEmpty()) {
					LOGGER.info("Iterating link to site Map for site ID 1 and mfDetailEntityList is not empty for {}",
							siteId1);

					// For macd scenarios we ll be having more than one tasks assigned to a site.
					// but mfDetail will be having same details. it is enough to take one entry from
					// the list and process it
					mfDetailEnitity = mfDetailEnitityList.get(0);
				}
			}

				if (mfDetailEnitity != null) {
					MfDetailsBean mfDetailsBean = getMfDetailsBean(mfDetailEnitity);
					linkBean.setMfLinkType(mfDetailsBean.getMfDetails().getMfLinkType());
		            LOGGER.info("setting mfDetailEnitity linkBean's mfLinkType as {}", linkBean.getMfLinkType());
					if (mfDetailEnitity.getSiteType().equals("SiteA")) {
			            LOGGER.info("Filling siteA Details in the bean");
						linkBean.setSiteADetail(fillSiteADetails(siteStatusesMap, mfDetailsBean, mfDetailEnitity.getSiteId()));
					} else if (mfDetailEnitity.getSiteType().equals("SiteB")) {
			            LOGGER.info("Filling siteB Details in the bean");
						linkBean.setSiteBDetail(fillSiteBDetails(siteStatusesMap, mfDetailsBean, mfDetailEnitity.getSiteId()));
					}
					
					getIntraResponse(siteStatusesMap, linkBean, mfDetailEnitity, mfDetailsBean);
				}
				// site 2
				MfDetail mfDetailEntity2 = null;
				if(siteId2!=null) {
				List<MfDetail> mfDetailEntity2List = mfDetailRepository.findBySiteIdAndStatus(Integer.valueOf(siteId2),"CLOSED");
				if(mfDetailEntity2List!=null && !mfDetailEntity2List.isEmpty()) {
					mfDetailEntity2= mfDetailEntity2List.get(0);
		            LOGGER.info("Iterating link to site Map for site ID2 and mfDetailEntityList is not empty for {}", siteId2);
				}
				}
				
				if (mfDetailEntity2 != null) {
		            LOGGER.info("mfDetailEntity2", mfDetailEntity2);
					MfDetailsBean mfDetailsBean2 = getMfDetailsBean(mfDetailEntity2);
					linkBean.setMfLinkType(mfDetailsBean2.getMfDetails().getMfLinkType());
		            LOGGER.info("LinkBean for mfDetailEntity2 is set with mfLinkType {}", linkBean.getMfLinkType());
					if (mfDetailEntity2.getSiteType().equals("SiteA")) {
			            LOGGER.info("Filling siteA Details in the bean for mfDetailEntity2");
						linkBean.setSiteADetail(fillSiteADetails(siteStatusesMap, mfDetailsBean2, mfDetailEntity2.getSiteId()));
					} else if (mfDetailEntity2.getSiteType().equals("SiteB")) {
			            LOGGER.info("Filling siteB Details in the bean for mfDetailEntity2");
						linkBean.setSiteBDetail(fillSiteBDetails(siteStatusesMap, mfDetailsBean2, mfDetailEntity2.getSiteId()));
					}
					getIntraResponse(siteStatusesMap, linkBean, mfDetailEntity2, mfDetailsBean2);

				}
			
		});

		LOGGER.info("==============LINKBEAN LIST ================={}",linkBeanList);
		
		// Now in link Details Bean , we have all the site Related Details
		if(!CollectionUtils.isEmpty(linkBeanList)) {
			LOGGER.info("==============LINKBEAN LIST SIZE================={}",linkBeanList.size());

		linkBeanList.stream().forEach(link -> {
			LOGGER.info("======current link in loop details {},{},{},{}",
					link.getLinkId(),link.getMfLinkType(),link.getOverallLinkFeasibilityStatus(),link.getLinkResponseJson());

			if (link.getMfLinkType().equalsIgnoreCase("Intercity")) {
				LOGGER.info("saveMfResponseDetailsInLinkResponseTable#Inside intercity feasible /non feasible decision");

				MfDetail mfdetailOptA = mfDetailRepository.findByLinkIdAndSiteTypeAndStatus(link.getLinkId(), "SiteA","CLOSED");
				if (link.getSiteADetail() != null && link.getSiteBDetail() != null) {
					// both site A and site B are available - inter both manual case
					LOGGER.info( " Inside intercity siteADetail and siteBDetail available case");

					link.setOverallLinkFeasibilityStatus(
							(link.getSiteADetail().getSiteAFeasibilityStatus().startsWith("Feasible")
									&& link.getSiteBDetail().getSiteBFeasibilityStatus().startsWith("Feasible"))
											? ManualFeasibilityWFConstants.FEASIBLE
											: ManualFeasibilityWFConstants.NOT_FEASIBLE);
					link.setLinkResponseJson(mfdetailOptA.getMfLinkResponseJson());
					LOGGER.info("intercty link response json creation {}",link.getLinkResponseJson());

				}

				else if (link.getSiteADetail() != null && link.getSiteBDetail() == null) {
					// site A has task in MF
					// check site B is system Feasible
					LOGGER.info( "saveMfResponseDetailsInLinkResponseTable#Inside intercity siteADetail available and siteBDetail not available case");

					MfDetailsBean mfDetailsBean = getMfDetailsBean(mfdetailOptA);
					if (mfDetailsBean != null && mfDetailsBean.getMfDetails() != null
							&& mfDetailsBean.getMfDetails().getbEndPredictedAcessFeasibility() != null) {
						link.setOverallLinkFeasibilityStatus((mfDetailsBean.getMfDetails()
								.getbEndPredictedAcessFeasibility().startsWith("Feasible")
								&& link.getSiteADetail().getSiteAFeasibilityStatus().startsWith("Feasible"))
										? ManualFeasibilityWFConstants.FEASIBLE
										: ManualFeasibilityWFConstants.NOT_FEASIBLE);

					}
					// construct default provider for system feasible sites
					MfSiteBDetailsBean siteB = new MfSiteBDetailsBean();
					link.setSiteBDetail(siteB);
					siteB.setFeasibilityModeSiteB("OnnetWL_NPL");
					siteB.setProviderForSiteB("TATA COMMUNICATIONS");
					siteB.setFeasibilityTypeSiteB("MAN");
					link.setLinkResponseJson(mfdetailOptA.getMfLinkResponseJson());
					LOGGER.info( "saveMfResponseDetailsInLinkResponseTable#Inside intercity siteADetail available and siteBDetail not available case linkResponse Json {}",link.getLinkResponseJson());


				}
				
				else if(link.getSiteADetail() == null && link.getSiteBDetail() != null) {
					LOGGER.info( "saveMfResponseDetailsInLinkResponseTable#Inside intercity siteBDetail available and siteADetail not available case");

					// site A no mF task and site B Mf task Available
					MfDetail mfdetailOptB = mfDetailRepository.findByLinkIdAndSiteTypeAndStatus(link.getLinkId(), "SiteB","CLOSED");

					MfDetailsBean mfDetailsBean = getMfDetailsBean(mfdetailOptB);
					if (mfDetailsBean != null && mfDetailsBean.getMfDetails() != null
							&& mfDetailsBean.getMfDetails().getaEndPredictedAcessFeasibility() != null) {
						link.setOverallLinkFeasibilityStatus((mfDetailsBean.getMfDetails()
								.getaEndPredictedAcessFeasibility().startsWith("Feasible")
								&& link.getSiteBDetail().getSiteBFeasibilityStatus().startsWith("Feasible"))
								? ManualFeasibilityWFConstants.FEASIBLE
										: ManualFeasibilityWFConstants.NOT_FEASIBLE);
					}
					
					// construct default provider for system feasible sites
					MfSiteADetailsBean siteA = new MfSiteADetailsBean();
					link.setSiteADetail(siteA);
					siteA.setFeasibilityModeSiteA("OnnetWL_NPL");
					siteA.setProviderForSiteA("TATA COMMUNICATIONS");
					siteA.setFeasibilityTypeSiteA("MAN");
					
					link.setLinkResponseJson(mfdetailOptB.getMfLinkResponseJson());
					LOGGER.info( "saveMfResponseDetailsInLinkResponseTable#Inside intercity siteADetail available and siteBDetail not available case linkResponse Json {}",link.getLinkResponseJson());

				}
			}
			
			if (link.getMfLinkType().equalsIgnoreCase("Intracity")) {
				LOGGER.info("saveMfResponseDetailsInLinkResponseTable#Inside intra both ends mapped. linkResponse Json");

				if (linksToSitesMap.containsKey(link.getLinkId())) {
					MfDetail mfDetailEntity = null;
					
					if (link.getSiteADetail() != null && link.getSiteBDetail() != null) {
					
						String siteid = linksToSitesMap.get(link.getLinkId());
						List<MfDetail> mfDetailList = mfDetailRepository.findBySiteId(Integer.valueOf(siteid));
						if(!CollectionUtils.isEmpty(mfDetailList)) {
							mfDetailEntity = mfDetailList.get(0);
						}
						link.setOverallLinkFeasibilityStatus(
								mfDetailEntity.getMfLinkResponseJson() != null
								? ManualFeasibilityWFConstants.FEASIBLE
										: ManualFeasibilityWFConstants.NOT_FEASIBLE);
						link.setLinkResponseJson(mfDetailEntity.getMfLinkResponseJson());
						LOGGER.info("saveMfResponseDetailsInLinkResponseTable#Inside intra both ends mapped. linkResponse Json {}",link.getLinkResponseJson());
					}
					
				

					if (link.getSiteADetail() != null && link.getSiteBDetail() == null) {
						LOGGER.info("saveMfResponseDetailsInLinkResponseTable#Inside intra Aend not null and B ENd null case.");

						mfDetailEntity = mfDetailRepository.findByLinkIdAndSiteTypeAndStatus(link.getLinkId(), "SiteA","CLOSED");
						MfDetailsBean mfDetailsBean = getMfDetailsBean(mfDetailEntity);

						link.setOverallLinkFeasibilityStatus(
								(mfDetailsBean.getMfDetails().getbEndPredictedAcessFeasibility().startsWith("Feasible")
										&& link.getSiteADetail().getSiteAFeasibilityStatus().startsWith("Feasible"))
												? ManualFeasibilityWFConstants.FEASIBLE
												: ManualFeasibilityWFConstants.NOT_FEASIBLE);
						
						MfSiteBDetailsBean siteB = new MfSiteBDetailsBean();
						link.setSiteBDetail(siteB);
						siteB.setFeasibilityModeSiteB("OnnetWL_NPL");
						siteB.setProviderForSiteB("TATA COMMUNICATIONS");
						siteB.setFeasibilityTypeSiteB("MAN");

						link.setLinkResponseJson(mfDetailEntity.getMfLinkResponseJson());
						LOGGER.info("saveMfResponseDetailsInLinkResponseTable#Inside intra Aend not null and B ENd null case. linkJson {}",link.getLinkResponseJson());
					}

					if (link.getSiteADetail() == null && link.getSiteBDetail() != null) {
						LOGGER.info("saveMfResponseDetailsInLinkResponseTable#Inside intra Bend not null and A ENd null case.");

						mfDetailEntity = mfDetailRepository.findByLinkIdAndSiteTypeAndStatus(link.getLinkId(), "SiteB","CLOSED");
						MfDetailsBean mfDetailsBean = getMfDetailsBean(mfDetailEntity);

						link.setOverallLinkFeasibilityStatus(
								(mfDetailsBean.getMfDetails().getaEndPredictedAcessFeasibility().startsWith("Feasible")
										&& link.getSiteBDetail().getSiteBFeasibilityStatus().startsWith("Feasible"))
												? ManualFeasibilityWFConstants.FEASIBLE
												: ManualFeasibilityWFConstants.NOT_FEASIBLE);
						MfSiteADetailsBean siteA = new MfSiteADetailsBean();
						link.setSiteADetail(siteA);
						siteA.setFeasibilityModeSiteA("OnnetWL_NPL");
						siteA.setProviderForSiteA("TATA COMMUNICATIONS");
						siteA.setFeasibilityTypeSiteA("MAN");
						link.setLinkResponseJson(mfDetailEntity.getMfLinkResponseJson());
						LOGGER.info("saveMfResponseDetailsInLinkResponseTable#Inside intra Bend not null and A ENd null case. linkJson {}",link.getLinkResponseJson());
					}
				}
			}
			link.setIsSelected(link.getOverallLinkFeasibilityStatus().startsWith("Feasible") ? 1 :0);
		});
	}
		
		populateNPLResponse(linkBeanList, mfDetail);
	}

	private void constructLinkToSiteMap(Integer quoteId, HashMap<Integer, String> linksToSitesMap) {
		LOGGER.info("====Construct Link TO Site Map======= ");
		List<MfDetail> listOfMfDetails = mfDetailRepository.findByQuoteIdAndStatus(quoteId,"CLOSED");
		// Now we have linkIds and its corresponding Sites
		listOfMfDetails.stream().forEach(x -> {
			if (linksToSitesMap.get(x.getLinkId()) == null) {
				linksToSitesMap.put(x.getLinkId(), String.valueOf(x.getSiteId()));
			} else {
				String value = linksToSitesMap.get(x.getLinkId());
				String newVal = value + "," + String.valueOf(x.getSiteId());
				linksToSitesMap.put(x.getLinkId(), newVal);
			}
		});
	}

      private void getIntraResponse(Map<Integer, String> siteStatusesMap, MfLinkDetailsBean linkBean,
			MfDetail mfDetailEnitity, MfDetailsBean mfDetailsBean) {
		if (mfDetailsBean.getMfDetails().getMfLinkType().equalsIgnoreCase("Intracity")) {
			MfResponseDetail intraP2pResp = mfResponseDetailRepository
					.findBySiteIdAndTypeAndIsSelected(mfDetailEnitity.getSiteId(), "P2P", 1);

			MfResponseDetail intraAEndResp = mfResponseDetailRepository
					.findBySiteIdAndTypeAndIsSelected(mfDetailEnitity.getSiteId(), "A-End", 1);

			MfResponseDetail intraBEndResp = mfResponseDetailRepository
					.findBySiteIdAndTypeAndIsSelected(mfDetailEnitity.getSiteId(), "B-End", 1);
			if (intraP2pResp != null) {
				// if p2p response is avaialble its a combination of
				// both A and B.
				// site A and site B attributes will be same
				linkBean.setSiteADetail(
						fillIntraADetails(siteStatusesMap, mfDetailsBean, mfDetailEnitity.getSiteId(), intraP2pResp));
				linkBean.setSiteBDetail(
						fillIntraBDetails(siteStatusesMap, mfDetailsBean, mfDetailEnitity.getSiteId(), intraP2pResp));

			} else if (intraAEndResp != null) {
				linkBean.setSiteADetail(
						fillIntraADetails(siteStatusesMap, mfDetailsBean, mfDetailEnitity.getSiteId(), intraAEndResp));

			}
			if (intraBEndResp != null) {
				linkBean.setSiteBDetail(
						fillIntraBDetails(siteStatusesMap, mfDetailsBean, mfDetailEnitity.getSiteId(), intraAEndResp));

			}

		}
	}	
			
			

	private MfSiteADetailsBean fillSiteADetails(Map<Integer, String> siteStatusesMap, MfDetailsBean mfDetailsBean,
			Integer siteId) {
		MfSiteADetailsBean siteADetail = new MfSiteADetailsBean();
		MfDetailAttributes mfDetailsAttr = mfDetailsBean.getMfDetails();
		siteADetail.setSiteAId(siteId);
		siteADetail.setSiteAEndType(mfDetailsAttr.getMfLinkEndType());
		siteADetail.setSiteAFeasibilityStatus(siteStatusesMap.get(siteId));

		if (!mfDetailsAttr.getMfLinkEndType().equalsIgnoreCase("P2P")
				&& !mfDetailsAttr.getMfLinkType().equalsIgnoreCase("Intracity")) {
            LOGGER.info("Filling siteA Details for non P2P and non Intracity");
			MfResponseDetail selectedResponseA = mfResponseDetailRepository.findBySiteIdAndTypeAndIsSelected(siteId,
					mfDetailsAttr.getMfLinkEndType(), 1);

			if (selectedResponseA != null) {

				siteADetail.setFeasibilityModeSiteA(selectedResponseA.getFeasibilityMode());
				siteADetail.setFeasibilityTypeSiteA(selectedResponseA.getFeasibilityType());
				siteADetail.setProviderForSiteA(selectedResponseA.getProvider());
				LOGGER.info("selected response is available with mode {}, type {}, provider {}",siteADetail.getFeasibilityModeSiteA(),
						siteADetail.getFeasibilityTypeSiteA(),siteADetail.getProviderForSiteA());
			}

		}
		return siteADetail;
	}
	
	private MfSiteADetailsBean fillIntraADetails(Map<Integer, String> siteStatusesMap, MfDetailsBean mfDetailsBean,
			Integer siteId, MfResponseDetail selectedResponseA) {
		LOGGER.info( "Inside NplTaskService#fillIntraADetails method ");

		MfSiteADetailsBean siteASetail = new MfSiteADetailsBean();
		MfDetailAttributes mfDetailsAttr = mfDetailsBean.getMfDetails();
		siteASetail.setSiteAEndType(mfDetailsAttr.getMfLinkEndType());
		siteASetail.setSiteAFeasibilityStatus(siteStatusesMap.get(siteId));
		siteASetail.setSiteAId(siteId);

			if (selectedResponseA != null) {
				siteASetail.setFeasibilityModeSiteA(selectedResponseA.getFeasibilityMode());
				siteASetail.setFeasibilityTypeSiteA(selectedResponseA.getFeasibilityType());
				siteASetail.setProviderForSiteA(selectedResponseA.getProvider());
			}

			return siteASetail;
		}
		
	private MfSiteBDetailsBean fillIntraBDetails(Map<Integer, String> siteStatusesMap, MfDetailsBean mfDetailsBean,
			Integer siteId, MfResponseDetail selectedResponseA) {
		LOGGER.info( "Inside NplTaskService#fillIntraBDetails method ");

		MfSiteBDetailsBean siteBSetail = new MfSiteBDetailsBean();
		MfDetailAttributes mfDetailsAttr = mfDetailsBean.getMfDetails();
		siteBSetail.setSiteBEndType(mfDetailsAttr.getMfLinkEndType());
		siteBSetail.setSiteBFeasibilityStatus(siteStatusesMap.get(siteId));
		siteBSetail.setSiteIdB(siteId);
			if (selectedResponseA != null) {
				siteBSetail.setFeasibilityModeSiteB(selectedResponseA.getFeasibilityMode());
				siteBSetail.setFeasibilityTypeSiteB(selectedResponseA.getFeasibilityType());
				siteBSetail.setProviderForSiteB(selectedResponseA.getProvider());
			}

			return siteBSetail;
		}
		
	
	
	private MfSiteBDetailsBean fillSiteBDetails(Map<Integer, String> siteStatusesMap, MfDetailsBean mfDetailsBean, Integer siteId) {

		MfSiteBDetailsBean siteBDetail = new MfSiteBDetailsBean();
		MfDetailAttributes mfDetailsAttr = mfDetailsBean.getMfDetails();
		siteBDetail.setSiteBEndType(mfDetailsAttr.getMfLinkEndType());
		siteBDetail.setSiteBFeasibilityStatus(siteStatusesMap.get(siteId));
		siteBDetail.setSiteIdB(siteId);

		MfResponseDetail selectedResponseB = null;
		if( !mfDetailsAttr.getMfLinkEndType().equalsIgnoreCase("P2P") && !mfDetailsAttr.getMfLinkType().equalsIgnoreCase("Intracity")) {
			LOGGER.info( "Inside NplTaskService#fillSiteBDetails method non INtra - non P2P scenario");
		 selectedResponseB = mfResponseDetailRepository
				.findBySiteIdAndTypeAndIsSelected(siteId, mfDetailsAttr.getMfLinkEndType(), 1);
		if (selectedResponseB != null) {
			siteBDetail.setFeasibilityModeSiteB(selectedResponseB.getFeasibilityMode());
			siteBDetail.setFeasibilityTypeSiteB(selectedResponseB.getFeasibilityType());
			siteBDetail.setProviderForSiteB(selectedResponseB.getProvider());
			LOGGER.info("selected response site B is available with mode {}, type {}, provider {}",siteBDetail.getFeasibilityModeSiteB(),
					siteBDetail.getFeasibilityTypeSiteB(),siteBDetail.getProviderForSiteB());
		}
		}
		return siteBDetail;
	}
		
	

	private void populateNPLResponse(ArrayList<MfLinkDetailsBean> linkBeanList, MfDetail mfDetail) {
		LOGGER.info( "Inside NplTaskService#populateNPLResponse method ");

		List<String> listOfPricingAttrs = Arrays.asList("a_lm_nrc_ospcapex_onwl", "a_lm_nrc_nerental_onwl", "a_lm_nrc_bw_onwl",
				"a_lm_nrc_mux_onwl", "a_lm_arc_bw_onwl", "a_local_loop_bw",

				"b_lm_nrc_ospcapex_onwl", "b_lm_nrc_nerental_onwl", "b_lm_nrc_bw_onwl",
				"b_lm_nrc_mux_onwl", "b_lm_arc_bw_onwl", "b_local_loop_bw",

				// radwin and UBR
				"a_lm_nrc_bw_onrf", "a_lm_arc_bw_backhaul_onrf", "a_lm_nrc_mast_onrf",
				"b_lm_nrc_bw_onrf", "b_lm_arc_bw_backhaul_onrf", "b_lm_nrc_mast_onrf",

				// offnet wireline
				"a_lm_arc_modem_charges_offwl", "a_lm_arc_bw_offwl",
				"a_lm_otc_nrc_installation_offwl",

				"b_lm_arc_modem_charges_offwl", "b_lm_arc_bw_offwl",
				"b_lm_otc_nrc_installation_offwl",

				// offnet wireless
				"a_lm_arc_bw_prov_ofrf", "a_lm_nrc_mast_ofrf", "a_lm_nrc_bw_prov_ofrf",
				"b_lm_arc_bw_prov_ofrf", "b_lm_nrc_mast_ofrf", "b_lm_nrc_bw_prov_ofrf"

		);
		linkBeanList.stream().forEach( x -> {
		
			MfNplResponseDetail nplResponse = new MfNplResponseDetail();
			nplResponse.setLinkId(x.getLinkId());
				
			JSONObject dataEnvelopeObj = null;
			JSONParser jsonParser = new JSONParser();
			
				try {
					if (x.getLinkResponseJson() != null) {
						dataEnvelopeObj = (JSONObject) jsonParser.parse(x.getLinkResponseJson());
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			for (String pricingAttr : listOfPricingAttrs) {

				if (dataEnvelopeObj != null && dataEnvelopeObj.get(pricingAttr) != null) {

					String convertedVal = "";
					Object charge = dataEnvelopeObj.get(pricingAttr);
					if (charge != null) {
						convertedVal = String.valueOf(charge);
						dataEnvelopeObj.put(pricingAttr, convertedVal);
					}
				}
			}
			
			// patch
			if(dataEnvelopeObj.get("a_local_loop_bw")!=null) {
				LOGGER.info( "Inside attribute mapping from a_local_loop_bw to a_bandwidth");
				dataEnvelopeObj.put("a_local_loop_bw", dataEnvelopeObj.get("a_local_loop_bw")+ " "+ "Mbps");
				dataEnvelopeObj.put("a_bandwidth", dataEnvelopeObj.get("a_local_loop_bw"));
			}
			
			if(dataEnvelopeObj.get("b_local_loop_bw")!=null) {
				LOGGER.info( "Inside attribute mapping from b_local_loop_bw to b_bandwidth");
				dataEnvelopeObj.put("b_local_loop_bw", dataEnvelopeObj.get("b_local_loop_bw")+ " "+ "Mbps");
				dataEnvelopeObj.put("b_bandwidth", dataEnvelopeObj.get("b_local_loop_bw"));
			}
			
			nplResponse.setCreateResponseJson(dataEnvelopeObj.toJSONString());
			nplResponse.setFeasibilityStatus(x.getOverallLinkFeasibilityStatus());
			nplResponse.setFeasibilityCheck("Manual");
			nplResponse.setType(x.getMfLinkType());
			nplResponse.setQuoteId(x.getQuoteId());
			nplResponse.setIsSelected(x.getIsSelected());
			if (x.getSiteADetail() != null) {
				MfSiteADetailsBean siteA = x.getSiteADetail();
				nplResponse.setProvider(siteA.getProviderForSiteA());
				nplResponse.setFeasibilityMode(siteA.getFeasibilityModeSiteA());
				nplResponse.setFeasibilityType(siteA.getFeasibilityTypeSiteA());
			}
			if (x.getSiteBDetail() != null) {
				MfSiteBDetailsBean siteB = x.getSiteBDetail();
				nplResponse.setProviderB(siteB.getProviderForSiteB());
				nplResponse.setFeasibilityModeB(siteB.getFeasibilityModeSiteB());
				nplResponse.setFeasibilityTypeB(siteB.getFeasibilityTypeSiteB());
			}
			
			nplResponse.setCreatedTime(new Timestamp(new Date().getTime()));
			nplResponse.setMfRank(0);
			mfNplResponseDetailRepository.save(nplResponse);
			LOGGER.info("Link response for linkId {} is {}",x.getLinkId(),nplResponse);
			
		});

		try {
			LOGGER.info("setting latest response as selected in mfNplLinkResponse");
			MfDetailAttributes mfDetailsAttr = Utils.convertJsonToObject(mfDetail.getMfDetails(), MfDetailAttributes.class);
			if (mfDetailsAttr.isRetriggerTaskForFeasibleSites()) {
				List<MfNplResponseDetail> mfNplResponseDetails = mfNplResponseDetailRepository.findByQuoteId(mfDetail.getQuoteId());
				mfNplResponseDetails.forEach(mfNplResponse -> {
					mfNplResponse.setIsSelected(0);
					mfNplResponseDetailRepository.save(mfNplResponse);
				});
				MfNplResponseDetail selectedMfResponseDetail = mfNplResponseDetails.stream()
						.sorted(Comparator.comparing(MfNplResponseDetail::getCreatedTime).reversed())
						.findFirst().orElse(null);
				if (Objects.nonNull(selectedMfResponseDetail)) {
					selectedMfResponseDetail.setIsSelected(1);
					mfNplResponseDetailRepository.save(selectedMfResponseDetail);
					LOGGER.info("latest response selected");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while selecting latest response  {}", e.getMessage());
		}

	}

	private MfResponseDetail segregateResponse(Integer siteId, List<MfResponseDetail> mfResponsDetails) {
		LOGGER.info( "Inside NplTaskService#segregateResponse method ");

		if (!CollectionUtils.isEmpty(mfResponsDetails)) {
			MfResponseDetail selectedMfResponseDetail = taskService.filterResponsesBasedOnStatusAndRankAndFeasibleMode(
					mfResponsDetails);
			return selectedMfResponseDetail;
		} else {
			LOGGER.info("No MF response found for site {} ", siteId);
			return null;
		}
	}
	
	/**
	 * @param linkId
	 * This function is used to fetch response for given linkid
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public List<MfResponseDetail> fetchResponse(String linkId) throws TclCommonException {
		Map<String, String> mfLinkDetailsQueueResponse = (Map<String, String>) mqUtils.sendAndReceive(fetchMFResponseInOmsMQ,linkId);
		String siteAId=mfLinkDetailsQueueResponse.get("siteAId");
		String siteBId=mfLinkDetailsQueueResponse.get("siteBId");
		LOGGER.info("sites details from oms::{}"+siteAId+" "+siteBId);
		validateSiteInformation(siteAId, siteBId);
		List<MfResponseDetail> combinedList=new ArrayList<MfResponseDetail>();
		LOGGER.info("Fetch response from DB");
		List<MfResponseDetail> recordInDBforSiteAId = mfResponseDetailRepository.findBySiteId(Integer.parseInt(siteAId));
		List<MfResponseDetail> recordInDBforSiteBId = mfResponseDetailRepository.findBySiteId(Integer.parseInt(siteBId));
		
		if(!recordInDBforSiteAId.isEmpty() || !recordInDBforSiteBId.isEmpty()) {
		LOGGER.info("Process Mf response");
		combinedList.addAll(processMfResponseDetailList(recordInDBforSiteAId));
		combinedList.addAll(processMfResponseDetailList(recordInDBforSiteBId));
		}
		return combinedList;
	}	
	protected void validateSiteInformation(String siteAId, String siteBId) throws TclCommonException {
		if ((siteAId.isEmpty()) && (siteBId.isEmpty())) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_NOT_FOUND);
		}
	}
	
	private List<MfResponseDetail> processMfResponseDetailList(List<MfResponseDetail> recordInDB) {
		recordInDB = recordInDB.stream().filter(p -> p.getCreateResponseJson() != null).map(x -> {
			FetchResponseBean frBean = new FetchResponseBean();

			frBean.setId(x.getId());
			frBean.setTaskId(x.getTaskId());
			frBean.setSiteId(x.getSiteId());
			frBean.setProvider(x.getProvider());
			frBean.setCreateResponseJson(x.getCreateResponseJson());
			frBean.setCreatedBy(x.getCreatedBy());
			frBean.setCreatedTime(x.getCreatedTime());
			frBean.setUpdatedBy(x.getUpdatedBy());
			frBean.setUpdatedTime(x.getUpdatedTime());
			frBean.setType(x.getType());
			frBean.setFeasibilityMode(x.getFeasibilityMode());
			frBean.setMfRank(x.getMfRank());
			frBean.setIsSelected(x.getIsSelected());
			frBean.setFeasibilityStatus(x.getFeasibilityStatus());
			frBean.setFeasibilityCheck(x.getFeasibilityCheck());
			frBean.setFeasibilityType(x.getFeasibilityType());
			frBean.setQuoteId(x.getQuoteId());

			JSONObject dataEnvelopeObj = null;
			JSONParser jsonParser = new JSONParser();
			try {
				if (x.getCreateResponseJson() != null) {
					dataEnvelopeObj = (JSONObject) jsonParser.parse(x.getCreateResponseJson());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (x.getFeasibilityType() != null && dataEnvelopeObj != null && !dataEnvelopeObj.isEmpty()) {

				chargesCalculation(x, dataEnvelopeObj);
			}

			return x;
		}).collect(Collectors.toList());
		return recordInDB;
	}
	
	private void chargesCalculation(MfResponseDetail x, JSONObject dataEnvelopeObj) {
		// ================================================================================
		// Offnet Wireline
		// ================================================================================

		if (x.getFeasibilityType().equals("Offnet Wireline") && x.getType().equals("A-End")) {
			
			LOGGER.info("Offnet WireLine{} A-End");
			// otc charges =OTC Modem Charges +OTC/NRC - Installation
			if (dataEnvelopeObj.get("a_lm_otc_modem_charges_offwl") != null  || dataEnvelopeObj.get("a_lm_otc_nrc_installation_offwl") != null) {
				
				Double otcCharges = getCharges(dataEnvelopeObj.get("a_lm_otc_modem_charges_offwl")) + getCharges( dataEnvelopeObj.get("a_lm_otc_nrc_installation_offwl"));
				x.setOtcTotal(otcCharges);
			}
			// arc charges = ARC - BW + arc _modem charges
			if (dataEnvelopeObj.get("a_lm_arc_modem_charges_offwl") != null  || dataEnvelopeObj.get("a_lm_arc_bw_offwl") != null) {
				x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_arc_modem_charges_offwl"))
						+ getCharges(dataEnvelopeObj.get("a_lm_arc_bw_offwl")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
			
		}
			
			if (x.getFeasibilityType().equals("Offnet Wireline") && x.getType().equals("B-End")) {
				LOGGER.info("Offnet Wireline B-End");
				// otc charges =OTC Modem Charges +OTC/NRC - Installation
				if (dataEnvelopeObj.get("b_lm_otc_modem_charges_offwl") != null || dataEnvelopeObj.get("b_lm_otc_nrc_installation_offwl") != null) {
					Double otcCharges = getCharges(dataEnvelopeObj.get("b_lm_otc_modem_charges_offwl")) + getCharges( dataEnvelopeObj.get("b_lm_otc_nrc_installation_offwl"));
					x.setOtcTotal(otcCharges);
				}
				// arc charges = ARC - BW + arc _modem charges
				if (dataEnvelopeObj.get("b_lm_arc_modem_charges_offwl") != null || dataEnvelopeObj.get("b_lm_arc_bw_offwl") != null) {
					x.setArcTotal(getCharges(dataEnvelopeObj.get("b_lm_arc_modem_charges_offwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_arc_bw_offwl")));
				}
				x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
				
			}
				
				if (x.getFeasibilityType().equals("Offnet Wireline") && x.getType().equals("P2P")) {
					
				LOGGER.info("Offnet Wireline P2P");
					// otc charges =OTC Modem Charges +OTC/NRC - Installation
					if (dataEnvelopeObj.get("a_lm_otc_modem_charges_offwl") != null || dataEnvelopeObj.get("b_lm_otc_modem_charges_offwl") != null || dataEnvelopeObj.get("a_lm_otc_nrc_installation_offwl") != null || dataEnvelopeObj.get("b_lm_otc_nrc_installation_offwl") != null) {
						
						Double otcCharges = getCharges(dataEnvelopeObj.get("a_lm_otc_modem_charges_offwl")) + getCharges( dataEnvelopeObj.get("a_lm_otc_nrc_installation_offwl")) +
								getCharges(dataEnvelopeObj.get("b_lm_otc_modem_charges_offwl")) + getCharges( dataEnvelopeObj.get("b_lm_otc_nrc_installation_offwl"));
						x.setOtcTotal(otcCharges);
					}
					// arc charges = ARC - BW + arc _modem charges
					if (dataEnvelopeObj.get("a_lm_arc_modem_charges_offwl") != null ||dataEnvelopeObj.get("b_lm_arc_modem_charges_offwl") != null || dataEnvelopeObj.get("a_lm_arc_bw_offwl") != null||dataEnvelopeObj.get("b_lm_arc_bw_offwl") != null) {
						x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_arc_modem_charges_offwl"))
								+ getCharges(dataEnvelopeObj.get("a_lm_arc_bw_offwl"))
								+getCharges(dataEnvelopeObj.get("b_lm_arc_modem_charges_offwl"))
								+ getCharges(dataEnvelopeObj.get("b_lm_arc_bw_offwl")));
					}

			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
			
			
		// ================================================================================
		// offnet wireless
		// ================================================================================

		else if (x.getFeasibilityType().equals("Offnet Wireless") ) {
			if (x.getType().equals("A-End")) {
				LOGGER.info("Offnet Wireless A-End");
				if (dataEnvelopeObj.get("a_lm_nrc_mast_ofrf") != null
						|| dataEnvelopeObj.get("a_lm_nrc_bw_prov_ofrf") != null) {

					// otc charges = Mast Charges + OTC/NRC - Installation
					Double otcCharges = getCharges(dataEnvelopeObj.get("a_lm_nrc_mast_ofrf"))
							+ getCharges(dataEnvelopeObj.get("a_lm_nrc_bw_prov_ofrf"));
					x.setOtcTotal(otcCharges);
				}
				if (dataEnvelopeObj.get("a_lm_arc_bw_prov_ofrf") != null) {
					// arc charges = ARC - BW + ARC Modem Charges
					x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_arc_bw_prov_ofrf")));
				}
				x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
			}
			
			if (x.getType().equals("B-End")) {
				LOGGER.info("Offnet Wireless B-End");
				if (dataEnvelopeObj.get("b_lm_nrc_mast_ofrf") != null
						|| dataEnvelopeObj.get("b_lm_nrc_bw_prov_ofrf") != null) {

					// otc charges = Mast Charges + OTC/NRC - Installation
					Double otcCharges = getCharges(dataEnvelopeObj.get("b_lm_nrc_mast_ofrf"))
							+ getCharges(dataEnvelopeObj.get("b_lm_nrc_bw_prov_ofrf"));
					x.setOtcTotal(otcCharges);
				}
				if (dataEnvelopeObj.get("b_lm_arc_bw_prov_ofrf") != null) {
					// arc charges = ARC - BW + ARC Modem Charges
					x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_arc_bw_prov_ofrf")));
				}
				x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
			}
			
			if(x.getType().equals("P2P")) {
				LOGGER.info("Offnet Wireless P2P");
				if (dataEnvelopeObj.get("a_lm_nrc_mast_ofrf") != null || dataEnvelopeObj.get("b_lm_nrc_mast_ofrf") != null || dataEnvelopeObj.get("a_lm_nrc_bw_prov_ofrf") != null && dataEnvelopeObj.get("b_lm_nrc_bw_prov_ofrf") != null) {

					// otc charges = Mast Charges + OTC/NRC - Installation
					Double otcCharges = getCharges(dataEnvelopeObj.get("a_lm_nrc_mast_ofrf")) +getCharges(dataEnvelopeObj.get("a_lm_nrc_bw_prov_ofrf")) +getCharges(dataEnvelopeObj.get("b_lm_nrc_mast_ofrf")) +getCharges(dataEnvelopeObj.get("b_lm_nrc_bw_prov_ofrf"));
					x.setOtcTotal(otcCharges);
				}
				if (dataEnvelopeObj.get("a_lm_arc_bw_prov_ofrf") != null || dataEnvelopeObj.get("b_lm_arc_bw_prov_ofrf") != null) {
					// arc charges = ARC - BW + ARC Modem Charges
					x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_arc_bw_prov_ofrf"))+getCharges(dataEnvelopeObj.get("b_lm_arc_bw_prov_ofrf")));
				}
				x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
				}
		}

		// ================================================================================
		// MAN VBL
		// ================================================================================

		else if (x.getFeasibilityType().equals("MAN/VBL")) {
			if (x.getType().equals("A-End")) {
				LOGGER.info("MAN/VBL A-End");
				if (dataEnvelopeObj.get("a_lm_nrc_bw_onwl") != null
						|| dataEnvelopeObj.get("a_lm_nrc_prow_onwl") != null) {

					// otc charges =OTC/NRC - Installation + prov value otc
					Double otcinstal = getCharges(dataEnvelopeObj.get("a_lm_nrc_bw_onwl"));
					Double prov = getCharges(dataEnvelopeObj.get("a_lm_nrc_prow_onwl"));
					x.setOtcTotal(otcinstal + prov);
				}
				if (dataEnvelopeObj.get("a_lm_nrc_nerental_onwl") != null
						|| dataEnvelopeObj.get("a_lm_arc_bw_onwl") != null
						|| dataEnvelopeObj.get("a_lm_arc_prow_onwl") != null) {
					// arc charges = ARC - LRC/NE Rental + ARC - BW + PROW value ARC
					x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_nrc_nerental_onwl"))
							+ getCharges(dataEnvelopeObj.get("a_lm_arc_bw_onwl"))
							+ getCharges(dataEnvelopeObj.get("a_lm_arc_prow_onwl")));
				}
				// network capex

				if (dataEnvelopeObj.get("a_network_capex") != null) {
					x.setNetworkCapex(getCharges(dataEnvelopeObj.get("a_network_capex")));

				}
				// capex calculation - Man VBL
				if (dataEnvelopeObj.get("a_lm_nrc_ospcapex_onwl") != null
						|| dataEnvelopeObj.get("a_lm_nrc_inbldg_onwl") != null
						|| dataEnvelopeObj.get("a_lm_nrc_mux_onwl") != null) {

					x.setCapexTotal(getCharges(dataEnvelopeObj.get("a_lm_nrc_ospcapex_onwl"))
							+ getCharges(dataEnvelopeObj.get("a_lm_nrc_inbldg_onwl"))
							+ getCharges(dataEnvelopeObj.get("a_lm_nrc_mux_onwl")));
				}
				x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
			}

			if (x.getType().equals("B-End")) {
				LOGGER.info("MAN/VBL B-End");
				if (dataEnvelopeObj.get("b_lm_nrc_bw_onwl") != null
						|| dataEnvelopeObj.get("b_lm_nrc_prow_onwl") != null) {

					// otc charges =OTC/NRC - Installation + prov value otc
					Double otcinstal = getCharges(dataEnvelopeObj.get("b_lm_nrc_bw_onwl"));
					Double prov = getCharges(dataEnvelopeObj.get("b_lm_nrc_prow_onwl"));
					x.setOtcTotal(otcinstal + prov);
				}
				if (dataEnvelopeObj.get("b_lm_nrc_nerental_onwl") != null
						|| dataEnvelopeObj.get("b_lm_arc_bw_onwl") != null
						|| dataEnvelopeObj.get("b_lm_arc_prow_onwl") != null) {
					// arc charges = ARC - LRC/NE Rental + ARC - BW + PROW value ARC
					x.setArcTotal(getCharges(dataEnvelopeObj.get("b_lm_nrc_nerental_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_arc_bw_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_arc_prow_onwl")));
				}
				// network capex

				if (dataEnvelopeObj.get("b_network_capex") != null) {
					x.setNetworkCapex(getCharges(dataEnvelopeObj.get("b_network_capex")));

				}
				// capex calculation - Man VBL
				if (dataEnvelopeObj.get("b_lm_nrc_ospcapex_onwl") != null
						|| dataEnvelopeObj.get("b_lm_nrc_inbldg_onwl") != null
						|| dataEnvelopeObj.get("b_lm_nrc_mux_onwl") != null) {

					x.setCapexTotal(getCharges(dataEnvelopeObj.get("b_lm_nrc_ospcapex_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_nrc_inbldg_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_nrc_mux_onwl")));
				}
				x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
			}
			if (x.getType().equals("P2P")) {
				LOGGER.info("MAN/VBL P2P");
				if (dataEnvelopeObj.get("a_lm_nrc_bw_onwl") != null || dataEnvelopeObj.get("b_lm_nrc_bw_onwl") != null
						|| dataEnvelopeObj.get("a_lm_nrc_prow_onwl") != null
						|| dataEnvelopeObj.get("b_lm_nrc_prow_onwl") != null) {

					// otc charges =OTC/NRC - Installation + prov value otc
					Double otcinstal = getCharges(dataEnvelopeObj.get("a_lm_nrc_bw_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_nrc_bw_onwl"));
					Double prov = getCharges(dataEnvelopeObj.get("a_lm_nrc_prow_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_nrc_prow_onwl"));
					x.setOtcTotal(otcinstal + prov);
				}
				if (dataEnvelopeObj.get("a_lm_nrc_nerental_onwl") != null
						|| dataEnvelopeObj.get("a_lm_arc_bw_onwl") != null
						|| dataEnvelopeObj.get("a_lm_arc_prow_onwl") != null
						|| dataEnvelopeObj.get("b_lm_nrc_nerental_onwl") != null
						|| dataEnvelopeObj.get("b_lm_arc_bw_onwl") != null
						|| dataEnvelopeObj.get("b_lm_arc_prow_onwl") != null) {
					// arc charges = ARC - LRC/NE Rental + ARC - BW + PROW value ARC
					x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_nrc_nerental_onwl"))
							+ getCharges(dataEnvelopeObj.get("a_lm_arc_bw_onwl"))
							+ getCharges(dataEnvelopeObj.get("a_lm_arc_prow_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_nrc_nerental_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_arc_bw_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_arc_prow_onwl")));
				}
				// network capex

				if (dataEnvelopeObj.get("a_network_capex") != null || dataEnvelopeObj.get("b_network_capex") != null) {
					x.setNetworkCapex(getCharges(dataEnvelopeObj.get("a_network_capex"))
							+ getCharges(dataEnvelopeObj.get("b_network_capex")));

				}
				// capex calculation - Man VBL
				if (dataEnvelopeObj.get("a_lm_nrc_ospcapex_onwl") != null
						|| dataEnvelopeObj.get("a_lm_nrc_inbldg_onwl") != null
						|| dataEnvelopeObj.get("a_lm_nrc_mux_onwl") != null
						|| dataEnvelopeObj.get("b_lm_nrc_ospcapex_onwl") != null
						|| dataEnvelopeObj.get("b_lm_nrc_inbldg_onwl") != null
						|| dataEnvelopeObj.get("b_lm_nrc_mux_onwl") != null) {

					x.setCapexTotal(getCharges(dataEnvelopeObj.get("a_lm_nrc_ospcapex_onwl"))
							+ getCharges(dataEnvelopeObj.get("a_lm_nrc_inbldg_onwl"))
							+ getCharges(dataEnvelopeObj.get("a_lm_nrc_mux_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_nrc_ospcapex_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_nrc_inbldg_onwl"))
							+ getCharges(dataEnvelopeObj.get("b_lm_nrc_mux_onwl")));
				}
				x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
			}

		}
		// ================================================================================
		// RADWIN
		// ================================================================================

		else if (x.getFeasibilityType().equals("RADWIN")) {
			if(x.getType().equals("A-End")) {
				LOGGER.info("RADWIN A-End");
			if (dataEnvelopeObj.get("a_lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("a_lm_nrc_mast_onrf") != null) {

				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges = getCharges(dataEnvelopeObj.get("a_lm_nrc_bw_onrf"))
						+getCharges(dataEnvelopeObj.get("a_lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("a_lm_arc_bw_onrf") != null || dataEnvelopeObj.get("a_lm_arc_bw_backhaul_onrf") != null || dataEnvelopeObj.get("a_lm_arc_converter_charges_onrf") != null || dataEnvelopeObj.get("a_lm_arc_colocation_onrf") != null) {
				// arc charges = ARC-Radwin(BW) + ARC - BW + ARC convertor charges + colocation
				// charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_arc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_arc_converter_charges_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_arc_colocation_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
		
			if(x.getType().equals("B-End")) {
				LOGGER.info("RADWIN B-End");
			if (dataEnvelopeObj.get("b_lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("b_lm_nrc_mast_onrf") != null) {

				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges = getCharges(dataEnvelopeObj.get("b_lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("b_lm_arc_bw_onrf") != null || dataEnvelopeObj.get("b_lm_arc_bw_backhaul_onrf") != null
					 || dataEnvelopeObj.get("b_lm_arc_converter_charges_onrf") != null
					 || dataEnvelopeObj.get("b_lm_arc_colocation_onrf") != null) {
				// arc charges = ARC-Radwin(BW) + ARC - BW + ARC convertor charges + colocation
				// charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("b_lm_arc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_arc_converter_charges_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_arc_colocation_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
	
			if(x.getType().equals("P2P")) {
				LOGGER.info("RADWIN P2P");
			if (dataEnvelopeObj.get("a_lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("b_lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("a_lm_nrc_mast_onrf") != null || dataEnvelopeObj.get("b_lm_nrc_mast_onrf") != null) {

				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges = getCharges(dataEnvelopeObj.get("a_lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_nrc_mast_onrf"))
						+getCharges(dataEnvelopeObj.get("a_lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("a_lm_arc_bw_onrf") != null ||dataEnvelopeObj.get("b_lm_arc_bw_onrf") != null || dataEnvelopeObj.get("a_lm_arc_bw_backhaul_onrf") != null || dataEnvelopeObj.get("b_lm_arc_bw_backhaul_onrf") != null
					|| dataEnvelopeObj.get("a_lm_arc_converter_charges_onrf") != null || dataEnvelopeObj.get("b_lm_arc_converter_charges_onrf") != null
					|| dataEnvelopeObj.get("a_lm_arc_colocation_onrf") != null || dataEnvelopeObj.get("b_lm_arc_colocation_onrf") != null) {
				// arc charges = ARC-Radwin(BW) + ARC - BW + ARC convertor charges + colocation
				// charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_arc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_arc_converter_charges_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_arc_colocation_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_arc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_arc_converter_charges_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_arc_colocation_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
	}

		// ================================================================================
		// UBR PMP / WiMax
		// ================================================================================

		else if (x.getFeasibilityType().equals("UBR PMP/WiMax")) {
			if(x.getType().equals("A-End")) {
				LOGGER.info("UBR PMP/WiMax A-End");
			if (dataEnvelopeObj.get("a_lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("a_lm_nrc_mast_onrf") != null) {
				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges =getCharges(dataEnvelopeObj.get("a_lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("a_lm_arc_bw_backhaul_onrf") != null || dataEnvelopeObj.get("a_lm_arc_converter_charges_onrf") != null) {
				// arc charges = ARC - BW + arc convertor charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_arc_converter_charges_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
			
			if(x.getType().equals("B-End")) {
				LOGGER.info("UBR PMP/WiMax B-End");
			if (dataEnvelopeObj.get("b_lm_nrc_bw_onrf") != null ||dataEnvelopeObj.get("b_lm_nrc_mast_onrf") != null) {
				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges = getCharges(dataEnvelopeObj.get("b_lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("b_lm_arc_bw_backhaul_onrf") != null || dataEnvelopeObj.get("b_lm_arc_converter_charges_onrf") != null) {
				// arc charges = ARC - BW + arc convertor charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("b_lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_arc_converter_charges_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
		
			if(x.getType().equals("P2P")) {
				LOGGER.info("UBR PMP/WiMax P2P");
			if (dataEnvelopeObj.get("a_lm_nrc_bw_onrf") != null ||dataEnvelopeObj.get("b_lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("a_lm_nrc_mast_onrf") != null ||dataEnvelopeObj.get("b_lm_nrc_mast_onrf") != null) {
				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges = getCharges(dataEnvelopeObj.get("a_lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_nrc_mast_onrf"))
						+getCharges(dataEnvelopeObj.get("b_lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("a_lm_arc_bw_backhaul_onrf") != null ||dataEnvelopeObj.get("b_lm_arc_bw_backhaul_onrf") != null || dataEnvelopeObj.get("a_lm_arc_converter_charges_onrf") != null||dataEnvelopeObj.get("b_lm_arc_converter_charges_onrf") != null) {
				// arc charges = ARC - BW + arc convertor charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("a_lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("a_lm_arc_converter_charges_onrf"))
						+getCharges(dataEnvelopeObj.get("b_lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("b_lm_arc_converter_charges_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
	}
}

		
	/**
	 * Get Charges
	 *
	 * @param double value
	 * @return {@link Double}
	 */
	private Double getCharges(Object charge) {
		Double mfCharge = 0.0D;
		if (charge != null) {
			if (charge instanceof Double) {
				mfCharge = (Double) charge;
			} else if (charge instanceof String && !charge.equals("")) {
				mfCharge = new Double((String) charge);
			} else if (charge instanceof Long) {
				mfCharge = new Double((Long) charge);
			} else if (charge instanceof Integer) {
				mfCharge = new Double((Integer) charge);
			}
		}
		return mfCharge;
	}
	
	
	public Map<String, Object> filterSystemResponseBasedOnPrefix(String prefix, String systemResponseJsonStr){
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> dataMap =null;
		Map<String, Object> prefixFilteredMap =  new HashMap<>();

		try {
			dataMap = mapper.readValue(systemResponseJsonStr, new TypeReference<HashMap<String, Object>>(){});
		} catch (IOException e) {
			
		}
		if (dataMap != null) {
		/*	dataMap.forEach(map -> {
				systemResponseMap.putAll(map.entrySet().stream()
			        .collect(Collectors.toMap(entry -> (String)entry.getKey(), entry -> entry.getValue())));
			}); */
			
			if(dataMap != null && !dataMap.isEmpty()) {
				if(prefix!=null) {
				prefixFilteredMap = dataMap.entrySet().stream().filter(  entry -> entry.getKey().startsWith(prefix))
				.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
			}else if(prefix ==null) {
				
				prefixFilteredMap.putAll(dataMap);
				
			}
		}
	}
		return prefixFilteredMap;
	
	}

	public MfDetailsBean getMfDetailsBean(MfDetail mfDetailOpt) {
		 MfDetailsBean mfDetailsBean  = new MfDetailsBean ();
		try {
			if(mfDetailOpt!=null)
							mfDetailsBean.setMfDetails(Utils.convertJsonToObject(mfDetailOpt.getMfDetails(), MfDetailAttributes.class));
						} catch (TclCommonException e) {
							LOGGER.warn("Error on converting the mfDetail json string to object {} ", e.getMessage());
						}
		
		return mfDetailsBean;
	}
	
	public String saveMfNplResponseDetailsInOms(Integer quoteId) throws TclCommonException {

	List<MfNplResponseDetailBean> mfResponseDetailBeans = getMfResponseDetailsOfSiteIds(quoteId);
	saveMfResponseDetailMQ(mfResponseDetailBeans);
	return "success";
	}
	
	public List<MfNplResponseDetailBean> getMfResponseDetailsOfSiteIds(Integer quoteId) {
		
		List<MfNplResponseDetailBean> mfResponseDetailBeans = new ArrayList<>();
		 List<MfNplResponseDetail> resDetail = mfNplResponseDetailRepository.findByQuoteId(quoteId);
		resDetail.stream().forEach(linkDetail -> {
			MfNplResponseDetailBean mfBean=new MfNplResponseDetailBean();
			LOGGER.info("Feasibility status of the link {} is {}" ,linkDetail.getLinkId(), linkDetail.getFeasibilityStatus());	
			//mfBean.setId(linkDetail.getId());
			mfBean.setFeasibilityMode(linkDetail.getFeasibilityMode());
			mfBean.setFeasibilityType(linkDetail.getFeasibilityType());
			mfBean.setLinkId(linkDetail.getLinkId());
			mfBean.setType(linkDetail.getType());
			mfBean.setProvider(linkDetail.getProvider());
			mfBean.setMfRank(linkDetail.getMfRank());
			mfBean.setIsSelected(linkDetail.getIsSelected());
			mfBean.setCreateResponseJson(linkDetail.getCreateResponseJson());
			mfBean.setCreatedTime(linkDetail.getCreatedTime());
			mfBean.setFeasibilityCheck(linkDetail.getFeasibilityCheck());
			mfBean.setFeasibilityModeB(linkDetail.getFeasibilityModeB());
			mfBean.setFeasibilityTypeB(linkDetail.getFeasibilityTypeB());
			mfBean.setProviderB(linkDetail.getProviderB());
			mfBean.setFeasibilityStatus(linkDetail.getFeasibilityStatus());
			mfBean.setQuoteId(linkDetail.getQuoteId());
		
			mfResponseDetailBeans.add(mfBean);
		});
		return mfResponseDetailBeans;
	}
	
	/**
	 * Save MF Response Detail MQ
	 *
	 * @param mfResponseDetailBeans
	 */
	public void saveMfResponseDetailMQ(List<MfNplResponseDetailBean> mfResponseDetailBeans) {
		if (!CollectionUtils.isEmpty(mfResponseDetailBeans)) {
			try {
				String request = Utils.convertObjectToJson(mfResponseDetailBeans);
				LOGGER.info(
						"MDC Filter token value in before Queue call saving mf response details in site feasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(saveMFResponseInOmsMQ, request);
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.SAVE_MF_RESPONSE_IN_OMS_MQ_ERROR, e,
						ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.LINKS_NOT_FOUND,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}
	
	/**
	 * used to get afm and asp teams task details for NPL
	 * @param siteCode
	 * @return
	 * @throws TclCommonException
	 */
	public List<OmsTaskDetailBean> getAfmAndAspTaskDetailsForNPL(String siteCode) throws TclCommonException {
		if (StringUtils.isEmpty(siteCode))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		List<String> mstDefKeys = new ArrayList<>();
		mstDefKeys.add(ManualFeasibilityWFConstants.MF_AFM);
		mstDefKeys.add(ManualFeasibilityWFConstants.MF_ASP);
		List<OmsTaskDetailBean> omsTaskBeanList = new ArrayList<>();
		List<Task> tasks = taskRepository.findBySiteCodeAndMstTaskDef_keyIn(siteCode, mstDefKeys);
		List<Task> tasksBasedOnLinks = new ArrayList<Task>();

		
		if (!CollectionUtils.isEmpty(tasks)) {
			MfDetailAttributes mfDetailAttr = null;
			try {
				mfDetailAttr = Utils.convertJsonToObject(tasks.get(0).getMfDetail().getMfDetails(),
						MfDetailAttributes.class);
			} catch (TclCommonException e) {
				LOGGER.error("Error while converting json to object", e);
			}
			Integer linkIdForGivenTask = mfDetailAttr.getLinkId();
			List<MfDetail> mfDetailsEntityList = mfDetailRepository.findByLinkId(linkIdForGivenTask);

			mfDetailsEntityList.stream().forEach(x -> {
				List<Task> taskBySite = taskRepository.findBySiteId(x.getSiteId());
				tasksBasedOnLinks.addAll(taskBySite);
			});
		}
		
		
		if (!tasksBasedOnLinks.isEmpty()) {
			omsTaskBeanList.addAll(tasksBasedOnLinks.stream().map(task -> {
				OmsTaskDetailBean omsTaskDetailBean = new OmsTaskDetailBean();
				omsTaskDetailBean.setFeasibilityId(task.getFeasibilityId());
				omsTaskDetailBean.setAssignedOn(task.getCreatedTime());
				omsTaskDetailBean.setUpdatedOn(task.getUpdatedTime());
				MfTaskDetail mfTaskDetail = mfTaskDetailRepository.findByTaskId(task.getId());
				omsTaskDetailBean.setAssignedTo(mfTaskDetail.getAssignedGroup());
				omsTaskDetailBean.setAssignedFrom(mfTaskDetail.getAssignedFrom());
				omsTaskDetailBean.setFeasibilityStatus(mfTaskDetail.getStatus());
				omsTaskDetailBean.setReason(mfTaskDetail.getReason());
				omsTaskDetailBean.setRemarks(mfTaskDetail.getResponderComments());
				JSONObject dataObj = null;
				JSONParser jsonParser = new JSONParser();
				try {
					if (task.getMfDetail() != null && task.getMfDetail().getMfDetails() != null) {
						dataObj = (JSONObject) jsonParser.parse(task.getMfDetail().getMfDetails());
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(dataObj.get("mfLinkEndType") !=null) {
				omsTaskDetailBean.setTastRealatedTo((String) dataObj.get("mfLinkEndType"));
				}
				return omsTaskDetailBean;
			}).collect(Collectors.toList()));

		}
		return omsTaskBeanList;

	}

	public LinkDetailsManualBean makeLink(QuoteDetailForTask quoteDetail) {
		LinkDetailsManualBean linkDetailsManualBean =null;
		try {
			// Both are MF responses
			if (quoteDetail.getaEndResponseId() != null && quoteDetail.getbEndResponseId() != null) {
				Map<String, Object> selectedMapA = new HashMap<String, Object>();
				Map<String, Object> selectedMapB = new HashMap<String, Object>();
				Optional<MfResponseDetail> desiredResponseEntryForA = mfResponseDetailRepository
						.findById(quoteDetail.getaEndResponseId());
				Optional<MfResponseDetail> desiredResponseEntryForB = mfResponseDetailRepository
						.findById(quoteDetail.getbEndResponseId());

				if (desiredResponseEntryForA.isPresent() && desiredResponseEntryForB.isPresent()) {
					selectedMapA = filterSystemResponseBasedOnPrefix(null,
							desiredResponseEntryForA.get().getCreateResponseJson());
					LOGGER.info("Site A selected Json", selectedMapA);
					selectedMapB = filterSystemResponseBasedOnPrefix(null,
							desiredResponseEntryForB.get().getCreateResponseJson());
					LOGGER.info("Site B selected Json", selectedMapB);
					if (!selectedMapA.isEmpty() && !selectedMapB.isEmpty()) {
						selectedMapA.putAll(selectedMapB);
						JSONObject json = new JSONObject();
						json.putAll(selectedMapA);
						linkDetailsManualBean = new LinkDetailsManualBean();
						populateGenralAttrs(linkDetailsManualBean, json,quoteDetail);
						populateBEndAttrs(linkDetailsManualBean, desiredResponseEntryForB.get());
						populateAEndAttrs(linkDetailsManualBean, desiredResponseEntryForA.get());
					}
				}
			}

			// AEnd MF response , B System feasible
			else if (quoteDetail.getaEndResponseId() != null && quoteDetail.isbEndSystemFeasible()
					&& quoteDetail.getMfDetailIdForSystemFeasibleEnd() != null) {
				Optional<MfResponseDetail> desiredResponseEntryForA = mfResponseDetailRepository
						.findById(quoteDetail.getaEndResponseId());
				Map<String, Object> aselectedMap = new HashMap<String, Object>();
				if (desiredResponseEntryForA.isPresent()) {
					aselectedMap = filterSystemResponseBasedOnPrefix(null,
							desiredResponseEntryForA.get().getCreateResponseJson());
					LOGGER.info("Site A selected Json", aselectedMap);

				}
				Optional<MfDetail> mfDetailEntry = mfDetailRepository
						.findById(quoteDetail.getMfDetailIdForSystemFeasibleEnd());
				if (mfDetailEntry.isPresent()) {
					Map<String, Object> systemAttrMap = filterSystemResponseBasedOnPrefix("b_",
							mfDetailEntry.get().getSystemLinkResponseJson());
					LOGGER.info("Site B system selected Json", systemAttrMap);
					if (!aselectedMap.isEmpty() && !systemAttrMap.isEmpty()) {
						aselectedMap.putAll(systemAttrMap);
						JSONObject json = new JSONObject();
						json.putAll(aselectedMap);
						linkDetailsManualBean = new LinkDetailsManualBean();
						populateGenralAttrs(linkDetailsManualBean, json,quoteDetail);
						MfResponseDetail desiredResponseEntryForBSystem = new MfResponseDetail();
						desiredResponseEntryForBSystem.setFeasibilityMode("OnnetWL_NPL");
						desiredResponseEntryForBSystem.setProvider("TATA COMMUNICATIONS");
						desiredResponseEntryForBSystem.setFeasibilityType("MAN");
						populateBEndAttrs(linkDetailsManualBean, desiredResponseEntryForBSystem);
						populateAEndAttrs(linkDetailsManualBean, desiredResponseEntryForA.get());
					}
				}
			}

			// BEnd MF response , A System feasible

			else if (quoteDetail.getbEndResponseId() != null && quoteDetail.isaEndSystemFeasible()
					&& quoteDetail.getMfDetailIdForSystemFeasibleEnd() != null) {
				Optional<MfResponseDetail> desiredResponseEntryForB = mfResponseDetailRepository
						.findById(quoteDetail.getbEndResponseId());
				Map<String, Object> bselectedMap = new HashMap<String, Object>();
				if (desiredResponseEntryForB.isPresent()) {
					bselectedMap = filterSystemResponseBasedOnPrefix(null,
							desiredResponseEntryForB.get().getCreateResponseJson());
					LOGGER.info("Site B selected Json", bselectedMap);

				}
				Optional<MfDetail> mfDetailEntry = mfDetailRepository
						.findById(quoteDetail.getMfDetailIdForSystemFeasibleEnd());
				if (mfDetailEntry.isPresent()) {
					Map<String, Object> systemAttrMap = filterSystemResponseBasedOnPrefix("a_",
							mfDetailEntry.get().getSystemLinkResponseJson());
					LOGGER.info("Site A system selected Json", systemAttrMap);
					if (!bselectedMap.isEmpty() && !systemAttrMap.isEmpty()) {
						bselectedMap.putAll(systemAttrMap);
						JSONObject json = new JSONObject();
						json.putAll(bselectedMap);
						linkDetailsManualBean = new LinkDetailsManualBean();
						populateGenralAttrs(linkDetailsManualBean, json,quoteDetail);
						MfResponseDetail desiredResponseEntryForASystem = new MfResponseDetail();
						desiredResponseEntryForASystem.setFeasibilityMode("OnnetWL_NPL");
						desiredResponseEntryForASystem.setProvider("TATA COMMUNICATIONS");
						desiredResponseEntryForASystem.setFeasibilityType("MAN");
						populateBEndAttrs(linkDetailsManualBean, desiredResponseEntryForB.get());
						populateAEndAttrs(linkDetailsManualBean, desiredResponseEntryForASystem);
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return linkDetailsManualBean;
	}

	private void populateAEndAttrs(LinkDetailsManualBean linkDetailsManualBean,
			MfResponseDetail desiredResponseEntryForA) {
		linkDetailsManualBean.setFeasibilityModeA(desiredResponseEntryForA.getFeasibilityMode());
		linkDetailsManualBean.setProviderA(desiredResponseEntryForA.getProvider());
		linkDetailsManualBean.setFeasibilityTypeA(desiredResponseEntryForA.getFeasibilityType());
	}

	private void populateBEndAttrs(LinkDetailsManualBean linkDetailsManualBean,
			MfResponseDetail desiredResponseEntryForB) {
		linkDetailsManualBean.setFeasibilityModeB(desiredResponseEntryForB.getFeasibilityMode());
		linkDetailsManualBean.setProviderB(desiredResponseEntryForB.getProvider());
		linkDetailsManualBean.setFeasibilityTypeB(desiredResponseEntryForB.getFeasibilityType());
	}

	private void populateGenralAttrs(LinkDetailsManualBean linkDetailsManualBean, JSONObject json, QuoteDetailForTask quoteDetail) {
		linkDetailsManualBean.setLinkId(quoteDetail.getLinkId());
		linkDetailsManualBean.setFeasibilityCheck("manual");
		linkDetailsManualBean.setType(quoteDetail.getType());
		linkDetailsManualBean.setRank(0);
		linkDetailsManualBean.setFeasibilityCode(Utils.generateUid());
		linkDetailsManualBean.setIsSelected(0);
		linkDetailsManualBean.setResponseJson(json.toJSONString());
	}
}
