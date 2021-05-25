package com.tcl.dias.serviceactivation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * This class has methods to expose offnet API details endpoints
 * 
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Configuration
public class OffnetDetailsConfig {

	@Bean(name = "offnetdetails")
	public SimpleWsdl11Definition defaultRadwinHandlingWsdl11Definition() throws Exception {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource(
				"wsdl/offnetdetails/offnetdetails.wsdl"));
		return wsdl11Definition;
	}

	@Bean(name = "TSReservation")
	public SimpleWsdl11Definition defaultTsReservationWsdl11Definition() throws Exception {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("wsdl/offnetdetails/TSReservation.wsdl"));
		return wsdl11Definition;
	}

	@Bean(name = "AcknowledgementOffnet")
	public XsdSchema acknowledgementRadwin() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/AcknowledgementOffnet.xsd"));
	}

	@Bean(name = "offnetInterface")
	public XsdSchema offnetInterface() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/offnetInterface.xsd"));
	}
	
	@Bean(name = "protectionOffnetInterface")
	public XsdSchema protectionOffnetInterface() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/protectionOffnetInterface.xsd"));
	}
	
	@Bean(name = "requestHeader")
	public XsdSchema requestHeader() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/requestHeader.xsd"));
	}
	
	@Bean(name = "ReserveTSTally")
	public XsdSchema reserveTSTally() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/ReserveTSTally.xsd"));
	}
	
	@Bean(name = "ReserveTSTallyResponse")
	public XsdSchema reserveTSTallyResponse() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/ReserveTSTallyResponse.xsd"));
	}
		
	@Bean(name = "setElectricalHandOffDetails")
	public XsdSchema setElectricalHandOffDetails() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/setElectricalHandOffDetails.xsd"));
	}
	
	@Bean(name = "setElectricalHandOffDetailsResponse")
	public XsdSchema setElectricalHandOffDetailsResponse() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/setElectricalHandOffDetailsResponse.xsd"));
	}
	
	@Bean(name = "submitTSSelectionFault")
	public XsdSchema submitTSSelectionFault() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/submitTSSelectionFault.xsd"));
	}
	
	@Bean(name = "submitTSSelectionFaultResponse")
	public XsdSchema submitTSSelectionFaultResponse() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/submitTSSelectionFaultResponse.xsd"));
	}
	@Bean(name = "timeSlot")
	public XsdSchema timeSlot() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/timeSlot.xsd"));
	}
	
	@Bean(name = "timeSlotSubmitionFault")
	public XsdSchema timeSlotSubmitionFault() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/timeSlotSubmitionFault.xsd"));
	}
	
	@Bean(name = "workerOffnetInterface")
	public XsdSchema workerOffnetInterface() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/offnetdetails/workerOffnetInterface.xsd"));
	}
}
