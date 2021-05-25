package com.tcl.dias.products.gvpn.integration;

import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration product controller test class for
 * TestGVPNProductControllerIntegration.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGVPNProductControllerIntegration {
	/*
	 * 
	 * @Autowired ObjectCreator objectCreator;
	 * 
	 * @Autowired GVPNProductController gvpnProductController;
	 * 
	 * @MockBean ServiceAreaMatrixGvpnRepository serviceAreaMatrixGvpnRepository;
	 * 
	 * @MockBean GvpnSlaViewRepository gvpnSlaViewRepository;
	 * 
	 * @MockBean ProductSlaCosSpecRepository productSlaCosSpecRepository;
	 * 
	 * 
	 * 
	 * Test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValue() throws Exception {
	 * Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(productSlaCosSpecRepository.
	 * findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm(Mockito.anyInt(),
	 * Mockito.anyInt(),Mockito.anyString()))
	 * .thenReturn(Optional.of(objectCreator.createProductSlaCosSpec()));
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueIdAndSlaIdNoIn(Mockito.
	 * any(BigInteger.class),Mockito.anyList()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response!=null && response.getData()!=null &&
	 * response.getResponseCode()==200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForServiceMatrixException() throws Exception
	 * { Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString())) .thenThrow(RuntimeException.class);
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException1() throws Exception
	 * { Mockito.when(gvpnSlaViewRepository.findByFactorValueIdAndSlaIdNoIn(Mockito.
	 * any(BigInteger.class),Mockito.anyList()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt())) .thenThrow(RuntimeException.class);
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException2() throws Exception
	 * { Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt())) .thenThrow(RuntimeException.class);
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException3() throws Exception
	 * { Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.
	 * createGvpnSlaRequestForException()); assertTrue(response==null ||
	 * response.getData()==null || response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException4() throws Exception
	 * { Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(null); assertTrue(response==null ||
	 * response.getData()==null || response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException5() throws Exception
	 * { Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString())) .thenReturn(Optional.empty()); ResponseResource<List<SLADto>>
	 * response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException6() throws Exception
	 * {
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueIdAndSlaIdNoIn(Mockito.
	 * any(BigInteger.class),Mockito.anyList()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt())) .thenReturn(Lists.emptyList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.
	 * createGvpnSlaRequestForException()); assertTrue(response==null ||
	 * response.getData()==null || response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException7() throws Exception
	 * { Mockito.when(gvpnSlaViewRepository.findByFactorValueIdAndSlaIdNoIn(Mockito.
	 * any(BigInteger.class),Mockito.anyList()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt())) .thenReturn(Optional.empty());
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenReturn(objectCreator.createGvpnSlaViewList());
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException8() throws Exception
	 * {
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(productSlaCosSpecRepository.
	 * findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm(Mockito.anyInt(),
	 * Mockito.anyInt(),Mockito.anyString())) .thenThrow(RuntimeException.class);
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException9() throws Exception
	 * {
	 * 
	 * Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(productSlaCosSpecRepository.
	 * findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm(Mockito.anyInt(),
	 * Mockito.anyInt(),Mockito.anyString()))
	 * .thenReturn(Optional.of(objectCreator.createProductSlaCosSpec()));
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenThrow(RuntimeException.class); ResponseResource<List<SLADto>>
	 * response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException10() throws Exception
	 * {
	 * 
	 * Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(productSlaCosSpecRepository.
	 * findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm(Mockito.anyInt(),
	 * Mockito.anyInt(),Mockito.anyString()))
	 * .thenReturn(Optional.of(objectCreator.createProductSlaCosSpec()));
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenReturn(Lists.emptyList()); ResponseResource<List<SLADto>>
	 * response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException11() throws Exception
	 * { Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(productSlaCosSpecRepository.
	 * findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm(Mockito.anyInt(),
	 * Mockito.anyInt(),Mockito.anyString()))
	 * .thenReturn(Optional.of(objectCreator.createProductSlaCosSpec()));
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueIdAndSlaIdNoIn(Mockito.
	 * any(BigInteger.class),Mockito.anyList())) .thenReturn(Optional.empty());
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * 
	 * Test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException12() throws Exception
	 * { ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.
	 * createGvpnSlaRequestForException()); assertTrue(response==null ||
	 * response.getData()==null || response.getResponseCode()!=200); }
	 * 
	 * 
	 * negative test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException13() throws Exception
	 * {
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueIdAndSlaIdNoIn(Mockito.
	 * any(BigInteger.class),Mockito.anyList()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt())) .thenReturn(Lists.emptyList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * neagtive test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException14() throws Exception
	 * { Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(productSlaCosSpecRepository.
	 * findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm(Mockito.anyInt(),
	 * Mockito.anyInt(),Mockito.anyString())) .thenReturn(Optional.empty());
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueIdAndSlaIdNoIn(Mockito.
	 * any(BigInteger.class),Mockito.anyList()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 * 
	 * 
	 * neagtive test case to test getSlaValue
	 * 
	 * @Test public void testGetSlaValueForSlaViewRepoException15() throws Exception
	 * { Mockito.when(serviceAreaMatrixGvpnRepository.findByLocationId(Mockito.
	 * anyString()))
	 * .thenReturn(Optional.of(objectCreator.createTierDetailForGvpn()));
	 * Mockito.when(gvpnSlaViewRepository.
	 * findDistinctBySltVariantAndAccessTopologyAndSlaId(Mockito.anyString(),Mockito
	 * .anyString(),Mockito.anyInt()))
	 * .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findBySltVariantAndSlaId(Mockito.anyString
	 * (),Mockito.anyInt()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * Mockito.when(productSlaCosSpecRepository.
	 * findByPdtCatalogIdAndSlaMetricMaster_IdAndCosSchemaNm(Mockito.anyInt(),
	 * Mockito.anyInt(),Mockito.anyString())) .thenThrow(RuntimeException.class);
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueId(Mockito.any(BigInteger
	 * .class))) .thenReturn(objectCreator.createGvpnSlaViewList());
	 * Mockito.when(gvpnSlaViewRepository.findByFactorValueIdAndSlaIdNoIn(Mockito.
	 * any(BigInteger.class),Mockito.anyList()))
	 * .thenReturn(Optional.of(objectCreator.createGvpnSlaView()));
	 * ResponseResource<List<SLADto>> response =
	 * gvpnProductController.getSlaValue(objectCreator.createGvpnSlaRequest());
	 * assertTrue(response==null || response.getData()==null ||
	 * response.getResponseCode()!=200); }
	 */}
