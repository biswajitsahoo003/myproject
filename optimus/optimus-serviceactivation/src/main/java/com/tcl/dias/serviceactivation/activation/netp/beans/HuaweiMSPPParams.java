
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HuaweiMSPPParams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HuaweiMSPPParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="logicType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LCASMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HuaweiMSPPParams", namespace = "http://www.tcl.com/2014/11/transmissionsvc/xsd", propOrder = {
    "logicType",
    "lcasMode",
    "attribute1",
    "attribute2"
})
public class HuaweiMSPPParams {

    protected String logicType;
    @XmlElement(name = "LCASMode")
    protected String lcasMode;
    protected String attribute1;
    protected String attribute2;

    /**
     * Gets the value of the logicType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogicType() {
        return logicType;
    }

    /**
     * Sets the value of the logicType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogicType(String value) {
        this.logicType = value;
    }

    /**
     * Gets the value of the lcasMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLCASMode() {
        return lcasMode;
    }

    /**
     * Sets the value of the lcasMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLCASMode(String value) {
        this.lcasMode = value;
    }

    /**
     * Gets the value of the attribute1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute1() {
        return attribute1;
    }

    /**
     * Sets the value of the attribute1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute1(String value) {
        this.attribute1 = value;
    }

    /**
     * Gets the value of the attribute2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute2() {
        return attribute2;
    }

    /**
     * Sets the value of the attribute2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute2(String value) {
        this.attribute2 = value;
    }

}
