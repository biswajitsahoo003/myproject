
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SerialInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SerialInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}Interface">
 *       &lt;sequence>
 *         &lt;element name="encapsulation" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="HDLC"/>
 *               &lt;enumeration value="PPP"/>
 *               &lt;enumeration value="MLPPP"/>
 *               &lt;enumeration value="FRAME_RELAY"/>
 *               &lt;enumeration value="AAL0"/>
 *               &lt;enumeration value="AAL5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isFramed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isCRC4Enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="crcSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="dlciValue" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="timeslot" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="channelGroupNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mlppBundleId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HDLCConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}HDLCConfig" minOccurs="0"/>
 *         &lt;element name="mode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ACCESS"/>
 *               &lt;enumeration value="HYBRID"/>
 *               &lt;enumeration value="NETWORK"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="portType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="framing" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="NO_CRC_G704"/>
 *               &lt;enumeration value="G704"/>
 *               &lt;enumeration value="E1_UNFRAMED"/>
 *               &lt;enumeration value="ESF"/>
 *               &lt;enumeration value="SF"/>
 *               &lt;enumeration value="DS1_UNFRAMED"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="mtu" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "SerialInterface", propOrder = {
    "encapsulation",
    "isFramed",
    "isCRC4Enabled",
    "crcSize",
    "dlciValue",
    "timeslot",
    "channelGroupNumber",
    "mlppBundleId",
    "hdlcConfig",
    "mode",
    "portType",
    "framing",
    "mtu",
    "bfdConfig"
})
public class SerialInterface
    extends Interface
{

    protected String encapsulation;
    protected Boolean isFramed;
    protected Boolean isCRC4Enabled;
    protected Integer crcSize;
    protected Integer dlciValue;
    protected List<String> timeslot;
    protected String channelGroupNumber;
    protected String mlppBundleId;
    @XmlElement(name = "HDLCConfig")
    protected HDLCConfig hdlcConfig;
    protected String mode;
    protected String portType;
    protected String framing;
    protected Integer mtu;
    protected BFDConfig bfdConfig;

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
     * Gets the value of the isFramed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFramed() {
        return isFramed;
    }

    /**
     * Sets the value of the isFramed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFramed(Boolean value) {
        this.isFramed = value;
    }

    /**
     * Gets the value of the isCRC4Enabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCRC4Enabled() {
        return isCRC4Enabled;
    }

    /**
     * Sets the value of the isCRC4Enabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCRC4Enabled(Boolean value) {
        this.isCRC4Enabled = value;
    }

    /**
     * Gets the value of the crcSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCrcSize() {
        return crcSize;
    }

    /**
     * Sets the value of the crcSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCrcSize(Integer value) {
        this.crcSize = value;
    }

    /**
     * Gets the value of the dlciValue property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDlciValue() {
        return dlciValue;
    }

    /**
     * Sets the value of the dlciValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDlciValue(Integer value) {
        this.dlciValue = value;
    }

    /**
     * Gets the value of the timeslot property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timeslot property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimeslot().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTimeslot() {
        if (timeslot == null) {
            timeslot = new ArrayList<String>();
        }
        return this.timeslot;
    }

    /**
     * Gets the value of the channelGroupNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelGroupNumber() {
        return channelGroupNumber;
    }

    /**
     * Sets the value of the channelGroupNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelGroupNumber(String value) {
        this.channelGroupNumber = value;
    }

    /**
     * Gets the value of the mlppBundleId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMlppBundleId() {
        return mlppBundleId;
    }

    /**
     * Sets the value of the mlppBundleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMlppBundleId(String value) {
        this.mlppBundleId = value;
    }

    /**
     * Gets the value of the hdlcConfig property.
     * 
     * @return
     *     possible object is
     *     {@link HDLCConfig }
     *     
     */
    public HDLCConfig getHDLCConfig() {
        return hdlcConfig;
    }

    /**
     * Sets the value of the hdlcConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link HDLCConfig }
     *     
     */
    public void setHDLCConfig(HDLCConfig value) {
        this.hdlcConfig = value;
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

}
