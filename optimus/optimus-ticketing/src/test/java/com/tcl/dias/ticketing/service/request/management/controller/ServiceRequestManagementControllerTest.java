package com.tcl.dias.ticketing.service.request.management.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.ticketing.controller.v1.PlannedEventsController;
import com.tcl.dias.ticketing.controller.v1.TicketingController;
import com.tcl.dias.ticketing.creator.TicketingMockBeanCreator;
import com.tcl.dias.ticketing.service.category.controller.v1.ServiceCategoryController;
import com.tcl.dias.ticketing.service.request.management.controller.v1.ServiceRequestManagementController;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the TicketingControllerTest.java class. used for service and change request controller
 * 
 *
 * @author CHETAN CHAUDHARY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration

public class ServiceRequestManagementControllerTest {

	@Autowired
	ServiceRequestManagementController serviceRequestManagementController;

	@MockBean
	private RestClientService restClientService;

	@Autowired
	TicketingMockBeanCreator mockBeanCreator;

	@Autowired
	TicketingController ticketingController;

	@Autowired
	PlannedEventsController plannedEventsController;
	
	@MockBean
	private Utils Utils;

	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;
	
	/**
	 * init
	 */
	@Before
	public void init() {
		Mockito.when(restClientService.getWithQueryParam(Mockito.anyString(), Mockito.any(), Mockito.any()))
		.thenReturn(mockBeanCreator.getRestResponse());
	}
	
	@Test
	public void testServiceAndChangeRequestFilters() throws TclCommonException {
		ResponseResource<RestResponse> response = serviceRequestManagementController.filters("serviceType", "serviceType", "requestType", "state", "SR", "");
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
		
	}
	
	@Test
	public void testServiceAndChangeRequestFilters2() throws TclCommonException {
		ResponseResource<RestResponse> response = serviceRequestManagementController.filters("serviceType", "serviceType", "requestType", "state", "CR", "");
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
		
	}
	
	@Test
	public void testServiceAndChangeRequestFilters3() throws TclCommonException {
		ResponseResource<RestResponse> response = serviceRequestManagementController.filters("serviceType", "serviceType", "requestType", "state", "", "");
		assertTrue(response.getStatus()==Status.ERROR);
		
	}
	
	/**
	 * @author chetan chaudhary
	 * Negative test case for ServiceAndChangeRequestFilters
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @throws TclCommonException
	 */
	@Test
	public void testServiceAndChangeRequestFiltersForNull() throws TclCommonException {
		ResponseResource<RestResponse> response = serviceRequestManagementController.filters(null, "service", null, null, null, null);
		assertTrue(response.getData()==null);
		
	}
}
