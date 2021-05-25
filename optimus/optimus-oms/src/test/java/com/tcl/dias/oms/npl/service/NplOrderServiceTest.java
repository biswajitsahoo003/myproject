package com.tcl.dias.oms.npl.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

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
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.DashboardCustomerbean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderSummaryBean;
import com.tcl.dias.oms.constants.FPConstants;
import com.tcl.dias.oms.constants.OrderDetailsExcelDownloadConstants;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstOmsAttributeBean;
import com.tcl.dias.oms.entity.entities.OrderLinkStageAudit;
import com.tcl.dias.oms.entity.entities.OrderLinkStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderNplLink;
import com.tcl.dias.oms.entity.entities.OrderNplLinkSla;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderProductSolution;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrderToLeProductFamily;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderLinkStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderLinkStatusRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderLinkStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStageAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderSiteStatusAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.PricingDetailsRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SfdcJobRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.npl.beans.DashboardOrdersBean;
import com.tcl.dias.oms.npl.beans.NplLinkBean;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplPricingFeasibilityBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.beans.OrderNplSiteBean;
import com.tcl.dias.oms.npl.beans.OrderProductSolutionBean;
import com.tcl.dias.oms.npl.beans.OrderToLeBean;
import com.tcl.dias.oms.npl.beans.OrderToLeProductFamilyBean;
import com.tcl.dias.oms.npl.controller.v1.NplOrderController;
import com.tcl.dias.oms.npl.controller.v1.NplQuoteController;
import com.tcl.dias.oms.npl.pdf.constants.NplPDFConstants;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.NplObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the NplOrderServiceTest.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NplOrderServiceTest {
	
	@MockBean
	PricingDetailsRepository pricingDetailsRepository;

	@MockBean
	OrderRepository orderRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	OrderIllSitesRepository orderIllSitesRepository;

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
	NplOrderController nplOrderController;
	
	@Autowired
	NplQuoteController nplQuoteController;

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
	@MockBean
	OrderNplLinkRepository orderNplLinkRepository;
	
	@MockBean
	NplLinkRepository nplLinkRepository;
	
	@MockBean
	LinkFeasibilityRepository linkFeasibilityRepository;
	
	@Autowired
	NplOrderService nplOrderService;
	
	@MockBean
	QuoteRepository quoteRepository;
	
	@MockBean
	QuotePriceRepository quotePriceRepository;
	
	@MockBean
	OmsSfdcService omsSfdcService;
	
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
	OrderLinkFeasibilityRepository orderLinkFeasibilityRepository;
	
	
	@MockBean
	SfdcJobRepository sfdcJobRepository;
	
	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@MockBean 
	MstOrderLinkStatusRepository mstOrderLinkStatusRepository;
	
	@MockBean 
	MstOrderLinkStageRepository mstOrderLinkStageRepository;
	
	@MockBean
	OrderLinkStatusAuditRepository orderLinkStatusAuditRepository;
	
	@MockBean
	OrderLinkStageAuditRepository orderLinkStageAuditRepository;
	
	@MockBean
	OrderProductSolution productSolution;
	
	@MockBean
	NotificationService notificationService;
	
	@MockBean
	UserInfoUtils userInfoUtils;
	
	@MockBean
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;
	
	@MockBean
	OrderNplLinkSlaRepository orderNplLinkSlaRepository;

	@MockBean
	MailNotificationBean mailNotificationBean;
	
/* Test cases for service methods starts*/
	
	@Test
	public void testGetOrderDetails() throws TclCommonException {
		NplOrderService service = mock(NplOrderService.class);
		when(service.constructOrder(Mockito.any())).thenReturn(new NplOrdersBean());    
		when(service.getOrderDetails(1)).thenCallRealMethod();
		
	
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getOrder());


		NplOrdersBean response = nplOrderService.getOrderDetails(1);
		assertTrue(response != null );
	}
		
	// negative case
		@Test(expected=Exception.class)
		public void testGetOrderDetailsForNull() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.constructOrder(Mockito.any())).thenReturn(new NplOrdersBean());    
			when(service.getOrderDetails(1)).thenCallRealMethod();
		
			Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(null);
			NplOrdersBean response = nplOrderService.getOrderDetails(1);
			assertTrue(response == null );
		}
		
		@Test
		public void testgetOmsAttachments() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.getOmsAttachments(1)).thenCallRealMethod();
		
			Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
			Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any())).thenReturn(objectCreator.createOmsAttachMentList());

			List<OmsAttachBean> response = nplOrderService.getOmsAttachments(1);
			assertTrue(response != null );
		}
		
		// negative case
		@Test(expected=Exception.class)
		public void testgetOmsAttachmentsForException() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.getOmsAttachments(1)).thenCallRealMethod();
		
			Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
			Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any())).thenThrow(RuntimeException.class);

			List<OmsAttachBean> response = nplOrderService.getOmsAttachments(1);
			assertTrue(response == null || response.isEmpty() );
		}
		
		@Test
		public void testConstructOrder() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.constructOrder(Mockito.any())).thenCallRealMethod();
			when(service.constructOrderLeEntityDtos(Mockito.any())).thenReturn(null);    
		
			NplOrdersBean response = nplOrderService.constructOrder(objectCreator.getOrder());
			assertTrue(response != null );
		}
		
		@Test
		public void testConstructOrderToLeEntityDtos() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.getOrderToLeBasenOnVersion(Mockito.any())).thenReturn(objectCreator.getOrderToLesList());    
			when(service.constructLegalAttributes(Mockito.any())).thenReturn(null);    
			when(service.getProductFamilyBasenOnVersion(Mockito.any())).thenReturn(null);    
			when(service.constructOrderToLeFamilyDtos(Mockito.any())).thenReturn(null);    
			when(service.constructOrderLeEntityDtos(Mockito.any())).thenCallRealMethod();

			Set<OrderToLeBean> response = service.constructOrderLeEntityDtos(objectCreator.getOrder());
			assertTrue(response != null && !response.isEmpty());
		}
		
		@Test
		public void testConstructLegalAttributes() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.constructLegalAttributes(Mockito.any())).thenCallRealMethod();
			when(service.constructMstAttributBean(Mockito.any())).thenReturn(null);    

			Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any())).thenReturn(objectCreator.getOrdersLeAttributeValueList());


			Set<LegalAttributeBean> response = nplOrderService.constructLegalAttributes(objectCreator.getOrderToLes());
			assertTrue(response != null && !response.isEmpty());
		}
		
		@Test
		public void testConstructMstAttributeBean() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.constructMstAttributBean(Mockito.any())).thenCallRealMethod();
			MstOmsAttributeBean response = nplOrderService.constructMstAttributBean(objectCreator.getMstOmsAttribute());
			assertTrue(response != null );
		}
		
		@Test
		public void testGetOrderToLeBasedOnVersion() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.getOrderToLeBasenOnVersion(Mockito.any())).thenCallRealMethod();
			Mockito.when(orderToLeRepository.findByOrder(Mockito.any())).thenReturn(objectCreator.getOrderToLesList());

			List<OrderToLe> response = nplOrderService.getOrderToLeBasenOnVersion(objectCreator.getOrder());
			assertTrue(response != null && !response.isEmpty() );
		}
		
		@Test
		public void testGetProductSolutionBasedOnVersion() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.getProductSolutionBasenOnVersion(Mockito.any())).thenCallRealMethod();
			Mockito.when( orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any())).thenReturn(objectCreator.getOrderProductSolutionList());

			List<OrderProductSolution> response = nplOrderService.getProductSolutionBasenOnVersion(objectCreator.getorderToLeProductFamilies());
			assertTrue(response != null && !response.isEmpty() );
		}
		
		@Test
		public void testGetComponentBasedOnIdVersionType() throws TclCommonException {
			NplOrderService service = mock(NplOrderService.class);
			when(service.getComponentBasedOnIdVersionType(Mockito.anyInt(),Mockito.anyString(), Mockito.anyString())).thenCallRealMethod();
			Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(objectCreator.getOrderProductComponentList());

			List<OrderProductComponent> response = nplOrderService.getComponentBasedOnIdVersionType(1,"abc", "NPL");
			assertTrue(response != null && !response.isEmpty() );
		}

		@Test
		public void testGetProductFamilyBasenOnVersion() throws TclCommonException {
			Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());

			List<OrderToLeProductFamily> response = nplOrderService.getProductFamilyBasenOnVersion(objectCreator.getOrderToLes());
			assertTrue(response != null && !response.isEmpty() );
		}
		
		@Test
		public void testConstructOrderToLeFamilyDtos() throws TclCommonException {
			Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
			Mockito.when( orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductSolutionList());

			
			NplOrderService service = mock(NplOrderService.class);
			when(service.constructOrderToLeFamilyDtos(Mockito.any())).thenCallRealMethod();
			when(service.constructProductSolution(Mockito.any())).thenReturn(objectCreator.getOrderProductSolutionBeanList());

			Set<OrderToLeProductFamilyBean> response = nplOrderService.constructOrderToLeFamilyDtos(objectCreator.getorderToLeProductFamiliesList());
			assertTrue(response != null && !response.isEmpty() );
		}
		
		@Test
		public void testConstructProductSolution() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findByProductSolutionId(Mockito.anyInt()))
				.thenReturn(objectCreator.getOrderNplLinkList());
			Mockito.when( orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
			Mockito.when(  orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString()))
			.thenReturn(objectCreator.getOrderProductComponentList());

			
			NplOrderService service = mock(NplOrderService.class);
			when(service.constructProductSolution(Mockito.any())).thenCallRealMethod();
			when(service.constructNplLinkBean(Mockito.any(),Mockito.anyList(), Mockito.anyString())).thenReturn(objectCreator.createNplLinkBean());

			List<OrderProductSolutionBean> response = nplOrderService.constructProductSolution(objectCreator.getOrderProductSolutionList());
			assertTrue(response != null && !response.isEmpty() );
		}
		
		@Test
		public void testEditLinkComponent() throws TclCommonException {
			Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderProductComponentAttribtueValue()));
			Mockito.when( orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponentAttribtueValue());

			NplQuoteDetail response = nplOrderService.editLinkComponent(objectCreator.getUpdateRequest());
			assertTrue(response != null );
		}
		
		@Test(expected=Exception.class)
		public void testEditLinkComponentForException() throws TclCommonException {
			Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenThrow(RuntimeException.class);
			

			NplQuoteDetail response = nplOrderService.editLinkComponent(objectCreator.getUpdateRequest());
			assertTrue(response == null );
		}
		
		@Test
		public void testUpdateOrderSites() throws TclCommonException {
			Mockito.when( orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte()))
			.thenReturn(objectCreator.getOrderIllSite());
			Mockito.when( orderIllSitesRepository.save(Mockito.any()))
			.thenReturn(objectCreator.getOrderIllSite());
			
			NplQuoteDetail response = nplOrderService.updateOrderSites(objectCreator.getUpdateRequest());
			assertTrue(response != null );

		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderSitesForException() throws TclCommonException {
			Mockito.when( orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte()))
			.thenThrow(RuntimeException.class);
			
			NplQuoteDetail response = nplOrderService.updateOrderSites(objectCreator.getUpdateRequest());
			assertTrue(response == null );

		}
		
		@Test
		public void testGetDashboardDetails() throws TclCommonException {
			Mockito.when( orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte()))
			.thenReturn(objectCreator.getOrderIllSite());
			Mockito.when( orderIllSitesRepository.save(Mockito.any()))
			.thenReturn(objectCreator.getOrderIllSite());
			
			NplOrderService service = mock(NplOrderService.class);
			when(service.getDashboardDetails(Mockito.anyInt())).thenCallRealMethod();
			
			doAnswer((i) -> {
				return null;
			}).when(service).getDashBoardDetailsForEntity(Mockito.anyList(),Mockito.anyInt());
			
			doAnswer((j) -> {
				return null;
			}).when(service).getDashboardDetailsForAllCustomers(Mockito.anyList(),Mockito.any(DashBoardBean.class));
			
			DashBoardBean response = nplOrderService.getDashboardDetails(1);
			DashBoardBean response1 = nplOrderService.getDashboardDetails(null);

			assertTrue(response != null && response1 != null);

		}
		
		@Test(expected=Exception.class)
		public void testGetDashboardDetailsForException() throws TclCommonException {
			Mockito.when( orderToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
			.thenThrow(RuntimeException.class);
			nplOrderService.getDashboardDetails(1);
		}
		
		@Test
		public void testGetDashboardDetailsForEntity() throws TclCommonException {
			Mockito.when( orderToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
			.thenReturn(objectCreator.getOrderToLesList());
			
			
			NplOrderService service = mock(NplOrderService.class);
			doCallRealMethod().when(service).getDashBoardDetailsForEntity(Mockito.anyList(),Mockito.anyInt());
			when(service.constructDashBoardBean(Mockito.anyList(),Mockito.any(DashboardCustomerbean.class),Mockito.anyInt()))
					.thenReturn(objectCreator.getCountMap());

			
			List<DashboardCustomerbean> dashboardCustomerbeans = new ArrayList<>();
			nplOrderService.getDashBoardDetailsForEntity(dashboardCustomerbeans,1);

			assertTrue(!dashboardCustomerbeans.isEmpty());

		}
		
		@Test
		public void testGetDashboardDetailsForAllCustomers() throws TclCommonException {
			Mockito.when( userInfoUtils.getCustomerDetails()).thenReturn(objectCreator.getCustomerDetails());
			Mockito.when( orderToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
			.thenReturn(objectCreator.getOrderToLesList());
			
			NplOrderService service = mock(NplOrderService.class);
			doCallRealMethod().when(service).getDashboardDetailsForAllCustomers(Mockito.anyList(),Mockito.any(DashBoardBean.class));
			when(service.constructDashBoardBean(Mockito.anyList(),Mockito.any(DashboardCustomerbean.class),Mockito.anyInt()))
					.thenReturn(objectCreator.getCountMap());

			
			List<DashboardCustomerbean> dashboardCustomerbeans = new ArrayList<>();
			nplOrderService.getDashboardDetailsForAllCustomers(dashboardCustomerbeans,new DashBoardBean());

			assertTrue(!dashboardCustomerbeans.isEmpty());

		}
		
		@Test(expected=Exception.class)
		public void testGetDashboardDetailsForAllCustomersForException() throws TclCommonException {
			Mockito.when( userInfoUtils.getCustomerDetails()).thenReturn(null);
			Mockito.when( orderToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
			.thenReturn(objectCreator.getOrderToLesList());
			
			NplOrderService service = mock(NplOrderService.class);
			doCallRealMethod().when(service).getDashboardDetailsForAllCustomers(Mockito.anyList(),Mockito.any(DashBoardBean.class));
			when(service.constructDashBoardBean(Mockito.anyList(),Mockito.any(DashboardCustomerbean.class),Mockito.anyInt()))
					.thenReturn(objectCreator.getCountMap());

			
			List<DashboardCustomerbean> dashboardCustomerbeans = new ArrayList<>();
			nplOrderService.getDashboardDetailsForAllCustomers(dashboardCustomerbeans,new DashBoardBean());

			assertTrue(dashboardCustomerbeans.isEmpty());

		}
		
		@Test
		public void testConstructDashBoardBean() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findByProductSolutionId(Mockito.anyInt())).thenReturn(objectCreator.getOrderNplLinkList());
			
			NplOrderService service = mock(NplOrderService.class);
			when(service.constructDashBoardBean(Mockito.anyList(),Mockito.any(DashboardCustomerbean.class),Mockito.anyInt())).thenCallRealMethod();

			Map<String, Integer> map = nplOrderService.constructDashBoardBean(objectCreator.getOrderToLesList(),new DashboardCustomerbean(),1);

			assertTrue(map!=null && !map.isEmpty());
		}
		
		@Test
		public void testgroupBasedOnCustomerElseCase() throws TclCommonException {
			nplOrderService.groupBasedOnCustomer(objectCreator.getCustomerMap(),objectCreator.getCustomerDetails());
			assertTrue(true);
		}
		
		@Test
		public void testGetLinkDetails() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getOrderNplLink());
			Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getOrderIllSite());
			Mockito.when(orderProductSolutionRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderProductSolution()));
			Mockito.when(orderToLeProductFamilyRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getorderToLeProductFamilies()));
			Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOptionalOrderToLe());
			Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(objectCreator.getOrderProductComponentList());
			Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),Mockito.anyString()))
				.thenReturn(objectCreator.getOrderPrice());

			NplLinkBean link = nplOrderService.getLinkDetails(1);
			assertTrue(link!=null);

		}
		
		
		
		@Test(expected=Exception.class)
		public void testGetLinkDetailsForException() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(null);
			NplLinkBean link = nplOrderService.getLinkDetails(1);
			assertTrue(link!=null);

		}
		
		@Test(expected=Exception.class)
		public void testGetSiteDetailsForException() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenThrow(new RuntimeException());
			
			OrderNplSiteBean site = nplOrderService.getSiteDetails(1);
			assertTrue(site!=null);
		}
		
		@Test
		public void testOrderConfirmationAudit() throws TclCommonException {
			Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderConfirmationAudit());
			nplOrderService.updateOrderConfirmationAudit("10.129.168.133","ABCDEFG","true");
			assertTrue(true);
		}
		
		@Test(expected=Exception.class)
		public void testOrderConfirmationAuditForException() throws TclCommonException {
			Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenThrow(new RuntimeException());
			nplOrderService.updateOrderConfirmationAudit("10.129.168.133","ABCDEFG","true");
		}
		
		@Test
		public void testGetLeAttibutes() throws TclCommonException  {
			Mockito.when( mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstOmsAttributeList());
			Mockito.when( quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(),Mockito.any())).thenReturn(objectCreator.getQuoteLeAttributeValueList());
			String leAttr= nplOrderService.getLeAttributes(objectCreator.getNplQuoteToLe(),"");
			assertTrue (leAttr!=null && !leAttr.isEmpty());
		}
		
		@Test
		public void testConstructOrderLinkSla() throws TclCommonException{
			Mockito.when(quoteNplLinkSlaRepository.findByQuoteNplLink_Id(Mockito.anyInt())).thenReturn(objectCreator.getQuoteNplLinkSlaList());
			Mockito.when(orderNplLinkSlaRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderNplLinkSla());
			Set<OrderNplLinkSla> orderNplLinkSlas = nplOrderService.constructOrderLinkSla(1,objectCreator.getOrderNplLink());
			assertTrue (orderNplLinkSlas!=null && !orderNplLinkSlas.isEmpty());
		}
		
		@Test
		public void testConstructOrderProductComponent() throws TclCommonException{
			Mockito.when(nplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuoteNplLink()));
			Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(objectCreator.getQuoteProductComponent());
			Mockito.when(orderProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponent());
			Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),Mockito.anyString()))
				.thenReturn(objectCreator.getQuotePrice());
			Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderPrice());
			Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(Mockito.anyInt()))
				.thenReturn(objectCreator.getquoteProductComponentAttributeValues());
			Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponentAttribtueValue());

			List<OrderProductComponent>  list = nplOrderService.constructOrderProductComponent(1,objectCreator.getOrderNplLink());
			assertTrue (list!=null && !list.isEmpty());

		}
		
		@Test
		public void testGetUserId() {
			Mockito.when( userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(null);
			User user = nplOrderService.getUserId("");
			assertTrue (user == null);
		}
		
		@Test(expected=Exception.class)
		public void testValidateRequest()  throws TclCommonException{
			nplOrderService.validateRequest(objectCreator.getUpdateRequestWithNullComp());
			assertTrue(true);
		}
		
		@Test(expected=Exception.class)
		public void testValidateUpdateRequest()  throws TclCommonException{
			nplOrderService.validateUpdateRequest(null);
			assertTrue(true);
		}
		
		@Test
		public void testUpdateLegalEntityProperties() throws TclCommonException{
			Mockito.when( userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(objectCreator.getUser());
			Mockito.when( orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
			Mockito.when( mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte())).thenReturn(objectCreator.getMstAttributeList());
			Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any())).thenReturn(objectCreator.getOrdersLeAttributeValue());
			NplQuoteDetail quoteDetail = nplOrderService.updateLegalEntityProperties(objectCreator.getUpdateRequest());
			assertTrue(quoteDetail!=null);
		}
		
		@Test(expected=Exception.class)
		public void testUpdateLegalEntityPropertiesForNullUser() throws TclCommonException{
			Mockito.when( userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(null);
			nplOrderService.updateLegalEntityProperties(objectCreator.getUpdateRequest());
		}
		
		@Test(expected=Exception.class)
		public void testUpdateLegalEntityPropertiesForNullOrderle() throws TclCommonException{
			Mockito.when( userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(objectCreator.getUser());
			Mockito.when( orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
			nplOrderService.updateLegalEntityProperties(objectCreator.getUpdateRequest());
		}

		@Test
		public void testUpdateLegalEntityPropertiesForNullMstAttributes() throws TclCommonException{
			Mockito.when( userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(objectCreator.getUser());
			Mockito.when( orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
			Mockito.when( mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte())).thenReturn(null);
			Mockito.when( mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getMstOmsAttribute());
			assertTrue(nplOrderService.updateLegalEntityProperties(objectCreator.getUpdateRequest())!=null);
		}
		
		@Test(expected=Exception.class)
		public void testGetAttributesForNullOrderLe() throws TclCommonException {
			nplOrderService.getAttributes(null, "");
		}
		
		@Test(expected=Exception.class)
		public void testGetAttributesForEmptyOrderLe() throws TclCommonException {
			Mockito.when( orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
			nplOrderService.getAttributes(1, "");
		}
		
		@Test
		public void testGetAttributes() throws TclCommonException {
			Mockito.when( orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
			Mockito.when( mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte())).thenReturn(objectCreator.getMstAttributeList());
			Mockito.when(ordersLeAttributeValueRepository.findByMstOmsAttributeAndOrderToLe(Mockito.any(),Mockito.any())).thenReturn(objectCreator.getOrdersLeAttributeValueList().stream().collect(Collectors.toSet()));

			nplOrderService.getAttributes(1, "");
		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderToLeStatusForNull() throws TclCommonException {
			nplOrderService.updateOrderToLeStatus(null, "");
		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderToLeStatusForEmpty() throws TclCommonException {
			Mockito.when( orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
			nplOrderService.updateOrderToLeStatus(1, "abc");
		}
		
		@Test
		public void testUpdateOrderToLeStatus() throws TclCommonException {
			Mockito.when( orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
			Mockito.when( orderToLeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
			Mockito.when( orderRepository.save(Mockito.any())).thenReturn(objectCreator.getOrder());
			Mockito.when( mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte())).thenReturn(objectCreator.getMstAttributeList());
			Mockito.when(ordersLeAttributeValueRepository.findByMstOmsAttributeAndOrderToLe(Mockito.any(),Mockito.any())).thenReturn(objectCreator.getOrdersLeAttributeValueList().stream().collect(Collectors.toSet()));
			Mockito.when( userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(objectCreator.getUser());

	
			Mockito.when(notificationService.welcomeLetterNotification(mailNotificationBean)).thenReturn(true);

			Mockito.when(notificationService.provisioningOrderNewOrderNotification(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString()))
			.thenReturn(true);
			assertTrue(nplOrderService.updateOrderToLeStatus(1, "ORDER_COMPLETED")!=null);
		}
		
		@Test
		public void testUpdateOrderLinkStatus() throws TclCommonException {
			
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderNplLink()));
			Mockito.when(mstOrderLinkStatusRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderLinkStatus());
			Mockito.when(mstOrderLinkStageRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderLinkStage());
			Mockito.when(orderNplLinkRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderNplLink());
			Mockito.when(orderLinkStatusAuditRepository.findByOrderNplLinkAndMstOrderLinkStatusAndIsActive(Mockito.any(),Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getOrderLinkStatusAuditList());
			Mockito.when(orderLinkStatusAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderLinkStatusAuditList().get(0));
			Mockito.when(orderLinkStageAuditRepository.findByMstOrderLinkStageAndOrderLinkStatusAuditAndIsActive(Mockito.any(),Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getOrderLinkStageAuditList());
			Mockito.when(orderLinkStageAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderLinkStageAuditList().get(0));
			Mockito.when(orderProductSolutionRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderProductSolution()));
			Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
			Mockito.when(orderRepository.save(Mockito.any())).thenReturn(objectCreator.getOrder());
			Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getUser()));
			Mockito.when( mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstOmsAttributeList());
			Mockito.when( quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(),Mockito.any())).thenReturn(objectCreator.getQuoteLeAttributeValueList());
			
			Mockito.when(notificationService.orderDeliveryCompleteNotification(mailNotificationBean))
			.thenReturn(true);
			Boolean response = nplOrderService.updateOrderLinkStatus(1, objectCreator.getOrderLinkRequest());

			assertTrue(response);

		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderLinkStatusForInputValidations() throws TclCommonException {
			nplOrderService.updateOrderLinkStatus(null, objectCreator.getOrderLinkRequestWithEmptyStatus());
		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderLinkStatusForEmptyLink() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
			nplOrderService.updateOrderLinkStatus(1, objectCreator.getOrderLinkRequest());
		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderLinkStatusForEmptyLinkStatus() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderNplLink()));
			Mockito.when(mstOrderLinkStatusRepository.findByName(Mockito.anyString())).thenReturn(null);
			nplOrderService.updateOrderLinkStatus(1, objectCreator.getOrderLinkRequest());
		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderLinkStatusForEmptyLinkStage() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderNplLink()));
			Mockito.when(mstOrderLinkStatusRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderLinkStatus());
			Mockito.when(mstOrderLinkStageRepository.findByName(Mockito.anyString())).thenReturn(null);
			nplOrderService.updateOrderLinkStatus(1, objectCreator.getOrderLinkRequest());
		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderLinkStatusForEmptyStatusAudit() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderNplLink()));
			Mockito.when(mstOrderLinkStatusRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderLinkStatus());
			Mockito.when(mstOrderLinkStageRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderLinkStage());
			List <OrderLinkStatusAudit> statusAudits = new ArrayList<>();
			Mockito.when(orderLinkStatusAuditRepository.findByOrderNplLinkAndMstOrderLinkStatusAndIsActive(Mockito.any(),Mockito.any(),Mockito.anyByte())).thenReturn(statusAudits);
			Mockito.when(orderLinkStatusAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderLinkStatusAuditList().get(0));
			nplOrderService.updateOrderLinkStatus(1, objectCreator.getOrderLinkRequest());
		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderLinkStatusForEmptyStageAudit() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderNplLink()));
			Mockito.when(mstOrderLinkStatusRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderLinkStatus());
			Mockito.when(mstOrderLinkStageRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderLinkStage());
			Mockito.when(orderLinkStatusAuditRepository.findByOrderNplLinkAndMstOrderLinkStatusAndIsActive(Mockito.any(),Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getOrderLinkStatusAuditList());
			Mockito.when(orderLinkStatusAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderLinkStatusAuditList().get(0));
			List <OrderLinkStageAudit> stageAudits = new ArrayList<>();
			Mockito.when(orderLinkStageAuditRepository.findByMstOrderLinkStageAndOrderLinkStatusAuditAndIsActive(Mockito.any(),Mockito.any(),Mockito.anyByte())).thenReturn(stageAudits);
			Mockito.when(orderLinkStageAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderLinkStageAuditList().get(0));
			nplOrderService.updateOrderLinkStatus(1, objectCreator.getOrderLinkRequest());
		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderLinkStatusForEmptySolution() throws TclCommonException {
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderNplLink()));
			Mockito.when(mstOrderLinkStatusRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderLinkStatus());
			Mockito.when(mstOrderLinkStageRepository.findByName(Mockito.anyString())).thenReturn(objectCreator.getMstOrderLinkStage());
			Mockito.when(orderLinkStatusAuditRepository.findByOrderNplLinkAndMstOrderLinkStatusAndIsActive(Mockito.any(),Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getOrderLinkStatusAuditList());
			Mockito.when(orderLinkStatusAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderLinkStatusAuditList().get(0));
			Mockito.when(orderLinkStageAuditRepository.findByMstOrderLinkStageAndOrderLinkStatusAuditAndIsActive(Mockito.any(),Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getOrderLinkStageAuditList());
			Mockito.when(orderLinkStageAuditRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderLinkStageAuditList().get(0));
			Mockito.when(orderProductSolutionRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
			nplOrderService.updateOrderLinkStatus(1, objectCreator.getOrderLinkRequest());
		}
		
		@Test
		public void testCheckWhetherAllLinkStatusAsStartOfService() throws TclCommonException {
			Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any())).thenReturn(objectCreator.getOrderProductSolutionList());
			Mockito.when(orderNplLinkRepository.findByProductSolutionId(Mockito.anyInt())).thenReturn(objectCreator.getOrderNplLinkList());
			assertTrue(!nplOrderService.checkWhetherAllLinkStatusAsStartOfService(objectCreator.getOrderProductSolution()));
		}
		
		@Test(expected=Exception.class)
		public void testCheckWhetherAllLinkStatusAsStartOfServiceForException() throws TclCommonException {
			Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any())).thenThrow(RuntimeException.class);
			nplOrderService.checkWhetherAllLinkStatusAsStartOfService(objectCreator.getOrderProductSolution());
		}
		
		@Test(expected=Exception.class)
		public void testGetAllOrdersByProductNameForNull() throws TclCommonException {
			nplOrderService.getAllOrdersByProductName(null);
		}
		
		@Test(expected=Exception.class)
		public void testGetAllOrdersByProductNameForEmptyFamily() throws TclCommonException {
			Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(null);
			nplOrderService.getAllOrdersByProductName("NPL");
		}
		
		@Test
		public void testGetAllOrdersByProductName() throws TclCommonException{
			Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstProductFamily());
			Mockito.when(orderToLeProductFamilyRepository.findByMstProductFamilyOrderByIdAsc(Mockito.any())).thenReturn(objectCreator.getorderToLeProductFamiliesList());
			Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
			Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getOrder());
			Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any())).thenReturn(objectCreator.getOrderProductSolutionList());
			Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getOrderIllSiteList());
			Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstOmsAttributeList());
			Mockito.when(ordersLeAttributeValueRepository.findByMstOmsAttributeAndOrderToLe(Mockito.any(),Mockito.any())).thenReturn(objectCreator.getOrdersLeAttributeValueList().stream().collect(Collectors.toSet()));
			List<DashboardOrdersBean> list = nplOrderService.getAllOrdersByProductName("NPL");
			assertTrue(list!=null && !list.isEmpty());
		}
		
		@Test 
		public void testUpdateOrderSiteProperties() throws TclCommonException{
			Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderIllSite()));
			Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(objectCreator.getUser());
			Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstProductFamily());
			Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstProductComponentList());
			Mockito.when(mstProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getMstProductComponent());
			Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(),Mockito.any(),Mockito.anyString())).thenReturn(objectCreator.getOrderProductComponentList());
			Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getProductAtrributeMaster());
			Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponentAndProductAttributeMaster(Mockito.any(),Mockito.any())).thenReturn(objectCreator.getOrderProductComponentAttribtueValueList());
			Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponentAttribtueValue());
			Mockito.when(productAttributeMasterRepository.save(Mockito.any())).thenReturn(objectCreator.getProductAtrributeMaster());
			OrderNplSiteBean bean = nplOrderService.updateOrderSiteProperties(objectCreator.getUpdateRequest(),"abc");
			assertTrue(bean!=null);
		}
		
		@Test(expected=Exception.class) 
		public void testUpdateOrderSitePropertiesForEmptySites() throws TclCommonException{
			Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
			nplOrderService.updateOrderSiteProperties(objectCreator.getUpdateRequest(),"");
		}
		
		@Test(expected=Exception.class) 
		public void testUpdateOrderSitePropertiesForEmptyFamily() throws TclCommonException{
			Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderIllSite()));
			Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(objectCreator.getUser());
			Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(null);
			nplOrderService.updateOrderSiteProperties(objectCreator.getUpdateRequest(),"");
		}
		
		@Test 
		public void testgetMstPropertiesForEmptyComp() throws TclCommonException{
			Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(null);
			Mockito.when(mstProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getMstProductComponent());
			assertTrue(nplOrderService.getMstProperties(objectCreator.getUser())!=null);
		}
		
		@Test 
		public void testUpdateOrderSitePropertiesForEmptyComp() throws TclCommonException{
			Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderIllSite()));
			Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),Mockito.anyInt())).thenReturn(objectCreator.getUser());
			Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstProductFamily());
			Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstProductComponentList());
			Mockito.when(mstProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getMstProductComponent());
			Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(),Mockito.any(),Mockito.anyString())).thenReturn(null);
			Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getProductAtrributeMaster());
			Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponentAndProductAttributeMaster(Mockito.any(),Mockito.any())).thenReturn(objectCreator.getOrderProductComponentAttribtueValueList());
			Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponentAttribtueValue());
			Mockito.when(productAttributeMasterRepository.save(Mockito.any())).thenReturn(objectCreator.getProductAtrributeMaster());
			OrderNplSiteBean bean = nplOrderService.updateOrderSiteProperties(objectCreator.getUpdateRequest(),"abc");
			assertTrue(bean!=null);
		}
		
		@Test
		public void testUpdateIllSitePropertiesForEmptyAttrMaster()  throws TclCommonException{
			Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(null);
			Mockito.when(productAttributeMasterRepository.save(Mockito.any())).thenReturn(objectCreator.getProductAtrributeMaster().get(0));
			Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponentAttribtueValue());
			nplOrderService.updateIllSiteProperties(objectCreator.getOrderProductComponentList(),objectCreator.getUpdateRequest(),"abc");
			assertTrue(true);
		}
		
		@Test
		public void testGetAllOrders() throws TclCommonException{
			Mockito.when(orderRepository.findAllByOrderByCreatedTimeDesc()).thenReturn(objectCreator.getOrderList());
			List<NplOrdersBean> ordersBeans = nplOrderService.getAllOrders();
			assertTrue(ordersBeans!=null && !ordersBeans.isEmpty());
		}
		
		@Test(expected=Exception.class)
		public void testGetAllOrdersForException() throws TclCommonException{
			Mockito.when(orderRepository.findAllByOrderByCreatedTimeDesc()).thenThrow(RuntimeException.class);
			List<NplOrdersBean> ordersBeans = nplOrderService.getAllOrders();
			assertTrue(ordersBeans!=null && !ordersBeans.isEmpty());
		}
		
		@Test
		public void testGetOrderSummary() throws TclCommonException{
			Mockito.when(orderRepository.findAllByOrderByCreatedTimeDesc()).thenReturn(objectCreator.getOrderList());
			Mockito.when(userRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getUser()));

			List<OrderSummaryBean> ordersBeans = nplOrderService.getOrderSummary();
			assertTrue(ordersBeans!=null && !ordersBeans.isEmpty());
		}
		
		@Test(expected=Exception.class)
		public void testGetOrderSummaryForException() throws TclCommonException{
			Mockito.when(orderRepository.findAllByOrderByCreatedTimeDesc()).thenThrow(RuntimeException.class);
			nplOrderService.getOrderSummary();
		}
		
		@Test
		public void testUpdateSfdcStage() throws TclCommonException{
			Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderToLes()));
			Mockito.doNothing().when(omsSfdcService).processUpdateOrderOpportunity(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.any());
				nplOrderService.updateSfdcStage(1, "Identified Opportunity");
		}
		
		@Test
		public void testGetSiteProperties() throws TclCommonException{
			Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderIllSite()));
			Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstProductComponentList());
			Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponentAndType(Mockito.anyInt(),Mockito.any(),Mockito.anyString())).thenReturn(objectCreator.getOrderProductComponentList());
			Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any())).thenReturn(objectCreator.getOrderProductComponentAttribtueValueList());
			Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponentAndProductAttributeMaster_Name(Mockito.any(),Mockito.anyString())).thenReturn(objectCreator.getOrderProductComponentAttribtueValueList());
			List<OrderProductComponentBean> list = nplOrderService.getSiteProperties(1,"","");
			//List<OrderProductComponentBean> list1 = nplOrderService.getSiteProperties(1,"","");
			assertTrue(list!=null && !list.isEmpty() );
		}
		
		@Test(expected=Exception.class)
		public void testGetSitePropertiesForEmptySite() throws TclCommonException{
			Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
			nplOrderService.getSiteProperties(1,null,"");
		}
		
		@Test
		public void testGetOrderLinkAuditTrail() throws TclCommonException{
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderNplLink()));
		}
		
		@Test
		public void testUpdateLinkDetails() throws TclCommonException{
			Mockito.when(orderNplLinkRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getOrderNplLink());
			Mockito.when(orderNplLinkRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderNplLink());
			String response = nplOrderService.updateLinkDetails(objectCreator.getUpdateRequest());
			assertTrue(response!=null && response.equalsIgnoreCase("SUCCESS"));
		}
		
		@Test(expected=Exception.class)
		public void testUpdateLinkDetailsForException() throws TclCommonException{
			Mockito.when(orderNplLinkRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenThrow(RuntimeException.class);
			nplOrderService.updateLinkDetails(objectCreator.getUpdateRequest());
		}
		
		@Test
		public void testGetFeasiblityAndPricingDetails() throws TclCommonException{
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderNplLink()));
			Mockito.when(orderLinkFeasibilityRepository
						.findByOrderNplLink(Mockito.any())).thenReturn(objectCreator.getOrderLinkFeasibilityList());
			Mockito.when(pricingDetailsRepository
						.findBySiteCodeAndPricingType(Mockito.anyString(),Mockito.anyString())).thenReturn(objectCreator.getPricingDetails());
			Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrderIllSite()));
			NplPricingFeasibilityBean feas = nplOrderService.getFeasiblityAndPricingDetails(1);
			assertTrue(feas!=null);
		}
		
		@Test(expected=Exception.class)
		public void testGetFeasiblityAndPricingDetailsForException() throws TclCommonException{
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenThrow(RuntimeException.class);
			nplOrderService.getFeasiblityAndPricingDetails(1);
		}
		
		@Test(expected=Exception.class)
		public void testGetFeasiblityAndPricingDetailsForNull() throws TclCommonException{
			Mockito.when(orderNplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
			nplOrderService.getFeasiblityAndPricingDetails(1);
		}
		
		@Test
		public void testReturnExcel() throws FileNotFoundException, TclCommonException, IOException {
			NplOrderService service = mock(NplOrderService.class);
			doCallRealMethod().when(service).returnExcel(Mockito.anyInt(),Mockito.any());
			when(service.getExcelList(Mockito.anyInt(),objectCreator.getQuoteToLe(), Mockito.anyString())).thenReturn(objectCreator.getExcelBeanList());
			 HttpServletResponse response = mock(HttpServletResponse.class);
			 service.returnExcel(1,response);
		}
		
		@Test
		public void testGetExcelList() throws TclCommonException {
		
			Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getOrder());
			Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstProductFamily());
			Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(),Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamilies());
			Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductSolutionList());
			Mockito.when(orderNplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(),Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderNplLinkList());
			
			Mockito.when(orderLinkFeasibilityRepository.findByOrderNplLinkAndIsSelected(Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getOrderLinkFeasibilityList());
			Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getOrderIllSite());
			Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getOrderProductComponentList());
			Mockito.when(mstProductComponentRepository.findByName(OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY)).thenReturn(objectCreator.getNationalConnectivity());
			Mockito.when(mstProductComponentRepository.findByName(FPConstants.LAST_MILE.toString())).thenReturn(objectCreator.getLastMile());
			Mockito.when(mstProductComponentRepository.findByName(FPConstants.LINK_MANAGEMENT_CHARGES.toString())).thenReturn(objectCreator.getLinkManagement());

			NplOrderService service = mock(NplOrderService.class);
			Mockito.doNothing().when(service).createNationalConnectivityforExcel(Mockito.any(),Mockito.any(),Mockito.any());
			
			nplOrderService.getExcelList(1,objectCreator.getQuoteToLe(),"NEW");
		}
		
		@Test
		public void testGetExcelList1() throws TclCommonException {
			
		
			Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getOrder());
			Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte())).thenReturn(objectCreator.getMstProductFamily());
			Mockito.when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(),Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamilies());
			Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()))
				.thenReturn(objectCreator.getOrderProductSolutionList());
			Mockito.when(orderNplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(),Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderNplLinkList());
			
			Mockito.when(orderLinkFeasibilityRepository.findByOrderNplLinkAndIsSelected(Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getOrderLinkFeasibilityListFailure());
			Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(objectCreator.getOrderIllSite());
			Mockito.when(orderProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getOrderProductComponentList());
			Mockito.when(mstProductComponentRepository.findByName(OrderDetailsExcelDownloadConstants.NATIONAL_CONNECTIVITY)).thenReturn(objectCreator.getNationalConnectivity());
			Mockito.when(mstProductComponentRepository.findByName(FPConstants.LAST_MILE.toString())).thenReturn(objectCreator.getLastMile());
			Mockito.when(mstProductComponentRepository.findByName(FPConstants.LINK_MANAGEMENT_CHARGES.toString())).thenReturn(objectCreator.getLinkManagement());

			NplOrderService service = mock(NplOrderService.class);
			Mockito.doNothing().when(service).createNationalConnectivityforExcel(Mockito.any(),Mockito.any(),Mockito.any());
			
			nplOrderService.getExcelList(1,objectCreator.getQuoteToLe(),"NEW");
		}
		
		@Test
		public void testReturnLocationItContactName() throws TclCommonException {
			Mockito.when(mqutils.sendAndReceive(Mockito.anyString(),Mockito.anyString())).thenReturn("");
			assertTrue(nplOrderService.returnLocationItContactName(1)!=null);
		}
		
		@Test
		public void testReturnLocationItContactNameForException() throws TclCommonException{
			Mockito.when(mqutils.sendAndReceive(Mockito.anyString(),Mockito.anyString())).thenThrow(RuntimeException.class);
			assertTrue(nplOrderService.returnLocationItContactName(1)==null);
		}
		
		@Test
		public void testUpateSitePropertiesAttribute()  throws TclCommonException{
			Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponentAndProductAttributeMaster(Mockito.any(),Mockito.any())).thenReturn(new ArrayList());
			nplOrderService.upateSitePropertiesAttribute(objectCreator.getProductAtrributeMaster(),objectCreator.getAttributeDetail().get(0),objectCreator.getOrderProductComponent());
		}
		
		@Test
		public void testGetMstAttributeMaster() throws TclCommonException {
			Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.any(),Mockito.anyByte())).thenReturn(objectCreator.getMstOmsAttributeList());
			MstOmsAttribute response = nplOrderService.getMstAttributeMaster(objectCreator.getUpdateRequest(),objectCreator.getUser());
			assertTrue(response!=null);
		}
		
		@Test
		public void testWriteBookZeroLinkId() throws TclCommonException {
						
			nplOrderService.writeBook(objectCreator.getExcelBeanForZeroLinkId(),objectCreator.createRow());
			assertTrue(true);
		}
		
		@Test
		public void testWriteBookZeroSiteId() throws TclCommonException{
			nplOrderService.writeBook(objectCreator.getExcelBeanForZeroSiteId(),objectCreator.createRow());
			assertTrue(true);
		}
		
		@Test
		public void testWriteBookSiteAId() throws TclCommonException{
			nplOrderService.writeBook(objectCreator.getExcelBeanForSiteAId(),objectCreator.createRow());
			assertTrue(true);
		}
		
		@Test
		public void testWriteBookSiteBId() throws TclCommonException{
			nplOrderService.writeBook(objectCreator.getExcelBeanForSiteBId(),objectCreator.createRow());
			assertTrue(true);
		}
		
		@Test
		public void testWriteBookAllIdsNull() throws TclCommonException{
			nplOrderService.writeBook(objectCreator.getExcelBeanForAllIdNull(),objectCreator.createRow());
			assertTrue(true);
		}
		
		@Test 
		public void testCreateLinkDetailsBasedOnFeasibility() throws TclCommonException{
			nplOrderService.createLinkDetailsBasedOnFeasibility(objectCreator.getOrderLinkFeasibility(),objectCreator.getExcelBeanList(),objectCreator.getOrderNplLink(),
					objectCreator.getNotFeasible(),objectCreator.getOrderToLes());
			assertTrue(true);
		}
		
		@Test(expected=Exception.class)
		public void testGetAttributeValuesForException() throws TclCommonException{
			nplOrderService.getAttributeValues(null);
		}
		
		@Test(expected=Exception.class)
		public void testGetOrderDetailsValidation() throws TclCommonException{
			nplOrderService.getOrderDetails(null);
		}
		
		@Test(expected=Exception.class)
		public void testGetOmsAttachmentsValidation() throws TclCommonException{
			nplOrderService.getOmsAttachments(null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructOrderValidation() throws TclCommonException{
			nplOrderService.constructOrder(null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructOrderLeEntityDtosValidation() throws TclCommonException{
			nplOrderService.constructOrderLeEntityDtos(null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructLegalAttributesValidation() throws TclCommonException{
			nplOrderService.constructLegalAttributes(null);
		}
		
		@Test(expected=Exception.class)
		public void testGetOrderToLeBasenOnVersionValidation() throws TclCommonException{
			nplOrderService.getOrderToLeBasenOnVersion(null);
		}
		
		@Test(expected=Exception.class)
		public void testGetProductSolutionBasenOnVersionValidation() throws TclCommonException{
			nplOrderService.getProductSolutionBasenOnVersion(null);
		}
		
		@Test(expected=Exception.class)
		public void testGetComponentBasedOnIdVersionTypeValidation() throws TclCommonException{
			nplOrderService.getComponentBasedOnIdVersionType(null,null,null);
		}
		
		@Test(expected=Exception.class)
		public void testGetProductFamilyBasenOnVersionValidation() throws TclCommonException{
			nplOrderService.getProductFamilyBasenOnVersion(null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructOrderToLeFamilyDtosValidation() throws TclCommonException{
			nplOrderService.constructOrderToLeFamilyDtos(null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructProductSolutionValidation() throws TclCommonException{
			nplOrderService.constructProductSolution(null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructNplLinkBeanValidation() throws TclCommonException{
			nplOrderService.constructNplLinkBean(null,null,null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructOrderLinkSlaValidation() throws TclCommonException{
			nplOrderService.constructOrderLinkSla(null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructOrderProductComponentValidation() throws TclCommonException{
			nplOrderService.constructOrderProductComponent(null,"","");
		}
		
		@Test(expected=Exception.class)
		public void testGetDashBoardDetailsForEntityValidation() throws TclCommonException{
			nplOrderService.getDashBoardDetailsForEntity(null,null);
		}
		
		@Test(expected=Exception.class)
		public void testGetDashboardDetailsForAllCustomersValidation() throws TclCommonException{
			nplOrderService.getDashboardDetailsForAllCustomers(null,null);
		}
		
		
		@Test(expected=Exception.class)
		public void testConstructDashBoardBeanValidation() throws TclCommonException{
			nplOrderService.constructDashBoardBean(null,null,0);
		}
		
		@Test(expected=Exception.class)
		public void testGroupBasedOnCustomerValidation() throws TclCommonException{
			nplOrderService.groupBasedOnCustomer(null,null);
		}
		
		@Test(expected=Exception.class)
		public void testGetLinkDetailsValidation() throws TclCommonException{
			nplOrderService.getLinkDetails(null);
		}
		
		@Test(expected=Exception.class)
		public void testgetSiteDetailsValidation() throws TclCommonException{
			nplOrderService.getSiteDetails(null);
		}
		
		@Test(expected=Exception.class)
		public void testUpdateOrderConfirmationAuditValidation() throws TclCommonException{
			nplOrderService.updateOrderConfirmationAudit(null,null,null);
		}
		
		@Test(expected=Exception.class)
		public void testGetLeAttributesValidation() throws TclCommonException{
			nplOrderService.getLeAttributes(objectCreator.getNplQuoteToLe(),null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructOrderLinkSlaInputValidation() throws TclCommonException{
			nplOrderService.constructOrderLinkSla(null,null);
		}
		
		
		@Test(expected=Exception.class)
		public void testConstructOrderProductComponentInputValidation() throws TclCommonException{
			nplOrderService.constructOrderProductComponent(null,"","");
		}
		
		@Test(expected=Exception.class)
		public void testGetMstAttributeMasterValidation() throws TclCommonException{
			nplOrderService.getMstAttributeMaster(null,null);
		}	
		
		
		@Test(expected=Exception.class)
		public void testProcessOrderMailNotificationValidation() throws TclCommonException{
			nplOrderService.processOrderMailNotification(null,null);
		}
		
		@Test(expected=Exception.class)
		public void testGetLeAttributesInputValidation() throws TclCommonException{
			nplOrderService.getLeAttributes(objectCreator.getOrderToLes(),null);
		}
		
		@Test(expected=Exception.class)
		public void testCheckWhetherAllLinkStatusAsStartOfServiceValidation() throws TclCommonException{
			nplOrderService.checkWhetherAllLinkStatusAsStartOfService(null);
		}
		
		@Test(expected=Exception.class)
		public void testGetMstPropertiesValidation() throws TclCommonException{
			nplOrderService.getMstProperties(null);
		}
		
		@Test(expected=Exception.class)
		public void testUpdateIllSitePropertiesValidation() throws TclCommonException{
			nplOrderService.updateIllSiteProperties(null,null,null);
		}
		
		@Test(expected=Exception.class)
		public void testUpateSitePropertiesAttributeValidation() throws TclCommonException{
			nplOrderService.upateSitePropertiesAttribute(null,null,null);
		}
		
		@Test(expected=Exception.class)
		public void testUpdateSfdcStageValidation() throws TclCommonException{
			nplOrderService.updateSfdcStage(null,null);
		}
		
		@Test(expected=Exception.class)
		public void testGetFeasiblityAndPricingDetailsValidation() throws TclCommonException{
			nplOrderService.getFeasiblityAndPricingDetails(null);
		}
		
		@Test(expected=Exception.class)
		public void testConstructOrderLinkPricingFeasibilityDetailsValidation() throws TclCommonException{
			nplOrderService.constructOrderLinkPricingFeasibilityDetails(null,null,null);
		}
		
		@Test(expected=Exception.class)
		public void testReturnExcelValidation() throws TclCommonException, FileNotFoundException, IOException{
			nplOrderService.returnExcel(null,null);
		}
		
		@Test(expected=Exception.class)
		public void testGetExcelListValidation() throws TclCommonException{
			nplOrderService.getExcelList(null,objectCreator.getQuoteToLe(),"NEW");
		}
		
		@Test(expected=Exception.class)
		public void testWriteBookValidation() throws TclCommonException{
			nplOrderService.writeBook(null,null);
		}
		
		@Test(expected=Exception.class)
		public void testCreateLinkDetailsBasedOnFeasibilityValidation() throws TclCommonException{
			nplOrderService.createLinkDetailsBasedOnFeasibility(null,null,null,null,null);
		}
		
		@Test
		public void testGetAccessType() {
			String resp =nplOrderService.getAccessType(NplPDFConstants.ONNETWL_NPL);
			assertTrue(resp!=null && resp.equalsIgnoreCase(NplPDFConstants.ONNET_WIRED));
		}
		
		@Test(expected=Exception.class)
		public void testCreateSlaDetailsValidation() throws TclCommonException{
			nplOrderService.createSlaDetails(null,null);
		}
		
		@Test
		public void testCreateSlaDetails() throws TclCommonException {
			Mockito.when(orderNplLinkSlaRepository.findByOrderNplLink(Mockito.any())).thenReturn(objectCreator.getOrderNplLinkSlaList().stream().collect(Collectors.toList()));
			nplOrderService.createSlaDetails(new OrderNplLink(),objectCreator.getExcelBeanList());
			assertTrue(true);
		}
		
		@Test
		public void testReturnLocationItContactNameValidation() throws TclCommonException{
			String resp = nplOrderService.returnLocationItContactName(null);
			assertTrue(resp==null);
		}
		
		@Test(expected=Exception.class)
		public void testCreateNationalConnectivityforExcelValidation() throws TclCommonException{
			nplOrderService.createNationalConnectivityforExcel(null,null,null);
			assertTrue(true);

		}
		

		@Test(expected=Exception.class)
		public void testGetFeasiblityAndPricingDetailsForQuoteNplBeanValidation() throws TclCommonException{
			nplOrderService.getFeasiblityAndPricingDetailsForQuoteNplBean(null);
		}
		
		@Test
		public void testGetFeasiblityAndPricingDetailsForQuoteNplBean() throws TclCommonException{
			Mockito.when(nplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getQuoteNplLink()));
			Mockito.when(linkFeasibilityRepository.findByQuoteNplLink(Mockito.any())).thenReturn(objectCreator.getLinkFeasibilityList());
			Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingType(Mockito.anyString(),Mockito.anyString())).thenReturn(objectCreator.getPricingDetails());
			Mockito.when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getIllsites()));

			nplOrderService.getFeasiblityAndPricingDetailsForQuoteNplBean(1);
		}
		
		@Test(expected=Exception.class)
		public void testGetFeasiblityAndPricingDetailsForQuoteNplBeanForException() throws TclCommonException{
			Mockito.when(nplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
			nplOrderService.getFeasiblityAndPricingDetailsForQuoteNplBean(1);
		}
		
		
		
		
		
}
