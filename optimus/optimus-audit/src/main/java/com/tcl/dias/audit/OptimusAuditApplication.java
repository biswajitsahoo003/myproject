package com.tcl.dias.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * This class is used to capture all the audit and write it to the audit schema
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class OptimusAuditApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusAuditApplication.class, args);
	}
}
