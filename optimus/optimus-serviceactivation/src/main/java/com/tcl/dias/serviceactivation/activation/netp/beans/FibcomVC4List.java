
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FibcomVC4List complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FibcomVC4List">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MSBId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MSBAU4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="InternalAU4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FibcomVC4List", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "msbId",
    "msbau4",
    "internalAU4"
})
public class FibcomVC4List {

    @XmlElement(name = "MSBId")
    protected String msbId;
    @XmlElement(name = "MSBAU4")
    protected String msbau4;
    @XmlElement(name = "InternalAU4")
    protected String internalAU4;

    /**
     * Gets the value of the msbId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSBId() {
        return msbId;
    }

    /**
     * Sets the value of the msbId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSBId(String value) {
        this.msbId = value;
    }

    /**
     * Gets the value of the msbau4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMSBAU4() {
        return msbau4;
    }

    /**
     * Sets the value of the msbau4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMSBAU4(String value) {
        this.msbau4 = value;
    }

    /**
     * Gets the value of the internalAU4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternalAU4() {
        return internalAU4;
    }

    /**
     * Sets the value of the internalAU4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternalAU4(String value) {
        this.internalAU4 = value;
    }

}
