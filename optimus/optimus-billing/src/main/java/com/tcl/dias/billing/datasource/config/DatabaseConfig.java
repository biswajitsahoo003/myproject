package com.tcl.dias.billing.datasource.config;

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
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
public class DatabaseConfig {
	
	@Bean(name = "financeDataBase")
	@ConfigurationProperties(prefix = "finance.datasource")
	public ComboPooledDataSource financeDataSource() {
		return new ComboPooledDataSource();
	}

	@Bean(name = "financeTemplate")
	public JdbcTemplate financeTemplate(@Qualifier("financeDataBase") DataSource ds) {
		return new JdbcTemplate(ds);
	}

	@Bean(name = "dsInvoices")
	@ConfigurationProperties(prefix = "postgres.datasource")
	public ComboPooledDataSource invoicesDataSource() {
		return new ComboPooledDataSource();
	}

	@Bean(name = "invoicesTemplate")
	public JdbcTemplate invoicesTemplate(@Qualifier("dsInvoices") DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
	@Bean(name = "dsBillingDisputeTickets")
	@ConfigurationProperties(prefix = "ces.db2.datasource")
	public ComboPooledDataSource billingDisputeTicketsDataSource() {
		return new ComboPooledDataSource();
	}

	@Bean(name = "billingDisputeTicketsTemplate")
	public JdbcTemplate billingDisputeTicketsTemplate(@Qualifier("dsBillingDisputeTickets") DataSource ds) {
		return new JdbcTemplate(ds);
	}

	@Bean(name = "dsGBSInvoices")
	@ConfigurationProperties(prefix = "gbs.datasource")
	public ComboPooledDataSource gbsInvoicesDataSource() {
		return new ComboPooledDataSource();
	}

	@Bean(name = "gbsInvoicesTemplate")
	public JdbcTemplate gbsInvoicesTemplate(@Qualifier("dsGBSInvoices") DataSource ds) {
		return new JdbcTemplate(ds);
	}
}