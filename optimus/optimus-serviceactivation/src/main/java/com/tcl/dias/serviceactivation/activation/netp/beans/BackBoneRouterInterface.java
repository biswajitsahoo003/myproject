
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for backBoneRouterInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="backBoneRouterInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="v4IPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MTU" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="IPPIMMode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="SPARSE"/>
 *               &lt;enumeration value="DENSE"/>
 *               &lt;enumeration value="SPARSE-DENSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="loadInterval" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="isMPLSIPEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="outboundQoSPolicyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "backBoneRouterInterface", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2013/_01", propOrder = {
    "name",
    "v4IPAddress",
    "description",
    "mtu",
    "ippimMode",
    "loadInterval",
    "isMPLSIPEnabled",
    "outboundQoSPolicyName",
    "isObjectInstanceModified"
})
@XmlSeeAlso({
    BackBoneEthernetInterface.class,
    BackBonePOSInterface.class,
    BackBoneSerialInterface.class
})
public class BackBoneRouterInterface {

    protected String name;
    protected IPV4Address v4IPAddress;
    protected String description;
    @XmlElement(name = "MTU")
    protected Integer mtu;
    @XmlElement(name = "IPPIMMode")
    protected String ippimMode;
    protected Integer loadInterval;
    protected String isMPLSIPEnabled;
    protected String outboundQoSPolicyName;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the v4IPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getV4IPAddress() {
        return v4IPAddress;
    }

    /**
     * Sets the value of the v4IPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setV4IPAddress(IPV4Address value) {
        this.v4IPAddress = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the mtu property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMTU() {
        return mtu;
    }

    /**
     * Sets the value of the mtu property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMTU(Integer value) {
        this.mtu = value;
    }

    /**
     * Gets the value of the ippimMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPPIMMode() {
        return ippimMode;
    }

    /**
     * Sets the value of the ippimMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPPIMMode(String value) {
        this.ippimMode = value;
    }

    /**
     * Gets the value of the loadInterval property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLoadInterval() {
        return loadInterval;
    }

    /**
     * Sets the value of the loadInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLoadInterval(Integer value) {
        this.loadInterval = value;
    }

    /**
     * Gets the value of the isMPLSIPEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsMPLSIPEnabled() {
        return isMPLSIPEnabled;
    }

    /**
     * Sets the value of the isMPLSIPEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsMPLSIPEnabled(String value) {
        this.isMPLSIPEnabled = value;
    }

    /**
     * Gets the value of the outboundQoSPolicyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutboundQoSPolicyName() {
        return outboundQoSPolicyName;
    }

    /**
     * Sets the value of the outboundQoSPolicyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutboundQoSPolicyName(String value) {
        this.outboundQoSPolicyName = value;
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
