package com.tcl.dias.oms.webex.controller;

import com.google.common.collect.Sets;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.OrderGsc;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderGscDetailRepository;
import com.tcl.dias.oms.entity.repository.OrderGscRepository;
import com.tcl.dias.oms.entity.repository.OrderGscTfnRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrderUcaasRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteGscRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.QuoteUcaasRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gsc.GscTestUtil;
import com.tcl.dias.oms.gsc.pdf.beans.GscOrderAttributesBean;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.isv.controller.v1.InternalStakeViewController;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.dias.oms.utils.WebexObjectCreator;
import com.tcl.dias.oms.webex.beans.WebexLicenseManualPricingBean;
import com.tcl.dias.oms.webex.beans.WebexOrderDataBean;
import com.tcl.dias.oms.webex.beans.WebexQuoteDataBean;
import com.tcl.dias.oms.webex.service.WebexQuotePdfService;
import com.tcl.dias.oms.webex.util.WebexConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * This file contains the InternalStakeViewControllerTest.java class.
 *
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(properties = { "swift.api.enabled = true", "temp.download.url.expiryWindow = 1",
		"temp.upload.url.expiryWindow = 5" })
public class UcaasInternalStakeViewControllerTest {

	@Autowired
	InternalStakeViewController internalStakeViewController;

	@MockBean
	OrderRepository orderRepository;

	@Autowired
	ObjectCreator objectCreator;

	@Autowired
	GscObjectCreator gscObjectCreator;

	@MockBean
	UserRepository userRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	OrderPriceRepository orderPriceRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@MockBean
	PricingDetailsRepository pricingDetailsRepository;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	ProductSolutionRepository productSolutionRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	IllSiteRepository illSiteRepository;

	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	QuotePriceRepository quotePriceRepository;

	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@MockBean
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@MockBean
	OrderGscDetailRepository orderGscDetailRepository;

	@MockBean
	QuoteGscRepository quoteGscRepository;

	@MockBean
	QuoteGscDetailsRepository quoteGscDetailsRepository;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	QuoteUcaasRepository quoteUcaasRepository;

	@Autowired
	WebexObjectCreator webexObjectCreator;

	@MockBean
	OrderUcaasRepository orderUcaasRepository;

	@MockBean
	OmsSfdcService omsSfdcService;

	@MockBean
	OrderGscRepository orderGscRepository;

	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	OrderGscTfnRepository orderGscTfnRepository;

	@MockBean
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	CustomerRepository customerRepository;

	@MockBean
	UserInfoUtils userInfoUtils;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	CofDetailsRepository cofDetailsRepository;
	
	@MockBean
	PartnerCustomerDetailsService partnerCustomerDetailsService;

	@MockBean
	OmsAttachmentRepository omsAttachmentRepository;
	
	@MockBean
	FileStorageService fileStorageService;

	@MockBean
	GscAttachmentHelper gscAttachmentHelper;

	@MockBean
	ByteArrayOutputStream outByteStream;


	@org.junit.Before
	public void init() {
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		Mockito.when(quoteRepository.findTop100ByOrderByCreatedTimeDesc()).thenReturn(objectCreator.getQuoteList());
		Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn((Optional.of(objectCreator.getUser())));
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getQuote());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(webexObjectCreator.getQuoteLeAttributesWithDiffMstAttribute());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.getSolutionList());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(objectCreator.getQuoteToLeFamilyList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(objectCreator.getQuoteToLeList());

		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(objectCreator.getquoteProductComponentAttributeValues());

		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getquoteProductComponentAttributeValues());

		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.geQuotePrice());

		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(Mockito.anyInt(),
				Mockito.anyString())).thenReturn(objectCreator.getQuoteProductComponent());

		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),
				Mockito.anyString())).thenReturn(objectCreator.getQuoteProductComponent());

		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());

		Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStatus());
		Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStage());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(gscObjectCreator.createQuoteToLe()));
		Mockito.when(quoteGscRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(gscObjectCreator.getQuoteGsc()));
		Mockito.when(quoteGscDetailsRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(gscObjectCreator.quotegscdetail));
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(gscObjectCreator.createQuote());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(gscObjectCreator.createQuoteToLe());
		Mockito.when(quoteGscRepository.save(Mockito.any())).thenReturn(gscObjectCreator.getQuoteGsc());

		// mocking quote ucaas repository
		Mockito.when(quoteUcaasRepository.findByQuoteToLeId(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.getQuoteLines());
		Mockito.when(quoteUcaasRepository.save(Mockito.any())).thenReturn(webexObjectCreator.createUcaas());
		Mockito.when(quoteUcaasRepository.findByQuoteToLeIdAndNameAndStatus(Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyByte())).thenReturn(webexObjectCreator.createUcaas());
		Mockito.when(quoteUcaasRepository.findByQuoteToLeAndIsConfig(Mockito.any(), Mockito.anyByte()))
				.thenReturn(Collections.singletonList(webexObjectCreator.getQuoteUcaasConfiguration()));

		// mocking order ucaas repository
		Mockito.when(orderUcaasRepository.save(Mockito.any())).thenReturn(webexObjectCreator.createUcaas());
		Mockito.when(orderUcaasRepository.findByOrderToLeId(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.getOrderLines());
		Mockito.when(orderUcaasRepository.findByOrderToLeIdAndNameAndStatus(Mockito.anyInt(), Mockito.any(),
				Mockito.anyByte())).thenReturn(webexObjectCreator.getOrderUcaasConfiguration());

		// mocking customer repository.
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getCustomer());

		// mocking user repository.
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getUser());
		Mockito.when(userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerList());
		Mockito.when(userInfoUtils.getUserType()).thenReturn("sales");

		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.any())).thenReturn(objectCreator.getCofDetails());

	}

	/**
	 * test for get webex quote details
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetWebexQuoteDetails() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.getQuoteToLeList().stream().findAny());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getMstProductFamily());
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn(webexObjectCreator.getMstProductComponent());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(webexObjectCreator.createQuoteToLeProductFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(webexObjectCreator.createProductSolutionList());
		ResponseResource<WebexQuoteDataBean> response = internalStakeViewController.getWebexQuoteById(1234);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test for get webex order details
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetWebexOrderDetails() throws TclCommonException {
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any())).thenReturn(webexObjectCreator.createOrderToLe());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.createOptionalOrderToLe());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(webexObjectCreator.getOrderToLeProductFamilies().stream().findAny().get());
		when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		when(orderProductSolutionRepository.findById(Mockito.any()))
				.thenReturn(Optional.of(webexObjectCreator.getOrderProductSolution()));
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(webexObjectCreator.createOrdersLeAttributeValues());
		when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()))
				.thenReturn(webexObjectCreator.getOrderProductSolutionsList());
		ResponseResource<WebexOrderDataBean> response = internalStakeViewController.getWebexOrderById(1234);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	/**
	 *
	 * Test for updating license components.
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLicenseComponentPrices() throws TclCommonException {
		Mockito.when(quoteUcaasRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(webexObjectCreator.createUcaas()));
		Mockito.when(quoteUcaasRepository.findByQuoteToLeIdAndIsConfig(Mockito.any(), Mockito.anyByte()))
				.thenReturn(Collections.singletonList(webexObjectCreator.createUcaas()));
		WebexLicenseManualPricingBean request = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/webex/controller/isv_update_price_request.json", WebexLicenseManualPricingBean.class);
		ResponseResource<WebexLicenseManualPricingBean> response = internalStakeViewController
				.updateLicenseComponentPrices(request, false);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

		// Updating skuID
		response = internalStakeViewController.updateLicenseComponentPrices(request, true);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * Test for approve manual quotes.
	 */
	@Test
	public void testApproveManualQuote() throws TclCommonException {
		MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
		Mockito.when(orderRepository.save(Mockito.any())).thenReturn(webexObjectCreator.getOrders());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(webexObjectCreator.getQuoteToLeList());
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(webexObjectCreator.getOrderToLe());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(webexObjectCreator.getQuoteLeAttributeValues());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(webexObjectCreator.createQuoteToLeProductFamily());
		Mockito.when(orderToLeProductFamilyRepository.save(Mockito.any()))
				.thenReturn(webexObjectCreator.getOrderToLeProductFamily());
		doNothing().when(omsSfdcService).processUpdateOpportunity(Mockito.any(), Mockito.anyString(),
				Mockito.anyString(), Mockito.any());
		Mockito.when(orderProductSolutionRepository.save(Mockito.any()))
				.thenReturn(webexObjectCreator.getOrderProductSolution());
		Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(objectCreator.getUser()));
		ResponseResource<WebexOrderDataBean> response = internalStakeViewController.approveManualQuotesForUcaas(1234, 1,
				mockHttpServletRequest);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * Test for saving order to le attributes
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testSaveWebexOrderLeAttributes() throws TclCommonException {
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getOrders());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(webexObjectCreator.getOrderToLe()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getMstOmsAttributeList());
		Mockito.when(ordersLeAttributeValueRepository
				.findByMstOmsAttributeAndOrderToLe(Mockito.any(MstOmsAttribute.class), Mockito.any(OrderToLe.class)))
				.thenReturn(Sets.newHashSet(webexObjectCreator.getOrderLeAttributeValue()));
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any(OrdersLeAttributeValue.class)))
				.thenReturn(objectCreator.getOrdersLeAttributeValue());
		GscOrderAttributesBean request = GscTestUtil.fromJsonFile(
				"com/tcl/dias/oms/webex/controller/isv_legalentity_attributes.json", GscOrderAttributesBean.class);
		ResponseResource<GscOrderAttributesBean> response = internalStakeViewController.saveWebexOrderLeAttributes(1, 2,
				request);
		assertTrue(response.getData().getAttributes().size() > 0);
	}

	/**
	 * Unit testing for get ucaas details excel
	 *
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@Test
	public void testGetUCaaSOrderDetailsExcel() throws IOException, TclCommonException {
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getOrders());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getMstProductFamily());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(webexObjectCreator.getOrderToLeProductFamily());
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()))
				.thenReturn(webexObjectCreator.getOrderProductSolutionsList());
		Mockito.when(orderGscRepository.findByorderProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
				.thenReturn(Arrays.asList(webexObjectCreator.getOrderGsc()));
		Mockito.when(orderGscDetailRepository.findByorderGsc(Mockito.any(OrderGsc.class)))
				.thenReturn(new ArrayList<>(webexObjectCreator.getOrderGscDetails()));
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn(webexObjectCreator.getMstProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameIn(Mockito.anyList()))
				.thenReturn(Arrays.asList(webexObjectCreator.getProductAttributeMaster()));
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Arrays.asList(webexObjectCreator.getOrderProductComponent()));
		Mockito.when(orderGscTfnRepository.findByOrderGscDetailId(Mockito.anyInt()))
				.thenReturn(Arrays.asList(webexObjectCreator.getOrderGscTfn()));
		Mockito.when(orderIllSitesRepository
				.findByOrderProductSolutionAndStatus(Mockito.any(OrderProductSolution.class), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getOrderIllSiteList());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(OrderToLe.class),
				Mockito.any(MstProductFamily.class))).thenReturn(webexObjectCreator.getOrderToLeProductFamily());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(webexObjectCreator.getOrderPrice());
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
				.thenReturn(new ArrayList<>(webexObjectCreator.getOrderToLeSet()));
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(webexObjectCreator.getOrderToLe()));
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any(OrderToLe.class)))
				.thenReturn(Arrays.asList(webexObjectCreator.getOrderLeAttributeValue()));
		Mockito.when(thirdPartyServiceJobsRepository.findAllByRefIdAndServiceTypeInAndThirdPartySourceIn(
				Mockito.anyString(), Mockito.anyList(), Mockito.anyList()))
				.thenReturn(Arrays.asList(webexObjectCreator.getThirdPartyServiceJob()));
		Mockito.when(orderUcaasRepository.findByOrderToLeIdAndNameAndStatus(Mockito.anyInt(), Mockito.anyString(),
				Mockito.anyByte())).thenReturn(webexObjectCreator.getOrderUcaasConfiguration());
		Mockito.when(orderProductComponentsAttributeValueRepository
				.findByOrderProductComponent(Mockito.any(OrderProductComponent.class)))
				.thenReturn(Arrays.asList(webexObjectCreator.getOrderProductComponentsAttributeValue()));
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn("");
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("rabbitmq.location.address.request"), Mockito.anyString()))
				.thenReturn(objectCreator.getAddressDetail());
		Mockito.when(orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.getOrderProductComponentList());
		List<OrderSiteFeasibility> orderSiteFeasibilityList = webexObjectCreator.getOrderIllSiteFeasiblity();
		orderSiteFeasibilityList.get(0).setFeasibilityType(FPConstants.CUSTOM.toString());
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(Mockito.any(OrderIllSite.class),
				Mockito.anyByte())).thenReturn(orderSiteFeasibilityList);
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(),
				Mockito.any(MstProductComponent.class), Mockito.anyString()))
				.thenReturn(Arrays.asList(webexObjectCreator.getOrderProductComponent()));
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
				Mockito.any(MstProductComponent.class))).thenReturn(webexObjectCreator.getOrderProductComponentList());
//		Mockito.when(gscOrderDetailService.getCityNumberConfiguration())
		ResponseResource<HttpServletResponse> response = internalStakeViewController.getUCaaSOrderDetailsExcel(1,
				httpServletResponse);
		assertTrue(response.getStatus() == Status.FAILURE);

		httpServletResponse = new MockHttpServletResponse();
		orderSiteFeasibilityList.get(0).setFeasibilityType("test");
		orderSiteFeasibilityList.get(0).setFeasibilityMode(WebexConstants.INTL);
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(Mockito.any(OrderIllSite.class),
				Mockito.anyByte())).thenReturn(orderSiteFeasibilityList);

		OrderProductComponent orderProductComponent = webexObjectCreator.getOrderProductComponent();
		orderProductComponent.setMstProductComponent(webexObjectCreator.getMstProductComponent());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(),
				Mockito.any(MstProductComponent.class), Mockito.anyString()))
				.thenReturn(Arrays.asList(orderProductComponent));
		response = internalStakeViewController.getUCaaSOrderDetailsExcel(1, httpServletResponse);
		assertTrue(response.getStatus() == Status.FAILURE);

		httpServletResponse = new MockHttpServletResponse();
		orderSiteFeasibilityList.get(0).setFeasibilityType("test");
		orderSiteFeasibilityList.get(0).setFeasibilityMode(WebexConstants.INTL);
		orderSiteFeasibilityList.get(0).setRank(null);
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(Mockito.any(OrderIllSite.class),
				Mockito.anyByte())).thenReturn(orderSiteFeasibilityList);

		orderProductComponent.setMstProductComponent(webexObjectCreator.getMstProductComponentGO());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(),
				Mockito.any(MstProductComponent.class), Mockito.anyString()))
				.thenReturn(Arrays.asList(orderProductComponent));
		response = internalStakeViewController.getUCaaSOrderDetailsExcel(1, httpServletResponse);
		assertTrue(response.getStatus() == Status.FAILURE);

		orderProductComponent.setMstProductComponent(webexObjectCreator.getMstProductComponentMPLS());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(),
				Mockito.any(MstProductComponent.class), Mockito.anyString()))
				.thenReturn(Arrays.asList(orderProductComponent));
		httpServletResponse = new MockHttpServletResponse();
		orderSiteFeasibilityList.get(0).setFeasibilityType("test");
		orderSiteFeasibilityList.get(0).setFeasibilityMode("");
		orderSiteFeasibilityList.get(0).setRank(1);
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(Mockito.any(OrderIllSite.class),
				Mockito.anyByte())).thenReturn(orderSiteFeasibilityList);
		response = internalStakeViewController.getUCaaSOrderDetailsExcel(1, httpServletResponse);
		assertTrue(response.getStatus() == Status.FAILURE);

		orderProductComponent.setMstProductComponent(webexObjectCreator.getMstProductComponentPSTN());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(),
				Mockito.any(MstProductComponent.class), Mockito.anyString()))
				.thenReturn(Arrays.asList(orderProductComponent));
		httpServletResponse = new MockHttpServletResponse();
		orderSiteFeasibilityList.get(0).setFeasibilityType("test");
		orderSiteFeasibilityList.get(0).setFeasibilityMode("");
		orderSiteFeasibilityList.get(0).setRank(null);
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(Mockito.any(OrderIllSite.class),
				Mockito.anyByte())).thenReturn(orderSiteFeasibilityList);
		response = internalStakeViewController.getUCaaSOrderDetailsExcel(1, httpServletResponse);
		assertTrue(response.getStatus() == Status.FAILURE);

		orderProductComponent.setMstProductComponent(webexObjectCreator.getMstProductComponentCPE());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(),
				Mockito.any(MstProductComponent.class), Mockito.anyString()))
				.thenReturn(Arrays.asList(orderProductComponent));
		httpServletResponse = new MockHttpServletResponse();
		orderSiteFeasibilityList.get(0).setFeasibilityType("test");
		orderSiteFeasibilityList.get(0).setFeasibilityMode("");
		orderSiteFeasibilityList.get(0).setRank(null);
		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(Mockito.any(OrderIllSite.class),
				Mockito.anyByte())).thenReturn(orderSiteFeasibilityList);
		response = internalStakeViewController.getUCaaSOrderDetailsExcel(1, httpServletResponse);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * test for upload webex cof pdf.
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUploadWebexCofPdf() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.getQuoteToLeList());
		Mockito.when(orderRepository.findByQuoteAndStatus(Mockito.any(Quote.class), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getOrders());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString()))
				.thenReturn(webexObjectCreator.getCofDetails());
		Mockito.when(omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(Mockito.anyString(),
				Mockito.anyInt(), Mockito.anyString())).thenReturn(webexObjectCreator.getOmsAttachments());
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("attachment.requestId.queue"), Mockito.anyString()))
				.thenReturn(Utils.convertObjectToJson(webexObjectCreator.getAttachmentBean()));
		Mockito.when(quoteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(webexObjectCreator.createQuote()));
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test", null, "test".getBytes());
		ResponseResource<TempUploadUrlInfo> response = internalStakeViewController.uploadWebexCofPdf(31933,
				mockMultipartFile);
		assertTrue(Status.SUCCESS == response.getStatus());

		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.any())).thenReturn(null);
		response = internalStakeViewController.uploadWebexCofPdf(31933, mockMultipartFile);
		assertTrue(Status.SUCCESS == response.getStatus());
	}

	@Test
	public void testGenerateWebexSignedCofPdfOrder() throws TclCommonException, IOException {
		WebexQuotePdfService mockPdfService = mock(WebexQuotePdfService.class);
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		Mockito.when(orderRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(webexObjectCreator.createOrder()));
		Mockito.when(partnerCustomerDetailsService.getCustomerDetailsBasedOnUserType())
				.thenReturn(webexObjectCreator.getCustomerList());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(webexObjectCreator.getOrderToLe()));
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString()))
				.thenReturn(webexObjectCreator.getCofDetails());
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.getQuoteToLeList());
		Mockito.when(orderRepository.findByQuoteAndStatus(Mockito.any(Quote.class), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getOrders());
		Mockito.when(omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(Mockito.anyString(),
				Mockito.anyInt(), Mockito.anyString())).thenReturn(webexObjectCreator.getOmsAttachments());
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("attachment.requestId.queue"), Mockito.anyString()))
				.thenReturn(Utils.convertObjectToJson(webexObjectCreator.getAttachmentBean()));

		doNothing().when(mockPdfService).processDownloadCof(Mockito.any(), Mockito.any());
		ResponseResource response = internalStakeViewController.generateWebexSignedCofPdf(1234, 4321,
				httpServletResponse);
		Assert.assertTrue(response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGenerateWebexSignedCofPdfQuote() throws TclCommonException, IOException {
		WebexQuotePdfService mockPdfService = mock(WebexQuotePdfService.class);
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		Mockito.when(orderRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(webexObjectCreator.createOrder()));
		Mockito.when(partnerCustomerDetailsService.getCustomerDetailsBasedOnUserType())
				.thenReturn(webexObjectCreator.getCustomerList());
		// Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(webexObjectCreator.getOrderToLe()));
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString()))
				.thenReturn(webexObjectCreator.getCofDetails());
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.getQuoteToLeList());
		Mockito.when(orderRepository.findByQuoteAndStatus(Mockito.any(Quote.class), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getOrders());
		Mockito.when(omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(Mockito.anyString(),
				Mockito.anyInt(), Mockito.anyString())).thenReturn(webexObjectCreator.getOmsAttachments());
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("attachment.requestId.queue"), Mockito.anyString()))
				.thenReturn(Utils.convertObjectToJson(webexObjectCreator.getAttachmentBean()));

		doNothing().when(mockPdfService).processDownloadCof(Mockito.any(), Mockito.any());
		ResponseResource response = internalStakeViewController.generateWebexSignedCofPdf(1234, 4321,
				httpServletResponse);
		Assert.assertTrue(response.getStatus() == Status.SUCCESS);
	}

//	@ConditionalOnProperty(prefix = "swift.api.enabled", value = "true")
	@Test
	public void testDownloadWebexCofPdf() throws TclCommonException {
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.getQuoteToLeList());
		Mockito.when(orderRepository.findByQuoteAndStatus(Mockito.any(Quote.class), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getOrders());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString()))
				.thenReturn(webexObjectCreator.getCofDetails());
		Mockito.when(omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(
				Mockito.contains(CommonConstants.ORDERS), Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(new ArrayList<>());
		Mockito.when(omsAttachmentRepository.findByReferenceNameAndReferenceIdAndAttachmentType(
				Mockito.contains(CommonConstants.QUOTES), Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(webexObjectCreator.getOmsAttachments());
		Mockito.when(mqUtils.sendAndReceive(Mockito.contains("attachment.requestId.queue"), Mockito.anyString()))
				.thenReturn(Utils.convertObjectToJson(webexObjectCreator.getAttachmentBean()));
		ResponseResource response = internalStakeViewController.downloadWebexCofPdf(1234, httpServletResponse);
		Assert.assertTrue(response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testUpdateCofUploadedDetails() throws TclCommonException {
		Mockito.when(omsAttachmentRepository.save(Mockito.any(OmsAttachment.class)))
				.thenReturn(webexObjectCreator.getOmsAttachments().get(0));
		ResponseResource response = internalStakeViewController.updateManualCofDetailsForUcaas(1234, 4321, "12",
				"www.test.com");
		Assert.assertTrue(response.getStatus() == Status.SUCCESS);
	}
}
