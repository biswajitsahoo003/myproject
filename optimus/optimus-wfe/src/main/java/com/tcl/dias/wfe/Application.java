package com.tcl.dias.wfe;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 
 * @author PAULRAJ SUNDAR
 *
 */
@SpringBootApplication
@EnableProcessApplication
@ComponentScan("com.tcl.dias")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
