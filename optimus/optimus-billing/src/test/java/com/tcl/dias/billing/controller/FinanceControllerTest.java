package com.tcl.dias.billing.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.billing.controller.v1.FinanceController;
import com.tcl.dias.billing.service.v1.FinanceService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * Testing FinanceControllerTest api positive and negative cases
 *
 * @author Kruthika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FinanceControllerTest {
	@MockBean
	FinanceService financeService;

	@Autowired
	FinanceController financeController;


		@Test
		public void testGetSOAController() throws TclCommonException {
			Mockito.when(financeService.buildSOA(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<>());
			ResponseEntity<byte[]> response = financeController.getAllOpenTxnsPDF("SAPCODE");
			assertTrue(response != null && response.getStatusCode() == HttpStatus.OK);
		}

}