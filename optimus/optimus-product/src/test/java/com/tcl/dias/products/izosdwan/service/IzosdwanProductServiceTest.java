package com.tcl.dias.products.izosdwan.service;

import static org.junit.Assert.assertFalse;

import java.util.List;

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

import com.tcl.dias.common.beans.VendorProfileDetailsBean;
import com.tcl.dias.productcatelog.entity.repository.VwSdwanProductOfferingRepository;
import com.tcl.dias.products.izosdwan.service.v1.IzosdwanProductService;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IzosdwanProductServiceTest {
	
	@Autowired
	IzosdwanProductService izosdwanProductService;
	
	@MockBean
	VwSdwanProductOfferingRepository vwSdwanProductOfferingRepository;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@Test
	public void testGetOfferingsPositive() throws TclCommonException {
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueVendors()).thenReturn(objectCreator.getTheVendors());
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueProfilesForTheVendor(Mockito.anyString())).thenReturn(objectCreator.getProfiles());
		Mockito.when(vwSdwanProductOfferingRepository.findByProfileCdAndVendorCd(Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getProductOfferingsList());
		List<VendorProfileDetailsBean> vendorProfileDetailsBeans = izosdwanProductService.getProductOfferingsForSdwanBasedOnVendor("Select");
		assertFalse(vendorProfileDetailsBeans==null);
		
	}
	
	@Test
	public void testGetOfferingsPositive1() throws TclCommonException {
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueVendors()).thenReturn(objectCreator.getTheVendors());
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueProfilesForTheVendor(Mockito.anyString())).thenReturn(objectCreator.getProfiles());
		Mockito.when(vwSdwanProductOfferingRepository.findByProfileCdAndVendorCd(Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getProductOfferingsList());
		List<VendorProfileDetailsBean> vendorProfileDetailsBeans = izosdwanProductService.getProductOfferingsForSdwanBasedOnVendor("Cisco");
		assertFalse(vendorProfileDetailsBeans==null);
		
	}
	
	@Test
	public void testGetOfferingsPositive2() throws TclCommonException {
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueVendors()).thenReturn(objectCreator.getTheVendors());
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueProfilesForTheVendor(Mockito.anyString())).thenReturn(objectCreator.getProfiles());
		Mockito.when(vwSdwanProductOfferingRepository.findByProfileCdAndVendorCd(Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getProductOfferingsList());
		List<VendorProfileDetailsBean> vendorProfileDetailsBeans = izosdwanProductService.getProductOfferingsForSdwanBasedOnVendor("All");
		assertFalse(vendorProfileDetailsBeans==null);
		
	}
	
	@Test(expected = Exception.class)
	public void testGetOfferingsNegative() throws TclCommonException {
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueVendors()).thenReturn(objectCreator.getTheVendors());
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueProfilesForTheVendor(Mockito.anyString())).thenReturn(objectCreator.getProfiles());
		Mockito.when(vwSdwanProductOfferingRepository.findByProfileCdAndVendorCd(Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getProductOfferingsList());
		izosdwanProductService.getProductOfferingsForSdwanBasedOnVendor(null);
		
	}
	
	@Test(expected = Exception.class)
	public void testGetOfferingsNegative1() throws TclCommonException {
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueVendors()).thenReturn(objectCreator.getTheVendors());
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueProfilesForTheVendor(Mockito.anyString())).thenThrow(Exception.class);
		Mockito.when(vwSdwanProductOfferingRepository.findByProfileCdAndVendorCd(Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getProductOfferingsList());
		izosdwanProductService.getProductOfferingsForSdwanBasedOnVendor("All");
		
	}
	
	@Test(expected = Exception.class)
	public void testGetOfferingsNegative2() throws TclCommonException {
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueVendors()).thenThrow(Exception.class);
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueProfilesForTheVendor(Mockito.anyString())).thenReturn(objectCreator.getProfiles());
		Mockito.when(vwSdwanProductOfferingRepository.findByProfileCdAndVendorCd(Mockito.anyString(), Mockito.anyString())).thenReturn(objectCreator.getProductOfferingsList());
		izosdwanProductService.getProductOfferingsForSdwanBasedOnVendor("All");
		
	}
	
	@Test(expected = Exception.class)
	public void testGetOfferingsNegative3() throws TclCommonException {
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueVendors()).thenReturn(objectCreator.getTheVendors());
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueProfilesForTheVendor(Mockito.anyString())).thenReturn(objectCreator.getProfiles());
		Mockito.when(vwSdwanProductOfferingRepository.findByProfileCdAndVendorCd(Mockito.anyString(), Mockito.anyString())).thenThrow(Exception.class);
		izosdwanProductService.getProductOfferingsForSdwanBasedOnVendor("All");
		
	}
	
	@Test(expected = Exception.class)
	public void testGetOfferingsNegative4() throws TclCommonException {
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueVendors()).thenThrow(Exception.class);
		Mockito.when(vwSdwanProductOfferingRepository.getUniqueProfilesForTheVendor(Mockito.anyString())).thenThrow(Exception.class);
		Mockito.when(vwSdwanProductOfferingRepository.findByProfileCdAndVendorCd(Mockito.anyString(), Mockito.anyString())).thenThrow(Exception.class);
		izosdwanProductService.getProductOfferingsForSdwanBasedOnVendor("All");
		
	}

}
