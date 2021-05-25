
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="request" type="{http://www.tcl.com/2011/11/netordsvc/xsd}EORequest" minOccurs="0"/>
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderType" type="{http://www.tcl.com/2011/11/netordsvc/xsd}OrderType" minOccurs="0"/>
 *         &lt;element name="orderCategory" type="{http://www.tcl.com/2011/11/netordsvc/xsd}OrderCategory" minOccurs="0"/>
 *         &lt;element name="AddNodeMSB" type="{http://www.tcl.com/2011/11/netordsvc/xsd}AddNodeMSB" minOccurs="0"/>
 *         &lt;element name="isSDHConfigurable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isMPLSTPConfigurable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderInfo", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "request",
    "orderId",
    "orderType",
    "orderCategory",
    "addNodeMSB",
    "isSDHConfigurable",
    "isMPLSTPConfigurable"
})
public class OrderInfo {

    protected EORequest request;
    protected String orderId;
    @XmlSchemaType(name = "string")
    protected OrderType orderType;
    @XmlSchemaType(name = "string")
    protected OrderCategory2 orderCategory;
    @XmlElement(name = "AddNodeMSB")
    protected AddNodeMSB addNodeMSB;
    protected Boolean isSDHConfigurable;
    protected Boolean isMPLSTPConfigurable;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link EORequest }
     *     
     */
    public EORequest getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link EORequest }
     *     
     */
    public void setRequest(EORequest value) {
        this.request = value;
    }

    /**
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the orderType property.
     * 
     * @return
     *     possible object is
     *     {@link OrderType }
     *     
     */
    public OrderType getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderType }
     *     
     */
    public void setOrderType(OrderType value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the orderCategory property.
     * 
     * @return
     *     possible object is
     *     {@link OrderCategory2 }
     *     
     */
    public OrderCategory2 getOrderCategory() {
        return orderCategory;
    }

    /**
     * Sets the value of the orderCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderCategory2 }
     *     
     */
    public void setOrderCategory(OrderCategory2 value) {
        this.orderCategory = value;
    }

    /**
     * Gets the value of the addNodeMSB property.
     * 
     * @return
     *     possible object is
     *     {@link AddNodeMSB }
     *     
     */
    public AddNodeMSB getAddNodeMSB() {
        return addNodeMSB;
    }

    /**
     * Sets the value of the addNodeMSB property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddNodeMSB }
     *     
     */
    public void setAddNodeMSB(AddNodeMSB value) {
        this.addNodeMSB = value;
    }

    /**
     * Gets the value of the isSDHConfigurable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsSDHConfigurable() {
        return isSDHConfigurable;
    }

    /**
     * Sets the value of the isSDHConfigurable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsSDHConfigurable(Boolean value) {
        this.isSDHConfigurable = value;
    }

    /**
     * Gets the value of the isMPLSTPConfigurable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMPLSTPConfigurable() {
        return isMPLSTPConfigurable;
    }

    /**
     * Sets the value of the isMPLSTPConfigurable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMPLSTPConfigurable(Boolean value) {
        this.isMPLSTPConfigurable = value;
    }

}
