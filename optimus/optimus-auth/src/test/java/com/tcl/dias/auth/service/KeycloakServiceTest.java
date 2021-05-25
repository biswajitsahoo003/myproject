package com.tcl.dias.auth.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.common.keycloack.bean.KeyCloackRoles;
import com.tcl.common.keycloack.bean.KeycloakUserResponseBean;
import com.tcl.common.keycloack.bean.RolesBean;
import com.tcl.dias.auth.utils.ObjectCreator;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class KeycloakServiceTest {

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
	KeycloakService keycloackService;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	RestClientService restClientService;
	
	
	
	@Test
	public void createRolesNegative() throws TclCommonException{
		
		RolesBean role = keycloackService.createRoles(null); 
		assertTrue(role == null);
	}
	
	@Test
	public void createUserPositive() throws TclCommonException{
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
				.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.postKeyCloak(Mockito.anyString(),Mockito.anyString(), Mockito.any()))
				.thenReturn(objectCreator.restResponseWithSuccess());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
				.thenReturn(objectCreator.getUserByUserNameKeycloak());
		Mockito.when(restClientService.putKeyCloak(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseWithSuccess());
		
		Boolean bool = keycloackService.createUser(objectCreator.getKeyCloakUserBean(), objectCreator.getRolesList(), "password");
		assertTrue(bool !=null);
	}  
	
	@Test
	public void createUserNegative() throws TclCommonException{ 
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
				.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.postKeyCloak(Mockito.anyString(),Mockito.anyString(), Mockito.any()))
				.thenReturn(objectCreator.restResponseWithSuccess());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
				.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		Boolean bool = keycloackService.createUser(objectCreator.getKeyCloakUserBean(), objectCreator.getRolesList(), "password");
		assertTrue(bool !=null);
	}
	
	@Test
	public void deleteUserNegative() throws TclCommonException{
		
		Boolean bool = keycloackService.deleteUser(null);
		assertTrue(bool == null);
	}

	@Test
	public void getRoleByNamePositive() throws TclCommonException{
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloak());
		RolesBean role = keycloackService.getRoleByName("String");
		assertTrue(role != null); 
	}
	
	@Test
	public void getRoleByNameNegative() throws TclCommonException{
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithDataNull());
		RolesBean role = keycloackService.getRoleByName("String");
		assertTrue(role != null);
	}
	
	@Test
	public void getAllRolesNegative() throws TclCommonException{
		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		KeyCloackRoles key = keycloackService.getAllRoles();
		assertTrue(key == null);
	}
	
	
	@Test
	public void deleteRoleForTheUserNegative() throws TclCommonException{
		
		String str = keycloackService.deleteRoleForTheUser(null,null);
		assertTrue(str == null);
	}

	@Test
	public void getKeycloakUserIdByUserNameNegative() throws TclCommonException{
		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		String key = keycloackService.getKeycloakUserIdByUserName("user");
		assertTrue(key == null);
		
	}
	
	@Test
	public void getRolesPresentToTheUserNegative() throws TclCommonException{
		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		List<RolesBean> list = keycloackService.getRolesPresentToTheUser("user");
		assertTrue(list == null);
		
	}
	
	@Test
	public void getRolesPresentToTheUserNegative2() throws TclCommonException{
		List<RolesBean> list = keycloackService.getRolesPresentToTheUser(null);
		assertTrue(list == null);
	}
	
	@Test
	public void addOrRemoveTheRolesNegative() throws TclCommonException{
		String str = keycloackService.addOrRemoveTheRoles(null,null);
		assertTrue(str == null);
	}
	
	@Test
	public void addOrRemoveTheRolesNegative2() throws TclCommonException{          
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		List<String> roles = new ArrayList<>();
		String str = keycloackService.addOrRemoveTheRoles("Str",roles);
		assertTrue(str == null);
	}
	
	@Test
	public void addOrRemoveTheRolesNegative3() throws TclCommonException{         
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloak());
		List<String> roles = new ArrayList<>();
		String str = keycloackService.addOrRemoveTheRoles("Str",roles);
		assertTrue(str == null);
	}
	
	
	@Test
	public void getAndConstructRolesBeanNegative() throws TclCommonException{		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		List<RolesBean> list = keycloackService.getAndConstructRolesBean(objectCreator.getRolesList());
		assertTrue(list == null);
	}
	
	@Test
	public void setPasswordForUserNegative() throws TclCommonException{
		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		Mockito.when(restClientService.putKeyCloak(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseWithoutSuccess());
		keycloackService.setPasswordForUser("password" , "uderId");
	}
	
	
	
	
	
	@Test
	public void deleteTheUser() throws TclCommonException{	//check
		keycloackService.deleteTheUser(null);
	}
	
	@Test
	public void impersonateKeycloakUserPositive() throws TclCommonException{		//check
		HttpHeaders http = keycloackService.impersonateKeycloakUser("String");
		assertTrue(http != null);
	}
	
	@Test
	public void getKeycloakUserBeanByUserNameNegative() throws TclCommonException{
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		
		KeycloakUserResponseBean bean = keycloackService.getKeycloakUserBeanByUserName("userName");
		assertTrue(bean == null);
	}
	
	
	@Test
	public void setRequiredActionToEnableOTPNegative() throws TclCommonException{
		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		Boolean bool = keycloackService.setRequiredActionToEnableOTP("userName");
		assertTrue(bool != null);
	}
	
	@Test
	public void disableOtpCredetialsForUserNegative() throws TclCommonException{
		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.getUserByUserNameKeycloakWithoutId());
		Boolean bool = keycloackService.disableOtpCredetialsForUser("userName");
		assertTrue(bool != null);
	}
	
	@Test
	public void disableOtpForTheUserNegative() throws TclCommonException{
		
		Mockito.when(restClientService.postWithoutHeader(Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.restResponseForGetToken());
		Mockito.when(restClientService.getCallForKeycloak(Mockito.anyString(), Mockito.any()))
		.thenReturn(null);
		Boolean bool = keycloackService.disableOtpForTheUser("userName");
		assertTrue(bool != null);
	}
	
	
	
	
	
	
	
	
}
