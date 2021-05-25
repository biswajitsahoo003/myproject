package com.tcl.dias.oms.webex.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.common.webex.beans.SIExistingGVPNBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.cisco.beans.B2BServiceB2BAcquireQuotePortType;
import com.tcl.dias.oms.cisco.beans.B2BServiceB2BListQuotePortType;
import com.tcl.dias.oms.cisco.beans.CiscoB2BService;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscTfnRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.service.v2.GscQuotePdfService2;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.gsc.util.GscConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.WebexObjectCreator;
import com.tcl.dias.oms.webex.beans.PricingUcaasRequestBean;
import com.tcl.dias.oms.webex.beans.WebexOrderDataBean;
import com.tcl.dias.oms.webex.beans.WebexQuoteDataBean;
import com.tcl.dias.oms.webex.beans.WebexQuotePricingBean;
import com.tcl.dias.oms.webex.beans.WebexSolutionBean;
import com.tcl.dias.oms.webex.common.UcaasOmsSfdcComponent;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;

/**
 * This file contains the testing scenarios of Webex Product.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(properties = { "ccw.url.auth = https://cloudsso.cisco.com/as/token.oauth2",
		"ccw.api.client.id = medn9qfwqa6tnsgqbut5bd3g", "ccw.api.client.secret = cYUk4emPjEwc7aFJREYYagnm",
		"ccw.url.listquote = https://api.cisco.com/commerce/QUOTING/v1/ListQuoteService",
		"ccw.url.acquirequote = https://api.cisco.com/commerce/QUOTING/v1/AcquireQuoteService",
		"ccw.username = ucaasteam62140", "ccw.password = Tata@123",
		"ucaas.pricing.request.url=http://10.133.208.121/new/ucaas/pricing/api", "system.proxy.host=",
		"system.proxy.port=" ,"swift.api.enabled = true"})
public class WebexQuoteControllerTest {

	@Autowired
	WebexObjectCreator objectCreator;

	@Autowired
	WebexQuoteController webexQuoteController;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	QuoteUcaasRepository quoteUcaasRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	QuoteGscRepository quoteGscRepository;

	@MockBean
	ProductSolutionRepository productSolutionRepository;

	@MockBean
	OrderRepository orderRepository;

	@MockBean
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	CustomerRepository customerRepository;

	@MockBean
	UserRepository userRepository;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;

	@MockBean
	OmsSfdcService omsSfdcService;

	@MockBean
	PricingDetailsRepository pricingDetailsRepository;

	@MockBean
	UcaasOmsSfdcComponent ucaasOmsSfdcComponent;

	@MockBean
	B2BServiceB2BListQuotePortType listQuotePort;

	@MockBean
	B2BServiceB2BAcquireQuotePortType acquireQuotePort;

	@MockBean
	CiscoB2BService ciscoB2BService;

	@MockBean
	UserInfoUtils userInfoUtils;

	@MockBean
	DocusignAuditRepository docusignAuditRepository;

	@MockBean
	CofDetailsRepository cofDetailsRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OrderUcaasRepository orderUcaasRepository;

	@MockBean
	OrderGscRepository orderGscRepository;

	@MockBean
	QuoteGscTfnRepository quoteGscTfnRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@MockBean
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@MockBean
	OrderGscTfnRepository orderGscTfnRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	GscQuotePdfService2 gscQuotePdfService2;

	@MockBean
	FileStorageService fileStorageService;

	@MockBean
	GscAttachmentHelper gscAttachmentHelper;

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

	/**
	 * Initialize mock repositories
	 */
	@Before
	public void init() throws TclCommonException {

		// mocking mst repository.
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductFamily());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstOmsAttributeList());
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstProductComponent());

		// mocking customer repository.
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getCustomer());

		// mocking user repository.
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());
		Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerList());
		Mockito.when(userInfoUtils.getUserType()).thenReturn("sales");

		// mocking quote repository.
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.createQuote());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.createQuote()));
		Mockito.when(quoteRepository.save(Mockito.any())).thenReturn(objectCreator.createQuote());

		// mocking quoteLeRepository
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt())).thenReturn(objectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.getQuoteToLeList().stream().findAny());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(objectCreator.createQuoteToLe());

		// mocking order repository.
		Mockito.when(orderRepository.findByQuoteAndStatus(Mockito.any(Quote.class), Mockito.anyByte()))
				.thenReturn(objectCreator.createOrder());

		// mocking product solution repository.
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.createProductSolutionList());
		Mockito.when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.getProductSolution().stream().findFirst());
		Mockito.when(productSolutionRepository.save(Mockito.any())).thenReturn(objectCreator.createProductSolution());
		Mockito.when(productSolutionRepository.findBySolutionCode(Mockito.anyString()))
				.thenReturn(objectCreator.createProductSolutionList());

		// mocking quote gsc repository
		Mockito.when(quoteGscRepository.findByProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.createQuoteGscList());
		Mockito.when(quoteGscRepository.save(Mockito.any())).thenReturn(objectCreator.getQuoteGsc());
		Mockito.when(quoteGscRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getQuoteGsc()));

		// mocking quoteGscDetailsRepository
		Mockito.when(quoteGscDetailsRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteGscDetails().stream().findAny().get());
		Mockito.when(quoteGscDetailsRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getQuoteGscDetails().stream().findFirst());
		Mockito.when(quoteGscTfnRepository.findByQuoteGscDetail(Mockito.any())).thenReturn(objectCreator.getQuoteGscTfnList());

		// mocking quote ucaas repository
		Mockito.when(quoteUcaasRepository.findByQuoteToLeId(Mockito.anyInt()))
				.thenReturn(objectCreator.getQuoteLines());
		Mockito.when(quoteUcaasRepository.save(Mockito.any())).thenReturn(objectCreator.createUcaas());
		Mockito.when(quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyByte())).thenReturn(objectCreator.createUcaas());
		Mockito.when(quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(Mockito.anyInt(),
				Mockito.contains(WebexConstants.CONFIGURATION), Mockito.anyByte()))
				.thenReturn(objectCreator.getQuoteUcaasConfiguration());
		Mockito.when(quoteUcaasRepository.findByQuoteToLeAndIsConfig(Mockito.any(), Mockito.anyByte()))
				.thenReturn(Arrays.asList(objectCreator.getQuoteUcaasConfiguration()));
		Mockito.when(quoteUcaasRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuoteUcaas()));

		// mocking quoteToLeProductFamilyRepository
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.createQuoteToLeProductFamily());

		// mocking quoteLeAttributeValueRepository
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValues());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValue());

		// mocking quoteToLeProductFamilyRepository
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe_Id(Mockito.anyInt()))
				.thenReturn(objectCreator.createQuoteToLeProductFamily());

		// mocking quoteProductComponentRepository
		Mockito.when(quoteProductComponentRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getProductComponent());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.createQuoteProductComponents());

		// mocking queue calls
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any())).thenReturn("");

		// mocking pricing api response
		Mockito.when(pricingDetailsRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createPricingDetailsResponse());

		// mocking sfdc component
		Mockito.when(ucaasOmsSfdcComponent.getOmsSfdcService()).thenReturn(omsSfdcService);
		doNothing().when(omsSfdcService).processCreateOpty(Mockito.any(QuoteToLe.class), Mockito.anyString());

		//Mocking orderLeAttributeValue Repository
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderLeAttributevalue());

		//Mocking orderproductsolution repository
		Mockito.when(orderProductSolutionRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductSolution());

		//Mocking order ucaas
		Mockito.when(orderUcaasRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderUcaas());

		// Mocking order Gsc
		Mockito.when(orderGscRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderGsc());

		//Mocking orderproductcomponent Repository
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.anyInt(),Mockito.any())).thenReturn(objectCreator.getOrderProductComponentList());

		//Mocking mstordersitestage repository
		Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderSiteStage());

		//Mocking mstordersitestatus repository
		Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderSiteStatus());
	}

	/**
	 * Test create quote
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testCreateQuoteForSharedPayPerUse() throws TclCommonException {
		//Test create quote for shared pay per use model
		WebexQuoteDataBean webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/create quote/Shared_PPU_PSTN_TDI.json",
				WebexQuoteDataBean.class);
		ResponseResource<WebexQuoteDataBean> response = webexQuoteController.createQuote(webexQuoteDataBean);
		assertTrue(!response.getData().getSolutions().isEmpty());

		//Test create quote for Shared Pay Per Seat - Toll Dial-In + Bridge Country Dial Out model
		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/create quote/Shared_PPS_PSTN_TDIBDO.json",
				WebexQuoteDataBean.class);
		response = webexQuoteController.createQuote(webexQuoteDataBean);
		assertTrue(response.getData().getSolutions().size() == 2);

		//Test create quote for Dedicated Pay Per Seat - Toll Dial-In
		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/create quote/Dedicated_PPS_PSTN_TDI.json",
				WebexQuoteDataBean.class);
		response = webexQuoteController.createQuote(webexQuoteDataBean);
		assertTrue(response.getData().getSolutions().size() == 1);

		// Test create quote for Dedicated Pay Per Use LNS/ITFS/GO
		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/create quote/Dedicated_PPU_PSTN_TDITDO.json",
				WebexQuoteDataBean.class);
		response = webexQuoteController.createQuote(webexQuoteDataBean);
		assertTrue(response.getData().getSolutions().size() == 3);

		//Test create quote for Dedicated Pay Per Seat - CUG required true
		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/create quote/Dedicated_PPS_TDI_MPLS_New.json",
				WebexQuoteDataBean.class);
		response = webexQuoteController.createQuote(webexQuoteDataBean);
		assertTrue(response.getData().getCugRequired() == true);

		//Test create quote for Dedicated Pay Per Seat TDI MPLS
		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/create quote/Dedicated_PPS_TDITDO_MPLS_New.json",
				WebexQuoteDataBean.class);
		response = webexQuoteController.createQuote(webexQuoteDataBean);
		assertTrue(response.getData().getCugRequired()==true);

		//Test create quote for Dedicated Pay Per Use MPLS
		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/create quote/Dedicated_PPU_TDIBDO_MPLS_New.json",
				WebexQuoteDataBean.class);
		response = webexQuoteController.createQuote(webexQuoteDataBean);
		assertTrue(response.getData().getSolutions().size() == 3);
	}

	/**
	 * Test update quote (Access type - PSTN -> MPLS)
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuote() throws TclCommonException {

		// Test update Quote access type change.
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.getGscProductSolution());
		doNothing().when(omsSfdcService).processProductServiceForSolution(Mockito.any(QuoteToLe.class),
				Mockito.any(ProductSolution.class), Mockito.anyString());
		WebexQuoteDataBean webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/update_quote_001.json",
				WebexQuoteDataBean.class);
		ResponseResource<WebexQuoteDataBean> response = webexQuoteController.updateQuote(webexQuoteDataBean);
		assertEquals(WebexConstants.DEDICATED, response.getData().getAudioGreeting());
		assertEquals("MPLS", response.getData().getAccessType());

		// Test update quote for shared pay per seat to dedicated pay per seat.
		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/update_quote_002.json",
				WebexQuoteDataBean.class);
		response = webexQuoteController.updateQuote(webexQuoteDataBean); 
		assertEquals(WebexConstants.DEDICATED, response.getData().getAudioGreeting());

		// Test update quote for shared pay per seat to dedicated pay per use, toll dial-in to toll dial in + bridge ctry dial-out.
		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/update_quote_003.json",
				WebexQuoteDataBean.class);
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any())).thenReturn(objectCreator.getQuoteToLeProductFamilyList());
		response = webexQuoteController.updateQuote(webexQuoteDataBean);
		assertEquals(WebexConstants.TOLL_DIAL_IN_BRIDGE_DIAL_OUT, response.getData().getAudioType());

		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/update_quote_004.json",
				WebexQuoteDataBean.class);
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any())).thenReturn(objectCreator.getQuoteToLeProductFamilyList());
		response = webexQuoteController.updateQuote(webexQuoteDataBean);

		webexQuoteDataBean = fromJsonFile("com/tcl/dias/oms/webex/controller/update_quote_005.json",
				WebexQuoteDataBean.class);
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any())).thenReturn(objectCreator.getQuoteToLeProductFamilyList());
		response = webexQuoteController.updateQuote(webexQuoteDataBean);

		webexQuoteDataBean.setPaymentModel(WebexConstants.PAYPER_SEAT);
		webexQuoteDataBean.setAudioType(WebexConstants.TOLL_DIAL_IN_BRIDGE_DIAL_OUT);
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any())).thenReturn(objectCreator.getQuoteToLeProductFamilyList());
		response = webexQuoteController.updateQuote(webexQuoteDataBean);

		webexQuoteDataBean.setPaymentModel(WebexConstants.PAYPER_SEAT);
		webexQuoteDataBean.setCugRequired(true);
		webexQuoteDataBean.setAudioType(WebexConstants.TOLL_DIAL_IN_BRIDGE_DIAL_OUT);
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any())).thenReturn(objectCreator.getQuoteToLeProductFamilyList());
		response = webexQuoteController.updateQuote(webexQuoteDataBean);

		webexQuoteDataBean.setPaymentModel(WebexConstants.PAYPER_USE);
		webexQuoteDataBean.setCugRequired(false);
		webexQuoteDataBean.setAudioType(null);
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any())).thenReturn(objectCreator.getQuoteToLeProductFamilyList());
		response = webexQuoteController.updateQuote(webexQuoteDataBean);

	}

	/**
	 * test for get quote api
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetQuote() throws TclCommonException {
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(Mockito.any()))
				.thenReturn(objectCreator.createQuoteProductComponentsAttributeValuesWithDiffAttr());
		Mockito.when(quoteGscDetailsRepository.findByQuoteGsc(Mockito.any()))
				.thenReturn(objectCreator.getQuoteGscDetails().stream().collect(Collectors.toList()));
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.getGVPNRequestBean()));
		ResponseResource<WebexQuoteDataBean> response = webexQuoteController.getWebexQuoteById(1);
		Assert.assertEquals((long) response.getData().getQuoteId(), 1);
	}

	/**
	 * test for get bom details (positive)
	 *
	 * @throws TclCommonException
	 * @throws JAXBException
	 */
	@Test
	public void testPositiveGetBomDetails()
			throws TclCommonException, JAXBException, InterruptedException, XMLStreamException {
		Mockito.doNothing().when(quoteUcaasRepository).delete(Mockito.any(QuoteUcaas.class));
		Mockito.when(quoteUcaasRepository.save(Mockito.any(QuoteUcaas.class)))
				.thenReturn(objectCreator.getQuoteUcaas());
		Mockito.when(quoteUcaasRepository.findByQuoteToLeAndIsConfig(Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.getQuoteLines());
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("rabbitmq.product.webex.sku.queue"), Mockito.anyString())).thenReturn(Utils.convertObjectToJson(objectCreator.getSkuDetailsResponse()));
		ResponseResource<WebexSolutionBean> response = webexQuoteController.getBomDetails(30132, 40129, "45884425");
		Assert.assertTrue(response.getData().getUcaasQuotes().size() > 0);

		ResponseResource<WebexSolutionBean> response1 = webexQuoteController.getBomDetails(30132, 40129, "47637366");
		Assert.assertEquals(response1.getData().getMessage(),
				String.format(WebexConstants.CLOUD_MEETING_LICENSE_ABSENT_MESSAGE, "47637366"));

		ResponseResource<WebexSolutionBean> response2 = webexQuoteController.getBomDetails(30132, 40129, "45267622");
		Assert.assertEquals(response2.getData().getMessage(), String.format(WebexConstants.CONTRACT_PERIOD_MISMATCH_MESSAGE, response2.getData().getContractPeriod()));
	}

	/**
	 * test for get bom details (negative)
	 *
	 * @throws TclCommonException
	 * @throws JAXBException
	 */
	@Test
	public void testNegativeGetBomDetails() throws TclCommonException, JAXBException, InterruptedException {
		doNothing().when(quoteUcaasRepository).delete(Mockito.any(QuoteUcaas.class));
		Mockito.when(quoteUcaasRepository.save(Mockito.any(QuoteUcaas.class)))
				.thenReturn(objectCreator.getQuoteUcaas());
		ResponseResource<WebexSolutionBean> response = webexQuoteController.getBomDetails(30132, 40129, "0000000");
		Assert.assertEquals(Status.FAILURE.toString(), response.getData().getStatus());
	}

	/**
	 * test for approve quotes
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testApproveQuotes() throws TclCommonException {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributesWithDiffMstAttribute());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
		.thenReturn(objectCreator.createProductSolutionWithGscAndWebex());
		Mockito.when(quoteGscDetailsRepository.findByQuoteGsc(Mockito.any()))
				.thenReturn(objectCreator.createQuoteGscDetailsList());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),
				Mockito.anyString())).thenReturn(objectCreator.createQuoteProductComponents());
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(Mockito.anyInt()))
				.thenReturn(objectCreator.createQuoteProductComponentsAttributeValue());
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any())).thenReturn(objectCreator.createOrderToLe());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(null);
		Mockito.when(orderToLeProductFamilyRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderToLeProductFamily());
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLe());
		Mockito.when(orderProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponent());
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponentsAttributeValue());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.any())).thenReturn(objectCreator.getCofDetails());
		Mockito.when(fileStorageService.uploadGscObject(Mockito.anyString(), Mockito.any(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getStoredObject());
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		doNothing().when(omsSfdcService).processUpdateOpportunity(Mockito.any(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		ResponseResource<WebexOrderDataBean> response = webexQuoteController.approveQuotes(1234,
				GscConstants.ACTION_APPROVE, mockHttpServletResponse);
		Assert.assertEquals("UCWB2910195VXKJH", response.getData().getOrderCode());

		Mockito.when(orderProductSolutionRepository.save(Mockito.any())).thenReturn(objectCreator.getWebexOrderProductSolution());
		response = webexQuoteController.approveQuotes(1234,
				GscConstants.ACTION_APPROVE, mockHttpServletResponse);
		Assert.assertEquals("UCWB2910195VXKJH", response.getData().getOrderCode());
	}

	/**
	 * test for generate quote pdf
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGenerateQuotePdf() throws TclCommonException {
		Mockito.when(quoteGscDetailsRepository.findByQuoteGsc(Mockito.any()))
				.thenReturn(objectCreator.createQuoteGscDetailsList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValues());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any())).thenReturn("");
		Mockito.when(authTokenDetailRepository.find(Mockito.any())).thenReturn(objectCreator.getUserInfo());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.createProductSolutionWithGscAndWebex());
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("rabbitmq.gsc.outbound.pricing.queue"), Mockito.anyString())).thenReturn(Utils.convertObjectToJson(objectCreator.getGscOutboundpriceBean()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(Mockito.any())).thenReturn(objectCreator.createQuoteProductComponentsAttributeValuesWithDiffAttr());
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		ResponseResource<?> response = webexQuoteController.generateQuotePdf(1234, mockHttpServletResponse);
		Assert.assertEquals(Status.SUCCESS, response.getStatus());
	}

	/**
	 * Test for getting servicde inventory details from gvpn
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testPositiveGetServiceInventoryDetailsGVPN() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.getGVPNRequestBean()));
		ResponseResource<SIExistingGVPNBean> response = webexQuoteController.getServiceInventoryDetailsGVPN("754", 1,
				10);
		Assert.assertEquals(WebexConstants.INVENTORY_RECEIVED_MESSAGE, response.getData().getMessage());
	}

	/**
	 * Test for getting service inventory details from gvpn (negative)
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeGetServiceInventoryDetailsGVPN() throws TclCommonException {
		ResponseResource<SIExistingGVPNBean> response = webexQuoteController.getServiceInventoryDetailsGVPN("223", 1,
				10);
		Assert.assertEquals(WebexConstants.INVENTORY_NOT_AVAILABLE_MESSAGE, response.getData().getMessage());
	}

	/**
	 * Test for searching service inventory details from gvpn (positive)
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testPositiveGetSIDetailGVPNBySearchCriteria() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.getGVPNRequestBean()));
		ResponseResource<SIExistingGVPNBean> response = webexQuoteController.getSIDetailGVPNBySearchCriteria("754", 1,
				10, "Mumbai", null, null);
		Assert.assertEquals(WebexConstants.INVENTORY_RECEIVED_MESSAGE, response.getData().getMessage());
	}

	/**
	 * Test for getting service inventory details from gvpn (negative)
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testNegativeGetSIDetailGVPNBySearchCriteria() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any())).thenReturn("");
		ResponseResource<SIExistingGVPNBean> response = webexQuoteController.getSIDetailGVPNBySearchCriteria("754", 1,
				10, "Chennai", null, null);
		Assert.assertEquals(WebexConstants.INVENTORY_NOT_AVAILABLE_MESSAGE, response.getData().getMessage());
	}

	/**
	 * Test for download service inventory details
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetProductFamilyDetails() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any()))
				.thenReturn(Utils.convertObjectToJson(objectCreator.getGVPNRequestBean()));
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		ResponseResource<HttpServletResponse> response = webexQuoteController.getProductFamilyDetails("754",
				httpServletResponse);
		if (Objects.nonNull(response.getData()))
			Assert.assertEquals(response.getStatus(), Status.SUCCESS);
		else
			Assert.assertEquals(response.getStatus(), Status.FAILURE);
	}

	/**
	 * Test for share quote via email
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testShareQuoteViaEmail() throws TclCommonException {
		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		ResponseResource<ServiceResponse> resource = webexQuoteController.shareQuote(30075,
				"ssyed.ali@tatacommunications.com", mockHttpServletResponse);
		assertTrue(resource.getData().getFileName().equalsIgnoreCase("Quote_30075.pdf"));
	}

	/**
	 * Test for webex pricing feasbility
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testWebexPricing() throws TclCommonException {
		PricingUcaasRequestBean requestBean = fromJsonFile("com/tcl/dias/oms/webex/controller/pricing_request.json",
				PricingUcaasRequestBean.class);
		Mockito.when(quoteUcaasRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuoteUcaasWithResponse()));
		ResponseResource<WebexQuotePricingBean> response = webexQuoteController.triggerPricing(requestBean);
		assertTrue(response.getData().getTotalTcv().doubleValue() > 0);
	}

	/**
	 * Test for checking process docusign
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testProcessDocusign() throws TclCommonException {
		Mockito.when(docusignAuditRepository.findByOrderRefUuid(Mockito.any())).thenReturn(null);
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		webexQuoteController.processDocuSign(123, 12, "syed", "test@gmail.com", mockHttpServletResponse, null);
	}
}
