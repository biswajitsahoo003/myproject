
package com.tcl.dias.serviceactivation.cramer.createclrasync.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.cramer.createclrasync.beans package. 
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

    private final static QName _GetCLRCreationDetailsFault1GetCLRCreationDetailsFault_QNAME = new QName("http://ACE_CLRCreation_Module/GetCLRCreationDetails", "getCLRCreationDetailsFault1_getCLRCreationDetailsFault");
    private final static QName _SubmitCLRCreationFailureResponseFault1SubmitCLRCreationFailureResponseFault_QNAME = new QName("http://ACE_CLRCreation_Module/GetCLRCreationDetails", "submitCLRCreationFailureResponseFault1_submitCLRCreationFailureResponseFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.cramer.createclrasync.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCLRCreationDetails }
     * 
     */
    public GetCLRCreationDetails createGetCLRCreationDetails() {
        return new GetCLRCreationDetails();
    }

    /**
     * Create an instance of {@link CLRCreationDetails }
     * 
     */
    public CLRCreationDetails createCLRCreationDetails() {
        return new CLRCreationDetails();
    }

    /**
     * Create an instance of {@link GetCLRCreationDetailsResponse }
     * 
     */
    public GetCLRCreationDetailsResponse createGetCLRCreationDetailsResponse() {
        return new GetCLRCreationDetailsResponse();
    }

    /**
     * Create an instance of {@link CLRCreationDetailsAcknowledgement }
     * 
     */
    public CLRCreationDetailsAcknowledgement createCLRCreationDetailsAcknowledgement() {
        return new CLRCreationDetailsAcknowledgement();
    }

    /**
     * Create an instance of {@link SubmitCLRCreationFailureResponse }
     * 
     */
    public SubmitCLRCreationFailureResponse createSubmitCLRCreationFailureResponse() {
        return new SubmitCLRCreationFailureResponse();
    }

    /**
     * Create an instance of {@link SubmitCLRCreationFailureResponse2 }
     * 
     */
    public SubmitCLRCreationFailureResponse2 createSubmitCLRCreationFailureResponse2() {
        return new SubmitCLRCreationFailureResponse2();
    }

    /**
     * Create an instance of {@link SubmitCLRCreationFailureResponseResponse }
     * 
     */
    public SubmitCLRCreationFailureResponseResponse createSubmitCLRCreationFailureResponseResponse() {
        return new SubmitCLRCreationFailureResponseResponse();
    }

    /**
     * Create an instance of {@link Acknowledgement }
     * 
     */
    public Acknowledgement createAcknowledgement() {
        return new Acknowledgement();
    }

    /**
     * Create an instance of {@link Attributes }
     * 
     */
    public Attributes createAttributes() {
        return new Attributes();
    }

    /**
     * Create an instance of {@link ClrAttrs }
     * 
     */
    public ClrAttrs createClrAttrs() {
        return new ClrAttrs();
    }

    /**
     * Create an instance of {@link CLRcreationFailureResponse }
     * 
     */
    public CLRcreationFailureResponse createCLRcreationFailureResponse() {
        return new CLRcreationFailureResponse();
    }

    /**
     * Create an instance of {@link RespAttr }
     * 
     */
    public RespAttr createRespAttr() {
        return new RespAttr();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "http://ACE_CLRCreation_Module/GetCLRCreationDetails", name = "getCLRCreationDetailsFault1_getCLRCreationDetailsFault")
    public JAXBElement<Acknowledgement> createGetCLRCreationDetailsFault1GetCLRCreationDetailsFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_GetCLRCreationDetailsFault1GetCLRCreationDetailsFault_QNAME, Acknowledgement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "http://ACE_CLRCreation_Module/GetCLRCreationDetails", name = "submitCLRCreationFailureResponseFault1_submitCLRCreationFailureResponseFault")
    public JAXBElement<Acknowledgement> createSubmitCLRCreationFailureResponseFault1SubmitCLRCreationFailureResponseFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_SubmitCLRCreationFailureResponseFault1SubmitCLRCreationFailureResponseFault_QNAME, Acknowledgement.class, null, value);
    }

}
