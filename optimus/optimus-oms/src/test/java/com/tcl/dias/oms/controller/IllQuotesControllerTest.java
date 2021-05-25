package com.tcl.dias.oms.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.OptimusOmsApplication;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.DashBoardSiteBean;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.ProductAttributeMasterBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.beans.UserRequest;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.dto.QuoteDelegationDto;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.OrderIllSiteSla;
import com.tcl.dias.oms.entity.entities.OrderPrice;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrderSiteFeasibility;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;
import com.tcl.dias.oms.entity.entities.PricingEngineResponse;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.SiteFeasibility;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.CurrencyConversionRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
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
import com.tcl.dias.oms.ill.controller.v1.IllOrderController;
import com.tcl.dias.oms.ill.controller.v1.IllQuoteController;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IllQuotesControllerTest.java class. This class
 * contains all the test cases for the IllQuotesControllerTest
 * 
 *
 * @author SEKHAR ER
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IllQuotesControllerTest {

	@MockBean
	protected QuoteRepository quoteRepository;

	@MockBean
	protected QuoteToLeRepository quoteToLeRepository;

	@MockBean
	IllSiteRepository illSiteRepository;

	@MockBean
	private QuotePriceRepository quotePriceRepository;

	@MockBean
	private QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	protected CustomerRepository customerRepository;

	@MockBean
	private OrderRepository orderRepository;

	@Autowired
	protected ObjectCreator quoteObjectCreator;

	@MockBean
	private QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	IllQuoteController quotesController;

	@Autowired
	IllOrderController illOrderController;

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
	protected UserRepository userRepository;

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
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	OrderProductComponentsAttributeValueRepository orderProductComponentsAttributeValueRepository;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	Utils utils;

	@MockBean
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@MockBean
	OrderIllSiteSlaRepository orderIllSiteSlaRepository;

	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;

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

	@MockBean
	AuthTokenDetailRepository authTokenDetailRepository;

	@Autowired
	IllQuoteService illQuoteService;

	@MockBean
	CurrencyConversionRepository currencyConversionRepository;

	@MockBean
	CofDetailsRepository cofDetailsRepository;

	@MockBean
	DocusignAuditRepository docusignAuditRepository;

	@MockBean
	OmsAttachmentRepository omsAttachmentRepository;

	/**
	 * 
	 * init- predefined mocks
	 * 
	 * @throws TclCommonException
	 */
	/*
	 * @Before public void init() throws TclCommonException {
	 * 
	 * Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
	 * .thenReturn((quoteObjectCreator.getMstProductComponent()));
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin",
	 * 1)).thenReturn((quoteObjectCreator.getUser()));
	 * Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getQuote());
	 * Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString
	 * (), Mockito.anyByte())) .thenReturn(quoteObjectCreator.geProductFamily());
	 * Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getIllsites());
	 * Mockito.when(illSiteRepository.findByIdInAndStatus(Mockito.anyList(),
	 * Mockito.anyByte())) .thenReturn((quoteObjectCreator.getIllsitesMock()));
	 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
	 * Mockito.when(quoteProductComponentRepository.findByReferenceId(Mockito.anyInt
	 * ())) .thenReturn((quoteObjectCreator.getQuoteProductComponent()));
	 * Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.
	 * anyString(), Mockito.anyString()))
	 * .thenReturn(quoteObjectCreator.geQuotePrice());
	 * Mockito.when(quoteProductComponentRepository.findByReferenceId(Mockito.anyInt
	 * ())) .thenReturn((quoteObjectCreator.getQuoteProductComponent()));
	 * Mockito.when(quoteProductComponentRepository.findByReferenceIdIn(Mockito.
	 * anyList())) .thenReturn((quoteObjectCreator.getQuoteProductComponent()));
	 * Mockito.when(orderIllSiteSlaRepository.save(Mockito.any())) .thenReturn(new
	 * OrderIllSiteSla());
	 * 
	 * 
	 * Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.
	 * anyInt(), Mockito.anyByte()))
	 * 
	 * .thenReturn((quoteObjectCreator.getCustomer()));
	 * Mockito.when(mstProductOfferingRepository.
	 * findByMstProductFamilyAndProductNameAndStatus(Mockito.any(),
	 * Mockito.anyString(),
	 * Mockito.anyByte())).thenReturn((quoteObjectCreator.getMstOffering()));
	 * 
	 * Mockito.when(mstProductOfferingRepository.
	 * findByMstProductFamilyAndProductNameAndStatus(Mockito.any(),
	 * Mockito.anyString(),
	 * Mockito.anyByte())).thenReturn((quoteObjectCreator.getMstOffering()));
	 * 
	 * Mockito.when(productSolutionRepository.
	 * findByQuoteToLeProductFamilyAndMstProductOffering(
	 * quoteObjectCreator.getQuoteToLeFamily(),
	 * quoteObjectCreator.getMstOffering()))
	 * .thenReturn((quoteObjectCreator.getSolution()));
	 * Mockito.when(quoteProductComponentsAttributeValueRepository.findById(Mockito.
	 * anyInt()))
	 * .thenReturn(Optional.ofNullable(quoteObjectCreator.getAttribute()));
	 * Mockito.when(orderConfirmationAuditRepository.save(Mockito.any()))
	 * .thenReturn(new OrderConfirmationAudit());
	 * 
	 * 
	 * Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getCustomer());
	 * Mockito.when(customerRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getUserList()));
	 * Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt
	 * (), Mockito.matches("Open")))
	 * .thenReturn(Optional.of(quoteObjectCreator.getQuoteDelegation()));
	 * Mockito.when(quoteProductComponentRepository.
	 * findByReferenceIdAndQuoteVersionAndMstProductComponent_Name(
	 * Mockito.anyInt(), Mockito.anyInt(), Mockito.matches("SITE_PROPERTIES")))
	 * .thenReturn(quoteObjectCreator.getQuoteProductComponent()); Mockito.when(
	 * quoteProductComponentRepository.findByReferenceIdAndQuoteVersion(Mockito.
	 * anyInt(), Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.getQuoteProductComponent());
	 * Mockito.when(quoteProductComponentsAttributeValueRepository
	 * .findByQuoteProductComponent_IdAndQuoteVersionAndProductAttributeMaster_Name(
	 * Mockito.anyInt(), Mockito.anyInt(), Mockito.matches("LOCAL_IT_CONTACT")))
	 * .thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
	 * Mockito.doThrow(new
	 * RuntimeException()).when(quoteProductComponentsAttributeValueRepository)
	 * .delete(new QuoteProductComponentsAttributeValue()); Mockito.doThrow(new
	 * RuntimeException()).when(quoteProductComponentRepository) .delete(new
	 * QuoteProductComponent()); Mockito.doThrow(new
	 * RuntimeException()).when(illSiteRepository).save(new QuoteIllSite());
	 * Mockito.doThrow(new RuntimeException()).when(quoteToLeRepository).save(new
	 * QuoteToLe());
	 * 
	 * mock(UserInformation.class);
	 * 
	 * Authentication authentication = mock(Authentication.class);
	 * 
	 * SecurityContext securityContext = mock(SecurityContext.class);
	 * 
	 * when(securityContext.getAuthentication()).thenReturn(authentication);
	 * 
	 * SecurityContextHolder.setContext(securityContext);
	 * when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
	 * .thenReturn(quoteObjectCreator.getUserInformation());
	 * 
	 * Mockito.when(illSiteRepository.findByProductSolutionAndQuoteVersionAndStatus(
	 * Mockito.any(), Mockito.anyInt(),
	 * Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());
	 * 
	 * when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(
	 * quoteObjectCreator.getQuote2()));
	 * 
	 * Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
	 * .thenReturn(quoteObjectCreator.getCustomerDetailsBean());
	 * Mockito.when(quoteToLeProductFamilyRepository.
	 * findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteToLeFamily());
	 * Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.
	 * any())) .thenReturn(quoteObjectCreator.createProductSolutions());
	 * Mockito.when(quoteProductComponentRepository.
	 * findByReferenceIdAndQuoteVersionAndType(Mockito.anyInt(), Mockito.anyInt(),
	 * Mockito.anyString())).thenReturn((quoteObjectCreator.getQuoteProductComponent
	 * ())); doNothing().when(mqUtils).send(Mockito.anyString(),
	 * Mockito.anyString());
	 * Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),
	 * Mockito.anyString())) .thenReturn(quoteObjectCreator.getAddressDetail());
	 * Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getUser());
	 * Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getCustomer());
	 * Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));
	 * doNothing().when(siteFeasibilityRepository).delete(Mockito.any());
	 * doNothing().when(siteFeasibilityRepository).deleteAll(Mockito.any()); }
	 * 
	 *//**
		 * 
		 * testcreateDocument - positive case
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * 
	 * @Test public void testcreateDocument() throws TclCommonException {
	 * when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
	 * when(quoteToLeRepository.save(Mockito.any())).thenReturn(new QuoteToLe());
	 * when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
	 * .thenReturn(quoteObjectCreator.getCustomerDetailsBean());
	 * Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(null);
	 * ResponseResource<CreateDocumentDto> response =
	 * quotesController.createDocument(1, 1, quoteObjectCreator.getDocumentDto());
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	 * 
	 * }
	 * 
	 *//**
		 * 
		 * testcreateDocument - negative case
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * 
	 * @Test public void testcreateDocumentException() throws TclCommonException {
	 * Mockito.when(quoteRepository.findById(null)).thenReturn((Optional.ofNullable(
	 * null))); ResponseResource<CreateDocumentDto> response =
	 * quotesController.createDocument(null, null, null);
	 * assertTrue(response.getStatus() == Status.FAILURE);
	 * 
	 * }
	 * 
	 *//**
		 * test create quote positive test case
		 **/
	/*
	 * @Test public void testcreateQuote() throws Exception {
	 * ResponseResource<QuoteResponse> response =
	 * quotesController.createQuote(quoteObjectCreator.getQuoteDetail(), 1);
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote Negative test case
		 **/
	/*
	 * @Test public void testcreateQuoteInvalidCustomer() throws Exception {
	 * Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.
	 * anyInt(), Mockito.anyByte())) .thenReturn(null);
	 * ResponseResource<QuoteResponse> response =
	 * quotesController.createQuote(quoteObjectCreator.getQuoteDetail(), 1);
	 * assertTrue(response != null && response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * test create quote positive test case without passing QuoteleId and QuoteId
		 **/
	/*
	 * @Test public void testcreateQuote2() throws Exception {
	 * Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),
	 * Mockito.anyString()))
	 * .thenReturn(quoteObjectCreator.getCustomerDetailsBean());
	 * ResponseResource<QuoteResponse> response =
	 * quotesController.createQuote(quoteObjectCreator.getQuoteDetail2(), 1);
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote positive test case
		 **/
	/*
	 * @Test public void testcreateQuoteWithValid() throws Exception {
	 * 
	 * Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getUser());
	 * Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getCustomer());
	 * Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.
	 * of(quoteObjectCreator.getQuote()));
	 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
	 * Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getQuoteToLe()); ResponseResource<QuoteResponse> response
	 * = quotesController.createQuote(quoteObjectCreator.getQuoteDetail(), 1);
	 * 
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote positive test case
		 **/
	/*
	 * @Test public void testcreateQuoteWithValidAndCustomerNull() throws Exception
	 * {
	 * 
	 * Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getCustomer());
	 * Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.
	 * of(quoteObjectCreator.getQuote()));
	 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
	 * Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getQuoteToLe());
	 * Mockito.when(quoteToLeProductFamilyRepository.
	 * findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteToLeFamily());
	 * Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.
	 * any())) .thenReturn(quoteObjectCreator.createProductSolutions());
	 * 
	 * ResponseResource<QuoteResponse> response =
	 * quotesController.createQuote(quoteObjectCreator.getQuoteDetail(), null);
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote positive test case
		 **/
	/*
	 * @Test public void testcreateQuoteWithDifferentOfferingName() throws Exception
	 * {
	 * 
	 * Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getCustomer());
	 * Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.
	 * of(quoteObjectCreator.getQuote()));
	 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
	 * Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getQuoteToLe());
	 * Mockito.when(quoteToLeProductFamilyRepository.
	 * findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteToLeFamily());
	 * Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.
	 * any())) .thenReturn(quoteObjectCreator.createProductSolutions());
	 * 
	 * ResponseResource<QuoteResponse> response =
	 * quotesController.createQuote(quoteObjectCreator.getQuoteDetailNew(), 1);
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote negative test case
		 **/
	/*
	 * @Test public void testcreateQuoteExcpetion() throws Exception {
	 * Mockito.when(userRepository.findByUsernameAndStatus(null,
	 * 1)).thenReturn((null)); ResponseResource<QuoteResponse> response =
	 * quotesController.createQuote(null, null); assertTrue(response.getStatus() ==
	 * Status.FAILURE); }
	 * 
	 *//**
		 * test create quote positive test case
		 **/
	/*
	 * @Test // to do:need to change the mocking public void
	 * testupdateSiteInformation() throws Exception {
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin",
	 * 1)).thenReturn(quoteObjectCreator.getUser());
	 * Mockito.when(productSolutionRepository.
	 * findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
	 * Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
	 * Mockito.when(quoteToLeProductFamilyRepository.
	 * findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteToLeFamily());
	 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
	 * Mockito.when(quoteProductComponentRepository.findByReferenceId(Mockito.anyInt
	 * ())) .thenReturn(quoteObjectCreator.getQuoteProductComponent());
	 * Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getMstProductComponent()));
	 * Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));
	 * Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceNameAndVersion
	 * (Mockito.anyString(), Mockito.anyString(),
	 * Mockito.anyInt())).thenReturn(quoteObjectCreator.geQuotePrice());
	 * Mockito.when(quoteToLeRepository.findByQuoteAndQuoteVersion(Mockito.any(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getQuoteToLeList());
	 * Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndQuoteVersion(
	 * Mockito.any(), Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
	 * ResponseResource<QuoteBean> response =
	 * quotesController.updateSiteInformation(0, null,
	 * quoteObjectCreator.getQuoteDetail(), 0); assertTrue(response != null &&
	 * response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote Negative test case
		 **/
	/*
	 * @Test // to do:need to change the mocking public void
	 * testupdateSiteInformationWihtoutProduct() throws Exception {
	 * Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString
	 * (), Mockito.anyByte())) .thenReturn(null); ResponseResource<QuoteBean>
	 * response = quotesController.updateSiteInformation(0, null,
	 * quoteObjectCreator.getQuoteDetail(), 0); assertTrue(response != null &&
	 * response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * test create quote Positive test case
		 **/
	/*
	 * @Test // to do:need to change the mocking public void
	 * testupdateSiteInformationWithoutQuoteDetail() throws Exception {
	 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(
	 * Optional.empty()); ResponseResource<QuoteBean> response =
	 * quotesController.updateSiteInformation(0, null,
	 * quoteObjectCreator.getQuoteDetail(), 0); assertTrue(response != null &&
	 * response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote Negative test case
		 **/
	/*
	 * @Test // to do:need to change the mocking public void
	 * testupdateSiteInformationWithoutQuoteDetailAndNulldetails() throws Exception
	 * { Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(
	 * Optional.empty());
	 * Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())).thenReturn(null); ResponseResource<QuoteBean> response =
	 * quotesController.updateSiteInformation(0, null,
	 * quoteObjectCreator.getQuoteDetail(), 0); assertTrue(response != null &&
	 * response.getStatus() == Status.FAILURE); }
	 * 
	 * @Test // to do:need to change the mocking public void
	 * testupdateSiteInformationForAttributeEmpty() throws Exception {
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin",
	 * 1)).thenReturn(quoteObjectCreator.getUser());
	 * Mockito.when(productSolutionRepository.
	 * findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
	 * Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
	 * Mockito.when(quoteToLeProductFamilyRepository.
	 * findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteToLeFamily());
	 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte())) .thenReturn(null);
	 * Mockito.when(quoteProductComponentRepository.findByReferenceId(Mockito.anyInt
	 * ())) .thenReturn(quoteObjectCreator.getQuoteProductComponent());
	 * Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getMstProductComponent()));
	 * Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
	 * 
	 * .thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));
	 * 
	 * ResponseResource<QuoteBean> response =
	 * quotesController.updateSiteInformation(0, null,
	 * quoteObjectCreator.getQuoteDetail(), 0); assertTrue(response != null &&
	 * response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote positive test case
		 **/
	/*
	 * @Test // to do:need to change the mocking public void
	 * testupdateSiteInformationWithSite() throws Exception {
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin",
	 * 1)).thenReturn(quoteObjectCreator.getUser());
	 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
	 * Mockito.when(productSolutionRepository.
	 * findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
	 * Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
	 * Mockito.when(quoteToLeProductFamilyRepository.
	 * findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteToLeFamily());
	 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
	 * Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMaster().get(0)
	 * )); Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.
	 * craeteQuoteProductComponent()))
	 * .thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
	 * Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getMstProductComponent()));
	 * Mockito.when(quoteProductComponentsAttributeValueRepository.
	 * findByQuoteProductComponent(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());
	 * Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getQuotePrice());
	 * 
	 * ResponseResource<QuoteBean> response =
	 * quotesController.updateSiteInformation(0, null,
	 * quoteObjectCreator.getQuoteDetail(), 0);
	 * 
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote positive test case
		 **/
	/*
	 * @Test // to do:need to change the mocking public void
	 * testupdateSiteInformationWithEmptySite() throws Exception {
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin",
	 * 1)).thenReturn(quoteObjectCreator.getUser());
	 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
	 * Mockito.when(productSolutionRepository.
	 * findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
	 * Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
	 * Mockito.when(quoteToLeProductFamilyRepository.
	 * findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteToLeFamily());
	 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
	 * Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMaster().get(0)
	 * ));
	 * 
	 * Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.
	 * craeteQuoteProductComponent()))
	 * .thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
	 * Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getMstProductComponent()));
	 * Mockito.when(quoteProductComponentsAttributeValueRepository.
	 * findByQuoteProductComponent(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());
	 * Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getQuotePrice());
	 * Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getQuoteIllSite());
	 * 
	 * ResponseResource<QuoteBean> response =
	 * quotesController.updateSiteInformation(0, null,
	 * quoteObjectCreator.getQuoteDetailWithoutSiteID(), 0);
	 * 
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * test create quote negative test case
		 **/
	/*
	 * @Test public void testupdateSiteInformationException() throws Exception {
	 * Mockito.when(userRepository.findByUsernameAndStatus(null,
	 * 1)).thenReturn((null)); ResponseResource<QuoteBean> response =
	 * quotesController.updateSiteInformation(null, null, null, null);
	 * assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * 
		 * testgetQuoteConfigurationException- get Quote Configuration-negative
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testgetQuoteConfigurationException() throws Exception {
	 * Mockito.when(userRepository.findByUsernameAndStatus(null,
	 * 1)).thenReturn((null)); ResponseResource<QuoteBean> response =
	 * quotesController.getQuoteConfiguration(null, "ALL");
	 * assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * 
		 * testgetQuoteConfiguration-get quote configuration postive
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testgetQuoteConfiguration() throws Exception {
	 * Mockito.when(quoteRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.ofNullable(quoteObjectCreator.getQuote2()));
	 * 
	 * Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getUser());
	 * 
	 * ResponseResource<QuoteBean> response =
	 * quotesController.getQuoteConfiguration(1, "ALL"); assertTrue(response != null
	 * && response.getStatus() == Status.SUCCESS);
	 * 
	 * }
	 * 
	 * 
	 * test case for all user details by customerID - positive case
	 *
	 * @Test public void testgetAllUsersByCustomerId() throws TclCommonException {
	 * ResponseResource<List<UserDto>> response =
	 * quotesController.getAllUserDetailsByCustomer(); assertTrue(response != null
	 * && response.getStatus() == Status.SUCCESS); }
	 * 
	 * 
	 * * test case for all user details by customerID - negative case
	 * 
	 * @Test public void testgetAllUsersByCustomerIdNull() throws TclCommonException
	 * {
	 * 
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin",
	 * 1)).thenReturn(null); ResponseResource<List<UserDto>> response =
	 * quotesController.getAllUserDetailsByCustomer();
	 * assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * for testing getTaxExcemptedSiteInfo testgetSiteInfooException
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void getTaxExemptedSiteInfo() throws Exception {
	 * Mockito.when(illSiteRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.ofNullable(quoteObjectCreator.getIllsites()));
	 * ResponseResource<QuoteResponse> response =
	 * quotesController.getTaxExemptedSiteInfo(0, null, null); assertTrue(response
	 * != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * for testing taxExempted testgetSiteInfooException
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void getTaxExemptedSiteInfoException() throws Exception {
	 * 
	 * Mockito.when(quoteRepository.findById(null)).thenReturn((null));
	 * ResponseResource<QuoteResponse> response =
	 * quotesController.getTaxExemptedSiteInfo(null, null, null);
	 * assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * for testing siteIfo testgetSiteInfooException
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testgetSiteInfo() throws Exception {
	 * Mockito.when(illSiteRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.ofNullable(quoteObjectCreator.getIllsites()));
	 * ResponseResource<QuoteResponse> response = quotesController.getSiteInfo(1, 1,
	 * 1); assertTrue(response.getData() != null && response.getStatus() ==
	 * Status.SUCCESS); }
	 * 
	 *//**
		 * for testing siteIfo testgetSiteInfooException
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testgetSiteInfoException() throws Exception {
	 * 
	 * Mockito.when(illSiteRepository.findById(null)).thenReturn(Optional.ofNullable
	 * (null));
	 * 
	 * Mockito.when(illSiteRepository.findById(null)).thenReturn((null));
	 * ResponseResource<QuoteResponse> response = quotesController.getSiteInfo(null,
	 * null, null); assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * for testing delete site testdeleteIllsites
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testdeleteIllsites() throws Exception {
	 * when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getIllsites());
	 * Mockito.when(quoteProductComponentRepository.findByReferenceId(Mockito.anyInt
	 * ())) .thenReturn(quoteObjectCreator.getQuoteProductComponent());
	 * doNothing().when(quoteProductComponentsAttributeValueRepository).delete(
	 * Mockito.any());
	 * doNothing().when(quoteProductComponentRepository).delete(Mockito.any());
	 * when(quoteIllSiteSlaRepository.findByQuoteIllSite(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteIllSiteSlaList());
	 * doNothing().when(quoteIllSiteSlaRepository).delete(Mockito.any());
	 * when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getSiteFeasibilityList());
	 * doNothing().when(siteFeasibilityRepository).delete(Mockito.any());
	 * doNothing().when(illSiteRepository).delete(Mockito.any());
	 * ResponseResource<QuoteDetail> response = quotesController.deactivateSites(1,
	 * 1, 1, 1, QuoteConstants.DELETE.toString()); assertTrue(response.getData() !=
	 * null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * for testing delete site testdeleteIllsitesException
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testdeleteIllsitesException() throws Exception {
	 * Mockito.when(illSiteRepository.findById(null)).thenReturn(null);
	 * Mockito.when(quoteProductComponentRepository.findByReferenceId(null)).
	 * thenReturn(null); ResponseResource<QuoteDetail> response =
	 * quotesController.deactivateSites(null, null, null, null, null);
	 * assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 * 
	 * * test case to get the trigger email once the user is selected - positive
	 * case
	 * 
	 * 
	 * @Test public void testTriggerEmail() throws TclCommonException {
	 * Mockito.when(userRepository.findByEmailIdAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getUser());
	 * Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt
	 * (), Mockito.anyString()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getQuoteDelegation()));
	 * TriggerEmailRequest triggerRequest = new TriggerEmailRequest();
	 * triggerRequest.setEmailId("test@gmail.com");
	 * triggerRequest.setQuoteToLeId(1); MockHttpServletRequest httpServletRequest =
	 * new MockHttpServletRequest();
	 * httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
	 * ResponseResource<TriggerEmailResponse> response =
	 * quotesController.triggerEmail(null, null, triggerRequest,
	 * httpServletRequest); assertTrue(response != null && response.getStatus() ==
	 * Status.SUCCESS); }
	 * 
	 * 
	 * * test case to get the trigger email once the user is selected - negative
	 * case
	 * 
	 * 
	 * @Test public void testTriggerEmail2() throws TclCommonException {
	 * Mockito.when(userRepository.findByEmailIdAndStatus(null,
	 * 1)).thenReturn(null);
	 * 
	 * TriggerEmailRequest triggerRequest = new TriggerEmailRequest();
	 * triggerRequest.setEmailId(null); triggerRequest.setQuoteToLeId(0);
	 * MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
	 * httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
	 * ResponseResource<TriggerEmailResponse> response =
	 * quotesController.triggerEmail(null, null, triggerRequest,
	 * httpServletRequest); assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 * @Test public void testTriggerEmailForEmpty() throws TclCommonException {
	 * Mockito.when(userRepository.findByEmailIdAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getUser());
	 * Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt
	 * (), Mockito.anyString())) .thenReturn(Optional.empty()); TriggerEmailRequest
	 * triggerRequest = new TriggerEmailRequest();
	 * triggerRequest.setEmailId("test@gmail.com");
	 * 
	 * triggerRequest.setQuoteToLeId(1); MockHttpServletRequest httpServletRequest =
	 * new MockHttpServletRequest();
	 * httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
	 * ResponseResource<TriggerEmailResponse> response =
	 * quotesController.triggerEmail(null, null, triggerRequest,
	 * httpServletRequest); assertTrue(response.getData() != null); }
	 * 
	 * 
	 * * test case to get the quote Id if the delegated user is in Open status -
	 * positive case
	 * 
	 * 
	 * @Test public void testgetDelegatedUsers() throws TclCommonException {
	 * 
	 * Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.craeteUser());
	 * 
	 * Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt
	 * (), Mockito.anyString()))
	 * .thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteDelegation()));
	 * 
	 * ResponseResource<QuoteDelegationDto> response =
	 * quotesController.getDelegatedUser(); assertTrue(response != null &&
	 * response.getStatus() == Status.SUCCESS); }
	 * 
	 * 
	 * * test case to get the quote Id if the delegated user is in Open status -
	 * positive case
	 * 
	 * 
	 * @Test public void testgetDelegatedUsersWithException() throws
	 * TclCommonException {
	 * 
	 * Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt
	 * (), Mockito.anyString())) .thenReturn(Optional.empty());
	 * 
	 * ResponseResource<QuoteDelegationDto> response =
	 * quotesController.getDelegatedUser(); assertTrue(response.getData() == null);
	 * }
	 * 
	 *//**
		 * for testing disable site - positive test case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testdisableIllsites() throws Exception {
	 * 
	 * ResponseResource<QuoteDetail> response = quotesController.deactivateSites(1,
	 * 2, 5, 1, QuoteConstants.DISABLE.toString()); assertTrue(response.getData() !=
	 * null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * for testing disable site - negative test case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testdisableIllsitesNegativeCase() throws Exception {
	 * Mockito.when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(null);
	 * ResponseResource<QuoteDetail> response = quotesController.deactivateSites(0,
	 * 0, 0, null, ""); assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * for testingtestQuotesDetailsBasedOnCustomerNegativeCase- positive test case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testQuotesDetailsBasedOnCustomer() throws Exception {
	 * Mockito.when(customerRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.of(quoteObjectCreator.getCustomer()));
	 * 
	 * Mockito.when(illSiteRepository.findByProductSolutionAndQuoteVersionAndStatus(
	 * Mockito.any(), Mockito.anyInt(),
	 * Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());
	 * 
	 * ResponseResource<List<QuoteBean>> response =
	 * quotesController.getQuotesDetailsBasedOnCustomer(1); assertTrue(response !=
	 * null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * for testing testQuotesDetailsBasedOnCustomerNegativeCase - negative test case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testQuotesDetailsBasedOnCustomerNegativeCase() throws
	 * Exception {
	 * Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())).thenReturn(null); ResponseResource<List<QuoteBean>>
	 * response = quotesController.getQuotesDetailsBasedOnCustomer(1);
	 * doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
	 * assertTrue(response.getStatus() == Status.FAILURE && response.getData() ==
	 * null); }
	 * 
	 *//**
		 * for getQuotesDetailsBasedOnCustomerLegalEntity- positive test case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testgetQuotesDetailsBasedOnCustomerLegalEntity() throws
	 * Exception {
	 * 
	 * Mockito.when(quoteToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.
	 * anyInt())) .thenReturn(quoteObjectCreator.getQuoteToLeList());
	 * 
	 * ResponseResource<List<QuoteBean>> response =
	 * quotesController.getQuotesDetailsBasedOnCustomerLegalEntity(1);
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * for testing getQuotesDetailsBasedOnCustomerLegalEntity - negative test case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void getQuotesDetailsBasedOnCustomerLegalEntity() throws
	 * Exception {
	 * Mockito.when(quoteToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.
	 * anyInt())).thenReturn(null);
	 * 
	 * ResponseResource<List<QuoteBean>> response =
	 * quotesController.getQuotesDetailsBasedOnCustomerLegalEntity(1);
	 * assertTrue(response.getData() == null || response.getStatus() ==
	 * Status.FAILURE); }
	 * 
	 *//**
		 * for testing testeditSites site - positive test case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testeditSites() throws Exception {
	 * 
	 * ResponseResource<QuoteDetail> response = quotesController.editSites(null,
	 * null, null, quoteObjectCreator.getUpdateRequest()); assertTrue(response !=
	 * null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * for testing testeditSites site - negative test case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testeditSitesNegativeCase() throws Exception {
	 * 
	 * ResponseResource<QuoteDetail> response = quotesController.editSites(null,
	 * null, null, null); assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * for testing dashboard api - negative case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testDashboardNegativeCase() throws Exception {
	 * 
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin",
	 * 1)).thenReturn((quoteObjectCreator.getRootUser()));
	 * 
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin",
	 * 1)).thenReturn((quoteObjectCreator.getRootUser())); Authentication
	 * authentication = mock(Authentication.class);
	 * 
	 * SecurityContext securityContext = mock(SecurityContext.class);
	 * 
	 * when(securityContext.getAuthentication()).thenReturn(authentication);
	 * 
	 * SecurityContextHolder.setContext(securityContext);
	 * when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
	 * .thenReturn(quoteObjectCreator.getUserInformation());
	 * 
	 * ResponseResource<DashBoardBean> response =
	 * quotesController.getDashboardDetails(null);
	 * 
	 * assertTrue(response.getData() != null); }
	 * 
	 *//**
		 * for testing dashboard api
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testDashboard() throws Exception {
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin", 1))
	 * .thenReturn((quoteObjectCreator.getRootUserWithQuote()));
	 * 
	 * Authentication authentication = mock(Authentication.class);
	 * 
	 * SecurityContext securityContext = mock(SecurityContext.class);
	 * 
	 * when(securityContext.getAuthentication()).thenReturn(authentication);
	 * 
	 * SecurityContextHolder.setContext(securityContext);
	 * when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
	 * .thenReturn(quoteObjectCreator.getUserInformation());
	 * ResponseResource<DashBoardBean> response =
	 * quotesController.getDashboardDetails(1);
	 * 
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * positive test case passing update request testApprovedQuotesPositiveCase
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testApprovedQuotes() throws TclCommonException {
	 * 
	 * Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.
	 * of(quoteObjectCreator.getQuote()));
	 * Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getQuote2());
	 * Mockito.when(orderRepository.findByQuoteAndStatus(Mockito.any(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getOrder());
	 * Mockito.when(orderRepository.save(quoteObjectCreator.getOrder())).thenReturn(
	 * quoteObjectCreator.getOrder());
	 * Mockito.when(quoteToLeRepository.findByQuoteAndQuoteVersion(Mockito.any(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getQuoteToLeList());
	 * Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any(
	 * ))) .thenReturn(new OrderProductComponentsAttributeValue());
	 * Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceNameAndVersion
	 * (Mockito.anyString(), Mockito.anyString(),
	 * Mockito.anyInt())).thenReturn(quoteObjectCreator.geQuotePrice());
	 * Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(new
	 * OrderPrice());
	 * Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(
	 * new OrderSiteFeasibility()); MockHttpServletRequest httpServletRequest = new
	 * MockHttpServletRequest(); httpServletRequest.setParameter("X-Forwarded-For",
	 * "127.0.0.1"); ResponseResource<QuoteDetail> response =
	 * quotesController.approvedQuotes(quoteObjectCreator.getUpdateRequest(),
	 * httpServletRequest);
	 * 
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * positive test case passing update request testApprovedQuotesPositiveCase
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testApprovedQuotesWithEmptyOrder() throws
	 * TclCommonException {
	 * 
	 * Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.
	 * of(quoteObjectCreator.getQuote()));
	 * Mockito.when(orderRepository.findByQuoteAndStatus(Mockito.any(),
	 * Mockito.anyByte())).thenReturn(null);
	 * Mockito.when(orderRepository.save(quoteObjectCreator.getOrder())).thenReturn(
	 * quoteObjectCreator.getOrder());
	 * Mockito.when(quoteToLeRepository.findByQuoteAndQuoteVersion(Mockito.any(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getQuoteToLeList());
	 * Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(new
	 * OrderToLe());
	 * Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(
	 * new OrderSiteFeasibility()); MockHttpServletRequest httpServletRequest = new
	 * MockHttpServletRequest(); httpServletRequest.setParameter("X-Forwarded-For",
	 * "127.0.0.1"); ResponseResource<QuoteDetail> response =
	 * quotesController.approvedQuotes(quoteObjectCreator.getUpdateRequest(),
	 * httpServletRequest); assertTrue(response.getData() != null); }
	 * 
	 *//**
		 * positive test case passing update request testApprovedQuotesPositiveCase
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testApprovedQuotesWithEmptyOrder1() throws
	 * TclCommonException {
	 * 
	 * when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getQuote2());
	 * Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.
	 * of(quoteObjectCreator.getQuote()));
	 * Mockito.when(orderRepository.findByQuoteAndStatus(Mockito.any(),
	 * Mockito.anyByte())).thenReturn(null);
	 * Mockito.when(orderRepository.save(quoteObjectCreator.getOrder())).thenReturn(
	 * quoteObjectCreator.getOrder());
	 * Mockito.when(quoteToLeRepository.findByQuoteAndQuoteVersion(Mockito.any(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getQuoteToLeList());
	 * Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndQuoteVersion(
	 * Mockito.any(), Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.getQuoteToLeFamilyList());
	 * Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getOrderToLes());
	 * Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndQuoteVersion(
	 * Mockito.any(), Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
	 * Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any())).thenReturn
	 * (new OrdersLeAttributeValue());
	 * Mockito.when(orderToLeProductFamilyRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getorderToLeProductFamilies());
	 * Mockito.when(orderProductSolutionRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getOrderProductSolution());
	 * Mockito.when(illSiteRepository.findByProductSolutionAndQuoteVersionAndStatus(
	 * Mockito.any(), Mockito.anyInt(),
	 * Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites2());
	 * Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any())).
	 * thenReturn(null);
	 * Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(
	 * null); Mockito.when(orderIllSitesRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getOrderIllSite()); Mockito.when(
	 * quoteProductComponentRepository.findByReferenceIdAndQuoteVersion(Mockito.
	 * anyInt(), Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.getQuoteProductComponent());
	 * Mockito.when(orderProductComponentRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getOrderProductComponent());
	 * Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceNameAndVersion
	 * (Mockito.anyString(), Mockito.anyString(),
	 * Mockito.anyInt())).thenReturn(quoteObjectCreator.getQuotePrice());
	 * Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getOrderPrice());
	 * Mockito.when(quoteProductComponentsAttributeValueRepository
	 * .findByQuoteProductComponent_IdAndQuoteVersion(Mockito.any(),
	 * Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
	 * Mockito.when(
	 * quoteProductComponentRepository.findByReferenceIdAndQuoteVersion(Mockito.
	 * anyInt(), Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.getQuoteProductComponent());
	 * Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(null);
	 * Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),
	 * Mockito.anyString())) .thenReturn(quoteObjectCreator.getAddressDetail());
	 * Mockito.when(order.getId()).thenReturn(1);
	 * Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(
	 * new OrderSiteFeasibility());
	 * 
	 * MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
	 * httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.approvedQuotes(quoteObjectCreator.getUpdateRequest(),
	 * httpServletRequest); assertTrue(response.getData() == null ||
	 * response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * negative test case passing update request as null
		 * testApprovedQuotesNegativeCase
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testApprovedQuotesNegativeCase() throws TclCommonException
	 * {
	 * Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(
	 * new OrderSiteFeasibility()); MockHttpServletRequest httpServletRequest = new
	 * MockHttpServletRequest(); httpServletRequest.setParameter("X-Forwarded-For",
	 * "127.0.0.1"); ResponseResource<QuoteDetail> response =
	 * quotesController.approvedQuotes(null, httpServletRequest);
	 * assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * negative test case passing quoteId as null testApprovedQuotesNegativeCase
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testApprovedQuotesNegativeCaseQuoteIdAsNull() throws
	 * TclCommonException {
	 * Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(
	 * new OrderSiteFeasibility());
	 * 
	 * Optional<Quote> optionalQuote = null;
	 * Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(
	 * optionalQuote); MockHttpServletRequest httpServletRequest = new
	 * MockHttpServletRequest(); httpServletRequest.setParameter("X-Forwarded-For",
	 * "127.0.0.1"); ResponseResource<QuoteDetail> response = quotesController
	 * .approvedQuotes(quoteObjectCreator.getUpdateRequestQuoteIdNull(),
	 * httpServletRequest); assertTrue(response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * negative test case passing customerId as null
		 * testGetQuotesDetailsBasedOnCustomer
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testGetQuotesDetailsBasedOnCustomeAsCustomerIdAsNull()
	 * throws TclCommonException {
	 * 
	 * Mockito.when(customerRepository.findById(Mockito.anyInt())).thenReturn((
	 * Optional.empty()));
	 * 
	 * ResponseResource<List<QuoteBean>> response =
	 * quotesController.getQuotesDetailsBasedOnCustomer(null);
	 * 
	 * assertTrue(response.getResponseCode() == 500 || response.getData().isEmpty()
	 * || response == null); }
	 * 
	 *//**
		 * positive test case passing customerId which returns Quote details
		 * testGetQuotesDetailsBasedOnCustomer
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * 
	 * @Test public void testGetQuotesDetailsBasedOnCustomer() throws
	 * TclCommonException {
	 * 
	 * Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getQuote());
	 * Mockito.when(quoteToLeRepository.findByQuoteAndQuoteVersion(Mockito.any(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getQuoteToLeList());
	 * Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndQuoteVersion(
	 * Mockito.any(), Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.getQuoteToLeFamilyList()); Mockito.when(
	 * productSolutionRepository.findByQuoteToLeProductFamilyAndQuoteVersion(Mockito
	 * .any(), Mockito.anyInt())) .thenReturn(quoteObjectCreator.getSolutionList());
	 * Mockito.when(illSiteRepository.findByProductSolutionAndQuoteVersionAndStatus(
	 * Mockito.any(), Mockito.anyInt(),
	 * Mockito.anyByte())).thenReturn(quoteObjectCreator.getIllsitesMock());
	 * Mockito.when(siteFeasibilityRepository.findByQuoteIllSiteAndIsSelected(
	 * Mockito.any(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getSiteFeasibilities());
	 * ResponseResource<List<QuoteBean>> response =
	 * quotesController.getQuotesDetailsBasedOnCustomer(1);
	 * 
	 * assertTrue(response.getData() != null && response.getStatus() ==
	 * Status.SUCCESS); }
	 * 
	 *//**
		 * positive test case passing customerLegalEntityId which returns Quote details
		 * 
		 * testGetQuotesDetailsBasedOnCustomerLegalEntity
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * 
	 * @Test public void testGetQuotesDetailsBasedOnCustomerLegalEntity() throws
	 * TclCommonException {
	 * 
	 * Mockito.when(quoteToLeRepository.findByErfCusCustomerLegalEntityId(1))
	 * .thenReturn((quoteObjectCreator.getQuoteToLeList()));
	 * 
	 * ResponseResource<List<QuoteBean>> response =
	 * quotesController.getQuotesDetailsBasedOnCustomerLegalEntity(1);
	 * 
	 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * negative test case passing customerLegalEntityId as null
		 * 
		 * testGetQuotesDetailsBasedOnCustomerLegalEntityAsIdNull
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * 
	 * @Test public void testGetQuotesDetailsBasedOnCustomerLegalEntityAsIdNull()
	 * throws TclCommonException {
	 * 
	 * Mockito.when(quoteToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.
	 * anyInt())) .thenReturn((quoteObjectCreator.getQuoteToLeList()));
	 * 
	 * ResponseResource<List<QuoteBean>> response =
	 * quotesController.getQuotesDetailsBasedOnCustomerLegalEntity(null);
	 * 
	 * assertTrue(response.getResponseCode() == 500 || response.getData().isEmpty()
	 * || response.getData() == null); }
	 * 
	 *//**
		 * possitive case for testupdateSiteProperties
		 * 
		 * testupdateSiteProperties
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * 
	 * @Test public void testupdateSiteProperties() throws TclCommonException {
	 * Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.
	 * craeteQuoteProductComponent()))
	 * .thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
	 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
	 * Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
	 * Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn((quoteObjectCreator.getMstProductComponentList()));
	 * Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any(
	 * ))) .thenReturn(quoteObjectCreator.getAttribute());
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.updateSiteProperties(null, null, null,
	 * quoteObjectCreator.getUpdateRequest()); assertTrue(response.getData() != null
	 * && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * negative case for testupdateSiteProperties
		 * 
		 * testupdateSiteProperties
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * 
	 * @Test public void testupdateSitePropertiesForException() throws
	 * TclCommonException {
	 * Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.
	 * craeteQuoteProductComponent()))
	 * .thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
	 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
	 * Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
	 * Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn((quoteObjectCreator.getMstProductComponentList()));
	 * Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any(
	 * ))) .thenReturn(quoteObjectCreator.getAttribute());
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.updateSiteProperties(null, null, null, null);
	 * assertTrue(response.getData() == null && response.getStatus() ==
	 * Status.FAILURE); }
	 * 
	 *//**
		 * negative case for testupdateSiteProperties
		 * 
		 * testupdateSiteProperties
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * 
	 * @Test public void testupdateSitePropertiesForInvaliedRequest() throws
	 * TclCommonException {
	 * Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.
	 * craeteQuoteProductComponent()))
	 * .thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
	 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
	 * Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
	 * Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn((quoteObjectCreator.getMstProductComponentList()));
	 * Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any(
	 * ))) .thenReturn(quoteObjectCreator.getAttribute());
	 * 
	 * UpdateRequest request = quoteObjectCreator.getUpdateRequest();
	 * request.setSiteId(null); ResponseResource<QuoteDetail> response =
	 * quotesController.updateSiteProperties(null, null, null, request);
	 * assertTrue(response.getData() == null && response.getStatus() ==
	 * Status.FAILURE); }
	 * 
	 *//**
		 * possitive case for testupdateSiteProperties
		 * 
		 * testupdateSiteProperties
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * 
	 * @Test public void testupdateSitePropertiesForProductAttribute() throws
	 * TclCommonException {
	 * Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.
	 * craeteQuoteProductComponent()))
	 * .thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
	 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte())) .thenReturn(null);
	 * Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getProductAtrributeMas());
	 * Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn((quoteObjectCreator.getMstProductComponentList()));
	 * Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any(
	 * ))) .thenReturn(new QuoteProductComponentsAttributeValue());
	 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
	 * anyString(), Mockito.anyByte())) .thenReturn(null);
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.updateSiteProperties(null, null, null,
	 * quoteObjectCreator.getUpdateRequest()); assertTrue(response.getData() != null
	 * && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * possitive case for testupdateLegalEntityProperties
		 * 
		 * testupdateLegalEntityProperties
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testupdateLegalEntityProperties() throws TclCommonException
	 * { Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteToLe()));
	 * Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getMstOmsAttribute());
	 * Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
	 * Mockito.when(quoteToLeRepository.save(new QuoteToLe()));
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.updateLegalEntityProperties(1, 1,
	 * quoteObjectCreator.getUpdateRequest()); QuoteDetail details=
	 * illQuoteService.updateLegalEntityProperties(
	 * quoteObjectCreator.getUpdateRequest()); assertTrue(details != null); }
	 * 
	 *//**
		 * possitive case for testupdateLegalEntityProperties
		 * 
		 * testupdateLegalEntityProperties
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testupdateLegalEntityPropertiesWithMstNull() throws
	 * TclCommonException {
	 * 
	 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
	 * anyString(), Mockito.anyByte())) .thenReturn(new ArrayList<>());
	 * Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getMstOmsAttribute());
	 * Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.updateLegalEntityProperties(1, 1,
	 * quoteObjectCreator.getUpdateRequest()); assertTrue(response.getData() != null
	 * && response.getStatus() == Status.SUCCESS); }
	 * 
	 *//**
		 * negative case for testupdateLegalEntityProperties
		 * 
		 * testupdateLegalEntityProperties
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testupdateLegalEntityPropertiesForException() throws
	 * TclCommonException {
	 * 
	 * 
	 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getMstAttributeList());
	 * 
	 * 
	 * Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getMstOmsAttribute());
	 * Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
	 * 
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.updateLegalEntityProperties(null, null, null);
	 * assertTrue(response.getData() == null && response.getStatus() ==
	 * Status.FAILURE); }
	 * 
	 *//**
		 * negative case for testupdateLegalEntityProperties
		 * 
		 * testupdateLegalEntityProperties
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void testupdateLegalEntityPropertiesForQuote() throws
	 * TclCommonException {
	 * 
	 * 
	 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getMstAttributeList());
	 * 
	 * 
	 * Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(
	 * quoteObjectCreator.getMstOmsAttribute());
	 * Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
	 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
	 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(
	 * Optional.empty());
	 * 
	 * UpdateRequest request = quoteObjectCreator.getUpdateRequest();
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.updateLegalEntityProperties(null, null, request);
	 * assertTrue(response.getData() == null && response.getStatus() ==
	 * Status.FAILURE); }
	 * 
	 *//**
		 * this test case tests the main application
		 * 
		 */
	/*
	 * // @Test public void TestApplicationContext() { String[] arr = { "", "" };
	 * OptimusOmsApplication.main(arr); }
	 * 
	 *//**
		 * this is for coverage all beans test.
		 * 
		 * @throws TclCommonException
		 */
	/*
	 * @Test public void coverBeanTest() { AttributeDetail attributeDetail = new
	 * AttributeDetail(); attributeDetail.setAttributeId(1);
	 * attributeDetail.setAttributeMasterId(1); attributeDetail.setName("beans");
	 * attributeDetail.setValue("beans");
	 * assertTrue(attributeDetail.getAttributeId() != null &&
	 * attributeDetail.getAttributeMasterId() != null && attributeDetail.getName()
	 * != null && attributeDetail.getValue() != null);
	 * 
	 * UserRequest userRequest = new UserRequest(); List<Integer> integerList = new
	 * ArrayList<>(); integerList.add(1); userRequest.setUserIds(integerList);
	 * assertTrue(userRequest != null && userRequest.getUserIds() != null &&
	 * !userRequest.getUserIds().isEmpty());
	 * 
	 * ProductAttributeMaster master = new ProductAttributeMaster();
	 * master.setCreatedBy("shekhar"); master.setCreatedTime(new Date());
	 * master.setDescription("someDescripetion"); ProductAttributeMasterBean
	 * productAttributeMasterBean = new ProductAttributeMasterBean(master);
	 * productAttributeMasterBean.setDescription("some discription");
	 * productAttributeMasterBean.setId(1);
	 * productAttributeMasterBean.setName("shekhar");
	 * productAttributeMasterBean.setStatus((byte) 1);
	 * assertTrue(productAttributeMasterBean.getDescription() != null &&
	 * productAttributeMasterBean.getId() != null &&
	 * productAttributeMasterBean.getName() != null &&
	 * productAttributeMasterBean.getStatus() != null);
	 * 
	 * DashBoardSiteBean dashBoardSiteBean = new DashBoardSiteBean();
	 * dashBoardSiteBean.setStage("stage"); dashBoardSiteBean.setStatus((byte) 1);
	 * assertTrue(dashBoardSiteBean.getStatus() == 1 && dashBoardSiteBean.getStage()
	 * != null);
	 * 
	 * }
	 * 
	 * 
	 * positive test case
	 * 
	 * @Test public void testgetSiteProperties() throws TclCommonException {
	 * ResponseResource<List<QuoteProductComponentBean>> response =
	 * quotesController.getSiteProperties(0, 1, 1); assertTrue(response.getData() !=
	 * null && response.getStatus() == Status.SUCCESS); }
	 * 
	 * 
	 * negative test case : when siteId is null
	 * 
	 * @Test public void testgetSitePropertiesForException() throws
	 * TclCommonException { ResponseResource<List<QuoteProductComponentBean>>
	 * response = quotesController.getSiteProperties(null, null, null);
	 * assertTrue(response.getResponseCode() == 500 || response.getData().isEmpty()
	 * || response == null); }
	 * 
	 * 
	 * negative test case : when IllSites are not present
	 * 
	 * @Test public void testgetSitePropertiesExceptionForEmptySiteProperties()
	 * throws TclCommonException {
	 * Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())).thenReturn(null);
	 * ResponseResource<List<QuoteProductComponentBean>> response =
	 * quotesController.getSiteProperties(0, null, null);
	 * assertTrue(response.getResponseCode() == 500 || response.getData().isEmpty()
	 * || response == null); }
	 * 
	 * 
	 * Get all attribute details by quote to le id : Positive case
	 * 
	 * @throws TclCommonException
	 * 
	 * @Test public void getAttributeDetailsByQuoteToLeId() throws
	 * TclCommonException { ResponseResource<Set<LegalAttributeBean>> response =
	 * quotesController.getAllAttributesByQuoteToLeId(1, 1);
	 * assertTrue(response.getData() != null && response.getStatus() ==
	 * Status.SUCCESS); }
	 * 
	 * 
	 * Get all attribute details by quote to le id : Negative case : Quote to le id
	 * null
	 * 
	 * @throws TclCommonException
	 * 
	 * @Test public void getAttributeDetailsByQuoteToLeIdWithoutID() throws
	 * TclCommonException { ResponseResource<Set<LegalAttributeBean>> response =
	 * quotesController.getAllAttributesByQuoteToLeId(null, null);
	 * assertTrue(response != null && response.getStatus() == Status.FAILURE); }
	 * 
	 * 
	 * Get all attribute details by quote to le id : Negative case --> Incorrect
	 * Information
	 * 
	 * @throws TclCommonException
	 * 
	 * @Test public void getAttributeDetailsByQuoteToLeIdWithoutData() throws
	 * TclCommonException {
	 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(
	 * Optional.empty()); ResponseResource<Set<LegalAttributeBean>> response =
	 * quotesController.getAllAttributesByQuoteToLeId((Integer) 1, 1);
	 * assertTrue(response != null && response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * for testing delete site testdeleteIllsites - negative case
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testdeleteIllsitesNegative() throws Exception {
	 * 
	 * Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
	 * Mockito.anyByte())).thenReturn(null); ResponseResource<QuoteDetail> response
	 * = quotesController.deactivateSites(0, 0, 0, null,
	 * QuoteConstants.DELETE.toString()); assertTrue(response != null &&
	 * response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * for testing delete site testdeleteIllsites Positive with no effect
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testdeleteIllsitesNoEffer() throws Exception {
	 * 
	 * Mockito.when(quoteProductComponentRepository.findByReferenceId(0))
	 * .thenReturn(quoteObjectCreator.getQuoteProductComponent());
	 * Mockito.when(illSiteRepository.findById(0)).thenReturn(Optional.ofNullable(
	 * quoteObjectCreator.getIllsites()));
	 * Mockito.when(userRepository.findByUsernameAndStatus("admin",
	 * 1)).thenReturn(quoteObjectCreator.getUser()); ResponseResource<QuoteDetail>
	 * response = quotesController.deactivateSites(0, 0, 0, null,
	 * QuoteConstants.ATTRIBUTES.toString()); assertTrue(response != null &&
	 * response.getStatus() == Status.FAILURE); }
	 * 
	 * 
	 * poditive test case for updateQuoteToLeStatus Input param : quoteToLeId,status
	 * response : QuoteDetail
	 * 
	 * // @Test public void testUpdateQuoteToLeStatus() throws TclCommonException {
	 * 
	 * when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
	 * when(quoteToLeRepository.save(new QuoteToLe()));
	 * 
	 * QuoteDetail response = illQuoteService.updateQuoteToLeStatus(1, "test");
	 * assertTrue(response != null);
	 * 
	 * }
	 * 
	 * 
	 * negative test case for updateQuoteToLeStatus Input param : quoteToLeId,status
	 * response : Exception
	 * 
	 * //@Test public void testUpdateQuoteToLeStatusforNullquoteToLeIdstatus()
	 * throws TclCommonException {
	 * 
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.updateQuoteToLeStatus(null, null, null);
	 * assertTrue(response.getData() == null && response.getStatus() ==
	 * Status.FAILURE); }
	 * 
	 * 
	 * negative test case for updateQuoteToLeStatus Input param : quoteToLeId,status
	 * response : Exception
	 * 
	 * //@Test public void testUpdateQuoteToLeStatusforEmptyQuoteToLe() throws
	 * TclCommonException {
	 * when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.updateQuoteToLeStatus(1, 1, "Test");
	 * assertTrue(response.getData() == null && response.getStatus() ==
	 * Status.FAILURE); }
	 * 
	 * 
	 * positive test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 * 
	 * @Test public void testPersistListOfQuoteLeAttributes() throws
	 * TclCommonException {
	 * when(userRepository.findByUsernameAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getUser());
	 * when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
	 * when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getMstAttributeList());
	 * when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(
	 * Mockito.any(), Mockito.anyString()))
	 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
	 * when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(new
	 * QuoteLeAttributeValue()); ResponseResource<QuoteDetail> response =
	 * quotesController.persistListOfQuoteLeAttributes(1, 1,
	 * quoteObjectCreator.getUpdateRequest()); assertTrue(response.getData() != null
	 * && response.getStatus() == Status.SUCCESS); }
	 * 
	 * 
	 * positive test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 * 
	 * @Test public void
	 * testPersistListOfQuoteLeAttributesforNullQuoteLeAttributeValue() throws
	 * TclCommonException {
	 * when(userRepository.findByUsernameAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getUser());
	 * when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
	 * when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),
	 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getMstAttributeList());
	 * when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(
	 * Mockito.any(), Mockito.anyString())) .thenReturn(null);
	 * when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(new
	 * QuoteLeAttributeValue()); ResponseResource<QuoteDetail> response =
	 * quotesController.persistListOfQuoteLeAttributes(1, 1,
	 * quoteObjectCreator.getUpdateRequest()); assertTrue(response.getData() != null
	 * && response.getStatus() == Status.SUCCESS); }
	 * 
	 * 
	 * positive test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 * 
	 * @Test public void testPersistListOfQuoteLeAttributesforNullMstAttr() throws
	 * TclCommonException {
	 * when(userRepository.findByUsernameAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getUser());
	 * when(quoteToLeRepository.findById(Mockito.anyInt()))
	 * .thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
	 * when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),
	 * Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getMstAttributeListWithNullAttr());
	 * when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(new
	 * MstOmsAttribute());
	 * when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(
	 * Mockito.any(), Mockito.anyString()))
	 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.persistListOfQuoteLeAttributes(1, 1,
	 * quoteObjectCreator.getUpdateRequest()); }
	 * 
	 * 
	 * negative test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 * 
	 * @Test public void testPersistListOfQuoteLeAttributesForNullQuoteToLe() throws
	 * TclCommonException {
	 * when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
	 * ResponseResource<QuoteDetail> response =
	 * quotesController.persistListOfQuoteLeAttributes(1, 1,
	 * quoteObjectCreator.getUpdateRequest()); assertTrue(response.getData() == null
	 * && response.getStatus() == Status.FAILURE); }
	 * 
	 * 
	 * negative test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 * 
	 * @Test public void testPersistListOfQuoteLeAttributesForNullUser() throws
	 * TclCommonException {
	 * Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(),
	 * Mockito.anyInt())).thenReturn((null)); ResponseResource<QuoteDetail> response
	 * = quotesController.persistListOfQuoteLeAttributes(1, 1,
	 * quoteObjectCreator.getUpdateRequest()); assertTrue(response.getData() == null
	 * && response.getStatus() == Status.FAILURE); }
	 * 
	 *//**
		 * for Positive
		 * 
		 * @throws Exception
		 *//*
			 * @Test public void testtriggerForFeasibilityBean() throws Exception {
			 * 
			 * List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();
			 * 
			 * QuoteProductComponentsAttributeValue obj1 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj1.getProductAttributeMaster().setId(1); list.add(obj1);
			 * 
			 * QuoteProductComponentsAttributeValue obj2 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj2.getProductAttributeMaster().setId(2); list.add(obj2);
			 * 
			 * QuoteProductComponentsAttributeValue obj3 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj3.getProductAttributeMaster().setId(3); obj3.setAttributeValues("1=2");
			 * list.add(obj3);
			 * 
			 * QuoteProductComponentsAttributeValue obj4 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj3.setAttributeValues("1=2"); obj3.getProductAttributeMaster().setId(4);
			 * list.add(obj4);
			 * 
			 * QuoteProductComponentsAttributeValue obj5 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj5.getProductAttributeMaster().setId(5);
			 * obj5.setAttributeValues("Fully Managed"); list.add(obj5);
			 * 
			 * QuoteProductComponentsAttributeValue obj14 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj14.getProductAttributeMaster().setId(14);
			 * obj14.setAttributeValues("Physically Managed"); list.add(obj14);
			 * 
			 * QuoteProductComponentsAttributeValue obj15 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj15.getProductAttributeMaster().setId(15);
			 * obj15.setAttributeValues("proactive_services"); list.add(obj15);
			 * 
			 * QuoteProductComponentsAttributeValue obj16 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj15.getProductAttributeMaster().setId(16);
			 * obj15.setAttributeValues("Configuration Management"); list.add(obj16);
			 * 
			 * QuoteProductComponentsAttributeValue obj13 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj15.getProductAttributeMaster().setId(13);
			 * obj15.setAttributeValues("Unmanaged"); list.add(obj13);
			 * 
			 * QuoteProductComponentsAttributeValue obj6 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj6.getProductAttributeMaster().setId(6); list.add(obj6);
			 * 
			 * QuoteProductComponentsAttributeValue obj7 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj7.getProductAttributeMaster().setId(7); list.add(obj7);
			 * 
			 * QuoteProductComponentsAttributeValue obj8 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj8.getProductAttributeMaster().setId(8); list.add(obj8);
			 * 
			 * QuoteProductComponentsAttributeValue obj9 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj9.getProductAttributeMaster().setId(9); list.add(obj9);
			 * 
			 * QuoteProductComponentsAttributeValue obj10 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj10.getProductAttributeMaster().setId(10); list.add(obj10);
			 * 
			 * QuoteProductComponentsAttributeValue obj11 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj11.getProductAttributeMaster().setId(11); list.add(obj11);
			 * 
			 * QuoteProductComponentsAttributeValue obj12 =
			 * quoteObjectCreator.createQuoteProductComponentsAttributeValue();
			 * obj12.getProductAttributeMaster().setId(12); list.add(obj12);
			 * 
			 * Mockito.when(quoteProductComponentsAttributeValueRepository
			 * .findByQuoteProductComponent_IdAndQuoteVersion(Mockito.anyInt(),
			 * Mockito.anyInt())).thenReturn(list);
			 * 
			 * ProductAttributeMaster pm2 = quoteObjectCreator.getProductAtrributeMas();
			 * pm2.setName("Port Bandwidth");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(2))).
			 * thenReturn(Optional.of(pm2));
			 * 
			 * ProductAttributeMaster pm3 = quoteObjectCreator.getProductAtrributeMas();
			 * pm3.setName("IPv6 Address Pool Size");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(3))).
			 * thenReturn(Optional.of(pm3));
			 * 
			 * ProductAttributeMaster pm4 = quoteObjectCreator.getProductAtrributeMas();
			 * pm4.setName("IPv4 Address Pool Size");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(4))).
			 * thenReturn(Optional.of(pm4));
			 * 
			 * ProductAttributeMaster pm5 = quoteObjectCreator.getProductAtrributeMas();
			 * pm5.setName("CPE Management Type");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(5))).
			 * thenReturn(Optional.of(pm5));
			 * 
			 * ProductAttributeMaster pm13 = quoteObjectCreator.getProductAtrributeMas();
			 * pm13.setName("CPE Management Type");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(13))).
			 * thenReturn(Optional.of(pm13));
			 * 
			 * ProductAttributeMaster pm14 = quoteObjectCreator.getProductAtrributeMas();
			 * pm14.setName("CPE Management Type");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(14))).
			 * thenReturn(Optional.of(pm14));
			 * 
			 * ProductAttributeMaster pm15 = quoteObjectCreator.getProductAtrributeMas();
			 * pm15.setName("CPE Management Type");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(15))).
			 * thenReturn(Optional.of(pm15));
			 * 
			 * ProductAttributeMaster pm16 = quoteObjectCreator.getProductAtrributeMas();
			 * pm16.setName("CPE Management Type");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(16))).
			 * thenReturn(Optional.of(pm16));
			 * 
			 * ProductAttributeMaster pm6 = quoteObjectCreator.getProductAtrributeMas();
			 * pm6.setName("Interface");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(6))).
			 * thenReturn(Optional.of(pm6));
			 * 
			 * ProductAttributeMaster pm7 = quoteObjectCreator.getProductAtrributeMas();
			 * pm7.setName("CPE");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(7))).
			 * thenReturn(Optional.of(pm7));
			 * 
			 * ProductAttributeMaster pm8 = quoteObjectCreator.getProductAtrributeMas();
			 * pm8.setName("CPE Basic Chassis");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(8))).
			 * thenReturn(Optional.of(pm8));
			 * 
			 * ProductAttributeMaster pm9 = quoteObjectCreator.getProductAtrributeMas();
			 * pm9.setName("Service Variant");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(9))).
			 * thenReturn(Optional.of(pm9));
			 * 
			 * ProductAttributeMaster pm10 = quoteObjectCreator.getProductAtrributeMas();
			 * pm10.setName("Local Loop Bandwidth");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(10))).
			 * thenReturn(Optional.of(pm10));
			 * 
			 * ProductAttributeMaster pm11 = quoteObjectCreator.getProductAtrributeMas();
			 * pm11.setName("CPE Basic Chassis");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(11))).
			 * thenReturn(Optional.of(pm11));
			 * 
			 * ProductAttributeMaster pm12 = quoteObjectCreator.getProductAtrributeMas();
			 * pm12.setName("CPE Basic Chassis");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(12))).
			 * thenReturn(Optional.of(pm12));
			 * 
			 * ProductAttributeMaster pm1 = quoteObjectCreator.getProductAtrributeMas();
			 * pm1.setName("Burstable Bandwidth");
			 * Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(1))).
			 * thenReturn(Optional.of(pm1));
			 * 
			 * Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(new
			 * QuoteToLe()); Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),
			 * Mockito.eq("1"))) .thenReturn(quoteObjectCreator.getCustomerDetailsBean());
			 * ResponseResource<String> response = quotesController
			 * .triggerForFeasibilityBean(quoteObjectCreator.getFeasibilityBean());
			 * assertTrue(response != null && response.getStatus() == Status.SUCCESS); }
			 * 
			 * 
			 * Positive test case for getContactAttributeDetails
			 * 
			 * @Test public void testGetContactAttributeDetails() throws TclCommonException
			 * {
			 * 
			 * List<String> nameValue = Arrays.asList("CONTACTID", "CONTACTNAME",
			 * "CONTACTEMAIL", "DESIGNATION", "CONTACTNO"); nameValue.forEach(value -> {
			 * Optional<QuoteToLe> quToLe =
			 * quoteObjectCreator.returnQuoteToLeForUpdateStatusForContactAttributeInfo();
			 * Set<QuoteLeAttributeValue> set = new HashSet<>(); QuoteLeAttributeValue
			 * quoteLeAttributeValue = new QuoteLeAttributeValue(); MstOmsAttribute
			 * mstOmsAttribute = new MstOmsAttribute(); mstOmsAttribute.setName(value);
			 * quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			 * set.add(quoteLeAttributeValue); quToLe.get().setQuoteLeAttributeValues(set);
			 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(
			 * quToLe); try { ResponseResource<ContactAttributeInfo> response =
			 * quotesController.getContactAttributeDetails(1, 1);
			 * assertTrue(response.getData() != null && response.getStatus() ==
			 * Status.SUCCESS); } catch (TclCommonException e) { e.printStackTrace(); } });
			 * ResponseResource<ContactAttributeInfo> response =
			 * quotesController.getContactAttributeDetails(1, 1);
			 * assertTrue(response.getData() != null && response.getStatus() ==
			 * Status.SUCCESS);
			 * 
			 * }
			 * 
			 * 
			 * Negative test case for getContactAttributeDetails
			 * 
			 * @Test public void testGetContactAttributeDetailsForExceotion() throws
			 * TclCommonException {
			 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null)
			 * ; ResponseResource<ContactAttributeInfo> response =
			 * quotesController.getContactAttributeDetails(1, 1);
			 * assertTrue(response.getData() == null && response.getStatus() ==
			 * Status.FAILURE);
			 * 
			 * }
			 * 
			 * 
			 * Positive test case for updateSdfcStage
			 * 
			 * @Test public void testUpdateSdfcStage() throws TclCommonException {
			 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
			 * .thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
			 * ResponseResource<String> response = quotesController.updateSdfcStage(1, 1,
			 * "Proposal Sent"); }
			 * 
			 * 
			 * Positive test case for updateBillingInfoForSfdc
			 * 
			 * @Test public void testUpdateBillingInfoForSfdc() throws TclCommonException {
			 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
			 * .thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
			 * Mockito.when(quoteLeAttributeValueRepository.
			 * findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
			 * Mockito.anyString())).thenReturn(quoteObjectCreator.
			 * getQuoteLeAttributeValueList());
			 * Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(
			 * new QuoteLeAttributeValue()); ResponseResource<QuoteBean> response =
			 * quotesController
			 * .updateBillingInfoForSfdc(quoteObjectCreator.getBillingRequest());
			 * assertTrue(response.getData() != null && response.getStatus() ==
			 * Status.SUCCESS); }
			 * 
			 * 
			 * Positive test case for updateBillingInfoForSfdc
			 * 
			 * @Test public void testUpdateBillingInfoForSfdcForNullQuoteLeAttributeValue()
			 * throws TclCommonException {
			 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
			 * .thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
			 * Mockito.when(quoteLeAttributeValueRepository.
			 * findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
			 * Mockito.anyString())).thenReturn(null);
			 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getMstAttributeListWithNullAttr());
			 * Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(new
			 * MstOmsAttribute()); ResponseResource<QuoteBean> response = quotesController
			 * .updateBillingInfoForSfdc(quoteObjectCreator.getBillingRequest());
			 * assertTrue(response.getData() != null && response.getStatus() ==
			 * Status.SUCCESS); }
			 * 
			 * 
			 * negative test case for updateBillingInfoForSfdc
			 * 
			 * @Test public void testUpdateBillingInfoForSfdcForException() throws
			 * TclCommonException {
			 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null)
			 * ; ResponseResource<QuoteBean> response = quotesController
			 * .updateBillingInfoForSfdc(quoteObjectCreator.getBillingRequest());
			 * assertTrue(response.getData() == null && response.getStatus() ==
			 * Status.FAILURE); }
			 * 
			 * 
			 * 
			 * test case for triggering pricing
			 * 
			 * @Test public void testpricing() throws TclCommonException {
			 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
			 * .thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteToLe1()));
			 * 
			 * Mockito.when(siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelected(
			 * Mockito.anyInt(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getSiteFeasibilities());
			 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getMstAttributeList());
			 * Mockito.when(quoteLeAttributeValueRepository.
			 * findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
			 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
			 * 
			 * Mockito.when(siteFeasibilityRepository.save(Mockito.any())).thenReturn(new
			 * SiteFeasibility());
			 * Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(new
			 * QuoteToLe());
			 * Mockito.when(pricingDetailsRepository.save(Mockito.any())).thenReturn(new
			 * PricingDetail());
			 * Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingType(Mockito.
			 * anyString(), Mockito.anyString()))
			 * .thenReturn(quoteObjectCreator.getPricingDetails());
			 * 
			 * ResponseResource<String> response = quotesController.triggerPricing(1, 1);
			 * assertTrue(response.getData() != null && response.getStatus() !=
			 * Status.FAILURE); }
			 * 
			 * 
			 * Negitive test case for triggering pricing
			 * 
			 * test case is not required as of now //@Test public void testpricingWithNull()
			 * throws TclCommonException {
			 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
			 * .thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteToLe1()));
			 * 
			 * Mockito.when(siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelected(
			 * Mockito.anyInt(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getSiteFeasibilities());
			 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getMstAttributeList());
			 * Mockito.when(quoteLeAttributeValueRepository.
			 * findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
			 * .thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
			 * 
			 * Mockito.when(siteFeasibilityRepository.save(Mockito.any())).thenReturn(new
			 * SiteFeasibility());
			 * Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(new
			 * QuoteToLe());
			 * Mockito.when(pricingDetailsRepository.save(Mockito.any())).thenReturn(new
			 * PricingDetail());
			 * Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingType(Mockito.
			 * anyString(), Mockito.anyString()))
			 * .thenReturn(quoteObjectCreator.getPricingDetails());
			 * 
			 * ResponseResource<String> response = quotesController.triggerPricing(null,
			 * null); assertTrue(response == null); }
			 * 
			 * 
			 * Positive test case for feasibilityCheck
			 * 
			 * @Test public void testFeasibilityCheck() throws TclCommonException {
			 * List<String> nameValue = Arrays.asList("is_feasiblity_check_done",
			 * "is_pricing_check_done"); nameValue.forEach(value -> { Optional<QuoteToLe>
			 * quToLe =
			 * quoteObjectCreator.returnQuoteToLeForUpdateStatusForContactAttributeInfo();
			 * Set<QuoteLeAttributeValue> set = new HashSet<>(); QuoteLeAttributeValue
			 * quoteLeAttributeValue = new QuoteLeAttributeValue(); MstOmsAttribute
			 * mstOmsAttribute = new MstOmsAttribute(); mstOmsAttribute.setName(value);
			 * quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			 * set.add(quoteLeAttributeValue); quToLe.get().setQuoteLeAttributeValues(set);
			 * Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(
			 * quToLe); try { ResponseResource<QuoteLeAttributeBean> response =
			 * quotesController.feasibilityCheck(1); assertTrue(response.getData() != null
			 * && response.getStatus() == Status.SUCCESS); } catch (TclCommonException e) {
			 * e.printStackTrace(); } }); }
			 * 
			 * 
			 * Positive test case for updateSitePropertiesAttributes
			 * 
			 * // @Test public void testUpdateSitePropertiesAttributes() throws Throwable {
			 * Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
			 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getIllsites());
			 * Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString
			 * (), Mockito.anyByte())) .thenReturn(quoteObjectCreator.geProductFamily());
			 * Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getMstProductComponentListForNull());
			 * Mockito.when(quoteProductComponentRepository.
			 * findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
			 * Mockito.any())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
			 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
			 * Mockito.when(quoteProductComponentsAttributeValueRepository
			 * .findByQuoteProductComponentAndProductAttributeMaster(Mockito.any(),
			 * Mockito.any()))
			 * .thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());
			 * Mockito.when(quoteProductComponentsAttributeValueRepository.save(new
			 * QuoteProductComponentsAttributeValue())); QuoteDetail response =
			 * illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.
			 * getUpdateRequest()); assertTrue(response != null);
			 * ResponseResource<QuoteDetail> response = quotesController
			 * .updateSitePropertiesAttributes(1,1,1,quoteObjectCreator.getUpdateRequest());
			 * assertTrue(response.getData() != null && response.getStatus() ==
			 * Status.SUCCESS); }
			 * 
			 * 
			 * Positive test case for updateSitePropertiesAttributes
			 * 
			 * // @Test public void
			 * testUpdateSitePropertiesAttributesForNullQuoteProductComponentsAttributeValue
			 * () throws TclCommonException {
			 * Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
			 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getIllsites());
			 * Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString
			 * (), Mockito.anyByte())) .thenReturn(quoteObjectCreator.geProductFamily());
			 * Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getMstProductComponentListForNull());
			 * Mockito.when(quoteProductComponentRepository.
			 * findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
			 * Mockito.any())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
			 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getProductAtrributeMaster());
			 * Mockito.when(quoteProductComponentsAttributeValueRepository
			 * .findByQuoteProductComponentAndProductAttributeMaster(Mockito.any(),
			 * Mockito.any())).thenReturn(null);
			 * Mockito.when(quoteProductComponentsAttributeValueRepository.save(new
			 * QuoteProductComponentsAttributeValue()));
			 * 
			 * QuoteDetail response =
			 * illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.
			 * getUpdateRequest()); assertTrue(response != null);
			 * ResponseResource<QuoteDetail> response = quotesController
			 * .updateSitePropertiesAttributes(1,1,1,quoteObjectCreator.getUpdateRequest());
			 * assertTrue(response.getData() != null && response.getStatus() ==
			 * Status.SUCCESS);
			 * 
			 * }
			 * 
			 * 
			 * Positive test case for updateSitePropertiesAttributes
			 * 
			 * // @Test public void
			 * testUpdateSitePropertiesAttributesForNullProductAttributeMaster() throws
			 * TclCommonException {
			 * Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
			 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getIllsites());
			 * Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString
			 * (), Mockito.anyByte())) .thenReturn(quoteObjectCreator.geProductFamily());
			 * Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getMstProductComponentListForNull());
			 * Mockito.when(quoteProductComponentRepository.
			 * findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
			 * Mockito.any())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
			 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
			 * anyString(), Mockito.anyByte())) .thenReturn(null);
			 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getProductAtrributeMasterForNull());
			 * Mockito.when(productAttributeMasterRepository.save(Mockito.any())).thenReturn
			 * (new ProductAttributeMaster());
			 * Mockito.when(quoteProductComponentsAttributeValueRepository.save(new
			 * QuoteProductComponentsAttributeValue()));
			 * 
			 * QuoteDetail response =
			 * illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.
			 * getUpdateRequest()); assertTrue(response != null);
			 * ResponseResource<QuoteDetail> response = quotesController
			 * .updateSitePropertiesAttributes(1,1,1,quoteObjectCreator.getUpdateRequest());
			 * assertTrue(response.getData() != null && response.getStatus() ==
			 * Status.SUCCESS);
			 * 
			 * }
			 * 
			 * 
			 * Positive test case for updateSitePropertiesAttributes
			 * 
			 * 
			 * // @Test //@Test public void
			 * testUpdateSitePropertiesAttributesForNullQuoteProductComponent() throws
			 * TclCommonException {
			 * Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
			 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getIllsites());
			 * Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString
			 * (), Mockito.anyByte())) .thenReturn(quoteObjectCreator.geProductFamily());
			 * Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getMstProductComponentListForNull());
			 * Mockito.when(quoteProductComponentRepository.
			 * findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
			 * Mockito.any())).thenReturn(null);
			 * Mockito.when(quoteProductComponentRepository.save(Mockito.any())).thenReturn(
			 * new QuoteProductComponent());
			 * Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.
			 * anyString(), Mockito.anyByte()))
			 * .thenReturn(quoteObjectCreator.getProductAtrributeMasterForNull());
			 * Mockito.when(productAttributeMasterRepository.save(Mockito.any())).thenReturn
			 * (new ProductAttributeMaster());
			 * Mockito.when(quoteProductComponentsAttributeValueRepository.save(new
			 * QuoteProductComponentsAttributeValue()));
			 * 
			 * 
			 * // QuoteDetail response = //
			 * illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.
			 * getUpdateRequest()); // assertTrue(response != null); QuoteDetail response =
			 * illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.
			 * getUpdateRequest()); assertTrue(response != null);
			 * ResponseResource<QuoteDetail> response = quotesController
			 * .updateSitePropertiesAttributes(1,1,1,quoteObjectCreator.getUpdateRequest());
			 * assertTrue(response.getData() != null && response.getStatus() ==
			 * Status.SUCCESS); }
			 * 
			 * 
			 * Negative test case for updateSitePropertiesAttributes by passing null
			 * UpdateRequest
			 * 
			 * @Test public void testUpdateSitePropertiesAttributesForMstProductFamily()
			 * throws TclCommonException {
			 * Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
			 * Mockito.anyByte())) .thenReturn(quoteObjectCreator.getIllsites());
			 * Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString
			 * (), Mockito.anyByte())) .thenReturn(null); ResponseResource<QuoteDetail>
			 * response = quotesController.updateSitePropertiesAttributes(1, 1, 1,
			 * quoteObjectCreator.getUpdateRequest()); assertTrue(response.getData() == null
			 * && response.getStatus() == Status.FAILURE); }
			 * 
			 * 
			 * Negative test case for updateSitePropertiesAttributes by passing null
			 * UpdateRequest
			 * 
			 * @Test public void testUpdateSitePropertiesAttributesForQuoteIllSite() throws
			 * TclCommonException {
			 * Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),
			 * Mockito.anyByte())).thenReturn(null); ResponseResource<QuoteDetail> response
			 * = quotesController.updateSitePropertiesAttributes(1, 1, 1,
			 * quoteObjectCreator.getUpdateRequest()); assertTrue(response.getData() == null
			 * && response.getStatus() == Status.FAILURE); }
			 * 
			 * 
			 * Negative test case for updateSitePropertiesAttributes by passing null
			 * UpdateRequest
			 * 
			 * @Test public void testUpdateSitePropertiesAttributesForNullUpdateRequest()
			 * throws TclCommonException { ResponseResource<QuoteDetail> response =
			 * quotesController.updateSitePropertiesAttributes(1, 1, 1, null);
			 * assertTrue(response.getData() == null && response.getStatus() ==
			 * Status.FAILURE); }
			 */

	/**
	 * 
	 * init- predefined mocks
	 * 
	 * @throws TclCommonException
	 */
	@Before
	public void init() throws TclCommonException {
		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());
		Mockito.when(authTokenDetailRepository.find(Mockito.any())).thenReturn(quoteObjectCreator.getUSerInfp());

		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn((quoteObjectCreator.getMstProductComponent()));
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn((quoteObjectCreator.getUser()));
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findByIdInAndStatus(Mockito.anyList(), Mockito.anyByte()))
				.thenReturn((quoteObjectCreator.getIllsitesMock()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.geQuotePrice());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		Mockito.when(orderIllSiteSlaRepository.save(Mockito.any())).thenReturn(new OrderIllSiteSla());

		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))

				.thenReturn((quoteObjectCreator.getCustomer()));
		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(Mockito.any(),
				Mockito.anyString(), Mockito.anyByte())).thenReturn((quoteObjectCreator.getMstOffering()));

		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(Mockito.any(),
				Mockito.anyString(), Mockito.anyByte())).thenReturn((quoteObjectCreator.getMstOffering()));

		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(
				quoteObjectCreator.getQuoteToLeFamily(), quoteObjectCreator.getMstOffering()))
				.thenReturn((quoteObjectCreator.getSolution()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getAttribute()));
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(new OrderConfirmationAudit());

		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getCustomer());
		Mockito.when(customerRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getUserList()));
		Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt(), Mockito.matches("Open")))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteDelegation()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
				Mockito.anyInt(), Mockito.matches("SITE_PROPERTIES")))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(Mockito.anyInt()
						, Mockito.matches("LOCAL_IT_CONTACT")))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentsAttributeValueRepository)
				.delete(new QuoteProductComponentsAttributeValue());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentRepository)
				.delete(new QuoteProductComponent());
		Mockito.doThrow(new RuntimeException()).when(illSiteRepository).save(new QuoteIllSite());
		Mockito.doThrow(new RuntimeException()).when(quoteToLeRepository).save(new QuoteToLe());
		/*
		 * mock(UserInformation.class);
		 * 
		 * Authentication authentication = mock(Authentication.class);
		 * 
		 * SecurityContext securityContext = mock(SecurityContext.class);
		 * 
		 * when(securityContext.getAuthentication()).thenReturn(authentication);
		 * 
		 * SecurityContextHolder.setContext(securityContext);
		 * when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
		 * .thenReturn(quoteObjectCreator.getUserInformation());
		 */
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());

		when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote2()));

		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getCustomerDetailsBean());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(quoteObjectCreator.createProductSolutions());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(),
				 Mockito.anyString())).thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getAddressDetail());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getCustomer());
		Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));
		doNothing().when(siteFeasibilityRepository).delete(Mockito.any());
		doNothing().when(siteFeasibilityRepository).deleteAll(Mockito.any());
	}

	/**
	 * 
	 * testcreateDocument - positive case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testcreateDocument() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
		Mockito.when(authTokenDetailRepository.find(Mockito.any())).thenReturn(quoteObjectCreator.getUSerInfp());

		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(new QuoteToLe());
		Mockito.when(mqUtils.sendAndReceive(Mockito.any(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getCustomerLedetailsBean());
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(null);
		ResponseResource<CreateDocumentDto> response = quotesController.createDocument(1, 1,
				quoteObjectCreator.getDocumentDto());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	/**
	 * 
	 * testcreateDocument - negative case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testcreateDocumentException() throws TclCommonException {
		Mockito.when(quoteRepository.findById(null)).thenReturn((Optional.ofNullable(null)));
		ResponseResource<CreateDocumentDto> response = quotesController.createDocument(null, null, null);
		assertTrue(response.getStatus() == Status.FAILURE);

	}

	/**
	 * test create quote positive test case
	 **/
	@Test
	public void testcreateQuote() throws Exception {
		ResponseResource<QuoteResponse> response = quotesController.createQuote(quoteObjectCreator.getQuoteDetail(), 1,false);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote Negative test case
	 **/
	@Test
	public void testcreateQuoteInvalidCustomer() throws Exception {
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(null);
		ResponseResource<QuoteResponse> response = quotesController.createQuote(quoteObjectCreator.getQuoteDetail(), 1,false);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * test create quote positive test case without passing QuoteleId and QuoteId
	 **/
	@Test
	public void testcreateQuote2() throws Exception {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getCustomerDetailsBean());
		ResponseResource<QuoteResponse> response = quotesController.createQuote(quoteObjectCreator.getQuoteDetail2(),
				1,false);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote positive test case
	 **/
	@Test
	public void testcreateQuoteWithValid() throws Exception {

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getCustomer());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLe());
		ResponseResource<QuoteResponse> response = quotesController.createQuote(quoteObjectCreator.getQuoteDetail(), 1,false);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote positive test case
	 **/
	@Test
	public void testcreateQuoteWithValidAndCustomerNull() throws Exception {

		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getCustomer());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLe());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(quoteObjectCreator.createProductSolutions());

		ResponseResource<QuoteResponse> response = quotesController.createQuote(quoteObjectCreator.getQuoteDetail(),
				null,false);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote positive test case
	 **/
	@Test
	public void testcreateQuoteWithDifferentOfferingName() throws Exception {

		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getCustomer());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLe());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(quoteObjectCreator.createProductSolutions());

		ResponseResource<QuoteResponse> response = quotesController.createQuote(quoteObjectCreator.getQuoteDetailNew(),
				1,false);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote negative test case
	 **/
	@Test
	public void testcreateQuoteExcpetion() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus(null, 1)).thenReturn((null));
		ResponseResource<QuoteResponse> response = quotesController.createQuote(null, null,false);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * test create quote positive test case
	 **/
	@Test
	// to do:need to change the mocking
	public void testupdateSiteInformation() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn(quoteObjectCreator.getUser());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
				Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getMstProductComponent()));
		Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(quoteObjectCreator.geQuotePrice());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		ResponseResource<QuoteBean> response = quotesController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0, null);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote Negative test case
	 **/
	@Test
	// to do:need to change the mocking
	public void testupdateSiteInformationWihtoutProduct() throws Exception {
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		ResponseResource<QuoteBean> response = quotesController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * test create quote Positive test case
	 **/
	@Test
	// to do:need to change the mocking
	public void testupdateSiteInformationWithoutQuoteDetail() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<QuoteBean> response = quotesController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0, null);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote Negative test case
	 **/
	@Test
	// to do:need to change the mocking
	public void testupdateSiteInformationWithoutQuoteDetailAndNulldetails() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<QuoteBean> response = quotesController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	@Test
	// to do:need to change the mocking
	public void testupdateSiteInformationForAttributeEmpty() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn(quoteObjectCreator.getUser());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
				Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getMstProductComponent()));
		Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))

				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMas()));

		ResponseResource<QuoteBean> response = quotesController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0, null);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote positive test case
	 **/
	@Test
	// to do:need to change the mocking
	public void testupdateSiteInformationWithSite() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn(quoteObjectCreator.getUser());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
				Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMaster().get(0)));
		Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
		Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getMstProductComponent()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(Mockito.any()))
				.thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());
		Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuotePrice());

		ResponseResource<QuoteBean> response = quotesController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetail(), 0, null);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote positive test case
	 **/
	@Test
	// to do:need to change the mocking
	public void testupdateSiteInformationWithEmptySite() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn(quoteObjectCreator.getUser());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
				Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getProductAtrributeMaster().get(0)));

		Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
		Mockito.when(mstProductComponentRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getMstProductComponent()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent(Mockito.any()))
				.thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());
		Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuotePrice());
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteIllSite());

		ResponseResource<QuoteBean> response = quotesController.updateSiteInformation(0, null,
				quoteObjectCreator.getQuoteDetailWithoutSiteID(), 0, null);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote negative test case
	 **/
	@Test
	public void testupdateSiteInformationException() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus(null, 1)).thenReturn((null));
		ResponseResource<QuoteBean> response = quotesController.updateSiteInformation(null, null, null, null, null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * 
	 * testgetQuoteConfigurationException- get Quote Configuration-negative
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetQuoteConfigurationException() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus(null, 1)).thenReturn((null));
		ResponseResource<QuoteBean> response = quotesController.getQuoteConfiguration(null, "ALL",false);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * 
	 * testgetQuoteConfiguration-get quote configuration postive
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetQuoteConfiguration() throws Exception {
		Mockito.when(quoteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getQuote2()));

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(Mockito.any(),
				Mockito.any(Byte.class))).thenReturn(quoteObjectCreator.getListOfQouteIllSites());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamilyList());

		Mockito.when(
				productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(quoteObjectCreator.getSolutionList());

		ResponseResource<QuoteBean> response = quotesController.getQuoteConfiguration(1, "ALL",false);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	// /*
	// * test case for all user details by customerID - positive case
	// **/
	// @Test
	// public void testgetAllUsersByCustomerId() throws TclCommonException {
	// ResponseResource<List<UserDto>> response =
	// quotesController.getAllUserDetailsByCustomer();
	// assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	// }
	//
	// /*
	// * * test case for all user details by customerID - negative case
	// */
	// @Test
	// public void testgetAllUsersByCustomerIdNull() throws TclCommonException {
	// Mockito.when(userRepository.findByUsernameAndStatus("admin",
	// 1)).thenReturn(null);
	// ResponseResource<List<UserDto>> response =
	// quotesController.getAllUserDetailsByCustomer();
	// assertTrue(response.getStatus() == Status.FAILURE);
	// }

	/**
	 * for testing getTaxExcemptedSiteInfo testgetSiteInfooException
	 * 
	 * @throws Exception
	 */
	@Test
	public void getTaxExemptedSiteInfo() throws Exception {
		Mockito.when(illSiteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getIllsites()));
		ResponseResource<QuoteResponse> response = quotesController.getTaxExemptedSiteInfo(0, null, null);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing taxExempted testgetSiteInfooException
	 * 
	 * @throws Exception
	 */
	@Test
	public void getTaxExemptedSiteInfoException() throws Exception {

		Mockito.when(quoteRepository.findById(null)).thenReturn((null));
		ResponseResource<QuoteResponse> response = quotesController.getTaxExemptedSiteInfo(null, null, null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * for testing siteIfo testgetSiteInfooException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetSiteInfo() throws Exception {
		Mockito.when(illSiteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getIllsites()));
		ResponseResource<QuoteResponse> response = quotesController.getSiteInfo(1, 1, 1);
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing siteIfo testgetSiteInfooException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetSiteInfoException() throws Exception {

		Mockito.when(illSiteRepository.findById(null)).thenReturn(Optional.ofNullable(null));

		Mockito.when(illSiteRepository.findById(null)).thenReturn((null));
		ResponseResource<QuoteResponse> response = quotesController.getSiteInfo(null, null, null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * for testing delete site testdeleteIllsites
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeleteIllsites() throws Exception {
		when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		doNothing().when(quoteProductComponentsAttributeValueRepository).delete(Mockito.any());
		doNothing().when(quoteProductComponentRepository).delete(Mockito.any());
		when(quoteIllSiteSlaRepository.findByQuoteIllSite(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteIllSiteSlaList());
		doNothing().when(quoteIllSiteSlaRepository).delete(Mockito.any());
		when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilityList());
		doNothing().when(siteFeasibilityRepository).delete(Mockito.any());
		doNothing().when(illSiteRepository).delete(Mockito.any());
		ResponseResource<QuoteDetail> response = quotesController.deactivateSites(1, 1, 1, 1,
				QuoteConstants.DELETE.toString());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing delete site testdeleteIllsitesException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeleteIllsitesException() throws Exception {
		Mockito.when(illSiteRepository.findById(null)).thenReturn(null);
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(null,null)).thenReturn(null);
		ResponseResource<QuoteDetail> response = quotesController.deactivateSites(null, null, null, null, null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/*
	 * * test case to get the trigger email once the user is selected - positive
	 * case
	 */

	@Test
	public void testTriggerEmail() throws TclCommonException {
		Mockito.when(userRepository.findByEmailIdAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteDelegation()));
		TriggerEmailRequest triggerRequest = new TriggerEmailRequest();
		triggerRequest.setEmailId("test@gmail.com");
		triggerRequest.setQuoteToLeId(1);
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<TriggerEmailResponse> response = quotesController.triggerEmail(null, null, triggerRequest,
				httpServletRequest);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * * test case to get the trigger email once the user is selected - negative
	 * case
	 */

	@Test
	public void testTriggerEmail2() throws TclCommonException {
		Mockito.when(userRepository.findByEmailIdAndStatus(null, 1)).thenReturn(null);

		TriggerEmailRequest triggerRequest = new TriggerEmailRequest();
		triggerRequest.setEmailId(null);
		triggerRequest.setQuoteToLeId(0);
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<TriggerEmailResponse> response = quotesController.triggerEmail(null, null, triggerRequest,
				httpServletRequest);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testTriggerEmailForEmpty() throws TclCommonException {
		Mockito.when(userRepository.findByEmailIdAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Optional.empty());
		TriggerEmailRequest triggerRequest = new TriggerEmailRequest();
		triggerRequest.setEmailId("test@gmail.com");

		triggerRequest.setQuoteToLeId(1);
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<TriggerEmailResponse> response = quotesController.triggerEmail(null, null, triggerRequest,
				httpServletRequest);
		assertTrue(response.getData() != null);
	}

	/*
	 * * test case to get the quote Id if the delegated user is in Open status -
	 * positive case
	 */

	@Test
	public void testgetDelegatedUsers() throws TclCommonException {

		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.craeteUser());

		Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteDelegation()));

		ResponseResource<QuoteDelegationDto> response = quotesController.getDelegatedUser();
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * * test case to get the quote Id if the delegated user is in Open status -
	 * positive case
	 */

	@Test
	public void testgetDelegatedUsersWithException() throws TclCommonException {

		Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Optional.empty());

		ResponseResource<QuoteDelegationDto> response = quotesController.getDelegatedUser();
		assertTrue(response.getData() == null);
	}

	/**
	 * for testing disable site - positive test case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdisableIllsites() throws Exception {

		ResponseResource<QuoteDetail> response = quotesController.deactivateSites(1, 2, 5, 1,
				QuoteConstants.DISABLE.toString());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing disable site - negative test case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdisableIllsitesNegativeCase() throws Exception {
		Mockito.when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<QuoteDetail> response = quotesController.deactivateSites(0, 0, 0, null, "");
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * for testingtestQuotesDetailsBasedOnCustomerNegativeCase- positive test case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQuotesDetailsBasedOnCustomer() throws Exception {
		Mockito.when(customerRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getCustomer()));

		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(), 
				Mockito.any())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());

		ResponseResource<List<QuoteBean>> response = quotesController.getQuotesDetailsBasedOnCustomer(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing testQuotesDetailsBasedOnCustomerNegativeCase - negative test case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testQuotesDetailsBasedOnCustomerNegativeCase() throws Exception {
		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<List<QuoteBean>> response = quotesController.getQuotesDetailsBasedOnCustomer(1);
		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		assertTrue(response.getStatus() == Status.FAILURE && response.getData() == null);
	}

	/**
	 * for getQuotesDetailsBasedOnCustomerLegalEntity- positive test case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetQuotesDetailsBasedOnCustomerLegalEntity() throws Exception {

		Mockito.when(quoteToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());

		ResponseResource<List<QuoteBean>> response = quotesController.getQuotesDetailsBasedOnCustomerLegalEntity(1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing getQuotesDetailsBasedOnCustomerLegalEntity - negative test case
	 * 
	 * @throws Exception
	 */
	@Test
	public void getQuotesDetailsBasedOnCustomerLegalEntity() throws Exception {
		Mockito.when(quoteToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt())).thenReturn(null);

		ResponseResource<List<QuoteBean>> response = quotesController.getQuotesDetailsBasedOnCustomerLegalEntity(1);
		assertTrue(response.getData() == null || response.getStatus() == Status.FAILURE);
	}

	/**
	 * for testing testeditSites site - positive test case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testeditSites() throws Exception {

		ResponseResource<QuoteDetail> response = quotesController.editSites(null, null, null,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testeditSitesUpdateCpe() throws Exception {
		Mockito.when(illSiteRepository.findById(Mockito.any()))
				.thenReturn((Optional.of(quoteObjectCreator.getQuoteIllSite())));
		ResponseResource<QuoteDetail> response = quotesController.editSites(null, null, null,
				quoteObjectCreator.getUpdateRequest1());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testeditSitesUpdateCpeChange() throws Exception {
		Mockito.when(illSiteRepository.findById(Mockito.any()))
				.thenReturn((Optional.of(quoteObjectCreator.getQuoteIllSite())));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
				Mockito.anyInt(), Mockito.matches("CPE")))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		ResponseResource<QuoteDetail> response = quotesController.editSites(null, null, null,
				quoteObjectCreator.getUpdateRequest3());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing testeditSites site - negative test case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testeditSitesNegativeCase() throws Exception {

		ResponseResource<QuoteDetail> response = quotesController.editSites(null, null, null, null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * for testing dashboard api - negative case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDashboardNegativeCase() throws Exception {

		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn((quoteObjectCreator.getRootUser()));

		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn((quoteObjectCreator.getRootUser()));
		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());

		ResponseResource<DashBoardBean> response = quotesController.getDashboardDetails(null);

		assertTrue(response.getData() != null);
	}

	/**
	 * for testing dashboard api
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDashboard() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1))
				.thenReturn((quoteObjectCreator.getRootUserWithQuote()));

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());
		ResponseResource<DashBoardBean> response = quotesController.getDashboardDetails(1);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing dashboard api
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDashboardWithLegalentity() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1))
				.thenReturn((quoteObjectCreator.getRootUserWithQuote()));

		Authentication authentication = mock(Authentication.class);

		SecurityContext securityContext = mock(SecurityContext.class);
		Mockito.when(quoteToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.any()))
				.thenReturn((quoteObjectCreator.getQuoteToLeList()));

		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());
		ResponseResource<DashBoardBean> response = quotesController.getDashboardDetails(null);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * positive test case passing update request testApprovedQuotesPositiveCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotes() throws TclCommonException {

		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote2());
		Mockito.when(orderRepository.save(quoteObjectCreator.getOrder())).thenReturn(quoteObjectCreator.getOrder());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(orderProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(new OrderProductComponentsAttributeValue());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(quoteObjectCreator.geQuotePrice());
		Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(new OrderPrice());
		Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(new OrderSiteFeasibility());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<QuoteDetail> response = quotesController.approvedQuotes(quoteObjectCreator.getUpdateRequest(),
				httpServletRequest);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * positive test case passing update request testApprovedQuotesPositiveCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesWithEmptyOrder() throws TclCommonException {

		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(orderRepository.save(quoteObjectCreator.getOrder())).thenReturn(quoteObjectCreator.getOrder());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(new OrderToLe());
		Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(new OrderSiteFeasibility());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<QuoteDetail> response = quotesController.approvedQuotes(quoteObjectCreator.getUpdateRequest(),
				httpServletRequest);
		assertTrue(response.getData() != null);
	}

	/**
	 * positive test case passing update request testApprovedQuotesPositiveCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesWithEmptyOrder4() throws TclCommonException {
		Mockito.when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getOrderConfirmationAudit());

		Mockito.when(
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingTypeNotIn(Mockito.anyString(),null))
				.thenReturn(quoteObjectCreator.getPricingDetails());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		Mockito.when(
				productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(quoteObjectCreator.getSolutionList());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.any()))
				.thenReturn(quoteObjectCreator.getCofDetails());
		Mockito.when(quoteDelegationRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteDelegationList());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamilyList());
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSitesNotFeasible());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(orderRepository.save(quoteObjectCreator.getOrder())).thenReturn(quoteObjectCreator.getOrder());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(new OrderToLe());
		Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(new OrderSiteFeasibility());
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(1, (byte) 1))
				.thenReturn(quoteObjectCreator.getListOfQouteIllSitesNotFeasible());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<QuoteDetail> response = quotesController.approvedQuotes(quoteObjectCreator.getUpdateRequest(),
				httpServletRequest);
		assertTrue(response.getData() != null);
	}

	/**
	 * positive test case passing update request testApprovedQuotesPositiveCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesWithEmptyOrder1() throws TclCommonException {

		when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote2());
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote()));
		Mockito.when(orderRepository.save(quoteObjectCreator.getOrder())).thenReturn(quoteObjectCreator.getOrder());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamilyList());
		Mockito.when(orderToLeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getOrderToLes());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(ordersLeAttributeValueRepository.save(Mockito.any())).thenReturn(new OrdersLeAttributeValue());
		Mockito.when(orderToLeProductFamilyRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getorderToLeProductFamilies());
		Mockito.when(orderProductSolutionRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getOrderProductSolution());
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites2());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any())).thenReturn(null);
		Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(null);
		Mockito.when(orderIllSitesRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getOrderIllSite());
		Mockito.when(
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(orderProductComponentRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getOrderProductComponent());
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(),
				Mockito.anyString())).thenReturn(quoteObjectCreator.getQuotePrice());
		Mockito.when(orderPriceRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getOrderPrice());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(null);
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getAddressDetail());
		Mockito.when(order.getId()).thenReturn(1);
		Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(new OrderSiteFeasibility());

		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<QuoteDetail> response = quotesController.approvedQuotes(quoteObjectCreator.getUpdateRequest(),
				httpServletRequest);
		assertTrue(response.getData() == null || response.getStatus() == Status.SUCCESS);
	}

	/**
	 * negative test case passing update request as null
	 * testApprovedQuotesNegativeCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesNegativeCase() throws TclCommonException {
		Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(new OrderSiteFeasibility());
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<QuoteDetail> response = quotesController.approvedQuotes(null, httpServletRequest);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * negative test case passing quoteId as null testApprovedQuotesNegativeCase
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testApprovedQuotesNegativeCaseQuoteIdAsNull() throws TclCommonException {
		Mockito.when(orderSiteFeasibilityRepository.save(Mockito.any())).thenReturn(new OrderSiteFeasibility());

		Optional<Quote> optionalQuote = null;
		Mockito.when(quoteRepository.findById(Mockito.anyInt())).thenReturn(optionalQuote);
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<QuoteDetail> response = quotesController
				.approvedQuotes(quoteObjectCreator.getUpdateRequestQuoteIdNull(), httpServletRequest);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	/**
	 * negative test case passing customerId as null
	 * testGetQuotesDetailsBasedOnCustomer
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testGetQuotesDetailsBasedOnCustomeAsCustomerIdAsNull() throws TclCommonException {

		Mockito.when(customerRepository.findById(Mockito.anyInt())).thenReturn((Optional.empty()));

		ResponseResource<List<QuoteBean>> response = quotesController.getQuotesDetailsBasedOnCustomer(null);

		assertTrue(response.getResponseCode() == 500 || response.getData().isEmpty() || response == null);
	}

	/**
	 * positive test case passing customerId which returns Quote details
	 * testGetQuotesDetailsBasedOnCustomer
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testGetQuotesDetailsBasedOnCustomer() throws TclCommonException {

		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamilyList());
		Mockito.when(
				productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(quoteObjectCreator.getSolutionList());
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getIllsitesMock());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSiteAndIsSelected(Mockito.any(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilities());
		ResponseResource<List<QuoteBean>> response = quotesController.getQuotesDetailsBasedOnCustomer(1);

		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * positive test case passing customerLegalEntityId which returns Quote details
	 * 
	 * testGetQuotesDetailsBasedOnCustomerLegalEntity
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testGetQuotesDetailsBasedOnCustomerLegalEntity() throws TclCommonException {

		Mockito.when(quoteToLeRepository.findByErfCusCustomerLegalEntityId(1))
				.thenReturn((quoteObjectCreator.getQuoteToLeList()));

		ResponseResource<List<QuoteBean>> response = quotesController.getQuotesDetailsBasedOnCustomerLegalEntity(1);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * negative test case passing customerLegalEntityId as null
	 * 
	 * testGetQuotesDetailsBasedOnCustomerLegalEntityAsIdNull
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testGetQuotesDetailsBasedOnCustomerLegalEntityAsIdNull() throws TclCommonException {

		Mockito.when(quoteToLeRepository.findByErfCusCustomerLegalEntityId(Mockito.anyInt()))
				.thenReturn((quoteObjectCreator.getQuoteToLeList()));

		ResponseResource<List<QuoteBean>> response = quotesController.getQuotesDetailsBasedOnCustomerLegalEntity(null);

		assertTrue(response.getResponseCode() == 500 || response.getData().isEmpty() || response.getData() == null);
	}

	/**
	 * possitive case for testupdateSiteProperties
	 * 
	 * testupdateSiteProperties
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testupdateSiteProperties() throws TclCommonException {
		Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn((quoteObjectCreator.getMstProductComponentList()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getAttribute());
		ResponseResource<QuoteDetail> response = quotesController.updateSiteProperties(null, null, null,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * negative case for testupdateSiteProperties
	 * 
	 * testupdateSiteProperties
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testupdateSitePropertiesForException() throws TclCommonException {
		Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn((quoteObjectCreator.getMstProductComponentList()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getAttribute());
		ResponseResource<QuoteDetail> response = quotesController.updateSiteProperties(null, null, null, null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * negative case for testupdateSiteProperties
	 * 
	 * testupdateSiteProperties
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testupdateSitePropertiesForInvaliedRequest() throws TclCommonException {
		Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn((quoteObjectCreator.getMstProductComponentList()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getAttribute());

		UpdateRequest request = quoteObjectCreator.getUpdateRequest();
		request.setSiteId(null);
		ResponseResource<QuoteDetail> response = quotesController.updateSiteProperties(null, null, null, request);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * possitive case for testupdateSiteProperties
	 * 
	 * testupdateSiteProperties
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testupdateSitePropertiesForProductAttribute() throws TclCommonException {
		Mockito.when(quoteProductComponentRepository.save(quoteObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMas());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn((quoteObjectCreator.getMstProductComponentList()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(new QuoteProductComponentsAttributeValue());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		ResponseResource<QuoteDetail> response = quotesController.updateSiteProperties(null, null, null,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * possitive case for testupdateLegalEntityProperties
	 * 
	 * testupdateLegalEntityProperties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateLegalEntityProperties() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteToLe()));
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(quoteToLeRepository.save(new QuoteToLe()));
		ResponseResource<QuoteDetail> response = quotesController.updateLegalEntityProperties(1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null);

		QuoteDetail details = illQuoteService.updateLegalEntityProperties(quoteObjectCreator.getUpdateRequest());
		assertTrue(details != null);
	}

	/**
	 * possitive case for testupdateLegalEntityProperties
	 * 
	 * testupdateLegalEntityProperties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateLegalEntityPropertiesWithMstNull() throws TclCommonException {

		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(new ArrayList<>());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
		ResponseResource<QuoteDetail> response = quotesController.updateLegalEntityProperties(1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * negative case for testupdateLegalEntityProperties
	 * 
	 * testupdateLegalEntityProperties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateLegalEntityPropertiesForException() throws TclCommonException {

		/*
		 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
		 * anyString(), Mockito.anyByte()))
		 * .thenReturn(quoteObjectCreator.getMstAttributeList());
		 */

		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());

		ResponseResource<QuoteDetail> response = quotesController.updateLegalEntityProperties(null, null, null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * negative case for testupdateLegalEntityProperties
	 * 
	 * testupdateLegalEntityProperties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateLegalEntityPropertiesForQuote() throws TclCommonException {

		/*
		 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
		 * anyString(), Mockito.anyByte()))
		 * .thenReturn(quoteObjectCreator.getMstAttributeList());
		 */

		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

		UpdateRequest request = quoteObjectCreator.getUpdateRequest();
		ResponseResource<QuoteDetail> response = quotesController.updateLegalEntityProperties(null, null, request);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * this test case tests the main application
	 * 
	 */
	@Test
	public void TestApplicationContext() {
		String[] arr = { "", "" };
		OptimusOmsApplication.main(arr);
	}

	/**
	 * this is for coverage all beans test.
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void coverBeanTest() {
		AttributeDetail attributeDetail = new AttributeDetail();
		attributeDetail.setAttributeId(1);
		attributeDetail.setAttributeMasterId(1);
		attributeDetail.setName("beans");
		attributeDetail.setValue("beans");
		assertTrue(attributeDetail.getAttributeId() != null && attributeDetail.getAttributeMasterId() != null
				&& attributeDetail.getName() != null && attributeDetail.getValue() != null);

		UserRequest userRequest = new UserRequest();
		List<Integer> integerList = new ArrayList<>();
		integerList.add(1);
		userRequest.setUserIds(integerList);
		assertTrue(userRequest != null && userRequest.getUserIds() != null && !userRequest.getUserIds().isEmpty());

		ProductAttributeMaster master = new ProductAttributeMaster();
		master.setCreatedBy("shekhar");
		master.setCreatedTime(new Date());
		master.setDescription("someDescripetion");
		ProductAttributeMasterBean productAttributeMasterBean = new ProductAttributeMasterBean(master);
		productAttributeMasterBean.setDescription("some discription");
		productAttributeMasterBean.setId(1);
		productAttributeMasterBean.setName("shekhar");
		productAttributeMasterBean.setStatus((byte) 1);
		assertTrue(productAttributeMasterBean.getDescription() != null && productAttributeMasterBean.getId() != null
				&& productAttributeMasterBean.getName() != null && productAttributeMasterBean.getStatus() != null);

		DashBoardSiteBean dashBoardSiteBean = new DashBoardSiteBean();
		dashBoardSiteBean.setStage("stage");
		dashBoardSiteBean.setStatus((byte) 1);
		assertTrue(dashBoardSiteBean.getStatus() == 1 && dashBoardSiteBean.getStage() != null);

	}

	/*
	 * positive test case
	 */
	@Test
	public void testgetSiteProperties() throws TclCommonException {
		ResponseResource<List<QuoteProductComponentBean>> response = quotesController.getSiteProperties(0, 1, 1);
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * negative test case : when siteId is null
	 */
	@Test
	public void testgetSitePropertiesForException() throws TclCommonException {
		ResponseResource<List<QuoteProductComponentBean>> response = quotesController.getSiteProperties(null, null,
				null);
		assertTrue(response.getResponseCode() == 500 || response.getData().isEmpty() || response == null);
	}

	/*
	 * negative test case : when IllSites are not present
	 */
	@Test
	public void testgetSitePropertiesExceptionForEmptySiteProperties() throws TclCommonException {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<List<QuoteProductComponentBean>> response = quotesController.getSiteProperties(0, null, null);
		assertTrue(response.getResponseCode() == 500 || response.getData().isEmpty() || response == null);
	}

	/*
	 * Get all attribute details by quote to le id : Positive case
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void getAttributeDetailsByQuoteToLeId() throws TclCommonException {
		ResponseResource<Set<LegalAttributeBean>> response = quotesController.getAllAttributesByQuoteToLeId(1, 1);
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * Get all attribute details by quote to le id : Negative case : Quote to le id
	 * null
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void getAttributeDetailsByQuoteToLeIdWithoutID() throws TclCommonException {
		ResponseResource<Set<LegalAttributeBean>> response = quotesController.getAllAttributesByQuoteToLeId(null, null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * Get all attribute details by quote to le id : Negative case --> Incorrect
	 * Information
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void getAttributeDetailsByQuoteToLeIdWithoutData() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<Set<LegalAttributeBean>> response = quotesController.getAllAttributesByQuoteToLeId((Integer) 1,
				1);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * for testing delete site testdeleteIllsites - negative case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeleteIllsitesNegative() throws Exception {

		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<QuoteDetail> response = quotesController.deactivateSites(0, 0, 0, null,
				QuoteConstants.DELETE.toString());
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * for testing delete site testdeleteIllsites Positive with no effect
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeleteIllsitesNoEffer() throws Exception {

		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(0,null))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(illSiteRepository.findById(0)).thenReturn(Optional.ofNullable(quoteObjectCreator.getIllsites()));
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn(quoteObjectCreator.getUser());
		ResponseResource<QuoteDetail> response = quotesController.deactivateSites(0, 0, 0, null,
				QuoteConstants.ATTRIBUTES.toString());
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * poditive test case for updateQuoteToLeStatus Input param : quoteToLeId,status
	 * response : QuoteDetail
	 */
	@Test
	public void testUpdateQuoteToLeStatus() throws TclCommonException {

		when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
		when(quoteToLeRepository.save(new QuoteToLe()));

		QuoteDetail response = illQuoteService.updateQuoteToLeStatus(1, "test");
		assertTrue(response != null);

	}

	/*
	 * negative test case for updateQuoteToLeStatus Input param : quoteToLeId,status
	 * response : Exception
	 */
	@Test
	public void testUpdateQuoteToLeStatusforNullquoteToLeIdstatus() throws TclCommonException {

		ResponseResource<QuoteDetail> response = quotesController.updateQuoteToLeStatus(null, null, null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * negative test case for updateQuoteToLeStatus Input param : quoteToLeId,status
	 * response : Exception
	 */
	@Test
	public void testUpdateQuoteToLeStatusforEmptyQuoteToLe() throws TclCommonException {
		when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<QuoteDetail> response = quotesController.updateQuoteToLeStatus(1, 1, "Test");
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testUpdateQuoteToLeStatusforQuoteToLe() throws TclCommonException {
		when(quoteToLeRepository.findById(1)).thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));
		ResponseResource<QuoteDetail> response = quotesController.updateQuoteToLeStatus(1, 1, "SELECT_CONFIGURATION");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * positive test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 */
	@Test
	public void testPersistListOfQuoteLeAttributes() throws TclCommonException {
		when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
		when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(new QuoteLeAttributeValue());
		ResponseResource<QuoteDetail> response = quotesController.persistListOfQuoteLeAttributes(1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * positive test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 */
	@Test
	public void testPersistListOfQuoteLeAttributesforNullQuoteLeAttributeValue() throws TclCommonException {
		when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
		when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(), Mockito.anyString()))
				.thenReturn(null);
		when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(new QuoteLeAttributeValue());
		ResponseResource<QuoteDetail> response = quotesController.persistListOfQuoteLeAttributes(1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * positive test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 */
	@Test
	public void testPersistListOfQuoteLeAttributesforNullMstAttr() throws TclCommonException {
		when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
		when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeListWithNullAttr());
		when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(new MstOmsAttribute());
		when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		ResponseResource<QuoteDetail> response = quotesController.persistListOfQuoteLeAttributes(1, 1,
				quoteObjectCreator.getUpdateRequest());
	}

	/*
	 * negative test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 */
	@Test
	public void testPersistListOfQuoteLeAttributesForNullQuoteToLe() throws TclCommonException {
		when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<QuoteDetail> response = quotesController.persistListOfQuoteLeAttributes(1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * negative test for persistListOfQuoteLeAttributes input param : UpdateRequest
	 * response : QuoteDetail
	 *
	 */
	@Test
	public void testPersistListOfQuoteLeAttributesForNullUser() throws TclCommonException {
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn((null));
		ResponseResource<QuoteDetail> response = quotesController.persistListOfQuoteLeAttributes(1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * for Positive
	 * 
	 * @throws Exception
	 */
	@Test
	public void testtriggerForFeasibilityBean() throws Exception {

		List<QuoteProductComponentsAttributeValue> list = new ArrayList<>();

		QuoteProductComponentsAttributeValue obj1 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj1.getProductAttributeMaster().setId(1);
		list.add(obj1);

		QuoteProductComponentsAttributeValue obj2 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj2.getProductAttributeMaster().setId(2);
		list.add(obj2);

		QuoteProductComponentsAttributeValue obj3 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj3.getProductAttributeMaster().setId(3);
		obj3.setAttributeValues("1=2");
		list.add(obj3);

		QuoteProductComponentsAttributeValue obj4 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj3.setAttributeValues("1=2");
		obj3.getProductAttributeMaster().setId(4);
		list.add(obj4);

		QuoteProductComponentsAttributeValue obj5 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj5.getProductAttributeMaster().setId(5);
		obj5.setAttributeValues("Fully Managed");
		list.add(obj5);

		QuoteProductComponentsAttributeValue obj14 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj14.getProductAttributeMaster().setId(14);
		obj14.setAttributeValues("Physically Managed");
		list.add(obj14);

		QuoteProductComponentsAttributeValue obj15 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj15.getProductAttributeMaster().setId(15);
		obj15.setAttributeValues("proactive_services");
		list.add(obj15);

		QuoteProductComponentsAttributeValue obj16 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj15.getProductAttributeMaster().setId(16);
		obj15.setAttributeValues("Configuration Management");
		list.add(obj16);

		QuoteProductComponentsAttributeValue obj13 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj15.getProductAttributeMaster().setId(13);
		obj15.setAttributeValues("Unmanaged");
		list.add(obj13);

		QuoteProductComponentsAttributeValue obj6 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj6.getProductAttributeMaster().setId(6);
		list.add(obj6);

		QuoteProductComponentsAttributeValue obj7 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj7.getProductAttributeMaster().setId(7);
		list.add(obj7);

		QuoteProductComponentsAttributeValue obj8 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj8.getProductAttributeMaster().setId(8);
		list.add(obj8);

		QuoteProductComponentsAttributeValue obj9 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj9.getProductAttributeMaster().setId(9);
		list.add(obj9);

		QuoteProductComponentsAttributeValue obj10 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj10.getProductAttributeMaster().setId(10);
		list.add(obj10);

		QuoteProductComponentsAttributeValue obj11 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj11.getProductAttributeMaster().setId(11);
		list.add(obj11);

		QuoteProductComponentsAttributeValue obj12 = quoteObjectCreator.createQuoteProductComponentsAttributeValue();
		obj12.getProductAttributeMaster().setId(12);
		list.add(obj12);

		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.anyInt())).thenReturn(list);

		ProductAttributeMaster pm2 = quoteObjectCreator.getProductAtrributeMas();
		pm2.setName("Port Bandwidth");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(2))).thenReturn(Optional.of(pm2));

		ProductAttributeMaster pm3 = quoteObjectCreator.getProductAtrributeMas();
		pm3.setName("IPv6 Address Pool Size");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(3))).thenReturn(Optional.of(pm3));

		ProductAttributeMaster pm4 = quoteObjectCreator.getProductAtrributeMas();
		pm4.setName("IPv4 Address Pool Size");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(4))).thenReturn(Optional.of(pm4));

		ProductAttributeMaster pm5 = quoteObjectCreator.getProductAtrributeMas();
		pm5.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(5))).thenReturn(Optional.of(pm5));

		ProductAttributeMaster pm13 = quoteObjectCreator.getProductAtrributeMas();
		pm13.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(13))).thenReturn(Optional.of(pm13));

		ProductAttributeMaster pm14 = quoteObjectCreator.getProductAtrributeMas();
		pm14.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(14))).thenReturn(Optional.of(pm14));

		ProductAttributeMaster pm15 = quoteObjectCreator.getProductAtrributeMas();
		pm15.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(15))).thenReturn(Optional.of(pm15));

		ProductAttributeMaster pm16 = quoteObjectCreator.getProductAtrributeMas();
		pm16.setName("CPE Management Type");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(16))).thenReturn(Optional.of(pm16));

		ProductAttributeMaster pm6 = quoteObjectCreator.getProductAtrributeMas();
		pm6.setName("Interface");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(6))).thenReturn(Optional.of(pm6));

		ProductAttributeMaster pm7 = quoteObjectCreator.getProductAtrributeMas();
		pm7.setName("CPE");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(7))).thenReturn(Optional.of(pm7));

		ProductAttributeMaster pm8 = quoteObjectCreator.getProductAtrributeMas();
		pm8.setName("CPE Basic Chassis");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(8))).thenReturn(Optional.of(pm8));

		ProductAttributeMaster pm9 = quoteObjectCreator.getProductAtrributeMas();
		pm9.setName("Service Variant");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(9))).thenReturn(Optional.of(pm9));

		ProductAttributeMaster pm10 = quoteObjectCreator.getProductAtrributeMas();
		pm10.setName("Local Loop Bandwidth");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(10))).thenReturn(Optional.of(pm10));

		ProductAttributeMaster pm11 = quoteObjectCreator.getProductAtrributeMas();
		pm11.setName("CPE Basic Chassis");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(11))).thenReturn(Optional.of(pm11));

		ProductAttributeMaster pm12 = quoteObjectCreator.getProductAtrributeMas();
		pm12.setName("CPE Basic Chassis");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(12))).thenReturn(Optional.of(pm12));

		ProductAttributeMaster pm1 = quoteObjectCreator.getProductAtrributeMas();
		pm1.setName("Burstable Bandwidth");
		Mockito.when(productAttributeMasterRepository.findById(Mockito.eq(1))).thenReturn(Optional.of(pm1));

		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(new QuoteToLe());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getCustomerDetailsBean());
		ResponseResource<String> response = quotesController
				.triggerForFeasibilityBean(quoteObjectCreator.getFeasibilityBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * Positive test case for getContactAttributeDetails
	 */
	@Test
	public void testGetContactAttributeDetails() throws TclCommonException {

		List<String> nameValue = Arrays.asList("CONTACTID", "CONTACTNAME", "CONTACTEMAIL", "DESIGNATION", "CONTACTNO");
		nameValue.forEach(value -> {
			Optional<QuoteToLe> quToLe = quoteObjectCreator.returnQuoteToLeForUpdateStatusForContactAttributeInfo();
			Set<QuoteLeAttributeValue> set = new HashSet<>();
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setName(value);
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			set.add(quoteLeAttributeValue);
			quToLe.get().setQuoteLeAttributeValues(set);
			Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(quToLe);
			try {
				ResponseResource<ContactAttributeInfo> response = quotesController.getContactAttributeDetails(1, 1);
				assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
		});
		ResponseResource<ContactAttributeInfo> response = quotesController.getContactAttributeDetails(1, 1);
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);

	}

	/*
	 * Negative test case for getContactAttributeDetails
	 */
	@Test
	public void testGetContactAttributeDetailsForExceotion() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<ContactAttributeInfo> response = quotesController.getContactAttributeDetails(1, 1);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);

	}

	/*
	 * Positive test case for updateSdfcStage
	 */
	@Test
	public void testUpdateSdfcStage() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
		ResponseResource<String> response = quotesController.updateSdfcStage(1, 1, "Proposal Sent");
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);

	}

	/*
	 * Positive test case for updateBillingInfoForSfdc
	 */
	@Test
	public void testUpdateBillingInfoForSfdc() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
				Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(new QuoteLeAttributeValue());
		ResponseResource<QuoteBean> response = quotesController
				.updateBillingInfoForSfdc(quoteObjectCreator.getBillingRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * Positive test case for updateBillingInfoForSfdc
	 */
	@Test
	public void testUpdateBillingInfoForSfdcForNullQuoteLeAttributeValue() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.returnQuoteToLeForUpdateStatus());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
				Mockito.anyString())).thenReturn(null);
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeListWithNullAttr());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(new MstOmsAttribute());
		ResponseResource<QuoteBean> response = quotesController
				.updateBillingInfoForSfdc(quoteObjectCreator.getBillingRequest());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * negative test case for updateBillingInfoForSfdc
	 */
	@Test
	public void testUpdateBillingInfoForSfdcForException() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(null);
		ResponseResource<QuoteBean> response = quotesController
				.updateBillingInfoForSfdc(quoteObjectCreator.getBillingRequest());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * 
	 * test case for triggering pricing
	 */
	@Test
	public void testpricing() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteToLe1()));

		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelected(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilities());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());

		Mockito.when(siteFeasibilityRepository.save(Mockito.any())).thenReturn(new SiteFeasibility());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(new QuoteToLe());
		Mockito.when(pricingDetailsRepository.save(Mockito.any())).thenReturn(new PricingEngineResponse());
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingType(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getPricingDetails());

		ResponseResource<String> response = quotesController.triggerPricing(1, 1);
		assertTrue(response.getData() != null && response.getStatus() != Status.FAILURE);
	}

	/*
	 * Negitive test case for triggering pricing
	 */
	/* test case is not required as of now */
	@Test
	public void testpricingWithNull() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteToLe1()));

		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite_IdAndIsSelected(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilities());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());

		Mockito.when(siteFeasibilityRepository.save(Mockito.any())).thenReturn(new SiteFeasibility());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(new QuoteToLe());
		Mockito.when(pricingDetailsRepository.save(Mockito.any())).thenReturn(new PricingEngineResponse());
		Mockito.when(pricingDetailsRepository.findBySiteCodeAndPricingType(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getPricingDetails());

		ResponseResource<String> response = quotesController.triggerPricing(null, null);
		assertTrue(response == null);
	}

	/*
	 * Positive test case for feasibilityCheck
	 */
	@Test
	public void testFeasibilityCheck() throws TclCommonException {
		List<String> nameValue = Arrays.asList("is_feasiblity_check_done", "is_pricing_check_done");
		nameValue.forEach(value -> {
			Optional<QuoteToLe> quToLe = quoteObjectCreator.returnQuoteToLeForUpdateStatusForContactAttributeInfo();
			Set<QuoteLeAttributeValue> set = new HashSet<>();
			QuoteLeAttributeValue quoteLeAttributeValue = new QuoteLeAttributeValue();
			MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
			mstOmsAttribute.setName(value);
			quoteLeAttributeValue.setMstOmsAttribute(mstOmsAttribute);
			set.add(quoteLeAttributeValue);
			quToLe.get().setQuoteLeAttributeValues(set);
			Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(quToLe);
			try {
				ResponseResource<QuoteLeAttributeBean> response = quotesController.feasibilityCheck(1);
				assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS);
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
		});
	}

	/*
	 * Positive test case for updateSitePropertiesAttributes
	 */
	@Test
	public void testUpdateSitePropertiesAttributes() throws Throwable {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());

		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstProductComponentListForNull());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
				Mockito.any())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(new QuoteProductComponentsAttributeValue()));
		QuoteDetail response = illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null);

	}

	@Test
	public void testUpdateSitePropertiesAttributes1() throws Throwable {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstProductComponentListForNull());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
				Mockito.any())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(new QuoteProductComponentsAttributeValue()))
				.thenReturn(new QuoteProductComponentsAttributeValue());
		;
		QuoteDetail response = illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null);

	}

	/*
	 * Positive test case for updateSitePropertiesAttributes
	 */
	@Test
	public void testUpdateSitePropertiesAttributesForNullQuoteProductComponentsAttributeValue()
			throws TclCommonException {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstProductComponentListForNull());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
				Mockito.any())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any())).thenReturn(null);
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(new QuoteProductComponentsAttributeValue()))
				.thenReturn(new QuoteProductComponentsAttributeValue());

		QuoteDetail response = illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null);
		ResponseResource<QuoteDetail> res = quotesController.updateSitePropertiesAttributes(1, 1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(res.getData() != null && res.getStatus() == Status.SUCCESS);

	}

	/*
	 * Positive test case for updateSitePropertiesAttributes
	 */
	@Test
	public void testUpdateSitePropertiesAttributesForNullProductAttributeMaster() throws TclCommonException {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstProductComponentListForNull());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
				Mockito.any())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMasterForNull());
		Mockito.when(productAttributeMasterRepository.save(Mockito.any())).thenReturn(new ProductAttributeMaster());
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(new QuoteProductComponentsAttributeValue()))
				.thenReturn(new QuoteProductComponentsAttributeValue());

		QuoteDetail response = illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null);
		ResponseResource<QuoteDetail> res = quotesController.updateSitePropertiesAttributes(1, 1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(res.getData() != null && res.getStatus() == Status.SUCCESS);

	}

	/*
	 * Positive test case for updateSitePropertiesAttributes
	 */

	@Test
	public void testUpdateSitePropertiesAttributesForNullQuoteProductComponent() throws TclCommonException {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstProductComponentListForNull());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
				Mockito.any())).thenReturn(null);
		Mockito.when(quoteProductComponentRepository.save(Mockito.any())).thenReturn(new QuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMasterForNull());
		Mockito.when(productAttributeMasterRepository.save(Mockito.any())).thenReturn(new ProductAttributeMaster());
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(new QuoteProductComponentsAttributeValue()))
				.thenReturn(new QuoteProductComponentsAttributeValue());

		QuoteDetail response = illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.getUpdateRequest());
		assertTrue(response != null);
		QuoteDetail res1 = illQuoteService.updateSitePropertiesAttributes(quoteObjectCreator.getUpdateRequest());
		assertTrue(res1 != null);
		ResponseResource<QuoteDetail> res = quotesController.updateSitePropertiesAttributes(1, 1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(res.getData() != null && res.getStatus() == Status.SUCCESS);
	}

	/*
	 * Negative test case for updateSitePropertiesAttributes by passing null
	 * UpdateRequest
	 */
	@Test
	public void testUpdateSitePropertiesAttributesForMstProductFamily() throws TclCommonException {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		ResponseResource<QuoteDetail> response = quotesController.updateSitePropertiesAttributes(1, 1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * Negative test case for updateSitePropertiesAttributes by passing null
	 * UpdateRequest
	 */
	@Test
	public void testUpdateSitePropertiesAttributesForQuoteIllSite() throws TclCommonException {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		ResponseResource<QuoteDetail> response = quotesController.updateSitePropertiesAttributes(1, 1, 1,
				quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * Negative test case for updateSitePropertiesAttributes by passing null
	 * UpdateRequest
	 */
	@Test
	public void testUpdateSitePropertiesAttributesForNullUpdateRequest() throws TclCommonException {
		ResponseResource<QuoteDetail> response = quotesController.updateSitePropertiesAttributes(1, 1, 1, null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * Positive test case for updateCurrency
	 * 
	 */
	@Test
	public void testUpdateCurrency() throws TclCommonException {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuotePriceList());
		Mockito.when(
				currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getCurrencyConversionRate()));

		ResponseResource<String> response = quotesController.updateCurrency(679, 678, "USD");
		System.out.println(response.getResponseCode());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * Negative test case for updateCurrency
	 * 
	 */
	@Test
	public void testUpdateCurrencyForNull() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuotePriceList());
		Mockito.when(
				currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getCurrencyConversionRate()));

		ResponseResource<String> response = quotesController.updateCurrency(null, null, null);
		System.out.println(response.getResponseCode());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * Negative test case for updateCurrency
	 * 
	 */
	@Test
	public void testUpdateCurrencyForException() throws TclCommonException {

		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuotePriceList());
		Mockito.when(
				currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getCurrencyConversionRate()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
		ResponseResource<String> response = quotesController.updateCurrency(679, 678, "USD");
		System.out.println(response.getResponseCode());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * Negative test case for updateCurrency
	 * 
	 */
	@Test
	public void testUpdateCurrencyForInputCurrencyForException1() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(null);
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuotePriceList());
		Mockito.when(
				currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getCurrencyConversionRate()));

		ResponseResource<String> response = quotesController.updateCurrency(679, 678, "USD");
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/*
	 * @Test public void getSupplierUnavailableTriggerEmail() throws
	 * TclCommonException { ResponseResource<TriggerEmailResponse> response =
	 * quotesController.getSupplierUnavailableTriggerEmail(1, 1);
	 * assertTrue(response != null); }
	 */

	/*
	 * @Test public void getNegativeSupplierUnavailableTriggerEmail() throws
	 * TclCommonException {
	 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
	 * anyString(), Mockito.anyByte()))
	 * .thenReturn(quoteObjectCreator.getMstAttributeList());
	 * Mockito.when(quoteLeAttributeValueRepository.
	 * findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
	 * .thenReturn(null);
	 * Mockito.when(quoteToLeRepository.findByQuoteAndQuoteVersion(Mockito.any(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getQuoteToLeList());
	 * Mockito.when(quotePriceRepository.findByQuoteIdAndVersion(Mockito.anyInt(),
	 * Mockito.anyInt())) .thenReturn(quoteObjectCreator.getQuotePriceList());
	 * Mockito.when(
	 * currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(
	 * ), Mockito.anyString()))
	 * .thenReturn(Optional.ofNullable(quoteObjectCreator.getCurrencyConversionRate(
	 * )));
	 * 
	 * ResponseResource<TriggerEmailResponse> response =
	 * quotesController.getSupplierUnavailableTriggerEmail(null, null);
	 * assertTrue(response != null); }
	 */

	@Test
	public void generateCof() throws TclCommonException {
		HttpServletResponse response = mock(HttpServletResponse.class);
		ResponseResource<String> generateCofPdf = quotesController.generateCofPdf(1, 1, response, false);

		assertTrue(generateCofPdf != null);

	}

	@Test
	public void testNegativeGenerateCof() throws TclCommonException {
		ResponseResource<String> generateCofPdf = quotesController.generateCofPdf(1, 1, null, false);
		assertTrue(generateCofPdf != null);

	}

	@Test
	public void approvedManualQuotes() throws TclCommonException {
		HttpServletRequest response = mock(HttpServletRequest.class);
		Mockito.when(cofDetailsRepository.findByOrderUuidAndSource(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getCofDetails());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSite(Mockito.any()))
				.thenReturn(quoteObjectCreator.getSiteFeasibilities1());
		ResponseResource<QuoteDetail> resource = quotesController.approvedManualQuotes(1, 1, response);
		assertTrue(resource != null);

	}

	@Test
	public void testNegativeapprovedManualQuotes() throws TclCommonException {
		ResponseResource<QuoteDetail> resource = quotesController.approvedManualQuotes(1, 1, null);
		assertTrue(resource != null);

	}

	@Test
	public void generateQuotePdf() throws TclCommonException {
		HttpServletResponse response = mock(HttpServletResponse.class);
		ResponseResource<String> generateCofPdf = quotesController.generateQuotePdf(1, 1, response);

		assertTrue(generateCofPdf != null);

	}

	@Test
	public void testNegativegenerateQuotePdf() throws TclCommonException {
		HttpServletResponse response = mock(HttpServletResponse.class);

		ResponseResource<String> generateCofPdf = quotesController.generateQuotePdf(1, 1, null);
		assertTrue(generateCofPdf != null);

	}

	@Test
	public void shareQuote() throws TclCommonException {
		HttpServletResponse response = mock(HttpServletResponse.class);
		ResponseResource<ServiceResponse> responseResource = quotesController.shareQuote(1, 1, "vivekkumar@gmail.com");

		assertTrue(responseResource != null);

	}

	@Test
	public void testNegativeShareQuote() throws TclCommonException {
		HttpServletResponse response = mock(HttpServletResponse.class);

		ResponseResource<ServiceResponse> responseResource = quotesController.shareQuote(null, null, null);
		assertTrue(responseResource != null);

	}

	@Test
	public void triggerAccountManagerCofDownloadNotification() throws TclCommonException {
		HttpServletResponse response = mock(HttpServletResponse.class);
		ResponseEntity<String> responseResource = quotesController.triggerAccountManagerCofDownloadNotification(1, 2);

		assertTrue(responseResource != null);

	}

	@Test
	public void testNegativetriggerAccountManagerCofDownloadNotification() throws TclCommonException {
		HttpServletResponse response = mock(HttpServletResponse.class);

		ResponseEntity<String> responseResource = quotesController.triggerAccountManagerCofDownloadNotification(null,
				null);
		assertTrue(responseResource != null);

	}

	@Test
	public void uploadCofPdf() throws TclCommonException, IOException {
		HttpServletResponse response = mock(HttpServletResponse.class);
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				quoteObjectCreator.getFileAsByteArray());
		ResponseResource<TempUploadUrlInfo> responseResource = quotesController.uploadCofPdf(1, 2, response, result);

		assertTrue(responseResource != null);

	}

	@Test
	public void NegativeuploadCofPdf() throws TclCommonException, IOException {
		HttpServletResponse response = mock(HttpServletResponse.class);
		String name = "location_template.txt";
		String originalFileName = "location_template.xlsx";
		String contentType = "text/plain";
		MultipartFile result = new MockMultipartFile(name, originalFileName, contentType,
				quoteObjectCreator.getFileAsByteArray());
		ResponseResource<TempUploadUrlInfo> responseResource = quotesController.uploadCofPdf(1, 2, response, null);
		assertTrue(responseResource != null);

	}

	@Test
	public void processDocusign() throws TclCommonException, IOException {
		HttpServletRequest response = mock(HttpServletRequest.class);
		ApproverListBean approverList=new ApproverListBean();
		approverList.setApprovers(new ArrayList<>());
		ResponseResource<String> responseResource = quotesController.processDocusignWithApprover(1, 1, false, "v",
				"vivek@gmail.com",approverList, null);

		assertTrue(responseResource != null);

	}

	@Test
	public void NegativeProcessDocusign() throws TclCommonException, IOException {
		HttpServletRequest response = mock(HttpServletRequest.class);
		ApproverListBean approverList=new ApproverListBean();
		approverList.setApprovers(new ArrayList<>());
		ResponseResource<String> responseResource = quotesController.processDocusignWithApprover(1, 1, false, "v",
				"vivek@gmail.com",approverList, null);
		assertTrue(responseResource != null);

	}

	@Test
	public void testEmailTrigger() throws TclCommonException, IOException {
		ResponseResource<TriggerEmailResponse> responseResource = quotesController.getCustomerEmailIdTriggerEmail(1, 1);
		assertTrue(responseResource != null);

	}

	/*
	 * positive test case for delegate User sales login
	 * 
	 */
	@Test
	public void testDelegateUserForSalesLogin() throws TclCommonException, IOException {
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());
		ResponseResource<TriggerEmailResponse> responseResource =	quotesController.deletegateUserForSalesUserLogin(1,1,httpServletRequest);
//		TriggerEmailResponse responseResource = illQuoteService.delegateUserForSalesLogin(1, "1.1.1.1");
		assertTrue(responseResource != null);

	}

	/*
	 * positive test case for delete Quote
	 * 
	 */
	@Test
	public void testDeleteQuote() throws TclCommonException, IOException {

		Mockito.when(quoteRepository.findById(1)).thenReturn(Optional.ofNullable(quoteObjectCreator.getQuote()));
		Mockito.when(orderRepository.findByQuote(Mockito.any()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getOrder()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getQuotePriceList());
		Mockito.when(orderPriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getOrderPriceList());
		Mockito.when(orderProductComponentRepository.findByReferenceId(Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getOrderProductComponentList());
		Mockito.when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
		Mockito.when(cofDetailsRepository.findByOrderUuid(Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getCofDetails());
		Mockito.when(docusignAuditRepository.findByOrderRefUuid(Mockito.any())).thenReturn(null);
		Mockito.when(ordersLeAttributeValueRepository.findByOrderToLe(Mockito.any())).thenReturn(null);
		Mockito.when(omsAttachmentRepository.findByOrderToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getAttachmentsList());
		illQuoteService.deleteQuote(1);
	}

}
