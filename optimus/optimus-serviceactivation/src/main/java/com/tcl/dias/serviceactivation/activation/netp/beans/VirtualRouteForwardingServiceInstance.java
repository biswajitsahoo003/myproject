
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VirtualRouteForwardingServiceInstance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VirtualRouteForwardingServiceInstance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maximumRoutes" type="{http://www.tcl.com/2011/11/ipsvc/xsd}MaximumRoutes" minOccurs="0"/>
 *         &lt;element name="ALUVPRNImportPolicy" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" minOccurs="0"/>
 *         &lt;element name="ALUVPRNExportPolicy" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" minOccurs="0"/>
 *         &lt;element name="CiscoImportPolicy" type="{http://www.tcl.com/2011/11/ipsvc/xsd}CiscoImportMap" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VirtualRouteForwardingServiceInstance", propOrder = {
    "name",
    "maximumRoutes",
    "aluvprnImportPolicy",
    "aluvprnExportPolicy",
    "ciscoImportPolicy"
})
public class VirtualRouteForwardingServiceInstance {

    protected String name;
    protected MaximumRoutes maximumRoutes;
    @XmlElement(name = "ALUVPRNImportPolicy")
    protected RoutingPolicy aluvprnImportPolicy;
    @XmlElement(name = "ALUVPRNExportPolicy")
    protected RoutingPolicy aluvprnExportPolicy;
    @XmlElement(name = "CiscoImportPolicy")
    protected CiscoImportMap ciscoImportPolicy;

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
     * Gets the value of the maximumRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link MaximumRoutes }
     *     
     */
    public MaximumRoutes getMaximumRoutes() {
        return maximumRoutes;
    }

    /**
     * Sets the value of the maximumRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link MaximumRoutes }
     *     
     */
    public void setMaximumRoutes(MaximumRoutes value) {
        this.maximumRoutes = value;
    }

    /**
     * Gets the value of the aluvprnImportPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingPolicy }
     *     
     */
    public RoutingPolicy getALUVPRNImportPolicy() {
        return aluvprnImportPolicy;
    }

    /**
     * Sets the value of the aluvprnImportPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingPolicy }
     *     
     */
    public void setALUVPRNImportPolicy(RoutingPolicy value) {
        this.aluvprnImportPolicy = value;
    }

    /**
     * Gets the value of the aluvprnExportPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingPolicy }
     *     
     */
    public RoutingPolicy getALUVPRNExportPolicy() {
        return aluvprnExportPolicy;
    }

    /**
     * Sets the value of the aluvprnExportPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingPolicy }
     *     
     */
    public void setALUVPRNExportPolicy(RoutingPolicy value) {
        this.aluvprnExportPolicy = value;
    }

    /**
     * Gets the value of the ciscoImportPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link CiscoImportMap }
     *     
     */
    public CiscoImportMap getCiscoImportPolicy() {
        return ciscoImportPolicy;
    }

    /**
     * Sets the value of the ciscoImportPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link CiscoImportMap }
     *     
     */
    public void setCiscoImportPolicy(CiscoImportMap value) {
        this.ciscoImportPolicy = value;
    }

}
