
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LAGIORInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LAGIORInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LAGID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LAGInterfaceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LAGMember" type="{http://www.tcl.com/2014/2/ipsvc/xsd}LAGMember" minOccurs="0"/>
 *         &lt;element name="selectionCriteria" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="BEST-PORT"/>
 *               &lt;enumeration value="HIGHEST-COUNT"/>
 *               &lt;enumeration value="HIGHEST-WEIGHT"/>
 *               &lt;enumeration value="SLAVE-TO-PARTNER"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="shutdown" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="portThreshold" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="action" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="DYNAMIC-COST"/>
 *               &lt;enumeration value="DOWN"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="interfaceDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="overrideDescription" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="mode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ACCESS"/>
 *               &lt;enumeration value="NETWORK"/>
 *               &lt;enumeration value="HYBRID"/>
 *               &lt;enumeration value="TRUNK"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="DOT1QTUNNEL"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="encapsulation" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="DOT1Q"/>
 *               &lt;enumeration value="QinQ"/>
 *               &lt;enumeration value="NONE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZendDeviceType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="CISCO"/>
 *               &lt;enumeration value="ALU"/>
 *               &lt;enumeration value="ASR"/>
 *               &lt;enumeration value="EGX"/>
 *               &lt;enumeration value="NNI"/>
 *               &lt;enumeration value="HUAWEI"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="HuaweiSwitchLAGConfigParams" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiSwitchLagConfigParams" minOccurs="0"/>
 *         &lt;element name="CISCORouterLAGConfigParams" type="{http://www.tcl.com/2014/6/ipsvc/xsd}CISCOLAGConfigParams" minOccurs="0"/>
 *         &lt;element name="stpRootProtectionEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="stpBpduFilterEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
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
@XmlType(name = "LAGIORInterface", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "lagid",
    "lagInterfaceName",
    "lagMember",
    "selectionCriteria",
    "shutdown",
    "portThreshold",
    "action",
    "interfaceDescription",
    "overrideDescription",
    "mode",
    "encapsulation",
    "zendDeviceType",
    "huaweiSwitchLAGConfigParams",
    "ciscoRouterLAGConfigParams",
    "stpRootProtectionEnabled",
    "stpBpduFilterEnabled"
})
public class LAGIORInterface {

    @XmlElement(name = "LAGID")
    protected String lagid;
    @XmlElement(name = "LAGInterfaceName")
    protected String lagInterfaceName;
    @XmlElement(name = "LAGMember")
    protected LAGMember lagMember;
    protected String selectionCriteria;
    protected String shutdown;
    protected String portThreshold;
    protected String action;
    protected String interfaceDescription;
    protected String overrideDescription;
    protected String mode;
    protected String encapsulation;
    @XmlElement(name = "ZendDeviceType")
    protected String zendDeviceType;
    @XmlElement(name = "HuaweiSwitchLAGConfigParams")
    protected HuaweiSwitchLagConfigParams huaweiSwitchLAGConfigParams;
    @XmlElement(name = "CISCORouterLAGConfigParams")
    protected CISCOLAGConfigParams ciscoRouterLAGConfigParams;
    protected String stpRootProtectionEnabled;
    protected String stpBpduFilterEnabled;

    /**
     * Gets the value of the lagid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLAGID() {
        return lagid;
    }

    /**
     * Sets the value of the lagid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLAGID(String value) {
        this.lagid = value;
    }

    /**
     * Gets the value of the lagInterfaceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLAGInterfaceName() {
        return lagInterfaceName;
    }

    /**
     * Sets the value of the lagInterfaceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLAGInterfaceName(String value) {
        this.lagInterfaceName = value;
    }

    /**
     * Gets the value of the lagMember property.
     * 
     * @return
     *     possible object is
     *     {@link LAGMember }
     *     
     */
    public LAGMember getLAGMember() {
        return lagMember;
    }

    /**
     * Sets the value of the lagMember property.
     * 
     * @param value
     *     allowed object is
     *     {@link LAGMember }
     *     
     */
    public void setLAGMember(LAGMember value) {
        this.lagMember = value;
    }

    /**
     * Gets the value of the selectionCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelectionCriteria() {
        return selectionCriteria;
    }

    /**
     * Sets the value of the selectionCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelectionCriteria(String value) {
        this.selectionCriteria = value;
    }

    /**
     * Gets the value of the shutdown property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShutdown() {
        return shutdown;
    }

    /**
     * Sets the value of the shutdown property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShutdown(String value) {
        this.shutdown = value;
    }

    /**
     * Gets the value of the portThreshold property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortThreshold() {
        return portThreshold;
    }

    /**
     * Sets the value of the portThreshold property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortThreshold(String value) {
        this.portThreshold = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the interfaceDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterfaceDescription() {
        return interfaceDescription;
    }

    /**
     * Sets the value of the interfaceDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterfaceDescription(String value) {
        this.interfaceDescription = value;
    }

    /**
     * Gets the value of the overrideDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOverrideDescription() {
        return overrideDescription;
    }

    /**
     * Sets the value of the overrideDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverrideDescription(String value) {
        this.overrideDescription = value;
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMode(String value) {
        this.mode = value;
    }

    /**
     * Gets the value of the encapsulation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncapsulation() {
        return encapsulation;
    }

    /**
     * Sets the value of the encapsulation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncapsulation(String value) {
        this.encapsulation = value;
    }

    /**
     * Gets the value of the zendDeviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZendDeviceType() {
        return zendDeviceType;
    }

    /**
     * Sets the value of the zendDeviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZendDeviceType(String value) {
        this.zendDeviceType = value;
    }

    /**
     * Gets the value of the huaweiSwitchLAGConfigParams property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiSwitchLagConfigParams }
     *     
     */
    public HuaweiSwitchLagConfigParams getHuaweiSwitchLAGConfigParams() {
        return huaweiSwitchLAGConfigParams;
    }

    /**
     * Sets the value of the huaweiSwitchLAGConfigParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiSwitchLagConfigParams }
     *     
     */
    public void setHuaweiSwitchLAGConfigParams(HuaweiSwitchLagConfigParams value) {
        this.huaweiSwitchLAGConfigParams = value;
    }

    /**
     * Gets the value of the ciscoRouterLAGConfigParams property.
     * 
     * @return
     *     possible object is
     *     {@link CISCOLAGConfigParams }
     *     
     */
    public CISCOLAGConfigParams getCISCORouterLAGConfigParams() {
        return ciscoRouterLAGConfigParams;
    }

    /**
     * Sets the value of the ciscoRouterLAGConfigParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link CISCOLAGConfigParams }
     *     
     */
    public void setCISCORouterLAGConfigParams(CISCOLAGConfigParams value) {
        this.ciscoRouterLAGConfigParams = value;
    }

    /**
     * Gets the value of the stpRootProtectionEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStpRootProtectionEnabled() {
        return stpRootProtectionEnabled;
    }

    /**
     * Sets the value of the stpRootProtectionEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStpRootProtectionEnabled(String value) {
        this.stpRootProtectionEnabled = value;
    }

    /**
     * Gets the value of the stpBpduFilterEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStpBpduFilterEnabled() {
        return stpBpduFilterEnabled;
    }

    /**
     * Sets the value of the stpBpduFilterEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStpBpduFilterEnabled(String value) {
        this.stpBpduFilterEnabled = value;
    }

}
