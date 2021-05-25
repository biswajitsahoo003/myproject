
package com.tcl.dias.serviceactivation.cramer.eoriordetails.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getIEOR complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getIEOR"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RequestHeader" type="{http://cramerserviceslibrary/csvc/wsdl/v3/eoriordetails}cramerRequestHeader" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getIEOR", propOrder = {
    "requestHeader"
})
@XmlRootElement(name = "getIEOR")
public class GetIEOR {

    @XmlElement(name = "RequestHeader")
    protected CramerRequestHeader requestHeader;

    /**
     * Gets the value of the requestHeader property.
     * 
     * @return
     *     possible object is
     *     {@link CramerRequestHeader }
     *     
     */
    public CramerRequestHeader getRequestHeader() {
        return requestHeader;
    }

    /**
     * Sets the value of the requestHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link CramerRequestHeader }
     *     
     */
    public void setRequestHeader(CramerRequestHeader value) {
        this.requestHeader = value;
    }

}
