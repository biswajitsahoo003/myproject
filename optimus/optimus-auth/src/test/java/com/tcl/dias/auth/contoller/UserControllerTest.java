package com.tcl.dias.auth.contoller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.auth.beans.LoginResponse;
import com.tcl.dias.auth.controller.UserController;
import com.tcl.dias.auth.service.UserInformationAuthService;
import com.tcl.dias.auth.service.UserService;
import com.tcl.dias.auth.utils.ObjectCreator;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserAccessDetailsBean;
import com.tcl.dias.common.beans.UserManagementBean;
import com.tcl.dias.common.beans.UserProductsBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class UserControllerTest {
	
	@Autowired
	UserController userController;
	
	@MockBean
	private UserRepository userRepository;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@Autowired
	@Mock
	UserInformationAuthService userInformationAuthService;
	
	@Autowired
	@Mock
	UserService userService;
	
	@MockBean
	MQUtils mqUtils;
	
	@Test
	public void addUserInOmsPositiveTest() throws TclCommonException {
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(objectCreator.constructUser());
		ResponseResource<UserManagementBean> response = userController.addUsersInOms(objectCreator.constructBeanForUserAddOnlyInOms());
		assertTrue(response!=null && response.getData() != null);
	}
	
	
	//test
	@Test
	public void addUserInOmsNegativeTest() throws TclCommonException {
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(objectCreator.constructUser());
		ResponseResource<UserManagementBean> response = userController.addUsersInOms(objectCreator.constructBeanForUserAddOnlyInOmsWithoutEmail());
		assertTrue(response!=null && response.getData() == null);
	}
	
	@Test
	public void addUserInOmsNegativeTest2() throws TclCommonException {
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(objectCreator.constructUser());
		ResponseResource<UserManagementBean> response = userController.addUsersInOms(null);
		assertTrue(response!=null && response.getData() == null);
	}
	
	@Test
	public void addUserInOmsNegativeTest3() throws TclCommonException {
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(null);
		ResponseResource<UserManagementBean> response = userController.addUsersInOms(objectCreator.constructBeanForUserAddOnlyInOms());
		assertTrue(response!=null && response.getData() == null);
	}
	
	@Test
	public void getUserInfoPositive() throws TclCommonException {
		ResponseResource<LoginResponse> response = userController.getUserInformations(null);
		assertTrue(response!=null);
	}
	
	//test cases for accessdetails api
	@Test
	public void getUserInformationsTest() throws TclCommonException {
		Mockito.when(userRepository.save(Mockito.any())).thenReturn(objectCreator.constructUser());
		ResponseResource<UserAccessDetailsBean> response = userController.userAccessDetails();
		assertTrue(response!=null);
	}
	
	@Test
	public void getUserAccessDetailsTest() throws TclCommonException {
		mock(UserAccessDetailsBean.class);
		UserAccessDetailsBean response = userService.getUserAccessDetails();
		assertTrue(response!=null);
	}
	

}
