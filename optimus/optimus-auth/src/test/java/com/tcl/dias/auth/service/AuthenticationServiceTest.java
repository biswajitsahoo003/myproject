package com.tcl.dias.auth.service;

import static org.junit.Assert.assertTrue;

import javax.validation.constraints.AssertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.auth.utils.ObjectCreator;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class AuthenticationServiceTest {

	@Value("${keycloak.auth-server-url}")
	String keycloakServerUrl;

	@Value("${keycloak.realm}")
	String realm;

	@Value("${info.keycloak_realm-url}")
	String realmUrl;

	@Value("${info.keycloak_granttype}")
	String grantType;

	@Value("${info.keycloak_user}")
	String userName;

	@Value("${info.keycloak_pwd}")
	String password;

	@Value("${info.keycloak_clientid}")
	String clientId;

	@Value("${info.keycloak.client.id}")
	String keycloackClientId;

	@Value("${info.keycloak.client.url}")
	String clentUrl;

	@Value("${info.keycloak_resetpassword}")
	String passwordResetUrl;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	RestClientService restClientService;
	
	
	@Test
	public void resetPasswordTestPOsitive() throws TclCommonException{
		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(),Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.putKeyCloak(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseWithSuccess());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloak());
		String str = authenticationService.resetPassword("Str1", "Str1", "Str1");
		assertTrue(str != null);
	}
	
	@Test
	public void resetPasswordTestNegative() throws TclCommonException{
		String str = authenticationService.resetPassword(null, null, null);
		assertTrue(str == null);
		
	}
	
	
	
	@Test
	public void getUserDetailByIdPositive() throws TclCommonException {
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloak());
		
		String str = authenticationService.getUserDetailById("String");
		assertTrue(str != null);
	}
	
	
	@Test
	public void getUserDetailByIdNegative() throws TclCommonException {
		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(null);
		String str = authenticationService.getUserDetailById(null);
		assertTrue(str == null);
		
	}
	
	
	
	
	
	
	
	
	
}
