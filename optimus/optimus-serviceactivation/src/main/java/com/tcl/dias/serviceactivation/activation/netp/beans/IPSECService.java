
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPSECService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPSECService">
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
 *         &lt;element name="CustomerCryptoKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LNSPublicIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="peIPSECConfigProfile" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}IPSECConfigParam" minOccurs="0"/>
 *         &lt;element name="ceIPSECConfigProfile" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}IPSECConfigParam" minOccurs="0"/>
 *         &lt;element name="LNSWANStaticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *         &lt;element name="CEWANStaticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *         &lt;element name="CEWANBGPOutboundPrefixlist" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}PrefixList" minOccurs="0"/>
 *         &lt;element name="IPSECCEWANAdditionalNetworkPrefixes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPSECService", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_02", propOrder = {
    "lnsConfigOption",
    "customerPublicIP",
    "customerCryptoKey",
    "lnsPublicIP",
    "peIPSECConfigProfile",
    "ceIPSECConfigProfile",
    "lnswanStaticRoutes",
    "cewanStaticRoutes",
    "cewanbgpOutboundPrefixlist",
    "ipseccewanAdditionalNetworkPrefixes"
})
public class IPSECService
    extends GVPNService
{

    protected IPSECService.LnsConfigOption lnsConfigOption;
    @XmlElement(name = "CustomerPublicIP")
    protected IPV4Address customerPublicIP;
    @XmlElement(name = "CustomerCryptoKey")
    protected String customerCryptoKey;
    @XmlElement(name = "LNSPublicIP")
    protected IPV4Address lnsPublicIP;
    protected IPSECConfigParam peIPSECConfigProfile;
    protected IPSECConfigParam ceIPSECConfigProfile;
    @XmlElement(name = "LNSWANStaticRoutes")
    protected StaticRoutes lnswanStaticRoutes;
    @XmlElement(name = "CEWANStaticRoutes")
    protected StaticRoutes cewanStaticRoutes;
    @XmlElement(name = "CEWANBGPOutboundPrefixlist")
    protected PrefixList cewanbgpOutboundPrefixlist;
    @XmlElement(name = "IPSECCEWANAdditionalNetworkPrefixes")
    protected List<String> ipseccewanAdditionalNetworkPrefixes;

    /**
     * Gets the value of the lnsConfigOption property.
     * 
     * @return
     *     possible object is
     *     {@link IPSECService.LnsConfigOption }
     *     
     */
    public IPSECService.LnsConfigOption getLnsConfigOption() {
        return lnsConfigOption;
    }

    /**
     * Sets the value of the lnsConfigOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSECService.LnsConfigOption }
     *     
     */
    public void setLnsConfigOption(IPSECService.LnsConfigOption value) {
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
     * Gets the value of the customerCryptoKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerCryptoKey() {
        return customerCryptoKey;
    }

    /**
     * Sets the value of the customerCryptoKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerCryptoKey(String value) {
        this.customerCryptoKey = value;
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
     * Gets the value of the peIPSECConfigProfile property.
     * 
     * @return
     *     possible object is
     *     {@link IPSECConfigParam }
     *     
     */
    public IPSECConfigParam getPeIPSECConfigProfile() {
        return peIPSECConfigProfile;
    }

    /**
     * Sets the value of the peIPSECConfigProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSECConfigParam }
     *     
     */
    public void setPeIPSECConfigProfile(IPSECConfigParam value) {
        this.peIPSECConfigProfile = value;
    }

    /**
     * Gets the value of the ceIPSECConfigProfile property.
     * 
     * @return
     *     possible object is
     *     {@link IPSECConfigParam }
     *     
     */
    public IPSECConfigParam getCeIPSECConfigProfile() {
        return ceIPSECConfigProfile;
    }

    /**
     * Sets the value of the ceIPSECConfigProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSECConfigParam }
     *     
     */
    public void setCeIPSECConfigProfile(IPSECConfigParam value) {
        this.ceIPSECConfigProfile = value;
    }

    /**
     * Gets the value of the lnswanStaticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutes }
     *     
     */
    public StaticRoutes getLNSWANStaticRoutes() {
        return lnswanStaticRoutes;
    }

    /**
     * Sets the value of the lnswanStaticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutes }
     *     
     */
    public void setLNSWANStaticRoutes(StaticRoutes value) {
        this.lnswanStaticRoutes = value;
    }

    /**
     * Gets the value of the cewanStaticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutes }
     *     
     */
    public StaticRoutes getCEWANStaticRoutes() {
        return cewanStaticRoutes;
    }

    /**
     * Sets the value of the cewanStaticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutes }
     *     
     */
    public void setCEWANStaticRoutes(StaticRoutes value) {
        this.cewanStaticRoutes = value;
    }

    /**
     * Gets the value of the cewanbgpOutboundPrefixlist property.
     * 
     * @return
     *     possible object is
     *     {@link PrefixList }
     *     
     */
    public PrefixList getCEWANBGPOutboundPrefixlist() {
        return cewanbgpOutboundPrefixlist;
    }

    /**
     * Sets the value of the cewanbgpOutboundPrefixlist property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrefixList }
     *     
     */
    public void setCEWANBGPOutboundPrefixlist(PrefixList value) {
        this.cewanbgpOutboundPrefixlist = value;
    }

    /**
     * Gets the value of the ipseccewanAdditionalNetworkPrefixes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ipseccewanAdditionalNetworkPrefixes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIPSECCEWANAdditionalNetworkPrefixes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIPSECCEWANAdditionalNetworkPrefixes() {
        if (ipseccewanAdditionalNetworkPrefixes == null) {
            ipseccewanAdditionalNetworkPrefixes = new ArrayList<String>();
        }
        return this.ipseccewanAdditionalNetworkPrefixes;
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
