package com.tcl.dias.notification;

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
public class OptimusNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusNotificationApplication.class, args);
	}
}
