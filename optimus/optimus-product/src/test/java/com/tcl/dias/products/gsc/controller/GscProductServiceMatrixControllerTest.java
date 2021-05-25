package com.tcl.dias.products.gsc.controller;

import com.tcl.dias.products.gsc.controller.v1.GscProductServiceMatrixController;
import com.tcl.dias.products.gsc.service.v1.GscProductServiceMatrixService;
import com.tcl.dias.products.util.ObjectCreator;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Controller test class for GSC product related operations
 *
 * @author Mayank Sharma
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GscProductServiceMatrixControllerTest {

	@MockBean
	GscProductServiceMatrixService gscProductServiceMatrixService;

	@Autowired
	GscProductServiceMatrixController gscProductServiceMatrixController;

	@Autowired
	ObjectCreator objectCreator;

	@Test
	public void testGetCities() throws Exception {
		
	}

	//	@Test
	//	public void testGetCountries() {
	//		Mockito.when(gscProductServiceMatrixService.getCountries("ITFS")).thenReturn(null);
	//		gscProductServiceMatrixController.getCountries("ITFS");
	//	}
	//
	//	@Test
	//	public void testGetServices() {
	//		Mockito.when(gscProductServiceMatrixService.getServiceMatrix("ITFS", "India")).thenReturn(null);
	//		gscProductServiceMatrixController.getServices("ITFS","India");
	//	}
}
