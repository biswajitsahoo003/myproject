package com.tcl.dias.oms.config;

import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

/**
 * This file contains the SOAPConnector.java class.
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class SOAPConnector extends WebServiceGatewaySupport {

	@Value("${system.proxy.host}")
	private String proxyIp;

	@Value("${system.proxy.port}")
	private int proxyPort;

	private static final Logger logger = LoggerFactory.getLogger(SOAPConnector.class);

	/**
	 * Method to call the web service with headers.
	 * @param url
	 * @param request
	 * @param webServiceMessageCallback
	 * @return
	 */
	public Object webServiceWithCallAction(String url, Object request, WebServiceMessageCallback webServiceMessageCallback) {
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor());
		HttpHost proxy = new HttpHost(proxyIp, proxyPort);
		builder.setProxy(proxy);

		CloseableHttpClient httpClient = builder.build();
		HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender(httpClient);

		WebServiceTemplate wsTemplate = this.getWebServiceTemplate();
		wsTemplate.setMessageSender(messageSender);
		return wsTemplate.marshalSendAndReceive(url, request ,webServiceMessageCallback);
	}

}