package com.tcl.dias.l2oworkflowutils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author Manojkumar R
 *
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class L2oworkflowUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(L2oworkflowUtilsApplication.class, args);
	}
}
