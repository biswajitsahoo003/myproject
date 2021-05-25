
package com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getMuxInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getMuxInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ServiceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RequestId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AEndMuxDetails" type="{urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer}muxDetail" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="ZEndMuxDetails" type="{urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer}muxDetail" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMuxInfo", propOrder = {
    "serviceId",
    "requestId",
    "aEndMuxDetails",
    "zEndMuxDetails"
})
@XmlRootElement(name = "getMuxInfo")
public class GetMuxInfo {

    @XmlElement(name = "ServiceId", required = true)
    protected String serviceId;
    @XmlElement(name = "RequestId", required = true)
    protected String requestId;
    @XmlElement(name = "AEndMuxDetails")
    protected List<MuxDetail> aEndMuxDetails;
    @XmlElement(name = "ZEndMuxDetails")
    protected List<MuxDetail> zEndMuxDetails;

    /**
     * Gets the value of the serviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the requestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the aEndMuxDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the aEndMuxDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAEndMuxDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MuxDetail }
     * 
     * 
     */
    public List<MuxDetail> getAEndMuxDetails() {
        if (aEndMuxDetails == null) {
            aEndMuxDetails = new ArrayList<MuxDetail>();
        }
        return this.aEndMuxDetails;
    }

    /**
     * Gets the value of the zEndMuxDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the zEndMuxDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getZEndMuxDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MuxDetail }
     * 
     * 
     */
    public List<MuxDetail> getZEndMuxDetails() {
        if (zEndMuxDetails == null) {
            zEndMuxDetails = new ArrayList<MuxDetail>();
        }
        return this.zEndMuxDetails;
    }

}
