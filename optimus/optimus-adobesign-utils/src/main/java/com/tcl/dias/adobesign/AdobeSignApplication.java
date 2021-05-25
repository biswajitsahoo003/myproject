package com.tcl.dias.adobesign;

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
public class AdobeSignApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdobeSignApplication.class, args);
	}
}
