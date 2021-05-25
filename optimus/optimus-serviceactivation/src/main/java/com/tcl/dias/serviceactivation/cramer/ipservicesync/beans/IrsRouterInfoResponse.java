
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for irsRouterInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="irsRouterInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IRSLoopbackIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IRSRouter" type="{http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices}irsRouter" minOccurs="0"/>
 *         &lt;element name="requestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "irsRouterInfoResponse", propOrder = {
    "irsLoopbackIP",
    "irsRouter",
    "requestID"
})
public class IrsRouterInfoResponse {

    @XmlElement(name = "IRSLoopbackIP")
    protected String irsLoopbackIP;
    @XmlElement(name = "IRSRouter")
    protected IrsRouter irsRouter;
    protected String requestID;

    /**
     * Gets the value of the irsLoopbackIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIRSLoopbackIP() {
        return irsLoopbackIP;
    }

    /**
     * Sets the value of the irsLoopbackIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIRSLoopbackIP(String value) {
        this.irsLoopbackIP = value;
    }

    /**
     * Gets the value of the irsRouter property.
     * 
     * @return
     *     possible object is
     *     {@link IrsRouter }
     *     
     */
    public IrsRouter getIRSRouter() {
        return irsRouter;
    }

    /**
     * Sets the value of the irsRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link IrsRouter }
     *     
     */
    public void setIRSRouter(IrsRouter value) {
        this.irsRouter = value;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

}
