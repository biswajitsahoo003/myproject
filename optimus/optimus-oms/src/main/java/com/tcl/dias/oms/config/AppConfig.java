package com.tcl.dias.oms.config;

import java.lang.invoke.MethodHandles;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


/**
 * AppConfig class contains configurations to connect with provided datasource
 * 
 * @author AnneF 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@Configuration
public class AppConfig {
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	
	@Value("${hibernate.datasource.mdmlake.user}")
	private String mdmLakeUser;

	@Value("${hibernate.datasource.mdmlake.jdbc-url}")
	private String mdmLakeUrl;

	@Value("${hibernate.datasource.mdmlake.password}")
	private String mdmLakePassword;
	
	@Value("${hibernate.datasource.driver-class}")
	private String mysqlDriver;
	
	@Value("${hibernate.datasource.user}")
	private String omsUser;

	@Value("${hibernate.datasource.jdbc-url}")
	private String omsUrl;

	@Value("${hibernate.datasource.password}")
	private String omsPassword;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public JdbcTemplate jdbcTemplateMDMLake(@Qualifier("jdbcMDMLake") DataSource dataSourceMysqlOms) {
		return new JdbcTemplate(dataSourceMysqlOms);
	}	
	
	@Bean(name = "jdbcMDMLake")
	@ConfigurationProperties("hibernate.datasource.mdmlake")
	DataSource dataSourceMysqlOms() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setUrl(mdmLakeUrl);
		driverManagerDataSource.setUsername(mdmLakeUser);
		driverManagerDataSource.setPassword(mdmLakePassword);
		driverManagerDataSource.setDriverClassName(mysqlDriver);
		return driverManagerDataSource;
	}
	
	@Primary
	@Bean
	@ConfigurationProperties("hibernate.datasource")
	DataSource dataSourceOmsDefault() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setUrl(omsUrl);
		driverManagerDataSource.setUsername(omsUser);
		driverManagerDataSource.setPassword(omsPassword);
		driverManagerDataSource.setDriverClassName(mysqlDriver);
		return driverManagerDataSource;
	}

	@Bean(name="WebexMarshaller")
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.tcl.dias.oms.cisco.beans");
		return marshaller;
	}

	@Bean(name="WebexSoapConnector")
	public SOAPConnector webex(@Qualifier("WebexMarshaller") Jaxb2Marshaller marshaller) {
		SOAPConnector client = new SOAPConnector();
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
