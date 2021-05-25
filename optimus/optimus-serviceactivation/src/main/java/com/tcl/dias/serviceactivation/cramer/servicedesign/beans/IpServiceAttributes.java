
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ipServiceAttributes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ipServiceAttributes"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="additional_lan_v6_pool_size" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsBFDEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Base_IP_address_arrangement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Base_PathType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BaseRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BaseRateUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BaseRate_NonStd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BTSIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BurstRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BurstRateUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BurstRate_NonStd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CustomerRequirement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CustomerType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CustomerWANIPSpecified" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="default_lan_v4_pool_size" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="default_lan_v6_pool_size" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="isExtendedLAN" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="IsExtendedLanChanged" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="IP_address_arrangement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsIpPathTypeChanged" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="IpPoolTypeRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Is_addtional_lan_v6_rqrd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="isLANIPCustProvided" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="LANIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LAN_provided_by_Customer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LastMileInterface" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LINKTYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsLMTypeChanged" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Master_VRF_ServiceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsMultiCastIPRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="IsMultiCosRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Multi_VRF_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Multi_VRF_Solution" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Multilink" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Multilink_Loadbalancing_ServiceIDs" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="NatNMSPoolRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NetworkCompType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NNIID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NNIType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="No_Of_VRFs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsNonRoutablePoolRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Nos_LAN_IP_Required" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="number_of_addtnl_lan_v6_pool" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NumberOfIpAddresses" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OldServiceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PathType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PortBandwidth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PortBandwidthUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PortBandwidth_NonStd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Pri_Sec_Mapping" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ProcessId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="Provider" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Role" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsRoutableToNonRoutable" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="RoutingProtocol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SAPID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SectorID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ServiceCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ServiceLink" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ServiceOption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsServiceOptionChanged" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="ServiceOrder" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SharedCPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SharedCPERequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="SharedLM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SharedLMRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="TclPopCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Total_VRF_Bandwidth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TYPE_OF_PEPLINK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UniquePopId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VLAN_LM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IsVRFLiteRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="ASNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ipServiceAttributes", propOrder = {
    "additionalLanV6PoolSize",
    "isBFDEnabled",
    "baseIPAddressArrangement",
    "basePathType",
    "baseRate",
    "baseRateUnit",
    "baseRateNonStd",
    "btsip",
    "burstRate",
    "burstRateUnit",
    "burstRateNonStd",
    "cuid",
    "customerRequirement",
    "customerType",
    "customerWANIPSpecified",
    "defaultLanV4PoolSize",
    "defaultLanV6PoolSize",
    "isExtendedLAN",
    "isExtendedLanChanged",
    "ipAddressArrangement",
    "isIpPathTypeChanged",
    "ipPoolTypeRequired",
    "isAddtionalLanV6Rqrd",
    "isLANIPCustProvided",
    "lanip",
    "lanProvidedByCustomer",
    "lastMileInterface",
    "linktype",
    "isLMTypeChanged",
    "masterVRFServiceId",
    "isMultiCastIPRequired",
    "isMultiCosRequired",
    "multiVRFFlag",
    "multiVRFSolution",
    "multilink",
    "multilinkLoadbalancingServiceIDs",
    "natNMSPoolRequired",
    "networkCompType",
    "nniid",
    "nniType",
    "noOfVRFs",
    "isNonRoutablePoolRequired",
    "nosLANIPRequired",
    "numberOfAddtnlLanV6Pool",
    "numberOfIpAddresses",
    "oldServiceId",
    "pathType",
    "portBandwidth",
    "portBandwidthUnit",
    "portBandwidthNonStd",
    "priSecMapping",
    "processId",
    "provider",
    "role",
    "isRoutableToNonRoutable",
    "routingProtocol",
    "sapid",
    "sectorID",
    "serviceCategory",
    "serviceLink",
    "serviceOption",
    "isServiceOptionChanged",
    "serviceOrder",
    "sharedCPE",
    "sharedCPERequired",
    "sharedLM",
    "sharedLMRequired",
    "tclPopCity",
    "totalVRFBandwidth",
    "typeofpeplink",
    "uniquePopId",
    "vlanlm",
    "isVRFLiteRequired",
    "asNumber"
})
public class IpServiceAttributes {

    @XmlElement(name = "additional_lan_v6_pool_size")
    protected String additionalLanV6PoolSize;
    @XmlElement(name = "IsBFDEnabled")
    protected boolean isBFDEnabled;
    @XmlElement(name = "Base_IP_address_arrangement")
    protected String baseIPAddressArrangement;
    @XmlElement(name = "Base_PathType")
    protected String basePathType;
    @XmlElement(name = "BaseRate")
    protected String baseRate;
    @XmlElement(name = "BaseRateUnit")
    protected String baseRateUnit;
    @XmlElement(name = "BaseRate_NonStd")
    protected String baseRateNonStd;
    @XmlElement(name = "BTSIP")
    protected String btsip;
    @XmlElement(name = "BurstRate")
    protected String burstRate;
    @XmlElement(name = "BurstRateUnit")
    protected String burstRateUnit;
    @XmlElement(name = "BurstRate_NonStd")
    protected String burstRateNonStd;
    @XmlElement(name = "CUID")
    protected String cuid;
    @XmlElement(name = "CustomerRequirement")
    protected String customerRequirement;
    @XmlElement(name = "CustomerType")
    protected String customerType;
    @XmlElement(name = "CustomerWANIPSpecified")
    protected boolean customerWANIPSpecified;
    @XmlElement(name = "default_lan_v4_pool_size")
    protected String defaultLanV4PoolSize;
    @XmlElement(name = "default_lan_v6_pool_size")
    protected String defaultLanV6PoolSize;
    protected boolean isExtendedLAN;
    @XmlElement(name = "IsExtendedLanChanged")
    protected boolean isExtendedLanChanged;
    @XmlElement(name = "IP_address_arrangement")
    protected String ipAddressArrangement;
    @XmlElement(name = "IsIpPathTypeChanged")
    protected boolean isIpPathTypeChanged;
    @XmlElement(name = "IpPoolTypeRequired")
    protected String ipPoolTypeRequired;
    @XmlElement(name = "Is_addtional_lan_v6_rqrd")
    protected String isAddtionalLanV6Rqrd;
    protected boolean isLANIPCustProvided;
    @XmlElement(name = "LANIP")
    protected String lanip;
    @XmlElement(name = "LAN_provided_by_Customer")
    protected String lanProvidedByCustomer;
    @XmlElement(name = "LastMileInterface")
    protected String lastMileInterface;
    @XmlElement(name = "LINKTYPE")
    protected String linktype;
    @XmlElement(name = "IsLMTypeChanged")
    protected boolean isLMTypeChanged;
    @XmlElement(name = "Master_VRF_ServiceId")
    protected String masterVRFServiceId;
    @XmlElement(name = "IsMultiCastIPRequired")
    protected boolean isMultiCastIPRequired;
    @XmlElement(name = "IsMultiCosRequired")
    protected boolean isMultiCosRequired;
    @XmlElement(name = "Multi_VRF_Flag")
    protected boolean multiVRFFlag;
    @XmlElement(name = "Multi_VRF_Solution")
    protected boolean multiVRFSolution;
    @XmlElement(name = "Multilink")
    protected String multilink;
    @XmlElement(name = "Multilink_Loadbalancing_ServiceIDs")
    protected List<String> multilinkLoadbalancingServiceIDs;
    @XmlElement(name = "NatNMSPoolRequired")
    protected String natNMSPoolRequired;
    @XmlElement(name = "NetworkCompType")
    protected String networkCompType;
    @XmlElement(name = "NNIID")
    protected String nniid;
    @XmlElement(name = "NNIType")
    protected String nniType;
    @XmlElement(name = "No_Of_VRFs")
    protected String noOfVRFs;
    @XmlElement(name = "IsNonRoutablePoolRequired")
    protected boolean isNonRoutablePoolRequired;
    @XmlElement(name = "Nos_LAN_IP_Required")
    protected String nosLANIPRequired;
    @XmlElement(name = "number_of_addtnl_lan_v6_pool")
    protected String numberOfAddtnlLanV6Pool;
    @XmlElement(name = "NumberOfIpAddresses")
    protected String numberOfIpAddresses;
    @XmlElement(name = "OldServiceId")
    protected String oldServiceId;
    @XmlElement(name = "PathType")
    protected String pathType;
    @XmlElement(name = "PortBandwidth")
    protected String portBandwidth;
    @XmlElement(name = "PortBandwidthUnit")
    protected String portBandwidthUnit;
    @XmlElement(name = "PortBandwidth_NonStd")
    protected String portBandwidthNonStd;
    @XmlElement(name = "Pri_Sec_Mapping")
    protected String priSecMapping;
    @XmlElement(name = "ProcessId")
    protected Long processId;
    @XmlElement(name = "Provider")
    protected String provider;
    @XmlElement(name = "Role")
    protected String role;
    @XmlElement(name = "IsRoutableToNonRoutable")
    protected boolean isRoutableToNonRoutable;
    @XmlElement(name = "RoutingProtocol")
    protected String routingProtocol;
    @XmlElement(name = "SAPID")
    protected String sapid;
    @XmlElement(name = "SectorID")
    protected String sectorID;
    @XmlElement(name = "ServiceCategory")
    protected String serviceCategory;
    @XmlElement(name = "ServiceLink")
    protected String serviceLink;
    @XmlElement(name = "ServiceOption")
    protected String serviceOption;
    @XmlElement(name = "IsServiceOptionChanged")
    protected boolean isServiceOptionChanged;
    @XmlElement(name = "ServiceOrder")
    protected String serviceOrder;
    @XmlElement(name = "SharedCPE")
    protected String sharedCPE;
    @XmlElement(name = "SharedCPERequired")
    protected boolean sharedCPERequired;
    @XmlElement(name = "SharedLM")
    protected String sharedLM;
    @XmlElement(name = "SharedLMRequired")
    protected boolean sharedLMRequired;
    @XmlElement(name = "TclPopCity")
    protected String tclPopCity;
    @XmlElement(name = "Total_VRF_Bandwidth")
    protected String totalVRFBandwidth;
    @XmlElement(name = "TYPE_OF_PEPLINK")
    protected String typeofpeplink;
    @XmlElement(name = "UniquePopId")
    protected String uniquePopId;
    @XmlElement(name = "VLAN_LM")
    protected String vlanlm;
    @XmlElement(name = "IsVRFLiteRequired")
    protected boolean isVRFLiteRequired;
    @XmlElement(name = "ASNumber")
    protected String asNumber;

    /**
     * Gets the value of the additionalLanV6PoolSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalLanV6PoolSize() {
        return additionalLanV6PoolSize;
    }

    /**
     * Sets the value of the additionalLanV6PoolSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalLanV6PoolSize(String value) {
        this.additionalLanV6PoolSize = value;
    }

    /**
     * Gets the value of the isBFDEnabled property.
     * 
     */
    public boolean isIsBFDEnabled() {
        return isBFDEnabled;
    }

    /**
     * Sets the value of the isBFDEnabled property.
     * 
     */
    public void setIsBFDEnabled(boolean value) {
        this.isBFDEnabled = value;
    }

    /**
     * Gets the value of the baseIPAddressArrangement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseIPAddressArrangement() {
        return baseIPAddressArrangement;
    }

    /**
     * Sets the value of the baseIPAddressArrangement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseIPAddressArrangement(String value) {
        this.baseIPAddressArrangement = value;
    }

    /**
     * Gets the value of the basePathType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBasePathType() {
        return basePathType;
    }

    /**
     * Sets the value of the basePathType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBasePathType(String value) {
        this.basePathType = value;
    }

    /**
     * Gets the value of the baseRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseRate() {
        return baseRate;
    }

    /**
     * Sets the value of the baseRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseRate(String value) {
        this.baseRate = value;
    }

    /**
     * Gets the value of the baseRateUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseRateUnit() {
        return baseRateUnit;
    }

    /**
     * Sets the value of the baseRateUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseRateUnit(String value) {
        this.baseRateUnit = value;
    }

    /**
     * Gets the value of the baseRateNonStd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseRateNonStd() {
        return baseRateNonStd;
    }

    /**
     * Sets the value of the baseRateNonStd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseRateNonStd(String value) {
        this.baseRateNonStd = value;
    }

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
     * Gets the value of the burstRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBurstRate() {
        return burstRate;
    }

    /**
     * Sets the value of the burstRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBurstRate(String value) {
        this.burstRate = value;
    }

    /**
     * Gets the value of the burstRateUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBurstRateUnit() {
        return burstRateUnit;
    }

    /**
     * Sets the value of the burstRateUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBurstRateUnit(String value) {
        this.burstRateUnit = value;
    }

    /**
     * Gets the value of the burstRateNonStd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBurstRateNonStd() {
        return burstRateNonStd;
    }

    /**
     * Sets the value of the burstRateNonStd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBurstRateNonStd(String value) {
        this.burstRateNonStd = value;
    }

    /**
     * Gets the value of the cuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUID() {
        return cuid;
    }

    /**
     * Sets the value of the cuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUID(String value) {
        this.cuid = value;
    }

    /**
     * Gets the value of the customerRequirement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerRequirement() {
        return customerRequirement;
    }

    /**
     * Sets the value of the customerRequirement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerRequirement(String value) {
        this.customerRequirement = value;
    }

    /**
     * Gets the value of the customerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the value of the customerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerType(String value) {
        this.customerType = value;
    }

    /**
     * Gets the value of the customerWANIPSpecified property.
     * 
     */
    public boolean isCustomerWANIPSpecified() {
        return customerWANIPSpecified;
    }

    /**
     * Sets the value of the customerWANIPSpecified property.
     * 
     */
    public void setCustomerWANIPSpecified(boolean value) {
        this.customerWANIPSpecified = value;
    }

    /**
     * Gets the value of the defaultLanV4PoolSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultLanV4PoolSize() {
        return defaultLanV4PoolSize;
    }

    /**
     * Sets the value of the defaultLanV4PoolSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultLanV4PoolSize(String value) {
        this.defaultLanV4PoolSize = value;
    }

    /**
     * Gets the value of the defaultLanV6PoolSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultLanV6PoolSize() {
        return defaultLanV6PoolSize;
    }

    /**
     * Sets the value of the defaultLanV6PoolSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultLanV6PoolSize(String value) {
        this.defaultLanV6PoolSize = value;
    }

    /**
     * Gets the value of the isExtendedLAN property.
     * 
     */
    public boolean isIsExtendedLAN() {
        return isExtendedLAN;
    }

    /**
     * Sets the value of the isExtendedLAN property.
     * 
     */
    public void setIsExtendedLAN(boolean value) {
        this.isExtendedLAN = value;
    }

    /**
     * Gets the value of the isExtendedLanChanged property.
     * 
     */
    public boolean isIsExtendedLanChanged() {
        return isExtendedLanChanged;
    }

    /**
     * Sets the value of the isExtendedLanChanged property.
     * 
     */
    public void setIsExtendedLanChanged(boolean value) {
        this.isExtendedLanChanged = value;
    }

    /**
     * Gets the value of the ipAddressArrangement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPAddressArrangement() {
        return ipAddressArrangement;
    }

    /**
     * Sets the value of the ipAddressArrangement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPAddressArrangement(String value) {
        this.ipAddressArrangement = value;
    }

    /**
     * Gets the value of the isIpPathTypeChanged property.
     * 
     */
    public boolean isIsIpPathTypeChanged() {
        return isIpPathTypeChanged;
    }

    /**
     * Sets the value of the isIpPathTypeChanged property.
     * 
     */
    public void setIsIpPathTypeChanged(boolean value) {
        this.isIpPathTypeChanged = value;
    }

    /**
     * Gets the value of the ipPoolTypeRequired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpPoolTypeRequired() {
        return ipPoolTypeRequired;
    }

    /**
     * Sets the value of the ipPoolTypeRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpPoolTypeRequired(String value) {
        this.ipPoolTypeRequired = value;
    }

    /**
     * Gets the value of the isAddtionalLanV6Rqrd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsAddtionalLanV6Rqrd() {
        return isAddtionalLanV6Rqrd;
    }

    /**
     * Sets the value of the isAddtionalLanV6Rqrd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsAddtionalLanV6Rqrd(String value) {
        this.isAddtionalLanV6Rqrd = value;
    }

    /**
     * Gets the value of the isLANIPCustProvided property.
     * 
     */
    public boolean isIsLANIPCustProvided() {
        return isLANIPCustProvided;
    }

    /**
     * Sets the value of the isLANIPCustProvided property.
     * 
     */
    public void setIsLANIPCustProvided(boolean value) {
        this.isLANIPCustProvided = value;
    }

    /**
     * Gets the value of the lanip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLANIP() {
        return lanip;
    }

    /**
     * Sets the value of the lanip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLANIP(String value) {
        this.lanip = value;
    }

    /**
     * Gets the value of the lanProvidedByCustomer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLANProvidedByCustomer() {
        return lanProvidedByCustomer;
    }

    /**
     * Sets the value of the lanProvidedByCustomer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLANProvidedByCustomer(String value) {
        this.lanProvidedByCustomer = value;
    }

    /**
     * Gets the value of the lastMileInterface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastMileInterface() {
        return lastMileInterface;
    }

    /**
     * Sets the value of the lastMileInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastMileInterface(String value) {
        this.lastMileInterface = value;
    }

    /**
     * Gets the value of the linktype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLINKTYPE() {
        return linktype;
    }

    /**
     * Sets the value of the linktype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLINKTYPE(String value) {
        this.linktype = value;
    }

    /**
     * Gets the value of the isLMTypeChanged property.
     * 
     */
    public boolean isIsLMTypeChanged() {
        return isLMTypeChanged;
    }

    /**
     * Sets the value of the isLMTypeChanged property.
     * 
     */
    public void setIsLMTypeChanged(boolean value) {
        this.isLMTypeChanged = value;
    }

    /**
     * Gets the value of the masterVRFServiceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMasterVRFServiceId() {
        return masterVRFServiceId;
    }

    /**
     * Sets the value of the masterVRFServiceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMasterVRFServiceId(String value) {
        this.masterVRFServiceId = value;
    }

    /**
     * Gets the value of the isMultiCastIPRequired property.
     * 
     */
    public boolean isIsMultiCastIPRequired() {
        return isMultiCastIPRequired;
    }

    /**
     * Sets the value of the isMultiCastIPRequired property.
     * 
     */
    public void setIsMultiCastIPRequired(boolean value) {
        this.isMultiCastIPRequired = value;
    }

    /**
     * Gets the value of the isMultiCosRequired property.
     * 
     */
    public boolean isIsMultiCosRequired() {
        return isMultiCosRequired;
    }

    /**
     * Sets the value of the isMultiCosRequired property.
     * 
     */
    public void setIsMultiCosRequired(boolean value) {
        this.isMultiCosRequired = value;
    }

    /**
     * Gets the value of the multiVRFFlag property.
     * 
     */
    public boolean isMultiVRFFlag() {
        return multiVRFFlag;
    }

    /**
     * Sets the value of the multiVRFFlag property.
     * 
     */
    public void setMultiVRFFlag(boolean value) {
        this.multiVRFFlag = value;
    }

    /**
     * Gets the value of the multiVRFSolution property.
     * 
     */
    public boolean isMultiVRFSolution() {
        return multiVRFSolution;
    }

    /**
     * Sets the value of the multiVRFSolution property.
     * 
     */
    public void setMultiVRFSolution(boolean value) {
        this.multiVRFSolution = value;
    }

    /**
     * Gets the value of the multilink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultilink() {
        return multilink;
    }

    /**
     * Sets the value of the multilink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultilink(String value) {
        this.multilink = value;
    }

    /**
     * Gets the value of the multilinkLoadbalancingServiceIDs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the multilinkLoadbalancingServiceIDs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMultilinkLoadbalancingServiceIDs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMultilinkLoadbalancingServiceIDs() {
        if (multilinkLoadbalancingServiceIDs == null) {
            multilinkLoadbalancingServiceIDs = new ArrayList<String>();
        }
        return this.multilinkLoadbalancingServiceIDs;
    }

    /**
     * Gets the value of the natNMSPoolRequired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNatNMSPoolRequired() {
        return natNMSPoolRequired;
    }

    /**
     * Sets the value of the natNMSPoolRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNatNMSPoolRequired(String value) {
        this.natNMSPoolRequired = value;
    }

    /**
     * Gets the value of the networkCompType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkCompType() {
        return networkCompType;
    }

    /**
     * Sets the value of the networkCompType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkCompType(String value) {
        this.networkCompType = value;
    }

    /**
     * Gets the value of the nniid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNNIID() {
        return nniid;
    }

    /**
     * Sets the value of the nniid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNNIID(String value) {
        this.nniid = value;
    }

    /**
     * Gets the value of the nniType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNNIType() {
        return nniType;
    }

    /**
     * Sets the value of the nniType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNNIType(String value) {
        this.nniType = value;
    }

    /**
     * Gets the value of the noOfVRFs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoOfVRFs() {
        return noOfVRFs;
    }

    /**
     * Sets the value of the noOfVRFs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoOfVRFs(String value) {
        this.noOfVRFs = value;
    }

    /**
     * Gets the value of the isNonRoutablePoolRequired property.
     * 
     */
    public boolean isIsNonRoutablePoolRequired() {
        return isNonRoutablePoolRequired;
    }

    /**
     * Sets the value of the isNonRoutablePoolRequired property.
     * 
     */
    public void setIsNonRoutablePoolRequired(boolean value) {
        this.isNonRoutablePoolRequired = value;
    }

    /**
     * Gets the value of the nosLANIPRequired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNosLANIPRequired() {
        return nosLANIPRequired;
    }

    /**
     * Sets the value of the nosLANIPRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNosLANIPRequired(String value) {
        this.nosLANIPRequired = value;
    }

    /**
     * Gets the value of the numberOfAddtnlLanV6Pool property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfAddtnlLanV6Pool() {
        return numberOfAddtnlLanV6Pool;
    }

    /**
     * Sets the value of the numberOfAddtnlLanV6Pool property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfAddtnlLanV6Pool(String value) {
        this.numberOfAddtnlLanV6Pool = value;
    }

    /**
     * Gets the value of the numberOfIpAddresses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfIpAddresses() {
        return numberOfIpAddresses;
    }

    /**
     * Sets the value of the numberOfIpAddresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfIpAddresses(String value) {
        this.numberOfIpAddresses = value;
    }

    /**
     * Gets the value of the oldServiceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldServiceId() {
        return oldServiceId;
    }

    /**
     * Sets the value of the oldServiceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldServiceId(String value) {
        this.oldServiceId = value;
    }

    /**
     * Gets the value of the pathType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathType() {
        return pathType;
    }

    /**
     * Sets the value of the pathType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathType(String value) {
        this.pathType = value;
    }

    /**
     * Gets the value of the portBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortBandwidth() {
        return portBandwidth;
    }

    /**
     * Sets the value of the portBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortBandwidth(String value) {
        this.portBandwidth = value;
    }

    /**
     * Gets the value of the portBandwidthUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortBandwidthUnit() {
        return portBandwidthUnit;
    }

    /**
     * Sets the value of the portBandwidthUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortBandwidthUnit(String value) {
        this.portBandwidthUnit = value;
    }

    /**
     * Gets the value of the portBandwidthNonStd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortBandwidthNonStd() {
        return portBandwidthNonStd;
    }

    /**
     * Sets the value of the portBandwidthNonStd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortBandwidthNonStd(String value) {
        this.portBandwidthNonStd = value;
    }

    /**
     * Gets the value of the priSecMapping property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriSecMapping() {
        return priSecMapping;
    }

    /**
     * Sets the value of the priSecMapping property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriSecMapping(String value) {
        this.priSecMapping = value;
    }

    /**
     * Gets the value of the processId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProcessId() {
        return processId;
    }

    /**
     * Sets the value of the processId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProcessId(Long value) {
        this.processId = value;
    }

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
        this.provider = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the isRoutableToNonRoutable property.
     * 
     */
    public boolean isIsRoutableToNonRoutable() {
        return isRoutableToNonRoutable;
    }

    /**
     * Sets the value of the isRoutableToNonRoutable property.
     * 
     */
    public void setIsRoutableToNonRoutable(boolean value) {
        this.isRoutableToNonRoutable = value;
    }

    /**
     * Gets the value of the routingProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoutingProtocol() {
        return routingProtocol;
    }

    /**
     * Sets the value of the routingProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoutingProtocol(String value) {
        this.routingProtocol = value;
    }

    /**
     * Gets the value of the sapid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSAPID() {
        return sapid;
    }

    /**
     * Sets the value of the sapid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSAPID(String value) {
        this.sapid = value;
    }

    /**
     * Gets the value of the sectorID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectorID() {
        return sectorID;
    }

    /**
     * Sets the value of the sectorID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectorID(String value) {
        this.sectorID = value;
    }

    /**
     * Gets the value of the serviceCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceCategory() {
        return serviceCategory;
    }

    /**
     * Sets the value of the serviceCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceCategory(String value) {
        this.serviceCategory = value;
    }

    /**
     * Gets the value of the serviceLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceLink() {
        return serviceLink;
    }

    /**
     * Sets the value of the serviceLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceLink(String value) {
        this.serviceLink = value;
    }

    /**
     * Gets the value of the serviceOption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceOption() {
        return serviceOption;
    }

    /**
     * Sets the value of the serviceOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceOption(String value) {
        this.serviceOption = value;
    }

    /**
     * Gets the value of the isServiceOptionChanged property.
     * 
     */
    public boolean isIsServiceOptionChanged() {
        return isServiceOptionChanged;
    }

    /**
     * Sets the value of the isServiceOptionChanged property.
     * 
     */
    public void setIsServiceOptionChanged(boolean value) {
        this.isServiceOptionChanged = value;
    }

    /**
     * Gets the value of the serviceOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceOrder() {
        return serviceOrder;
    }

    /**
     * Sets the value of the serviceOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceOrder(String value) {
        this.serviceOrder = value;
    }

    /**
     * Gets the value of the sharedCPE property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSharedCPE() {
        return sharedCPE;
    }

    /**
     * Sets the value of the sharedCPE property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSharedCPE(String value) {
        this.sharedCPE = value;
    }

    /**
     * Gets the value of the sharedCPERequired property.
     * 
     */
    public boolean isSharedCPERequired() {
        return sharedCPERequired;
    }

    /**
     * Sets the value of the sharedCPERequired property.
     * 
     */
    public void setSharedCPERequired(boolean value) {
        this.sharedCPERequired = value;
    }

    /**
     * Gets the value of the sharedLM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSharedLM() {
        return sharedLM;
    }

    /**
     * Sets the value of the sharedLM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSharedLM(String value) {
        this.sharedLM = value;
    }

    /**
     * Gets the value of the sharedLMRequired property.
     * 
     */
    public boolean isSharedLMRequired() {
        return sharedLMRequired;
    }

    /**
     * Sets the value of the sharedLMRequired property.
     * 
     */
    public void setSharedLMRequired(boolean value) {
        this.sharedLMRequired = value;
    }

    /**
     * Gets the value of the tclPopCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTclPopCity() {
        return tclPopCity;
    }

    /**
     * Sets the value of the tclPopCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTclPopCity(String value) {
        this.tclPopCity = value;
    }

    /**
     * Gets the value of the totalVRFBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalVRFBandwidth() {
        return totalVRFBandwidth;
    }

    /**
     * Sets the value of the totalVRFBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalVRFBandwidth(String value) {
        this.totalVRFBandwidth = value;
    }

    /**
     * Gets the value of the typeofpeplink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTYPEOFPEPLINK() {
        return typeofpeplink;
    }

    /**
     * Sets the value of the typeofpeplink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTYPEOFPEPLINK(String value) {
        this.typeofpeplink = value;
    }

    /**
     * Gets the value of the uniquePopId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniquePopId() {
        return uniquePopId;
    }

    /**
     * Sets the value of the uniquePopId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniquePopId(String value) {
        this.uniquePopId = value;
    }

    /**
     * Gets the value of the vlanlm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVLANLM() {
        return vlanlm;
    }

    /**
     * Sets the value of the vlanlm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVLANLM(String value) {
        this.vlanlm = value;
    }

    /**
     * Gets the value of the isVRFLiteRequired property.
     * 
     */
    public boolean isIsVRFLiteRequired() {
        return isVRFLiteRequired;
    }

    /**
     * Sets the value of the isVRFLiteRequired property.
     * 
     */
    public void setIsVRFLiteRequired(boolean value) {
        this.isVRFLiteRequired = value;
    }

    /**
     * Gets the value of the asNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASNumber() {
        return asNumber;
    }

    /**
     * Sets the value of the asNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASNumber(String value) {
        this.asNumber = value;
    }

}
