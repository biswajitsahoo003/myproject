//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.30 at 06:34:20 PM IST 
//


package com.tcl.dias.oms.cisco.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * In the core part of
 *                         ebMS-3 specification, an eb:Signal Message is
 *                         allowed to
 *                         contain
 *                         eb:MessageInfo and at most one Receipt Signal, at most one
 *                         eb:PullRequest
 *                         element, and/or a series of eb:Error elements. In
 *                         part 2 of the ebMS-3
 *                         specification, new signals may be introduced,
 *                         and for this reason,
 *                         an extensibility point is added here to the
 *                         eb:SignalMessage
 *                         element to
 *                         allow it to contain any elements.
 *                     
 * 
 * <p>Java class for SignalMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SignalMessage"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MessageInfo" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}MessageInfo"/&gt;
 *         &lt;element name="PullRequest" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}PullRequest" minOccurs="0"/&gt;
 *         &lt;element name="Receipt" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}Receipt" minOccurs="0"/&gt;
 *         &lt;element name="Error" type="{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}Error" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignalMessage", namespace = "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/", propOrder = {
    "messageInfo",
    "pullRequest",
    "receipt",
    "error",
    "any"
})
public class SignalMessage {

    @XmlElement(name = "MessageInfo", required = true)
    protected MessageInfo messageInfo;
    @XmlElement(name = "PullRequest")
    protected PullRequest pullRequest;
    @XmlElement(name = "Receipt")
    protected Receipt receipt;
    @XmlElement(name = "Error")
    protected List<Error> error;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the messageInfo property.
     * 
     * @return
     *     possible object is
     *     {@link MessageInfo }
     *     
     */
    public MessageInfo getMessageInfo() {
        return messageInfo;
    }

    /**
     * Sets the value of the messageInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageInfo }
     *     
     */
    public void setMessageInfo(MessageInfo value) {
        this.messageInfo = value;
    }

    /**
     * Gets the value of the pullRequest property.
     * 
     * @return
     *     possible object is
     *     {@link PullRequest }
     *     
     */
    public PullRequest getPullRequest() {
        return pullRequest;
    }

    /**
     * Sets the value of the pullRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link PullRequest }
     *     
     */
    public void setPullRequest(PullRequest value) {
        this.pullRequest = value;
    }

    /**
     * Gets the value of the receipt property.
     * 
     * @return
     *     possible object is
     *     {@link Receipt }
     *     
     */
    public Receipt getReceipt() {
        return receipt;
    }

    /**
     * Sets the value of the receipt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Receipt }
     *     
     */
    public void setReceipt(Receipt value) {
        this.receipt = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the error property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getError().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Error }
     * 
     * 
     */
    public List<Error> getError() {
        if (error == null) {
            error = new ArrayList<Error>();
        }
        return this.error;
    }

    /**
     * Gets the value of the any property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
