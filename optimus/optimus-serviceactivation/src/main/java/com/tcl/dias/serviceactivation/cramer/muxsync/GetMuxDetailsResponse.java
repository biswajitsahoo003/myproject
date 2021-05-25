
package com.tcl.dias.serviceactivation.cramer.muxsync;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getMuxDetailsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getMuxDetailsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="muxDetailResponse" type="{http://cramerserviceslibrary/csvc/wsdl/v1/muxdetail}getMuxDetailResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMuxDetailsResponse", propOrder = {
    "muxDetailResponse"
})
@XmlRootElement
public class GetMuxDetailsResponse {

    protected GetMuxDetailResponse muxDetailResponse;

    /**
     * Gets the value of the muxDetailResponse property.
     * 
     * @return
     *     possible object is
     *     {@link GetMuxDetailResponse }
     *     
     */
    public GetMuxDetailResponse getMuxDetailResponse() {
        return muxDetailResponse;
    }

    /**
     * Sets the value of the muxDetailResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetMuxDetailResponse }
     *     
     */
    public void setMuxDetailResponse(GetMuxDetailResponse value) {
        this.muxDetailResponse = value;
    }

}
