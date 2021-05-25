package com.tcl.dias.wfe.validations;

import static org.junit.Assert.assertTrue;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 
 * @author Dinahar V
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class BeanValidationTest {
	
	private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private  static  Student student = new Student();

    
 
    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        
        //failure case
//        student.setAge(101);
//        student.setName("123");
        
        //success case
        student.setId(1);
        student.setMarks(90);
        student.setAge(6);
        student.setName("name");
    }
 
    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    
    @Test
    public void printStudentBeanViolations() {
    	 Set<ConstraintViolation<Student>> violations= validator.validate(student);
    	 violations.forEach(violation -> System.out.println("violating property = "+violation.getPropertyPath() + " ----- Violation =  "+ violation.getMessage()));
    	 assertTrue(violations.isEmpty());

    }
}
