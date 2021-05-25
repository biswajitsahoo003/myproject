
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CramerIPServices", targetNamespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", wsdlLocation = "file:/F:/o2c/Cramer/WSDLs/CramerIPServiceSyncWSDL.wsdl")
public class CramerIPServices_Service
    extends Service
{

    private final static URL CRAMERIPSERVICES_WSDL_LOCATION;
    private final static WebServiceException CRAMERIPSERVICES_EXCEPTION;
    private final static QName CRAMERIPSERVICES_QNAME = new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "CramerIPServices");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/F:/o2c/Cramer/WSDLs/CramerIPServiceSyncWSDL.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CRAMERIPSERVICES_WSDL_LOCATION = url;
        CRAMERIPSERVICES_EXCEPTION = e;
    }

    public CramerIPServices_Service() {
        super(__getWsdlLocation(), CRAMERIPSERVICES_QNAME);
    }

    public CramerIPServices_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), CRAMERIPSERVICES_QNAME, features);
    }

    public CramerIPServices_Service(URL wsdlLocation) {
        super(wsdlLocation, CRAMERIPSERVICES_QNAME);
    }

    public CramerIPServices_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CRAMERIPSERVICES_QNAME, features);
    }

    public CramerIPServices_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CramerIPServices_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CramerIPServices
     */
    @WebEndpoint(name = "CramerIPServicesPort")
    public CramerIPServices getCramerIPServicesPort() {
        return super.getPort(new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "CramerIPServicesPort"), CramerIPServices.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CramerIPServices
     */
    @WebEndpoint(name = "CramerIPServicesPort")
    public CramerIPServices getCramerIPServicesPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices", "CramerIPServicesPort"), CramerIPServices.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CRAMERIPSERVICES_EXCEPTION!= null) {
            throw CRAMERIPSERVICES_EXCEPTION;
        }
        return CRAMERIPSERVICES_WSDL_LOCATION;
    }

}
