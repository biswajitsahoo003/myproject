package com.tcl.dias.constants;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.OptimusServiceApplication;
import com.tcl.dias.service.constants.ExceptionConstants;
import com.tcl.dias.service.constants.ServiceConstants;
import com.tcl.dias.service.swagger.constants.SwaggerConstants;

/**
 * This class contains the test cases for constants
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestConstants {

	/**
	 * This test case is to test the Exception constants
	 * 
	 */
	@Test
	public void TestExceptionConstants()
	{
		assertTrue(ExceptionConstants.COMMON_ERROR=="common.exception");
		assertTrue(ExceptionConstants.FAILED_TO_UPLOAD=="failed.to.upload");
		assertTrue(ExceptionConstants.FILE_EMPTY=="file.empty");
		assertTrue(ExceptionConstants.FILE_NAME_CONATINS_INVALID_CHARECTERS=="file.name.conatins.invalid.charectrs");
		assertTrue(ExceptionConstants.MISSING_INPUT_DATA=="missing.input.data");
	}
	
	
	/**
	 * 
	 * This test case is to test the Service  constants
	 * 
	 */
	 
	@Test
	public void TestServiceConstants()
	{
		ServiceConstants.getByCode("CONNECTION_TYPE");
		assertTrue(ServiceConstants.CONNECTION_TYPE.toString().equals("connectionType"));
		
		
	}
	
	/**
	 * this test case tests the main application
	 * 
	 */
	@Test
	public void TestApplicationContext()
	{
		String [] arr= {"",""};
		OptimusServiceApplication.main(arr);
		
	}
	
	
	
	/**
	 * This Test case is for swagger constants
	 */
	@Test
	public void TestApplicationSwagerConstants()
	{
		assertTrue(SwaggerConstants.ApiOperations.FileUpload.UPLOAD_FILE.equals("Uploading the File"));
	}
	
}
