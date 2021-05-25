package com.tcl.dias.oms.ipc.macd.service.v1;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.IPCMACDOrderSummaryResponse;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.entity.entities.Engagement;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.EngagementRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OrderCloudRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
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
import com.tcl.dias.oms.entity.repository.QuoteCloudRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ipc.beans.QuoteBean;
import com.tcl.dias.oms.ipc.macd.controller.v1.IPCQuoteMACDController;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.utils.IPCObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IPCQuoteMACDServiceTest.java class. This class
 * contains all the test cases for the IPCQuoteMACDServiceTest
 * 
 *
 * @author DimpleS
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IPCQuoteMACDServiceTest {

	@Autowired
	IPCQuoteMACDController ipcQuoteMACDController;

	@Autowired
	IPCQuoteMACDService ipcQuoteMACDService;

	IPCObjectCreator quoteObjectCreator;

	@MockBean
	private MACDUtils macdUtils;

	@MockBean
	private MQUtils mqUtils;

	@MockBean
	private Utils utils;

	@MockBean
	private UserInfoUtils userInfoUtils;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private QuotePriceRepository quotePriceRepository;

	@MockBean
	private QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	private MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	private QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	private ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	private QuoteRepository quoteRepository;

	@MockBean
	private QuoteToLeRepository quoteToLeRepository;

	@MockBean
	private QuoteCloudRepository quoteCloudRepository;

	@MockBean
	private MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	private ProductSolutionRepository productSolutionRepository;

	@MockBean
	private QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	private MstProductOfferingRepository mstProductOfferingRepository;

	@MockBean
	private MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	private QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	private PricingDetailsRepository pricingDetailsRepository;

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	private QuoteDelegationRepository quoteDelegationRepository;

	@MockBean
	private DocusignAuditRepository docusignAuditRepository;

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	private CofDetailsRepository cofDetailsRepository;

	@MockBean
	private EngagementRepository engagementRepository;

	@MockBean
	private OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@MockBean
	private OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	private OrderCloudRepository orderCloudRepository;

	@MockBean
	private OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	private OrderToLeRepository orderToLeRepository;

	@MockBean
	private OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	private OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	private OrderPriceRepository orderPriceRepository;

	@MockBean
	private OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	private MacdDetailRepository macdDetailRepository;

	/**
	 * 
	 * init- predefined mocks
	 * 
	 * @throws TclCommonException
	 */
	@Before
	public void init() throws TclCommonException {
		quoteObjectCreator = new IPCObjectCreator();
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());
		when(macdUtils.getSiOrderData(Mockito.anyString())).thenReturn(quoteObjectCreator.getSiOrderDataBean());
		when(userInfoUtils.getUserType()).thenReturn(quoteObjectCreator.userType());
		when(userInfoUtils.getCustomerDetails()).thenReturn(quoteObjectCreator.getCustomerList());
		when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn((quoteObjectCreator.getUser()));
		when(mqUtils.sendAndReceive(Mockito.eq("${pricing.request.ipc.queue}"), Mockito.anyString(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getPricingBean());
		when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1"), Mockito.any()))
				.thenReturn(quoteObjectCreator.getCustomerDetailsBean());
		when(quoteRepository.findByIdAndStatus(Mockito.eq(1), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote());
		when(quoteRepository.findByIdAndStatus(Mockito.eq(2), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote());
//		when(quoteRepository.findByIdAndStatus(Mockito.eq(1), Mockito.anyByte())).thenReturn(quoteObjectCreator.getQuote());
		when(quoteRepository.findById(Mockito.eq(1))).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		when(quoteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuote());
		when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
		when(quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLeList());
		when(quoteToLeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLe());
		when(mstProductOfferingRepository.findByProductNameAndStatus(Mockito.eq("L.Nickel"), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstOffering());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductFamily());
		when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(quoteObjectCreator.createProductSolutions());
		when(quoteCloudRepository.findByQuoteToLeIdAndStatusAndFpStatus(Mockito.anyInt(), Mockito.anyByte(),
				Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteCloud());
		when(quoteProductComponentRepository.findByReferenceIdAndMstProductFamily(Mockito.anyInt(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponents());
		when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponentsAttributeValue());
		when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponentsAttributeValue());
		when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.eq(1), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getCustomer());
		when(productSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getOptionalProductSolution());
		when(quoteCloudRepository.findById(Mockito.eq(1))).thenReturn(quoteObjectCreator.getOptionalQuoteCloud());
		when(quoteCloudRepository.findByIdAndQuoteToLeIdAndStatus(Mockito.anyInt(), Mockito.anyInt(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getQuoteCloud().get(0));
		when(quoteCloudRepository.getProductNameBasedOnQuoteCloudId(Mockito.any()))
				.thenReturn(quoteObjectCreator.getProductNames());
		when(quoteProductComponentRepository.findAllById(Mockito.anyListOf(Integer.class)))
				.thenReturn(quoteObjectCreator.getProductComponent());
		when(quoteToLeRepository.findByQuoteAndId(Mockito.any(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuoteToLe());
		when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(QuoteToLe.class),
				Mockito.any(MstOmsAttribute.class))).thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		when(quoteDelegationRepository.findByQuoteToLeAndAssignToAndStatus(Mockito.any(QuoteToLe.class),
				Mockito.anyInt(), Mockito.eq("open"))).thenReturn(quoteObjectCreator.getDelegateRequest());
		when(userRepository.findByEmailIdAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		when(docusignAuditRepository.findByOrderRefUuid(Mockito.eq("IPCVE23CV1")))
				.thenReturn(quoteObjectCreator.getDocusignAudit());
		when(quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameInAndStatus(Mockito.eq(2),
				Mockito.anyListOf(String.class), Mockito.anyByte())).thenReturn(quoteObjectCreator.getQuoteCloudMacd());
		when(quoteCloudRepository.findByQuoteToLeIdAndResourceDisplayNameInAndStatus(Mockito.eq(3),
				Mockito.anyListOf(String.class), Mockito.anyByte()))
						.thenReturn(quoteObjectCreator.getQuoteCloudMacdAdd());
		when(mqUtils.sendAndReceive(Mockito.eq("${rabbitmq.ipc.si.related.details.queue}"), Mockito.eq("2"),
				Mockito.any())).thenReturn(quoteObjectCreator.getSIQueueResponse());
		when(quoteToLeRepository.findById(Mockito.eq(2))).thenReturn(quoteObjectCreator.getOptionalQuoteCloudMACD());
		when(quoteToLeRepository.findById(Mockito.eq(3))).thenReturn(quoteObjectCreator.getOptionalQuoteCloudMACDAdd());
		when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.eq(2), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponentsConnectivityUpgrade());
		when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.eq(3), Mockito.any()))
				.thenReturn(quoteObjectCreator.getAddOnQuoteProductComponents());
		when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getMstProductComponent());
		when(mstProductComponentRepository.findByNameIn(Mockito.anyListOf(String.class)))
				.thenReturn(quoteObjectCreator.getListOfProductComponent());
		when(quotePriceRepository.findByReferenceNameAndReferenceIdIn(Mockito.anyString(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuotePrice());
		when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponentIn(Mockito.anyInt(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponents());
		when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getMstOmsAttributeList());
		when(orderRepository.findByQuoteAndStatus(Mockito.any(Quote.class), Mockito.anyByte())).thenReturn(null);
		when(cofDetailsRepository.findByOrderUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getCofDetails());
		when(quoteDelegationRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getDelegateRequestList());
		when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeProductFamilyList());
		when(engagementRepository.findByCustomerAndErfCusCustomerLeIdAndMstProductFamilyAndStatus(Mockito.any(),
				Mockito.any(), Mockito.any(), Mockito.any())).thenReturn((List<Engagement>) quoteObjectCreator.getEngagement());
		when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.any())).thenReturn(null);
		when(quoteCloudRepository.findByProductSolutionAndStatus(Mockito.any(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuoteCloudMacd());
		when(macdDetailRepository.findByQuoteToLeId(Mockito.any())).thenReturn(quoteObjectCreator.getMacdDetail());
		when(macdDetailRepository.findByTpsServiceIdAndOrderCategoryInAndStage(Mockito.eq("91920IPC90"), Mockito.any(),
				Mockito.any())).thenReturn(quoteObjectCreator.getMacdDetail());

	}

	// positive testcase
	@Test
	public void testHandleMacdRequestToCreateQuote() throws TclCommonException {
		MacdQuoteResponse response = ipcQuoteMACDService
				.handleMacdRequestToCreateQuote(quoteObjectCreator.getMacdQuoteRequest());
		assertTrue(response != null && response.getQuoteResponse() != null);
	}

	/**
	 * 
	 * handleMacdRequestToCreateQuote - negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test(expected = Exception.class)
	public void testHandleMacdRequestToCreateQuoteNegative() throws TclCommonException {
		MacdQuoteResponse response = ipcQuoteMACDService
				.handleMacdRequestToCreateQuote(quoteObjectCreator.getMacdQuoteRequestNegative());
	}

	// positive testcase
	@Test
	public void testUpdateSolutionName() throws TclCommonException {
		ipcQuoteMACDService.updateSolutionName(1, "L.Nickel", quoteObjectCreator.getSolutionDetail());
	}

	/**
	 * 
	 * updateSolutionName - negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test(expected = Exception.class)
	public void testUpdateSolutionNameNegative() throws TclCommonException {
		ipcQuoteMACDService.updateSolutionName(0, "L.Nickel", quoteObjectCreator.getSolutionDetail());
	}

	// positive testcase
	@Test
	public void testGetMACDQuoteDetails() throws TclCommonException {
		QuoteBean response = ipcQuoteMACDService.getMACDQuoteDetails(1);
		assertTrue(response != null && response.getQuoteId() == 2);
	}

	/**
	 * 
	 * Get Quote MACD- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test(expected = Exception.class)
	public void testGetMACDQuoteDetailsNegative() throws TclCommonException {
		QuoteBean response = ipcQuoteMACDService.getMACDQuoteDetails(0);
	}

	// positive testcase
	@Test
	public void testCreateMacdQuote() throws TclCommonException, ParseException {
		MacdQuoteResponse response = ipcQuoteMACDService.createMacdQuote(quoteObjectCreator.getMacdQuoteRequest());
		assertTrue(response != null && response.getQuoteType() == "MACD");
	}

	/**
	 * 
	 * createMacdQuote- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test(expected = Exception.class)
	public void testCreateMacdQuoteNegative() throws TclCommonException, ParseException {
		MacdQuoteResponse response = ipcQuoteMACDService
				.createMacdQuote(quoteObjectCreator.getMacdQuoteRequestNegative());
	}

	// positive testcase
	@Test
	public void testApprovedQuotes() throws TclCommonException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(orderProductSolutionRepository.saveAndFlush(Mockito.any())).thenReturn(null);
		when(orderCloudRepository.save(Mockito.any())).thenReturn(null);
		when(orderProductComponentRepository.save(Mockito.any())).thenReturn(null);
		when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(null);
		when(macdDetailRepository.save(Mockito.any())).thenReturn(null);
		when(ordersLeAttributeValueRepository.save(Mockito.any())).thenReturn(null);
		when(orderToLeProductFamilyRepository.save(Mockito.any())).thenReturn(null);
		when(orderToLeRepository.save(Mockito.any())).thenReturn(null);
		when(cofDetailsRepository.save(Mockito.any())).thenReturn(null);
		when(quoteDelegationRepository.save(Mockito.any())).thenReturn(null);
		when(orderProductComponentsAttributeValueRepository.save(Mockito.any())).thenReturn(null);
		when(orderPriceRepository.save(Mockito.any())).thenReturn(null);
		when(request.getHeader("X-Forwarded-For")).thenReturn("1");
		String forwardedIp = request.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		QuoteDetail response = ipcQuoteMACDService.macdApprovedQuotes(quoteObjectCreator.getUpdateRequest(), "1");
		assertTrue(response != null && response.getOrderLeIds().size() > 0);
	}

	/**
	 * 
	 * macdApprovedQuotes- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test(expected = Exception.class)
	public void testApprovedQuotesNegative() throws TclCommonException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(orderProductSolutionRepository.saveAndFlush(Mockito.any())).thenReturn(null);
		when(orderCloudRepository.save(Mockito.any())).thenReturn(null);
		when(orderProductComponentRepository.save(Mockito.any())).thenReturn(null);
		when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(null);
		when(macdDetailRepository.save(Mockito.any())).thenReturn(null);
		when(ordersLeAttributeValueRepository.save(Mockito.any())).thenReturn(null);
		when(orderToLeProductFamilyRepository.save(Mockito.any())).thenReturn(null);
		when(orderToLeRepository.save(Mockito.any())).thenReturn(null);
		when(cofDetailsRepository.save(Mockito.any())).thenReturn(null);
		when(quoteDelegationRepository.save(Mockito.any())).thenReturn(null);
		when(orderProductComponentsAttributeValueRepository.save(Mockito.any())).thenReturn(null);
		when(orderPriceRepository.save(Mockito.any())).thenReturn(null);
		when(request.getHeader("X-Forwarded-For")).thenReturn("1");
		String forwardedIp = request.getHeader(CommonConstants.XFORWARDEDFOR_HEADER);
		UpdateRequest updaterequest = quoteObjectCreator.getUpdateRequest();
		updaterequest.setQuoteId(null);
		QuoteDetail response = ipcQuoteMACDService.macdApprovedQuotes(updaterequest, null);
	}

	// positive testcase
	@Test
	public void testCheckMacdInitiatedBasedOnServiceId() throws TclCommonException {
		List<String> list = new ArrayList<String>();
		list.add("CONNECTIVITY_UPGRADE");
		boolean response = ipcQuoteMACDService.checkMacdInitiatedBasedOnServiceId("91920IPC90", list, "Get Quote");
		assertTrue(response == true);
	}

	/**
	 * 
	 * checkMacdInitiatedBasedOnServiceId- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testCheckMacdInitiatedBasedOnServiceIdNegative() throws TclCommonException {
		List<String> list = new ArrayList<String>();
		list.add("CONNECTIVITY_UPGRADE");
		boolean response = ipcQuoteMACDService.checkMacdInitiatedBasedOnServiceId("91920IPC91", list, "Get Quote");
		assertTrue(response == false);
	}

	// positive testcase
	@Test
	public void testCheckMacdInitiatedBasedOnQuoteToLe() throws TclCommonException {
		boolean response = ipcQuoteMACDService.checkMacdInitiatedBasedOnQuoteToLe(quoteObjectCreator.getQuoteToLe());
		assertTrue(response == true);
	}

	// postive testcase
	@Test
	public void testGetOrderSummary() throws TclCommonException {
		IPCMACDOrderSummaryResponse response = ipcQuoteMACDService.getOrderSummary(1, 2, "2");
		assertTrue(response != null && response.getIpcMACDAttributeSummaryList().size() > 0);
	}

	@Test
	public void testGetOrderSummary1() throws TclCommonException {
		IPCMACDOrderSummaryResponse response = ipcQuoteMACDService.getOrderSummary(1, 3, "2");
		assertTrue(response != null && response.getIpcMACDAttributeSummaryList().size() > 0);
	}

	/**
	 * 
	 * getOrderSummary- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test(expected = Exception.class)
	public void testGetOrderSummaryNegative() throws TclCommonException {
		IPCMACDOrderSummaryResponse response = ipcQuoteMACDService.getOrderSummary(0, 0, "");
	}
	
	// positive testcase
	@Test
	public void testCreateMacdOrderDetail() throws TclCommonException,ParseException {
		ipcQuoteMACDService.createMacdOrderDetail(quoteObjectCreator.getQuoteToLe(),"12/04/2019", "test");
	}

	
}
