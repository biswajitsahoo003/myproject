
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Name that will be assigned for VRF based on rules
 * 				defined by GIMEC team. Currently different vrf names are
 * 				used to identify India based IAServices such as PIA, SIA
 * 				etc. For international IAServices such as DIA / DIL a
 * 				different vrf name is given.
 * 			
 * 
 * <p>Java class for IAService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IAService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}IPService">
 *       &lt;sequence>
 *         &lt;element name="vrf" type="{http://www.tcl.com/2011/11/ipsvc/xsd}VirtualRouteForwardingServiceInstance" minOccurs="0"/>
 *         &lt;element name="InternetGatewayv4IPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="InternetGatewayv6IPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *         &lt;element name="NMSServerv4IPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="isRTBH" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="RTBHOption" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="RTBH"/>
 *               &lt;enumeration value="CRTBH"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IAService", propOrder = {
    "vrf",
    "internetGatewayv4IPAddress",
    "internetGatewayv6IPAddress",
    "nmsServerv4IPAddress",
    "isRTBH",
    "rtbhOption"
})
@XmlSeeAlso({
    IASMVOIPService.class,
    IASGIPVCService.class
})
public class IAService
    extends IPService
{

    protected VirtualRouteForwardingServiceInstance vrf;
    @XmlElement(name = "InternetGatewayv4IPAddress")
    protected IPV4Address internetGatewayv4IPAddress;
    @XmlElement(name = "InternetGatewayv6IPAddress")
    protected IPV6Address internetGatewayv6IPAddress;
    @XmlElement(name = "NMSServerv4IPAddress")
    protected IPV4Address nmsServerv4IPAddress;
    protected Boolean isRTBH;
    @XmlElement(name = "RTBHOption")
    protected String rtbhOption;

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
     * Gets the value of the internetGatewayv4IPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getInternetGatewayv4IPAddress() {
        return internetGatewayv4IPAddress;
    }

    /**
     * Sets the value of the internetGatewayv4IPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setInternetGatewayv4IPAddress(IPV4Address value) {
        this.internetGatewayv4IPAddress = value;
    }

    /**
     * Gets the value of the internetGatewayv6IPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV6Address }
     *     
     */
    public IPV6Address getInternetGatewayv6IPAddress() {
        return internetGatewayv6IPAddress;
    }

    /**
     * Sets the value of the internetGatewayv6IPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV6Address }
     *     
     */
    public void setInternetGatewayv6IPAddress(IPV6Address value) {
        this.internetGatewayv6IPAddress = value;
    }

    /**
     * Gets the value of the nmsServerv4IPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getNMSServerv4IPAddress() {
        return nmsServerv4IPAddress;
    }

    /**
     * Sets the value of the nmsServerv4IPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setNMSServerv4IPAddress(IPV4Address value) {
        this.nmsServerv4IPAddress = value;
    }

    /**
     * Gets the value of the isRTBH property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRTBH() {
        return isRTBH;
    }

    /**
     * Sets the value of the isRTBH property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRTBH(Boolean value) {
        this.isRTBH = value;
    }

    /**
     * Gets the value of the rtbhOption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRTBHOption() {
        return rtbhOption;
    }

    /**
     * Sets the value of the rtbhOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRTBHOption(String value) {
        this.rtbhOption = value;
    }

}
