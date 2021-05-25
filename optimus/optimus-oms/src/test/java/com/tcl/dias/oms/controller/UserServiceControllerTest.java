package com.tcl.dias.oms.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ContactResponse;
import com.tcl.dias.oms.beans.CustomerRequestBean;
import com.tcl.dias.oms.beans.UserDetails;
import com.tcl.dias.oms.beans.UserRequest;
import com.tcl.dias.oms.beans.UserSite;
import com.tcl.dias.oms.controller.v1.UserServiceController;
import com.tcl.dias.oms.entity.repository.CustomerRequestRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Test case class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceControllerTest {

	@Autowired
	UserServiceController userServiceController;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	UserRepository userRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;
	
	@MockBean
    OrderProductComponentRepository	orderProductComponentRepository;
	
	@MockBean
	CustomerRequestRepository customerRequestRepository;
	
	@MockBean
	MstProductComponentRepository mstProductComponentRepository;
	
	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;
	
	@MockBean
	ContactResponse contactResponse;
	
	@MockBean
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@MockBean
	MstOrderSiteStageRepository mstOrderSiteStageRepository;
	
	@Before
	public void init() {
		Mockito.when(userRepository.findByIdInAndStatus(Mockito.anyList(),Mockito.anyInt())).thenReturn(new ArrayList<>(objectCreator.getUserEntity()));
		mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
	
	}

	/**
	 * positive case testUserDetails1
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUserDetails1() throws TclCommonException {
		UserRequest userRequest = new UserRequest();
		List<Integer> users = new ArrayList<>();
		users.add(1);
		users.add(2);
		users.add(3);
		userRequest.setUserIds(users);
		ResponseResource<List<UserDetails>> response = userServiceController.getUserDetails(userRequest);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	/**
	 * negative case testUserDetails2
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUserDetailsForEmpty() throws TclCommonException {
		UserRequest userRequest = new UserRequest();
		List<Integer> users = new ArrayList<>();
		userRequest.setUserIds(users);
		ResponseResource<List<UserDetails>> response = userServiceController.getUserDetails(userRequest);
		assertTrue(response.getStatus() == Status.FAILURE || response.getData().isEmpty() || response == null);

	}

	/**
	 * positive case testUserDetailsByCutomerId
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUserDetailsByCustomerId() throws TclCommonException {
		Mockito.when(userRepository.findByCustomerIdAndStatus(Mockito.anyInt(),Mockito.anyInt())).thenReturn(new ArrayList<>());
		ResponseResource<List<UserDetails>> response = userServiceController.getUserDetailsByCustId(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * negative case testUserDetailsByCutomerId
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUserDetailsByCustomerIdException() throws TclCommonException {
		Mockito.when(userRepository.findByCustomerIdAndStatus(null,1)).thenReturn(null);
		ResponseResource<List<UserDetails>> response = userServiceController.getUserDetailsByCustId(null);
		assertTrue(response.getStatus() == Status.FAILURE || response.getData().isEmpty() || response == null);
	}

	/**
	 * negative case testUserDetailsByCutomerId
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUserDetailsByCustomerIdException2() throws TclCommonException {
		Mockito.when(userRepository.findByCustomerIdAndStatus(30000000,1)).thenReturn(new ArrayList<>());
		ResponseResource<List<UserDetails>> response = userServiceController.getUserDetailsByCustId(30000000);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * negative case testUserDetailsByCutomerId
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetUserSiteDetails() throws TclCommonException {
		Mockito.when(userRepository.findByCustomerIdAndStatus(Mockito.anyInt(),Mockito.anyInt())).thenReturn(new ArrayList<>());
		Mockito.when(orderToLeRepository
		.findByErfCusCustomerLegalEntityId(Mockito.anyInt())).thenReturn(objectCreator.getOrderToLesList());
		Mockito.when(orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
						Mockito.any())).thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(objectCreator.getMstProductComponentList());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(objectCreator.getProductAtrributeMaster());
		Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any())).thenReturn(objectCreator.createOrderProductsList());
		
		Mockito.when(mstOrderSiteStatusRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getMstOrderSiteStatus()));
		
		Mockito.when(mstOrderSiteStageRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getMstOrderSiteStage()));
		
		ResponseResource<List<UserSite>> response = userServiceController.getSiteInformation();
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * possitive case testCustomerRequest
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCustomerRequest() throws TclCommonException {
		//Mockito.when(customerRequestRepository.save(Mockito.any())).thenReturn(new CustomerRequest());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(objectCreator.getUser());
		CustomerRequestBean req=new CustomerRequestBean();
		req.setMessage("test message");
		req.setSubject("test");
		ResponseResource<String> response = userServiceController.persistCustomerRequset(req);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * possitive case testCustomerRequest
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCustomerRequestForNull() throws TclCommonException {
		//Mockito.when(customerRequestRepository.save(Mockito.any())).thenReturn(new CustomerRequest());
		ResponseResource<String> response = userServiceController.persistCustomerRequset(null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}
	
	@Test
	public void testGetContactDetailsForPilotTeam() throws TclCommonException{
		doNothing().when(contactResponse).setPhoneNo(Mockito.anyString());
		doNothing().when(contactResponse).setEmail(Mockito.anyString());
		doNothing().when(contactResponse).setName(Mockito.anyString());
		ResponseResource<ContactResponse> response = userServiceController.getContactDetailsForPilotTeam();
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		
	}
	
}
