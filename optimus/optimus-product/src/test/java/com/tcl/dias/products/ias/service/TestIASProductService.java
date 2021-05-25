package com.tcl.dias.products.ias.service;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
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

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixIAS;
import com.tcl.dias.productcatelog.entity.repository.IasPriceBookRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductCompAssnRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductFamilyRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductServiceAreaMatrixIASRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixIASRepository;
import com.tcl.dias.productcatelog.entity.repository.IasSLAViewRepository;
import com.tcl.dias.products.constants.SLAParameters;
import com.tcl.dias.products.constants.ServiceVariants;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.ias.service.v1.IASProductService;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the test cases for IASProductsService.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestIASProductService {

	@Autowired
	IASProductService iasProductService;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	ProductRepository productRepository;

	@MockBean
	ProductFamilyRepository prodFamilyRepository;

	@MockBean
	ProductCompAssnRepository prodCompAssnRepository;

	@MockBean
	IasPriceBookRepository iasPriceBookRepository;

	@MockBean
	ProductServiceAreaMatrixIASRepository productServiceAreaMatrixIASRepository;

	@MockBean
	IasSLAViewRepository slaViewRepository;

	@MockBean
	List<SLADto> list;
	
	@MockBean
	ServiceAreaMatrixIASRepository serviceAreaMatrixIASRepository;

	
	/*
	 * Positive Test case - for retrieving pricing details
	 * 
	 *
	 */
	@Test
	public void testGetIasPricingDetails() throws Exception {
		Mockito.when(iasPriceBookRepository.findAll()).thenReturn(objectCreator.createIasPriceBookList());
		List<?> iasPriceList = iasProductService.getPrice();
		assertFalse("Pricing details not available", iasPriceList == null || iasPriceList.isEmpty());
	}
	
	/*
	 * negative test case to test the service method to retrieve details of a product
	 * offering
	 * 
	 * @author Vinod
	 */
	@Test(expected = Exception.class)
	public void testGetPricingDetailsForException() throws Exception {
		Mockito.when(iasPriceBookRepository.findAll()).thenThrow(Exception.class);
		iasProductService.getPrice();
	}

	/*
	 * test case to get SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected=TclCommonException.class)
	public void testgetSlaValueForStandard() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createServiceMatrixIAS()));
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientId(Mockito.anyInt(),
		// Mockito.anyInt(), Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(
		// Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository
		// .findByFactorGroupIdIn(objectCreator.createFactorGroupList(ServiceVariants.STANDARD.getId())))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));

		List<SLADto> SLADtoList = iasProductService.getSlaValue(1, ServiceVariants.STANDARD.getId(), 1, 1, 1);
		assertFalse("sla details not available", SLADtoList == null || SLADtoList.isEmpty() || SLADtoList.size() < 3);

	}
	/*
	 * test case to get SLA details
	 * 
	 * 
	 */
	@Test(expected=TclCommonException.class)
	public void testgetSlaValueForPremiumn() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createServiceMatrixIAS()));
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientId(Mockito.anyInt(),
		// Mockito.anyInt(), Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(
		// Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository
		// .findByFactorGroupIdIn(objectCreator.createFactorGroupList(ServiceVariants.STANDARD.getId())))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));

		List<SLADto> SLADtoList = iasProductService.getSlaValue(1, ServiceVariants.PREMIUM.getId(), 1, 1, 1);
		assertFalse("sla details not available", SLADtoList == null || SLADtoList.isEmpty() || SLADtoList.size() < 3);

	}

	/*
	 * test case to get SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected=TclCommonException.class)
	public void testgetSlaValueForCompressed() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createServiceMatrixIAS()));
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientId(Mockito.anyInt(),
		// Mockito.anyInt(), Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.COMPRESSED.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(
		// Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.COMPRESSED.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository
		// .findByFactorGroupIdIn(objectCreator.createFactorGroupList(ServiceVariants.COMPRESSED.getId())))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.COMPRESSED.getId(),SLAParameters.NETWORK_UP_TIME.getId()));

		List<SLADto> SLADtoList = iasProductService.getSlaValue(1, ServiceVariants.COMPRESSED.getId(), 1, 1, 1);
		assertFalse("sla details not available", SLADtoList == null || SLADtoList.isEmpty() || SLADtoList.size() < 3);

	}
	
	/*
	 * test case to get SLA details
	 * 
	 * 
	 */
	@Test(expected=TclCommonException.class)
	public void testgetSlaValueForEnhanced() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createServiceMatrixIAS()));
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientId(Mockito.anyInt(),
		// Mockito.anyInt(), Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.COMPRESSED.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(
		// Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.COMPRESSED.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository
		// .findByFactorGroupIdIn(objectCreator.createFactorGroupList(ServiceVariants.COMPRESSED.getId())))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.COMPRESSED.getId(),SLAParameters.NETWORK_UP_TIME.getId()));

		List<SLADto> SLADtoList = iasProductService.getSlaValue(1, ServiceVariants.ENHANCED.getId(), 1, 1, 1);
		assertFalse("sla details not available", SLADtoList == null || SLADtoList.isEmpty() || SLADtoList.size() < 3);

	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForException() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenThrow(Exception.class);
		iasProductService.getSlaValue(1, 1, 1, 1, 1);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForNullValues() throws Exception {
		iasProductService.getSlaValue(null, null, null, null, null);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForNullValueAtPos1() throws Exception {
		iasProductService.getSlaValue(null, 1, 1, 1, 1);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForNullValueAtPos2() throws Exception {
		iasProductService.getSlaValue(1, null, 1, 1, 1);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForNullValueAtPos3() throws Exception {
		iasProductService.getSlaValue(1, 1, null, 1, 1);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForNullValueAtPos4() throws Exception {
		iasProductService.getSlaValue(1, 1, 1, null, 1);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForNullValueAtPos5() throws Exception {
		iasProductService.getSlaValue(1, 1, 1, 1, null);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForInvalidInput() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(999)).thenThrow(Exception.class);
		iasProductService.getSlaValue(9999, 9999, 9999, 9999, 9999);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForInvalidInputAtPos1() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(999)).thenThrow(Exception.class);
		iasProductService.getSlaValue(9999, 1, 1, 1, 1);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForInvalidInputAtPos2() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(999)).thenThrow(Exception.class);
		iasProductService.getSlaValue(1, 9999, 1, 1, 1);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForInvalidInputAtPos3() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(999)).thenThrow(Exception.class);
		iasProductService.getSlaValue(1, 1, 9999, 1, 1);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForInvalidInputAtPos4() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(999)).thenThrow(Exception.class);
		iasProductService.getSlaValue(1, 1, 1, 9999, 1);
	}

	/*
	 * negative Test case - for retrieving SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testgetSlaValueForInvalidInputAtPos5() throws Exception {

		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(999)).thenThrow(Exception.class);
		iasProductService.getSlaValue(1, 1, 1, 1, 9999);
	}

	

	/*
	 * negative test case to test getSLABasedOnTwoFactors method
	 * 
	 * @author Dinahar V
	 */
	@Test(expected = Exception.class)
	public void testGetSLABasedOnTwoFactors() throws Exception {
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt()))
		// .thenReturn(objectCreator.createEmptySLAViewList());
		iasProductService.getSLABasedOnTwoFactors(1, 1, 1, 1, 1, new ArrayList<SLADto>());
	}

	/*
	 * negative test case to test getSLABasedOnTwoFactors method
	 * 
	 * @author Dinahar V
	 */
	@Test(expected = Exception.class)
	public void testGetSLABasedOnTwoFactors1() throws Exception {
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository
		// .findByFactorGroupIdIn(objectCreator.createFactorGroupList(ServiceVariants.STANDARD.getId())))
		// .thenReturn(objectCreator.createEmptySLAViewList());

		iasProductService.getSLABasedOnTwoFactors(1, 1, 1, 1, 1, new ArrayList<SLADto>());
	}

	/*
	 * negative test case to test getSLABasedOnTwoFactors method
	 * 
	 * @author Dinahar V
	 */
	@Test(expected = Exception.class)
	public void testGetSLABasedOnTwoFactors2() throws Exception {
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt()))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.STANDARD.getId(),SLAParameters.NETWORK_UP_TIME.getId()));
		// Mockito.when(slaViewRepository
		// .findByFactorGroupIdIn(objectCreator.createFactorGroupList(ServiceVariants.STANDARD.getId())))
		// .thenReturn(objectCreator.createSLAViewList(ServiceVariants.COMPRESSED.getId(),SLAParameters.NETWORK_UP_TIME.getId()));

		iasProductService.getSLABasedOnTwoFactors(1, 1, 1, 1, 1, new ArrayList<SLADto>());
	}

	/*
	 * negative test case to get SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForNoServiceMatrixMapping() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenReturn(Optional.empty());
		iasProductService.getSlaValue(1, ServiceVariants.STANDARD.getId(), 1, 1, 1);
	}

	/*
	 * negative test case to get SLA details
	 * 
	 * @author Dinahar Vivekanandan
	 */
	@Test(expected = Exception.class)
	public void testGetSlaValueForEmptyPktDrop() throws Exception {
		Mockito.when(productServiceAreaMatrixIASRepository.findByLocationId(Mockito.anyInt()))
				.thenReturn(Optional.of(objectCreator.createServiceMatrixIAS()));
		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientId(Mockito.anyInt(),
		// Mockito.anyInt(),
		// Mockito.anyInt())).thenReturn(objectCreator.createEmptySLAViewList());
		// iasProductService.getSlaValue(1, ServiceVariants.STANDARD.getId(), 1, 1, 1);
	}

//	/*
//	 * negative test case to get SLA based on single factor
//	 * 
//	 * @author Dinahar Vivekanandan
//	 */
//	@Test(expected = Exception.class)
//	public void testGetSLABasedOnSingleFactorForEmptyResult() throws Exception {
//		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(Mockito.anyInt(),
//		// Mockito.anyInt(), Mockito.anyInt(),
//		// Mockito.anyInt())).thenReturn(objectCreator.createEmptySLAViewList());
//		// iasProductService.getSLABasedOnSingleFactor(1,
//		// ServiceVariants.STANDARD.getId(), 1, 1, Lists.emptyList());
//	}

//	/*
//	 * negative test case to get SLA based on single factor
//	 * 
//	 * @author Dinahar Vivekanandan
//	 */
//	@Test(expected = Exception.class)
//	public void testGetSLABasedOnSingleFactorForException() throws Exception {
//		// Mockito.when(slaViewRepository.findByProductOfferingIdAndSlaIdAndServiceVarientIdAndFactorValueId(Mockito.anyInt(),
//		// Mockito.anyInt(), Mockito.anyInt(),
//		// Mockito.anyInt())).thenThrow(Exception.class);
//		// iasProductService.getSLABasedOnSingleFactor(1,
//		// ServiceVariants.STANDARD.getId(), 1, 1,Lists.emptyList());
//	}

	/*
	 * positive test case to get SLA based on tier
	 * 
	 * @author Anne Nisha
	 */
	@Test
	public void testGetProcessProductSlaForTire1() throws Exception {
		Mockito.when(slaViewRepository.findBySlaIdAndFactorValue(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getProductSlaBean());
		Mockito.when(slaViewRepository.findBySlaIdAndSltVariant(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getProductSlaBean());
		ProductSlaBean pBean = iasProductService.processProductSla("1,factor,process");
		assertTrue(pBean != nullValue());

	}
	/*
	 * positive test case to get SLA based on tier
	 * 
	 * @author Anne Nisha
	 */
	@Test
	public void testGetProcessProductSlaForTire2() throws Exception {
		Mockito.when(slaViewRepository.findBySlaIdAndFactorValue(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getProductSlaBean());
		Mockito.when(slaViewRepository.findBySlaIdAndSltVariant(Mockito.anyInt(), Mockito.anyString()))
		.thenReturn(objectCreator.getProductSlaBean());
		ProductSlaBean pBean = iasProductService.processProductSla("2,factor,process");
		assertTrue(pBean != nullValue());

	}
	
	/*
	 * positive test case to get SLA based on tier
	 * 
	 * @author Anne Nisha
	 */
	@Test
	public void testGetProcessProductSlaForTire3() throws Exception {
		Mockito.when(slaViewRepository.findBySlaIdAndFactorValue(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(objectCreator.getProductSlaBean());
		Mockito.when(slaViewRepository.findBySlaIdAndSltVariant(Mockito.anyInt(), Mockito.anyString()))
		  		.thenReturn(objectCreator.getProductSlaBean());
		ProductSlaBean pBean = iasProductService.processProductSla("3,factor,process");
		assertTrue(pBean != nullValue());

	}
	/*
	 * negative test case to get SLA based on tier
	 * 
	 * @author Anne Nisha
	 */
	@Test(expected = Exception.class)
	public void testGetProcessProductSlaNegativeTestCase() throws Exception {
		Mockito.when(slaViewRepository.findBySlaIdAndFactorValueId(Mockito.anyInt(), Mockito.anyInt()))
				.thenThrow(Exception.class);
		ProductSlaBean pBean = iasProductService.processProductSla("1");

	}
	
	/*
	 * positive test case to get SLA with city based on tier
	 * 
	 * 
	 */
	@Test
	public void testprocessProductSlaWithCityForTire1() throws Exception {
		List<ServiceAreaMatrixIAS> serviceAreaMatrixIASList=new ArrayList<>();
		serviceAreaMatrixIASList.add(objectCreator.getServiceAreaMatrixIAS(1));
		Mockito.when(serviceAreaMatrixIASRepository.findByCityDtl( Mockito.anyString()))
				.thenReturn(serviceAreaMatrixIASList);
		ProductSlaBean pBean = iasProductService.processProductSlaWithCity("1,factor,process");
		assertTrue(pBean != null);
	}
	
	/*
	 * positive test case to get SLA with city based on tier
	 * 
	 * 
	 */
	@Test
	public void testprocessProductSlaWithCityForTire2() throws Exception {
		List<ServiceAreaMatrixIAS> serviceAreaMatrixIASList=new ArrayList<>();
		serviceAreaMatrixIASList.add(objectCreator.getServiceAreaMatrixIAS(2));
		Mockito.when(serviceAreaMatrixIASRepository.findByCityDtl( Mockito.anyString()))
				.thenReturn(serviceAreaMatrixIASList);
		ProductSlaBean pBean = iasProductService.processProductSlaWithCity("2,factor,process");
		assertTrue(pBean != nullValue());
	}
	
	/*
	 * positive test case to get SLA with city based on tier
	 * 
	 * 
	 */
	@Test
	public void testprocessProductSlaWithCityForTire3() throws Exception {
		List<ServiceAreaMatrixIAS> serviceAreaMatrixIASList=new ArrayList<>();
		serviceAreaMatrixIASList.add(objectCreator.getServiceAreaMatrixIAS(3));
		Mockito.when(serviceAreaMatrixIASRepository.findByCityDtl( Mockito.anyString()))
				.thenReturn(serviceAreaMatrixIASList);
		ProductSlaBean pBean = iasProductService.processProductSlaWithCity("3,factor,process");
		assertTrue(pBean != nullValue());
	}
}
