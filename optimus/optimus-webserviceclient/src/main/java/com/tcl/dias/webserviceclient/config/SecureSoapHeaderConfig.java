package com.tcl.dias.webserviceclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

/**
 * SecureSoapHeaderConfig Class is used for SOAP WebService configuration
 * 
 *
 * @author SAMUEL S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@Configuration
public class
SecureSoapHeaderConfig {
	
	private @Value("${soap.username:}") String soapUserName;
	private @Value("${soap.pwd:}") String soapPwd;
	private @Value("${soap.connection.timeout:45000}") int connectionTimeout;
	private @Value("${soap.read.timeout:45000}") int readTimeout;
	
	@Bean
	public Wss4jSecurityInterceptor securityInterceptor() {
		SimplePasswordValidationCallbackHandler callbackHandler = new SimplePasswordValidationCallbackHandler();
		Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
		wss4jSecurityInterceptor.setSecurementMustUnderstand(false);
		wss4jSecurityInterceptor.setValidationCallbackHandler(callbackHandler);
		wss4jSecurityInterceptor.setValidateRequest(false);
		wss4jSecurityInterceptor.setValidateResponse(false);
		wss4jSecurityInterceptor.setSecurementActions("UsernameToken");
		wss4jSecurityInterceptor.setSecurementUsername(soapUserName);
		wss4jSecurityInterceptor.setSecurementPassword(soapPwd);
		
		return wss4jSecurityInterceptor;
	}
	
	@Bean
	public HttpComponentsMessageSender httpComponentsMessageSender() {
		HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
		httpComponentsMessageSender.setReadTimeout(readTimeout);
		httpComponentsMessageSender.setConnectionTimeout(connectionTimeout);
		return httpComponentsMessageSender;
	}
	
	@Bean
	public SaajSoapMessageFactory soap12MessageFactory() {
		SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory();
		saajSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
		saajSoapMessageFactory.afterPropertiesSet();
		return saajSoapMessageFactory;
	}
	
	@Bean
	public SaajSoapMessageFactory soap11MessageFactory() {
		SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory();
		saajSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_11);
		saajSoapMessageFactory.afterPropertiesSet();
		return saajSoapMessageFactory;
	}

}
