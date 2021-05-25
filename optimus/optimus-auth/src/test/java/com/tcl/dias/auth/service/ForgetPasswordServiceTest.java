package com.tcl.dias.auth.service;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.auth.beans.ForgotPasswordRequest;
import com.tcl.dias.auth.beans.ForgotPasswordResponse;
import com.tcl.dias.auth.beans.ResetPasswordRequest;
import com.tcl.dias.auth.redis.beans.ResetUserInfoBean;
import com.tcl.dias.auth.redis.service.ResetUserInfoService;
import com.tcl.dias.auth.utils.ObjectCreator;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class ForgetPasswordServiceTest {

	@Autowired
	ForgotPasswordService forgetPasswordService;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	ResetUserInfoService resetUserInfoService ;
	
	@Autowired
	AuthenticationService authenticationService;
	

//	@MockBean
//	AuthenticationService authenticationService;
	
	@MockBean
	RestClientService restClientService;
	
	@MockBean
	UserRepository userRepository;
	
	
	@Test
	public void processForgotPasswordPositive() throws TclCommonException{
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(),Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloak());
		Mockito.when(userRepository.findByEmailIdAndStatus(Mockito.anyString(),Mockito.anyInt()))
				.thenReturn(objectCreator.findByEmailIdAndStatus());
		
		ForgotPasswordResponse response = forgetPasswordService.processForgotPassword(objectCreator.password());
		assertTrue(response != null);
	}
	
	@Test
	public void processForgotPasswordNegative() throws TclCommonException{
		
		ForgotPasswordResponse response = forgetPasswordService.processForgotPassword(null);
		assertTrue(response == null); 
	}
	
	@Test
	public void processForgotPasswordNegative2() throws TclCommonException{
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(),Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloak());
		Mockito.when(userRepository.findByEmailIdAndStatus(Mockito.anyString(),Mockito.anyInt()))
				.thenReturn(null);
		
		ForgotPasswordResponse response = forgetPasswordService.processForgotPassword(objectCreator.password());
		assertTrue(response != null);
	}


	@Test
	public void processResetPasswordPositive() throws TclCommonException{ 
		
		Mockito.when(resetUserInfoService.find(Mockito.anyString()))
			.thenReturn(objectCreator.resetTokenUserInfoProcess());
		String str = forgetPasswordService.processResetPassword(objectCreator.resetPasswordRequest(), "String");
		assertTrue(str != null);
	}
	
	@Test
	public void processResetPasswordNegative1() throws TclCommonException{
		
		String str = forgetPasswordService.processResetPassword(objectCreator.resetPasswordRequest(), "String");
		assertTrue(str == null);
	}
	
	@Test
	public void processResetPasswordNegative2() throws TclCommonException{
		
		String str = forgetPasswordService.processResetPassword(new ResetPasswordRequest(), "String");
		assertTrue(str == null);
	}
	
	@Test
	public void processValidateResetTokenPOsitive() throws TclCommonException{
		
		Mockito.when(resetUserInfoService.find(Mockito.anyString()))
			.thenReturn(objectCreator.resetTokenUserInfoProcess());
		String str = forgetPasswordService.processValidateResetToken("resetToken");
		assertTrue(str != null);
		
	}
	
	@Test
	public void processValidateResetTokenNegative1() throws TclCommonException{
		Mockito.when(resetUserInfoService.find(Mockito.anyString()))
		.thenReturn(null);
		String str = forgetPasswordService.processValidateResetToken(null);
		assertTrue(str == null);
		
	}
	
	@Test
	public void processValidateResetTokenNegative2() throws TclCommonException{
		Mockito.when(resetUserInfoService.find(Mockito.anyString()))
		.thenReturn(new ResetUserInfoBean());
		String str = forgetPasswordService.processValidateResetToken(null);
		assertTrue(str == null);
		
	}
	

	
	
}
