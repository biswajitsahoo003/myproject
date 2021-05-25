package com.tcl.dias.products.validations;

import static org.junit.Assert.assertTrue;

import java.lang.invoke.MethodHandles;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.products.dto.AttributeMasterDto;
import com.tcl.dias.products.dto.AttributeValueDto;
import com.tcl.dias.products.dto.ComponentDto;
import com.tcl.dias.products.dto.CpeBomDto;
import com.tcl.dias.products.dto.GvpnSlaRequestDto;
import com.tcl.dias.products.dto.ProductComponentAssocDto;
import com.tcl.dias.products.dto.ProductDto;
import com.tcl.dias.products.dto.ProductFamilyDto;
import com.tcl.dias.products.dto.ResourceDto;
import com.tcl.dias.products.dto.SLADto;
import com.tcl.dias.products.util.ObjectCreator;


/**
 * This file contains the test cases for Bean validations for all data transfer objects
 * 
 *
 * @author Dinahar Vivekanandan
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestBeanValidation {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	
	private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Autowired
    ObjectCreator objectCreator;
 
    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        
    
    }
 
    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    /*
     * Test case to test AttributeMasterDto bean validations
     * @author Dinahar Vivekanandan
     */
    @Test
    public void testAttributeMasterDto() {
    	 Set<ConstraintViolation<AttributeMasterDto>> violations= validator.validate(objectCreator.createAttributeMasterDto());
    	 violations.forEach(violation -> LOGGER.info("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
    
    /*
     * Test case to test AttributeValueDto bean validations
     * @author Dinahar Vivekanandan
     */
    @Test
    public void testAttributeValueDto() {
    	 Set<ConstraintViolation<AttributeValueDto>> violations= validator.validate(objectCreator.createAttributeValueDto());
    	 violations.forEach(violation -> LOGGER.info("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
    
    /*
     * Test case to test ProductComponentAssocDto bean validations
     * @author Dinahar Vivekanandan
     */
    @Test
    public void testProductComponentAssocDto() {
    	ProductComponentAssocDto dtoObj = new ProductComponentAssocDto(objectCreator.createProductComponentAssoc());
    	 Set<ConstraintViolation<ProductComponentAssocDto>> violations= validator.validate(dtoObj);
    	 violations.forEach(violation -> LOGGER.info("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
    
    /*
     * Test case to test ProductDto bean validations
     * @author Dinahar Vivekanandan
     */
    @Test
    public void testProductDto() {
    	 Set<ConstraintViolation<ProductDto>> violations= validator.validate(objectCreator.createProductDto());
    	 violations.forEach(violation -> LOGGER.info("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
    
    /*
     * Test case to test ProductFamilyDto bean validations
     * @author Dinahar Vivekanandan
     */
    @Test
    public void testProductFamilyDto() {
    	 Set<ConstraintViolation<ProductFamilyDto>> violations= validator.validate(objectCreator.createProductFamilyDto());
    	 violations.forEach(violation -> LOGGER.info("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
    
    /*
     * Test case to test ProductSlaDto bean validations
     * @author Dinahar Vivekanandan
     */
    @Test
    public void testProductSlaDto() {
    	 Set<ConstraintViolation<SLADto>> violations= validator.validate(objectCreator.createSLADto());
    	 violations.forEach(violation -> LOGGER.info("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
    
    /*
     * Test case to test CpeBomDto bean validations
     * @author Dinahar Vivekanandan
     */
    @Test
    public void testCpeBomDto() {
    	 Set<ConstraintViolation<CpeBomDto>> violations= validator.validate(objectCreator.createCpeBomDto());
    	 violations.forEach(violation -> LOGGER.info("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
    
    /*
     * Test case to test ResourceDto bean validations
     * @author Dinahar Vivekanandan
     */
    @Test
    public void testResourceDto() {
    	 Set<ConstraintViolation<ResourceDto>> violations= validator.validate(objectCreator.createResourceDto());
    	 violations.forEach(violation -> LOGGER.info("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
    
    /*
     * Test case to test GvpnSlaRequestDto bean validations
     * @author Dinahar Vivekanandan
     */
    @Test
    public void testGvpnSlaRequestDto() {
    	 Set<ConstraintViolation<GvpnSlaRequestDto>> violations= validator.validate(objectCreator.createGvpnSlaRequest());
    	 violations.forEach(violation -> LOGGER.info("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
}
