package com.tcl.dias.applications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@PropertySource("classpath:application.properties")
@ComponentScan("com.tcl.dias")
@EntityScan("com.tcl.dias.entity")
@EnableJpaRepositories("com.tcl.dias.repository")
public class ApplicationRunner {

	  public static void main(String[] args) {
	    SpringApplication.run(ApplicationRunner.class, args);
	  }
}
