
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HuaweiSwitchLagConfigParams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HuaweiSwitchLagConfigParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="XValue" type="{http://www.tcl.com/2014/5/ipsvc/xsd}HuaweiLAGInterfaceStackVLANParams" minOccurs="0"/>
 *         &lt;element name="YValue" type="{http://www.tcl.com/2014/5/ipsvc/xsd}HuaweiLAGInterfaceStackVLANParams" minOccurs="0"/>
 *         &lt;element name="ZValue" type="{http://www.tcl.com/2014/5/ipsvc/xsd}HuaweiLAGInterfaceStackVLANParams" minOccurs="0"/>
 *         &lt;element name="TaggedVLANIDs" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiVLANs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="UntaggedVLANSIDs" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiVLANs" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "HuaweiSwitchLagConfigParams", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "xValue",
    "yValue",
    "zValue",
    "taggedVLANIDs",
    "untaggedVLANSIDs",
    "isObjectInstanceModified"
})
public class HuaweiSwitchLagConfigParams {

    @XmlElement(name = "XValue")
    protected HuaweiLAGInterfaceStackVLANParams xValue;
    @XmlElement(name = "YValue")
    protected HuaweiLAGInterfaceStackVLANParams yValue;
    @XmlElement(name = "ZValue")
    protected HuaweiLAGInterfaceStackVLANParams zValue;
    @XmlElement(name = "TaggedVLANIDs")
    protected List<HuaweiVLANs> taggedVLANIDs;
    @XmlElement(name = "UntaggedVLANSIDs")
    protected List<HuaweiVLANs> untaggedVLANSIDs;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the xValue property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiLAGInterfaceStackVLANParams }
     *     
     */
    public HuaweiLAGInterfaceStackVLANParams getXValue() {
        return xValue;
    }

    /**
     * Sets the value of the xValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiLAGInterfaceStackVLANParams }
     *     
     */
    public void setXValue(HuaweiLAGInterfaceStackVLANParams value) {
        this.xValue = value;
    }

    /**
     * Gets the value of the yValue property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiLAGInterfaceStackVLANParams }
     *     
     */
    public HuaweiLAGInterfaceStackVLANParams getYValue() {
        return yValue;
    }

    /**
     * Sets the value of the yValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiLAGInterfaceStackVLANParams }
     *     
     */
    public void setYValue(HuaweiLAGInterfaceStackVLANParams value) {
        this.yValue = value;
    }

    /**
     * Gets the value of the zValue property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiLAGInterfaceStackVLANParams }
     *     
     */
    public HuaweiLAGInterfaceStackVLANParams getZValue() {
        return zValue;
    }

    /**
     * Sets the value of the zValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiLAGInterfaceStackVLANParams }
     *     
     */
    public void setZValue(HuaweiLAGInterfaceStackVLANParams value) {
        this.zValue = value;
    }

    /**
     * Gets the value of the taggedVLANIDs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taggedVLANIDs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaggedVLANIDs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HuaweiVLANs }
     * 
     * 
     */
    public List<HuaweiVLANs> getTaggedVLANIDs() {
        if (taggedVLANIDs == null) {
            taggedVLANIDs = new ArrayList<HuaweiVLANs>();
        }
        return this.taggedVLANIDs;
    }

    /**
     * Gets the value of the untaggedVLANSIDs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the untaggedVLANSIDs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUntaggedVLANSIDs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HuaweiVLANs }
     * 
     * 
     */
    public List<HuaweiVLANs> getUntaggedVLANSIDs() {
        if (untaggedVLANSIDs == null) {
            untaggedVLANSIDs = new ArrayList<HuaweiVLANs>();
        }
        return this.untaggedVLANSIDs;
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
