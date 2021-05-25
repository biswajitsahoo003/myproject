package com.tcl.dias.servicefulfillment.controller;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.servicefulfillment.beans.AdditionalTechnicalDetailsBean;
import com.tcl.dias.servicefulfillment.beans.BuildingAuthorityContractRequest;
import com.tcl.dias.servicefulfillment.beans.CapexBean;
import com.tcl.dias.servicefulfillment.beans.CompleteOspAcceptanceBean;
import com.tcl.dias.servicefulfillment.beans.ConductSiteSurveyBean;
import com.tcl.dias.servicefulfillment.beans.ConductSiteSurveyManBean;
import com.tcl.dias.servicefulfillment.beans.ConfigureMuxBean;
import com.tcl.dias.servicefulfillment.beans.CreateInventoryRecord;
import com.tcl.dias.servicefulfillment.beans.DeliverMuxBean;
import com.tcl.dias.servicefulfillment.beans.IBDBean;
import com.tcl.dias.servicefulfillment.beans.InstallMuxBean;
import com.tcl.dias.servicefulfillment.beans.IntegrateMuxBean;
import com.tcl.dias.servicefulfillment.beans.MrnOspMuxRequest;
import com.tcl.dias.servicefulfillment.beans.OSPBean;
import com.tcl.dias.servicefulfillment.beans.PRowRequest;
import com.tcl.dias.servicefulfillment.beans.PaymentBean;
import com.tcl.dias.servicefulfillment.beans.PlannedEventBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoBuildingAuthorityBean;
import com.tcl.dias.servicefulfillment.beans.ProwCostApproval;
import com.tcl.dias.servicefulfillment.beans.RowBeanRequest;
import com.tcl.dias.servicefulfillment.beans.SiteReadinessDetailBean;
import com.tcl.dias.servicefulfillment.beans.VendorDetailsBean;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.objectcreator.ObjectCreator;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.CustomerAppointmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.MuxDetailBean;
import com.tcl.dias.servicefulfillmentutils.delegates.CreateServiceDelegate;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

 
/**
 * Test Case file for Enrichment Controller
 *
 * @author Mayank S
 *
 */

//@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EnhancedEnrichmentControllerTest extends Thread{
	
	@MockBean
	CreateServiceDelegate createServiceDelegate;
	
	@Autowired
	private ObjectCreator objectCreator;

	@Autowired
	private TaskRepository taskRepo;

	@Autowired
	TestRestTemplate restTemplate;
	
	@Value("${keycloak.auth-server-url}")
	private String authServer;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${keycloak.resource}")
	private String resource;

	@Value("${servicefulfillment.keycloak.testUser}")
	private String keycloakTestUser;

	@Value("${servicefulfillment.keycloak.testPassword}")
	private String keycloakTestPassword;

	@Autowired
	RuntimeService runtimeService;
	

	@Before
	public void init() throws TclCommonException{
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getRestTemplate().getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		//interceptors.add(new KeycloakInterceptor(authServer, realm, resource, keycloakTestUser, keycloakTestPassword));
		restTemplate.getRestTemplate().setInterceptors(interceptors);
		
		
	}
	
	/**
	 * This method is used to test Enrichment Workflow
	 * 
	 * @throws TclCommonException
	 * @throws InterruptedException 
	 */
	
	@Test
	public void testSaveAdditionalTechDetails() throws TclCommonException, InterruptedException {
		
		Integer serviceId = 1073;

		
		
		
		
		// Task : Validate Supporting Documents
			List<Task> taskList = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "validate-supporting-document");
			if (!taskList.isEmpty()){
			
			Integer taskId = taskList.get(0).getId();

			sleep(1000);

			HttpEntity<List<AttachmentBean>> requestEntity1 = new HttpEntity<>(objectCreator.validateSupportingDocument());
			ResponseEntity<ResponseResource> responseEntity1 = restTemplate.postForEntity("/v1/attachment/validation/task/" + taskId, requestEntity1, ResponseResource.class);
			}
			
		// Task : Additional Technical Details
			List<Task> taskList1 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "advanced-enrichment");
			if (!taskList1.isEmpty()){
				Integer taskId1 = taskList1.get(0).getId();

				sleep(1000);

				HttpEntity<AdditionalTechnicalDetailsBean> requestEntity = new HttpEntity<>(objectCreator.createAdditionalTechnicalDetails());
				ResponseEntity<ResponseResource> responseEntity = restTemplate.postForEntity("/v1/lm-implementation/task/additional-technical-details/" + taskId1, requestEntity, ResponseResource.class);
			}
		 

//		// Task : Confirm Site Readiness Details
		List<Task> taskList2 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-confirm-site-readiness-details");
		if(!taskList2.isEmpty()) {
			Integer taskId2 = taskList2.get(0).getId();

			sleep(1000);

			HttpEntity<SiteReadinessDetailBean> requestEntity1 = new HttpEntity<SiteReadinessDetailBean>(objectCreator.createSiteReadinessDetailBean());
			ResponseEntity<ResponseResource> responseEntity1 = restTemplate.postForEntity("/v1/lm-implementation/task/site-readiness-confirmation/" + taskId2, requestEntity1, ResponseResource.class);
		}
		
//		// Task: Create Service Manual
//		List<Task> taskList0 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "create-service-manual");
//		
//		
//		if (!taskList0.isEmpty()){
//		
//		Integer taskId = taskList0.get(0).getId();
//
//		sleep(1000);
//
//		HttpEntity<RaiseJeopardy> requestEntity1 = new HttpEntity<>(objectCreator.createServiceManual());
//		ResponseEntity<ResponseResource> responseEntity1 = restTemplate.postForEntity("/v1/offnet-lm-implementation/task/raise-jeopardy/" + taskId, requestEntity1, ResponseResource.class); 
//		
//		}
//		Mockito.when(methodCall)
//		
//		Mockito.doNothing().when(createServiceDelegate).execute(Mockito.any());	
		
//		CreateServiceDelegate createServiceDelegate;
//		DelegateExecution execution;
//		createServiceDelegate.execute(execution);
//		execution.setVariable("createServiceCompleted", true);
		
//		ResponseEntity<ResponseResource> responseEntity1 = restTemplate.postForEntity("/v1/offnet-lm-implementation/task/raise-jeopardy/" + taskId, requestEntity1, ResponseResource.class); 
		
	
		//
//
//		//	"---------------------------------------------------------------------------------------------------------------------------------------------------"
//
//		// Next Flow : LM Implementation
//
//		// Task : Provide Appointment for Site Survey
		
	
		List<Task> taskList3 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"customer-appointment-rf-ss");

		if(!taskList3.isEmpty()) {
			Integer taskId3 = taskList3.get(0).getId();

			sleep(1000);

			// without attachmentpermissionid -- TODO <<need to create another flow with attachmentpermissionid>>
			HttpEntity<CustomerAppointmentBean> requestEntity2 = new HttpEntity<CustomerAppointmentBean>(objectCreator.createCustomerAppointmentBean());
			ResponseEntity<ResponseResource> responseEntity2 = restTemplate.postForEntity("/v1/lm-implementation/task/customer-appointments/" + taskId3, requestEntity2, ResponseResource.class);
		}
		//
//
//		// Task : Select Vendor for Site Survey
//z
		List<Task> taskList4 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"select-vendor-rf-ss");
		if(!taskList4.isEmpty()) {
			Integer taskId4 = taskList4.get(0).getId();

			sleep(1000);

			HttpEntity<VendorDetailsBean> requestEntity3 = new HttpEntity<VendorDetailsBean>(objectCreator.createVendorDetailsBean());
			ResponseEntity<ResponseResource> responseEntity3 = restTemplate.postForEntity("/v1/lm-implementation/task/select-vendors/" + taskId4, requestEntity3, ResponseResource.class);
		}

		// Task : Arrange Field Engineer for Site Survey

		List<Task> taskList5 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"arrange-field-engineer-ss-man");
		if(!taskList5.isEmpty()) {
			Integer taskId5 = taskList5.get(0).getId();

			sleep(1000);

			HttpEntity<FieldEngineerDetailsBean> requestEntity4 = new HttpEntity<FieldEngineerDetailsBean>(objectCreator.createFieldEngineerDetailsBean());
			ResponseEntity<ResponseResource> responseEntity4 = restTemplate.postForEntity("/v1/lm-implementation/task/field-engineer-arrangements/" + taskId5, requestEntity4, ResponseResource.class);
		}

		// Task : Conduct Survey

		List<Task> taskList6 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-conduct-site-survey");
		if(!taskList6.isEmpty()) {
			Integer taskId6 = taskList6.get(0).getId();

			sleep(1000);

			HttpEntity<ConductSiteSurveyBean> requestEntity5 = new HttpEntity<ConductSiteSurveyBean>(objectCreator.createConductSiteSurveyBean());
			ResponseEntity<ResponseResource> responseEntity5 = restTemplate.postForEntity("/v1/lm-implementation/task/conduct-site-survey/" + taskId6, requestEntity5, ResponseResource.class);
		}

        // Task : Conduct SurveyMan

        List<Task> taskList6M = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-conduct-site-survey-man");
        if(!taskList6M.isEmpty()) {
            Integer taskId6 = taskList6M.get(0).getId();

            sleep(1000);

            HttpEntity<ConductSiteSurveyManBean> requestEntity5 = new HttpEntity<ConductSiteSurveyManBean>(objectCreator.createConductSiteSurveyManBean());
            ResponseEntity<ResponseResource> responseEntity5 = restTemplate.postForEntity("/v1/lm-implementation/task/conduct-site-survey-man/" + taskId6, requestEntity5, ResponseResource.class);
        }

//		List<Task> taskList7 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-define-project-plan");
//		if(!taskList7.isEmpty()) {
//			Integer taskId7 = taskList7.get(0).getId();
//
//			sleep(1000);
//
//			HttpEntity<DefineScopeWorkProjectPlanBeanList> requestEntity6 = new HttpEntity<>(objectCreator.createDefineScopeWorkProjectPlanBeanList());
//			ResponseEntity<ResponseResource> responseEntity6 = restTemplate.postForEntity("/v1/lm-implementation/task/ss-project-plan/" + taskId7, requestEntity6, ResponseResource.class);
//		}


		// Task : Approve Capex
		List<Task> taskList8 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-approve-capex");
		if(!taskList8.isEmpty()) {
			Integer taskId8 = taskList8.get(0).getId();

			sleep(1000);

			HttpEntity<CapexBean> requestEntity7 = new HttpEntity<CapexBean>(objectCreator.createCapexBean());
			ResponseEntity<ResponseResource> responseEntity7 = restTemplate.postForEntity("/v1/lm-implementation/task/approve-capex/" + taskId8, requestEntity7, ResponseResource.class);
		}
		
		// Task : Apply for PROW
				List<Task> taskList9P = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-apply-prow");
				if(!taskList9P.isEmpty()) {
					Integer taskId9 = taskList9P.get(0).getId();

					sleep(1000);

					HttpEntity<PRowRequest> requestEntity8 = new HttpEntity<>(objectCreator.createPRowRequest());
					ResponseEntity<ResponseResource> responseEntity8 = restTemplate.postForEntity("/v1/lm-implementation/task/apply-for-prow/" + taskId9, requestEntity8, ResponseResource.class);
				}

		// Task : Apply for ROW
		List<Task> taskList9 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-apply-row");
		if(!taskList9.isEmpty()) {
			Integer taskId9 = taskList9.get(0).getId();

			sleep(1000);

			HttpEntity<RowBeanRequest> requestEntity8 = new HttpEntity<>(objectCreator.createRowBeanRequest());
			ResponseEntity<ResponseResource> responseEntity8 = restTemplate.postForEntity("/v1/lm-implementation/task/apply-for-row-permission/" + taskId9, requestEntity8, ResponseResource.class);
		}

//		// Task : Prepare PO for Government Authority
//		List<Task> taskList10 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-prepare-po-govt");
//		if(!taskList10.isEmpty()) {
//			Integer taskId10 = taskList10.get(0).getId();
//
//			sleep(1000);
//
//			HttpEntity<PreparePoGovernmentAuthorityBean> requestEntity9 = new HttpEntity<>(objectCreator.createPreparePoGovernmentAuthorityBean());
//			ResponseEntity<ResponseResource> responseEntity9 = restTemplate.postForEntity("/v1/lm-implementation/task/row-govt-pos/" + taskId10, requestEntity9, ResponseResource.class);
//		}

		// Task : Pay to Government Authority
		List<Task> taskList11 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-pay-govt-auth");
		if(!taskList11.isEmpty()) {
			Integer taskId11 = taskList11.get(0).getId();

			sleep(1000);

			HttpEntity<PaymentBean> requestEntity10 = new HttpEntity<>(objectCreator.createPaymentBean());
//			@SuppressWarnings({"rawtypes", "unused"})
			ResponseEntity<ResponseResource> responseEntity10 = restTemplate.postForEntity("/v1/lm-implementation/task/row-govt-payments/" + taskId11, requestEntity10, ResponseResource.class);
		}

		
		//Task: Approval of Prow cost
		List<Task> taskList11P = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"approval-prow-cost");
		if(!taskList11P.isEmpty()) {
			Integer taskId11 = taskList11P.get(0).getId();

			sleep(1000);

			HttpEntity<ProwCostApproval> requestEntity10 = new HttpEntity<>(objectCreator.createProwCostApproval());
//			@SuppressWarnings({"rawtypes", "unused"})
			ResponseEntity<ResponseResource> responseEntity10 = restTemplate.postForEntity("/v1/lm-implementation/task/prow-cost-approval/" + taskId11, requestEntity10, ResponseResource.class);
		}	
		
		
		
		//Task: Building Authority Contract
				List<Task> taskList11B = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-building-authority-contract");
				if(!taskList11B.isEmpty()) {
					Integer taskId11 = taskList11B.get(0).getId();

					sleep(1000);

					HttpEntity<BuildingAuthorityContractRequest> requestEntity10 = new HttpEntity<>(objectCreator.createBuildingAuthorityContractRequest());
//					@SuppressWarnings({"rawtypes", "unused"})
					ResponseEntity<ResponseResource> responseEntity10 = restTemplate.postForEntity("/v1/lm-implementation/task/building-authority-contract/" + taskId11, requestEntity10, ResponseResource.class);
				}	
		
		//Task: Provide PO to Building Authority
				List<Task> taskList12B = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-po-building-authority");
				if(!taskList12B.isEmpty()) {
					Integer taskId11 = taskList12B.get(0).getId();

					sleep(1000);

					HttpEntity<ProvidePoBuildingAuthorityBean> requestEntity10 = new HttpEntity<>(objectCreator.createProvidePoBuildingAuthorityBean());
//					@SuppressWarnings({"rawtypes", "unused"})
					ResponseEntity<ResponseResource> responseEntity10 = restTemplate.postForEntity("/v1/lm-implementation/task/prow-contract-pos/" + taskId11, requestEntity10, ResponseResource.class);
				}	
				
				
				//Task: Pay Building Authority
				List<Task> taskList12PBA = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-building-authority-payment");
				if(!taskList12PBA.isEmpty()) {
					Integer taskId11 = taskList12PBA.get(0).getId();

					sleep(1000);

					HttpEntity<PaymentBean> requestEntity10 = new HttpEntity<>(objectCreator.createPaymentBean());
//					@SuppressWarnings({"rawtypes", "unused"})
					ResponseEntity<ResponseResource> responseEntity10 = restTemplate.postForEntity("/v1/lm-implementation/task/row-govt-payments/" + taskId11, requestEntity10, ResponseResource.class);
				}	
				
			
				
		// Task : Release MRN for OSP/IBD Work
		List<Task> taskList12 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"release-mrn-osp-work");
		if(!taskList12.isEmpty()) {
			Integer taskId12 = taskList12.get(0).getId();

			sleep(100);

			HttpEntity<MrnOspMuxRequest> requestEntity11 = new HttpEntity<MrnOspMuxRequest>(objectCreator.createMrnOspMuxRequest());
			ResponseEntity<ResponseResource> responseEntity11 = restTemplate.postForEntity("/v1/lm-implementation/task/mrn-osp-ibd-materials/" + taskId12, requestEntity11, ResponseResource.class);
		}


		
	

		// Task : Create and Release MRN for Mux
		List<Task> taskList17 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-create-mux-mrn");
		if(!taskList17.isEmpty()) {
			Integer taskId17 = taskList17.get(0).getId();

			sleep(100);

			HttpEntity<MrnOspMuxRequest> requestEntity16 = new HttpEntity<MrnOspMuxRequest>(objectCreator.createMrnOspMuxRequest());
			ResponseEntity<ResponseResource> responseEntity16 = restTemplate.postForEntity("/v1/lm-implementation/task/mux-mrns/" + taskId17, requestEntity16, ResponseResource.class);
		}

		

		// Task : Arrange Field Engineer for OSP/IBD Work
		List<Task> taskList13 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"arrange-field-engineer-osp");
		if(!taskList13.isEmpty()) {
			Integer taskId13 = taskList13.get(0).getId();

			sleep(100);

			HttpEntity<FieldEngineerDetailsBean> requestEntity12 = new HttpEntity<FieldEngineerDetailsBean>(objectCreator.createFieldEngineerDetailsBean());
			ResponseEntity<ResponseResource> responseEntity12 = restTemplate.postForEntity("/v1/lm-implementation/task/field-engineer-arrangements/" + taskId13, requestEntity12, ResponseResource.class);
		}
		
		// Task : Deliver Mux
				List<Task> taskList20 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "lm-deliver-mux");
				if(!taskList20.isEmpty()) {
					Integer taskId20 = taskList20.get(0).getId();

					sleep(100);

					HttpEntity<DeliverMuxBean> requestEntity19 = new HttpEntity<DeliverMuxBean>(objectCreator.createDeliverMuxBean());
					ResponseEntity<ResponseResource> responseEntity19 = restTemplate.postForEntity("/v1/lm-implementation/task/deliver-mux/" + taskId20, requestEntity19, ResponseResource.class);
				}

		// Task : Confirm Appointment ibd
		List<Task> taskList18C = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"customer-appointment-ibd");
		if(!taskList18C.isEmpty()) {
			Integer taskId18 = taskList18C.get(0).getId();

			sleep(100);

			HttpEntity<CustomerAppointmentBean> requestEntity17 = new HttpEntity<CustomerAppointmentBean>(objectCreator.createCustomerAppointmentBean());
			ResponseEntity<ResponseResource> responseEntity17 = restTemplate.postForEntity("/v1/lm-implementation/task/customer-appointments/" + taskId18, requestEntity17, ResponseResource.class);
		}
		
		// Task : Complete OSP Work
		List<Task> taskList14 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "lm-complete-osp-work");
		if(!taskList14.isEmpty()) {
			Integer taskId14 = taskList14.get(0).getId();

			sleep(100);

			HttpEntity<OSPBean> requestEntity13 = new HttpEntity<OSPBean>(objectCreator.createOSPBean());
			ResponseEntity<ResponseResource> responseEntity13 = restTemplate.postForEntity("/v1/lm-implementation/task/osp-works/" + taskId14, requestEntity13, ResponseResource.class);
		}	
		
		
		// Task : Complete IBD Work
				List<Task> taskList14I = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "lm-complete-ibd-work");
				if(!taskList14I.isEmpty()) {
					Integer taskId14 = taskList14I.get(0).getId();

					sleep(100);

					HttpEntity<IBDBean> requestEntity13 = new HttpEntity<IBDBean>(objectCreator.createIBDBean());
					ResponseEntity<ResponseResource> responseEntity13 = restTemplate.postForEntity("/v1/lm-implementation/task/ibd-works/" + taskId14, requestEntity13, ResponseResource.class);
				}	
				
				// Task : Complete OSP Acceptance Testing
				List<Task> taskList15 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"complete-osp-acceptance-testing");
				if(!taskList15.isEmpty()) {
					Integer taskId15 = taskList15.get(0).getId();

					sleep(100);

					HttpEntity<CompleteOspAcceptanceBean> requestEntity14 = new HttpEntity<CompleteOspAcceptanceBean>(objectCreator.createCompleteOSPAcceptanceBean());
					ResponseEntity<ResponseResource> responseEntity14 = restTemplate.postForEntity("/v1/lm-implementation/task/osp-acceptance/" + taskId15, requestEntity14, ResponseResource.class);
				}
				// Task : Create Inventory Record
				List<Task> taskList16 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"create-osp-inventory-record");
				if(!taskList16.isEmpty()) {
					Integer taskId16 = taskList16.get(0).getId();

					sleep(100);

					HttpEntity<CreateInventoryRecord> requestEntity15 = new HttpEntity<CreateInventoryRecord>(objectCreator.createOSPInventoryRecord());
					ResponseEntity<ResponseResource> responseEntity15 = restTemplate.postForEntity("/v1/lm-implementation/task/osp-inventory-record/" + taskId16, requestEntity15, ResponseResource.class);
				}
				
		// Task : Confirm Appointment for Mux installation
		List<Task> taskList18 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"customer-appointment-mux-installation");
		if(!taskList18.isEmpty()) {
			Integer taskId18 = taskList18.get(0).getId();

			sleep(100);

			HttpEntity<CustomerAppointmentBean> requestEntity17 = new HttpEntity<CustomerAppointmentBean>(objectCreator.createCustomerAppointmentBean());
			ResponseEntity<ResponseResource> responseEntity17 = restTemplate.postForEntity("/v1/lm-implementation/task/customer-appointments/" + taskId18, requestEntity17, ResponseResource.class);
		}

		// Task : Arrange Field Engineer for Mux Installation
		List<Task> taskList19 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"arrange-field-engineer-mux-installation");
		if(!taskList19.isEmpty()) {
			Integer taskId19 = taskList19.get(0).getId();

			sleep(100);

			HttpEntity<FieldEngineerDetailsBean> requestEntity18 = new HttpEntity<FieldEngineerDetailsBean>(objectCreator.createFieldEngineerDetailsBean());
			ResponseEntity<ResponseResource> responseEntity18 = restTemplate.postForEntity("/v1/lm-implementation/task/field-engineer-arrangements/" + taskId19, requestEntity18, ResponseResource.class);
		}

		
		// Task : Install Mux
		List<Task> taskList21 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"lm-install-mux");
		if(!taskList21.isEmpty()) {
			Integer taskId21 = taskList21.get(0).getId();

			sleep(100);

			HttpEntity<InstallMuxBean> requestEntity20 = new HttpEntity<InstallMuxBean>(objectCreator.createInstallMuxBean());
			@SuppressWarnings({"rawtypes", "unused"})
			ResponseEntity<ResponseResource> responseEntity20 = restTemplate.postForEntity("/v1/lm-implementation/task/install-mux/" + taskId21, requestEntity20, ResponseResource.class);
		}
		
		sleep(10000);

		//Prepare Planned Event Async
		List<Task> taskList21A = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId,"prepare-planned-event-async");
		if(!taskList21A.isEmpty()) {
			String taskProcessInstID = taskList21A.get(0).getWfProcessInstId();

		 Execution execution = runtimeService.createExecutionQuery().processInstanceId(taskProcessInstID).activityId("prepare-planned-event-async").singleResult();
         if (execution != null) {
//        log.info("WfProcessInstId={} executionId={}", taskProcessInstID, execution.getId());
             runtimeService.trigger(execution.getId());
         }
		}

		// Task : Raise Planned Event
		List<Task> taskList22 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "raise-planned-event");
		if(!taskList22.isEmpty()) {
			Integer taskId22 = taskList22.get(0).getId();

			sleep(100);

			HttpEntity<PlannedEventBean> requestEntity21 = new HttpEntity<PlannedEventBean>(objectCreator.createPlannedEventBean());
			ResponseEntity<ResponseResource> responseEntity21 = restTemplate.postForEntity("/v1/lm-implementation/task/planned-event/" + taskId22, requestEntity21, ResponseResource.class);
		}

		// Task : Integrate Mux
		List<Task> taskList23 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "lm-integrate-mux");
		if(!taskList23.isEmpty()) {
			Integer taskId23 = taskList23.get(0).getId();

			sleep(100);

			HttpEntity<IntegrateMuxBean> requestEntity22 = new HttpEntity<IntegrateMuxBean>(objectCreator.createIntegrateMuxBean());
			ResponseEntity<ResponseResource> responseEntity22 = restTemplate.postForEntity("/v1/lm-implementation/task/integrate-mux/" + taskId23, requestEntity22, ResponseResource.class);
		}

		// Task : Configure Mux
		List<Task> taskList24 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "lm-configure-mux");
		if(!taskList24.isEmpty()) {
			Integer taskId24 = taskList24.get(0).getId();

			sleep(100);

			HttpEntity<ConfigureMuxBean> requestEntity23 = new HttpEntity<ConfigureMuxBean>(objectCreator.createConfigureMuxBean());
			ResponseEntity<ResponseResource> responseEntity23 = restTemplate.postForEntity("/v1/lm-implementation/task/configure-mux/" + taskId24, requestEntity23, ResponseResource.class);
		}

		// Fast Connect Flow will be triggered after this
		List<Task> taskList25 = taskRepo.findByServiceIdAndMstTaskDef_key(serviceId, "select-mux");
		if(!taskList25.isEmpty()) {
			String taskProcessInstID = taskList25.get(0).getWfProcessInstId();
			Integer taskId25 = taskList25.get(0).getId();

			sleep(100);

			HttpEntity<MuxDetailBean> requestEntity24 = new HttpEntity<>(objectCreator.createMuxDetailBean());
//			Execution execution = runtimeService.createExecutionQuery().processInstanceId(taskProcessInstID).activityId("select-mux").singleResult();
//			 if (execution != null) {
//			runtimeService.setVariable(execution.getId(), "isMuxInfoAvailable", true);
//			runtimeService.setVariable(execution.getId(), "isMuxIPAvailable", true);
//			runtimeService.setVariable(execution.getId(), "isMuxInfoSyncCallSuccess", true);
//			runtimeService.trigger(execution.getId());}
			ResponseEntity<ResponseResource> responseEntity24 = restTemplate.postForEntity("/v1/lm-implementation/task/mux-details/" + taskId25, requestEntity24, ResponseResource.class);
		}


	}}
