package com.tcl.dias.customer.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.ServiceScheduleBean;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.customer.bean.AttachmentBean;
import com.tcl.dias.customer.bean.BillingAddress;
import com.tcl.dias.customer.bean.CustomerLeContactDetailsBean;
import com.tcl.dias.customer.bean.LeStateGstBean;
import com.tcl.dias.customer.bean.ServiceResponse;
import com.tcl.dias.customer.controller.v1.CustomerController;
import com.tcl.dias.customer.dto.AttributesDto;
import com.tcl.dias.customer.dto.CustomerConatctInfoResponseDto;
import com.tcl.dias.customer.dto.CustomerDto;
import com.tcl.dias.customer.dto.CustomerLeBillingInfoDto;
import com.tcl.dias.customer.dto.CustomerLegalEntityDto;
import com.tcl.dias.customer.dto.CustomerLegalEntityProductResponseDto;
import com.tcl.dias.customer.dto.OmsDetailsBean;
import com.tcl.dias.customer.entity.entities.CustomerLeAttributeValue;
import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
import com.tcl.dias.customer.entity.entities.SpLeAttributeValue;
import com.tcl.dias.customer.entity.repository.AttachmentRepository;
import com.tcl.dias.customer.entity.repository.CurrencyMasterRepository;
import com.tcl.dias.customer.entity.repository.CustomerAttributeValueRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeAttributeValueRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeBillingInfoRepository;
import com.tcl.dias.customer.entity.repository.CustomerLeContactRepository;
import com.tcl.dias.customer.entity.repository.CustomerLegalEntityRepository;
import com.tcl.dias.customer.entity.repository.CustomerRepository;
import com.tcl.dias.customer.entity.repository.LeStateGstRepository;
import com.tcl.dias.customer.entity.repository.MstCountryRepository;
import com.tcl.dias.customer.entity.repository.MstCustomerSpAttributeRepository;
import com.tcl.dias.customer.entity.repository.MstLeAttributeRepository;
import com.tcl.dias.customer.entity.repository.ServiceProviderLegalEntityCountryRepository;
import com.tcl.dias.customer.entity.repository.ServiceProviderLegalEntityRepository;
import com.tcl.dias.customer.entity.repository.ServiceProviderRepository;
import com.tcl.dias.customer.entity.repository.SpLeAttributeValueRepository;
import com.tcl.dias.customer.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the integration test cases for Customerinformation.
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
public class CustomerIntegrationTest {

	@MockBean
	private CustomerLegalEntityRepository customerLegalEntityRepository;

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	private CustomerLeAttributeValueRepository customerLeAttributeValueRepository;

	@MockBean
	private MstLeAttributeRepository mstLeAttributeRepository;

	@MockBean
	private SpLeAttributeValueRepository spLeAttributeValueRepository;

	@MockBean
	private ServiceProviderLegalEntityRepository serviceProviderLegalEntityRepository;

	@Autowired
	private CustomerController customerController;

	@Autowired
	private ObjectCreator objectCreator;

	@MockBean
	private ServiceProviderRepository serviceProviderRepository;

	/*@MockBean
	CustomerLeAttributeValueRepository customerBillingRepository;*/

	@MockBean
	AttachmentRepository attachmentRepository;

	@MockBean
	ServiceProviderLegalEntityCountryRepository spleCountryRespository;
	
	@MockBean
	CustomerLeBillingInfoRepository customerLeBillingInfoRepository;
	
	@MockBean
	LeStateGstRepository leStateGstRepository;
	
	@MockBean
	MstCountryRepository mstCountryRepository;
	
	@MockBean
	MstCustomerSpAttributeRepository mstCustomerSpAttributeRepository;
	
	@MockBean
	CustomerAttributeValueRepository customerAttributeValueRepository;
	
	@MockBean
	MQUtils mqUtils;
	
	
	@MockBean
	CustomerLeContactRepository customerLeContactRepository;
	
	@MockBean
	CurrencyMasterRepository currencyMasterRepository;
	
	@MockBean
	UserInfoUtils userInfoUtils;

	

	private static final Integer INVALID_CLE_ID = 189;
	private static final Integer INVALID_SLE_ID = 190;
	
	@Before
	public void init() throws AmqpException, TclCommonException {
		
		Mockito.when(customerLeBillingInfoRepository.findByCustomerLegalEntity_IdAndIsactive(Mockito.anyInt(), Mockito.anyString()))
		.thenReturn(objectCreator.getCustomerLeBillingInfoList());
		
		Mockito.when(customerLeBillingInfoRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(objectCreator.getCustomerLeBillingInfo()));
		
		
		Mockito.when(leStateGstRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(objectCreator.getLeStateGst()));
		
		Mockito.when(leStateGstRepository.findByCustomerLegalEntity(Mockito.any()))
		.thenReturn(objectCreator.getLeStateGstList());
		
		Mockito.when(customerLegalEntityRepository.findByCustomerId(Mockito.any()))
		.thenReturn(objectCreator.createCustomerLegalEntity());

		Mockito.when(mstCountryRepository.findByName(Mockito.any()))
		.thenReturn(objectCreator.createMstCountryList());
		
		Mockito.when(attachmentRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.ofNullable(objectCreator.createAttachMent()));
		
		Mockito.when(customerLegalEntityRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(objectCreator.craeteCustomerLegalEntity()));
		
		Mockito.when(leStateGstRepository.findByCustomerLegalEntityAndState(Mockito.any(),Mockito.anyString()))
		.thenReturn(objectCreator.getLeStateGstList());
		
		Mockito.when(mstLeAttributeRepository.findByName(Mockito.anyString()))
		.thenReturn(Optional.of(objectCreator.craeteMstLeAttribute()));
		
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(Mockito.anyInt(),Mockito.anyInt()))
		.thenReturn(objectCreator.createcustomerLeAttributeValue());
		
		Mockito.when(attachmentRepository.save(Mockito.any()))
		.thenReturn(objectCreator.createAttachMent());
		
		Mockito.when(customerLeAttributeValueRepository.save(Mockito.any()))
		.thenReturn(new CustomerLeAttributeValue());
		
		Mockito.when(mstCustomerSpAttributeRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte()))
		.thenReturn(objectCreator.createMstCustomerSpAttribute());
		
		Mockito.when(customerRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(objectCreator.createCustomer()));
		
		Mockito.when(customerAttributeValueRepository.findByCustomerAndMstCustomerSpAttribute(Mockito.any(),Mockito.any()))
		.thenReturn(objectCreator.createCustomerAttributeValueList());
		
		Mockito.when(serviceProviderLegalEntityRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(objectCreator.createserviceProviderLegalEntity()));
		
		Mockito.when(mstCustomerSpAttributeRepository.findByNameInAndStatus(Mockito.any(),Mockito.anyByte()))
		.thenReturn(objectCreator.createMstCustomerSpAttributeList());
		
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity_Id(Mockito.anyInt()))
		.thenReturn(objectCreator.getBillingDetails());
		
		Mockito.doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		Mockito.when(customerLegalEntityRepository.saveAndFlush(Mockito.any())).
		thenReturn(objectCreator.createCustomerLeAttributeValue());
		Mockito.when(customerLegalEntityRepository.findAllById(Mockito.anyInt()))
		.thenReturn((objectCreator.createCustomlegalEntity()));
		Mockito.when(spLeAttributeValueRepository
				.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn((objectCreator.craeteSpLeAttributeValue()));
		Mockito.when(customerLeBillingInfoRepository.save(Mockito.any())).thenReturn(objectCreator.getCustomerLeBillingInfo());
		 Mockito.when(spleCountryRespository.findByMstCountryAndIsDefault(Mockito.any(),
				 Mockito.anyByte()))
				 .thenReturn(objectCreator.createServiceProviderLeCountry());
		 Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerDetailList());
		 Mockito.when(userInfoUtils.getUserType()).thenReturn("sales");
	}


	/**
	 * 
	 * Positive TestCase for getting contact Info details and Sp info Details
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testFindByCustomerLeId() throws TclCommonException {

		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity(Mockito.any()))
				.thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(spLeAttributeValueRepository.findByServiceProviderLegalEntity(Mockito.any()))
				.thenReturn((objectCreator.createsupplierLeAttributeValue()));

		ResponseResource<CustomerConatctInfoResponseDto> response = customerController.getContactInfoDetailsById(1, 1);

		assertTrue(response != null);
	}

	@Test
	public void testFindByCustomerLeIdForEmpty() throws TclCommonException {

		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity(Mockito.any()))
				.thenReturn(new ArrayList<CustomerLeAttributeValue>());
		Mockito.when(spLeAttributeValueRepository.findByServiceProviderLegalEntity(Mockito.any()))
				.thenReturn((objectCreator.createsupplierLeAttributeValue()));

		ResponseResource<CustomerConatctInfoResponseDto> response = customerController.getContactInfoDetailsById(1, 1);

		assertTrue(response.getMessage() == null || response == null || response.getData() == null);
	}

	@Test
	public void testFindByCustomerLeIdForSpEmpty() throws TclCommonException {

		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity(Mockito.any()))
				.thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(spLeAttributeValueRepository
				.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn((new SpLeAttributeValue()));
		Mockito.when(spLeAttributeValueRepository.findByServiceProviderLegalEntity(Mockito.any()))
				.thenReturn((new ArrayList<SpLeAttributeValue>()));

		ResponseResource<CustomerConatctInfoResponseDto> response = customerController.getContactInfoDetailsById(1, 1);

		assertTrue(response.getData() == null);
	}

	@Test
	public void testFindByCustomerLeIdForisPresent() throws TclCommonException {

		Mockito.when(customerLegalEntityRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity(Mockito.any()))
				.thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(spLeAttributeValueRepository.findByServiceProviderLegalEntity(Mockito.any()))
				.thenReturn((new ArrayList<SpLeAttributeValue>()));

		ResponseResource<CustomerConatctInfoResponseDto> response = customerController.getContactInfoDetailsById(1, 1);

		assertTrue(response.getData() == null);
	}

	@Test
	public void testFindByCustomerLeIdForisPresentCustomer() throws TclCommonException {

		Mockito.when(customerRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity(Mockito.any()))
				.thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(spLeAttributeValueRepository
				.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn((new SpLeAttributeValue()));
		Mockito.when(spLeAttributeValueRepository.findByServiceProviderLegalEntity(Mockito.any()))
				.thenReturn((new ArrayList<SpLeAttributeValue>()));

		ResponseResource<CustomerConatctInfoResponseDto> response = customerController.getContactInfoDetailsById(1, 1);

		assertTrue(response.getData() == null);
	}

	@Test
	public void testFindByCustomerLeIdForisPresentServiceProvider() throws TclCommonException {

		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity(Mockito.any()))
				.thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(serviceProviderLegalEntityRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(spLeAttributeValueRepository.findByServiceProviderLegalEntity(Mockito.any()))
				.thenReturn((new ArrayList<SpLeAttributeValue>()));

		ResponseResource<CustomerConatctInfoResponseDto> response = customerController.getContactInfoDetailsById(1, 1);

		assertTrue(response.getData() == null);
	}

	/**
	 * 
	 * 
	 * Negative TestCase with Null Value for getting contact Info details and Sp
	 * info Details
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testFindByCustomerLeIdForNullAndValid() throws TclCommonException {
		ResponseResource<CustomerConatctInfoResponseDto> response = customerController.getContactInfoDetailsById(null,
				1);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);

	}

	/**
	 * 
	 * 
	 * Negative TestCase with Null Value for getting contact Info details and Sp
	 * info Details
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testFindByCustomerLeIdForValidAndNull() throws TclCommonException {
		ResponseResource<CustomerConatctInfoResponseDto> response = customerController.getContactInfoDetailsById(1,
				null);
		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);

	}

	/**
	 * 
	 * Negative TestCase with InvalidValue Value for getting contact Info details
	 * and Sp info Details
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testFindByCustomerLeIdForInValid() throws TclCommonException {
		Mockito.when(customerLegalEntityRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(customerRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(serviceProviderLegalEntityRepository.findById(Mockito.anyInt())).thenReturn(null);

		ResponseResource<CustomerConatctInfoResponseDto> response = customerController
				.getContactInfoDetailsById(INVALID_CLE_ID, INVALID_CLE_ID);

		assertTrue(response == null || response.getData() == null || response.getResponseCode() != 200);
	}

	/*
	 * test cases for possitive scenario fetch for CustomerLegalEntity info
	 */

	@Test
	public void testGetCustomerLegalEntityDetailsByCustomerId() throws Exception {
		ResponseResource<List<CustomerLegalEntityDto>> response = customerController
				.getCustomerLegalEntityDetailsByCustomerId(2,null);
		assertTrue(response != null);

	}

	/*
	 * test cases for negative scenario fetch for CustomerLegalEntity info
	 */

	@Test
	public void testGetCustomerLegalEntityDetailsByCustomerIdForException() throws Exception {
		Mockito.when(customerLegalEntityRepository.findByCustomerId(Mockito.anyInt()))
				.thenReturn(new ArrayList<CustomerLegalEntity>());
		ResponseResource<List<CustomerLegalEntityDto>> response = customerController
				.getCustomerLegalEntityDetailsByCustomerId(null,null);
		assertTrue(response.getMessage() == null || response == null || response.getData() == null);
	}

	/*
	 * test cases for testGetCustomerLegalEntityDetailsByCustomerIdForEmpty
	 */
	@Test
	public void testGetCustomerLegalEntityDetailsByCustomerIdForEmpty() throws Exception {
		Mockito.when(customerLegalEntityRepository.findByCustomerId(Mockito.anyInt()))
				.thenReturn(new ArrayList<CustomerLegalEntity>());
		ResponseResource<List<CustomerLegalEntityDto>> response = customerController
				.getCustomerLegalEntityDetailsByCustomerId(2,null);
	}

	/*
	 * test cases for testGetEmailIdAndTriggerEmail
	 */
	@Test
	public void testGetEmailIdAndTriggerEmail() throws Exception {
		Mockito.when(serviceProviderRepository.findByName(Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.craeteServiceLeAttribute()));
		Mockito.when(spLeAttributeValueRepository.findByServiceProviderLegalEntityAndMstLeAttribute(Mockito.any(),
				Mockito.any())).thenReturn(Optional.of(objectCreator.craeteSPLAttrLeAttribute()));
		//ResponseResource<TriggerEmailResponse> response = customerController.getCustomerEmailIdTriggerEmail();
		//assertTrue(response.getMessage() != null || response != null);
	}

	/*
	 * test cases for testGetEmailIdAndTriggerEmailForException
	 */
	@Test
	public void testGetEmailIdAndTriggerEmailForException() throws Exception {
		Mockito.when(mstLeAttributeRepository.findByName(Mockito.anyString())).thenReturn(null);
		Mockito.when(serviceProviderRepository.findByName(Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.craeteServiceLeAttribute()));
		Mockito.when(spLeAttributeValueRepository.findByServiceProviderLegalEntityAndMstLeAttribute(Mockito.any(),
				Mockito.any())).thenReturn(Optional.of(objectCreator.craeteSPLAttrLeAttribute()));
		//ResponseResource<TriggerEmailResponse> response = customerController.getCustomerEmailIdTriggerEmail();
		//assertTrue(response.getMessage() == null || response.getData() == null);
	}

	/*
	 * test cases for positive scenario fetch for billing details
	 */
	@Test
	public void testgetBillingDetailsById() throws TclCommonException {

		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity_Id(Mockito.anyInt()))
				.thenReturn(objectCreator.getBillingDetails());
		ResponseResource<List<AttributesDto>> response = customerController.getBillingDetailsById(1,"GSIP");
		assertTrue(response != null);
	}

	/*
	 * test cases for negative scenario fetch for billing details
	 */
	@Test
	public void testgetBillingDetailsByIdForNUll() throws Exception {

		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity_Id(Mockito.anyInt()))
				.thenReturn(objectCreator.getBillingDetails());
		ResponseResource<List<AttributesDto>> response = customerController.getBillingDetailsById(null,"GSIP");
		assertTrue(response == null || response.getResponseCode() != 200 || response.getData() != null);
	}

	/*
	 * test cases for negative scenario fetch for billing details
	 */
	@Test
	public void testgetBillingDetailsByIdForEmptyList() throws Exception {

		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity_Id(Mockito.anyInt()))
				.thenReturn(new ArrayList<CustomerLeAttributeValue>());
		ResponseResource<List<AttributesDto>> response = customerController.getBillingDetailsById(1,"GSIP");
		assertTrue(response.getData() == null);

	}

	/*
	 * Positive test case for fetching MSADocument
	 */

	public void testMSADocumentFetch() throws TclCommonException {
		Mockito.when(attachmentRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<List<AttachmentBean>> response = customerController.getMSADocuments(112, null);
		assertTrue(response.getData() != null);
	}

	/*
	 * negative test case for fetching MSADocument
	 */
	@Test
	public void testMSADocumentFetchException() throws TclCommonException {
		Mockito.when(attachmentRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<List<AttachmentBean>> response = customerController.getMSADocuments(null, null);
		assertTrue(response.getData() == null);
	}

	/**
	 * positive test case passing both values
	 * 
	 * testgetCustomerLegalEntityDetailsById
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetCustomerLegalEntityDetailsById() throws TclCommonException {

		System.out.println("info========");
		ResponseResource<CustomerLegalEntityProductResponseDto> response = customerController
				.getCustomerLegalEntityDetailsById(1, "IAS",null);
		assertTrue(response.getData() != null);

	}

	/**
	 * positive test case passing both values
	 * 
	 * testgetCustomerLegalEntityDetailsById
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetCustomerLegalEntityDetailsByIdPositive2() throws TclCommonException {

		Mockito.when(customerLegalEntityRepository.findAllById(Mockito.anyInt()))
				.thenReturn((objectCreator.createCustomlegalEntity2()));

		System.out.println("info========");
		ResponseResource<CustomerLegalEntityProductResponseDto> response = customerController
				.getCustomerLegalEntityDetailsById(1, "IAS",null);
		assertTrue(response.getData() != null && response.getResponseCode() == 200);

	}

	/**
	 * negative test case passing customer legal id as null
	 * 
	 * testgetCustomerLegalEntityDetailsByIdAsIdNull
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetCustomerLegalEntityDetailsByIdAsIdNull() throws TclCommonException {

		Integer id = null;
		Mockito.when(customerLegalEntityRepository.findAllById(id)).thenReturn(null);

		ResponseResource<CustomerLegalEntityProductResponseDto> response = customerController
				.getCustomerLegalEntityDetailsById(null, "IAS",null);
		assertTrue(response != null && response.getResponseCode() == 400);

	}

	/**
	 * negative test case passing product name as null
	 * 
	 * testgetCustomerLegalEntityDetailsByIdAsProductNameNull
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetCustomerLegalEntityDetailsByIdAsProductNameNull() throws TclCommonException {

		Integer id = null;
		Mockito.when(customerLegalEntityRepository.findAllById(id)).thenReturn(null);

		ResponseResource<CustomerLegalEntityProductResponseDto> response = customerController
				.getCustomerLegalEntityDetailsById(112, null,null);
		assertTrue(response.getData() == null);

	}

	/**
	 * negative test case passing both values and passing null for spleCountry
	 * 
	 * testgetCustomerLegalEntityDetailsById
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetCustomerLegalEntityDetailsByIdsplecountryNull() throws TclCommonException {

		Mockito.when(spleCountryRespository.findByMstCountryAndIsDefault(Mockito.any(), Mockito.anyByte()))
				.thenReturn(null);

		ResponseResource<CustomerLegalEntityProductResponseDto> response = customerController
				.getCustomerLegalEntityDetailsById(1, "IAS",null);
		//assertTrue(response != null && response.getResponseCode() == 500);
		assertTrue(response.getData() == null);

	}

	/**
	 * negative test case passing product name as null
	 * 
	 * testgetCustomerLegalEntityDetailsByIdAsProductNameNull
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetMSADocuments() throws TclCommonException {
		Mockito.when(customerLeAttributeValueRepository
		.findByCustomerLeIdAndMstLeAttributesIdAndProductName(Mockito.anyInt(), Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity(Mockito.any()))
				.thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(attachmentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.createAttachMent()));

		ResponseResource<List<AttachmentBean>> response = customerController.getMSADocuments(1, "IAS");
		assertTrue(response.getData() != null);

	}

	/**
	 * 
	 * testCustomerLegalEntityDetailsById
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCustomerLegalEntityDetailsById() throws TclCommonException {
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity(Mockito.any()))
				.thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(attachmentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.createAttachMent()));

		ResponseResource<List<CustomerLegalEntityDto>> response = customerController
				.getCustomerLegalEntityDetailsByCustomerId(2,null);
		assertTrue(response.getData() != null);

	}

	/**
	 * testCustomerLegalEntitiesAllDetailsById - negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCustomerLegalEntitiesAllDetailsByIdNegative() throws TclCommonException {
		List<Integer> inputList = new ArrayList<Integer>();
		inputList.add(1);
		Mockito.when(customerLegalEntityRepository.findByCustomerId(Mockito.anyInt()))
				.thenReturn(new ArrayList<CustomerLegalEntity>());
		Mockito.when(attachmentRepository.findByIdIn(inputList)).thenReturn(objectCreator.createAttachmentList());

		ResponseResource<List<CustomerDto>> response = customerController.getCustomerLegalEntityAllDetails();
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * testCustomerLegalEntitiesAllDetailsById
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCustomerLegalEntitiesAllDetailsById() throws TclCommonException {
		List<Integer> inputList = new ArrayList<Integer>();
		inputList.add(1);
		Mockito.when(customerLegalEntityRepository.findByCustomerId(Mockito.anyInt()))
				.thenReturn(objectCreator.createCustomerLegalEntityWithAttributes());
		Mockito.when(attachmentRepository.findByIdIn(inputList)).thenReturn(objectCreator.createAttachmentList());

		ResponseResource<List<CustomerDto>> response = customerController.getCustomerLegalEntityAllDetails();
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * testCustomerLegalEntitiesAllDetailsById
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCustomerLegalEntitiesAllDetailsByIdWithoutAttachment() throws TclCommonException {
		List<Integer> inputList = new ArrayList<Integer>();
		inputList.add(1);
		Mockito.when(customerLegalEntityRepository.findByCustomerId(Mockito.anyInt()))
				.thenReturn(objectCreator.createCustomerLegalEntityWOAttachments());
		Mockito.when(attachmentRepository.findByIdIn(inputList)).thenReturn(new ArrayList());

		ResponseResource<List<CustomerDto>> response = customerController.getCustomerLegalEntityAllDetails();
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * testCustomerLegalEntitiesAllDetailsNull - null case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCustomerLegalEntitiesAllDetailsNull() throws TclCommonException {
		List<Integer> inputList = new ArrayList<Integer>();
		inputList.add(1);
		Mockito.when(customerLegalEntityRepository.findByCustomerId(Mockito.anyInt()))
				.thenReturn(null);
		Mockito.when(attachmentRepository.findByIdIn(inputList)).thenReturn(objectCreator.createAttachmentList());

		ResponseResource<List<CustomerDto>> response = customerController.getCustomerLegalEntityAllDetails();
		assertTrue(response.getData() == null);
	}

	/**
	 * Finding Customer Legal entity details by legal entity id
	 * 
	 * @throws TclCommonException
	 * 
	 */

	/*
	 * Positive test case
	 */

	@Test
	public void testCustomerLegalEntityByLegalEntityIdPositive() throws TclCommonException {
		Set<Integer> inputList = new HashSet<Integer>();
		inputList.add(2);
		Mockito.when(customerLegalEntityRepository.findAllByIdIn(new ArrayList<>(inputList)))
				.thenReturn(objectCreator.returnCustomerLegalEntity());
		ResponseResource<List<CustomerLegalEntityDto>> response = customerController
				.getCustomerLegalEntityDetailsByCustomerLeId(inputList);
		assertTrue(response.getStatus() == Status.SUCCESS);
	}

	/*
	 * Negative case- Passing null value for legal entity
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCustomerLegalEntityByLegalEntityIdWithNullInputList() throws TclCommonException {
		ResponseResource<List<CustomerLegalEntityDto>> response = customerController
				.getCustomerLegalEntityDetailsByCustomerLeId(null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/*
	 * Negative case- Passing insufficient data in Entity
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testCustomerLegalEntityByLegalEntityIdCatch() throws TclCommonException {
		Set<Integer> inputList = new HashSet<Integer>();
		inputList.add(1);
		Mockito.when(customerLegalEntityRepository.findAllByIdIn(new ArrayList<>(inputList)))
				.thenReturn(objectCreator.returnCustomerLegalEntityInsufficient());
		ResponseResource<List<CustomerLegalEntityDto>> response = customerController
				.getCustomerLegalEntityDetailsByCustomerLeId(inputList);
		assertTrue(response.getData() == null);
	}
	
	/*
	 * possitive case- testgetBillingContact
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetBillingContact() throws TclCommonException {
		
		ResponseResource<List<BillingContact>> response = customerController.getBillingContacts(1);
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	/*
	 * negative case- testgetBillingContact
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetBillingContactNullId() throws TclCommonException {
		
		ResponseResource<List<BillingContact>> response = customerController.getBillingContacts(null);
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * possitive case- testgetBillingContact
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetBillingContactsById() throws TclCommonException {
		
		ResponseResource<BillingContact> response = customerController.getBillingContactsById(1,1);
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	/*
	 * negative case- testgetBillingContact
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetBillingContactsByIdNullId() throws TclCommonException {
		
		ResponseResource<BillingContact> response = customerController.getBillingContactsById(1,null);
		
		assertTrue(response.getData() == null);
	}
	/*
	 * negative case- testgetBillingContact
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetBillingContactsByIdNullId1() throws TclCommonException {
		
		ResponseResource<BillingContact> response = customerController.getBillingContactsById(null,1);
		
		assertTrue(response.getData() != null);
	}
	/*
	 * negative case- testgetBillingContact
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetBillingContactsByIdNullIdd() throws TclCommonException {
		
		ResponseResource<BillingContact> response = customerController.getBillingContactsById(null,null);
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * possitive case- testgetBillingContact
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetGstDetails() throws TclCommonException {
		
		ResponseResource<BillingAddress> response = customerController.getGstDetails(1,1);
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	/*
	 * negative case- testgetBillingContact
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetGstDetailsNullId() throws TclCommonException {
		
		ResponseResource<BillingAddress> response = customerController.getGstDetails(1,null);
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * possitive case- testgetCustomerAttchmentDeatilsById
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetCustomerAttchmentDeatilsById() throws TclCommonException {
		
		ResponseResource<AttachmentBean> response = customerController.getCustomerAttchmentDeatilsById(1,1);
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	/*
	 * negative case- testgetCustomerAttchmentDeatilsByIdNullId
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetCustomerAttchmentDeatilsByIdNullId() throws TclCommonException {
		
		ResponseResource<AttachmentBean> response = customerController.getCustomerAttchmentDeatilsById(1,null);
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * possitive case- testgetCustomerAttchmentDeatilsById
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetBillingDetailsForOms() throws TclCommonException {
		
		ResponseResource<OmsDetailsBean> response = customerController.getBillingDetailsForOms(1);
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	
	/*
	 * possitive case- testgetLeStateGst
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLeStateGst() throws TclCommonException {
		
		ResponseResource<List<LeStateGstBean>> response = customerController.getLeStateGst(1,"1");
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	/*
	 * negative case- testgetLeStateGstIdNullId
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetLeStateGstIdNullId() throws TclCommonException {
		
		ResponseResource<List<LeStateGstBean>> response = customerController.getLeStateGst(null,"1");
		
		assertTrue(response.getData() != null);
	}
	
	/*
	 * negative case- testgetLeStateGstIdNullId
	 * 
	 * @throws TclCommonException
	 */
	/*@Test
	public void testgetLeStateGstNullId() throws TclCommonException {
		
		ResponseResource<List<LeStateGstBean>> response = customerController.getLeStateGst(1,null);
		
		assertTrue(response.getData() == null);
	}*/
	
	/*
	 * negative case- testgetLeStateGstIdNullId
	 * 
	 * @throws TclCommonException
	 */
	/*@Test
	public void testgetLeStateGstIdNull() throws TclCommonException {
		
		ResponseResource<List<LeStateGstBean>> response = customerController.getLeStateGst(null,null);
		
		assertTrue(response.getData() == null);
	}*/
	
	/*
	 * possitive case- testgetLeStateGst
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testMSAupLoadLegalEntityFile() throws TclCommonException {
		
		ResponseResource<AttachmentBean> response = customerController.upLoadLegalEntityFile(getMultipartfile(),1,"Reference","MSA", "IAS");
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	@Test
	public void testMSAupLoadLegalEntityFile2() throws TclCommonException {
		Mockito.when(customerLeAttributeValueRepository
		.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);
		ResponseResource<AttachmentBean> response = customerController.upLoadLegalEntityFile(getMultipartfile(),1,"Reference","MSA", "IAS");
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	
	/*
	 * possitive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testSSDupLoadLegalEntityFile() throws TclCommonException {
		
		ResponseResource<AttachmentBean> response = customerController.upLoadLegalEntityFile(getMultipartfile(),1,"Reference1","Service Schedule", "IAS");
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	
	/*
	 * possitive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testMSAupLoadLegalEntityFileForAttributeNull() throws TclCommonException {
		
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(Mockito.anyInt(),Mockito.anyInt()))
		.thenReturn(null);
		
		ResponseResource<AttachmentBean> response = customerController.upLoadLegalEntityFile(getMultipartfile(),1,"Reference","MSA", null);
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	
	
	/*
	 * possitive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testMSAupLoadCustomerFile() throws TclCommonException {
		
		ResponseResource<AttachmentBean> response = customerController.upLoadCustomerFile(getMultipartfile(),1,"Reference","MSA");
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	
	@Test
	public void testMSAupLoadCustomerFile2() throws TclCommonException {
		Mockito.when(customerAttributeValueRepository
		.findByCustomerAndMstCustomerSpAttribute(Mockito.any(), Mockito.any())).thenReturn(null);
		ResponseResource<AttachmentBean> response = customerController.upLoadCustomerFile(getMultipartfile(),1,"Reference","MSA");
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	/*
	 * possitive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testSSDupLoadCustomerFile() throws TclCommonException {
		Mockito.when(customerAttributeValueRepository.findByCustomerAndMstCustomerSpAttribute(Mockito.any(),Mockito.any()))
		.thenReturn(new ArrayList<>());
		
		ResponseResource<AttachmentBean> response = customerController.upLoadCustomerFile(getMultipartfile(),1,"Reference1","Service Schedule");
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	
	/*
	 * possitive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testMSAupLoadCustomerFileForAttributeNull() throws TclCommonException {
		
		Mockito.when(customerAttributeValueRepository.findByCustomerAndMstCustomerSpAttribute(Mockito.any(),Mockito.any()))
		.thenReturn(null);
		
		ResponseResource<AttachmentBean> response = customerController.upLoadCustomerFile(getMultipartfile(),1,"Reference","MSA");
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	
	
	/*
	 * possitive case- testgetLeStateGst
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testgetAttachments() throws TclCommonException {
		Mockito.when(customerLeAttributeValueRepository
		.findByCustomerLeIdAndAttachmentId(Mockito.anyInt(), Mockito.anyString())).thenReturn(objectCreator.createcustomerLeAttributeValue());
		ResponseEntity<Resource> response = customerController.getAttachments(1,1);		
		assertTrue(response!=null);
	}
	
	/*
	 * negative case
	 * 
	 * @throws TclCommonException
	 */
	/*@Test
	public void testgetAttachmentsNullId() throws TclCommonException {
		
		ResponseEntity<Resource> response = customerController.getAttachments(null,null);
		
		//ResponseResource<Resource> response = new ResponseResource<Resource>(response1.getBody());
		
		assertTrue(response== null);
	}*/
	
	
	
	
	/*
	 * possitive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupLoadFile() throws TclCommonException {
		
		ResponseResource<ServiceResponse> response = customerController.upLoadFile(1,getMultipartfile(),1,1,getIntegerId(),"Reference","COF","");
		
		assertTrue(response.getStatus() == Status.SUCCESS && response.getData()!=null);
	}
	
	/*
	 * negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupLoadFileLeIdNull() throws TclCommonException {
		
		ResponseResource<ServiceResponse> response = customerController.upLoadFile(null,getMultipartfile(),1,1,getIntegerId(),"Reference","MSA","");
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupLoadFileFileNull() throws TclCommonException {
		
		ResponseResource<ServiceResponse> response = customerController.upLoadFile(1,null,1,1,getIntegerId(),"Reference","MSA","");
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupLoadFileOrderIdNull() throws TclCommonException {
		
		ResponseResource<ServiceResponse> response = customerController.upLoadFile(1,getMultipartfile(),null,1,getIntegerId(),"Reference","MSA","");
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupLoadFileQuoteIdNull() throws TclCommonException {
		
		ResponseResource<ServiceResponse> response = customerController.upLoadFile(1,getMultipartfile(),1,null,getIntegerId(),"Reference","MSA","");
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupLoadFileListIdNull() throws TclCommonException {
		
		ResponseResource<ServiceResponse> response = customerController.upLoadFile(1,getMultipartfile(),1,1,null,"Reference","MSA","");
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupLoadFileReferenceNull() throws TclCommonException {
		
		ResponseResource<ServiceResponse> response = customerController.upLoadFile(1,getMultipartfile(),1,1,getIntegerId(),null,"MSA","");
		
		assertTrue(response.getData() == null);
	}
	
	/*
	 * negative case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupLoadFileTypeNull() throws TclCommonException {
		
		ResponseResource<ServiceResponse> response = customerController.upLoadFile(1,getMultipartfile(),1,1,getIntegerId(),"Reference",null,"");
		
		assertTrue(response.getData() == null);
	}
	public MockMultipartFile getMultipartfile() {
		MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());
		return file;
	}
	
	public List<Integer> getIntegerId(){
		List<Integer> integerList=new ArrayList();
		integerList.add(1);
		return integerList;
	}
	
	
	/*
	 * negative test case for fetching MSADocument
	 */
	@Test
	public void testMSADocumentFetchAttributeNull() throws TclCommonException {
		
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(Mockito.anyInt(),Mockito.anyInt()))
		.thenReturn(null);
		ResponseResource<List<AttachmentBean>> response = customerController.getMSADocuments(1, null);
		assertTrue(response.getData() != null);
	}
	
	
	/*
	 * positive test case for testSuplierEntity
	 */
	@Test
	public void testSuplierEntity() throws TclCommonException {
		Mockito.when(customerLegalEntityRepository.findAllById(Mockito.anyInt())).thenReturn(objectCreator.createCustomlegalEntity());
		ResponseResource<CustomerLegalEntityProductResponseDto> response = customerController.getSupplierLegalEntityBasedOnCust(objectCreator.createCustomerLegalEntityRequestBean());
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
		
		
	}
	/*
	 * negative test case for testSuplierEntity
	 */
	@Test
	public void testNegativeSuplierEntity() throws TclCommonException {
		
		ResponseResource<CustomerLegalEntityProductResponseDto> response = customerController.getSupplierLegalEntityBasedOnCust(null);
		assertTrue(response.getData() != null || response.getStatus() == Status.FAILURE);
		
		
	}
	@Test
	public void testGetAllCustomer() throws TclCommonException {
		Mockito.when(customerRepository.findAll()).thenReturn(objectCreator.createCustomerList());
		ResponseResource<List<CustomerDto>> response = customerController.getAllCustomer();
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test(expected=Exception.class)
	public void testGetAllCustomerException() throws TclCommonException {
		Mockito.when(customerRepository.findAll()).thenReturn(null);
		ResponseResource<List<CustomerDto>> response = customerController.getAllCustomer();
		assertTrue(response == null);
	}
	@Test
	public void testGetCustomerLeContactDetails() throws TclCommonException{
		ResponseResource<CustomerLeContactDetailsBean> response = customerController.getCustomerLeContactDetails(1);
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testAttachSSDocument() throws TclCommonException{
		ResponseResource<ServiceScheduleBean> response = customerController.attachSSDocument(1, objectCreator.getServiceScheduleBean());
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testUpLoadSSFile() throws TclCommonException{
		ResponseResource<String> response = customerController.upLoadSSFile(new MockMultipartFile("fileName", new byte[2]),"NPL");
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testUpLoadMSAFile() throws TclCommonException{
		ResponseResource<String> response = customerController.upLoadMSAFile(new MockMultipartFile("fileName", new byte[2]),"NPL");
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetCustomerLegalEntityDetailsByCustomerIdForServiceAprch1() throws TclCommonException{
		List<String> serviceName= new ArrayList<>();
		serviceName.add("ACANS");
		Mockito.when(customerLegalEntityRepository.findAllByGivenCountry(Mockito.anyInt(), Mockito.anyString())).thenReturn(objectCreator.createCustomerLegalEntity());
		ResponseResource<List<CustomerLegalEntityDto>> response =
				customerController.getCustomerLegalEntityDetailsByCustomerIdForService(2, "GSIP",serviceName,"India",
						"Public IP");
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetCustomerLegalEntityDetailsByCustomerIdForServiceAprch2() throws TclCommonException{
		List<String> serviceName= new ArrayList<>();
		serviceName.add("Domestic Voice");
		Mockito.when(customerLegalEntityRepository.findAllByGivenCountry(Mockito.anyInt(), Mockito.anyString())).thenReturn(objectCreator.createCustomerLegalEntity());
		ResponseResource<List<CustomerLegalEntityDto>> response =
				customerController.getCustomerLegalEntityDetailsByCustomerIdForService(2, "GSIP",serviceName,"India",
						"Public IP");
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetCustomerLegalEntityDetailsByCustomerIdForServiceAprch3() throws TclCommonException{
		List<String> serviceName= new ArrayList<>();
		serviceName.add("ITFS");
		Mockito.when(customerLegalEntityRepository.findAllByGivenCountry(Mockito.anyInt(), Mockito.anyString())).thenReturn(objectCreator.createCustomerLegalEntity());
		ResponseResource<List<CustomerLegalEntityDto>> response =
				customerController.getCustomerLegalEntityDetailsByCustomerIdForService(2, "GSIP",serviceName,"India",
						"Public IP");
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testEditBillingInfo() throws TclCommonException{
		String queueResponse="{\"request\":\"{\"quoteToLeId\":1,\"attrName\":\"Billing Address\",\"attrValue\":\"porur\",\"userName\":\"optimus\"}\",\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(queueResponse);
		ResponseResource<CustomerLeBillingInfoDto> response = customerController.editBillingInfo(2, objectCreator.getCustomerLeBillingRequestBean());
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void upLoadCountrySpecificFilesTest() throws TclCommonException, IOException {  
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn("1");
		MockMultipartFile file = new MockMultipartFile("file", "NameOfTheFile", "multipart/form-data", new byte[2]);
		ResponseResource<ServiceResponse> response = customerController.upLoadCountrySpecificFiles("ITFS",
				"India", file);
		assertEquals(Status.SUCCESS, response.getStatus());
	}
	@Test
	public void testGetSupplierLegalEntityDetailsByCustomerLegalIdForService() throws TclCommonException{
		List<String> currencyVal=new ArrayList<>();
		currencyVal.add("INR");
		List<String> serviceName=new ArrayList<>();
		currencyVal.add("ACANS");
		Mockito.when(currencyMasterRepository.findShortNameById(Mockito.anyInt())).thenReturn(currencyVal);
		ResponseResource<Set<CustomerLegalEntityProductResponseDto>> response = customerController.getSupplierLegalEntityDetailsByCustomerLegalIdForService(2,"GSIP",serviceName);
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetSupplierLegalEntityDetailsByCustomerLegalIdForService2() throws TclCommonException{
		List<String> currencyVal=new ArrayList<>();
		currencyVal.add("INR");
		Mockito.when(currencyMasterRepository.findShortNameById(Mockito.anyInt())).thenReturn(currencyVal);
		ResponseResource<Set<CustomerLegalEntityProductResponseDto>> response = customerController.getSupplierLegalEntityDetailsByCustomerLegalIdForService(2,"GSIP",new ArrayList<String>());
		assertTrue(response.getData() != null || response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testGetCountrySpecificAttachments() throws TclCommonException{
		ResponseEntity<Resource> response = customerController.getCountrySpecificAttachments(1);
		assertTrue(response != null);
	}
	@Test
	public void testSaveOrUpdateGstNumberForLegalEntity() throws TclCommonException{
		ResponseResource<String> response = customerController.saveOrUpdateGstNumberForLegalEntity(2,3,"GST123");
		assertTrue(response != null);
	}
	@Test
	public void testSaveOrUpdateGstNumberForLegalEntityApproch2() throws TclCommonException{
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntity_IdAndMstLeAttribute_Id(Mockito.anyInt(),Mockito.anyInt()))
		.thenReturn(new ArrayList<CustomerLeAttributeValue>());
		ResponseResource<String> response = customerController.saveOrUpdateGstNumberForLegalEntity(2,3,"GST123");
		assertTrue(response != null);
	}
	
}
