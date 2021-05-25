package com.tcl.dias.serviceactivation.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}

	@Bean(name = "getmuxinfoasync")
	public SimpleWsdl11Definition defaultWsdl11Definition() throws Exception {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/getmuxinfo/getmuxinfoasync.wsdl"));
		return wsdl11Definition;
	}	
	@Bean(name = "GetMuxConsumerWS")
	public SimpleWsdl11Definition getMuxConsumerWS() throws Exception {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/getmuxinfo/GetMuxConsumerWS.wsdl"));
		return wsdl11Definition;
	}
	@Bean(name = "getMuxInfo")
	public XsdSchema getMuxInfo() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getmuxinfo/getMuxInfo.xsd"));
	}	
	@Bean(name = "getMuxInfoAck")
	public XsdSchema getMuxInfoAck() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getmuxinfo/getMuxInfoAck.xsd"));
	}	
	@Bean(name = "muxDetail")
	public XsdSchema muxDetail() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getmuxinfo/muxDetail.xsd"));
	}		
	@Bean(name = "submitMuxDetailFailureResponseAck")
	public XsdSchema submitMuxDetailFailureResponseAck() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getmuxinfo/submitMuxDetailFailureResponseAck.xsd"));
	}
    @Bean(name = "submitMuxDetailFailureResponse")
	public XsdSchema submitMuxDetailFailureResponse() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getmuxinfo/submitMuxDetailFailureResponse.xsd"));
	}
    @Bean(name = "muxDetailFailureResponse")
	public XsdSchema muxDetailFailureResponse() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getmuxinfo/muxDetailFailureResponse.xsd"));
	}
    @Bean(name = "Acknowledgement")
    public XsdSchema Acknowledgement() throws Exception {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getmuxinfo/Acknowledgement.xsd"));
    }
    /************getMuxInfo ends**************/

    /************clrcreationdetails begins**************/

	@Bean(name = "clrcreationdetails")
	public SimpleWsdl11Definition clrcreationdetails() {
		SimpleWsdl11Definition simpleWsdl11Definition = new SimpleWsdl11Definition();
		simpleWsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/createclrasync/clrcreationdetails.wsdl"));
		return simpleWsdl11Definition;
	}
    @Bean(name = "clrcreationdetails_1")
	public SimpleWsdl11Definition clrcreationdetails_1() {
		SimpleWsdl11Definition simpleWsdl11Definition = new SimpleWsdl11Definition();
		simpleWsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/createclrasync/clrcreationdetails_1.wsdl"));
		return simpleWsdl11Definition;
	}

    @Bean(name = "ClrAcknowledgement")
    public XsdSchema ClrAcknowledgement() throws Exception {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/createclrasync/ClrAcknowledgement.xsd"));
    }

    @Bean(name = "clrattributes")
    public XsdSchema clrattributes() throws Exception {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/createclrasync/clrattributes.xsd"));
    }

	@Bean(name = "CLRCreationDetails")
	public XsdSchema CLRCreationDetails() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/createclrasync/CLRCreationDetails.xsd"));
	}
	@Bean(name = "ClrAttrs")
	public XsdSchema ClrAttrs() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/createclrasync/ClrAttrs.xsd"));
	}	
	@Bean(name = "RespAttr")
	public XsdSchema RespAttr() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/createclrasync/RespAttr.xsd"));
	}
	@Bean(name = "CLRCreationDetailsAcknowledgement")
	public XsdSchema CLRCreationDetailsAcknowledgement() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/createclrasync/CLRCreationDetailsAcknowledgement.xsd"));
	}
	
	@Bean(name = "CLRcreationFailureResponse")
	public XsdSchema CLRcreationFailureResponse() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/createclrasync/CLRcreationFailureResponse.xsd"));
	}
	
	@Bean(name = "SubmitCLRCreationFailureResponse")
	public XsdSchema SubmitCLRCreationFailureResponse() throws Exception {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/createclrasync/SubmitCLRCreationFailureResponse.xsd"));
	}	
		
	/************clrcreationdetails ends**************/


	/************getHDConfig begins**************/

	@Bean(name = "getHDConfig")
	public SimpleWsdl11Definition getHdConfig() {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/gethdconfigdetailsasync/getHDConfig.wsdl"));
		return wsdl11Definition;
	}

	@Bean(name = "GetHDCONFIGDetails")
	public SimpleWsdl11Definition getHdConfigDetails() {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/gethdconfigdetailsasync/GetHDCONFIGDetails.wsdl"));
		return wsdl11Definition;
	}

	@Bean(name = "HDAcknowledgement")
	public XsdSchema AcknowledgementHdConfig(){
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/gethdconfigdetailsasync/HDAcknowledgement.xsd"));
	}

	@Bean(name = "HDCONFIGAttrs")
	public XsdSchema HDCONFIGAttrs() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/gethdconfigdetailsasync/HDCONFIGAttrs.xsd"));
	}

	@Bean(name = "HDCONFIGDetails")
	public XsdSchema HDCONFIGDetails() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/gethdconfigdetailsasync/HDCONFIGDetails.xsd"));
	}
	
	@Bean(name = "HDAttributes")
	public XsdSchema attributes() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/gethdconfigdetailsasync/HDAttributes.xsd"));
	}

	@Bean(name = "HDCONFIGDetailsAcknowledgement")
	public XsdSchema HDCONFIGDetailsAcknowledgement() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/gethdconfigdetailsasync/HDCONFIGDetailsAcknowledgement.xsd"));
	}

	@Bean(name = "HDCONFIGFailureResponse")
	public XsdSchema HDCONFIGFailureResponse() {
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/gethdconfigdetailsasync/HDCONFIGFailureResponse.xsd"));
	}

	@Bean(name = "SubmitHDCONFIGFailureResponse")
	public XsdSchema SubmitHDCONFIGFailureResponse(){
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/gethdconfigdetailsasync/SubmitHDCONFIGFailureResponse.xsd"));
	}


	/************getHDConfig ends**************/


	/************downtimeasync begins**************/
	
	
	
	@Bean(name = "downtimeasync")
	public SimpleWsdl11Definition downTimeAsync(){
		SimpleWsdl11Definition simpleWsdl11Definition = new SimpleWsdl11Definition();
		simpleWsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/downtimeasync/downtimeasync.wsdl"));
		return simpleWsdl11Definition;
	}
	
	@Bean(name = "GetServiceDowntimeDetails")
	public SimpleWsdl11Definition getServiceDowntimeDetails() throws Exception {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/downtimeasync/GetServiceDowntimeDetails.wsdl"));
		return wsdl11Definition;
	}	

	@Bean(name = "DowntimeAcknowledgement")
	public XsdSchema DowntimeAsyncAcknowledgement(){
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/downtimeasync/DowntimeAcknowledgement.xsd"));
	}

	@Bean(name = "GetServiceDowntimeDetails")
	public SimpleWsdl11Definition GetServiceDowntimeDetails(){
		SimpleWsdl11Definition simpleWsdl11Definition = new SimpleWsdl11Definition();
		simpleWsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/downtimeasync/GetServiceDowntimeDetails.wsdl"));
		return simpleWsdl11Definition;
	}

	@Bean(name = "RequestHeader")
	public XsdSchema RequestHeader(){
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/downtimeasync/RequestHeader.xsd"));
	}

	@Bean(name = "ServiceDownDtls")
	public XsdSchema ServiceDownDtls(){
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/downtimeasync/ServiceDownDtls.xsd"));
	}

	@Bean(name = "ServiceDownDtlsResponse")
	public XsdSchema ServiceDownDtlsResponse(){
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/downtimeasync/ServiceDownDtlsResponse.xsd"));
	}

	@Bean(name = "ServiceDowntimeSubmitionFault")
	public XsdSchema ServiceDowntimeSubmitionFault(){
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/downtimeasync/ServiceDowntimeSubmitionFault.xsd"));
	}

	@Bean(name = "SubmitServiceDowntimeFault")
	public XsdSchema SubmitServiceDowntimeFault(){
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/downtimeasync/SubmitServiceDowntimeFault.xsd"));
	}

	@Bean(name = "SubmitServiceDowntimeFaultResponse")
	public XsdSchema SubmitServiceDowntimeFaultResponse(){
		return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/downtimeasync/SubmitServiceDowntimeFaultResponse.xsd"));
	}

}