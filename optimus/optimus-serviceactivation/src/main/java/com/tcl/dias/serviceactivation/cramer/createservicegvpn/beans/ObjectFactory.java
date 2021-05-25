
package com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans package. 
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

    private final static QName _Success_QNAME = new QName("http://com.tcl.cboss.ossbss.isc.ws", "Success");
    private final static QName _ServiceInfo_QNAME = new QName("http://com.tcl.cboss.ossbss.isc.ws", "ServiceInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceInfo }
     * 
     */
    public ServiceInfo createServiceInfo() {
        return new ServiceInfo();
    }

    /**
     * Create an instance of {@link Component }
     * 
     */
    public Component createComponent() {
        return new Component();
    }

    /**
     * Create an instance of {@link Service }
     * 
     */
    public Service createService() {
        return new Service();
    }

    /**
     * Create an instance of {@link ComponentList }
     * 
     */
    public ComponentList createComponentList() {
        return new ComponentList();
    }

    /**
     * Create an instance of {@link MapEntry }
     * 
     */
    public MapEntry createMapEntry() {
        return new MapEntry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tcl.cboss.ossbss.isc.ws", name = "Success")
    public JAXBElement<Boolean> createSuccess(Boolean value) {
        return new JAXBElement<Boolean>(_Success_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.tcl.cboss.ossbss.isc.ws", name = "ServiceInfo")
    public JAXBElement<ServiceInfo> createServiceInfo(ServiceInfo value) {
        return new JAXBElement<ServiceInfo>(_ServiceInfo_QNAME, ServiceInfo.class, null, value);
    }

}
