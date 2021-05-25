
package com.tcl.dias.serviceactivation.cramer.ipservicesync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setCLRResponseClr complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setCLRResponseClr">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="result" type="{http://com.tatacommunications.cramer.ace.service.ws.CramerIPServices}SetClrResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setCLRResponseClr", propOrder = {
    "result"
})
@XmlRootElement(name ="setCLRResponse") 
public class SetCLRResponseClr {

    protected SetClrResponse result;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link SetClrResponse }
     *     
     */
    public SetClrResponse getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link SetClrResponse }
     *     
     */
    public void setResult(SetClrResponse value) {
        this.result = value;
    }

}
