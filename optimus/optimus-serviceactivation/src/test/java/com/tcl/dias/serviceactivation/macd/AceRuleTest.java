package com.tcl.dias.serviceactivation.macd;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This class is test class for AceRule class
 * 
 * @author diksha garg
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AceRuleTest {

	@Autowired
	AceRule aceRule;

	@Test
	public void testIsPortChanged() {
		try {

			aceRule.isPortChanged("091CHEN6230A0001650");// 1651,1650

		} catch (TclCommonException e) {

			e.printStackTrace();
		}
	}
	
	@Test
	public void testIsDowntimeRequired() {
		try {

			aceRule.isDowntimeRequired("091CHEN6230A0001650");// 1651,1650

		} catch (TclCommonException e) {

			e.printStackTrace();
		}
	}

}
