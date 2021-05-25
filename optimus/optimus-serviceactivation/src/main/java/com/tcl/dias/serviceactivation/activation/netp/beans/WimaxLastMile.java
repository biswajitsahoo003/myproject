
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WimaxLastMile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WimaxLastMile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BTSIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UniqueName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BSName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SUMACAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PortSpeed" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="SUMgmtIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="SUMgmtSubnet" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="GatewayMgmtIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="HomeRegion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProvisioningMode">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="mvlanUntaggedmode" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_01}MVLANUntaggedMode" minOccurs="0"/>
 *                   &lt;element name="natmode" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}NATMode" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="InstanceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WimaxLastMile", propOrder = {
    "btsip",
    "uniqueName",
    "bsName",
    "sumacAddress",
    "portSpeed",
    "suMgmtIP",
    "suMgmtSubnet",
    "gatewayMgmtIP",
    "homeRegion",
    "description1",
    "description2",
    "serviceType",
    "serviceSubType",
    "provisioningMode",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified",
    "instanceID"
})
public class WimaxLastMile {

    @XmlElement(name = "BTSIP")
    protected String btsip;
    @XmlElement(name = "UniqueName")
    protected String uniqueName;
    @XmlElement(name = "BSName")
    protected String bsName;
    @XmlElement(name = "SUMACAddress")
    protected String sumacAddress;
    @XmlElement(name = "PortSpeed")
    protected Bandwidth portSpeed;
    @XmlElement(name = "SUMgmtIP")
    protected IPV4Address suMgmtIP;
    @XmlElement(name = "SUMgmtSubnet")
    protected IPV4Address suMgmtSubnet;
    @XmlElement(name = "GatewayMgmtIP")
    protected IPV4Address gatewayMgmtIP;
    @XmlElement(name = "HomeRegion")
    protected String homeRegion;
    @XmlElement(name = "Description1")
    protected String description1;
    @XmlElement(name = "Description2")
    protected String description2;
    protected String serviceType;
    protected String serviceSubType;
    @XmlElement(name = "ProvisioningMode", required = true)
    protected WimaxLastMile.ProvisioningMode provisioningMode;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;
    @XmlElement(name = "InstanceID")
    protected String instanceID;

    /**
     * Gets the value of the btsip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBTSIP() {
        return btsip;
    }

    /**
     * Sets the value of the btsip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBTSIP(String value) {
        this.btsip = value;
    }

    /**
     * Gets the value of the uniqueName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueName() {
        return uniqueName;
    }

    /**
     * Sets the value of the uniqueName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueName(String value) {
        this.uniqueName = value;
    }

    /**
     * Gets the value of the bsName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBSName() {
        return bsName;
    }

    /**
     * Sets the value of the bsName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBSName(String value) {
        this.bsName = value;
    }

    /**
     * Gets the value of the sumacAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUMACAddress() {
        return sumacAddress;
    }

    /**
     * Sets the value of the sumacAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUMACAddress(String value) {
        this.sumacAddress = value;
    }

    /**
     * Gets the value of the portSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getPortSpeed() {
        return portSpeed;
    }

    /**
     * Sets the value of the portSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setPortSpeed(Bandwidth value) {
        this.portSpeed = value;
    }

    /**
     * Gets the value of the suMgmtIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getSUMgmtIP() {
        return suMgmtIP;
    }

    /**
     * Sets the value of the suMgmtIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setSUMgmtIP(IPV4Address value) {
        this.suMgmtIP = value;
    }

    /**
     * Gets the value of the suMgmtSubnet property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getSUMgmtSubnet() {
        return suMgmtSubnet;
    }

    /**
     * Sets the value of the suMgmtSubnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setSUMgmtSubnet(IPV4Address value) {
        this.suMgmtSubnet = value;
    }

    /**
     * Gets the value of the gatewayMgmtIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getGatewayMgmtIP() {
        return gatewayMgmtIP;
    }

    /**
     * Sets the value of the gatewayMgmtIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setGatewayMgmtIP(IPV4Address value) {
        this.gatewayMgmtIP = value;
    }

    /**
     * Gets the value of the homeRegion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeRegion() {
        return homeRegion;
    }

    /**
     * Sets the value of the homeRegion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeRegion(String value) {
        this.homeRegion = value;
    }

    /**
     * Gets the value of the description1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription1() {
        return description1;
    }

    /**
     * Sets the value of the description1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription1(String value) {
        this.description1 = value;
    }

    /**
     * Gets the value of the description2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription2() {
        return description2;
    }

    /**
     * Sets the value of the description2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription2(String value) {
        this.description2 = value;
    }

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
     * Gets the value of the provisioningMode property.
     * 
     * @return
     *     possible object is
     *     {@link WimaxLastMile.ProvisioningMode }
     *     
     */
    public WimaxLastMile.ProvisioningMode getProvisioningMode() {
        return provisioningMode;
    }

    /**
     * Sets the value of the provisioningMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link WimaxLastMile.ProvisioningMode }
     *     
     */
    public void setProvisioningMode(WimaxLastMile.ProvisioningMode value) {
        this.provisioningMode = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="mvlanUntaggedmode" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_01}MVLANUntaggedMode" minOccurs="0"/>
     *         &lt;element name="natmode" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}NATMode" minOccurs="0"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "mvlanUntaggedmode",
        "natmode"
    })
    public static class ProvisioningMode {

        protected MVLANUntaggedMode mvlanUntaggedmode;
        protected NATMode natmode;

        /**
         * Gets the value of the mvlanUntaggedmode property.
         * 
         * @return
         *     possible object is
         *     {@link MVLANUntaggedMode }
         *     
         */
        public MVLANUntaggedMode getMvlanUntaggedmode() {
            return mvlanUntaggedmode;
        }

        /**
         * Sets the value of the mvlanUntaggedmode property.
         * 
         * @param value
         *     allowed object is
         *     {@link MVLANUntaggedMode }
         *     
         */
        public void setMvlanUntaggedmode(MVLANUntaggedMode value) {
            this.mvlanUntaggedmode = value;
        }

        /**
         * Gets the value of the natmode property.
         * 
         * @return
         *     possible object is
         *     {@link NATMode }
         *     
         */
        public NATMode getNatmode() {
            return natmode;
        }

        /**
         * Sets the value of the natmode property.
         * 
         * @param value
         *     allowed object is
         *     {@link NATMode }
         *     
         */
        public void setNatmode(NATMode value) {
            this.natmode = value;
        }

    }

}
