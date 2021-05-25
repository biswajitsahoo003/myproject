package com.tcl.dias.serviceactivation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
public class GetClrAsyncConfig {

    @Bean(name = "getclrasync")
    public SimpleWsdl11Definition defaultGetClrInfoWsdl11Definition() throws Exception {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/getclrasync/getclrasync.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "CramerGetCLRResponse")
    public SimpleWsdl11Definition defaultCramerGetCLRResponse() throws Exception {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/getclrasync/CramerGetCLRResponse.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "Broadcast")
    public XsdSchema broadcast() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/Broadcast.xsd"));
    }


    @Bean(name = "CienaParam")
    public XsdSchema cienaParam() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/CienaParam.xsd"));
    }


    @Bean(name = "CramerServiceErrorDetails")
    public XsdSchema cramerServiceErrorDetails() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/CramerServiceErrorDetails.xsd"));
    }

    @Bean(name = "CramerServiceHeader")
    public XsdSchema cramerServiceHeader() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/CramerServiceHeader.xsd"));
    }

    @Bean(name = "CramerTxServiceRequestFailure")
    public XsdSchema cramerTxServiceRequestFailure() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/CramerTxServiceRequestFailure.xsd"));
    }

    @Bean(name = "Customer")
    public XsdSchema customer() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/Customer.xsd"));
    }

    @Bean(name = "L2Params")
    public XsdSchema lParams() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/L2Params.xsd"));
    }

    @Bean(name = "NodeToBeConfigured")
    public XsdSchema nodeToBeConfigured() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/NodeToBeConfigured.xsd"));
    }

    @Bean(name = "OrderInfo")
    public XsdSchema orderInfo() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/OrderInfo.xsd"));
    }

    @Bean(name = "Protection")
    public XsdSchema protection() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/Protection.xsd"));
    }

    @Bean(name = "Topology")
    public XsdSchema topology() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/Topology.xsd"));
    }

    @Bean(name = "TopologyMember")
    public XsdSchema TopologyMember() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/TopologyMember.xsd"));
    }

    @Bean(name = "TransmissionService")
    public XsdSchema transmissionService() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/TransmissionService.xsd"));
    }

    @Bean(name = "Worker")
    public XsdSchema worker() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getclrasync/Worker.xsd"));
    }


}
