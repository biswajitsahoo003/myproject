
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
 *         &lt;element name="inputDetails" type="{http://CESV/CES_NGP_Single_Ticket.tws}NGP_CES_Single_Ticket_Details"/&gt;
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
    "inputDetails"
})
@XmlRootElement(name = "Create_Single_Ticket", namespace = "http://CESV/CES_NGP_Single_Ticket.tws")
public class CreateSingleTicket {

    @XmlElement(namespace = "http://CESV/CES_NGP_Single_Ticket.tws", required = true)
    protected NGPCESSingleTicketDetails inputDetails;

    /**
     * Gets the value of the inputDetails property.
     * 
     * @return
     *     possible object is
     *     {@link NGPCESSingleTicketDetails }
     *     
     */
    public NGPCESSingleTicketDetails getInputDetails() {
        return inputDetails;
    }

    /**
     * Sets the value of the inputDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link NGPCESSingleTicketDetails }
     *     
     */
    public void setInputDetails(NGPCESSingleTicketDetails value) {
        this.inputDetails = value;
    }

}
