package com.tcl.dias.products.consumer;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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

import com.tcl.dias.common.beans.DataCenterBean;
import com.tcl.dias.products.service.v1.ProductsService;
/**
 * This file contains test cases for the ProductDataCenterConsumer.java class.
 * 
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestProductDataCenterConsumer {

	@Autowired
	ProductDataCenterConsumer productDataCenterConsumer;
	
	@MockBean
	ProductsService productsService;
	
	@Test
	public void testProcessSlaDetails() throws Exception {
		DataCenterBean dataCenterBean=new DataCenterBean();
		List<DataCenterBean> dataCenters=new ArrayList<>();
		dataCenters.add(dataCenterBean);
		Mockito.when(productsService.getDataCenter(Mockito.any()))
				.thenReturn(dataCenters);
		String response = productDataCenterConsumer.getDataCenters("1,2");
		assertTrue(response != null);
	}
}
