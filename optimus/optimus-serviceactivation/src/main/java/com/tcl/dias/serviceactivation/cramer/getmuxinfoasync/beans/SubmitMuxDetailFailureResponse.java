
package com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for submitMuxDetailFailureResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="submitMuxDetailFailureResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ServiceId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RequestId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="failure" type="{urn:com.tcl.ace.tdm.getmux.ws.GetMuxConsumer}muxDetailFailureResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitMuxDetailFailureResponse", propOrder = {
    "serviceId",
    "requestId",
    "failure"
})
@XmlRootElement(name = "submitMuxDetailFailureResponse")
public class SubmitMuxDetailFailureResponse {

    @XmlElement(name = "ServiceId", required = true)
    protected String serviceId;
    @XmlElement(name = "RequestId", required = true)
    protected String requestId;
    protected MuxDetailFailureResponse failure;

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
     * Gets the value of the failure property.
     * 
     * @return
     *     possible object is
     *     {@link MuxDetailFailureResponse }
     *     
     */
    public MuxDetailFailureResponse getFailure() {
        return failure;
    }

    /**
     * Sets the value of the failure property.
     * 
     * @param value
     *     allowed object is
     *     {@link MuxDetailFailureResponse }
     *     
     */
    public void setFailure(MuxDetailFailureResponse value) {
        this.failure = value;
    }

}
