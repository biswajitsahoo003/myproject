
package com.tcl.dias.serviceactivation.cramer.isvalidbts.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.networkinventoryclient.cramer.isvalidbts.beans package. 
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

    private final static QName _IsValidBTSResponse_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v2/ws/bts", "isValidBTSResponse");
    private final static QName _IsValidBTS_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v2/ws/bts", "isValidBTS");
    private final static QName _ServiceFault_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v2/ws/bts", "ServiceFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.networkinventoryclient.cramer.isvalidbts.beans
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
     * Create an instance of {@link IsValidBTSResponse }
     * 
     */
    public IsValidBTSResponse createIsValidBTSResponse() {
        return new IsValidBTSResponse();
    }

    /**
     * Create an instance of {@link IsValidBTS }
     * 
     */
    public IsValidBTS createIsValidBTS() {
        return new IsValidBTS();
    }

    /**
     * Create an instance of {@link BtsValidationResponse }
     * 
     */
    public BtsValidationResponse createBtsValidationResponse() {
        return new BtsValidationResponse();
    }

    /**
     * Create an instance of {@link BtsValidationRequest }
     * 
     */
    public BtsValidationRequest createBtsValidationRequest() {
        return new BtsValidationRequest();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link IsValidBTSResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v2/ws/bts", name = "isValidBTSResponse")
    public JAXBElement<IsValidBTSResponse> createIsValidBTSResponse(IsValidBTSResponse value) {
        return new JAXBElement<IsValidBTSResponse>(_IsValidBTSResponse_QNAME, IsValidBTSResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsValidBTS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v2/ws/bts", name = "isValidBTS")
    public JAXBElement<IsValidBTS> createIsValidBTS(IsValidBTS value) {
        return new JAXBElement<IsValidBTS>(_IsValidBTS_QNAME, IsValidBTS.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v2/ws/bts", name = "ServiceFault")
    public JAXBElement<ServiceFault> createServiceFault(ServiceFault value) {
        return new JAXBElement<ServiceFault>(_ServiceFault_QNAME, ServiceFault.class, null, value);
    }

}
