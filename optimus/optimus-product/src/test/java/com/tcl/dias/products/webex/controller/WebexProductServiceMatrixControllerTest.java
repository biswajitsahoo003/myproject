package com.tcl.dias.products.webex.controller;

import static com.tcl.dias.products.webex.constants.WebexProductServiceMatrixConstant.BRIDGE_USA;
import static com.tcl.dias.products.webex.constants.WebexProductServiceMatrixConstant.DEDICATED;
import static com.tcl.dias.products.webex.constants.WebexProductServiceMatrixConstant.GLOBAL_OUTBOUND;
import static com.tcl.dias.products.webex.constants.WebexProductServiceMatrixConstant.LNS;
import static com.tcl.dias.products.webex.constants.WebexProductServiceMatrixConstant.PAYPERSEAT;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.productcatelog.entity.repository.GscLnsDedicatedPriceViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCGlobalOutBndViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCITFSViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixGSCLNSViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixWEBEXALLProductsRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixWEBEXBridgeCountryRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixWEBEXGlobalOutboundRepository;
import com.tcl.dias.productcatelog.entity.repository.ServiceAreaMatrixWEBEXLNSRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexLnsAllCountryPricingRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexLnsSharedPayPerSeatPricingRepository;
import com.tcl.dias.productcatelog.entity.repository.WebexOutboundAllCountryPricingRepository;
import com.tcl.dias.products.util.WebexObjectCreator;
import com.tcl.dias.products.webex.beans.WebexProductCountriesBean;
import com.tcl.dias.products.webex.beans.WebexProductLocationBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for WebexProductServiceMatrixController.
 *
 *
 * @author Syed Ali
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebexProductServiceMatrixControllerTest {

	@Autowired
	WebexProductServiceMatrixController webexProductServiceMatrixController;

	@Autowired
	WebexObjectCreator webexObjectCreator;

	@MockBean
	ServiceAreaMatrixWEBEXLNSRepository serviceAreaMatrixWeBEXLNSRepository;

	@MockBean
	ServiceAreaMatrixWEBEXGlobalOutboundRepository serviceAreaMatrixWEBEXGlobalOutboundRepository;

	@MockBean
	ServiceAreaMatrixWEBEXBridgeCountryRepository serviceAreaMatrixWEBEXBridgeCountryRepository;

	@MockBean
	ServiceAreaMatrixWEBEXALLProductsRepository serviceAreaMatrixWEBEXALLProductsRepository;

	@MockBean
	ServiceAreaMatrixGSCLNSViewRepository serviceAreaMatrixGSCLNSViewRepository;

	@MockBean
	ServiceAreaMatrixGSCITFSViewRepository serviceAreaMatrixGSCITFSViewRepository;

	@MockBean
	ServiceAreaMatrixGSCGlobalOutBndViewRepository serviceAreaMatrixGSCGlobalOutBndViewRepository;

	@MockBean
	WebexLnsAllCountryPricingRepository webexLnsAllCountryPricingRepository;

	@MockBean
	WebexLnsSharedPayPerSeatPricingRepository webexLnsSharedPayPerSeatPricingRepository;

	@MockBean
	GscLnsDedicatedPriceViewRepository gscLnsDedicatedPriceViewRepository;

	@MockBean
	WebexOutboundAllCountryPricingRepository webexOutboundAllCountryPricingRepository;

	/**
	 * Initialize mock repositories
	 */
	@Before
	public void init() {
		Mockito.when(serviceAreaMatrixWeBEXLNSRepository.findPackagedCountries())
				.thenReturn(webexObjectCreator.createObjects());

		Mockito.when(serviceAreaMatrixWEBEXBridgeCountryRepository.findByDistinctName())
				.thenReturn(webexObjectCreator.createObjects());

		Mockito.when(serviceAreaMatrixWEBEXBridgeCountryRepository.findByRegion(Mockito.anyString()))
				.thenReturn(webexObjectCreator.createObjects());

		Mockito.when(serviceAreaMatrixWEBEXGlobalOutboundRepository.findPackagedCountries())
				.thenReturn(webexObjectCreator.createObjects());

		Mockito.when(serviceAreaMatrixWEBEXALLProductsRepository.findByDistinctCountry())
				.thenReturn(webexObjectCreator.createObjects());

		Mockito.when(serviceAreaMatrixGSCLNSViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryName())
				.thenReturn(webexObjectCreator.createObjects());

		Mockito.when(serviceAreaMatrixGSCITFSViewRepository
				.findDistinctByIso3CountryCodeAndAndIsoCountryNameWhereIndicatorPresent())
				.thenReturn(webexObjectCreator.createObjects());

		Mockito.when(serviceAreaMatrixGSCGlobalOutBndViewRepository.findDistinctByIso3CountryCodeAndAndIsoCountryName())
				.thenReturn(webexObjectCreator.createObjects());
	}

	/**
	 * test get countries for shared pay per seat
	 *
	 *
	 */
	@Test
	public void testGetCountries() throws TclCommonException {
		ResponseResource<WebexProductLocationBean> response = webexProductServiceMatrixController.getCountries("Shared",
				"Global Outbound", "AMER", "PayPer Seat", "PSTN", false);
		assertTrue(response.getData().getDestinations().size() > 0 && response.getData().getSources().size() > 0);
	}

	/**
	 * test get countries for Dedicated pay per Use
	 *
	 *
	 */
	@Test
	public void testGetCountriesForDedicated() throws TclCommonException {
		ResponseResource<WebexProductLocationBean> response = webexProductServiceMatrixController
				.getCountries("Dedicated", "Global Outbound", "AMER", "PayPer Use", "PSTN", false);
		assertTrue(response.getData().getDestinations().size() > 0 && response.getData().getSources().size() > 0);
	}

	/**
	 * test get all countries
	 *
	 *
	 */
	@Test
	public void testGetAllCountries() throws TclCommonException {
		ResponseResource<WebexProductCountriesBean> response = webexProductServiceMatrixController
				.getAllCountries("LNS");
		assertTrue(response.getData().getCountries().size() > 0);
	}

	/**
	 * download prices for LNS, Dedicated, PayPer Seat configuration
	 *
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@Test
	public void testDownloadPricesLNS() throws DocumentException, IOException, TclCommonException {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		Mockito.when(webexLnsAllCountryPricingRepository.findByCountry())
				.thenReturn(webexObjectCreator.createObjects());
		Mockito.when(webexLnsSharedPayPerSeatPricingRepository.findByCountry())
				.thenReturn(webexObjectCreator.createObjects());
		Mockito.when(gscLnsDedicatedPriceViewRepository.findByCountry()).thenReturn(webexObjectCreator.createObjects());
		ResponseEntity<HttpServletResponse> response = webexProductServiceMatrixController
				.downloadPrices(mockHttpServletResponse, DEDICATED, LNS, PAYPERSEAT, "pdf", null, null);
		Assert.assertEquals(MockHttpServletResponse.SC_OK, response.getStatusCodeValue());
	}

	/**
	 * ō download prices for Global Outbound, Dedicated, PayPer Seat, Bridge region
	 * AMER configuration
	 *
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@Test
	public void testDownloadPricesOutbound() throws DocumentException, IOException, TclCommonException {
		MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
		Mockito.when(webexOutboundAllCountryPricingRepository.findByCountry())
				.thenReturn(webexObjectCreator.createObjects());
		ResponseEntity<HttpServletResponse> response = webexProductServiceMatrixController.downloadPrices(
				mockHttpServletResponse, DEDICATED, GLOBAL_OUTBOUND, PAYPERSEAT, "pdf", BRIDGE_USA, true);
		Assert.assertEquals(MockHttpServletResponse.SC_OK, response.getStatusCodeValue());
	}

}
