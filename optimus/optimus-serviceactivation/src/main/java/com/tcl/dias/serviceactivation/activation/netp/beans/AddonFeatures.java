
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddonFeatures complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddonFeatures">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hostedExchangeCEConfig" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}HostedExchangeCEConfig" minOccurs="0"/>
 *         &lt;element name="isTelepresenceEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isITIMSNATConfigEnabled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NNICPEConfig" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}RemoteSite" minOccurs="0"/>
 *         &lt;element name="vsatCPEConfig" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}RemoteSite" minOccurs="0"/>
 *         &lt;element name="multicasting" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}Multicasting" minOccurs="0"/>
 *         &lt;element name="SLARWConfig" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_06}SLARWConfig" minOccurs="0"/>
 *         &lt;element name="IZO_Details" type="{http://IZO_Priv_25Oct17}IZO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddonFeatures", propOrder = {
    "hostedExchangeCEConfig",
    "isTelepresenceEnabled",
    "isITIMSNATConfigEnabled",
    "nnicpeConfig",
    "vsatCPEConfig",
    "multicasting",
    "slarwConfig",
    "izoDetails"
})
public class AddonFeatures {

    protected HostedExchangeCEConfig hostedExchangeCEConfig;
    protected String isTelepresenceEnabled;
    protected String isITIMSNATConfigEnabled;
    @XmlElement(name = "NNICPEConfig")
    protected RemoteSite nnicpeConfig;
    protected RemoteSite vsatCPEConfig;
    protected Multicasting multicasting;
    @XmlElement(name = "SLARWConfig")
    protected SLARWConfig slarwConfig;
    @XmlElement(name = "IZO_Details")
    protected IZO izoDetails;

    /**
     * Gets the value of the hostedExchangeCEConfig property.
     * 
     * @return
     *     possible object is
     *     {@link HostedExchangeCEConfig }
     *     
     */
    public HostedExchangeCEConfig getHostedExchangeCEConfig() {
        return hostedExchangeCEConfig;
    }

    /**
     * Sets the value of the hostedExchangeCEConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link HostedExchangeCEConfig }
     *     
     */
    public void setHostedExchangeCEConfig(HostedExchangeCEConfig value) {
        this.hostedExchangeCEConfig = value;
    }

    /**
     * Gets the value of the isTelepresenceEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsTelepresenceEnabled() {
        return isTelepresenceEnabled;
    }

    /**
     * Sets the value of the isTelepresenceEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsTelepresenceEnabled(String value) {
        this.isTelepresenceEnabled = value;
    }

    /**
     * Gets the value of the isITIMSNATConfigEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsITIMSNATConfigEnabled() {
        return isITIMSNATConfigEnabled;
    }

    /**
     * Sets the value of the isITIMSNATConfigEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsITIMSNATConfigEnabled(String value) {
        this.isITIMSNATConfigEnabled = value;
    }

    /**
     * Gets the value of the nnicpeConfig property.
     * 
     * @return
     *     possible object is
     *     {@link RemoteSite }
     *     
     */
    public RemoteSite getNNICPEConfig() {
        return nnicpeConfig;
    }

    /**
     * Sets the value of the nnicpeConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteSite }
     *     
     */
    public void setNNICPEConfig(RemoteSite value) {
        this.nnicpeConfig = value;
    }

    /**
     * Gets the value of the vsatCPEConfig property.
     * 
     * @return
     *     possible object is
     *     {@link RemoteSite }
     *     
     */
    public RemoteSite getVsatCPEConfig() {
        return vsatCPEConfig;
    }

    /**
     * Sets the value of the vsatCPEConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteSite }
     *     
     */
    public void setVsatCPEConfig(RemoteSite value) {
        this.vsatCPEConfig = value;
    }

    /**
     * Gets the value of the multicasting property.
     * 
     * @return
     *     possible object is
     *     {@link Multicasting }
     *     
     */
    public Multicasting getMulticasting() {
        return multicasting;
    }

    /**
     * Sets the value of the multicasting property.
     * 
     * @param value
     *     allowed object is
     *     {@link Multicasting }
     *     
     */
    public void setMulticasting(Multicasting value) {
        this.multicasting = value;
    }

    /**
     * Gets the value of the slarwConfig property.
     * 
     * @return
     *     possible object is
     *     {@link SLARWConfig }
     *     
     */
    public SLARWConfig getSLARWConfig() {
        return slarwConfig;
    }

    /**
     * Sets the value of the slarwConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLARWConfig }
     *     
     */
    public void setSLARWConfig(SLARWConfig value) {
        this.slarwConfig = value;
    }

    /**
     * Gets the value of the izoDetails property.
     * 
     * @return
     *     possible object is
     *     {@link IZO }
     *     
     */
    public IZO getIZODetails() {
        return izoDetails;
    }

    /**
     * Sets the value of the izoDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link IZO }
     *     
     */
    public void setIZODetails(IZO value) {
        this.izoDetails = value;
    }

}
