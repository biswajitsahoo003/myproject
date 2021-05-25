package com.tcl.dias.serviceinventory.entity.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * this class used to config the connectionpool
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@EntityScan("com.tcl.dias.serviceinventory.entity.entities")
@EnableJpaRepositories(basePackages = "com.tcl.dias.serviceinventory.entity.repository")
public class JpaConfig {

	/**
	 * this bean is used to establish connection.
	 * @ return ComboPooledDataSource 
	 * */	
	@Bean
	@ConfigurationProperties("hibernate.datasource")
	public ComboPooledDataSource dataSource() {
		return new ComboPooledDataSource();
	}    

}


