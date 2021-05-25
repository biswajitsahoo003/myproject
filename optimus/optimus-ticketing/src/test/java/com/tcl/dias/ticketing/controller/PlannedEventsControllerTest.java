package com.tcl.dias.ticketing.controller;

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
import com.tcl.dias.response.beans.ServiceCategoryResponse;
import com.tcl.dias.response.beans.ServiceResponseBean;
import com.tcl.dias.ticketing.controller.v1.PlannedEventsController;
import com.tcl.dias.ticketing.controller.v1.TicketingController;
import com.tcl.dias.ticketing.creator.TicketingMockBeanCreator;
import com.tcl.dias.ticketing.response.PlannedEventsResponse;
import com.tcl.dias.ticketing.service.category.controller.v1.ServiceCategoryController;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the TicketingControllerTest.java class. used for ticketing
 * test constroller
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class PlannedEventsControllerTest {

	@MockBean
	private RestClientService restClientService;

	@Autowired
	TicketingMockBeanCreator mockBeanCreator;

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
	 * testGetTicketDetails
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetTicketDetails() throws TclCommonException {
		ResponseResource<PlannedEventsResponse> resource = plannedEventsController.getTicketDetails(Mockito.any());

		assertTrue(resource != null);

	}

	/**
	 * testNegativeGetTicketDetailss
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeGetTicketDetails() throws TclCommonException {

		Mockito.when(restClientService.getWithQueryParam(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(mockBeanCreator.getErrorRestResponse());

		ResponseResource<PlannedEventsResponse> resource = plannedEventsController.getTicketDetails(Mockito.any());
		assertTrue(resource != null);

	}
	/**
	 * @author chetchau
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @throws TclCommonException
	 */
	@Test
	public void testPlannedEventsControllerFilters() throws TclCommonException {
		ResponseResource<RestResponse> response = plannedEventsController.filters("searchBy", "serviceType", "serviceType");
		assertTrue(response!=null && response.getData()!=null && response.getStatus()==Status.SUCCESS);
	
	}
	
	/**
	 * @author chetchau
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativePlannedEventsControllerFilters() throws TclCommonException {
		ResponseResource<RestResponse> response = plannedEventsController.filters(null, null, "service");
		assertTrue(response.getData()==null);	
	}
	
}
