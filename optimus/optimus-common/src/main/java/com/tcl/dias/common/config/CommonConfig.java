package com.tcl.dias.common.config;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.tcl.gvg.exceptionhandler.propertyhandler.PropertyHandler;

/**
 * Property file loader class
 * 
 * @author Manojkumar R
 *
 */
@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class CommonConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Value("${system.proxy.host}")
	String proxyHost;

	@Value("${system.proxy.port}")
	Integer proxyPort;

	/**
	 * propertyInitializer - Initializes a property Handler from a property File.
	 * 
	 * @return Property Handler
	 */
	@Bean
	public PropertyHandler propertyInitializer() {
		PropertyHandler handler = PropertyHandler.getInstance();
		handler.setProperties(exceptionProperty());
		return handler;
	}

	/**
	 * exceptionProperty - Creates a Property based on the property file.
	 * 
	 * @return Property
	 */
	public Properties exceptionProperty() {
		InputStream inputStream = null;
		Properties property = new Properties();
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			inputStream = classloader.getResourceAsStream("exception_messages.properties");
			property.load(inputStream);
			inputStream.close();
		} catch (Exception e) {
			LOGGER.error("Error while loading the properties file. Properties file may not available: {}",
					e.getMessage());
		}
		return property;
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RestTemplate restTemplate() {
		LOGGER.info("RestTemplate got initiated");
		if (StringUtils.isNotBlank(proxyHost) && proxyPort != null) {
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
			requestFactory.setProxy(proxy);
			return new RestTemplate(requestFactory);
		} else {
			return new RestTemplate();
		}
	}

	@Bean
	public ErrorHandler errorHandler() {
		return new ConditionalRejectingErrorHandler(new MyFatalExceptionStrategy());
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setErrorHandler(errorHandler());
		return factory;
	}

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}
}
