package com.qcm.hotel.portal.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.qcm.hotel.portal.entites")
@EnableJpaRepositories(basePackages = "com.qcm.hotel.portal.repository")
public class JpaConfig {
	
	@Bean
	@ConfigurationProperties("spring.datasource")
	public DataSource  dataSource() {
		 return DataSourceBuilder.create().build();
	}
}
