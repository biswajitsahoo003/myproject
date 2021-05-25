package com.tcl.dias.servicefulfillmentutils.delegates;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstActivityDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("createServiceDelegate")
public class CreateServiceDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(CreateServiceDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${queue.createservicesync}")
	String createServiceSync;

	@Autowired
	MstTaskDefRepository mstTaskDefRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskCacheService taskCacheService;

	@Autowired
	MstActivityDefRepository mstActivityDefRepository;

	@Autowired
	TaskDataService taskDataService;

	@Autowired
	TaskService taskService;

	@Autowired
	WorkFlowService workFlowService;
	
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Override
	public void execute(DelegateExecution execution) {
		logger.info("CreateServiceDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		String errorMessage="";
		Map<String, Object> taskDataMap = new HashMap<>();
		Task task = workFlowService.processServiceTask(execution);

		try {


			// Task task = taskService.getTaskByExecution(execution);
			if (Objects.nonNull(task)) {
				taskDataMap = taskDataService.getTaskData(task);
				logger.info("taskDataMap in create service {} ", taskDataMap);
			} else {
				logger.info("Task is null in create service delegate");
			}
			
			
			String productType = (String)execution.getVariables().get(CramerConstants.PRODUCT_NAME);
			String createServiceSyncResponse = "";
			if(Objects.nonNull(productType)) {
			if(productType.equalsIgnoreCase("ILL") || productType.equalsIgnoreCase("IAS") || productType.equalsIgnoreCase("BYON Internet")) {
				logger.info("productType={}", productType);
				Map<String, String> illMap  = getIllMap(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(),
						taskDataMap);
				String req = Utils.convertObjectToJson(illMap);
				logger.info("create service constructed map => {} ", req);
				createServiceSyncResponse = (String) mqUtils.sendAndReceive(createServiceSync, req,120000);
	            logger.info("createServiceSyncResponse {}", createServiceSyncResponse);

	          
			}else if(productType.equalsIgnoreCase("GVPN") || productType.equalsIgnoreCase("IZOPC")) {
				Map<String, String> gvpnMap = getGvpnMap(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(),
						taskDataMap);
				
				String req = Utils.convertObjectToJson(gvpnMap);
				logger.info("create service gvpn constructed map => {} ", req);
				createServiceSyncResponse = (String) mqUtils.sendAndReceive(createServiceSync, req,120000);
	            logger.info("createServiceSyncResponse gvpn {}", createServiceSyncResponse);
			}else if(productType.equalsIgnoreCase("NPL") ) {
				Map<String, String> nplMap  = getNplMap(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(),
						taskDataMap);
				String req = Utils.convertObjectToJson(nplMap);
				logger.info("create service constructed map => {} ", req);
				createServiceSyncResponse = (String) mqUtils.sendAndReceive(createServiceSync, req,120000);
	            logger.info("createServiceSyncResponse {}", createServiceSyncResponse);	          
			}else if(productType.equalsIgnoreCase("IZOSDWAN_CGW")) {
				Map<String, String> cgwMap  = getCgwMap(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(),
						taskDataMap);
				String req = Utils.convertObjectToJson(cgwMap);
				logger.info("create service cgw constructed map => {} ", req);
				createServiceSyncResponse = (String) mqUtils.sendAndReceive(createServiceSync, req);
	            logger.info("createServiceSyncResponse cgw {}", createServiceSyncResponse);	          
			}
			
			  if (createServiceSyncResponse == null) {
	                execution.setVariable("createServiceCompleted", false);
	                execution.setVariable("createServiceErrorMessage", CramerConstants.SYSTEM_ERROR);
	                errorMessage=CramerConstants.SYSTEM_ERROR;
	            }
	            else {
	                Response response = Utils.convertJsonToObject(createServiceSyncResponse, Response.class);
	                if (Boolean.valueOf(response.getStatus()))
	                    execution.setVariable("createServiceCompleted", true);
	                else {
	                    execution.setVariable("createServiceCompleted", false);
	                    execution.setVariable("createServiceErrorMessage", response.getErrorMessage());
	                    errorMessage=response.getErrorMessage();
	                }
	            }
		
			}else {
	            logger.error("product name / service type is null, so not able to perform create service call ");
			}

        } catch (Exception e) {
            logger.error("CreateServiceDelegate Exception => ", e);
          //to do for testing we have done have to revoke  execution.setVariable("createServiceCompleted", false);
            execution.setVariable("createServiceCompleted", false);

            execution.setVariable("createServiceErrorMessage", e.getMessage());
            errorMessage=CramerConstants.SYSTEM_ERROR;
        }
		
		String message = StringUtils.trimToEmpty(errorMessage);
		if (StringUtils.isNotBlank(message)) {
	        Optional<ScServiceDetail> scServiceDetail=	scServiceDetailRepository.findById(task.getServiceId());
				if (scServiceDetail.isPresent()) {
					logger.info("CreateServiceDelegate error log started");

			componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(), "createServiceErrorMessage", message,  AttributeConstants.ERROR_MESSAGE,"create-service");
		}
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
    }

	/**
	 * private method to construct create service info bean for create service
	 * cramer call.
	 * 
	 * @param feasibilityAttr
	 * @return
	 * @throws TclCommonException
	 */
	/*private CreateServiceSyncBean getCreateServiceSyncBean(String processInstanceId, Map<String, Object> taskDataMap) {
		CreateServiceSyncBean createServiceSyncBean = new CreateServiceSyncBean();
		createServiceSyncBean
				.setServiceId(StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.SERVICE_CODE))));
		createServiceSyncBean
				.setCopfId(StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.ORDER_ID))));
		createServiceSyncBean.setRequestId(processInstanceId);
		createServiceSyncBean.setRequestingSystem(CramerConstants.REQUESTING_SYSTEM);
		String cpeManagementType = String.valueOf(taskDataMap.get(CramerConstants.CPE_MANAGEMENT_TYPE));
		if (cpeManagementType.equalsIgnoreCase(CramerConstants.FULLY_MANAGED)
				|| cpeManagementType.equalsIgnoreCase(CramerConstants.PHYSICALLY_MANAGED)
				|| cpeManagementType.equalsIgnoreCase(CramerConstants.PROACTIVE_MONITORED)
				|| cpeManagementType.equalsIgnoreCase(CramerConstants.PROACTIVE_SERVICES)) {
			createServiceSyncBean.setServiceOption(CramerConstants.MANAGED);
			createServiceSyncBean.setScopeOfManagement(cpeManagementType);
		} else {
			createServiceSyncBean.setServiceOption(CramerConstants.UNMANAGED);
		}

		createServiceSyncBean.setCustomerName(
				StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.CUSTOMER_NAME))));
		createServiceSyncBean.setLmBandwidthUnit(CramerConstants.Mbps);
		createServiceSyncBean.setLmBandwidthValue(
				StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.LOCAL_LOOP_BANDWIDTH))));
		createServiceSyncBean.setServiceBandwidthUnit(CramerConstants.Mbps);
		createServiceSyncBean.setServiceBandwidthValue(
				StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.PORT_BANDWIDTH))));
		createServiceSyncBean.setServiceType(CramerConstants.ILL);
		createServiceSyncBean
				.setSiteCode(StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.SITE_ID))));
		return createServiceSyncBean;
	}*/
	
	
	private Map<String, String> getCgwMap(String string, Map<String, Object> taskDataMap) {
		String orderCategory=Objects.nonNull(taskDataMap.get(CramerConstants.ORDER_CATEGORY))?
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.ORDER_CATEGORY))):null;
		String orderSubCategory=Objects.nonNull(taskDataMap.get(CramerConstants.ORDER_SUB_CATEGORY))?
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.ORDER_SUB_CATEGORY))):null;
		orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
		orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
		Map<String, String> gvpnRequestMap = new HashMap<>();
		gvpnRequestMap.put("SERVICE_ID",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.SERVICE_CODE))));
		gvpnRequestMap.put("SERVICE_TYPE",CramerConstants.GVPN);
		gvpnRequestMap.put("CUSTOMER_NAME",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.CUSTOMER_NAME))));
		gvpnRequestMap.put("SOLUTION_ID",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.SOLUTION_ID))));
		gvpnRequestMap.put("GVPN_ORDER_TYPE",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.ORDER_TYPE))));
		gvpnRequestMap.put("GVPN_ORDER_CATEGORY",Objects.nonNull(orderCategory)?
				orderCategory:null);
		String topology = StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.SERVICE_TOPOLOGY)));
		gvpnRequestMap.put("SERVICE_TOPOLOGY","Mesh");
		return gvpnRequestMap;
	}

	/** getIllMap - create service MAP construction for product type ill/ias
	 * @param processInstanceId
	 * @param taskDataMap
	 * @return
	 */
	private Map<String, String> getIllMap(String processInstanceId, Map<String, Object> taskDataMap) {
		Map<String, String> illRequestMap = new HashMap<>();
		illRequestMap.put("SERVICE_ID",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.SERVICE_CODE))));
		illRequestMap.put("COPF_ID",StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.ORDER_ID))));
		illRequestMap.put("REQUEST_ID",processInstanceId);
		illRequestMap.put("REQUESTING_SYSTEM",CramerConstants.REQUESTING_SYSTEM);
		String cpeManagementType = String.valueOf(taskDataMap.get(CramerConstants.CPE_MANAGEMENT_TYPE));
		if (cpeManagementType.equalsIgnoreCase(CramerConstants.FULLY_MANAGED)
				|| cpeManagementType.equalsIgnoreCase(CramerConstants.PHYSICALLY_MANAGED)
				|| cpeManagementType.equalsIgnoreCase(CramerConstants.PROACTIVE_MONITORED)
				|| cpeManagementType.equalsIgnoreCase(CramerConstants.PROACTIVE_MONITORING)
				|| cpeManagementType.equalsIgnoreCase(CramerConstants.PROACTIVE_SERVICES)) {
			illRequestMap.put("SERVICE_OPTION",CramerConstants.MANAGED);
			illRequestMap.put("SCOPE_OF_MANAGEMENT",cpeManagementType);
		} else {
			illRequestMap.put("SERVICE_OPTION",CramerConstants.UNMANAGED);
			illRequestMap.put("SCOPE_OF_MANAGEMENT","");
		}
		illRequestMap.put("CUSTOMER_NAME",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.CUSTOMER_NAME))));
		
		illRequestMap.put("LM_BANDWIDTH_UNIT",CramerConstants.Mbps);
		illRequestMap.put("LM_BANDWIDTH_VALUE",
				StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.LOCAL_LOOP_BANDWIDTH))));
		illRequestMap.put("SERVICE_BANDWIDTH_UNIT",CramerConstants.Mbps);
		illRequestMap.put("SERVICE_BANDWIDTH_VALUE",
				StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.PORT_BANDWIDTH))));
		illRequestMap.put("SERVICE_TYPE",CramerConstants.ILL);
		illRequestMap.put("SITE_CODE",StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.SITE_ID))));
		try {
			String primarySeconday=StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.PRIMARY_SECONDARY));			
			String link=(String) taskDataMap.getOrDefault("prisecLink",null);
			
			if(primarySeconday.equalsIgnoreCase(CramerConstants.PRIMARY) && link==null) {
				illRequestMap.put(CramerConstants.PRIMARY_SECONDARY,null);
			}else if(primarySeconday.equalsIgnoreCase(CramerConstants.SECONDARY)) {
				illRequestMap.put(CramerConstants.PRIMARY_SECONDARY,"SECONDARY");
			}else if(primarySeconday.equalsIgnoreCase(CramerConstants.PRIMARY)) {
				illRequestMap.put(CramerConstants.PRIMARY_SECONDARY,"PRIMARY");
			}
			illRequestMap.put("prisecLink",link);
		}catch(Exception ee) {
			logger.error("Exception ",ee);
		}
		
		return illRequestMap;
	}
	
	/** getGvpnMap - create service MAP construction for product type gvpn
	 * @param processInstanceId
	 * @param taskDataMap
	 * @return
	 */
	private Map<String, String> getGvpnMap(String processInstanceId, Map<String, Object> taskDataMap) {
		String orderCategory=Objects.nonNull(taskDataMap.get(CramerConstants.ORDER_CATEGORY))?
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.ORDER_CATEGORY))):null;
		String orderSubCategory=Objects.nonNull(taskDataMap.get(CramerConstants.ORDER_SUB_CATEGORY))?
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.ORDER_SUB_CATEGORY))):null;
		orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
		orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
		Map<String, String> gvpnRequestMap = new HashMap<>();
		gvpnRequestMap.put("SERVICE_ID",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.SERVICE_CODE))));
		gvpnRequestMap.put("SERVICE_TYPE",CramerConstants.GVPN);
		gvpnRequestMap.put("CUSTOMER_NAME",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.CUSTOMER_NAME))));
		gvpnRequestMap.put("SOLUTION_ID",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.SOLUTION_ID))));
		gvpnRequestMap.put("GVPN_ORDER_TYPE",
				StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.ORDER_TYPE))));
		gvpnRequestMap.put("GVPN_ORDER_CATEGORY",Objects.nonNull(orderCategory)?
				orderCategory:null);
		String topology = StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.SERVICE_TOPOLOGY)));
		if (topology.toLowerCase().contains("mesh")) {
			gvpnRequestMap.put("SERVICE_TOPOLOGY","Mesh");
		} else if (topology.toLowerCase().contains("hub")) {
			gvpnRequestMap.put("SERVICE_TOPOLOGY","Hub");
		} else {
			gvpnRequestMap.put("SERVICE_TOPOLOGY","Mesh");
		}
		if("MACD".equals(taskDataMap.get(CramerConstants.ORDER_TYPE)) && Objects.nonNull(orderSubCategory)){
			if(orderSubCategory.toLowerCase().contains("parallel") && orderSubCategory.toLowerCase().contains("shifting")){
				logger.info("UPDATE GVPN_ORDER_CATEGORY for PARALLEL SHIFT");
				gvpnRequestMap.put("GVPN_ORDER_CATEGORY","PARALLEL UPGRADE");
			}else if(orderSubCategory.toLowerCase().contains("parallel")){
				logger.info("UPDATE GVPN_ORDER_CATEGORY for PARALLEL");
				gvpnRequestMap.put("GVPN_ORDER_CATEGORY",orderSubCategory);
			}
		}
		return gvpnRequestMap;
	}
	
	private Map<String, String> getNplMap(String processInstanceId, Map<String, Object> taskDataMap) {
		Map<String, String> illRequestMap = new HashMap<>();
		illRequestMap.put("SERVICE_ID",StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.SERVICE_CODE))));
		illRequestMap.put("COPF_ID",StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.ORDER_ID))));
		illRequestMap.put("REQUEST_ID",processInstanceId);
		illRequestMap.put("REQUESTING_SYSTEM",CramerConstants.REQUESTING_SYSTEM);
		
		illRequestMap.put("SERVICE_OPTION",CramerConstants.UNMANAGED);
		illRequestMap.put("SCOPE_OF_MANAGEMENT","");
		
		illRequestMap.put("CUSTOMER_NAME",StringUtils.trimToEmpty((String) (taskDataMap.get(CramerConstants.CUSTOMER_NAME))));
		
		illRequestMap.put("LM_BANDWIDTH_UNIT",CramerConstants.Mbps);
		illRequestMap.put("LM_BANDWIDTH_VALUE",StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.LOCAL_LOOP_BANDWIDTH))));
		illRequestMap.put("SERVICE_BANDWIDTH_UNIT",CramerConstants.Mbps);
		illRequestMap.put("SERVICE_BANDWIDTH_VALUE",StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.PORT_BANDWIDTH))));
		
		String serviceSubType = StringUtils.trimToEmpty(String.valueOf(taskDataMap.get("serviceSubType")));
		
		illRequestMap.put("SERVICE_TYPE",CramerConstants.NPL_INTRACITY);
		
		illRequestMap.put("SITE_CODE",StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.SITE_ID))));
		return illRequestMap;
	}
}
