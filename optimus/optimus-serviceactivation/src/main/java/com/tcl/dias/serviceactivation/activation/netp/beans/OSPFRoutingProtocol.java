
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OSPFRoutingProtocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OSPFRoutingProtocol">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}RoutingProtocol">
 *       &lt;sequence>
 *         &lt;element name="processId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="domainId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="areaId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isNetworkTypeP2P" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isIPOSPFMTUIgnore" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isAuthenticationRequired" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="authenticationMode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="MD5"/>
 *               &lt;enumeration value="DEFAULT"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="authenticationPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isRedistributeConnectedEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isRedistributeStaticEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isOSPFOriginateDefault" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ospfCost" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="redistributionRoutemapName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LocalPreference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authenticationKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="V2"/>
 *               &lt;enumeration value="V3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OSPFNeighborOutboundRoutingPolicy" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" minOccurs="0"/>
 *         &lt;element name="OSPFNeighborOutboundRoutingPolicyV6" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" minOccurs="0"/>
 *         &lt;element name="ALUDefaultOriginateConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUDefaultOriginateConfig" minOccurs="0"/>
 *         &lt;element name="shamLinkConfig" type="{http://www.tcl.com/2014/5/ipsvc/xsd}ShamlinkConfig" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="localPreferenceV6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALUDefaultOriginateV6Config" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUDefaultOriginateConfig" minOccurs="0"/>
 *         &lt;element name="networkType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="P2P"/>
 *               &lt;enumeration value="P2MP"/>
 *               &lt;enumeration value="Broadcast"/>
 *               &lt;enumeration value="Nonbroadcast"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OSPFRoutingProtocol", propOrder = {
    "processId",
    "domainId",
    "areaId",
    "isNetworkTypeP2P",
    "isIPOSPFMTUIgnore",
    "isAuthenticationRequired",
    "authenticationMode",
    "authenticationPassword",
    "isRedistributeConnectedEnabled",
    "isRedistributeStaticEnabled",
    "isOSPFOriginateDefault",
    "ospfCost",
    "redistributionRoutemapName",
    "localPreference",
    "authenticationKey",
    "version",
    "ospfNeighborOutboundRoutingPolicy",
    "ospfNeighborOutboundRoutingPolicyV6",
    "aluDefaultOriginateConfig",
    "shamLinkConfig",
    "localPreferenceV6",
    "aluDefaultOriginateV6Config",
    "networkType"
})
public class OSPFRoutingProtocol
    extends RoutingProtocol
{

    protected String processId;
    protected String domainId;
    protected String areaId;
    protected String isNetworkTypeP2P;
    protected String isIPOSPFMTUIgnore;
    protected String isAuthenticationRequired;
    @XmlElement(defaultValue = "MD5")
    protected String authenticationMode;
    protected String authenticationPassword;
    protected String isRedistributeConnectedEnabled;
    protected String isRedistributeStaticEnabled;
    protected String isOSPFOriginateDefault;
    protected Integer ospfCost;
    protected String redistributionRoutemapName;
    @XmlElement(name = "LocalPreference")
    protected String localPreference;
    protected String authenticationKey;
    protected String version;
    @XmlElement(name = "OSPFNeighborOutboundRoutingPolicy")
    protected RoutingPolicy ospfNeighborOutboundRoutingPolicy;
    @XmlElement(name = "OSPFNeighborOutboundRoutingPolicyV6")
    protected RoutingPolicy ospfNeighborOutboundRoutingPolicyV6;
    @XmlElement(name = "ALUDefaultOriginateConfig")
    protected ALUDefaultOriginateConfig aluDefaultOriginateConfig;
    protected List<ShamlinkConfig> shamLinkConfig;
    protected String localPreferenceV6;
    @XmlElement(name = "ALUDefaultOriginateV6Config")
    protected ALUDefaultOriginateConfig aluDefaultOriginateV6Config;
    protected String networkType;

    /**
     * Gets the value of the processId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * Sets the value of the processId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessId(String value) {
        this.processId = value;
    }

    /**
     * Gets the value of the domainId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainId() {
        return domainId;
    }

    /**
     * Sets the value of the domainId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainId(String value) {
        this.domainId = value;
    }

    /**
     * Gets the value of the areaId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAreaId() {
        return areaId;
    }

    /**
     * Sets the value of the areaId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAreaId(String value) {
        this.areaId = value;
    }

    /**
     * Gets the value of the isNetworkTypeP2P property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsNetworkTypeP2P() {
        return isNetworkTypeP2P;
    }

    /**
     * Sets the value of the isNetworkTypeP2P property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsNetworkTypeP2P(String value) {
        this.isNetworkTypeP2P = value;
    }

    /**
     * Gets the value of the isIPOSPFMTUIgnore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsIPOSPFMTUIgnore() {
        return isIPOSPFMTUIgnore;
    }

    /**
     * Sets the value of the isIPOSPFMTUIgnore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsIPOSPFMTUIgnore(String value) {
        this.isIPOSPFMTUIgnore = value;
    }

    /**
     * Gets the value of the isAuthenticationRequired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsAuthenticationRequired() {
        return isAuthenticationRequired;
    }

    /**
     * Sets the value of the isAuthenticationRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsAuthenticationRequired(String value) {
        this.isAuthenticationRequired = value;
    }

    /**
     * Gets the value of the authenticationMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationMode() {
        return authenticationMode;
    }

    /**
     * Sets the value of the authenticationMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationMode(String value) {
        this.authenticationMode = value;
    }

    /**
     * Gets the value of the authenticationPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationPassword() {
        return authenticationPassword;
    }

    /**
     * Sets the value of the authenticationPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationPassword(String value) {
        this.authenticationPassword = value;
    }

    /**
     * Gets the value of the isRedistributeConnectedEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsRedistributeConnectedEnabled() {
        return isRedistributeConnectedEnabled;
    }

    /**
     * Sets the value of the isRedistributeConnectedEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsRedistributeConnectedEnabled(String value) {
        this.isRedistributeConnectedEnabled = value;
    }

    /**
     * Gets the value of the isRedistributeStaticEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsRedistributeStaticEnabled() {
        return isRedistributeStaticEnabled;
    }

    /**
     * Sets the value of the isRedistributeStaticEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsRedistributeStaticEnabled(String value) {
        this.isRedistributeStaticEnabled = value;
    }

    /**
     * Gets the value of the isOSPFOriginateDefault property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsOSPFOriginateDefault() {
        return isOSPFOriginateDefault;
    }

    /**
     * Sets the value of the isOSPFOriginateDefault property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsOSPFOriginateDefault(String value) {
        this.isOSPFOriginateDefault = value;
    }

    /**
     * Gets the value of the ospfCost property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOspfCost() {
        return ospfCost;
    }

    /**
     * Sets the value of the ospfCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOspfCost(Integer value) {
        this.ospfCost = value;
    }

    /**
     * Gets the value of the redistributionRoutemapName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRedistributionRoutemapName() {
        return redistributionRoutemapName;
    }

    /**
     * Sets the value of the redistributionRoutemapName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRedistributionRoutemapName(String value) {
        this.redistributionRoutemapName = value;
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
     * Gets the value of the authenticationKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationKey() {
        return authenticationKey;
    }

    /**
     * Sets the value of the authenticationKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationKey(String value) {
        this.authenticationKey = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the ospfNeighborOutboundRoutingPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingPolicy }
     *     
     */
    public RoutingPolicy getOSPFNeighborOutboundRoutingPolicy() {
        return ospfNeighborOutboundRoutingPolicy;
    }

    /**
     * Sets the value of the ospfNeighborOutboundRoutingPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingPolicy }
     *     
     */
    public void setOSPFNeighborOutboundRoutingPolicy(RoutingPolicy value) {
        this.ospfNeighborOutboundRoutingPolicy = value;
    }

    /**
     * Gets the value of the ospfNeighborOutboundRoutingPolicyV6 property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingPolicy }
     *     
     */
    public RoutingPolicy getOSPFNeighborOutboundRoutingPolicyV6() {
        return ospfNeighborOutboundRoutingPolicyV6;
    }

    /**
     * Sets the value of the ospfNeighborOutboundRoutingPolicyV6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingPolicy }
     *     
     */
    public void setOSPFNeighborOutboundRoutingPolicyV6(RoutingPolicy value) {
        this.ospfNeighborOutboundRoutingPolicyV6 = value;
    }

    /**
     * Gets the value of the aluDefaultOriginateConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ALUDefaultOriginateConfig }
     *     
     */
    public ALUDefaultOriginateConfig getALUDefaultOriginateConfig() {
        return aluDefaultOriginateConfig;
    }

    /**
     * Sets the value of the aluDefaultOriginateConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ALUDefaultOriginateConfig }
     *     
     */
    public void setALUDefaultOriginateConfig(ALUDefaultOriginateConfig value) {
        this.aluDefaultOriginateConfig = value;
    }

    /**
     * Gets the value of the shamLinkConfig property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shamLinkConfig property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShamLinkConfig().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ShamlinkConfig }
     * 
     * 
     */
    public List<ShamlinkConfig> getShamLinkConfig() {
        if (shamLinkConfig == null) {
            shamLinkConfig = new ArrayList<ShamlinkConfig>();
        }
        return this.shamLinkConfig;
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
     * Gets the value of the aluDefaultOriginateV6Config property.
     * 
     * @return
     *     possible object is
     *     {@link ALUDefaultOriginateConfig }
     *     
     */
    public ALUDefaultOriginateConfig getALUDefaultOriginateV6Config() {
        return aluDefaultOriginateV6Config;
    }

    /**
     * Sets the value of the aluDefaultOriginateV6Config property.
     * 
     * @param value
     *     allowed object is
     *     {@link ALUDefaultOriginateConfig }
     *     
     */
    public void setALUDefaultOriginateV6Config(ALUDefaultOriginateConfig value) {
        this.aluDefaultOriginateV6Config = value;
    }

    /**
     * Gets the value of the networkType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkType() {
        return networkType;
    }

    /**
     * Sets the value of the networkType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkType(String value) {
        this.networkType = value;
    }

}
