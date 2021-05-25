package com.tcl.dias.rules;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * This class has application to executeRules business rules for various products
 * 
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class OptimusOmsRulesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusOmsRulesApplication.class, args);
	}

}
