package com.tcl.dias.auth.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.auth.beans.CustomerLe;
import com.tcl.dias.auth.utils.ObjectCreator;
import com.tcl.dias.common.beans.UserProductsBean;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.redis.service.AuthTokenService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)


public class UserInformationAuthServiceTest {

	@Autowired
	UserInformationAuthService userInformationAuthService;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@MockBean
	MQUtils mqUtils;
	
	@MockBean
	 UserRepository userRepository;
	
	@MockBean
	CustomerRepository customerRepository;
	
	@MockBean
	AuthTokenService authTokenService;
	
	@Autowired
	@Mock
	EngagementRepository enagagementRepository;
	
	
	@Autowired
	AuthTokenDetailRepository authTokenDetailRepository;
	
	
	
	  @Before public void init() throws TclCommonException {
	  
	  Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.anyString(),Mockito.any())).
	  thenReturn("Test1");
	  
	  }
	 
	 
	
	@Test
	public void returnCustomerInformationsPositive() throws TclCommonException { 
		
		String str = userInformationAuthService.returnCustomerInformations(objectCreator.getIds());
		assertTrue(str != null);

		
	}
	
	@Test
	public void returnCustomerInformationNegative() throws TclCommonException{
//		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any(), Mockito.any()))
//				.thenThrow(new RuntimeException("TestException"));
		String str = userInformationAuthService.returnCustomerInformations(null);  
		assertTrue(str == null);
		
	}
	
	
	

	@Test
	public void extractEngagementDetailsTest() throws TclCommonException{
		
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication); 
		SecurityContextHolder.setContext(securityContext);
//		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//				.thenReturn(objectCreator.getUserInformation1());			 

		
//		UserInfoUtils userInfoutils = mock(UserInfoUtils.class);
//		Mockito.when(userInfoUtils.getCustomerDetails())
//		.thenReturn(objectCreator.getCustomerDetails());
//		
//		Mockito.when( authTokenDetailRepository.find(Mockito.any()))
//		.thenReturn(objectCreator.getUserInformation());
//		
		List<CustomerLe> list = userInformationAuthService.extractEngagementDetails();
		assertTrue(list != null);
		
	}


	
	
	@Test
	public void getUserInformationNegativeTest() throws TclCommonException {
		
//		Mockito.when(authTokenService.find(Mockito.anyString())).thenReturn(null);
		UserInformation user = userInformationAuthService.getUserInformation(null);
		assertTrue(user == null);
		
	}
	
	//test case for accessdetails api
	
	@Test
	public void getUserEngagementsTest() throws TclCommonException{
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any(), Mockito.any())).thenReturn(null);
		mock(UserProductsBean.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication); 
		SecurityContextHolder.setContext(securityContext);
		List<UserProductsBean> userProductListResponse = userInformationAuthService.getUserEngagements();
		assertTrue(userProductListResponse !=null);
		
	}
	
}
