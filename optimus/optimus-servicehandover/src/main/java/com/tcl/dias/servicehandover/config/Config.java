package com.tcl.dias.servicehandover.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Config {
	
	@Value("${getCustomer}")
	private String getCustomerOperation;

	@Value("${createOrder}")
	private String createOrderOperation;

	@Value("${invoiceGeneration}")
	private String invoiceGenerationOperation;
	
	@Value("${terminateService}")
	private String terminateServiceOperation;
	
	@Value("${sureAddress}")
	private String sureAddressOperation;
	
	@Value("${intlAccountCreation}")
	private String intlAccountCreateOperation;
	
	@Value("${applyTax}")
	private String applyTaxOperation;
	
	@Bean(name="OrderMarshaller")
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();		
		marshaller.setContextPath("com.tcl.dias.servicehandover.ipc.beans.createorder");
		return marshaller;
	}
	
	@Bean(name="CustomerMarshaller")
	public Jaxb2Marshaller marshaller2() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();		
		marshaller.setContextPath("com.tcl.dias.servicehandover.ipc.beans.customerdata");
		return marshaller;
	}
	
	@Bean(name="InvoiceMarshaller")
	public Jaxb2Marshaller marshaller3() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();		
		marshaller.setContextPath("com.tcl.dias.servicehandover.ipc.beans.invoice");
		return marshaller;
	}
	
	@Bean(name="TerminationMarshaller")
	public Jaxb2Marshaller marshaller4() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();		
		marshaller.setContextPath("com.tcl.dias.servicehandover.bean.lr.termination");
		return marshaller;
	}
	
	@Bean(name="SureAddressMarshaller")
	public Jaxb2Marshaller marshaller5() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();		
		marshaller.setContextPath("com.tcl.dias.servicehandover.beans.sureaddress");
		return marshaller;
	}
	
	@Bean(name="IntlAccountMarshaller")
	public Jaxb2Marshaller marshaller6() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();		
		marshaller.setContextPath("com.tcl.dias.servicehandover.intl.account.beans");
		return marshaller;
	}
	
	@Bean(name="ApplyTaxMarshaller")
	public Jaxb2Marshaller marshaller7() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();		
		marshaller.setContextPath("com.tcl.dias.servicehandover.beans.taxcapture");
		return marshaller;
	}

	@Bean(name="Customer")
	public SOAPConnector retrieveCustomerSoapConnector(@Qualifier("CustomerMarshaller") Jaxb2Marshaller marshaller) {
		SOAPConnector client = new SOAPConnector();
		client.setDefaultUri(getCustomerOperation);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
	
	@Bean(name="Order")
	public SOAPConnector createOrderSoapConnector(@Qualifier("OrderMarshaller") Jaxb2Marshaller marshaller) {
		SOAPConnector client = new SOAPConnector();
		client.setDefaultUri(createOrderOperation);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
	
	@Bean(name="Invoice")
	public SOAPConnector invoiceSoapConnector(@Qualifier("InvoiceMarshaller") Jaxb2Marshaller marshaller) {
		SOAPConnector client = new SOAPConnector();
		client.setDefaultUri(invoiceGenerationOperation);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
	
	@Bean(name="Terminate")
	public SOAPConnector terminateServiceSoapConnector(@Qualifier("TerminationMarshaller") Jaxb2Marshaller marshaller) {
		SOAPConnector client = new SOAPConnector();
		client.setDefaultUri(terminateServiceOperation);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
	
	@Bean(name="SureAddress")
	public SOAPConnector sureAddress(@Qualifier("SureAddressMarshaller") Jaxb2Marshaller marshaller) {
		SOAPConnector client = new SOAPConnector();
		client.setDefaultUri(sureAddressOperation);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
	
	@Bean(name="IntlAccount")
	public SOAPConnector intlAccount(@Qualifier("IntlAccountMarshaller") Jaxb2Marshaller marshaller) {
		SOAPConnector client = new SOAPConnector();
		client.setDefaultUri(intlAccountCreateOperation);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
	
	@Bean(name="ApplyTax")
	public SOAPConnector applyTax(@Qualifier("ApplyTaxMarshaller") Jaxb2Marshaller marshaller) {
		SOAPConnector client = new SOAPConnector();
		client.setDefaultUri(applyTaxOperation);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
