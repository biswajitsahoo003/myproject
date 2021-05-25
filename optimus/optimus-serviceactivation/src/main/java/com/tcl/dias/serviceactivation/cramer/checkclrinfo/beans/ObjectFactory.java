
package com.tcl.dias.serviceactivation.cramer.checkclrinfo.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.networkinventoryclient.cramer.checkclrinfo.beans package. 
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

    private final static QName _SOAPException_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.checkclrstatus", "SOAPException");
    private final static QName _CheckClrInfoResponse_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.checkclrstatus", "checkClrInfoResponse");
    private final static QName _CheckClrInfo_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.checkclrstatus", "checkClrInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.networkinventoryclient.cramer.checkclrinfo.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SOAPException }
     * 
     */
    public SOAPException createSOAPException() {
        return new SOAPException();
    }

    /**
     * Create an instance of {@link CheckClrInfoResponse }
     * 
     */
    public CheckClrInfoResponse createCheckClrInfoResponse() {
        return new CheckClrInfoResponse();
    }

    /**
     * Create an instance of {@link CheckClrInfo }
     * 
     */
    public CheckClrInfo createCheckClrInfo() {
        return new CheckClrInfo();
    }

    /**
     * Create an instance of {@link Throwable }
     * 
     */
    public Throwable createThrowable() {
        return new Throwable();
    }

    /**
     * Create an instance of {@link OrderInfo }
     * 
     */
    public OrderInfo createOrderInfo() {
        return new OrderInfo();
    }

    /**
     * Create an instance of {@link ErrorDetails }
     * 
     */
    public ErrorDetails createErrorDetails() {
        return new ErrorDetails();
    }

    /**
     * Create an instance of {@link StackTraceElement }
     * 
     */
    public StackTraceElement createStackTraceElement() {
        return new StackTraceElement();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SOAPException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.checkclrstatus", name = "SOAPException")
    public JAXBElement<SOAPException> createSOAPException(SOAPException value) {
        return new JAXBElement<SOAPException>(_SOAPException_QNAME, SOAPException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckClrInfoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.checkclrstatus", name = "checkClrInfoResponse")
    public JAXBElement<CheckClrInfoResponse> createCheckClrInfoResponse(CheckClrInfoResponse value) {
        return new JAXBElement<CheckClrInfoResponse>(_CheckClrInfoResponse_QNAME, CheckClrInfoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CheckClrInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.ws.checkclrstatus", name = "checkClrInfo")
    public JAXBElement<CheckClrInfo> createCheckClrInfo(CheckClrInfo value) {
        return new JAXBElement<CheckClrInfo>(_CheckClrInfo_QNAME, CheckClrInfo.class, null, value);
    }

}
