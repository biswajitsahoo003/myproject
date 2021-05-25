
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataMDT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataMDT">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="Threshold" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataMDT", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "ipAddress",
    "threshold"
})
public class DataMDT {

    @XmlElement(name = "IPAddress")
    protected IPV4Address ipAddress;
    @XmlElement(name = "Threshold")
    protected Integer threshold;

    /**
     * Gets the value of the ipAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getIPAddress() {
        return ipAddress;
    }

    /**
     * Sets the value of the ipAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setIPAddress(IPV4Address value) {
        this.ipAddress = value;
    }

    /**
     * Gets the value of the threshold property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getThreshold() {
        return threshold;
    }

    /**
     * Sets the value of the threshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setThreshold(Integer value) {
        this.threshold = value;
    }

}
