package com.tcl.dias.oms.gsc.controller.v2;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteGsc;
import com.tcl.dias.oms.entity.entities.QuoteGscDetail;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
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
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.beans.GscContactAttributeInfo;
import com.tcl.dias.oms.gsc.beans.GscDocumentBean;
import com.tcl.dias.oms.gsc.beans.GscProductComponentBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteAttributesBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.service.v1.GscPricingFeasibilityService;
import com.tcl.dias.oms.pdf.service.GscQuotePdfService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.dias.oms.utils.WebexObjectCreator;
import com.tcl.dias.oms.webex.beans.DeleteConfigurationBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains testing scenarios of GscQuoteController2.
 *
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GscQuoteController2Test {

	@Autowired
	GscObjectCreator objectCreator;

	/*
	 * @Autowired ObjectCreator omsObjectCreator;
	 */

	@Autowired
	GscQuoteController2 gscQuoteController2;

	@Autowired
	WebexObjectCreator webexObjectCreator;

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

	@MockBean
	QuoteUcaasRepository quoteUcaasRepository;

	private <T> T fromJsonFile(String jsonFilePath, TypeReference<List<GscQuoteConfigurationBean>> clazz) {
		URL url = Resources.getResource(jsonFilePath);
		CharSource charSource = Resources.asCharSource(url, Charsets.UTF_8);
		try {
			return new ObjectMapper().readValue(charSource.openStream(), clazz);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return null;
	}

	/**
	 * init- predefined mocks
	 *
	 * @throws TclCommonException
	 */
	@Before
	public void init() throws TclCommonException {

		// mocking mst repository
		Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getMstProductComponent()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstOmsAttributeList());
		Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStage());
		Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStatus());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductComponentList());

		// mocking user information
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerList());
		Mockito.when(userInfoUtils.getUserType()).thenReturn("sales");

		// mocking user repository
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());

		// mocking quote repository
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.createOptionalQuote());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.createQuote());

		// mocking quote to le repository
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getPricingQuoteLe()));
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(objectCreator.createQuoteToLe());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createQuoteToLe()));

		// mocking quote to le product family repository
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteProductFamilies());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeFamily());

		// mocking quote to le attribute value repository
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValue());
		when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(webexObjectCreator.getQuoteLeAttributeValues());

		// mocking quote gsc repository
		Mockito.when(quoteGscRepository.findByProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.createQuoteGscList());
		Mockito.when(quoteGscRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteGsc()));
		Mockito.when(quoteGscRepository.save(Mockito.any())).thenReturn(new QuoteGsc());
		Mockito.when(quoteGscRepository.findByQuoteToLe(Mockito.any())).thenReturn(objectCreator.createQuoteGscList());

		// mcking quote gsc details repository
		Mockito.when(quoteGscDetailsRepository.findByQuoteGsc(Mockito.any()))
				.thenReturn(objectCreator.getQuoteGscDetailSet());
		Mockito.when(quoteGscDetailsRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteGscDetail()));
		Mockito.when(quoteGscDetailsRepository.save(Mockito.any())).thenReturn(new QuoteGscDetail());

		// mocking quote price repository
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getQuotePrice());
		Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(new QuotePrice());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt())).thenReturn(Arrays.asList(new QuotePrice()));
		Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(objectCreator.getQuotePrice());

		// mocking quote product component repository
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),
				Mockito.anyString())).thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteProductComponent().get(0)));

		// mocking quote product component attribute value repository
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getQuoteProductComponentAttributeValues());
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(Mockito.anyInt()))
				.thenReturn(objectCreator.getListOfQuoteProductComponentsAttributeValue());
		Mockito.when(quoteProductComponentsAttributeValueRepository.findAllById(Mockito.anyIterable()))
				.thenReturn(objectCreator.getListOfQuoteProductComponentsAttributeValue());
		doNothing().when(quoteProductComponentsAttributeValueRepository).deleteAll(Mockito.anyIterable());
		Mockito.when(quoteProductComponentsAttributeValueRepository.saveAll(Mockito.anyIterable()))
				.thenReturn(objectCreator.getListOfQuoteProductComponentsAttributeValue());

		// mocking order repository
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(new Order());

		// mocking order gsc repository
		Mockito.when(orderGscRepository.save(Mockito.any())).thenReturn(objectCreator.getordergsc());

		// mocking order gsc details repository
		Mockito.when(orderGscDetailsRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderGscDetail());

		// mocking order le repository
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(new OrderToLe());

		// mocking order to le product family repository
		Mockito.when(orderToLeProductFamilyRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductFamily());

		// order le attribute value repository
		Mockito.when(orderLeAttributeValueRepository.save(Mockito.any())).thenReturn(new OrdersLeAttributeValue());

		// order product solution repository
		Mockito.when(orderProductSolutionRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductSolution());

		// mocking order product component repository
		Mockito.when(orderProductComponentRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponent());

		// mocking order product component attribute value repository
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createOrderAttributes());

		// mocking product attribute master repository
		Mockito.when(productAttributeMasterRepository.findByNameIn(Mockito.any()))
				.thenReturn(objectCreator.getProductAttributeMasterList());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getProductAtrributeMaster());

		// mocking product solution repository
		Mockito.when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createProductSolutions()));

		// mocking illSite repository
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(objectCreator.getIllsite());

		// mocking rest calls
		Mockito.when(restClientService.post(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getRestResponse());

		// mocking RabbitMQ Queues
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any())).thenReturn("");
	}

	@Test
	public void testGetQuoteConfigurations() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getCustomerDetailsBean());
		ResponseResource<List<GscQuoteConfigurationBean>> response = gscQuoteController2.getQuoteConfigurations(1000,
				1000, 1000, false);
		assertEquals(Status.SUCCESS, response.getStatus());
	}

	@Test
	public void testCreateQuoteConfigurations() throws TclCommonException {
		Set<QuoteProductComponentsAttributeValue> temp = new HashSet<>();
		temp.add(webexObjectCreator.getQuoteProductComponentsAttributeValue());
		QuoteProductComponent quoteProductComponent = objectCreator.getQuoteProductComponent().get(0);
		quoteProductComponent.setQuoteProductComponentsAttributeValues(temp);
		Mockito.when(quoteProductComponentRepository.save(Mockito.any(QuoteProductComponent.class)))
				.thenReturn(quoteProductComponent);
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getCustomerDetailsBean());
		Mockito.when(quoteGscDetailsRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteGscDetail());
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(webexObjectCreator.getQuoteProductComponentsAttributeValue());
		List<GscQuoteConfigurationBean> quoteDataBean = fromJsonFile(
				"com/tcl/dias/oms/gsc/controller/create_configuration_001.json",
				new TypeReference<List<GscQuoteConfigurationBean>>() {
				});
		ResponseResource<List<GscQuoteConfigurationBean>> response = gscQuoteController2.createConfiguration(1000, 1000,
				1000, quoteDataBean);
		assertEquals(Status.SUCCESS, response.getStatus());
	}

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

	@Test
	public void testUpdateQuoteToLeStatus() throws TclCommonException {
		ResponseResource<GscQuoteToLeBean> response = gscQuoteController2.updateQuoteToLeStatus(4321, 1234,
				String.valueOf(QuoteStageConstants.CHECKOUT));
		Assert.assertEquals(String.valueOf(QuoteStageConstants.CHECKOUT), response.getData().getStage());
	}

	@Test
	public void testUpdateOrDeleteProductComponentAttributes() throws TclCommonException {
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteComponentsAttributeValues().stream().findAny().get());
		ResponseResource<List<GscProductComponentBean>> response = gscQuoteController2
				.updateOrDeleteProductComponentAttributes(1234, 3214, 4321, 2134,
						objectCreator.getGscProductComponentBeanList().getData());
		Assert.assertEquals("CPE",
				response.getData().get(0).getAttributes().stream().findAny().get().getAttributeName());
	}

	@Test
	public void testUpdateTermsInMonths() throws TclCommonException {
		ResponseResource<GscQuoteToLeBean> response = gscQuoteController2.updateTermsInMonths(1, "36 months", "update");
		Assert.assertEquals("36 months", response.getData().getTermsInMonths());
	}

	@Test
	public void testUpdateCurrencyValueByCode() throws TclCommonException {
		ResponseResource<String> response = gscQuoteController2.updateCurrencyValueByCode(1, 1000, "INR");
		Assert.assertEquals("INR", response.getData());
	}

	@Test
	public void testUpdateCurrencyValueByCode1() throws TclCommonException {
		ResponseResource<String> response = gscQuoteController2.updateCurrencyValueByCode(1, 1000, "USD");
		Assert.assertEquals("USD", response.getData());
	}

	@Test
	public void testGetProductComponentAttributes() throws TclCommonException {
		ResponseResource<List<GscProductComponentBean>> response = gscQuoteController2
				.getProductComponentAttributes(1234, 4321, 2134);
		Assert.assertTrue(Objects.nonNull(response.getData()));
	}

	@Test
	public void testGetQuoteComponentAttributes() throws TclCommonException {
		ResponseResource<GscQuoteAttributesBean> response = gscQuoteController2.getQuoteComponentAttributes(1234);
		Assert.assertTrue(Objects.nonNull(response.getData().getAttributes()));
	}

	@Test
	public void testGetQuoteLeAttributes() throws TclCommonException {
		ResponseResource<GscQuoteAttributesBean> response = gscQuoteController2.getQuoteLeAttributes(1, 1000);
		Assert.assertTrue(Objects.nonNull(response.getData().getAttributes()));
	}

	@Test
	public void testSaveQuoteLeAttributes() throws TclCommonException {
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteComponentsAttributeValues().stream().findAny().get());
		GscQuoteAttributesBean bean = fromJsonFile(
				"com/tcl/dias/oms/webex/controller/create_quote_le_attributes_001.json", GscQuoteAttributesBean.class);
		ResponseResource<GscQuoteAttributesBean> response = gscQuoteController2.saveQuoteLeAttributes(1, 1000, bean);
		Assert.assertTrue(Objects.nonNull(response.getData().getAttributes()));
	}

	@Test
	public void testGetContactAttributeDetails() throws TclCommonException {
		ResponseResource<GscContactAttributeInfo> response = gscQuoteController2.getContactAttributeDetails(1, 1000);
		Assert.assertTrue(Objects.nonNull(response.getData()));
	}

	@Test
	public void testDeleteConfigurations() throws TclCommonException {
		Mockito.when(quoteUcaasRepository.findByQuoteToLeId(1234)).thenReturn(objectCreator.getUcaasQuotes());
		DeleteConfigurationBean deleteConfigurationBean = objectCreator.createDeleteConfigurationBean();
		ResponseResource<DeleteConfigurationBean> response = gscQuoteController2.deleteConfigurations(1,
				deleteConfigurationBean);
		Assert.assertTrue(response.getStatus().equals(Status.SUCCESS));
	}

	@Test
	public void testCreateGscDocument() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.any(), Mockito.any()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.createCustomerLeDetailsBean()));
		GscDocumentBean documentBean = objectCreator.createDocumentBean();
		ResponseResource<GscDocumentBean> response = gscQuoteController2.createDocument(1, 1234, documentBean);
		Assert.assertEquals((long) 1, (long) response.getData().getQuoteId());
	}
}
