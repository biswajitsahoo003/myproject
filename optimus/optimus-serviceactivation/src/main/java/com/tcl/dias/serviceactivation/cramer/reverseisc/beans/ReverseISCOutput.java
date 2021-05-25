package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reverseISCOutput complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reverseISCOutput"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SDWanAttributes" type="{http://com.tatacommunications.cramer.reverseisc.ws}sdWanPort" minOccurs="0"/&gt;
 *         &lt;element name="LMAttributes" type="{http://com.tatacommunications.cramer.reverseisc.ws}lmAttributes" minOccurs="0"/&gt;
 *         &lt;element name="NNIAttributes" type="{http://com.tatacommunications.cramer.reverseisc.ws}nniAttributes" minOccurs="0"/&gt;
 *         &lt;element name="NNI-Protected-Port" type="{http://com.tatacommunications.cramer.reverseisc.ws}nniProtectedPort" minOccurs="0"/&gt;
 *         &lt;element name="RequestId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResponseHeader" type="{http://com.tatacommunications.cramer.reverseisc.ws}responseHeader" minOccurs="0"/&gt;
 *         &lt;element name="RoadWarriorAttributes" type="{http://com.tatacommunications.cramer.reverseisc.ws}rwAttributes" minOccurs="0"/&gt;
 *         &lt;element name="ServiceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VPN-ILLPortAttributes" type="{http://com.tatacommunications.cramer.reverseisc.ws}vpnIllPortAttributes" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reverseISCOutput", propOrder = {
    "sdWanAttributes",
    "lmAttributes",
    "nniAttributes",
    "nniProtectedPort",
    "requestId",
    "responseHeader",
    "roadWarriorAttributes",
    "serviceId",
    "vpnIllPortAttributes"
})
public class ReverseISCOutput {

    @XmlElement(name = "SDWanAttributes")
    protected SdWanPort sdWanAttributes;
    @XmlElement(name = "LMAttributes")
    protected LmAttributes lmAttributes;
    @XmlElement(name = "NNIAttributes")
    protected NniAttributes nniAttributes;
    @XmlElement(name = "NNI-Protected-Port")
    protected NniProtectedPort nniProtectedPort;
    @XmlElement(name = "RequestId")
    protected String requestId;
    @XmlElement(name = "ResponseHeader")
    protected ResponseHeader responseHeader;
    @XmlElement(name = "RoadWarriorAttributes")
    protected RwAttributes roadWarriorAttributes;
    @XmlElement(name = "ServiceId")
    protected String serviceId;
    @XmlElement(name = "VPN-ILLPortAttributes")
    protected VpnIllPortAttributes vpnIllPortAttributes;
	public LmAttributes getLmAttributes() {
		return lmAttributes;
	}
	public void setLmAttributes(LmAttributes lmAttributes) {
		this.lmAttributes = lmAttributes;
	}
	public NniAttributes getNniAttributes() {
		return nniAttributes;
	}
	public void setNniAttributes(NniAttributes nniAttributes) {
		this.nniAttributes = nniAttributes;
	}
	public NniProtectedPort getNniProtectedPort() {
		return nniProtectedPort;
	}
	public void setNniProtectedPort(NniProtectedPort nniProtectedPort) {
		this.nniProtectedPort = nniProtectedPort;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public ResponseHeader getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}
	public RwAttributes getRoadWarriorAttributes() {
		return roadWarriorAttributes;
	}
	public void setRoadWarriorAttributes(RwAttributes roadWarriorAttributes) {
		this.roadWarriorAttributes = roadWarriorAttributes;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public VpnIllPortAttributes getVpnIllPortAttributes() {
		return vpnIllPortAttributes;
	}
	public void setVpnIllPortAttributes(VpnIllPortAttributes vpnIllPortAttributes) {
		this.vpnIllPortAttributes = vpnIllPortAttributes;
	}

    /**
     * Gets the value of the sdWanAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link SdWanPort }
     *     
     */
    public SdWanPort getSDWanAttributes() {
        return sdWanAttributes;
    }

    /**
     * Sets the value of the sdWanAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link SdWanPort }
     *     
     */
    public void setSDWanAttributes(SdWanPort value) {
        this.sdWanAttributes = value;
    }

    

  

}
