
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GenericCiscoIORRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GenericCiscoIORRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="router" type="{http://NetworkOrderServicesLibrary/netord/bo/_2012/_12}IORRouter" minOccurs="0"/>
 *         &lt;element name="uplinkPort1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uplinkPort2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="predefinedBGPNetwork" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="predefinedBGPNetworkMask" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="predefinedBGPRoutemapName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ospfProcessID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ospfAreaID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ospfNetwork" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="InstanceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "GenericCiscoIORRequest", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2013/_02", propOrder = {
    "serviceType",
    "serviceSubType",
    "router",
    "uplinkPort1",
    "uplinkPort2",
    "predefinedBGPNetwork",
    "predefinedBGPNetworkMask",
    "predefinedBGPRoutemapName",
    "ospfProcessID",
    "ospfAreaID",
    "ospfNetwork",
    "serviceID",
    "instanceID",
    "isObjectInstanceModified"
})
public class GenericCiscoIORRequest {

    protected String serviceType;
    protected String serviceSubType;
    protected IORRouter router;
    protected String uplinkPort1;
    protected String uplinkPort2;
    protected String predefinedBGPNetwork;
    protected String predefinedBGPNetworkMask;
    protected String predefinedBGPRoutemapName;
    protected String ospfProcessID;
    protected String ospfAreaID;
    protected String ospfNetwork;
    protected String serviceID;
    @XmlElement(name = "InstanceID")
    protected String instanceID;
    protected Boolean isObjectInstanceModified;

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
     * Gets the value of the serviceSubType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceSubType() {
        return serviceSubType;
    }

    /**
     * Sets the value of the serviceSubType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceSubType(String value) {
        this.serviceSubType = value;
    }

    /**
     * Gets the value of the router property.
     * 
     * @return
     *     possible object is
     *     {@link IORRouter }
     *     
     */
    public IORRouter getRouter() {
        return router;
    }

    /**
     * Sets the value of the router property.
     * 
     * @param value
     *     allowed object is
     *     {@link IORRouter }
     *     
     */
    public void setRouter(IORRouter value) {
        this.router = value;
    }

    /**
     * Gets the value of the uplinkPort1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUplinkPort1() {
        return uplinkPort1;
    }

    /**
     * Sets the value of the uplinkPort1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUplinkPort1(String value) {
        this.uplinkPort1 = value;
    }

    /**
     * Gets the value of the uplinkPort2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUplinkPort2() {
        return uplinkPort2;
    }

    /**
     * Sets the value of the uplinkPort2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUplinkPort2(String value) {
        this.uplinkPort2 = value;
    }

    /**
     * Gets the value of the predefinedBGPNetwork property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPredefinedBGPNetwork() {
        return predefinedBGPNetwork;
    }

    /**
     * Sets the value of the predefinedBGPNetwork property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPredefinedBGPNetwork(String value) {
        this.predefinedBGPNetwork = value;
    }

    /**
     * Gets the value of the predefinedBGPNetworkMask property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPredefinedBGPNetworkMask() {
        return predefinedBGPNetworkMask;
    }

    /**
     * Sets the value of the predefinedBGPNetworkMask property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPredefinedBGPNetworkMask(String value) {
        this.predefinedBGPNetworkMask = value;
    }

    /**
     * Gets the value of the predefinedBGPRoutemapName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPredefinedBGPRoutemapName() {
        return predefinedBGPRoutemapName;
    }

    /**
     * Sets the value of the predefinedBGPRoutemapName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPredefinedBGPRoutemapName(String value) {
        this.predefinedBGPRoutemapName = value;
    }

    /**
     * Gets the value of the ospfProcessID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOspfProcessID() {
        return ospfProcessID;
    }

    /**
     * Sets the value of the ospfProcessID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOspfProcessID(String value) {
        this.ospfProcessID = value;
    }

    /**
     * Gets the value of the ospfAreaID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOspfAreaID() {
        return ospfAreaID;
    }

    /**
     * Sets the value of the ospfAreaID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOspfAreaID(String value) {
        this.ospfAreaID = value;
    }

    /**
     * Gets the value of the ospfNetwork property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOspfNetwork() {
        return ospfNetwork;
    }

    /**
     * Sets the value of the ospfNetwork property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOspfNetwork(String value) {
        this.ospfNetwork = value;
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
