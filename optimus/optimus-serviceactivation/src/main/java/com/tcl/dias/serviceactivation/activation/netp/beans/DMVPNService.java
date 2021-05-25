
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DMVPNService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DMVPNService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}GVPNService">
 *       &lt;sequence>
 *         &lt;element name="lnsConfigOption" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="peLNSPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PE-LNS-Path" minOccurs="0"/>
 *                   &lt;element name="peLNSLoopbackPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_06}PELNSLoopbackPath" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CustomerPublicIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="LNSPublicIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="PESideConfig" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}DMVPNPESideConfig" minOccurs="0"/>
 *         &lt;element name="CESideConfig" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}DMVPNCESideConfig" minOccurs="0"/>
 *         &lt;element name="cryptoKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DMVPNService", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_02", propOrder = {
    "lnsConfigOption",
    "customerPublicIP",
    "lnsPublicIP",
    "peSideConfig",
    "ceSideConfig",
    "cryptoKey"
})
public class DMVPNService
    extends GVPNService
{

    protected DMVPNService.LnsConfigOption lnsConfigOption;
    @XmlElement(name = "CustomerPublicIP")
    protected IPV4Address customerPublicIP;
    @XmlElement(name = "LNSPublicIP")
    protected IPV4Address lnsPublicIP;
    @XmlElement(name = "PESideConfig")
    protected DMVPNPESideConfig peSideConfig;
    @XmlElement(name = "CESideConfig")
    protected DMVPNCESideConfig ceSideConfig;
    protected String cryptoKey;

    /**
     * Gets the value of the lnsConfigOption property.
     * 
     * @return
     *     possible object is
     *     {@link DMVPNService.LnsConfigOption }
     *     
     */
    public DMVPNService.LnsConfigOption getLnsConfigOption() {
        return lnsConfigOption;
    }

    /**
     * Sets the value of the lnsConfigOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link DMVPNService.LnsConfigOption }
     *     
     */
    public void setLnsConfigOption(DMVPNService.LnsConfigOption value) {
        this.lnsConfigOption = value;
    }

    /**
     * Gets the value of the customerPublicIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getCustomerPublicIP() {
        return customerPublicIP;
    }

    /**
     * Sets the value of the customerPublicIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setCustomerPublicIP(IPV4Address value) {
        this.customerPublicIP = value;
    }

    /**
     * Gets the value of the lnsPublicIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getLNSPublicIP() {
        return lnsPublicIP;
    }

    /**
     * Sets the value of the lnsPublicIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setLNSPublicIP(IPV4Address value) {
        this.lnsPublicIP = value;
    }

    /**
     * Gets the value of the peSideConfig property.
     * 
     * @return
     *     possible object is
     *     {@link DMVPNPESideConfig }
     *     
     */
    public DMVPNPESideConfig getPESideConfig() {
        return peSideConfig;
    }

    /**
     * Sets the value of the peSideConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link DMVPNPESideConfig }
     *     
     */
    public void setPESideConfig(DMVPNPESideConfig value) {
        this.peSideConfig = value;
    }

    /**
     * Gets the value of the ceSideConfig property.
     * 
     * @return
     *     possible object is
     *     {@link DMVPNCESideConfig }
     *     
     */
    public DMVPNCESideConfig getCESideConfig() {
        return ceSideConfig;
    }

    /**
     * Sets the value of the ceSideConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link DMVPNCESideConfig }
     *     
     */
    public void setCESideConfig(DMVPNCESideConfig value) {
        this.ceSideConfig = value;
    }

    /**
     * Gets the value of the cryptoKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCryptoKey() {
        return cryptoKey;
    }

    /**
     * Sets the value of the cryptoKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCryptoKey(String value) {
        this.cryptoKey = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="peLNSPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PE-LNS-Path" minOccurs="0"/>
     *         &lt;element name="peLNSLoopbackPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_06}PELNSLoopbackPath" minOccurs="0"/>
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
        "peLNSPath",
        "peLNSLoopbackPath"
    })
    public static class LnsConfigOption {

        protected PELNSPath peLNSPath;
        protected PELNSLoopbackPath peLNSLoopbackPath;

        /**
         * Gets the value of the peLNSPath property.
         * 
         * @return
         *     possible object is
         *     {@link PELNSPath }
         *     
         */
        public PELNSPath getPeLNSPath() {
            return peLNSPath;
        }

        /**
         * Sets the value of the peLNSPath property.
         * 
         * @param value
         *     allowed object is
         *     {@link PELNSPath }
         *     
         */
        public void setPeLNSPath(PELNSPath value) {
            this.peLNSPath = value;
        }

        /**
         * Gets the value of the peLNSLoopbackPath property.
         * 
         * @return
         *     possible object is
         *     {@link PELNSLoopbackPath }
         *     
         */
        public PELNSLoopbackPath getPeLNSLoopbackPath() {
            return peLNSLoopbackPath;
        }

        /**
         * Sets the value of the peLNSLoopbackPath property.
         * 
         * @param value
         *     allowed object is
         *     {@link PELNSLoopbackPath }
         *     
         */
        public void setPeLNSLoopbackPath(PELNSLoopbackPath value) {
            this.peLNSLoopbackPath = value;
        }

    }

}
