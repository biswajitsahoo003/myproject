package com.tcl.dias.oms.izpoc.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.QuoteResponse;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CurrencyConversionRepository;
import com.tcl.dias.oms.entity.repository.CustomerRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.MstProductOfferingRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteSlaRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SiteFeasibilityRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.izopc.beans.QuoteBean;
import com.tcl.dias.oms.izopc.beans.QuoteDetail;
import com.tcl.dias.oms.izopc.service.v1.IzoPcQuoteService;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.IzoPcObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains test cases for  IzoPcQuoteService.java class.
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IzoPcQuoteServiceTest {
	
	@Autowired
	IzoPcQuoteService izoPcQuoteService;
	
	@MockBean
	NotificationService notificationService;
	
	@Autowired
	IzoPcObjectCreator izoPcObjectCreator;
	
	@MockBean
	OmsSfdcService omsSfdcService;
	
	@MockBean
	UserRepository userRepository;
	
	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;
	
	@MockBean
	CustomerRepository customerRepository;
	
	@MockBean
	QuoteRepository quoteRepository;
	
	@MockBean
	QuoteToLeRepository quoteToLeRepository;
	
	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;
	
	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;
	
	@MockBean
	ProductSolutionRepository productSolutionRepository;
	
	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@MockBean
	QuotePriceRepository quotePriceRepository;
	
	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@MockBean
	QuoteIllSiteSlaRepository quoteIllSiteSlaRepository;
	
	@MockBean
	SiteFeasibilityRepository siteFeasibilityRepository;
	
	@MockBean
	IllSiteRepository illSiteRepository;
	
	@MockBean
	MstProductOfferingRepository mstProductOfferingRepository;
	
	@MockBean
	MstProductComponentRepository mstProductComponentRepository;
	
	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@MockBean
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;
	
	@MockBean
	MQUtils mqUtils;
	
	@MockBean
	CurrencyConversionRepository currencyConversionRepository;
	
	@Before
	public void init() throws TclCommonException {
		Mockito.when(mstProductComponentRepository.findByName(Mockito.anyString()))
				.thenReturn((izoPcObjectCreator.getMstProductComponent()));
		Mockito.when(userRepository.findByUsernameAndStatus("admin", 1)).thenReturn((izoPcObjectCreator.getUser()));
		
		Mockito.when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findByIdInAndStatus(Mockito.anyList(), Mockito.anyByte()))
				.thenReturn((izoPcObjectCreator.getIllsitesMock()));
		
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn((izoPcObjectCreator.getQuoteProductComponent()));
		
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn((izoPcObjectCreator.getQuoteProductComponent()));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdInAndReferenceName(Mockito.anyList(),null))
				.thenReturn((izoPcObjectCreator.getQuoteProductComponent()));

		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn((izoPcObjectCreator.getCustomer()));
		
		Mockito.when(quoteProductComponentsAttributeValueRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(izoPcObjectCreator.getAttribute()));
		Mockito.when(orderConfirmationAuditRepository.save(Mockito.any())).thenReturn(new OrderConfirmationAudit());

		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getCustomer());
		Mockito.when(customerRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(izoPcObjectCreator.getUserList()));
		
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
				Mockito.anyInt(), Mockito.matches("SITE_PROPERTIES")))
				.thenReturn(izoPcObjectCreator.getQuoteProductComponent());
		Mockito.when(
				quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn(izoPcObjectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(Mockito.anyInt(),
						 Mockito.matches("LOCAL_IT_CONTACT")))
				.thenReturn(izoPcObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentsAttributeValueRepository)
				.delete(new QuoteProductComponentsAttributeValue());
		Mockito.doThrow(new RuntimeException()).when(quoteProductComponentRepository)
				.delete(new QuoteProductComponent());
		Mockito.doThrow(new RuntimeException()).when(illSiteRepository).save(new QuoteIllSite());

		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(izoPcObjectCreator.getListOfQouteIllSites());

		when(quoteRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(izoPcObjectCreator.getQuote2()));

		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndType(
				Mockito.anyInt(), Mockito.anyString())).thenReturn((izoPcObjectCreator.getQuoteProductComponent()));
		
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(izoPcObjectCreator.getAddressDetail());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(izoPcObjectCreator.getUser());
		Mockito.when(customerRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getCustomer());
		Mockito.when(productAttributeMasterRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.of(izoPcObjectCreator.getProductAtrributeMas()));
		doNothing().when(siteFeasibilityRepository).delete(Mockito.any());
		doNothing().when(siteFeasibilityRepository).deleteAll(Mockito.any());
		
		
	}
	
	/**
	 * 
	 * createQuote - positive case
	 * 
	 * @throws TclCommonException
	 */

	@Test
	public void testCreateQuote() throws TclCommonException {
		
		mockCreateQuoteFlow(false,false,false,false,false);
		QuoteResponse response = izoPcQuoteService.createQuote(izoPcObjectCreator.getQuoteDetail_IZO(), 1);
		
		assertTrue(response.getQuoteId() !=null);
	}
	
	@Test
	public void testCreateQuoteForQuoteDetailWithoutQuoteId() throws TclCommonException {
		
		mockCreateQuoteFlow(false,false,false,false,false);
		QuoteResponse response = izoPcQuoteService.createQuote(izoPcObjectCreator.getQuoteDetail2_IZO(), 1);
		assertTrue(response!=null);
	}
	
	@Test
	public void testCreateQuoteForNullErfCust() throws TclCommonException {
		
		mockCreateQuoteFlow(false,false,false,false,false);
		QuoteResponse response = izoPcQuoteService.createQuote(izoPcObjectCreator.getQuoteDetail_IZO(), null);
		
		assertTrue(response.getQuoteId() !=null);
	}
	
	@Test
	public void testCreateQuoteForNullFamilySolnAttr() throws TclCommonException {
		
		mockCreateQuoteFlow(true,true,true,false,false);
		QuoteResponse response = izoPcQuoteService.createQuote(izoPcObjectCreator.getQuoteDetail_IZO(), null);
		
		assertTrue(response.getQuoteId() !=null);
	}
	
	
	@Test
	public void testCreateQuoteForNullSolnAttr() throws TclCommonException {
		
		mockCreateQuoteFlow(false,true,true,false,false);
		QuoteResponse response = izoPcQuoteService.createQuote(izoPcObjectCreator.getQuoteDetail_IZO(), null);
		
		assertTrue(response.getQuoteId() !=null);
	}
	
	@Test
	public void testCreateQuoteForNullAttr() throws TclCommonException {
		
		mockCreateQuoteFlow(false,false,true,false,false);
		QuoteResponse response = izoPcQuoteService.createQuote(izoPcObjectCreator.getQuoteDetail_IZO(), null);
		
		assertTrue(response.getQuoteId() !=null);
	}
	
	@Test
	public void testCreateQuoteForNullComp() throws TclCommonException {
		
		mockCreateQuoteFlow(false,false,false,true,false);
		QuoteResponse response = izoPcQuoteService.createQuote(izoPcObjectCreator.getQuoteDetail_IZO(), null);
		
		assertTrue(response.getQuoteId() !=null);
	}
	
	@Test(expected=Exception.class)
	public void testCreateQuoteForNullCust() throws TclCommonException {
		
		mockCreateQuoteFlow(false,false,false,false,true);
		izoPcQuoteService.createQuote(izoPcObjectCreator.getQuoteDetail_IZO(), 1);
		
	}
	
	@Test
	public void testCreateQuoteForQuoteDetailWithoutSite() throws TclCommonException {
		
		mockCreateQuoteFlow(false,false,false,false,false);
		QuoteResponse response = izoPcQuoteService.createQuote(izoPcObjectCreator.getQuoteDetailWithoutSite(), 1);
		
		assertTrue(response.getQuoteId() !=null);
	}
	
	/*
	 * test case to test null check for quote detail in createQuote
	 */
	@Test(expected=Exception.class)
	public void testCreateQuoteForNullQuoteDetail() throws TclCommonException {
		 izoPcQuoteService.createQuote(null, 1);

	}
	
	@Test
	public void testConstructIllSites() throws TclCommonException {
		izoPcQuoteService.constructIllSites(izoPcObjectCreator.getSolution(), izoPcObjectCreator.getUser(), 
				izoPcObjectCreator.getSitDetails().get(0), izoPcObjectCreator.getMstProductFamily());
		assertTrue(true);
	}
	
	@Test(expected=Exception.class)
	public void testConstructIllSitesForNullInput() throws TclCommonException {
		izoPcQuoteService.constructIllSites(null, izoPcObjectCreator.getUser(), 
				izoPcObjectCreator.getSitDetails().get(0), izoPcObjectCreator.getMstProductFamily());
	}
	
	public void mockCreateQuoteFlow(boolean isFamilyNull,boolean isSolnNull ,boolean isAttrMasterNull,boolean isCompNull,boolean isCustNull) throws TclCommonException {
		// mock getUserId
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(izoPcObjectCreator.getUser());
		
		//mockProcessQuote
		Mockito.when(customerRepository.findByErfCusCustomerIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(isCustNull?null:izoPcObjectCreator.getCustomer());
		Mockito.when(quoteRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getQuote());
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(izoPcObjectCreator.getQuote());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteToLe_IZO());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(izoPcObjectCreator.getQuoteToLe_IZO()));
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(izoPcObjectCreator.getMstProductFamily());
		Mockito.when(quoteToLeProductFamilyRepository
				.findByQuoteToLeAndMstProductFamily(Mockito.any(), Mockito.any())).thenReturn(isFamilyNull?null:izoPcObjectCreator.getQuoteToLeFamily());
		Mockito.when(quoteToLeProductFamilyRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteToLeProductFamily_IZO());
		Mockito.when(productSolutionRepository
				.findByQuoteToLeProductFamily(Mockito.any())).thenReturn(izoPcObjectCreator.getProductSolutionList());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null)).thenReturn(izoPcObjectCreator.getQuoteProductComponent());		
		doAnswer((i) -> {
			return null;
		}).when(quoteProductComponentRepository).delete(Mockito.any());
		doAnswer((i) -> {
			return null;
		}).when(quoteProductComponentsAttributeValueRepository).delete(Mockito.any());
		Mockito.doNothing().when(omsSfdcService).processDeleteProduct(Mockito.any(), Mockito.any());
		doAnswer((i) -> {
			return null;
		}).when(productSolutionRepository).delete(Mockito.any());
		Mockito.when(mstProductOfferingRepository.findByMstProductFamilyAndProductNameAndStatus(Mockito.any(),Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getMstProductOffering());
		Mockito.when(mstProductOfferingRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getMstProductOffering());
		Mockito.when(productSolutionRepository
				.findByQuoteToLeProductFamilyAndMstProductOffering(Mockito.any(),Mockito.any())).thenReturn(isSolnNull?null:izoPcObjectCreator.getProductSolutionList().get(0));
		Mockito.when(productSolutionRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.craeteproductSolutions());

		Mockito.doNothing().when(omsSfdcService).processProductServiceForSolution(Mockito.any(),Mockito.any(), Mockito.anyString());
		Mockito.when(illSiteRepository.findByProductSolutionAndStatus(
				Mockito.any(), Mockito.anyByte())).thenReturn(izoPcObjectCreator.getQuoteIllSiteList());		
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getIllsites());
		Mockito.when(illSiteRepository.findByIdAndStatus(
				Mockito.anyInt(), Mockito.anyByte())).thenReturn(izoPcObjectCreator.getIllsites());

		Mockito.when(mstProductComponentRepository
				.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte())).thenReturn(isCompNull?null:izoPcObjectCreator.getMstProductComponentList());
		Mockito.when(mstProductComponentRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getMstProductComponent());
		Mockito.when(quoteProductComponentRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.createQuoteProductComponent());
		Mockito.when(quoteProductComponentsAttributeValueRepository.saveAndFlush(Mockito.any())).thenReturn(izoPcObjectCreator.craeteproductComponentAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
			.thenReturn(isAttrMasterNull?null:izoPcObjectCreator.getProductAtrributeList());

		// mock persistQuoteLeAttributes
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getMstOmsAttribute());
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(izoPcObjectCreator.getMstAttributeList());

		Mockito.doNothing().when(omsSfdcService).processCreateOpty(Mockito.any(), Mockito.anyString());
	}
		
	@Test(expected=Exception.class)
	public void testGetQuoteForException() throws TclCommonException {
		izoPcQuoteService.getQuoteDetails(null, "ALL",false);
	}
	
	@Test
	public void testGetQuote() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(izoPcObjectCreator.getQuote());
		mockConstructQuote();
		QuoteBean response = izoPcQuoteService.getQuoteDetails(1, "ALL",false);
		assertTrue(response!=null);
	}
	
	public void mockConstructQuote() {
		mockConstructQuoteLeEntitDtos();
		Mockito.when(orderConfirmationAuditRepository.findByOrderRefUuid(Mockito.anyString())).thenReturn(izoPcObjectCreator.getOrderConfirmationAudit());
	}
	
	public void mockConstructQuoteLeEntitDtos() {
		
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
			.thenReturn(izoPcObjectCreator.getQuoteToLeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
			.thenReturn(izoPcObjectCreator.getQuoteToLeFamilyList());
	
		mockConstructQuoteToLeFamilyDtos();
	}
	
	public void mockConstructQuoteToLeFamilyDtos() {
		Mockito.when(productSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()))
		.thenReturn(izoPcObjectCreator.getProductSolutionList());
		mockConstructProductSolution();
	}
	
	public void mockConstructProductSolution() {
		Mockito.when(illSiteRepository.findByProductSolutionIdAndStatus(Mockito.anyInt(), Mockito.anyByte()))
			.thenReturn(izoPcObjectCreator.getIllsitesLists());
		mockConstructIllSiteDtos();
	}
	
	public void mockConstructIllSiteDtos() {
		Mockito.when(siteFeasibilityRepository.findByQuoteIllSiteAndIsSelected(Mockito.any(), Mockito.anyByte()))
			.thenReturn(izoPcObjectCreator.getSiteFeasibilities());
		mockConstructQuoteProductComponent();
		
	}
	
	public void mockConstructQuoteProductComponent() {
		
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name( Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(izoPcObjectCreator.getQuoteProductComponent());
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null))
				.thenReturn(izoPcObjectCreator.getQuoteProductComponent());
		Mockito.when(quotePriceRepository.findByReferenceNameAndReferenceId( Mockito.anyString(), Mockito.anyString()))
			.thenReturn(izoPcObjectCreator.getQuotePriceList());
		Mockito.when(quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_Id(Mockito.anyInt()))
				.thenReturn(izoPcObjectCreator.getquoteProductComponentAttributeValues());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(izoPcObjectCreator.getquoteProductComponentAttributeValues());
		mockConstructAttribute();
	}
	
	public void mockConstructAttribute() {
		Mockito.when(quotePriceRepository.findByReferenceIdAndReferenceName(  Mockito.anyString(),Mockito.anyString()))
		.thenReturn(izoPcObjectCreator.getQuotePrice());
	}
	
	
	/**
	 * for testing testeditSites site - positive test case
	 * 
	 * @throws Exception
	 */
	@Test
	public void testeditSites() throws Exception {
		QuoteDetail response = izoPcQuoteService.editSiteComponent(izoPcObjectCreator.getUpdateRequest());
		assertTrue(response != null );
	}

	@Test
	public void testeditSitesUpdateCpe() throws Exception {
		Mockito.when(illSiteRepository.findById(Mockito.any()))
				.thenReturn((Optional.of(izoPcObjectCreator.getQuoteIllSite())));
		QuoteDetail response = izoPcQuoteService.editSiteComponent(izoPcObjectCreator.getUpdateRequest1());
		assertTrue(response != null );
	}

	@Test
	public void testeditSitesUpdateCpeChange() throws Exception {
		Mockito.when(illSiteRepository.findById(Mockito.any()))
				.thenReturn((Optional.of(izoPcObjectCreator.getQuoteIllSite())));
		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(
				Mockito.anyInt(), Mockito.matches("CPE")))
				.thenReturn(izoPcObjectCreator.getQuoteProductComponent());
		QuoteDetail response = izoPcQuoteService.editSiteComponent(izoPcObjectCreator.getUpdateRequest3());
		assertTrue(response != null );
	}

	/**
	 * for testing testeditSites site - negative test case
	 * 
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testeditSitesNegativeCase() throws Exception {
		 izoPcQuoteService.editSiteComponent(null);
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
		Mockito.when(quoteProductComponentRepository.save(izoPcObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(izoPcObjectCreator.craeteQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getProductAtrributeMaster());
		Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getProductAtrributeMaster());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn((izoPcObjectCreator.getMstProductComponentList()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getAttribute());
		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(),Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getMstProductFamily());
		QuoteDetail response = izoPcQuoteService.updateSiteProperties(izoPcObjectCreator.getUpdateRequest());
		assertTrue(response!=null);
	}

	/**
	 * negative case for testupdateSiteProperties
	 * 
	 * testupdateSiteProperties
	 * 
	 * @throws TclCommonException
	 */

	@Test(expected=Exception.class)
	public void testupdateSitePropertiesForException() throws TclCommonException {
		Mockito.when(quoteProductComponentRepository.save(izoPcObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(izoPcObjectCreator.craeteQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getProductAtrributeMaster());
		Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getProductAtrributeMaster());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn((izoPcObjectCreator.getMstProductComponentList()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getAttribute());
		 izoPcQuoteService.updateSiteProperties( null);
	}

	/**
	 * negative case for testupdateSiteProperties
	 * 
	 * testupdateSiteProperties
	 * 
	 * @throws TclCommonException
	 */

	@Test(expected=Exception.class)
	public void testupdateSitePropertiesForInvalidRequest() throws TclCommonException {
		Mockito.when(quoteProductComponentRepository.save(izoPcObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(izoPcObjectCreator.craeteQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getProductAtrributeMaster());
		Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getProductAtrributeMaster());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn((izoPcObjectCreator.getMstProductComponentList()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getAttribute());

		UpdateRequest request = izoPcObjectCreator.getUpdateRequest();
		request.setSiteId(null);
		izoPcQuoteService.updateSiteProperties(request);
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
		Mockito.when(quoteProductComponentRepository.save(izoPcObjectCreator.craeteQuoteProductComponent()))
				.thenReturn(izoPcObjectCreator.craeteQuoteProductComponent());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		Mockito.when(productAttributeMasterRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getProductAtrributeMas());
		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn((izoPcObjectCreator.getMstProductComponentList()));
		Mockito.when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(new QuoteProductComponentsAttributeValue());
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(null);
		QuoteDetail response = izoPcQuoteService.updateSiteProperties(izoPcObjectCreator.getUpdateRequest());
		assertTrue(response != null );
	}
	
	/**
	 * Method to test processMailAttachment method
	 * @throws TclCommonException
	 */
	@Test
	public void testProcessMailAttachment() throws TclCommonException {
		Mockito.when(quoteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(izoPcObjectCreator.getQuote());
		mockConstructQuote();
		ServiceResponse responseResource = izoPcQuoteService.processMailAttachment( "izopc@gmail.com",1);
		assertTrue(responseResource != null);

	}

	/**
	 * negative test case for processMailAttachment method
	 * @throws TclCommonException
	 */
	@Test(expected=Exception.class)
	public void testProcessMailAttachmentForNull() throws TclCommonException {
		 izoPcQuoteService.processMailAttachment(null,null);

	}
	
	/**
	 * negative test case for processMailAttachment method
	 * @throws TclCommonException
	 */
	@Test(expected=Exception.class)
	public void testProcessMailAttachmentForException() throws TclCommonException {
		Mockito.when(notificationService.processShareQuoteNotification(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyString()))
					.thenThrow(RuntimeException.class);
		 izoPcQuoteService.processMailAttachment( "izopc@gmail.com",1);

	}
	

	@Test
	public void testGetSiteProperties() throws TclCommonException {
		when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(izoPcObjectCreator.getIllsites());
		when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(Mockito.anyInt(),Mockito.anyString())).thenReturn(izoPcObjectCreator.getQuoteProductComponent());
		when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null)).thenReturn(izoPcObjectCreator.getQuoteProductComponent());
		
		List<QuoteProductComponentBean> response = izoPcQuoteService.getSiteProperties(9);
		assertTrue(response != null );
	}
	
	@Test(expected=TclCommonException.class)
	public void testGetSitePropertiesWithNullSiteId() throws TclCommonException {
		when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(izoPcObjectCreator.getIllsites());
		when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(Mockito.anyInt(),Mockito.anyString())).thenReturn(izoPcObjectCreator.getQuoteProductComponent());
		when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null)).thenReturn(izoPcObjectCreator.getQuoteProductComponent());
		List<QuoteProductComponentBean> response = izoPcQuoteService.getSiteProperties(null);
	}
	
	@Test
	public void testGetSitePropertiesWithWrongSiteId() throws TclCommonException {
		when(illSiteRepository.findByIdAndStatus(Mockito.anyInt(), Mockito.anyByte())).thenReturn(izoPcObjectCreator.getIllsites());
		when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_Name(Mockito.anyInt(),Mockito.anyString())).thenReturn(null);
		when(quoteProductComponentRepository.findByReferenceIdAndReferenceName(Mockito.anyInt(),null)).thenReturn(null);
		List<QuoteProductComponentBean> response = izoPcQuoteService.getSiteProperties(6);
		
		assertTrue(response.isEmpty());
	}

	/*
	 * Positive test case for updateCurrency
	 * 
	 */
	@Test
	public void testUpdateCurrency() throws TclCommonException {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(izoPcObjectCreator.getQuotePriceList());
		Mockito.when(
				currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(izoPcObjectCreator.getCurrencyConversionRate()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(izoPcObjectCreator.getOptionalQuoteToLeWithSites());
		Mockito.when(mstOmsAttributeRepository
				.findByNameAndIsActive(Mockito.anyString(),Mockito.anyByte())).thenReturn(izoPcObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository
				.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when( quoteToLeRepository.findByQuote(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt())).thenReturn(izoPcObjectCreator.getQuotePriceList());
		Mockito.when(quoteRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getQuote());
		Mockito.when(quoteToLeRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteToLe());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(illSiteRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getQuoteIllSite());
		Mockito.when(quotePriceRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getQuotePrice());

		 izoPcQuoteService.updateCurrency(679, 678, "USD");
		assertTrue(true);
	}

	/*
	 * Negative test case for updateCurrency
	 * 
	 */
	@Test(expected=Exception.class)
	public void testUpdateCurrencyForNull() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(izoPcObjectCreator.getQuotePriceList());
		Mockito.when(
				currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(izoPcObjectCreator.getCurrencyConversionRate()));

		izoPcQuoteService.updateCurrency(null, null, null);
	}

	/*
	 * Negative test case for updateCurrency
	 * 
	 */
	@Test
	public void testUpdateCurrencyForException() throws TclCommonException {

		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteLeAttributeValueList());
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(izoPcObjectCreator.getQuotePriceList());
		Mockito.when(
				currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(izoPcObjectCreator.getCurrencyConversionRate()));
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(null));
		 izoPcQuoteService.updateCurrency(679, 678, "USD");
		
		assertTrue(true);
	}

	/*
	 * Negative test case for updateCurrency
	 * 
	 */
	@Test
	public void testUpdateCurrencyForInputCurrencyForException1() throws TclCommonException {
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(izoPcObjectCreator.getMstAttributeList());
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(null);
		Mockito.when(quoteToLeRepository.findByQuote(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteToLeList());
		Mockito.when(quotePriceRepository.findByQuoteId(Mockito.anyInt()))
				.thenReturn(izoPcObjectCreator.getQuotePriceList());
		Mockito.when(
				currencyConversionRepository.findByOutputCurrencyAndStatus(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.ofNullable(izoPcObjectCreator.getCurrencyConversionRate()));

		izoPcQuoteService.updateCurrency(679, 678, "USD");
		assertTrue(true);
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
				.thenReturn(Optional.ofNullable(izoPcObjectCreator.getQuoteToLe()));
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(quoteToLeRepository.save(new QuoteToLe())).thenReturn(izoPcObjectCreator.getQuoteToLe_IZO());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(izoPcObjectCreator.craeteUser());
		QuoteDetail response = izoPcQuoteService.updateLegalEntityProperties(izoPcObjectCreator.getUpdateRequest());
		assertTrue(response != null);

		
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
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
		.thenReturn(Optional.ofNullable(izoPcObjectCreator.getQuoteToLe()));
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(new ArrayList<>());
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(izoPcObjectCreator.craeteUser());
		QuoteDetail response = izoPcQuoteService.updateLegalEntityProperties(izoPcObjectCreator.getUpdateRequest());
		assertTrue(response!=null);
	}

	/**
	 * negative case for testupdateLegalEntityProperties
	 * 
	 * testupdateLegalEntityProperties
	 * 
	 * @throws TclCommonException
	 */
	@Test(expected=Exception.class)
	public void testupdateLegalEntityPropertiesForException() throws TclCommonException {

		/*
		 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
		 * anyString(), Mockito.anyByte()))
		 * .thenReturn(izoPcObjectCreator.getMstAttributeList());
		 */

		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteLeAttributeValue());

		 izoPcQuoteService.updateLegalEntityProperties(null);
	}

	/**
	 * negative case for testupdateLegalEntityProperties
	 * 
	 * testupdateLegalEntityProperties
	 * 
	 * @throws TclCommonException
	 */
	@Test(expected=Exception.class)
	public void testUpdateLegalEntityPropertiesForQuote() throws TclCommonException {

		/*
		 * Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.
		 * anyString(), Mockito.anyByte()))
		 * .thenReturn(izoPcObjectCreator.getMstAttributeList());
		 */

		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

		UpdateRequest request = izoPcObjectCreator.getUpdateRequest();
		izoPcQuoteService.updateLegalEntityProperties(request);
	}
	
	/**
	 * negative case for testupdateLegalEntityProperties
	 * 
	 * testupdateLegalEntityProperties
	 * 
	 * @throws TclCommonException
	 */
	@Test(expected=Exception.class)
	public void testUpdateLegalEntityPropertiesForUserNull() throws TclCommonException {
		Mockito.when(quoteToLeRepository.findById(Mockito.anyInt()))
				.thenReturn(Optional.ofNullable(izoPcObjectCreator.getQuoteToLe()));
		Mockito.when(mstOmsAttributeRepository.save(Mockito.any())).thenReturn(izoPcObjectCreator.getMstOmsAttribute());
		Mockito.when(quoteLeAttributeValueRepository.save(Mockito.any()))
				.thenReturn(izoPcObjectCreator.getQuoteLeAttributeValue());
		Mockito.when(quoteToLeRepository.save(new QuoteToLe())).thenReturn(izoPcObjectCreator.getQuoteToLe_IZO());
		Mockito.when(userRepository.findByUsernameAndStatus(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
		izoPcQuoteService.updateLegalEntityProperties(izoPcObjectCreator.getUpdateRequest());

		
	}


}
