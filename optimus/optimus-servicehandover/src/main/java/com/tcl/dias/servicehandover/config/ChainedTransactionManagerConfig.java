package com.tcl.dias.servicehandover.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


/**
 *ChainedTransactionManager for handling multiple transaction
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Configuration
public class ChainedTransactionManagerConfig {

	@Bean(name = "chainedTransactionManager")
	@Primary
	public ChainedTransactionManager transactionManager(

			@Qualifier("flowable") PlatformTransactionManager flowable,
			@Qualifier("servicefulfillmentTransactionManager") PlatformTransactionManager servicefulfillmentTransactionManager,@Qualifier("serviceActivationTransactionManager") PlatformTransactionManager serviceActivationTransactionManager) {
		return new ChainedTransactionManager(flowable, servicefulfillmentTransactionManager);
	}
}
