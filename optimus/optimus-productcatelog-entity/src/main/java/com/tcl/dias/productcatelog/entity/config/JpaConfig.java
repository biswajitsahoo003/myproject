package com.tcl.dias.productcatelog.entity.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * @author Manojkumar R
 *
 */
@Configuration
@EntityScan("com.tcl.dias.productcatelog.entity.entities")
@EnableJpaRepositories(basePackages = "com.tcl.dias.productcatelog.entity.repository")
public class JpaConfig {

	@Bean
	@ConfigurationProperties("hibernate.datasource")
	public ComboPooledDataSource dataSource() {
		return new ComboPooledDataSource();
	}

}
