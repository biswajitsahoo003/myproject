
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HBSConfigParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HBSConfigParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mirUL" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mirDL" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="vlanMode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="none"/>
 *               &lt;enumeration value="tag"/>
 *               &lt;enumeration value="provider"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="dataVlanID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="dataVlanPriority" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="allowedVlanID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="untagVlanID" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="hsuIngressTraffic" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TAG"/>
 *               &lt;enumeration value="TRANSPARENT"/>
 *               &lt;enumeration value="NA"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="hsuEgressTraffic" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="FILTER"/>
 *               &lt;enumeration value="TRANSPARENT"/>
 *               &lt;enumeration value="UNTAGFILTERED"/>
 *               &lt;enumeration value="UNTAGALL"/>
 *               &lt;enumeration value="NA"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="field1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "HBSConfigParameters", namespace = "http://test-rad/com/tcl/www/_2014/_2/ipsvc/xsd", propOrder = {
    "mirUL",
    "mirDL",
    "vlanMode",
    "dataVlanID",
    "dataVlanPriority",
    "allowedVlanID",
    "untagVlanID",
    "hsuIngressTraffic",
    "hsuEgressTraffic",
    "field1",
    "field2",
    "field3",
    "field4",
    "isObjectInstanceModified"
})
public class HBSConfigParameters {

    protected Integer mirUL;
    protected Integer mirDL;
    protected String vlanMode;
    protected Integer dataVlanID;
    protected Integer dataVlanPriority;
    protected Integer allowedVlanID;
    protected Boolean untagVlanID;
    protected String hsuIngressTraffic;
    protected String hsuEgressTraffic;
    protected String field1;
    protected String field2;
    protected String field3;
    protected String field4;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the mirUL property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMirUL() {
        return mirUL;
    }

    /**
     * Sets the value of the mirUL property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMirUL(Integer value) {
        this.mirUL = value;
    }

    /**
     * Gets the value of the mirDL property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMirDL() {
        return mirDL;
    }

    /**
     * Sets the value of the mirDL property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMirDL(Integer value) {
        this.mirDL = value;
    }

    /**
     * Gets the value of the vlanMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVlanMode() {
        return vlanMode;
    }

    /**
     * Sets the value of the vlanMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVlanMode(String value) {
        this.vlanMode = value;
    }

    /**
     * Gets the value of the dataVlanID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDataVlanID() {
        return dataVlanID;
    }

    /**
     * Sets the value of the dataVlanID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDataVlanID(Integer value) {
        this.dataVlanID = value;
    }

    /**
     * Gets the value of the dataVlanPriority property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDataVlanPriority() {
        return dataVlanPriority;
    }

    /**
     * Sets the value of the dataVlanPriority property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDataVlanPriority(Integer value) {
        this.dataVlanPriority = value;
    }

    /**
     * Gets the value of the allowedVlanID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAllowedVlanID() {
        return allowedVlanID;
    }

    /**
     * Sets the value of the allowedVlanID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAllowedVlanID(Integer value) {
        this.allowedVlanID = value;
    }

    /**
     * Gets the value of the untagVlanID property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUntagVlanID() {
        return untagVlanID;
    }

    /**
     * Sets the value of the untagVlanID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUntagVlanID(Boolean value) {
        this.untagVlanID = value;
    }

    /**
     * Gets the value of the hsuIngressTraffic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHsuIngressTraffic() {
        return hsuIngressTraffic;
    }

    /**
     * Sets the value of the hsuIngressTraffic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHsuIngressTraffic(String value) {
        this.hsuIngressTraffic = value;
    }

    /**
     * Gets the value of the hsuEgressTraffic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHsuEgressTraffic() {
        return hsuEgressTraffic;
    }

    /**
     * Sets the value of the hsuEgressTraffic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHsuEgressTraffic(String value) {
        this.hsuEgressTraffic = value;
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
