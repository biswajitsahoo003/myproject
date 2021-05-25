
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NetworkName complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NetworkName">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="megaRegion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="majorRegion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NetworkName", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "megaRegion",
    "majorRegion"
})
public class NetworkName {

    protected String megaRegion;
    protected String majorRegion;

    /**
     * Gets the value of the megaRegion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMegaRegion() {
        return megaRegion;
    }

    /**
     * Sets the value of the megaRegion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMegaRegion(String value) {
        this.megaRegion = value;
    }

    /**
     * Gets the value of the majorRegion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMajorRegion() {
        return majorRegion;
    }

    /**
     * Sets the value of the majorRegion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMajorRegion(String value) {
        this.majorRegion = value;
    }

}
