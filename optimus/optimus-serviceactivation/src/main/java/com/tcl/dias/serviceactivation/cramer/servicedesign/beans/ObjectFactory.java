
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.cramer.servicedesign.beans package. 
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

    private final static QName _ServiceFault_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr", "ServiceFault");
    private final static QName _InitiateCLRCreation_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr", "initiateCLRCreation");
    private final static QName _InitiateCLRCreationResponse_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr", "initiateCLRCreationResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.cramer.servicedesign.beans
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
     * Create an instance of {@link InitiateCLRCreation }
     * 
     */
    public InitiateCLRCreation createInitiateCLRCreation() {
        return new InitiateCLRCreation();
    }

    /**
     * Create an instance of {@link InitiateCLRCreationResponse }
     * 
     */
    public InitiateCLRCreationResponse createInitiateCLRCreationResponse() {
        return new InitiateCLRCreationResponse();
    }

    /**
     * Create an instance of {@link OrderDetails }
     * 
     */
    public OrderDetails createOrderDetails() {
        return new OrderDetails();
    }

    /**
     * Create an instance of {@link EndDetails }
     * 
     */
    public EndDetails createEndDetails() {
        return new EndDetails();
    }

    /**
     * Create an instance of {@link ProtOffnetIfaceDtls }
     * 
     */
    public ProtOffnetIfaceDtls createProtOffnetIfaceDtls() {
        return new ProtOffnetIfaceDtls();
    }

    /**
     * Create an instance of {@link TimeSlot }
     * 
     */
    public TimeSlot createTimeSlot() {
        return new TimeSlot();
    }

    /**
     * Create an instance of {@link WrkrOffnetIfaceDtls }
     * 
     */
    public WrkrOffnetIfaceDtls createWrkrOffnetIfaceDtls() {
        return new WrkrOffnetIfaceDtls();
    }

    /**
     * Create an instance of {@link UccServDetails }
     * 
     */
    public UccServDetails createUccServDetails() {
        return new UccServDetails();
    }

    /**
     * Create an instance of {@link UccService }
     * 
     */
    public UccService createUccService() {
        return new UccService();
    }

    /**
     * Create an instance of {@link Attributes }
     * 
     */
    public Attributes createAttributes() {
        return new Attributes();
    }

    /**
     * Create an instance of {@link UaApplicable }
     * 
     */
    public UaApplicable createUaApplicable() {
        return new UaApplicable();
    }

    /**
     * Create an instance of {@link ClrDesignDtls }
     * 
     */
    public ClrDesignDtls createClrDesignDtls() {
        return new ClrDesignDtls();
    }

    /**
     * Create an instance of {@link IldDtls }
     * 
     */
    public IldDtls createIldDtls() {
        return new IldDtls();
    }

    /**
     * Create an instance of {@link NldDtls }
     * 
     */
    public NldDtls createNldDtls() {
        return new NldDtls();
    }

    /**
     * Create an instance of {@link IpServiceAttributes }
     * 
     */
    public IpServiceAttributes createIpServiceAttributes() {
        return new IpServiceAttributes();
    }

    /**
     * Create an instance of {@link AdditionalAttr }
     * 
     */
    public AdditionalAttr createAdditionalAttr() {
        return new AdditionalAttr();
    }

    /**
     * Create an instance of {@link ServiceDesignResponse }
     * 
     */
    public ServiceDesignResponse createServiceDesignResponse() {
        return new ServiceDesignResponse();
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
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr", name = "ServiceFault")
    public JAXBElement<ServiceFault> createServiceFault(ServiceFault value) {
        return new JAXBElement<ServiceFault>(_ServiceFault_QNAME, ServiceFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitiateCLRCreation }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InitiateCLRCreation }{@code >}
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr", name = "initiateCLRCreation")
    public JAXBElement<InitiateCLRCreation> createInitiateCLRCreation(InitiateCLRCreation value) {
        return new JAXBElement<InitiateCLRCreation>(_InitiateCLRCreation_QNAME, InitiateCLRCreation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitiateCLRCreationResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InitiateCLRCreationResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr", name = "initiateCLRCreationResponse")
    public JAXBElement<InitiateCLRCreationResponse> createInitiateCLRCreationResponse(InitiateCLRCreationResponse value) {
        return new JAXBElement<InitiateCLRCreationResponse>(_InitiateCLRCreationResponse_QNAME, InitiateCLRCreationResponse.class, null, value);
    }

}
