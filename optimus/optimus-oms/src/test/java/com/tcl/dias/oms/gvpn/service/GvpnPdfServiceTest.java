package com.tcl.dias.oms.gvpn.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CurrencyConversionRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SfdcJobRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.gvpn.controller.v1.GvpnOrderController;
import com.tcl.dias.oms.gvpn.controller.v1.GvpnQuoteController;
import com.tcl.dias.oms.gvpn.service.v1.GvpnQuoteService;
import com.tcl.dias.oms.pdf.service.GvpnQuotePdfService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the GvpnPdfServiceTest.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GvpnPdfServiceTest {

	@Autowired
	GvpnQuotePdfService gvpnQuotePdfService;

	@MockBean
	GvpnQuoteService gvpnQuoteService;

	@Autowired
	private ObjectCreator gvpnObjectCreator;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	OmsSfdcService omsSfdcService;

	@MockBean
	OrderRepository orderRepository;

	@MockBean
	private QuoteToLeRepository quoteToLeRepository;

	@MockBean
	IllSiteRepository illSiteRepository;

	@MockBean
	private QuotePriceRepository quotePriceRepository;

	@MockBean
	private QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	private CustomerRepository customerRepository;

	@Autowired
	private ObjectCreator quoteObjectCreator;

	@MockBean
	private QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	GvpnQuoteController gvpnQuotesController;

	@Autowired
	GvpnOrderController gvpnOrderController;

	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;

	@MockBean
	private MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	private MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	private ProductSolutionRepository productSolutionRepository;

	@MockBean
	private QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	private MstProductOfferingRepository mstProductOfferingRepository;
	@MockBean
	private QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	private ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private OrderToLeRepository orderToLeRepository;

	@MockBean
	private OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	private OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	OrderPriceRepository orderPriceRepository;

	@MockBean
	private CurrencyConversionRepository currencyConversionRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	Utils utils;

	@MockBean
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@MockBean
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@MockBean
	SfdcJobRepository sfdcJobRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@MockBean
	QuoteDetail detail;

	@MockBean
	Order order;

	@MockBean
	PricingDetailsRepository pricingDetailsRepository;

	@MockBean
	QuoteToLe quoteToLe;

	@Value("${info.customer_le_location_queue}")
	String customerLeLocationQueue;

	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;

	@MockBean
	CofDetailsRepository cofDetailsRepository;
	

	

	@Before
	public void init() throws TclCommonException {
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());
		Mockito.when(authTokenDetailRepository.find(Mockito.any()))
		.thenReturn(quoteObjectCreator.getUSerInfp());
	
		Mockito.when(gvpnQuoteService.getQuoteDetails(Mockito.anyInt(), Mockito.anyString(),false, null))
				.thenReturn(gvpnObjectCreator.getQuoteBean());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString()))
				.thenReturn(gvpnObjectCreator.getCofDetails());
		Mockito.when(cofDetailsRepository.save(Mockito.any(CofDetails.class)))
				.thenReturn(gvpnObjectCreator.getCofDetails());
		Mockito.when(quoteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(gvpnObjectCreator.getQuote()));
		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.any());
		Mockito.when(orderRepository.findById(Mockito.any()))
				.thenReturn(Optional.ofNullable(gvpnObjectCreator.getOrder()));
	}

	@Test
	public void testProcessCofPdfApproch1() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(gvpnObjectCreator.getAddressDetailJSON());
		String htmlData = gvpnQuotePdfService.processCofPdf(1, new MockHttpServletResponse(), null, false, 1,null);
		assertTrue(htmlData != null && !htmlData.isEmpty());

	}

	@Test
	public void testProcessCofPdfApproch2() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(gvpnObjectCreator.getAddressDetailJSON());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString())).thenReturn(null);
		Mockito.when(gvpnQuoteService.getQuoteDetails(1, null,false, null)).thenReturn(gvpnObjectCreator.getQuoteBean());
		String htmlData = gvpnQuotePdfService.processCofPdf(1, new MockHttpServletResponse(), null, true, 1,null);
		assertTrue(htmlData != null && !htmlData.isEmpty());

	}

	@Test
	public void testProcessQuotePdf() throws Exception {
		Mockito.when(gvpnQuoteService.getQuoteDetails(1, null,false, null)).thenReturn(gvpnObjectCreator.getQuoteBean());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(gvpnObjectCreator.getAddressDetailJSON());
		gvpnQuotePdfService.processQuotePdf(1, new MockHttpServletResponse(), 1);
		assertTrue(true);

	}

	@Test
	public void testProcessDocusign() throws Exception {
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(1,null))
				.thenReturn(gvpnObjectCreator.getQuoteProductComponent());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(1),
				QuoteConstants.COMPONENTS.toString())).thenReturn(gvpnObjectCreator.getQuotePrice());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(quoteObjectCreator.getIllsites1()))
				.thenReturn(gvpnObjectCreator.getSiteFeasibilities());
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus( 1, (byte) 1))
				.thenReturn(gvpnObjectCreator.getIllsitesLists());
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
				.thenReturn(gvpnObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity(Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		HttpServletResponse response = mock(HttpServletResponse.class);
		Mockito.when(mqUtils.sendAndReceive("rabbitmq_supplier_details_customer", String.valueOf(1)))
				.thenReturn(gvpnObjectCreator.getSpJSON());
		Mockito.when(mqUtils.sendAndReceive("rabbitmq_location_details",
				String.valueOf(gvpnObjectCreator.getQuoteIllSiteBean().get(0).getLocationId())))
				.thenReturn(gvpnObjectCreator.getAddressDetail());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(gvpnObjectCreator.getAddressDetailJSON());
		Mockito.when(gvpnQuoteService.getQuoteDetails(1, null,false, null)).thenReturn(gvpnObjectCreator.getQuoteBean());

		gvpnQuotePdfService.processDocusign(1, 1, true, "optimus@tatacommunications.com", "optimus",null);
		assertTrue(true);

	}

	@Test
	public void testUploadCofPdfApproch1() throws Exception {
		Quote quote = gvpnObjectCreator.getQuote();
		quote.setQuoteCode(Utils.generateUid());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(quote));
		MockMultipartFile multipartFile = new MockMultipartFile("optimus", "optimusFile", "application/json",
				"optimus".getBytes());
		gvpnQuotePdfService.uploadCofPdf(1, multipartFile, 1);
		assertTrue(true);

	}

	@Test
	public void testUploadCofPdfApproch2() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("optimus", "optimusFile", "application/json",
				"optimus".getBytes());
		Quote quote = gvpnObjectCreator.getQuote();
		quote.setQuoteCode(Utils.generateUid());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(quote));
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString())).thenReturn(null);
		gvpnQuotePdfService.uploadCofPdf(1, multipartFile, 1);
		assertTrue(true);

	}

	@Test
	public void testProcessApprovedCof() throws Exception {
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.any())).thenReturn(gvpnObjectCreator.getCofDetails());
		gvpnQuotePdfService.processApprovedCof(1,1, new MockHttpServletResponse());
		assertTrue(true);

	}

	@Test
	public void testProcessQuoteHtml() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(gvpnObjectCreator.getAddressDetailJSON());
		gvpnQuotePdfService.processQuoteHtml(1);
		assertTrue(true);
	}

	@Test
	public void testApprovedManualQuotesWithout() throws Exception {
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(1,null))
				.thenReturn(gvpnObjectCreator.getQuoteProductComponent());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(1),
				QuoteConstants.COMPONENTS.toString())).thenReturn(gvpnObjectCreator.getQuotePrice());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(quoteObjectCreator.getIllsites1()))
				.thenReturn(gvpnObjectCreator.getSiteFeasibilities());
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(1, (byte) 1))
				.thenReturn(gvpnObjectCreator.getIllsitesLists());
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
				.thenReturn(gvpnObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity(Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		HttpServletResponse response = mock(HttpServletResponse.class);
		Mockito.when(mqUtils.sendAndReceive("rabbitmq_supplier_details_customer", String.valueOf(1)))
				.thenReturn(gvpnObjectCreator.getSpJSON());
		Mockito.when(mqUtils.sendAndReceive("rabbitmq_location_details",
				String.valueOf(gvpnObjectCreator.getQuoteIllSiteBean().get(0).getLocationId())))
				.thenReturn(gvpnObjectCreator.getAddressDetail());

		Mockito.when(gvpnQuoteService.getQuoteDetails(1, null,false, null)).thenReturn(gvpnObjectCreator.getQuoteBean());

		String s = gvpnQuotePdfService.processCofPdf(1, response, false, true, 1,null);
		assertTrue(s != null);
	}

	@Test
	public void testApprovedManualQuotesWithoutCof() throws Exception {
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(1,null))
				.thenReturn(gvpnObjectCreator.getQuoteProductComponent());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(1),
				QuoteConstants.COMPONENTS.toString())).thenReturn(gvpnObjectCreator.getQuotePrice());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(quoteObjectCreator.getIllsites1()))
				.thenReturn(gvpnObjectCreator.getSiteFeasibilities());
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(1, (byte) 1))
				.thenReturn(gvpnObjectCreator.getIllsitesLists());
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any(QuoteToLe.class)))
				.thenReturn(gvpnObjectCreator.getQuoteDelegationList());
		Mockito.doNothing().when(omsSfdcService).processSiteDetails(Mockito.any(QuoteToLe.class));
		Mockito.doNothing().when(omsSfdcService).processUpdateOpportunity(Mockito.any(Date.class), Mockito.eq("1"),
				Mockito.eq("feasible"), Mockito.any(QuoteToLe.class));
		HttpServletResponse response = mock(HttpServletResponse.class);
		Mockito.when(mqUtils.sendAndReceive("rabbitmq_supplier_details_customer", String.valueOf(1)))
				.thenReturn(gvpnObjectCreator.getSpJSON());
		Mockito.when(mqUtils.sendAndReceive("rabbitmq_location_details",
				String.valueOf(gvpnObjectCreator.getQuoteIllSiteBean().get(0).getLocationId())))
				.thenReturn(gvpnObjectCreator.getAddressDetail());

		Mockito.when(gvpnQuoteService.getQuoteDetails(1, null,false, null)).thenReturn(gvpnObjectCreator.getQuoteBean());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.any())).thenReturn(null);

		String s = gvpnQuotePdfService.processCofPdf(1, response, false, true, 1,null);
		assertTrue(s != null);
	}

}
