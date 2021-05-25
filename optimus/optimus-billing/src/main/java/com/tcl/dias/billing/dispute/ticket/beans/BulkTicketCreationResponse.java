
package com.tcl.dias.billing.dispute.ticket.beans;

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
 *         &lt;element name="response" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="errorMsg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="parentTicketNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="childTicketNo" type="{http://CESV/NGP_CES_Bulk_Ticket.tws}ArrayOf_string"/&gt;
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
    "response",
    "errorMsg",
    "parentTicketNo",
    "childTicketNo"
})
@XmlRootElement(name = "bulkTicketCreationResponse")
public class BulkTicketCreationResponse {

    @XmlElement(required = true)
    protected String response;
    @XmlElement(required = true)
    protected String errorMsg;
    @XmlElement(required = true)
    protected String parentTicketNo;
    @XmlElement(required = true)
    protected ArrayOfString2 childTicketNo;

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponse(String value) {
        this.response = value;
    }

    /**
     * Gets the value of the errorMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Sets the value of the errorMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMsg(String value) {
        this.errorMsg = value;
    }

    /**
     * Gets the value of the parentTicketNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentTicketNo() {
        return parentTicketNo;
    }

    /**
     * Sets the value of the parentTicketNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentTicketNo(String value) {
        this.parentTicketNo = value;
    }

    /**
     * Gets the value of the childTicketNo property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString2 }
     *     
     */
    public ArrayOfString2 getChildTicketNo() {
        return childTicketNo;
    }

    /**
     * Sets the value of the childTicketNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString2 }
     *     
     */
    public void setChildTicketNo(ArrayOfString2 value) {
        this.childTicketNo = value;
    }

}
