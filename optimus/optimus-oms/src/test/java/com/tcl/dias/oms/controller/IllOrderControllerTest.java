package com.tcl.dias.oms.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.AuditBean;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.DashboardOrdersBean;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrderSiteRequest;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.dto.OrderToLeDto;
import com.tcl.dias.oms.entity.entities.OrderIllSite;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderSiteProvisionAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStageAudit;
import com.tcl.dias.oms.entity.entities.OrderSiteStatusAudit;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStageRepository;
import com.tcl.dias.oms.entity.repository.MstOrderSiteStatusRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
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
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.controller.v1.IllOrderController;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IllOrderControllerTest.java class. This class contains
 * all the test cases for the IllOrderControllerTest
 * 
 *
 * @author Kusuma Kumar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
public class IllOrderControllerTest {

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
	ObjectCreator objectCreator;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@Autowired
	IllOrderController illOrderController;

	@MockBean
	private QuoteToLeRepository quoteToLeRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;

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
	OrderSiteFeasibilityRepository orderSiteFeasibilityRepository;

	/**
	 * 
	 * init- predefined mocks
	 */
	@Before
	public void init() {

		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
				.thenReturn((objectCreator.getOrderToLesList()));
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any())).thenReturn((objectCreator.getOrderProductSolutionList()));
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderIllSiteList()));
		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn((objectCreator.getOrderProductComponentList()));
		Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createOrderProducts()));
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.doThrow(new RuntimeException()).when(orderProductComponentsAttributeValueRepository)
				.save(new OrderProductComponentsAttributeValue());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSite());
		Mockito.when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrders());
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderIllSite()));
		Mockito.when(orderToLeRepository.findByOrder(Mockito.any()))
				.thenReturn((objectCreator.getOrderToLesList()));
		Mockito.when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn((objectCreator.getOrderProductSolutionList()));
		Mockito.when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				 Mockito.anyByte())).thenReturn((objectCreator.getOrderIllSiteList()));
		Mockito.when(
				orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn((objectCreator.getOrderProductComponentList()));
		Mockito.when(
				orderProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.any(), Mockito.any()))
				.thenReturn((objectCreator.getOrderProductComponentList()));
		Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createOrderProducts()));
		Mockito.when(orderToLeProductFamilyRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.doThrow(new RuntimeException()).when(orderProductComponentsAttributeValueRepository)
				.save(new OrderProductComponentsAttributeValue());
		Mockito.when(orderPriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(objectCreator.getOrderPrice());

		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrder()));
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.geProductFamily());
		when(orderToLeProductFamilyRepository.findByOrderToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
		.thenReturn(objectCreator.getorderToLeProductFamilies());

	}

	/**
	 * test create Order possitive test case
	 **/
	@Test
	public void testgetOrderDetails() throws TclCommonException {
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValueList());

		ResponseResource<OrdersBean> response = illOrderController.getOrderDetails(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create Order negative test case
	 **/
	@Test
	public void testgetOrderDetailsForNull() throws TclCommonException {
		ResponseResource<OrdersBean> response = illOrderController.getOrderDetails(null);
		assertTrue(response.getData() == null);
	}

	/**
	 * test create Order negative test case
	 **/
	@Test
	public void testeditOrderSitesForNull() throws TclCommonException {
		Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createOrderProducts()));
		ResponseResource<QuoteDetail> response = illOrderController.editOrderSites(null, null, null, null);
		assertTrue(response.getData() == null);
	}

	/*
	 * Edit Order sites - negative test case - Component validation - exception
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testEditOrderSitesWithoutComponentDetails() throws TclCommonException {

		ResponseResource<QuoteDetail> response = illOrderController.editOrderSites(null, null,
				objectCreator.returnUpdateRequestWithoutComponentDetails(), null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * Edit Order sites - Positive test case - Invalid attribute id
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testEditOrderSitesWithInvalidAttributeId() throws TclCommonException {
		Mockito.when(orderProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.empty());

		ResponseResource<QuoteDetail> response = illOrderController.editOrderSites(1, 1,
				objectCreator.returnUpdateRequestForInvalidAttributeId(), 1);
		assertTrue(response.getData() == null || response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create Order negative test case
	 **/
	@Test
	public void testeditOrderSites() throws TclCommonException {
		UpdateRequest updateRequest = new UpdateRequest();
		ComponentDetail componentDetails = new ComponentDetail();
		AttributeDetail attributeDetail = new AttributeDetail();
		List<AttributeDetail> attributeDetailList = new ArrayList<>();
		List<ComponentDetail> componentDetailsList = new ArrayList<>();
		attributeDetail.setAttributeId(1);
		attributeDetail.setAttributeMasterId(1);
		attributeDetail.setName("tata");
		attributeDetail.setValue("tata");
		attributeDetailList.add(attributeDetail);
		componentDetails.setAttributes(attributeDetailList);
		componentDetails.setComponentId(1);
		componentDetails.setComponentMasterId(1);
		componentDetailsList.add(componentDetails);
		updateRequest.setSiteId(1);
		updateRequest.setQuoteId(1);
		updateRequest.setComponentDetails(componentDetailsList);
		updateRequest.setRequestorDate(new Timestamp(new Date().getTime()));
		ResponseResource<QuoteDetail> response = illOrderController.editOrderSites(null, null, updateRequest, null);
		// ResponseResource<QuoteDetail> response =
		// illOrderController.editOrderSites(updateRequest);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create testupdateOrderSites possitive test case
	 **/
	@Test
	public void testupdateOrderSites() throws TclCommonException {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.setSiteId(1);
		updateRequest.setQuoteId(1);
		ResponseResource<QuoteDetail> response = illOrderController.updateOrderSites(null, null, updateRequest);
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderSites(updateRequest);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create testupdateOrderSites negative test case
	 **/
	@Test
	public void testgetOrderSitesForNull() throws TclCommonException {
		ResponseResource<QuoteDetail> response = illOrderController.updateOrderSites(null, null, null);
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderSites(null);
		assertTrue(response.getData() == null);
	}

	/**
	 * test create testupdateOrderSites possitive test case
	 **/
	@Test
	public void testgetSiteDetails() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderIllSiteForSiteDetails());
		Mockito.when(orderToLeProductFamilyRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getorderToLeProductFamilies()));
		Mockito.when(orderProductSolutionRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderProductSolutionWithOrderToLeProductFamiles()));

		ResponseResource<OrderIllSiteBean> response = illOrderController.getSiteDetails(1, 1, 1);
		// ResponseResource<OrderIllSiteBean> response =
		// illOrderController.getSiteDetails(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create testupdateOrderSites negative test case
	 **/
	@Test
	public void testgetSiteDetailsForNull() throws TclCommonException {
		ResponseResource<OrderIllSiteBean> response = illOrderController.getSiteDetails(null, null, null);
		// ResponseResource<OrderIllSiteBean> response =
		// illOrderController.getSiteDetails(null);
		assertTrue(response.getData() == null);
	}

	/**
	 * test create testupdateOrderSites possitive test case
	 **/
	@Test
	public void testgetAttachments() throws TclCommonException {
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getAttachmentsList());
		ResponseResource<List<OmsAttachBean>> response = illOrderController.getAttachments(1, 1);
		// ResponseResource<List<OmsAttachBean>> response =
		// illOrderController.getAttachments(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS && !response.getData().isEmpty());
	}

	/**
	 * test create testupdateOrderSites negative test case
	 **/
	@Test
	public void testgetAttachmentsForNull() throws TclCommonException {
		Mockito.when(omsAttachmentRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getAttachmentsList());
		ResponseResource<List<OmsAttachBean>> response = illOrderController.getAttachments(null, null);
		// ResponseResource<List<OmsAttachBean>> response =
		// illOrderController.getAttachments(null);
		assertTrue(response.getData() == null || response.getData().isEmpty());
	}

	/**
	 * test create testupdateOrderSites Negative test case - Unsatisfied attachment
	 * information
	 **/
	@Test
	public void testgetAttachmentsUnsatisfiedAttachementInfo() throws TclCommonException {
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(objectCreator.getAttachmentsListNegative());
		ResponseResource<List<OmsAttachBean>> response = illOrderController.getAttachments(1, null);
		// ResponseResource<List<OmsAttachBean>> response =
		// illOrderController.getAttachments(1);
		assertTrue(response.getData() == null || response.getData().isEmpty());
	}

	/**
	 * test create for dash board for active orders
	 **/
	@Test
	public void testForGetDashBoard() throws TclCommonException {

		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);

		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());

		Mockito.when(orderToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
				.thenReturn(objectCreator.getOrderToLesListForDashBoard());

		ResponseResource<DashBoardBean> response = illOrderController.getDashboardDetails(1);

		assertTrue(response.getData() != null);
	}

	/**
	 * test create for dash board for Inactive orders
	 **/
	@Test
	public void testForGetDashBoardForInactive() throws TclCommonException {

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());

		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);

		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		ResponseResource<DashBoardBean> response = illOrderController.getDashboardDetails(null);
		assertTrue(response.getData() != null);
	}

	/**
	 * test case for update legal entity Properties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityProperties() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getMstOmsAttribute());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValue());
		Mockito.when(
				ordersLeAttributeValueRepository.findByOrderToLe(Mockito.anyObject()))
				.thenReturn(objectCreator.returnOrdersLeAttributeValueList());
		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		ResponseResource<QuoteDetail> response = illOrderController.updateLeaglEntityAttribute(null, null,
				objectCreator.getUpdateRequest());
		// .updateLeaglEntityAttribute(objectCreator.getUpdateRequest());
		assertTrue(response.getData() != null);

	}

	/**
	 * test case for update legal entity Properties- Negative case -Without User
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithUserNull() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValue());
		Mockito.when(
				ordersLeAttributeValueRepository.findByOrderToLe(Mockito.anyObject()))
				.thenReturn(objectCreator.returnOrdersLeAttributeValueList());

		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		ResponseResource<QuoteDetail> response = illOrderController.updateLeaglEntityAttribute(null, null,
				objectCreator.getUpdateRequest());
		// .updateLeaglEntityAttribute(objectCreator.getUpdateRequest());
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);

	}

	/**
	 * test case for update legal entity Properties- Negative case -Without Order to
	 * Le Null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithOrderToLeNull() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderToLes());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValue());
		Mockito.when(
				ordersLeAttributeValueRepository.findByOrderToLe(Mockito.anyObject()))
				.thenReturn(objectCreator.returnOrdersLeAttributeValueList());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		UserInformation applicationUser = mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());
		ResponseResource<QuoteDetail> response = illOrderController.updateLeaglEntityAttribute(null, null,
				objectCreator.getUpdateRequest());
		// .updateLeaglEntityAttribute(objectCreator.getUpdateRequest());
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);

	}

	/**
	 * test case for update legal entity Properties- Negative case -Without Order
	 * Request
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithOutRequest() throws TclCommonException {
		ResponseResource<QuoteDetail> response = illOrderController.updateLeaglEntityAttribute(null, null, null);
		// .updateLeaglEntityAttribute(null);
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);

	}

	/**
	 * test case for update legal entity Properties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithMstAttributeNull() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getMstAttribute());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.getOrdersLeAttributeValue());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(objectCreator.getMstAttribute());

		ResponseResource<QuoteDetail> response = illOrderController.updateLeaglEntityAttribute(null, null,
				objectCreator.getUpdateRequest());
		assertTrue(response.getData() == null);

	}

	/**
	 * test case for update legal entity Properties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateLegalEntityPropertiesWithException() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes()));

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUserForInactiveOrders());
		mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(null);
		ResponseResource<DashBoardBean> response = illOrderController.getDashboardDetails(Mockito.anyInt());
		assertTrue(response.getData() == null);

	}

	/*
	 * * Update Quote Status -Positive case
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuotePositive() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateQuoteToLeStatus(1, "SOLUTIONS_CHOOSED");
		// assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	/*
	 * Update Quote Status -Negative case - Quote Id Null
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteIdNull() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateQuoteToLeStatus(null, "ORDER_CREATED");
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Status Null
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteStatusNull() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateQuoteToLeStatus(1, null);
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Null inputs
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteNullInputs() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateQuoteToLeStatus(null, null);
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Invalid Status
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteInvalidStatus() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateQuoteToLeStatus(1, "test");
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Invalid Id
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteInvalidId() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateQuoteToLeStatus(1, "SOLUTIONS_CHOOSED");
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Status -Negative case - Optional Empty
	 * 
	 * @throws TclCommonException
	 */
	// @Test
	public void testUpdateQuoteOptionalEmpty() throws TclCommonException {
		Optional<QuoteToLe> le = objectCreator.returnQuoteToLeForUpdateStatus();
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(quoteToLeRepository.save(le.get())).thenReturn(le.get());
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateQuoteToLeStatus(1, "SOLUTIONS_CHOOSED");
		// assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderPositive() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<QuoteDetail> response = illOrderController.updateOrderToLeStatus(1, 1, "ORDER_CREATED");
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderToLeStatus(1, "ORDER_CREATED");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	/*
	 * Update Order Status -Negative case - Order Id Null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderOrderIdNull() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<QuoteDetail> response = illOrderController.updateOrderToLeStatus(null, null, "ORDER_CREATED");
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderToLeStatus(null, "ORDER_CREATED");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Status Null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderStatusNull() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<QuoteDetail> response = illOrderController.updateOrderToLeStatus(1, null, null);
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderToLeStatus(1, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Null inputs
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderNullInputs() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<QuoteDetail> response = illOrderController.updateOrderToLeStatus(null, null, null);
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderToLeStatus(null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Invalid Status
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderInvalidStatus() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(le);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<QuoteDetail> response = illOrderController.updateOrderToLeStatus(1, null, "Test");
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderToLeStatus(1, "Test");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Invalid Id
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderInvalidId() throws TclCommonException {
		Optional<OrderToLe> le = objectCreator.returnOrderToLeForUpdateStatus();
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(orderToLeRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<QuoteDetail> response = illOrderController.updateOrderToLeStatus(1, null, "ORDER_CREATED");
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderToLeStatus(1, "ORDER_CREATED");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Order Status -Negative case - Optional empty
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateOrderOptionalEmpty() throws TclCommonException {
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(orderToLeRepository.save(objectCreator.returnOrderToLeForUpdateStatus().get()))
				.thenReturn(objectCreator.returnOrderToLeForUpdateStatus().get());
		ResponseResource<QuoteDetail> response = illOrderController.updateOrderToLeStatus(1, null, "ORDER_CREATED");
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderToLeStatus(1, "ORDER_CREATED");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Positive
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetail() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());

		Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStatus());
		Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStage());
		Mockito.when(orderSiteStatusAuditRepository.findByOrderIllSiteAndMstOrderSiteStatusAndIsActive(Mockito.any(),
				Mockito.any(), Mockito.anyByte())).thenReturn(objectCreator.getOrderSiteStatusAuditList());
		Mockito.when(orderSiteStatusAuditRepository.save(Mockito.any())).thenReturn(new OrderSiteStatusAudit());
		Mockito.when(orderSiteStageAuditRepository.findByMstOrderSiteStageAndOrderSiteStatusAuditAndIsActive(
				Mockito.any(), Mockito.any(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrderSiteStageAuditList());
		Mockito.when(orderSiteStageAuditRepository.save(Mockito.any())).thenReturn(new OrderSiteStageAudit());

		ResponseResource<List<OrderSiteProvisionAudit>> response = illOrderController.updateOrderSiteStatus(1, 1, 1,
				objectCreator.getOrderSiteRequest());
		// assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderSiteStatus(1, "CONFIGURE_SITES");
		assertTrue(response.getStatus() == Status.SUCCESS);

	}

	/*
	 * Update Quote Detail Status -Positive
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetail2() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());

		Mockito.when(mstOrderSiteStatusRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStatus());
		Mockito.when(mstOrderSiteStageRepository.findByName(Mockito.anyString()))
				.thenReturn(objectCreator.getMstOrderSiteStage());
		Mockito.when(orderSiteStatusAuditRepository.findByOrderIllSiteAndMstOrderSiteStatusAndIsActive(Mockito.any(),
				Mockito.any(), Mockito.anyByte())).thenReturn(Collections.EMPTY_LIST);
		Mockito.when(orderSiteStatusAuditRepository.save(Mockito.any())).thenReturn(new OrderSiteStatusAudit());
		Mockito.when(orderSiteStageAuditRepository.findByMstOrderSiteStageAndOrderSiteStatusAuditAndIsActive(
				Mockito.any(), Mockito.any(), Mockito.anyByte())).thenReturn(Collections.EMPTY_LIST);
		Mockito.when(orderSiteStageAuditRepository.save(Mockito.any())).thenReturn(new OrderSiteStageAudit());

		ResponseResource<List<OrderSiteProvisionAudit>> response = illOrderController.updateOrderSiteStatus(1, 1, 1,
				objectCreator.getOrderSiteRequest());
		// assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		// ResponseResource<QuoteDetail> response =
		// illOrderController.updateOrderSiteStatus(1, "CONFIGURE_SITES");
		assertTrue(response.getStatus() == Status.SUCCESS);

	}

	/*
	 * Update Quote Detail Status -Negative - Id Null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailIdNull() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		OrderSiteRequest request = new OrderSiteRequest();
		request.setMstOrderSiteStatusName("PROVISION_SITES");
		ResponseResource<List<OrderSiteProvisionAudit>> response = illOrderController.updateOrderSiteStatus(null, null,
				null, request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Status null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailStatusNull() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<List<OrderSiteProvisionAudit>> response = illOrderController.updateOrderSiteStatus(1, null,
				null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Inputs null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailInputsNull() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		ResponseResource<List<OrderSiteProvisionAudit>> response = illOrderController.updateOrderSiteStatus(null, null,
				null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Invalid status
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailInvalidStatus() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		OrderSiteRequest request = new OrderSiteRequest();
		request.setMstOrderSiteStatusName("TEST");
		ResponseResource<List<OrderSiteProvisionAudit>> response = illOrderController.updateOrderSiteStatus(1, null,
				null, request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Invalid id
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailInvalidId() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(null);
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		OrderSiteRequest request = new OrderSiteRequest();
		request.setMstOrderSiteStatusName("PROVISION_SITES");
		ResponseResource<List<OrderSiteProvisionAudit>> response = illOrderController.updateOrderSiteStatus(1, null,
				null, request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Update Quote Detail Status -Negative - Invalid id
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testUpdateQuoteDetailOptionalEmpty() throws TclCommonException {
		Optional<OrderIllSite> le = objectCreator.returnQuoteDetailForUpdateStatus();
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(orderIllSitesRepository.save(le.get())).thenReturn(le.get());
		OrderSiteRequest request = new OrderSiteRequest();
		request.setMstOrderSiteStatusName("PROVISION_SITES");
		ResponseResource<List<OrderSiteProvisionAudit>> response = illOrderController.updateOrderSiteStatus(1, null,
				null, request);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);

	}

	/**
	 * test create for retrieving attibutes and sites for customer legal entity id
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSites() throws TclCommonException {
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes1()));
		ResponseResource<OrderToLeDto> response = illOrderController.getAttributesAndSites(1, 1, null);
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * negative test for retrieving attibutes and sites for customer legal entity id
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSitesForNull() throws Exception {
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.getOrderToLes1()));
		ResponseResource<OrderToLeDto> response = illOrderController.getAttributesAndSites(null, null, null);
		assertTrue(response == null || response.getData() == null || response.getStatus() != Status.SUCCESS);
	}

	/**
	 * test create for retrieving attibutes and sites for customer legal entity
	 * id-Positive
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSitesWithAttribute() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		when(orderRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(objectCreator.getOrder()));
		ResponseResource<OrderToLeDto> response = illOrderController.getAttributesAndSites(1, 1, "test");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create for retrieving attibutes and sites for customer legal entity id
	 * -- Positve
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSitesWithAttributeWithoutData() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		ResponseResource<OrderToLeDto> response = illOrderController.getAttributesAndSites(1, null,
				Mockito.matches("test"));
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);
	}

	/**
	 * test create for retrieving attibutes and sites for customer legal entity id-
	 * Negative
	 * 
	 * @throws TclCommonException
	 **/
	@Test
	public void testGetAttributesAndSitesWithoutData() throws TclCommonException {
		Mockito.when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<OrderToLeDto> response = illOrderController.getAttributesAndSites(1, null, null);
		assertTrue(response != null && response.getResponseCode() == 404);
	}

	/*
	 * positive test case : getAllOrdersByProductName
	 * 
	 * @inputparam : productname
	 */
	@Test
	public void testGetAllOrdersByProductName() throws TclCommonException {
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.geProductFamily());
		when(orderToLeProductFamilyRepository.findByMstProductFamilyOrderByIdAsc(Mockito.any()))
				.thenReturn(objectCreator.getorderToLeProductFamiliesList());
		when(orderToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(objectCreator.getOrderToLes()));
		when(orderRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(objectCreator.getOrder());
		when(orderProductSolutionRepository.findByOrderToLeProductFamily(Mockito.any()
				)).thenReturn(objectCreator.getOrderProductSolutionList());
		when(orderIllSitesRepository.findByOrderProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(objectCreator.getOrderIllSiteList());
		when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstAttributeList());
		when(ordersLeAttributeValueRepository.findByMstOmsAttributeAndOrderToLe(Mockito.any(), Mockito.any()))
				.thenReturn(objectCreator.setOrdersLeAttributeValue());
		ResponseResource<List<DashboardOrdersBean>> response = illOrderController.getAllOrdersByProductName("test");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * negative test case : getAllOrdersByProductName
	 * 
	 * @inputparam : productname as null response : List<DashboardOrdersBean> as
	 * null
	 */
	@Test
	public void testGetAllOrdersByProductNameException() throws TclCommonException {
		ResponseResource<List<DashboardOrdersBean>> response = illOrderController.getAllOrdersByProductName(null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE
				&& response.getResponseCode() == 500);
	}

	/*
	 * negaive test case : getAllOrdersByProductName
	 * 
	 * @inputparam : productname
	 * 
	 * response : List<DashboardOrdersBean> as null
	 */
	@Test
	public void testGetAllOrdersByProductForNullProductFamily() throws TclCommonException {
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<List<DashboardOrdersBean>> response = illOrderController.getAllOrdersByProductName("test");
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE
				&& response.getResponseCode() == 500);
	}

	/*
	 * positive test case for updateSiteProperties {main} input param :
	 * UpdateRequest response : OrderIllSiteBean
	 */
	@Test
	public void testUpdateSiteProperties() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductFamily());
		when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductComponentList());
		when(orderProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponent());
		when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getProductAtrributeMaster());
		when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createOrderProducts());
		when(orderProductComponentsAttributeValueRepository.findByOrderProductComponentAndProductAttributeMaster(Mockito.any(),Mockito.any()))
		.thenReturn(objectCreator.createOrderProductsList());
		ResponseResource<OrderIllSiteBean> response = illOrderController.updateSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS
				&& response.getResponseCode() == 200);
	}

	/*
	 * positive test case for updateSiteProperties input param : UpdateRequest
	 * response : OrderIllSiteBean
	 */
	@Test
	public void testUpdateSitePropertiesforNullMstProductComponent() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.geProductFamily());
		when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		when(mstProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getMstProductComponent());
		when(orderProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponent());
		when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getProductAtrributeMaster());
		when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createOrderProducts());

		ResponseResource<OrderIllSiteBean> response = illOrderController.updateSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS
				&& response.getResponseCode() == 200);
	}

	/*
	 * positive test case for updateSiteProperties input param : UpdateRequest
	 * response : OrderIllSiteBean
	 */
	@Test
	public void testUpdateSitePropertiesforNullProductAttributeMaster() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.geProductFamily());
		when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		when(mstProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getMstProductComponent());
		when(orderProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponent());
		when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		when(productAttributeMasterRepository.save(Mockito.any())).thenReturn(objectCreator.getProductAtrributeMas());
		when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createOrderProducts());

		ResponseResource<OrderIllSiteBean> response = illOrderController.updateSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS
				&& response.getResponseCode() == 200);
	}

	/*
	 * negative test case for updateSiteProperties with null OrderIllSite input
	 * param : UpdateRequest response : exception (Error Code : 500)
	 */
	@Test
	public void testUpdateSitePropertiesforNullOrderIllSite() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<OrderIllSiteBean> response = illOrderController.updateSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE
				&& response.getResponseCode() == 500);
	}

	/*
	 * negative test case for updateSiteProperties with null MstProductFamily input
	 * param : UpdateRequest response : exception (Error Code : 500)
	 */
	@Test
	public void testUpdateSitePropertiesforNullMstProductFamily() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<OrderIllSiteBean> response = illOrderController.updateSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE
				&& response.getResponseCode() == 500);
	}
	
	/*
	 * positive test case for updateSiteProperties {main} input param :
	 * UpdateRequest response : OrderIllSiteBean
	 */
	@Test
	public void testUpdateSitePropertiesForelse() throws TclCommonException {
		when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.craeteUser());
		when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductFamily());
		when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductComponentList());
		when(orderProductComponentRepository.save(Mockito.any())).thenReturn(objectCreator.getOrderProductComponent());
		when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getProductAtrributeMaster());
		when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(objectCreator.createOrderProducts());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(
				Mockito.anyInt(), Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(
				Mockito.anyInt(), Mockito.any()))
				.thenReturn(new ArrayList<>());
		ResponseResource<OrderIllSiteBean> response = illOrderController.updateSiteProperties(1, 1, 1,
				objectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS
				&& response.getResponseCode() == 200);
	}

	/*
	 * 
	 * Positive test case for updateSdfcStage
	 * 
	 * input param : orderToLeId,stage
	 * 
	 */

	@Test
	public void testUpdateSdfcStageAsCOFReceived() throws TclCommonException {
		when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.returnOrderToLeForUpdateStatus());
		Mockito.doNothing().when(mqutils).send(Mockito.anyString(), Mockito.anyString());
		ResponseResource<String> response = illOrderController.updateSdfcStage(1, 1, "Closed Won – COF Received");
		assertTrue(response.getStatus() == Status.SUCCESS && response.getResponseCode() == 200);
	}

	/*
	 * 
	 * Positive test case for updateSdfcStage
	 * 
	 * input param : orderToLeId,stage
	 * 
	 */

	@Test
	public void testUpdateSdfcStageAsOrderProcessing() throws TclCommonException {
		when(orderToLeRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.returnOrderToLeForUpdateStatus());
		Mockito.doNothing().when(mqutils).send(Mockito.anyString(), Mockito.anyString());
		ResponseResource<String> response = illOrderController.updateSdfcStage(1, 1, "Closed Won – Order Processing");
		assertTrue(response.getStatus() == Status.SUCCESS && response.getResponseCode() == 200);
	}

	/*
	 * Positive test case for getSiteProperties Result :
	 * List<OrderProductComponentBean>
	 */
	@Test
	public void testGetSiteProperties() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductComponentList());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(
				Mockito.anyInt(), Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(orderProductComponentsAttributeValueRepository
				.findByOrderProductComponentAndProductAttributeMaster_Name(Mockito.any(), Mockito.anyString()))
				.thenReturn(objectCreator.createOrderProductsList());
		ResponseResource<List<OrderProductComponentBean>> response = illOrderController.getSiteProperties(1, 1, 1,
				"Test Attribute");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * Positive test case for getSiteProperties Result :
	 * List<OrderProductComponentBean>
	 */
	@Test
	public void testGetSitePropertiesForNullAttrName() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt()))
				.thenReturn(objectCreator.returnQuoteDetailForUpdateStatus());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(objectCreator.getMstProductComponentList());
		Mockito.when(orderProductComponentRepository.findByReferenceIdAndMstProductComponent(
				Mockito.anyInt(), Mockito.any()))
				.thenReturn(objectCreator.getOrderProductComponentList());
		Mockito.when(orderProductComponentsAttributeValueRepository.findByOrderProductComponent(Mockito.any()))
				.thenReturn(objectCreator.createOrderProductsList());

		ResponseResource<List<OrderProductComponentBean>> response = illOrderController.getSiteProperties(1, 1, 1,
				null);
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * Negative test case for getSiteProperties Result : Exception
	 */
	@Test
	public void testGetSitePropertiesForNullOrderIllSite() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<List<OrderProductComponentBean>> response = illOrderController.getSiteProperties(1, 1, 1,
				"Test Attribute");
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);
	}

	/*
	 * Positive Test case for getOrderSiteAuditTrail Result :
	 * ResponseResource<AuditBean>
	 */
	@Test
	public void testGetOrderSiteAuditTrail() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(objectCreator.getOrderIllSiteOptional());
		Mockito.when(orderSiteStatusAuditRepository.findByOrderIllSiteAndIsActive(Mockito.any(), Mockito.anyByte())).thenReturn(objectCreator.getOrderSiteStatusAuditList());
		ResponseResource<AuditBean> response = illOrderController.getOrderSiteAuditTrail(1, 1, 1);
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}
	
	/*
	 * Positive Test case for getOrderSiteAuditTrail Result :
	 * ResponseResource<AuditBean>
	 */
	@Test
	public void testGetOrderSiteAuditTrailForNullOrderIllSite() throws TclCommonException {
		Mockito.when(orderIllSitesRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<AuditBean> response = illOrderController.getOrderSiteAuditTrail(1, 1, 1);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * Negative Test case for getOrderSiteAuditTrail Result :
	 * ResponseResource<AuditBean>
	 */
	@Test
	public void testGetOrderSiteAuditTrailForNullSiteId() throws TclCommonException {

		ResponseResource<AuditBean> response = illOrderController.getOrderSiteAuditTrail(1, 1, null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}
	
	/*
	 * Negative Test case for getOrderSiteAuditTrail Result :
	 * ResponseResource<AuditBean>
	 */
	//@Test

}
