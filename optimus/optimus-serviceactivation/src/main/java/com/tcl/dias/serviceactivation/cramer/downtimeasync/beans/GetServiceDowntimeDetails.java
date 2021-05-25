
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
 *         &lt;element name="ServiceDowntimeDtls" type="{http://ACE_ServiceDowntime_Module}ServiceDownDtls"/&gt;
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
    "serviceDowntimeDtls"
})
@XmlRootElement(name = "getServiceDowntimeDetails", namespace = "http://ACE_ServiceDowntime_Module/GetServiceDowntimeDetails")
public class GetServiceDowntimeDetails {

    @XmlElement(name = "ServiceDowntimeDtls", required = true, nillable = true)
    protected ServiceDownDtls serviceDowntimeDtls;

    /**
     * Gets the value of the serviceDowntimeDtls property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceDownDtls }
     *     
     */
    public ServiceDownDtls getServiceDowntimeDtls() {
        return serviceDowntimeDtls;
    }

    /**
     * Sets the value of the serviceDowntimeDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDownDtls }
     *     
     */
    public void setServiceDowntimeDtls(ServiceDownDtls value) {
        this.serviceDowntimeDtls = value;
    }

}
