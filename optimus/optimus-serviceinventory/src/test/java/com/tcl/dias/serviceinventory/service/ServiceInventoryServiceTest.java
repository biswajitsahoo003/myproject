package com.tcl.dias.serviceinventory.service;

import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.ProductInformationBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.utils.ObjectCreator;
import com.tcl.dias.serviceinventory.beans.SIOrderBean;
import com.tcl.dias.serviceinventory.beans.SIServiceInformationBean;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;
import com.tcl.dias.serviceinventory.entity.repository.SIOrderRepository;
import com.tcl.dias.serviceinventory.entity.repository.SIServiceDetailRepository;
import com.tcl.dias.serviceinventory.service.v1.SIServiceDetailSpecification;
import com.tcl.dias.serviceinventory.service.v1.ServiceInventoryService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.tcl.dias.serviceinventory.util.ServiceInventoryUtils.toJson;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceInventoryServiceTest {

	@MockBean
	MQUtils mqUtils;

	@MockBean
	SIOrderRepository siOrderRepository;

	@MockBean
	Utils utils;

	@MockBean
	SIServiceDetailRepository siServiceDetailRepository;


	@Autowired
	ObjectCreator objectCreator;

	@Autowired
	ServiceInventoryService serviceInventoryService;

	
	@Test
	public void testGetOrderDetails() throws TclCommonException {
		Mockito.when(siOrderRepository.findByErfCustCustomerIdAndOrderStatusNotIgnoreCase(Mockito.anyString(),Mockito.anyString())).
		thenReturn(objectCreator.getSIOrderList());
		List<SIOrderBean>  list = serviceInventoryService.getOrderDetails(2);
		assertTrue(Objects.nonNull(list));
	}

	@Test(expected=Exception.class)
	public void testGetOrderDetailsForNull() throws TclCommonException {
		serviceInventoryService.getOrderDetails(null);
	}


	//positive case
	@Test
	public void getAllServiceDetailsByProduct() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getCustomerLedIds());
		Mockito.when(siOrderRepository.getServiceDetail(Mockito.anyList(), Mockito.anyList(), Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getServiceInformationBeans());
		Mockito.when(siOrderRepository.getServiceDetail(Mockito.anyList(), Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getServiceInformationBeans());
		Mockito.when(siServiceDetailRepository.getDistictAliasByProduct(Mockito.any(), Mockito.any(), Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getAlias());
		SIServiceInformationBean  siServiceInfo = serviceInventoryService.getAllServiceDetailsByProduct(1, 1, 1,1,1,null,"Yes",null);
		assertTrue(Objects.nonNull(siServiceInfo));
	}
	
	
	@Test(expected=Exception.class)
	public void getAllServiceDetailsByProduct_test1() throws TclCommonException {
		Mockito.when(siOrderRepository.getServiceDetail(Mockito.anyList(), Mockito.anyList(), Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getServiceInformationBeans());
		Mockito.when(siOrderRepository.getServiceDetail(Mockito.anyList(), Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getServiceInformationBeans());
		Mockito.when(siServiceDetailRepository.getServiceCountByProduct(Mockito.any(), Mockito.any(),Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(1);
		Mockito.when(siServiceDetailRepository.getDistictCityByProduct(Mockito.anyInt(), Mockito.any(),Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getCities());
		Mockito.when(siServiceDetailRepository.getDistictAliasByProduct(Mockito.any(), Mockito.any(), Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getAlias());
		SIServiceInformationBean  siServiceInfo = serviceInventoryService.getAllServiceDetailsByProduct(1, 1, 1,1,1,null,"Yes",null);
	}
	
	@Test(expected=Exception.class)
	public void getAllServiceDetailsByProduct_test2() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getCustomerLedIds());
		Mockito.when(siOrderRepository.getServiceDetail(Mockito.anyList(), Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getServiceInformationBeans());
		Mockito.when(siOrderRepository.getServiceDetail(Mockito.anyList(), Mockito.anyList(),Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getServiceInformationBeans());
		Mockito.when(siServiceDetailRepository.getDistictCityByProduct(Mockito.anyInt(), Mockito.any(),Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getCities());
		Mockito.when(siServiceDetailRepository.getDistictAliasByProduct(Mockito.any(), Mockito.any(),Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getAlias());
		Mockito.when(siServiceDetailRepository.getServiceCountByProduct(Mockito.any(), Mockito.any(),Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(1);
		SIServiceInformationBean  siServiceInfo = serviceInventoryService.getAllServiceDetailsByProduct(null, 1, 1,1,1,null,"Yes",null);
	}
	
	@Test(expected=Exception.class)
	public void getAllServiceDetailsByProduct_test3() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getCustomerLedIds());
		Mockito.when(siOrderRepository.getServiceDetail(Mockito.anyList(), Mockito.anyList(), Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getServiceInformationBeans());
		Mockito.when(siOrderRepository.getServiceDetail(Mockito.anyList(), Mockito.anyList(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(),Mockito.any(),Mockito.anyInt())).thenReturn(objectCreator.getServiceInformationBeans());
		Mockito.when(siServiceDetailRepository.getServiceCountByProduct(Mockito.any(), Mockito.any(),Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(1);
		Mockito.when(siServiceDetailRepository.getDistictCityByProduct(Mockito.anyInt(), Mockito.any(),Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getCities());
		Mockito.when(siServiceDetailRepository.getDistictAliasByProduct(Mockito.any(), Mockito.any(),Mockito.anyList(), Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getAlias());
		SIServiceInformationBean  siServiceInfo = serviceInventoryService.getAllServiceDetailsByProduct(1, -1, 1,1,1,1,"Yes",null);
	}
	
	//positive case
		@Test
		public void getServiceDetailsByProduct() throws TclCommonException {
			SIServiceDetailRepository mock= Mockito.mock(SIServiceDetailRepository.class);
			Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getCustomerLedIds());
			 Page<SIServiceDetail> pagedResponse = new PageImpl(objectCreator.getServiceDetails());
			ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
			Specification<SIServiceDetail> spec=SIServiceDetailSpecification.getServiceDetails("mumbai", "Nilkamal", "SFSDF3$#", new ArrayList<Integer>(), new ArrayList<Integer>(),  1,1,1, "","Yes");
			/* Mockito.verify(mock).findAll(spec, Mockito.eq(pageSpecificationArgument.capture()));*/
			 Mockito.when(siServiceDetailRepository.findAll(spec, PageRequest.of(2 - 1, 50))).thenReturn(pagedResponse);
			Mockito.when(siServiceDetailRepository.getDistictCityByProduct(Mockito.anyInt(), Mockito.any(), Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getCities());
			Mockito.when(siServiceDetailRepository.getDistictAliasByProduct(Mockito.any(), Mockito.any(), Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getAlias());
			PagedResult<List<SIServiceInformationBean>> siServiceInfo = serviceInventoryService.getServiceDetailsWithPaginationAndSearch(1, 1, 1, "Mumbai", "Nilkamal", "W23434SDFSDF",1,1,null, "",1,"Yes");
			assertTrue(Objects.isNull(siServiceInfo));
		}
		
		@Test(expected=Exception.class)
		public void getServiceDetailsByProduct_test2() throws TclCommonException {
			SIServiceDetailRepository mock= Mockito.mock(SIServiceDetailRepository.class);
			 Page<SIServiceDetail> pagedResponse = new PageImpl(objectCreator.getServiceDetails());
			ArgumentCaptor<Pageable> pageSpecificationArgument = ArgumentCaptor.forClass(Pageable.class);
			Specification<SIServiceDetail> spec=SIServiceDetailSpecification.getServiceDetails("mumbai", "Nilkamal", "SFSDF3$#", new ArrayList<Integer>(), new ArrayList<Integer>(), 1,1,1, "","No");
			/* Mockito.verify(mock).findAll(spec, Mockito.eq(pageSpecificationArgument.capture()));*/
			 Mockito.when(siServiceDetailRepository.findAll(spec, PageRequest.of(2 - 1, 50))).thenReturn(pagedResponse);
			Mockito.when(siServiceDetailRepository.getDistictCityByProduct(Mockito.anyInt(), Mockito.any(), Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getCities());
			Mockito.when(siServiceDetailRepository.getDistictAliasByProduct(Mockito.any(), Mockito.any(), Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).thenReturn(objectCreator.getAlias());
			PagedResult<List<SIServiceInformationBean>> siServiceInfo = serviceInventoryService.getServiceDetailsWithPaginationAndSearch(1, 1, 1, "Mumbai", "Nilkamal", "W23434SDFSDF",1,1,null, "",1,"No");
			assertTrue(Objects.isNull(siServiceInfo));
		}

	@Test
	public void test1GetAllProductServiceInformation() throws TclCommonException {
		
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.any(), Mockito.any()))
		.thenReturn(toJson(objectCreator.getProductInformationBeans()));


		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(),Mockito.anyString(), Mockito.any()))
		.thenReturn(objectCreator.createCustomerIdsList());


		Mockito.when(siServiceDetailRepository.getServiceCountByProduct(Mockito.any(), Mockito.any(), Mockito.anyList(),Mockito.anyInt(),Mockito.anyInt())).
		thenReturn(1);
		List<ProductInformationBean> list = serviceInventoryService.getAllProductServiceInformation(1,1);
		assertTrue(Objects.nonNull(list));

	}

	@Test(expected=Exception.class)
	public void test2GetAllProductServiceInformation() throws TclCommonException {
		serviceInventoryService.getAllProductServiceInformation(1,1);
	}


}
