
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManagedElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManagedElement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nodeDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nodeDefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="emsName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="networkName" type="{http://www.tcl.com/2011/11/netordsvc/xsd}NetworkName" minOccurs="0"/>
 *         &lt;element name="mgmtIPAddress" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
 *                   &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="os" type="{http://www.tcl.com/2011/11/ace/common/xsd}OperatingSystem" minOccurs="0"/>
 *         &lt;element name="deviceAccessProtocol" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="SSH"/>
 *               &lt;enumeration value="TELNET"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cityCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="managementPort" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="HuaweiSwitchParams" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiSwitchParams" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManagedElement", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "name",
    "nodeType",
    "nodeDef",
    "nodeDefId",
    "emsName",
    "networkName",
    "mgmtIPAddress",
    "os",
    "deviceAccessProtocol",
    "cityCode",
    "cityName",
    "managementPort",
    "huaweiSwitchParams"
})
public class ManagedElement {

    protected String name;
    protected String nodeType;
    protected String nodeDef;
    protected String nodeDefId;
    protected String emsName;
    protected NetworkName networkName;
    protected ManagedElement.MgmtIPAddress mgmtIPAddress;
    protected OperatingSystem os;
    @XmlElement(defaultValue = "NOT_APPLICABLE")
    protected String deviceAccessProtocol;
    protected String cityCode;
    protected String cityName;
    protected Integer managementPort;
    @XmlElement(name = "HuaweiSwitchParams")
    protected HuaweiSwitchParams huaweiSwitchParams;

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
     * Gets the value of the nodeDef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeDef() {
        return nodeDef;
    }

    /**
     * Sets the value of the nodeDef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeDef(String value) {
        this.nodeDef = value;
    }

    /**
     * Gets the value of the nodeDefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeDefId() {
        return nodeDefId;
    }

    /**
     * Sets the value of the nodeDefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeDefId(String value) {
        this.nodeDefId = value;
    }

    /**
     * Gets the value of the emsName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmsName() {
        return emsName;
    }

    /**
     * Sets the value of the emsName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmsName(String value) {
        this.emsName = value;
    }

    /**
     * Gets the value of the networkName property.
     * 
     * @return
     *     possible object is
     *     {@link NetworkName }
     *     
     */
    public NetworkName getNetworkName() {
        return networkName;
    }

    /**
     * Sets the value of the networkName property.
     * 
     * @param value
     *     allowed object is
     *     {@link NetworkName }
     *     
     */
    public void setNetworkName(NetworkName value) {
        this.networkName = value;
    }

    /**
     * Gets the value of the mgmtIPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link ManagedElement.MgmtIPAddress }
     *     
     */
    public ManagedElement.MgmtIPAddress getMgmtIPAddress() {
        return mgmtIPAddress;
    }

    /**
     * Sets the value of the mgmtIPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManagedElement.MgmtIPAddress }
     *     
     */
    public void setMgmtIPAddress(ManagedElement.MgmtIPAddress value) {
        this.mgmtIPAddress = value;
    }

    /**
     * Gets the value of the os property.
     * 
     * @return
     *     possible object is
     *     {@link OperatingSystem }
     *     
     */
    public OperatingSystem getOs() {
        return os;
    }

    /**
     * Sets the value of the os property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperatingSystem }
     *     
     */
    public void setOs(OperatingSystem value) {
        this.os = value;
    }

    /**
     * Gets the value of the deviceAccessProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceAccessProtocol() {
        return deviceAccessProtocol;
    }

    /**
     * Sets the value of the deviceAccessProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceAccessProtocol(String value) {
        this.deviceAccessProtocol = value;
    }

    /**
     * Gets the value of the cityCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * Sets the value of the cityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityCode(String value) {
        this.cityCode = value;
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
     * Gets the value of the managementPort property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getManagementPort() {
        return managementPort;
    }

    /**
     * Sets the value of the managementPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setManagementPort(Integer value) {
        this.managementPort = value;
    }

    /**
     * Gets the value of the huaweiSwitchParams property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiSwitchParams }
     *     
     */
    public HuaweiSwitchParams getHuaweiSwitchParams() {
        return huaweiSwitchParams;
    }

    /**
     * Sets the value of the huaweiSwitchParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiSwitchParams }
     *     
     */
    public void setHuaweiSwitchParams(HuaweiSwitchParams value) {
        this.huaweiSwitchParams = value;
    }


    /**
     * 
     * 							<!-- IP Address of EMS/NMS/Device -->
     * 						
     * 
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
     *         &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "v4Address",
        "v6Address"
    })
    public static class MgmtIPAddress {

        protected IPV4Address v4Address;
        protected IPV6Address v6Address;

        /**
         * Gets the value of the v4Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV4Address }
         *     
         */
        public IPV4Address getV4Address() {
            return v4Address;
        }

        /**
         * Sets the value of the v4Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV4Address }
         *     
         */
        public void setV4Address(IPV4Address value) {
            this.v4Address = value;
        }

        /**
         * Gets the value of the v6Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV6Address }
         *     
         */
        public IPV6Address getV6Address() {
            return v6Address;
        }

        /**
         * Sets the value of the v6Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV6Address }
         *     
         */
        public void setV6Address(IPV6Address value) {
            this.v6Address = value;
        }

    }

}
