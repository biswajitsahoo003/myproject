package com.tcl.dias.oms.gvpn.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
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
import com.tcl.dias.oms.gvpn.service.v1.GvpnOrderService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.utils.NplObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the GvpnOrderServiceTest.java class.
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
public class GvpnOrderServiceTest {

	@MockBean
	OrderRepository orderRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;

	@MockBean
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@MockBean
	OrderProductSolutionRepository orderProductSolutionRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	OrderToLeProductFamilyRepository orderToLeProductFamilyRepository;

	@MockBean
	OrderPriceRepository orderPriceRepository;

	@MockBean
	OmsAttachmentRepository omsAttachmentRepository;

	@MockBean
	UserRepository userRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	@Qualifier("nplObjectCreator")
	private NplObjectCreator objectCreator;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	GvpnOrderController gvpnOrderController;

	@Autowired
	GvpnQuoteController gvpnQuoteController;

	@MockBean
	private QuoteToLeRepository quoteToLeRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;
	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@MockBean
	MQUtils mqutils;

	@MockBean
	MstOrderSiteStatusRepository mstOrderSiteStatusRepository;

	@MockBean
	MstOrderSiteStageRepository mstOrderSiteStageRepository;

	@MockBean
	OrderSiteStatusAuditRepository orderSiteStatusAuditRepository;

	@MockBean
	OrderSiteStageAuditRepository orderSiteStageAuditRepository;

	@Autowired
	GvpnOrderService gvpnOrderService;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	QuotePriceRepository quotePriceRepository;

	@MockBean
	IllSiteRepository illSiteRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;
	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

	@MockBean
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@MockBean
	SfdcJobRepository sfdcJobRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	OrderProductSolution productSolution;

	@MockBean
	NotificationService notificationService;

	@MockBean
	UserInfoUtils userInfoUtils;
	
	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;

	/* Test cases for service methods starts */
	
	@Before
	public void init() throws TclCommonException {

		Mockito.when(orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getGvpnOrderProductComponentList());

		Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponentAttribtueValueList());
		Mockito.when(orderProductComponentRepository
				.findByReferenceIdAndMstProductComponent(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getGvpnOrderProductComponentList());
		
		

		Mockito.when(orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster_Name(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponentAttribtueValueList());
		
		Mockito.when(orderIllSiteSlaRepository
				.findByOrderIllSite(Mockito.any()))
				.thenReturn(objectCreator.getGvpnOrderSiteSla1());
		
		

	}

	@Test
	public void testGetOrderDetails() throws TclCommonException {
		GvpnOrderService service = mock(GvpnOrderService.class);
		when(service.getOrderDetails(1)).thenCallRealMethod();

		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrder());

		OrdersBean response = gvpnOrderService.getOrderDetails(1);
		assertTrue(response != null);
	}

	// negative case
	@Test(expected = Exception.class)
	public void testGetOrderDetailsForNull() throws TclCommonException {
		GvpnOrderService service = mock(GvpnOrderService.class);
		when(service.getOrderDetails(1)).thenCallRealMethod();

		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		OrdersBean response = gvpnOrderService.getOrderDetails(1);
		assertTrue(response == null);
	}

	@Test
	public void testgetOmsAttachments() throws TclCommonException {
		GvpnOrderService service = mock(GvpnOrderService.class);
		when(service.getOmsAttachments(1)).thenCallRealMethod();

		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.createOmsAttachMentList());

		List<OmsAttachBean> response = gvpnOrderService.getOmsAttachments(1);
		assertTrue(response != null);
	}

	// negative case
	@Test(expected = Exception.class)
	public void testgetOmsAttachmentsForException() throws TclCommonException {
		GvpnOrderService service = mock(GvpnOrderService.class);
		when(service.getOmsAttachments(1)).thenCallRealMethod();

		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any())).thenThrow(RuntimeException.class);

		List<OmsAttachBean> response = gvpnOrderService.getOmsAttachments(1);
		assertTrue(response == null || response.isEmpty());
	}

	@Test
	public void testGetProductSolutionBasedOnVersion() throws TclCommonException {
		GvpnOrderService service = mock(GvpnOrderService.class);

	}

	@Test
	public void testGetComponentBasedOnIdVersionType() throws TclCommonException {
		GvpnOrderService service = mock(GvpnOrderService.class);
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),
				 Mockito.anyString())).thenReturn(objectCreator.getOrderProductComponentList());

	}

	@Test
	public void testGetProductFamilyBasenOnVersion() throws TclCommonException {
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());

	}

	@Test
	public void testConstructProductSolution() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),
				 Mockito.anyString())).thenReturn(objectCreator.getOrderProductComponentList());

		GvpnOrderService service = mock(GvpnOrderService.class);

	}

	@Test
	public void testUpdateOrderSites() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderIllSitesRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderIllSite());

		QuoteDetail response = gvpnOrderService.updateOrderSites(objectCreator.getUpdateRequest());
		assertTrue(response != null);

	}

	@Test(expected = Exception.class)
	public void testUpdateOrderSitesForException() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenThrow(RuntimeException.class);

		QuoteDetail response = gvpnOrderService.updateOrderSites(objectCreator.getUpdateRequest());
		assertTrue(response == null);

	}

	@Test
	public void testGetDashboardDetails() throws TclCommonException {
		Mockito.when(orderToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
		.thenReturn(objectCreator.getOrderToLesGVpnList());
		Mockito.when(authTokenDetailRepository.find(Mockito.any()))
		.thenReturn(objectCreator.getUSerInfp());
		
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderIllSitesRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
		.thenReturn(objectCreator.getOrderToLesList());		
		DashBoardBean response = gvpnOrderService.getDashboardDetails(1);
		DashBoardBean response1 = gvpnOrderService.getDashboardDetails(null);

		assertTrue(response != null && response1 != null);

	}

	@Test(expected = Exception.class)
	public void testGetDashboardDetailsForException() throws TclCommonException {
		Mockito.when(orderToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
				.thenThrow(RuntimeException.class);
		gvpnOrderService.getDashboardDetails(1);
	}

	@Test
	public void testGetLinkDetails() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderProductSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderProductSolution()));
		Mockito.when(orderToLeProductFamilyRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getorderToLeProductFamilies()));
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOptionalOrderToLe());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),
				 Mockito.anyString())).thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());

	}

	@Test(expected = Exception.class)
	public void testGetSiteDetailsForException() throws TclCommonException {

		OrderIllSiteBean site = gvpnOrderService.getSiteDetails(1);
		assertTrue(site != null);
	}

	@Test
	public void testOrderConfirmationAudit() throws TclCommonException {
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderConfirmationAudit());
		// gvpnOrderService.updateOrderConfirmationAudit("10.129.168.133","ABCDEFG","true");
		assertTrue(true);
	}

	@Test(expected = Exception.class)
	public void testOrderConfirmationAuditForException() throws TclCommonException {
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenThrow(new RuntimeException());
		// gvpnOrderService.updateOrderConfirmationAudit("10.129.168.133","ABCDEFG","true");
	}

	@Test
	public void testGetLeAttibutes() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstOmsAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getQuoteLeAttributeValueList());
		String leAttr = gvpnOrderService.getLeAttributes(objectCreator.getOrderToLes(), "");
		assertTrue(leAttr != null && !leAttr.isEmpty());
	}

	@Test
	public void testConstructOrderProductComponent() throws TclCommonException {
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),
				 Mockito.anyString())).thenReturn(objectCreator.getQuoteProductComponent());
		Mockito.when(orderProductComponentRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponent());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getQuotePrice());
		Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderPrice());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.anyInt()))
				.thenReturn(objectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponentAttribtueValue());

	}

	@Test
	public void testReturnLocationItContactName() throws TclCommonException {
		Mockito.when(mqutils.sendAndReceive(Mockito.anyString(), Mockito.anyString())).thenReturn("");
		assertTrue(gvpnOrderService.returnLocationItContactName(1) != null);
	}

	@Test
	public void testReturnLocationItContactNameForException() throws TclCommonException {
		Mockito.when(mqutils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(RuntimeException.class);
		assertTrue(gvpnOrderService.returnLocationItContactName(1) == null);
	}

	@Test
	public void testGetExcelList() throws TclCommonException {

		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrder());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductFamily());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamilies());
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getOrderProductSolutionList());

		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(mstProductComponentRepository.findByName(FPConstants.LAST_MILE.toString()))
				.thenReturn(objectCreator.getLastMile());

		gvpnOrderService.getExcelList(1);
	}

	@Test
	public void testReturnExcel() throws FileNotFoundException, TclCommonException, IOException {
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderGvpnSite());

		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrder());

		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getGvpnProductFamily());

		when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getGvpnOrderToLeProductFamilies());

		when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getGvpnOrderProductSolutionList());

		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderGvpnSiteList()));

		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn((objectCreator.getOrderProductComponentGVPN()));

		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(Mockito.any(), Mockito.anyByte()))
				.thenReturn((objectCreator.getOrderGvpnSiteFeasiblity()));

	
		HttpServletResponse response = mock(HttpServletResponse.class);
		gvpnOrderService.returnExcel(1, response);
	}
	
	@Test
	public void testReturnExcelNOnFeasible() throws FileNotFoundException, TclCommonException, IOException {
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderGvpnSite());

		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrder());

		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getGvpnProductFamily());

		when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getGvpnOrderToLeProductFamilies());

		when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getGvpnOrderProductSolutionList());

		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderGvpnSiteList()));

		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn((objectCreator.getOrderProductComponentGVPN()));

		/*Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(Mockito.any(), Mockito.anyByte()))
				.thenReturn((objectCreator.getOrderGvpnSiteFeasiblity()));*/

	
		HttpServletResponse response = mock(HttpServletResponse.class);
		gvpnOrderService.returnExcel(1, response);
	}
	
	
	
	@Test
	public void testSecondaryCompReturnExcel() throws FileNotFoundException, TclCommonException, IOException {
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderGvpnSite());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrder());

		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getGvpnProductFamily());

		when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.getGvpnOrderToLeProductFamilies());

		when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getGvpnOrderProductSolutionList());

		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderGvpnSiteList()));

		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn((objectCreator.getOrderProductComponentList()));

		Mockito.when(orderSiteFeasibilityRepository.findByOrderIllSiteAndIsSelected(Mockito.any(), Mockito.anyByte()))
				.thenReturn((objectCreator.getOrderGvpnSiteFeasiblity()));

	
		HttpServletResponse response = mock(HttpServletResponse.class);
		gvpnOrderService.returnExcel(1, response);
	}
	

	@Test
	public void getOrderSummaryForExcelTest() throws TclCommonException, IOException {
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrder());
		when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getGvpnOrderProductSolutionList());
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any()
			, Mockito.anyByte())).thenReturn((objectCreator.getOrderGvpnSiteList()));
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderGvpnSiteList()));
		
		
		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn((objectCreator.getOrderProductComponentList()));
		
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());
		

		Mockito.when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.anyString()))
				.thenReturn(objectCreator.getOrderConfirmationAudit());
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
				.thenReturn(objectCreator.getOrderToLesList());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValueList());
		HttpServletResponse response = mock(HttpServletResponse.class);

		gvpnOrderService.getOrderSummaryForExcel(1, response);
	}
	
	
	@Test
	public void getSiteProperitiesForSites() throws TclCommonException, IOException {
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrder());
		when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getGvpnOrderProductSolutionList());
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderGvpnSiteList()));
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderGvpnSiteList()));
		
		Mockito.when(orderIllSitesRepository.findById(Mockito.any())).thenReturn((Optional.of(objectCreator.getOrderIllSite())));
		
		
		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn((objectCreator.getOrderProductComponentList()));
		
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());
		
		Mockito.when(orderRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.of( objectCreator.getOrder()));

		Mockito.when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.anyString()))
				.thenReturn(objectCreator.getOrderConfirmationAudit());
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
				.thenReturn(objectCreator.getOrderToLesList());
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValueList());
		HttpServletResponse response = mock(HttpServletResponse.class);
		Mockito.when(mstProductComponentRepository.findByName(Mockito.any()))
		.thenReturn(objectCreator.getMstProductComponent());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.any(),Mockito.any()))
		.thenReturn(objectCreator.getMstProductComponentList());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.any(), Mockito.any()))
		.thenReturn(objectCreator.getGvpnOrderProductComponentList());
		
		gvpnOrderService.getSiteProperitiesForSites((objectCreator.getOrderIllSiteList()));
	}
	
	
	
	
	
	
}
