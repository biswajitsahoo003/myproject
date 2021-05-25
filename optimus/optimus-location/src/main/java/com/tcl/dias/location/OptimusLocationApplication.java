package com.tcl.dias.location;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This file contains the OptimusLocationApplication.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class OptimusLocationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusLocationApplication.class, args);
	}
}
