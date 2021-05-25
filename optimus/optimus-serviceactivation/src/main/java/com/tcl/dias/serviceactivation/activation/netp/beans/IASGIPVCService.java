
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IAS_GIPVCService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IAS_GIPVCService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}IAService">
 *       &lt;sequence>
 *         &lt;element name="Filter" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="BROWSING"/>
 *               &lt;enumeration value="NON_BROWSING"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ACLName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IAS_GIPVCService", propOrder = {
    "filter",
    "aclName"
})
public class IASGIPVCService
    extends IAService
{

    @XmlElement(name = "Filter")
    protected String filter;
    @XmlElement(name = "ACLName")
    protected String aclName;

    /**
     * Gets the value of the filter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Sets the value of the filter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilter(String value) {
        this.filter = value;
    }

    /**
     * Gets the value of the aclName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACLName() {
        return aclName;
    }

    /**
     * Sets the value of the aclName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACLName(String value) {
        this.aclName = value;
    }

}
