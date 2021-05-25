
package com.tcl.dias.serviceactivation.activation.netp.beans;

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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orderDetails" type="{http://www.tcl.com/2011/11/netordsvc/xsd}OrderInfo"/>
 *         &lt;element name="header" type="{http://www.tcl.com/2011/11/ace/common/xsd}ACEHeader"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orderDetails",
    "header"
})
@XmlRootElement(name = "performNetworkOrderRequest", namespace = "http://NetPServicesLibrary/netpsvc/wsdl/v1/NetPRequest")
public class PerformNetworkOrderRequest {

    @XmlElement(required = true, nillable = true)
    protected OrderInfo orderDetails;
    @XmlElement(required = true, nillable = true)
    protected ACEHeader header;

    /**
     * Gets the value of the orderDetails property.
     * 
     * @return
     *     possible object is
     *     {@link OrderInfo }
     *     
     */
    public OrderInfo getOrderDetails() {
        return orderDetails;
    }

    /**
     * Sets the value of the orderDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderInfo }
     *     
     */
    public void setOrderDetails(OrderInfo value) {
        this.orderDetails = value;
    }

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link ACEHeader }
     *     
     */
    public ACEHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link ACEHeader }
     *     
     */
    public void setHeader(ACEHeader value) {
        this.header = value;
    }

}
