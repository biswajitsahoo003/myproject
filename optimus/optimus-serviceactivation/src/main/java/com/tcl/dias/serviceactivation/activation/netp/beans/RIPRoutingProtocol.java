
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RIPRoutingProtocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RIPRoutingProtocol">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}RoutingProtocol">
 *       &lt;sequence>
 *         &lt;element name="version" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="v1"/>
 *               &lt;enumeration value="v2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="redistributionRoutemapName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
 *         &lt;element name="LocalPreference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groupName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RIPNeighborOutboundRoutingPolicy" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" minOccurs="0"/>
 *         &lt;element name="RIPNeighborOutboundRoutingPolicyV6" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" minOccurs="0"/>
 *         &lt;element name="isOriginateDefaultEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ALUDefaultOriginateConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUDefaultOriginateConfig" minOccurs="0"/>
 *         &lt;element name="localPreferenceV6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "RIPRoutingProtocol", propOrder = {
    "version",
    "redistributionRoutemapName",
    "isRedistributeConnectedEnabled",
    "isRedistributeStaticEnabled",
    "localPreference",
    "groupName",
    "ripNeighborOutboundRoutingPolicy",
    "ripNeighborOutboundRoutingPolicyV6",
    "isOriginateDefaultEnabled",
    "aluDefaultOriginateConfig",
    "localPreferenceV6",
    "aluDefaultOriginateV6Config"
})
public class RIPRoutingProtocol
    extends RoutingProtocol
{

    protected String version;
    protected String redistributionRoutemapName;
    protected String isRedistributeConnectedEnabled;
    protected String isRedistributeStaticEnabled;
    @XmlElement(name = "LocalPreference")
    protected String localPreference;
    protected String groupName;
    @XmlElement(name = "RIPNeighborOutboundRoutingPolicy")
    protected RoutingPolicy ripNeighborOutboundRoutingPolicy;
    @XmlElement(name = "RIPNeighborOutboundRoutingPolicyV6")
    protected RoutingPolicy ripNeighborOutboundRoutingPolicyV6;
    protected Boolean isOriginateDefaultEnabled;
    @XmlElement(name = "ALUDefaultOriginateConfig")
    protected ALUDefaultOriginateConfig aluDefaultOriginateConfig;
    protected String localPreferenceV6;
    @XmlElement(name = "ALUDefaultOriginateV6Config")
    protected ALUDefaultOriginateConfig aluDefaultOriginateV6Config;

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
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the ripNeighborOutboundRoutingPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingPolicy }
     *     
     */
    public RoutingPolicy getRIPNeighborOutboundRoutingPolicy() {
        return ripNeighborOutboundRoutingPolicy;
    }

    /**
     * Sets the value of the ripNeighborOutboundRoutingPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingPolicy }
     *     
     */
    public void setRIPNeighborOutboundRoutingPolicy(RoutingPolicy value) {
        this.ripNeighborOutboundRoutingPolicy = value;
    }

    /**
     * Gets the value of the ripNeighborOutboundRoutingPolicyV6 property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingPolicy }
     *     
     */
    public RoutingPolicy getRIPNeighborOutboundRoutingPolicyV6() {
        return ripNeighborOutboundRoutingPolicyV6;
    }

    /**
     * Sets the value of the ripNeighborOutboundRoutingPolicyV6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingPolicy }
     *     
     */
    public void setRIPNeighborOutboundRoutingPolicyV6(RoutingPolicy value) {
        this.ripNeighborOutboundRoutingPolicyV6 = value;
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

}
