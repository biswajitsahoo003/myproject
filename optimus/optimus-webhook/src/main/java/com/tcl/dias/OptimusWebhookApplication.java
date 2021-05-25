package com.tcl.dias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.tcl.dias"})
public class OptimusWebhookApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusWebhookApplication.class, args);
	}
}
