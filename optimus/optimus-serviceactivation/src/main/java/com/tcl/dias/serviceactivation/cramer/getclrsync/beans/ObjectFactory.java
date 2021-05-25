
package com.tcl.dias.serviceactivation.cramer.getclrsync.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import com.tcl.dias.serviceactivation.beans.CramerServiceHeader;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.networkinventoryclient.cramer.getclrsync.beans package. 
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

    private final static QName _GetCLR_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async", "getCLR");
    private final static QName _GetCLRResponse_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async", "getCLRResponse");
    private final static QName _AsyncCLRInfoFault_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.async.ws.setclr", "Async_CLRInfoFault");
    private final static QName _AsyncServiceDetailFault_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async", "Async_ServiceDetailFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.networkinventoryclient.cramer.getclrsync.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AsyncCLRInfoFault }
     * 
     */
    public AsyncCLRInfoFault createAsyncCLRInfoFault() {
        return new AsyncCLRInfoFault();
    }

    /**
     * Create an instance of {@link GetCLRResponse }
     * 
     */
    public GetCLRResponse createGetCLRResponse() {
        return new GetCLRResponse();
    }

    /**
     * Create an instance of {@link GetCLR }
     * 
     */
    public GetCLR createGetCLR() {
        return new GetCLR();
    }

    /**
     * Create an instance of {@link CramerServiceHeader }
     * 
     */
    public CramerServiceHeader createCramerServiceHeader() {
        return new CramerServiceHeader();
    }

    /**
     * Create an instance of {@link Throwable }
     * 
     */
    public Throwable createThrowable() {
        return new Throwable();
    }

    /**
     * Create an instance of {@link ServiceDetailResponseAsync }
     * 
     */
    public ServiceDetailResponseAsync createServiceDetailResponseAsync() {
        return new ServiceDetailResponseAsync();
    }

    /**
     * Create an instance of {@link CramerServiceRequestAck }
     * 
     */
    public CramerServiceRequestAck createCramerServiceRequestAck() {
        return new CramerServiceRequestAck();
    }

    /**
     * Create an instance of {@link StackTraceElement }
     * 
     */
    public StackTraceElement createStackTraceElement() {
        return new StackTraceElement();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCLR }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async", name = "getCLR")
    public JAXBElement<GetCLR> createGetCLR(GetCLR value) {
        return new JAXBElement<GetCLR>(_GetCLR_QNAME, GetCLR.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCLRResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async", name = "getCLRResponse")
    public JAXBElement<GetCLRResponse> createGetCLRResponse(GetCLRResponse value) {
        return new JAXBElement<GetCLRResponse>(_GetCLRResponse_QNAME, GetCLRResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AsyncCLRInfoFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.async.ws.setclr", name = "Async_CLRInfoFault")
    public JAXBElement<AsyncCLRInfoFault> createAsyncCLRInfoFault(AsyncCLRInfoFault value) {
        return new JAXBElement<AsyncCLRInfoFault>(_AsyncCLRInfoFault_QNAME, AsyncCLRInfoFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AsyncCLRInfoFault }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tatacommunications.cramer.ace.service.async.ws.getclr.ServiceDetail_Async", name = "Async_ServiceDetailFault")
    public JAXBElement<AsyncCLRInfoFault> createAsyncServiceDetailFault(AsyncCLRInfoFault value) {
        return new JAXBElement<AsyncCLRInfoFault>(_AsyncServiceDetailFault_QNAME, AsyncCLRInfoFault.class, null, value);
    }

}
