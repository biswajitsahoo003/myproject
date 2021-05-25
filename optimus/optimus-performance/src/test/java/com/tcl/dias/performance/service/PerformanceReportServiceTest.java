package com.tcl.dias.performance.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.performance.request.ReportGeneratorRequest;
import com.tcl.dias.performance.service.v1.PerformanceReportService;
import com.tcl.dias.performance.utils.ObjectCreator;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * Used for positive and negative cases of PerformanceReportService class 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PerformanceReportServiceTest {
	
	@Autowired
	PerformanceReportService performanceReportService;
	
	@MockBean
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	ObjectCreator objectCreator; 
	
	@MockBean
	UserInfoUtils userInfoUtils;
	
	@MockBean
	MQUtils mqUtils;

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
		Mockito.when(userInfoUtils.getUserInformation(Mockito.anyString())).thenReturn(objectCreator.getUserInformation());
	}
	@Test
	public void testGetPerformanceReportDetails5Min() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getPerformanceReportDetails(objectCreator.getReportGeneratorRequest("5"));
		assertTrue(response != null);
	}
	@Test
	public void testGetPerformanceReportDetailsHour() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getPerformanceReportDetails(objectCreator.getReportGeneratorRequest("Hour"));
		assertTrue(response != null);
	}
	@Test
	public void testGetPerformanceReportDetailsDay() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getPerformanceReportDetails(objectCreator.getReportGeneratorRequest("Day"));
		assertTrue(response != null);
	}
	@Test(expected=Exception.class)
	public void testGetPerformanceReportDetailsNull() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getPerformanceReportDetails(objectCreator.getReportGeneratorRequest(null));
	}
	@Test
	public void testGetUsageReportDetails5Min() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getUsageReportDetails(objectCreator.getReportGeneratorRequest("5"));
		assertTrue(response != null);
	}
	@Test
	public void testGetUsageReportDetailsHour() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getUsageReportDetails(objectCreator.getReportGeneratorRequest("Hour"));
		assertTrue(response != null);
	}
	@Test
	public void testGetUsageReportDetailsDay() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getUsageReportDetails(objectCreator.getReportGeneratorRequest("Day"));
		assertTrue(response != null);
	}
	@Test(expected=Exception.class)
	public void testGetUsageReportDetailsNull() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getUsageReportDetails(objectCreator.getReportGeneratorRequest(null));
	}
	@Test
	public void testGetConcurentAndBandwidthReportDetails5Min() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getConcurentAndBandwidthReportDetails(objectCreator.getReportGeneratorRequest("5"));
		assertTrue(response != null);
	}
	@Test
	public void testGetConcurentAndBandwidthReportDetailsHour() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getConcurentAndBandwidthReportDetails(objectCreator.getReportGeneratorRequest("Hour"));
		assertTrue(response != null);
	}
	@Test
	public void testGetConcurentAndBandwidthReportDetailsDay() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getConcurentAndBandwidthReportDetails(objectCreator.getReportGeneratorRequest("Day"));
		assertTrue(response != null);
	}
	@Test(expected=Exception.class)
	public void testGetConcurentAndBandwidthReportDetailsNull() throws TclCommonException {
		Mockito.when(mqUtils.sendAndReceive(Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(objectCreator.getSfdcLeIdsJSON());
		Mockito.when(namedParameterJdbcTemplate.queryForList(Mockito.anyString(), Mockito.anyMap())).thenReturn(new ArrayList<>());
		List<Map<String, Object>> response= performanceReportService
				.getConcurentAndBandwidthReportDetails(objectCreator.getReportGeneratorRequest(null));
	}
}
