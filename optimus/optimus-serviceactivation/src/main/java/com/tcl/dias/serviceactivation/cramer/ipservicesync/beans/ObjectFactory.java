
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.tcl.dias.serviceactivation.beans.CramerServiceHeader;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the crameripservices.ws.service.ace.cramer.tatacommunications.com package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetIPServiceInfo_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "getIPServiceInfo");
    private final static QName _GetIRSRouterInfo_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "getIRSRouterInfo");
    private final static QName _GetIRSRouterInfoResponse_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "getIRSRouterInfoResponse");
    private final static QName _AssignDummyWANIP_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "assignDummyWANIP");
    private final static QName _GetIPServiceInfoFault_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "getIPServiceInfoFault");
    private final static QName _SetCLRFault_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "SetCLRFault");
    private final static QName _AssignDummyWANIPResponse_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "assignDummyWANIPResponse");
    private final static QName _SetCLR_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "setCLR");
    private final static QName _ReleaseDummyWANIP_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "releaseDummyWANIP");
    private final static QName _SetCLRResponseClr_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "setCLRResponseClr");
    private final static QName _ReleaseDummyWANIPFault_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "releaseDummyWANIPFault");
    private final static QName _ReleaseDummyWANIPResponse_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "releaseDummyWANIPResponse");
    private final static QName _AssignDummyWANIPFault_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "assignDummyWANIPFault");
    private final static QName _GetIPServiceInfoResponse_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "getIPServiceInfoResponse");
    private final static QName _GetIRSRouterInfoFault_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "getIRSRouterInfoFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: crameripservices.ws.service.ace.cramer.tatacommunications.com
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SetCLRFault }
     * 
     */
    public SetCLRFault createSetCLRFault() {
        return new SetCLRFault();
    }

    /**
     * Create an instance of {@link AssignDummyWANIPResponse }
     * 
     */
    public AssignDummyWANIPResponse createAssignDummyWANIPResponse() {
        return new AssignDummyWANIPResponse();
    }

    /**
     * Create an instance of {@link SetCLR }
     * 
     */
    public SetCLR createSetCLR() {
        return new SetCLR();
    }

    /**
     * Create an instance of {@link ReleaseDummyWANIP }
     * 
     */
    public ReleaseDummyWANIP createReleaseDummyWANIP() {
        return new ReleaseDummyWANIP();
    }

    /**
     * Create an instance of {@link SetCLRResponseClr }
     * 
     */
    public SetCLRResponseClr createSetCLRResponseClr() {
        return new SetCLRResponseClr();
    }

    /**
     * Create an instance of {@link AssignDummyWANIPFault }
     * 
     */
    public AssignDummyWANIPFault createAssignDummyWANIPFault() {
        return new AssignDummyWANIPFault();
    }

    /**
     * Create an instance of {@link GetIPServiceInfoResponse }
     * 
     */
    public GetIPServiceInfoResponse createGetIPServiceInfoResponse() {
        return new GetIPServiceInfoResponse();
    }

    /**
     * Create an instance of {@link GetIRSRouterInfoFault }
     * 
     */
    public GetIRSRouterInfoFault createGetIRSRouterInfoFault() {
        return new GetIRSRouterInfoFault();
    }

    /**
     * Create an instance of {@link ReleaseDummyWANIPFault }
     * 
     */
    public ReleaseDummyWANIPFault createReleaseDummyWANIPFault() {
        return new ReleaseDummyWANIPFault();
    }

    /**
     * Create an instance of {@link ReleaseDummyWANIPResponse }
     * 
     */
    public ReleaseDummyWANIPResponse createReleaseDummyWANIPResponse() {
        return new ReleaseDummyWANIPResponse();
    }

    /**
     * Create an instance of {@link GetIRSRouterInfo }
     * 
     */
    public GetIRSRouterInfo createGetIRSRouterInfo() {
        return new GetIRSRouterInfo();
    }

    /**
     * Create an instance of {@link GetIRSRouterInfoResponse }
     * 
     */
    public GetIRSRouterInfoResponse createGetIRSRouterInfoResponse() {
        return new GetIRSRouterInfoResponse();
    }

    /**
     * Create an instance of {@link GetIPServiceInfo }
     * 
     */
    public GetIPServiceInfo createGetIPServiceInfo() {
        return new GetIPServiceInfo();
    }

    /**
     * Create an instance of {@link AssignDummyWANIP }
     * 
     */
    public AssignDummyWANIP createAssignDummyWANIP() {
        return new AssignDummyWANIP();
    }

    /**
     * Create an instance of {@link GetIPServiceInfoFault }
     * 
     */
    public GetIPServiceInfoFault createGetIPServiceInfoFault() {
        return new GetIPServiceInfoFault();
    }

    /**
     * Create an instance of {@link CramerServiceHeader }
     * 
     */
    public CramerServiceHeader createCramerServiceHeader() {
        return new CramerServiceHeader();
    }

    /**
     * Create an instance of {@link SetClrResponse }
     * 
     */
    public SetClrResponse createSetClrResponse() {
        return new SetClrResponse();
    }

    /**
     * Create an instance of {@link CramerIPServiceRequestAck }
     * 
     */
    public CramerIPServiceRequestAck createCramerIPServiceRequestAck() {
        return new CramerIPServiceRequestAck();
    }

    /**
     * Create an instance of {@link IrsRouterInfoResponse }
     * 
     */
    public IrsRouterInfoResponse createIrsRouterInfoResponse() {
        return new IrsRouterInfoResponse();
    }

    /**
     * Create an instance of {@link AssignDummyWANIPResponse2 }
     * 
     */
    public AssignDummyWANIPResponse2 createAssignDummyWANIPResponse2() {
        return new AssignDummyWANIPResponse2();
    }

    /**
     * Create an instance of {@link IrsRouter }
     * 
     */
    public IrsRouter createIrsRouter() {
        return new IrsRouter();
    }

    /**
     * Create an instance of {@link Throwable }
     * 
     */
    public Throwable createThrowable() {
        return new Throwable();
    }

    /**
     * Create an instance of {@link StackTraceElement }
     * 
     */
    public StackTraceElement createStackTraceElement() {
        return new StackTraceElement();
    }

    /**
     * Create an instance of {@link ReleaseDummyWANIPResponse2 }
     * 
     */
    public ReleaseDummyWANIPResponse2 createReleaseDummyWANIPResponse2() {
        return new ReleaseDummyWANIPResponse2();
    }

    /**
     * Create an instance of {@link IPServiceInfoResponse }
     * 
     */
    public IPServiceInfoResponse createIPServiceInfoResponse() {
        return new IPServiceInfoResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIPServiceInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "getIPServiceInfo")
    public JAXBElement<GetIPServiceInfo> createGetIPServiceInfo(GetIPServiceInfo value) {
        return new JAXBElement<GetIPServiceInfo>(_GetIPServiceInfo_QNAME, GetIPServiceInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIRSRouterInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "getIRSRouterInfo")
    public JAXBElement<GetIRSRouterInfo> createGetIRSRouterInfo(GetIRSRouterInfo value) {
        return new JAXBElement<GetIRSRouterInfo>(_GetIRSRouterInfo_QNAME, GetIRSRouterInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIRSRouterInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "getIRSRouterInfoResponse")
    public JAXBElement<GetIRSRouterInfoResponse> createGetIRSRouterInfoResponse(GetIRSRouterInfoResponse value) {
        return new JAXBElement<GetIRSRouterInfoResponse>(_GetIRSRouterInfoResponse_QNAME, GetIRSRouterInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssignDummyWANIP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "assignDummyWANIP")
    public JAXBElement<AssignDummyWANIP> createAssignDummyWANIP(AssignDummyWANIP value) {
        return new JAXBElement<AssignDummyWANIP>(_AssignDummyWANIP_QNAME, AssignDummyWANIP.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIPServiceInfoFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "getIPServiceInfoFault")
    public JAXBElement<GetIPServiceInfoFault> createGetIPServiceInfoFault(GetIPServiceInfoFault value) {
        return new JAXBElement<GetIPServiceInfoFault>(_GetIPServiceInfoFault_QNAME, GetIPServiceInfoFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetCLRFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "SetCLRFault")
    public JAXBElement<SetCLRFault> createSetCLRFault(SetCLRFault value) {
        return new JAXBElement<SetCLRFault>(_SetCLRFault_QNAME, SetCLRFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssignDummyWANIPResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "assignDummyWANIPResponse")
    public JAXBElement<AssignDummyWANIPResponse> createAssignDummyWANIPResponse(AssignDummyWANIPResponse value) {
        return new JAXBElement<AssignDummyWANIPResponse>(_AssignDummyWANIPResponse_QNAME, AssignDummyWANIPResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetCLR }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "setCLR")
    public JAXBElement<SetCLR> createSetCLR(SetCLR value) {
        return new JAXBElement<SetCLR>(_SetCLR_QNAME, SetCLR.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReleaseDummyWANIP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "releaseDummyWANIP")
    public JAXBElement<ReleaseDummyWANIP> createReleaseDummyWANIP(ReleaseDummyWANIP value) {
        return new JAXBElement<ReleaseDummyWANIP>(_ReleaseDummyWANIP_QNAME, ReleaseDummyWANIP.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetCLRResponseClr }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "setCLRResponseClr")
    public JAXBElement<SetCLRResponseClr> createSetCLRResponseClr(SetCLRResponseClr value) {
        return new JAXBElement<SetCLRResponseClr>(_SetCLRResponseClr_QNAME, SetCLRResponseClr.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReleaseDummyWANIPFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "releaseDummyWANIPFault")
    public JAXBElement<ReleaseDummyWANIPFault> createReleaseDummyWANIPFault(ReleaseDummyWANIPFault value) {
        return new JAXBElement<ReleaseDummyWANIPFault>(_ReleaseDummyWANIPFault_QNAME, ReleaseDummyWANIPFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReleaseDummyWANIPResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "releaseDummyWANIPResponse")
    public JAXBElement<ReleaseDummyWANIPResponse> createReleaseDummyWANIPResponse(ReleaseDummyWANIPResponse value) {
        return new JAXBElement<ReleaseDummyWANIPResponse>(_ReleaseDummyWANIPResponse_QNAME, ReleaseDummyWANIPResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssignDummyWANIPFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "assignDummyWANIPFault")
    public JAXBElement<AssignDummyWANIPFault> createAssignDummyWANIPFault(AssignDummyWANIPFault value) {
        return new JAXBElement<AssignDummyWANIPFault>(_AssignDummyWANIPFault_QNAME, AssignDummyWANIPFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIPServiceInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "getIPServiceInfoResponse")
    public JAXBElement<GetIPServiceInfoResponse> createGetIPServiceInfoResponse(GetIPServiceInfoResponse value) {
        return new JAXBElement<GetIPServiceInfoResponse>(_GetIPServiceInfoResponse_QNAME, GetIPServiceInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIRSRouterInfoFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", name = "getIRSRouterInfoFault")
    public JAXBElement<GetIRSRouterInfoFault> createGetIRSRouterInfoFault(GetIRSRouterInfoFault value) {
        return new JAXBElement<GetIRSRouterInfoFault>(_GetIRSRouterInfoFault_QNAME, GetIRSRouterInfoFault.class, null, value);
    }

}
