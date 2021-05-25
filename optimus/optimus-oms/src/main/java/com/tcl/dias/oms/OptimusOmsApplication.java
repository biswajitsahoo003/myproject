package com.tcl.dias.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * This file contains the OptimusOmsApplication.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
@EnableTransactionManagement
public class OptimusOmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusOmsApplication.class, args);
	}
}
