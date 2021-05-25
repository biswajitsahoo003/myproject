package com.tcl.dias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.metrics.export.prometheus.EnablePrometheusMetrics;

/**
 * 
 * This Class is the main appilcation for oms-service
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class OptimusPerformanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptimusPerformanceApplication.class, args);
	}
}
