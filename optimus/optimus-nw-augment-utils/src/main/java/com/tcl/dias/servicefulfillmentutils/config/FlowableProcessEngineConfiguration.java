package com.tcl.dias.servicefulfillmentutils.config;

import javax.sql.DataSource;

import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * This class have all the FlowableProcessEngineConfiguration configs
 * 
 *
 * @author Samuel S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
public class FlowableProcessEngineConfiguration implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
    	processEngineConfiguration.setDataSource(flowableDataSource());
    	processEngineConfiguration.setTransactionManager(transactionManager());
    	processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
    	//processEngineConfiguration.setTransactionsExternallyManaged(true);
    	
        // Async history cfg
    	//processEngineConfiguration.setAsyncHistoryEnabled(true);
    	//processEngineConfiguration.setAsyncHistoryExecutorActivate(true);
    	//processEngineConfiguration.setAsyncHistoryExecutorMessageQueueMode(true);
    }



	/*@Bean(name = "flowableDataSource")
	@ConfigurationProperties(prefix = "flowable.datasource")
	public ComboPooledDataSource flowableDataSource() {
		return new ComboPooledDataSource();
	}  */
    
    @Bean(name = "flowableDataSourceProperties")
    @ConfigurationProperties("flowable.datasource")
    public DataSourceProperties flowableDataSourceProperties() {
        return new DataSourceProperties();
    }
    
    @Bean(name = "flowableDataSource")
	@ConfigurationProperties(prefix = "flowable.datasource")
	public DataSource flowableDataSource() {
    	HikariDataSource  hikariDataSource = flowableDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    	hikariDataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
    	return hikariDataSource;
	}

	@Bean(name="flowable")
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(flowableDataSource());
        return transactionManager;
    }   
	

}
