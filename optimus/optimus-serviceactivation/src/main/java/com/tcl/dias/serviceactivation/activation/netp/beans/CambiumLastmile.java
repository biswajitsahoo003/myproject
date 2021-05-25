
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CambiumLastmile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CambiumLastmile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BTSIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BSName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SUMACAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PortSpeed" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="SUMgmtIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="SUMgmtSubnet" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="GatewayMgmtIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="serviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="InstanceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="colourCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="installationColourCode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLE"/>
 *               &lt;enumeration value="DISABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RFScanList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enableLargeDataVCQ" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLE"/>
 *               &lt;enumeration value="DISABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="transmitterOutputPower" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="SNMPParameters" type="{http://www.tcl.com/2014/2/ipsvc/xsd}SNMPParameters" minOccurs="0"/>
 *         &lt;element name="viewableToGuestUsers" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLE"/>
 *               &lt;enumeration value="DISABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cambiumSiteDetails" type="{http://www.tcl.com/2014/2/ipsvc/xsd}CambiumSite" minOccurs="0"/>
 *         &lt;element name="linkSpeed" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="FORCED100F"/>
 *               &lt;enumeration value="FORCED10F"/>
 *               &lt;enumeration value="FORCED10H"/>
 *               &lt;enumeration value="FORCED100H"/>
 *               &lt;enumeration value="FORCED1000F"/>
 *               &lt;enumeration value="AUTO10F10H"/>
 *               &lt;enumeration value="AUTO100H10H"/>
 *               &lt;enumeration value="AUTO100H10F10H"/>
 *               &lt;enumeration value="AUTO100F100H"/>
 *               &lt;enumeration value="AUTO100F100H10H"/>
 *               &lt;enumeration value="AUTO100F100H10F10H"/>
 *               &lt;enumeration value="AUTO1000F100F100H10F10H"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ethernetLink" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLE"/>
 *               &lt;enumeration value="DISABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="regionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="webpageAutoUpdate" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="bridgeEntryTimeout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dynamicRateAdapt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="multicastDestAddress" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="MULTICAST"/>
 *               &lt;enumeration value="BROADCAST"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deviceDefaultReset" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="powerupMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="frameTimingPulseGated" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLE"/>
 *               &lt;enumeration value="DISABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cambiumAntennaParameters" type="{http://www.tcl.com/2014/2/ipsvc/xsd}CambiumAntennaParameters" minOccurs="0"/>
 *         &lt;element name="cambiumVLANAttributes" type="{http://www.tcl.com/2014/2/ipsvc/xsd}CambiumVLANAttributes" minOccurs="0"/>
 *         &lt;element name="field11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cambiumAAAConfig" type="{http://www.tcl.com/2014/2/ipsvc/xsd}CambiumAAAConfig" minOccurs="0"/>
 *         &lt;element name="APIVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DHCPState" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLED"/>
 *               &lt;enumeration value="DISABLED"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="DeviceType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Cambium"/>
 *               &lt;enumeration value="Cambium450i"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CambiumLastmile", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "btsip",
    "bsName",
    "sumacAddress",
    "portSpeed",
    "suMgmtIP",
    "suMgmtSubnet",
    "gatewayMgmtIP",
    "serviceType",
    "serviceSubType",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified",
    "instanceID",
    "colourCode",
    "installationColourCode",
    "rfScanList",
    "enableLargeDataVCQ",
    "transmitterOutputPower",
    "snmpParameters",
    "viewableToGuestUsers",
    "cambiumSiteDetails",
    "linkSpeed",
    "ethernetLink",
    "regionCode",
    "webpageAutoUpdate",
    "bridgeEntryTimeout",
    "dynamicRateAdapt",
    "multicastDestAddress",
    "deviceDefaultReset",
    "powerupMode",
    "frameTimingPulseGated",
    "cambiumAntennaParameters",
    "cambiumVLANAttributes",
    "field11",
    "field12",
    "cambiumAAAConfig",
    "apiVersion",
    "dhcpState",
    "deviceType"
})
public class CambiumLastmile {

    @XmlElement(name = "BTSIP")
    protected String btsip;
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
    protected String serviceType;
    protected String serviceSubType;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;
    @XmlElement(name = "InstanceID")
    protected String instanceID;
    protected String colourCode;
    protected String installationColourCode;
    @XmlElement(name = "RFScanList")
    protected String rfScanList;
    protected String enableLargeDataVCQ;
    protected Integer transmitterOutputPower;
    @XmlElement(name = "SNMPParameters")
    protected SNMPParameters snmpParameters;
    protected String viewableToGuestUsers;
    protected CambiumSite cambiumSiteDetails;
    protected String linkSpeed;
    protected String ethernetLink;
    protected String regionCode;
    protected Integer webpageAutoUpdate;
    protected String bridgeEntryTimeout;
    protected String dynamicRateAdapt;
    protected String multicastDestAddress;
    protected String deviceDefaultReset;
    protected String powerupMode;
    protected String frameTimingPulseGated;
    protected CambiumAntennaParameters cambiumAntennaParameters;
    protected CambiumVLANAttributes cambiumVLANAttributes;
    protected String field11;
    protected String field12;
    protected CambiumAAAConfig cambiumAAAConfig;
    @XmlElement(name = "APIVersion")
    protected String apiVersion;
    @XmlElement(name = "DHCPState")
    protected String dhcpState;
    @XmlElement(name = "DeviceType")
    protected String deviceType;

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
     * Gets the value of the colourCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColourCode() {
        return colourCode;
    }

    /**
     * Sets the value of the colourCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColourCode(String value) {
        this.colourCode = value;
    }

    /**
     * Gets the value of the installationColourCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstallationColourCode() {
        return installationColourCode;
    }

    /**
     * Sets the value of the installationColourCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstallationColourCode(String value) {
        this.installationColourCode = value;
    }

    /**
     * Gets the value of the rfScanList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRFScanList() {
        return rfScanList;
    }

    /**
     * Sets the value of the rfScanList property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRFScanList(String value) {
        this.rfScanList = value;
    }

    /**
     * Gets the value of the enableLargeDataVCQ property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableLargeDataVCQ() {
        return enableLargeDataVCQ;
    }

    /**
     * Sets the value of the enableLargeDataVCQ property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableLargeDataVCQ(String value) {
        this.enableLargeDataVCQ = value;
    }

    /**
     * Gets the value of the transmitterOutputPower property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTransmitterOutputPower() {
        return transmitterOutputPower;
    }

    /**
     * Sets the value of the transmitterOutputPower property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTransmitterOutputPower(Integer value) {
        this.transmitterOutputPower = value;
    }

    /**
     * Gets the value of the snmpParameters property.
     * 
     * @return
     *     possible object is
     *     {@link SNMPParameters }
     *     
     */
    public SNMPParameters getSNMPParameters() {
        return snmpParameters;
    }

    /**
     * Sets the value of the snmpParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link SNMPParameters }
     *     
     */
    public void setSNMPParameters(SNMPParameters value) {
        this.snmpParameters = value;
    }

    /**
     * Gets the value of the viewableToGuestUsers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getViewableToGuestUsers() {
        return viewableToGuestUsers;
    }

    /**
     * Sets the value of the viewableToGuestUsers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setViewableToGuestUsers(String value) {
        this.viewableToGuestUsers = value;
    }

    /**
     * Gets the value of the cambiumSiteDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CambiumSite }
     *     
     */
    public CambiumSite getCambiumSiteDetails() {
        return cambiumSiteDetails;
    }

    /**
     * Sets the value of the cambiumSiteDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CambiumSite }
     *     
     */
    public void setCambiumSiteDetails(CambiumSite value) {
        this.cambiumSiteDetails = value;
    }

    /**
     * Gets the value of the linkSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkSpeed() {
        return linkSpeed;
    }

    /**
     * Sets the value of the linkSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkSpeed(String value) {
        this.linkSpeed = value;
    }

    /**
     * Gets the value of the ethernetLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEthernetLink() {
        return ethernetLink;
    }

    /**
     * Sets the value of the ethernetLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEthernetLink(String value) {
        this.ethernetLink = value;
    }

    /**
     * Gets the value of the regionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * Sets the value of the regionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionCode(String value) {
        this.regionCode = value;
    }

    /**
     * Gets the value of the webpageAutoUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWebpageAutoUpdate() {
        return webpageAutoUpdate;
    }

    /**
     * Sets the value of the webpageAutoUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWebpageAutoUpdate(Integer value) {
        this.webpageAutoUpdate = value;
    }

    /**
     * Gets the value of the bridgeEntryTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBridgeEntryTimeout() {
        return bridgeEntryTimeout;
    }

    /**
     * Sets the value of the bridgeEntryTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBridgeEntryTimeout(String value) {
        this.bridgeEntryTimeout = value;
    }

    /**
     * Gets the value of the dynamicRateAdapt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicRateAdapt() {
        return dynamicRateAdapt;
    }

    /**
     * Sets the value of the dynamicRateAdapt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicRateAdapt(String value) {
        this.dynamicRateAdapt = value;
    }

    /**
     * Gets the value of the multicastDestAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMulticastDestAddress() {
        return multicastDestAddress;
    }

    /**
     * Sets the value of the multicastDestAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMulticastDestAddress(String value) {
        this.multicastDestAddress = value;
    }

    /**
     * Gets the value of the deviceDefaultReset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceDefaultReset() {
        return deviceDefaultReset;
    }

    /**
     * Sets the value of the deviceDefaultReset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceDefaultReset(String value) {
        this.deviceDefaultReset = value;
    }

    /**
     * Gets the value of the powerupMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowerupMode() {
        return powerupMode;
    }

    /**
     * Sets the value of the powerupMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowerupMode(String value) {
        this.powerupMode = value;
    }

    /**
     * Gets the value of the frameTimingPulseGated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrameTimingPulseGated() {
        return frameTimingPulseGated;
    }

    /**
     * Sets the value of the frameTimingPulseGated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrameTimingPulseGated(String value) {
        this.frameTimingPulseGated = value;
    }

    /**
     * Gets the value of the cambiumAntennaParameters property.
     * 
     * @return
     *     possible object is
     *     {@link CambiumAntennaParameters }
     *     
     */
    public CambiumAntennaParameters getCambiumAntennaParameters() {
        return cambiumAntennaParameters;
    }

    /**
     * Sets the value of the cambiumAntennaParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link CambiumAntennaParameters }
     *     
     */
    public void setCambiumAntennaParameters(CambiumAntennaParameters value) {
        this.cambiumAntennaParameters = value;
    }

    /**
     * Gets the value of the cambiumVLANAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link CambiumVLANAttributes }
     *     
     */
    public CambiumVLANAttributes getCambiumVLANAttributes() {
        return cambiumVLANAttributes;
    }

    /**
     * Sets the value of the cambiumVLANAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link CambiumVLANAttributes }
     *     
     */
    public void setCambiumVLANAttributes(CambiumVLANAttributes value) {
        this.cambiumVLANAttributes = value;
    }

    /**
     * Gets the value of the field11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField11() {
        return field11;
    }

    /**
     * Sets the value of the field11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField11(String value) {
        this.field11 = value;
    }

    /**
     * Gets the value of the field12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField12() {
        return field12;
    }

    /**
     * Sets the value of the field12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField12(String value) {
        this.field12 = value;
    }

    /**
     * Gets the value of the cambiumAAAConfig property.
     * 
     * @return
     *     possible object is
     *     {@link CambiumAAAConfig }
     *     
     */
    public CambiumAAAConfig getCambiumAAAConfig() {
        return cambiumAAAConfig;
    }

    /**
     * Sets the value of the cambiumAAAConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link CambiumAAAConfig }
     *     
     */
    public void setCambiumAAAConfig(CambiumAAAConfig value) {
        this.cambiumAAAConfig = value;
    }

    /**
     * Gets the value of the apiVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPIVersion() {
        return apiVersion;
    }

    /**
     * Sets the value of the apiVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPIVersion(String value) {
        this.apiVersion = value;
    }

    /**
     * Gets the value of the dhcpState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDHCPState() {
        return dhcpState;
    }

    /**
     * Sets the value of the dhcpState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDHCPState(String value) {
        this.dhcpState = value;
    }

    /**
     * Gets the value of the deviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Sets the value of the deviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceType(String value) {
        this.deviceType = value;
    }

}
