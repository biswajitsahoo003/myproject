package com.tcl.dias.imobileauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * This class is used to capture all the mobile auth services
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class OptimusMobileAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusMobileAuthApplication.class, args);
	}
	
}
