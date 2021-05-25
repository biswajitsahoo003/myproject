
package com.tcl.dias.serviceactivation.cramer.downtime.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.cramer.downtime.beans package. 
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

    private final static QName _ServiceFault_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v1/downtime", "ServiceFault");
    private final static QName _CheckDownTime_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v1/downtime", "checkDownTime");
    private final static QName _CheckDownTimeResponse_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v1/downtime", "checkDownTimeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.cramer.downtime.beans
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
     * Create an instance of {@link CheckDownTime }
     * 
     */
    public CheckDownTime createCheckDownTime() {
        return new CheckDownTime();
    }

    /**
     * Create an instance of {@link CheckDownTimeResponse }
     * 
     */
    public CheckDownTimeResponse createCheckDownTimeResponse() {
        return new CheckDownTimeResponse();
    }

    /**
     * Create an instance of {@link DownTimeRequiredResponse }
     * 
     */
    public DownTimeRequiredResponse createDownTimeRequiredResponse() {
        return new DownTimeRequiredResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceFault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ServiceFault }{@code >}
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v1/downtime", name = "ServiceFault")
    public JAXBElement<ServiceFault> createServiceFault(ServiceFault value) {
        return new JAXBElement<ServiceFault>(_ServiceFault_QNAME, ServiceFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckDownTime }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CheckDownTime }{@code >}
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v1/downtime", name = "checkDownTime")
    public JAXBElement<CheckDownTime> createCheckDownTime(CheckDownTime value) {
        return new JAXBElement<CheckDownTime>(_CheckDownTime_QNAME, CheckDownTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckDownTimeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CheckDownTimeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v1/downtime", name = "checkDownTimeResponse")
    public JAXBElement<CheckDownTimeResponse> createCheckDownTimeResponse(CheckDownTimeResponse value) {
        return new JAXBElement<CheckDownTimeResponse>(_CheckDownTimeResponse_QNAME, CheckDownTimeResponse.class, null, value);
    }

}
