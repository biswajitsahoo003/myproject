package com.tcl.dias.customer.Controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.customer.bean.ServiceResponse;
import com.tcl.dias.customer.dto.CustomerConatctInfoResponseDto;
import com.tcl.dias.customer.isv.controller.IsvCustomerController;
import com.tcl.dias.customer.service.v1.CustomerService;
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
public class IsvCustomerControllerTest {
	@MockBean
	CustomerService customerService;

	@Autowired
	IsvCustomerController isvCustomerController;

	@Test
	public void testGetCustomerLegalEntityDetailsByCustomerId() throws TclCommonException {
		Mockito.when(customerService.getContactInfoDetaisByCustLegalIdAndSPLegalId(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(new CustomerConatctInfoResponseDto());
		ResponseResource<CustomerConatctInfoResponseDto> response = isvCustomerController.getContactInfoDetailsById(2,
				3);
		assertTrue(response != null);

	}

	@Test
	public void testGetAttachments() throws TclCommonException, MalformedURLException {
		Mockito.when(customerService.getAttachments(Mockito.anyInt(), Mockito.anyInt()))
		.thenReturn(new ClassPathResource("org/springframework/core/io/Resource.class"));
		ResponseEntity<Resource> response = isvCustomerController.getAttachments(2,3);
		assertTrue(response != null );
	}

	@Test
	public void testUpLoadLegalEntityFile() throws TclCommonException, IOException {
		List<Integer> referenceIds = new ArrayList<>();
		referenceIds.add(1);
		MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", new byte[2]);
		Mockito.when(customerService.processUploadFiles(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.anyInt(),Mockito.anyString())).thenReturn(new ServiceResponse());
		ResponseResource<ServiceResponse> response = isvCustomerController.upLoadFile(3, file, 4, 5, referenceIds,
				"referenceName", "MSA","");
		assertEquals(Status.SUCCESS, response.getStatus());

	}
}
