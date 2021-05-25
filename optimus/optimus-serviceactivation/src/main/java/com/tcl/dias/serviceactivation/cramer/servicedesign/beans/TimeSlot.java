
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for timeSlot complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="timeSlot"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AU4_S" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="KLM_S" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timeSlot", propOrder = {
    "au4S",
    "klms"
})
public class TimeSlot {

    @XmlElement(name = "AU4_S")
    protected String au4S;
    @XmlElement(name = "KLM_S")
    protected String klms;

    /**
     * Gets the value of the au4S property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAU4S() {
        return au4S;
    }

    /**
     * Sets the value of the au4S property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAU4S(String value) {
        this.au4S = value;
    }

    /**
     * Gets the value of the klms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKLMS() {
        return klms;
    }

    /**
     * Sets the value of the klms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKLMS(String value) {
        this.klms = value;
    }

}
