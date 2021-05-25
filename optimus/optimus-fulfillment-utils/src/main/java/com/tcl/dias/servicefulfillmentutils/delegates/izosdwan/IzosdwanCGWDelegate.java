package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.DownTimeDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("sdwanCGWDelegate")
public class IzosdwanCGWDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanCGWDelegate.class);
		
		@Autowired
		WorkFlowService workFlowService;
		
		@Autowired
		ScSolutionComponentRepository scSolutionComponentRepository;
		
		@Autowired
		ScComponentRepository scComponentRepository;
		
		@Autowired
		TaskRepository taskRepository;
		
		@Autowired
		DownTimeDetailsRepository downTimeDetailsRepository;
		
		@Autowired
		ScComponentAttributesRepository scComponentAttributesRepository;

		@Autowired
		ScServiceDetailRepository scServiceDetailRepository;
		
		@Autowired
		ComponentAndAttributeService componentAndAttributeService;


	public void execute(DelegateExecution execution) {
		logger.info("SdwanCGWDelegate  invoked");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		Integer solutionId = (Integer) execution.getVariable("solutionId");
		String cgwType = (String) execution.getVariable("cgwType");
		logger.info("SdwanCGWDelegate.solutionId={},serviceId={},serviceCode={},executionProcessInstId={}", solutionId,serviceId,serviceCode,execution.getProcessInstanceId());
		List<Integer> overlayDCDRServiceIdList=scSolutionComponentRepository.findAllByScServiceDetail3_idAndComponentGroupAndPriorityAndIsActive(solutionId,"OVERLAY","DC/DR","Y");
		if(overlayDCDRServiceIdList!=null && !overlayDCDRServiceIdList.isEmpty()){
			logger.info("DC/DR exists for CGW Service Id::{}",serviceId);
			logger.info("Overlay DC/DR Site exists with List Size::{}",overlayDCDRServiceIdList.size());
			List<Task> serviceAcceptanceList = taskRepository
					.findByServiceIdInAndMstTaskDef_keyAndSiteTypeAndMstStatus_codeOrderByCreatedTimeDesc(
							overlayDCDRServiceIdList, "sdwan-service-acceptance","A", "CLOSED");
			if(serviceAcceptanceList!=null && !serviceAcceptanceList.isEmpty()){
				logger.info("OverlayId Service Acceptance task completed with Acceptance Size::{}",serviceAcceptanceList.size());
				execution.setVariable("isOverlayAcceptanceCompleted", true);
				/*if ("Primary".equals(cgwType)) {
					try {
						generateBillStartDate(null, null, serviceId);
					} catch (TclCommonException e) {
						logger.error("SdwanCGWDelegate Exception {}", e);
					}
				}*/
			}else{
				logger.info("OverlayId Service Acceptance task not yet completed={}",overlayDCDRServiceIdList.size());
				execution.setVariable("isOverlayAcceptanceCompleted", false);
				checkAcceptanceTaskCompletedforP1(solutionId,serviceId,execution);
			}
		}else{
			logger.info("Overlay P1 Alone Exists");
			checkAcceptanceTaskCompletedforP1(solutionId,serviceId,execution);
		}
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,"");
	}


	private void checkAcceptanceTaskCompletedforP1(Integer solutionId, Integer serviceId, DelegateExecution execution) {
		logger.info("checkAcceptanceTaskCompletedforP1.DC/DR not exists for CGW Service Id::{}",serviceId);
		List<Integer> overlayP1ServiceIdList=scSolutionComponentRepository.findAllByScServiceDetail3_idAndComponentGroupAndPriorityAndIsActive(solutionId,"OVERLAY","P1","Y");
		if(overlayP1ServiceIdList!=null && !overlayP1ServiceIdList.isEmpty()){
			logger.info("Overlay P1 Site exists with List Size::{}",overlayP1ServiceIdList.size());
			List<Task> serviceAcceptanceList = taskRepository
					.findByServiceIdInAndMstTaskDef_keyAndSiteTypeAndMstStatus_codeOrderByCreatedTimeDesc(
							overlayP1ServiceIdList, "sdwan-service-acceptance","A", "CLOSED");
			if(serviceAcceptanceList!=null && !serviceAcceptanceList.isEmpty()){
				logger.info("OverlayId Service Acceptance task completed with Acceptance Size::{}",serviceAcceptanceList.size());
				execution.setVariable("isOverlayAcceptanceCompleted", true);
				/*if ("Primary".equals(cgwType)) {
					try {
						generateBillStartDate(null, null, serviceId);
					} catch (TclCommonException e) {
						logger.error("SdwanCGWDelegate Exception {}", e);
					}
				}*/
			} else {
				logger.info("OverlayId Service Acceptance task not yet completed={}",overlayP1ServiceIdList.size());
				execution.setVariable("isOverlayAcceptanceCompleted", false);
			}
		}else{
			logger.info("OverlayId Service Id not exists either with DC/DR or P1");
			execution.setVariable("isOverlayAcceptanceCompleted", false);
		}
	}
	
	/*public void generateBillStartDate(ScServiceDetail scServiceDetail, String commissioningDate, Integer serviceId)
			throws TclCommonException {
		logger.info("generateBillStartDate invoked for Underlay Service id {} ", serviceId);

		Map<String, String> atMap = new HashMap<>();

		if (scServiceDetail == null) {
			scServiceDetail = scServiceDetailRepository.findById(serviceId).get();
		}

		Date commDate = new Date();
		try {
			if (commissioningDate != null)
				commDate = new SimpleDateFormat("yyyy-MM-dd").parse(commissioningDate);
		} catch (Exception ee) {
			logger.error("commissioningDateException {}", ee);
		}

		atMap.put("commissioningDate", DateUtil.convertDateToString(commDate));
		LocalDateTime commissioningDateLD = LocalDateTime.ofInstant(commDate.toInstant(), ZoneId.systemDefault());
		ScComponentAttribute billFreePeriodComponet = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), "billFreePeriod", "LM", "A");
		if (billFreePeriodComponet != null) {
			int billFreePeriod = 0;
			try {
				if (StringUtils.isNotBlank(billFreePeriodComponet.getAttributeValue()))
					billFreePeriod = Integer.parseInt(billFreePeriodComponet.getAttributeValue());
			} catch (Exception ee) {
			}
			atMap.put("billStartDate",
					DateUtil.convertDateToString(Timestamp.valueOf(commissioningDateLD.plusDays(billFreePeriod))));

		} else {
			atMap.put("billStartDate", DateUtil.convertDateToString(Timestamp.valueOf(commissioningDateLD)));
		}
		componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM,"A");
	}*/

}
