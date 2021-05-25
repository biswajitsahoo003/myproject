package com.tcl.dias.l2oworkflow.entity.config;

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
@EntityScan("com.tcl.dias.l2oworkflow.entity.entities")
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "l2oworkflowEntityManagerFactory", transactionManagerRef = "l2oworkflowTransactionManager", basePackages = {
		"com.tcl.dias.l2oworkflow.entity.repository" })
public class JpaConfig {

	@Primary
	@Bean(name = "l2oworkflowDataSourceProperties")
    @ConfigurationProperties("hibernate.datasource")
    public DataSourceProperties l2oworkflowDataSourceProperties() {
        return new DataSourceProperties();
    }
	
	
	/**
	 * this bean is used to establish connection. @ return ComboPooledDataSource
	 */
	@Primary
	@Bean(name = "l2oworkflowDataSource")
	@ConfigurationProperties(prefix = "hibernate.datasource")
	public DataSource dataSource() {
		HikariDataSource  hikariDataSource = l2oworkflowDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    	hikariDataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
    	return hikariDataSource;
	} 

	@Primary
	@Bean(name = "l2oworkflowEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean l2oworkflowEntityManagerFactory(
			EntityManagerFactoryBuilder builder, @Qualifier("l2oworkflowDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.tcl.dias.l2oworkflow.entity")
				.persistenceUnit("l2oworkflow").build();
	}
	
	@Bean(name = "l2oworkflowTransactionManager")
	public PlatformTransactionManager l2oworkflowTransactionManager(
			@Qualifier("l2oworkflowEntityManagerFactory") EntityManagerFactory l2oworkflowEntityManagerFactory) {
		return new JpaTransactionManager(l2oworkflowEntityManagerFactory);
	}
	


}
