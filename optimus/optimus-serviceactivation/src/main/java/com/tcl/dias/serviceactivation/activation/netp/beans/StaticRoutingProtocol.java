
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StaticRoutingProtocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StaticRoutingProtocol">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}RoutingProtocol">
 *       &lt;sequence>
 *         &lt;element name="PEWANStaticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *         &lt;element name="CEWANStaticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *         &lt;element name="LocalPreference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localPreferenceV6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RedistributionRouteMapName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StaticRoutingProtocol", propOrder = {
    "pewanStaticRoutes",
    "cewanStaticRoutes",
    "localPreference",
    "localPreferenceV6",
    "redistributionRouteMapName"
})
public class StaticRoutingProtocol
    extends RoutingProtocol
{

    @XmlElement(name = "PEWANStaticRoutes")
    protected StaticRoutes pewanStaticRoutes;
    @XmlElement(name = "CEWANStaticRoutes")
    protected StaticRoutes cewanStaticRoutes;
    @XmlElement(name = "LocalPreference")
    protected String localPreference;
    protected String localPreferenceV6;
    @XmlElement(name = "RedistributionRouteMapName")
    protected String redistributionRouteMapName;

    /**
     * Gets the value of the pewanStaticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutes }
     *     
     */
    public StaticRoutes getPEWANStaticRoutes() {
        return pewanStaticRoutes;
    }

    /**
     * Sets the value of the pewanStaticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutes }
     *     
     */
    public void setPEWANStaticRoutes(StaticRoutes value) {
        this.pewanStaticRoutes = value;
    }

    /**
     * Gets the value of the cewanStaticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutes }
     *     
     */
    public StaticRoutes getCEWANStaticRoutes() {
        return cewanStaticRoutes;
    }

    /**
     * Sets the value of the cewanStaticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutes }
     *     
     */
    public void setCEWANStaticRoutes(StaticRoutes value) {
        this.cewanStaticRoutes = value;
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
     * Gets the value of the localPreferenceV6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalPreferenceV6() {
        return localPreferenceV6;
    }

    /**
     * Sets the value of the localPreferenceV6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalPreferenceV6(String value) {
        this.localPreferenceV6 = value;
    }

    /**
     * Gets the value of the redistributionRouteMapName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRedistributionRouteMapName() {
        return redistributionRouteMapName;
    }

    /**
     * Sets the value of the redistributionRouteMapName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRedistributionRouteMapName(String value) {
        this.redistributionRouteMapName = value;
    }

}
