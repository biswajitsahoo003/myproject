package com.tcl.dias.billing.service.v1;

import java.io.FileOutputStream;

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

import com.tcl.dias.billing.utils.BillingObjectCreator;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BillingServiceTest {

	@MockBean
	UserInfoUtils userInfoUtils;

	@MockBean
	UserInformation userInfo;
	
	@MockBean
	MQUtils mqUtils;

	@MockBean
	@Qualifier("invoicesTemplate")
	JdbcTemplate invoicesTemplate;
	
	@MockBean
	FileOutputStream fos;
	
	@Autowired
	BillingService billingService;
	
	@Autowired
	@Qualifier("billingObjectCreator")
	BillingObjectCreator objCreator;
	
	@Before
	public void init() throws Exception {
		Mockito.when(userInfoUtils
				.getUserInformation(Mockito.anyString()))
				.thenReturn(Mockito.mock(UserInformation.class));
		
		Mockito.when(invoicesTemplate
				.queryForMap(Mockito.anyString()))
				.thenReturn(objCreator.queryResultMap());

		Mockito.when(mqUtils
				.sendAndReceive(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(objCreator.getBillingAccounts());

		Mockito.doNothing()
				.when(fos)
				.write(Mockito.any());
	}
	
}