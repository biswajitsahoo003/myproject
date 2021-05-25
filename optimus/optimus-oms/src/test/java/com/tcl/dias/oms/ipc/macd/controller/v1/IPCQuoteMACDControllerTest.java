package com.tcl.dias.oms.ipc.macd.controller.v1;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.IPCMACDOrderSummaryResponse;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
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
import com.tcl.dias.oms.ipc.macd.service.v1.IPCQuoteMACDService;
import com.tcl.dias.oms.macd.beans.MacdQuoteRequest;
import com.tcl.dias.oms.macd.beans.MacdQuoteResponse;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.utils.IPCObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IPCQuoteMACDControllerTest.java class. This class
 * contains all the test cases for the IPCQuoteMACDControllerTest
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
public class IPCQuoteMACDControllerTest {

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
		when(quoteDelegationRepository.findByQuoteToLe(Mockito.any())).thenReturn(quoteObjectCreator.getDelegateRequestList());
		when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLeProductFamilyList());
		when(engagementRepository.findByCustomerAndErfCusCustomerLeIdAndMstProductFamilyAndStatus(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn((List<Engagement>) quoteObjectCreator.getEngagement());
		when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.any())).thenReturn(null);
		when(quoteCloudRepository.findByProductSolutionAndStatus(Mockito.any(),Mockito.anyByte())).thenReturn(quoteObjectCreator.getQuoteCloudMacd());
	when(macdDetailRepository.findByQuoteToLeId(Mockito.any())).thenReturn(quoteObjectCreator.getMacdDetail());
	
	}

	// positive testcase
	@Test
	public void testCreateQuote() throws TclCommonException {
		ResponseResource<MacdQuoteResponse> response = ipcQuoteMACDController
				.createQuote(quoteObjectCreator.getMacdQuoteRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * Create Quote - negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testCreateQuoteNegative() throws TclCommonException {
		ResponseResource<MacdQuoteResponse> response = ipcQuoteMACDController
				.createQuote(quoteObjectCreator.getMacdQuoteRequestNegative());
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	@Test
	public void testCreateQuoteNegative1() throws TclCommonException {
		ResponseResource<MacdQuoteResponse> response = ipcQuoteMACDController
				.createQuote(quoteObjectCreator.getMacdQuoteRequestNegative1());
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	@Test
	public void testCreateQuoteNegative2() throws TclCommonException {
		MacdQuoteRequest request = quoteObjectCreator.getMacdQuoteRequest();
		ResponseResource<MacdQuoteResponse> response = ipcQuoteMACDController.createQuote(request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	// positive testcase
	@Test
	public void testGetQuote() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.getMACDQuoteDetails(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * Get Quote MACD- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetQuoteNegative() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.getMACDQuoteDetails(0);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive testcase
	@Test
	public void testAddQuoteCloudProduct() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.addQuoteCloudProduct(1, 1,
				quoteObjectCreator.getQuoteCloudAddRequest(), 1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * addQuoteCloudProductNegative- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testAddQuoteCloudProductNegative() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.addQuoteCloudProduct(1, 1,
				quoteObjectCreator.getQuoteCloudAddRequest(), 0);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testAddQuoteCloudProductNegative1() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.addQuoteCloudProduct(0, 0,
				quoteObjectCreator.getQuoteCloudAddRequest(), 0);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive testcase
	@Test
	public void testDeleteQuoteCloudSolution() throws TclCommonException {
		List<Integer> list = new ArrayList<>();
		list.add(12345);
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.deleteQuoteCloudSolution(list, 1, 1, 1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * deleteQuoteCloudSolution- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testDeleteQuoteCloudSolutionNegative() throws TclCommonException {
		List<Integer> list = new ArrayList<>();
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.deleteQuoteCloudSolution(list, 0, 0, 0);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testDeleteQuoteCloudSolutionNegative1() throws TclCommonException {
		List<Integer> list = new ArrayList<>();
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.deleteQuoteCloudSolution(list, 0, 0, 1);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testDeleteQuoteCloudSolutionNegative2() throws TclCommonException {
		List<Integer> list = new ArrayList<>();
		list.add(12345);
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.deleteQuoteCloudSolution(list, 0, 0, 0);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive testcase
	@Test
	public void testDeleteQuoteCloudProduct() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.deleteQuoteCloudProduct(1, 1, 1, 1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * deleteQuoteCloudProduct- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testDeleteQuoteCloudProductNegative() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.deleteQuoteCloudProduct(0, 0, 0, 1);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testDeleteQuoteCloudProductNegative1() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.deleteQuoteCloudProduct(0, 1, 1, 1);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testDeleteQuoteCloudProductNegative2() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.deleteQuoteCloudProduct(0, 0, 1, 1);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive case
	@Test
	public void testAddComponent() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.addComponent(1, 1,
				quoteObjectCreator.getComponentList());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * addComponent- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testAddComponentNegative() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.addComponent(0, 1,
				quoteObjectCreator.getComponentList());
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testAddComponentNegative1() throws TclCommonException {
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.addComponent(1, 1, null);
		assertTrue(response != null && response.getStatus() == Status.ERROR);
	}

	// positive testcase
	@Test
	public void testUpdateSolutionName() throws TclCommonException {
		ResponseResource<String> response = ipcQuoteMACDController.updateSolutionName(1, "L.Nickel",
				quoteObjectCreator.getSolutionDetail());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * updateSolutionName- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateSolutionNameNegative() throws TclCommonException {
		ResponseResource<String> response = ipcQuoteMACDController.updateSolutionName(0, "L.Nickel",
				quoteObjectCreator.getSolutionDetail());
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testUpdateSolutionNameNegative3() throws TclCommonException {
		ResponseResource<String> response = ipcQuoteMACDController.updateSolutionName(0, null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive testcase
	@Test
	public void testRemoveComponent() throws TclCommonException {
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(123);
		idList.add(456);
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.removeComponent(1, idList);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * removeComponent- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testRemoveComponentNegative() throws TclCommonException {
		List<Integer> idList = new ArrayList<Integer>();
		ResponseResource<QuoteBean> response = ipcQuoteMACDController.removeComponent(0, idList);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive case
	@Test
	public void testShareQuote() throws TclCommonException {
		ResponseResource<ServiceResponse> response = ipcQuoteMACDController.shareQuote(1, 1, "abc@gmail.com");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * shareQuote- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testShareQuoteNegative() throws TclCommonException {
		ResponseResource<ServiceResponse> response = ipcQuoteMACDController.shareQuote(0, 1, "abc@gmail.com");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testShareQuoteNegative1() throws TclCommonException {
		ResponseResource<ServiceResponse> response = ipcQuoteMACDController.shareQuote(1, 1, "abc.gmail.com");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive testcase
	@Test
	public void testUpdateTermAndMonths() throws TclCommonException {
		ResponseResource<String> response = ipcQuoteMACDController.updateTermAndMonths(1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * updateTermAndMonths- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateTermAndMonthsNegative() throws TclCommonException {
		ResponseResource<String> response = ipcQuoteMACDController.updateTermAndMonths(1, 1, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive testcase
	@Test
	public void testUpdateCurrency() throws TclCommonException {
		ResponseResource<String> response = ipcQuoteMACDController.updateCurrency(1, 1, "USD");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * updateTermAndMonths- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateCurrencyNegative() throws TclCommonException {
		ResponseResource<String> response = ipcQuoteMACDController.updateCurrency(1, 1, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive testcase
//	@Test
//	public void testUpdateQuoteStage() throws TclCommonException {
//		ResponseResource<String> response = ipcQuoteMACDController.updateQuoteStage(1, 1, "Get Quote");
//		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
//	}

	/**
	 * 
	 * updateQuoteStage- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteStageNegative() throws TclCommonException {
		ResponseResource<String> response = ipcQuoteMACDController.updateQuoteStage(0, 0, null);
		assertTrue(response != null && response.getStatus() == Status.ERROR);
	}

	// positive testcase
	@Test
	public void testTriggerEmail() throws TclCommonException {
		TriggerEmailRequest emailRequest = quoteObjectCreator.getEmailRequest();
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("X-Forwarded-For")).thenReturn("1");
		ResponseResource<TriggerEmailResponse> response = ipcQuoteMACDController.triggerEmail(1, 1, emailRequest,
				request);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * triggerEmail- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testTriggerEmailNegative() throws TclCommonException {
		TriggerEmailRequest emailRequest = null;
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("X-Forwarded-For")).thenReturn("1");
		ResponseResource<TriggerEmailResponse> response = ipcQuoteMACDController.triggerEmail(1, 1, emailRequest,
				request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testTriggerEmailNegative1() throws TclCommonException {
		TriggerEmailRequest emailRequest = null;
		HttpServletRequest request = mock(HttpServletRequest.class);
		ResponseResource<TriggerEmailResponse> response = ipcQuoteMACDController.triggerEmail(1, 1, emailRequest,
				request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testTriggerEmailNegative2() throws TclCommonException {
		TriggerEmailRequest emailRequest = null;
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("X-Forwarded-For")).thenReturn("1");
		ResponseResource<TriggerEmailResponse> response = ipcQuoteMACDController.triggerEmail(0, 0, emailRequest,
				request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// positive testcase
	@Test
	public void testUpdateLegalEntityProperties() throws TclCommonException {
		ResponseResource<QuoteDetail> response = ipcQuoteMACDController.updateLegalEntityProperties(1, 1,
				quoteObjectCreator.getUpdateRequestAttr());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * updateLegalEntityProperties- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesNegative() throws TclCommonException {
		ResponseResource<QuoteDetail> response = ipcQuoteMACDController.updateLegalEntityProperties(0, 0, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// postive testcase
	@Test
	public void testProcessDocusign() throws TclCommonException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		ResponseResource<String> response = ipcQuoteMACDController.processDocusign(1, 1, true, "test",
				"abc@tatacommunications.com", quoteObjectCreator.getApproversList(), request);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * processDocusign- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testProcessDocusignNegative() throws TclCommonException {
		HttpServletRequest request = mock(HttpServletRequest.class);
		ResponseResource<String> response = ipcQuoteMACDController.processDocusign(0, 0, true, "test",
				"abc.tatacommunications.com", null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	// postive testcase
	@Test
	public void testGetOrderSummary() throws TclCommonException {
		ResponseResource<IPCMACDOrderSummaryResponse> response = ipcQuoteMACDController.getOrderSummary(1, 2, "2");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testGetOrderSummary1() throws TclCommonException {
		ResponseResource<IPCMACDOrderSummaryResponse> response = ipcQuoteMACDController.getOrderSummary(1, 3, "2");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * getOrderSummary- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testGetOrderSummaryNegative() throws TclCommonException {
		ResponseResource<IPCMACDOrderSummaryResponse> response = ipcQuoteMACDController.getOrderSummary(0, 0, "");
		assertTrue(response != null && response.getStatus() == Status.ERROR);
	}

	// postive testcase
	@Test
	public void testPersistListOfQuoteLeAttributes() throws TclCommonException {
		ResponseResource<QuoteDetail> response = ipcQuoteMACDController.persistListOfQuoteLeAttributes(1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * 
	 * persistListOfQuoteLeAttributes- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
	public void testPersistListOfQuoteLeAttributesNegative() throws TclCommonException {
		ResponseResource<QuoteDetail> response = ipcQuoteMACDController.persistListOfQuoteLeAttributes(1, 1, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
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
		ResponseResource<QuoteDetail> response = ipcQuoteMACDController.approvedQuotes(quoteObjectCreator.getUpdateRequest(),request);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	/**
	 * 
	 * approvedQuotes- negative testcases
	 *
	 * @throws TclCommonException
	 */
	@Test
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
		ResponseResource<QuoteDetail> response = ipcQuoteMACDController.approvedQuotes(quoteObjectCreator.getUpdateRequest(),null);
		assertTrue(response != null && response.getStatus() == Status.ERROR);
	}
	@Test
	public void testApprovedQuotesNegative1() throws TclCommonException {
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
		ResponseResource<QuoteDetail> response = ipcQuoteMACDController.approvedQuotes(null,null);
		assertTrue(response != null && response.getStatus() == Status.ERROR);
	}
}
