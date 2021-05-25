
package com.tcl.dias.serviceactivation.offnetdetails.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.offnetdetails.beans package. 
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

    private final static QName _SetOpticalHandOffDetailsFault1SetOpticalHandOffDetailsFault_QNAME = new QName("com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", "setOpticalHandOffDetailsFault1_setOpticalHandOffDetailsFault");
    private final static QName _SetElectricalHandOffDetailsFault1SetElectricalHandOffDetailsFault_QNAME = new QName("com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", "setElectricalHandOffDetailsFault1_setElectricalHandOffDetailsFault");
    private final static QName _SubmitTSSelectionFaultFault1SubmitFault_QNAME = new QName("com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", "submitTSSelectionFaultFault1_submitFault");
    private final static QName _SubmitTSSelectionFaultResponse_QNAME = new QName("urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", "submitTSSelectionFaultResponse");
    private final static QName _SubmitTSSelectionFault_QNAME = new QName("urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", "submitTSSelectionFault");
    private final static QName _SetElectricalHandOffDetailsResponse_QNAME = new QName("urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", "setElectricalHandOffDetailsResponse");
    private final static QName _SetElectricalHandOffDetails_QNAME = new QName("urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", "setElectricalHandOffDetails");
    private final static QName _ReserveTSTallyResponse_QNAME = new QName("urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", "ReserveTSTallyResponse");
    private final static QName _ReserveTSTally_QNAME = new QName("urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", "ReserveTSTally");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.offnetdetails.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Acknowledgement }
     * 
     */
    public Acknowledgement createAcknowledgement() {
        return new Acknowledgement();
    }

    /**
     * Create an instance of {@link SubmitTSSelectionFaultResponse }
     * 
     */
    public SubmitTSSelectionFaultResponse createSubmitTSSelectionFaultResponse() {
        return new SubmitTSSelectionFaultResponse();
    }

    /**
     * Create an instance of {@link SubmitTSSelectionFault }
     * 
     */
    public SubmitTSSelectionFault createSubmitTSSelectionFault() {
        return new SubmitTSSelectionFault();
    }

    /**
     * Create an instance of {@link SetElectricalHandOffDetailsResponse }
     * 
     */
    public SetElectricalHandOffDetailsResponse createSetElectricalHandOffDetailsResponse() {
        return new SetElectricalHandOffDetailsResponse();
    }

    /**
     * Create an instance of {@link SetElectricalHandOffDetails }
     * 
     */
    public SetElectricalHandOffDetails createSetElectricalHandOffDetails() {
        return new SetElectricalHandOffDetails();
    }

    /**
     * Create an instance of {@link ReserveTSTallyResponse }
     * 
     */
    public ReserveTSTallyResponse createReserveTSTallyResponse() {
        return new ReserveTSTallyResponse();
    }

    /**
     * Create an instance of {@link ReserveTSTally }
     * 
     */
    public ReserveTSTally createReserveTSTally() {
        return new ReserveTSTally();
    }

    /**
     * Create an instance of {@link TimeSlotSubmitionFault }
     * 
     */
    public TimeSlotSubmitionFault createTimeSlotSubmitionFault() {
        return new TimeSlotSubmitionFault();
    }

    /**
     * Create an instance of {@link RequestHeader }
     * 
     */
    public RequestHeader createRequestHeader() {
        return new RequestHeader();
    }

    /**
     * Create an instance of {@link TimeSlot }
     * 
     */
    public TimeSlot createTimeSlot() {
        return new TimeSlot();
    }

    /**
     * Create an instance of {@link OffnetInterface }
     * 
     */
    public OffnetInterface createOffnetInterface() {
        return new OffnetInterface();
    }

    /**
     * Create an instance of {@link WorkerOffnetInterface }
     * 
     */
    public WorkerOffnetInterface createWorkerOffnetInterface() {
        return new WorkerOffnetInterface();
    }

    /**
     * Create an instance of {@link ProtectionOffnetInterface }
     * 
     */
    public ProtectionOffnetInterface createProtectionOffnetInterface() {
        return new ProtectionOffnetInterface();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", name = "setOpticalHandOffDetailsFault1_setOpticalHandOffDetailsFault")
    public JAXBElement<Acknowledgement> createSetOpticalHandOffDetailsFault1SetOpticalHandOffDetailsFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_SetOpticalHandOffDetailsFault1SetOpticalHandOffDetailsFault_QNAME, Acknowledgement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", name = "setElectricalHandOffDetailsFault1_setElectricalHandOffDetailsFault")
    public JAXBElement<Acknowledgement> createSetElectricalHandOffDetailsFault1SetElectricalHandOffDetailsFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_SetElectricalHandOffDetailsFault1SetElectricalHandOffDetailsFault_QNAME, Acknowledgement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Acknowledgement }{@code >}
     */
    @XmlElementDecl(namespace = "com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", name = "submitTSSelectionFaultFault1_submitFault")
    public JAXBElement<Acknowledgement> createSubmitTSSelectionFaultFault1SubmitFault(Acknowledgement value) {
        return new JAXBElement<Acknowledgement>(_SubmitTSSelectionFaultFault1SubmitFault_QNAME, Acknowledgement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitTSSelectionFaultResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubmitTSSelectionFaultResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", name = "submitTSSelectionFaultResponse")
    public JAXBElement<SubmitTSSelectionFaultResponse> createSubmitTSSelectionFaultResponse(SubmitTSSelectionFaultResponse value) {
        return new JAXBElement<SubmitTSSelectionFaultResponse>(_SubmitTSSelectionFaultResponse_QNAME, SubmitTSSelectionFaultResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitTSSelectionFault }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubmitTSSelectionFault }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", name = "submitTSSelectionFault")
    public JAXBElement<SubmitTSSelectionFault> createSubmitTSSelectionFault(SubmitTSSelectionFault value) {
        return new JAXBElement<SubmitTSSelectionFault>(_SubmitTSSelectionFault_QNAME, SubmitTSSelectionFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetElectricalHandOffDetailsResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetElectricalHandOffDetailsResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", name = "setElectricalHandOffDetailsResponse")
    public JAXBElement<SetElectricalHandOffDetailsResponse> createSetElectricalHandOffDetailsResponse(SetElectricalHandOffDetailsResponse value) {
        return new JAXBElement<SetElectricalHandOffDetailsResponse>(_SetElectricalHandOffDetailsResponse_QNAME, SetElectricalHandOffDetailsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetElectricalHandOffDetails }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SetElectricalHandOffDetails }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", name = "setElectricalHandOffDetails")
    public JAXBElement<SetElectricalHandOffDetails> createSetElectricalHandOffDetails(SetElectricalHandOffDetails value) {
        return new JAXBElement<SetElectricalHandOffDetails>(_SetElectricalHandOffDetails_QNAME, SetElectricalHandOffDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveTSTallyResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReserveTSTallyResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", name = "ReserveTSTallyResponse")
    public JAXBElement<ReserveTSTallyResponse> createReserveTSTallyResponse(ReserveTSTallyResponse value) {
        return new JAXBElement<ReserveTSTallyResponse>(_ReserveTSTallyResponse_QNAME, ReserveTSTallyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReserveTSTally }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReserveTSTally }{@code >}
     */
    @XmlElementDecl(namespace = "urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption", name = "ReserveTSTally")
    public JAXBElement<ReserveTSTally> createReserveTSTally(ReserveTSTally value) {
        return new JAXBElement<ReserveTSTally>(_ReserveTSTally_QNAME, ReserveTSTally.class, null, value);
    }

}
