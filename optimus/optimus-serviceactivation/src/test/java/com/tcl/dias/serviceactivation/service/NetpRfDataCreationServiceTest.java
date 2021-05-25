package com.tcl.dias.serviceactivation.service;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NetpRfDataCreationServiceTest {

	@Autowired
	NetpRfDataCreationService netpRfDataCreationService;

	@Test
	public void netpRadwinCreateTest() throws TclCommonException {
		System.out.println(netpRfDataCreationService.netpRadwinCreate("091MUVA623012422078", "10.192.114.65", "HSntNetwork:1005",
				"091MUVA623012422078_Housing_dev", "MIGR_NETP_LM_RADW_Jun2020_11_0550", "00:15:67:5f:d3:05", "FALSE"));
	}

	@Test
	public void netpCambiumCreateTest() throws TclCommonException {
		System.out.println(netpRfDataCreationService.netpCambiumCreate("091CHEN623031917O29", "CWnt0000Fg",
				"091CHEN623031917O29_BANK_OF_BAN", "01 15 67 fb f0 47", "MIGR_NETP_LM_CAMB_APR2020_3", "FALSE"));
	}

	@Test
	public void netpRadwinModifyMacdTest() throws TclCommonException {
		System.out.println(netpRfDataCreationService.netpRadwinModifyMacd("091MUVA623012422078", "10.192.114.65", "HSntNetwork:1005",
				"091MUVA623012422078_Housing_dev", "MIGR_NETP_LM_RADW_Jun2020_11_0550", "00:15:67:5f:d3:09", "FALSE"));

	}

}