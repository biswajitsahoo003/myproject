package com.tcl.dias.products.gvpn.service;

import static org.hamcrest.CoreMatchers.nullValue;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ProductSlaBean;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.productcatelog.entity.entities.ServiceAreaMatrixGVPN;
import com.tcl.dias.productcatelog.entity.repository.CpeBomGvpnViewRepository;
import com.tcl.dias.productcatelog.entity.repository.GvpnSlaCosViewRepository;
import com.tcl.dias.productcatelog.entity.repository.GvpnSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductSlaCosSpecRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGvpnRepository;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.GvpnSlaRequestDto;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.dto.ServiceAreaMatrixNPLDto;
import com.tcl.dias.products.gvpn.service.v1.GVPNProductService;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Test class for the TestGVPNProductService.java class.
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

public class TestGVPNProductService { 

	@MockBean
	ServiceAreaMatrixGvpnRepository serviceAreaMatrixGvpnRepository;

	@MockBean
	GvpnSlaViewRepository gvpnSlaViewRepository;

	@Autowired
	ObjectCreator objectCreator;

	@Autowired
	GVPNProductService gvpnProductService;
	
	@MockBean
	ProductSlaCosSpecRepository productSlaCosSpecRepository;
	
	@MockBean
	GvpnSlaCosViewRepository gvpnSlaCosViewRepository;
	
	@MockBean
	CpeBomGvpnViewRepository cpeBomGvpnViewRepository;
	
	@Before
	public void init()throws TclCommonException
	{
		Mockito.when(gvpnSlaViewRepository.findDistinctByAccessTopologyAndSlaId(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(objectCreator.getGvpnSlaViewList());
		Mockito.when(gvpnSlaViewRepository.findBySlaId(Mockito.anyInt()))
				.thenReturn(objectCreator.getGvpnSlaViewList());
		Mockito.when(gvpnSlaCosViewRepository.findBySlaIdAndPopTierCd(Mockito.anyInt(), Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.getGvpnSlaCosView()));
		Mockito.when(gvpnSlaCosViewRepository.findByCosSchemaName(Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.getGvpnSlaCosView()));
	}
	
	/*
	 * positive Test case to get Sla Value
	 */
	@Test
	public void testGetSlaValue() throws Exception {
		Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.anyString()))
				.thenReturn(Optional.of(objectCreator.createTierDetailForGvpn("1")));
		Mockito.when(productSlaCosSpecRepository.findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm
				(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString())).thenReturn(Optional.of(objectCreator.getProductSlaCosSpec()));
		/*
		 * Mockito.when(gvpnSlaViewRepository.
		 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
		 * .anyString(),Mockito.anyInt()))
		 * .thenReturn(objectCreator.createGvpnSlaViewList());
		 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
		 * (),Mockito.anyInt()))
		 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
		 */
		/*
		 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
		 * .class))) .thenReturn(objectCreator.createGvpnSlaViewList());
		 * Mockito.when(gvpnSlaViewRepository.findByFactorValueIdAndSlaIdNoIn(Mockito.
		 * any(BigInteger.class),Mockito.anyList()))
		 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
		 */
		List<SLADto> slaDtoList = gvpnProductService.getSlaValue(objectCreator.createGvpnSlaRequest());
		assertTrue(slaDtoList != null && !slaDtoList.isEmpty());
	}

	/*
	 * negative Test case to get Sla Value
	 */
	@Test(expected = TclCommonException.class)
	public void testGetSlaValueForExceptionArgNull() throws Exception {
		gvpnProductService.getSlaValue(null);
	}
	
//	@Test(expected = TclCommonException.class)
//	public void testGetSlaValueForException1() throws Exception {
//	Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.anyString())).thenThrow(Exception.class);
//	List<SLADto> slaDtoList = gvpnProductService.getSlaValue(objectCreator.createGvpnSlaRequest());
//	}
	

//	@Test(expected = TclCommonException.class)
//	public void testGetSlaValueForException1() throws Exception {
//		Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.anyString())).thenReturn(null);
//		List<SLADto> slaDtoList = gvpnProductService.getSlaValue(null);
//		assertTrue(slaDtoList != null && !slaDtoList.isEmpty());
//	}
	
	/*
	 * positive test case to get Service Availability And No Of Incidents
	 */
	@Test(expected = Exception.class)
	public void testGetServiceAvailabilityAndNoOfIncidents() throws Exception {
		Mockito.when(gvpnSlaViewRepository.findDistinctByAccessTopologyAndSlaId(Mockito.anyString(), Mockito.anyInt()))
				.thenReturn(Lists.emptyList());
		gvpnProductService.getServiceAvailabilityAndNoOfIncidents(objectCreator.createGvpnSlaRequest(), 1, 1);
	}
	
	/*
	 * negative test case to get Service Availability And No Of Incidents
	 */
	@Test(expected = Exception.class)
	public void testGetServiceAvailabilityAndNoOfIncidentsForNull() throws Exception {
		gvpnProductService.getServiceAvailabilityAndNoOfIncidents(new GvpnSlaRequestDto(), 1, 1);
	}
	
	/*
	 * negative test case to get Service Availability And No Of Incidents
	 */
	@Test(expected = Exception.class)
	public void testGetServiceAvailabilityAndNoOfIncidentsForArgNull() throws Exception {
		gvpnProductService.getServiceAvailabilityAndNoOfIncidents(null, null, null);
	}
	

	/*
	 * negative test case to retrieve the SLA factors, - Time to Restore, Mean
	 * Time to Restore
	 */
	@Test(expected = Exception.class)
	public void testGetTtrAndMttrAndJitterLevelForNull() throws Exception {
		gvpnProductService.getTtrAndMttrAndJitterLevel(null, null, null);
	}

	/*
	 * negative test case to retrieve Packet delivery ratio
	 */
	@Test(expected = Exception.class)
	public void testGetPktDeliveryRatioForNull() throws Exception {
		gvpnProductService.getPktDeliveryRatio(null);
	}
	
	/*
	 * positive test case to get Process Product Sla
	 */
	@Test
	public void testPocessProductSlaForTire1() throws Exception {
		ProductSlaBean pBean = gvpnProductService.processProductSla("Routine,1");
		assertTrue(pBean != nullValue());
	}
	
	/*
	 * positive test case to get Process Product Sla
	 */
	@Test
	public void testPocessProductSlaForTire2() throws Exception {
		ProductSlaBean pBean = gvpnProductService.processProductSla("Routine,2");
		assertTrue(pBean != nullValue());
	}
	
	/*
	 * positive test case to get Process Product Sla
	 */
	@Test
	public void testPocessProductSlaForTire3() throws Exception {
		ProductSlaBean pBean = gvpnProductService.processProductSla("Routine,3");
		assertTrue(pBean != nullValue());
	}
	
	/*
	 * positive test case to get Process Product Sla with city
	 */
	@Test
	public void testProcessProductSlaWithCityPopTier1() throws Exception{
		List<ServiceAreaMatrixGVPN> serviceAreaMatrixGVPNList=new ArrayList<>();
		serviceAreaMatrixGVPNList.add(objectCreator.createTierDetailForGvpn("1"));
		Mockito.when(serviceAreaMatrixGvpnRepository
		.findByCityNmContainingIgnoreCase(Mockito.anyString())).thenReturn(serviceAreaMatrixGVPNList);
		ProductSlaBean pBean=gvpnProductService.processProductSlaWithCity("Routine,Chennai");
		assertTrue(pBean != nullValue());
	}
	
	/*
	 * positive test case to get Process Product Sla with city
	 */
	@Test
	public void testProcessProductSlaWithCityPopTier2() throws Exception{
		List<ServiceAreaMatrixGVPN> serviceAreaMatrixGVPNList=new ArrayList<>();
		serviceAreaMatrixGVPNList.add(objectCreator.createTierDetailForGvpn("2"));
		Mockito.when(serviceAreaMatrixGvpnRepository
		.findByCityNmContainingIgnoreCase(Mockito.anyString())).thenReturn(serviceAreaMatrixGVPNList);
		ProductSlaBean pBean=gvpnProductService.processProductSlaWithCity("Routine,Chennai");
		assertTrue(pBean != nullValue());
	}
	
	/*
	 * positive test case to get Process Product Sla with city
	 */
	@Test
	public void testProcessProductSlaWithCityPopTier3() throws Exception{
		List<ServiceAreaMatrixGVPN> serviceAreaMatrixGVPNList=new ArrayList<>();
		serviceAreaMatrixGVPNList.add(objectCreator.createTierDetailForGvpn("3"));
		Mockito.when(serviceAreaMatrixGvpnRepository
		.findByCityNmContainingIgnoreCase(Mockito.anyString())).thenReturn(serviceAreaMatrixGVPNList);
		ProductSlaBean pBean=gvpnProductService.processProductSlaWithCity("Routine,Chennai");
		assertTrue(pBean != nullValue());
	}
	
	/*
	 * positive test case to retrieve CPE BOM details
	 */ 
	@Test
	public void testGetCpeBom() throws Exception{
		Mockito.when(cpeBomGvpnViewRepository
		.findByPortInterfaceAndRoutingProtocolAndCpeManagementOptionAndMaxBwInMbpsGreaterThanEqualOrderByMaxBwInMbpsAsc(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyDouble())).
		thenReturn(objectCreator.getCpeBomGvpnViewList());
		List<CpeBomDto> cpeBomDto=gvpnProductService.getCpeBom(1D, "Fast Ethernet", "static", "Fully Managed",null);
		assertTrue(cpeBomDto != nullValue());
	}
	
	
	/*
	 * negative test case to retrieve CPE BOM details
	 */ 
	@Test(expected = TclCommonException.class)
	public void testGetCpeBomForExceptionArgNull() throws Exception {
		gvpnProductService.getCpeBom(null, null, null, null,null);
	}
	
	/*
	 * negative test case to retrieve CPE BOM details
	 */ 
	@Test(expected = TclCommonException.class)
	public void testGetCpeBomException() throws Exception{
		Mockito.when(cpeBomGvpnViewRepository
		.findByPortInterfaceAndRoutingProtocolAndCpeManagementOptionAndMaxBwInMbpsGreaterThanEqualOrderByMaxBwInMbpsAsc(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),Mockito.anyDouble())).
		thenReturn(null);
		List<CpeBomDto> cpeBomDto=gvpnProductService.getCpeBom(1D, "Fast Ethernet", "static", "Fully Managed",null);
		assertTrue(cpeBomDto != nullValue());
	}
	
	
	/*
	 * positive test case to retrieve TierCd
	 */ 
	@Test
	public void testGetTierCd()  throws Exception{
		List<ServiceAreaMatrixGVPN> serviceAreaMatrixGVPNList=new ArrayList<>();
		serviceAreaMatrixGVPNList.add(objectCreator.createTierDetailForGvpn("1"));
		Mockito.when(serviceAreaMatrixGvpnRepository
		.findByCityNmContainingIgnoreCase(Mockito.anyString())).thenReturn(serviceAreaMatrixGVPNList);
		String tierCd=gvpnProductService.getTierCd("chennai");
		assertTrue(tierCd != null);
	}
	
	/*
	 * negative test case to retrieve TierCd
	 */ 
	@Test
	public void testGetTierCdForNegative()  throws Exception{
		Mockito.when(serviceAreaMatrixGvpnRepository
		.findByCityNmContainingIgnoreCase(Mockito.anyString())).thenReturn(Lists.emptyList());
		String tierCd=gvpnProductService.getTierCd("chennai");
		assertTrue(tierCd != null);
	}
	
	/*
	 * negative Test case to retrieve round trip delay details
	 */
	@Test(expected = TclCommonException.class)
	public void testGetRoundTripDelayForExceptionArgNull() throws Exception {
		gvpnProductService.getRoundTripDelay(null);
	}
	
	
}
