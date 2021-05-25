package com.tcl.dias.webserviceclient.service.impl;

import java.io.ByteArrayInputStream;
import java.net.URL;

import org.apache.axis.client.Call;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import com.tcl.dias.webserviceclient.beans.SoapRequest;
import com.tcl.dias.webserviceclient.config.SecureSoapHeaderConfig;
import com.tcl.dias.webserviceclient.service.GenericWebserviceClient;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * GenericWebserviceClientImpl Class is used for all the SOAP calls
 * 
 *
 * @author SAMUEL S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@Service
public class GenericWebserviceClientImpl implements GenericWebserviceClient {
	private static final Logger logger = LoggerFactory.getLogger(GenericWebserviceClientImpl.class);
	
	@Autowired
	private SecureSoapHeaderConfig soapConfig;	

	@SuppressWarnings("unchecked")
	@Override
	public <T> T doSoapCallForObject(SoapRequest soapRequest, Class<T> clazz) {
		T response = null;
		try {
		
            WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
			
			if("11".equals(soapRequest.getSoapVersion()))webServiceTemplate.setMessageFactory(soapConfig.soap11MessageFactory());
			else webServiceTemplate.setMessageFactory(soapConfig.soap12MessageFactory());
			
			HttpComponentsMessageSender messageSender = soapConfig.httpComponentsMessageSender();
			
			if(soapRequest.getConnectionTimeout() !=0)messageSender.setConnectionTimeout(soapRequest.getConnectionTimeout());
			if(soapRequest.getReadTimeout() !=0)messageSender.setReadTimeout(soapRequest.getReadTimeout());
			webServiceTemplate.setMessageSender(messageSender);
			
			Wss4jSecurityInterceptor wss4jSecurityInterceptor = soapConfig.securityInterceptor();
			
			if("plain".equals(soapRequest.getPType()))wss4jSecurityInterceptor.setSecurementPasswordType("PasswordText");
			if(StringUtils.isNoneBlank(soapRequest.getSoapUserName()))wss4jSecurityInterceptor.setSecurementUsername(soapRequest.getSoapUserName());
			if(StringUtils.isNoneBlank(soapRequest.getSoapPwd()))wss4jSecurityInterceptor.setSecurementPassword(soapRequest.getSoapPwd());
			
			
			webServiceTemplate.setInterceptors(new ClientInterceptor[] {wss4jSecurityInterceptor});
			
			Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
			logger.info("getContextPath={}=>URL::{}",soapRequest.getContextPath(),soapRequest.getUrl());
			marshaller.setContextPath(soapRequest.getContextPath());
			webServiceTemplate.setMarshaller(marshaller);
			webServiceTemplate.setUnmarshaller(marshaller);
			webServiceTemplate.afterPropertiesSet();
		
				response = (T) webServiceTemplate.marshalSendAndReceive(soapRequest.getUrl(),
						soapRequest.getRequestObject(),
						new SoapActionCallback(soapRequest.getSoapActionUri()));
		}catch(Exception ex) {
			logger.error("Exception-genericwebserviceclientpath={}=>URL::{}",soapRequest.getContextPath(),soapRequest.getUrl());
			logger.error("Exception in generic web service client ", ex);
			throw ex;
		}
	
		return response;
	}
	
	@Override
	public String doSoapCallForString(SoapRequest soapRequest) throws TclCommonException  {
		String response = "";
		try {
			String request = soapRequest.getRequestString();
			
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();
			Call call = (Call) service.createCall();
			call.setUseSOAPAction(true);
			if(StringUtils.isNoneBlank(soapRequest.getSoapActionUri()))call.setSOAPActionURI(soapRequest.getSoapActionUri());
			call.setTargetEndpointAddress(new URL(soapRequest.getUrl()));
			call.setTimeout(soapRequest.getReadTimeout());
			
			SOAPEnvelope env  = new SOAPEnvelope(new ByteArrayInputStream(request.getBytes()));
			Object result = call.invoke(env);
			
			if(result!=null) response = String.valueOf(result);
			
			response = response.replaceAll("&quot;", "\"")
					.replaceAll("&apos;", "\'")
					.replaceAll("&lt;", "<")
					.replaceAll("&gt;", ">");
			
			if(StringUtils.isNotBlank(soapRequest.getStartTag()) && StringUtils.isNotBlank(soapRequest.getEndTag())) {
				response = parseBody(response,soapRequest.getStartTag(),soapRequest.getEndTag());
			}
		}catch(Exception ex) {
			throw new TclCommonException(ex);
		}
		return response;
	}
	
	private String parseBody(String respXml,String startTag, String endTag) {
		if(respXml.indexOf(startTag)>=0 && respXml.indexOf(endTag)>=0) {
			return respXml.substring(respXml.indexOf(startTag), respXml.indexOf(endTag));
		}
		return respXml;
	}

}
