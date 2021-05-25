
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PartnerDevice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PartnerDevice">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ActivationCommonLibrary/ace/common/bo/_20113_01}Device">
 *       &lt;sequence>
 *         &lt;element name="PartnerdeviceWANIPv4address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="PartnerdeviceWANIPv6address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PartnerDevice", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_02", propOrder = {
    "partnerdeviceWANIPv4Address",
    "partnerdeviceWANIPv6Address"
})
public class PartnerDevice
    extends Device
{

    @XmlElement(name = "PartnerdeviceWANIPv4address")
    protected IPV4Address partnerdeviceWANIPv4Address;
    @XmlElement(name = "PartnerdeviceWANIPv6address")
    protected IPV6Address partnerdeviceWANIPv6Address;

    /**
     * Gets the value of the partnerdeviceWANIPv4Address property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getPartnerdeviceWANIPv4Address() {
        return partnerdeviceWANIPv4Address;
    }

    /**
     * Sets the value of the partnerdeviceWANIPv4Address property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setPartnerdeviceWANIPv4Address(IPV4Address value) {
        this.partnerdeviceWANIPv4Address = value;
    }

    /**
     * Gets the value of the partnerdeviceWANIPv6Address property.
     * 
     * @return
     *     possible object is
     *     {@link IPV6Address }
     *     
     */
    public IPV6Address getPartnerdeviceWANIPv6Address() {
        return partnerdeviceWANIPv6Address;
    }

    /**
     * Sets the value of the partnerdeviceWANIPv6Address property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV6Address }
     *     
     */
    public void setPartnerdeviceWANIPv6Address(IPV6Address value) {
        this.partnerdeviceWANIPv6Address = value;
    }

}
