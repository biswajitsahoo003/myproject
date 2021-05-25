
package com.tcl.dias.serviceactivation.cramer.downtime.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for checkDownTimeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="checkDownTimeResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="downTimeResponse" type="{http://cramerserviceslibrary/csvc/wsdl/v1/downtime}downTimeRequiredResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkDownTimeResponse", propOrder = {
    "downTimeResponse"
})
@XmlRootElement
public class CheckDownTimeResponse {

    protected DownTimeRequiredResponse downTimeResponse;

    /**
     * Gets the value of the downTimeResponse property.
     * 
     * @return
     *     possible object is
     *     {@link DownTimeRequiredResponse }
     *     
     */
    public DownTimeRequiredResponse getDownTimeResponse() {
        return downTimeResponse;
    }

    /**
     * Sets the value of the downTimeResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link DownTimeRequiredResponse }
     *     
     */
    public void setDownTimeResponse(DownTimeRequiredResponse value) {
        this.downTimeResponse = value;
    }

}
