package com.tcl.dias.performance.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * This file contains the DBConfigurations.java class.
 * used to configure different DB connection
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@Component
public class DBConfigurations {

	@Bean(name = "dsVertica")
	@ConfigurationProperties(prefix = "vertica.datasource")
	public ComboPooledDataSource verticaDataSource() {
		 return new ComboPooledDataSource();
	}

	@Bean(name = "reportTemplate")
	public NamedParameterJdbcTemplate reportTemplate(@Qualifier("dsVertica") DataSource ds) {
		return new NamedParameterJdbcTemplate(ds);
	}

	@Bean(name = "dsTeradata")
	@ConfigurationProperties(prefix = "teradata.datasource")
	public ComboPooledDataSource teraDataDataSource() {
		return new ComboPooledDataSource();
	}

	@Bean(name = "teradataTemplate")
	public JdbcTemplate teradataTemplate(@Qualifier("dsTeradata") DataSource ds) {
		return new JdbcTemplate(ds);
	}

	@Bean(name = "dsOptimus")
	@ConfigurationProperties(prefix = "spring.datasource")
	public ComboPooledDataSource optimusDataSource() {
		return new ComboPooledDataSource();
	}

	@Bean(name = "optimusTemplate")
	public JdbcTemplate optimusTemplate(@Qualifier("dsOptimus") DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
}