package com.tcl.dias.oms.webex.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
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
import com.tcl.dias.oms.gsc.util.GscAttachmentHelper;
import com.tcl.dias.oms.isv.controller.v1.InternalStakeViewController;
import com.tcl.dias.oms.partner.service.v1.PartnerCustomerDetailsService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.GscObjectCreator;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.dias.oms.utils.WebexObjectCreator;
import com.tcl.dias.oms.webex.service.WebexQuotePdfService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

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
@TestPropertySource(properties = { "swift.api.enabled = false", "temp.download.url.expiryWindow = 1",
		"temp.upload.url.expiryWindow = 5" })
public class UcaasInternalStakeViewControllerTestSwiftDisabled {

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
		Assert.assertTrue(response.getStatus() == Status.FAILURE);

	}

	@Test
	public void testDownloadWebexCofPdf() throws TclCommonException {
		MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
		Mockito.when(quoteToLeRepository.findByQuote_Id(Mockito.anyInt()))
				.thenReturn(webexObjectCreator.getQuoteToLeList());
		Mockito.when(orderRepository.findByQuoteAndStatus(Mockito.any(Quote.class), Mockito.anyByte()))
				.thenReturn(webexObjectCreator.getOrders());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString()))
				.thenReturn(webexObjectCreator.getCofDetails());
		ResponseResource response = internalStakeViewController.downloadWebexCofPdf(1234, httpServletResponse);
		Assert.assertTrue(response.getStatus() == Status.FAILURE);
	}

}
