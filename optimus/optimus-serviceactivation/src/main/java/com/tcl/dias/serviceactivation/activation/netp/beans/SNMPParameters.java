
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SNMPParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SNMPParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SNMPPermission" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SNMPTrapIPList" type="{http://www.tcl.com/2014/2/ipsvc/xsd}IPAddressList" minOccurs="0"/>
 *         &lt;element name="SNMPAccessingIPList" type="{http://www.tcl.com/2014/2/ipsvc/xsd}IPAddressList" minOccurs="0"/>
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
@XmlType(name = "SNMPParameters", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "snmpPermission",
    "snmpTrapIPList",
    "snmpAccessingIPList",
    "isObjectInstanceModified"
})
public class SNMPParameters {

    @XmlElement(name = "SNMPPermission")
    protected String snmpPermission;
    @XmlElement(name = "SNMPTrapIPList")
    protected IPAddressList snmpTrapIPList;
    @XmlElement(name = "SNMPAccessingIPList")
    protected IPAddressList snmpAccessingIPList;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the snmpPermission property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSNMPPermission() {
        return snmpPermission;
    }

    /**
     * Sets the value of the snmpPermission property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSNMPPermission(String value) {
        this.snmpPermission = value;
    }

    /**
     * Gets the value of the snmpTrapIPList property.
     * 
     * @return
     *     possible object is
     *     {@link IPAddressList }
     *     
     */
    public IPAddressList getSNMPTrapIPList() {
        return snmpTrapIPList;
    }

    /**
     * Sets the value of the snmpTrapIPList property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPAddressList }
     *     
     */
    public void setSNMPTrapIPList(IPAddressList value) {
        this.snmpTrapIPList = value;
    }

    /**
     * Gets the value of the snmpAccessingIPList property.
     * 
     * @return
     *     possible object is
     *     {@link IPAddressList }
     *     
     */
    public IPAddressList getSNMPAccessingIPList() {
        return snmpAccessingIPList;
    }

    /**
     * Sets the value of the snmpAccessingIPList property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPAddressList }
     *     
     */
    public void setSNMPAccessingIPList(IPAddressList value) {
        this.snmpAccessingIPList = value;
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
