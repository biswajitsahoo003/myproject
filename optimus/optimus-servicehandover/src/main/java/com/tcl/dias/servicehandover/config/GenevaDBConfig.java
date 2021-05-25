package com.tcl.dias.servicehandover.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

/**
 * This class is used for configuring geneva database
 * 
 * @author Yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Configuration
@EntityScan("com.tcl.servicehandover.entity")
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "genevaEntityManagerFactory", transactionManagerRef = "genevaTransactionManager", basePackages = {
		"com.tcl.dias.servicehandover.repository" })

public class GenevaDBConfig {

	
   @Bean(name="genevaDataSourceProperties")
    @ConfigurationProperties(prefix = "geneva.datasource")
    public DataSourceProperties genevaDataSourceProperties() {
    	return new DataSourceProperties();
    }    

    @Bean(name = "genevaDataSource")
	@ConfigurationProperties(prefix = "geneva.datasource")
	public DataSource dataSource() {
		HikariDataSource  hikariDataSource = genevaDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    	hikariDataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
    	return hikariDataSource;
	}
	
	@Bean(name = "genevaEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean genevaEntityManagerFactory(
			EntityManagerFactoryBuilder builder, @Qualifier("genevaDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.tcl.servicehandover.entity")
				.persistenceUnit("genevaPU").build();
	}
	
	@Bean(name = "genevaTransactionManager")
	public PlatformTransactionManager genevaTransactionManager(
			@Qualifier("genevaEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
    
}