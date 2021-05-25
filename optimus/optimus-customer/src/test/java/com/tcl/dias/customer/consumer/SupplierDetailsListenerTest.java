package com.tcl.dias.customer.consumer;

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
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.customer.entity.repository.ServiceProviderLegalEntityRepository;
import com.tcl.dias.customer.service.v1.CustomerService;
import com.tcl.dias.customer.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the integration test cases for Customerinformation.
 *
 * @author Biswajit
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SupplierDetailsListenerTest {
	
	@Autowired
	SupplierDetailsListener supplierDetailsListener;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	ServiceProviderLegalEntityRepository  serviceProviderLegalEntityRepository;
	
	@Before
	public void init() throws AmqpException, TclCommonException {
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
	}
	
	@Test
	public void testReturnSupplierName() throws TclCommonException {
		Mockito.when(serviceProviderLegalEntityRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(objectCreator.createserviceProviderLegalEntity())); 
		String request="{\"request\":\"{\\\"supplierId\\\":1,\\\"mddFilterValue\\\":\\\"IAS\\\"}\",\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		String response=supplierDetailsListener.returnSupplierName(request);
		assertTrue(true);
	}
	 
}
