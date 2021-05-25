
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getIRSRouterInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getIRSRouterInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PERouter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "getIRSRouterInfo", propOrder = {
    "peRouter",
    "requestID"
})
public class GetIRSRouterInfo {

    @XmlElement(name = "PERouter")
    protected String peRouter;
    protected String requestID;

    /**
     * Gets the value of the peRouter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPERouter() {
        return peRouter;
    }

    /**
     * Sets the value of the peRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPERouter(String value) {
        this.peRouter = value;
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
