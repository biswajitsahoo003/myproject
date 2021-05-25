package com.tcl.dias.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * 
 * This file contains the OptimusAuthApplication.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class OptimusAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusAuthApplication.class, args);
	}
}
