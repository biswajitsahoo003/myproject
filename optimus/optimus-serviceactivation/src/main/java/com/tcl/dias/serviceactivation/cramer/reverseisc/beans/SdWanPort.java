
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sdWanPort complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sdWanPort"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SDWAN_on_Different_underlay_IZO_IW_MPLS_Use_Case_4" type="{http://com.tatacommunications.cramer.reverseisc.ws}SDWAN_on_Different_underlay_IZO_IW_MPLS_Use_Case_4" minOccurs="0"/&gt;
 *         &lt;element name="LAN_VR_Internet_WAN_VR_Connection_Use_case_3" type="{http://com.tatacommunications.cramer.reverseisc.ws}LAN_VR_Internet_WAN_VR_Connection_Use_case_3" minOccurs="0"/&gt;
 *         &lt;element name="Logical_Port_Details" type="{http://com.tatacommunications.cramer.reverseisc.ws}Logical_Port_Details" minOccurs="0"/&gt;
 *         &lt;element name="Physical_Port_Details" type="{http://com.tatacommunications.cramer.reverseisc.ws}Phyiscal_Port_Details" minOccurs="0"/&gt;
 *         &lt;element name="IZO_Private_Public_Cloud_Connection_Use_case_IZO_Public_1b" type="{http://com.tatacommunications.cramer.reverseisc.ws}IZO_Private_Public_Cloud_Connection_Use_case_IZO_Public_1b" minOccurs="0"/&gt;
 *         &lt;element name="Routing_Protocol" type="{http://com.tatacommunications.cramer.reverseisc.ws}Routing_Protocol" minOccurs="0"/&gt;
 *         &lt;element name="useCaseName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Vlan_Details" type="{http://com.tatacommunications.cramer.reverseisc.ws}VLAN_NUMBER" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdWanPort", propOrder = {
    "sdwanOnDifferentUnderlayIZOIWMPLSUseCase4",
    "lanvrInternetWANVRConnectionUseCase3",
    "logicalPortDetails",
    "physicalPortDetails",
    "izoPrivatePublicCloudConnectionUseCaseIZOPublic1B",
    "routingProtocol",
    "useCaseName",
    "vlanDetails"
})
public class SdWanPort {

    @XmlElement(name = "SDWAN_on_Different_underlay_IZO_IW_MPLS_Use_Case_4")
    protected SDWANOnDifferentUnderlayIZOIWMPLSUseCase4 sdwanOnDifferentUnderlayIZOIWMPLSUseCase4;
    @XmlElement(name = "LAN_VR_Internet_WAN_VR_Connection_Use_case_3")
    protected LANVRInternetWANVRConnectionUseCase3 lanvrInternetWANVRConnectionUseCase3;
    @XmlElement(name = "Logical_Port_Details")
    protected LogicalPortDetails logicalPortDetails;
    @XmlElement(name = "Physical_Port_Details")
    protected PhyiscalPortDetails physicalPortDetails;
    @XmlElement(name = "IZO_Private_Public_Cloud_Connection_Use_case_IZO_Public_1b")
    protected IZOPrivatePublicCloudConnectionUseCaseIZOPublic1B izoPrivatePublicCloudConnectionUseCaseIZOPublic1B;
    @XmlElement(name = "Routing_Protocol")
    protected RoutingProtocol routingProtocol;
    protected String useCaseName;
    @XmlElement(name = "Vlan_Details")
    protected VLANNUMBER vlanDetails;

    /**
     * Gets the value of the sdwanOnDifferentUnderlayIZOIWMPLSUseCase4 property.
     * 
     * @return
     *     possible object is
     *     {@link SDWANOnDifferentUnderlayIZOIWMPLSUseCase4 }
     *     
     */
    public SDWANOnDifferentUnderlayIZOIWMPLSUseCase4 getSDWANOnDifferentUnderlayIZOIWMPLSUseCase4() {
        return sdwanOnDifferentUnderlayIZOIWMPLSUseCase4;
    }

    /**
     * Sets the value of the sdwanOnDifferentUnderlayIZOIWMPLSUseCase4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link SDWANOnDifferentUnderlayIZOIWMPLSUseCase4 }
     *     
     */
    public void setSDWANOnDifferentUnderlayIZOIWMPLSUseCase4(SDWANOnDifferentUnderlayIZOIWMPLSUseCase4 value) {
        this.sdwanOnDifferentUnderlayIZOIWMPLSUseCase4 = value;
    }

    /**
     * Gets the value of the lanvrInternetWANVRConnectionUseCase3 property.
     * 
     * @return
     *     possible object is
     *     {@link LANVRInternetWANVRConnectionUseCase3 }
     *     
     */
    public LANVRInternetWANVRConnectionUseCase3 getLANVRInternetWANVRConnectionUseCase3() {
        return lanvrInternetWANVRConnectionUseCase3;
    }

    /**
     * Sets the value of the lanvrInternetWANVRConnectionUseCase3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link LANVRInternetWANVRConnectionUseCase3 }
     *     
     */
    public void setLANVRInternetWANVRConnectionUseCase3(LANVRInternetWANVRConnectionUseCase3 value) {
        this.lanvrInternetWANVRConnectionUseCase3 = value;
    }

    /**
     * Gets the value of the logicalPortDetails property.
     * 
     * @return
     *     possible object is
     *     {@link LogicalPortDetails }
     *     
     */
    public LogicalPortDetails getLogicalPortDetails() {
        return logicalPortDetails;
    }

    /**
     * Sets the value of the logicalPortDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link LogicalPortDetails }
     *     
     */
    public void setLogicalPortDetails(LogicalPortDetails value) {
        this.logicalPortDetails = value;
    }

    /**
     * Gets the value of the physicalPortDetails property.
     * 
     * @return
     *     possible object is
     *     {@link PhyiscalPortDetails }
     *     
     */
    public PhyiscalPortDetails getPhysicalPortDetails() {
        return physicalPortDetails;
    }

    /**
     * Sets the value of the physicalPortDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhyiscalPortDetails }
     *     
     */
    public void setPhysicalPortDetails(PhyiscalPortDetails value) {
        this.physicalPortDetails = value;
    }

    /**
     * Gets the value of the izoPrivatePublicCloudConnectionUseCaseIZOPublic1B property.
     * 
     * @return
     *     possible object is
     *     {@link IZOPrivatePublicCloudConnectionUseCaseIZOPublic1B }
     *     
     */
    public IZOPrivatePublicCloudConnectionUseCaseIZOPublic1B getIZOPrivatePublicCloudConnectionUseCaseIZOPublic1B() {
        return izoPrivatePublicCloudConnectionUseCaseIZOPublic1B;
    }

    /**
     * Sets the value of the izoPrivatePublicCloudConnectionUseCaseIZOPublic1B property.
     * 
     * @param value
     *     allowed object is
     *     {@link IZOPrivatePublicCloudConnectionUseCaseIZOPublic1B }
     *     
     */
    public void setIZOPrivatePublicCloudConnectionUseCaseIZOPublic1B(IZOPrivatePublicCloudConnectionUseCaseIZOPublic1B value) {
        this.izoPrivatePublicCloudConnectionUseCaseIZOPublic1B = value;
    }

    /**
     * Gets the value of the routingProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingProtocol }
     *     
     */
    public RoutingProtocol getRoutingProtocol() {
        return routingProtocol;
    }

    /**
     * Sets the value of the routingProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingProtocol }
     *     
     */
    public void setRoutingProtocol(RoutingProtocol value) {
        this.routingProtocol = value;
    }

    /**
     * Gets the value of the useCaseName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUseCaseName() {
        return useCaseName;
    }

    /**
     * Sets the value of the useCaseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUseCaseName(String value) {
        this.useCaseName = value;
    }

    /**
     * Gets the value of the vlanDetails property.
     * 
     * @return
     *     possible object is
     *     {@link VLANNUMBER }
     *     
     */
    public VLANNUMBER getVlanDetails() {
        return vlanDetails;
    }

    /**
     * Sets the value of the vlanDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link VLANNUMBER }
     *     
     */
    public void setVlanDetails(VLANNUMBER value) {
        this.vlanDetails = value;
    }

}
