package com.tcl.dias.networkaugment.entity.config;

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
 * @author Samuel.S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@EntityScan("com.tcl.dias.networkaugment.entity.entities")
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "networkAugmentationEntityManagerFactory", transactionManagerRef = "networkAugmentationTransactionManager", basePackages = {
		"com.tcl.dias.networkaugment.entity.repository" })
public class JpaConfig {

	@Primary
	@Bean(name = "networkAugmentationDataSourceProperties")
    @ConfigurationProperties("hibernate.datasource")
    public DataSourceProperties networkAugmentationDataSourceProperties() {
        return new DataSourceProperties();
    }
	
	
	/**
	 * this bean is used to establish connection. @ return ComboPooledDataSource
	 */
	@Primary
	@Bean(name = "networkAugmentationDataSource")
	@ConfigurationProperties(prefix = "hibernate.datasource")
	public DataSource dataSource() {
		HikariDataSource  hikariDataSource = networkAugmentationDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    	hikariDataSource.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
    	return hikariDataSource;
	}
	
	
	
	/*@Primary
	@Bean(name = "networkAugmentationDataSource")
	@ConfigurationProperties("hibernate.datasource")
	public ComboPooledDataSource dataSource() {
		return new ComboPooledDataSource();
	}*/    

	@Primary
	@Bean(name = "networkAugmentationEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean networkAugmentationEntityManagerFactory(
			EntityManagerFactoryBuilder builder, @Qualifier("networkAugmentationDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.tcl.dias.networkaugment.entity")
				.persistenceUnit("networkaugmentation").build();
	}
	
	@Bean(name = "networkAugmentationTransactionManager")
	public PlatformTransactionManager networkAugmentationTransactionManager(
			@Qualifier("networkAugmentationEntityManagerFactory") EntityManagerFactory networkAugmentationEntityManagerFactory) {
		return new JpaTransactionManager(networkAugmentationEntityManagerFactory);
	}
	


}
