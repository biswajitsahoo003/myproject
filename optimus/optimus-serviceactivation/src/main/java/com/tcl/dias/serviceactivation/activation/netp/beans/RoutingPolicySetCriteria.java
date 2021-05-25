
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoutingPolicySetCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RoutingPolicySetCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RegexAS_Path" type="{http://www.tcl.com/2014/4/ipsvc/xsd}ALUASPathConfig" minOccurs="0"/>
 *         &lt;element name="neightbourCommunity" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicyNeighbourCommunity" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="localPreference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AS_Path_Prepend" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AS_Path_Prepend_Index" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="metric" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vpnName" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "RoutingPolicySetCriteria", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "regexASPath",
    "neightbourCommunity",
    "localPreference",
    "asPathPrepend",
    "asPathPrependIndex",
    "metric",
    "vpnName",
    "isObjectInstanceModified"
})
public class RoutingPolicySetCriteria {

    @XmlElement(name = "RegexAS_Path")
    protected ALUASPathConfig regexASPath;
    protected List<RoutingPolicyNeighbourCommunity> neightbourCommunity;
    protected String localPreference;
    @XmlElement(name = "AS_Path_Prepend")
    protected String asPathPrepend;
    @XmlElement(name = "AS_Path_Prepend_Index")
    protected Integer asPathPrependIndex;
    protected String metric;
    protected List<String> vpnName;
    protected Boolean isObjectInstanceModified;

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
     * Gets the value of the neightbourCommunity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the neightbourCommunity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNeightbourCommunity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoutingPolicyNeighbourCommunity }
     * 
     * 
     */
    public List<RoutingPolicyNeighbourCommunity> getNeightbourCommunity() {
        if (neightbourCommunity == null) {
            neightbourCommunity = new ArrayList<RoutingPolicyNeighbourCommunity>();
        }
        return this.neightbourCommunity;
    }

    /**
     * Gets the value of the localPreference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalPreference() {
        return localPreference;
    }

    /**
     * Sets the value of the localPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalPreference(String value) {
        this.localPreference = value;
    }

    /**
     * Gets the value of the asPathPrepend property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASPathPrepend() {
        return asPathPrepend;
    }

    /**
     * Sets the value of the asPathPrepend property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASPathPrepend(String value) {
        this.asPathPrepend = value;
    }

    /**
     * Gets the value of the asPathPrependIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getASPathPrependIndex() {
        return asPathPrependIndex;
    }

    /**
     * Sets the value of the asPathPrependIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setASPathPrependIndex(Integer value) {
        this.asPathPrependIndex = value;
    }

    /**
     * Gets the value of the metric property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetric() {
        return metric;
    }

    /**
     * Sets the value of the metric property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetric(String value) {
        this.metric = value;
    }

    /**
     * Gets the value of the vpnName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vpnName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVpnName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getVpnName() {
        if (vpnName == null) {
            vpnName = new ArrayList<String>();
        }
        return this.vpnName;
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
