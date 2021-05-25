
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
 *         &lt;element name="ticketNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="response" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="errorMsg" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "ticketNo",
    "response",
    "errorMsg"
})
@XmlRootElement(name = "Create_Single_TicketResponse", namespace = "http://CESV/CES_NGP_Single_Ticket.tws")
public class CreateSingleTicketResponse {

    @XmlElement(namespace = "http://CESV/CES_NGP_Single_Ticket.tws", required = true)
    protected String ticketNo;
    @XmlElement(namespace = "http://CESV/CES_NGP_Single_Ticket.tws", required = true)
    protected String response;
    @XmlElement(namespace = "http://CESV/CES_NGP_Single_Ticket.tws", required = true)
    protected String errorMsg;

    /**
     * Gets the value of the ticketNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketNo() {
        return ticketNo;
    }

    /**
     * Sets the value of the ticketNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketNo(String value) {
        this.ticketNo = value;
    }

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

}
