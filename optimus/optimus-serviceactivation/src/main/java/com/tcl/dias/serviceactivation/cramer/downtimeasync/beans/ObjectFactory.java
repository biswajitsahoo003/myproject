
package com.tcl.dias.serviceactivation.cramer.downtimeasync.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.cramer.downtimeasync.beans package. 
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

    private final static QName _GetServiceDowntimeDetailsFault1GetServiceDowntimeDetailsFault_QNAME = new QName("http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails", "getServiceDowntimeDetailsFault1_getServiceDowntimeDetailsFault");
    private final static QName _SubmitServiceDowntimeFaultFault1SubmitDowntimeFault_QNAME = new QName("http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails", "submitServiceDowntimeFaultFault1_submitDowntimeFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.cramer.downtimeasync.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetServiceDowntimeDetails }
     * 
     */
    public GetServiceDowntimeDetails createGetServiceDowntimeDetails() {
        return new GetServiceDowntimeDetails();
    }

    /**
     * Create an instance of {@link ServiceDownDtls }
     * 
     */
    public ServiceDownDtls createServiceDownDtls() {
        return new ServiceDownDtls();
    }

    /**
     * Create an instance of {@link GetServiceDowntimeDetailsResponse }
     * 
     */
    public GetServiceDowntimeDetailsResponse createGetServiceDowntimeDetailsResponse() {
        return new GetServiceDowntimeDetailsResponse();
    }

    /**
     * Create an instance of {@link ServiceDownDtlsResponse }
     * 
     */
    public ServiceDownDtlsResponse createServiceDownDtlsResponse() {
        return new ServiceDownDtlsResponse();
    }

    /**
     * Create an instance of {@link SubmitServiceDowntimeFault }
     * 
     */
    public SubmitServiceDowntimeFault createSubmitServiceDowntimeFault() {
        return new SubmitServiceDowntimeFault();
    }

    /**
     * Create an instance of {@link SubmitServiceDowntimeFault2 }
     * 
     */
    public SubmitServiceDowntimeFault2 createSubmitServiceDowntimeFault2() {
        return new SubmitServiceDowntimeFault2();
    }

    /**
     * Create an instance of {@link SubmitServiceDowntimeFaultResponse }
     * 
     */
    public SubmitServiceDowntimeFaultResponse createSubmitServiceDowntimeFaultResponse() {
        return new SubmitServiceDowntimeFaultResponse();
    }

    /**
     * Create an instance of {@link SubmitServiceDowntimeFaultResponse2 }
     * 
     */
    public SubmitServiceDowntimeFaultResponse2 createSubmitServiceDowntimeFaultResponse2() {
        return new SubmitServiceDowntimeFaultResponse2();
    }

    /**
     * Create an instance of {@link Acknowledgement }
     * 
     */
    public Acknowledgement createAcknowledgement() {
        return new Acknowledgement();
    }

    /**
     * Create an instance of {@link ServiceDowntimeSubmitionFault }
     * 
     */
    public ServiceDowntimeSubmitionFault createServiceDowntimeSubmitionFault() {
        return new ServiceDowntimeSubmitionFault();
    }

    /**
     * Create an instance of {@link RequestHeader }
     * 
     */
    public RequestHeader createRequestHeader() {
        return new RequestHeader();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails", name = "getServiceDowntimeDetailsFault1_getServiceDowntimeDetailsFault")
    public JAXBElement<Acknowledgement> createGetServiceDowntimeDetailsFault1GetServiceDowntimeDetailsFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_GetServiceDowntimeDetailsFault1GetServiceDowntimeDetailsFault_QNAME, Acknowledgement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails", name = "submitServiceDowntimeFaultFault1_submitDowntimeFault")
    public JAXBElement<Acknowledgement> createSubmitServiceDowntimeFaultFault1SubmitDowntimeFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_SubmitServiceDowntimeFaultFault1SubmitDowntimeFault_QNAME, Acknowledgement.class, null, value);
    }

}
