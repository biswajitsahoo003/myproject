
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StaticRoutes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StaticRoutes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StaticRouteList" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IPSubnetADValueMap" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="serviceCommunity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="regionalCommunity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="popCommunity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "StaticRoutes", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "staticRouteList",
    "serviceCommunity",
    "regionalCommunity",
    "popCommunity",
    "isObjectInstanceModified"
})
public class StaticRoutes {

    @XmlElement(name = "StaticRouteList")
    protected List<IPSubnetADValueMap> staticRouteList;
    protected String serviceCommunity;
    protected String regionalCommunity;
    protected String popCommunity;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the staticRouteList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the staticRouteList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStaticRouteList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPSubnetADValueMap }
     * 
     * 
     */
    public List<IPSubnetADValueMap> getStaticRouteList() {
        if (staticRouteList == null) {
            staticRouteList = new ArrayList<IPSubnetADValueMap>();
        }
        return this.staticRouteList;
    }

    /**
     * Gets the value of the serviceCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceCommunity() {
        return serviceCommunity;
    }

    /**
     * Sets the value of the serviceCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceCommunity(String value) {
        this.serviceCommunity = value;
    }

    /**
     * Gets the value of the regionalCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionalCommunity() {
        return regionalCommunity;
    }

    /**
     * Sets the value of the regionalCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionalCommunity(String value) {
        this.regionalCommunity = value;
    }

    /**
     * Gets the value of the popCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPopCommunity() {
        return popCommunity;
    }

    /**
     * Sets the value of the popCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPopCommunity(String value) {
        this.popCommunity = value;
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
