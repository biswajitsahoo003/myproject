
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				CPE is inherited from Managed Element. Management IP is
 * 				inherited however it will not be used for CPE config
 * 				orders. Management INterface already has an IP Address
 * 				associated with it. Netp will use this field instead of
 * 				the mgmtIPAddress field defined in the Parent ME Object.
 * 			
 * 
 * <p>Java class for CustomerPremiseEquipment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerPremiseEquipment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hostName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="loopbackInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Interface" minOccurs="0"/>
 *         &lt;element name="isCEACEConfigurable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="wanInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}WANInterface" minOccurs="0"/>
 *         &lt;element name="lanInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}LANInterface" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="cpeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snmpServerCommunity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cpeInitConfigParams" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}CPEInitConfigParams" minOccurs="0"/>
 *         &lt;element name="isCPEReachable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CEVRFName" type="{http://www.tcl.com/2011/11/ipsvc/xsd}VirtualRouteForwardingServiceInstance" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerPremiseEquipment", propOrder = {
    "hostName",
    "loopbackInterface",
    "isCEACEConfigurable",
    "wanInterface",
    "lanInterface",
    "cpeType",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified",
    "snmpServerCommunity",
    "cpeInitConfigParams",
    "isCPEReachable",
    "cevrfName"
})
public class CustomerPremiseEquipment {

    protected String hostName;
    protected Interface loopbackInterface;
    protected Boolean isCEACEConfigurable;
    protected WANInterface wanInterface;
    protected List<LANInterface> lanInterface;
    protected String cpeType;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;
    protected String snmpServerCommunity;
    protected CPEInitConfigParams cpeInitConfigParams;
    protected Boolean isCPEReachable;
    @XmlElement(name = "CEVRFName")
    protected VirtualRouteForwardingServiceInstance cevrfName;

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the loopbackInterface property.
     * 
     * @return
     *     possible object is
     *     {@link Interface }
     *     
     */
    public Interface getLoopbackInterface() {
        return loopbackInterface;
    }

    /**
     * Sets the value of the loopbackInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link Interface }
     *     
     */
    public void setLoopbackInterface(Interface value) {
        this.loopbackInterface = value;
    }

    /**
     * Gets the value of the isCEACEConfigurable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCEACEConfigurable() {
        return isCEACEConfigurable;
    }

    /**
     * Sets the value of the isCEACEConfigurable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCEACEConfigurable(Boolean value) {
        this.isCEACEConfigurable = value;
    }

    /**
     * Gets the value of the wanInterface property.
     * 
     * @return
     *     possible object is
     *     {@link WANInterface }
     *     
     */
    public WANInterface getWanInterface() {
        return wanInterface;
    }

    /**
     * Sets the value of the wanInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link WANInterface }
     *     
     */
    public void setWanInterface(WANInterface value) {
        this.wanInterface = value;
    }

    /**
     * Gets the value of the lanInterface property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lanInterface property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanInterface().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LANInterface }
     * 
     * 
     */
    public List<LANInterface> getLanInterface() {
        if (lanInterface == null) {
            lanInterface = new ArrayList<LANInterface>();
        }
        return this.lanInterface;
    }

    /**
     * Gets the value of the cpeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpeType() {
        return cpeType;
    }

    /**
     * Sets the value of the cpeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpeType(String value) {
        this.cpeType = value;
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
     * Gets the value of the isChildObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsChildObjectInstanceModified() {
        return isChildObjectInstanceModified;
    }

    /**
     * Sets the value of the isChildObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsChildObjectInstanceModified(Boolean value) {
        this.isChildObjectInstanceModified = value;
    }

    /**
     * Gets the value of the snmpServerCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSnmpServerCommunity() {
        return snmpServerCommunity;
    }

    /**
     * Sets the value of the snmpServerCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSnmpServerCommunity(String value) {
        this.snmpServerCommunity = value;
    }

    /**
     * Gets the value of the cpeInitConfigParams property.
     * 
     * @return
     *     possible object is
     *     {@link CPEInitConfigParams }
     *     
     */
    public CPEInitConfigParams getCpeInitConfigParams() {
        return cpeInitConfigParams;
    }

    /**
     * Sets the value of the cpeInitConfigParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link CPEInitConfigParams }
     *     
     */
    public void setCpeInitConfigParams(CPEInitConfigParams value) {
        this.cpeInitConfigParams = value;
    }

    /**
     * Gets the value of the isCPEReachable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCPEReachable() {
        return isCPEReachable;
    }

    /**
     * Sets the value of the isCPEReachable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCPEReachable(Boolean value) {
        this.isCPEReachable = value;
    }

    /**
     * Gets the value of the cevrfName property.
     * 
     * @return
     *     possible object is
     *     {@link VirtualRouteForwardingServiceInstance }
     *     
     */
    public VirtualRouteForwardingServiceInstance getCEVRFName() {
        return cevrfName;
    }

    /**
     * Sets the value of the cevrfName property.
     * 
     * @param value
     *     allowed object is
     *     {@link VirtualRouteForwardingServiceInstance }
     *     
     */
    public void setCEVRFName(VirtualRouteForwardingServiceInstance value) {
        this.cevrfName = value;
    }

}
