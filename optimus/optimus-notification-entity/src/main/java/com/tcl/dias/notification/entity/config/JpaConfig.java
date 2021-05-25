package com.tcl.dias.notification.entity.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * This file contains the JpaConfig.java class.
 * This class consist of Jpa Configuration
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@EntityScan("com.tcl.dias.notification.entity.entities")
@EnableJpaRepositories(basePackages = "com.tcl.dias.notification.entity.repository")
public class JpaConfig {

	/**
	 * 
	 * dataSource-pool config
	 * @return ComboPooledDataSource
	 */
	@Bean
	@ConfigurationProperties("hibernate.datasource")
	public ComboPooledDataSource dataSource() {
		return new ComboPooledDataSource();
	}

}
