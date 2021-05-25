
package com.tcl.dias.serviceactivation.cramer.muxsync;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cramerserviceslibrary.csvc.wsdl.v1.muxdetail package. 
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

    private final static QName _ServiceFault_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v1/muxdetail", "ServiceFault");
    private final static QName _GetMuxDetailsResponse_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v1/muxdetail", "getMuxDetailsResponse");
    private final static QName _GetMuxDetails_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v1/muxdetail", "getMuxDetails");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cramerserviceslibrary.csvc.wsdl.v1.muxdetail
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetMuxDetailsResponse }
     * 
     */
    public GetMuxDetailsResponse createGetMuxDetailsResponse() {
        return new GetMuxDetailsResponse();
    }

    /**
     * Create an instance of {@link GetMuxDetails }
     * 
     */
    public GetMuxDetails createGetMuxDetails() {
        return new GetMuxDetails();
    }

    /**
     * Create an instance of {@link ServiceFault }
     * 
     */
    public ServiceFault createServiceFault() {
        return new ServiceFault();
    }

    /**
     * Create an instance of {@link HandOffDetail }
     * 
     */
    public HandOffDetail createHandOffDetail() {
        return new HandOffDetail();
    }

    /**
     * Create an instance of {@link GetMuxDetailResponse }
     * 
     */
    public GetMuxDetailResponse createGetMuxDetailResponse() {
        return new GetMuxDetailResponse();
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
     * Create an instance of {@link StackTraceElement }
     * 
     */
    public StackTraceElement createStackTraceElement() {
        return new StackTraceElement();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v1/muxdetail", name = "ServiceFault")
    public JAXBElement<ServiceFault> createServiceFault(ServiceFault value) {
        return new JAXBElement<ServiceFault>(_ServiceFault_QNAME, ServiceFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMuxDetailsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v1/muxdetail", name = "getMuxDetailsResponse")
    public JAXBElement<GetMuxDetailsResponse> createGetMuxDetailsResponse(GetMuxDetailsResponse value) {
        return new JAXBElement<GetMuxDetailsResponse>(_GetMuxDetailsResponse_QNAME, GetMuxDetailsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMuxDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v1/muxdetail", name = "getMuxDetails")
    public JAXBElement<GetMuxDetails> createGetMuxDetails(GetMuxDetails value) {
        return new JAXBElement<GetMuxDetails>(_GetMuxDetails_QNAME, GetMuxDetails.class, null, value);
    }

}
