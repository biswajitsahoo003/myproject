
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MPLSBackboneIORRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MPLSBackboneIORRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="IOR"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="serviceSubtype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Router1" type="{http://NetworkOrderServicesLibrary/netord/bo/_2012/_12}IORRouter" minOccurs="0"/>
 *         &lt;element name="Router2" type="{http://NetworkOrderServicesLibrary/netord/bo/_2012/_12}IORRouter" minOccurs="0"/>
 *         &lt;element name="routingProtocol" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}BackboneOSPFRoutingProtocol" minOccurs="0"/>
 *         &lt;element name="instanceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MPLSBackboneIORRequest", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2013/_01", propOrder = {
    "serviceType",
    "serviceSubtype",
    "serviceID",
    "router1",
    "router2",
    "routingProtocol",
    "instanceID",
    "isObjectInstanceModified"
})
public class MPLSBackboneIORRequest {

    protected String serviceType;
    protected String serviceSubtype;
    @XmlElement(name = "ServiceID")
    protected String serviceID;
    @XmlElement(name = "Router1")
    protected IORRouter router1;
    @XmlElement(name = "Router2")
    protected IORRouter router2;
    protected BackboneOSPFRoutingProtocol routingProtocol;
    protected String instanceID;
    protected String isObjectInstanceModified;

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

    /**
     * Gets the value of the serviceSubtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceSubtype() {
        return serviceSubtype;
    }

    /**
     * Sets the value of the serviceSubtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceSubtype(String value) {
        this.serviceSubtype = value;
    }

    /**
     * Gets the value of the serviceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceID() {
        return serviceID;
    }

    /**
     * Sets the value of the serviceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceID(String value) {
        this.serviceID = value;
    }

    /**
     * Gets the value of the router1 property.
     * 
     * @return
     *     possible object is
     *     {@link IORRouter }
     *     
     */
    public IORRouter getRouter1() {
        return router1;
    }

    /**
     * Sets the value of the router1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link IORRouter }
     *     
     */
    public void setRouter1(IORRouter value) {
        this.router1 = value;
    }

    /**
     * Gets the value of the router2 property.
     * 
     * @return
     *     possible object is
     *     {@link IORRouter }
     *     
     */
    public IORRouter getRouter2() {
        return router2;
    }

    /**
     * Sets the value of the router2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link IORRouter }
     *     
     */
    public void setRouter2(IORRouter value) {
        this.router2 = value;
    }

    /**
     * Gets the value of the routingProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link BackboneOSPFRoutingProtocol }
     *     
     */
    public BackboneOSPFRoutingProtocol getRoutingProtocol() {
        return routingProtocol;
    }

    /**
     * Sets the value of the routingProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link BackboneOSPFRoutingProtocol }
     *     
     */
    public void setRoutingProtocol(BackboneOSPFRoutingProtocol value) {
        this.routingProtocol = value;
    }

    /**
     * Gets the value of the instanceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceID() {
        return instanceID;
    }

    /**
     * Sets the value of the instanceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceID(String value) {
        this.instanceID = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsObjectInstanceModified(String value) {
        this.isObjectInstanceModified = value;
    }

}
