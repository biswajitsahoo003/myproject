
package com.tcl.dias.serviceactivation.cramer.checkipclr.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.networkinventoryclient.cramer.checkipclr.beans package. 
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

    private final static QName _CheckIPCLRDetail_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v3/checkipclr", "checkIPCLRDetail");
    private final static QName _CheckIPCLRDetailResponse_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v3/checkipclr", "checkIPCLRDetailResponse");
    private final static QName _ServiceFault_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v3/checkipclr", "ServiceFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.networkinventoryclient.cramer.checkipclr.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceFault }
     * 
     */
    public ServiceFault createServiceFault() {
        return new ServiceFault();
    }

    /**
     * Create an instance of {@link CheckIPCLRDetail }
     * 
     */
    public CheckIPCLRDetail createCheckIPCLRDetail() {
        return new CheckIPCLRDetail();
    }

    /**
     * Create an instance of {@link CheckIPCLRDetailResponse }
     * 
     */
    public CheckIPCLRDetailResponse createCheckIPCLRDetailResponse() {
        return new CheckIPCLRDetailResponse();
    }

    /**
     * Create an instance of {@link Acknowledgement }
     * 
     */
    public Acknowledgement createAcknowledgement() {
        return new Acknowledgement();
    }

    /**
     * Create an instance of {@link Throwable }
     * 
     */
    public Throwable createThrowable() {
        return new Throwable();
    }

    /**
     * Create an instance of {@link RequestHeader }
     * 
     */
    public RequestHeader createRequestHeader() {
        return new RequestHeader();
    }

    /**
     * Create an instance of {@link StackTraceElement }
     * 
     */
    public StackTraceElement createStackTraceElement() {
        return new StackTraceElement();
    }

    /**
     * Create an instance of {@link IpClrDetailResponse }
     * 
     */
    public IpClrDetailResponse createIpClrDetailResponse() {
        return new IpClrDetailResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckIPCLRDetail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v3/checkipclr", name = "checkIPCLRDetail")
    public JAXBElement<CheckIPCLRDetail> createCheckIPCLRDetail(CheckIPCLRDetail value) {
        return new JAXBElement<CheckIPCLRDetail>(_CheckIPCLRDetail_QNAME, CheckIPCLRDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckIPCLRDetailResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v3/checkipclr", name = "checkIPCLRDetailResponse")
    public JAXBElement<CheckIPCLRDetailResponse> createCheckIPCLRDetailResponse(CheckIPCLRDetailResponse value) {
        return new JAXBElement<CheckIPCLRDetailResponse>(_CheckIPCLRDetailResponse_QNAME, CheckIPCLRDetailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v3/checkipclr", name = "ServiceFault")
    public JAXBElement<ServiceFault> createServiceFault(ServiceFault value) {
        return new JAXBElement<ServiceFault>(_ServiceFault_QNAME, ServiceFault.class, null, value);
    }

}
