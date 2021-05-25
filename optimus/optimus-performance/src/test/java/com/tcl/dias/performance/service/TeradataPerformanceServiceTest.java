package com.tcl.dias.performance.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.beans.ServerityTicketResponse;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.performance.service.v1.TeradataPerformanceService;
import com.tcl.dias.performance.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the TeradataPerformanceServiceTest.java class.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TeradataPerformanceServiceTest {

	@Autowired
	private TeradataPerformanceService service;

	@Autowired
	private ObjectCreator objectCreator;

	@MockBean
	private UserInfoUtils userInfoUtils;

	@MockBean
	private MQUtils mqUtils;

	@MockBean
	@Qualifier(value = "teradataTemplate")
	private JdbcTemplate jdbcTemplate;

	@Before
	public void init() throws TclCommonException {
		mock(UserInformation.class);

		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(objectCreator.getUserInformation());

		Mockito.doNothing().when(mqUtils).send(Mockito.anyString(), Mockito.anyString());
		Mockito.when(userInfoUtils.getUserInformation(Mockito.anyString()))
				.thenReturn(objectCreator.getUserInformation());

		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
				.thenReturn(objectCreator.getSfdcLeIdsJSON());
	}

	@Test
	public void testgetMonthlyOutageReport() throws TclCommonException {
		Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(new ArrayList<>());

		Map<String, Object> response = service.getMonthlyOutageReport("12-2018");
		assertTrue(response != null);
	}

	@Test(expected = TclCommonException.class)
	public void testgetMonthlyOutageReportWithInvalidQueryResponse() throws TclCommonException {
		Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(null);

		Map<String, Object> response = service.getMonthlyOutageReport("12-2018");
		assertTrue(response != null);
	}

	@Test(expected = TclCommonException.class)
	public void testgetMonthlyOutageReportWithMonthEmpty() throws TclCommonException {
		Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(new ArrayList<>());

		Map<String, Object> response = service.getMonthlyOutageReport("");
		assertNull(response);
	}

	@Test(expected = TclCommonException.class)
	public void testgetMonthlyOutageReportWithInvalidMonth() throws TclCommonException {
		Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(new ArrayList<>());

		Map<String, Object> response = service.getMonthlyOutageReport(null);
		assertNull(response);
	}

	@Test
	public void testMonthlyTicketTrend() throws TclCommonException {
		Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(new ArrayList<>());

		ServerityTicketResponse response = service.getMonthlySeverityTickets("12-2018",null);
		assertTrue(response != null);
	}

	@Test(expected = TclCommonException.class)
	public void testMonthlyTicketTrendWithInvalidQueryReponse() throws TclCommonException {
		Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(null);

		ServerityTicketResponse response = service.getMonthlySeverityTickets("12-2018",null);
		assertNull(response.getTrends());
	}

	@Test(expected = TclCommonException.class)
	public void testMonthlyTicketTrendWithEmptyValue() throws TclCommonException {
		Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(new ArrayList<>());

		ServerityTicketResponse response = service.getMonthlySeverityTickets("",null);
		assertNull(response.getTrends());
	}

	@Test(expected = TclCommonException.class)
	public void testMonthlyTicketTrendWithInvaidMonth() throws TclCommonException {
		Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(new ArrayList<>());

		ServerityTicketResponse response = service.getMonthlySeverityTickets(null,null);
		assertNull(response.getTrends());
	}

	@Test
	public void testTicketTrendBulkData() throws TclCommonException {
		Mockito.when(jdbcTemplate.queryForList(Mockito.anyString())).thenReturn(new ArrayList<>());
		ServerityTicketResponse response = service.getTicketTrendSeverityWise();
		assertTrue(response != null);
	}

}
