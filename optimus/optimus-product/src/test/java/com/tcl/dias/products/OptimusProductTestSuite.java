package com.tcl.dias.products;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tcl.dias.products.controller.TestProductController;
import com.tcl.dias.products.integration.TestProductControllerIntegration;
import com.tcl.dias.products.service.TestProductService;
import com.tcl.dias.products.validations.TestBeanValidation;


@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestProductController.class,
	TestProductService.class,
	TestProductControllerIntegration.class,
	TestBeanValidation.class	
})
public class OptimusProductTestSuite {

}
