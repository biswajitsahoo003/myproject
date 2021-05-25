
package com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.v2 package. 
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

    private final static QName _GetHDCONFIGDetailsFault1GetHDCONFIGDetailsFault_QNAME = new QName("http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails", "getHDCONFIGDetailsFault1_getHDCONFIGDetailsFault");
    private final static QName _SubmitHDCONFIGFailureResponseFault1SubmitHDCONFIGFailureResponseFault_QNAME = new QName("http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails", "submitHDCONFIGFailureResponseFault1_submitHDCONFIGFailureResponseFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.cramer.gethdconfigdetailsasync.beans.v2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetHDCONFIGDetails }
     * 
     */
    public GetHDCONFIGDetails createGetHDCONFIGDetails() {
        return new GetHDCONFIGDetails();
    }

    /**
     * Create an instance of {@link HDCONFIGDetails }
     * 
     */
    public HDCONFIGDetails createHDCONFIGDetails() {
        return new HDCONFIGDetails();
    }

    /**
     * Create an instance of {@link GetHDCONFIGDetailsResponse }
     * 
     */
    public GetHDCONFIGDetailsResponse createGetHDCONFIGDetailsResponse() {
        return new GetHDCONFIGDetailsResponse();
    }

    /**
     * Create an instance of {@link HDCONFIGDetailsAcknowledgement }
     * 
     */
    public HDCONFIGDetailsAcknowledgement createHDCONFIGDetailsAcknowledgement() {
        return new HDCONFIGDetailsAcknowledgement();
    }

    /**
     * Create an instance of {@link Acknowledgement }
     * 
     */
    public Acknowledgement createAcknowledgement() {
        return new Acknowledgement();
    }

    /**
     * Create an instance of {@link SubmitHDCONFIGFailureResponse }
     * 
     */
    public SubmitHDCONFIGFailureResponse createSubmitHDCONFIGFailureResponse() {
        return new SubmitHDCONFIGFailureResponse();
    }

    /**
     * Create an instance of {@link SubmitHDCONFIGFailureResponse2 }
     * 
     */
    public SubmitHDCONFIGFailureResponse2 createSubmitHDCONFIGFailureResponse2() {
        return new SubmitHDCONFIGFailureResponse2();
    }

    /**
     * Create an instance of {@link SubmitHDCONFIGFailureResponseResponse }
     * 
     */
    public SubmitHDCONFIGFailureResponseResponse createSubmitHDCONFIGFailureResponseResponse() {
        return new SubmitHDCONFIGFailureResponseResponse();
    }

    /**
     * Create an instance of {@link Attributes }
     * 
     */
    public Attributes createAttributes() {
        return new Attributes();
    }

    /**
     * Create an instance of {@link HDCONFIGAttrs }
     * 
     */
    public HDCONFIGAttrs createHDCONFIGAttrs() {
        return new HDCONFIGAttrs();
    }

    /**
     * Create an instance of {@link HDCONFIGFailureResponse }
     * 
     */
    public HDCONFIGFailureResponse createHDCONFIGFailureResponse() {
        return new HDCONFIGFailureResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails", name = "getHDCONFIGDetailsFault1_getHDCONFIGDetailsFault")
    public JAXBElement<Acknowledgement> createGetHDCONFIGDetailsFault1GetHDCONFIGDetailsFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_GetHDCONFIGDetailsFault1GetHDCONFIGDetailsFault_QNAME, Acknowledgement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "http://ACEIAS_HDCONFIG_Module/GetHDCONFIGDetails", name = "submitHDCONFIGFailureResponseFault1_submitHDCONFIGFailureResponseFault")
    public JAXBElement<Acknowledgement> createSubmitHDCONFIGFailureResponseFault1SubmitHDCONFIGFailureResponseFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_SubmitHDCONFIGFailureResponseFault1SubmitHDCONFIGFailureResponseFault_QNAME, Acknowledgement.class, null, value);
    }

}
