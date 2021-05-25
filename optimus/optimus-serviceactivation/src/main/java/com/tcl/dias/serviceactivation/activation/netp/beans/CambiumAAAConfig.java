
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CambiumAAAConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CambiumAAAConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EthernetAccess" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLE"/>
 *               &lt;enumeration value="DISABLE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AuthenticationKey" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="selectKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EnforceAuth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Phase1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Phase2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UseRealmStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServerCommonName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NetworkAccessibility" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserAuthenticationMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AllowLocalLoginAfterAAAReject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeviceAccessTracking" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UplinkBurstAllocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DownlinkBurstAllocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BandwidthUplinkSustainRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BandwidthDownlinkSustainRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HighPriorityUplinkCIR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HighPriorityDownlinkCIR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LowPriorityUplinkCIR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LowPriorityDownlinkCIR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EnableBroadcastMulticastRate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HighPriorityChannel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Realm" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Identity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CambiumAAAConfig", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "ethernetAccess",
    "authenticationKey",
    "selectKey",
    "enforceAuth",
    "phase1",
    "phase2",
    "useRealmStatus",
    "username",
    "password",
    "serverCommonName",
    "networkAccessibility",
    "userAuthenticationMode",
    "allowLocalLoginAfterAAAReject",
    "deviceAccessTracking",
    "uplinkBurstAllocation",
    "downlinkBurstAllocation",
    "bandwidthUplinkSustainRate",
    "bandwidthDownlinkSustainRate",
    "highPriorityUplinkCIR",
    "highPriorityDownlinkCIR",
    "lowPriorityUplinkCIR",
    "lowPriorityDownlinkCIR",
    "enableBroadcastMulticastRate",
    "highPriorityChannel",
    "realm",
    "identity"
})
public class CambiumAAAConfig {

    @XmlElement(name = "EthernetAccess")
    protected String ethernetAccess;
    @XmlElement(name = "AuthenticationKey")
    protected Integer authenticationKey;
    protected String selectKey;
    @XmlElement(name = "EnforceAuth")
    protected String enforceAuth;
    @XmlElement(name = "Phase1")
    protected String phase1;
    @XmlElement(name = "Phase2")
    protected String phase2;
    @XmlElement(name = "UseRealmStatus")
    protected String useRealmStatus;
    @XmlElement(name = "Username")
    protected String username;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "ServerCommonName")
    protected String serverCommonName;
    @XmlElement(name = "NetworkAccessibility")
    protected String networkAccessibility;
    @XmlElement(name = "UserAuthenticationMode")
    protected String userAuthenticationMode;
    @XmlElement(name = "AllowLocalLoginAfterAAAReject")
    protected String allowLocalLoginAfterAAAReject;
    @XmlElement(name = "DeviceAccessTracking")
    protected String deviceAccessTracking;
    @XmlElement(name = "UplinkBurstAllocation")
    protected String uplinkBurstAllocation;
    @XmlElement(name = "DownlinkBurstAllocation")
    protected String downlinkBurstAllocation;
    @XmlElement(name = "BandwidthUplinkSustainRate")
    protected String bandwidthUplinkSustainRate;
    @XmlElement(name = "BandwidthDownlinkSustainRate")
    protected String bandwidthDownlinkSustainRate;
    @XmlElement(name = "HighPriorityUplinkCIR")
    protected String highPriorityUplinkCIR;
    @XmlElement(name = "HighPriorityDownlinkCIR")
    protected String highPriorityDownlinkCIR;
    @XmlElement(name = "LowPriorityUplinkCIR")
    protected String lowPriorityUplinkCIR;
    @XmlElement(name = "LowPriorityDownlinkCIR")
    protected String lowPriorityDownlinkCIR;
    @XmlElement(name = "EnableBroadcastMulticastRate")
    protected String enableBroadcastMulticastRate;
    @XmlElement(name = "HighPriorityChannel")
    protected String highPriorityChannel;
    @XmlElement(name = "Realm")
    protected Integer realm;
    @XmlElement(name = "Identity")
    protected String identity;

    /**
     * Gets the value of the ethernetAccess property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEthernetAccess() {
        return ethernetAccess;
    }

    /**
     * Sets the value of the ethernetAccess property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEthernetAccess(String value) {
        this.ethernetAccess = value;
    }

    /**
     * Gets the value of the authenticationKey property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAuthenticationKey() {
        return authenticationKey;
    }

    /**
     * Sets the value of the authenticationKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAuthenticationKey(Integer value) {
        this.authenticationKey = value;
    }

    /**
     * Gets the value of the selectKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectKey() {
        return selectKey;
    }

    /**
     * Sets the value of the selectKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectKey(String value) {
        this.selectKey = value;
    }

    /**
     * Gets the value of the enforceAuth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnforceAuth() {
        return enforceAuth;
    }

    /**
     * Sets the value of the enforceAuth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnforceAuth(String value) {
        this.enforceAuth = value;
    }

    /**
     * Gets the value of the phase1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhase1() {
        return phase1;
    }

    /**
     * Sets the value of the phase1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhase1(String value) {
        this.phase1 = value;
    }

    /**
     * Gets the value of the phase2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhase2() {
        return phase2;
    }

    /**
     * Sets the value of the phase2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhase2(String value) {
        this.phase2 = value;
    }

    /**
     * Gets the value of the useRealmStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseRealmStatus() {
        return useRealmStatus;
    }

    /**
     * Sets the value of the useRealmStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseRealmStatus(String value) {
        this.useRealmStatus = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the serverCommonName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerCommonName() {
        return serverCommonName;
    }

    /**
     * Sets the value of the serverCommonName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerCommonName(String value) {
        this.serverCommonName = value;
    }

    /**
     * Gets the value of the networkAccessibility property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkAccessibility() {
        return networkAccessibility;
    }

    /**
     * Sets the value of the networkAccessibility property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkAccessibility(String value) {
        this.networkAccessibility = value;
    }

    /**
     * Gets the value of the userAuthenticationMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserAuthenticationMode() {
        return userAuthenticationMode;
    }

    /**
     * Sets the value of the userAuthenticationMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserAuthenticationMode(String value) {
        this.userAuthenticationMode = value;
    }

    /**
     * Gets the value of the allowLocalLoginAfterAAAReject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllowLocalLoginAfterAAAReject() {
        return allowLocalLoginAfterAAAReject;
    }

    /**
     * Sets the value of the allowLocalLoginAfterAAAReject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllowLocalLoginAfterAAAReject(String value) {
        this.allowLocalLoginAfterAAAReject = value;
    }

    /**
     * Gets the value of the deviceAccessTracking property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceAccessTracking() {
        return deviceAccessTracking;
    }

    /**
     * Sets the value of the deviceAccessTracking property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceAccessTracking(String value) {
        this.deviceAccessTracking = value;
    }

    /**
     * Gets the value of the uplinkBurstAllocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUplinkBurstAllocation() {
        return uplinkBurstAllocation;
    }

    /**
     * Sets the value of the uplinkBurstAllocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUplinkBurstAllocation(String value) {
        this.uplinkBurstAllocation = value;
    }

    /**
     * Gets the value of the downlinkBurstAllocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDownlinkBurstAllocation() {
        return downlinkBurstAllocation;
    }

    /**
     * Sets the value of the downlinkBurstAllocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDownlinkBurstAllocation(String value) {
        this.downlinkBurstAllocation = value;
    }

    /**
     * Gets the value of the bandwidthUplinkSustainRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBandwidthUplinkSustainRate() {
        return bandwidthUplinkSustainRate;
    }

    /**
     * Sets the value of the bandwidthUplinkSustainRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBandwidthUplinkSustainRate(String value) {
        this.bandwidthUplinkSustainRate = value;
    }

    /**
     * Gets the value of the bandwidthDownlinkSustainRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBandwidthDownlinkSustainRate() {
        return bandwidthDownlinkSustainRate;
    }

    /**
     * Sets the value of the bandwidthDownlinkSustainRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBandwidthDownlinkSustainRate(String value) {
        this.bandwidthDownlinkSustainRate = value;
    }

    /**
     * Gets the value of the highPriorityUplinkCIR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHighPriorityUplinkCIR() {
        return highPriorityUplinkCIR;
    }

    /**
     * Sets the value of the highPriorityUplinkCIR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHighPriorityUplinkCIR(String value) {
        this.highPriorityUplinkCIR = value;
    }

    /**
     * Gets the value of the highPriorityDownlinkCIR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHighPriorityDownlinkCIR() {
        return highPriorityDownlinkCIR;
    }

    /**
     * Sets the value of the highPriorityDownlinkCIR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHighPriorityDownlinkCIR(String value) {
        this.highPriorityDownlinkCIR = value;
    }

    /**
     * Gets the value of the lowPriorityUplinkCIR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLowPriorityUplinkCIR() {
        return lowPriorityUplinkCIR;
    }

    /**
     * Sets the value of the lowPriorityUplinkCIR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLowPriorityUplinkCIR(String value) {
        this.lowPriorityUplinkCIR = value;
    }

    /**
     * Gets the value of the lowPriorityDownlinkCIR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLowPriorityDownlinkCIR() {
        return lowPriorityDownlinkCIR;
    }

    /**
     * Sets the value of the lowPriorityDownlinkCIR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLowPriorityDownlinkCIR(String value) {
        this.lowPriorityDownlinkCIR = value;
    }

    /**
     * Gets the value of the enableBroadcastMulticastRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableBroadcastMulticastRate() {
        return enableBroadcastMulticastRate;
    }

    /**
     * Sets the value of the enableBroadcastMulticastRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableBroadcastMulticastRate(String value) {
        this.enableBroadcastMulticastRate = value;
    }

    /**
     * Gets the value of the highPriorityChannel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHighPriorityChannel() {
        return highPriorityChannel;
    }

    /**
     * Sets the value of the highPriorityChannel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHighPriorityChannel(String value) {
        this.highPriorityChannel = value;
    }

    /**
     * Gets the value of the realm property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRealm() {
        return realm;
    }

    /**
     * Sets the value of the realm property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRealm(Integer value) {
        this.realm = value;
    }

    /**
     * Gets the value of the identity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * Sets the value of the identity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentity(String value) {
        this.identity = value;
    }

}
