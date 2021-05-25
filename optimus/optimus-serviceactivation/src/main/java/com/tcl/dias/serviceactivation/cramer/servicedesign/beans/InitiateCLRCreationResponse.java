
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for initiateCLRCreationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="initiateCLRCreationResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="clrCreationResult" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}serviceDesignResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "initiateCLRCreationResponse", propOrder = {
    "clrCreationResult"
})
@XmlRootElement(name = "initiateCLRCreationResponse")
public class InitiateCLRCreationResponse {

    protected ServiceDesignResponse clrCreationResult;

    /**
     * Gets the value of the clrCreationResult property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDesignResponse }
     *     
     */
    public ServiceDesignResponse getClrCreationResult() {
        return clrCreationResult;
    }

    /**
     * Sets the value of the clrCreationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDesignResponse }
     *     
     */
    public void setClrCreationResult(ServiceDesignResponse value) {
        this.clrCreationResult = value;
    }

}
