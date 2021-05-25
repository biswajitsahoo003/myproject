package com.tcl.dias.servicehandover.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

public class SOAPConnector extends WebServiceGatewaySupport {

	@Value("${soap.read.timeout:300000}")
	private int readTimeout;

	@Value("${soap.connection.timeout:300000}")
	private int connectionTimeout;
	
	@Value("${system.proxy.host}")
	private String proxyIp;
	
	@Value("${system.proxy.port}")
	private int proxyPort;
    
	/*
	 * public Object callWebService(String url, Object request){ return
	 * getWebServiceTemplate().marshalSendAndReceive(url, request); }
	 */
	private static final Logger logger = LoggerFactory.getLogger(SOAPConnector.class);

	public Object callWebService(String endpoint, Object requestPayload) {

		WebServiceTemplate wsTemplate = this.getWebServiceTemplate();
		try {
			logger.info("setting time out for {} secs", readTimeout / 1000);
			HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
			messageSender.setConnectionTimeout(connectionTimeout);
			messageSender.setReadTimeout(readTimeout);
			wsTemplate.setMessageSender(messageSender);
		} catch (ClassCastException | NumberFormatException cex) {
			logger.warn("Cannot set WS timeout: " + cex.getMessage());
		}

		return wsTemplate.marshalSendAndReceive(endpoint, requestPayload);

	}

	public Object webServiceWithCallAction(String url, Object request, SoapActionCallback actionCallback) {
		HttpClientBuilder builder = HttpClientBuilder.create();
        builder.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());
        HttpHost proxy = new HttpHost(proxyIp, proxyPort);
        builder.setProxy(proxy);
        
        CloseableHttpClient httpClient = builder.build();
        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(httpClient);
        
		WebServiceTemplate wsTemplate = this.getWebServiceTemplate();
		wsTemplate.setMessageSender(messageSender);
		return wsTemplate.marshalSendAndReceive(url, request, actionCallback);
	}

}