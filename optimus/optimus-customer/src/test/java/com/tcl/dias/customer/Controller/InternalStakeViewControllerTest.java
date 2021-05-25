package com.tcl.dias.customer.Controller;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.CustomerAttachmentBean;
import com.tcl.dias.common.beans.LegalEntityBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.customer.dto.CustomerDto;
import com.tcl.dias.customer.entity.repository.AttachmentRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeAttributeValueRepository;
import com.tcl.dias.customer.entity.repository.CustomerLegalEntityRepository;
import com.tcl.dias.customer.entity.repository.CustomerRepository;
import com.tcl.dias.customer.entity.repository.MstLeAttributeRepository;
import com.tcl.dias.customer.isv.controller.v1.InternalStakeViewController;
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
public class InternalStakeViewControllerTest {
	@Autowired
	CustomerService customerService;
	
	@Autowired
	InternalStakeViewController internalStakeViewController;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	MQUtils mqUtils;
	
	@MockBean
	AttachmentRepository attachmentRepository;
	
	@MockBean
	CustomerRepository customerRepository;
	
	@MockBean
	MstLeAttributeRepository mstLeAttributeRepository;
	
	@MockBean
	CustomerLegalEntityRepository customerLegalEntityRepository;
	
	@MockBean
	CustomerLeAttributeValueRepository customerLeAttributeValueRepository;
	
	
	
	@Before
	public void init() throws TclCommonException {

		Mockito.when(mstLeAttributeRepository.findByName(Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.craeteMstLeAttribute()));
		Mockito.when(customerLegalEntityRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.craeteCustomerLegalEntity()));
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLeIdAndMstLeAttributesIdAndProductName(
				Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(attachmentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.createAttachMent()));
		Mockito.when(attachmentRepository.save(Mockito.any())).thenReturn(objectCreator.createAttachMent());
		Mockito.when(customerLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createCustomerLeAttributeValue());
		Mockito.when(customerLegalEntityRepository.findByCustomerIdIn(Mockito.any())).thenReturn(objectCreator.createCustomerLegalEntity());
		
	}
	@Test
	public void testGetCustomerLegalEntityDetailsByCustomerId() throws TclCommonException {
		ResponseResource<List<LegalEntityBean>> response = internalStakeViewController
				.getCustomerLegalEntityDetailsByCustomerId(new ArrayList<>());
		assertTrue(response != null);

	}
	@Test
	public void testGetAllCustomer() throws TclCommonException {
		Mockito.when(customerRepository.findAll()).thenReturn(objectCreator.createCustomerList());
		ResponseResource<List<CustomerDto>> response = internalStakeViewController.getAllCustomer();
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testUpLoadLegalEntityFile() throws TclCommonException, IOException {
		MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", new byte[2]);
		ResponseResource<CustomerAttachmentBean> response = internalStakeViewController.upLoadLegalEntityFile(file, 2, "referenceName", "MSA", "GVPN");
		assertTrue(response!=null);
	}
	@Test
	public void testUpLoadLegalEntityFileApproch2() throws TclCommonException, IOException {
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLeIdAndMstLeAttributesIdAndProductName(
				Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(null);
		MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", new byte[2]);
		ResponseResource<CustomerAttachmentBean> response = internalStakeViewController.upLoadLegalEntityFile(file, 2,
				"referenceName", "MSA", "GVPN");
		assertTrue(response!=null);
	}
}
