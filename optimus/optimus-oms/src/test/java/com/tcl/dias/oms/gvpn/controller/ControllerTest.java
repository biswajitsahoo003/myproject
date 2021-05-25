package com.tcl.dias.oms.gvpn.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.oms.gvpn.controller.v1.GvpnOrderController;
import com.tcl.dias.oms.gvpn.controller.v1.GvpnQuoteController;
import com.tcl.dias.oms.gvpn.service.v1.GvpnOrderService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.pricing.bean.FeasibilityBean;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the GvpnQuotesControllerTest.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ControllerTest {

	@MockBean
	GvpnQuoteService gvpnQuoteService;
	
	@MockBean
	GvpnOrderService gvpnOrderService;
	
	@MockBean
	GvpnQuotePdfService gvpnQuotePdfService;
	@Autowired
	GvpnOrderController gvpnOrderController;

	@Autowired
	GvpnQuoteController gvpnQuoteController;
	
	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;
	
	@MockBean
	GvpnPricingFeasibilityService gvpnPricingFeasibilityService;
	
	@Autowired
	private ObjectCreator quoteObjectCreator;

	
	@Before
	public void init() throws TclCommonException {
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());
		Mockito.when(authTokenDetailRepository.find(Mockito.any()))
		.thenReturn(quoteObjectCreator.getUSerInfp());}

	
 

	@Test
	public void uploadCofPdfController() throws TclCommonException {
		HttpServletResponse  mockedRequest = Mockito.mock(HttpServletResponse.class);
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());


	    ResponseResource<String> string = gvpnQuoteController.uploadCofPdf(1, 1, mockedRequest,
	    		firstFile);
		assertTrue(string != null );
	}

	@Test
	public void generateQuotePdf() throws TclCommonException {
		HttpServletResponse  mockedRequest = Mockito.mock(HttpServletResponse.class);

		ResponseResource<String> string = gvpnQuoteController.generateQuotePdf(1,1,
				mockedRequest);

		assertTrue(string != null );
	}
	
	@Test
	public void generateCofPdf() throws TclCommonException {
		HttpServletResponse  mockedRequest = Mockito.mock(HttpServletResponse.class);


		ResponseResource<String> string = gvpnQuoteController.generateCofPdf(1,1,
				mockedRequest,true);

		assertTrue(string != null );
	}
	
	@Test
	public void triggerForFeasibilityBean() throws TclCommonException {

		ResponseResource<String> string = gvpnQuoteController.triggerForFeasibilityBean(new FeasibilityBean());

		assertTrue(string != null );
	}
	
	@Test
	public void getOrderDetailsExcel() throws TclCommonException, IOException {
		HttpServletResponse  mockedRequest = Mockito.mock(HttpServletResponse.class);

		ResponseEntity<String> string = gvpnOrderController.getOrderDetailsExcel(1,mockedRequest);

		assertTrue(string != null );
	}
	
	
	
	

}
