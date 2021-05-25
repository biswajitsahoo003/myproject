
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BGPRoutingProtocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BGPRoutingProtocol">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}RoutingProtocol">
 *       &lt;sequence>
 *         &lt;element name="remoteASNumber" type="{http://www.tcl.com/2011/11/ace/common/xsd}ASNumber" minOccurs="0"/>
 *         &lt;element name="CPEASNumber" type="{http://www.tcl.com/2011/11/ace/common/xsd}ASNumber" minOccurs="0"/>
 *         &lt;element name="bgpNeighbourLocalASNumber" type="{http://www.tcl.com/2011/11/ace/common/xsd}ASNumber" minOccurs="0"/>
 *         &lt;element name="isOriginateDefaultEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isASOverriden" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isAuthenticationRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="authenticationMode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="MD5"/>
 *               &lt;enumeration value="DES3"/>
 *               &lt;enumeration value="DEFAULT"/>
 *               &lt;enumeration value="HASH2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="authenticationPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isEBGPMultihopRequired" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="multihopTTL" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isSOORequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="splitHorizon" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="inboundBGPv4PrefixesAllowed" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}PrefixList" minOccurs="0"/>
 *         &lt;element name="outboundBGPv4PrefixesAllowed" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}PrefixList" minOccurs="0"/>
 *         &lt;element name="inboundBGPv6PrefixesAllowed" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}PrefixList" minOccurs="0"/>
 *         &lt;element name="outboundBGPv6PrefixesAllowed" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}PrefixList" minOccurs="0"/>
 *         &lt;element name="maxPrefix" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxPrefixThreshold" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="routesExchanged" type="{http://www.tcl.com/2011/11/ipsvc/xsd}routesExchanged" minOccurs="0"/>
 *         &lt;element name="holdTime" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="helloTime" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isRedistributeConnectedEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isRedistributeStaticEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isNeighbourShutdownRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="localBGPNeighbourUpdateSource" type="{http://www.tcl.com/2011/11/ipsvc/xsd}LoopbackInterface" minOccurs="0"/>
 *         &lt;element name="remoteBGPNeighbourUpdateSource" type="{http://www.tcl.com/2011/11/ipsvc/xsd}LoopbackInterface" minOccurs="0"/>
 *         &lt;element name="neighbourCommunity" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="remoteIPV4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="remoteIPV6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *         &lt;element name="bgpSoftConfigInboundEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="inboundBGPNeighbourRoutemapName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AS_PATH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LocalPreference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BGPNeighborInboundRouteMap" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BGPNeighborInboundRouteMapV6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BGPNeighborOutboundRouteMap" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BGPNeighborOutboundRouteMapV6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metric" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALUkeepAlive" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALUmultiPathCost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALUBGPPeerGroupInboundRoutingPolicy" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ALUBGPPeerGroupOutboundRoutingPolicy" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ALUBGPPeerGroupInboundRoutingPolicyV6" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ALUBGPPeerGroupOutboundRoutingPolicyV6" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ALUBGPPeerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALUBackupPath" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="IPV4"/>
 *               &lt;enumeration value="IPV6"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="BOTH"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ALUDefaultOriginateConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUDefaultOriginateConfig" minOccurs="0"/>
 *         &lt;element name="ALUDisableBGPPeerGrpExtCommunity" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ALUBGPPeerType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALUBGPMEDValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LocalPreferenceInVPNPolicy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LocalPreferenceV6InVPNPolicy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isLPInVPNPolicyConfigured" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ALUMultiHopValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALUDefaultOriginateV6Config" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUDefaultOriginateConfig" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BGPRoutingProtocol", propOrder = {
    "remoteASNumber",
    "cpeasNumber",
    "bgpNeighbourLocalASNumber",
    "isOriginateDefaultEnabled",
    "isASOverriden",
    "isAuthenticationRequired",
    "authenticationMode",
    "authenticationPassword",
    "isEBGPMultihopRequired",
    "multihopTTL",
    "isSOORequired",
    "splitHorizon",
    "inboundBGPv4PrefixesAllowed",
    "outboundBGPv4PrefixesAllowed",
    "inboundBGPv6PrefixesAllowed",
    "outboundBGPv6PrefixesAllowed",
    "maxPrefix",
    "maxPrefixThreshold",
    "routesExchanged",
    "holdTime",
    "helloTime",
    "isRedistributeConnectedEnabled",
    "isRedistributeStaticEnabled",
    "isNeighbourShutdownRequired",
    "localBGPNeighbourUpdateSource",
    "remoteBGPNeighbourUpdateSource",
    "neighbourCommunity",
    "remoteIPV4Address",
    "remoteIPV6Address",
    "bgpSoftConfigInboundEnabled",
    "inboundBGPNeighbourRoutemapName",
    "aspath",
    "localPreference",
    "bgpNeighborInboundRouteMap",
    "bgpNeighborInboundRouteMapV6",
    "bgpNeighborOutboundRouteMap",
    "bgpNeighborOutboundRouteMapV6",
    "metric",
    "alUkeepAlive",
    "alUmultiPathCost",
    "alubgpPeerGroupInboundRoutingPolicy",
    "alubgpPeerGroupOutboundRoutingPolicy",
    "alubgpPeerGroupInboundRoutingPolicyV6",
    "alubgpPeerGroupOutboundRoutingPolicyV6",
    "alubgpPeerName",
    "aluBackupPath",
    "aluDefaultOriginateConfig",
    "aluDisableBGPPeerGrpExtCommunity",
    "alubgpPeerType",
    "alubgpmedValue",
    "localPreferenceInVPNPolicy",
    "localPreferenceV6InVPNPolicy",
    "isLPInVPNPolicyConfigured",
    "aluMultiHopValue",
    "aluDefaultOriginateV6Config"
})
public class BGPRoutingProtocol
    extends RoutingProtocol
{

    protected ASNumber remoteASNumber;
    @XmlElement(name = "CPEASNumber")
    protected ASNumber cpeasNumber;
    protected ASNumber bgpNeighbourLocalASNumber;
    protected Boolean isOriginateDefaultEnabled;
    protected String isASOverriden;
    protected Boolean isAuthenticationRequired;
    protected String authenticationMode;
    protected String authenticationPassword;
    protected String isEBGPMultihopRequired;
    protected Integer multihopTTL;
    protected Boolean isSOORequired;
    protected Boolean splitHorizon;
    protected PrefixList inboundBGPv4PrefixesAllowed;
    protected PrefixList outboundBGPv4PrefixesAllowed;
    protected PrefixList inboundBGPv6PrefixesAllowed;
    protected PrefixList outboundBGPv6PrefixesAllowed;
    protected Integer maxPrefix;
    protected Integer maxPrefixThreshold;
    protected RoutesExchanged routesExchanged;
    protected Integer holdTime;
    protected Integer helloTime;
    protected Boolean isRedistributeConnectedEnabled;
    protected Boolean isRedistributeStaticEnabled;
    protected Boolean isNeighbourShutdownRequired;
    protected LoopbackInterface localBGPNeighbourUpdateSource;
    protected LoopbackInterface remoteBGPNeighbourUpdateSource;
    protected List<String> neighbourCommunity;
    protected IPV4Address remoteIPV4Address;
    protected IPV6Address remoteIPV6Address;
    protected String bgpSoftConfigInboundEnabled;
    protected String inboundBGPNeighbourRoutemapName;
    @XmlElement(name = "AS_PATH")
    protected String aspath;
    @XmlElement(name = "LocalPreference")
    protected String localPreference;
    @XmlElement(name = "BGPNeighborInboundRouteMap")
    protected String bgpNeighborInboundRouteMap;
    @XmlElement(name = "BGPNeighborInboundRouteMapV6")
    protected String bgpNeighborInboundRouteMapV6;
    @XmlElement(name = "BGPNeighborOutboundRouteMap")
    protected String bgpNeighborOutboundRouteMap;
    @XmlElement(name = "BGPNeighborOutboundRouteMapV6")
    protected String bgpNeighborOutboundRouteMapV6;
    protected String metric;
    @XmlElement(name = "ALUkeepAlive")
    protected String alUkeepAlive;
    @XmlElement(name = "ALUmultiPathCost")
    protected String alUmultiPathCost;
    @XmlElement(name = "ALUBGPPeerGroupInboundRoutingPolicy")
    protected List<RoutingPolicy> alubgpPeerGroupInboundRoutingPolicy;
    @XmlElement(name = "ALUBGPPeerGroupOutboundRoutingPolicy")
    protected List<RoutingPolicy> alubgpPeerGroupOutboundRoutingPolicy;
    @XmlElement(name = "ALUBGPPeerGroupInboundRoutingPolicyV6")
    protected List<RoutingPolicy> alubgpPeerGroupInboundRoutingPolicyV6;
    @XmlElement(name = "ALUBGPPeerGroupOutboundRoutingPolicyV6")
    protected List<RoutingPolicy> alubgpPeerGroupOutboundRoutingPolicyV6;
    @XmlElement(name = "ALUBGPPeerName")
    protected String alubgpPeerName;
    @XmlElement(name = "ALUBackupPath")
    protected String aluBackupPath;
    @XmlElement(name = "ALUDefaultOriginateConfig")
    protected ALUDefaultOriginateConfig aluDefaultOriginateConfig;
    @XmlElement(name = "ALUDisableBGPPeerGrpExtCommunity")
    protected Boolean aluDisableBGPPeerGrpExtCommunity;
    @XmlElement(name = "ALUBGPPeerType")
    protected String alubgpPeerType;
    @XmlElement(name = "ALUBGPMEDValue")
    protected String alubgpmedValue;
    @XmlElement(name = "LocalPreferenceInVPNPolicy")
    protected String localPreferenceInVPNPolicy;
    @XmlElement(name = "LocalPreferenceV6InVPNPolicy")
    protected String localPreferenceV6InVPNPolicy;
    protected Boolean isLPInVPNPolicyConfigured;
    @XmlElement(name = "ALUMultiHopValue")
    protected String aluMultiHopValue;
    @XmlElement(name = "ALUDefaultOriginateV6Config")
    protected ALUDefaultOriginateConfig aluDefaultOriginateV6Config;

    /**
     * Gets the value of the remoteASNumber property.
     * 
     * @return
     *     possible object is
     *     {@link ASNumber }
     *     
     */
    public ASNumber getRemoteASNumber() {
        return remoteASNumber;
    }

    /**
     * Sets the value of the remoteASNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link ASNumber }
     *     
     */
    public void setRemoteASNumber(ASNumber value) {
        this.remoteASNumber = value;
    }

    /**
     * Gets the value of the cpeasNumber property.
     * 
     * @return
     *     possible object is
     *     {@link ASNumber }
     *     
     */
    public ASNumber getCPEASNumber() {
        return cpeasNumber;
    }

    /**
     * Sets the value of the cpeasNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link ASNumber }
     *     
     */
    public void setCPEASNumber(ASNumber value) {
        this.cpeasNumber = value;
    }

    /**
     * Gets the value of the bgpNeighbourLocalASNumber property.
     * 
     * @return
     *     possible object is
     *     {@link ASNumber }
     *     
     */
    public ASNumber getBgpNeighbourLocalASNumber() {
        return bgpNeighbourLocalASNumber;
    }

    /**
     * Sets the value of the bgpNeighbourLocalASNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link ASNumber }
     *     
     */
    public void setBgpNeighbourLocalASNumber(ASNumber value) {
        this.bgpNeighbourLocalASNumber = value;
    }

    /**
     * Gets the value of the isOriginateDefaultEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOriginateDefaultEnabled() {
        return isOriginateDefaultEnabled;
    }

    /**
     * Sets the value of the isOriginateDefaultEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOriginateDefaultEnabled(Boolean value) {
        this.isOriginateDefaultEnabled = value;
    }

    /**
     * Gets the value of the isASOverriden property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsASOverriden() {
        return isASOverriden;
    }

    /**
     * Sets the value of the isASOverriden property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsASOverriden(String value) {
        this.isASOverriden = value;
    }

    /**
     * Gets the value of the isAuthenticationRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAuthenticationRequired() {
        return isAuthenticationRequired;
    }

    /**
     * Sets the value of the isAuthenticationRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAuthenticationRequired(Boolean value) {
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
     * Gets the value of the isEBGPMultihopRequired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsEBGPMultihopRequired() {
        return isEBGPMultihopRequired;
    }

    /**
     * Sets the value of the isEBGPMultihopRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsEBGPMultihopRequired(String value) {
        this.isEBGPMultihopRequired = value;
    }

    /**
     * Gets the value of the multihopTTL property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMultihopTTL() {
        return multihopTTL;
    }

    /**
     * Sets the value of the multihopTTL property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMultihopTTL(Integer value) {
        this.multihopTTL = value;
    }

    /**
     * Gets the value of the isSOORequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSOORequired() {
        return isSOORequired;
    }

    /**
     * Sets the value of the isSOORequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSOORequired(Boolean value) {
        this.isSOORequired = value;
    }

    /**
     * Gets the value of the splitHorizon property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSplitHorizon() {
        return splitHorizon;
    }

    /**
     * Sets the value of the splitHorizon property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSplitHorizon(Boolean value) {
        this.splitHorizon = value;
    }

    /**
     * Gets the value of the inboundBGPv4PrefixesAllowed property.
     * 
     * @return
     *     possible object is
     *     {@link PrefixList }
     *     
     */
    public PrefixList getInboundBGPv4PrefixesAllowed() {
        return inboundBGPv4PrefixesAllowed;
    }

    /**
     * Sets the value of the inboundBGPv4PrefixesAllowed property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrefixList }
     *     
     */
    public void setInboundBGPv4PrefixesAllowed(PrefixList value) {
        this.inboundBGPv4PrefixesAllowed = value;
    }

    /**
     * Gets the value of the outboundBGPv4PrefixesAllowed property.
     * 
     * @return
     *     possible object is
     *     {@link PrefixList }
     *     
     */
    public PrefixList getOutboundBGPv4PrefixesAllowed() {
        return outboundBGPv4PrefixesAllowed;
    }

    /**
     * Sets the value of the outboundBGPv4PrefixesAllowed property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrefixList }
     *     
     */
    public void setOutboundBGPv4PrefixesAllowed(PrefixList value) {
        this.outboundBGPv4PrefixesAllowed = value;
    }

    /**
     * Gets the value of the inboundBGPv6PrefixesAllowed property.
     * 
     * @return
     *     possible object is
     *     {@link PrefixList }
     *     
     */
    public PrefixList getInboundBGPv6PrefixesAllowed() {
        return inboundBGPv6PrefixesAllowed;
    }

    /**
     * Sets the value of the inboundBGPv6PrefixesAllowed property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrefixList }
     *     
     */
    public void setInboundBGPv6PrefixesAllowed(PrefixList value) {
        this.inboundBGPv6PrefixesAllowed = value;
    }

    /**
     * Gets the value of the outboundBGPv6PrefixesAllowed property.
     * 
     * @return
     *     possible object is
     *     {@link PrefixList }
     *     
     */
    public PrefixList getOutboundBGPv6PrefixesAllowed() {
        return outboundBGPv6PrefixesAllowed;
    }

    /**
     * Sets the value of the outboundBGPv6PrefixesAllowed property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrefixList }
     *     
     */
    public void setOutboundBGPv6PrefixesAllowed(PrefixList value) {
        this.outboundBGPv6PrefixesAllowed = value;
    }

    /**
     * Gets the value of the maxPrefix property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxPrefix() {
        return maxPrefix;
    }

    /**
     * Sets the value of the maxPrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxPrefix(Integer value) {
        this.maxPrefix = value;
    }

    /**
     * Gets the value of the maxPrefixThreshold property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxPrefixThreshold() {
        return maxPrefixThreshold;
    }

    /**
     * Sets the value of the maxPrefixThreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxPrefixThreshold(Integer value) {
        this.maxPrefixThreshold = value;
    }

    /**
     * Gets the value of the routesExchanged property.
     * 
     * @return
     *     possible object is
     *     {@link RoutesExchanged }
     *     
     */
    public RoutesExchanged getRoutesExchanged() {
        return routesExchanged;
    }

    /**
     * Sets the value of the routesExchanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutesExchanged }
     *     
     */
    public void setRoutesExchanged(RoutesExchanged value) {
        this.routesExchanged = value;
    }

    /**
     * Gets the value of the holdTime property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHoldTime() {
        return holdTime;
    }

    /**
     * Sets the value of the holdTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHoldTime(Integer value) {
        this.holdTime = value;
    }

    /**
     * Gets the value of the helloTime property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHelloTime() {
        return helloTime;
    }

    /**
     * Sets the value of the helloTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHelloTime(Integer value) {
        this.helloTime = value;
    }

    /**
     * Gets the value of the isRedistributeConnectedEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRedistributeConnectedEnabled() {
        return isRedistributeConnectedEnabled;
    }

    /**
     * Sets the value of the isRedistributeConnectedEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRedistributeConnectedEnabled(Boolean value) {
        this.isRedistributeConnectedEnabled = value;
    }

    /**
     * Gets the value of the isRedistributeStaticEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRedistributeStaticEnabled() {
        return isRedistributeStaticEnabled;
    }

    /**
     * Sets the value of the isRedistributeStaticEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRedistributeStaticEnabled(Boolean value) {
        this.isRedistributeStaticEnabled = value;
    }

    /**
     * Gets the value of the isNeighbourShutdownRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNeighbourShutdownRequired() {
        return isNeighbourShutdownRequired;
    }

    /**
     * Sets the value of the isNeighbourShutdownRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNeighbourShutdownRequired(Boolean value) {
        this.isNeighbourShutdownRequired = value;
    }

    /**
     * Gets the value of the localBGPNeighbourUpdateSource property.
     * 
     * @return
     *     possible object is
     *     {@link LoopbackInterface }
     *     
     */
    public LoopbackInterface getLocalBGPNeighbourUpdateSource() {
        return localBGPNeighbourUpdateSource;
    }

    /**
     * Sets the value of the localBGPNeighbourUpdateSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoopbackInterface }
     *     
     */
    public void setLocalBGPNeighbourUpdateSource(LoopbackInterface value) {
        this.localBGPNeighbourUpdateSource = value;
    }

    /**
     * Gets the value of the remoteBGPNeighbourUpdateSource property.
     * 
     * @return
     *     possible object is
     *     {@link LoopbackInterface }
     *     
     */
    public LoopbackInterface getRemoteBGPNeighbourUpdateSource() {
        return remoteBGPNeighbourUpdateSource;
    }

    /**
     * Sets the value of the remoteBGPNeighbourUpdateSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoopbackInterface }
     *     
     */
    public void setRemoteBGPNeighbourUpdateSource(LoopbackInterface value) {
        this.remoteBGPNeighbourUpdateSource = value;
    }

    /**
     * Gets the value of the neighbourCommunity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the neighbourCommunity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNeighbourCommunity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNeighbourCommunity() {
        if (neighbourCommunity == null) {
            neighbourCommunity = new ArrayList<String>();
        }
        return this.neighbourCommunity;
    }

    /**
     * Gets the value of the remoteIPV4Address property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getRemoteIPV4Address() {
        return remoteIPV4Address;
    }

    /**
     * Sets the value of the remoteIPV4Address property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setRemoteIPV4Address(IPV4Address value) {
        this.remoteIPV4Address = value;
    }

    /**
     * Gets the value of the remoteIPV6Address property.
     * 
     * @return
     *     possible object is
     *     {@link IPV6Address }
     *     
     */
    public IPV6Address getRemoteIPV6Address() {
        return remoteIPV6Address;
    }

    /**
     * Sets the value of the remoteIPV6Address property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV6Address }
     *     
     */
    public void setRemoteIPV6Address(IPV6Address value) {
        this.remoteIPV6Address = value;
    }

    /**
     * Gets the value of the bgpSoftConfigInboundEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBgpSoftConfigInboundEnabled() {
        return bgpSoftConfigInboundEnabled;
    }

    /**
     * Sets the value of the bgpSoftConfigInboundEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBgpSoftConfigInboundEnabled(String value) {
        this.bgpSoftConfigInboundEnabled = value;
    }

    /**
     * Gets the value of the inboundBGPNeighbourRoutemapName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboundBGPNeighbourRoutemapName() {
        return inboundBGPNeighbourRoutemapName;
    }

    /**
     * Sets the value of the inboundBGPNeighbourRoutemapName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboundBGPNeighbourRoutemapName(String value) {
        this.inboundBGPNeighbourRoutemapName = value;
    }

    /**
     * Gets the value of the aspath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASPATH() {
        return aspath;
    }

    /**
     * Sets the value of the aspath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASPATH(String value) {
        this.aspath = value;
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
     * Gets the value of the bgpNeighborInboundRouteMap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBGPNeighborInboundRouteMap() {
        return bgpNeighborInboundRouteMap;
    }

    /**
     * Sets the value of the bgpNeighborInboundRouteMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBGPNeighborInboundRouteMap(String value) {
        this.bgpNeighborInboundRouteMap = value;
    }

    /**
     * Gets the value of the bgpNeighborInboundRouteMapV6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBGPNeighborInboundRouteMapV6() {
        return bgpNeighborInboundRouteMapV6;
    }

    /**
     * Sets the value of the bgpNeighborInboundRouteMapV6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBGPNeighborInboundRouteMapV6(String value) {
        this.bgpNeighborInboundRouteMapV6 = value;
    }

    /**
     * Gets the value of the bgpNeighborOutboundRouteMap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBGPNeighborOutboundRouteMap() {
        return bgpNeighborOutboundRouteMap;
    }

    /**
     * Sets the value of the bgpNeighborOutboundRouteMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBGPNeighborOutboundRouteMap(String value) {
        this.bgpNeighborOutboundRouteMap = value;
    }

    /**
     * Gets the value of the bgpNeighborOutboundRouteMapV6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBGPNeighborOutboundRouteMapV6() {
        return bgpNeighborOutboundRouteMapV6;
    }

    /**
     * Sets the value of the bgpNeighborOutboundRouteMapV6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBGPNeighborOutboundRouteMapV6(String value) {
        this.bgpNeighborOutboundRouteMapV6 = value;
    }

    /**
     * Gets the value of the metric property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetric() {
        return metric;
    }

    /**
     * Sets the value of the metric property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetric(String value) {
        this.metric = value;
    }

    /**
     * Gets the value of the alUkeepAlive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALUkeepAlive() {
        return alUkeepAlive;
    }

    /**
     * Sets the value of the alUkeepAlive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALUkeepAlive(String value) {
        this.alUkeepAlive = value;
    }

    /**
     * Gets the value of the alUmultiPathCost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALUmultiPathCost() {
        return alUmultiPathCost;
    }

    /**
     * Sets the value of the alUmultiPathCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALUmultiPathCost(String value) {
        this.alUmultiPathCost = value;
    }

    /**
     * Gets the value of the alubgpPeerGroupInboundRoutingPolicy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alubgpPeerGroupInboundRoutingPolicy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALUBGPPeerGroupInboundRoutingPolicy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoutingPolicy }
     * 
     * 
     */
    public List<RoutingPolicy> getALUBGPPeerGroupInboundRoutingPolicy() {
        if (alubgpPeerGroupInboundRoutingPolicy == null) {
            alubgpPeerGroupInboundRoutingPolicy = new ArrayList<RoutingPolicy>();
        }
        return this.alubgpPeerGroupInboundRoutingPolicy;
    }

    /**
     * Gets the value of the alubgpPeerGroupOutboundRoutingPolicy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alubgpPeerGroupOutboundRoutingPolicy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALUBGPPeerGroupOutboundRoutingPolicy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoutingPolicy }
     * 
     * 
     */
    public List<RoutingPolicy> getALUBGPPeerGroupOutboundRoutingPolicy() {
        if (alubgpPeerGroupOutboundRoutingPolicy == null) {
            alubgpPeerGroupOutboundRoutingPolicy = new ArrayList<RoutingPolicy>();
        }
        return this.alubgpPeerGroupOutboundRoutingPolicy;
    }

    /**
     * Gets the value of the alubgpPeerGroupInboundRoutingPolicyV6 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alubgpPeerGroupInboundRoutingPolicyV6 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALUBGPPeerGroupInboundRoutingPolicyV6().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoutingPolicy }
     * 
     * 
     */
    public List<RoutingPolicy> getALUBGPPeerGroupInboundRoutingPolicyV6() {
        if (alubgpPeerGroupInboundRoutingPolicyV6 == null) {
            alubgpPeerGroupInboundRoutingPolicyV6 = new ArrayList<RoutingPolicy>();
        }
        return this.alubgpPeerGroupInboundRoutingPolicyV6;
    }

    /**
     * Gets the value of the alubgpPeerGroupOutboundRoutingPolicyV6 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alubgpPeerGroupOutboundRoutingPolicyV6 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALUBGPPeerGroupOutboundRoutingPolicyV6().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoutingPolicy }
     * 
     * 
     */
    public List<RoutingPolicy> getALUBGPPeerGroupOutboundRoutingPolicyV6() {
        if (alubgpPeerGroupOutboundRoutingPolicyV6 == null) {
            alubgpPeerGroupOutboundRoutingPolicyV6 = new ArrayList<RoutingPolicy>();
        }
        return this.alubgpPeerGroupOutboundRoutingPolicyV6;
    }

    /**
     * Gets the value of the alubgpPeerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALUBGPPeerName() {
        return alubgpPeerName;
    }

    /**
     * Sets the value of the alubgpPeerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALUBGPPeerName(String value) {
        this.alubgpPeerName = value;
    }

    /**
     * Gets the value of the aluBackupPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALUBackupPath() {
        return aluBackupPath;
    }

    /**
     * Sets the value of the aluBackupPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALUBackupPath(String value) {
        this.aluBackupPath = value;
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
     * Gets the value of the aluDisableBGPPeerGrpExtCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isALUDisableBGPPeerGrpExtCommunity() {
        return aluDisableBGPPeerGrpExtCommunity;
    }

    /**
     * Sets the value of the aluDisableBGPPeerGrpExtCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setALUDisableBGPPeerGrpExtCommunity(Boolean value) {
        this.aluDisableBGPPeerGrpExtCommunity = value;
    }

    /**
     * Gets the value of the alubgpPeerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALUBGPPeerType() {
        return alubgpPeerType;
    }

    /**
     * Sets the value of the alubgpPeerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALUBGPPeerType(String value) {
        this.alubgpPeerType = value;
    }

    /**
     * Gets the value of the alubgpmedValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALUBGPMEDValue() {
        return alubgpmedValue;
    }

    /**
     * Sets the value of the alubgpmedValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALUBGPMEDValue(String value) {
        this.alubgpmedValue = value;
    }

    /**
     * Gets the value of the localPreferenceInVPNPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalPreferenceInVPNPolicy() {
        return localPreferenceInVPNPolicy;
    }

    /**
     * Sets the value of the localPreferenceInVPNPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalPreferenceInVPNPolicy(String value) {
        this.localPreferenceInVPNPolicy = value;
    }

    /**
     * Gets the value of the localPreferenceV6InVPNPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalPreferenceV6InVPNPolicy() {
        return localPreferenceV6InVPNPolicy;
    }

    /**
     * Sets the value of the localPreferenceV6InVPNPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalPreferenceV6InVPNPolicy(String value) {
        this.localPreferenceV6InVPNPolicy = value;
    }

    /**
     * Gets the value of the isLPInVPNPolicyConfigured property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsLPInVPNPolicyConfigured() {
        return isLPInVPNPolicyConfigured;
    }

    /**
     * Sets the value of the isLPInVPNPolicyConfigured property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsLPInVPNPolicyConfigured(Boolean value) {
        this.isLPInVPNPolicyConfigured = value;
    }

    /**
     * Gets the value of the aluMultiHopValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALUMultiHopValue() {
        return aluMultiHopValue;
    }

    /**
     * Sets the value of the aluMultiHopValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALUMultiHopValue(String value) {
        this.aluMultiHopValue = value;
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

}
