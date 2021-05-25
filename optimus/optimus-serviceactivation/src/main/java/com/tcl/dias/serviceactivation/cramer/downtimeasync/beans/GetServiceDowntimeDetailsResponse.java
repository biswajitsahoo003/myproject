
package com.tcl.dias.serviceactivation.cramer.downtimeasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ServiceDowntimeDtlsResponse" type="{http://ACE_ServiceDowntime_Module}ServiceDownDtlsResponse"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "serviceDowntimeDtlsResponse"
})
@XmlRootElement(name = "getServiceDowntimeDetailsResponse", namespace = "http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails")
public class GetServiceDowntimeDetailsResponse {

    @XmlElement(name = "ServiceDowntimeDtlsResponse", required = true, nillable = true)
    protected ServiceDownDtlsResponse serviceDowntimeDtlsResponse;

    /**
     * Gets the value of the serviceDowntimeDtlsResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDownDtlsResponse }
     *     
     */
    public ServiceDownDtlsResponse getServiceDowntimeDtlsResponse() {
        return serviceDowntimeDtlsResponse;
    }

    /**
     * Sets the value of the serviceDowntimeDtlsResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDownDtlsResponse }
     *     
     */
    public void setServiceDowntimeDtlsResponse(ServiceDownDtlsResponse value) {
        this.serviceDowntimeDtlsResponse = value;
    }

}
