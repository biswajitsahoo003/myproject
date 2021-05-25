
package com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.tcl.dias.serviceactivation.beans.CramerServiceHeader;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://www.tcl.com/2012/09/csvc/xsd}CramerServiceHeader"/>
 *         &lt;element name="response" type="{http://www.tcl.com/2012/09/csvc/xsd}clrInfoResponse"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "response"
})
@XmlRootElement(name = "setCLRInfoResponse", namespace = "http://www.tcl.com/2012/09/csvc/inf")
public class SetCLRInfoResponse {

    @XmlElement(required = true, nillable = true)
    protected CramerServiceHeader header;
    @XmlElement(required = true, nillable = true)
    protected ClrInfoResponse response;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link CramerServiceHeader }
     *     
     */
    public CramerServiceHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link CramerServiceHeader }
     *     
     */
    public void setHeader(CramerServiceHeader value) {
        this.header = value;
    }

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link ClrInfoResponse }
     *     
     */
    public ClrInfoResponse getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClrInfoResponse }
     *     
     */
    public void setResponse(ClrInfoResponse value) {
        this.response = value;
    }

}
