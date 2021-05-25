package com.tcl.dias.common.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * this class used to config the database
 * 
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Configuration
public class DatabaseConfig {
	
	@Bean(name = "webhookDataBase")
	@ConfigurationProperties(prefix = "webhook.datasource")
	public ComboPooledDataSource financeDataSource() {
		return new ComboPooledDataSource();
	}

	@Bean
	public JdbcTemplate gbsInvoicesTemplate(@Qualifier("webhookDataBase") DataSource ds) {
		return new JdbcTemplate(ds);
	}
}