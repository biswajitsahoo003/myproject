package com.tcl.dias.servicefulfillment.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

import java.io.IOException;
import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.beans.CapexBean;
import com.tcl.dias.servicefulfillment.beans.CompleteAcceptanceTestingBean;
import com.tcl.dias.servicefulfillment.beans.ConductSiteSurveyBean;
import com.tcl.dias.servicefulfillment.beans.IBDBean;
import com.tcl.dias.servicefulfillment.beans.InstallMuxBean;
import com.tcl.dias.servicefulfillment.beans.MrnForMuxBean;
import com.tcl.dias.servicefulfillment.beans.MrnOspMuxRequest;
import com.tcl.dias.servicefulfillment.beans.OSPBean;
import com.tcl.dias.servicefulfillment.beans.PaymentBean;
import com.tcl.dias.servicefulfillment.beans.PreparePoGovernmentAuthorityBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoBuildingAuthorityBean;
import com.tcl.dias.servicefulfillment.beans.RowBeanRequest;
import com.tcl.dias.servicefulfillment.beans.SiteReadinessDetailBean;
import com.tcl.dias.servicefulfillment.beans.VendorDetailsBean;
import com.tcl.dias.servicefulfillment.controller.v1.LMImplementationController;
import com.tcl.dias.servicefulfillment.entity.repository.AppointmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstVendorsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskDataRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VendorsRepository;
import com.tcl.dias.servicefulfillment.objectcreator.ObjectCreator;
import com.tcl.dias.servicefulfillmentutils.beans.CustomerAppointmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.MuxDetailBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Test Cases for LM Implementation Controller
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LMImplementationControllerTest {

	@Autowired
	LMImplementationController lmImplementationController;
	
	@MockBean
	org.flowable.engine.TaskService flowableTaskService;

	@Autowired
	private ObjectCreator objectCreator;
	
	@MockBean
	TaskDataRepository taskDataRepository;
	
	@MockBean
	FieldEngineerRepository fieldEngineerRepository;
	
	@MockBean
	TaskRepository taskRepository;
	
	@MockBean
	VendorsRepository vendorsRepository;
	
	@MockBean
	MstVendorsRepository mstVendorsRepository;
	
	@MockBean
	AppointmentRepository appointmentRepository;
	
	@Before
	public void init() {
		Mockito.when(taskRepository.save(Mockito.any())).thenReturn(objectCreator.createTask());
		Mockito.when(taskDataRepository.save(Mockito.any())).thenReturn(objectCreator.createTaskData());
		Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createTask()));
	}
	
	/**
	 * Positive test case for Site Readiness Confirmation
	 * 
	 * testSiteReadinessConfirmartion
	 * @throws TclCommonException
	 */
	@Test
	public void testSiteReadinessConfirmartion() throws TclCommonException {
		doNothing().when(flowableTaskService).complete(Mockito.anyString(), Mockito.any());
		ResponseResource<SiteReadinessDetailBean> response = lmImplementationController.siteReadinessConfirmation(objectCreator.createSiteReadinessDetailBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for save Mux Details
	 * 
	 * testSaveMuxDetails
	 * @throws TclCommonException
	 */
	
	@Test
	public void testSaveMuxDetails() throws TclCommonException {
		ResponseResource<MuxDetailBean> response = lmImplementationController.saveMuxDetails( objectCreator.createMuxDetailBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	

	
	/**Positive test case for conduct site survey task
	 * @throws TclCommonException
	 */
	@Test
	public void testSaveConductSiteSurveyDetails() throws TclCommonException {
		ResponseResource<ConductSiteSurveyBean> response = lmImplementationController.saveConductSiteSurveyDetails( objectCreator.createConductSiteSurveyBean());
		System.out.println(Utils.convertObjectToJson(response));
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for saving Vendor Details
	 * 
	 * testSaveVendorDetails
	 * @throws TclCommonException
	 */
	
	@Test
	public void testSaveVendorDetails() throws TclCommonException {
		Mockito.when(vendorsRepository.save(Mockito.any())).thenReturn(objectCreator.createVendor());
		Mockito.when(mstVendorsRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createMstVendor()));
		ResponseResource<VendorDetailsBean> response = lmImplementationController.saveVendorDetails( objectCreator.createVendorDetailsBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive Test cases for saving Field Engineer Details
	 * 
	 * testSaveFieldEngineerDetails
	 * @throws TclCommonException
	 */
	
	@Test
	public void testSaveFieldEngineerDetails() throws TclCommonException {
		Mockito.when(fieldEngineerRepository.save(Mockito.any())).thenReturn(objectCreator.createFieldEngineer());
		Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createTaskWithKey("arrange-field-engineer-ss")));
		ResponseResource<FieldEngineerDetailsBean> response = lmImplementationController.saveFieldEngineerDetails( objectCreator.createFieldEngineerDetailsBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for Scheduling Customer Appointment
	 * 
	 * testScheduleCustomerAppointment
	 * @throws TclCommonException
	 */
	
	@Test
	public void testScheduleCustomerAppointment() throws TclCommonException {
		Mockito.when(appointmentRepository.save(Mockito.any())).thenReturn(objectCreator.createAppointment());
		Mockito.when(taskRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createTaskWithKey("customer-appointment-ss")));
		ResponseResource<CustomerAppointmentBean> response = lmImplementationController.scheduleCustomerAppointment( objectCreator.createCustomerAppointmentBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for applying for ROW Permission
	 * 
	 * testApplyForRowPermission
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	
	@Test
	public void testApplyForRowPermission() throws TclCommonException {
		ResponseResource<RowBeanRequest> response = lmImplementationController.applyForRowPermission(objectCreator.createRowBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive Test case for Creating Mrn for Mux
	 * 
	 * testCreateMrnForMux
	 * @throws TclCommonException
	 */
	
	@Test
	public void testCreateMrnForMux() throws TclCommonException {
		ResponseResource<MrnForMuxBean> response = lmImplementationController.createMrnForMux( objectCreator.createMrnMuxBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive Test Case for Releasing Mrn For OSP IBD 
	 * 
	 * testReleaseMrnForOspIbdMaterial
	 * @throws TclCommonException
	 */
	
	@Test
	public void testReleaseMrnForOspIbdMaterial() throws TclCommonException {
		ResponseResource<MrnOspMuxRequest> response = lmImplementationController.releaseMrnForOspIbdMaterial(null);
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive Test Case for Install Mux
	 * 
	 * testInstallMux
	 * @throws TclCommonException
	 */
	
	@Test
	public void testInstallMux() throws TclCommonException {
		ResponseResource<InstallMuxBean> response = lmImplementationController.installMux( objectCreator.createInstallMuxBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive Test case for Providing PO to Building Authority
	 * 
	 * testProvidePoBuildingAuthority
	 * @throws TclCommonException
	 */
	
	/**
	 * Positive Test Case for Preparing PO for Government Authority
	 * 
	 * testPreparePoGovernmentAuthority
	 * @throws TclCommonException
	 */
	

	
	/**
	 * Positive test case for Approving Deviation in Capex
	 * 
	 * testApproveCapex
	 * @throws TclCommonException
	 */
	
	@Test
	public void testApproveCapex() throws TclCommonException {
		ResponseResource<CapexBean> response = lmImplementationController.approveCapex( objectCreator.createCapexBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for Completing OSP Work
	 * 
	 * testCompleteOSPWork
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	
	@Test
	public void testCompleteOSPWork() throws TclCommonException, IOException {
		ResponseResource<OSPBean> response = lmImplementationController.completeOSPWork( objectCreator.createOspBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for Completing IBD Work
	 * 
	 * testCompleteIBDWork
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	
	@Test
	public void testCompleteIBDWork() throws TclCommonException, IOException {
		ResponseResource<IBDBean> response = lmImplementationController.completeIBDWork( objectCreator.createIbdBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for Pay to Building Authority
	 * 
	 * testPayBuildingAuthority
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	
	@Test
	public void testPayBuildingAuthority() throws TclCommonException, IOException {
		ResponseResource<PaymentBean> response = lmImplementationController.payBuildingAuthority( null);
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for Building Authority Contact
	 * 
	 * testBuildingAuthorityContact
	 * @throws TclCommonException
	 */
	
/*	@Test
	public void testBuildingAuthorityContact() throws TclCommonException {
		//ResponseResource<RowBeanRequest> response = lmImplementationController.buildingAuthorityContract( objectCreator.createRowBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}*/
	
	/**
	 * Positive Test Case for Defining Scope Work 
	 * 
	 * testDefineScopeWorkProjectPlan
	 * @throws TclCommonException
	 */
	
	/*@Test
	public void testDefineScopeWorkProjectPlan() throws TclCommonException {
		List<DefineScopeWorkProjectPlanBeanList> response = (List<DefineScopeWorkProjectPlanBeanList>) lmImplementationController.defineScopeWorkProjectPlan( objectCreator.createDefineScopeWorkProjectPlanBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}*/
	
	/**
	 * Positive Test Case for Applying for PRow
	 * 
	 * testApplyForPRow
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	
/*	@Test
	public void testApplyForPRow() throws TclCommonException, IOException {
		ResponseResource<RowBeanRequest> response = lmImplementationController.applyForPRow(objectCreator.createRowBean(), 1);
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}*/
	
	/**
	 * Positive test case for Row Govt Payments
	 * 
	 * testRowGovtPayments
	 * @throws TclCommonException
	 * @throws IOException 
	 */
	
	@Test
	public void testRowGovtPayments() throws TclCommonException, IOException {
		ResponseResource<PaymentBean> response = lmImplementationController.rowGovtPayments(new PaymentBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	/**
	 * Positive test case for completing Acceptance Testing
	 * 
	 * testCompleteAcceptanceTesting
	 * @throws TclCommonException
	 */
	
	@Test
	public void testCompleteAcceptanceTesting() throws TclCommonException {
		ResponseResource<CompleteAcceptanceTestingBean> response = lmImplementationController.completeAcceptanceTesting( objectCreator.createCompleteAcceptanceTestingBean());
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
}
