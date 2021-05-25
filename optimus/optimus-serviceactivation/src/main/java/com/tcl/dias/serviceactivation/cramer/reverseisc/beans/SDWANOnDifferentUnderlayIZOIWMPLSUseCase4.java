
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SDWAN_on_Different_underlay_IZO_IW_MPLS_Use_Case_4 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SDWAN_on_Different_underlay_IZO_IW_MPLS_Use_Case_4"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LAN_VR_DISTRIBUTION_LAN_VR_CONNECTION" type="{http://com.tatacommunications.cramer.reverseisc.ws}lanVRConnectionDifferentUnderlayVo" minOccurs="0"/&gt;
 *         &lt;element name="WAN_IP" type="{http://com.tatacommunications.cramer.reverseisc.ws}wanIPUseCase4" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SDWAN_on_Different_underlay_IZO_IW_MPLS_Use_Case_4", propOrder = {
    "lanvrdistributionlanvrconnection",
    "wanip"
})
public class SDWANOnDifferentUnderlayIZOIWMPLSUseCase4 {

    @XmlElement(name = "LAN_VR_DISTRIBUTION_LAN_VR_CONNECTION")
    protected LanVRConnectionDifferentUnderlayVo lanvrdistributionlanvrconnection;
    @XmlElement(name = "WAN_IP")
    protected WanIPUseCase4 wanip;

    /**
     * Gets the value of the lanvrdistributionlanvrconnection property.
     * 
     * @return
     *     possible object is
     *     {@link LanVRConnectionDifferentUnderlayVo }
     *     
     */
    public LanVRConnectionDifferentUnderlayVo getLANVRDISTRIBUTIONLANVRCONNECTION() {
        return lanvrdistributionlanvrconnection;
    }

    /**
     * Sets the value of the lanvrdistributionlanvrconnection property.
     * 
     * @param value
     *     allowed object is
     *     {@link LanVRConnectionDifferentUnderlayVo }
     *     
     */
    public void setLANVRDISTRIBUTIONLANVRCONNECTION(LanVRConnectionDifferentUnderlayVo value) {
        this.lanvrdistributionlanvrconnection = value;
    }

    /**
     * Gets the value of the wanip property.
     * 
     * @return
     *     possible object is
     *     {@link WanIPUseCase4 }
     *     
     */
    public WanIPUseCase4 getWANIP() {
        return wanip;
    }

    /**
     * Sets the value of the wanip property.
     * 
     * @param value
     *     allowed object is
     *     {@link WanIPUseCase4 }
     *     
     */
    public void setWANIP(WanIPUseCase4 value) {
        this.wanip = value;
    }

}
