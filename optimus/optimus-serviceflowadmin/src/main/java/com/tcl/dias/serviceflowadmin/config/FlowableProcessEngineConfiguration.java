package com.tcl.dias.serviceflowadmin.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * This class have all the FlowableProcessEngineConfiguration configs
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
public class FlowableProcessEngineConfiguration {

	@Bean(name = "flowableDataSource")
	@ConfigurationProperties(prefix = "flowable.datasource")
	@Primary
	public ComboPooledDataSource flowableDataSource() {
		return new ComboPooledDataSource();
	}

	@Bean(name = "flowableTransactionManager")
	@Primary
	public PlatformTransactionManager flowableTransactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(flowableDataSource());
		return transactionManager;
	}

	/*
	 * @Bean public SpringProcessEngineConfiguration processEngineConfiguration() {
	 * SpringProcessEngineConfiguration configuration = new
	 * SpringProcessEngineConfiguration();
	 * configuration.setDataSource(flowableDataSource());
	 * configuration.setTransactionManager(flowableTransactionManager());
	 * configuration.setDatabaseSchemaUpdate(SpringProcessEngineConfiguration.
	 * DB_SCHEMA_UPDATE_FALSE); configuration.setAsyncExecutorActivate(true);
	 * configuration.setDatabaseType(ProcessEngineConfiguration.DATABASE_TYPE_MYSQL)
	 * ;
	 * 
	 * configuration.setAsyncHistoryEnabled(true);
	 * configuration.setAsyncHistoryExecutorActivate(true);
	 * 
	 * configuration.setAsyncHistoryJsonGroupingEnabled(true);
	 * configuration.setAsyncHistoryJsonGzipCompressionEnabled(true);
	 * configuration.setAsyncHistoryJsonGroupingThreshold(10);
	 * 
	 * 
	 * return configuration; }
	 * 
	 * @Bean public ProcessEngine processEngine() { return
	 * processEngineConfiguration().buildProcessEngine(); }
	 */

}
