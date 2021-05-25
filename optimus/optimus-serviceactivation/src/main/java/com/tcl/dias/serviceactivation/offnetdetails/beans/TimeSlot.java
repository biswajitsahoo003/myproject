
package com.tcl.dias.serviceactivation.offnetdetails.beans;

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
 *         &lt;element name="KLM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AU4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "klm",
    "au4"
})
public class TimeSlot {

    @XmlElement(name = "KLM")
    protected String klm;
    @XmlElement(name = "AU4")
    protected String au4;

    /**
     * Gets the value of the klm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKLM() {
        return klm;
    }

    /**
     * Sets the value of the klm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKLM(String value) {
        this.klm = value;
    }

    /**
     * Gets the value of the au4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAU4() {
        return au4;
    }

    /**
     * Sets the value of the au4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAU4(String value) {
        this.au4 = value;
    }

}
