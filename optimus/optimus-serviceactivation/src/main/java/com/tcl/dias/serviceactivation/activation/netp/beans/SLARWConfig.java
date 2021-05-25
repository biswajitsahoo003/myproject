
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SLARWConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLARWConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="remoteCPE" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}RemoteSite" minOccurs="0"/>
 *         &lt;element name="cpeMode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="SINGLE"/>
 *               &lt;enumeration value="DUAL"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="peWANIPPool" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="srpWANIPPool" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="oldsrpWANIPPool" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="mCPEWANIPPool" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="oldmCPEWANIPPool" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="isFirstSite" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "SLARWConfig", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_06", propOrder = {
    "remoteCPE",
    "cpeMode",
    "peWANIPPool",
    "srpWANIPPool",
    "oldsrpWANIPPool",
    "mcpewanipPool",
    "oldmCPEWANIPPool",
    "isFirstSite",
    "isObjectInstanceModified"
})
public class SLARWConfig {

    protected RemoteSite remoteCPE;
    protected String cpeMode;
    protected IPV4Address peWANIPPool;
    protected IPV4Address srpWANIPPool;
    protected IPV4Address oldsrpWANIPPool;
    @XmlElement(name = "mCPEWANIPPool")
    protected IPV4Address mcpewanipPool;
    protected IPV4Address oldmCPEWANIPPool;
    protected Boolean isFirstSite;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the remoteCPE property.
     * 
     * @return
     *     possible object is
     *     {@link RemoteSite }
     *     
     */
    public RemoteSite getRemoteCPE() {
        return remoteCPE;
    }

    /**
     * Sets the value of the remoteCPE property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteSite }
     *     
     */
    public void setRemoteCPE(RemoteSite value) {
        this.remoteCPE = value;
    }

    /**
     * Gets the value of the cpeMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCpeMode() {
        return cpeMode;
    }

    /**
     * Sets the value of the cpeMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCpeMode(String value) {
        this.cpeMode = value;
    }

    /**
     * Gets the value of the peWANIPPool property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getPeWANIPPool() {
        return peWANIPPool;
    }

    /**
     * Sets the value of the peWANIPPool property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setPeWANIPPool(IPV4Address value) {
        this.peWANIPPool = value;
    }

    /**
     * Gets the value of the srpWANIPPool property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getSrpWANIPPool() {
        return srpWANIPPool;
    }

    /**
     * Sets the value of the srpWANIPPool property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setSrpWANIPPool(IPV4Address value) {
        this.srpWANIPPool = value;
    }

    /**
     * Gets the value of the oldsrpWANIPPool property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getOldsrpWANIPPool() {
        return oldsrpWANIPPool;
    }

    /**
     * Sets the value of the oldsrpWANIPPool property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setOldsrpWANIPPool(IPV4Address value) {
        this.oldsrpWANIPPool = value;
    }

    /**
     * Gets the value of the mcpewanipPool property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getMCPEWANIPPool() {
        return mcpewanipPool;
    }

    /**
     * Sets the value of the mcpewanipPool property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setMCPEWANIPPool(IPV4Address value) {
        this.mcpewanipPool = value;
    }

    /**
     * Gets the value of the oldmCPEWANIPPool property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getOldmCPEWANIPPool() {
        return oldmCPEWANIPPool;
    }

    /**
     * Sets the value of the oldmCPEWANIPPool property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setOldmCPEWANIPPool(IPV4Address value) {
        this.oldmCPEWANIPPool = value;
    }

    /**
     * Gets the value of the isFirstSite property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFirstSite() {
        return isFirstSite;
    }

    /**
     * Sets the value of the isFirstSite property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFirstSite(Boolean value) {
        this.isFirstSite = value;
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
