package com.tcl.dias.l2oworkflowutils.service.v1;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.CreateResponseBean;
import com.tcl.dias.common.beans.FetchResponseBean;
import com.tcl.dias.common.beans.Mf3DSiteStatus;
import com.tcl.dias.common.beans.Mf3DTaskResponse;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetailAudit;
import com.tcl.dias.l2oworkflow.entity.entities.PreMfResponse;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.repository.MfDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskDetailAuditRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.PreMfResponseRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
import com.tcl.dias.l2oworkflowutils.beans.Mf3DDashboardbean;
import com.tcl.dias.l2oworkflowutils.beans.MfTaskDetailBean;
import com.tcl.dias.l2oworkflowutils.beans.PreMfResponseBean;
import com.tcl.dias.l2oworkflowutils.constants.ExceptionConstants;
import com.tcl.dias.l2oworkflowutils.constants.ManualFeasibilityWFConstants;
import com.tcl.dias.l2oworkflowutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional(readOnly = false)
public class Mf3DMapsTaskService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Mf3DMapsTaskService.class);

	@Autowired
	protected TaskRepository taskRepository;

	@Autowired
	PreMfResponseRepository preMfResponseRepository;
	
	@Autowired
	MfTaskDetailRepository mfTaskDetailRepository;
	
	@Autowired
	MfTaskDetailAuditRepository mfTaskDetailAuditRepository;
	
	@Autowired
	CmmnRuntimeService cmmnRuntimeService;
	
	@Autowired
	MfDetailRepository mfDetailRepository;
	
	@Autowired
	TaskService taskService;

	
	@Autowired
	MQUtils mqUtils;
	

	@Value("${save.pre.mf.response.in.oms.mq}")
	String savePreMFResponseInOmsMQ;
	/**
	 * 
	 * This function is used to saveResponse Details
	 * 
	 * @param action
	 * @param        <CreateResponseBean>
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public <createResponseBean> void saveOrUpdatePreMfResponse(CreateResponseBean createResponseBean, String action)
			throws TclCommonException {

		if (action.equals("save")) {
			LOGGER.info("Save create response in DB");
			String orchCategory = null;
			Optional<Task> taskOpt = taskRepository.findById(createResponseBean.getTaskId());
			PreMfResponse preMfCreateResponse = new PreMfResponse();
			preMfCreateResponse.setTaskId(createResponseBean.getTaskId());
			preMfCreateResponse.setSiteId(createResponseBean.getSiteId());
			preMfCreateResponse.setProvider(createResponseBean.getProvider());
			if (taskOpt.isPresent())
				preMfCreateResponse.setProduct(taskOpt.get().getServiceType());
			ObjectMapper mapper = new ObjectMapper();

			JSONObject dataEnvelopeObj = null;
			JSONParser jsonParser = new JSONParser();
			try {
				dataEnvelopeObj = (JSONObject) jsonParser.parse(createResponseBean.getCreateResponseAttr());
				if (dataEnvelopeObj.get("Predicted_Access_Feasibility") != null) {
					preMfCreateResponse
							.setFeasibilityStatus((String) dataEnvelopeObj.get("Predicted_Access_Feasibility"));
				}
				if (dataEnvelopeObj.get("provider_name") != null) {
					preMfCreateResponse.setProvider((String) dataEnvelopeObj.get("provider_name"));
				}
				if (!dataEnvelopeObj.containsKey("feasibility_response_id")
						|| (dataEnvelopeObj.get("feasibility_response_id") == null)
						|| (dataEnvelopeObj.get("feasibility_response_id") != null
								&& (dataEnvelopeObj.get("feasibility_response_id").equals("")))) {
					dataEnvelopeObj.put("feasibility_response_id", Utils.generateTaskResponseId());
				}

				if (dataEnvelopeObj.get("Orch_LM_Type") != null && !dataEnvelopeObj.get("Orch_LM_Type").equals("")) {
					dataEnvelopeObj.put("Type", dataEnvelopeObj.get("Orch_LM_Type"));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String jsonStr = "";
			try {
				jsonStr = mapper.writeValueAsString(dataEnvelopeObj);
			} catch (JsonProcessingException e) {
				LOGGER.info("Exception occurred in parsing response json");
			}

			if (createResponseBean.getFeasibilityType() != null) {
				if (createResponseBean.getFeasibilityType().equals("UBR PMP / WiMax'")) {
					orchCategory = "UBR PMP ( 2mb and 4 mb - BTS column)";
					dataEnvelopeObj.put("Orch_Category", orchCategory);
				}
				if (createResponseBean.getFeasibilityType().equals("RADWIN'")) {
					orchCategory = "UBR PMP ( 2mb and 4 mb - BTS column)";
					dataEnvelopeObj.put("Orch_Category", orchCategory);
				}
			}
			preMfCreateResponse.setCreateResponseJson(jsonStr);
			preMfCreateResponse.setCreatedBy(Utils.getSource());
			// mfCreateResponse.setUpdatedTime(new Timestamp(new Date().getTime()));
			preMfCreateResponse.setCreatedTime(new Timestamp(new Date().getTime()));
			preMfCreateResponse.setUpdatedTime(preMfCreateResponse.getCreatedTime());
			preMfCreateResponse.setType(createResponseBean.getType());
			preMfCreateResponse.setFeasibilityMode(createResponseBean.getFeasibilityMode());
			preMfCreateResponse.setFeasibilityType(createResponseBean.getFeasibilityType());
			preMfCreateResponse.setIsSelected(0);
			preMfCreateResponse.setFeasibilityCheck("manual");

			if (createResponseBean.getQuoteId() != null) {
				preMfCreateResponse.setQuoteId(Integer.valueOf(createResponseBean.getQuoteId()));
			}

			if (createResponseBean.getQuoteCode() != null) {
				preMfCreateResponse.setQuoteCode(createResponseBean.getQuoteCode());
			}
			PreMfResponse savedResponse = preMfResponseRepository.save(preMfCreateResponse);
			// processMfResponseDetailAudit(savedResponse, false);
		} else if (action.equals("update") && createResponseBean.getTaskId() != null
				&& createResponseBean.getRowId() != null) {

			Optional<PreMfResponse> mfCreateResponseopt = preMfResponseRepository
					.findById(Integer.valueOf(createResponseBean.getRowId()));

			if (mfCreateResponseopt.isPresent()) {
				PreMfResponse mfCreateResponseForUpdate = mfCreateResponseopt.get();
				mfCreateResponseForUpdate.setTaskId(createResponseBean.getTaskId());

				if (createResponseBean.getSiteId() != null) {
					mfCreateResponseForUpdate.setSiteId(createResponseBean.getSiteId());
				}

				if (createResponseBean.getCreateResponseAttr() != null) {
					ObjectMapper mapper = new ObjectMapper();
					JSONObject dataEnvelopeObj = null;
					JSONParser jsonParser = new JSONParser();
					try {
						dataEnvelopeObj = (JSONObject) jsonParser.parse(createResponseBean.getCreateResponseAttr());
						if (dataEnvelopeObj.get("Predicted_Access_Feasibility") != null) {
							mfCreateResponseForUpdate.setFeasibilityStatus(
									(String) dataEnvelopeObj.get("Predicted_Access_Feasibility"));
						}

						if (dataEnvelopeObj.get("provider_name") != null) {
							mfCreateResponseForUpdate.setProvider((String) dataEnvelopeObj.get("provider_name"));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String jsonStr = "";
					try {
						jsonStr = mapper.writeValueAsString(dataEnvelopeObj);
					} catch (JsonProcessingException e) {
						LOGGER.info("Exception occurred in parsing response json");
					}
					mfCreateResponseForUpdate.setCreateResponseJson(jsonStr);
				}
				if (createResponseBean.getType() != null) {
					mfCreateResponseForUpdate.setType(createResponseBean.getType());
				}
				if (createResponseBean.getFeasibilityMode() != null) {
					mfCreateResponseForUpdate.setFeasibilityMode(createResponseBean.getFeasibilityMode());
				}
				mfCreateResponseForUpdate.setUpdatedBy(Utils.getSource());
				if (createResponseBean.getFeasibilityType() != null) {
					mfCreateResponseForUpdate.setFeasibilityType(createResponseBean.getFeasibilityType());
				}
				if (createResponseBean.getQuoteId() != null) {
					mfCreateResponseForUpdate.setQuoteId(Integer.valueOf(createResponseBean.getQuoteId()));
				}
				if (createResponseBean.getQuoteCode() != null) {
					mfCreateResponseForUpdate.setQuoteCode(createResponseBean.getQuoteCode());
				}
				// saving updated time as current time stamp
				mfCreateResponseForUpdate.setUpdatedTime(new Timestamp(new Date().getTime()));

				PreMfResponse updatedResponse = preMfResponseRepository.save(mfCreateResponseForUpdate);
				// processMfResponseDetailAudit(updatedResponse, true);
			}
		}
	}

	/**
	 * 
	 * This function is used to fetch response Details for given siteId
	 * 
	 * @param <CreateResponseBean>
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public List<PreMfResponse> fetch3DMfResponse(Integer siteId) throws TclCommonException {
		LOGGER.info("Fetch response from DB");
		List<PreMfResponse> recordInDB = preMfResponseRepository.findBySiteId(siteId);

		return processMfResponseDetailList(recordInDB);
	}

	/**
	 * Method to process MFResponse Detail
	 * 
	 * @author krutsrin
	 * @param recordInDB
	 * @return list of processed MFResponseDetail
	 */
	private List<PreMfResponse> processMfResponseDetailList(List<PreMfResponse> recordInDB) {
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
			frBean.setMfRank(x.getRank());
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

	private void chargesCalculation(PreMfResponse x, JSONObject dataEnvelopeObj) {
		// ================================================================================
		// Offnet Wireline
		// ================================================================================

		if (x.getFeasibilityType().equals("Offnet Wireline")) {

			// otc charges =OTC Modem Charges +OTC/NRC - Installation
			if (dataEnvelopeObj.get("lm_otc_modem_charges_offwl") != null
					|| dataEnvelopeObj.get("lm_otc_nrc_installation_offwl") != null) {
				Double otcCharges = getCharges(dataEnvelopeObj.get("lm_otc_modem_charges_offwl"))
						+ +getCharges(dataEnvelopeObj.get("lm_otc_nrc_installation_offwl"));
				x.setOtcTotal(otcCharges);
			}
			// arc charges = ARC - BW + arc _modem charges
			if (dataEnvelopeObj.get("lm_arc_modem_charges_offwl") != null
					|| dataEnvelopeObj.get("lm_arc_bw_offwl") != null) {
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_arc_modem_charges_offwl"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_bw_offwl")));
			}

			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
		// ================================================================================
		// offnet wireless
		// ================================================================================

		else if (x.getFeasibilityType().equals("Offnet Wireless")) {

			if (dataEnvelopeObj.get("lm_nrc_mast_ofrf") != null || dataEnvelopeObj.get("lm_nrc_bw_prov_ofrf") != null) {

				// otc charges = Mast Charges + OTC/NRC - Installation
				Double otcCharges = getCharges(dataEnvelopeObj.get("lm_nrc_mast_ofrf"))
						+ +getCharges(dataEnvelopeObj.get("lm_nrc_bw_prov_ofrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("lm_arc_bw_prov_ofrf") != null) {
				// arc charges = ARC - BW + ARC Modem Charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_arc_bw_prov_ofrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}

		// ================================================================================
		// MAN VBL
		// ================================================================================

		else if (x.getFeasibilityType().equals("MAN/VBL")) {

			if (dataEnvelopeObj.get("lm_nrc_bw_onwl") != null || dataEnvelopeObj.get("lm_nrc_prow_onwl") != null) {

				// otc charges =OTC/NRC - Installation + prov value otc
				Double otcinstal = getCharges(dataEnvelopeObj.get("lm_nrc_bw_onwl"));
				Double prov = getCharges(dataEnvelopeObj.get("lm_nrc_prow_onwl"));
				x.setOtcTotal(otcinstal + prov);
			}
			if (dataEnvelopeObj.get("lm_nrc_nerental_onwl") != null || dataEnvelopeObj.get("lm_arc_bw_onwl") != null
					|| dataEnvelopeObj.get("lm_arc_prow_onwl") != null) {
				// arc charges = ARC - LRC/NE Rental + ARC - BW + PROW value ARC
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_nrc_nerental_onwl"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_bw_onwl"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_prow_onwl")));
			}
			// network capex

			if (dataEnvelopeObj.get("network_capex") != null) {
				x.setNetworkCapex(getCharges(dataEnvelopeObj.get("network_capex")));

			}
			// capex calculation - Man VBL
			if (dataEnvelopeObj.get("lm_nrc_ospcapex_onwl") != null || dataEnvelopeObj.get("capex_in_building") != null
					|| dataEnvelopeObj.get("lm_nrc_mux_onwl") != null) {

				x.setCapexTotal(getCharges(dataEnvelopeObj.get("lm_nrc_ospcapex_onwl"))
						+ getCharges(dataEnvelopeObj.get("capex_in_building"))
						+ getCharges(dataEnvelopeObj.get("lm_nrc_mux_onwl")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
		// ================================================================================
		// RADWIN
		// ================================================================================

		else if (x.getFeasibilityType().equals("RADWIN")) {
			if (dataEnvelopeObj.get("lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("lm_nrc_mast_onrf") != null) {

				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges = getCharges(dataEnvelopeObj.get("lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("lm_arc_bw_onrf") != null || dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf") != null
					|| dataEnvelopeObj.get("lm_arc_converter_charges_onrf") != null
					|| dataEnvelopeObj.get("lm_arc_colocation_onrf") != null) {
				// arc charges = ARC-Radwin(BW) + ARC - BW + ARC convertor charges + colocation
				// charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_arc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_converter_charges_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_colocation_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}

		// ================================================================================
		// UBR PMP / WiMax
		// ================================================================================

		else if (x.getFeasibilityType().equals("UBR PMP/WiMax")) {

			if (dataEnvelopeObj.get("lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("lm_nrc_mast_onrf") != null) {
				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges = getCharges(dataEnvelopeObj.get("lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf") != null
					|| dataEnvelopeObj.get("lm_arc_converter_charges_onrf") != null) {
				// arc charges = ARC - BW + arc convertor charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_converter_charges_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
	}
	
	
	/**
	 * Method to edit task status for 3D preMf response
	 * 
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	public void editTaskStatus(MfTaskDetailBean mfTaskDetailBean) throws TclCommonException {
		LOGGER.info("Inside editTaskStatus method to edit status for the task {} ", mfTaskDetailBean.getTaskId());
		try {
			if (mfTaskDetailBean.getSiteId() != null && mfTaskDetailBean.getTaskId() != null) {
				Optional<Task> taskOpt = taskRepository.findById(mfTaskDetailBean.getTaskId());
				

				if(taskOpt.isPresent() && (taskOpt.get().getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)
						|| taskOpt.get().getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP))){
					
					List<PreMfResponse> recordInDB =preMfResponseRepository.findBytaskId(taskOpt.get().getId());
					Optional<PreMfResponse> result =null;
					if(recordInDB!=null && !recordInDB.isEmpty()) {
						result = recordInDB.stream().filter( x -> x.getFeasibilityStatus()!=null)
							.filter(y -> y.getFeasibilityStatus().startsWith(ManualFeasibilityWFConstants.FEASIBLE)).findAny();
						}
					
					if(mfTaskDetailBean.getStatus()!=null && mfTaskDetailBean.getStatus().equalsIgnoreCase(ManualFeasibilityWFConstants.FEASIBLE)) {
						if(result==null || !result.isPresent()) {
							LOGGER.info("Feasible responses has not been created but overall status is feasible for taskid {} :",taskOpt.get().getId());
							throw new TclCommonException(ExceptionConstants.NO_FEASIBLE_RESPONSE,
									ResponseResource.R_CODE_ERROR);
						}
					}else if(mfTaskDetailBean.getStatus()!=null && mfTaskDetailBean.getStatus().equalsIgnoreCase(ManualFeasibilityWFConstants.NOT_FEASIBLE)) {
						
						if(result!=null && result.isPresent()) {
							LOGGER.info("Feasible responses are available but overall status is Not-feasible for taskid {} :",taskOpt.get().getId());
							throw new TclCommonException(ExceptionConstants.FEASIBLE_RESPONSE_AVAILABLE,
									ResponseResource.R_CODE_ERROR);
						}
					}
				}
				if (mfTaskDetailBean.getStatus() != null
						&& mfTaskDetailBean.getStatus().equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN)
						&& taskOpt.isPresent()
						&& taskOpt.get().getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)) {
					LOGGER.info("Checking ASP task status..");
					List<MfTaskDetail> requestorAssignedTask = mfTaskDetailRepository
							.findByRequestorTaskIdAndAssignedToStartsWithAndStatusIn(mfTaskDetailBean.getTaskId(), "ASP",
									Arrays.asList(ManualFeasibilityWFConstants.PENDING));
					if (!requestorAssignedTask.isEmpty()) {
						LOGGER.info("ASP task is open while returning AFM task..");
						throw new TclCommonException(ExceptionConstants.ASP_TASK_IS_OPEN,
								ResponseResource.R_CODE_ERROR);
					}
				} else if (StringUtils.isNotEmpty(mfTaskDetailBean.getStatus())
						&& mfTaskDetailBean.getStatus().equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN)
						&& taskOpt.isPresent() && taskOpt.get().getMstTaskDef().getKey()
								.equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP)) {
					MfTaskDetail aspTaskDetail = mfTaskDetailRepository.findByTaskId(mfTaskDetailBean.getTaskId());
					if(aspTaskDetail !=null && aspTaskDetail.getRequestorTaskId() !=null) {
					Optional<Task> parentTaskOpt = taskRepository
							.findById(aspTaskDetail.getRequestorTaskId());
					if (parentTaskOpt.isPresent() && parentTaskOpt.get().getMstStatus().getCode()
							.equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
						LOGGER.info("Parent task with Id : {} is closed ",parentTaskOpt.get().getId());
						throw new TclCommonException(ExceptionConstants.PARENT_TASK_IS_CLOSED,
								ResponseResource.R_CODE_ERROR);
					}
					}
				} 
				MfTaskDetail mfTaskDetail = mfTaskDetailRepository.findBySiteIdAndTaskId(mfTaskDetailBean.getSiteId(),
						mfTaskDetailBean.getTaskId());
				if (mfTaskDetail != null) {
					mfTaskDetail.setResponderComments(mfTaskDetailBean.getResponderComments());
					mfTaskDetail.setStatus(mfTaskDetailBean.getStatus());
					mfTaskDetail.setReason(mfTaskDetailBean.getReason());
					MfTaskDetailAudit mfTaskDetailAudit = new MfTaskDetailAudit(mfTaskDetail);
					mfTaskDetailAudit.setCreatedBy(Utils.getSource());
					mfTaskDetailAuditRepository.save(mfTaskDetailAudit);
					mfTaskDetailRepository.save(mfTaskDetail);
				}
				if (taskOpt.isPresent()) {
					Task task = taskOpt.get();
					if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_PRV)) {
						cmmnRuntimeService.setVariable(task.getWfProcessInstId(),
								ManualFeasibilityWFConstants.PRV_STATUS, mfTaskDetailBean.getStatus());
						cmmnRuntimeService.setVariable(task.getWfProcessInstId(),
								ManualFeasibilityWFConstants.PRV_COMMENTS, mfTaskDetailBean.getResponderComments());
					}
				}
			} else
				throw new TclCommonException(ExceptionConstants.TASK_DETAILS_EMPTY,
						ResponseResource.R_CODE_BAD_REQUEST);
		} catch (Exception e) {
			if (e instanceof TclCommonException) {
				throw e;
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
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
			map.put("quoteId", quoteId);
			if(taskService.checkIfTaskClosed(task.getWfProcessInstId(), "asp")
					&& taskService.checkIfTaskClosed(task.getWfProcessInstId(), "afm")) {
				//selectRelevantManualPreMfFeasibleResponse(siteId, mfStatus, mfDetail);
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
					
					// if all tasks for the quote  is completed, then consider the quote is completed in mf and send to queue
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
						saveMfResponseDetailMQ(quoteId, allSiteStatusesOfQuote, task.getId(),task.getServiceType());
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error in fetching response details {}", e.getMessage());
			}
		}
			try {
				
				// Update task closed status to MfDetail
				mfDetail.setStatus(TaskStatusConstants.CLOSED_STATUS);
				mfDetail.setIsActive(CommonConstants.INACTIVE);
				mfDetailRepository.save(mfDetail);

			} catch (IllegalArgumentException e) {
				LOGGER.error("MF response process failed ", e);
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
	
	/**
	 * 
	 * This function is used to fetch response Details for given siteId 3d maps
	 * 
	 * @param <CreateResponseBean>
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public PreMfResponseBean get3DMfTaskResponse(Integer taskid) throws TclCommonException {
		LOGGER.info("Fetch response from DB into fetch3DMfTaskResponse");
		PreMfResponseBean preMfResponseBean = new PreMfResponseBean();
		try {
			List<Mf3DTaskResponse> responseList = new ArrayList<Mf3DTaskResponse>();
			Optional<Task> task = taskRepository.findById(taskid);
			if (task.isPresent()) {
				preMfResponseBean.setFeasibilityCode(task.get().getFeasibilityId());
				if (task.get().getMfDetail() != null) {
					if (task.get().getMfDetail().getMfDetails() != null) {
						try {
							MfDetailAttributes attribute = Utils.convertJsonToObject(
									task.get().getMfDetail().getMfDetails(), MfDetailAttributes.class);
							preMfResponseBean.setCustomerName(attribute.getCustomerName());
							String siteAddress = (attribute.getAddressLineOne() != null ? attribute.getAddressLineOne()
									: "") + " "
									+ (attribute.getAddressLineTwo() != null ? attribute.getAddressLineTwo() : "") + " "
									+ (attribute.getCity() != null ? attribute.getCity() : "") + " "
									+ (attribute.getState() != null ? attribute.getState() : "") + " "
									+ (attribute.getCountry() != null ? attribute.getCountry() : "") + " "
									+ (attribute.getPincode() != null ? attribute.getPincode() : "");
							preMfResponseBean.setSiteAddress(siteAddress);
						} catch (TclCommonException e) {
							LOGGER.warn("Exception in converting string to MfDetailAttributes bean" + e);
						}

					}
				}
				List<PreMfResponse> mfResponseList = preMfResponseRepository.findBytaskId(taskid);
				if (!mfResponseList.isEmpty()) {
					mfResponseList.stream().forEach(res -> {
						Mf3DTaskResponse mf3DTaskResponse = new Mf3DTaskResponse();
						mf3DTaskResponse.setFeasibilityMode(res.getFeasibilityMode());
						mf3DTaskResponse.setFeasibilityStatus(res.getFeasibilityStatus());
						mf3DTaskResponse.setFeasibilityType(res.getFeasibilityType());
						mf3DTaskResponse.setId(res.getId());
						mf3DTaskResponse.setIsSelected(res.getIsSelected());
						mf3DTaskResponse.setProvider(res.getProvider());
						mf3DTaskResponse.setType(res.getType());
						mf3DTaskResponse.setCreatedResponse(res.getCreateResponseJson());
						JSONParser jsonParser = new JSONParser();
						try {
							JSONObject dataEnvelopeObj = (JSONObject) jsonParser.parse(res.getCreateResponseJson());

							if (dataEnvelopeObj.get("Mast_3KM_avg_mast_ht") != null) {
								mf3DTaskResponse.setMastHeight(getCharges(dataEnvelopeObj.get("Mast_3KM_avg_mast_ht")));
							}

							mf3DTaskResponse.setTypeOfLm("Single");

							if (res.getFeasibilityType().equals("Offnet Wireline")) {

								// otc charges =OTC Modem Charges +OTC/NRC - Installation
								if (dataEnvelopeObj.get("lm_otc_modem_charges_offwl") != null
										|| dataEnvelopeObj.get("lm_otc_nrc_installation_offwl") != null) {
									Double otcCharges = getCharges(dataEnvelopeObj.get("lm_otc_modem_charges_offwl"))
											+ +getCharges(dataEnvelopeObj.get("lm_otc_nrc_installation_offwl"));
									mf3DTaskResponse.setOtcNrcInstallation(otcCharges);
								}
								// arc charges = ARC - BW + arc _modem charges
								if (dataEnvelopeObj.get("lm_arc_modem_charges_offwl") != null
										|| dataEnvelopeObj.get("lm_arc_bw_offwl") != null) {
									mf3DTaskResponse
											.setArcBw(getCharges(dataEnvelopeObj.get("lm_arc_modem_charges_offwl"))
													+ getCharges(dataEnvelopeObj.get("lm_arc_bw_offwl")));
								}

							}
							// ================================================================================
							// offnet wireless
							// ================================================================================

							else if (res.getFeasibilityType().equals("Offnet Wireless")) {

								if (dataEnvelopeObj.get("lm_nrc_mast_ofrf") != null
										|| dataEnvelopeObj.get("lm_nrc_bw_prov_ofrf") != null) {

									// otc charges = Mast Charges + OTC/NRC - Installation
									Double otcCharges = getCharges(dataEnvelopeObj.get("lm_nrc_mast_ofrf"))
											+ +getCharges(dataEnvelopeObj.get("lm_nrc_bw_prov_ofrf"));
									mf3DTaskResponse.setOtcNrcInstallation(otcCharges);
									mf3DTaskResponse.setMastCost(getCharges(dataEnvelopeObj.get("lm_nrc_mast_ofrf")));
								}
								if (dataEnvelopeObj.get("lm_arc_bw_prov_ofrf") != null) {
									// arc charges = ARC - BW + ARC Modem Charges
									mf3DTaskResponse.setArcBw(getCharges(dataEnvelopeObj.get("lm_arc_bw_prov_ofrf")));
								}

							}

							// ================================================================================
							// MAN VBL
							// ================================================================================

							else if (res.getFeasibilityType().equals("MAN/VBL")) {

								if (dataEnvelopeObj.get("lm_nrc_bw_onwl") != null
										|| dataEnvelopeObj.get("lm_nrc_prow_onwl") != null) {

									// otc charges =OTC/NRC - Installation + prov value otc
									Double otcinstal = getCharges(dataEnvelopeObj.get("lm_nrc_bw_onwl"));
									Double prov = getCharges(dataEnvelopeObj.get("lm_nrc_prow_onwl"));
									mf3DTaskResponse.setOtcNrcInstallation(otcinstal + prov);
								}
								if (dataEnvelopeObj.get("lm_nrc_nerental_onwl") != null
										|| dataEnvelopeObj.get("lm_arc_bw_onwl") != null
										|| dataEnvelopeObj.get("lm_arc_prow_onwl") != null) {
									// arc charges = ARC - LRC/NE Rental + ARC - BW + PROW value ARC
									mf3DTaskResponse.setArcBw(getCharges(dataEnvelopeObj.get("lm_nrc_nerental_onwl"))
											+ getCharges(dataEnvelopeObj.get("lm_arc_bw_onwl"))
											+ getCharges(dataEnvelopeObj.get("lm_arc_prow_onwl")));
								}

							}
							// ================================================================================
							// RADWIN
							// ================================================================================

							else if (res.getFeasibilityType().equals("RADWIN")) {
								if (dataEnvelopeObj.get("lm_nrc_bw_onrf") != null
										|| dataEnvelopeObj.get("lm_nrc_mast_onrf") != null) {

									// otc charges =OTC/NRC - Installation + Mast Charges
									Double otcCharges = getCharges(dataEnvelopeObj.get("lm_nrc_bw_onrf"))
											+ getCharges(dataEnvelopeObj.get("lm_nrc_mast_onrf"));
									mf3DTaskResponse.setOtcNrcInstallation(otcCharges);
									mf3DTaskResponse.setMastCost(getCharges(dataEnvelopeObj.get("lm_nrc_mast_onrf")));
								}
								if (dataEnvelopeObj.get("lm_arc_bw_onrf") != null
										|| dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf") != null
										|| dataEnvelopeObj.get("lm_arc_converter_charges_onrf") != null
										|| dataEnvelopeObj.get("lm_arc_colocation_onrf") != null) {
									// arc charges = ARC-Radwin(BW) + ARC - BW + ARC convertor charges + colocation
									// charges
									mf3DTaskResponse.setArcBw(getCharges(dataEnvelopeObj.get("lm_arc_bw_onrf"))
											+ getCharges(dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf"))
											+ getCharges(dataEnvelopeObj.get("lm_arc_converter_charges_onrf"))
											+ getCharges(dataEnvelopeObj.get("lm_arc_colocation_onrf")));
									mf3DTaskResponse.setArcConverterCharges(
											getCharges(dataEnvelopeObj.get("lm_arc_converter_charges_onrf")));
								}

							}

							// ================================================================================
							// UBR PMP / WiMax
							// ================================================================================

							else if (res.getFeasibilityType().equals("UBR PMP/WiMax")) {

								if (dataEnvelopeObj.get("lm_nrc_bw_onrf") != null
										|| dataEnvelopeObj.get("lm_nrc_mast_onrf") != null) {
									// otc charges =OTC/NRC - Installation + Mast Charges
									Double otcCharges = getCharges(dataEnvelopeObj.get("lm_nrc_bw_onrf"))
											+ getCharges(dataEnvelopeObj.get("lm_nrc_mast_onrf"));
									mf3DTaskResponse.setOtcNrcInstallation(otcCharges);
									mf3DTaskResponse.setMastCost(getCharges(dataEnvelopeObj.get("lm_nrc_mast_onrf")));
								}
								if (dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf") != null
										|| dataEnvelopeObj.get("lm_arc_converter_charges_onrf") != null) {
									// arc charges = ARC - BW + arc convertor charges
									mf3DTaskResponse.setArcBw(getCharges(dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf"))
											+ getCharges(dataEnvelopeObj.get("lm_arc_converter_charges_onrf")));
									mf3DTaskResponse.setArcConverterCharges(
											getCharges(dataEnvelopeObj.get("lm_arc_converter_charges_onrf")));

								}

							}

						} catch (ParseException e) {
							LOGGER.warn("Exception in json conversation" + e);
						}
						responseList.add(mf3DTaskResponse);
					});
				}
				preMfResponseBean.setTaskResponse(responseList);
			}

		} catch (Exception e) {
			LOGGER.warn("Exception in getting response 3d maps" + e);
		}

		return preMfResponseBean;
	}
	 
	/**
	 * 
	 * This function is used to get the list of task for the given userName 3d maps
	 * 
	 * @param userName
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public List<Mf3DDashboardbean> get3DMfDashboardTasks(String userName) throws TclCommonException {
		LOGGER.info("Entered into get3DMfDashboardTasks" + userName);
		List<Mf3DDashboardbean> dashboardbean = new ArrayList<Mf3DDashboardbean>();
		try {
			if (userName.equalsIgnoreCase(Utils.getSource())) {
				List<MfDetail> mfDetail = mfDetailRepository.findByCreatedByAndIsPreMfTaskOrderByIdDesc(userName, "1");
				LOGGER.info("3DMfDashboardTasks mfDetail size" + mfDetail.size());
				if (!mfDetail.isEmpty()) {
					mfDetail.stream().forEach(detail -> {
						String pattern = "MM-dd-yyyy";
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
						String date = simpleDateFormat.format(detail.getCreatedTime());
						List<Task> taskList = taskRepository.findByMfDetail(detail);

						if (!taskList.isEmpty()) {
							taskList.stream().forEach(task -> {
								if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)
										|| task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP)) {
									Mf3DDashboardbean mf3DDashboardbean = new Mf3DDashboardbean();
									mf3DDashboardbean.setCreatedDate(date);
									mf3DDashboardbean.setProductName(task.getServiceType());
									mf3DDashboardbean.setQuoteCode(task.getQuoteCode());
									mf3DDashboardbean.setQuoteId(String.valueOf(task.getQuoteId()));
									mf3DDashboardbean.setSiteCode(task.getSiteCode());
									mf3DDashboardbean.setSiteId(String.valueOf(task.getSiteId()));
									mf3DDashboardbean.setFeasibilityId(task.getFeasibilityId());
									mf3DDashboardbean.setStatus(task.getMstStatus().getCode());
									mf3DDashboardbean.setTaskId(task.getId());
									List<PreMfResponse> responseList = preMfResponseRepository
											.findBytaskId(task.getId());
									List<String> resList = new ArrayList<String>();
									if (!responseList.isEmpty()) {
										responseList.stream().forEach(res -> {
											if (res.getFeasibilityMode() != null) {
												resList.add(res.getFeasibilityMode());
											}
										});
										mf3DDashboardbean.setLmTypes(resList);
									}
									MfTaskDetail taskDetail=mfTaskDetailRepository.findByTaskId(task.getId());
									if(taskDetail!=null) {
										mf3DDashboardbean.setComments(taskDetail.getResponderComments());
									}

									dashboardbean.add(mf3DDashboardbean);
								}
							});
							
						}
					});

				}

			}

		} catch (Exception e) {
			LOGGER.warn("Exception in get dashboard list" + e);
		}
		LOGGER.info("3DMfDashboardTasks list size" + dashboardbean.size());
		return dashboardbean;

	}
	

	
	public void saveMfResponseDetailMQ(Integer quoteId, Map<Integer, String> allSiteStatusesOfQuote, Integer id,
			String serviceType) {
		
    List<Mf3DSiteStatus> status = new ArrayList<Mf3DSiteStatus>();
			try {
				allSiteStatusesOfQuote.forEach((k,v) ->{
					List<PreMfResponse> responses = preMfResponseRepository.findBySiteId(k);
					Optional<PreMfResponse> feasibleResponse = responses.stream().filter( x -> x.getFeasibilityStatus()!=null)
					.filter(y -> y.getFeasibilityStatus().startsWith("Feasible")).findFirst();
					
					if(feasibleResponse.isPresent()) {
						Mf3DSiteStatus individualSiteStatus = new Mf3DSiteStatus();
						individualSiteStatus.setSiteId(k);
						individualSiteStatus.setFeasibility(1);
						individualSiteStatus.setSiteStatus("F");
						status.add(individualSiteStatus);

					}else {
						
						Mf3DSiteStatus individualSiteStatus = new Mf3DSiteStatus();
						individualSiteStatus.setSiteId(k);
						individualSiteStatus.setFeasibility(0);
						individualSiteStatus.setSiteStatus("N");
						status.add(individualSiteStatus);
					}
					
				});
				
				
				String request = Utils.convertObjectToJson(status);
				LOGGER.info(
						"MDC Filter token value in before Queue call saving mf response details in site feasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(savePreMFResponseInOmsMQ, request);
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.SAVE_MF_RESPONSE_IN_OMS_MQ_ERROR, e,
						ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}
		} 
	
}
