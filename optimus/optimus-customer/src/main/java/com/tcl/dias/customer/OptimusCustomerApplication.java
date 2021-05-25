package com.tcl.dias.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * this class consists application initialization
 * 
 * @author KusumaK
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class OptimusCustomerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusCustomerApplication.class, args);
	}
}
