
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrefixListEntry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PrefixListEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NetworkPrefix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SubnetRangeMinimum" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="SubnetRangeMaximum" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Operator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrefixListEntry", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "networkPrefix",
    "subnetRangeMinimum",
    "subnetRangeMaximum",
    "operator"
})
public class PrefixListEntry {

    @XmlElement(name = "NetworkPrefix")
    protected String networkPrefix;
    @XmlElement(name = "SubnetRangeMinimum")
    protected Integer subnetRangeMinimum;
    @XmlElement(name = "SubnetRangeMaximum")
    protected Integer subnetRangeMaximum;
    @XmlElement(name = "Operator")
    protected String operator;

    /**
     * Gets the value of the networkPrefix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkPrefix() {
        return networkPrefix;
    }

    /**
     * Sets the value of the networkPrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkPrefix(String value) {
        this.networkPrefix = value;
    }

    /**
     * Gets the value of the subnetRangeMinimum property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSubnetRangeMinimum() {
        return subnetRangeMinimum;
    }

    /**
     * Sets the value of the subnetRangeMinimum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSubnetRangeMinimum(Integer value) {
        this.subnetRangeMinimum = value;
    }

    /**
     * Gets the value of the subnetRangeMaximum property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSubnetRangeMaximum() {
        return subnetRangeMaximum;
    }

    /**
     * Sets the value of the subnetRangeMaximum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSubnetRangeMaximum(Integer value) {
        this.subnetRangeMaximum = value;
    }

    /**
     * Gets the value of the operator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the value of the operator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperator(String value) {
        this.operator = value;
    }

}
