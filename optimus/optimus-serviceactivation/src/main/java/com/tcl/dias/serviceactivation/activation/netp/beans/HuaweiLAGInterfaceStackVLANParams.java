
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HuaweiLAGInterfaceStackVLANParams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HuaweiLAGInterfaceStackVLANParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="destinationVLAN" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiVLANs" minOccurs="0"/>
 *         &lt;element name="stackedVLAN" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiVLANs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HuaweiLAGInterfaceStackVLANParams", namespace = "http://www.tcl.com/2014/5/ipsvc/xsd", propOrder = {
    "destinationVLAN",
    "stackedVLAN",
    "isObjectInstanceModified"
})
public class HuaweiLAGInterfaceStackVLANParams {

    protected HuaweiVLANs destinationVLAN;
    protected List<HuaweiVLANs> stackedVLAN;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the destinationVLAN property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiVLANs }
     *     
     */
    public HuaweiVLANs getDestinationVLAN() {
        return destinationVLAN;
    }

    /**
     * Sets the value of the destinationVLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiVLANs }
     *     
     */
    public void setDestinationVLAN(HuaweiVLANs value) {
        this.destinationVLAN = value;
    }

    /**
     * Gets the value of the stackedVLAN property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stackedVLAN property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStackedVLAN().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HuaweiVLANs }
     * 
     * 
     */
    public List<HuaweiVLANs> getStackedVLAN() {
        if (stackedVLAN == null) {
            stackedVLAN = new ArrayList<HuaweiVLANs>();
        }
        return this.stackedVLAN;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsObjectInstanceModified(Boolean value) {
        this.isObjectInstanceModified = value;
    }

}
