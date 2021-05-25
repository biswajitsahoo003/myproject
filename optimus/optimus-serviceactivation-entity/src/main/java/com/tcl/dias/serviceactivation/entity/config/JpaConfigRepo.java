package com.tcl.dias.serviceactivation.entity.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * 
 * This file contains the JpaConfig.java class.
 * This class consist of Jpa Configuration
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@EntityScan("com.tcl.dias.serviceactivation.entity.entities")
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "serviceActivationEntityManagerFactory", transactionManagerRef = "serviceActivationTransactionManager", basePackages = {
		"com.tcl.dias.serviceactivation.entity.repository" })
public class JpaConfigRepo {

	@Bean(name = "serviceActivationDataSourceProperties")
    @ConfigurationProperties("activation.datasource")
    public DataSourceProperties serviceActivationDataSourceProperties() {
        return new DataSourceProperties();
    }
	
	@Bean(name = "serviceActivationDataSource")
	@ConfigurationProperties(prefix = "activation.datasource")
	public DataSource dataSource() {
		HikariDataSource  hikariDataSource = serviceActivationDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    	hikariDataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
    	return hikariDataSource;
	}
	
	@Bean(name = "serviceActivationEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean servicefulfillmentEntityManagerFactory(
			EntityManagerFactoryBuilder builder, @Qualifier("serviceActivationDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.tcl.dias.serviceactivation.entity.entities")
				.persistenceUnit("serviceActivation").build();
	}
	
	@Bean(name = "serviceActivationTransactionManager")
	public PlatformTransactionManager servicefulfillmentTransactionManager(
			@Qualifier("serviceActivationEntityManagerFactory") EntityManagerFactory servicefulfillmentEntityManagerFactory) {
		return new JpaTransactionManager(servicefulfillmentEntityManagerFactory);
	}

}
