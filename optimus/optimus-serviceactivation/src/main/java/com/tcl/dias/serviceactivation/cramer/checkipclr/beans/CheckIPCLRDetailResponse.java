
package com.tcl.dias.serviceactivation.cramer.checkipclr.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for checkIPCLRDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="checkIPCLRDetailResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://cramerserviceslibrary/csvc/wsdl/v3/checkipclr}ipClrDetailResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkIPCLRDetailResponse", propOrder = {
    "_return"
})
@XmlRootElement
public class CheckIPCLRDetailResponse {

    @XmlElement(name = "return")
    protected IpClrDetailResponse _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link IpClrDetailResponse }
     *     
     */
    public IpClrDetailResponse getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link IpClrDetailResponse }
     *     
     */
    public void setReturn(IpClrDetailResponse value) {
        this._return = value;
    }

}
