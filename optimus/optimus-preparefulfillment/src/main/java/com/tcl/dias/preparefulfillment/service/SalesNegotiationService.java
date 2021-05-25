package com.tcl.dias.preparefulfillment.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskRemark;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRemarkRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.SalesNegotiationBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.delegates.NotificationDelegate;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional(readOnly = true)
public class SalesNegotiationService extends ServiceFulfillmentBaseService {
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	private TaskRemarkRepository taskRemarkRepository;
	
	@Autowired
	FlowableBaseService flowableBaseService;
	
	@Autowired
	private NotificationDelegate notificationDelegate;
	
	@Autowired
	private CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ProcessL2OService processL2OService;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	private TaskRepository taskRepository;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesNegotiationService.class);
	
	@Transactional(readOnly = false)
	public SalesNegotiationBean processSalesNegotiationCsmTask(SalesNegotiationBean salesNegotiationBean) throws TclCommonException {
		
		LOGGER.info("Inside Peocess Sales Negotiation CSM {}", salesNegotiationBean.getServiceId());
		
		Task task = getTaskByIdAndWfTaskId(salesNegotiationBean.getTaskId(), salesNegotiationBean.getWfTaskId());
		
		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
		
		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
		
		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		if(salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Order Amendment")) {
			description = "Order Amendmended by " + userName;
		}else if(salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Cancellation"))  {
			description = "Order Cancelled by " + userName;
		} else {
			if(scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED_INITIATED)) {
				throw new TclCommonRuntimeException(ExceptionConstants.RESOURCE_RELEASE_INITIATED_NOT_COMPLETE, ResponseResource.R_CODE_ERROR);
			}
			if (scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED)) {
				taskService.processDeleteService(scServiceDetail.getId());
				processL2OService.processReInitateService(scServiceDetail.getId(), false);
				
			}
			description = "Order Moved to Service Delivery by " + userName;
			varMap.put("csmResumeService", true);
			updateServiceStatusAndCreatedNewStatus(scServiceDetail,
					TaskStatusConstants.INPROGRESS, "MOVE_TO_SERVICE_DELIVERY");
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			scServiceDetailRepository.save(scServiceDetail);
		}
		componentMap.put("processNegotiatioInitiationType", salesNegotiationBean.getProcessInitiationType());
		closeCurrentTask(salesNegotiationBean, task, scServiceDetail, userName, description, varMap, componentMap);
		
		LOGGER.info("Sales Negotiation CSM end {}", salesNegotiationBean.getServiceId());
		return salesNegotiationBean;
	}

	private void closeCurrentTask(SalesNegotiationBean salesNegotiationBean, Task task, ScServiceDetail scServiceDetail,
			String userName, String description, Map<String, Object> varMap, Map<String, String> componentMap)
			throws TclCommonException {
		if(!componentMap.isEmpty()) {
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), componentMap, AttributeConstants.COMPONENT_LM,
					AttributeConstants.SITETYPE_A);
		}
		saveTaskRemarks(scServiceDetail, description, userName);
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, salesNegotiationBean.getDelayReason(), null,
				 salesNegotiationBean);
		//Close Current Task
		flowableBaseService.taskDataEntry(task, salesNegotiationBean, varMap);
	}
	
	private void saveTaskRemarks(ScServiceDetail scServiceDetail, String remarks, String userName) {
		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks(remarks);
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}

	/**
	 * @author vivek
	 *
	 * @param salesNegotiationBean
	 * @return
	 * @throws TclCommonException 
	 */
	@Transactional(readOnly = false)
	public SalesNegotiationBean processCustomerDelayResourceTrigger(SalesNegotiationBean salesNegotiationBean) throws TclCommonException {
		Map<String, Object> processMap = new HashMap<>();

		Task task = taskRepository.findById(salesNegotiationBean.getTaskId()).get();
		Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
				.getComponentAttributes(task.getServiceId(), "LM", "A");
		processMap.put(MasterDefConstants.LOCAL_IT_CONTACT_NAME, scComponentAttributesmap.get(MasterDefConstants.LOCAL_IT_CONTACT_NAME));
		processMap.put(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL, scComponentAttributesmap.get(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL));
		notificationDelegate.processCustomerRemainderAndResourceRelease(task.getMstTaskDef().getKey(), task.getServiceId(), task.getServiceCode(), task.getScServiceDetail(), scComponentAttributesmap, processMap, "/optimus/tasks/dashboard");
		return salesNegotiationBean;
	}

	/**
	 * @author vivek
	 *
	 * @param salesNegotiationBean
	 * @return
	 * @throws TclCommonException 
	 */
	@Transactional(readOnly = false)
	public SalesNegotiationBean processNegotiationCimTask(SalesNegotiationBean salesNegotiationBean)
			throws TclCommonException {

		LOGGER.info("Inside Peocess Sales Negotiation CSM {}", salesNegotiationBean.getServiceId());

		Task task = getTaskByIdAndWfTaskId(salesNegotiationBean.getTaskId(), salesNegotiationBean.getWfTaskId());

		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(task.getServiceId());

		ScServiceDetail scServiceDetail = optionalScServiceDetail.get();

		String userName = userInfoUtils.getUserInformation().getUserId();
		String description = "";
		Map<String, Object> varMap = new HashMap<>();
		Map<String, String> componentMap = new HashMap<>();
		varMap.put("pmTaskAction", "CLOSE");
		componentMap.put("processNegotiatioInitiationType", salesNegotiationBean.getProcessInitiationType());

		if (salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Order Amendment")) {
			description = "Order Amendmended by " + userName;
		} else if (salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Cancellation")) {
			description = "Order Cancelled by " + userName;

			LOGGER.info("Sales Negotiation CSM end {}", salesNegotiationBean.getServiceId());
		} else if (salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Resume Service Delivery")) {

			if (scServiceDetail.getMstStatus().getCode()
					.equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED_INITIATED)) {
				throw new TclCommonRuntimeException(ExceptionConstants.RESOURCE_RELEASE_INITIATED_NOT_COMPLETE,
						ResponseResource.R_CODE_ERROR);
			}
			if (scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED)) {
				taskService.processDeleteService(scServiceDetail.getId());
				processL2OService.processReInitateService(scServiceDetail.getId(), false);
				

			}

			description = "Order Moved to Service Delivery by " + userName;
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.INPROGRESS,
					"MOVE_TO_SERVICE_DELIVERY");
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			scServiceDetailRepository.save(scServiceDetail);
			varMap.put("resumeServiceCIM","YES");

		}

		closeCurrentTask(salesNegotiationBean, task, scServiceDetail, userName, description, varMap, componentMap);

		return salesNegotiationBean;

	}
}
