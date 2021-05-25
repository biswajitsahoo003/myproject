
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VirtualBearer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VirtualBearer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CienaVB" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}Worker" minOccurs="0"/>
 *         &lt;element name="CienaParam" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}CienaParam" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VirtualBearer", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "cienaVB",
    "cienaParam"
})
public class VirtualBearer {

    @XmlElement(name = "CienaVB")
    protected Worker cienaVB;
    @XmlElement(name = "CienaParam")
    protected CienaParam cienaParam;

    /**
     * Gets the value of the cienaVB property.
     * 
     * @return
     *     possible object is
     *     {@link Worker }
     *     
     */
    public Worker getCienaVB() {
        return cienaVB;
    }

    /**
     * Sets the value of the cienaVB property.
     * 
     * @param value
     *     allowed object is
     *     {@link Worker }
     *     
     */
    public void setCienaVB(Worker value) {
        this.cienaVB = value;
    }

    /**
     * Gets the value of the cienaParam property.
     * 
     * @return
     *     possible object is
     *     {@link CienaParam }
     *     
     */
    public CienaParam getCienaParam() {
        return cienaParam;
    }

    /**
     * Sets the value of the cienaParam property.
     * 
     * @param value
     *     allowed object is
     *     {@link CienaParam }
     *     
     */
    public void setCienaParam(CienaParam value) {
        this.cienaParam = value;
    }

}
