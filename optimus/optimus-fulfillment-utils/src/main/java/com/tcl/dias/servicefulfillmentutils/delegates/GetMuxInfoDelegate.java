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
import com.tcl.dias.servicefulfillmentutils.beans.MuxInfoSyncBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("getMuxInfoDelegate")
public class GetMuxInfoDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(GetMuxInfoDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${queue.muxinfosync}")
	String muxInfoSyncQueue;

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
        logger.info("getMuxInfoDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
        String errorMessage="";
		String errorCode="";

        Map<String, Object> taskDataMap = new HashMap<>();
        Task task  = workFlowService.processServiceTask(execution);

        try {
            //	Task task = taskService.getTaskByExecution(execution);
            if (Objects.nonNull(task)) {
                taskDataMap = taskDataService.getTaskData(task);
                logger.info("taskDataMap {} ",taskDataMap);
            } else {
                logger.info("Task is null in get mux info sync call delegate");
            }

            MuxInfoSyncBean muxInfoSyncBean = getMuxInfoSyncBean(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(), taskDataMap);
            String req = Utils.convertObjectToJson(muxInfoSyncBean);
            logger.info("muxInfoSyncRequest {} ", req);

            // String muxInfoSyncResponse="true";
            String muxInfoSyncResponse = (String) mqUtils.sendAndReceive(muxInfoSyncQueue, req,120000);
            logger.info("muxInfoSyncResponse {}", muxInfoSyncResponse);


            if (StringUtils.isBlank(muxInfoSyncResponse)) {
                execution.setVariable("isMuxInfoSyncCallSuccess", false);
                execution.setVariable("muxInfoErrorMessage", CramerConstants.SYSTEM_ERROR);
                errorMessage = CramerConstants.SYSTEM_ERROR;
            } else {
                Response response = Utils.convertJsonToObject(muxInfoSyncResponse, Response.class);
                if (Boolean.valueOf(response.getStatus())) {
                    execution.setVariable("isMuxInfoSyncCallSuccess", true);
                } else {
                    execution.setVariable("isMuxInfoSyncCallSuccess", false);
                    execution.setVariable("muxInfoErrorMessage", response.getErrorMessage());
                    errorMessage=response.getErrorMessage();
                    errorCode=response.getErrorCode();

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            execution.setVariable("isMuxInfoSyncCallSuccess", false);
            execution.setVariable("muxInfoErrorMessage", CramerConstants.SYSTEM_ERROR);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
         errorMessage = StringUtils.trimToEmpty(errorMessage);
		if (StringUtils.isNotBlank(errorMessage)) {

			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetail.isPresent()) {
				try {
		            logger.info("GetMuxInfoDelegate started");

					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
							"muxInfoErrorMessage",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
							AttributeConstants.ERROR_MESSAGE, "get-mux-info");
				} catch (TclCommonException e) {
					logger.error(
							"getMuxInfoDelegate------------------- getting error message details----------->{}",
							e);
				}
			}
		}
		
		//temp
		//execution.setVariable("isMuxInfoSyncCallSuccess", true);

        workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }

	/**
	 * private method to construct mux info bean for mux info cramer call.
	 * 
	 * @param processInstanceId
	 * @param taskDataMap
	 * @return
	 */
	private MuxInfoSyncBean getMuxInfoSyncBean(String processInstanceId, Map<String, Object> taskDataMap) {
		MuxInfoSyncBean muxInfoSyncBean = new MuxInfoSyncBean();
		muxInfoSyncBean.setServiceId(StringUtils.trimToEmpty((String)(taskDataMap.get(CramerConstants.SERVICE_CODE))));
		muxInfoSyncBean.setCopfId(StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.ORDER_ID))));
		muxInfoSyncBean.setFeasibilityId(StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.FEASIBILITY_ID))));
		String endMuxNodeName= StringUtils.trimToEmpty((String)(taskDataMap.get("endMuxNodeName")));
		
		String serviceType = StringUtils.trimToEmpty((String)(taskDataMap.get(CramerConstants.PRODUCT_NAME)));
		logger.info("getMuxInfoSyncBean-ServiceId={} serviceType= {} ", muxInfoSyncBean.getServiceId(),serviceType);
		muxInfoSyncBean.setServiceType(serviceType);
		
		muxInfoSyncBean.setRequestId(processInstanceId+"#"+endMuxNodeName);
		muxInfoSyncBean.setRequestingSystem(CramerConstants.OPTIMUS);
		logger.info("Set ORDER TYPE, ORDER CATEGORY Dynamically");
		String orderType=(String)taskDataMap.get(CramerConstants.ORDER_TYPE);
		if(orderType.equals(CramerConstants.TYPE_MACD)){
			String orderCategory= StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.ORDER_CATEGORY));
			String orderSubCategory= StringUtils.trimToEmpty((String)taskDataMap.get(CramerConstants.ORDER_SUB_CATEGORY));
			orderCategory = OrderCategoryMapping.getOrderCategory(orderCategory, orderSubCategory);
			orderSubCategory = OrderCategoryMapping.getOrderSubCategory(orderCategory, orderSubCategory);
			if(StringUtils.isNotBlank(serviceType) && "NPL".equalsIgnoreCase(serviceType)) {
				muxInfoSyncBean.setRequestType(CramerConstants.CHANGE);
				logger.info("NPL MACD orderCategory={} orderSubCategory={}",orderCategory,orderSubCategory);
				if(CramerConstants.ADD_SITE_SERVICE.equals(orderCategory)){
					muxInfoSyncBean.setRequestType(CramerConstants.TYPE_NEW);
					muxInfoSyncBean.setOrderType(CramerConstants.TYPE_NEW);
				}else if(Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("lm")
						|| orderSubCategory.toLowerCase().contains("bso") || orderSubCategory.equalsIgnoreCase("Shifting"))){
					logger.info("LM SHIFTING");
					muxInfoSyncBean.setOrderType("LM SHIFTING");
				}else{
					muxInfoSyncBean.setOrderType(orderSubCategory.toUpperCase());
				}
			
			}else {
				if(CramerConstants.ADD_SITE_SERVICE.equals(orderCategory)){
					logger.info("MUX for ADD SITE or PARALLEL");
					muxInfoSyncBean.setRequestType(CramerConstants.TYPE_NEW);
					muxInfoSyncBean.setOrderType(CramerConstants.TYPE_NEW);
				}else if(Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("parallel"))){
					logger.info("PARALLEL");
					muxInfoSyncBean.setRequestType(CramerConstants.CHANGE);
					muxInfoSyncBean.setOrderType(orderSubCategory.toUpperCase());
				}else if(Objects.nonNull(orderSubCategory) && (orderSubCategory.toLowerCase().contains("lm")
						|| orderSubCategory.toLowerCase().contains("bso") || orderSubCategory.equalsIgnoreCase("Shifting"))){
					logger.info("LM SHIFTING");
					muxInfoSyncBean.setRequestType(CramerConstants.CHANGE);
					muxInfoSyncBean.setOrderType("LM SHIFTING");
				}else{
					logger.info("MUX for not PARALLEL");
					muxInfoSyncBean.setRequestType(CramerConstants.CHANGE);
					muxInfoSyncBean.setOrderType(orderSubCategory.toUpperCase());
					//muxInfoSyncBean.setOrderType((String)taskDataMap.get(CramerConstants.ORDER_CATEGORY));
				}
			}
			
		}else if(orderType.equals(CramerConstants.TYPE_NEW)){
			muxInfoSyncBean.setRequestType(CramerConstants.TYPE_NEW);
			muxInfoSyncBean.setOrderType(CramerConstants.TYPE_NEW);
		}
		muxInfoSyncBean.setBandwidthUnit(CramerConstants.MBPS);
		String bwValue = (String)(taskDataMap.get("localLoopBandwidth"));
		if(bwValue.equals("0.5") || bwValue.equals("0.256") || bwValue.equals("0.25") || bwValue.equals("0.125"))bwValue="1"; 
		
		muxInfoSyncBean.setBandwidthValue(bwValue); 
		if(Objects.nonNull(taskDataMap.get("burstableBwUnit"))){
			logger.info("Burstable bw unit exists ");
			muxInfoSyncBean.setBurstableBandwidthUnit((String)taskDataMap.get("burstableBwUnit"));
		}
		if(Objects.nonNull(taskDataMap.get("burstableBandwidth"))){
			logger.info("Burstable bw exists");
			muxInfoSyncBean.setBurstableBandwidthValue((String)taskDataMap.get("burstableBandwidth"));
		}
		muxInfoSyncBean.setMuxIp(StringUtils.trimToEmpty((String)(taskDataMap.get("endMuxNodeIp"))));
		muxInfoSyncBean.setMuxName(endMuxNodeName);
		
		String interfaceType = StringUtils.trimToEmpty((String)(taskDataMap.get("interface")));
		
		if(interfaceType.equalsIgnoreCase(CramerConstants.FE) || interfaceType.equalsIgnoreCase(CramerConstants.FAST_ETHERNET)) {
			interfaceType = CramerConstants.FAST_ETHERNET;
		}else if(interfaceType.equalsIgnoreCase(CramerConstants.GE) || interfaceType.toLowerCase().contains("gig ethernet") || interfaceType.toLowerCase().contains("gigabit ethernet")){
			interfaceType = CramerConstants.GIGABIT_ETHERNET;
		}else if(interfaceType.contains("10G")){
			interfaceType = "10 G";
		}else if(interfaceType.contains("G.957")){
			interfaceType = "G.957 Optical";
		}else if(interfaceType.contains("1000base LX (Optical)")){
			interfaceType = "1000-BASE-LX";
		}else if(interfaceType.toLowerCase().contains("10-base") || interfaceType.toLowerCase().contains("10 base") || interfaceType.toLowerCase().contains("10base")){
			interfaceType = CramerConstants.FAST_ETHERNET;
		}else if((interfaceType.toLowerCase().contains("100-base") || interfaceType.toLowerCase().contains("100 base") || interfaceType.toLowerCase().contains("100base")) && !interfaceType.toLowerCase().contains("lx") ){
			interfaceType = CramerConstants.FAST_ETHERNET;
		}else if((interfaceType.toLowerCase().contains("100-base") || interfaceType.toLowerCase().contains("100 base") || interfaceType.toLowerCase().contains("100base")) && interfaceType.toLowerCase().contains("lx") ){
			interfaceType = "100-BASE-LX";
		}else if(interfaceType.contains("BASE")){
			interfaceType = interfaceType.trim().replaceAll(" ", "-");
		}else if(interfaceType.toUpperCase().contains("BASE")){
			interfaceType = interfaceType.toUpperCase().trim().replaceAll(" ", "-");
		}
		muxInfoSyncBean.setPortType(interfaceType);
		
		//muxInfoSyncBean.setPortType("Fast Ethernet");
		//muxInfoSyncBean.setPortType("100-Base-TX");
		return muxInfoSyncBean;
	}	
}
