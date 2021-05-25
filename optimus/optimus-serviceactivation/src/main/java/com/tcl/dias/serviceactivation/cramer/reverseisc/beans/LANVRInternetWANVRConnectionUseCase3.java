
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LAN_VR_Internet_WAN_VR_Connection_Use_case_3 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LAN_VR_Internet_WAN_VR_Connection_Use_case_3"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="INTERNET_WAN_VR_CONNECTION" type="{http://com.tatacommunications.cramer.reverseisc.ws}internetWANVRConnectionIpNumberVo" minOccurs="0"/&gt;
 *         &lt;element name="WAN_IP" type="{http://com.tatacommunications.cramer.reverseisc.ws}wanIPUseCase3" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LAN_VR_Internet_WAN_VR_Connection_Use_case_3", propOrder = {
    "internetwanvrconnection",
    "wanip"
})
public class LANVRInternetWANVRConnectionUseCase3 {

    @XmlElement(name = "INTERNET_WAN_VR_CONNECTION")
    protected InternetWANVRConnectionIpNumberVo internetwanvrconnection;
    @XmlElement(name = "WAN_IP")
    protected WanIPUseCase3 wanip;

    /**
     * Gets the value of the internetwanvrconnection property.
     * 
     * @return
     *     possible object is
     *     {@link InternetWANVRConnectionIpNumberVo }
     *     
     */
    public InternetWANVRConnectionIpNumberVo getINTERNETWANVRCONNECTION() {
        return internetwanvrconnection;
    }

    /**
     * Sets the value of the internetwanvrconnection property.
     * 
     * @param value
     *     allowed object is
     *     {@link InternetWANVRConnectionIpNumberVo }
     *     
     */
    public void setINTERNETWANVRCONNECTION(InternetWANVRConnectionIpNumberVo value) {
        this.internetwanvrconnection = value;
    }

    /**
     * Gets the value of the wanip property.
     * 
     * @return
     *     possible object is
     *     {@link WanIPUseCase3 }
     *     
     */
    public WanIPUseCase3 getWANIP() {
        return wanip;
    }

    /**
     * Sets the value of the wanip property.
     * 
     * @param value
     *     allowed object is
     *     {@link WanIPUseCase3 }
     *     
     */
    public void setWANIP(WanIPUseCase3 value) {
        this.wanip = value;
    }

}
