
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoutingPolicyMatchCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RoutingPolicyMatchCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PrefixList" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}PrefixList" minOccurs="0"/>
 *         &lt;element name="neighbourCommunity" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicyNeighbourCommunity" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RegexAS_Path" type="{http://www.tcl.com/2014/4/ipsvc/xsd}ALUASPathConfig" minOccurs="0"/>
 *         &lt;element name="protocol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "RoutingPolicyMatchCriteria", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "prefixList",
    "neighbourCommunity",
    "regexASPath",
    "protocol",
    "isObjectInstanceModified"
})
public class RoutingPolicyMatchCriteria {

    @XmlElement(name = "PrefixList")
    protected PrefixList prefixList;
    protected List<RoutingPolicyNeighbourCommunity> neighbourCommunity;
    @XmlElement(name = "RegexAS_Path")
    protected ALUASPathConfig regexASPath;
    protected String protocol;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the prefixList property.
     * 
     * @return
     *     possible object is
     *     {@link PrefixList }
     *     
     */
    public PrefixList getPrefixList() {
        return prefixList;
    }

    /**
     * Sets the value of the prefixList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrefixList }
     *     
     */
    public void setPrefixList(PrefixList value) {
        this.prefixList = value;
    }

    /**
     * Gets the value of the neighbourCommunity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the neighbourCommunity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNeighbourCommunity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoutingPolicyNeighbourCommunity }
     * 
     * 
     */
    public List<RoutingPolicyNeighbourCommunity> getNeighbourCommunity() {
        if (neighbourCommunity == null) {
            neighbourCommunity = new ArrayList<RoutingPolicyNeighbourCommunity>();
        }
        return this.neighbourCommunity;
    }

    /**
     * Gets the value of the regexASPath property.
     * 
     * @return
     *     possible object is
     *     {@link ALUASPathConfig }
     *     
     */
    public ALUASPathConfig getRegexASPath() {
        return regexASPath;
    }

    /**
     * Sets the value of the regexASPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link ALUASPathConfig }
     *     
     */
    public void setRegexASPath(ALUASPathConfig value) {
        this.regexASPath = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocol(String value) {
        this.protocol = value;
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
