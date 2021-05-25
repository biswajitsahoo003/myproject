
package com.tcl.dias.serviceactivation.cramer.isvalidbts.beans;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "ServiceFault", targetNamespace = "http://cramerserviceslibrary/csvc/wsdl/v2/ws/bts")
public class ServiceFault_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ServiceFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public ServiceFault_Exception(String message, ServiceFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public ServiceFault_Exception(String message, ServiceFault faultInfo, java.lang.Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.tcl.dias.networkinventoryclient.cramer.isvalidbts.beans.ServiceFault
     */
    public ServiceFault getFaultInfo() {
        return faultInfo;
    }

}
