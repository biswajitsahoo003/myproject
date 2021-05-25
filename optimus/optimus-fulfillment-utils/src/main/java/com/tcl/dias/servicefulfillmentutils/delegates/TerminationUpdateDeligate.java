/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.delegates;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import com.tcl.dias.servicefulfillmentutils.service.v1.RfService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceStatusDetails;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceStatusDetailsRepository;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 *
 */

@Component("terminationStatusUpdateDelegate")
public class TerminationUpdateDeligate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(TerminationUpdateDeligate.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.o2c.si.terminate.queue}")
	private String siTerminateDetail;

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	TaskService taskService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	TaskCacheService taskCacheService;

	@Autowired
	ServiceStatusDetailsRepository serviceStatusDetailsRepository;

	@Autowired
	private UserInfoUtils userInfoUtils;

	@Value("${rabbitmq.sa.service.terminate.queue}")
	private String saServiceTerminateQueue;

	@Autowired
	RfService rfService;

	@Override
	public void execute(DelegateExecution execution) {
		logger.info("TerminationUpdateDeligate  invoked for {} ", execution.getCurrentActivityId());
		String errorMessage = "";
		Task task = null;
		try {
			task = workFlowService.processServiceTask(execution);
			Map<String, Object> varibleMap = execution.getVariables();
			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
			String serviceCode = (String) varibleMap.get(MasterDefConstants.SERVICE_CODE);
			updateServiceDetailsToTerminate(serviceId,serviceCode);
		} catch (Exception e) {
			logger.error("TerminationUpdateDeligate Exception {} ", e);
			errorMessage = CramerConstants.SYSTEM_ERROR;
		}
		errorMessage = StringUtils.trimToEmpty(errorMessage);

		workFlowService.processServiceTaskCompletion(execution, errorMessage);
	}

	@Transactional(readOnly = true)
	public void updateServiceDetailsToTerminate(Integer ServiceId, String serviceCode) throws TclCommonException {

		Optional<ScServiceDetail> opOldActiveScServiceDetail = scServiceDetailRepository.findById(ServiceId);
		
		ScServiceDetail activeAndNotMigratedService = scServiceDetailRepository.findFirstByUuidAndMstStatus_codeAndIsMigratedOrder(serviceCode,TaskStatusConstants.ACTIVE, "N");
		if (activeAndNotMigratedService!=null) {
			activeAndNotMigratedService.setMstStatus(taskCacheService.getMstStatus("TERMINATE"));
			activeAndNotMigratedService.setUpdatedDate(new Timestamp(new Date().getTime()));
			activeAndNotMigratedService.setServiceConfigDate(new Timestamp(new Date().getTime()));
			activeAndNotMigratedService.setServiceConfigStatus(TaskStatusConstants.TERMINATE);
			scServiceDetailRepository.save(activeAndNotMigratedService);
			updateServiceStatusAndCreatedNewStatus(activeAndNotMigratedService, "TERMINATE");
		}
		
		if (opOldActiveScServiceDetail.isPresent()) {
			logger.info("Changing SC OLD SERVICE ID ACTIVE TO TERMINATE");
			ScServiceDetail oldActiveScServiceDetail = opOldActiveScServiceDetail.get();
			updateServiceStatusAndCreatedNewStatus(oldActiveScServiceDetail, "TERMINATE");

			oldActiveScServiceDetail.setMstStatus(taskCacheService.getMstStatus("TERMINATE"));
			oldActiveScServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
			oldActiveScServiceDetail.setServiceConfigDate(new Timestamp(new Date().getTime()));
			oldActiveScServiceDetail.setServiceConfigStatus(TaskStatusConstants.TERMINATE);
			scServiceDetailRepository.save(oldActiveScServiceDetail);
			mqUtils.send(saServiceTerminateQueue, String.valueOf(oldActiveScServiceDetail.getId()));
			mqUtils.send(siTerminateDetail, oldActiveScServiceDetail.getUuid());
			rfService.populateP2PandPmpTerminationData(oldActiveScServiceDetail);

		}
	}

	private void updateServiceStatusAndCreatedNewStatus(ScServiceDetail scServiceDetail, String status) {

		ServiceStatusDetails serviceStatusDetails = serviceStatusDetailsRepository
				.findFirstByScServiceDetail_idOrderByIdDesc(scServiceDetail.getId());

		if (serviceStatusDetails != null) {
			serviceStatusDetails.setEndTime(new Timestamp(new Date().getTime()));
			serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));
			serviceStatusDetailsRepository.save(serviceStatusDetails);
		}
		createServiceStaus(scServiceDetail, status);
	}

	private ServiceStatusDetails createServiceStaus(ScServiceDetail scServiceDetail, String mstStatus) {

		ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
		serviceStatusDetails.setScServiceDetail(scServiceDetail);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			serviceStatusDetails.setUserName(userInfoUtils.getUserInformation().getUserId());
		}
		serviceStatusDetails.setCreatedTime(new Timestamp(new Date().getTime()));
		serviceStatusDetails.setStartTime(new Timestamp(new Date().getTime()));
		serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));

		serviceStatusDetails.setStatus(mstStatus);
		serviceStatusDetailsRepository.save(serviceStatusDetails);

		return serviceStatusDetails;

	}
}
