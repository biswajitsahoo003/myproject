
package com.tcl.dias.serviceactivation.cramer.isvalidbts.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for isValidBTSResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="isValidBTSResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="validationOutput" type="{http://cramerserviceslibrary/csvc/wsdl/v2/ws/bts}btsValidationResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isValidBTSResponse", propOrder = {
    "validationOutput"
})
@XmlRootElement
public class IsValidBTSResponse {

    protected BtsValidationResponse validationOutput;

    /**
     * Gets the value of the validationOutput property.
     * 
     * @return
     *     possible object is
     *     {@link BtsValidationResponse }
     *     
     */
    public BtsValidationResponse getValidationOutput() {
        return validationOutput;
    }

    /**
     * Sets the value of the validationOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link BtsValidationResponse }
     *     
     */
    public void setValidationOutput(BtsValidationResponse value) {
        this.validationOutput = value;
    }

}
