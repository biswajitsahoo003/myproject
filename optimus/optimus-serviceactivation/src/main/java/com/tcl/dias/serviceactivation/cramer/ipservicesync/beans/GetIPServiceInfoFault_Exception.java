
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "getIPServiceInfoFault", targetNamespace = "http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices")
public class GetIPServiceInfoFault_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private GetIPServiceInfoFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public GetIPServiceInfoFault_Exception(String message, GetIPServiceInfoFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public GetIPServiceInfoFault_Exception(String message, GetIPServiceInfoFault faultInfo, java.lang.Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: crameripservices.ws.service.ace.cramer.tatacommunications.com.GetIPServiceInfoFault
     */
    public GetIPServiceInfoFault getFaultInfo() {
        return faultInfo;
    }

}
