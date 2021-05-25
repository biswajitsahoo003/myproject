
package com.tcl.dias.serviceactivation.cramer.createservicegvpn.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for serviceInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="serviceInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="service" type="{http://com.tcl.cboss.ossbss.isc.ws}service"/>
 *         &lt;element name="componentList" type="{http://com.tcl.cboss.ossbss.isc.ws}componentList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceInfo", propOrder = {
    "service",
    "componentList"
})
@XmlRootElement(name = "ServiceInfo")
public class ServiceInfo {

    @XmlElement(required = true)
    protected Service service;
    @XmlElement(required = true)
    protected ComponentList componentList;

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link Service }
     *     
     */
    public Service getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link Service }
     *     
     */
    public void setService(Service value) {
        this.service = value;
    }

    /**
     * Gets the value of the componentList property.
     * 
     * @return
     *     possible object is
     *     {@link ComponentList }
     *     
     */
    public ComponentList getComponentList() {
        return componentList;
    }

    /**
     * Sets the value of the componentList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComponentList }
     *     
     */
    public void setComponentList(ComponentList value) {
        this.componentList = value;
    }

}
