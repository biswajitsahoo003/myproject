package com.tcl.dias.ticketing.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.response.beans.ServiceCategoryResponse;
import com.tcl.dias.response.beans.ServiceResponseBean;
import com.tcl.dias.ticketing.controller.v1.PlannedEventsController;
import com.tcl.dias.ticketing.controller.v1.TicketingController;
import com.tcl.dias.ticketing.creator.TicketingMockBeanCreator;
import com.tcl.dias.ticketing.response.CreateTicketResponse;
import com.tcl.dias.ticketing.response.TicketingResponse;
import com.tcl.dias.ticketing.service.category.controller.v1.ServiceCategoryController;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the TicketingControllerTest.java class. used for ticketing
 * test constroller
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class TicketingControllerTest {

	@Autowired
	ServiceCategoryController serviceCategoryController;

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
		mock(UserInformation.class);
		// mock(UserInfoUtils.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(mockBeanCreator.getUserInformation());
		// Mockito.when(authTokenDetailRepository.find(Mockito.any())).thenReturn(quoteObjectCreator.getUSerInfp());
		Mockito.when(authTokenDetailRepository.find(Mockito.any())).thenReturn(mockBeanCreator.getUSerInfp());
		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getRestResponse());

		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getRestResponse());
		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getRestResponse());
		Mockito.mock(Utils.getClass());

	}

	/**
	 * testGetServiceDetails
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetServiceDetails() throws TclCommonException {
		ResponseResource<ServiceResponseBean> resource = serviceCategoryController.getServiceDetails("56578782");

		assertTrue(resource != null);

	}

	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetCategories() throws TclCommonException {
		List<String> stringList = new ArrayList<>();
		stringList.add("values");
		ResponseResource<ServiceCategoryResponse> resource = serviceCategoryController.getCategories(stringList);
		assertTrue(resource != null);

	}

	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetTicketDetails() throws TclCommonException {
		ResponseResource<CreateTicketResponse> resource = ticketingController.getTicketDetails("12345");
		assertTrue(resource != null);

	}

	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetDetails() throws TclCommonException {
		ResponseResource<TicketingResponse> resource = ticketingController
				.getDetails(mockBeanCreator.getUpdateRequest());
		assertTrue(resource != null);

	}

	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateTicket() throws TclCommonException {
		ResponseResource<CreateTicketResponse> resource = ticketingController
				.updateTicket(mockBeanCreator.getUpdateTicketRequest(), "12345");
		assertTrue(resource != null);

	}
	
	/**
	 * @author chetchau
	 * @throws TclCommonException
	 */
	@Test
	public void testEscalateTicket() throws TclCommonException {
		ResponseResource<CreateTicketResponse> resource = ticketingController
				.updateTicket(mockBeanCreator.getEscaleTicketRequest(), "12345");
		assertTrue(resource != null);

	}
	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCreateTicket() throws TclCommonException {
		ResponseResource<List<CreateTicketResponse>> resource = ticketingController
				.createTicket(mockBeanCreator.createTicketRequest());
		assertTrue(resource != null);

	}

	/**
	 * testGetServiceDetails
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeGetServiceDetails() throws TclCommonException {

		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		ResponseResource<ServiceResponseBean> resource = serviceCategoryController.getServiceDetails("56578782");

		assertTrue(resource != null);

	}

	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeGetCategories() throws TclCommonException {

		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		List<String> stringList = new ArrayList<>();
		stringList.add("values");
		ResponseResource<ServiceCategoryResponse> resource = serviceCategoryController.getCategories(stringList);
		assertTrue(resource != null);

	}

	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeGetTicketDetails() throws TclCommonException {

		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		ResponseResource<CreateTicketResponse> resource = ticketingController.getTicketDetails("12345");

		assertTrue(resource != null);

	}

	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeGetDetails() throws TclCommonException {

		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		ResponseResource<TicketingResponse> resource = ticketingController
				.getDetails(mockBeanCreator.getUpdateRequest());
		assertTrue(resource != null);

	}

	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeUpdateTicket() throws TclCommonException {

		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		ResponseResource<CreateTicketResponse> resource = ticketingController
				.updateTicket(mockBeanCreator.getUpdateTicketRequest(), "12345");
		assertTrue(resource != null);

	}

	/**
	 * testGetCategories
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeCreateTicket() throws TclCommonException {

		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		Mockito.when(restClientService.postWithClientRepo(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		Mockito.when(restClientService.patch(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());
		ResponseResource<List<CreateTicketResponse>> resource = ticketingController
				.createTicket(mockBeanCreator.createTicketRequest());

		assertTrue(resource != null);

	}		
	
	/**
	 * Positive Test case for testing Filters
	 * @author chetchau
	 * @throws TclCommonException
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	
	@Test
	public void testTicketingControllerFilters() throws TclCommonException, JsonParseException, JsonMappingException, IOException {
		ResponseResource<RestResponse> response = ticketingController.filters("searchBy", "serviceType", "serviceType", "issueType", "impact", "state", "2018-01-01 00:00:00", "2019-02-15 00:00:00");
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	}
	
	

}
