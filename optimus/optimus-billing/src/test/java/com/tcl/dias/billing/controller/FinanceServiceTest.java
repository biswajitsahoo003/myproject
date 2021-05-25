package com.tcl.dias.billing.controller;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.billing.service.v1.FinanceService;
import com.tcl.dias.billing.utils.BillingObjectCreator;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * Testing FinanceServiceTest api positive and negative cases
 *
 * @author Kruthika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FinanceServiceTest {
	@Autowired
	FinanceService financeService;

	@Autowired
	BillingObjectCreator billingObjectCreator;

	@MockBean
	MQUtils mqUtils;

	@MockBean
	UserInfoUtils userInfoUtils;

	@Autowired
	@Qualifier("billingObjectCreator")
	BillingObjectCreator objCreator;

	@MockBean
	@Qualifier("financeDB")

	JdbcTemplate financeDB;

	@Before
	public void init() throws TclCommonException {
		Mockito.when(userInfoUtils.getUserInformation(Mockito.any()))
				.thenReturn(billingObjectCreator.getUserInformation());
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(billingObjectCreator.getBillingAccounts());

	}
	
	/*@Test(expected = Exception.class)
	public void testCreateSoaPDF() throws TclCommonException {
		String paramArr[] = { "INVOICE", "PAYMENT", "TDS?PROVISION", "CREDIT NOTE", "TDS RECEIVED" };
		List<String> paramList = Arrays.stream(paramArr).collect(Collectors.toList());
		Mockito.when(financeDB.queryForList(Mockito.any())).thenReturn(objCreator.getDBValues());
		SoaPDFBean soaBean = financeService.getSOA_PDF(paramList, "VI000176");
		assertTrue(soaBean != null);
	}*/
}