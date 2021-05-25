package com.tcl.dias.products.izopc.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.productcatelog.entity.repository.CloudProviderAttributeRepository;
import com.tcl.dias.productcatelog.entity.repository.IzoPcSlaViewRepository;
import com.tcl.dias.productcatelog.entity.repository.ProductDataCentreAssocRepository;
import com.tcl.dias.productcatelog.entity.repository.ProviderRepository;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.izopc.beans.CloudProviderAttributeBean;
import com.tcl.dias.products.izopc.beans.DataCenterForCloudProvidersBean;
import com.tcl.dias.products.izopc.beans.DataCenterProviderDetails;
import com.tcl.dias.products.izopc.service.v1.IZOPCProductService;
import com.tcl.dias.products.npl.service.v1.NPLProductService;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the test cases for IZOPCProductsService.java class.
 * 
 * @author Mansi Bedi
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestIZOPCProductService {
	
	@Autowired
	IZOPCProductService iZOPCProductService;

	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	ProductDataCentreAssocRepository productDataCentreAssocRepository;
	
	@MockBean
	CloudProviderAttributeRepository cloudProviderAttributeRepository;
	
	@MockBean
	IzoPcSlaViewRepository izoPcSlaViewRepository;
	
	@MockBean
	ProviderRepository providerRepository;
	
	/*
	 * Positive test case - for retrieving cloud provider details
	 * 
	 */
	@Test
	public void testGetCloudProviderDetails() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findDistinctCloudProviderName()).thenReturn(objectCreator.getCloudProviderName());
		List<String> cloudProviderList = iZOPCProductService.getCloudProviderDetails();
		assertTrue(cloudProviderList != null && !cloudProviderList.isEmpty());
		
	}
	
	
	/*
	 * Positive test case - for retrieving data center details
	 * 
	 */
	@Test
	public void testGetDataCenterDetails() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByCloudProviderName(Mockito.anyString()))
				.thenReturn(objectCreator.getDataCenterDetails());
		List<DataCenterProviderDetails> dataCenterList = iZOPCProductService.getDataCenterDetails("provider");
		assertTrue(dataCenterList != null && !dataCenterList.isEmpty());
		
	}
	
	/*
	 * negative test case - for retrieving data center details
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetDataCenterDetailsForArgNull() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByCloudProviderName(Mockito.anyString()))
		.thenReturn(objectCreator.getDataCenterDetails());
		List<DataCenterProviderDetails> dataCenterList = iZOPCProductService.getDataCenterDetails(null);
		//assertTrue(dataCenterList != null && !dataCenterList.isEmpty());
		
	}
	
	
	/*
	 * Positive test case - for retrieving cloud provider attributes
	 * 
	 */
	@Test
	public void testGetCloudProviderAttribute() throws Exception {
		Mockito.when(cloudProviderAttributeRepository.findByCloudProviderNameAndAttributeName(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objectCreator.getCloudProviderAttributeList());
		List<CloudProviderAttributeBean> cloudProviderList = iZOPCProductService.getCloudProviderAttribute("provider", "attribute");
		assertTrue(cloudProviderList != null && !cloudProviderList.isEmpty());
		
	}
	
	/*
	 * negative test case - for retrieving cloud provider attributes
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetCloudProviderAttributeForBothArgNull() throws Exception {
		Mockito.when(cloudProviderAttributeRepository.findByCloudProviderNameAndAttributeName(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(objectCreator.getCloudProviderAttributeList());
		List<CloudProviderAttributeBean> cloudProviderList = iZOPCProductService.getCloudProviderAttribute(null , null);
		//assertTrue(dataCenterList != null && !dataCenterList.isEmpty());
		
	}
	
	/*
	 * negative test case - for retrieving cloud provider attributes
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetCloudProviderAttributeForArg1Null() throws Exception {
		Mockito.when(cloudProviderAttributeRepository.findByCloudProviderNameAndAttributeName(Mockito.anyString(), Mockito.anyString()))
		.thenReturn(objectCreator.getCloudProviderAttributeList());
		List<CloudProviderAttributeBean> cloudProviderList = iZOPCProductService.getCloudProviderAttribute(null , "attribute");
		//assertTrue(dataCenterList != null && !dataCenterList.isEmpty());
		
	}
	
	/*
	 * Positive test case - for retrieving data center details based on dc code
	 * 
	 */
	@Test
	public void testGetDataCenter() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByDataCenterCd(Mockito.anyString()))
				.thenReturn(objectCreator.getDataCenterDetails());
		DataCenterProviderDetails dcDataCenterList = iZOPCProductService.getDataCenter("code1");
		assertTrue(dcDataCenterList != null);
		
	}
	
	/*
	 * Negative test case - for retrieving data center details based on dc code
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetDataCenterForNegativeCase() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByDataCenterCd(Mockito.anyString()))
				.thenReturn(Lists.emptyList());
		DataCenterProviderDetails dcDataCenterList = iZOPCProductService.getDataCenter("code1");
		assertTrue(dcDataCenterList != null);
		
	}
	
	/*
	 * Negative test case - for retrieving data center details based on dc code
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetDataCenterForArgNull() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByDataCenterCd(Mockito.anyString()))
				.thenReturn(objectCreator.getDataCenterDetails());
		DataCenterProviderDetails dcDataCenterList = iZOPCProductService.getDataCenter(null);
		assertTrue(dcDataCenterList != null);
		
	}
	
	/*
	 * Positive test case - for retrieving sla details
	 * 
	 */
//	@Test
//	public void testGetSlaDetails() throws Exception {
//		Mockito.when(izoPcSlaViewRepository.findAll()).thenReturn(objectCreator.getDataCenterDetails());
//		DataCenterProviderDetails dcDataCenterList = iZOPCProductService.getDataCenter("code1");
//		assertTrue(dcDataCenterList != null);
//		
//	}
	
	/*
	 * Positive test case - for retrieving cloud provider alias
	 * 
	 */
	@Test
	public void testGetCloudProviderAlias() throws Exception {
		Mockito.when(providerRepository.findByName(Mockito.any()))
				.thenReturn(objectCreator.getCloudProviderDetail());
		Object cloudProviderAlias = iZOPCProductService.getCloudProviderAlias(Utils.convertObjectToJson("true"));
		assertTrue(cloudProviderAlias != null);
		
	}
	
	/*
	 *Negative test case - for retrieving cloud provider alias
	 * 
	 */
	@Test(expected=Exception.class)
	public void testGetCloudProviderAliasForArgNull() throws Exception {
		Mockito.when(providerRepository.findByName(Mockito.any()))
				.thenReturn(objectCreator.getCloudProviderDetail());
		Object cloudProviderAlias = iZOPCProductService.getCloudProviderAlias(null);
		assertTrue(cloudProviderAlias != null); 
		
	}
	
	/*
	 * Positive test case - to get sla details
	 * 
	 */
	@Test
	public void testGetSlaDetails() throws Exception {
		Mockito.when(izoPcSlaViewRepository.findAll())
				.thenReturn(objectCreator.getIzoPcSlaViewList());
		List<SLADto> slaList = iZOPCProductService.getSlaDetails();
		assertTrue(slaList != null  && !slaList.isEmpty());
		
	}
	
	/*
	 * Positive test case - to get datacenter details for cloud provider list
	 * 
	 */
	@Test
	public void testGetDataCenterDetailsForCloudProviderList() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByCloudProviderName(Mockito.anyString())).
			thenReturn(objectCreator.getDataCenterDetails());
		
		List<DataCenterForCloudProvidersBean> response = iZOPCProductService.getDataCenterDetailsForCloudProviderList(objectCreator.getCloudProviderName());
		assertTrue(response != null  && !response.isEmpty());
		 
	}

	/*
	 * Negative test case - to get datacenter details for cloud provider list
	 * 
	 */
	@Test(expected=TclCommonException.class)
	public void testGetDataCenterDetailsForCloudProviderListforArgNull() throws Exception {
		Mockito.when(productDataCentreAssocRepository.findByCloudProviderName(Mockito.anyString())).
			thenReturn(objectCreator.getDataCenterDetails());
		
		List<DataCenterForCloudProvidersBean> response = iZOPCProductService.getDataCenterDetailsForCloudProviderList(null);
		assertTrue(response != null  && !response.isEmpty());
		 
	}


}
