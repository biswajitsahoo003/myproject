package com.tcl.dias.oms;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the OptimusOmsApplicationTests.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OptimusOmsApplicationTests {

	@Autowired
	IllPricingFeasibilityService illPricingFeasibilityService;

	//@Test
	public void contextLoads() throws TclCommonException {
		CustomerDetailsBean customer = illPricingFeasibilityService.processCustomerData(2);
		System.out.println(customer);
	}

}
