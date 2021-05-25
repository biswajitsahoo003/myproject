package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


import com.tcl.dias.servicefulfillment.entity.entities.CpeDeviceNameDetail;
import com.tcl.dias.servicefulfillment.entity.repository.CpeDeviceNameDetailRepository;

import com.tcl.dias.common.constants.TeamsDROdrConstants;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillmentutils.beans.teamsdr.*;
import com.tcl.dias.servicefulfillmentutils.constants.TeamsDRFulfillmentConstants;

import com.tcl.dias.common.constants.CommonConstants;

import com.tcl.dias.servicefulfillment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.RenewalCommercialVettingDetails;
import com.tcl.dias.servicefulfillmentutils.beans.RenewalAttachmentDetails;
import com.tcl.dias.servicefulfillmentutils.beans.ScTeamsDRServiceCommercialBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDataBean;
import com.tcl.dias.servicefulfillmentutils.beans.teamsdr.TeamsDRMgComponentBean;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
import com.tcl.dias.common.utils.Currencies;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.ForexService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.custom.IScWebexServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.repository.CpeCostDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.CustomerCrnRepository;
import com.tcl.dias.servicefulfillment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstMuxBomTier1Repository;
import com.tcl.dias.servicefulfillment.entity.repository.PlannedEventRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProCreationRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceSlaRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScTeamsDRServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScWebexServiceCommercialRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskDataRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VwBomMaterialDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VwBomMuxDetailsRepository;
import com.tcl.dias.servicefulfillmentutils.abstractservice.ITaskDataService;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.BillingAccountDetail;
import com.tcl.dias.servicefulfillmentutils.beans.BomMaterialDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.CGWDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.ChargeLineItemBean;
import com.tcl.dias.servicefulfillmentutils.beans.CosDetail;
import com.tcl.dias.servicefulfillmentutils.beans.CpeCostDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeInstallationBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeInstallationSupportBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpePrPoBean;
import com.tcl.dias.servicefulfillmentutils.beans.CpeVendorBean;
import com.tcl.dias.servicefulfillmentutils.beans.EndpointMaterialsBean;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetails;
import com.tcl.dias.servicefulfillmentutils.beans.LineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.MaterialMasterBean;
import com.tcl.dias.servicefulfillmentutils.beans.MstAppointmentDocumentBean;
import com.tcl.dias.servicefulfillmentutils.beans.MstCatalogueBean;
import com.tcl.dias.servicefulfillmentutils.beans.OverlayBean;
import com.tcl.dias.servicefulfillmentutils.beans.PlannedEventBean;
import com.tcl.dias.servicefulfillmentutils.beans.ScWebexServiceCommercialBean;
import com.tcl.dias.servicefulfillmentutils.beans.SolutionBean;
import com.tcl.dias.servicefulfillmentutils.beans.UnderlayBean;
import com.tcl.dias.servicefulfillmentutils.beans.UnderlayDetails;
import com.tcl.dias.servicefulfillmentutils.beans.UseCaseDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.VwMuxBomDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DocumentBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited TaskDataService is used to get
 * the task details
 */

@Service
@Transactional(readOnly = true)
public class TaskDataService implements ITaskDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDataService.class);

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    AttributeService attributeService;

    @Autowired
    TaskPlanRepository taskPlanRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    PlannedEventRepository plannedEventRepository;

    @Autowired
    RuntimeService runtimeService;
    
    @Autowired
    ScServiceAttributeRepository scServiceAttributeRepository;
    
    @Autowired
    VwBomMaterialDetailRepository vwBomMaterialDetailRepository;
    
    @Autowired
    VwBomMuxDetailsRepository vwBomMuxDetailsRepository;
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;
    
    @Autowired
    ScComponentRepository scComponentRepository;
    
    @Autowired
    ScComponentAttributesRepository scComponentAttributesRepository;
    
    @Autowired
    ScServiceCommercialRepository scServiceCommercialRepository;
    
    @Autowired
    CustomerCrnRepository crnRepository;
	
    @Autowired
    MstMuxBomTier1Repository mstMuxBomTier1Repository;
    
    @Autowired
    BillingChargeLineItemService billingChargeLineItemService;
    
    @Autowired
    UcaasBillingChargeLineItemService ucaasBillingChargeLineItemService;
    
    @Autowired
	ScServiceSlaRepository scServiceSlaRepository;
    
    @Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;

    @Autowired
	CpeCostDetailsRepository cpeCostDetailsRepository;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
	
	@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;
	
	@Autowired
	ForexService forexService;
	
	@Autowired
	IpcBillingChargeLineItemService ipcBillingChargeLineItemService;
	
	@Autowired
	ScWebexServiceCommercialRepository  scWebexServiceCommercialRepository;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Autowired
	ProCreationRepository proCreationRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	FieldEngineerRepository fieldEngineerRepository;

	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	BillingSdwanChargeLineItemService billingSdwanChargeLineItemService; 
	
	@Autowired
	MQUtils mqUtils;
	
	@Value("${queue.serviceactivation.handovernote}")
	String servicehandovernote_queue;

	@Autowired
	ScAttachmentRepository scAttachmentRepository;

	@Autowired
	CpeDeviceNameDetailRepository cpeDeviceNameDetailRepository;
	
	@Autowired
	protected TaskDataRepository taskDataRepository;

	@Autowired
	TeamsDRBillingChargeLineItemService teamsDRBillingChargeLineItemService;

	@Autowired
	TeamsDRService teamsDRService;

	@Autowired
	ScTeamsDRServiceCommercialRepository scTeamsDRServiceCommercialRepository;
	
	@Value("${queue.serviceactivation.vpnsolution}")
	String vpnsolution_queue;
	
	@Autowired
	private GscFlowGroupRepository gscFlowGroupRepository;
	
	@Autowired
	private ComponentAndAttributeService componentAndAttributeService;
	
	@Override
	public Map<String, Object> getTaskData(Task task) throws TclCommonException {
		LOGGER.info("get Task Data method started with Key {} ", task.getMstTaskDef().getKey());
		Integer serviceId = task.getServiceId();
		Map<String, Object> taskDataMap = attributeService.getTaskAttributes(task.getMstTaskDef().getKey(), serviceId,task.getSiteType());
		LOGGER.info("Variable map has values ; {} ", taskDataMap);
		
		
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("lm-install-mux")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("lm-create-mux-mrn")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("lm-deliver-mux")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("install-rf-equipment")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("lm-create-rf-mrn")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("deliver-rf-equipment")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("create-osp-inventory-record")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("lm-configure-mux")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("enrich-service-design-jeopardy")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("enrich-service-design-ip-jeopardy")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("enrich-service-design-tx-jeopardy")) {

			constructBomDetails(task, taskDataMap);
		}
		
		getFileEngineerDetails(task,taskDataMap);
		
		if (task.getOrderType() != null && task.getOrderType().equalsIgnoreCase("MACD") 
				&& null != task.getOrderCode() && !task.getOrderCode().startsWith(IpcConstants.IPC)) {
			ScServiceDetail prevScServiceDetail = scServiceDetailRepository
					.findFirstByUuidAndMstStatus_codeOrderByIdDesc(task.getServiceCode(), TaskStatusConstants.ACTIVE);
			if (prevScServiceDetail != null) {
				ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								prevScServiceDetail.getId(), "programName", "LM",
								task.getSiteType() != null ? task.getSiteType() : "A");

				if (scComponentAttribute != null && scComponentAttribute.getAttributeValue() != null) {
					taskDataMap.put("prevProgramName", scComponentAttribute.getAttributeValue());
				}

			}
		}
		
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-order-creation-repc-jeopardy")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-order-creation-repc-jeopardy")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-patch-jeopardy")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-patch-jeopardy")) {
			Optional<GscFlowGroup> gscFlowgroupOptional = gscFlowGroupRepository.findById(task.getGscFlowGroupId());

			if (gscFlowgroupOptional.isPresent()) {

				String errorMsg = null;

				if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-order-creation-repc-jeopardy")
						|| task.getMstTaskDef().getKey()
								.equalsIgnoreCase("did-new-number-order-creation-repc-jeopardy")) {
					errorMsg = componentAndAttributeService.getFlowGruopAdditionalParam("repcCallFaliure",
							task.getGscFlowGroupId(), task.getServiceId());
				} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("did-porting-number-patch-jeopardy")
						|| task.getMstTaskDef().getKey().equalsIgnoreCase("did-new-number-patch-jeopardy")) {
					errorMsg = componentAndAttributeService.getFlowGruopAdditionalParam("patchCallFaliure",
							task.getGscFlowGroupId(), task.getServiceId());

				}

				taskDataMap.put("errorMessage", errorMsg);
			}

		}
		

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("lm-rf-conduct-site-survey")) {
			String localLoopBandwidth = StringUtils.trimToEmpty((String)taskDataMap.get("localLoopBandwidth"));

			String scenarioType = StringUtils.trimToEmpty((String)taskDataMap.get("wirelessScenario"));
			LOGGER.info("scenarioType : {}",scenarioType);
			processRfBomDetails(localLoopBandwidth, taskDataMap, scenarioType);
		
		}else if (task.getMstTaskDef().getKey().equalsIgnoreCase("lm-conduct-site-survey") 
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("lm-conduct-site-survey-man")||
				 task.getMstTaskDef().getKey().equalsIgnoreCase("lm-complete-osp-work") ||
				 task.getMstTaskDef().getKey().equalsIgnoreCase("complete-osp-acceptance-testing")||
				 task.getMstTaskDef().getKey().equalsIgnoreCase("lm-install-mux")||
				 task.getMstTaskDef().getKey().equalsIgnoreCase("lm-integrate-mux") ||
				 task.getMstTaskDef().getKey().equalsIgnoreCase("lm-complete-ibd-work")) {
			String localLoopBandwidth = StringUtils.trimToEmpty((String) taskDataMap.get("localLoopBandwidth"));
			processMuxBomDetails(localLoopBandwidth, taskDataMap);

			String ospWorkRequired = "No";
			if (taskDataMap.containsKey("ospDistance")) {
				String ospDistance = StringUtils.trimToEmpty(String.valueOf(taskDataMap.get("ospDistance")));
				if (StringUtils.isNotBlank(ospDistance)) {
					Double ospDistanceInt = Double.valueOf(ospDistance);
					if (ospDistanceInt > 0) {
						ospWorkRequired = "Yes";
					}
				}
			}
			taskDataMap.put("ospWorkRequired", ospWorkRequired);
		}else if (task.getMstTaskDef().getKey().startsWith("arrange-field-engineer")
				|| task.getMstTaskDef().getKey().startsWith("customer-appointment")
				|| task.getMstTaskDef().getKey().startsWith("select-vendor")
				|| task.getMstTaskDef().getKey().startsWith("sdwan-customer-appointment")
				|| task.getMstTaskDef().getKey().startsWith("sdwan-arrange-field-engineer")) {
			processMstAppointmentDocuments(task, taskDataMap, getTaskType(task.getMstTaskDef().getKey()));
		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("lm-confirm-site-readiness-details")) {
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "lm-deliver-mux", task.getSiteType()==null?"A":task.getSiteType());
			if (taskPlan != null)taskDataMap.put("tentativeDate", taskPlan.getPlannedStartTime());
		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("advanced-enrichment")
				||(task.getMstTaskDef().getKey().equalsIgnoreCase("advanced-enrichment-rejected")) ) {
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "enrich-service-design", task.getSiteType()==null?"A":task.getSiteType());
			if (taskPlan != null)taskDataMap.put("tentativeDate", taskPlan.getPlannedStartTime());
			if ("GVPN".equals(task.getServiceType())) {
				getCosDatailsAttributes(serviceId, taskDataMap);
				if(task.getScServiceDetail().getMultiVrfSolution()!=null && CommonConstants.Y.equalsIgnoreCase(task.getScServiceDetail().getMultiVrfSolution())){
					LOGGER.info("Inside MultiVrfAttribute block:{}",serviceId);
					getMultiVrfAttributes(serviceId, taskDataMap);
				}
			}
		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("raise-planned-event")) {
			taskDataMap.put("availableSlots", getAvailableSlotsForMux(task.getId()));
		}

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("po-offnet-lm-provider-site2")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("po-offnet-lm-provider")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("negotiate-offnet-lm-provider")) {
			
						
			String lastMile = StringUtils.trimToEmpty((String)taskDataMap.get("lmType")).toLowerCase();
			if(lastMile.contains("wl") || lastMile.contains("wireline")) {
				String nrcwl = StringUtils.trimToEmpty((String)taskDataMap.get("nrcwl"));
				String arcwl = StringUtils.trimToEmpty((String)taskDataMap.get("arcwl"));
				
				taskDataMap.put("nrc", nrcwl);
				taskDataMap.put("arc", arcwl);	
			}
			
			try {
				if(taskDataMap.get("feasibilityResponseType")==null) {
					taskDataMap.put("feasibilityResponseType", "L1");	
				}
				if(taskDataMap.get("supplierDeliveryTimeline")!=null) {
					String supplierDeliveryTimeline = String.valueOf(taskDataMap.get("supplierDeliveryTimeline"));
					taskDataMap.put("supplierDeliveryTimeline",supplierDeliveryTimeline+" Weeks");
				}
				if(taskDataMap.get("validityPeriod")!=null) {
					String validityPeriod = String.valueOf(taskDataMap.get("validityPeriod"));
					taskDataMap.put("validityPeriod",validityPeriod+" Days");
				}else {
					taskDataMap.put("validityPeriod","30 Days");
				}
				
				if(taskDataMap.get("contractTerm")!=null) {
					String contractTerm = String.valueOf(taskDataMap.get("contractTerm"));
					taskDataMap.put("contractTerm",contractTerm);
				}else if(taskDataMap.get("lastMilecontractTerm")!=null) {
					String contractTerm = String.valueOf(taskDataMap.get("lastMilecontractTerm"));
					taskDataMap.put("contractTerm",contractTerm);
				}
				if(taskDataMap.get("vendorName")!=null) {
					taskDataMap.put("offnetProviderName",taskDataMap.get("vendorName"));
				}
				
			}catch(Exception ee) {
				LOGGER.error("Exception in po-offnet-lm-provider {}",ee);
			}
			 
		}
		if(task.getMstTaskDef().getKey().equals("commercial-vetting-termination")) {
			List<LineItemDetailsBean> itemDetailsBeans = billingChargeLineItemService.loadServiceTerminationLineItem(task.getServiceId());
			String accountNumbers = billingChargeLineItemService.getActiveAccounts(task.getServiceId().toString(), task.getServiceType());
			taskDataMap.put("lineItemDetails", itemDetailsBeans);
			taskDataMap.put("activeAccounts", accountNumbers);
		}
		if(task.getMstTaskDef().getKey().equals("product-commissioning-jeopardy-cancellation")) {
			ScChargeLineitem chargeLineitem= chargeLineitemRepository.findFirstByServiceCancellation(task.getServiceId().toString(),task.getServiceType());
			List<LineItemDetailsBean> itemDetailsBeanList = new ArrayList<>();
			ChargeLineItemBean chargeLineitemBean = new ChargeLineItemBean();
			if(chargeLineitem!=null){
				LineItemDetailsBean itemDetailsBean = new LineItemDetailsBean();
				itemDetailsBean.setAccountNumber(chargeLineitem.getAccountNumber());
				itemDetailsBean.setLineitem(chargeLineitem.getChargeLineitem());
				itemDetailsBean.setNrc(chargeLineitem.getNrc());
				itemDetailsBean.setCancellationDate(chargeLineitem.getTermDate());
				itemDetailsBean.setDescription(chargeLineitem.getComponentDesc());
				itemDetailsBeanList.add(itemDetailsBean);
			}
			chargeLineitemBean.setLineItemDetails(itemDetailsBeanList);
			taskDataMap.put("lineItemDetails", itemDetailsBeanList);
		}
		if(task.getMstTaskDef().getKey().equals("commercial-vetting-cancellation")) {
			List<LineItemDetailsBean> itemDetailsBeans = billingChargeLineItemService.loadServiceCancellationLineItem(task.getServiceId());
			taskDataMap.put("lineItemDetails", itemDetailsBeans);
		}
		if(task.getMstTaskDef().getKey().equals("comm-valid-service-termination")) {
			ScChargeLineitem chargeLineitem= chargeLineitemRepository.findFirstByServiceTermination(task.getServiceId().toString(),task.getServiceType());
			List<LineItemDetailsBean> itemDetailsBeanList = new ArrayList<>();
			ChargeLineItemBean chargeLineitemBean = new ChargeLineItemBean();
			if(chargeLineitem!=null){
				LineItemDetailsBean itemDetailsBean = new LineItemDetailsBean();
				itemDetailsBean.setLineitem(chargeLineitem.getChargeLineitem());
				itemDetailsBean.setEtcWaiver(chargeLineitem.getEtcWaiver());
				itemDetailsBean.setEtcCharge(chargeLineitem.getEtcCharge());
				itemDetailsBean.setTerminationDate(chargeLineitem.getTermDate());
				itemDetailsBean.setDescription(chargeLineitem.getComponentDesc());
				itemDetailsBeanList.add(itemDetailsBean);
			}
			chargeLineitemBean.setLineItemDetails(itemDetailsBeanList);
			String accountNumbers = billingChargeLineItemService.getActiveAccounts(task.getServiceId().toString(), task.getServiceType());
			taskDataMap.put("lineItemDetails", itemDetailsBeanList);
			taskDataMap.put("activeAccounts", accountNumbers);

		}

		

		if(task.getMstTaskDef().getKey().equalsIgnoreCase("commercial-vetting")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("cpe-product-commissioning-jeopardy") 
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("comm-valid-billing-issue")|| task.getMstTaskDef().getKey().equalsIgnoreCase("cpe-comm-valid")) {
			List<CustomerCrn> customerCrnDetails = crnRepository.findByCustomerRef((String)taskDataMap.get("customerRef"));
			List<BillingAccountDetail> accountDetails = new ArrayList<>();
			if(Objects.nonNull(customerCrnDetails)) {
				customerCrnDetails.forEach(customerCrn->{
					BillingAccountDetail accountDetail = new BillingAccountDetail();
					accountDetail.setAccountnumber(customerCrn.getAccountNumber());
					accountDetail.setType(customerCrn.getServiceType());
					accountDetails.add(accountDetail);
				});
			}
			CpeBomResource bomResource =  getBomResources(task);
			String billingMethod = (String) taskDataMap.get("billingMethod");
			//AccountInputData accountInputData = billingChargeLineItemService.getAccountInputDetails(task.getServiceId().toString(),task.getOrderCode());
			List<LineItemDetailsBean> itemDetailsBeans = new ArrayList<>();
			if("NPL".equals(task.getServiceType())) {
				itemDetailsBeans = billingChargeLineItemService.loadLineItemsNPL(task.getServiceId(),billingMethod);
			}else if ("BYON Internet".equals(task.getServiceType())) {
				ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
				ScSolutionComponent scSolutionComponent = scSolutionComponentRepository.findByScServiceDetail1(scServiceDetail);
				if (scSolutionComponent != null) {
					taskDataMap.put("overLayServiceUuid", scSolutionComponent.getParentServiceCode());
				}
			}else if ("IZOSDWAN".equals(task.getServiceType()) || "IZO SDWAN".equals(task.getServiceType())) {
				itemDetailsBeans = billingSdwanChargeLineItemService.loadSdwanLineItems(task.getServiceId(),
						billingMethod);
			}else if("IZOSDWAN_CGW".equals(task.getServiceType())) {
				itemDetailsBeans = billingSdwanChargeLineItemService.loadCgwLineItems(task.getServiceId(),billingMethod);
			}else if("UCAAS".equals(task.getServiceType())) {
				itemDetailsBeans = ucaasBillingChargeLineItemService.loadLineItems(task.getServiceId(), billingMethod);
			}else if(CommonConstants.MICROSOFT_CLOUD_SOLUTIONS.equals(task.getServiceType())) {
				itemDetailsBeans = teamsDRBillingChargeLineItemService.loadLineItems(task.getServiceId(), billingMethod);
			}
			else {
				itemDetailsBeans = billingChargeLineItemService.loadLineItems(task.getServiceId(),bomResource,billingMethod);
			}
			
			if (itemDetailsBeans != null) {
				Double totalNrc = itemDetailsBeans.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getNrc())));
				Double totalArc = itemDetailsBeans.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getArc())));
				Double totalMrc = itemDetailsBeans.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getMrc())));

				taskDataMap.put("totalArc", String.format("%.2f",totalArc));
				taskDataMap.put("totalNrc", String.format("%.2f",totalNrc));
				taskDataMap.put("totalMrc", String.format("%.2f",totalMrc));
			}
			
			taskDataMap.put("accountnumberList", accountDetails);
			taskDataMap.put("lineItemDetails", itemDetailsBeans);
			
			if ("GVPN".equals(task.getServiceType()) || "IAS".equals(task.getServiceType())) {
				if (taskDataMap.get("localLoopBandwidth").equals("0.125")) {
					taskDataMap.put("localLoopBandwidth", "128");
					taskDataMap.put("localLoopBandwidthUnit", "Kbps");
				}
				if (taskDataMap.get("localLoopBandwidth").equals("0.25")) {
					taskDataMap.put("localLoopBandwidth", "256");
					taskDataMap.put("localLoopBandwidthUnit", "Kbps");
				}
				if (taskDataMap.get("localLoopBandwidth").equals("0.5")) {
					taskDataMap.put("localLoopBandwidth", "512");
					taskDataMap.put("localLoopBandwidthUnit", "Kbps");
				}

				LOGGER.info("Local loop Bandwidth is {} Uom {} ", taskDataMap.get("localLoopBandwidth"),
						taskDataMap.get("localLoopBandwidthUnit"));

				if (taskDataMap.get("portBandwidth").equals("0.125")) {
					taskDataMap.put("portBandwidth", "128");
					taskDataMap.put("bwUnit", "Kbps");
				}
				if (taskDataMap.get("portBandwidth").equals("0.25")) {
					taskDataMap.put("portBandwidth", "256");
					taskDataMap.put("bwUnit", "Kbps");
				}
				if (taskDataMap.get("portBandwidth").equals("0.5")) {
					taskDataMap.put("portBandwidth", "512");
					taskDataMap.put("bwUnit", "Kbps");
				}
				LOGGER.info("Port Bandwidth is {} Uom {} ", taskDataMap.get("portBandwidth"),
						taskDataMap.get("bwUnit"));

			}

			ScServiceDetail vrfService = scServiceDetailRepository.findByUuidAndMstStatus_code(task.getServiceCode(),TaskStatusConstants.INPROGRESS);
			if (vrfService != null
					&& (vrfService.getMultiVrfSolution() != null && "Y".equals(vrfService.getMultiVrfSolution()))) {
				if (vrfService.getIsMultiVrf() != null && "N".equals(vrfService.getIsMultiVrf())
						&& vrfService.getMasterVrfServiceId() != null) {
					ScServiceDetail masterVrfServiceCode = scServiceDetailRepository.findById(vrfService.getMasterVrfServiceId()).get();
					if (masterVrfServiceCode != null) {
						taskDataMap.put("masterVrfServiceId", masterVrfServiceCode.getUuid());
					}
				}
			}
		}
		
		if(task.getMstTaskDef().getKey().startsWith("ipc")) {
			Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository.findById(task.getServiceId());
			ScServiceDetail ipcServiceDetail = optionalServiceDetails.get();
			taskDataMap.put("siteAddress", ipcServiceDetail.getSourceCity() + IpcConstants.SPECIAL_CHARACTER_COMMA 
					+ IpcConstants.SINGLE_SPACE + ipcServiceDetail.getSourceState() + IpcConstants.SPECIAL_CHARACTER_COMMA 
					+ IpcConstants.SINGLE_SPACE +optionalServiceDetails.get().getSourceCountry());
			taskDataMap.put("siteAddressFull", ipcServiceDetail.getSiteAddress());
			taskDataMap.put("serviceSubType", "Non-Standard");
			taskDataMap.put("serviceComponentType", "Single DC Cloud Access");
			taskDataMap.put("feasibilityRemarks", "OK");
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("ipc-commercial-vetting") || task.getMstTaskDef().getKey().equalsIgnoreCase("ipc-comm-valid") || task.getMstTaskDef().getKey().equalsIgnoreCase("ipc-tax-capture")) {
			List<LineItemDetailsBean> ipcItemDetailsBeans = ipcBillingChargeLineItemService.loadLineItems(true, task.getId());
			taskDataMap.put("lineItemDetails", ipcItemDetailsBeans);
			if (IpcConstants.MACD.equals(task.getOrderType()) && IpcConstants.DELETE_VM.equals(task.getOrderCategory())) {
				taskDataMap.put("deleteLineItemDetails", ipcBillingChargeLineItemService.loadDeletedLineItems(task.getId()));
			}
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("ipc-delete-vm")) {
			taskDataMap.put("deleteLineItemDetails", ipcBillingChargeLineItemService.formatAndFrameFinalIpcLineItemsForGSMCTask(task.getId()));
		}
		
		if (task.getMstTaskDef().getKey().equalsIgnoreCase(IpcConstants.MST_TASK_ATTRIBUTES_IPC_PROJECT_IMPLEMENTATION)) {
			List<Task> ipcProcurementTaskL = taskRepository.findByServiceIdAndMstTaskDef_keyIn(task.getServiceId(), Arrays.asList(IpcConstants.MST_TASK_ATTRIBUTES_IPC_PROCUREMENT));
			if (ipcProcurementTaskL.isEmpty()) {
				taskDataMap.put(IpcConstants.IS_PROCUREMENT_TASK_ALREADY_TRIGGERRED, false);
			} else {
				taskDataMap.put(IpcConstants.IS_PROCUREMENT_TASK_ALREADY_TRIGGERRED, true);
			}
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("service-acceptance") 
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("deemed-service-acceptance")
				|| task.getMstTaskDef().getKey().contains("service-handover")) {
			getSlaValues(serviceId,taskDataMap);
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("service-acceptance")
					&& taskDataMap.containsKey("productName") && taskDataMap.get("productName").toString().equalsIgnoreCase("IZOPC")) {
				taskDataMap.put("productName", "GVPN -  IZO Private Connect");
			}
		}
		
		
		
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("dispatch-cpe")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("pr-for-cpe-order")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("po-for-cpe-order")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("po-release-for-cpe-order")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("pr-for-cpe-order-manual")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("po-for-cpe-order-manual")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("po-release-for-cpe-order-manual")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("generate-mrn-cpe-transfer")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("provide-wbsglcc-details")				
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("confirm-material-availability")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("pr-cpe-installation")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("po-cpe-installation") 				 
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("po-release-cpe-installation")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("raise-sto")) {
			getmastCatalogueDetails(task, taskDataMap);
			getBillingDate(task,taskDataMap);
			

	
		}
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-arrange-field-engineer-cpe-installation")){
			LOGGER.info("Task:: {} ",task.getMstTaskDef().getKey());
			List<ScComponent> scComponentList=scComponentRepository.findByScServiceDetailId(task.getServiceId());
			taskDataMap.put("cpeDetails", null);
			if(scComponentList!=null && !scComponentList.isEmpty()){
				List<CpeInstallationSupportBean> cpeInstallationSupportBeanList= new ArrayList<>();
				for(ScComponent scComponent:scComponentList){
					Map<String, String> scComponentAttributesMap =commonFulfillmentUtils.getComponentAttributes(task.getServiceId(), AttributeConstants.COMPONENT_LM, scComponent.getSiteType());
					LOGGER.info("scComponentAttributesMap {} ",scComponentAttributesMap);
					CpeInstallationSupportBean cpeInstallationSupportBean = new CpeInstallationSupportBean();
					cpeInstallationSupportBean.setInstallationPONumber(StringUtils.trimToEmpty(scComponentAttributesMap.get("cpeInstallationPoNumber")));
					cpeInstallationSupportBean.setInstallationVendorEmailId(StringUtils.trimToEmpty(scComponentAttributesMap.get("cpeInstallationPrVendorEmailId")));
					cpeInstallationSupportBean.setInstallationVendorName(StringUtils.trimToEmpty(scComponentAttributesMap.get("cpeInstallationPrVendorName")));
					cpeInstallationSupportBean.setSerialNumber(StringUtils.trimToEmpty(scComponentAttributesMap.get("cpeSerialNumber")));
					cpeInstallationSupportBean.setSupportPONumber(StringUtils.trimToEmpty(scComponentAttributesMap.get("cpeSupportPoNumber")));
					if(scComponentAttributesMap.containsKey("CPE Basic Chassis") && scComponentAttributesMap.get("CPE Basic Chassis")!=null
							&& !scComponentAttributesMap.get("CPE Basic Chassis").isEmpty()) {
						LOGGER.info("CPE Basis Chassis exists::{}",scComponentAttributesMap.get("CPE Basic Chassis"));
						Optional<ScAdditionalServiceParam> scAddParam = scAdditionalServiceParamRepository
								.findById(Integer.valueOf(scComponentAttributesMap.get("CPE Basic Chassis")));
						if (scAddParam.isPresent()) {
							LOGGER.info("CPE Basis Chassis param exists::{}",scAddParam.get().getValue());
							cpeInstallationSupportBean.setCpeBomDetails(scAddParam.get().getValue());
						}
					}
					cpeInstallationSupportBeanList.add(cpeInstallationSupportBean);
				}
				LOGGER.info("CpeInstallationSupportBeanList size:: {} ",cpeInstallationSupportBeanList.size());
				taskDataMap.put("cpeDetails", cpeInstallationSupportBeanList);
			}
		}

		
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-provide-wbsglcc-details")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-pr-cpe-installation")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-po-cpe-installation")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-po-release-cpe-installation")) {
			LOGGER.info("TaskId::{},OrderCode::{}",task.getServiceId(),task.getOrderCode());
			ScComponent scComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(task.getServiceId(), "LM", task.getSiteType());
			taskDataMap.put("cpeComponentId", scComponent.getId());
			getNodeName(scComponent.getId(),"cpeDeviceName",taskDataMap,"cpeDeviceName");
			getNodeName(scComponent.getId(),"CPE",taskDataMap,"cpeContractType");
			getCpeCostDetails(task, taskDataMap,scComponent.getId(),null);
			Optional<ScServiceDetail> scServiceDetailOptional= scServiceDetailRepository.findById(task.getServiceId());
			if(scServiceDetailOptional.isPresent() && scServiceDetailOptional.get().getDestinationCountry()!=null && scServiceDetailOptional.get().getDestinationCountry().equalsIgnoreCase("India")){
				LOGGER.info("Site is Domestic ::{}",task.getServiceId());
				getSdwanBillingDate(task,taskDataMap);
			}else{
				LOGGER.info("Site is International ::{}",task.getServiceId());
				getNodeName(scComponent.getId(),"cpeBillStartDate",taskDataMap,"cpeBillStartDate");
			}
		}
		
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-pr-for-cpe-order")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-po-for-cpe-order")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-po-release-for-cpe-order")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-confirm-material-availability")) {
			LOGGER.info("TaskId::{},VendorCode::{},VendorName::{}", task.getId(), task.getVendorCode(),task.getVendorName());
			taskDataMap.put("vendorCode", task.getVendorCode());
			taskDataMap.put("vendorName", task.getVendorName());
			ScComponent scComponent = scComponentRepository
					.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(task.getServiceId(), "LM",
							task.getSiteType());
			taskDataMap.put("cpeComponentId", scComponent.getId());
			getNodeName(scComponent.getId(), "cpeDeviceName", taskDataMap, "cpeDeviceName");
			getNodeName(scComponent.getId(), "CPE", taskDataMap, "cpeContractType");
			getCpeCostDetails(task, taskDataMap, scComponent.getId(), task.getVendorCode());
			Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetailOptional.isPresent() && scServiceDetailOptional.get().getDestinationCountry() != null
					&& scServiceDetailOptional.get().getDestinationCountry().equalsIgnoreCase("India")) {
				LOGGER.info("Site is Domestic ::{}", task.getServiceId());
				getSdwanBillingDate(task, taskDataMap);
			} else {
				LOGGER.info("Site is International ::{}", task.getServiceId());
				getNodeName(scComponent.getId(), "cpeBillStartDate", taskDataMap, "cpeBillStartDate");
			}
			LOGGER.info("TaskId::{},VendorCode::{}", task.getId(), task.getVendorCode());
			List<String> cpeTypeList = new ArrayList<>();
			cpeTypeList.add("HARDWARE");
			cpeTypeList.add("LICENCE");
			taskDataMap.put("cpeSupplyHardwarePrNumber", null);
			taskDataMap.put("cpeSupplyHardwarePrVendorName", null);
			taskDataMap.put("cpeSupplyHardwarePrDate", null);
			taskDataMap.put("cpeSupplyHardwarePoNumber", null);
			taskDataMap.put("cpeSupplyHardwarePoDate", null);
			taskDataMap.put("cpeSupplyHardwarePoStatus", null);
			taskDataMap.put("cpeSupplyHardwarePrStatus", null);
			taskDataMap.put("cpeSupplyHardwarePrType", null);
			taskDataMap.put("cpeLicencePrNumber", null);
			taskDataMap.put("cpeLicenseVendorName", null);
			taskDataMap.put("cpeLicencePrDate", null);
			taskDataMap.put("cpeLicencePoNumber", null);
			taskDataMap.put("cpeLicencePoDate", null);
			taskDataMap.put("cpeLicencePoStatus", null);
			taskDataMap.put("cpeLicencePrStatus", null);
			taskDataMap.put("cpeSupplyLicencePrType", null);
			List<ProCreation> proCreationList = proCreationRepository
					.findByServiceIdAndServiceCodeAndVendorCodeAndComponentIdAndTypeIn(task.getServiceId(),
							task.getServiceCode(), task.getVendorCode(), scComponent.getId(), cpeTypeList);
			if (proCreationList != null && !proCreationList.isEmpty()) {
				LOGGER.info("proCreationList::{}", proCreationList.size());
				proCreationList.forEach(proCreation -> {
					if ("HARDWARE".equalsIgnoreCase(proCreation.getType())) {
						taskDataMap.put("cpeSupplyHardwarePrNumber", proCreation.getPrNumber());
						taskDataMap.put("cpeSupplyHardwarePrVendorName", proCreation.getVendorName());
						taskDataMap.put("cpeSupplyHardwarePrDate", proCreation.getPrCreatedDate() != null
								? DateUtil.convertTimestampToDate(proCreation.getPrCreatedDate()) : null);
						taskDataMap.put("cpeSupplyHardwarePoNumber", proCreation.getPoNumber());
						taskDataMap.put("cpeSupplyHardwarePoDate", proCreation.getPoCreatedDate() != null
								? DateUtil.convertTimestampToDate(proCreation.getPoCreatedDate()) : null);
						taskDataMap.put("cpeSupplyHardwarePoStatus", proCreation.getPoStatus());
						taskDataMap.put("cpeSupplyHardwarePrStatus", proCreation.getPrStatus());
						taskDataMap.put("cpeSupplyHardwarePrType", proCreation.getPrCreatedType());
					} else if ("LICENCE".equalsIgnoreCase(proCreation.getType())) {
						taskDataMap.put("cpeLicencePrNumber", proCreation.getPrNumber());
						taskDataMap.put("cpeLicenseVendorName", proCreation.getVendorName());
						taskDataMap.put("cpeLicencePrDate", proCreation.getPrCreatedDate() != null
								? DateUtil.convertTimestampToDate(proCreation.getPrCreatedDate()) : null);
						taskDataMap.put("cpeLicencePoNumber", proCreation.getPoNumber());
						taskDataMap.put("cpeLicencePoDate", proCreation.getPoCreatedDate() != null
								? DateUtil.convertTimestampToDate(proCreation.getPoCreatedDate()) : null);
						taskDataMap.put("cpeLicencePoStatus", proCreation.getPoStatus());
						taskDataMap.put("cpeLicencePrStatus", proCreation.getPrStatus());
						taskDataMap.put("cpeSupplyLicencePrType", proCreation.getPrCreatedType());
					}
				});
			}
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-dispatch-cpe")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-track-cpe-delivery")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-dispatch-track-cpe-international")){
			LOGGER.info("TaskId::{},VendorCode::{}",task.getId(),task.getVendorCode());
			ScComponent scComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(task.getServiceId(), "LM", task.getSiteType());
			taskDataMap.put("cpeComponentId", scComponent.getId());
			getNodeName(scComponent.getId(),"cpeDeviceName",taskDataMap,"cpeDeviceName");
			getNodeName(scComponent.getId(),"CPE",taskDataMap,"cpeContractType");
			getCpeCostDetails(task, taskDataMap,scComponent.getId(),null);
			Optional<ScServiceDetail> scServiceDetailOptional= scServiceDetailRepository.findById(task.getServiceId());
			if(scServiceDetailOptional.isPresent() && scServiceDetailOptional.get().getDestinationCountry()!=null && scServiceDetailOptional.get().getDestinationCountry().equalsIgnoreCase("India")){
				LOGGER.info("Site is Domestic ::{}",task.getServiceId());
				getSdwanBillingDate(task,taskDataMap);
			}else{
				LOGGER.info("Site is International ::{}",task.getServiceId());
				getNodeName(scComponent.getId(),"cpeBillStartDate",taskDataMap,"cpeBillStartDate");
				getNodeName(scComponent.getId(),"cpeSerialNumber",taskDataMap,"cpeSerialNumber");
			}
			LOGGER.info("TaskId::{},VendorCode::{}",task.getId(),task.getVendorCode());
			List<String> cpeTypeList= new ArrayList<>();
			cpeTypeList.add("HARDWARE");
			cpeTypeList.add("LICENCE");
			taskDataMap.put("cpePrPoDetails", null);
			getCpeDetailsBasedOnVendor(task.getServiceId(), task.getServiceCode(), scComponent.getId(),cpeTypeList,taskDataMap);
		}

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-provide-wbsglcc-details")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-pr-licence") 
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-po-licence")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-po-licence-release")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-provide-wbsglcc-details-endpoint")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-pr-endpoint")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-po-endpoint") 
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-po-endpoint-release")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-pr-endpoint-install-support")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-po-endpoint-install-support")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-po-endpoint-release-install-support")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-confirm-material-availability")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-generate-endpoint-invoice")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-dispatch-endpoint")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-track-endpoint-delivery")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-configure-endpoint")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-install-endpoint")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-access-code-activation")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-config-access-code")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-srn-generation-endpoint")
				) {
			getScWebexServiceCommercialDetails(task, taskDataMap);
			getBillingDateEndpoint(task,taskDataMap);
		}

		//To process teamsdr mediagateways.
		processTeamsDRMediagateways(task,taskDataMap);

		//To process teamsdr managed services
		processTeamsDRManagedServices(task, taskDataMap);

		// To process teamsdr commercials...
		// processTeamsDRCommercials(task,taskDataMap);


		if (task.getMstTaskDef().getKey().equalsIgnoreCase("product-commissioning-jeopardy")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("cpe-product-commissioning-jeopardy")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("comm-valid-billing-issue")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("tax-capture")) {
			ScOrder scOrder = scOrderRepository.findByOpOrderCodeAndIsActive(task.getOrderCode(), "Y");
			if(scOrder!=null && scOrder.getDemoFlag()!=null && "Y".equals(scOrder.getDemoFlag())) {
				Map<String, String> demoAttrMap = billingChargeLineItemService.loadDemoOrderBillDate(task.getServiceId());
				if(demoAttrMap!=null) {
					taskDataMap.put("demoBillStartDate", demoAttrMap.get("demoBillStartDate"));
					taskDataMap.put("demoBillEndDate", demoAttrMap.get("demoBillEndDate"));
				}
			}
			billingChargeLineItemService.loadAdditionalAttributes(task.getServiceId(),task.getScOrderId());
			List<ScChargeLineitem> chargeLineitems= chargeLineitemRepository.findByServiceId(task.getServiceId().toString());
			List<LineItemDetailsBean> itemDetailsBeanList = new ArrayList<>();
			ChargeLineItemBean chargeLineitemBean = new ChargeLineItemBean();
			chargeLineitems.forEach(chargeLineitem->{
				LineItemDetailsBean itemDetailsBean = new LineItemDetailsBean();
				itemDetailsBean.setAccountNumber(chargeLineitem.getAccountNumber());
				itemDetailsBean.setLineitem(chargeLineitem.getChargeLineitem());
				itemDetailsBean.setNrc(chargeLineitem.getNrc());
				itemDetailsBean.setMrc(chargeLineitem.getMrc());
				itemDetailsBean.setArc(chargeLineitem.getArc());
				itemDetailsBean.setServiceType(chargeLineitem.getServiceType());
				itemDetailsBean.setQuantity(chargeLineitem.getQuantity());
				itemDetailsBean.setUnitOfMeasurement(chargeLineitem.getUnitOfMeasurement());
				itemDetailsBean.setIsProrated(chargeLineitem.getIsProrated());
				itemDetailsBean.setBillingMethod(chargeLineitem.getBillingMethod());
				itemDetailsBean.setComponent(chargeLineitem.getComponent());
				itemDetailsBean.setCpeModel(chargeLineitem.getCpeModel());
				itemDetailsBean.setUsageArc(chargeLineitem.getUsageArc());
				itemDetailsBean.setHsnCode(chargeLineitem.getHsnCode());
				itemDetailsBean.setDescription(chargeLineitem.getComponentDesc());
				itemDetailsBean.setEffectiveOverage(chargeLineitem.getEffectiveOverage());
				itemDetailsBean.setEffectiveUsage(chargeLineitem.getEffectiveUsage());
				itemDetailsBeanList.add(itemDetailsBean);
			});
			if (chargeLineitems != null) {
				Double totalNrc = chargeLineitems.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getNrc())));
				Double totalArc = chargeLineitems.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getArc())));
				Double totalMrc = chargeLineitems.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getMrc())));

				taskDataMap.put("totalArc", String.format("%.2f",totalArc));
				taskDataMap.put("totalNrc", String.format("%.2f",totalNrc));
				taskDataMap.put("totalMrc", String.format("%.2f",totalMrc));
			}
			chargeLineitemBean.setLineItemDetails(itemDetailsBeanList);
			taskDataMap.put("lineItemDetails", itemDetailsBeanList);
		}
		taskDataMap.put("isInSez", "No");
		if (taskDataMap.containsKey("taxExemptionReason")) {
			String reason = (String) taskDataMap.get("taxExemptionReason");

			if (reason !=null && reason.equalsIgnoreCase("CUSTOMER IN SEZ")) {
				taskDataMap.put("isInSez", "Yes");

			} else {
				taskDataMap.put("isInSez", "No");

			}
		}
		taskDataMap.put("systemDate", new Date());
		
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("install-cpe")) {

			taskDataMap.put("cpeAmcStartDate", task.getCreatedTime());
			ScContractInfo contractInfo = scContractInfoRepository.findFirstByScOrder_id(task.getScOrderId());
			if (contractInfo != null) {
				Timestamp cpeAmcEndDate = getCpeAmcEndDate(task.getCreatedTime(), contractInfo.getOrderTermInMonths());
				if (cpeAmcEndDate != null) {
					taskDataMap.put("cpeAmcEndDate", cpeAmcEndDate);
				}
			}}

		if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-advanced-enrichment")){
			LOGGER.info("sdwan advance enrichment details::{}",task.getServiceId());
			List<ScComponent> scComponents = scComponentRepository.
					findByScServiceDetailId(task.getServiceId());
			List<CpeDetailsBean> cpeDetailsBeanList = getCpeDetails(scComponents);
			taskDataMap.put("cpeDetails", cpeDetailsBeanList);
			LOGGER.info("Izosdwan underLayDetails and cpeDetailsOverlay fetched");
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("renewal-commercial-vetting")){
			LOGGER.info("Renewals details serviceId: {} and serviceCode: {}", task.getServiceId(),task.getServiceCode());
			List<Integer>  renewalIds = scServiceDetailRepository.getIdByScOrderUuidAndUuid(task.getOrderCode());
			LOGGER.info("ServiceIds size: {}", renewalIds.size());
			List<RenewalCommercialVettingDetails> renewalCommercialVettingDetails = new ArrayList<>();
			List<String>  serviceAttributeList = new ArrayList<>();
			serviceAttributeList.add("Service Variant");
			serviceAttributeList.add("CPE");
			serviceAttributeList.add("Service type");
			serviceAttributeList.add("TAX_EXCEMPTED_REASON");
			serviceAttributeList.add("Additional IPs");
			serviceAttributeList.add("Cross Connect Type");
			serviceAttributeList.add("Media Type");
			serviceAttributeList.add("Type of Fibre Entry");
			serviceAttributeList.add("Fiber Type");
			serviceAttributeList.add("No. of Fiber pairs");
			serviceAttributeList.add("No. of Cable pairs");

			List<String> attachmentList = new ArrayList<>();
			attachmentList.add("PO");
			attachmentList.add("GSTCET");
			attachmentList.add("Tax");

			renewalIds.stream().forEach(id ->{
				List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository.
						findByScServiceDetail_idAndAttributeNameIn(id,serviceAttributeList);
				RenewalCommercialVettingDetails details = new RenewalCommercialVettingDetails();
				setRenewalDetails(scServiceAttributes, details, id);
				String billingMethod = (String) taskDataMap.get("billingMethod");
				setRenewalLineItems(details,id, task,billingMethod);
				List<AttachmentBean> attachmentBeans = attachmentService.getAllAttachmentByServiceIdAndcategory
						(id,attachmentList,task.getSiteType());
				details.setAttachmentDetails(attachmentBeans);
				details.setServiceId(id);
				renewalCommercialVettingDetails.add(details);

			});
			taskDataMap.put("renewalCommercialVettingDetails", renewalCommercialVettingDetails);
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("renewal-update-po-details")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("renewal-update-contract-details")){
			LOGGER.info("Renewals Update PO or Contract details serviceId: {} and serviceCode: {}", task.getServiceId(),task.getServiceCode());
			List<Integer>  renewalIds = scServiceDetailRepository.getIdByScOrderUuid(task.getOrderCode());
			LOGGER.info("ServiceIds size: {}", renewalIds.size());
			List<RenewalAttachmentDetails> renewalAttachmentDetails = new ArrayList<>();
			renewalIds.stream().forEach(id ->{
				RenewalAttachmentDetails details = new RenewalAttachmentDetails();
				List<String> attachmentList = new ArrayList<>();
				attachmentList.add("PO");
				List<AttachmentBean> attachmentBeans = attachmentService.getAllAttachmentByServiceIdAndcategory
						(id,attachmentList,task.getSiteType());
				details.setAttachmentDetails(attachmentBeans);
				details.setServiceId(id);
				renewalAttachmentDetails.add(details);
			});
			taskDataMap.put("renewalContractPOAttachmentDetails", renewalAttachmentDetails);
		}

		if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-underlay-e2e-service-testing-intl")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("service-provision-readiness")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("place-order")){
			LOGGER.info("sdwan underlay e2e service testing or service provision details::{}",task.getServiceId());
			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(task.getServiceId());
			ScServiceDetail scServiceDetail = scServiceDetailOpt.get();
			ScSolutionComponent scSolutionComponent = scSolutionComponentRepository.findByScServiceDetail1(scServiceDetail);
			LOGGER.info("parent service id for underlay::{}",scSolutionComponent.getScServiceDetail2().getId());
			Optional<ScComponent> scComponentOptional = scComponentRepository.findById(scSolutionComponent.getCpeComponentId());
			if(scComponentOptional.isPresent()) {
				LOGGER.info("Component Exists with id::{}",scComponentOptional.get().getId());
				CpeDeviceNameDetail cpeDeviceNameDetail = cpeDeviceNameDetailRepository.findByScServiceDetailAndScComponent
						(scSolutionComponent.getScServiceDetail2(),scComponentOptional.get());
				if(cpeDeviceNameDetail!=null) {
					LOGGER.info("Cpe Device Name exists for underlay service id::{}",scSolutionComponent.getScServiceDetail1().getId());
					taskDataMap.put("cpeDeviceName", cpeDeviceNameDetail.getCpeDeviceName());
				}
			}
			taskDataMap.put("overlayServiceId", scSolutionComponent.getScServiceDetail2().getUuid());
			taskDataMap.put("customerType","Others");
			taskDataMap.put("customerCategory",scSolutionComponent.getScServiceDetail1().getScOrder().getCustomerSegment()!=null?scSolutionComponent.getScServiceDetail1().getScOrder().getCustomerSegment():"Enterprise");
			if(task.getMstTaskDef().getKey().equalsIgnoreCase("place-order")) {
				String billingMethod = (String) taskDataMap.get("billingMethod");
				List<LineItemDetailsBean> itemDetailsBeans = new ArrayList<>();
				itemDetailsBeans = billingChargeLineItemService.loadLineItems(task.getServiceId(),null,billingMethod);
				if (itemDetailsBeans != null) {
					Double totalNrc = itemDetailsBeans.stream()
							.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getNrc())));
					Double totalArc = itemDetailsBeans.stream()
							.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getArc())));
					Double totalMrc = itemDetailsBeans.stream()
							.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getMrc())));

					taskDataMap.put("totalArc", String.format("%.2f",totalArc));
					taskDataMap.put("totalNrc", String.format("%.2f",totalNrc));
					taskDataMap.put("totalMrc", String.format("%.2f",totalMrc));
				}
				taskDataMap.put("lineItemDetails", itemDetailsBeans);
			}
		}

		if(task.getMstTaskDef().getKey().equalsIgnoreCase("byon-readiness")){
			LOGGER.info("byon readiness details::{}",task.getServiceId());
			String slaValue=getSlaValue(task.getScServiceDetail().getScServiceSlas());
			taskDataMap.put("networkUptime",slaValue);
			taskDataMap.put("byonCpeType","");
			taskDataMap.put("serviceClassification", "");
			ScSolutionComponent scSolutionComponent=scSolutionComponentRepository.findByScServiceDetail1_idAndOrderCodeAndIsActive(serviceId, task.getOrderCode(), "Y");
			if(scSolutionComponent!=null){
				LOGGER.info("ScSolution exists::{}",scSolutionComponent.getId());
				Integer overlayId=scSolutionComponent.getScServiceDetail2().getId();
				LOGGER.info("OverlayId::{}",overlayId);
				List<String> attributeNames=new ArrayList<>(); 
				attributeNames.add("noOfCpe");
				attributeNames.add("serviceClassification");
				List<ScServiceAttribute> scServiceAttributeList=scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(overlayId,attributeNames );
				if(scServiceAttributeList != null &&  !scServiceAttributeList.isEmpty() )
				{
					LOGGER.info("scServiceAttribute list is not empty");
					scServiceAttributeList.forEach(scServiceAttribute ->{
						if(scServiceAttribute.getAttributeName().equalsIgnoreCase("noOfCpe"))
						{
							LOGGER.info("NoOfCpe attribute exists::{}",scServiceAttribute.getAttributeValue());
							String noOfCpe = scServiceAttribute.getAttributeValue();
							if (noOfCpe.equalsIgnoreCase("One")) {
								taskDataMap.put("byonCpeType", "Single CPE");
							} else if (noOfCpe.equalsIgnoreCase("Two")) {
								taskDataMap.put("byonCpeType", "Dual CPE");
							}	
						}else if(scServiceAttribute.getAttributeName().equalsIgnoreCase("serviceClassification"))
						{
							LOGGER.info("serviceClassification attribute exists::{}",scServiceAttribute.getAttributeValue());
							String siteType = scServiceAttribute.getAttributeValue();
							taskDataMap.put("serviceClassification", siteType);
						}
					});					
				}
			}
			LOGGER.info("byon readiness taskDataMap::{}",taskDataMap);
		}
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-service-acceptance") ||
				task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-billing-issue") ||
				task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-service-issue") 
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-raise-turnup-request")){
			LOGGER.info("sdwan service acceptance details::{}",task.getServiceId());
			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			if(scServiceDetailOpt.isPresent()) {
				scServiceDetail =scServiceDetailOpt.get();
			}				
			ScOrder scOrder = scServiceDetail.getScOrder();				
			Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();			
			scServiceAttributes = scServiceDetail.getScServiceAttributes();			
			Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
			scOrderAttributes=scServiceDetail.getScOrder().getScOrderAttributes();				
			Map<String, String> scOrderAttributesmap = new HashMap<>();
			scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);
			Map<String, String> scServiceAttributesmap = new HashMap<>();				
			scServiceAttributesmap = scServiceAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);
			List<String> componentGroups = new ArrayList<>();
			componentGroups.add("UNDERLAY");
			componentGroups.add("OVERLAY");
			OverlayBean overlayBean=null;
			List<Map<String,Integer>> layDetails=scSolutionComponentRepository.findScServiceDetailByComponentType(scServiceDetail.getScOrderUuid(),componentGroups,"Y",scServiceDetail.getId());
			if(layDetails!=null && !layDetails.isEmpty()){
				LOGGER.info("Details Exists: {}",layDetails);
				overlayBean=setOverlayUnderlayDetails(layDetails, scServiceAttributesmap, scServiceDetail, scOrder, scOrderAttributesmap,overlayBean);
			}
			taskDataMap.put("overlay", overlayBean);			
		}
		
		if ((task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-cgw-service-acceptance")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-cgw-raise-turnup-request")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-cgw-service-issue")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-cgw-billing-issue")) && 
				taskDataMap.containsKey("primarySecondary") && taskDataMap.get("primarySecondary")!=null &&
				taskDataMap.containsKey("prisecLink") && taskDataMap.get("prisecLink")!=null) {
			ScServiceDetail serviceLinkServiceDetail=scServiceDetailRepository.findByUuidAndScOrderUuid(taskDataMap.get("prisecLink").toString(), task.getOrderCode());
			if(serviceLinkServiceDetail!=null){
				LOGGER.info("PriSec Service Id exists");
				taskDataMap.put("prisecId", serviceLinkServiceDetail.getId());
			}
			if("Secondary".equalsIgnoreCase(taskDataMap.get("primarySecondary").toString())){
				LOGGER.info("SY CGW Service Id::{},Task Id::{}",task.getServiceId(),task.getId());
				taskDataMap.put("commissioningDate", "NA");
				taskDataMap.put("billStartDate", "NA");
			}
			
		}
		
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-config-cpe")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-config-cpe-tda")) {
			LOGGER.info("Inside CPE Config Task ::{}", task.getServiceId());
			List<ScComponent> scComponents = scComponentRepository.
					findByScServiceDetailId(task.getServiceId());
			List<CpeDetailsBean> cpeDetailsBeanList = getCpeDetails(scComponents);
			taskDataMap.put("cpeDetails", cpeDetailsBeanList);
			LOGGER.info("Izosdwan underLayDetails and cpeDetailsOverlay fetched");
			LOGGER.info("CPE Config taskDataMap::{}", taskDataMap);
		}

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-install-cpe")) {
			LOGGER.info("Sdwan Cpe Install details::{}",task.getServiceId());
//			taskDataMap.put("cpeAmcStartDate", task.getCreatedTime());
//			ScContractInfo contractInfo = scContractInfoRepository.findFirstByScOrder_id(task.getScOrderId());
//			if (contractInfo != null) {
//				Timestamp cpeAmcEndDate = getCpeAmcEndDate(task.getCreatedTime(), contractInfo.getOrderTermInMonths());
//				if (cpeAmcEndDate != null) {
//					taskDataMap.put("cpeAmcEndDate", cpeAmcEndDate);
//				}
//			}
			List<ScComponent> scComponents = scComponentRepository.
					findByScServiceDetailId(task.getServiceId());
			List<CpeInstallationBean> cpeInstallDetails = getCpeInstallDetails(scComponents,task);
			taskDataMap.put("cpeInstallationDetails", cpeInstallDetails);
			LOGGER.info("Sdwan Cpe Installation completed");
		}

		if(task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-cgw-advanced-enrichment")){
			LOGGER.info("Sdwan Cgw details::{}",task.getServiceId());
			ScServiceDetail scServiceDetail = task.getScServiceDetail();
			if(Objects.nonNull(scServiceDetail) && scServiceDetail.getPrimarySecondary().equalsIgnoreCase("Primary")){
				String secondaryCgwServieId = scServiceDetail.getPriSecServiceLink();
				if(!secondaryCgwServieId.isEmpty() && Objects.nonNull(secondaryCgwServieId)) {
					taskDataMap.put("secondaryCGWServiceId", secondaryCgwServieId);
					taskDataMap.put("secondaryCGWLocation",null);
					ScServiceDetail cgwSyServiceDetail= scServiceDetailRepository.findByUuidAndScOrderUuid(secondaryCgwServieId, task.getOrderCode());
					if(cgwSyServiceDetail!=null){
						ScComponent scComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(cgwSyServiceDetail.getId(), "LM", "A");
						ScComponentAttribute scComponentSiteAddressAttribute=scComponentAttributesRepository.findFirstByScComponent_idAndAttributeName(scComponent.getId(),"siteAddress");
						if(scComponentSiteAddressAttribute!=null && scComponentSiteAddressAttribute.getAttributeValue()!=null && !scComponentSiteAddressAttribute.getAttributeValue().isEmpty()){
							LOGGER.info("secondarySiteAddress Exists {}", scComponentSiteAddressAttribute.getAttributeValue());
							taskDataMap.put("secondaryCGWLocation", scComponentSiteAddressAttribute.getAttributeValue());
						}
					}
					LOGGER.info("secondaryCgwServieId {}", secondaryCgwServieId);
				}
				getCosDatailsAttributes(serviceId, taskDataMap);
				Map<String, String> scComponentAttributesMap =commonFulfillmentUtils.getComponentAttributes(
						serviceId, AttributeConstants.COMPONENT_LM, "A");
				String heteroBw = StringUtils.trimToEmpty(scComponentAttributesMap.get("heteroBw"));
				String migrationBw = StringUtils.trimToEmpty(scComponentAttributesMap.get("migrationBw"));
				String siteAddress = StringUtils.trimToEmpty(scComponentAttributesMap.get("siteAddress"));
				String useCase=StringUtils.trimToEmpty(scComponentAttributesMap.get("useCase"));
				List<UseCaseDetailBean> useCaseDetailBeanList = new ArrayList<>();
				taskDataMap.put("useCaseDetails", null);
				taskDataMap.put("primaryCGWLocation", siteAddress);
				if(!useCase.isEmpty()){
					LOGGER.info("Use Case exists::{}",useCase);
					String useCases[]=useCase.split(";");
					for(String useCaseSplit:useCases){
						UseCaseDetailBean useCaseDetailBean = new UseCaseDetailBean();
						useCaseDetailBean.setName(useCaseSplit);
						useCaseDetailBean.setBandwidthUnit("Mbps");
						if(useCaseSplit!=null){
							LOGGER.info("Use Case Split exists::{}",useCaseSplit);
							if(replaceSpace(useCaseSplit).toLowerCase().contains("usecase2")){
								LOGGER.info("Use Case 2 exists");
								useCaseDetailBean.setBandwidth(migrationBw);
							}else if(replaceSpace(useCaseSplit).toLowerCase().contains("usecase4")){
								LOGGER.info("Use Case 4 exists");
								useCaseDetailBean.setBandwidth(heteroBw);
							}
						}
						useCaseDetailBeanList.add(useCaseDetailBean);
					}
					taskDataMap.put("useCaseDetails", useCaseDetailBeanList);
				}
			}
			LOGGER.info("sdwan Cgw details fetched");
		}
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("assist-cmip")) {
			LOGGER.info("Inside Assist CMIP Task ::{}", task.getServiceId());
			getIzoSdwanDetails(task.getServiceId(), task.getServiceCode(), task.getOrderCode(), taskDataMap);
			LOGGER.info("Izosdwan underLayDetails and cpeDetailsOverlay fetched");
			LOGGER.info("CPE Config taskDataMap::{}", taskDataMap);
		}
		/*if (task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-service-issue")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-raise-turnup-request")) {
			LOGGER.info("Inside {} Task ::{}", task.getMstTaskDef().getKey(), task.getServiceId());
			List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailId(task.getServiceId());
			List<CpeDetailsBean> cpeDetailsBeanList = getCpeDetails(scComponents);
			taskDataMap.put("cpeDetails", cpeDetailsBeanList);
			Map<String, String> attr = commonFulfillmentUtils.getComponentAttributes(task.getServiceId(),
					AttributeConstants.COMPONENT_LM, "A");
			if (attr.containsKey("commissioningDate")) {
				taskDataMap.put("commissioningDate", attr.get("commissioningDate"));
			}
			LOGGER.info("Izosdwan underLayDetails and cpeDetailsOverlay fetched");
			LOGGER.info("{} taskDataMap::{}", task.getMstTaskDef().getKey(), taskDataMap);
		}*/

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("rf-config-p2p")){
			ScServiceDetail activeScServiceDetail = scServiceDetailRepository
					.findFirstByUuidAndMstStatus_codeOrderByIdDesc(task.getServiceCode(), "ACTIVE");
			if("Hot Upgrade".equalsIgnoreCase(task.getScServiceDetail().getOrderSubCategory()) && activeScServiceDetail.getScOrderUuid()!=null)
			{
				taskRepository.findByServiceIdAndMstTaskDef_key(activeScServiceDetail.getId(), "rf-config-p2p").
						stream(). findFirst().
						ifPresent(t ->
						{
							TaskData p2pRfData = taskDataRepository.findFirstByTask_idOrderByCreatedTimeDesc(t.getId());
							if(Objects.nonNull(p2pRfData))
								taskDataMap.put("previousCommonData", p2pRfData.getData());

						});
			}
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("gsc-order-suppliers")) {
			List<DocumentBean> documentsList = new LinkedList<>();
			Optional.ofNullable(scAttachmentRepository.findAllByScServiceDetail_Id(serviceId)).get().stream()
					.map(ScAttachment::getAttachment).forEach(attachment -> {
						DocumentBean document = new DocumentBean();
						document.setAttachmentId(attachment.getId());
						document.setCategory(attachment.getCategory());
						document.setName(attachment.getName());
						document.setUrl(attachment.getUriPathOrUrl());
						documentsList.add(document);
					});
			taskDataMap.put("documents", documentsList);
		}
		
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("izopc-advanced-enrichment")
				||(task.getMstTaskDef().getKey().equalsIgnoreCase("izopc-advanced-enrichment-rejected")) ) {
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "izopc-manual-enrich-service-design", task.getSiteType()==null?"A":task.getSiteType());
			if (taskPlan != null)taskDataMap.put("tentativeDate", taskPlan.getPlannedStartTime());
			if ("IZOPC".equals(task.getServiceType())) {
				getCosDatailsAttributes(serviceId, taskDataMap);
			}
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("izopc-manual-enrich-service-design")){
			LOGGER.info("izopc manual enrich design details::{}",task.getServiceId());
			Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository.findById(task.getServiceId());
			if(scServiceDetailOptional.isPresent()) {
				ScServiceDetail scServiceDetail=scServiceDetailOptional.get();
				ScServiceAttribute cloudProviderAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(task.getServiceId(), "Cloud Provider");
				if(scServiceDetail.getPrimarySecondary()!=null && scServiceDetail.getPrimarySecondary().equalsIgnoreCase("Primary")
						&& cloudProviderAttribute!=null && cloudProviderAttribute.getAttributeValue()!=null 
						&& !cloudProviderAttribute.getAttributeValue().equalsIgnoreCase("IBM Direct Link")) {
					LOGGER.info("izopc manual enrich design other than csp::{}",cloudProviderAttribute.getAttributeValue());
					taskDataMap.put("primarySecondary", "SINGLE");
				}
				if(cloudProviderAttribute!=null && cloudProviderAttribute.getAttributeValue()!=null 
						&& (cloudProviderAttribute.getAttributeValue().equalsIgnoreCase("Google Public Cloud")
								|| cloudProviderAttribute.getAttributeValue().equalsIgnoreCase("Google Cloud Interconnect-Private"))) {
					LOGGER.info("izopc manual enrich design google csp::{}",cloudProviderAttribute.getAttributeValue());
					taskDataMap.put("wanIpProvidedBy", "Partner");
				}
				taskDataMap.put("asNumberProvidedByCustomer", "NO");
				ScComponentAttribute asNumberAttribute = scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(task.getServiceId(), "asNumber","LM",task.getSiteType());
				if(asNumberAttribute!=null && asNumberAttribute.getAttributeValue()!=null && asNumberAttribute.getAttributeValue().equalsIgnoreCase("Customer public AS Number")) {
					LOGGER.info("izopc manual enrich design asNumber::{}",asNumberAttribute.getAttributeValue());
					taskDataMap.put("asNumberProvidedByCustomer", "YES");
				}
				taskDataMap.put("parentSolutionId", null);
				ScServiceAttribute referenceServiceIdAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(task.getServiceId(), "Service Id");
				ScServiceAttribute vpnTopologyAttribute = scServiceAttributeRepository.findFirstByScServiceDetail_idAndAttributeName(task.getServiceId(),"VPN Topology");
				if(referenceServiceIdAttribute!=null && referenceServiceIdAttribute.getAttributeValue()!=null && vpnTopologyAttribute!=null && vpnTopologyAttribute.getAttributeValue()!=null) {
					LOGGER.info("izopc manual enrich design referenceServiceIdAttribute::{}",referenceServiceIdAttribute.getAttributeValue());
					Map<String,String> vpnAttributesMap= new HashMap<>();
					vpnAttributesMap.put("serviceCode",referenceServiceIdAttribute.getAttributeValue());
					vpnAttributesMap.put("vpnTopology",vpnTopologyAttribute.getAttributeValue());
					String response = (String) mqUtils.sendAndReceive(vpnsolution_queue, Utils.convertObjectToJson(vpnAttributesMap));
					if (response != null) {
						LOGGER.info("izopc manual enrich design parent solution exists::{} for serviceCode::{}",response,referenceServiceIdAttribute.getAttributeValue());
						taskDataMap.put("parentSolutionId", response);
					}
				}
			}
		}
		return taskDataMap;
	}
	
	private void setRenewalDetails(List<ScServiceAttribute> scServiceAttributes,
			RenewalCommercialVettingDetails details, Integer id) {

			LOGGER.info("enter setRenewalDetails for service id: {}", id);
			for(ScServiceAttribute attributeDetails : scServiceAttributes){
				if(attributeDetails.getAttributeName().equalsIgnoreCase("Service Variant")) {
					details.setServiceVariant(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("CPE")) {
					details.setCpeContractType(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("Service type")) {
					details.setUsageType(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("TAX_EXCEMPTED_REASON")) {
					details.setTaxExemptionReason(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("Additional IPs")) {
					details.setAdditionalIp(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("Cross Connect Type")) {
					details.setCrossConnectType(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("Media Type")) {
					details.setMediaType(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("Type of Fibre Entry")) {
					details.setFiberEntryType(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("Fiber Type")) {
					details.setFiberType(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("No. of Fiber pairs")) {
					details.setFiberPairNumber(attributeDetails.getAttributeValue());
				}
				if(attributeDetails.getAttributeName().equalsIgnoreCase("No. of Cable pairs")) {
					details.setCablePairNumber(attributeDetails.getAttributeValue());
				}
			}
		}

		private void setRenewalLineItems(RenewalCommercialVettingDetails details, Integer id, Task task, String billingMethod){
			LOGGER.info("enter setRenewalLineItems for service id: {}", id);
			CpeBomResource bomResource = null;
			try {
				bomResource =  getBomResourcesRenewal(id,details);
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
			List<LineItemDetailsBean> itemDetailsBeans = new ArrayList<>();
			if("NPL".equals(task.getServiceType())) {
				itemDetailsBeans = billingChargeLineItemService.loadLineItemsNPL(id,billingMethod);
			} else {
				itemDetailsBeans = billingChargeLineItemService.loadLineItems(id,bomResource,billingMethod);
			}

			if (itemDetailsBeans != null) {
				Double totalNrc = itemDetailsBeans.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getNrc())));
				Double totalArc = itemDetailsBeans.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getArc())));
				Double totalMrc = itemDetailsBeans.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getMrc())));

				details.setTotalArc(String.format("%.2f",totalArc));
				details.setTotalNrc(String.format("%.2f",totalNrc));
				details.setTotalMrc(String.format("%.2f",totalMrc));
			}
			details.setItemDetailsBeans(itemDetailsBeans);
		}

	/**
	 * Method to process mg.
	 *
	 * @param task
	 * @param taskDataMap
	 */
	private void processTeamsDRMediagateways(Task task, Map<String, Object> taskDataMap) {
		// Double exchangeValue = forexService.convertCurrency(Currencies.USD,
		// Currencies.INR);
		if (task.getScServiceDetail().getScOrderUuid()
				.startsWith(CommonConstants.UCDR) && TeamsDRFulfillmentConstants.MEDIA_GATEWAY
				.equalsIgnoreCase(task.getScServiceDetail().getErfPrdCatalogOfferingName())) {
			List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailId(task.getServiceId());
			List<TeamsDRMgComponentBean> mgComponents = new ArrayList<>();
			AtomicReference<String> country = new AtomicReference<>();
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
			country.set(scServiceDetail.getDestinationCountry());
			scComponents.forEach(scComponent -> {
				TeamsDRMgComponentBean teamsDRMgComponentBean = new TeamsDRMgComponentBean();
				teamsDRMgComponentBean.setComponentId(scComponent.getId());
				teamsDRMgComponentBean.setComponentName(scComponent.getComponentName());
				Map<String, String> attributesMap = new HashMap<>();
				AtomicReference<String> purchaseType = new AtomicReference<>();
				List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository.findByScComponent(scComponent);
				if (AttributeConstants.COMPONENT_LM.equalsIgnoreCase(scComponent.getComponentName())) {
					scComponentAttributes.forEach(scComponentAttribute -> {
						if ("Local IT Contact Name".equals(scComponentAttribute.getAttributeName())) {
							taskDataMap.put("localItContactName", scComponentAttribute.getAttributeValue());
						} else if ("Local IT Contact Number".equals(scComponentAttribute.getAttributeName())) {
							taskDataMap.put("localItContactMobile", scComponentAttribute.getAttributeValue());
						} else if ("Local IT Contact E-mail ID".equals(scComponentAttribute.getAttributeName())) {
							taskDataMap.put("localItContactEmailId", scComponentAttribute.getAttributeValue());
						} else if (TeamsDROdrConstants.TOTAL_COMMITTED_USERS
								.equals(scComponentAttribute.getAttributeName())) {
							taskDataMap.put("totalCommittedUsers", scComponentAttribute.getAttributeValue());
						}
						else {
							taskDataMap.put(scComponentAttribute.getAttributeName(),
									scComponentAttribute.getAttributeValue());
						}
					});
				}
				scComponentAttributes.forEach(scComponentAttribute -> {
					if ("Local IT Contact Name".equals(scComponentAttribute.getAttributeName())) {
						attributesMap.put("localItContactName", scComponentAttribute.getAttributeValue());
					} else if ("Local IT Contact Number".equals(scComponentAttribute.getAttributeName())) {
						attributesMap.put("localItContactMobile", scComponentAttribute.getAttributeValue());
					} else if ("Local IT Contact E-mail ID".equals(scComponentAttribute.getAttributeName())) {
						attributesMap.put("localItContactEmailId", scComponentAttribute.getAttributeValue());
					} else if (TeamsDROdrConstants.TOTAL_COMMITTED_USERS
							.equals(scComponentAttribute.getAttributeName())) {
						taskDataMap.put("totalCommittedUsers", scComponentAttribute.getAttributeValue());
					} else {
						attributesMap.put(scComponentAttribute.getAttributeName(),
								scComponentAttribute.getAttributeValue());
					}
					if (CommonConstants.CPE_BOM.equals(scComponentAttribute.getAttributeName())) {
						String serviceParamId = scComponentAttribute.getAttributeValue();
						if (Objects.nonNull(serviceParamId) && StringUtils.isNotBlank(serviceParamId)) {
							scAdditionalServiceParamRepository.findById(Integer.valueOf(serviceParamId))
									.ifPresent(scAdditionalServiceParam -> {
										if (Objects.nonNull(scAdditionalServiceParam.getValue())) {
											teamsDRMgComponentBean.setCpeBOM(scAdditionalServiceParam.getValue());
										}

									});
						}
					} else if ("Media Gateway Purchase Type".equals(scComponentAttribute.getAttributeName())) {
						purchaseType.set(scComponentAttribute.getAttributeValue());
					}
				});

				if (!"LM".equals(scComponent.getComponentName())) {
					List<MstCatalogueBean> mstCatalogueBeans = new ArrayList<>();
					cpeCostDetailsRepository.findByServiceIdAndComponentId(task.getServiceId(), scComponent.getId())
							.forEach(cpeCostDetails -> {
								MstCostCatalogue mstCostCatalogue;
								if ("Rental Purchase".equals(purchaseType.get())) {
									mstCostCatalogue = mstCostCatalogueRepository
											.findByBundledBomAndCategoryForRental(scComponent.getComponentName(),
													cpeCostDetails.getCategory())
											.stream().findAny().get();
								} else {
									mstCostCatalogue = mstCostCatalogueRepository
											.findByBundledBomAndCategoryForOutright(scComponent.getComponentName(),
													cpeCostDetails.getCategory())
											.stream().findAny().get();
								}
								MstCatalogueBean catalogueBean = new MstCatalogueBean();
								catalogueBean.setId(cpeCostDetails.getId());
								catalogueBean.setCategory(cpeCostDetails.getCategory());
								catalogueBean.setBundledBom(scComponent.getComponentName());
								catalogueBean.setDescription(cpeCostDetails.getDescription());
								catalogueBean.setDdpCharge(mstCostCatalogue.getDdpCharge());
								catalogueBean.setIncrementalRate(cpeCostDetails.getIncrementalRate());
								// if(Objects.nonNull(exchangeValue) && "India".equals(country.get())){
								// Double incrementalPrice = mstCostCatalogue.getIncrementalRate();
								// Double totalTPinINR = ((exchangeValue + incrementalPrice) *
								// mstCostCatalogue.getTotalPriceDdp());
								// catalogueBean.setTotalTPinINR(totalTPinINR);
								// catalogueBean.setFxSpotRate(exchangeValue);
								// }
								catalogueBean.setTotalTPinINR(cpeCostDetails.getCalculatedPrice());
								catalogueBean.setHsnCode(cpeCostDetails.getHsnCode());
								catalogueBean.setIncrementalRate(cpeCostDetails.getIncrementalRate());
								catalogueBean.setMarginThreePercentage(mstCostCatalogue.getMarginThreePercentage());
								catalogueBean.setOem(cpeCostDetails.getOem());
								catalogueBean.setPerListPriceUsd(cpeCostDetails.getPerListPriceUsd());
								catalogueBean.setRentalMaterialCode(mstCostCatalogue.getRentalMaterialCode());
								catalogueBean.setSaleMaterialCode(mstCostCatalogue.getSaleMaterialCode());
								catalogueBean.setProcurementDiscountPercentage(
										mstCostCatalogue.getProcurementDiscountPercentage());
								catalogueBean.setProductCode(cpeCostDetails.getProductCode());
								catalogueBean.setQuantity(cpeCostDetails.getQuantity());
								catalogueBean.setServiceNumber(cpeCostDetails.getServiceNumber());
								catalogueBean.setShortText(cpeCostDetails.getShortText());
								catalogueBean.setTotalListPriceUsd(cpeCostDetails.getCalculatedPrice());
								catalogueBean.setTotalPriceDdp(mstCostCatalogue.getTotalPriceDdp());
								catalogueBean.setTotalPriceMargin(mstCostCatalogue.getTotalPriceMargin());
								catalogueBean.setTotalPriceUsd(mstCostCatalogue.getTotalPriceUsd());
								catalogueBean.setMaterialsList(populateWebexEndpointComponent(task.getServiceId(),
										scComponent.getComponentName()));
								catalogueBean.setVendorName(cpeCostDetails.getVendorName());
								catalogueBean.setVendorCode(cpeCostDetails.getVendorCode());
								mstCatalogueBeans.add(catalogueBean);
							});
					teamsDRMgComponentBean.setMstCatalogues(mstCatalogueBeans);
				}
				getBillingDateEndpoint(task, taskDataMap);
				teamsDRMgComponentBean.setAttributes(attributesMap);
				teamsDRMgComponentBean.setCommercials(
						getScTeamsDRServiceCommercialDetails(task, taskDataMap, scComponent.getComponentName()));
				mgComponents.add(teamsDRMgComponentBean);

			});
			taskDataMap.put("mgComponents", mgComponents);


			// Process tasks data...
			List<TaskDataBean> taskDataBeans =  new ArrayList<>();
			taskRepository.findByScOrderId(task.getScOrderId()).stream().filter(task1 ->
					"teamsdr-upload-lld-migration-document".equals(task1.getMstTaskDef().getKey()))
					.forEach(task1 -> {
						TaskData taskData = taskDataRepository.findFirstByTask_idOrderByCreatedTimeDesc(task1.getId());
						if (taskData != null) {
							TaskDataBean dataBean = new TaskDataBean();
							dataBean.setData(Utils.convertJsonStingToJson(taskData.getData()));
							dataBean.setName(taskData.getName());
							dataBean.setCreatedTime(taskData.getCreatedTime());
							taskDataBeans.add(dataBean);
						}
					});
			taskDataMap.put("teamsDRTasksData", taskDataBeans);
		}
	}

	/**
	 * Method to process managed services
	 *
	 * @param task
	 * @param taskDataMap
	 */
	private void processTeamsDRManagedServices(Task task, Map<String, Object> taskDataMap) {
		// Double exchangeValue = forexService.convertCurrency(Currencies.USD,
		// Currencies.INR);
		if (task.getScServiceDetail().getScOrderUuid().startsWith(CommonConstants.UCDR) && Objects
				.nonNull(task.getScServiceDetail().getErfPrdCatalogOfferingName()) && task.getScServiceDetail()
				.getErfPrdCatalogOfferingName().contains(TeamsDRFulfillmentConstants.PLAN)) {
			List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailId(task.getServiceId());
			List<TeamsDRManagedServicesComponentBean> managedServicesComponents = new ArrayList<>();
			AtomicReference<String> country = new AtomicReference<>();
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
			country.set(scServiceDetail.getDestinationCountry());
			scComponents.forEach(scComponent -> {
				TeamsDRManagedServicesComponentBean teamsDRManagedServicesComponentBean = new TeamsDRManagedServicesComponentBean();
				teamsDRManagedServicesComponentBean.setComponentId(scComponent.getId());
				teamsDRManagedServicesComponentBean.setComponentName(scComponent.getComponentName());
				Map<String, String> attributesMap = new HashMap<>();
				AtomicReference<String> purchaseType = new AtomicReference<>();
				List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository
						.findByScComponent(scComponent);
				if (AttributeConstants.COMPONENT_LM.equalsIgnoreCase(scComponent.getComponentName())) {
					scComponentAttributes.forEach(scComponentAttribute -> {
						if ("Local IT Contact Name".equals(scComponentAttribute.getAttributeName())) {
							taskDataMap.put("localItContactName", scComponentAttribute.getAttributeValue());
						} else if ("Local IT Contact Number".equals(scComponentAttribute.getAttributeName())) {
							taskDataMap.put("localItContactMobile", scComponentAttribute.getAttributeValue());
						} else if ("Local IT Contact E-mail ID".equals(scComponentAttribute.getAttributeName())) {
							taskDataMap.put("localItContactEmailId", scComponentAttribute.getAttributeValue());
						} else if (TeamsDROdrConstants.TOTAL_COMMITTED_USERS
								.equals(scComponentAttribute.getAttributeName())) {
							taskDataMap.put("totalCommittedUsers", scComponentAttribute.getAttributeValue());
						} else {
							if(Objects.nonNull(scComponentAttribute) && Objects.nonNull(scComponentAttribute.getIsAdditionalParam())
							 && CommonConstants.Y.equals(scComponentAttribute.getIsAdditionalParam()) && Objects.nonNull(scComponentAttribute.getAttributeValue())){
								scAdditionalServiceParamRepository.findById(Integer.parseInt(scComponentAttribute.getAttributeValue())).ifPresent(
										scAdditionalServiceParam -> {
											taskDataMap.put(scComponentAttribute.getAttributeName(),scAdditionalServiceParam.getValue());
										}
								);
							}else{
								taskDataMap.put(scComponentAttribute.getAttributeName(),
										scComponentAttribute.getAttributeValue());
							}
						}
					});
				}
				scComponentAttributes.forEach(scComponentAttribute -> {
					if ("Local IT Contact Name".equals(scComponentAttribute.getAttributeName())) {
						attributesMap.put("localItContactName", scComponentAttribute.getAttributeValue());
					} else if ("Local IT Contact Number".equals(scComponentAttribute.getAttributeName())) {
						attributesMap.put("localItContactMobile", scComponentAttribute.getAttributeValue());
					} else if ("Local IT Contact E-mail ID".equals(scComponentAttribute.getAttributeName())) {
						attributesMap.put("localItContactEmailId", scComponentAttribute.getAttributeValue());
					} else if (TeamsDROdrConstants.TOTAL_COMMITTED_USERS
							.equals(scComponentAttribute.getAttributeName())) {
						taskDataMap.put("totalCommittedUsers", scComponentAttribute.getAttributeValue());
					} else {
						attributesMap
								.put(scComponentAttribute.getAttributeName(), scComponentAttribute.getAttributeValue());
					}
				});

				teamsDRManagedServicesComponentBean.setAttributes(attributesMap);
				//adding commercial data in LM component
				if (scComponent.getComponentName().equalsIgnoreCase(AttributeConstants.COMPONENT_LM))
					teamsDRManagedServicesComponentBean.setScTeamsDRServiceCommercialBeans(
							getScTeamsDRServiceCommercialDetails(task, taskDataMap, null));
				managedServicesComponents.add(teamsDRManagedServicesComponentBean);
			});
			taskDataMap.put("managedServiceComponents", managedServicesComponents);
			List<ScComponent> lmAndCountryComponents = scComponentRepository
					.findByScServiceDetailIdAndSiteType(task.getScServiceDetail().getId(), "A");
			//including all sites if no scComponent id tagged to task
			List<ScComponent> siteComponents = new ArrayList<>();
			if (Objects.isNull(task.getScComponent()))
				siteComponents = scComponentRepository
						.findByScServiceDetailIdAndSiteType(task.getScServiceDetail().getId(),
								AttributeConstants.DR_SITE);
			else
				siteComponents = Arrays.asList(task.getScComponent());

			List<TeamsDRManagedServiceCountryBean> countryBeans;
			if ("teamsdr-managed-services-advanced-enrichment".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				countryBeans = populateCountryDetailsAdvancedEnrichment(task, lmAndCountryComponents, siteComponents);
			} else {
				countryBeans = populateCountryDetailsForOtherTasks(task, lmAndCountryComponents, siteComponents);
			}
			taskDataMap.put("teamsdrCountryDetails", countryBeans);
			List<TeamsDRBatchDetails> teamsDRBatchDetails = new ArrayList<>();
			siteComponents.forEach(siteComponent -> {
				List<GscFlowGroup> flowGroups = teamsDRService
						.getFlowGroupByRefIdAndType(String.valueOf(siteComponent.getId()),
								AttributeConstants.COMPONENT);
				if (Objects.nonNull(flowGroups) && !flowGroups.isEmpty()) {
					flowGroups.forEach(flowGroup -> {
						Map<String, Object> batchAttributes = new HashMap<>();
						TeamsDRBatchDetails teamsDRBatchDetail = new TeamsDRBatchDetails();
						teamsDRBatchDetail.setBatchId(flowGroup.getId());
						List<FlowGroupAttribute> flowGroupAttributes = teamsDRService.getFlowGroupAttributes(flowGroup);
						flowGroupAttributes.forEach(flowGroupAttribute -> {
							batchAttributes.put("attributeId", flowGroupAttribute.getId());
							batchAttributes
									.put(flowGroupAttribute.getAttributeName(), flowGroupAttribute.getAttributeValue());
						});
						teamsDRBatchDetail.setBatchAttributes(batchAttributes);
						teamsDRBatchDetails.add(teamsDRBatchDetail);
						taskDataMap.put("taskBatchDetails", teamsDRBatchDetails);
					});
				}
			});

			// Process tasks data...
			List<TaskDataBean> taskDataBeans = new ArrayList<>();
			taskRepository.findByScOrderId(task.getScOrderId()).stream()
					.filter(task1 -> "teamsdr-upload-lld-migration-document".equals(task1.getMstTaskDef().getKey()))
					.forEach(task1 -> {
						TaskData taskData = taskDataRepository.findFirstByTask_idOrderByCreatedTimeDesc(task1.getId());
						if (taskData != null) {
							TaskDataBean dataBean = new TaskDataBean();
							dataBean.setData(Utils.convertJsonStingToJson(taskData.getData()));
							dataBean.setName(taskData.getName());
							dataBean.setCreatedTime(taskData.getCreatedTime());
							taskDataBeans.add(dataBean);
						}
					});
			taskDataMap.put("teamsDRTasksData", taskDataBeans);
		}
	}

	/**
	 * Populate country details for other task
	 *
	 * @param task
	 * @param lmAndCountryComponents
	 * @param siteComponents
	 * @return
	 */
	private List<TeamsDRManagedServiceCountryBean> populateCountryDetailsForOtherTasks(Task task,
			List<ScComponent> lmAndCountryComponents, List<ScComponent> siteComponents) {
		List<TeamsDRManagedServiceCountryBean> countryBeans = new ArrayList<>();
		//filtering countries that do not have any sites (site name convention - countryName_siteName)
		lmAndCountryComponents.stream().filter(lmAndCtryComponent -> !AttributeConstants.COMPONENT_LM
				.equalsIgnoreCase(lmAndCtryComponent.getComponentName()) && siteComponents.stream()
				.anyMatch(site -> site.getComponentName().startsWith(lmAndCtryComponent.getComponentName())))
				.forEach(countryComponent -> {
					TeamsDRManagedServiceCountryBean countryBean = new TeamsDRManagedServiceCountryBean();
					Map<String, Object> countryAttributesMap = new HashMap<>();
					countryBean.setCountryCompId(countryComponent.getId());
					countryBean.setCountryName(countryComponent.getComponentName());
					countryBean.setManagedServiceSiteDetails(new ArrayList<>());
					List<ScComponentAttribute> countryComponentAttributes = scComponentAttributesRepository
							.findByScComponent(countryComponent);
					countryComponentAttributes.forEach(countryComp -> countryAttributesMap
							.put(countryComp.getAttributeName(), countryComp.getAttributeValue()));
					countryBean.setCountryAttributes(countryAttributesMap);
					siteComponents.stream().filter(siteComponent -> siteComponent.getComponentName()
							.startsWith(countryBean.getCountryName())).forEach(siteComponent -> {
						TeamsDRManagedServiceSiteDetails serviceSiteDetails = new TeamsDRManagedServiceSiteDetails();
						serviceSiteDetails.setTeamsDRBatchDetails(new ArrayList<>());
						serviceSiteDetails.setSiteCompId(siteComponent.getId());
						serviceSiteDetails.setSiteName(
								siteComponent.getComponentName().substring(countryBean.getCountryName().length() + 1));
						serviceSiteDetails.setComponentName(siteComponent.getComponentName());
						Map<String, Object> siteAttributesMap = new HashMap<>();
						List<ScComponentAttribute> siteComponentAttributes = scComponentAttributesRepository
								.findByScComponent(siteComponent);
						siteComponentAttributes.forEach(siteComp -> siteAttributesMap
								.put(siteComp.getAttributeName(), siteComp.getAttributeValue()));
						serviceSiteDetails.setSiteAttributes(siteAttributesMap);
						if (Objects.nonNull(task.getGscFlowGroupId())) {
							GscFlowGroup flowGroups = teamsDRService.getFlowGroupById(task.getGscFlowGroupId());
							TeamsDRBatchDetails teamsDRBatchDetails = new TeamsDRBatchDetails();
							teamsDRBatchDetails.setBatchId(task.getGscFlowGroupId());
							List<FlowGroupAttribute> flowGroupAttributes = teamsDRService
									.getFlowGroupAttributes(flowGroups);
							Map<String, Object> batchAttributes = new HashMap<>();
							flowGroupAttributes.forEach(flowGroupAttribute -> {
								batchAttributes.put("attributeId", flowGroupAttribute.getId());
								batchAttributes.put(flowGroupAttribute.getAttributeName(),
										flowGroupAttribute.getAttributeValue());
							});
							teamsDRBatchDetails.setBatchAttributes(batchAttributes);
							serviceSiteDetails.getTeamsDRBatchDetails().add(teamsDRBatchDetails);
						}
						countryBean.getManagedServiceSiteDetails().add(serviceSiteDetails);
					});
					countryBeans.add(countryBean);
				});
		return countryBeans;
	}

	/**
	 * Populate country details for advanced enrichment task
	 *
	 * @param task
	 * @param lmAndCountryComponents
	 * @param siteComponents
	 * @return
	 */
	private List<TeamsDRManagedServiceCountryBean> populateCountryDetailsAdvancedEnrichment(Task task,
			List<ScComponent> lmAndCountryComponents, List<ScComponent> siteComponents) {
		List<TeamsDRManagedServiceCountryBean> countryBeans = new ArrayList<>();
		lmAndCountryComponents.stream().filter(lmAndCtryComponent -> !AttributeConstants.COMPONENT_LM
				.equalsIgnoreCase(lmAndCtryComponent.getComponentName())).forEach(countryComponent -> {
			TeamsDRManagedServiceCountryBean countryBean = new TeamsDRManagedServiceCountryBean();
			Map<String, Object> countryAttributesMap = new HashMap<>();
			countryBean.setCountryCompId(countryComponent.getId());
			countryBean.setCountryName(countryComponent.getComponentName());
			countryBean.setManagedServiceSiteDetails(new ArrayList<>());
			List<ScComponentAttribute> countryComponentAttributes = scComponentAttributesRepository
					.findByScComponent(countryComponent);
			countryComponentAttributes.forEach(countryComp -> countryAttributesMap
					.put(countryComp.getAttributeName(), countryComp.getAttributeValue()));
			countryBean.setCountryAttributes(countryAttributesMap);
			siteComponents.stream()
					.filter(siteComponent -> siteComponent.getComponentName().startsWith(countryBean.getCountryName()))
					.forEach(siteComponent -> {
						TeamsDRManagedServiceSiteDetails serviceSiteDetails = new TeamsDRManagedServiceSiteDetails();
						serviceSiteDetails.setTeamsDRBatchDetails(new ArrayList<>());
						serviceSiteDetails.setSiteCompId(siteComponent.getId());
						serviceSiteDetails.setSiteName(
								siteComponent.getComponentName().substring(countryBean.getCountryName().length() + 1));
						serviceSiteDetails.setComponentName(siteComponent.getComponentName());
						Map<String, Object> siteAttributesMap = new HashMap<>();
						List<ScComponentAttribute> siteComponentAttributes = scComponentAttributesRepository
								.findByScComponent(siteComponent);
						siteComponentAttributes.forEach(siteComp -> siteAttributesMap
								.put(siteComp.getAttributeName(), siteComp.getAttributeValue()));
						serviceSiteDetails.setSiteAttributes(siteAttributesMap);
						if (Objects.nonNull(task.getGscFlowGroupId())) {
							GscFlowGroup flowGroups = teamsDRService.getFlowGroupById(task.getGscFlowGroupId());
							TeamsDRBatchDetails teamsDRBatchDetails = new TeamsDRBatchDetails();
							teamsDRBatchDetails.setBatchId(task.getGscFlowGroupId());
							List<FlowGroupAttribute> flowGroupAttributes = teamsDRService
									.getFlowGroupAttributes(flowGroups);
							Map<String, Object> batchAttributes = new HashMap<>();
							flowGroupAttributes.forEach(flowGroupAttribute -> {
								batchAttributes.put("attributeId", flowGroupAttribute.getId());
								batchAttributes.put(flowGroupAttribute.getAttributeName(),
										flowGroupAttribute.getAttributeValue());
							});
							teamsDRBatchDetails.setBatchAttributes(batchAttributes);
							serviceSiteDetails.getTeamsDRBatchDetails().add(teamsDRBatchDetails);
						}
						countryBean.getManagedServiceSiteDetails().add(serviceSiteDetails);
					});
			countryBeans.add(countryBean);
		});
		return countryBeans;
	}

	/**
	 * Method to process teamsdr commercials.
	 *
	 * @param task
	 * @param taskDataMap
	 */
	private void processTeamsDRCommercials(Task task, Map<String, Object> taskDataMap) {
		if (task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-provide-wbsglcc-details-endpoint")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-advanced-enrichment")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-pr-endpoint")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-po-endpoint")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-po-endpoint-release")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-pr-endpoint-install-support")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-po-endpoint-install-support")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-po-endpoint-release-install-support")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-confirm-material-availability")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-generate-endpoint-invoice")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-dispatch-endpoint")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-track-endpoint-delivery")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-configure-mg")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-install-endpoint")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-access-code-activation")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-config-access-code")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-srn-generation-endpoint")) {
			getScTeamsDRServiceCommercialDetails(task, taskDataMap, null);
			getBillingDateEndpoint(task, taskDataMap);
			getCpeBomMediaGateway(task, taskDataMap);
		}
	}

	/**
	 * Get CPE BOM Endpoint
	 *
	 * @param task
	 * @param taskDataMap
	 */
	private void getCpeBomMediaGateway(Task task, Map<String, Object> taskDataMap) {
		List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailId(task.getServiceId());
		scComponents.forEach(scComponent -> {
			scComponent.getScComponentAttributes().forEach(attribute -> {
				if (attribute.getAttributeName().equalsIgnoreCase("CPE Bom Response")) {
					LOGGER.info("CPE Bom Response componentId: {}", scComponent.getId());
					if (!attribute.getAttributeValue().isEmpty()) {
						LOGGER.info("CPE Bom Response id value: {}", attribute.getAttributeValue());
						Optional<ScAdditionalServiceParam> param = scAdditionalServiceParamRepository.
								findById(Integer.valueOf(attribute.getAttributeValue()));
						LOGGER.info("value from additional service: {}", param);
						if (param.isPresent() && !param.get().getValue().isEmpty()) {
							taskDataMap.put(AttributeConstants.CPE_BOM_RESPONSE, param.get().getValue());
							LOGGER.info("value set for cpe basis{}", scComponent.getId());
						}
					}
				}
			});
		});
	}

	private void getNodeName(Integer componentId, String attributeName, Map<String, Object> taskDataMap, String nodeName) {
		LOGGER.info("getNodeName method invoked for componentId::{},attributeName::{},nodeName::{}",componentId,attributeName,nodeName);
		ScComponentAttribute scComponentCPEAttribute=scComponentAttributesRepository.findFirstByScComponent_idAndAttributeName(componentId,attributeName);
		taskDataMap.put(nodeName, "");
		if(scComponentCPEAttribute!=null && scComponentCPEAttribute.getAttributeValue()!=null && !scComponentCPEAttribute.getAttributeValue().isEmpty()){
			LOGGER.info("getNodeName method::attributeName exists::{}",attributeName);
			taskDataMap.put(nodeName, scComponentCPEAttribute.getAttributeValue());
		}
	}

	private String replaceSpace(String input){
		return input.replaceAll("\\s+","");
	}
	
	private void getCpeDetailsBasedOnVendor(Integer serviceId, String serviceCode, Integer componentId,
			List<String> cpeTypeList, Map<String, Object> taskDataMap) {
		LOGGER.info("getCpeDetailsBasedOnVendor method invoked");
		List<Map<String,String>> vendorBasedServiceListMap=proCreationRepository.getIdAndVendorCode(serviceId, serviceCode, componentId,cpeTypeList);
		if(vendorBasedServiceListMap!=null && !vendorBasedServiceListMap.isEmpty()){
			LOGGER.info("VendorMap Size: {}",vendorBasedServiceListMap.size());
			CpeDetailBean cpeDetailBean=  new CpeDetailBean();
			List<CpeVendorBean> cpeVendorList= new ArrayList<>();
			for (Map<String,String> vendorBasedServiceMap : vendorBasedServiceListMap) {
				LOGGER.info("VendorCode: {}",vendorBasedServiceMap.get("vendorCode"));
				LOGGER.info("ProCreation Ids: {}",vendorBasedServiceMap.get("ids"));
				//String vendorCode=vendorBasedServiceMap.get("vendorCode");
				String proCreationIds=vendorBasedServiceMap.get("ids");
				String[] proCreationList= proCreationIds.split(",");
				CpeVendorBean cpeVendorBean = new CpeVendorBean();
				List<CpePrPoBean> cpePrPoBeanList= new ArrayList<>();
				getPrPoDetails(proCreationList,cpePrPoBeanList,cpeVendorBean);
		    	LOGGER.info("cpePrPoBeanList: {}",cpePrPoBeanList.size());
		    	cpeVendorBean.setCpePrPoBeanList(cpePrPoBeanList);
		    	cpeVendorList.add(cpeVendorBean);
			}
			LOGGER.info("cpeVendorList: {}",cpeVendorList.size());
			cpeDetailBean.setCpeVendorBeanList(cpeVendorList);
			taskDataMap.put("cpePrPoDetails", cpeDetailBean);
		}
	}

	private void getPrPoDetails(String[] proCreationList, List<CpePrPoBean> cpePrPoBeanList, CpeVendorBean cpeVendorBean) {
		LOGGER.info("getPrPoDetails method invoked");
		for(String proCreation:proCreationList){
    		Integer proCreationId=Integer.valueOf(proCreation);
    		LOGGER.info("proCreationId: {}",proCreationId);
    		Optional<ProCreation> proCreationOptional= proCreationRepository.findById(proCreationId);
    		if(proCreationOptional.isPresent()){
    			ProCreation cpeProCreation=proCreationOptional.get();
    			cpeVendorBean.setVendorCode(cpeProCreation.getVendorCode());
    			cpeVendorBean.setVendorName(cpeProCreation.getVendorName());
    			CpePrPoBean cpePrPoBean = new CpePrPoBean();
    			if("HARDWARE".equalsIgnoreCase(cpeProCreation.getType())){
		    		cpePrPoBean.setCpeSupplyHardwarePrNumber(cpeProCreation.getPrNumber());
		    		cpePrPoBean.setCpeSupplyHardwarePrDate(cpeProCreation.getPrCreatedDate()!=null?DateUtil.convertTimestampToDate(cpeProCreation.getPrCreatedDate()):null);
		    		cpePrPoBean.setCpeSupplyHardwarePrStatus(cpeProCreation.getPrStatus());
		    		cpePrPoBean.setCpeSupplyHardwarePrType(cpeProCreation.getPrCreatedType());
		    		cpePrPoBean.setCpeSupplyHardwarePrVendorName(cpeProCreation.getVendorName());
		    		cpePrPoBean.setCpeSupplyHardwarePoNumber(cpeProCreation.getPoNumber());
		    		cpePrPoBean.setCpeSupplyHardwarePoDate(cpeProCreation.getPoCreatedDate()!=null?DateUtil.convertTimestampToDate(cpeProCreation.getPoCreatedDate()):null);
		    		cpePrPoBean.setCpeSupplyHardwarePoStatus(cpeProCreation.getPoNumber());
    			}else if("LICENCE".equalsIgnoreCase(cpeProCreation.getType())){
    				cpePrPoBean.setCpeLicencePrNumber(cpeProCreation.getPrNumber());
    				cpePrPoBean.setCpeLicencePrDate(cpeProCreation.getPrCreatedDate()!=null?DateUtil.convertTimestampToDate(cpeProCreation.getPrCreatedDate()):null);
    				cpePrPoBean.setCpeLicencePrStatus(cpeProCreation.getPrStatus());
    				cpePrPoBean.setCpeLicencePoNumber(cpeProCreation.getPoNumber());
    				cpePrPoBean.setCpeLicencePoDate(cpeProCreation.getPoCreatedDate()!=null?DateUtil.convertTimestampToDate(cpeProCreation.getPoCreatedDate()):null);
    				cpePrPoBean.setCpeLicencePoStatus(cpeProCreation.getPoStatus());
    				cpePrPoBean.setCpeLicenseVendorName(cpeProCreation.getVendorName());
    				cpePrPoBean.setCpeSupplyLicencePrType(cpeProCreation.getPrCreatedType());
    			}
    			cpePrPoBeanList.add(cpePrPoBean);
    		}
    	}
		
	}

	/**
	 * @author vivek
	 *
	 * @param task
	 * @param taskDataMap
	 */
	private void getFileEngineerDetails(Task task, Map<String, Object> taskDataMap) {

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("complete-internal-cabling-ce")) {

			List<FieldEngineer> fieldEngineers = fieldEngineerRepository
					.findByServiceIdAndAppointmentType(task.getServiceId(), "cable-extension-ce");

			taskDataMap.put("feDetails", getFeDetails(fieldEngineers,"testing"));

		}
		else if ("conduct-lm-test-onnet-wireline".equalsIgnoreCase(task.getMstTaskDef().getKey())) {

			List<FieldEngineer> fieldEngineers = fieldEngineerRepository
					.findByServiceIdAndAppointmentType(task.getServiceId(), "cable-extension-ce");

			taskDataMap.put("feDetails", getFeDetails(fieldEngineers,"cabling"));
			
		}

		
		else if (task.getMstTaskDef().getKey().equalsIgnoreCase("complete-internal-cabling-pe")) {
			List<FieldEngineer> fieldEngineers = fieldEngineerRepository
					.findByServiceIdAndAppointmentType(task.getServiceId(), "cable-extension-pe");
			taskDataMap.put("feDetails", getFeDetails(fieldEngineers,"testing"));

		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("complete-cc-internal-cabling-ce")) {
			List<FieldEngineer> fieldEngineers = fieldEngineerRepository
					.findByServiceIdAndAppointmentType(task.getServiceId(), "cable-extension-ce");
			taskDataMap.put("feDetails", getFeDetails(fieldEngineers,"testing"));

		}
		
		 else if (task.getMstTaskDef().getKey().equalsIgnoreCase("complete-cable-swap-ce")) {
				List<FieldEngineer> fieldEngineers = fieldEngineerRepository
						.findByServiceIdAndAppointmentType(task.getServiceId(), "cable-swap-ce-man");
				taskDataMap.put("feDetails", getFeDetails(fieldEngineers));

			}
		
		 else if (task.getMstTaskDef().getKey().equalsIgnoreCase("complete-cable-swap-pe")) {
			List<FieldEngineer> fieldEngineers = fieldEngineerRepository
					.findByServiceIdAndAppointmentType(task.getServiceId(), "cable-swap-pe-man");
			taskDataMap.put("feDetails", getFeDetails(fieldEngineers));

		}

	}

	/**
	 * @author vivek
	 *
	 * @param fieldEngineers
	 * @param string
	 * @return
	 */
	private List<FieldEngineerDetails> getFeDetails(List<FieldEngineer> fieldEngineers, String type) {

		List<FieldEngineerDetails> details = new ArrayList<>();

		if (fieldEngineers != null && !fieldEngineers.isEmpty()) {

			fieldEngineers.forEach(fe -> {
				if(!type.equalsIgnoreCase(fe.getWorkType())) {
				FieldEngineerDetails fieldEngineer = new FieldEngineerDetails();
				fieldEngineer.setFeName(fe.getName());
				fieldEngineer.setFeMobileNumber(fe.getMobile());
				fieldEngineer.setFeEmail(fe.getEmail());
				fieldEngineer.setSecondaryName(fe.getSecondaryname());
				fieldEngineer.setSecondaryContactNumber(fe.getSecondarymobile());
				fieldEngineer.setSecondaryEmailId(fe.getSecondaryemail());
				fieldEngineer.setFeType(fe.getFeType());
				fieldEngineer.setWorkType(fe.getWorkType());
				fieldEngineer.setType(fe.getAppointmentType());
				details.add(fieldEngineer);
				}

			});

		}

		return details;

	}

	private List<FieldEngineerDetails> getFeDetails(List<FieldEngineer> fieldEngineers) {

		List<FieldEngineerDetails> details = new ArrayList<>();

		if (fieldEngineers != null && !fieldEngineers.isEmpty()) {

			fieldEngineers.forEach(fe -> {
				FieldEngineerDetails fieldEngineer = new FieldEngineerDetails();
				fieldEngineer.setFeName(fe.getName());
				fieldEngineer.setFeMobileNumber(fe.getMobile());
				fieldEngineer.setFeEmail(fe.getEmail());
				fieldEngineer.setSecondaryName(fe.getSecondaryname());
				fieldEngineer.setSecondaryContactNumber(fe.getSecondarymobile());
				fieldEngineer.setSecondaryEmailId(fe.getSecondaryemail());
				fieldEngineer.setFeType(fe.getFeType());
				fieldEngineer.setWorkType(fe.getWorkType());
				fieldEngineer.setType(fe.getAppointmentType());
				details.add(fieldEngineer);

			});

		}

		return details;

	}
		
	private Timestamp getCpeAmcEndDate(Timestamp createdTime, Double orderTermInMonths) {
		Timestamp cpeAmcEndDate = null;
		try {
			if (orderTermInMonths != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(createdTime);
				//cal.add(Calendar.MONTH, orderTermInMonths);
				cal.add(Calendar.DAY_OF_WEEK, getOrderTermInMonthsToDays(orderTermInMonths));
				cpeAmcEndDate = new Timestamp(cal.getTime().getTime());
			}
			return cpeAmcEndDate;
		} catch (Exception e) {
			LOGGER.info("ERROR in getCpeAmcEndDate returning new Timestamp(0): {}", e);
			return createdTime;
		}

	}
	
	

	@Override
	public Map<String, Object> getTaskData(Task task,String siteType) throws TclCommonException {
		LOGGER.info("get Task Data method started with Key {} ", task.getMstTaskDef().getKey());
		Integer serviceId = task.getServiceId();
		Map<String, Object> varMap = null;
		Map<String, Object> taskDataMap = attributeService.getTaskAttributes(task.getMstTaskDef().getKey(), serviceId,siteType);
		LOGGER.info("Variable map has values ; {} ", varMap);
		
		
		taskDataMap.put("systemDate", new Date());
		return taskDataMap;
	}

	private void getBillingDate(Task task, Map<String, Object> taskDataMap) {
		TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(task.getServiceId(), "install-cpe", "A");

		if (taskPlan != null) {
			LocalDateTime cpeTime = taskPlan.getPlannedStartTime().toLocalDateTime().minusDays(7);

			LOGGER.info("cpe time:{}", cpeTime);

			if (cpeTime.isAfter(LocalDateTime.now())) {
				taskDataMap.put("cpeBillStartDate", cpeTime);

			} else {
				taskDataMap.put("cpeBillStartDate", Timestamp.valueOf(LocalDateTime.now().plusDays(1)));

			}

		}
	}
	
	private void getSdwanBillingDate(Task task, Map<String, Object> taskDataMap) {
		TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(task.getServiceId(), "sdwan-install-cpe", "A");

		if (taskPlan != null) {
			LocalDateTime cpeTime = taskPlan.getPlannedStartTime().toLocalDateTime().minusDays(7);

			LOGGER.info("cpe time:{}", cpeTime);

			if (cpeTime.isAfter(LocalDateTime.now())) {
				taskDataMap.put("cpeBillStartDate", cpeTime);

			} else {
				taskDataMap.put("cpeBillStartDate", Timestamp.valueOf(LocalDateTime.now().plusDays(1)));

			}

		}
	}

	private void getmastCatalogueDetails(Task task, Map<String, Object> taskDataMap) throws TclCommonException {
		LOGGER.info("getmastCatalogueDetails method invoked for serviceid::{}",task.getServiceId());
		CpeBomResource bomResources = getBomResources(task);
		List<MstCatalogueBean> catalogueBeans = new ArrayList<>();

		if (bomResources != null) {
			LOGGER.info("ServiceId::{} with CPEBomName::{}",task.getServiceId(),bomResources.getBomName());
			List<MstCostCatalogue> mstCostCatalogues = mstCostCatalogueRepository
					.findByDistinctBundledBom(bomResources.getBomName());
			Double exchangeValue = forexService.convertCurrency(Currencies.USD, Currencies.INR);
			if (!mstCostCatalogues.isEmpty()) {
				mstCostCatalogues.stream().forEach(mstCat -> {
					MstCatalogueBean catalogueBean = new MstCatalogueBean();
					BeanUtils.copyProperties(mstCat, catalogueBean);
					Double incrementalPrice = mstCat.getIncrementalRate();

					if (exchangeValue != null) {
						Double totalTPinINR = ((exchangeValue + incrementalPrice) * mstCat.getTotalPriceDdp());
						catalogueBean.setTotalTPinINR(totalTPinINR);
						catalogueBean.setFxSpotRate(exchangeValue);
					}
					catalogueBeans.add(catalogueBean);
				});

			}
		}

		taskDataMap.put("mstCostCatalogues", catalogueBeans);

	}
	

	private void getCpeCostDetails(Task task, Map<String, Object> taskDataMap, Integer componentId, String vendorCode) throws TclCommonException {
		LOGGER.info("getCpeCostDetails method invoked");
		List<CpeCostDetails> cpeCostDetailList = null;
		if(vendorCode!=null){
			LOGGER.info("vendorCode exists::{}",vendorCode);
			cpeCostDetailList = cpeCostDetailsRepository
					.findByServiceIdAndServiceCodeAndComponentIdAndVendorCode(task.getServiceId(),task.getServiceCode(),componentId,vendorCode);
		}else{
			LOGGER.info("vendorCode not exists");
			cpeCostDetailList = cpeCostDetailsRepository
					.findByServiceIdAndServiceCodeAndComponentId(task.getServiceId(),task.getServiceCode(),componentId);
		}
		List<CpeCostDetailBean> cpeCostDetailBeans = new ArrayList<>();
		if(cpeCostDetailList!=null && !cpeCostDetailList.isEmpty()){
			cpeCostDetailList.stream().forEach(cpeCostCatalog ->{
				LOGGER.info("CpeCostDetails Id method invoked::{}",cpeCostCatalog.getId());
				CpeCostDetailBean cpeCostDetailBean = new CpeCostDetailBean();
				BeanUtils.copyProperties(cpeCostCatalog, cpeCostDetailBean);
				if(cpeCostCatalog.getCurrency().equalsIgnoreCase("INR")){
					LOGGER.info("CpeCostDetails Id::{}, Cpe Currency::{}",cpeCostCatalog.getId(),cpeCostCatalog.getCurrency());
					Double exchangeValue = forexService.convertCurrency(Currencies.USD, Currencies.INR);
					if (exchangeValue != null) {
						LOGGER.info("ExchangeValue::{}",exchangeValue);
						Double totalTPinINR = ((exchangeValue+ cpeCostCatalog.getIncrementalRate()) * cpeCostCatalog.getCalculatedPrice());
						cpeCostDetailBean.setTotalTPinINR(totalTPinINR);
						cpeCostDetailBean.setFxSpotRate(exchangeValue);
					}
				}
				cpeCostDetailBeans.add(cpeCostDetailBean);
			});
		}
		taskDataMap.put("cpeCostDetails", cpeCostDetailBeans);
	}
		
	private void getScWebexServiceCommercialDetails(Task task, Map<String, Object> taskDataMap) throws TclCommonException{
		List<ScWebexServiceCommercialBean> scWebexServiceCommercialBeans = new ArrayList<>();
		List<IScWebexServiceCommercial> scWebexServiceCommercials = null;
		List<String> type = new ArrayList<String>();
		if(task.getMstTaskDef().getKey().contains("endpoint") 
				|| task.getMstTaskDef().getKey().equals("ucwb-confirm-material-availability")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-access-code-activation")
				||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-config-access-code")) {
			type.add("Endpoint");
		} else {
			type.add("License");
			//type.add("Subscription");
		}
		scWebexServiceCommercials = scWebexServiceCommercialRepository
				.getByScServiceDetailIdAndComponentType(task.getServiceId(), type);
		if (scWebexServiceCommercials != null) {
            Double totalUnitListPrice = scWebexServiceCommercials.stream().collect(Collectors.summingDouble(detail -> detail.getCiscoUnitListPrice()));
            Double totalUnitNetPrice = scWebexServiceCommercials.stream().collect(Collectors.summingDouble(detail -> detail.getCiscoUnitNetPrice()));
            Double totalUnitDiscountPrice = scWebexServiceCommercials.stream().collect(Collectors.summingDouble(detail -> detail.getCiscoDiscntPrct()));

            taskDataMap.put("totalUnitListPrice", String.format("%.2f",totalUnitListPrice));
            taskDataMap.put("totalUnitNetPrice", String.format("%.2f",totalUnitNetPrice));
            taskDataMap.put("totalUnitDiscountPrice", String.format("%.2f",totalUnitDiscountPrice));
        }
		if (Objects.nonNull(scWebexServiceCommercials) && !scWebexServiceCommercials.isEmpty()) {
			scWebexServiceCommercials.stream().forEach(webexComm -> {
				ScWebexServiceCommercialBean scWebexServiceCommercialBean = new ScWebexServiceCommercialBean();
				BeanUtils.copyProperties(webexComm, scWebexServiceCommercialBean);
				if(task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-confirm-material-availability")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-dispatch-endpoint")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-track-endpoint-delivery")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-install-endpoint")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-configure-endpoint")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-access-code-activation")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-config-access-code")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("ucwb-srn-generation-endpoint")
						) {
					scWebexServiceCommercialBean.setMaterialsList(
						populateWebexEndpointComponent(task.getServiceId(), webexComm.getComponentName()));
				}
				scWebexServiceCommercialBeans.add(scWebexServiceCommercialBean);
			});
		}
		taskDataMap.put("scWebexServiceCommercial", scWebexServiceCommercialBeans);
	}

	/**
	 * Method to process scTeamsDRServiceCommercials
	 * @param task
	 * @param taskDataMap
	 */
	private List<ScTeamsDRServiceCommercialBean> getScTeamsDRServiceCommercialDetails(Task task, Map<String, Object> taskDataMap,String componentName) {
		List<ScTeamsDRServiceCommercialBean> scTeamsDrServiceCommercialBeans = new ArrayList<>();
		List<ScTeamsDRServiceCommercial> scTeamsDRServiceCommercials = null;
		List<String> type = new ArrayList<String>();
		if (TeamsDRFulfillmentConstants.MEDIA_GATEWAY
				.equalsIgnoreCase(task.getScServiceDetail().getErfPrdCatalogOfferingName()))
			type.add(TeamsDRFulfillmentConstants.ENDPOINT);
		else if (Objects.nonNull(task.getScServiceDetail().getErfPrdCatalogOfferingName()) && task.getScServiceDetail()
				.getErfPrdCatalogOfferingName().contains(TeamsDRFulfillmentConstants.PLAN))
			type.add(TeamsDRFulfillmentConstants.PLAN);

		//		else {
		//			type.add("License");
		//			//type.add("Subscription");
		//		}
		if(Objects.isNull(componentName)){
			scTeamsDRServiceCommercials = scTeamsDRServiceCommercialRepository
					.findByScServiceDetailIdAndComponentTypeIn(task.getServiceId(), type);
		}else{
			scTeamsDRServiceCommercials = scTeamsDRServiceCommercialRepository
					.findByScServiceDetailIdAndComponentNameAndComponentTypeIn(task.getServiceId(), componentName,type);
		}
		if (scTeamsDRServiceCommercials != null) {
			Double totalMrc = scTeamsDRServiceCommercials.stream().map(ScTeamsDRServiceCommercial::getMrc).filter(Objects::nonNull)
					.mapToDouble(mrc -> mrc).sum();
			Double totalNrc = scTeamsDRServiceCommercials.stream().map(ScTeamsDRServiceCommercial::getNrc).filter(Objects::nonNull)
					.mapToDouble(nrc -> nrc).sum();
			Double totalArc = scTeamsDRServiceCommercials.stream().map(ScTeamsDRServiceCommercial::getArc).filter(Objects::nonNull)
					.mapToDouble(arc -> arc).sum();
			Double totalEffectiveUsage =  scTeamsDRServiceCommercials.stream().map(ScTeamsDRServiceCommercial::getUsage).filter(Objects::nonNull)
					.mapToDouble(usage -> usage).sum();
			Double totalEffectiveOverage =  scTeamsDRServiceCommercials.stream().map(ScTeamsDRServiceCommercial::getOverage).filter(Objects::nonNull)
					.mapToDouble(overage -> overage).sum();

			taskDataMap.put("totalUnitListPrice", String.format("%.2f",totalMrc));
			taskDataMap.put("totalUnitNetPrice", String.format("%.2f",totalNrc));
			taskDataMap.put("totalUnitDiscountPrice", String.format("%.2f",totalArc));
			taskDataMap.put("totalEffectiveUsage", String.format("%.2f",totalEffectiveUsage));
			taskDataMap.put("totalEffectiveOverage", String.format("%.2f",totalEffectiveOverage));
		}
		if (Objects.nonNull(scTeamsDRServiceCommercials) && !scTeamsDRServiceCommercials.isEmpty()) {
			scTeamsDRServiceCommercials.stream().forEach(teamsdrCommercial -> {
				ScTeamsDRServiceCommercialBean scTeamsDRServiceCommercialBean = new ScTeamsDRServiceCommercialBean();
				scTeamsDRServiceCommercialBean.setComponentId(teamsdrCommercial.getId());
				scTeamsDRServiceCommercialBean.setComponentName(teamsdrCommercial.getComponentName());
				scTeamsDRServiceCommercialBean.setComponentDesc(teamsdrCommercial.getChargeItem());
				scTeamsDRServiceCommercialBean.setHsnCode(teamsdrCommercial.getHsnCode());
				scTeamsDRServiceCommercialBean.setQuantity(teamsdrCommercial.getQuantity());
				scTeamsDRServiceCommercialBean.setContractType(teamsdrCommercial.getContractType());
				scTeamsDRServiceCommercialBean.setMrc(teamsdrCommercial.getMrc());
				scTeamsDRServiceCommercialBean.setNrc(teamsdrCommercial.getNrc());
				scTeamsDRServiceCommercialBean.setArc(teamsdrCommercial.getArc());
				scTeamsDRServiceCommercialBean.setTcv(teamsdrCommercial.getTcv());
				scTeamsDRServiceCommercialBean.setEffectiveUsage(teamsdrCommercial.getUsage());
				scTeamsDRServiceCommercialBean.setEffectiveOverage(teamsdrCommercial.getOverage());
				Optional<MstCostCatalogue> mstCostCatalogue = mstCostCatalogueRepository.
						findByBundledBomAndCategoryForRental(teamsdrCommercial.getComponentName(),teamsdrCommercial.getChargeItem()).stream().findFirst();

				mstCostCatalogue.ifPresent(costCatalogue -> {
					//scTeamsDRServiceCommercialBean.setSupportType(mstCostCatalogue.get().g);
					scTeamsDRServiceCommercialBean.setSaleMaterialCode(costCatalogue.getSaleMaterialCode());
					scTeamsDRServiceCommercialBean.setRentalMaterialCode(costCatalogue.getRentalMaterialCode());
					scTeamsDRServiceCommercialBean.setVendorName(costCatalogue.getVendorName());
				});
				if(task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-confirm-material-availability")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-dispatch-endpoint")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-track-endpoint-delivery")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-install-endpoint")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-configure-mg")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-access-code-activation")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-config-access-code")
						||task.getMstTaskDef().getKey().equalsIgnoreCase("teamsdr-srn-generation-endpoint")
				) {
					scTeamsDRServiceCommercialBean.setMaterialsList(
							populateWebexEndpointComponent(task.getServiceId(), teamsdrCommercial.getComponentName()));
				}
				scTeamsDrServiceCommercialBeans.add(scTeamsDRServiceCommercialBean);
			});
		}
		if(Objects.nonNull(componentName)){
			taskDataMap.put("scTeamsDRServiceCommercial", scTeamsDrServiceCommercialBeans);
		}
		return scTeamsDrServiceCommercialBeans;
	}

	private List<EndpointMaterialsBean> populateWebexEndpointComponent(Integer serviceId, String component) {
		List<ScComponent> scComponents = scComponentRepository.findByScServiceDetailIdAndComponentName(serviceId, component);
		List<EndpointMaterialsBean> endpointBeans = new ArrayList<>();
		for(ScComponent scComponent : scComponents) {
			EndpointMaterialsBean endpointBean = new EndpointMaterialsBean();
			endpointBean.setId(scComponent.getId());
			List<String> attributeNames = new ArrayList<>();
			attributeNames.add("serialNumber");
			attributeNames.add("endpointDeliveryDate");
			attributeNames.add("endpointEndOfSale");
			attributeNames.add("endpointEndOfLife");
			attributeNames.add("endpointAmcStartDate");
			attributeNames.add("endpointAmcEndDate");
			List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository.findByScComponentAndAttributeNameIn(scComponent, attributeNames);
			for(ScComponentAttribute scComponentAttribute : scComponentAttributes) {
				switch(scComponentAttribute.getAttributeName()) {
					case "serialNumber":
						endpointBean.setSerialNumber(scComponentAttribute.getAttributeValue());
						break;
					case "endpointDeliveryDate":
						endpointBean.setDeliveryDate(scComponentAttribute.getAttributeValue());
						break;
					case "endpointEndOfSale":
						endpointBean.setEndOfSale(scComponentAttribute.getAttributeValue());
						break;
					case "endpointEndOfLife":
						endpointBean.setEndOfLife(scComponentAttribute.getAttributeValue());
						break;
					case "endpointAmcStartDate":
						endpointBean.setAmcStartDate(scComponentAttribute.getAttributeValue());
						break;
					case "endpointAmcEndDate":
						endpointBean.setAmcEndDate(scComponentAttribute.getAttributeValue());
						break;
				}
			}			
			endpointBeans.add(endpointBean);
		}
		return endpointBeans;
	}

	private void getSlaValues(Integer serviceId, Map<String, Object> taskDataMap) {
		List<ScServiceSla> slas = scServiceSlaRepository.findAllByScServiceDetail_Id(serviceId);
		if(!CollectionUtils.isEmpty(slas))
			slas.forEach(scServiceSla -> {
				if(scServiceSla.getSlaComponent().equalsIgnoreCase("Round Trip Delay (RTD)"))
					taskDataMap.put("roundTripDelay",scServiceSla.getSlaValue());
				if(scServiceSla.getSlaComponent().equalsIgnoreCase("Packet Drop"))
					taskDataMap.put("packetDrop",scServiceSla.getSlaValue());
				if(scServiceSla.getSlaComponent().equalsIgnoreCase("Network Uptime") || scServiceSla.getSlaComponent().toLowerCase().contains("service availability"))
					taskDataMap.put("networkUptime",scServiceSla.getSlaValue());
				if(scServiceSla.getSlaComponent().toLowerCase().contains("jitter"))
					taskDataMap.put("jitter",scServiceSla.getSlaValue());
				if(scServiceSla.getSlaComponent().toLowerCase().contains("mean time"))
					taskDataMap.put("meanTimeToRestore",scServiceSla.getSlaValue());
				if(scServiceSla.getSlaComponent().toLowerCase().contains("packet delivery ratio"))
					taskDataMap.put("packetDeliveryRatio",scServiceSla.getSlaValue());
				if(scServiceSla.getSlaComponent().toLowerCase().contains("time to restore"))
					taskDataMap.put("timeToRestore",scServiceSla.getSlaValue());
			});
	}


	private boolean isDifferentSite(ScServiceDetail scServiceDetail, Map<String, Object> taskDataMap) {
		LOGGER.info("getDestinationState:{} and getSourceState:{}", taskDataMap.get("destinationState"),
				taskDataMap.get("sourceState"));

		if (taskDataMap.get("destinationState") != null && taskDataMap.get("sourceState") != null
				&& !((String) taskDataMap.get("destinationState")).equalsIgnoreCase((String)taskDataMap.get("sourceState"))) {
			return true;
		} else {
			return false;
		}
	}

	private void constructBomDetails(Task task, Map<String, Object> taskDataMap) {
		String lastMile = StringUtils.trimToEmpty((String)taskDataMap.get("lmType"));		
		
		if (lastMile.contains("RF")) {			
			LOGGER.info("constructBomDetails------------> :{}",lastMile);
			String rfMake = StringUtils.trimToEmpty((String)taskDataMap.get("rfMake"));

			if (rfMake != null) {				
				LOGGER.info("rfMake------------> :{}",rfMake);

				List<VwBomMaterialDetail> vwBomMaterialDetails = vwBomMaterialDetailRepository.findByMake(rfMake);
				List<BomMaterialDetailsBean> bomMaterialDetailsBeans = new ArrayList<>();
				vwBomMaterialDetails.forEach(bomMaterialDetail -> {
					groupRfBomDetails(bomMaterialDetail, bomMaterialDetailsBeans);
				});
				taskDataMap.put("bomDetails", bomMaterialDetailsBeans);
			}
		} else {			
			LOGGER.info("mux------------> ");
			String muxMake = StringUtils.trimToEmpty((String)taskDataMap.get("muxMake"));

			if (muxMake != null) {
				LOGGER.info("scComponentAttribute------------> :{}",muxMake);

				List<VwMuxBomMaterialDetail> vmMuxBomMaterialDetails = vwBomMuxDetailsRepository.findByMake(muxMake);
				
				LOGGER.info("vmMuxBomMaterialDetails------------> :{}",vmMuxBomMaterialDetails);

				List<VwMuxBomDetailBean> bomMaterialDetailsBeans = new ArrayList<>();
				vmMuxBomMaterialDetails.forEach(bomMaterialDetail -> {
					groupBomValues(bomMaterialDetailsBeans, bomMaterialDetail, taskDataMap);
				});
				taskDataMap.put("bomDetails", bomMaterialDetailsBeans);

			}

		}

	}


	private void processMuxBomDetails(String localoopBand, Map<String, Object> taskDataMap) {
		if(localoopBand=="")localoopBand="10";
		List<VwMuxBomMaterialDetail> vwMuxBomMaterialDetails = vwBomMuxDetailsRepository
				.findByBandwidthLessThan(Double.valueOf(localoopBand));
		
		if (vwMuxBomMaterialDetails.isEmpty()) {

			vwMuxBomMaterialDetails = vwBomMuxDetailsRepository.findAll();

		}
		
		List<VwMuxBomDetailBean> bomMaterialDetailsBeans = new ArrayList<>();

		vwMuxBomMaterialDetails.forEach(bomMaterialDetail -> {
			groupBomValues(bomMaterialDetailsBeans, bomMaterialDetail, taskDataMap);
		});
		
		
		taskDataMap.put("bomDetails", bomMaterialDetailsBeans);

	}
	
	

	private void groupBomValues(List<VwMuxBomDetailBean> bomMaterialDetailsBeans, VwMuxBomMaterialDetail bomMaterialDetail, Map<String, Object> taskDataMap) {
		VwMuxBomDetailBean bean = new VwMuxBomDetailBean();
		BeanUtils.copyProperties(bomMaterialDetail, bean);
		bean.setMake(bomMaterialDetail.getMake());
	
		if (bomMaterialDetailsBeans.contains(bean)) {
			bean = bomMaterialDetailsBeans.get(bomMaterialDetailsBeans.indexOf(bean));
			MaterialMasterBean masterBean = new MaterialMasterBean();
			BeanUtils.copyProperties(bomMaterialDetail, masterBean);
			bean.getMaterials().add(masterBean);
	
		} else {
	
			bomMaterialDetailsBeans.add(bean);
		}
	
	}

	private void processRfBomDetails(String localoopBand, Map<String, Object> taskDataMap,
			String scenarioType) {
		List<BomMaterialDetailsBean> bomMaterialDetailsBeans = new ArrayList<>();

		if (scenarioType != null) {
			List<VwBomMaterialDetail> bomMaterialDetails = vwBomMaterialDetailRepository.findByBandwidthLessThan("RF",
					Double.valueOf(localoopBand));

			LOGGER.info("scenarioType:{} and localoopBand:{} and response size------------> :{}", scenarioType,
					localoopBand, bomMaterialDetails.size());
			
			
			if (!bomMaterialDetails.isEmpty()) {

				List<VwBomMaterialDetail> filterbomMaterialDetails = bomMaterialDetails.stream()
						.filter(bom -> scenarioType.trim().contains(bom.getScenarioType().trim())).collect(Collectors.toList());

				LOGGER.info(" after filter  scenarioType:{} and localoopBand:{} and response size------------> :{}",
						scenarioType, localoopBand, filterbomMaterialDetails.size());

				filterbomMaterialDetails.forEach(bomMaterialDetail -> {
					groupRfBomDetails(bomMaterialDetail, bomMaterialDetailsBeans);

				});
				taskDataMap.put("bomDetails", bomMaterialDetailsBeans);

			}
		}
		
		 if (bomMaterialDetailsBeans.isEmpty()) {
			List<VwBomMaterialDetail> bomMaterialDetails = vwBomMaterialDetailRepository.findByBomType("RF");

			bomMaterialDetails.forEach(bomMaterialDet -> {
				groupRfBomDetails(bomMaterialDet, bomMaterialDetailsBeans);

			});
			taskDataMap.put("bomDetails", bomMaterialDetailsBeans);
		}

		else if (bomMaterialDetailsBeans.isEmpty()) {
			List<VwBomMaterialDetail> bomMaterialDetail = vwBomMaterialDetailRepository.findAll();
			bomMaterialDetail.forEach(bomMaterialDet -> {
				groupRfBomDetails(bomMaterialDet, bomMaterialDetailsBeans);

			});
			taskDataMap.put("bomDetails", bomMaterialDetailsBeans);

		}

	}

	private void groupRfBomDetails(VwBomMaterialDetail bomMaterialDetail,
			 List<BomMaterialDetailsBean> bomMaterialDetailsBeans) {
		BomMaterialDetailsBean bean = new BomMaterialDetailsBean();
		BeanUtils.copyProperties(bomMaterialDetail, bean);
		bean.setMake(bomMaterialDetail.getMake());

		if (bomMaterialDetailsBeans.contains(bean)) {
			bean = bomMaterialDetailsBeans.get(bomMaterialDetailsBeans.indexOf(bean));
			MaterialMasterBean masterBean = new MaterialMasterBean();
			BeanUtils.copyProperties(bomMaterialDetail, masterBean);
			bean.getMaterials().add(masterBean);

		} else {

			bomMaterialDetailsBeans.add(bean);
		}		
	}

	private String getTaskType(String key) {     
    	String value ="";
    	if(key.contains("arrange-field-engineer")) {
    		value = key.replace("arrange-field-engineer-","");    
    	}else if(key.contains("customer-appointment")) {
    		value = key.replace("customer-appointment-","");      		
    	}else if(key.contains("select-vendor")) {
    		value = key.replace("select-vendor-","");
    	}
        return value;
    }


    private Map<String, Object> processMstAppointmentDocuments(Task task, Map<String, Object> taskDataMap, String type) {
        List<MstAppointmentDocumentBean> mstAppointmentDocumentBeanList =
                attachmentService.getMstAppointmentForServiceAndType(task.getServiceId(), type);
        
        if(mstAppointmentDocumentBeanList==null || mstAppointmentDocumentBeanList.isEmpty()) {
        	mstAppointmentDocumentBeanList = attachmentService.getMstAppointmentForServiceAndType(task.getServiceId(), "ss");
        }
        taskDataMap.put("appointmentDocuments", mstAppointmentDocumentBeanList);
        return taskDataMap;
    }

    private List<PlannedEventBean> getAvailableSlotsForMux(Integer taskId) {
        List<PlannedEventBean> plannedEventBeanList = new ArrayList<>();
        Optional.ofNullable(taskService.getTaskById(taskId))
                .ifPresent(task -> plannedEventBeanList.addAll(plannedEventRepository.findAllByServiceCode(task.getServiceCode())
                        .stream()
                        .filter(plannedEvent -> "C-CLOSED".equalsIgnoreCase(plannedEvent.getOptimusStatus()))
                        .map(PlannedEventBean::new)
                        .collect(Collectors.toList())));
        return plannedEventBeanList;
    }
    
	public CpeBomResource getBomResources(Task task) throws TclCommonException {
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
		List<ScServiceAttribute> cpeChassisAttr = scServiceDetail.getScServiceAttributes().stream()
				.filter(attr -> attr.getAttributeName().equalsIgnoreCase("CPE Basic Chassis"))
				.collect(Collectors.toList());
		ScServiceAttribute scServiceAttribute = cpeChassisAttr.stream().findAny().orElse(null);
		CpeBomResource bomResources = null;
		if (scServiceAttribute != null) {
			String serviceParamId = scServiceAttribute.getAttributeValue();
			if (StringUtils.isNotBlank(serviceParamId)) {
				Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
						.findById(Integer.valueOf(serviceParamId));
				if (scAdditionalServiceParam.isPresent()) {
					String bomResponse = scAdditionalServiceParam.get().getValue();

					CpeBomResource[] bomResourcess = Utils.convertJsonToObject(bomResponse, CpeBomResource[].class);
				    bomResources = bomResourcess[0];
				}
			}

		}
		return bomResources;
	}
	
	
	public CpeBomResource getBomResourcesRenewal(Integer id,RenewalCommercialVettingDetails details) throws TclCommonException {
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(id);
		details.setServiceCode(scServiceDetail.get().getUuid());
		List<ScServiceAttribute> cpeChassisAttr = scServiceDetail.get().getScServiceAttributes().stream()
				.filter(attr -> attr.getAttributeName().equalsIgnoreCase("CPE Basic Chassis"))
				.collect(Collectors.toList());
		ScServiceAttribute scServiceAttribute = cpeChassisAttr.stream().findAny().orElse(null);
		CpeBomResource bomResources = null;
		if (scServiceAttribute != null) {
			String serviceParamId = scServiceAttribute.getAttributeValue();
			if (StringUtils.isNotBlank(serviceParamId)) {
				Optional<ScAdditionalServiceParam> scAdditionalServiceParam = scAdditionalServiceParamRepository
						.findById(Integer.valueOf(serviceParamId));
				if (scAdditionalServiceParam.isPresent()) {
					String bomResponse = scAdditionalServiceParam.get().getValue();

					CpeBomResource[] bomResourcess = Utils.convertJsonToObject(bomResponse, CpeBomResource[].class);
					bomResources = bomResourcess[0];
				}
			}
		}
		return bomResources;
	}
	public void getCosDatailsAttributes(Integer serviceId, Map<String, Object> taskData) {
		LOGGER.info("getCosDatailsAttributes::{}",serviceId);
		List<CosDetail> cosDetails = new ArrayList<>();
		List<String> attributeNames = Arrays.asList("cos 1", "cos 2", "cos 3", "cos 4", "cos 5", "cos 6",
				"cos 1 criteria", "cos 1 criteria value", "cos 2 criteria", "cos 2 criteria value",
				"cos 3 criteria", "cos 3 criteria value", "cos 4 criteria", "cos 4 criteria value",
				"cos 5 criteria", "cos 5 criteria value", "cos 6 criteria", "cos 6 criteria value");

		List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(serviceId, attributeNames);

		cosDetails.add(getCosDetailBean(scServiceAttributes, "cos 1", "cos 1 criteria", "cos 1 criteria value"));
		cosDetails.add(getCosDetailBean(scServiceAttributes, "cos 2", "cos 2 criteria", "cos 2 criteria value"));
		cosDetails.add(getCosDetailBean(scServiceAttributes, "cos 3", "cos 3 criteria", "cos 3 criteria value"));
		cosDetails.add(getCosDetailBean(scServiceAttributes, "cos 4", "cos 4 criteria", "cos 4 criteria value"));
		cosDetails.add(getCosDetailBean(scServiceAttributes, "cos 5", "cos 5 criteria", "cos 5 criteria value"));
		cosDetails.add(getCosDetailBean(scServiceAttributes, "cos 6", "cos 6 criteria", "cos 6 criteria value"));


		if (!cosDetails.isEmpty()) {
			taskData.put("cosDetails", cosDetails);
		}
		LOGGER.info("getCosDatailsAttributes persisted for serviceId::{}",serviceId);
	}

	private CosDetail getCosDetailBean(List<ScServiceAttribute> scServiceAttributes, String cosKey,String criteriaTypeKey, String criteriaKey) {
		LOGGER.info("Enter getCosDetailBean");
		CosDetail cosDetail = new CosDetail();
		cosDetail.setCosKey(cosKey);
		cosDetail.setCriteriaKey(criteriaKey);
		cosDetail.setCriteriaTypeKey(criteriaTypeKey);
		if(scServiceAttributes.stream().filter(serviceAttr -> serviceAttr.getAttributeName().
				equalsIgnoreCase(cosKey)).findFirst().isPresent()){
			cosDetail.setCosValue(scServiceAttributes.stream().filter(serviceAttr -> serviceAttr.getAttributeName().
					equalsIgnoreCase(cosKey)).findFirst().get().getAttributeValue());
		}
		if (scServiceAttributes.stream().filter(serviceAttr -> serviceAttr.getAttributeName().
				equalsIgnoreCase(criteriaKey)).findFirst().isPresent()) {
			cosDetail.setCriteriaValue(scServiceAttributes.stream().filter(serviceAttr ->
					serviceAttr.getAttributeName().equalsIgnoreCase(criteriaKey)).findFirst().get().getAttributeValue());
		}
		if (scServiceAttributes.stream().filter(serviceAttr -> serviceAttr.getAttributeName().
				equalsIgnoreCase(criteriaTypeKey)).findFirst().isPresent()) {
			cosDetail.setCriteriaTypeValue(scServiceAttributes.stream().filter(serviceAttr ->
					serviceAttr.getAttributeName().equalsIgnoreCase(criteriaTypeKey)).findFirst().get().getAttributeValue());

		}
		return cosDetail;
	}

	private List<CpeDetailsBean> getCpeDetails(List<ScComponent> scComponents) {
		LOGGER.info("getCpeDetails method invoked:: {}", scComponents.size());
		List<CpeDetailsBean> cpeDetailsBeanList = new ArrayList<>();
		for(ScComponent scComponent : scComponents) {
			LOGGER.info("scComponent details for scComponent id {}", scComponent.getId());
			CpeDetailsBean cpeDetailsBean = new CpeDetailsBean();
			cpeDetailsBean.setComponentId(scComponent.getId());
			scComponent.getScComponentAttributes().forEach(attribute->{
				if(attribute.getAttributeName().equalsIgnoreCase("EQUIPMENT_MODEL")){
					cpeDetailsBean.setCpeModel(attribute.getAttributeValue());
				}
				if(attribute.getAttributeName().equalsIgnoreCase("cpePartCode")){
					cpeDetailsBean.setPartCode(attribute.getAttributeValue());
				}
				if(attribute.getAttributeName().equalsIgnoreCase("cpeDeviceName")){
					cpeDetailsBean.setCpeDeviceName(attribute.getAttributeValue());
				}
				if(attribute.getAttributeName().equalsIgnoreCase("L2_PORTS")){
					cpeDetailsBean.setL2Port(attribute.getAttributeValue());
				}
				if(attribute.getAttributeName().equalsIgnoreCase("L3_PORTS")){
					cpeDetailsBean.setL3Port(attribute.getAttributeValue());
				}
				if(attribute.getAttributeName().equalsIgnoreCase("cpeType")){
					cpeDetailsBean.setCpeType(attribute.getAttributeValue());
				}
				if(attribute.getAttributeName().equalsIgnoreCase("CPE_MAX_BW")){
					cpeDetailsBean.setBwSupported(attribute.getAttributeValue());
				}
				String cpeNetworkDetails = "";
				if(attribute.getAttributeName().equalsIgnoreCase("cpeNetworkDetails")) {
					if(!attribute.getAttributeValue().isEmpty()){
						Optional<ScAdditionalServiceParam> param = scAdditionalServiceParamRepository.findById(Integer.valueOf(attribute.getAttributeValue()));
						if (param.isPresent() && !param.get().getValue().isEmpty())
							cpeNetworkDetails = param.get().getValue();
					}

				}
				cpeDetailsBean.setCpeNetworkDetails(cpeNetworkDetails);

//				if(attribute.getAttributeName().equalsIgnoreCase("templateName")) {
//					cpeDetailsBean.setTemplateName(attribute.getAttributeValue());
//				}
			});
			LOGGER.info("CpeDetails for component fetched");
			List<ScSolutionComponent> underlayList = scSolutionComponentRepository.findByCpeComponentId(scComponent.getId());
			getUnderlayValues(underlayList,cpeDetailsBean);
			cpeDetailsBeanList.add(cpeDetailsBean);
			LOGGER.info("Izosdwan CpeDetailsOverlay fetched {}", cpeDetailsBean);
		}
		return cpeDetailsBeanList;
	}


	private void getUnderlayValues(List<ScSolutionComponent> underlayList, CpeDetailsBean cpeDetailsBean) {
		LOGGER.info("getUnderlayValues method invoked:: {}", underlayList.size());
		try {
			List<UnderlayDetails> listCpeUnderlayDetails = new ArrayList<>();
			for (ScSolutionComponent scSolutionComponent : underlayList) {
				LOGGER.info("Underlay details for {}", scSolutionComponent.getScServiceDetail1().getId());
				UnderlayDetails underlayDetails = new UnderlayDetails();
				underlayDetails.setServiceId(scSolutionComponent.getScServiceDetail1().getId());
				underlayDetails.setServiceCode(scSolutionComponent.getServiceCode());
				underlayDetails.setLinkType(scSolutionComponent.getScServiceDetail1().getPrimarySecondary());
				underlayDetails.setProductName(scSolutionComponent.getScServiceDetail1().getErfPrdCatalogProductName());
				Map<String, String> attr = commonFulfillmentUtils.getComponentAttributes(
						scSolutionComponent.getScServiceDetail1().getId(), AttributeConstants.COMPONENT_LM, "A");
				if (scSolutionComponent.getScServiceDetail1().getErfPrdCatalogProductName().contains("BYON") 
						|| (scSolutionComponent.getScServiceDetail1().getErfPrdCatalogProductName().equalsIgnoreCase("IZO Internet WAN")
								&& !scSolutionComponent.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India"))
						|| scSolutionComponent.getScServiceDetail1().getErfPrdCatalogProductName().equalsIgnoreCase("DIA")
						|| (scSolutionComponent.getScServiceDetail1().getErfPrdCatalogProductName().equalsIgnoreCase("GVPN") && 
								!scSolutionComponent.getScServiceDetail1().getDestinationCountry().equalsIgnoreCase("India"))) {
					underlayDetails.setCommissioningDate("NA");
				} else {
					if (attr.containsKey("commissioningDate")) {
						underlayDetails.setCommissioningDate(attr.get("commissioningDate"));
					}
				}
				listCpeUnderlayDetails.add(underlayDetails);
			}
			cpeDetailsBean.setDetails(listCpeUnderlayDetails);
		} catch (Exception e) {
			LOGGER.error("error in fetching underlay data  for cpe :{}", e);
		}
	}

	public String getCpeType(Set<ScServiceAttribute> scServiceAttributes)
	   {
	      LOGGER.info("getCpeType method invoked:: {}", scServiceAttributes.size());
	      for(ScServiceAttribute scService : scServiceAttributes){
	         if(scService.getAttributeName().equalsIgnoreCase("noOfCpe"))
	         {
	            return scService.getAttributeValue();
	         }
	      }
	      return "";
	   }
	
	public String getSlaValue(Set<ScServiceSla> scServiceSlas)
	   {
	      LOGGER.info("getSlaValue method invoked:: {}", scServiceSlas.size());
	      for(ScServiceSla scService : scServiceSlas){	    	 
	         return scService.getSlaValue();
	      }
	      return "";
	   }

	private List<CpeInstallationBean> getCpeInstallDetails(List<ScComponent> scComponents,Task task) {
		LOGGER.info("getCpeInstallDetails method invoked:: {}", scComponents.size());
		Timestamp cpeAmcStartDate = task.getCreatedTime();
		Timestamp cpeAmcEndDate = null;
		ScContractInfo contractInfo = scContractInfoRepository.findFirstByScOrder_id(task.getScOrderId());
		if (contractInfo != null) {
			cpeAmcEndDate = getCpeAmcEndDate(task.getCreatedTime(), contractInfo.getOrderTermInMonths());
		}
		List<CpeInstallationBean> cpeInstallationBeanList = new ArrayList<>();
		for (ScComponent scComponent : scComponents) {
			LOGGER.info("scComponent details for scComponent id {}", scComponent.getId());
			getCpeDetails(cpeInstallationBeanList,scComponent,cpeAmcEndDate, cpeAmcStartDate);
		}
		LOGGER.info("getCpeInstallDetails completed");
		return cpeInstallationBeanList;
	}


	public void getCpeDetails(List<CpeInstallationBean> cpeInstallationBeanList,ScComponent scComponent,Timestamp cpeAmcEndDate,
						 Timestamp cpeAmcStartDate){
		LOGGER.info("getCpeDetails method invoked:: {}", scComponent.getId());
		CpeInstallationBean cpeInstallDetail = new CpeInstallationBean();
		cpeInstallDetail.setComponentId(scComponent.getId());
		scComponent.getScComponentAttributes().forEach(attribute-> {
			if (attribute.getAttributeName().equalsIgnoreCase("localItContactName")) {
				cpeInstallDetail.setLocalItContactName(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("localItContactEmailId")) {
				cpeInstallDetail.setLocalContactEmailId(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("localItContactMobile")) {
				cpeInstallDetail.setLocalContactNumber(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeInstallationPoNumber")) {
				cpeInstallDetail.setCpeInstallationPoNumber(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeInstallationPrVendorName")) {
				cpeInstallDetail.setCpeInstallationVendorName(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeSupportPrVendorName")) {
				cpeInstallDetail.setCpeSupportVendorName(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeSupportPoNumber")) {
				cpeInstallDetail.setCpeSupportPoNumber(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeSupplyHardwarePoNumber")) {
				cpeInstallDetail.setCpeSupplyHardwarePoNumber(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeSupplyHardwarePrVendorName")) {
				cpeInstallDetail.setCpeSupplyHardwareVendorName(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeSerialNumber")) {
				cpeInstallDetail.setCpeSerialNumber(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeCardSerialNumber")) {
				cpeInstallDetail.setCpeCardSerialNumber(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeOsVersion")) {
				cpeInstallDetail.setCpeOsVersion(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("cpeDeviceName")) {
				cpeInstallDetail.setDeviceName(attribute.getAttributeValue());
			}
			String amcStartDate = String.valueOf(cpeAmcStartDate);
			if (attribute.getAttributeName().equalsIgnoreCase("cpeAmcStartDate")) {
				amcStartDate = attribute.getAttributeValue();
			}
			cpeInstallDetail.setCpeAmcStartDate(amcStartDate);
			LOGGER.info("amcStartDate is : {}",amcStartDate );

			String amcEndDate = String.valueOf(cpeAmcEndDate);
			if (attribute.getAttributeName().equalsIgnoreCase("cpeAmcEndDate")) {
				amcEndDate = attribute.getAttributeValue();
			}
			cpeInstallDetail.setCpeAmcEndDate(amcEndDate);
			LOGGER.info("amcEndDate is : {}",amcEndDate );

			if (attribute.getAttributeName().equalsIgnoreCase("dateOfCpeInstallation")) {
				cpeInstallDetail.setDateOfCpeInstallation(attribute.getAttributeValue());
			}
			if (attribute.getAttributeName().equalsIgnoreCase("CPE Basic Chassis")) {
				LOGGER.info("CPE Basic Chassis componentId: {}",scComponent.getId());
				if(!attribute.getAttributeValue().isEmpty()){
					LOGGER.info("CPE Basic Chassis id value: {}",attribute.getAttributeValue());
					Optional<ScAdditionalServiceParam> param = scAdditionalServiceParamRepository.
							findById(Integer.valueOf(attribute.getAttributeValue()));
					LOGGER.info("value from additional service: {}", param);
					if (param.isPresent() && !param.get().getValue().isEmpty()) {
						cpeInstallDetail.setCpeBasicChassis(param.get().getValue());
						LOGGER.info("value set for cpe basis{}", scComponent.getId());
					}
				}
			}
		});
		cpeInstallationBeanList.add(cpeInstallDetail);
		LOGGER.info("getCpeDetails method completed for :: {}", scComponent.getId());
	}
	

	private UnderlayBean setByonIllBean(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap)throws TclCommonException, IllegalArgumentException {
		LOGGER.info("setByonIllBean method invoked");
		UnderlayBean underlayBean = new UnderlayBean();
		underlayBean.setOrderCode(scServiceDetail.getScOrderUuid());
		underlayBean.setServiceCode(scServiceDetail.getUuid());
		underlayBean.setServiceId(scServiceDetail.getId());
		underlayBean.setOrderType(scServiceDetail.getOrderType());
		underlayBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
		underlayBean.setOrderCategory(scServiceDetail.getOrderCategory()!=null?scServiceDetail.getOrderCategory():"");
		underlayBean.setPortBandwidth(scServiceAttributesmap.get("portBandwidth")!=null?scServiceAttributesmap.get("portBandwidth")+" Mbps":"");
		underlayBean.setLocalLoopBandwidth(scServiceAttributesmap.get("localLoopBandwidth")!=null?scServiceAttributesmap.get("localLoopBandwidth")+" Mbps":"");
		underlayBean.setLastMileType(scServiceDetail.getLastmileConnectionType());
		underlayBean.setSiteAddress(scComponentAttributesmap.getOrDefault("siteAddress", ""));
		underlayBean.setDemarcationBuildingName(scComponentAttributesmap.getOrDefault("demarcationBuildingName",""));
		underlayBean.setDemarcationFloor(scComponentAttributesmap.getOrDefault("demarcationFloor",""));
		underlayBean.setDemarcationRoom(scComponentAttributesmap.getOrDefault("demarcationRoom",""));
		underlayBean.setDemarcationWing(scComponentAttributesmap.getOrDefault("demarcationWing", ""));
		underlayBean.setThirdPartyServiceId(scServiceAttributesmap.getOrDefault("thirdPartyServiceID", ""));
		underlayBean.setThirdPartyWanIpAddress(scServiceAttributesmap.getOrDefault("thirdPartyIPAddress", ""));
		underlayBean.setThirdPartyProviderName(scServiceAttributesmap.getOrDefault("thirdPartyProviderName", ""));
		underlayBean.setThirdPartylinkUptimeAgreement(scServiceAttributesmap.getOrDefault("thirdPartylinkUptimeAgreement", ""));
		underlayBean.setCommissioningDate("NA");
		underlayBean.setCountry(scServiceDetail.getDestinationCountry());
		List<ScServiceSla> slas = scServiceSlaRepository.findAllByScServiceDetail_Id(scServiceDetail.getId());
		if (!CollectionUtils.isEmpty(slas))
			slas.forEach(scServiceSla -> {
				if (scServiceSla.getSlaComponent().equalsIgnoreCase("Service Availability %"))
					underlayBean.setServiceAvailability(scServiceSla.getSlaValue());
			});

		underlayBean.setDocumentIds(getScAttachments(scServiceDetail.getId()));

		return underlayBean;
	}
	
	private UnderlayBean setUnderLayBean(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap, Map<String, String> scComponentAttributesBmap) throws TclCommonException,
			IllegalArgumentException {
		LOGGER.info("setUnderLayBean method invoked");
		UnderlayBean underlayBean = new UnderlayBean();
		underlayBean.setOrderCode(scServiceDetail.getScOrderUuid());
		underlayBean.setServiceCode(scServiceDetail.getUuid());
		underlayBean.setServiceId(scServiceDetail.getId());
		underlayBean.setOrderType(scServiceDetail.getOrderType());
		underlayBean.setOrderCategory(scServiceDetail.getOrderCategory());
		String prodName = "";
		if (scServiceDetail.getErfPrdCatalogProductName() != null) {
			if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("ias")) {
				prodName = "IAS - Internet Access Service";

			} else if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("gvpn")) {
				prodName = "GVPN - Global Virtual Private Network";
			}
		}
		underlayBean.setProductName(prodName);
		if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
			underlayBean.setCustomerContractingEntity(
					scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
		underlayBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("LeStateGstNumber", "") + ";"
				+ scOrderAttributesmap.getOrDefault("GST_Address", ""));

		underlayBean.setLastMileType(scServiceDetail.getLastmileConnectionType());
		underlayBean.setCountry(scServiceDetail.getDestinationCountry());
		underlayBean.setLocalLoopProvider(scComponentAttributesmap.getOrDefault("lmType", ""));
		// underlayBean.setLocalLoopProvider(scComponentAttributesmap.getOrDefault("",""));
		underlayBean.setSiteAddress(scComponentAttributesmap.getOrDefault("siteAddress", ""));
		underlayBean.setDemarcationBuildingName(scComponentAttributesmap.getOrDefault("demarcationBuildingName", ""));
		underlayBean.setDemarcationFloor(scComponentAttributesmap.getOrDefault("demarcationFloor", ""));
		underlayBean.setDemarcationRoom(scComponentAttributesmap.getOrDefault("demarcationRoom", ""));
		underlayBean.setDemarcationWing(scComponentAttributesmap.getOrDefault("demarcationWing", ""));
		String billStartDate = formatDate(scComponentAttributesmap.getOrDefault("billStartDate", ""));
		underlayBean.setBillStartDate(billStartDate);
		underlayBean.setBillFreePeriod(scComponentAttributesmap.getOrDefault("billFreePeriod", ""));
		underlayBean
				.setDeemedAcceptanceApplicable(scComponentAttributesmap.getOrDefault("deemedAcceptanceApplicable", ""));
		String commissioningDate = formatDate(scComponentAttributesmap.getOrDefault("commissioningDate", ""));
		underlayBean.setCommissioningDate(commissioningDate);
		underlayBean.setPortBandwidth(scComponentAttributesmap.get("portBandwidth") != null
				? scComponentAttributesmap.get("portBandwidth") + " Mbps" : "");
		underlayBean.setLocalLoopBandwidth(scComponentAttributesmap.get("localLoopBandwidth") != null
				? scComponentAttributesmap.get("localLoopBandwidth") + " Mbps" : "");
		underlayBean.setEndMuxNodeIp(scComponentAttributesmap.get("endMuxNodeIp") != null
				? scComponentAttributesmap.get("endMuxNodeIp") : "");
		underlayBean.setEndMuxNodeName(scComponentAttributesmap.get("endMuxNodeName") != null
				? scComponentAttributesmap.get("endMuxNodeName") : "");
		underlayBean.setInterfaceType(scComponentAttributesmap.get("interfaceType") != null
				? scComponentAttributesmap.get("interfaceType") : "");
		underlayBean.setConnectorType(scComponentAttributesmap.get("Connector Type") != null
				? scComponentAttributesmap.get("Connector Type") : "");
		underlayBean.setAsNumber(
				scComponentAttributesmap.get("asNumber") != null ? scComponentAttributesmap.get("asNumber") : "");
		List<ScServiceSla> slas = scServiceSlaRepository.findAllByScServiceDetail_Id(scServiceDetail.getId());
		if (!CollectionUtils.isEmpty(slas)) {
			slas.forEach(scServiceSla -> {
				if (scServiceSla.getSlaComponent().equalsIgnoreCase("Service Availability %"))
					underlayBean.setServiceAvailability(scServiceSla.getSlaValue());
			});
		}
		String response = (String) mqUtils.sendAndReceive(servicehandovernote_queue, scServiceDetail.getUuid());
		if (response != null) {
			LOGGER.info("Response from Service Handover Queue");
			Map<String, Object> handoverAttr = (Map<String, Object>) Utils.convertJsonToObject(response, Map.class);
			if (handoverAttr != null) {
				LOGGER.info("Handover Attribute exists");
				underlayBean.setWanInterfaceType(
						handoverAttr.get("wanInterfaceType") != null ? (String) handoverAttr.get("wanInterfaceType")
								: "");
				underlayBean.setRoutingProtocol(
						handoverAttr.get("routingProtocol") != null ? (String) handoverAttr.get("routingProtocol")
								: "");
				underlayBean.setWanv4Address(
						handoverAttr.get("wanv4Address") != null ? (String) handoverAttr.get("wanv4Address") : "");
				underlayBean.setLanv4Address(
						handoverAttr.get("lanv4Address") != null ? (String) handoverAttr.get("lanv4Address") : "");
				underlayBean.setLanv6Address(
						handoverAttr.get("lanv6Address") != null ? (String) handoverAttr.get("lanv6Address") : "");
				underlayBean.setWanv6Address(
						handoverAttr.get("wanv6Address") != null ? (String) handoverAttr.get("wanv6Address") : "");
			}
		}
		return underlayBean;
	}
	
	private OverlayBean setSdwanOverlayBean(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap, String associatedServiceIds, OverlayBean saBean) {
		LOGGER.info("SetSdwanBean method invoked::{} ",scServiceDetail.getId());
		saBean = new OverlayBean();
		saBean.setOrderCode(scServiceDetail.getScOrderUuid());
		saBean.setServiceCode(scServiceDetail.getUuid());
		saBean.setServiceId(scServiceDetail.getId());
		saBean.setAssociatedServiceIds(associatedServiceIds.substring(0, associatedServiceIds.length() - 1));
		saBean.setOrderType(scServiceDetail.getOrderType());
		saBean.setOrderCategory(scServiceDetail.getOrderCategory());
		if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
			saBean.setCustomerContractingEntity(scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
		saBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("LeStateGstNumber", "") + ";"
				+ scOrderAttributesmap.getOrDefault("GST_Address", ""));
		
		saBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
		Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
		if(!scComponentAttributesmap.isEmpty()){
			LOGGER.info("ScComponentAttributesmap exists::{} ",scComponentAttributesmap);
			saBean.setSiteAddress(scComponentAttributesmap.getOrDefault("siteAddress",""));
			saBean.setCpeManagement(scComponentAttributesmap.getOrDefault("cpeManagementType",""));
			String billStartDate = formatDate(scComponentAttributesmap.getOrDefault("billStartDate", ""));
			saBean.setBillStartDate(billStartDate);
			saBean.setDeemedAcceptanceApplicable(scComponentAttributesmap.getOrDefault("deemedAcceptanceApplicable", ""));
			saBean.setBillFreePeriod(scComponentAttributesmap.getOrDefault("billFreePeriod", null));
			String commissioningDate = formatDate(scComponentAttributesmap.getOrDefault("commissioningDate", ""));
			saBean.setCommissioningDate(commissioningDate);
		}
		List<ScComponentAttribute> cpeSerialAttrList=scComponentAttributesRepository.findByScServiceDetailIdAndAttributeName(scServiceDetail.getId(), "cpeSerialNumber");
		String[] serialNumber = {""};
		if(cpeSerialAttrList!=null && !cpeSerialAttrList.isEmpty()){
			LOGGER.info("cpeSerialAttrList exists::{} ",cpeSerialAttrList.size());
			cpeSerialAttrList.stream().filter(cpeSerialAttr -> cpeSerialAttr.getAttributeValue()!=null).forEach(cpeSerialAttr -> {
				serialNumber[0]=serialNumber[0]+cpeSerialAttr.getAttributeValue()+",";
			});
			if(!serialNumber[0].isEmpty()){
				serialNumber[0]=serialNumber[0].substring(0, serialNumber[0].length() - 1);
			}
		}
		saBean.setCpeSerialNumbers(serialNumber[0]);
		return saBean;
	}
	

  private String formatDate(String sdate1) {
	String date = "";
	if (sdate1 != null && !sdate1.equals("")) {
		try {
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
			Date date1 = formatter1.parse(sdate1);
			date = formatter2.format(date1);
			//System.out.println(date);
		} catch(Exception e) {
			LOGGER.error("Date Parsing or Formatting Error : {}",e.getMessage());				
		}
	}
	return date;
   }
  
  public OverlayBean setOverlayUnderlayDetails(List<Map<String,Integer>> layDetails,Map<String, String> scServiceAttributesmap,ScServiceDetail scServiceDetail ,ScOrder scOrder,Map<String, String> 
  scOrderAttributesmap, OverlayBean overlayBean)throws TclCommonException { 
	  LOGGER.info("setOverlayUnderlayDetails method invoked::{} ",scServiceDetail.getId());
	  List<UnderlayBean> underlayBeanList=new ArrayList<>();
	  List<ScServiceDetail> underlayServiceDetailList = new ArrayList<>();
	  for (Map<String,Integer> overUnderlayMap : layDetails) {
		  LOGGER.info("Underlay Ids: {}",overUnderlayMap.get("underlayIds"));
		  LOGGER.info("Overlay Ids: {}",overUnderlayMap.get("overlayIds"));
		  String underlays=String.valueOf(overUnderlayMap.get("underlayIds"));
		  String[] underlayList= underlays.split(",");
		  String associatedServiceIds="";
		  for(String underlay:underlayList){
			  Integer underlayId=Integer.valueOf(underlay);
			  Optional<ScServiceDetail> underlayServiceDetailOptional=scServiceDetailRepository.findById(underlayId);
			  if(underlayServiceDetailOptional.isPresent()){
				  ScServiceDetail underlayServiceDetail=underlayServiceDetailOptional.get();
				  associatedServiceIds+=underlayServiceDetail.getUuid()+",";
				  LOGGER.info("ProductName: {}",underlayServiceDetail.getErfPrdCatalogProductName());
				  if("BYON Internet".equalsIgnoreCase(underlayServiceDetail.getErfPrdCatalogProductName())){
					  LOGGER.info("BYON Internet: {}",underlayServiceDetail.getId());
					  Set<ScServiceAttribute> underlayServiceAttributes = new HashSet<>();			
					  underlayServiceAttributes = underlayServiceDetail.getScServiceAttributes();
					  Map<String, String> underlayServiceAttributesmap = new HashMap<>();
					  underlayServiceAttributesmap = underlayServiceAttributes.stream().collect(HashMap::new,
							  (m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);
					  Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(underlayServiceDetail.getId(), "LM", "A");
					  UnderlayBean underlayBean=setByonIllBean(underlayServiceDetail, scOrder, scOrderAttributesmap, underlayServiceAttributesmap, scComponentAttributesmap);
					  underlayBeanList.add(underlayBean);
				  }
				  else if("IAS".equalsIgnoreCase(underlayServiceDetail.getErfPrdCatalogProductName())
						  || ("GVPN".equalsIgnoreCase(underlayServiceDetail.getErfPrdCatalogProductName())
	    							&& underlayServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))){
					  LOGGER.info("IAS or GVPN: {}",underlayServiceDetail.getId());
					  underlayServiceDetailList.add(underlayServiceDetail);
					  Set<ScServiceAttribute> underlayServiceAttributes = new HashSet<>();			
					  underlayServiceAttributes = underlayServiceDetail.getScServiceAttributes();
					  Map<String, String> underlayServiceAttributesmap = new HashMap<>();
					  underlayServiceAttributesmap = underlayServiceAttributes.stream().collect(HashMap::new,
							  (m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);
					  Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(underlayServiceDetail.getId(), "LM", "A");
					  Map<String, String> scComponentAttributesBmap= commonFulfillmentUtils.getComponentAttributes(underlayServiceDetail.getId(), "LM", "B");
					  UnderlayBean underlayBean=setUnderLayBean(underlayServiceDetail, 
							  scOrder, scOrderAttributesmap, underlayServiceAttributesmap, scComponentAttributesmap,
							  scComponentAttributesBmap);
					  underlayBeanList.add(underlayBean);
				  }else if("DIA".equalsIgnoreCase(underlayServiceDetail.getErfPrdCatalogProductName())
						  || ("IZO Internet WAN".equalsIgnoreCase(underlayServiceDetail.getErfPrdCatalogProductName())
								  && !underlayServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))
						  || ("GVPN".equalsIgnoreCase(underlayServiceDetail.getErfPrdCatalogProductName())
						  && !underlayServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))){
					  LOGGER.info("Dia or Iwan or international Gvpn: {}",underlayServiceDetail.getId());
					  underlayServiceDetailList.add(underlayServiceDetail);
					  UnderlayBean underlayBean=setIWANDIAGVPNBean(underlayServiceDetail);
					  underlayBeanList.add(underlayBean);
				  }
			  }
		  }
		  overlayBean =setSdwanOverlayBean(scServiceDetail, scOrder, scOrderAttributesmap,
				  scServiceAttributesmap,associatedServiceIds,overlayBean);
		  if(overlayBean!=null){
			  LOGGER.info("overlayBean exists::{}",underlayBeanList.size());
			  overlayBean.setUnderlayBeanList(underlayBeanList);
		  }
		  LOGGER.info("overlay bean populated ");
		  
	  }
	  return overlayBean;
  }

	private void getIzoSdwanDetails(Integer serviceId, String serviceCode, String orderCode, Map<String, Object> completeServiceDetails) {
		LOGGER.info("getIzoSdwanDetails method invoked");
		List<Map<Object,Object>> scSolutionMapList = null;
		if(serviceCode.toLowerCase().contains("sdsol")){
			LOGGER.info("Solution service Id as input");
			Map<Object,Object> solutionMap = new HashMap<>();
			solutionMap.put("solutionId", serviceId);
			solutionMap.put("orderCode", orderCode);
			scSolutionMapList=new ArrayList<>();
			scSolutionMapList.add(solutionMap);
		}else{
			LOGGER.info("Other than Solution service Id as input");
			scSolutionMapList = scSolutionComponentRepository.getSolutionDetails(serviceId, serviceCode,"Y");
		}
		if (scSolutionMapList != null && !scSolutionMapList.isEmpty()) {
			List<String> componentGroups = new ArrayList<>();
			componentGroups.add("OVERLAY");
			componentGroups.add("UNDERLAY");
			List<SolutionBean> solutionBeanList = new ArrayList<>();
			for (Map<Object, Object> solutionMap : scSolutionMapList) {
				LOGGER.info("Solution Id: {}", solutionMap.get("solutionId"));
				Integer solutionId = Integer.valueOf(String.valueOf(solutionMap.get("solutionId")));
				List<ScSolutionComponent> scSolutionComponentList = scSolutionComponentRepository
						.findAllByScServiceDetail3_idAndComponentGroupAndIsActive(solutionId, "OVERLAY", "Y");
				String solutionOrderCode = String.valueOf(solutionMap.get("orderCode"));
				LOGGER.info("solutionOrderCode: {}",solutionOrderCode);
				ScOrder scSolutionOrder = scOrderRepository.findByOpOrderCodeAndIsActive(solutionOrderCode, "Y");
				Set<ScServiceDetail> scServiceDetails = scSolutionOrder.getScServiceDetails();
				Map<Integer, ScServiceDetail> scServiceDetailMap = new HashMap<>();
				for (ScServiceDetail scServiceSolutionDetail : scServiceDetails) {
					scServiceDetailMap.put(scServiceSolutionDetail.getId(), scServiceSolutionDetail);
				}
				List<OverlayBean> overlayBeanList = new ArrayList<>();
				for (ScSolutionComponent scSolutionComponent : scSolutionComponentList) {
					LOGGER.info("Overlay Id: {}", scSolutionComponent.getScServiceDetail1().getId());
					List<Map<String, Integer>> layDetails = scSolutionComponentRepository
							.findScServiceDetailByComponentType(solutionOrderCode, componentGroups, "Y",
									scSolutionComponent.getScServiceDetail1().getId());
					if (layDetails != null && !layDetails.isEmpty()) {
						for (Map<String, Integer> overUnderlayMap : layDetails) {
							LOGGER.info("Underlay Ids: {}", overUnderlayMap.get("underlayIds"));
							LOGGER.info("Overlay Ids: {}", overUnderlayMap.get("overlayIds"));
							String underlays = String.valueOf(overUnderlayMap.get("underlayIds"));
							LOGGER.info("Underlay Ids{}", underlays);
							String[] underlayList = underlays.split(",");
							List<UnderlayBean> underlayBeanList = new ArrayList<>();
							for (String underlay : underlayList) {
								Integer underlayId = Integer.valueOf(underlay);
								if (scServiceDetailMap.containsKey(underlayId)) {
									ScServiceDetail underlayServiceDetail = scServiceDetailMap.get(underlayId);
									ScSolutionComponent scSolutionComponentUnderlay =  scSolutionComponentRepository.findByScServiceDetail1(underlayServiceDetail);
									
									UnderlayBean underlayBean = new UnderlayBean();
									underlayBean.setOrderId(scSolutionOrder.getId());
									underlayBean.setOrderCode(scSolutionOrder.getOpOrderCode());
									underlayBean.setOrderType(underlayServiceDetail.getOrderType());
									underlayBean.setOrderCategory(underlayServiceDetail.getOrderCategory());
									underlayBean.setOrderSubCategory(underlayServiceDetail.getOrderSubCategory());
									underlayBean
											.setProductName(underlayServiceDetail.getErfPrdCatalogProductName());
									underlayBean.setServiceCode(underlayServiceDetail.getUuid());
									underlayBean.setServiceId(underlayServiceDetail.getId());
									underlayBean
											.setOfferingName(underlayServiceDetail.getErfPrdCatalogOfferingName());
									underlayBean.setRrfsDate(underlayServiceDetail.getRrfsDate());
									underlayBean.setCommitedDeliveryDate(underlayServiceDetail.getServiceCommissionedDate());
									underlayBean.setSiteAddress(underlayServiceDetail.getSiteAddress());
									if(scSolutionComponentUnderlay!=null) {
										underlayBean.setPriority(scSolutionComponentUnderlay.getPriority());
									}
									underlayBeanList.add(underlayBean);
								}
							}
							Integer overlayId = overUnderlayMap.get("overlayIds");
							LOGGER.info("OverlayId Ids: {}", overUnderlayMap.get("overlayIds"));
							if (scServiceDetailMap.containsKey(overlayId)) {
								ScServiceDetail overlayServiceDetail = scServiceDetailMap.get(overlayId);
								OverlayBean overlayBean = new OverlayBean();
								overlayBean.setOrderId(scSolutionOrder.getId());
								overlayBean.setOrderCode(scSolutionOrder.getOpOrderCode());
								overlayBean.setOrderType(overlayServiceDetail.getOrderType());
								overlayBean.setOrderCategory(overlayServiceDetail.getOrderCategory());
								overlayBean.setOrderSubCategory(overlayServiceDetail.getOrderSubCategory());
								overlayBean.setProductName(overlayServiceDetail.getErfPrdCatalogProductName());
								overlayBean.setServiceCode(overlayServiceDetail.getUuid());
								overlayBean.setServiceId(overlayServiceDetail.getId());
								overlayBean.setOfferingName(overlayServiceDetail.getErfPrdCatalogOfferingName());
								overlayBean.setUnderlayBeanList(underlayBeanList);
								overlayBean.setUnderlaySize(underlayBeanList.size());
								overlayBean.setRrfsDate(overlayServiceDetail.getRrfsDate());
								overlayBean.setCommitedDeliveryDate(overlayServiceDetail.getServiceCommissionedDate());
								overlayBean.setSiteAddress(overlayServiceDetail.getSiteAddress());
								if(scSolutionComponent!=null) {
									overlayBean.setPriority(scSolutionComponent.getPriority());
								}
								overlayBeanList.add(overlayBean);
							}

						}
					}
				}
				List<CGWDetailsBean> cgwDetailsBeans = new ArrayList<>();
				List<ScSolutionComponent> scSolutionComponentListCgw = scSolutionComponentRepository
						.findAllByScServiceDetail3_idAndComponentGroupAndIsActive(solutionId, "CGW", "Y");
				if(scSolutionComponentListCgw!=null && !scSolutionComponentListCgw.isEmpty()) {
					scSolutionComponentListCgw.stream().forEach(scSolComp->{
						CGWDetailsBean cgwDetailsBean = new CGWDetailsBean();
						cgwDetailsBean.setOrderCode(scSolComp.getOrderCode());
						cgwDetailsBean.setOrderId(scSolComp.getScOrder().getId());
						cgwDetailsBean.setPriSec(scSolComp.getScServiceDetail1().getPrimarySecondary());
						cgwDetailsBean.setServiceCode(scSolComp.getServiceCode());
						cgwDetailsBean.setServiceId(scSolComp.getScServiceDetail1().getId());
						cgwDetailsBean.setPriSecLinkedServiceId(scSolComp.getScServiceDetail1().getPriSecServiceLink());
						cgwDetailsBean.setRrfsDate(scSolComp.getScServiceDetail1().getRrfsDate());
						cgwDetailsBean.setCommitedDeliveryDate(scSolComp.getScServiceDetail1().getServiceCommissionedDate());
						if(scSolComp!=null) {
							cgwDetailsBean.setPriority(scSolComp.getPriority());
						}
						cgwDetailsBean.setSiteAddress(scSolComp.getScServiceDetail1().getSiteAddress());
						cgwDetailsBeans.add(cgwDetailsBean);
					});
				}
				ScServiceDetail scSolutionServiceDetail=scServiceDetailMap.get(solutionId);
				SolutionBean solutionBean = new SolutionBean();
				solutionBean.setOrderId(scSolutionOrder.getId());
				solutionBean.setOrderCode(scSolutionOrder.getOpOrderCode());
				solutionBean.setOrderType(scSolutionServiceDetail.getOrderType());
				solutionBean.setOrderCategory(scSolutionServiceDetail.getOrderCategory());
				solutionBean.setOrderSubCategory(scSolutionServiceDetail.getOrderSubCategory());
				solutionBean.setProductName(scSolutionServiceDetail.getErfPrdCatalogProductName());
				solutionBean.setServiceCode(scSolutionServiceDetail.getUuid());
				solutionBean.setServiceId(scSolutionServiceDetail.getId());
				solutionBean.setOfferingName(scSolutionServiceDetail.getErfPrdCatalogOfferingName());
				solutionBean.setOverlayBeanList(overlayBeanList);
				solutionBean.setOverlaySize(overlayBeanList.size());
				solutionBean.setCgwDetailList(cgwDetailsBeans);
				
				solutionBeanList.add(solutionBean);
			}
			completeServiceDetails.put("serviceBundle", solutionBeanList);
		}
	}

	private List<AttachmentIdBean> getScAttachments(Integer serviceId) {
		List<AttachmentIdBean> attachmentBeans = new ArrayList<>();
		List<ScAttachment> scAttachments = scAttachmentRepository.findAllByScServiceDetail_Id(serviceId);
		scAttachments = scAttachments.stream().filter(sc -> sc.getAttachment().getCategory() != null)
				.collect(Collectors.toList());
		LOGGER.info("getScAttachments scAttachments size",scAttachments.size());

		Map<String, List<Attachment>> attachmentMap = scAttachments.stream().map(ScAttachment::getAttachment)
				.collect(Collectors.groupingBy(Attachment::getCategory));
		attachmentMap.keySet().forEach(category -> {
			AttachmentIdBean attachmentIdBean = new AttachmentIdBean();
			Integer attachmentId= attachmentMap.get(category).stream().sorted(Comparator.comparing(Attachment::getId).reversed())
					.findFirst().get().getId();
			LOGGER.info("getScAttachments id",attachmentId);
			attachmentIdBean.setAttachmentId(attachmentId);
			attachmentIdBean.setCategory(category);
			attachmentBeans.add(attachmentIdBean);
		});
		return attachmentBeans;
	}
	
	private void getBillingDateEndpoint(Task task, Map<String, Object> taskDataMap) {
		TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(task.getServiceId(), "install-endpoint", "A");
		if (taskPlan != null) {
			LocalDateTime endpointTime = taskPlan.getPlannedStartTime().toLocalDateTime().minusDays(7);
			LOGGER.info("endpoint time:{}", endpointTime);
			if (endpointTime.isAfter(LocalDateTime.now())) {
				taskDataMap.put("cpeBillStartDate", endpointTime);
			} else {
				taskDataMap.put("cpeBillStartDate", Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
			}
		}
	}


	private UnderlayBean setIWANDIAGVPNBean(ScServiceDetail scServiceDetail)throws TclCommonException, IllegalArgumentException {
		LOGGER.info("setIWANDIAGVPNBean method invoked");
		UnderlayBean underlayBean = new UnderlayBean();
		underlayBean.setOrderCode(scServiceDetail.getScOrderUuid());
		underlayBean.setServiceCode(scServiceDetail.getUuid());
		underlayBean.setServiceId(scServiceDetail.getId());
		underlayBean.setOrderType(scServiceDetail.getOrderType());
		underlayBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
		underlayBean.setOrderCategory(scServiceDetail.getOrderCategory()!=null?scServiceDetail.getOrderCategory():"");
		underlayBean.setCommissioningDate("NA");
		underlayBean.setCountry(scServiceDetail.getDestinationCountry());
		return underlayBean;
	}


	public void getMultiVrfAttributes(Integer serviceId, Map<String, Object> taskData) {
		LOGGER.info("Inside getMultiVrfAttribute for task detail:{}",serviceId);
		List<String> attributeNames = Arrays.asList(CommonConstants.FLEXICOS,CommonConstants.MASTER_VRF_FLAG,CommonConstants.MULTI_VRF_SOLUTION,
				CommonConstants.TOTAL_VRF_BANDWIDTH_MBPS,CommonConstants.NUMBER_OF_VRFS,CommonConstants.SLAVE_VRF_SERVICE_ID,CommonConstants.MASTER_VRF_SERVICE_ID,
				CommonConstants.CUSTOMER_PROJECT_NAME,CommonConstants.VRF_BASED_BILLING);

		List<ScServiceAttribute> scServiceAttributes = scServiceAttributeRepository.findByScServiceDetail_idAndAttributeNameIn(serviceId, attributeNames);
		LOGGER.info("getMultiVrfAttribute size:{}",scServiceAttributes.size());
		if(scServiceAttributes!=null && !scServiceAttributes.isEmpty()){
			taskData.putAll(scServiceAttributes.stream().collect(Collectors.toMap(ScServiceAttribute::getAttributeName,ScServiceAttribute::getAttributeValue)));
		}
	}

	
	private Integer getOrderTermInMonthsToDays(Double orderTermInMonths) {
		LOGGER.info("getOrderTermInMonthsToDays:{}",orderTermInMonths);
		Integer orderTermInMonthsToDays = 0;
		if (orderTermInMonths < 1) {
			LOGGER.info("orderTermInMonths less than 1:{}",orderTermInMonths);
			orderTermInMonthsToDays = 15;	//L2O will send min. value as 0.5
		} else {
			LOGGER.info("orderTermInMonths greater than 1:{}",orderTermInMonths);
			String orderTermInMonthsArray[] = Double.toString(orderTermInMonths).split("\\.");
			Integer orderTermInDorderTermInMonthBeforeDecimalPoint = Integer.valueOf(orderTermInMonthsArray[0]) * 30;
			Integer orderTermInDorderTermInMonthAfterDecimalPoint = Integer.valueOf(orderTermInMonthsArray[1]) > 0 ? 15 : 0; //L2O will send min. value as 0.5
			orderTermInMonthsToDays = orderTermInDorderTermInMonthBeforeDecimalPoint + orderTermInDorderTermInMonthAfterDecimalPoint;
		}
		LOGGER.info("orderTermInMonthsToDays:{}",orderTermInMonthsToDays);
		return orderTermInMonthsToDays;
	}
}

