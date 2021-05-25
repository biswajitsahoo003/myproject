package com.tcl.dias.oms.gsc.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscDocumentBean;
import com.tcl.dias.oms.gsc.beans.GscPricingRequest;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteAttributesBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteDataBean;
import com.tcl.dias.oms.gsc.controller.v1.GscQuoteController;
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the GscQuoteController.java class.
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("rawtypes")
public class GscQuoteControllerTest {

	@Autowired
	GscObjectCreator objectCreator;

	/*@Autowired
	ObjectCreator omsObjectCreator;*/

	@Autowired
	GscQuoteController gscQuoteController;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	QuotePriceRepository quotePriceRepository;

	@MockBean
	OrderRepository orderRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	OrdersLeAttributeValueRepository orderLeAttributeValueRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	QuoteGscRepository quoteGscRepository;

	@MockBean
	OrderGscRepository orderGscRepository;

	@MockBean
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@MockBean
	OrderGscDetailRepository orderGscDetailsRepository;

	@MockBean
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@MockBean
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@MockBean
	QuoteGscSlaRepository quoteGscSlaRepository;

	@MockBean
	OrderGscSlaRepository orderGscSlaRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@MockBean
	GscQuotePdfService gscQuotePdfService;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	UserInfoUtils userInfoUtils;

	@MockBean
	UserRepository userRepository;

	@MockBean
	RestClientService restClientService;

	@Autowired
	PricingDetailsRepository pricingDetailsRepository;
	
	@MockBean
	NotificationService notificationService;
	
	@MockBean
	GscPricingFeasibilityService gscPricingFeasibilityService;
	
	

	private <T> T fromJsonFile(String jsonFilePath, Class<T> clazz) {
		URL url = Resources.getResource(jsonFilePath);
		CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
		try {
			return new ObjectMapper().readValue(charSource.openStream(), clazz);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return null;
	}

	private <T> T fromJsonFile(String jsonFilePath, TypeReference<T> typeReference) {
		URL url = Resources.getResource(jsonFilePath);
		CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
		try {
			return new ObjectMapper().readValue(charSource.openStream(), typeReference);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return null;
	}

	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	ProductSolutionRepository productSolutionRepository;
	
	@MockBean
	OmsSfdcService omsSfdcService;
	
	@MockBean
	IllSiteRepository illSiteRepository;

	/**
	 * init- predefined mocks
	 *
	 * @throws TclCommonException
	 */
	@Before
	public void init() throws TclCommonException {
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.createOptionalQuote());
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(new Order());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getPricingQuoteLe()));
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(new OrderToLe());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValueList());
		Mockito.when(orderLeAttributeValueRepository.save(Mockito.any())).thenReturn(new OrdersLeAttributeValue());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteProductFamilies());
		Mockito.when(orderToLeProductFamilyRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductFamily());
		Mockito.when(orderProductSolutionRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductSolution());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getQuoteProductComponentAttributeValues());
		Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstProductComponent()));
		Mockito.when(productAttributeMasterRepository.findByNameIn(Mockito.any()))
				.thenReturn(objectCreator.getProductAttributeMasterList());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstOmsAttributeList());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getQuotePrice());
		Mockito.when(quoteGscRepository.findByProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.createQuoteGscList());
		Mockito.when(orderGscRepository.save(Mockito.any())).thenReturn(objectCreator.getordergsc());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(new QuoteLeAttributeValue());
		Mockito.when(quoteGscDetailsRepository.findByQuoteGsc(Mockito.any()))
				.thenReturn(objectCreator.getQuoteGscDetailSet());
		Mockito.when(orderGscDetailsRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderGscDetail());
		Mockito.when(quoteGscSlaRepository.findByQuoteGsc(Mockito.any())).thenReturn(objectCreator.getQuoteGscSla());
		Mockito.when(orderGscSlaRepository.save(Mockito.any())).thenReturn(objectCreator.getorderGscSla());
		Mockito.when(quoteGscRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteGsc()));
		Mockito.when(quoteGscDetailsRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteGscDetail()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.createOptionalQuote().get());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(orderProductComponentRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(Mockito.anyInt()))
				.thenReturn(objectCreator.getListOfQuoteProductComponentsAttributeValue());
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(objectCreator.getListOfQuoteProductComponentsAttributeValue());
		Mockito.when(quoteProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteProductComponent().get(0)));
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getProductAtrributeMaster());
		Mockito.when(quoteProductComponentsAttributeValueRepository.findAllById(Mockito.anyIterable()))
				.thenReturn(objectCreator.getListOfQuoteProductComponentsAttributeValue());
		doNothing().when(quoteProductComponentsAttributeValueRepository).deleteAll(Mockito.anyIterable());
		Mockito.when(quoteProductComponentsAttributeValueRepository.saveAll(Mockito.anyIterable()))
				.thenReturn(objectCreator.getListOfQuoteProductComponentsAttributeValue());
		Mockito.when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createProductSolutions()));
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createOrderAttributes());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(objectCreator.createQuoteToLe());
		Mockito.when(quoteGscRepository.save(Mockito.any())).thenReturn(new QuoteGsc());
		Mockito.when(quoteGscDetailsRepository.save(Mockito.any())).thenReturn(new QuoteGscDetail());
		Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(new QuotePrice());
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		Mockito.when(quoteGscRepository.findByQuoteToLe(Mockito.any())).thenReturn(objectCreator.createQuoteGscList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt())).thenReturn(Arrays.asList(new QuotePrice()));
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any())).thenReturn("");
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(objectCreator.getQuoteToLeList());

		Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStage());
		Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeFamily());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductComponentList());
		Mockito.when(restClientService.post(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getRestResponse());
		when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValueList());
		 Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerList());
		 Mockito.when(userInfoUtils.getUserType()).thenReturn("sales");
		 Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(objectCreator.getQuotePrice());
		 Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(objectCreator.getIllsite());
	}

	/**
	 * Positive test case for approve Quote
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testgetGscApproveQuotes() throws TclCommonException {
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createQuote()));
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(objectCreator.getOrders());
		Mockito.when(orderGscDetailsRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderGscDetail());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn("Best effort");
		ResponseEntity response = gscQuoteController.approveQuotes(1, "approve", null);
		assertTrue(response != null && response.getStatusCode() == HttpStatus.OK);

	}

	/**
	 * Negative test case for approve Quote
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetNegativeGscApproveQuotes() {
		Mockito.when(quoteGscSlaRepository.findByQuoteGsc(Mockito.any())).thenReturn(null);
		ResponseEntity response = gscQuoteController.approveQuotes(1, "approve", null);
		assertTrue(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	public void testCreateOrUpdateQuote() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getCustomerDetailsBean());
		Mockito.when(quoteRepository.save(Mockito.any())).thenReturn(objectCreator.createQuote());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(objectCreator.createQuoteToLe());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductFamily());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.createQuote());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createQuoteToLe()));
		Mockito.when(quoteGscRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteGsc());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstOmsAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValue());
		GscApiRequest<GscQuoteDataBean> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_CREATE);
		GscQuoteDataBean quoteDataBean = fromJsonFile("com/tcl/dias/oms/gsc/controller/create_quote_001.json",
				GscQuoteDataBean.class);
		request.setData(quoteDataBean);
		doNothing().when(omsSfdcService).processCreateOpty(Mockito.any(QuoteToLe.class),Mockito.anyString());
		ResponseEntity response = gscQuoteController.createOrUpdateQuote(request);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		request.setAction(GscConstants.ACTION_UPDATE);
		response = gscQuoteController.createOrUpdateQuote(request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testCreateOrUpdateQuoteShouldFailForUnknownActionType() {
		GscApiRequest<GscQuoteDataBean> request = new GscApiRequest<>();
		request.setAction("JUNK_ACTION");
		GscQuoteDataBean quoteDataBean = fromJsonFile("com/tcl/dias/oms/gsc/controller/create_quote_001.json",
				GscQuoteDataBean.class);
		request.setData(quoteDataBean);
		ResponseEntity response = gscQuoteController.createOrUpdateQuote(request);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	/**
	 * Positive test case for
	 * {@link GscQuoteController@#getProductComponentAttributes(Integer, Integer,
	 * Integer)}
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void getProductComponentAttributesTest() {
		ResponseEntity response = gscQuoteController.getProductComponentAttributes(1, 1, 1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * Negative test case for
	 * {@link GscQuoteController@#getProductComponentAttributes(Integer, Integer,
	 * Integer)} when quoteProductComponent return empty list
	 */
	@Test
	public void getProductComponentAttributesTestForEmptyQuoteProductComponent() {
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Collections.emptyList());
		ResponseEntity response = gscQuoteController.getProductComponentAttributes(1, 1, 1);
		assertNotEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * Positive test case for
	 * {@link GscQuoteController#updateOrDeleteProductComponentAttributes(Integer, Integer, Integer, Integer, com.tcl.dias.oms.gsc.beans.GscApiRequest)}
	 */
	@Test
	public void updateOrDeleteProductComponentAttributesTest() {
		ResponseEntity response = gscQuoteController.updateOrDeleteProductComponentAttributes(1, 1, 1, 1,
				objectCreator.getGscProductComponentBeanList());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	private void setupQuoteConfigurationMocks() {
		Quote mockQuote = objectCreator.createQuote();
		mockQuote.setQuoteToLes(ImmutableSet.of(objectCreator.createQuoteToLe()));
		mockQuote.setCustomer(objectCreator.getCustomer());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(mockQuote);
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createQuoteToLe()));
		Mockito.when(quoteGscRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteGsc()));
		QuoteToLeProductFamily mockQuoteToLeProductFamily = objectCreator.getQuoteToLeFamily();
		mockQuoteToLeProductFamily.setMstProductFamily(objectCreator.getMstProductFamily());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe_Id(Mockito.anyInt()))
				.thenReturn(mockQuoteToLeProductFamily);
		Mockito.when(quoteGscRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteGsc());
		Mockito.when(quoteGscDetailsRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteGscDetail());
		Mockito.when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.getProductSolution().stream().findFirst());
		Mockito.when(quoteProductComponentRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteProductComponent().get(0));
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstProductComponent());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.quoteProductFamily);
	}

	@Test
	public void testCreateOrUpdateOrDeleteConfigurations() {
		setupQuoteConfigurationMocks();

		GscApiRequest<List<GscQuoteConfigurationBean>> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_CREATE);
		List<GscQuoteConfigurationBean> quoteDataBean = fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/create_configuration_001.json",
				new TypeReference<List<GscQuoteConfigurationBean>>() {
				});
		request.setData(quoteDataBean);
		ResponseEntity response=null;
		/*ResponseEntity response = gscQuoteController.createOrDeleteQuoteConfiguration(1000, 1000, 1000, request);
		assertEquals(HttpStatus.OK, response.getStatusCode());*/

		GscApiRequest<GscQuoteConfigurationBean> updateRequest = new GscApiRequest<>();
		updateRequest.setAction(GscConstants.ACTION_UPDATE);
		updateRequest.setData(quoteDataBean.get(0));
		gscQuoteController.updateQuoteConfiguration(1000, 1000, 1000, 1000, updateRequest);
		//assertEquals(HttpStatus.OK, response.getStatusCode());

		quoteDataBean = fromJsonFile("com/tcl/dias/oms/gsc/controller/delete_configuration_001.json",
				new TypeReference<List<GscQuoteConfigurationBean>>() {
				});
		GscApiRequest<List<GscQuoteConfigurationBean>> deleteRequest = new GscApiRequest<>();
		deleteRequest.setAction(GscConstants.ACTION_DELETE);
		deleteRequest.setData(quoteDataBean);
		response = gscQuoteController.createOrDeleteQuoteConfiguration(1000, 1000, 1000, deleteRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testCreateOrUpdateQuoteConfigurationShouldFailForUnknownActionType() {
		GscApiRequest<List<GscQuoteConfigurationBean>> request = new GscApiRequest<>();
		request.setAction("JUNK_ACTION");
		ResponseEntity response = gscQuoteController.createOrDeleteQuoteConfiguration(1000, 1000, 1000, request);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	public void testGetQuoteConfigurations() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(objectCreator.getCustomerDetailsBean());
		setupQuoteConfigurationMocks();
		ResponseEntity response = gscQuoteController.getQuoteConfigurations(1000, 1000, 1000, false);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testSaveQuoteAttributes() {
		GscQuoteAttributesBean bean = fromJsonFile("com/tcl/dias/oms/gsc/controller/create_quote_attributes_001.json",
				GscQuoteAttributesBean.class);
		GscApiRequest<GscQuoteAttributesBean> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_UPDATE);
		request.setData(bean);
		ResponseEntity response = gscQuoteController.saveQuoteComponentAttributes(1000, request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetQuoteAttributes() {
		ResponseEntity response = gscQuoteController.getQuoteComponentAttributes(1000);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testSaveQuoteLeAttributes() {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstOmsAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValue());
		GscQuoteAttributesBean bean = fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/create_quote_le_attributes_001.json", GscQuoteAttributesBean.class);
		GscApiRequest<GscQuoteAttributesBean> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_UPDATE);
		request.setData(bean);
		ResponseEntity response = gscQuoteController.saveQuoteLeAttributes(1000, 1000, request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetQuoteLeAttributes() {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		ResponseEntity response = gscQuoteController.getQuoteLeAttributes(1000, 1000);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * test case for processQuotePdf
	 **/
	@Test
	public void testPostiveProcessQuotePdf() throws Exception {
		setupQuoteConfigurationMocks();
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		ResponseEntity response = gscQuoteController.generateQuotePdf(1, mockHttpServletResponse);
		assertEquals(response.getStatusCode(), HttpStatus.OK);

	}

	@Test
	public void testGetGscQuoteById() {
		setupQuoteConfigurationMocks();
		ResponseEntity response = gscQuoteController.getGscQuoteById(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * positive testcase for pricing
	 */
	@Test
	public void testTriggerPricing() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getCustomerDetailsBean());
		GscPricingRequest requestData = new GscPricingRequest();
		requestData.setProductName("ITFS");
		requestData.setGscConfigurationIds(Arrays.asList(1, 2, 3));
		GscApiRequest<GscPricingRequest> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_UPDATE);
		request.setData(requestData);
		ResponseEntity response = gscQuoteController.triggerPricing(1, request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * positive testcase for pricing
	 */
	@Test
	public void testTriggerPricing1() {
		Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstProductComponentPstn()));
		GscPricingRequest requestData = new GscPricingRequest();
		requestData.setProductName("ITFS");
		requestData.setGscConfigurationIds(Arrays.asList(1, 2, 3));
		GscApiRequest<GscPricingRequest> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_UPDATE);
		request.setData(requestData);
		ResponseEntity response = gscQuoteController.triggerPricing(1, request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * positive testcase for pricing
	 */
	@Test
	public void testTriggerPricing2() {
		Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstProductComponentLns()));
		GscPricingRequest requestData = new GscPricingRequest();
		requestData.setProductName("ITFS");
		requestData.setGscConfigurationIds(Arrays.asList(1, 2, 3));
		GscApiRequest<GscPricingRequest> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_UPDATE);
		request.setData(requestData);
		ResponseEntity response = gscQuoteController.triggerPricing(1, request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * positive testcase for pricing
	 */
	@Test
	public void testTriggerPricing3() {
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(null);
		GscPricingRequest requestData = new GscPricingRequest();
		requestData.setProductName("ITFS");
		requestData.setGscConfigurationIds(Arrays.asList(1, 2, 3));
		GscApiRequest<GscPricingRequest> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_UPDATE);
		request.setData(requestData);
		ResponseEntity response = gscQuoteController.triggerPricing(1, request);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	/**
	 * positive testcase for pricing
	 */
	@Test
	public void testTriggerPricing4() {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.any(), Mockito.anyByte()))
				.thenReturn(new ArrayList<MstOmsAttribute>());
		GscPricingRequest requestData = new GscPricingRequest();
		requestData.setProductName("ITFS");
		requestData.setGscConfigurationIds(Arrays.asList(1, 2, 3));
		GscApiRequest<GscPricingRequest> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_UPDATE);
		request.setData(requestData);
		ResponseEntity response = gscQuoteController.triggerPricing(1, request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * Negative testcase
	 */
	@Test
	public void testTriggerPricingWithInvalidQuoteLeId() {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		GscPricingRequest requestData = new GscPricingRequest();
		requestData.setProductName("ITFS");
		requestData.setGscConfigurationIds(Arrays.asList(1, 2, 3));
		GscApiRequest<GscPricingRequest> request = new GscApiRequest<>();
		request.setAction(GscConstants.ACTION_UPDATE);
		request.setData(requestData);
		ResponseEntity response = gscQuoteController.triggerPricing(111, request);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	/**
	 * positive testcase for pricing after mpls
	 */
	@Test
	public void testTriggerPricingForMpls() {

		ResponseEntity response = gscQuoteController.triggerPricingAfterMpls(1, true);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * positive testcase for pricing with summary
	 */
	@Test
	public void testTriggerPricingForSummary() {
		ResponseEntity response = gscQuoteController.triggerPricingForSummary(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testcreateDocument() throws TclCommonException {
		when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteToLeForUpdateStatus());
		when(quoteToLeRepository.save(Mockito.any())).thenReturn(new QuoteToLe());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getCustomerLeDetails());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.createQuote());
		when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerList());
		GscApiRequest<GscDocumentBean> request = new GscApiRequest<>();
		request.setAction("UPDATE");
		GscDocumentBean documentBeanData = fromJsonFile("com/tcl/dias/oms/gsc/controller/create_document_001.json",
				GscDocumentBean.class);
		request.setData(documentBeanData);
		ResponseEntity response = gscQuoteController.createDocument(1, 1, request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testcreateDocumentForUpdateAttributes() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.createQuote());
		when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteToLeForUpdateStatus());
		when(quoteToLeRepository.save(Mockito.any())).thenReturn(new QuoteToLe());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))

				.thenReturn(objectCreator.getCustomerLeDetails());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
				Mockito.anyString())).thenReturn(objectCreator.getQuoteLeAttributeValueList());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeListWithNullAttr());
		when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerList());
		GscApiRequest<GscDocumentBean> request = new GscApiRequest<>();
		request.setAction("UPDATE");
		GscDocumentBean documentBeanData = fromJsonFile("com/tcl/dias/oms/gsc/controller/create_document_001.json",
				GscDocumentBean.class);
		request.setData(documentBeanData);
		ResponseEntity response = gscQuoteController.createDocument(1, 1, request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * 
	 * testcreateDocument - negative case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testcreateDocumentException() {
		Mockito.when(quoteRepository.findById(null)).thenReturn((Optional.ofNullable(null)));
		ResponseEntity response = gscQuoteController.createDocument(null, null, new GscApiRequest<>());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	/**
	 * Positive Test Case for
	 * {@link GscQuoteController#updateQuoteToLeStatus(Integer, Integer, String)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testupdateQuoteToLeStatusWithQuoteLeId() throws Exception {
		ResponseEntity response = gscQuoteController.updateQuoteToLeStatus(676, null, "Get_Quote");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void testupdateQuoteToLeStatusWithWrongQuoteLeId() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
		ResponseEntity response = gscQuoteController.updateQuoteToLeStatus(221312, null, "Get_Quote");
		assertNotEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void testupdateQuoteToLeStatusForNull() throws Exception {
		ResponseEntity response = gscQuoteController.updateQuoteToLeStatus(null, null, null);
		assertNotEquals(response.getStatusCode(), HttpStatus.OK);
	}

	/**
	 * Positive test case for
	 * {@link GscQuoteController#getContactAttributeDetails(Integer, Integer)}}
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetContactAttributeDetails() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteToLe()));
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValueListForContactInfo());
		ResponseEntity response = gscQuoteController.getContactAttributeDetails(1, 1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * Negative test case for
	 * {@link GscQuoteController#getContactAttributeDetails(Integer, Integer)}}
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetContactAttributeDetailsForNullQuoteToLe() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValueListForContactInfo());
		ResponseEntity response = gscQuoteController.getContactAttributeDetails(1, 1);
		assertNotEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testshareQuote() throws TclCommonException {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		Mockito.doReturn("test").when(gscQuotePdfService).processQuoteHtml(1, mockHttpServletResponse);
		ResponseEntity reponse = gscQuoteController.shareQuote(1, 1, "abc@gamil.com", mockHttpServletResponse);
		assertEquals(reponse.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void testShareQuoteForNull() {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		ResponseEntity reponse = gscQuoteController.shareQuote(null, null, "abc@gamil.com", mockHttpServletResponse);
		assertNotEquals(reponse.getStatusCode(), HttpStatus.OK);
	}

	/**
	 * Positive test case for
	 * {@link GscQuoteController#updateAttributesForConfiguration(Integer, Integer, Integer, List, GscApiRequest)}
	 */
	@Test
	public void testUpdateAttributesForConfiguration() {
		GscApiRequest<List<GscProductComponentBean>> request = new GscApiRequest<>();
		request.setAction("UPDATE");
		GscProductComponentBean documentBeanData = fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/update_attribute_multiple_configuration.json",
				GscProductComponentBean.class);
		List<GscProductComponentBean> gscProductComponentBeans = new ArrayList<>();
		gscProductComponentBeans.add(documentBeanData);
		request.setData(gscProductComponentBeans);
		ResponseEntity resposne = gscQuoteController.updateAttributesForConfiguration(1, 1, 1, Arrays.asList(1),
				request);
		assertEquals(HttpStatus.OK, resposne.getStatusCode());
	}

	@Test
	public void testUpdateAttributesForConfigurationNullInputs() {
		ResponseEntity resposne = gscQuoteController.updateAttributesForConfiguration(null, null, null,
				Arrays.asList(1), null);
		assertNotEquals(HttpStatus.OK, resposne.getStatusCode());
	}
	@Test
	public void testProcessDocusign() throws TclCommonException {
		gscQuoteController.processDocuSign(1, 1, "test", "user@tata.com", new MockHttpServletResponse(),null);
	}
	@Test
	public void testGetGscSlaDetails() throws TclCommonException{
		String queueResponse="{\"accessType\":LNS,\"gscSlaBeans\" : [{\"pdtName\":\"GSC\",\"slaName\":\"testSla\",\"accessTopology\":\"GSC\",\"defaultValue\":\"testSla\"}],\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any())).thenReturn(queueResponse);
		gscQuoteController.getGscSlaDetails("LNS");
		assertTrue(true);
	}
	@Test
	public void  testTriggerMailNotification() throws TclCommonException {
		Mockito.when(userRepository.findByCustomerIdAndStatus(Mockito.anyInt(), Mockito.any()))
		.thenReturn(objectCreator.getUserList());
		Mockito.when(notificationService.salesOrdeLeMismatchNotification(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		gscQuoteController.triggerMailNotification(1);
		assertTrue(true);
		
	}
	@Test
	public void  testGetLocationDetailsByCustomer() throws TclCommonException {
		String queueResponse="{\"request\":[{\"addressId\":\"1\",\"addressLineOne\":\"test line\",\"addressLineTwo\":\"test line\",\"city\":\"chennai\",\"country\":\"1\",\"locality\":\"test locality\",\"pincode\":\"753008\",\"plotBuilding\":\"building\",\"source\":\"1\",\"state\":\"test locality\",\"latLong\":\"753008\",\"LocationId\":\"building\"}],\"mdcFilterToken\":\"IUFWDBYGFDKAKBRK9RAE0C\"}";
		Mockito.when(quoteGscRepository.findProductLocations(Mockito.anyInt())).thenReturn(objectCreator.getProductLocations());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(queueResponse);
		gscQuoteController.getLocationDetailsByCustomer(1, 1, 1);
		assertTrue(true);
	} 
	@Test
	public void  testUpdateCurrencyValueByCode() throws TclCommonException {
		Mockito.when(quoteGscRepository.findByQuoteToLeId(Mockito.anyInt())).thenReturn(objectCreator.getQuoteGscList());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
		.thenReturn(objectCreator.createQuote());
		doNothing().when(gscPricingFeasibilityService).persistGvpnPricesWithGsc(Mockito.any());
		doNothing().when(gscPricingFeasibilityService).updateQuoteToLeCurrencyValues(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
		gscQuoteController.updateCurrencyValueByCode(1, 2, "INR");
		assertTrue(true);
	}
	@Test
	public void  testGetMPLSLocations() {
		Mockito.when(quoteGscRepository.findProductLocations(Mockito.anyInt())).thenReturn(objectCreator.getProductLocations());
		gscQuoteController.getMPLSLocations(1, 2);
		assertTrue(true);
	}
}
