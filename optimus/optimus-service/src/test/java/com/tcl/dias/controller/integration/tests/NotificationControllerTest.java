package com.tcl.dias.controller.integration.tests;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.service.beans.ServiceResponse;
import com.tcl.dias.service.controller.v1.NotificationController;
import com.tcl.dias.service.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class contains the test cases for Notification service
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotificationControllerTest {

	@Autowired
	NotificationController notificationController;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	MQUtils mqUtils;

	/**
	 * Positive test case for notification controller
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNotification() throws TclCommonException {
		MockMultipartFile mpf = new MockMultipartFile("foo", "foooo.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello World".getBytes());

		ResponseResource<ServiceResponse> response = notificationController.sendFiletoUser(mpf,
				"sekhar.eerthineni@tatacommunicatons.com");
		assertTrue(response.getData().getStatus().equals(Status.SUCCESS));
	}
	
	/**
	 * Negative  test case for notification controller
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNotificationWithinvalidEmail() throws TclCommonException {
		MockMultipartFile mpf = new MockMultipartFile("foo", "foooo.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello World".getBytes());

		ResponseResource<ServiceResponse> response = notificationController.sendFiletoUser(mpf,
				"sekhar");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	
	
	/**
	 * Negative  test case for notification controller
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNotificationWitEmptyFile() throws TclCommonException {
		MockMultipartFile mpf = new MockMultipartFile("foo", "foooo.txt", MediaType.TEXT_PLAIN_VALUE,
				"".getBytes());

		ResponseResource<ServiceResponse> response = notificationController.sendFiletoUser(mpf,
				"sekhar");
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	

	/**
	 * Negative  test case for notification controller
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNotificationWitNull() throws TclCommonException {


		ResponseResource<ServiceResponse> response = notificationController.sendFiletoUser(null,
				null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	/**
	 * Negative  test case for notification controller
	 * 
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testNotificationWitNullemailAndValidFile() throws TclCommonException {


		MockMultipartFile mpf = new MockMultipartFile("foo", "foooo.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello World".getBytes());

		ResponseResource<ServiceResponse> response = notificationController.sendFiletoUser(mpf,
				null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}
	

}
