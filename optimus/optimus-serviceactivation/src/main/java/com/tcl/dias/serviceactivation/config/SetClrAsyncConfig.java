package com.tcl.dias.serviceactivation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
public class SetClrAsyncConfig {

	@Bean(name = "setclrasync")
	public SimpleWsdl11Definition defaultGetClrInfoWsdl11Definition() throws Exception {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/setclrasync/setclrasync.wsdl"));
		return wsdl11Definition;
	}

	@Bean(name = "SetCLRResponseInterface")
	public SimpleWsdl11Definition defaultCramerGetCLRResponse() throws Exception {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(
				new ClassPathResource("wsdl/cramer/setclrasync/SetCLRResponseInterface.wsdl"));
		return wsdl11Definition;
	}

	@Bean(name = "responseBO")
	public XsdSchema broadcast() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/setclrasync/responseBO.xsd"));
	}
}
