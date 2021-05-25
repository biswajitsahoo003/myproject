package com.tcl.dias.products.consumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.ResourceDto;
import com.tcl.dias.products.service.v1.ProductsService;
import com.tcl.dias.products.util.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
/**
 * This file contains test cases for the CpeBomDetailsConsumer.java class.
 * 
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCpeBomDetailsConsumer {

	@Autowired
	CpeBomDetailsConsumer cpeBomDetailsConsumer;
	
	@Autowired
	ObjectCreator objectCreator;
	
	@MockBean
	ProductsService productService;
	
	
	@Test
	public void testProcessSlaDetails() throws Exception {
		Set<CpeBomDto> cpeBomDtoList=new HashSet<>();
		cpeBomDtoList.add(objectCreator.getCpeBomDto());
		Mockito.when(productService.getCpeBomDetails(Mockito.anyList()))
				.thenReturn(cpeBomDtoList);
		String response = cpeBomDetailsConsumer.processSlaDetails("1,2");
		assertTrue(response != null);
	}
	
}
