package com.tcl.dias.oms;

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
public class OmsUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmsUtilsApplication.class, args);
	}
}
