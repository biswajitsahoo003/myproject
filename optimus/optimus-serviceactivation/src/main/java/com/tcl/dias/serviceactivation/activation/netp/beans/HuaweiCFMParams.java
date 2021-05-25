
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HuaweiCFMParams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HuaweiCFMParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MEPIDX" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MEPIDY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MDName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MAName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MDLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isSLAEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CFMMapVLANID" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiVLANs" minOccurs="0"/>
 *         &lt;element name="attribute1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "HuaweiCFMParams", namespace = "http://www.tcl.com/2014/5/ipsvc/xsd", propOrder = {
    "mepidx",
    "mepidy",
    "mdName",
    "maName",
    "mdLevel",
    "isSLAEnabled",
    "cfmMapVLANID",
    "attribute1",
    "attribute2",
    "isObjectInstanceModified"
})
public class HuaweiCFMParams {

    @XmlElement(name = "MEPIDX")
    protected String mepidx;
    @XmlElement(name = "MEPIDY")
    protected String mepidy;
    @XmlElement(name = "MDName")
    protected String mdName;
    @XmlElement(name = "MAName")
    protected String maName;
    @XmlElement(name = "MDLevel")
    protected String mdLevel;
    protected String isSLAEnabled;
    @XmlElement(name = "CFMMapVLANID")
    protected HuaweiVLANs cfmMapVLANID;
    protected String attribute1;
    protected String attribute2;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the mepidx property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMEPIDX() {
        return mepidx;
    }

    /**
     * Sets the value of the mepidx property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMEPIDX(String value) {
        this.mepidx = value;
    }

    /**
     * Gets the value of the mepidy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMEPIDY() {
        return mepidy;
    }

    /**
     * Sets the value of the mepidy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMEPIDY(String value) {
        this.mepidy = value;
    }

    /**
     * Gets the value of the mdName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDName() {
        return mdName;
    }

    /**
     * Sets the value of the mdName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDName(String value) {
        this.mdName = value;
    }

    /**
     * Gets the value of the maName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMAName() {
        return maName;
    }

    /**
     * Sets the value of the maName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMAName(String value) {
        this.maName = value;
    }

    /**
     * Gets the value of the mdLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDLevel() {
        return mdLevel;
    }

    /**
     * Sets the value of the mdLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDLevel(String value) {
        this.mdLevel = value;
    }

    /**
     * Gets the value of the isSLAEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsSLAEnabled() {
        return isSLAEnabled;
    }

    /**
     * Sets the value of the isSLAEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsSLAEnabled(String value) {
        this.isSLAEnabled = value;
    }

    /**
     * Gets the value of the cfmMapVLANID property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiVLANs }
     *     
     */
    public HuaweiVLANs getCFMMapVLANID() {
        return cfmMapVLANID;
    }

    /**
     * Sets the value of the cfmMapVLANID property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiVLANs }
     *     
     */
    public void setCFMMapVLANID(HuaweiVLANs value) {
        this.cfmMapVLANID = value;
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
