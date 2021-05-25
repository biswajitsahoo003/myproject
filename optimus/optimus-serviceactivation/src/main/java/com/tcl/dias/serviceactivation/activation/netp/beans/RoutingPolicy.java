
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoutingPolicy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RoutingPolicy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isStandardPolicy" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="matchCriteria" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicyMatchCriteria" minOccurs="0"/>
 *         &lt;element name="setCriteria" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicySetCriteria" minOccurs="0"/>
 *         &lt;element name="isPreprovisioned" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="routingPolicyTerm" type="{http://com/tcl/www/_2011/_11/ipsvc/xsd}RoutingPolicyTerm" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoutingPolicy", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "isStandardPolicy",
    "name",
    "matchCriteria",
    "setCriteria",
    "isPreprovisioned",
    "isObjectInstanceModified",
    "routingPolicyTerm"
})
public class RoutingPolicy {

    protected Boolean isStandardPolicy;
    protected String name;
    protected RoutingPolicyMatchCriteria matchCriteria;
    protected RoutingPolicySetCriteria setCriteria;
    protected Boolean isPreprovisioned;
    protected Boolean isObjectInstanceModified;
    protected List<RoutingPolicyTerm> routingPolicyTerm;

    /**
     * Gets the value of the isStandardPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsStandardPolicy() {
        return isStandardPolicy;
    }

    /**
     * Sets the value of the isStandardPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsStandardPolicy(Boolean value) {
        this.isStandardPolicy = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
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
     * Gets the value of the isPreprovisioned property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsPreprovisioned() {
        return isPreprovisioned;
    }

    /**
     * Sets the value of the isPreprovisioned property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsPreprovisioned(Boolean value) {
        this.isPreprovisioned = value;
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
     * Gets the value of the routingPolicyTerm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the routingPolicyTerm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoutingPolicyTerm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoutingPolicyTerm }
     * 
     * 
     */
    public List<RoutingPolicyTerm> getRoutingPolicyTerm() {
        if (routingPolicyTerm == null) {
            routingPolicyTerm = new ArrayList<RoutingPolicyTerm>();
        }
        return this.routingPolicyTerm;
    }

}
