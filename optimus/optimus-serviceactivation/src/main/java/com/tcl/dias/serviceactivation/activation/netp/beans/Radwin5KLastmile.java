
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Radwin5kLastmile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Radwin5kLastmile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BTSIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BSName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PortSpeed" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="serviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HSUConfigParameters" type="{http://test-rad/com/tcl/www/_2014/_2/ipsvc/xsd}HSUConfigParameters" minOccurs="0"/>
 *         &lt;element name="HBSConfigParameters" type="{http://test-rad/com/tcl/www/_2014/_2/ipsvc/xsd}HBSConfigParameters" minOccurs="0"/>
 *         &lt;element name="field1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="InstanceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Radwin5kLastmile", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "btsip",
    "bsName",
    "portSpeed",
    "serviceType",
    "serviceSubType",
    "hsuConfigParameters",
    "hbsConfigParameters",
    "field1",
    "field2",
    "field3",
    "field4",
    "instanceID",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified"
})
public class Radwin5KLastmile {

    @XmlElement(name = "BTSIP")
    protected String btsip;
    @XmlElement(name = "BSName")
    protected String bsName;
    @XmlElement(name = "PortSpeed")
    protected Bandwidth portSpeed;
    protected String serviceType;
    protected String serviceSubType;
    @XmlElement(name = "HSUConfigParameters")
    protected HSUConfigParameters hsuConfigParameters;
    @XmlElement(name = "HBSConfigParameters")
    protected HBSConfigParameters hbsConfigParameters;
    protected String field1;
    protected String field2;
    protected String field3;
    protected String field4;
    @XmlElement(name = "InstanceID")
    protected String instanceID;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;

    /**
     * Gets the value of the btsip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBTSIP() {
        return btsip;
    }

    /**
     * Sets the value of the btsip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBTSIP(String value) {
        this.btsip = value;
    }

    /**
     * Gets the value of the bsName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBSName() {
        return bsName;
    }

    /**
     * Sets the value of the bsName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBSName(String value) {
        this.bsName = value;
    }

    /**
     * Gets the value of the portSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getPortSpeed() {
        return portSpeed;
    }

    /**
     * Sets the value of the portSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setPortSpeed(Bandwidth value) {
        this.portSpeed = value;
    }

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

    /**
     * Gets the value of the serviceSubType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceSubType() {
        return serviceSubType;
    }

    /**
     * Sets the value of the serviceSubType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceSubType(String value) {
        this.serviceSubType = value;
    }

    /**
     * Gets the value of the hsuConfigParameters property.
     * 
     * @return
     *     possible object is
     *     {@link HSUConfigParameters }
     *     
     */
    public HSUConfigParameters getHSUConfigParameters() {
        return hsuConfigParameters;
    }

    /**
     * Sets the value of the hsuConfigParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link HSUConfigParameters }
     *     
     */
    public void setHSUConfigParameters(HSUConfigParameters value) {
        this.hsuConfigParameters = value;
    }

    /**
     * Gets the value of the hbsConfigParameters property.
     * 
     * @return
     *     possible object is
     *     {@link HBSConfigParameters }
     *     
     */
    public HBSConfigParameters getHBSConfigParameters() {
        return hbsConfigParameters;
    }

    /**
     * Sets the value of the hbsConfigParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link HBSConfigParameters }
     *     
     */
    public void setHBSConfigParameters(HBSConfigParameters value) {
        this.hbsConfigParameters = value;
    }

    /**
     * Gets the value of the field1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField1() {
        return field1;
    }

    /**
     * Sets the value of the field1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField1(String value) {
        this.field1 = value;
    }

    /**
     * Gets the value of the field2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField2() {
        return field2;
    }

    /**
     * Sets the value of the field2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField2(String value) {
        this.field2 = value;
    }

    /**
     * Gets the value of the field3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField3() {
        return field3;
    }

    /**
     * Sets the value of the field3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField3(String value) {
        this.field3 = value;
    }

    /**
     * Gets the value of the field4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField4() {
        return field4;
    }

    /**
     * Sets the value of the field4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField4(String value) {
        this.field4 = value;
    }

    /**
     * Gets the value of the instanceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceID() {
        return instanceID;
    }

    /**
     * Sets the value of the instanceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceID(String value) {
        this.instanceID = value;
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

    /**
     * Gets the value of the isChildObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsChildObjectInstanceModified() {
        return isChildObjectInstanceModified;
    }

    /**
     * Sets the value of the isChildObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsChildObjectInstanceModified(Boolean value) {
        this.isChildObjectInstanceModified = value;
    }

}
