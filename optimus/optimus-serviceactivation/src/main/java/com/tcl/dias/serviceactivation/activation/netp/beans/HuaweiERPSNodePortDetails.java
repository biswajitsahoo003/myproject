
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HuaweiERPSNodePortDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HuaweiERPSNodePortDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nodeType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="OTHER"/>
 *               &lt;enumeration value="WIMAX"/>
 *               &lt;enumeration value="HUAWEI"/>
 *               &lt;enumeration value="NNI"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PortDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RPLStatus" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="NEIGHBOUR"/>
 *               &lt;enumeration value="DISABLE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="OWNER"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CFMMIPLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="overrideDescription" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deviceRole" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="MAJOR"/>
 *               &lt;enumeration value="MEGA"/>
 *               &lt;enumeration value="ACCESS"/>
 *               &lt;enumeration value="AGGR"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isAutoNegotiationEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="speed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="duplex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ACLName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trafficPriority" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="VLANID" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiVLANs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="inboundCIR" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="outboundCIR" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="configureUplinkPort" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="attribute1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="huaweiCFMParams" type="{http://www.tcl.com/2014/5/ipsvc/xsd}HuaweiCFMParams" minOccurs="0"/>
 *         &lt;element name="remoteDeviceType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="HUAWEI"/>
 *               &lt;enumeration value="NNI"/>
 *               &lt;enumeration value="WIMAX"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
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
@XmlType(name = "HuaweiERPSNodePortDetails", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "nodeName",
    "nodeType",
    "portName",
    "portDescription",
    "rplStatus",
    "cfmmipLevel",
    "overrideDescription",
    "deviceRole",
    "cityName",
    "isAutoNegotiationEnabled",
    "speed",
    "duplex",
    "aclName",
    "trafficPriority",
    "vlanid",
    "inboundCIR",
    "outboundCIR",
    "configureUplinkPort",
    "attribute1",
    "attribute2",
    "isObjectInstanceModified",
    "huaweiCFMParams",
    "remoteDeviceType"
})
public class HuaweiERPSNodePortDetails {

    protected String nodeName;
    protected String nodeType;
    @XmlElement(name = "PortName")
    protected String portName;
    @XmlElement(name = "PortDescription")
    protected String portDescription;
    @XmlElement(name = "RPLStatus")
    protected String rplStatus;
    @XmlElement(name = "CFMMIPLevel")
    protected String cfmmipLevel;
    protected String overrideDescription;
    protected String deviceRole;
    protected String cityName;
    protected String isAutoNegotiationEnabled;
    protected String speed;
    protected String duplex;
    @XmlElement(name = "ACLName")
    protected String aclName;
    protected Integer trafficPriority;
    @XmlElement(name = "VLANID")
    protected List<HuaweiVLANs> vlanid;
    protected Bandwidth inboundCIR;
    protected Bandwidth outboundCIR;
    protected String configureUplinkPort;
    protected String attribute1;
    protected String attribute2;
    protected Boolean isObjectInstanceModified;
    protected HuaweiCFMParams huaweiCFMParams;
    protected String remoteDeviceType;

    /**
     * Gets the value of the nodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets the value of the nodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeName(String value) {
        this.nodeName = value;
    }

    /**
     * Gets the value of the nodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * Sets the value of the nodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeType(String value) {
        this.nodeType = value;
    }

    /**
     * Gets the value of the portName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortName() {
        return portName;
    }

    /**
     * Sets the value of the portName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortName(String value) {
        this.portName = value;
    }

    /**
     * Gets the value of the portDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortDescription() {
        return portDescription;
    }

    /**
     * Sets the value of the portDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortDescription(String value) {
        this.portDescription = value;
    }

    /**
     * Gets the value of the rplStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRPLStatus() {
        return rplStatus;
    }

    /**
     * Sets the value of the rplStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRPLStatus(String value) {
        this.rplStatus = value;
    }

    /**
     * Gets the value of the cfmmipLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCFMMIPLevel() {
        return cfmmipLevel;
    }

    /**
     * Sets the value of the cfmmipLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCFMMIPLevel(String value) {
        this.cfmmipLevel = value;
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
     * Gets the value of the deviceRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceRole() {
        return deviceRole;
    }

    /**
     * Sets the value of the deviceRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceRole(String value) {
        this.deviceRole = value;
    }

    /**
     * Gets the value of the cityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Sets the value of the cityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityName(String value) {
        this.cityName = value;
    }

    /**
     * Gets the value of the isAutoNegotiationEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsAutoNegotiationEnabled() {
        return isAutoNegotiationEnabled;
    }

    /**
     * Sets the value of the isAutoNegotiationEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsAutoNegotiationEnabled(String value) {
        this.isAutoNegotiationEnabled = value;
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
     * Gets the value of the duplex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDuplex() {
        return duplex;
    }

    /**
     * Sets the value of the duplex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDuplex(String value) {
        this.duplex = value;
    }

    /**
     * Gets the value of the aclName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACLName() {
        return aclName;
    }

    /**
     * Sets the value of the aclName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACLName(String value) {
        this.aclName = value;
    }

    /**
     * Gets the value of the trafficPriority property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTrafficPriority() {
        return trafficPriority;
    }

    /**
     * Sets the value of the trafficPriority property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTrafficPriority(Integer value) {
        this.trafficPriority = value;
    }

    /**
     * Gets the value of the vlanid property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vlanid property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVLANID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HuaweiVLANs }
     * 
     * 
     */
    public List<HuaweiVLANs> getVLANID() {
        if (vlanid == null) {
            vlanid = new ArrayList<HuaweiVLANs>();
        }
        return this.vlanid;
    }

    /**
     * Gets the value of the inboundCIR property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getInboundCIR() {
        return inboundCIR;
    }

    /**
     * Sets the value of the inboundCIR property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setInboundCIR(Bandwidth value) {
        this.inboundCIR = value;
    }

    /**
     * Gets the value of the outboundCIR property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getOutboundCIR() {
        return outboundCIR;
    }

    /**
     * Sets the value of the outboundCIR property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setOutboundCIR(Bandwidth value) {
        this.outboundCIR = value;
    }

    /**
     * Gets the value of the configureUplinkPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfigureUplinkPort() {
        return configureUplinkPort;
    }

    /**
     * Sets the value of the configureUplinkPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfigureUplinkPort(String value) {
        this.configureUplinkPort = value;
    }

    /**
     * Gets the value of the attribute1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute1() {
        return attribute1;
    }

    /**
     * Sets the value of the attribute1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute1(String value) {
        this.attribute1 = value;
    }

    /**
     * Gets the value of the attribute2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute2() {
        return attribute2;
    }

    /**
     * Sets the value of the attribute2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute2(String value) {
        this.attribute2 = value;
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

    /**
     * Gets the value of the huaweiCFMParams property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiCFMParams }
     *     
     */
    public HuaweiCFMParams getHuaweiCFMParams() {
        return huaweiCFMParams;
    }

    /**
     * Sets the value of the huaweiCFMParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiCFMParams }
     *     
     */
    public void setHuaweiCFMParams(HuaweiCFMParams value) {
        this.huaweiCFMParams = value;
    }

    /**
     * Gets the value of the remoteDeviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteDeviceType() {
        return remoteDeviceType;
    }

    /**
     * Sets the value of the remoteDeviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteDeviceType(String value) {
        this.remoteDeviceType = value;
    }

}
