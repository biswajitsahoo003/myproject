
package com.tcl.dias.serviceactivation.cramer.eoriordetails.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.cramer.eoriordetails.beans package. 
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

    private final static QName _ServiceFault_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails", "ServiceFault");
    private final static QName _GetIEOR_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails", "getIEOR");
    private final static QName _GetIEORResponse_QNAME = new QName("http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails", "getIEORResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.cramer.eoriordetails.beans
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
     * Create an instance of {@link GetIEOR }
     * 
     */
    public GetIEOR createGetIEOR() {
        return new GetIEOR();
    }

    /**
     * Create an instance of {@link GetIEORResponse }
     * 
     */
    public GetIEORResponse createGetIEORResponse() {
        return new GetIEORResponse();
    }

    /**
     * Create an instance of {@link CramerRequestHeader }
     * 
     */
    public CramerRequestHeader createCramerRequestHeader() {
        return new CramerRequestHeader();
    }

    /**
     * Create an instance of {@link IoreorDependancyOutput }
     * 
     */
    public IoreorDependancyOutput createIoreorDependancyOutput() {
        return new IoreorDependancyOutput();
    }

    /**
     * Create an instance of {@link EorList }
     * 
     */
    public EorList createEorList() {
        return new EorList();
    }

    /**
     * Create an instance of {@link IorList }
     * 
     */
    public IorList createIorList() {
        return new IorList();
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
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails", name = "ServiceFault")
    public JAXBElement<ServiceFault> createServiceFault(ServiceFault value) {
        return new JAXBElement<ServiceFault>(_ServiceFault_QNAME, ServiceFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIEOR }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetIEOR }{@code >}
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails", name = "getIEOR")
    public JAXBElement<GetIEOR> createGetIEOR(GetIEOR value) {
        return new JAXBElement<GetIEOR>(_GetIEOR_QNAME, GetIEOR.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetIEORResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetIEORResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails", name = "getIEORResponse")
    public JAXBElement<GetIEORResponse> createGetIEORResponse(GetIEORResponse value) {
        return new JAXBElement<GetIEORResponse>(_GetIEORResponse_QNAME, GetIEORResponse.class, null, value);
    }

}
