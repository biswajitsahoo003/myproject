package com.tcl.dias.servicefulfillment.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskService taskService;

	@Autowired
	TaskDataService taskDataService;

    @Test
    public void mrnEmailTest() throws TclCommonException{
    	notificationService.notifyMrnEmail("diksha.garg@tatacommunications.com","091CHEN0300A0002014");
    }
    
    @Test
	public void notificationServiceAcceptance() throws TclCommonException {

		Task task = taskService.getTaskById(24187);
		if (task != null) {

			Map<String, Object> taskDataMap = taskDataService.getTaskData(task);

			String customerName = StringUtils
					.trimToEmpty((String) taskDataMap.get(MasterDefConstants.LOCAL_IT_CONTACT_NAME));
//			String customerEmail = StringUtils
//					.trimToEmpty((String) taskDataMap.get(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL));

			String serviceCode = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SERVICE_CODE));
			String orderCode = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.ORDER_CODE));
			String type = task.getMstTaskDef().getTitle();
			String subject = task.getMstTaskDef().getName();

//			customerName = customerName.replace("@legomail.com", "");
			
			String deliveryDate = StringUtils.trimToEmpty((String) taskDataMap.get("customerAcceptanceDate"));	
			AttachmentBean attachment = (AttachmentBean) taskDataMap.get("Handover-note");
//			String url = appHost + "/optimus/tasks/dashboard";	
//			String customerUrl = appHost + "/optimus";						
			notificationService.notifyCustomerAcceptance("diksha.garg@tatacommunications.com","Test",deliveryDate,orderCode,serviceCode,"url",subject,attachment);



		}
	}
}
