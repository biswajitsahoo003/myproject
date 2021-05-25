
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EthernetInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EthernetInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}Interface">
 *       &lt;sequence>
 *         &lt;element name="duplex" type="{http://www.tcl.com/2011/11/ace/common/xsd}Duplex" minOccurs="0"/>
 *         &lt;element name="speed" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="1000"/>
 *               &lt;enumeration value="100"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="NO_NEGOTIATE"/>
 *               &lt;enumeration value="AUTO"/>
 *               &lt;enumeration value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isAutoNegotiation" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="LIMITED"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="qosLoopinPassthroughInterface" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vlan" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;whiteSpace value="collapse"/>
 *               &lt;enumeration value="TRUNK"/>
 *               &lt;enumeration value="ACCESS"/>
 *               &lt;enumeration value="TUNNEL"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="HYBRID"/>
 *               &lt;enumeration value="NETWORK"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="mediaType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="OPTICAL"/>
 *               &lt;enumeration value="ELECTRICAL"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="portType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="FE"/>
 *               &lt;enumeration value="GIGE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="encapsulation" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="DOT1Q"/>
 *               &lt;enumeration value="NULL"/>
 *               &lt;enumeration value="QnQ"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="svlan" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="handOff" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mtu" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="holdTimeUp" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="holdTimeDown" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="framing" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="LAN-PHY"/>
 *               &lt;enumeration value="WAN-PHY"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="maxMacLimit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bfdConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}BFDConfig" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EthernetInterface", propOrder = {
    "duplex",
    "speed",
    "isAutoNegotiation",
    "qosLoopinPassthroughInterface",
    "vlan",
    "mode",
    "mediaType",
    "portType",
    "encapsulation",
    "svlan",
    "handOff",
    "mtu",
    "holdTimeUp",
    "holdTimeDown",
    "framing",
    "maxMacLimit",
    "bfdConfig",
    "syncVLAN"
})
public class EthernetInterface
    extends Interface
{

    @XmlSchemaType(name = "string")
    protected Duplex duplex;
    protected String speed;
    protected String isAutoNegotiation;
    protected String qosLoopinPassthroughInterface;
    protected Integer vlan;
    @XmlElement(defaultValue = "NOT_APPLICABLE")
    protected String mode;
    protected String mediaType;
    protected String portType;
    protected String encapsulation;
    protected Integer svlan;
    protected String handOff;
    protected Integer mtu;
    protected Integer holdTimeUp;
    protected Integer holdTimeDown;
    protected String framing;
    protected String maxMacLimit;
    protected BFDConfig bfdConfig;
    protected Boolean syncVLAN;

    /**
     * Gets the value of the duplex property.
     * 
     * @return
     *     possible object is
     *     {@link Duplex }
     *     
     */
    public Duplex getDuplex() {
        return duplex;
    }

    /**
     * Sets the value of the duplex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duplex }
     *     
     */
    public void setDuplex(Duplex value) {
        this.duplex = value;
    }

    /**
     * Gets the value of the speed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * Sets the value of the speed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeed(String value) {
        this.speed = value;
    }

    /**
     * Gets the value of the isAutoNegotiation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsAutoNegotiation() {
        return isAutoNegotiation;
    }

    /**
     * Sets the value of the isAutoNegotiation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsAutoNegotiation(String value) {
        this.isAutoNegotiation = value;
    }

    /**
     * Gets the value of the qosLoopinPassthroughInterface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQosLoopinPassthroughInterface() {
        return qosLoopinPassthroughInterface;
    }

    /**
     * Sets the value of the qosLoopinPassthroughInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQosLoopinPassthroughInterface(String value) {
        this.qosLoopinPassthroughInterface = value;
    }

    /**
     * Gets the value of the vlan property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVlan() {
        return vlan;
    }

    /**
     * Sets the value of the vlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVlan(Integer value) {
        this.vlan = value;
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
     * Gets the value of the mediaType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Sets the value of the mediaType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMediaType(String value) {
        this.mediaType = value;
    }

    /**
     * Gets the value of the portType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortType() {
        return portType;
    }

    /**
     * Sets the value of the portType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortType(String value) {
        this.portType = value;
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
     * Gets the value of the svlan property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSvlan() {
        return svlan;
    }

    /**
     * Sets the value of the svlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSvlan(Integer value) {
        this.svlan = value;
    }

    /**
     * Gets the value of the handOff property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHandOff() {
        return handOff;
    }

    /**
     * Sets the value of the handOff property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHandOff(String value) {
        this.handOff = value;
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
     * Gets the value of the holdTimeUp property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHoldTimeUp() {
        return holdTimeUp;
    }

    /**
     * Sets the value of the holdTimeUp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHoldTimeUp(Integer value) {
        this.holdTimeUp = value;
    }

    /**
     * Gets the value of the holdTimeDown property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHoldTimeDown() {
        return holdTimeDown;
    }

    /**
     * Sets the value of the holdTimeDown property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHoldTimeDown(Integer value) {
        this.holdTimeDown = value;
    }

    /**
     * Gets the value of the framing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFraming() {
        return framing;
    }

    /**
     * Sets the value of the framing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFraming(String value) {
        this.framing = value;
    }

    /**
     * Gets the value of the maxMacLimit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxMacLimit() {
        return maxMacLimit;
    }

    /**
     * Sets the value of the maxMacLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxMacLimit(String value) {
        this.maxMacLimit = value;
    }

    /**
     * Gets the value of the bfdConfig property.
     * 
     * @return
     *     possible object is
     *     {@link BFDConfig }
     *     
     */
    public BFDConfig getBfdConfig() {
        return bfdConfig;
    }

    /**
     * Sets the value of the bfdConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link BFDConfig }
     *     
     */
    public void setBfdConfig(BFDConfig value) {
        this.bfdConfig = value;
    }

	public Boolean getSyncVLAN() {
		return syncVLAN;
	}

	public void setSyncVLAN(Boolean syncVLAN) {
		this.syncVLAN = syncVLAN;
	}

}
