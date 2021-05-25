
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoutingPolicyTerm complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RoutingPolicyTerm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="termAction" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ACCEPT"/>
 *               &lt;enumeration value="REJECT"/>
 *               &lt;enumeration value="NEXT-POLICY"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="matchCriteria" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicyMatchCriteria" minOccurs="0"/>
 *         &lt;element name="setCriteria" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicySetCriteria" minOccurs="0"/>
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
@XmlType(name = "RoutingPolicyTerm", namespace = "http://com/tcl/www/_2011/_11/ipsvc/xsd", propOrder = {
    "id",
    "termAction",
    "matchCriteria",
    "setCriteria",
    "isObjectInstanceModified"
})
public class RoutingPolicyTerm {

    protected String id;
    protected String termAction;
    protected RoutingPolicyMatchCriteria matchCriteria;
    protected RoutingPolicySetCriteria setCriteria;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the termAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermAction() {
        return termAction;
    }

    /**
     * Sets the value of the termAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermAction(String value) {
        this.termAction = value;
    }

    /**
     * Gets the value of the matchCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingPolicyMatchCriteria }
     *     
     */
    public RoutingPolicyMatchCriteria getMatchCriteria() {
        return matchCriteria;
    }

    /**
     * Sets the value of the matchCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingPolicyMatchCriteria }
     *     
     */
    public void setMatchCriteria(RoutingPolicyMatchCriteria value) {
        this.matchCriteria = value;
    }

    /**
     * Gets the value of the setCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingPolicySetCriteria }
     *     
     */
    public RoutingPolicySetCriteria getSetCriteria() {
        return setCriteria;
    }

    /**
     * Sets the value of the setCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingPolicySetCriteria }
     *     
     */
    public void setSetCriteria(RoutingPolicySetCriteria value) {
        this.setCriteria = value;
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
