package com.tcl.dias.servicefulfillment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.TestOffnetWirelessLMBean;
import com.tcl.dias.servicefulfillment.beans.TestOnnetWirelessLMBean;
import com.tcl.dias.servicefulfillment.beans.TestOnnetWirelineLMBean;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskData;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.FlowableBaseService;
import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceFulfillmentBaseService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Transactional(readOnly = true)
@Service
public class LMTestService extends ServiceFulfillmentBaseService{
	

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	  @Autowired
	  FlowableBaseService flowableBaseService;

	/**
     * This method is used to Conduct LM Test for Onnet wireline
	 *
	 * @param taskId
	 * @param conductLMTestBean
	 * @return ConductLMTestBean
	 * @throws TclCommonException
	 * @author Yogesh
	 * conductLMTest
	 */
	@Transactional(readOnly = false)
	public TestOnnetWirelineLMBean conductLMTestOnnetWireline( TestOnnetWirelineLMBean conductLMTestBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(conductLMTestBean.getTaskId(), conductLMTestBean.getWfTaskId());
		validateInputs(task, conductLMTestBean);
		processTaskLogDetails(task,"CLOSED",conductLMTestBean.getDelayReason(),null, conductLMTestBean);
		
		
		Map<String, String> atMap = new HashMap<>();
		atMap.put("lmTestingCompletedDate", String.valueOf(conductLMTestBean.getlMTestCompletionDate()));
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		
		if(!CollectionUtils.isEmpty(conductLMTestBean.getDocumentIds())){
			conductLMTestBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("appointmentAction", "close");
		return (TestOnnetWirelineLMBean) flowableBaseService.taskDataEntry(task, conductLMTestBean,wfMap);
	}
	
	/**
     * This method is used to Conduct LM Test for Onnet Wireless
	 *
	 * @param taskId
	 * @param testOnnetWirelessLMBean
	 * @return TestOnnetWirelessLMBean
	 * @throws TclCommonException
	 * @author Yogesh
	 * conductLMTestOnnetWireless 
	 */
	@Transactional(readOnly = false)
	public TestOnnetWirelessLMBean conductLMTestOnnetWireless( TestOnnetWirelessLMBean testOnnetWirelessLMBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(testOnnetWirelessLMBean.getTaskId(),testOnnetWirelessLMBean.getWfTaskId());
		validateInputs(task, testOnnetWirelessLMBean);
		processTaskLogDetails(task,"CLOSED",testOnnetWirelessLMBean.getDelayReason(),null, testOnnetWirelessLMBean);
		
		if(!CollectionUtils.isEmpty(testOnnetWirelessLMBean.getDocumentIds())){
			testOnnetWirelessLMBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("appointmentAction", "close");
		return (TestOnnetWirelessLMBean) flowableBaseService.taskDataEntry(task, testOnnetWirelessLMBean,wfMap);
	}
	
	/**
     * This method is used to Conduct LM Test for Offnet Wireless
	 *
	 * @param taskId
	 * @param testOffnetWirelessLMBean
	 * @return TestOffnetWirelessLMBean
	 * @throws TclCommonException
	 * @author Yogesh
	 * conductLMTestOffnetWireless
	 */
	@Transactional(readOnly = false)
	public TestOffnetWirelessLMBean conductLMTestOffnetWireless(TestOffnetWirelessLMBean testOffnetWirelessLMBean) throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(testOffnetWirelessLMBean.getTaskId(),testOffnetWirelessLMBean.getWfTaskId());
		validateInputs(task, testOffnetWirelessLMBean);
		processTaskLogDetails(task,"CLOSED",testOffnetWirelessLMBean.getDelayReason(),null, testOffnetWirelessLMBean);
		

		if(!CollectionUtils.isEmpty(testOffnetWirelessLMBean.getDocumentIds())){
			testOffnetWirelessLMBean.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}
		
		Map<String, String> atMap = new HashMap<>();
		atMap.put("lmTestingCompletedDate", String.valueOf(testOffnetWirelessLMBean.getLastMileTestCompletionDate()));
		componentAndAttributeService.updateAttributes(task.getServiceId(), atMap, AttributeConstants.COMPONENT_LM,task.getSiteType());
		
		Map<String, Object> wfMap = new HashMap<>();
		wfMap.put("appointmentAction", "close");
		return (TestOffnetWirelessLMBean) flowableBaseService.taskDataEntry(task, testOffnetWirelessLMBean,wfMap);
	}
	
	@Transactional(readOnly = false)
	public void saveAttachement(Map<String, Object> map) throws TclCommonException {
		List<TaskData> taskDet = taskDataRepository.findTaskDataForConductLmTest();
		if (!taskDet.isEmpty()) {
			TestOnnetWirelineLMBean taskDataBean = null;
			for (TaskData taskData : taskDet) {
				taskDataBean = Utils.fromJson(taskData.getData(), new TypeReference<TestOnnetWirelineLMBean>() {
				});
				Task task = taskData.getTask();
				if (taskDataBean != null && !taskDataBean.getDocumentIds().isEmpty()) {
					taskDataBean.getDocumentIds().stream().forEach(document -> {
						ScAttachment scAttachment = new ScAttachment();
						if (task.getScServiceDetail() != null) {
							scAttachment.setScServiceDetail(task.getScServiceDetail());
							scAttachment.setIsActive("Y");
							scAttachment.setServiceCode(task.getServiceCode());
							scAttachment.setSiteType(task.getSiteType());
							Optional<Attachment> optAttachment = attachmentRepository
									.findById(document.getAttachmentId());
							if (optAttachment.isPresent()) {
								Attachment attachment = optAttachment.get();
								scAttachment.setAttachment(attachment);
							}
							scAttachmentRepository.save(scAttachment);
						}
					});
				}
			}
		}
	}
}
