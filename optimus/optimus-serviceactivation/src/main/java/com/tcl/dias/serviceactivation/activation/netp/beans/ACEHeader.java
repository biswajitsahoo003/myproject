
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ACEHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ACEHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actionRequired" type="{http://www.tcl.com/2011/11/ace/common/xsd}ActionRequired" maxOccurs="unbounded" form="unqualified"/>
 *         &lt;element name="requestID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="originatingSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="originationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0" form="unqualified"/>
 *         &lt;element name="authUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ACEHeader", namespace = "http://www.tcl.com/2011/11/ace/common/xsd", propOrder = {
    "actionRequired",
    "requestID",
    "originatingSystem",
    "originationTime",
    "authUser"
})
public class ACEHeader {

    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected List<ActionRequired> actionRequired;
    protected String requestID;
    protected String originatingSystem;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar originationTime;
    protected String authUser;

    /**
     * Gets the value of the actionRequired property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actionRequired property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActionRequired().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActionRequired }
     * 
     * 
     */
    public List<ActionRequired> getActionRequired() {
        if (actionRequired == null) {
            actionRequired = new ArrayList<ActionRequired>();
        }
        return this.actionRequired;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the originatingSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginatingSystem() {
        return originatingSystem;
    }

    /**
     * Sets the value of the originatingSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginatingSystem(String value) {
        this.originatingSystem = value;
    }

    /**
     * Gets the value of the originationTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getOriginationTime() {
        return originationTime;
    }

    /**
     * Sets the value of the originationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setOriginationTime(XMLGregorianCalendar value) {
        this.originationTime = value;
    }

    /**
     * Gets the value of the authUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthUser() {
        return authUser;
    }

    /**
     * Sets the value of the authUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthUser(String value) {
        this.authUser = value;
    }

}
