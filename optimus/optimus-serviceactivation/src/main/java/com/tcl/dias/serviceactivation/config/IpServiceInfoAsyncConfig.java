package com.tcl.dias.serviceactivation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
public class IpServiceInfoAsyncConfig {

    @Bean(name = "getipserviceasync")
    public SimpleWsdl11Definition getIpServiceAsyncWsdl11Definition() {
        SimpleWsdl11Definition simpleWsdl11Definition = new SimpleWsdl11Definition();
        simpleWsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/getipserviceasync/getipserviceasync.wsdl"));
        return simpleWsdl11Definition;
    }

    @Bean(name = "ACE_IAS_Crammer_Response_Module_CramerIPServiceResponseExport1")
    public SimpleWsdl11Definition defaultWsdl11Definition() throws Exception {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("wsdl/cramer/getipserviceasync/ACE_IAS_Crammer_Response_Module_CramerIPServiceResponseExport1.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "AddOnServices")
    public XsdSchema addOnServices() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/AddOnServices.xsd"));
    }

    @Bean(name = "Attribute")
    public XsdSchema attribute() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/Attribute.xsd"));
    }

    @Bean(name = "APSRouter")
    public XsdSchema aPSRouter() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/APSRouter.xsd"));
    }

    @Bean(name = "BGP")
    public XsdSchema bgp() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/BGP.xsd"));
    }

    @Bean(name = "clrInfoResponse")
    public XsdSchema clrInfoResponse() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/clrInfoResponse.xsd"));
    }

    @Bean(name = "CramerCPE")
    public XsdSchema cramerCPE() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerCPE.xsd"));
    }

    @Bean(name = "CramerCPE_imports_1")
    public XsdSchema cramerCPE_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerCPE_imports_1.xsd"));
    }

    @Bean(name = "CramerDMVPNService")
    public XsdSchema cramerDMVPNService() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerDMVPNService.xsd"));
    }

    @Bean(name = "CramerDMVPNService_imports_1")
    public XsdSchema cramerDMVPNService_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerDMVPNService_imports_1.xsd"));
    }

    @Bean(name = "CramerDMVPNService_imports_2")
    public XsdSchema cramerDMVPNService_imports_2() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerDMVPNService_imports_2.xsd"));
    }

    @Bean(name = "CramerEthernetInterface")
    public XsdSchema cramerEthernetInterface() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerEthernetInterface.xsd"));
    }

    @Bean(name = "CramerEthernetInterface_imports_1")
    public XsdSchema cramerEthernetInterface_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerEthernetInterface_imports_1.xsd"));
    }

    @Bean(name = "CramerEthernetTopologyInfo")
    public XsdSchema cramerEthernetTopologyInfo() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerEthernetTopologyInfo.xsd"));
    }

    @Bean(name = "CramerIASService")
    public XsdSchema cramerIASService() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerIASService.xsd"));
    }


    @Bean(name = "CramerIASService_imports_1")
    public XsdSchema cramerIASService_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerIASService_imports_1.xsd"));
    }

    @Bean(name = "CramerInterface")
    public XsdSchema cramerInterface() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerInterface.xsd"));
    }

    @Bean(name = "CramerInterface_imports_1")
    public XsdSchema cramerInterface_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerInterface_imports_1.xsd"));
    }

    @Bean(name = "CramerIPSECService")
    public XsdSchema cramerIPSECService() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerIPSECService.xsd"));
    }

    @Bean(name = "CramerIPSECService_imports_1")
    public XsdSchema cramerIPSECService_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerIPSECService_imports_1.xsd"));
    }

    @Bean(name = "CramerIPSECService_imports_2")
    public XsdSchema cramerIPSECService_imports_2() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerIPSECService_imports_2.xsd"));
    }

    @Bean(name = "CramerIPServiceInfo")
    public XsdSchema cramerIPServiceInfo() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerIPServiceInfo.xsd"));
    }

    @Bean(name = "CramerIPServiceRequestFailure")
    public XsdSchema cramerIPServiceRequestFailure() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerIPServiceRequestFailure.xsd"));
    }

    @Bean(name = "CramerISDNInterface")
    public XsdSchema cramerISDNInterface() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerISDNInterface.xsd"));
    }

    @Bean(name = "CramerISDNInterface_imports_1")
    public XsdSchema cramerISDNInterface_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerISDNInterface_imports_1.xsd"));
    }

    @Bean(name = "CramerL2VPNService")
    public XsdSchema cramerL2VPNService() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerL2VPNService.xsd"));
    }

    @Bean(name = "CramerL2VPNService_imports_1")
    public XsdSchema cramerL2VPNService_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerL2VPNService_imports_1.xsd"));
    }

    @Bean(name = "CramerL2VPNService_imports_2")
    public XsdSchema cramerL2VPNService_imports_2() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerL2VPNService_imports_2.xsd"));
    }

    @Bean(name = "CramerL3NNILastmile")
    public XsdSchema cramerL3NNILastmile() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerL3NNILastmile.xsd"));
    }

    @Bean(name = "CramerL3NNILastmile_imports_1")
    public XsdSchema cramerL3NNILastmile_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerL3NNILastmile_imports_1.xsd"));
    }

    @Bean(name = "CramerLNSDetails")
    public XsdSchema cramerLNSDetails() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerLNSDetails.xsd"));
    }

    @Bean(name = "CramerLNSDetails_imports_1")
    public XsdSchema cramerLNSDetails_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerLNSDetails_imports_1.xsd"));
    }

    @Bean(name = "CramerLNSDetails_imports_2")
    public XsdSchema cramerLNSDetails_imports_2() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerLNSDetails_imports_2.xsd"));
    }

    @Bean(name = "CramerRWService")
    public XsdSchema cramerRWService() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerRWService.xsd"));
    }

    @Bean(name = "CramerRWService_imports_1")
    public XsdSchema cramerRWService_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerRWService_imports_1.xsd"));
    }

    @Bean(name = "CramerRWService_imports_2")
    public XsdSchema cramerRWService_imports_2() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerRWService_imports_2.xsd"));
    }

    @Bean(name = "CramerSDHInterface")
    public XsdSchema cramerSDHInterface() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerSDHInterface.xsd"));
    }

    @Bean(name = "CramerSDHInterface_imports_1")
    public XsdSchema cramerSDHInterface_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerSDHInterface_imports_1.xsd"));
    }

    @Bean(name = "CramerSerialInterface")
    public XsdSchema cramerSerialInterface() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerSerialInterface.xsd"));
    }

    @Bean(name = "CramerSerialInterface_imports_1")
    public XsdSchema cramerSerialInterface_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerSerialInterface_imports_1.xsd"));
    }

    @Bean(name = "CramerServiceErrorDetails")
    public XsdSchema cramerServiceErrorDetails() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerServiceErrorDetails.xsd"));
    }

    @Bean(name = "CramerServiceHeader")
    public XsdSchema cramerServiceHeader() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerServiceHeader.xsd"));
    }

    @Bean(name = "CramerTunnelInterface")
    public XsdSchema cramerTunnelInterface() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerTunnelInterface.xsd"));
    }

    @Bean(name = "CramerTunnelInterface_imports_1")
    public XsdSchema cramerTunnelInterface_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerTunnelInterface_imports_1.xsd"));
    }

    @Bean(name = "CramerVPLSService")
    public XsdSchema cramerVPLSService() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerVPLSService.xsd"));
    }

    @Bean(name = "CramerVPLSService_imports_1")
    public XsdSchema cramerVPLSService_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerVPLSService_imports_1.xsd"));
    }

    @Bean(name = "CramerVPLSService_imports_2")
    public XsdSchema cramerVPLSService_imports_2() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerVPLSService_imports_2.xsd"));
    }

    @Bean(name = "CramerVSATLastmile")
    public XsdSchema cramerVSATLastmile() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerVSATLastmile.xsd"));
    }

    @Bean(name = "CramerVSATLastmile_imports_1")
    public XsdSchema cramerVSATLastmile_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerVSATLastmile_imports_1.xsd"));
    }

    @Bean(name = "CramerWimaxLastmile")
    public XsdSchema cramerWimaxLastmile() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/CramerWimaxLastmile.xsd"));
    }

    @Bean(name = "Device")
    public XsdSchema device() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/Device.xsd"));
    }

    @Bean(name = "EIGRP")
    public XsdSchema eigrp() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/EIGRP.xsd"));
    }

    @Bean(name = "HSRP")
    public XsdSchema hsrp() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/HSRP.xsd"));
    }

    @Bean(name = "IPAddress")
    public XsdSchema iPAddress() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/IPAddress.xsd"));
    }

    @Bean(name = "IPService")
    public XsdSchema iPService() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/IPService.xsd"));
    }

    @Bean(name = "IPService_imports_1")
    public XsdSchema iPService_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/IPService_imports_1.xsd"));
    }

    @Bean(name = "IPV4Address")
    public XsdSchema IPV4Address() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/IPV4Address.xsd"));
    }

    @Bean(name = "IPV6Address")
    public XsdSchema IPV6Address() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/IPV6Address.xsd"));
    }

    @Bean(name = "IRSRouter")
    public XsdSchema IRSRouter() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/IRSRouter.xsd"));
    }

    @Bean(name = "lanRoutingProtocol")
    public XsdSchema lanRoutingProtocol() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/lanRoutingProtocol.xsd"));
    }

    @Bean(name = "lastmile")
    public XsdSchema lastmile() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/lastmile.xsd"));
    }

    @Bean(name = "lastmile_imports_1")
    public XsdSchema lastmile_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/lastmile_imports_1.xsd"));
    }

    @Bean(name = "mCPEwanIPDetails")
    public XsdSchema mCPEwanIPDetails() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/mCPEwanIPDetails.xsd"));
    }

    @Bean(name = "Multicasting")
    public XsdSchema Multicasting() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/Multicasting.xsd"));
    }

    @Bean(name = "OSPF")
    public XsdSchema OSPF() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/OSPF.xsd"));
    }

    @Bean(name = "PERouter")
    public XsdSchema PERouter() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/PERouter.xsd"));
    }

    @Bean(name = "RIP")
    public XsdSchema RIP() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/RIP.xsd"));
    }

    @Bean(name = "Service")
    public XsdSchema service() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/Service.xsd"));
    }

    @Bean(name = "Service_imports_1")
    public XsdSchema service_imports_1() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/Service_imports_1.xsd"));
    }

    @Bean(name = "SRPwanIPDetails")
    public XsdSchema SRPwanIPDetails() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/SRPwanIPDetails.xsd"));
    }

    @Bean(name = "Static")
    public XsdSchema Static() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/Static.xsd"));
    }

    @Bean(name = "Switch")
    public XsdSchema Switch() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/Switch.xsd"));
    }

    @Bean(name = "VRRP")
    public XsdSchema VRRP() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/VRRP.xsd"));
    }

    @Bean(name = "wanInterface")
    public XsdSchema wanInterface() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/wanInterface.xsd"));
    }

    @Bean(name = "wanRoutingProtocol")
    public XsdSchema wanRoutingProtocol() {
        return new SimpleXsdSchema(new ClassPathResource("wsdl/cramer/getipserviceasync/wanRoutingProtocol.xsd"));
    }
}

