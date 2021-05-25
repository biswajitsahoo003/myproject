package com.tcl.dias.customer.Controller;

import static org.junit.Assert.assertTrue;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.customer.controller.v1.CustomerController;
import com.tcl.dias.customer.service.v1.CustomerService;
import com.tcl.dias.customer.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerControllerTest {

	@MockBean
	CustomerService customerService;
	
	@Autowired
	CustomerController customerController;
	
	@MockBean
	UserInfoUtils userInfoUtils;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@Before
	public void init() throws AmqpException, TclCommonException {
	 Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerDetailList());
	 Mockito.when(userInfoUtils.getUserType()).thenReturn("sales");
	}

	@Test
	public void testUploadSSDocument() throws TclCommonException {
		MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", new byte[2]);
		Mockito.when(customerService.uploadSSDocument(Mockito.any(), Mockito.any()))
				.thenReturn("tata");
		ResponseResource<String> response = customerController.upLoadSSFile(file, "GVPN");
		assertTrue(response != null);

	}
	@Test
	public void testUploadMSADocument() throws TclCommonException {
		MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", new byte[2]);
		Mockito.when(customerService.uploadMSADocument(Mockito.any(), Mockito.any()))
				.thenReturn("tata");
		ResponseResource<String> response = customerController.upLoadMSAFile(file, "GVPN");
		assertTrue(response != null);

	}
}
