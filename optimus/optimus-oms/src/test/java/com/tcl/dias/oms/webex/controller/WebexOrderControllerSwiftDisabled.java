package com.tcl.dias.oms.webex.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.utils.WebexObjectCreator;
import com.tcl.dias.oms.webex.service.WebexOrderService;
import com.tcl.dias.oms.webex.service.WebexQuotePdfService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Test class for webex order controller when swift api is disabled
 * 
 * @author Srinivasa Raghavan
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(properties = { "swift.api.enabled = false" })
public class WebexOrderControllerSwiftDisabled {

	@Autowired
	WebexObjectCreator objectCreator;

	@Autowired
	WebexOrderController webexOrderController;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	CofDetailsRepository cofDetailsRepository;

	@MockBean
	ProductSolutionRepository productSolutionRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	HttpServletResponse httpServletResponse;

	@MockBean
	OrderRepository orderRepository;

	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	OrderUcaasRepository orderUcaasRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	UserRepository userRepository;

	@MockBean
	CustomerRepository customerRepository;

	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	QuoteUcaasRepository quoteUcaasRepository;

	@MockBean
	UserInfoUtils userInfoUtils;

	@MockBean
	QuoteGscRepository quoteGscRepository;

	@MockBean
	OrderGscRepository orderGscRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	WebexOrderService webexOrderService;

//	@MockBean
//	WebexQuotePdfService webexQuotePdfService;

	@MockBean
	DocusignService docuSignService;

	@Autowired
	WebexQuoteController webexQuoteController;

	@Autowired
	WebexQuotePdfService webexQuotePdfService;

	@Before
	public void init() throws TclCommonException {

		// mocking mstProductFamilyRepository
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductFamily());

		// mocking user repository
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());

		// mocking customer repository
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getCustomer());

		// mocking authtokenrepository
		Mockito.when(authTokenDetailRepository.find(Mockito.any())).thenReturn(objectCreator.getUserInfo());

		// mocking orderrepository
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any())).thenReturn(objectCreator.createOrderToLe());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.createOptionalOrderToLe());

		// mocking orderproductsolutionrepository
		when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductSolutionsList());

		// mocking orderToLeProductFamilyRepository
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getOrderToLeProductFamilies().stream().findAny().get());

		// mocking orderUcaas repository
		Mockito.when(orderUcaasRepository.findByOrderToLeId(Mockito.anyInt()))
				.thenReturn(objectCreator.getOrderLines());

		// mocking quoteLeAttributeValueRepository
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValues());

		// mocking orderRepository
		when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createOrder()));
		doNothing().when(orderProductComponentsAttributeValueRepository).deleteAll(Mockito.anyIterable());
		when(orderProductSolutionRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(objectCreator.getOrderProductSolution()));

		// mocking user information
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		// mocking rabbit mq
		when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any())).thenReturn("");

		// mocking quote repository
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.createQuote());

		// mocking quotele repository
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.getQuoteToLe()));
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt())).thenReturn(objectCreator.getQuoteToLeList());

		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.getQuoteToLeList().stream().findAny());

		// mocking ordersleAttributeValueRepository
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.createOrdersLeAttributeValues());

		// mocking productsolutionrepository
		when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.createProductSolutionWithGscAndWebex());

		// mocking quoteToLeProductFamilyRepository
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeProductFamilies().stream().findAny().get());

		// mocking quoteUcaas repository
		Mockito.when(quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyByte())).thenReturn(objectCreator.createUcaas());
		Mockito.when(quoteUcaasRepository.save(Mockito.any())).thenReturn(objectCreator.createUcaas());
		Mockito.when(quoteUcaasRepository.findByQuoteToLeId(Mockito.anyInt()))
				.thenReturn(objectCreator.getQuoteLines());

		// mocking customer repository.
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getCustomer());

		// mocking user repository.
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());
		Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerList());
		Mockito.when(userInfoUtils.getUserType()).thenReturn("sales");

		Mockito.when(orderUcaasRepository.findByOrderToLeIdAndIsConfig(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderLines());
	}

	/**
	 * generate cof pdf test
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGenerateCofPdf() throws TclCommonException {
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.any())).thenReturn(null);
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe_Id(Mockito.anyInt()))
				.thenReturn(objectCreator.getQuoteToLeProductFamilies().stream().findAny().get());
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("rabbitmq_billing_contact_queue"), Mockito.anyString()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.getBillingContact()));
		Mockito.when(quoteGscRepository.findByProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.createQuoteGscList());
		Mockito.when(
				mqUtils.sendAndReceive(Mockito.contains("${rabbitmq.customer.contact.details.queue}"), Mockito.any()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.getCustomerLeContactDetailBeans()));
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("rabbitmq_location_detail"), Mockito.any()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.getAddressDetail()));
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("${rabbitmq.suplierle.queue}"), Mockito.anyString()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.getSPDetails()));
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		String response = webexQuotePdfService.processCofPdf(1, httpServletResponse, true, true);
		assertNotNull(response);
		Assert.assertEquals("", response);

		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.any())).thenReturn(objectCreator.getCofDetails());
		List<QuoteLeAttributeValue> quoteLeAttributeValues = objectCreator.getQuoteLeAttributesWithDiffMstAttribute();
		httpServletResponse = new MockHttpServletResponse();
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any())).thenReturn(quoteLeAttributeValues);
		ResponseResource<?> response1 = webexOrderController.generateCofPdf(1, httpServletResponse);
		assertNotNull(response1);
		Assert.assertEquals(Status.SUCCESS, response1.getStatus());

	}
}
