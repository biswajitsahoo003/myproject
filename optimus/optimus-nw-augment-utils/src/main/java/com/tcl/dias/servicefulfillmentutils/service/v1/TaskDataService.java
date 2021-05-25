package com.tcl.dias.servicefulfillmentutils.service.v1;

import com.tcl.dias.common.servicefulfillment.beans.CpeBomResource;
//import com.tcl.dias.common.utils.Currencies;
import com.tcl.dias.common.utils.ForexService;
//import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.networkaugment.entity.entities.*;
import com.tcl.dias.networkaugment.entity.repository.*;
//import com.tcl.dias.servicefulfillmentutils.abstractservice.ITaskDataService;
//import com.tcl.dias.servicefulfillmentutils.beans.*;
//import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.abstractservice.ITaskDataService;
import com.tcl.dias.servicefulfillmentutils.abstractservice.ITaskDataService;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    /*@Autowired
    TaskPlanRepository taskPlanRepository;*/

    @Autowired
    TaskService taskService;

    /*@Autowired
    PlannedEventRepository plannedEventRepository;*/

    @Autowired
    RuntimeService runtimeService;
    
    @Autowired
    ScServiceAttributeRepository scServiceAttributeRepository;
    
    /*@Autowired
    VwBomMaterialDetailRepository vwBomMaterialDetailRepository;
    
    @Autowired
    VwBomMuxDetailsRepository vwBomMuxDetailsRepository;*/

    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;
    
    @Autowired
    ScComponentAttributesRepository scComponentAttributesRepository;
    
    /*@Autowired
    ScServiceCommercialRepository scServiceCommercialRepository;
    
    @Autowired
    CustomerCrnRepository crnRepository;
	
    @Autowired
    MstMuxBomTier1Repository mstMuxBomTier1Repository;
    
    @Autowired
    BillingChargeLineItemService billingChargeLineItemService;

    @Autowired
	ScServiceSlaRepository scServiceSlaRepository;
    
    @Autowired
	MstCostCatalogueRepository mstCostCatalogueRepository;*/


	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
	
	/*@Autowired
	ScChargeLineitemRepository chargeLineitemRepository;*/
	
	@Autowired
	ForexService forexService;
	
	/*@Autowired
	IpcBillingChargeLineItemService ipcBillingChargeLineItemService;*/

	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	FieldEngineerRepository fieldEngineerRepository;
	
	/*@Override
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
		
		if (task.getOrderType() != null && task.getOrderType().equalsIgnoreCase("MACD")) {
			ScServiceDetail prevScServiceDetail = scServiceDetailRepository
					.findByUuidAndMstStatus_code(task.getServiceCode(), TaskStatusConstants.ACTIVE);
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
				|| task.getMstTaskDef().getKey().startsWith("select-vendor")) {
			processMstAppointmentDocuments(task, taskDataMap, getTaskType(task.getMstTaskDef().getKey()));
		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("lm-confirm-site-readiness-details")) {
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "lm-deliver-mux", task.getSiteType()==null?"A":task.getSiteType());
			if (taskPlan != null)taskDataMap.put("tentativeDate", taskPlan.getPlannedStartTime());
		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("advanced-enrichment")
				||(task.getMstTaskDef().getKey().equalsIgnoreCase("advanced-enrichment-rejected")) ) {
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "enrich-service-design", task.getSiteType()==null?"A":task.getSiteType());
			if (taskPlan != null)taskDataMap.put("tentativeDate", taskPlan.getPlannedStartTime());
			getCosDatailsAttributes(serviceId,taskDataMap);
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
				itemDetailsBeanList.add(itemDetailsBean);
			}
			chargeLineitemBean.setLineItemDetails(itemDetailsBeanList);
			String accountNumbers = billingChargeLineItemService.getActiveAccounts(task.getServiceId().toString(), task.getServiceType());
			taskDataMap.put("lineItemDetails", itemDetailsBeanList);
			taskDataMap.put("activeAccounts", accountNumbers);

		}

		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("commercial-vetting")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("product-commissioning-jeopardy") 
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
			}else {
				itemDetailsBeans = billingChargeLineItemService.loadLineItems(task.getServiceId(),bomResource,billingMethod);
			}
			
			if (itemDetailsBeans != null) {
				Double totalNrc = itemDetailsBeans.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getNrc())));
				Double totalArc = itemDetailsBeans.stream()
						.collect(Collectors.summingDouble(detail -> Double.parseDouble(detail.getArc())));

				taskDataMap.put("totalArc", String.format("%.2f",totalArc));
				taskDataMap.put("totalNrc", String.format("%.2f",totalNrc));
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
		}
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("ipc-commercial-vetting") || task.getMstTaskDef().getKey().equalsIgnoreCase("ipc-comm-valid")) {
			List<LineItemDetailsBean> ipcItemDetailsBeans = ipcBillingChargeLineItemService.loadLineItems(task.getId());
			taskDataMap.put("lineItemDetails", ipcItemDetailsBeans);
			Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository
					.findById(task.getServiceId());
			taskDataMap.put("siteAddress", optionalServiceDetails.get().getSourceCity()+IpcConstants.SPECIAL_CHARACTER_COMMA+IpcConstants.SINGLE_SPACE+optionalServiceDetails.get().getSourceCountry());
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("ipc-validate-supporting-document")){
			Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository
					.findById(task.getServiceId());
			taskDataMap.put("siteAddress", optionalServiceDetails.get().getSourceCity()+IpcConstants.SPECIAL_CHARACTER_COMMA+IpcConstants.SINGLE_SPACE+optionalServiceDetails.get().getSourceCountry());
		}
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("service-acceptance") 
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("deemed-service-acceptance")
				|| task.getMstTaskDef().getKey().contains("service-handover")) {
			getSlaValues(serviceId,taskDataMap);
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
		
		
		if(task.getMstTaskDef().getKey().equalsIgnoreCase("product-commissioning-jeopardy") 
				||task.getMstTaskDef().getKey().equalsIgnoreCase("cpe-product-commissioning-jeopardy") 
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("comm-valid-billing-issue")) {
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
				itemDetailsBeanList.add(itemDetailsBean);
			});
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
		return taskDataMap;
	}*/

	/**
	 * @author vivek
	 *
	 * @param task
	 * @param taskDataMap
	 */
	/*private void getFileEngineerDetails(Task task, Map<String, Object> taskDataMap) {

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

	}*/

	/**
	 * @author vivek
	 *
	 * @param //fieldEngineers
	 * @param //string
	 * @return
	 */
	/*private List<FieldEngineerDetails> getFeDetails(List<FieldEngineer> fieldEngineers, String type) {

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
		
		

	private Timestamp getCpeAmcEndDate(Timestamp createdTime, Integer orderTermInMonths) {
		Timestamp cpeAmcEndDate = null;
		try {
			if (orderTermInMonths != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(createdTime);
				cal.add(Calendar.MONTH, orderTermInMonths);
				cpeAmcEndDate = new Timestamp(cal.getTime().getTime());
			}
			return cpeAmcEndDate;
		} catch (Exception e) {
			LOGGER.info("ERROR in getCpeAmcEndDate returning new Timestamp(0): {}", e);
			return createdTime;
		}

	}*/
	
	

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

	/*private void getBillingDate(Task task, Map<String, Object> taskDataMap) {
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

	private void getmastCatalogueDetails(Task task, Map<String, Object> taskDataMap) throws TclCommonException {

		CpeBomResource bomResources = getBomResources(task);
		List<MstCatalogueBean> catalogueBeans = new ArrayList<>();

		if (bomResources != null) {

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

	}*/
	
	

	/*private void groupBomValues(List<VwMuxBomDetailBean> bomMaterialDetailsBeans, VwMuxBomMaterialDetail bomMaterialDetail, Map<String, Object> taskDataMap) {
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
	
	}*/

	/*private void processRfBomDetails(String localoopBand, Map<String, Object> taskDataMap,
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
	}*/

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

	/*@Override
	public Map<String, Object> getTaskData(Task task) throws TclCommonException {
		return null;
	}*/
	@Override
	public Map<String, Object> getTaskData(Task task) throws TclCommonException {
		LOGGER.info("get Task Data method started with Key {} ", task.getMstTaskDef().getKey());
		Integer serviceId = task.getServiceId();
		Map<String, Object> taskDataMap = attributeService.getTaskAttributes(task.getMstTaskDef().getKey(), serviceId,task.getSiteType());
		LOGGER.info("Variable map has values ; {} ", taskDataMap);
		return taskDataMap;
	}


    /*private Map<String, Object> processMstAppointmentDocuments(Task task, Map<String, Object> taskDataMap, String type) {
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

	private void getCosDatailsAttributes(Integer serviceId, Map<String, Object> taskData) {

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
	}

	private CosDetail getCosDetailBean(List<ScServiceAttribute> scServiceAttributes, String cosKey,String criteriaTypeKey, String criteriaKey) {
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
	}*/

}
