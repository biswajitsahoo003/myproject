
package com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans package. 
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

    private final static QName _SubmitMuxDetailFailureResponseResponse_QNAME = new QName("urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", "submitMuxDetailFailureResponseResponse");
    private final static QName _GetMuxInfoResponse_QNAME = new QName("urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", "getMuxInfoResponse");
    private final static QName _SubmitMuxDetailFailureResponse_QNAME = new QName("urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", "submitMuxDetailFailureResponse");
    private final static QName _GetMuxInfo_QNAME = new QName("urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", "getMuxInfo");
    private final static QName _GetMuxInfoFault1GetMuxInfoFault_QNAME = new QName("urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", "getMuxInfoFault1_getMuxInfoFault");
    private final static QName _SubmitMuxDetailFailureResponseFault1SubmitMuxDetailFailureResponseFault_QNAME = new QName("urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", "submitMuxDetailFailureResponseFault1_submitMuxDetailFailureResponseFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SubmitMuxDetailFailureResponseAck }
     * 
     */
    public SubmitMuxDetailFailureResponseAck createSubmitMuxDetailFailureResponseAck() {
        return new SubmitMuxDetailFailureResponseAck();
    }

    /**
     * Create an instance of {@link GetMuxInfoAck }
     * 
     */
    public GetMuxInfoAck createGetMuxInfoAck() {
        return new GetMuxInfoAck();
    }

    /**
     * Create an instance of {@link SubmitMuxDetailFailureResponse }
     * 
     */
    public SubmitMuxDetailFailureResponse createSubmitMuxDetailFailureResponse() {
        return new SubmitMuxDetailFailureResponse();
    }

    /**
     * Create an instance of {@link GetMuxInfo }
     * 
     */
    public GetMuxInfo createGetMuxInfo() {
        return new GetMuxInfo();
    }

    /**
     * Create an instance of {@link Acknowledgement }
     * 
     */
    public Acknowledgement createAcknowledgement() {
        return new Acknowledgement();
    }

    /**
     * Create an instance of {@link MuxDetailFailureResponse }
     * 
     */
    public MuxDetailFailureResponse createMuxDetailFailureResponse() {
        return new MuxDetailFailureResponse();
    }

    /**
     * Create an instance of {@link MuxDetail }
     * 
     */
    public MuxDetail createMuxDetail() {
        return new MuxDetail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitMuxDetailFailureResponseAck }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubmitMuxDetailFailureResponseAck }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", name = "submitMuxDetailFailureResponseResponse")
    public JAXBElement<SubmitMuxDetailFailureResponseAck> createSubmitMuxDetailFailureResponseResponse(SubmitMuxDetailFailureResponseAck value) {
        return new JAXBElement<SubmitMuxDetailFailureResponseAck>(_SubmitMuxDetailFailureResponseResponse_QNAME, SubmitMuxDetailFailureResponseAck.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMuxInfoAck }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetMuxInfoAck }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", name = "getMuxInfoResponse")
    public JAXBElement<GetMuxInfoAck> createGetMuxInfoResponse(GetMuxInfoAck value) {
        return new JAXBElement<GetMuxInfoAck>(_GetMuxInfoResponse_QNAME, GetMuxInfoAck.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitMuxDetailFailureResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubmitMuxDetailFailureResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", name = "submitMuxDetailFailureResponse")
    public JAXBElement<SubmitMuxDetailFailureResponse> createSubmitMuxDetailFailureResponse(SubmitMuxDetailFailureResponse value) {
        return new JAXBElement<SubmitMuxDetailFailureResponse>(_SubmitMuxDetailFailureResponse_QNAME, SubmitMuxDetailFailureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMuxInfo }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetMuxInfo }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", name = "getMuxInfo")
    public JAXBElement<GetMuxInfo> createGetMuxInfo(GetMuxInfo value) {
        return new JAXBElement<GetMuxInfo>(_GetMuxInfo_QNAME, GetMuxInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", name = "getMuxInfoFault1_getMuxInfoFault")
    public JAXBElement<Acknowledgement> createGetMuxInfoFault1GetMuxInfoFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_GetMuxInfoFault1GetMuxInfoFault_QNAME, Acknowledgement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer", name = "submitMuxDetailFailureResponseFault1_submitMuxDetailFailureResponseFault")
    public JAXBElement<Acknowledgement> createSubmitMuxDetailFailureResponseFault1SubmitMuxDetailFailureResponseFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_SubmitMuxDetailFailureResponseFault1SubmitMuxDetailFailureResponseFault_QNAME, Acknowledgement.class, null, value);
    }

}
