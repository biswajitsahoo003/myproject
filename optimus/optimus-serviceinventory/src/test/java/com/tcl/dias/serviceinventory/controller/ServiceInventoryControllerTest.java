package com.tcl.dias.serviceinventory.controller;

import static org.junit.Assert.assertTrue;

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

import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ProductInformationBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.dias.serviceinventory.beans.SIOrderBean;
import com.tcl.dias.serviceinventory.beans.SIServiceInformationBean;
import com.tcl.dias.serviceinventory.controller.v1.ServiceInventoryController;
import com.tcl.dias.serviceinventory.service.v1.ServiceInventoryService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains test cases for the ServiceInventoryController.java class.
 * 
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceInventoryControllerTest {
	

	@MockBean
	ServiceInventoryService serviceInventoryService;

	
	@Autowired
	ObjectCreator objectCreator;
	
	@Autowired
	ServiceInventoryController serviceInventoryController;
	
	
	@Test
	public void testGetOrderDetails() throws TclCommonException {
		Mockito.when(serviceInventoryService.getOrderDetails(Mockito.anyInt())).thenReturn(objectCreator.getSIOrderBeanList());
		ResponseResource<List<SIOrderBean>> response = serviceInventoryController.getOrderDetails(2);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	

	//positive testcase
	@Test
	public void testGetServiceDetailsByProduct_case1() throws TclCommonException {
		Mockito.when(serviceInventoryService.getAllServiceDetailsByProduct(Mockito.anyInt(),Mockito.any(),Mockito.any(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(), Mockito.anyBoolean())).thenReturn(objectCreator.getSiServiceInfo());
		ResponseResource<SIServiceInformationBean> response = serviceInventoryController.getServiceDetailsByProduct(1, 1, 1,1,1,null,"Yes",null);
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test
	public void testGetServiceDetailsByProduct_case2() throws TclCommonException {
		Mockito.when(serviceInventoryService.getAllServiceDetailsByProduct(Mockito.anyInt(),Mockito.any(),Mockito.any(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(), Mockito.anyBoolean())).thenReturn(null);
		ResponseResource<SIServiceInformationBean> response = serviceInventoryController.getServiceDetailsByProduct(1, 1, 1,1,1,null,"Yes",null);
		assertTrue(response.getData()==null);
	}
	
	@Test
	public void testGetServiceDetailsByProductSearch_case3() throws TclCommonException {
		Mockito.when(serviceInventoryService.getServiceDetailsWithPaginationAndSearch(Mockito.anyInt(),Mockito.any(),Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(),Mockito.anyInt(),Mockito.anyString())).thenReturn(objectCreator.getSIServiceInformationList());
		ResponseResource<PagedResult<List<SIServiceInformationBean>>> response = serviceInventoryController.getServiceDetailsByProduct(1, 1, 1, "Mumbai", "Nilkamal", "091MUMB623029393221",1,1,null,null,1,"");
		assertTrue(response != null && response.getStatus() == Status.SUCCESS);
	}
	
	//Negative case
	@Test
	public void testGetServiceDetailsByProductSearch_case4() throws TclCommonException {
		
		Mockito.when(serviceInventoryService.getServiceDetailsWithPaginationAndSearch(Mockito.anyInt(),Mockito.any(),Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),Mockito.any(),Mockito.anyInt(),Mockito.anyString())).thenReturn(null);
		
		ResponseResource<PagedResult<List<SIServiceInformationBean>>> response = serviceInventoryController.getServiceDetailsByProduct(1, 1, 1, "Mumbai", "Nilkamal", "091MUMB623029393221",1,1,1, null,1,"");
	
		assertTrue(response.getData()==null);
	}

	@Test
	public void test1GetServiceDetailsProductwise() throws TclCommonException {
		Mockito.when(serviceInventoryService.getAllProductServiceInformation(Mockito.anyInt(),Mockito.anyInt())).
			thenReturn(objectCreator.createProductInformationBeanList());
		ResponseResource<List<ProductInformationBean>> response = serviceInventoryController.getServiceDetailsProductwise(1,1);
		assertTrue(response!=null && response.getStatus() == Status.SUCCESS);
	}
	
	@Test(expected=Exception.class)
	public void test2GetServiceDetailsProductwise() throws TclCommonException {
		Mockito.when(serviceInventoryService.getAllProductServiceInformation(Mockito.anyInt(),Mockito.anyInt())).
		thenThrow(Exception.class);
	ResponseResource<List<ProductInformationBean>> response = serviceInventoryController.getServiceDetailsProductwise(1,1);
	}
	
	@Test
	public void test3GetServiceDetailsProductwise() throws TclCommonException {
		Mockito.when(serviceInventoryService.getAllProductServiceInformation(Mockito.anyInt(),Mockito.anyInt())).
		thenReturn(null);
	ResponseResource<List<ProductInformationBean>> response = serviceInventoryController.getServiceDetailsProductwise(1,1);
	System.out.println(response.getResponseCode());
	assertTrue(response.getData() == null);
	}
}
