package com.tcl.dias.oms.npl.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyByte;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ContactAttributeInfo;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.MailNotificationBean;
import com.tcl.dias.oms.beans.QuoteLeAttributeBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.TriggerEmailRequest;
import com.tcl.dias.oms.beans.TriggerEmailResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.dto.CreateDocumentDto;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteDelegation;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuoteNplLinkSla;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.QuoteToLeProductFamily;
import com.tcl.dias.oms.entity.repository.CurrencyConversionRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.LinkFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderIllSitesRepository;
import com.tcl.dias.oms.entity.repository.OrderPriceRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.OrderProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteDelegationRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteNplLinkSlaRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.SlaMasterRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.ill.controller.v1.IllOrderController;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.beans.QuoteResponse;
import com.tcl.dias.oms.npl.controller.v1.NplQuoteController;
import com.tcl.dias.oms.npl.pdf.service.NplQuotePdfService;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.NplObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;



/**
 * This file contains test cases for the quotesController.java class.
 * 
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NplQuoteControllerTest {
	@MockBean
	private QuoteRepository quoteRepository;

	@MockBean
	private QuoteToLeRepository quoteToLeRepository;

	@MockBean
	private IllSiteRepository illSiteRepository;
	
	@MockBean
	private NplLinkRepository nplLinkRepository;
	
	@MockBean
	private OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@MockBean
	private QuotePriceRepository quotePriceRepository;

	@MockBean
	private QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	private OrderRepository orderRepository;

	@Autowired
	private NplObjectCreator quoteObjectCreator;

	@MockBean
	private QuoteDelegationRepository quoteDelegationRepository;

	@Autowired
	NplQuoteController quotesController;
	
	@Autowired
	NplQuoteService nplQuoteService;
	
	@Mock
	NplQuoteService mockNplQuoteService;

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
	private OmsSfdcService omsSfdcService;


	@MockBean
	private ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private SlaMasterRepository slaMasterRepository;
	
	@MockBean
	private CurrencyConversionRepository currencyConversionRepository;
	
	@MockBean
	private MQUtils mqUtils;

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
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;

	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;
	
	@MockBean
	QuoteNplLinkSlaRepository quoteNplLinkSlaRepository;
	
	@Mock
	ObjectMapper objectMapper;
	
	@Mock
	TemplateEngine templateEngine;
	
	@MockBean
	NplQuotePdfService mockNplQuotePdfService;
	
	@MockBean
	NplPricingFeasibilityService mockNplPricingFeasibilityService;
	
	@MockBean
	LinkFeasibilityRepository linkFeasibilityRepository;
	
	@MockBean
	NotificationService notificationService;

	@MockBean
	MailNotificationBean mailNotificationBean;
	
	/**
	 * 
	 * init- predefined mocks
	 * @throws TclCommonException 
	 */
	@Before
	public void init() throws TclCommonException {

		Mockito.mock(UserInformation.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                        .thenReturn(quoteObjectCreator.getUserInformation());
        Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(quoteObjectCreator.getUser());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(quoteObjectCreator.getMstProductFamily());
	
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
				Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(Mockito.any(), Mockito.anyString(), Mockito.anyByte())).thenReturn(quoteObjectCreator.getMstOffering());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(quoteObjectCreator.getQuoteIllSite());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(quoteObjectCreator.getQuote());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLeFamilyList());
	
		
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn((quoteObjectCreator.getMstProductComponent()));
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte()))
		.thenReturn((quoteObjectCreator.getMstProductComponentList()));
		Mockito.when(productAttributeMasterRepository.save(Mockito.any(ProductAttributeMaster.class))).
		thenReturn(quoteObjectCreator.getProductAtrributeMas());
		Mockito.when(mstProductComponentRepository.save(Mockito.any(MstProductComponent.class))).
		thenReturn(quoteObjectCreator.getMstProductComponent());
	
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getQuote());
		
		Mockito.when(illSiteRepository.findByIdInAndStatus(Mockito.anyList(), Mockito.anyByte()))
				.thenReturn((quoteObjectCreator.getIllsitesMock()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteToLe()));
		
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.geQuotePrice());
		
		Mockito.when(quoteProductComponentRepository.findByReferenceIdInAndReferenceName(Mockito.anyList(),null))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));

		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))

				.thenReturn((quoteObjectCreator.getCustomer()));
		
		
		Mockito.when(quoteProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(quoteObjectCreator.getAttribute()));

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
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn(quoteObjectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(Mockito.anyInt(),
						 Mockito.matches("LOCAL_IT_CONTACT")))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());

		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(nplLinkRepository.save(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteNplLink());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentsAttributeValueRepository)
				.delete(new QuoteProductComponentsAttributeValue());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentRepository)
				.delete(new QuoteProductComponent());
		Mockito.doThrow(new RuntimeException()).when(illSiteRepository).save(new QuoteIllSite());
		Mockito.doThrow(new RuntimeException()).when(quoteToLeRepository).save(new QuoteToLe());
		Mockito.doThrow(new RuntimeException()).when(nplLinkRepository).save(new QuoteNplLink());
		
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(), 
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());
		
		when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getQuote2()));
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.any())).thenReturn("");
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
				Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteNplLinkSlaRepository.save(Mockito.any(QuoteNplLinkSla.class))).thenReturn(quoteObjectCreator.getQuoteNplLinkSla());
		Mockito.when(slaMasterRepository.findBySlaName(Mockito.anyString())).thenReturn((quoteObjectCreator.createSlaMaster()));
		Mockito.when(quoteProductComponentRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.craeteQuoteProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
		.thenReturn(quoteObjectCreator.createQuoteProductComponentsAttributeValue());
		Mockito.when(nplLinkRepository.findByProductSolutionIdAndSiteAIdAndSiteBIdAndStatus(1, 1, 1, (byte) 1)).
        thenReturn(Optional.of(quoteObjectCreator.getQuoteNplLink()));
		Mockito.when(quoteProductComponentRepository
		.findByReferenceIdAndType(1, "primary")).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		doNothing().when(quoteProductComponentsAttributeValueRepository).delete(Mockito.any());
		doNothing().when(quoteProductComponentRepository).delete(Mockito.any());
		Mockito.when(nplLinkRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
		.thenReturn(quoteObjectCreator.getQuoteLinkNpl());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).
		thenReturn(quoteObjectCreator.getMstAttributeList());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteToLe());
		Mockito.when(quoteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuote());
		doNothing().when(linkFeasibilityRepository).deleteAll(Mockito.any());
		Mockito.when(linkFeasibilityRepository.findByQuoteNplLinkAndIsSelected(Mockito.any(), 
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getLinkFeasibilityList());
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSiteAndIsSelected(any(), anyByte()))
		.thenReturn(quoteObjectCreator.getSiteFeasibilities());
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(Mockito.anyInt(), Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteProductComponent());

	}

	/*
	 * positive test case for 	 * 
	 */
	@Test
	public void testUpdateLink()throws Exception
 {
		Mockito.when(orderConfirmationAuditRepository
				.findByOrderRefUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
			ResponseResource<NplQuoteBean> response = quotesController.updateLinkInformation(298, 
					quoteObjectCreator.getNplQuoteDetailForCreateNew(),1);
			assertTrue(response != null && response.getStatus() == Status.SUCCESS);
		
	}
	
	/*
	 * negative test case for 	 * 
	 */
	@Test
	public void testUpdateLink1()throws Exception
	{
		Mockito.when(nplLinkRepository.findByProductSolutionIdAndSiteAIdAndSiteBIdAndStatus(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(), Mockito.anyByte())).thenReturn(Optional.empty());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.anyString())).thenReturn(quoteObjectCreator.getProductSlaDetailsJSON());
		Mockito.when(orderConfirmationAuditRepository
				.findByOrderRefUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
			ResponseResource<NplQuoteBean> response = quotesController.updateLinkInformation(298, 
					quoteObjectCreator.getNplQuoteDetailForCreateNew1(),1);
		
			assertTrue(response.getStatus() == Status.FAILURE);
		
	}
	
	@Test
	public void testUpdateLink2()throws Exception
	{
		Mockito.when(orderConfirmationAuditRepository
				.findByOrderRefUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
			ResponseResource<NplQuoteBean> response = quotesController.updateLinkInformation(298, 
					quoteObjectCreator.getNplQuoteDetailForCreateNew2(),1);
		
			assertTrue(response.getStatus() == Status.FAILURE);
		
	}
	
	@Test
	public void testUpdateLink3()throws Exception
	{
		Mockito.when(orderConfirmationAuditRepository
				.findByOrderRefUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
			ResponseResource<NplQuoteBean> response = quotesController.updateLinkInformation(298, 
					quoteObjectCreator.getNplQuoteDetailForCreateNew3(),1);
		
			assertTrue(response.getStatus() == Status.FAILURE);
		
	}
	
	@Test
	public void testUpdateLinkForNull()throws Exception
	{
			
			ResponseResource<NplQuoteBean> response = quotesController.updateLinkInformation(null, 
					null,null);		
			assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
		
	}
	
	@Test
	public void testUpdateLinkFor()throws Exception
 {
			when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(any(),anyString(),anyByte())).thenReturn(null);
			
			ResponseResource<NplQuoteBean> response = quotesController.updateLinkInformation(1, 
					quoteObjectCreator.getNplQuoteDetail3(),1);		
			assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
		
	}
	
	@Test
	public void testupdateLinkInfowithSites() throws Exception {

		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(any(),anyString(),anyByte())).thenReturn(null);
		Mockito.when(orderConfirmationAuditRepository
				.findByOrderRefUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
		
		ResponseResource<NplQuoteBean> response = quotesController.updateLinkInformation(1, 
				quoteObjectCreator.getNplQuoteDetail3(),1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	/**
	 * positive case for testupdateSiteProperties
	 * 
	 * testupdateSiteProperties
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testupdateQuoteSiteProperties() throws TclCommonException {
		
		ResponseResource<NplQuoteDetail> response = quotesController
				.updateQuoteSiteProperties(1, 1, 1, "test", quoteObjectCreator.getNplUpdateRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testupdateQuoteSiteProperties2() throws TclCommonException {
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte()))
		.thenReturn(null);
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
		.thenReturn(null);
		ResponseResource<NplQuoteDetail> response = quotesController
				.updateQuoteSiteProperties(1, 1, 1, "test", quoteObjectCreator.getNplUpdateRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	/**
	 * negative case for testupdateSiteProperties
	 * 
	 * testupdateSiteProperties
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testupdateQuoteSitePropertiesForException() throws TclCommonException {
	ResponseResource<NplQuoteDetail> response = quotesController.updateQuoteSiteProperties(null, null, null, null, null);
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}
	
	/**
	 * 
	 * testcreateDocument - positive case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testCreateDocument() throws TclCommonException {
	
		//doNothing().when(nplQuoteService).updateBillingInfoForSfdc(Mockito.any(CustomerLeDetailsBean.class), Mockito.any(QuoteToLe.class));
		//Mockito.when(nplQuoteService.returnServiceProviderName(Mockito.anyInt())).thenReturn("test");
		/*Mockito.when(mqUtils.sendAndReceive("customerleattrQueue",
				String.valueOf(1))).thenReturn("");*/
		doNothing().when(omsSfdcService).processUpdateOpportunity(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
		ResponseResource<CreateDocumentDto> response = quotesController
				.createDocument(1, 1, quoteObjectCreator.getDocumentDto1());
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
	 * positive case for testupdateLegalEntityProperties
	 * 
	 * testupdateLegalEntityProperties
	 * 
	 * @throws TclCommonException
	 */
	@Test
	public void testupdateLegalEntityProperties() throws TclCommonException {
		ResponseResource<NplQuoteDetail> response = quotesController
				.updateLegalEntityProperties(1, 1, quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response != null && response.getStatus() == Status.SUCCESS);
	}

	@Test
	public void testupdateLegalEntityProperties2() throws TclCommonException {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
				Mockito.anyString())).thenReturn(new ArrayList<>());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).
		thenReturn(new ArrayList<>());
		ResponseResource<NplQuoteDetail> response = quotesController
				.updateLegalEntityProperties(1, 1, quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() != null && response != null && response.getStatus() == Status.SUCCESS);
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

		ResponseResource<NplQuoteDetail> response = quotesController.updateLegalEntityProperties(null, null, null);
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
	public void testupdateLegalEntityPropertiesForEmptyQuote() throws TclCommonException {
	
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

		ResponseResource<NplQuoteDetail> response = quotesController.updateLegalEntityProperties(null, null, quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getData() == null && response.getStatus() == Status.FAILURE);
	}

	/**
	 * test create quote Negative test case
	 **/
	@Test
	public void testcreateQuoteForNullCustomer() throws Exception {
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(null);
		ResponseResource<QuoteResponse> response = quotesController
				.createQuote(quoteObjectCreator.getNplQuoteDetailForCreateNew(), 1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}


	/**
	 * test create quote positive test case
	 **/
	@Test
	public void testcreateNewQuoteWithOutQuoteId() throws Exception {
		
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any()))
				.thenReturn(null);

		Mockito.when(quoteToLeProductFamilyRepository.save(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamily());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
				Mockito.any())).thenReturn(null);
		Mockito.when(productSolutionRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		Mockito.when(nplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		Mockito.when(nplLinkRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteNplLink());
		Mockito.when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getIllsites()));
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteIllSite());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
				Mockito.anyString())).thenReturn(new ArrayList<>());
		NplQuoteDetail nplQuoteDetails=quoteObjectCreator.getNplQuoteDetailForCreateNew();
		nplQuoteDetails.setQuoteId(null);
		nplQuoteDetails.setQuoteleId(null);
		ResponseResource<QuoteResponse> response = quotesController.createQuote(nplQuoteDetails, 25);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * test create quote positive test case
	 **/
	@Test
	public void testcreateQuoteWithQuoteId() throws Exception {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getQuoteToLe()));

		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),
				Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
				.thenReturn(quoteObjectCreator.getSolutionList());

		Mockito.when(productSolutionRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getSolution());
		Mockito.when(nplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(quoteObjectCreator.getQuoteNplLinkList());
		Mockito.when(nplLinkRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteNplLink());
		Mockito.when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getIllsites()));
		
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());
		ResponseResource<QuoteResponse> response = quotesController.createQuote(quoteObjectCreator.getNplQuoteDetail(),
				25);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}


	/**
	 * test create quote negative test case
	 **/
	@Test
	public void testcreateQuoteExcpetion() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus(null, 1)).thenReturn((null));
		ResponseResource<QuoteResponse> response = quotesController.createQuote(null, null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}
	/**
	 * for testing siteIfo testgetSiteInfooException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetLinkInfo() throws Exception {
		
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getIllsites4()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));

		ResponseResource<QuoteResponse> response = quotesController.getLinkInfo(5);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testgetLinkInfowithSites() throws Exception {

		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn((quoteObjectCreator.getQuoteProductComponent()));
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());

		ResponseResource<QuoteResponse> response = quotesController.getLinkInfo(5);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing siteIfo testgetSiteInfooException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetLinkInfoException() throws Exception {
		Mockito.when(nplLinkRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn((null));
		//Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(quoteObjectCreator.getIllsites());
		ResponseResource<QuoteResponse> response = quotesController.getLinkInfo(5);
		assertTrue(response.getStatus() == Status.FAILURE);
	}
	@Test
	public void testgetLinkInfoNullSite() throws Exception {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn((null));
		ResponseResource<QuoteResponse> response = quotesController.getLinkInfo(5);
		assertTrue(response.getStatus() == Status.FAILURE);
	}
	@Test
	public void testgetSiteProperties() throws Exception {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
				Mockito.anyInt(),  Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		ResponseResource<List<QuoteProductComponentBean>> response = quotesController.getSiteProperties(1, 1, 25);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testgetSitePropertiesForNullSite() throws Exception {
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(null);
		//Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
				//Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(quoteObjectCreator.getQuoteProductComponent());
		ResponseResource<List<QuoteProductComponentBean>> response = quotesController.getSiteProperties(1, 1, 25);
		assertTrue(response.getStatus() == Status.FAILURE);
	}


	/**
	 * for testing siteIfo testgetSiteInfooException
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetSitePropertiesExceptionForNull() throws Exception {
		ResponseResource<List<QuoteProductComponentBean>> response = quotesController.getSiteProperties(null, null,
				null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}
	
	/**
	 * for testing delete link
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeleteNplLink() throws Exception {
		when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
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
		Mockito.when(nplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(quoteObjectCreator.getQuoteLinkNpl()));
		doNothing().when(nplLinkRepository).delete(Mockito.any());
		Mockito.when(nplLinkRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteLinkNpl());
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getIllsites());

		ResponseResource<String> response = quotesController.deactivateLink(1,1,
				QuoteConstants.DELETE.toString());
		
		ResponseResource<String> response1 = quotesController.deactivateLink(1,1,
				QuoteConstants.DISABLE.toString());
		assertTrue(response.getData() != null && response.getStatus() == Status.SUCCESS &&
				response1.getData() != null && response1.getStatus() == Status.SUCCESS);
	}

	/**
	 * negative test case for testing delete link - when link id is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeleteNplLinkForInavlidLinkId() throws Exception {
		when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getIllsites());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
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
		Mockito.when(nplLinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
		doNothing().when(nplLinkRepository).delete(Mockito.any());
		Mockito.when(nplLinkRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getQuoteLinkNpl());
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(quoteObjectCreator.getIllsites());

		ResponseResource<String> response = quotesController.deactivateLink(1,1,
				QuoteConstants.DELETE.toString());
		
		assertTrue(response.getData() == null || response.getStatus() != Status.SUCCESS);

	}

	/**
	 * negative test case for testdeleteIllsites
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeleteIllLinkForNullLinkId() throws Exception {
		ResponseResource<String> response = quotesController.deactivateLink(1,null,
				QuoteConstants.DELETE.toString());
		assertTrue(response.getData() == null || response.getStatus() != Status.SUCCESS);
	}
	
	/**
	 * negative test case for testdeleteIllsites
	 * 
	 * @throws Exception
	 */
	@Test
	public void testdeleteIllLinkForInvalidAction() throws Exception {
		ResponseResource<String> response = quotesController.deactivateLink(1,1,"Invalid Text");
		assertTrue(response.getData() == null || response.getStatus() != Status.SUCCESS);
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
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(quoteObjectCreator.getOptionalQuoteToLe());
		Mockito.when(quoteDelegationRepository.save(Mockito.any(QuoteDelegation.class))).thenReturn(quoteObjectCreator.getQuoteDelegation());
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
	 * * test case to get the trigger email once the user is selected - positive
	 * case
	*/ 

	@Test
	public void testTriggerEmailForEmptyQuoteDelegation() throws TclCommonException {
		Mockito.when(userRepository.findByEmailIdAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(quoteObjectCreator.getUser());
		Mockito.when(quoteDelegationRepository.findByAssignToAndStatus(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(null));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(quoteObjectCreator.getOptionalQuoteToLe());
		Mockito.when(quoteDelegationRepository.save(Mockito.any(QuoteDelegation.class))).thenReturn(quoteObjectCreator.getQuoteDelegation());
		TriggerEmailRequest triggerRequest = new TriggerEmailRequest();
		triggerRequest.setEmailId("test@gmail.com");
		triggerRequest.setQuoteToLeId(1);
		MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
		httpServletRequest.setParameter("X-Forwarded-For", "127.0.0.1");
		ResponseResource<TriggerEmailResponse> response = quotesController.triggerEmail(null, null, triggerRequest,
				httpServletRequest);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	/* * test case to get the trigger email once the user is selected - negative
	 * case*/
	 

	@Test
	public void testTriggerEmailForNullEmailId() throws TclCommonException {
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
	public void testeditSites() throws Exception {
		
		Mockito.when(quoteProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(quoteObjectCreator.getAttribute()));
		Mockito.when(quoteNplLinkSlaRepository.findByQuoteNplLink_Id(Mockito.anyInt())).thenReturn(quoteObjectCreator.getQuoteNplLinkSlaList());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.save(Mockito.any(QuoteProductComponentsAttributeValue.class)))
				.thenReturn(quoteObjectCreator.createQuoteProductComponentsAttributeValue());
		UpdateRequest updateRequest = quoteObjectCreator.getUpdateRequest();
		updateRequest.setComponentDetails(quoteObjectCreator.getComponetDetails());
		ResponseResource<NplQuoteDetail> response = quotesController.editLinks(1, 1, 1,
				updateRequest);

		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/**
	 * for testing testeditSites site - negative test case
	 * 
	 * @throws Exception
	 */
	@Test

	public void testeditSitesNegativeCase() throws Exception {

		ResponseResource<NplQuoteDetail> response = quotesController.editLinks(null, null, null, null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}
	@Test
	public void testeditSitesWithNullComponentDetail() throws Exception {
		UpdateRequest  updateRequest =quoteObjectCreator.getUpdateRequest();
		updateRequest.setComponentDetails(null);
		ResponseResource<NplQuoteDetail> response = quotesController.editLinks(null, null, null, updateRequest);
		assertTrue(response.getStatus() == Status.FAILURE);
	}
	
	@Test
	public void getAllAttributeDetailsByQuoteToLeId() throws TclCommonException {
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
	public void getAllAttributeDetailsByQuoteToLeIdWithoutID() throws TclCommonException {
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
	public void getAllAttributeDetailsByQuoteToLeIdWithoutData() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		ResponseResource<Set<LegalAttributeBean>> response = quotesController.getAllAttributesByQuoteToLeId((Integer) 1,
				1);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}

	public void testUpdateQuoteToLeStatus() throws Exception {
	
		ResponseResource<NplQuoteDetail> response = quotesController.updateQuoteToLeStatus(298,2, "Select_configuration");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	/**
	 * negative testcase for testing updateQuoteToLeStatus
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateQuoteToLeStatusForNull() throws Exception {
	
		ResponseResource<NplQuoteDetail> response = quotesController.updateQuoteToLeStatus(null,null,null);
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
	}
	
	/**
	 * negative testcase for testing updateQuoteToLeStatus
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpdateQuoteToLeStatusForInvalid() throws Exception {
	
		ResponseResource<NplQuoteDetail> response = quotesController.updateQuoteToLeStatus(298,2,"select configuration without underscore");
		assertTrue(response != null && response.getStatus() == Status.FAILURE);
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
		Mockito.when(currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(),Mockito.anyString()))
		.thenReturn(Optional.ofNullable(quoteObjectCreator.getCurrencyConversionRate()));
	
		ResponseResource<String> response = quotesController.updateCurrency(679, 678, "USD");
		System.out.println(response.getResponseCode());
		assertTrue(response!= null && response.getStatus() == Status.SUCCESS);
	}
	
	/*
	 * Negative test case for updateCurrency 
	 * 
	 */
	@Test
	public void testUpdateCurrencyForNull() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository
				.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(quoteObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
		.thenReturn(quoteObjectCreator.getQuotePriceList());
		Mockito.when(currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(),Mockito.anyString()))
		.thenReturn(Optional.ofNullable(quoteObjectCreator.getCurrencyConversionRate()));
		
		ResponseResource<String> response = quotesController.updateCurrency(null, null, null);
		System.out.println(response.getResponseCode());
		assertTrue(response.getData()==null&& response.getStatus() == Status.FAILURE);
	}
	
	
	/*
	 * Negative test case for updateCurrency 
	 * 
	 */
	@Test
	public void testUpdateCurrencyForException() throws TclCommonException {

		Mockito.when(mstOmsAttributeRepository
				.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(quoteObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
		.thenReturn(quoteObjectCreator.getQuotePriceList());
		Mockito.when(currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(),Mockito.anyString()))
		.thenReturn(Optional.ofNullable(quoteObjectCreator.getCurrencyConversionRate()));
		Mockito.when( quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
		ResponseResource<String> response = quotesController.updateCurrency(679, 678, "USD");
		System.out.println(response.getResponseCode());
		assertTrue(response.getData()==null&& response.getStatus() == Status.FAILURE);
	}
	
	
	/*
	 * Negative test case for updateCurrency 
	 * 
	 */
	@Test
	public void testUpdateCurrencyForInputCurrencyForException1() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository
				.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(quoteObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
		.thenReturn(null);
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
		.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
		.thenReturn(quoteObjectCreator.getQuotePriceList());
		Mockito.when(currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(),Mockito.anyString()))
		.thenReturn(Optional.ofNullable(quoteObjectCreator.getCurrencyConversionRate()));
		
		ResponseResource<String> response = quotesController.updateCurrency(679, 678, "USD");
		assertTrue(response.getData()==null&& response.getStatus() == Status.FAILURE);
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

	/**
	 * 
	 * testgetQuoteConfigurationException- get Quote Configuration-negative
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetQuoteConfigurationException() throws Exception {
		Mockito.when(userRepository.findByUsernameAndStatus(null, 1)).thenReturn((null));
		ResponseResource<NplQuoteBean> response = quotesController.getQuoteConfiguration(null,null,false);
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
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(),Mockito.anyByte())).thenReturn(quoteObjectCreator.getQuote());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any(Quote.class)))
		.thenReturn(quoteObjectCreator.getQuoteToLeList());
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any(QuoteToLeProductFamily.class))).thenReturn(quoteObjectCreator.getSolutionList());
		Mockito.when(quoteLeAttributeValueRepository
		.findByQuoteToLe(Mockito.any(QuoteToLe.class))).thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(nplLinkRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(quoteObjectCreator.getQuoteNplLinkList());
		Mockito.when(illSiteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(quoteObjectCreator.getIllsites()));
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_Id(Mockito.any()))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(Mockito.anyInt(),
						Mockito.matches("LOCAL_IT_CONTACT")))
				.thenReturn(quoteObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(orderConfirmationAuditRepository
				.findByOrderRefUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
		
		ResponseResource<NplQuoteBean> response = quotesController.getQuoteConfiguration(1,"ALL",false);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}
	
	@Test
	public void testGetTaxExemptedSiteInfowithnullQuoteId() throws Exception {
		ResponseResource<QuoteResponse> response = quotesController.getTaxExemptedSiteInfo(null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}

	@Test
	public void testGetTaxExemptedSiteInfowithWrongQuoteId() throws Exception {
		ResponseResource<QuoteResponse> response = quotesController.getTaxExemptedSiteInfo(674);
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testGetTaxExemptedSiteInfo() throws Exception {
		when(quoteRepository.findByIdAndStatus(anyInt(), anyByte())).thenReturn(quoteObjectCreator.getQuote());
		ResponseResource<QuoteResponse> response = quotesController.getTaxExemptedSiteInfo(676);
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testPersistListOfQuoteLeAttributeswithNull() throws Exception {
		ResponseResource<NplQuoteDetail> response = quotesController.persistListOfQuoteLeAttributes(null,null,null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}
	
	@Test
	public void testPersistListOfQuoteLeAttributes() throws Exception {
		ResponseResource<NplQuoteDetail> response = quotesController.persistListOfQuoteLeAttributes(null,null,quoteObjectCreator.getUpdateRequest());
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testupdateQuoteToLeStatusForNull() throws Exception {
		ResponseResource<NplQuoteDetail> response = quotesController.updateQuoteToLeStatus(null,null,null);
		assertTrue(response.getStatus() == Status.FAILURE);
	}
	
	@Test
	public void testupdateQuoteToLeStatusWithWrongQuoteLeId() throws Exception {
		Mockito.when( quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
		ResponseResource<NplQuoteDetail> response = quotesController.updateQuoteToLeStatus(500,null,"Get_Quote");

		assertTrue(response.getStatus() == Status.FAILURE);
	}
	
	@Test
	public void testupdateQuoteToLeStatusWithQuoteLeId() throws Exception {
		ResponseResource<NplQuoteDetail> response = quotesController.updateQuoteToLeStatus(676,null,"Get_Quote");
		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	
	
	/*
	 * test case for updateSfdcStage
	 */
	@Test
	public void testUpdateSfdcStage()throws Exception
 {
		Mockito.when(quoteToLeRepository
				.findById(Mockito.anyInt())).thenReturn(quoteObjectCreator.getOptionalQuoteToLe());
		doNothing().when(omsSfdcService).processUpdateOpportunity(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
		ResponseResource<String> response = quotesController.updateSdfcStage(1, 2, "stage");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	@Test
	public void testPersistListOfQuoteLeAttributes2() throws Exception {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute_Name(Mockito.any(),
				Mockito.anyString())).thenReturn(null);
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte())).thenReturn(null);
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte()))
		.thenReturn(null);
		ResponseResource<NplQuoteDetail> response = quotesController.persistListOfQuoteLeAttributes(null, null,
				quoteObjectCreator.getUpdateRequest());

		assertTrue(response.getStatus() == Status.SUCCESS);
	}
	
	

	/*
	 * test case for processQuotePdf
	 */
	@Test
	public void testProcessQuotePdf() throws Exception {

		ResponseResource<String> response=quotesController.generateQuotePdf(1, 1,new MockHttpServletResponse());
		Mockito.doReturn("test").when(mockNplQuotePdfService).processQuotePdf(1, new MockHttpServletResponse(), 1);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}
	@Test
	public void testTriggerAccountManagerCofDownloadNotification() throws Exception{
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(quoteObjectCreator.getUser());
		ResponseEntity<String> response=quotesController.triggerAccountManagerCofDownloadNotification(Mockito.anyInt(), Mockito.anyInt());
		assertTrue(response == null);
				
	}
	@Test
	public void testShareQuote() throws Exception{
		Mockito.doReturn("test").when(mockNplQuotePdfService).processQuoteHtml(1);
		ResponseResource<ServiceResponse> response=quotesController.shareQuote(1, 2, "optimus@tatacommu.com");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testTriggerForFeasibilityBean() throws Exception{
		ResponseResource<String> response=quotesController.triggerForFeasibilityBean(quoteObjectCreator.getFeasibilityBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testTriggerPricing() throws Exception{
		ResponseResource<String> response=quotesController.triggerPricing(Mockito.anyInt(), Mockito.anyInt());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void  testFeasibilityCheck() throws Exception{
		QuoteToLe quoteToLe=quoteObjectCreator.getQuoteToLe();
		quoteToLe.setQuoteLeAttributeValues(quoteObjectCreator.getQuoteLeAttributeValueSet());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.ofNullable(quoteToLe));
		ResponseResource<QuoteLeAttributeBean> response=quotesController.feasibilityCheck(Mockito.anyInt());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void  testProcessCofPdf() throws Exception{
		
		ResponseResource<String> response=quotesController.generateCofPdf(1,1,new MockHttpServletResponse(),true );
		Mockito.doReturn("test").when(mockNplQuotePdfService).processCofPdf(1, new MockHttpServletResponse(), true, true, 1,null);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	} 
	@Test
	public void  testGetCustomerEmailIdTriggerEmail() throws Exception{
		Mockito.when(notificationService.salesOrderMultipleleNotification(mailNotificationBean)).thenReturn(true);
		ResponseResource<TriggerEmailResponse> response=quotesController.getCustomerEmailIdTriggerEmail(1,2);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	@Test
	public void testProcessDocusign() throws Exception {
		doNothing().when(mockNplQuotePdfService).processDocusign(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyBoolean(),Mockito.anyString(),Mockito.any(),Mockito.any());
		ResponseResource<String> response=quotesController.processDocusign(1, 1, true, "optimus", "optimus@tatacommunications.com", null,new MockHttpServletRequest());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}

	/*
	 * positive test case for 	 *
	 */
	@Test
	public void testUpdateUploadLink()throws Exception
	{
		Mockito.when(orderConfirmationAuditRepository
				.findByOrderRefUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
		ResponseResource<String> response = quotesController.uploadLinkInformation(quoteObjectCreator.getNplLinkUpdateBean());
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);

	}

	/*
	 * Negative test case for 	 *
	 */
	@Test
	public void testUpdateUploadLinkWithNull()throws Exception
	{
		Mockito.when(orderConfirmationAuditRepository
				.findByOrderRefUuid(Mockito.anyString())).thenReturn(quoteObjectCreator.getOrderConfirmationAudit());
		ResponseResource<String> response = quotesController.uploadLinkInformation(null);
		assertTrue(response == null && response.getStatus() == Status.FAILURE);

	}

}
