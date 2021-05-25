package com.tcl.dias.servicefulfillment.entity.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * this class used to config the connectionpool
 * 
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@EntityScan("com.tcl.dias.servicefulfillment.entity.entities")
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "servicefulfillmentEntityManagerFactory", transactionManagerRef = "servicefulfillmentTransactionManager", basePackages = {
		"com.tcl.dias.servicefulfillment.entity.repository" })
public class JpaConfig {

	@Primary
	@Bean(name = "servicefulfillmentDataSourceProperties")
    @ConfigurationProperties("hibernate.datasource")
    public DataSourceProperties servicefulfillmentDataSourceProperties() {
        return new DataSourceProperties();
    }
	
	
	/**
	 * this bean is used to establish connection. @ return ComboPooledDataSource
	 */
	@Primary
	@Bean(name = "servicefulfillmentDataSource")
	@ConfigurationProperties(prefix = "hibernate.datasource")
	public DataSource dataSource() {
		HikariDataSource  hikariDataSource = servicefulfillmentDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    	hikariDataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
    	return hikariDataSource;
	}
	
	
	
	/*@Primary
	@Bean(name = "servicefulfillmentDataSource")
	@ConfigurationProperties("hibernate.datasource")
	public ComboPooledDataSource dataSource() {
		return new ComboPooledDataSource();
	}*/    

	@Primary
	@Bean(name = "servicefulfillmentEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean servicefulfillmentEntityManagerFactory(
			EntityManagerFactoryBuilder builder, @Qualifier("servicefulfillmentDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.tcl.dias.servicefulfillment.entity")
				.persistenceUnit("servicefulfillment").build();
	}
	
	@Bean(name = "servicefulfillmentTransactionManager")
	public PlatformTransactionManager servicefulfillmentTransactionManager(
			@Qualifier("servicefulfillmentEntityManagerFactory") EntityManagerFactory servicefulfillmentEntityManagerFactory) {
		return new JpaTransactionManager(servicefulfillmentEntityManagerFactory);
	}
	


}
