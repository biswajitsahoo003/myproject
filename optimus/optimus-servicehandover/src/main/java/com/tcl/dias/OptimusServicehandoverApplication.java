package com.tcl.dias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


/**
 * This Class is the main application for Service handover
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@EnableRetry
@SpringBootApplication
public class OptimusServicehandoverApplication {
    public static void main(String[] args) {
        SpringApplication.run(OptimusServicehandoverApplication.class, args);
    }
}
