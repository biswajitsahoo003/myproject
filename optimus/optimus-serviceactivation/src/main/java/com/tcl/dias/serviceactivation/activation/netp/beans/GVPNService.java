
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GVPNService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GVPNService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}IPService">
 *       &lt;sequence>
 *         &lt;element name="CurrentCustomerSiteId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vrf" type="{http://www.tcl.com/2011/11/ipsvc/xsd}VirtualRouteForwardingServiceInstance" minOccurs="0"/>
 *         &lt;element name="isVRFLiteEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isProactiveMonitoringEnabled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CurrentLegID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pingV4IPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="pingV6IPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GVPNService", propOrder = {
    "currentCustomerSiteId",
    "vrf",
    "isVRFLiteEnabled",
    "isProactiveMonitoringEnabled",
    "currentLegID",
    "pingV4IPAddress",
    "pingV6IPAddress",
    "nmsServerv4IPAddress"
})
@XmlSeeAlso({
    IPSECService.class,
    DMVPNService.class,
    RoadWarriorService.class,
    P2PL2VPNService.class
})
public class GVPNService
    extends IPService
{

    @XmlElement(name = "CurrentCustomerSiteId")
    protected String currentCustomerSiteId;
    protected VirtualRouteForwardingServiceInstance vrf;
    protected Boolean isVRFLiteEnabled;
    protected String isProactiveMonitoringEnabled;
    @XmlElement(name = "CurrentLegID")
    protected String currentLegID;
    protected IPV4Address pingV4IPAddress;
    protected IPV6Address pingV6IPAddress;
    @XmlElement(name = "NMSServerv4IPAddress")
    protected IPV4Address nmsServerv4IPAddress;

    /**
     * Gets the value of the currentCustomerSiteId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentCustomerSiteId() {
        return currentCustomerSiteId;
    }

    /**
     * Sets the value of the currentCustomerSiteId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentCustomerSiteId(String value) {
        this.currentCustomerSiteId = value;
    }

    /**
     * Gets the value of the vrf property.
     * 
     * @return
     *     possible object is
     *     {@link VirtualRouteForwardingServiceInstance }
     *     
     */
    public VirtualRouteForwardingServiceInstance getVrf() {
        return vrf;
    }

    /**
     * Sets the value of the vrf property.
     * 
     * @param value
     *     allowed object is
     *     {@link VirtualRouteForwardingServiceInstance }
     *     
     */
    public void setVrf(VirtualRouteForwardingServiceInstance value) {
        this.vrf = value;
    }

    /**
     * Gets the value of the isVRFLiteEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVRFLiteEnabled() {
        return isVRFLiteEnabled;
    }

    /**
     * Sets the value of the isVRFLiteEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVRFLiteEnabled(Boolean value) {
        this.isVRFLiteEnabled = value;
    }

    /**
     * Gets the value of the isProactiveMonitoringEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsProactiveMonitoringEnabled() {
        return isProactiveMonitoringEnabled;
    }

    /**
     * Sets the value of the isProactiveMonitoringEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsProactiveMonitoringEnabled(String value) {
        this.isProactiveMonitoringEnabled = value;
    }

    /**
     * Gets the value of the currentLegID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentLegID() {
        return currentLegID;
    }

    /**
     * Sets the value of the currentLegID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentLegID(String value) {
        this.currentLegID = value;
    }

    /**
     * Gets the value of the pingV4IPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getPingV4IPAddress() {
        return pingV4IPAddress;
    }

    /**
     * Sets the value of the pingV4IPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setPingV4IPAddress(IPV4Address value) {
        this.pingV4IPAddress = value;
    }

    /**
     * Gets the value of the pingV6IPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV6Address }
     *     
     */
    public IPV6Address getPingV6IPAddress() {
        return pingV6IPAddress;
    }

    /**
     * Sets the value of the pingV6IPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV6Address }
     *     
     */
    public void setPingV6IPAddress(IPV6Address value) {
        this.pingV6IPAddress = value;
    }

	public IPV4Address getNmsServerv4IPAddress() {
		return nmsServerv4IPAddress;
	}

	public void setNmsServerv4IPAddress(IPV4Address nmsServerv4IPAddress) {
		this.nmsServerv4IPAddress = nmsServerv4IPAddress;
	}


}
