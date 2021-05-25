package com.tcl.dias.oms.webex.component;

import org.apache.http.client.methods.HttpPost;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.axiom.AxiomSoapMessage;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.ClientHttpRequestConnection;
import org.springframework.ws.transport.http.HttpComponentsConnection;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * This file contains the WebexHttpHeaderCallback.java class.
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class WebexHttpHeaderCallback implements WebServiceMessageCallback {
	private String headerKey;
	private String headerValue;
	private String soapAction;

	public WebexHttpHeaderCallback(String headerKey, String headerValue, String soapAction) {
		super();
		this.headerKey = headerKey;
		this.headerValue = headerValue;
		this.soapAction = soapAction;
	}

	public WebexHttpHeaderCallback() {
		super();
	}

	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
		addRequestHeader(headerKey, headerValue);
		if (StringUtils.hasText(this.soapAction)) {
			AxiomSoapMessage axiomMessage = (AxiomSoapMessage) message;
			axiomMessage.setSoapAction(this.soapAction);
		}
	}

	private void addRequestHeader(String headerKey, String headerValue) {
		TransportContext context = TransportContextHolder.getTransportContext();
		WebServiceConnection connection = context.getConnection();
		if (connection instanceof HttpComponentsConnection) {
			HttpComponentsConnection conn = (HttpComponentsConnection) connection;
			HttpPost post = conn.getHttpPost();
			post.addHeader(headerKey, headerValue);
		} else if (connection instanceof ClientHttpRequestConnection) {
			ClientHttpRequestConnection conn = (ClientHttpRequestConnection) connection;
			conn.getClientHttpRequest().getHeaders().add(headerKey, headerValue);
		}
	}
}
