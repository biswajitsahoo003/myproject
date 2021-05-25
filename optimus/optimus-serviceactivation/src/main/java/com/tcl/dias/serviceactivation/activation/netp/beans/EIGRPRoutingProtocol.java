
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EIGRPRoutingProtocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EIGRPRoutingProtocol">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}RoutingProtocol">
 *       &lt;sequence>
 *         &lt;element name="remoteASNumber" type="{http://www.tcl.com/2011/11/ace/common/xsd}ASNumber" minOccurs="0"/>
 *         &lt;element name="localASNumber" type="{http://www.tcl.com/2011/11/ace/common/xsd}ASNumber" minOccurs="0"/>
 *         &lt;element name="isSOORequired" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
 *         &lt;element name="InterfaceDelay" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="reliability" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="load" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mtu" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="redistributionDelay" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="redistributionRoutemapName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LocalPreference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bandwidthinKBPS" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EIGRPRoutingProtocol", propOrder = {
    "remoteASNumber",
    "localASNumber",
    "isSOORequired",
    "isRedistributeConnectedEnabled",
    "isRedistributeStaticEnabled",
    "interfaceDelay",
    "reliability",
    "load",
    "mtu",
    "redistributionDelay",
    "redistributionRoutemapName",
    "localPreference",
    "bandwidthinKBPS"
})
public class EIGRPRoutingProtocol
    extends RoutingProtocol
{

    protected ASNumber remoteASNumber;
    protected ASNumber localASNumber;
    protected String isSOORequired;
    protected String isRedistributeConnectedEnabled;
    protected String isRedistributeStaticEnabled;
    @XmlElement(name = "InterfaceDelay")
    protected Integer interfaceDelay;
    protected Integer reliability;
    protected Integer load;
    protected Integer mtu;
    protected Integer redistributionDelay;
    protected String redistributionRoutemapName;
    @XmlElement(name = "LocalPreference")
    protected String localPreference;
    protected Bandwidth bandwidthinKBPS;

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
     * Gets the value of the localASNumber property.
     * 
     * @return
     *     possible object is
     *     {@link ASNumber }
     *     
     */
    public ASNumber getLocalASNumber() {
        return localASNumber;
    }

    /**
     * Sets the value of the localASNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link ASNumber }
     *     
     */
    public void setLocalASNumber(ASNumber value) {
        this.localASNumber = value;
    }

    /**
     * Gets the value of the isSOORequired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsSOORequired() {
        return isSOORequired;
    }

    /**
     * Sets the value of the isSOORequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsSOORequired(String value) {
        this.isSOORequired = value;
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
     * Gets the value of the interfaceDelay property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInterfaceDelay() {
        return interfaceDelay;
    }

    /**
     * Sets the value of the interfaceDelay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInterfaceDelay(Integer value) {
        this.interfaceDelay = value;
    }

    /**
     * Gets the value of the reliability property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReliability() {
        return reliability;
    }

    /**
     * Sets the value of the reliability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReliability(Integer value) {
        this.reliability = value;
    }

    /**
     * Gets the value of the load property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLoad() {
        return load;
    }

    /**
     * Sets the value of the load property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLoad(Integer value) {
        this.load = value;
    }

    /**
     * Gets the value of the mtu property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMtu() {
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
    public void setMtu(Integer value) {
        this.mtu = value;
    }

    /**
     * Gets the value of the redistributionDelay property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRedistributionDelay() {
        return redistributionDelay;
    }

    /**
     * Sets the value of the redistributionDelay property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRedistributionDelay(Integer value) {
        this.redistributionDelay = value;
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
     * Gets the value of the bandwidthinKBPS property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getBandwidthinKBPS() {
        return bandwidthinKBPS;
    }

    /**
     * Sets the value of the bandwidthinKBPS property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setBandwidthinKBPS(Bandwidth value) {
        this.bandwidthinKBPS = value;
    }

}
