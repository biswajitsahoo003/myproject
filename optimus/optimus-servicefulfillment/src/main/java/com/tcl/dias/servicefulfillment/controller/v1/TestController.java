package com.tcl.dias.servicefulfillment.controller.v1;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.gsc.CreateSiteRequestBean;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRemarkRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.service.VendorService;
import com.tcl.dias.servicefulfillment.service.gsc.GscImplementationService;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SitesBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.delegates.gsc.CreateSiteResponseBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.CriticalPathService;
import com.tcl.dias.servicefulfillmentutils.service.v1.CustomerDepHoldService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscPdfService;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.PdfService;
import com.tcl.dias.servicefulfillmentutils.service.v1.RepcService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WebexPdfService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RestController
class TestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class); 
	private static final String localItContactEmailId = "localItContactEmailId";
	
	@Value("${app.host}")
	String appHost;
	
	@Autowired
	PdfService pdfservice;
	
	@Autowired
	WebexPdfService webexPdfservice;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskService taskService;

	@Autowired
	TaskDataService taskDataService;
	
	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;

	@Autowired
	CriticalPathService criticalPathService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	TaskRemarkRepository taskRemarkRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	CustomerDepHoldService customerDepHoldService;
	
	@Autowired
	GscImplementationService gscImplementationService;
	
	@Autowired
	RepcService repcservice;
	
	@Autowired
	GscPdfService gscPdfService;
	
	@GetMapping(value = "/gsc-sip-pdf/{uuid}/{serviceId}") 
	public void saveSipPdf(@PathVariable("uuid") String uuid, @PathVariable("serviceId")
    Integer serviceId) throws TclCommonException { 
		String resultw =gscPdfService.processSipHandoverPdf(uuid, serviceId,"A");
    }
	@GetMapping(value = "/pdf/{uuid}")
	public void savePdf(@PathVariable("uuid") String uuid, @RequestParam("serviceId") Integer serviceId)
			throws TclCommonException {
		String result = pdfservice.processServiceAcceptancePdf(uuid, serviceId);
	}
	
	@GetMapping(value = "/webex/pdf/{uuid}")
	public void saveWebexPdf(@PathVariable("uuid") String uuid, @RequestParam("serviceId") Integer serviceId)
			throws TclCommonException {
		String result = webexPdfservice.processServiceAcceptancePdf(uuid, serviceId);
	}

	@GetMapping(value = "/pdf/notification/{taskId}")
	public void notificationServiceAcceptance(@PathVariable("taskId") Integer taskId,
			@RequestParam("emailId") String emailId,@RequestParam("custName") String custName) throws TclCommonException {

		Task task = taskService.getTaskById(taskId);
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
			notificationService.notifyCustomerAcceptance(emailId, custName, deliveryDate,
					orderCode, serviceCode, "customer.tatacommunications.com", subject, attachment);

		}
	}
	
	@GetMapping(value = "/mrn/{serviceId}")
	public ResponseResource<String> notifyMrn(@PathVariable("serviceId") Integer serviceId)throws TclCommonException {
		taskService.notifyMrn(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);

	}
	
	@GetMapping(value = "/computeCriticalPath/{serviceId}")
	public void computeCriticalPath(@PathVariable("serviceId") String serviceId)
			throws TclCommonException {
		
		criticalPathService.computeCriticalPath(new Integer(serviceId));
		
	
	}
	
	/*
	 *String customerMail, Integer orderId, String customerName,
			String serviceCode, String serviceLink,String taskName, String orderCode
	 */
	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	@GetMapping(value = "/testOverdueAll/{serviceId}")
	public void notifyOverdue(@PathVariable("serviceId") Integer serviceId,@RequestParam("taskKey") String taskKey) throws TclCommonException {

		String customerName = "CustomerTest";
		String customerUrl = appHost + "/optimus";
		String customerEmail = "vivekkumar.k@tatacommunications.com";
		Optional<ScServiceDetail> optionalService=scServiceDetailRepository.findById(serviceId);
		ScServiceDetail scServiceDetail=	optionalService.get();
		
		List<String> toAddresses = new ArrayList<>();
		List<String> ccAddresses = new ArrayList<>();
		ScComponentAttribute customerContactEmailId = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), localItContactEmailId,  AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
		if(customerContactEmailId != null && customerContactEmailId.getAttributeValue() != null) {
			toAddresses.add(customerContactEmailId.getAttributeValue());
		} else {
			LOGGER.error("Customer contact Email Id is not there for given service Id " + scServiceDetail.getId());
			return;
		}
		if(scServiceDetail.getAssignedPM() != null) {
			ccAddresses.add(scServiceDetail.getAssignedPM());
		}
		
		Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId, taskKey);

		LOGGER.info("Inside notifyOverdue for service Id {}",serviceId);
		if (task != null && (task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
				|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD)
				|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)
				|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED))) {

			/*if (customerDepHoldService.is29DayTrigger(task)) {
				LOGGER.info("Inside is29DayTrigger {} for service Id {}",
						ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()),
						serviceId);
				LOGGER.info("Inside notifyOverDueReminderCustomerBeforeHold for service Id {}", serviceId);
				notificationService.notifyOverDueReminderCustomerBeforeHold(customerEmail,
						task.getScOrderId(), customerName, task.getServiceCode(), customerUrl,
						task.getMstTaskDef().getName(), task.getOrderCode(),toAddresses,ccAddresses);
			} else if (customerDepHoldService.is30DayHoldTrigger(task)) {
				LOGGER.info("Inside is30DayHoldTrigger {} for service Id {}",
						ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()),
						serviceId);
				LOGGER.info("Inside notifyForHold for service Id {}", serviceId);
				customerDepHoldService.notifyForHold(scServiceDetail, task, serviceId, customerEmail, customerName, customerUrl,toAddresses,ccAddresses);

			} else {
				LOGGER.info("Inside last else {} for service Id {}",
						ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()),
						serviceId);
				LOGGER.info("Inside notifyOverDueReminderCustomer for service Id {}", serviceId);
				notificationService.notifyOverDueReminderCustomer(customerEmail, task.getScOrderId(),
						customerName, task.getServiceCode(), customerUrl, task.getMstTaskDef().getName(),
						task.getOrderCode());
			}*/
		}
	
	}
	
	@GetMapping(value = "/createNewSite")
	public void computeCriticalPath() throws TclCommonException {
		String request = "{\r\n" + 
				"  \"customerId\": 21016,\r\n" + 
				"  \"taskId\": \"65277\",\r\n" + 
				"  \"wfTaskId\": \"96bf2572-72d5-22eb-b4c0-0242ac22002a\",\r\n" + 
				"  \"siteDetails\": [\r\n" + 
				"    {\r\n" + 
				"      \"corrolationId\": \"TEST1\",\r\n" + 
				"      \"siteName\": \"SITE 1 for TEST 1\",\r\n" + 
				"      \"siteType\": \"Customer\",\r\n" + 
				"      \"ownerType\": \"TCL\",\r\n" + 
				"      \"geoSpaceCode\": \"LAND\",\r\n" + 
				"      \"addressSeqNo\": \"3\",\r\n" + 
				"      \"location\": [\r\n" + 
				"        {\r\n" + 
				"          \"cityAbbr\": \"BER\",\r\n" + 
				"          \"countryAbbr\": \"GEW\"\r\n" + 
				"        }\r\n" + 
				"      ],\r\n" + 
				"      \"siteFunctions\": [\r\n" + 
				"        {\r\n" + 
				"          \"siteFunctionCd\": \"VOL\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"          \"siteFunctionCd\": \"VOG\"\r\n" + 
				"        }\r\n" + 
				"      ],\r\n" + 
				"      \"switchingUnitRequired\": true,\r\n" + 
				"      \"switchingUnitModel\": \"CISCO\",\r\n" + 
				"      \"contact\": [\r\n" + 
				"        {\r\n" + 
				"          \"phoneNumber\": \"4903011112222\",\r\n" + 
				"          \"emailAddress\": \"test.test@test.com\"\r\n" + 
				"        }\r\n" + 
				"      ]\r\n" + 
				"    }\r\n" + 
				"  ]\r\n" + 
				"}";
		CreateSiteRequestBean createSiteRequestBean = Utils.fromJson(request, new TypeReference<CreateSiteRequestBean>() {});
	//	gscImplementationService.createNewSite(createSiteRequestBean);
		repcservice.requestForCreateNewSite(request, 3715, "uriehrh837534959", "");
	}
	
}
