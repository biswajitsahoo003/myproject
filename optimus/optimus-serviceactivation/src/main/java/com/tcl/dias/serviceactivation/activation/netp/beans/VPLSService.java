
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VPLSService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VPLSService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}P2PL2VPNService">
 *       &lt;sequence>
 *         &lt;element name="bridgeDomainVLAN" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="managementVLAN" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="VFIName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HubPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VPLSService", propOrder = {
    "bridgeDomainVLAN",
    "managementVLAN",
    "vfiName",
    "hubPE"
})
public class VPLSService
    extends P2PL2VPNService
{

    protected Integer bridgeDomainVLAN;
    protected Integer managementVLAN;
    @XmlElement(name = "VFIName")
    protected String vfiName;
    @XmlElement(name = "HubPE")
    protected String hubPE;

    /**
     * Gets the value of the bridgeDomainVLAN property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBridgeDomainVLAN() {
        return bridgeDomainVLAN;
    }

    /**
     * Sets the value of the bridgeDomainVLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBridgeDomainVLAN(Integer value) {
        this.bridgeDomainVLAN = value;
    }

    /**
     * Gets the value of the managementVLAN property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getManagementVLAN() {
        return managementVLAN;
    }

    /**
     * Sets the value of the managementVLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setManagementVLAN(Integer value) {
        this.managementVLAN = value;
    }

    /**
     * Gets the value of the vfiName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVFIName() {
        return vfiName;
    }

    /**
     * Sets the value of the vfiName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVFIName(String value) {
        this.vfiName = value;
    }

    /**
     * Gets the value of the hubPE property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHubPE() {
        return hubPE;
    }

    /**
     * Sets the value of the hubPE property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHubPE(String value) {
        this.hubPE = value;
    }

}
