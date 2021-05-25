package com.tcl.dias.customer.consumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.customer.entity.entities.CustomerLeAttributeValue;
import com.tcl.dias.customer.entity.repository.AttachmentRepository;
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
 * @author Biswajit
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerDetailsConsumerTest {
	
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
	private ObjectCreator objectCreator;

	@MockBean
	private ServiceProviderRepository serviceProviderRepository;

	@MockBean
	AttachmentRepository attachmentRepository;

	@MockBean
	ServiceProviderLegalEntityCountryRepository spleCountryRespository;
	
	@MockBean
	CustomerLeBillingInfoRepository customerLeBillingInfoRepository;
	
	@MockBean
	MstCountryRepository mstCountryRepository;
	
	@MockBean
	MstCustomerSpAttributeRepository mstCustomerSpAttributeRepository;
	
	@MockBean
	CustomerAttributeValueRepository customerAttributeValueRepository;
	
	@Autowired
	SupplierDetailsListener supplierDetailsListener;
	
	@Autowired
	CustomerDetailsConsumer customerDetailsConsumer;
	
	@MockBean
	CustomerLeContactRepository customerLeContactRepository;
	
	@MockBean
	LeStateGstRepository leStateGstRepository;
	
	
	@Before
	public void init() throws AmqpException, TclCommonException {
		
		Mockito.when(customerLeBillingInfoRepository.findByCustomerLegalEntity_IdAndIsactive(Mockito.anyInt(), Mockito.anyString()))
		.thenReturn(objectCreator.getCustomerLeBillingInfoList());
		
		Mockito.when(customerLeBillingInfoRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(objectCreator.getCustomerLeBillingInfo()));
		
		Mockito.when(customerLegalEntityRepository.findByCustomerId(Mockito.any()))
		.thenReturn(objectCreator.createCustomerLegalEntity());

		Mockito.when(mstCountryRepository.findByName(Mockito.any()))
		.thenReturn(objectCreator.createMstCountryList());
		
		Mockito.when(attachmentRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.ofNullable(objectCreator.createAttachMent()));
		
		Mockito.when(customerLegalEntityRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(objectCreator.craeteCustomerLegalEntity()));

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
		
		Mockito.when(leStateGstRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of(objectCreator.getLeStateGst()));
		
		Mockito.when(leStateGstRepository.findByCustomerLegalEntity(Mockito.any()))
		.thenReturn(objectCreator.getLeStateGstList());
		
		Mockito.when(leStateGstRepository.findByCustomerLegalEntityAndState(Mockito.any(),Mockito.anyString()))
		.thenReturn(objectCreator.getLeStateGstList());
				
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
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLeIdAndProductName(Mockito.anyInt(),Mockito.any()))
		.thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(spLeAttributeValueRepository
				.findByMstLeAttribute_IdAndServiceProviderLegalEntity_Id(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn((objectCreator.craeteSpLeAttributeValue()));
		Mockito.when(customerRepository.findByIdInOrderByCustomerNameAsc(Mockito.any())).thenReturn(objectCreator.createCustomerList());
		Mockito.when(customerLegalEntityRepository.findByCustomerIdIn(Mockito.any())).thenReturn(objectCreator.createCustomerLegalEntity());
		Mockito.when(customerLeContactRepository.findByCustomerLeIdContactTypeIsNull(Mockito.anyInt())).thenReturn(objectCreator.getCustomerLeContactList());
		Mockito.when(customerLeAttributeValueRepository.findByCustomerLegalEntityAndProductNameAndMstLeAttribute
				(Mockito.any(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.createcustomerLeAttributeValue());
		Mockito.when(customerLeAttributeValueRepository.saveAndFlush(Mockito.any())).thenReturn(objectCreator.createCustomerLeAttributeValue());

	}
	
	private Message<String> getMessageList(String request) throws TclCommonException {
		Message<String> message=new GenericMessage<String>(request);
		return message;
	}
	
	@Test
	public void testprocessCustomerDetailsNameNull() throws TclCommonException {
		
		String response=customerDetailsConsumer.processCustomerDetails(null);
		assertTrue(response == null);
	}
	
	@Test
	public void testprocessCustomerDetails() throws TclCommonException {
		String request="{\"request\":\"1,2\",\"mdcFilterToken\":\"H66LGNCNN0MA5MGLGHAAQ9\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.processCustomerDetails(message);
		
		assertTrue(true);
	}
	
	@Test
	public void testgetCustomerLeAttributesNull() throws TclCommonException {
		
		String response=customerDetailsConsumer.getCustomerLeAttributes(null);
		assertTrue(response == null);
	}
	
	@Test
	public void testgetCustomerLeAttributes() throws TclCommonException {
		String request="{\"request\":\"5\",\"mdcFilterToken\":\"704QDHGO7ZFBIJJFGWXY7T\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.getCustomerLeAttributes(message);
		assertTrue(true);
	}
	
	@Test
	public void testprocessLeStateNull() throws TclCommonException {
		
		String response=customerDetailsConsumer.processLeState(null);
		assertTrue(response == null);
	}
	
	@Test
	public void testprocessLeState() throws TclCommonException {
		String request="{\"request\":\"1,2\",\"mdcFilterToken\":\"H66LGNCNN0MA5MGLGHAAQ9\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.processLeState(message);
		assertTrue(true);
	}

	@Test
	public void testprocessCustomerLeDetailsNull() throws TclCommonException {
		
		String response=customerDetailsConsumer.processCustomerLeDetails(null);
		assertTrue(response == null);
	}
	
	@Test
	public void testProcessCustomerLeDetails() throws TclCommonException {
		String request="{\"request\":\"114,1029\",\"mdcFilterToken\":\"H66LGNCNN0MA5MGLGHAAQ9\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.processCustomerLeDetails(message);
		assertTrue(true);
	}

	@Test
	public void testGetCustomerLeAttributesBaseOnProduct() throws TclCommonException {
		String request="{\"request\":\"{\\\"customerLeId\\\":1,\\\"productName\\\":\\\"IAS\\\"}\",\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		String response=customerDetailsConsumer.getCustomerLeAttributesBaseOnProduct(request);
		assertTrue(true);
	}
	@Test
	public void testGetCustomerLeAttributesBaseOnProductNull() throws TclCommonException {
		
		String response=customerDetailsConsumer.getCustomerLeAttributesBaseOnProduct(null);
		assertTrue(response == null);
	}
	@Test
	public void testProcessSuplierLeDetails() throws TclCommonException {
		String request="{\"request\":\"5\",\"mdcFilterToken\":\"704QDHGO7ZFBIJJFGWXY7T\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.processSuplierLeDetails(message);
		assertTrue(true);
	}
	@Test
	public void testProcessSuplierLeDetailsNull() throws TclCommonException {
		String response=customerDetailsConsumer.processSuplierLeDetails(null);
		assertTrue(response == null);
	}
	@Test
	public void testProcessBillingContact() throws TclCommonException {
		String request="{\"request\":\"6343\",\"mdcFilterToken\":\"TUMLFXS3EORHPSIEQCULKZ\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.processBillingContact(message);
		assertTrue(true);
	}
	@Test
	public void testProcessBillingContactNull() throws TclCommonException {
		String response=customerDetailsConsumer.processBillingContact(null);
		assertTrue(response == null);
	}
	@Test
	public void testProcessCustDetails() throws TclCommonException {
		String request="{\"request\":\"114,1029\",\"mdcFilterToken\":\"H66LGNCNN0MA5MGLGHAAQ9\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.processCustDetails(message);
		assertTrue(true);
	}
	@Test
	public void testProcessCustDetailsNull() throws TclCommonException {
		String response=customerDetailsConsumer.processCustDetails(null);
		assertTrue(response == null);
	}
	@Test
	public void testProcessCustomerLeFromCustomer() throws TclCommonException {
		String request="{\"request\":\"3\",\"mdcFilterToken\":\"TUMLFXS3EORHPSIEQCULKZ\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.processCustomerLeFromCustomer(message);
		assertTrue(true);
	}
	@Test
	public void testProcessCustomerLeFromCustomerNull() throws TclCommonException {
		String response=customerDetailsConsumer.processCustomerLeFromCustomer(null);
		assertTrue(response == null);
	}
	@Test
	public void testProcessCustomerLeFromLeId() throws TclCommonException {
		String request="{\"request\":\"114,1029\",\"mdcFilterToken\":\"H66LGNCNN0MA5MGLGHAAQ9\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.processCustomerLeFromLeId(message);
		assertTrue(true);
	}
	@Test
	public void testProcessCustomerLeFromLeIdNull() throws TclCommonException {
		String response=customerDetailsConsumer.processCustomerLeFromLeId(null);
		assertTrue(response == null);
	}
	@Test
	public void testProcessCustomerLeByCustomerIds() throws TclCommonException {
		String request="{\"request\":\"114,1029\",\"mdcFilterToken\":\"H66LGNCNN0MA5MGLGHAAQ9\"}";
		Message<String> message = getMessageList(request);
		String response=customerDetailsConsumer.processCustomerLeByCustomerIds(message);
		assertTrue(true);
	}
	@Test
	public void testProcessCustomerLeByCustomerIdsNull() throws TclCommonException {
		String response=customerDetailsConsumer.processCustomerLeByCustomerIds(null);
		assertTrue(response == null);
	}
	@Test
	public void testProcessCustomerLeSS() throws TclCommonException {
		String request="{\"request\":\"{\\\"productName\\\":\\\"IAS\\\",\\\"displayName\\\":\\\"Service Schedule\\\",\\\"name\\\":\\\"Service Schedule\\\",\\\"isSSUploaded\\\":\\\"true\\\",\\\"customerLeId\\\":\\\"3\\\"}\",,\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		String response=customerDetailsConsumer.processCustomerLeSS(request);
		assertTrue(true);
	}
	@Test
	public void testProcessCustomerLeSSApproch2() throws TclCommonException {
		Mockito.when(mstLeAttributeRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
		String request="{\"request\":\"{\\\"productName\\\":\\\"IAS\\\",\\\"displayName\\\":\\\"Service Schedule\\\",\\\"name\\\":\\\"Service Schedule\\\",\\\"isSSUploaded\\\":\\\"true\\\",\\\"customerLeId\\\":\\\"3\\\"}\",\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		String response=customerDetailsConsumer.processCustomerLeSS(request);
		assertTrue(true);
	}
	@Test
	public void testProcessCustomerLeSSNull() throws TclCommonException {
		String response=customerDetailsConsumer.processCustomerLeSS(null);
		assertTrue(response == null);
	}
	@Test
	public void testProcessCustomerLeMSA() throws TclCommonException {
		String request="{\"request\":\"{\\\"productName\\\":\\\"IAS\\\",\\\"displayName\\\":\\\"MSA\\\",\\\"name\\\":\\\"MSA\\\",\\\"isMSAUploaded\\\":\\\"true\\\",\\\"customerLeId\\\":\\\"3\\\"}\",\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		String response=customerDetailsConsumer.processCustomerLeMSA(request);
		assertTrue(true);
	}
	@Test
	public void testProcessCustomerLeMSAAprch2() throws TclCommonException {
		Mockito.when(mstLeAttributeRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
		String request="{\"request\":\"{\\\"productName\\\":\\\"IAS\\\",\\\"displayName\\\":\\\"MSA\\\",\\\"name\\\":\\\"MSA\\\",\\\"isMSAUploaded\\\":\\\"true\\\",\\\"customerLeId\\\":\\\"3\\\"}\",\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		String response=customerDetailsConsumer.processCustomerLeMSA(request);
		assertTrue(true);
	}
	@Test
	public void testProcessCustomerLeMSANull() throws TclCommonException {
		String response=customerDetailsConsumer.processCustomerLeMSA(null);
		assertTrue(response == null);
	}
	@Test
	public void testGetCustomerBillingContactDetails() throws TclCommonException {
		String request="{\"request\":\"3\",\"mdcFilterToken\":\"TUMLFXS3EORHPSIEQCULKZ\"}";
		String response=customerDetailsConsumer.getCustomerBillingContactDetails(request);
		assertTrue(true);
	}
	@Test
	public void testGetCustomerBillingContactDetailsNull() throws TclCommonException {
		String response=customerDetailsConsumer.getCustomerBillingContactDetails(null);
		assertTrue(response == null);
	}
	@Test
	public void testGetCustomerLeContactDetailsQueue() throws TclCommonException {
		String request="{\"request\":\"3\",\"mdcFilterToken\":\"TUMLFXS3EORHPSIEQCULKZ\"}";
		String response=customerDetailsConsumer.getCustomerLeContactDetails(request);
		assertTrue(true);
	}
	@Test
	public void testGetCustomerLeContactDetailsNull() throws TclCommonException {
		String response=customerDetailsConsumer.getCustomerLeContactDetails(null);
		assertTrue(response == null);
	}
	@Test
	public void testGetCustomerLeEmailDetails() throws TclCommonException {
		Mockito.when(customerLeContactRepository.findByEmailId(Mockito.anyString())).thenReturn(objectCreator.getCustomerLeContactList());
		String request="{\"request\":\"tata@com\",\"mdcFilterToken\":\"TUMLFXS3EORHPSIEQCULKZ\"}";
		String response=customerDetailsConsumer.getCustomerLeEmailDetails(request);
		assertTrue(true);
	}
	@Test
	public void testGetCustomerLeEmailDetailsNull() throws TclCommonException {
		String response=customerDetailsConsumer.getCustomerLeEmailDetails(null);
		assertTrue(response == null);
	}
	@Test
	public void testGetAllCustomerDetailsWithLe() throws TclCommonException {
		Mockito.when(customerLegalEntityRepository.findAll()).thenReturn(objectCreator.createCustomerLegalEntity());
		Mockito.when(customerLeContactRepository.findByEmailId(Mockito.anyString())).thenReturn(objectCreator.getCustomerLeContactList());
		String request="{\"request\":\"tata\",\"mdcFilterToken\":\"TUMLFXS3EORHPSIEQCULKZ\"}";
		String response=customerDetailsConsumer.getAllCustomerDetailsWithLe(request);
		assertTrue(true);
	}
	@Test
	public void testGetAllCustomerDetailsWithLeNull() throws TclCommonException {
		String response=customerDetailsConsumer.getCustomerLeEmailDetails(null);
		assertTrue(response == null);
	}
}
