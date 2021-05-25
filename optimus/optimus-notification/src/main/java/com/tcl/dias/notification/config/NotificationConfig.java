package com.tcl.dias.notification.config;

import java.lang.invoke.MethodHandles;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.tcl.dias.common.constants.CommonConstants;

/**
 * Property file loader class
 * 
 * @author Manojkumar R
 *
 */
@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class NotificationConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Value("${system.proxy.host}")
	String proxyHost;

	@Value("${system.proxy.port}")
	Integer proxyPort;

	@PostConstruct
	public void initIt() throws Exception {
		if (StringUtils.isNotBlank(proxyHost) && proxyPort != null) {
			LOGGER.info("Initiating the proxy for Docusign");
			System.setProperty("https.proxyHost", proxyHost);
			System.setProperty("https.proxyPort", proxyPort + CommonConstants.EMPTY);
			System.setProperty("http.proxyHost", proxyHost);
			System.setProperty("http.proxyPort", proxyPort + CommonConstants.EMPTY);
		}
	}
}
