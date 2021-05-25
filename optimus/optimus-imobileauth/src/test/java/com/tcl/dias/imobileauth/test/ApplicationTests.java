package com.tcl.dias.imobileauth.test;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.imobileauth.service.v1.IMobileService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author Manojkumar R
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	IMobileService imobileService;

	@Test
	public void contextLoads() throws TclCommonException {

		Map<String, String> response = imobileService.getMasterRealmCode();
		System.out.println(response);
	}

}
