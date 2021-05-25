package com.tcl.dias.products.gsc.service;

import com.tcl.dias.productcatelog.entity.entities.*;
import com.tcl.dias.productcatelog.entity.repository.*;
import com.tcl.dias.products.gsc.beans.GscProductLocationBean;
import com.tcl.dias.products.gsc.service.v1.GscProductServiceMatrixService;
import com.tcl.dias.products.util.ObjectCreator;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tcl.dias.products.gsc.constants.GscProductServiceMatrixConstant.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GscProductServiceMatrixServiceTest {

	@Autowired
	GscProductServiceMatrixService gscProductServiceMatrixService;

	@Autowired
	ObjectCreator objectCreator;

	@MockBean
	ServiceAreaMatrixGSCAudioCnfViewRepository serviceAreaMatrixGSCAudioCnfViewRepository;
	@MockBean
	ServiceAreaMatrixGSCDVViewRepository serviceAreaMatrixGSCDVViewRepository;
	@MockBean
	ServiceAreaMatrixGSCGlobalOutBndViewRepository serviceAreaMatrixGSCGlobalOutBndViewRepository;
	@MockBean
	ServiceAreaMatrixGSCITFSViewRepository serviceAreaMatrixGSCITFSViewRepository;
	@MockBean
	ServiceAreaMatrixGSCLNSViewRepository serviceAreaMatrixGSCLNSViewRepository;
	@MockBean
	ServiceAreaMatrixGSCUIFNViewRepository serviceAreaMatrixGSCUIFNViewRepository;

	public void init() {

	}

	@Test
	public void testGetCountriesForITFS() throws Exception {
		when(serviceAreaMatrixGSCITFSViewRepository.findAll()).thenReturn(getServiceAreaMatrixGSCITFSViews());
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(ITFS, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test(expected = Exception.class)
	public void testGetCountriesForITFS_Exception() throws Exception {
		when(serviceAreaMatrixGSCITFSViewRepository.findAll()).thenReturn(null);
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(ITFS, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());

	}

	@Test
	public void testGetCountriesForLNS() throws Exception {
		when(serviceAreaMatrixGSCLNSViewRepository.findAll()).thenReturn(getServiceAreaMatrixGSCLNSViews());
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(LNS, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test(expected = Exception.class)
	public void testGetCountriesForLNS_Exception() throws Exception {
		when(serviceAreaMatrixGSCLNSViewRepository.findAll()).thenReturn(null);
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(LNS, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test
	public void testGetCountriesForUIFN() throws Exception {
		when(serviceAreaMatrixGSCUIFNViewRepository.findAll()).thenReturn(getServiceAreaMatrixGSCUIFNViews());
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(UIFN, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test(expected = Exception.class)
	public void testGetCountriesForUIFN_Exception() throws Exception {
		when(serviceAreaMatrixGSCUIFNViewRepository.findAll()).thenReturn(null);
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(UIFN, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test
	public void testGetCountriesForAudioConference() throws Exception {
		when(serviceAreaMatrixGSCAudioCnfViewRepository.findAll()).thenReturn(getServiceAreaMatrixGSCAudioCnfView());
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(ACANS, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test(expected = Exception.class)
	public void testGetCountriesForAudioConference_Exception() throws Exception {
		when(serviceAreaMatrixGSCAudioCnfViewRepository.findAll()).thenReturn(null);
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(ACANS, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test
	public void testGetCountriesForGlobalOutbound() throws Exception {
		when(serviceAreaMatrixGSCGlobalOutBndViewRepository.findAll()).thenReturn(
				getServiceAreaMatrixGSCGlobalOutBndView());
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(GLOBAL_OUTBOUND, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test(expected = Exception.class)
	public void testGetCountriesForGlobalOutbound_Exception() throws Exception {
		when(serviceAreaMatrixGSCGlobalOutBndViewRepository.findAll()).thenReturn(null);
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(GLOBAL_OUTBOUND, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test
	public void testGetCountriesForDV() throws Exception {
		when(serviceAreaMatrixGSCDVViewRepository.findAll()).thenReturn(getServiceAreaMatrixGSCDVView());
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(DOMESTIC_VOICE, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	@Test(expected = Exception.class)
	public void testGetCountriesForDV_Exception() throws Exception {
		when(serviceAreaMatrixGSCDVViewRepository.findAll()).thenReturn(null);
		GscProductLocationBean gscProductLocationBean = gscProductServiceMatrixService.getCountries(DOMESTIC_VOICE, PUBLIC_IP, null, null);
		assertNotNull(gscProductLocationBean);
		assertEquals("India", gscProductLocationBean
				.getSources()
				.stream()
				.findFirst()
				.get()
				.getName());
	}

	private List<ServiceAreaMatrixGSCITFSView> getServiceAreaMatrixGSCITFSViews() {
		ServiceAreaMatrixGSCITFSView serviceAreaMatrixGSCITFSView = new ServiceAreaMatrixGSCITFSView();
		serviceAreaMatrixGSCITFSView.setIsoCountryName("India");
		serviceAreaMatrixGSCITFSView.setIso3CountryCode("IND");
		serviceAreaMatrixGSCITFSView.setEstimatedStandardLeadTimeDays("10");
		List<ServiceAreaMatrixGSCITFSView> list = new ArrayList<>();
		list.add(serviceAreaMatrixGSCITFSView);
		return list;
	}

	private List<ServiceAreaMatrixGSCLNSView> getServiceAreaMatrixGSCLNSViews() {
		ServiceAreaMatrixGSCLNSView serviceAreaMatrixGSCLNSView = new ServiceAreaMatrixGSCLNSView();
		serviceAreaMatrixGSCLNSView.setIsoCountryName("India");
		serviceAreaMatrixGSCLNSView.setIso3CountryCode("IND");
		serviceAreaMatrixGSCLNSView.setEstimatedStandardLeadTimeDays("10");
		List<ServiceAreaMatrixGSCLNSView> list = new ArrayList<>();
		list.add(serviceAreaMatrixGSCLNSView);
		return list;
	}

	private List<ServiceAreaMatrixGSCUIFNView> getServiceAreaMatrixGSCUIFNViews() {
		ServiceAreaMatrixGSCUIFNView serviceAreaMatrixGSCUIFNView = new ServiceAreaMatrixGSCUIFNView();
		serviceAreaMatrixGSCUIFNView.setIsoCountryName("India");
		serviceAreaMatrixGSCUIFNView.setIso3CountryCode("IND");
		serviceAreaMatrixGSCUIFNView.setEstimatedStandardLeadTimeDays("10");
		List<ServiceAreaMatrixGSCUIFNView> list = new ArrayList<>();
		list.add(serviceAreaMatrixGSCUIFNView);
		return list;
	}

	private List<ServiceAreaMatrixGSCAudioCnfView> getServiceAreaMatrixGSCAudioCnfView() {
		ServiceAreaMatrixGSCAudioCnfView serviceAreaMatrixGSCAudioCnfView = new ServiceAreaMatrixGSCAudioCnfView();
		serviceAreaMatrixGSCAudioCnfView.setIsoCountryName("India");
		serviceAreaMatrixGSCAudioCnfView.setIso3CountryCode("IND");
		serviceAreaMatrixGSCAudioCnfView.setEstimatedStandardLeadTimeDays("10");
		List<ServiceAreaMatrixGSCAudioCnfView> list = new ArrayList<>();
		list.add(serviceAreaMatrixGSCAudioCnfView);
		return list;
	}

	private List<ServiceAreaMatrixGSCGlobalOutBndView> getServiceAreaMatrixGSCGlobalOutBndView() {
		ServiceAreaMatrixGSCGlobalOutBndView serviceAreaMatrixGSCGlobalOutBndView = new ServiceAreaMatrixGSCGlobalOutBndView();
		serviceAreaMatrixGSCGlobalOutBndView.setIsoCountryName("India");
		List<ServiceAreaMatrixGSCGlobalOutBndView> list = new ArrayList<>();
		list.add(serviceAreaMatrixGSCGlobalOutBndView);
		return list;
	}

	private List<ServiceAreaMatrixGSCDVView> getServiceAreaMatrixGSCDVView() {
		ServiceAreaMatrixGSCDVView serviceAreaMatrixGSCDVView = new ServiceAreaMatrixGSCDVView();
		serviceAreaMatrixGSCDVView.setIsoCountryName("India");
		serviceAreaMatrixGSCDVView.setIso3CountryCode("IND");
		serviceAreaMatrixGSCDVView.setEstimatedStandardLeadTimeDays("10");
		List<ServiceAreaMatrixGSCDVView> list = new ArrayList<>();
		list.add(serviceAreaMatrixGSCDVView);
		return list;
	}

	@Test
	public void testGetRfsDate() {
		Mockito.when(serviceAreaMatrixGSCITFSViewRepository.findByIsoCountryName(Mockito.anyString()))
				.thenReturn(getServiceAreaMatrixGSCITFSViews());
		Mockito.when(serviceAreaMatrixGSCUIFNViewRepository.findByIsoCountryName(Mockito.anyString()))
				.thenReturn(getServiceAreaMatrixGSCUIFNViews());
		Mockito.when(serviceAreaMatrixGSCLNSViewRepository.findByIsoCountryName(Mockito.anyString()))
				.thenReturn(getServiceAreaMatrixGSCLNSViews());
		Mockito.when(serviceAreaMatrixGSCDVViewRepository.findByIsoCountryName(Mockito.anyString()))
				.thenReturn(getServiceAreaMatrixGSCDVView());
		Mockito.when(serviceAreaMatrixGSCAudioCnfViewRepository.findByIsoCountryName(Mockito.anyString()))
				.thenReturn(getServiceAreaMatrixGSCAudioCnfView());

		List<String> services = Arrays.asList("ITFS", "LNS", "UIFN", "ACANS", "ACDTFS", "Global Outbound",
				"Domestic Voice");
		List<String> accessTypes = Arrays.asList("PSTN", "MPL", "PUBLIC_IP");

		String countries = "India";

		services.forEach(service -> {
			accessTypes.forEach(accessType -> {
				assertNotNull(gscProductServiceMatrixService.getGscExpectedDateForDelivery(service, accessType, countries));
			});
		});
	}

	@Test(expected = NullPointerException.class)
	public void testGetRfsDateForNullService() {
		String countries = "India";
		gscProductServiceMatrixService.getGscExpectedDateForDelivery(null, null, countries);
	}

}
