
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IZO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IZO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="izoPrivate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="cloudProvider" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cloudType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IZO", namespace = "http://IZO_Priv_25Oct17", propOrder = {
    "izoPrivate",
    "cloudProvider",
    "cloudType"
})
public class IZO {

    protected Boolean izoPrivate;
    protected String cloudProvider;
    protected String cloudType;

    /**
     * Gets the value of the izoPrivate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIzoPrivate() {
        return izoPrivate;
    }

    /**
     * Sets the value of the izoPrivate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIzoPrivate(Boolean value) {
        this.izoPrivate = value;
    }

    /**
     * Gets the value of the cloudProvider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCloudProvider() {
        return cloudProvider;
    }

    /**
     * Sets the value of the cloudProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCloudProvider(String value) {
        this.cloudProvider = value;
    }

    /**
     * Gets the value of the cloudType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCloudType() {
        return cloudType;
    }

    /**
     * Sets the value of the cloudType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCloudType(String value) {
        this.cloudType = value;
    }

}
