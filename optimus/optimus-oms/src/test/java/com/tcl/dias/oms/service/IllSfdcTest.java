package com.tcl.dias.oms.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.SfdcJob;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.MstProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.OrderProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.OrdersLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeProductFamilyRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.SfdcJobRepository;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IllSfdcTest.java class. This class contains all the
 * test cases for the IllSfdcTest
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
public class IllSfdcTest {

	@Autowired
	private ObjectCreator quoteObjectCreator;

	@Autowired
	OmsSfdcService omsSfdcService;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@MockBean
	OrdersLeAttributeValueRepository ordersLeAttributeValueRepository;

	@MockBean
	ProductSolutionRepository quoteProductSolutionRepository;

	@MockBean
	QuoteToLeRepository quoteToLeRepository;

	@MockBean
	IllSiteRepository illSitesRepository;

	@MockBean
	QuoteRepository quoteRepository;

	@MockBean
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@MockBean
	QuoteToLeProductFamilyRepository quoteToLeProductFamilyRepository;

	@MockBean
	MstProductComponentRepository mstProductComponentRepository;

	@MockBean
	OrderProductComponentRepository orderProductComponentRepository;

	@MockBean
	QuoteProductComponentRepository quoteProductComponentRepository;

	@MockBean
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;

	@MockBean
	ProductAttributeMasterRepository productAttributeMasterRepository;

	@MockBean
	MstProductFamilyRepository mstProductFamilyRepository;

	@MockBean
	SfdcJobRepository sfdcJobRepository;

	@MockBean
	OrderToLeRepository orderToLeRepository;

	@Before
	public void init() throws TclCommonException {

		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());

		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn((quoteObjectCreator.getQuoteLeAttributeValueList()));

		Mockito.when(mstOmsAttributeRepository.findById(Mockito.anyInt()))
				.thenReturn((Optional.of(quoteObjectCreator.getMstAttribute())));

		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());

		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteLeAttributeValueList());

		Mockito.when(quoteToLeProductFamilyRepository.findByQuoteToLe(Mockito.any()))
				.thenReturn(quoteObjectCreator.getQuoteToLeFamilyList());

		Mockito.when(quoteProductSolutionRepository.findByQuoteToLeProductFamily(Mockito.any()
				)).thenReturn(quoteObjectCreator.createProductSolutions());

		Mockito.when(illSitesRepository.findByProductSolutionAndStatus(Mockito.any(),
				Mockito.anyByte())).thenReturn(quoteObjectCreator.getListOfQouteIllSites());

		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getAddressDetail());

		Mockito.when(quoteToLeRepository.findByQuote_QuoteCode(Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());

		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstAttributeList());

		Mockito.when(quoteToLeRepository.findByTpsSfdcOptyId(Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getQuoteToLeList());

		Mockito.when(mstProductFamilyRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.geProductFamily());

		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getProductAtrributeMaster());

		Mockito.when(illSitesRepository.findBySiteCodeAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getListOfQouteIllSites());

		Mockito.when(quoteProductComponentRepository.findByReferenceIdAndMstProductComponent(Mockito.anyInt(),
				Mockito.any())).thenReturn(quoteObjectCreator.getQuoteProductComponent());

		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any()))
				.thenReturn(quoteObjectCreator.createQuoteProductAttributeValue());

		Mockito.when(mstProductComponentRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(quoteObjectCreator.getMstProductComponentList());

		Mockito.when(orderToLeRepository.findByTpsSfdcCopfId(Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getOrderToLesList());

		when(productAttributeMasterRepository.save(Mockito.any())).thenReturn(new ProductAttributeMaster());
		when(quoteProductComponentRepository.save(Mockito.any())).thenReturn(new QuoteProductComponent());
		when(quoteLeAttributeValueRepository.save(Mockito.any())).thenReturn(new QuoteLeAttributeValue());
		when(quoteProductComponentsAttributeValueRepository.save(Mockito.any()))
				.thenReturn(new QuoteProductComponentsAttributeValue());

		mock(UserInformation.class);
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(quoteObjectCreator.getUserInformation());

	}

	/*
	 * 
	 * Positive test case for testprocessCreateOpty
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessCreateOpty() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.eq("1")))
				.thenReturn(quoteObjectCreator.getCustomerDetailsBean());
		omsSfdcService.processCreateOpty(quoteObjectCreator.getQuoteToLe(),Mockito.anyString());
	}

	/*
	 * 
	 * Positive test case for testprocessProductServices
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessProductServices() throws TclCommonException {
		omsSfdcService.processProductServices(quoteObjectCreator.getQuoteToLe(),quoteObjectCreator.getQuoteToLe().getTpsSfdcOptyId());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessSiteDetails() throws TclCommonException {
		omsSfdcService.processSiteDetails(quoteObjectCreator.getQuoteToLe());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessUpdateOpportunity() throws TclCommonException {
		omsSfdcService.processUpdateOpportunity(new Date(), "1", "Closed Won – COF Received",
				quoteObjectCreator.getQuoteToLe());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testtprocessUpdateOpportunity() throws TclCommonException {
		omsSfdcService.processUpdateOpportunity(new Date(), "1", "Closed Won – Order Processing",
				quoteObjectCreator.getQuoteToLe());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessUpdateOrderOpportunity() throws TclCommonException {
		omsSfdcService.processUpdateOrderOpportunity(new Date(), "1", "Closed Won – COF Received",
				quoteObjectCreator.getOrderToLes());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testtprocessUpdateOrderOpportunity() throws TclCommonException {
		omsSfdcService.processUpdateOrderOpportunity(new Date(), "1", "Closed Won – Order Processing",
				quoteObjectCreator.getOrderToLes());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessSfdcOpportunityCreateResponse() throws TclCommonException {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(new ArrayList<>());
		List<MstOmsAttribute> list = new ArrayList();
		list.add(null);
		Mockito.when(mstOmsAttributeRepository.findByNameAndIsActive(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(list);
		omsSfdcService.processSfdcOpportunityCreateResponse(quoteObjectCreator.createOpportunityResponseBean());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessSfdcProductService() throws TclCommonException {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(new ArrayList<>());
		omsSfdcService.processSfdcProductService(quoteObjectCreator.getProductServicesResponseBean());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessSfdcUpdateOptyForEmpty() throws TclCommonException {
		Mockito.when(quoteLeAttributeValueRepository.findByQuoteToLeAndMstOmsAttribute(Mockito.any(), Mockito.any()))
				.thenReturn(new ArrayList<>());
		omsSfdcService.processSfdcUpdateOpty(quoteObjectCreator.getStagingResponseBean());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessSfdcUpdateOpty() throws TclCommonException {
		omsSfdcService.processSfdcUpdateOpty(quoteObjectCreator.getStagingResponseBean());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessSfdcSites() throws TclCommonException {
		omsSfdcService.processSfdcSites(quoteObjectCreator.getSiteResponseBean());
	}

	/*
	 * 
	 * Positive test case for testprocessSiteDetails
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessSfdcSitesForEmpty() throws TclCommonException {
		Mockito.when(productAttributeMasterRepository.findByNameAndStatus(Mockito.anyString(), Mockito.anyByte()))
				.thenReturn(new ArrayList<>());
		Mockito.when(quoteProductComponentsAttributeValueRepository
				.findByQuoteProductComponentAndProductAttributeMaster(Mockito.any(), Mockito.any()))
				.thenReturn(new ArrayList<>());
		omsSfdcService.processSfdcSites(quoteObjectCreator.getSiteResponseBean());
	}

	/*
	 * Positive test case for testprocessDeleteProduct
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testprocessDeleteProduct() throws TclCommonException {
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(new SfdcJob());
		omsSfdcService.processDeleteProduct(quoteObjectCreator.getQuoteToLe(), quoteObjectCreator.getSolution());
	}

	/*
	 * Positive test case for testprocessDeleteProduct
	 * 
	 * input param : QuoteToLe
	 * 
	 */

	@Test
	public void testtprocessDeleteProduct() throws TclCommonException {
		Mockito.when(sfdcJobRepository.save(Mockito.any())).thenReturn(new SfdcJob());
		omsSfdcService.processDeleteProduct(quoteObjectCreator.getQuoteToLe(), quoteObjectCreator.getSolution1());
	}

	/*
	 * Positive test case for processProductServiceForSolution
	 */
	@Test
	public void testProcessProductServiceForSolution() throws TclCommonException {
		Mockito.when(quoteProductSolutionRepository.save(Mockito.any())).thenReturn(new ProductSolution());
		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		omsSfdcService.processProductServiceForSolution(quoteObjectCreator.getQuoteToLe(),
				quoteObjectCreator.getSolution2(),quoteObjectCreator.getQuoteToLe().getTpsSfdcOptyId());
	}

	/*
	 * Positive test case for processSfdcProductService
	 */
	@Test
	public void testProcessSfdcProductService() throws TclCommonException {
		Mockito.when(quoteProductSolutionRepository.findBySolutionCode(Mockito.anyString()))
				.thenReturn(quoteObjectCreator.createProductSolutions());
		Mockito.when(quoteProductSolutionRepository.save(Mockito.any())).thenReturn(new ProductSolution());
		Mockito.when(sfdcJobRepository.findByCodeAndType(Mockito.anyString(), Mockito.eq("PRODUCT")))
				.thenReturn(quoteObjectCreator.getSfdcJobList());
		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		doNothing().when(sfdcJobRepository).delete(Mockito.any());
		Mockito.when(sfdcJobRepository.findByCodeAndType(Mockito.anyString(),Mockito.eq("SITE")))
		.thenReturn(quoteObjectCreator.getSfdcJobList2());
		
		omsSfdcService.processSfdcProductService(quoteObjectCreator.getProductServicesResponseBean());
	}

	/*
	 * Positive test case for processSfdcProductService
	 */
	@Test
	public void testProcessSfdcProductServiceForNullProductSolution() throws TclCommonException {
		Mockito.when(quoteProductSolutionRepository.findBySolutionCode(Mockito.anyString()))
				.thenReturn(Collections.EMPTY_LIST);
		Mockito.when(sfdcJobRepository.findByCodeAndType(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(quoteObjectCreator.getSfdcJobList());
		
		doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		doNothing().when(sfdcJobRepository).delete(Mockito.any());

		omsSfdcService.processSfdcProductService(quoteObjectCreator.getProductServicesResponseBean());
	}
}
