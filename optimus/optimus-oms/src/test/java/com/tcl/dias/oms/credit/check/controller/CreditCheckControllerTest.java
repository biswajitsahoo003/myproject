package com.tcl.dias.oms.credit.check.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.CreditCheckStatusResponse;
import com.tcl.dias.oms.credit.check.controller.v1.CreditCheckController;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


/**
 * This class contains
 * all the test cases for the CreditCheckControllerTest
 * 
 *
 * @author Kusuma Kumar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */ 

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class CreditCheckControllerTest {
	
	@Autowired
	private ObjectCreator objectCreator;

	@Autowired
	private CreditCheckController creditCheckController;
	
	@MockBean
	QuoteToLeRepository quoteToLeRepository;
	
	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	
	@MockBean
	MQUtils mqUtils;
	
	@Before
	public void init() {
		
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(objectCreator.getUserInformation());
		
	}
	
	/**
	 * Get credit check status from quoteTOLe - positive
	 * @throws TclCommonException
	 */
	@Test
	public void testcheckCreditCheckStatus() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(objectCreator.getQuoteToLe2()));
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe_Id(Mockito.anyInt())).thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(objectCreator.getCustomerDetails());
		ResponseResource<CreditCheckStatusResponse> response =creditCheckController.checkCreditCheckStatus(1,50);
		assertTrue(response!=null && response.getStatus() == Status.SUCCESS);
	}
	

}
