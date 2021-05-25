/**
 * 
 */
package com.tcl.dias.nso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author KarMani
 *
 */


@SpringBootApplication
@ComponentScan("com.tcl.dias")
public class OptimusNSOApplication {

	public static void main(String[] args) {
	SpringApplication.run(OptimusNSOApplication.class, args);

	}

}
