package com.tcl.dias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * This Class is the main application for batch
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class OptimusBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusBatchApplication.class, args);
	}
}
